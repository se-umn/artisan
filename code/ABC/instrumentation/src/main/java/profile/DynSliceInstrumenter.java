package profile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.ArrayType;
import soot.Body;
import soot.IntType;
import soot.Local;
import soot.Modifier;
import soot.PatchingChain;
import soot.PrimType;
import soot.RefLikeType;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.VoidType;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.NewArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.RetStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticFieldRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import dua.Options;
import dua.global.ProgramFlowGraph;
import dua.global.dep.DependenceFinder;
import dua.global.dep.DependenceFinder.ControlDependence;
import dua.global.dep.DependenceFinder.DataDependence;
import dua.global.dep.DependenceFinder.Dependence;
import dua.global.dep.DependenceFinder.NodePoint;
import dua.global.dep.DependenceGraph;
import dua.method.CFG;
import dua.method.CFG.CFGNode;
import dua.method.CFGDefUses.CSArgVar;
import dua.method.CFGDefUses.Def;
import dua.method.CFGDefUses.NodeDefUses;
import dua.method.CFGDefUses.ObjVariable;
import dua.method.CFGDefUses.ReturnVar;
import dua.method.CFGDefUses.StdVariable;
import dua.method.CFGDefUses.Variable;
import dua.method.CallSite;
import dua.method.MethodTag;
import dua.method.ReachableUsesDefs;
import dua.util.Pair;
import dua.util.StringBasedComparator;
import dua.util.Util;
import fault.StmtMapper;

/** Communicates dependencies in (static) forward slices to runtime reporter through "slice.out". */
public class DynSliceInstrumenter {
	private final boolean includeVals;
	private final boolean includeChangeCov;
	private final boolean includeBrCov;
	private final boolean includeDUCov;
	private final int eventLimit;
	
	// vars and deps are assigned ids
	
	/** Holds all dependences to instrument. */
	private List<Dependence> allDeps;
	/** List of all variables defined/killed/used by deps. */
	private List<Variable> allVars = new ArrayList<Variable>();
	/** Set used to check fast whether a var is already in list or not. */
	private Map<Variable,Integer> cacheAllVarsToIds = new HashMap<Variable, Integer>();
	/** Used to avoid instrumenting multiple times for same def/kill (due to multiple deps from def). */
	private Map<NodePoint,List<Integer>> pointToDefOrKillVarIds = new HashMap<NodePoint, List<Integer>>();
	/** Used to avoid instrumenting multiple times for same use (due to multiple deps to use). */
	private Map<NodePoint,List<Integer>> pointToUseVarIds = new HashMap<NodePoint, List<Integer>>();
	/** Used to avoid instrumenting multiple times for same cd-source (due to multiple deps from predicate/cs). */
	private Map<NodePoint,Map<Integer,List<NodePoint>>> pointToBrToControlTgts = new HashMap<NodePoint, Map<Integer, List<NodePoint>>>();
	/** Maps each node to variables to read and report at runtime. Vars are ordered by def # (from defs list).
	 *  For all ctrl deps from pnt (e.g., app mtd calls), there is one extra list at the end. */
	private Map<NodePoint,List<List<Variable>>> pointToVarListsToRead = new HashMap<NodePoint, List<List<Variable>>>();
	/** Keeps track of how many probes have been split for each statement, to make sure a different object-array local for each new split probe. */
	private Map<Stmt,Integer> splitProbes = new HashMap<Stmt,Integer>();
	
	private SootClass clsObject;
	private SootClass clsReporter;
	private SootMethod mReportChange;
	private SootMethod mReportArrDef;
	private SootMethod mReportDef;
	private SootMethod mReportUse;
	/** Starting location of a number of control dependencies. */
	private SootMethod mReportBranch;
	/** Caller-side part of starting location of a number of control dependencies due to multi-target virtual calls. */
	private SootMethod mReportCallSrc;
	/** Callee-side part that completes starting location for a number of control dependencies due to multi-target virtual calls. */
	private SootMethod mReportCallTgt;
	/** Target for any type of control dependence. */
	private SootMethod mReportCDTgt;
	
	// hcai: added for *temporarily working around a bug in this instrumenter: found in PDFBox dyn. slice instrumentation, variables of type 'ByteArrayInputStream'
	// cannot find matches of uses in the var list when invoking getVarMayEqualAndAlias() 
	public static boolean ignoreUnMatchedVars = false;
	
	public DynSliceInstrumenter(boolean includeVals) {
		this(includeVals, false, false, false, -1);
	}
	public DynSliceInstrumenter(boolean includeVals, boolean includeChangeCov, boolean includeBrCov, boolean includeDUCov, int eventLimit) {
		this.includeVals = includeVals;
		this.includeChangeCov = includeChangeCov;
		this.includeBrCov = includeBrCov;
		this.includeDUCov = includeDUCov;
		this.eventLimit = eventLimit;
		
		clsObject = Scene.v().getSootClass("java.lang.Object");
		clsReporter = Scene.v().getSootClass("change.DynSliceReporter");
		// params: nId, pre
		mReportChange = clsReporter.getMethod("void reportChange(int,boolean)");
		// params: nId, pre, frameId, entry, varId, objBase, newarrsize, objVarVals[]
		mReportArrDef = clsReporter.getMethod("void reportArrDef(int,boolean,int,boolean,int,java.lang.Object,int,java.lang.Object[])");
		// params: nId, pre, frameId, entry, varId, interproc, objBase, arrelIdx, objVarVals[]
		mReportDef = clsReporter.getMethod("void reportDef(int,boolean,int,boolean,int,boolean,java.lang.Object,int,java.lang.Object[])");
		// params: nId, pre, frameId, entry, varId, objBase, arrelIdx
		mReportUse = clsReporter.getMethod("void reportUse(int,boolean,int,boolean,int,java.lang.Object,int)");
		// params: nId, pre, brId, frameId, objVarVals[]
		mReportBranch = clsReporter.getMethod("void reportBranch(int,boolean,int,int,java.lang.Object[])"); // brId is index in branches/app-targets list
		// params: nId, frameId, objVarVals[]  --  NOTE: pre is always true; frame is needed to link with predecessor dep-instances
		mReportCallSrc = clsReporter.getMethod("void reportCallSrc(int,int,java.lang.Object[])");
		// params: mtdId, frameId  --  NOTE: mtdId is a unique identifier for the target of a virtual call
		mReportCallTgt= clsReporter.getMethod("void reportCallTgt(int,int)");
		// params: nId, pre, frameId, entry
		mReportCDTgt = clsReporter.getMethod("void reportCDTgt(int,boolean,int,boolean)");
	}
	
	public void instrument(DependenceGraph depGraph) {
		List<DependenceGraph> depGraphs = new ArrayList<DependenceGraph>();
		depGraphs.add(depGraph);
		this.instrument(depGraphs);
	}
	public void instrument(List<DependenceGraph> depGraphs) {
		// graphs can share dependencies, so we collect all dependencies and assign ids to them
		// for all vars used in data/control deps, assign id
		// for all vars in data deps, find all kills and include those in instrumentation
		
		//  for def stmt with no call, take rhs uses before stmt
		//  for id stmt, take val of formal arg after end of id stmts block
		//  for inside-lib call def stmt, take val of used vars before stmt
		//  for lhs def stmt with call (lib or app), take val of defined var after stmt
		//  for control dep, take uses before stmt
		
		System.out.println("START finding all deps" + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
		findAllDeps(depGraphs); // fills allDeps
		System.out.println("END finding all deps" + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
		
		System.out.println("START finding vars and kills " + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
		findDepVarsAndKills(); // fills pointToDefOrKillVarIds, pointToUseVarIds, and pointToVarListsToRead
		System.out.println("END finding vars and kills " + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
		
		// IMPORTANT - order of def/use probes:
		//   1. For id stmts, use of formal param probe should occur *before* def of corresponding local,
		//      so insertBeforeNoRedirect(1st-non-id-stmt) for use should be called *after* insertBeforeNoRedirect(1st-non-id-stmt) for def
		//   2. For return-value use at call site CS and corresponding def of lhs side of call stmt,
		//      call insertBeforeNoRedirect on succ(CS) for def and *after* that call insertBeforeNoRedirect on succ(CS) for ret-var use
		//   3. For same-stmt def/use, call insertAtProbeBottom for def and *after* that call insertAtProbeTop for use
		
		VarIdGroupWriter varIdGroupWriter = new VarIdGroupWriter("varidgroups.out");
		varIdGroupWriter.writeSectionHeader("DEFINITIONS");
		
		/////////////////////////////
		// INSTRUMENT ALL DEFS/KILLS
		System.out.println("START instrumenting defs and kills " + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
		List<NodePoint> sortedPntsForDefKillVarKeys = new ArrayList<NodePoint>(pointToDefOrKillVarIds.keySet());
		Collections.sort(sortedPntsForDefKillVarKeys, NodePoint.NodePointComparator.inst);
		int varGroupId = -1; // group var ids to avoid excessive number of report calls due to lots of var ids per defined/used var at each point
		for (NodePoint pntDef : sortedPntsForDefKillVarKeys) {
			// instrument defs/kills for this point
			NodeDefUses nDef = (NodeDefUses) pntDef.getN();
			Stmt sDef = nDef.getStmt();
			
			SootMethod mDef = ProgramFlowGraph.inst().getContainingMethod(nDef);
			PatchingChain pchain = mDef.retrieveActiveBody().getUnits();
			Local lFrameId = getCreateAssignFrameIdLocal(mDef);
			
			List<Variable> defVars = getAllDefVars(pntDef); //nDef.getDefinedVars();
			List<List<Integer>> varIdsPerDef = new ArrayList<List<Integer>>(); // all vars that can be defined when corresponding var at this point is defined
			for (int defIdx = 0; defIdx < defVars.size(); ++defIdx)
				varIdsPerDef.add(new ArrayList<Integer>());
			for (Integer varId : pointToDefOrKillVarIds.get(pntDef)) {
				// ensure that we use var object defined in this node or used as arg call (local or const)
				Variable varAtDef = allVars.get(varId);
//				if (!varAtDef.isLocalOrConst())
//					varAtDef = getVarMayEqualAndAlias(defVars, varAtDef);
				final int defIdx = getVarMayEqualAndAlias(defVars, varAtDef);
				
				// hcai: skip variables that are collected in the Def/Kill var set at pntDef but have NO match in the definted var set at the same Nodepoint
				if (ignoreUnMatchedVars && -1 == defIdx) {
					System.out.print("HCAI Warning: variable " + varAtDef + " collected in the Def/Kill var set at " + pntDef + " but have no match in the defined var set at this point" );
					continue;
				}
				// -
				
//				final int defIdx = defVars.indexOf(varAtDef);
				assert defIdx >= 0 && defIdx < defVars.size();
				
				// associate varId with defIdx
				assert !varIdsPerDef.get(defIdx).contains(varId);
				varIdsPerDef.get(defIdx).add(varId);
			}
			
			// var id groups are in range [varGroupId + 1, varGrouId + defVars.size()] based on current value of varGroupId
			for (int defIdx = 0; defIdx < varIdsPerDef.size(); ++defIdx) {
				Variable varAtDef = defVars.get(defIdx);
				++varGroupId; // set var group id for this var defined in this point
				
				// save the var ids for this group id
				varIdGroupWriter.writeVarIdGroup(varGroupId, varIdsPerDef.get(defIdx));
				
				// special case: new array
				final boolean isNewArrVarDef = sDef instanceof AssignStmt && (((AssignStmt)sDef).getRightOp() instanceof NewArrayExpr)
												&& varAtDef.isArrayRef();
//				final boolean isCtorCall = sDef instanceof InvokeStmt && sDef.getInvokeExpr() instanceof SpecialInvokeExpr;
				
				// params: nId, frameId, entry, varId, obj, idx, obj[]
				List probeArgs = new ArrayList();
				List probeReport = new ArrayList();
				List args = new ArrayList();
				args.add(IntConstant.v(StmtMapper.getGlobalNodeId(nDef))); // param 1: node id
				args.add(IntConstant.v((pntDef.getRhsPos() == NodePoint.PRE_RHS)? 1 : 0)); // param 2: pre (position at node -- pre or post rhs)
				args.add(lFrameId);               // param 3: frame id
				args.add(IntConstant.v(sDef instanceof IdentityStmt? 1 : 0)); // param 4: entry of method (id stmt)
				args.add(IntConstant.v(varGroupId)); //varId));   // param 5: var group id
				Value vBaseOfObj = prepareObjArg(varAtDef, nDef, mDef, probeReport); //probeArgs);
				if (!isNewArrVarDef)
					args.add(IntConstant.v( (!(vBaseOfObj instanceof NullConstant) || Util.isReturnStmt(sDef))? 1 : 0 )); // param 6 (if regular def): interproc or not
				args.add(vBaseOfObj);                   // param 6 or 7: containing obj (or null const)
				final boolean isThereBaseOfObj = !(vBaseOfObj instanceof NullConstant);
				Value vIdx = prepareIdxArg(varAtDef, nDef, mDef, probeArgs);
				args.add(vIdx);                   // param 7 or 8: idx var, or -1 constant
				// param 7: obj[] varVals
				if (includeVals) {
					List<Variable> toReadVars = pointToVarListsToRead.containsKey(pntDef)? pointToVarListsToRead.get(pntDef).get(defIdx) : null;
					Local lVarValsArray;
					lVarValsArray = getCreateAssignVarValsArray(mDef, sDef, toReadVars, probeArgs);
					args.add(lVarValsArray);
				}
				else
					args.add(NullConstant.v());
				
				Stmt sReportCall = Jimple.v().newInvokeStmt( Jimple.v().newStaticInvokeExpr(
						(isNewArrVarDef? mReportArrDef : mReportDef).makeRef(), args) );
				probeReport.add(sReportCall);
				// create joint probe for easier use when not splitting it
				List probe = new ArrayList(probeArgs);
				probe.addAll(probeReport);
				
				//  for id stmt, take val of formal arg after end of id stmts block
				if (sDef instanceof IdentityStmt) {
					assert !isNewArrVarDef && !isThereBaseOfObj;
					InstrumManager.v().insertBeforeNoRedirect(pchain, probe, UtilInstrum.getFirstNonIdStmt(pchain)); // use report for rhs will be later inserted BEFORE this def probe
				}
				else {
					//  for lhs def stmt with call (lib or app), take val of defined var after stmt
					if (sDef.containsInvokeExpr() && sDef instanceof AssignStmt && pntDef.getRhsPos() == NodePoint.POST_RHS &&
							((Local)((AssignStmt)sDef).getLeftOp()) == varAtDef.getValue()) {
						assert nDef.getSuccs().size() == 1;
//						assert !nDef.hasAppCallees();
						assert !dua.util.Util.isCtorCall(sDef);
						assert !isNewArrVarDef && !isThereBaseOfObj;
						InstrumManager.v().insertBeforeNoRedirect(pchain, probe, firstSafeDefInstrStmt((Stmt)pchain.getSuccOf(sDef), mDef));
					}
					else {
						// ENSURE THAT DEF REPORT OCCURS AFTER USE REPORTS AND CDTGT REPORTS
						//  for inside-lib call def stmt, take val of used vars before stmt
						//  for def stmt with no call, take rhs uses before stmt
						//  SPECIAL CASE: def of array elements at new array stmt; gather idx before probe but report arrdef after!
						if (isNewArrVarDef || isThereBaseOfObj) {
							// split probe: args are collected before, but base obj and report occur after newarray/base ref
							Stmt sFirstSafeDefPnt = firstSafeDefInstrStmt(sDef, mDef); // sDef or first stmt after which it's safe to access base (array ref or base ref)
							if (sFirstSafeDefPnt == sDef) {
								Stmt sSuccOfSafeDef = (Stmt)pchain.getSuccOf(sFirstSafeDefPnt);//sDef);
	//							assert sFirstSafeDefPnt == sDef || sFirstSafeDefPnt == sSuccOfDef; // this instr point shouldn't happen before r0.(Object.<init>())()
								if (!probeArgs.isEmpty())
									InstrumManager.v().insertAtProbeBottom(pchain, probeArgs, sFirstSafeDefPnt); // point just before newarray or use of base (e.g. r0.(superclass)<init>)
								InstrumManager.v().insertBeforeNoRedirect(pchain, probeReport, sSuccOfSafeDef); // point after newarray or use of base (e.g. r0.(superclass)<init>)
								// update split-probe counter for this stmt
								Integer probeCount = splitProbes.get(sDef);
								if (probeCount == null)
									probeCount = SPLIT_PROBE_COUNT_START_INDEX;
								splitProbes.put(sDef, ++probeCount);
							}
							// *** RAUL_DEBUG ***
						}
						else
							InstrumManager.v().insertAtProbeBottom(pchain, probe, sDef); // *** RAUL_DEBUG *** firstSafeDefInstrStmt(sDef, mDef));
					}
				}
			}
		}
		System.out.println("END instrumenting defs and kills " + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
		
		/////////////////////////////
		// INSTRUMENT ALL USES
		System.out.println("START instrumenting uses " + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
		varIdGroupWriter.writeSectionHeader("USES");
		List<NodePoint> sortedPntsForUseVarKeys = new ArrayList<NodePoint>(pointToUseVarIds.keySet());
		Collections.sort(sortedPntsForUseVarKeys, NodePoint.NodePointComparator.inst);
		for (NodePoint pntUse : sortedPntsForUseVarKeys) {
			// instrument uses for this point
			NodeDefUses nUse = (NodeDefUses) pntUse.getN();
			Stmt sUse = nUse.getStmt();
			
			SootMethod mUse = ProgramFlowGraph.inst().getContainingMethod(nUse);
			PatchingChain pchain = mUse.retrieveActiveBody().getUnits();
			Local lFrameId = getCreateAssignFrameIdLocal(mUse);
			
			List<Variable> usedVars = getAllUsedVars(pntUse);
			List<List<Integer>> varIdsPerUse = new ArrayList<List<Integer>>(); // all vars that can be used when corresponding var at this point is used
			for (int useIdx = 0; useIdx < usedVars.size(); ++useIdx)
				varIdsPerUse.add(new ArrayList<Integer>());
			for (Integer varId : pointToUseVarIds.get(pntUse)) {
				// ensure that we use var object whose base is accessible in this node
				Variable varAtUse = allVars.get(varId);
//				if (!varAtUse.isLocalOrConst())
//					varAtUse = (Variable)getVarMayEqualAndAlias(usedVars, varAtUse);
				final int useIdx = getVarMayEqualAndAlias(usedVars, varAtUse);
				
				// hcai: skip variables that are collected in the Use var set at pntUse but have NO match in the used var set at the same Nodepoint
				if (ignoreUnMatchedVars && -1 == useIdx) {
					System.out.print("HCAI Warning: variable " + varAtUse + " collected in the Use var set at " + pntUse + " but have no match in the used var set at this point" );
					continue;
				}
				// -
				
//				final int useIdx = useVars.indexOf(varAtDef);
				assert useIdx >= 0 && useIdx < usedVars.size();
				
				// associate varId with defIdx
				assert !varIdsPerUse.get(useIdx).contains(varId);
				varIdsPerUse.get(useIdx).add(varId);
			}
			for (int useIdx = 0; useIdx < varIdsPerUse.size(); ++useIdx) {
				Variable varAtUse = usedVars.get(useIdx);
				++varGroupId; // set var group id for this var defined in this point
				
				// save the var ids for this group id
				varIdGroupWriter.writeVarIdGroup(varGroupId, varIdsPerUse.get(useIdx));
				
				// params: nId, frameId, entry, varId, obj, idx, obj[]
				List probe = new ArrayList();
				List args = new ArrayList();
				args.add(IntConstant.v(StmtMapper.getGlobalNodeId(nUse))); // param 1: node id
				args.add(IntConstant.v((pntUse.getRhsPos() == NodePoint.PRE_RHS)? 1 : 0)); // param 2: pre (position at node -- pre or post rhs)
				args.add(lFrameId);               // param 3: frame id
				args.add(IntConstant.v(sUse instanceof IdentityStmt? 1 : 0)); // param 4: entry of method (id stmt)
				args.add(IntConstant.v(varGroupId));   // param 5: var group id  // was: var id
				Value vObj = prepareObjArg(varAtUse, nUse, mUse, probe);
				args.add(vObj);                   // param 6: containing obj (or null const)
				Value vIdx = prepareIdxArg(varAtUse, nUse, mUse, probe);
				args.add(vIdx);                   // param 7: idx var, or -1 constant
				
				Stmt sReportCall = Jimple.v().newInvokeStmt( Jimple.v().newStaticInvokeExpr(mReportUse.makeRef(), args) );
				probe.add(sReportCall);
				
				//  SPECIAL CASE 1: for id stmt use, insert probe after end of id stmts block
				if (sUse instanceof IdentityStmt)
					InstrumManager.v().insertBeforeNoRedirect(pchain, probe, UtilInstrum.getFirstNonIdStmt(pchain)); // this goes BEFORE def report
				else if (nUse.hasAppCallees() && varAtUse.isLocalOrConst() && varAtUse instanceof ReturnVar) {
					//  SPECIAL CASE 2: for ret-val use at app call stmt, insert probe after stmt
//					assert !usedVars.contains(varAtUse);
					assert nUse.getSuccs().size() == 1;
					assert !nUse.hasAppCallees() || pntUse.getRhsPos() == NodePoint.POST_RHS;
					assert !dua.util.Util.isCtorCall(sUse);
					InstrumManager.v().insertBeforeNoRedirect(pchain, probe, (Stmt)pchain.getSuccOf(sUse));
				}
				else  //  GENERAL CASE: for inside-lib or explicit use, insert probe at TOP of stmt's probe (to ensure it occurs before corresponding def) 
					InstrumManager.v().insertAtProbeTop(pchain, probe, sUse);
			}
		}
		System.out.println("END instrumenting uses " + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
		
		// finish var id groups file
		varIdGroupWriter.finishWriting();
		
		/////////////////////////////
		// --CONTROL DEPS
		System.out.println("START instrumenting CDs " + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
		// 1. Instrument all cd sources (branches and multi-tgt virtual calls)
		System.out.println("DEBUG: ctrl deps"); // DEBUG
		Set<NodePoint> pntCDTgts = new HashSet<NodePoint>();
		Set<SootMethod> mtdCallees = new HashSet<SootMethod>(); // keeps track of targets of virtual call that need 'calleeTgt' instrumentation
		for (NodePoint pntSrc : pointToBrToControlTgts.keySet()) {
			Map<Integer,List<NodePoint>> brToCtrlTgts = pointToBrToControlTgts.get(pntSrc);
			for (Integer brId : brToCtrlTgts.keySet()) {
				// instrument branch/caller-callee edge -- src of 1 or more cds
				//   in case of virtual call, instrument the first part (caller) and store target (callee) for the second part to instrument later
				CFGNode nSrc = pntSrc.getN();
				Stmt sSrc = nSrc.getStmt();
				CallSite cs = nSrc.getCallSite();
				
				SootMethod mSrc = ProgramFlowGraph.inst().getContainingMethod(nSrc);
				PatchingChain pchain = mSrc.retrieveActiveBody().getUnits();
				
				// params if branch: nId, pre, brId, frameId, objVarVals[]
				// params if call:   nId, objVarVals[]
				//   read needed vars before src node: collect var values in array
				List probeSrc = new ArrayList();
				Local lFrameId = getCreateAssignFrameIdLocal(mSrc);
				List args = new ArrayList();
				args.add(IntConstant.v(StmtMapper.getGlobalNodeId(pntSrc.getN())));      // param 1: node id
				if (cs == null) {
					args.add(IntConstant.v(pntSrc.getRhsPos() == NodePoint.PRE_RHS? 1 : 0)); // param 2: pre position or not
					args.add(IntConstant.v(brId));                                           // param 3: br id
				}
				args.add(lFrameId);                                                      // param 2 (call) or 4 (branch): frame id
				// param 3 (call) or 5 (branch): obj[] varVals
				if (includeVals) {
					final int varListIdx = getAllDefVars(pntSrc).size();
					List<Variable> toReadVars = pointToVarListsToRead.get(pntSrc).get(varListIdx);
					Local lVarValsArray;
					lVarValsArray = getCreateAssignVarValsArray(mSrc, sSrc, toReadVars, probeSrc);
					args.add(lVarValsArray);
				}
				else
					args.add(NullConstant.v());
				
				Stmt sReportCall = Jimple.v().newInvokeStmt( Jimple.v().newStaticInvokeExpr((cs == null? mReportBranch : mReportCallSrc).makeRef(), args) );
				probeSrc.add(sReportCall); // either reportBranch or reportCallSrc
				
				if (cs == null)   // if branch, instrument that control-flow edge
					InstrumManager.v().insertProbeAt( nSrc.getOutBranches().get(brId) , probeSrc );
				else {
					// instrument point prior to call site -- first part of cd-src
					InstrumManager.v().insertAtProbeBottom(pchain, probeSrc, sSrc);
					
					// prepare to report later the second half of the caller-callee cd src, at the tgt of the call (i.e., the callee)
					mtdCallees.add(cs.getAppCallees().get(brId)); // brId is used to locate the target method instead of being the ctrl id itself
				}
				
				// update set of cd-tgts that will need instrumentation
				pntCDTgts.addAll(brToCtrlTgts.get(brId));
			}
		}
		
		// 2. Instrument all cd targets (branches and multi-tgt virtual calls)
		for (NodePoint pntTgt : pntCDTgts) {
			CFGNode nTgt = pntTgt.getN();
			SootMethod mTgt = ProgramFlowGraph.inst().getContainingMethod(nTgt);
			PatchingChain pchain = mTgt.retrieveActiveBody().getUnits();
			
			// params: nId, pre, frameId, entry
			List probe = new ArrayList();
			Local lFrameId = getCreateAssignFrameIdLocal(mTgt);
			List args = new ArrayList();
			args.add(IntConstant.v(StmtMapper.getGlobalNodeId(nTgt)));               // param 1: node id
			args.add(IntConstant.v(pntTgt.getRhsPos() == NodePoint.PRE_RHS? 1 : 0)); // param 2: pre position or not
			args.add(lFrameId);                                                      // param 3: frame id
			args.add(IntConstant.v(nTgt.getStmt() instanceof IdentityStmt? 1 : 0));  // param 4: entry of method (id stmt)
			Stmt sReportCall = Jimple.v().newInvokeStmt( Jimple.v().newStaticInvokeExpr(mReportCDTgt.makeRef(), args) );
			probe.add(sReportCall); // either reportBranch or reportCallSrc
			
			// instrument at probe before tgt and BEFORE defs and cd-srcs
			InstrumManager.v().insertAtProbeTop(pchain, probe, UtilInstrum.getFirstNonIdStmtFrom(pchain, nTgt.getStmt()));
		}
		
		// 3. Only now instrument the second part (callee) of the cd-src for virtual-call edges, so that target cd probes execute *after* this one
		for (SootMethod mCallee : mtdCallees) {
			// params: mtdId, frameId  --  NOTE: pre is always true; mtdId serves as a unique identifier of callee
			PatchingChain pchCallee = mCallee.retrieveActiveBody().getUnits();
			List probeCallee = new ArrayList();
			Local lFrameIdCallee = getCreateAssignFrameIdLocal(mCallee);
			List argsCallee = new ArrayList();
			argsCallee.add(IntConstant.v(ProgramFlowGraph.inst().getMethodIdx(mCallee)));  // param 1: mtd id  (ctrl id)
			argsCallee.add(lFrameIdCallee);                                                // param 2: frame id
			Stmt sReportCall = Jimple.v().newInvokeStmt( Jimple.v().newStaticInvokeExpr(mReportCallTgt.makeRef(), argsCallee) );
			probeCallee.add(sReportCall); // either reportBranch or reportCallSrc
			InstrumManager.v().insertBeforeNoRedirect(pchCallee, probeCallee, UtilInstrum.getFirstNonIdStmt(pchCallee));
		}
		
		// --END OF CTRL DEPS instrumentation
		System.out.println("END instrumenting CDs " + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
		
		/////////////////////////////
		// -- CHANGE COV instrumentation
		if (includeChangeCov) {
			for (DependenceGraph depGraph : depGraphs) {
				NodePoint pntChange = depGraph.getStart();
				CFGNode nChange = pntChange.getN();
				SootMethod mChange = ProgramFlowGraph.inst().getContainingMethod(nChange);
				PatchingChain pchain = mChange.retrieveActiveBody().getUnits();
				
				// params: nId, pre
				List probe = new ArrayList();
				List args = new ArrayList();
				args.add(IntConstant.v(StmtMapper.getGlobalNodeId(nChange)));               // param 1: node id
				args.add(IntConstant.v(pntChange.getRhsPos() == NodePoint.PRE_RHS? 1 : 0)); // param 2: pre position or not
				Stmt sReportCall = Jimple.v().newInvokeStmt( Jimple.v().newStaticInvokeExpr(mReportChange.makeRef(), args) );
				probe.add(sReportCall); // either reportBranch or reportCallSrc
				
				// instrument at probe before tgt and before everything else
				InstrumManager.v().insertAtProbeTop(pchain, probe, UtilInstrum.getFirstNonIdStmtFrom(pchain, nChange.getStmt()));
			}
		}
		
		/////////////////////////////
		// this should be the last instrumentation step involving the frame-id local, which must be initialized *before* all other probes that use it
		insertFrameIdProbes();
		
		/////////////////////////////
		// initialize reporter at BEGINNING of each entry method
		//  this must be the last instrumentation inserted to ensure initialization before anything else
		UtilInstrum.initializeAtEntryMethods(clsReporter, ProgramFlowGraph.inst().getEntryMethods());
		
		/////////////////////////////
		// slice.out:
		//   first section: vars from data dependencies and other uses ("inputs"), ordered by id
		//      var: type (loc/fld/arrel/obj),
		//           idx in locals box / field name / arrel idx / nothing (base IS object)
		//   second section: dependencies, ordered by id
		//      dep: type (du/br), src node, tgt node (if du), var id (if du)
		//   one section per dep graph: starting dependencies, dependencies that are linked in graph
		DynSliceOutFileWriter dynSliceOut = new DynSliceOutFileWriter(includeVals, includeChangeCov, includeBrCov, includeDUCov, eventLimit, allVars.size(),
				allDeps, depGraphs, cacheAllVarsToIds);
		dynSliceOut.printOutFile();
	}
	
	/** Returns the first "safe" stmt where we can instrument a definition that will include r0. The only unsafe place is before a call to superclass.<init>() in a ctor.
	 *  A "safe" place can be the call to the superclass ctor, since the instrumentation will not read r0 before it but instead will access r0 between the safe point and its successor. */
	private static Stmt firstSafeDefInstrStmt(Stmt sDef, SootMethod mDef) {
//		// *** RAUL_DEBUG: for now
//		return sDef;
		
		final boolean isInCtorMtd = mDef.getName().equals("<init>");
		// handle common case: when it's not a ctor
		if (!isInCtorMtd)
			return sDef;
		// now, traverse chain from sDef and return sDef as safe if we don't find call to base ctor (i.e., if base ctor call must be placed before sDef)
		//   if sDef occurs before, continue and return stmt for base ctor call as "safe"
		PatchingChain pchain = mDef.retrieveActiveBody().getUnits();
		Stmt sCurr = sDef;
		SootClass clsSuper = mDef.getDeclaringClass().getSuperclass();
		Local lThis = mDef.retrieveActiveBody().getLocals().getFirst();
		while (true) {
			// check for call to superclass ctor -- that's the safe point if found
			if (sCurr.containsInvokeExpr() && sCurr.getInvokeExpr() instanceof SpecialInvokeExpr) {
				SpecialInvokeExpr spInvExpr = (SpecialInvokeExpr) sCurr.getInvokeExpr();
				SootMethod mtdCalled = spInvExpr.getMethod();
				if (mtdCalled.getName().equals("<init>") && mtdCalled.getDeclaringClass().equals(clsSuper) && spInvExpr.getBase() == lThis)
					return sCurr; //(Stmt) pchain.getSuccOf(sCurr);
			}
			if (sCurr == pchain.getLast())
				break; // call to superclass ctor not found after sDef, so sDef is a safe point
			sCurr = (Stmt) pchain.getSuccOf(sCurr);
		}
		return sDef;
	}
	
	/** Finds (first) var in list that may equal and alias given var v. It is assumed that such a var exists in the list.
	 *  It handles the cases in which:
	 *  	1) var to match is a returned var and var list has a representative returned var at the end (to which it is matched), or
	 *      2) var is actual arg passed to an identify statement, which matches representative var in list for all such args.
	 *  @return Two objects: variable found and index of var in list */
	private int getVarMayEqualAndAlias(List<Variable> vars, Variable v) {
		int idx = 0;
		for (Variable vInList : vars) {
			if (vInList.mayEqualAndAlias(v))
				return idx;
			++idx;
		}
		// another posibility: v is an instance-invoke obj var whose base points to "cousin" type of a var in the list
		//   this may occur because cache of vars->varids in {@link #findDepVarsAndKills()} equates cousin def obj var to an existing def var in the cache, which is not easy to work around
		Value val = v.getValue();
		if (v instanceof ObjVariable && val instanceof InstanceInvokeExpr) {
			idx = 0;
			for (Variable vInList : vars) {
				Value valInList = vInList.getValue();
				if (valInList instanceof InstanceInvokeExpr && ((InstanceInvokeExpr)valInList).getBase() == ((InstanceInvokeExpr)val).getBase()) {
					System.out.println("DBG: in getVarMayEqualAndAlias, found cousin def obj var match of " + v + " and " + vInList + " via base locals");
					return idx;
				}
				++idx;
			}
			System.out.println("DBG: something wrong in getVarMayEqualAndAlias -- couldn't find cousin def obj var match of " + v + " via base locals in list " + vars);
			// hcai: allow for unsuccessful matching
			if (!ignoreUnMatchedVars) {
				assert false; // should never get here, but if we get here, we'll need to figure out what to do
			}
		}
		// another posibility: v is a returned var and vars has a returned var representative at the end
		if (v instanceof ReturnVar) {
			--idx;
			assert vars.get(idx) instanceof ReturnVar;
			return idx;
		}
		// a final posibility: v is a representative of all actual parameters linked to a formal parameter in the var list
		if (vars.size() == 1 && vars.get(0) instanceof CSArgVar)
			return 0;
		
		// hcai: allow for unsuccessful matching
		if (!ignoreUnMatchedVars) {
			assert false;
		}
		// -
		return -1;
	}
	
	private void findAllDeps(List<DependenceGraph> depGraphs) {
		// collect all deps from all fwd slices
		Set<Dependence> allDepsSet = new HashSet<Dependence>();
		for (DependenceGraph depGraph : depGraphs)
			allDepsSet.addAll(depGraph.getDeps());
		// create sorted list of all dependencies
		allDeps = new ArrayList<Dependence>(allDepsSet);
		Collections.sort(allDeps, new StringBasedComparator<Dependence>());
		
		/*
		// HCAI: just for debugging
		List<Dependence> allDeps2 = new ArrayList<Dependence>();
		for (Dependence dep: allDeps) {
			if (dep.toString().contains("ByteArrayInputStream")) {
				allDeps2.add(dep);
			}
		}
		allDeps = allDeps2;
		*/
	}
	
	private void findDepVarsAndKills() {
		// DEBUG
		final int MAX_DOT_COL = 150;
		final int ELEMS_PER_DOT = 100;
		int elemsLeft = ELEMS_PER_DOT;
		int dotsLeft = MAX_DOT_COL;
		
		// collect all needed vars:
		//   vars to read at dep src points
		//   def/use vars
		//   kills of def/use vars
		for (Dependence dep : allDeps) {
			if (--elemsLeft == 0) {
				System.out.print('.');
				elemsLeft = ELEMS_PER_DOT;
				if (--dotsLeft == 0) {
					dotsLeft = MAX_DOT_COL;
					System.out.println();
				}
			}
			
			// collecting vars to read from rhs of dep's src:
			//  for def stmt with no call, take rhs uses before stmt
			//  for id stmt, take val of formal arg after end of id stmts block
			//  for inside-lib call def stmt, take val of used vars before stmt
			//  for lhs def stmt with call (lib or app), take val of defined var after stmt
			//  for control dep, take uses before stmt
			NodePoint pntSrc = dep.getSrc();
			NodeDefUses nSrc = (NodeDefUses) pntSrc.getN();
			Stmt sSrc = nSrc.getStmt();
			
			// get/create list of vars-to-read for def
			List<Variable> varsToReadForDep;
			Variable varDep; // will be assigned "null" if ctrl dep
			if (dep instanceof DataDependence) {
				varDep = ((DataDependence)dep).getVar();
				if (nSrc.getStmt().containsInvokeExpr()) {
					if (sSrc instanceof AssignStmt && pntSrc.getRhsPos() == NodePoint.POST_RHS &&
							((Local)((AssignStmt)sSrc).getLeftOp()) == ((DataDependence)dep).getVar().getValue()) {
//						assert !nSrc.hasAppCallees();
						//  for lhs def stmt with call (lib or app), take val of defined var after stmt
						varsToReadForDep = new ArrayList<Variable>(); //nSrc.getDefinedVars();
						varsToReadForDep.add(((DataDependence)dep).getVar());
					}
					else {
						//  for inside-lib call def stmt, take val of all used vars before stmt (except for const str objs!)
						//   (vars might be repeated, such as obj vars, but it's better than missing vars due to equality)
						if (varDep.isStrConstObj()) {
							varsToReadForDep = new ArrayList<Variable>();
							varsToReadForDep.add(varDep);
						}
						else
							varsToReadForDep = nSrc.getUsedVars();
					}
				}
				else {
					if (nSrc.getStmt() instanceof IdentityStmt) {
						//  for id stmt, take val of formal arg after end of id stmts block
						varsToReadForDep = nSrc.getDefinedVars();
					}
					else {
						//  for def stmt with no call, take rhs uses before stmt
						varsToReadForDep = nSrc.getUsedVars();
					}
				}
				
				// add var to list of vars, if not there yet
				Integer varId = cacheAllVarsToIds.get(varDep);
				if (varId == null) {
					varId = cacheAllVarsToIds.size();
					cacheAllVarsToIds.put(varDep, varId);
					allVars.add(varDep);
				}
				
				// add def to node->def/kills map, for def/kill reporting
				List<Integer> defVarIdsForSrc = pointToDefOrKillVarIds.get(pntSrc);
				if (defVarIdsForSrc == null) {
					defVarIdsForSrc = new ArrayList<Integer>();
					pointToDefOrKillVarIds.put(pntSrc, defVarIdsForSrc);
				}
				if (!defVarIdsForSrc.contains(varId))
					defVarIdsForSrc.add(varId);
				
				// add use to node->uses map, for use reporting
				NodePoint pntTgt = dep.getTgt();
				List<Integer> useVarIdsForTgt = pointToUseVarIds.get(pntTgt);
				if (useVarIdsForTgt == null) {
					useVarIdsForTgt = new ArrayList<Integer>();
					pointToUseVarIds.put(pntTgt, useVarIdsForTgt);
				}
				if (!useVarIdsForTgt.contains(varId))
					useVarIdsForTgt.add(varId);
			}
			else {
				//  for control dep, take uses before stmt
				varsToReadForDep = nSrc.getUsedVars();
				varDep = null;
				
				// add cd-src to node->br->cd-tgts map, for cd-src (br) reporting
				final int brId = ((ControlDependence)dep).getBrId();
				Map<Integer, List<NodePoint>> brIdToTgts = Util.getCreateMapValue(pointToBrToControlTgts, pntSrc, HashMap.class);
				List<NodePoint> tgts = Util.getCreateMapValue(brIdToTgts, brId, ArrayList.class);
				tgts.add(dep.getTgt());
			}
			
			// associate list of vars-to-read to dep's src point
			if (includeVals) {
				// ... but only if their values need to be read
				List<Variable> allDefVarsAtPoint = getAllDefVars(pntSrc);
				final int numListsAtNode = allDefVarsAtPoint.size() + ((dep instanceof ControlDependence)? 1 : 0);
				List<List<Variable>> varListsToReadForSrcPnt = pointToVarListsToRead.get(pntSrc);
				if (varListsToReadForSrcPnt == null) {
					// init list of lists of vars to read for point
					varListsToReadForSrcPnt = new ArrayList<List<Variable>>(numListsAtNode);
					for (int i = 0; i < numListsAtNode; ++i)
						varListsToReadForSrcPnt.add(null);
					pointToVarListsToRead.put(pntSrc, varListsToReadForSrcPnt);
				}
				// get/create list of vars for def idx
				final int varListIdx = (dep instanceof ControlDependence)? numListsAtNode - 1 : allDefVarsAtPoint.indexOf(varDep);
				assert varListIdx >= 0;
				List<Variable> varsToReadForSrc = varListsToReadForSrcPnt.get(varListIdx);
				if (varsToReadForSrc == null) {
					varsToReadForSrc = new ArrayList<Variable>();
					varListsToReadForSrcPnt.set(varListIdx, varsToReadForSrc);
				}
				// only add vars not associated to point yet
				final boolean isCtorCall = dua.util.Util.isCtorCall(sSrc);
				final boolean isNewArr = sSrc instanceof AssignStmt && ((AssignStmt)sSrc).getRightOp() instanceof NewArrayExpr;
				SootMethod mSrc = ProgramFlowGraph.inst().getContainingMethod(nSrc);
				final boolean isInCtorMtd = mSrc.getName().equals("<init>");
				for (Variable var : varsToReadForDep) {
					if (isCtorCall) {
						Value baseCtor = ((SpecialInvokeExpr)sSrc.getInvokeExpr()).getBase();
						Value valToRead = var.getValue();
						if (valToRead == baseCtor)
							continue;
						if (var.isObject() && valToRead instanceof SpecialInvokeExpr && ((SpecialInvokeExpr)valToRead).getBase() == baseCtor)
							continue;
					}
					if (isNewArr) {
						if (var.getValue() == ((AssignStmt)sSrc).getLeftOp())
							continue;
					}
					if (isInCtorMtd) {
						// check whether var or its base is 'this' (local 0)
						Value local0 = mSrc.retrieveActiveBody().getLocals().getFirst();
						if (var.getValue() == local0 || var.getBaseLocal() == local0)
							continue; // skip base var in ctor, since it's not fully constructed
					}
					if (!varsToReadForSrc.contains(var))
						varsToReadForSrc.add(var);
				}
			}
		}
		
		// DEBUG
		System.out.println();
		System.out.println("DBG: finished finding vars " + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
		elemsLeft = ELEMS_PER_DOT;
		dotsLeft = MAX_DOT_COL;
		
		// add to pnt->def/kills map those kills (i.e. defs not in deps) of used vars
		List<NodePoint> sortedPntsForUseVarKeys = new ArrayList<NodePoint>(pointToUseVarIds.keySet());
		Collections.sort(sortedPntsForUseVarKeys, NodePoint.NodePointComparator.inst);
		// hcai: for debugging
		System.out.print("There will be "+ sortedPntsForUseVarKeys.size()/ELEMS_PER_DOT + " dots to be printed before finishing search for kills.");
		// -
		for (NodePoint pntUse : sortedPntsForUseVarKeys) {
			// DEBUG
			if (--elemsLeft == 0) {
				System.out.print('.');
				elemsLeft = ELEMS_PER_DOT;
				if (--dotsLeft == 0) {
					dotsLeft = MAX_DOT_COL;
					System.out.println();
				}
			}
			
			List<Integer> useVarIds = pointToUseVarIds.get(pntUse);
			for (Integer useVarIdToKill : useVarIds) {
				Variable vToKill = allVars.get(useVarIdToKill);
				if (vToKill.isKillable()) {
					for (Pair<Def,Integer> defAndPos : DependenceFinder.getAllDefsForUse(vToKill, pntUse.getN())) {
						// retrieve list of vars for node and determine whether it's a kill
						CFGNode nPossibleKill = defAndPos.first().getN();
						if (!(nPossibleKill instanceof NodeDefUses))
							continue; // special def that can't kill var
						// add kill as another def for this var
						NodePoint pntKill = new NodePoint(nPossibleKill, defAndPos.second());
						List<Integer> defVarsAtKillNode = pointToDefOrKillVarIds.get(pntKill);
						if (defVarsAtKillNode == null) {
							defVarsAtKillNode = new ArrayList<Integer>();
							pointToDefOrKillVarIds.put(pntKill, defVarsAtKillNode);
						}
						// ... but avoid repeating killed/defined vars for node
						if (!defVarsAtKillNode.contains(useVarIdToKill))
							defVarsAtKillNode.add(useVarIdToKill); // not there before -> it's a pure kill
					}
				}
			}
		}
		
		// DEBUG
		System.out.println();
		System.out.println("DBG: finished finding kills " + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
		
		// *** DEBUG - begin
		System.out.println("DEBUG: fwd slice vars");
		int i = 0;
		for (Variable v : allVars)
			System.out.println(" " + i++ + " " + v);
		// *** DEBUG - end
	}
	
	private Map<NodePoint,List<Variable>> cachePointToDefVars = new HashMap<NodePoint, List<Variable>>();
	/** Includes the vars defined at corresponding point in node -- pre or post.
	 *  For pre, it includes string objects created and passed as arguments, plus argument variables for explicit formal arguments.
	 *  For post, it includes all 'regular' defined vars at node as well as one representative returned variable if this is a call site that stores the result. */
	private List<Variable> getAllDefVars(NodePoint pnt) {
		// retrieve from cache, if present
		List<Variable> defVars = cachePointToDefVars.get(pnt);
		if (defVars != null)
			return defVars;
		// not in cache; compute and store in cache
		defVars = new ArrayList<Variable>();
		cachePointToDefVars.put(pnt, defVars);
		
		// fill var list
		NodeDefUses n = (NodeDefUses) pnt.getN();
		if (pnt.getRhsPos() == NodePoint.PRE_RHS) {
			// first: string const objects
			for (Variable v : n.getDefinedVars())
				if (v.isStrConstObj())
					defVars.add(v);
			// second: app call arguments
			CallSite cs = n.getCallSite();
			if (cs != null) {
				for (int i = 0; i < cs.getNumActualParams(); ++i) {
					Value val = cs.getActualParam(i);
					Variable argVar = (val instanceof Constant)? new CSArgVar((Constant)val, cs, i) : new StdVariable(val); // new CSArgVar(val, cs, i)
					defVars.add(argVar);
				}
			}
		}
		else {
			// first: all lhs and intra-lib call defined vars
			for (Variable varDef : n.getDefinedVars())
				if (!varDef.isStrConstObj())
					defVars.add(varDef);
			
			// second: return val definition
			Stmt s = n.getStmt();
			if (s instanceof ReturnStmt || s instanceof RetStmt) {
				List<ValueBox> useBoxes = s.getUseBoxes();
				if (!useBoxes.isEmpty()) {  // returns a value (i.e., it's not a simple 'return' stmt in 'void' method)
					assert useBoxes.size() == 1;
					SootMethod mFrom = ProgramFlowGraph.inst().getContainingMethod(s);
					MethodTag mTag = (MethodTag) mFrom.getTag(MethodTag.TAG_NAME);
					for (CallSite csCaller : mTag.getCallerSites()) {
						if (!csCaller.isReachableFromEntry())
							continue;
						Stmt sCaller = csCaller.getLoc().getStmt();
						if (sCaller instanceof AssignStmt) {
							// there should be one and only one returned local/const
							// NOTE: look at (unique) use box instead of NodeDefUses.getUsedVars which only considers vars, not constants
							Value vRet = ((ValueBox)useBoxes.iterator().next()).getValue();
							assert vRet instanceof Constant || vRet instanceof Local;
							Variable varToReturn = new ReturnVar(vRet, n);
							assert varToReturn.isLocalOrConst();
							defVars.add(varToReturn);
							break; // there is no need to create more than 1 such variable
						}
					}
				}
			}
		}
		
		return defVars;
	}
	
	private Map<NodePoint,List<Variable>> cachePointToUsedVars = new HashMap<NodePoint, List<Variable>>();
	/** In addition to vars declared as used at point, includes a representative of returned var/const if it's a call site (if stored) and a representative of an actual argument if it's an id stmt. */
	private List<Variable> getAllUsedVars(NodePoint pntUse) {
		// retrieve from cache, if present
		List<Variable> usedVars = cachePointToUsedVars.get(pntUse);
		if (usedVars != null)
			return usedVars;
		// not in cache; compute and store in cache
		usedVars = new ArrayList<Variable>();
		cachePointToUsedVars.put(pntUse, usedVars);
		
		NodeDefUses nUse = (NodeDefUses) pntUse.getN();
		if (pntUse.getRhsPos() == NodePoint.PRE_RHS) {
//			// app call arguments that are locals or pseudo-locals constants that represent just-created string objects that are passed as parameters
//			CallSite cs = nUse.getCallSite();
//			if (cs != null) {
//				assert cs.hasAppCallees();
//				for (int i = 0; i < cs.getNumActualParams(); ++i) {
//					Value val = cs.getActualParam(i);
//					Variable argVar = (val instanceof Constant)? new CSArgVar((Constant)val, cs, i) : new StdVariable(val); // new CSArgVar(val, cs, i)
//					usedVars.add(argVar);
//				}
//			}
			usedVars.addAll(nUse.getUsedVars());
		}
		else {
			// start with a clone of the list of 'regular' used vars, if this is not a call site or has at least one lib call target
			CallSite cs = nUse.getCallSite();
			if (cs == null || cs.hasLibCallees())
				usedVars.addAll(nUse.getUsedVars()); // may include string constants that represent reference to just-created string obj, passed as parameter
			
			// then, add a representative returned var, if this is a call site and stores a returned value
			if (nUse.getStmt() instanceof AssignStmt && nUse.hasAppCallees()) {
//				SootMethod mFirstCallee = cs.getAppCallees().get(0);
//				if (!mFirstCallee.getReturnType().equals(VoidType.v())) {
//					CFG cfgFirstCallee = ProgramFlowGraph.inst().getCFG(mFirstCallee);
//					// search for first return statement and its returned val, to create a representative of all returned variables
//					for (CFGNode n : cfgFirstCallee.getNodes()) {
//						if (n.isInCatchBlock())
//							continue;
//						Stmt s = n.getStmt();
//						if (s instanceof ReturnStmt || s instanceof RetStmt) {
//							List<ValueBox> useBoxes = s.getUseBoxes();
//							if (!useBoxes.isEmpty()) {  // returns a value (i.e., it's not a simple 'return' stmt in 'void' method)
//								// there should be one and only one returned local/const
//								assert useBoxes.size() == 1;
//								Value vRet = ((ValueBox)useBoxes.iterator().next()).getValue();
//								assert vRet instanceof Constant || vRet instanceof Local;
//								Variable varReturned = new ReturnVar(vRet, n);
//								assert varReturned.isLocalOrConst();
//								usedVars.add(varReturned);
//							}
//							break; // representative found, so we stop here
//						}
//					}
//					assert usedVars.get(usedVars.size()-1) instanceof ReturnVar; // we should have found a representative
//				}
				// create a dummy representative of returned var
				usedVars.add(new ReturnVar(NullConstant.v(), nUse)); // use call site as representative return node
			}
			
			// finally, add a representative actual-argument var, if this is an identity stmt
			if (nUse.getStmt() instanceof IdentityStmt) {
				CFG cfgUse = ProgramFlowGraph.inst().getContainingCFG(nUse);
				assert cfgUse.getNodes().get(0) == cfgUse.ENTRY && cfgUse.getNodes().get(1).getStmt() instanceof IdentityStmt;
				final int argIdx = cfgUse.getNodes().indexOf(nUse) - 1; // first node is ENTRY, so we subtract 1
				assert argIdx >= 0;
				Variable argVar = new CSArgVar(NullConstant.v(), cfgUse.getCallerSites().get(0), argIdx);
				usedVars.add(argVar);
			}
		}
		
		assert usedVars.size() > 0;
		return usedVars;
	}
	
	/** Used to hold frame-id init probes, which are not inserted until the end of instrumentation (so they appear first). */
	private Map<SootMethod,List> mToFrameIdProbes = new HashMap<SootMethod, List>();
	private static final String FR_ID_COUNT = "<fridcount>";
	private static final String LOCAL_FR_ID = "<loc_frid>";
	private static final String LOCAL_NEXT_FR_ID = "<loc_nextfrid>";
	
	/** Creates on-demand global frame id counter in main method, and local at the beginning with curr frame id. */
	private Local getCreateAssignFrameIdLocal(SootMethod m) {
		// 1. Get/create global frame id counter (static field in main class)
		SootField fldFrameIdCounter = getCreateInitFrameIdCounter();
		
		// 2. Get/create local var that holds curr frame id
		Body b = m.retrieveActiveBody();
		Local lFrameId = UtilInstrum.getLocal(b, LOCAL_FR_ID);
		if (lFrameId == null) {
			PatchingChain pchain = b.getUnits();
			lFrameId = UtilInstrum.getCreateLocal(b, LOCAL_FR_ID, IntType.v());
			
			// at the beginning of method, assign field value, increment field
			// (for simplicity, curr frame id in local keeps incremented id; we don't decrement it after updating field)
			// (as a consequence, first id is 1)
			List probe = new ArrayList();
			Stmt sGetField = Jimple.v().newAssignStmt(lFrameId,
					Jimple.v().newStaticFieldRef(fldFrameIdCounter.makeRef()));
			probe.add(sGetField);
			Stmt sIncCounter = Jimple.v().newAssignStmt(lFrameId, Jimple.v().newAddExpr(lFrameId, IntConstant.v(1)));
			probe.add(sIncCounter);
			Stmt sUpdateField = Jimple.v().newAssignStmt(
					Jimple.v().newStaticFieldRef(fldFrameIdCounter.makeRef()), lFrameId);
			probe.add(sUpdateField);
			assert !mToFrameIdProbes.containsKey(m);
			mToFrameIdProbes.put(m, probe);
		}
		
		return lFrameId;
	}
	private void insertFrameIdProbes() {
		for (SootMethod m : mToFrameIdProbes.keySet()) {
			PatchingChain pchain = m.retrieveActiveBody().getUnits();
			InstrumManager.v().insertBeforeNoRedirect(pchain, mToFrameIdProbes.get(m), UtilInstrum.getFirstNonIdStmt(pchain));
		}
	}
	/** Assigns to local (returned) in probe the next frame id (i.e., current frame id plus one). */
	private Local getCreateAssignNextFrameId(SootMethod m, List probe) {
		// 1. Get/create global frame id counter (static field in main class)
		SootField fldFrameIdCounter = getCreateInitFrameIdCounter();
		
		// 2. Get/create local var that holds curr frame id
		Body b = m.retrieveActiveBody();
		PatchingChain pchain = b.getUnits();
		Local lNextFrameId = UtilInstrum.getCreateLocal(b, LOCAL_NEXT_FR_ID, IntType.v());
		
		// at probe, assign field value to local, increment local
		Stmt sGetField = Jimple.v().newAssignStmt(lNextFrameId,
				Jimple.v().newStaticFieldRef(fldFrameIdCounter.makeRef()));
		probe.add(sGetField);
		Stmt sIncCounter = Jimple.v().newAssignStmt(lNextFrameId, Jimple.v().newAddExpr(lNextFrameId, IntConstant.v(1)));
		probe.add(sIncCounter);
		
		return lNextFrameId;
	}
	
	/** Gets/creates global frame id counter (static field in main class) */
	private SootField getCreateInitFrameIdCounter() {
		// pick first entry method
		SootClass clsMain = ProgramFlowGraph.inst().getEntryMethods().get(0).getDeclaringClass();
		SootField fldFrameIdCounter = null;
		for (Iterator itFld = clsMain.getFields().iterator(); itFld.hasNext(); ) {
			SootField fld = (SootField)itFld.next();
			if (fld.getName().equals(FR_ID_COUNT)) {
				fldFrameIdCounter = fld;
				break;
			}
		}
		if (fldFrameIdCounter == null) {
			// create field in main class
			fldFrameIdCounter = new SootField(FR_ID_COUNT, IntType.v(), Modifier.STATIC | Modifier.PUBLIC);
			clsMain.addField(fldFrameIdCounter);
			// should start at 0, so no need to init at <clinit> in main class
		}
		
		return fldFrameIdCounter;
	}
	
	private static final String BASE_CLS = "<basecls>";
	private static final String INST_ARG = "<objinstarg>";
	/** If var is instance field or array element, gets base obj from statement and assigns it to local. Value is null otherwise. */
	private Value prepareObjArg(Variable var, CFGNode n, SootMethod m, List probe) {
		// determine var's base, if any (null if no base)
		Value vBase = var.getBaseLocal();
		if (var.isObject() && !var.isStrConstObj()) {
			// use reference to "class" object if it's a static field or call
			if (vBase == null) {
				SootClass cls = null;
				Value val = var.getValue();
				if (val instanceof StaticFieldRef)
					cls = ((StaticFieldRef)val).getFieldRef().declaringClass();
				else if (val instanceof StaticInvokeExpr)
					cls = ((StaticInvokeExpr)val).getMethod().getDeclaringClass();
				
				if (cls != null) {
					Value valClsFld = Jimple.v().newStaticFieldRef( clsReporter.getFieldByName("inst").makeRef() );
					vBase = UtilInstrum.getCreateLocal(m.retrieveActiveBody(), BASE_CLS, RefType.v("java.lang.Object"));
					Stmt sAssignCls = Jimple.v().newAssignStmt(vBase, valClsFld);
					probe.add(sAssignCls);
				}
			}
		}
		
		// add code to probe to include base as arg, if var has a base; base arg is null otherwise
		if (vBase != null) {
			Local lInstArg = UtilInstrum.getCreateLocal(m.retrieveActiveBody(), INST_ARG, RefType.v("java.lang.Object"));
			Stmt sCastToObj = Jimple.v().newAssignStmt(lInstArg, vBase);
//				Jimple.v().newCastExpr(vBase, RefType.v("java.lang.Object")));
			probe.add(sCastToObj);
			
			return lInstArg;
		}
		
		return NullConstant.v();
	}
	
	private static final String IDX_ARG = "<arridxarg>";
	/** If var is array elem, gets index value and assigns it to local in probe. Value is -1 otherwise. */
	private Value prepareIdxArg(Variable var, CFGNode n, SootMethod m, List probe) {
		if (var.isArrayRef()) {
			Value valIdx = ((ArrayRef)var.getValue()).getIndex();
			Local lIdxArg = UtilInstrum.getCreateLocal(m.retrieveActiveBody(), IDX_ARG, IntType.v());
			Stmt sCopyIndex = Jimple.v().newAssignStmt(lIdxArg, valIdx);
			probe.add(sCopyIndex);
			
			return lIdxArg;
		}
		
		return IntConstant.v(-1);
	}
	
	private static final int SPLIT_PROBE_COUNT_START_INDEX = 1;
	private static final String LOCAL_VARVALS_ARR_PREFIX = "<loc_varvals";
	private static final String LOCAL_VARVALS_ARR_SUFFIX = ">";
	private static final String LOCAL_VARVAL_OBJ = "<loc_varvalobj>";
	
	private String getObjArrLocalName(Stmt s) {
		Integer probeCount = splitProbes.get(s);
		if (probeCount == null)
			probeCount = SPLIT_PROBE_COUNT_START_INDEX;
		return LOCAL_VARVALS_ARR_PREFIX + probeCount + LOCAL_VARVALS_ARR_SUFFIX;
	}
	
	/**
	 * @param s Location of where the probe would be at. Only needed to determine local name's index suffix.
	 */
	private Local getCreateAssignVarValsArray(SootMethod m, Stmt s, List<Variable> vars, List probe) { //, boolean splitProbes) {
		Body b = m.retrieveActiveBody();
		final String arrName = getObjArrLocalName(s); //splitProbes? getNextVarValsArrName(m) : LOCAL_VARVALS_ARR;
		Local lVarValsArray = UtilInstrum.getCreateLocal(b, arrName, ArrayType.v(clsObject.getType(), 1));
		
		// handle case in which var array is null (i.e., reporting a pure kill)
		if (vars == null) {
			Stmt sCopyNullToArray = Jimple.v().newAssignStmt(lVarValsArray, NullConstant.v());
			probe.add(sCopyNullToArray);
			return lVarValsArray;
		}
		
		final boolean reportVarsDynSice = Options.reportVarsDynSlice();
		final int numVarsToReport = reportVarsDynSice? vars.size() : 0;
		
		// add to probe creation of array of objects
		Stmt sNewArr = Jimple.v().newAssignStmt(lVarValsArray, Jimple.v().newNewArrayExpr(clsObject.getType(), IntConstant.v(numVarsToReport)));
		probe.add(sNewArr);
		// for each var to read, box it and assign it to corresponding position in array
		for (int i = 0; i < numVarsToReport; ++i) {  //vars.size(); ++i) {
			Variable v = vars.get(i);
			// create array elem value for i^th var
			Value vVarVal = Jimple.v().newArrayRef(lVarValsArray, IntConstant.v(i));
			Local lBase;
			if (v.isObject() && (lBase = ((ObjVariable)v).getBaseLocal()) != null) {
				// copy extracted base (ref to object) to array slot
				Stmt sCopyValToArrayElem = Jimple.v().newAssignStmt(vVarVal, lBase);
				probe.add(sCopyValToArrayElem);
				
//				System.out.println("DEBUG: instr local " + lBase + " m " + m);
			}
			else {
				// const, local, field, array elem, obj w/o base local
				Local lObj = UtilInstrum.getCreateLocal(b, LOCAL_VARVAL_OBJ, clsObject.getType());
				// *** AVOID invoking a function accidentally; use NULL instead of object represented by invoke
				Value vFrom = (v.getValue() instanceof InvokeExpr)? NullConstant.v() : v.getValue();
				addCopyBoxOrCastStmt(m, probe, lObj, vFrom);
				Stmt sCopyValToArrayElem = Jimple.v().newAssignStmt(vVarVal, lObj);
//				Stmt sCopyValToArrayElem = Jimple.v().newAssignStmt(vVarVal, NullConstant.v());
				probe.add(sCopyValToArrayElem);
			}
		}
		
		return lVarValsArray;
	}
	
	private Map<SootMethod,Integer> mToCurrArrId = new HashMap<SootMethod, Integer>();
	private String getNextVarValsArrName(SootMethod m) {
		Integer currId = mToCurrArrId.get(m);
		if (currId == null) {
			currId = 0;
			mToCurrArrId.put(m, currId);
		}
		else
			mToCurrArrId.put(m, ++currId);
		return LOCAL_VARVALS_ARR_PREFIX + currId;
	}
	
	private static final String LOCAL_BOX_PREFIX = "<loc_box_";
	private static final String LOCAL_BOX_SUFFIX = ">";
	
	private void addCopyBoxOrCastStmt(SootMethod m, List probe, Local lObj, Value vFrom) {
		// box local into obj local, if primitive; just copy to obj local otherwise
		Type t = vFrom.getType();
		
		// for use in boxing ctor call or cast stmt, non-const non-local value must be copied to local first
		Value vFinalFrom;
		if (!(vFrom instanceof Constant || vFrom instanceof Local)) {
			// get/create local of appropriate type
			Local lValCopy = UtilInstrum.getCreateLocal(m.retrieveActiveBody(), LOCAL_BOX_PREFIX+t+LOCAL_BOX_SUFFIX, t);
			Stmt sCopyToLocal = Jimple.v().newAssignStmt(lValCopy, vFrom);
			probe.add(sCopyToLocal);
			vFinalFrom = lValCopy; // replace vFrom
		}
		else
			vFinalFrom = vFrom;
		
		if (t instanceof PrimType) {
			Pair<RefType,SootMethod> refTypeAndCtor = dua.util.Util.getBoxingTypeAndCtor((PrimType)t);
			Stmt sNewBox = Jimple.v().newAssignStmt(lObj, Jimple.v().newNewExpr(refTypeAndCtor.first()));
			Stmt sInitBox = Jimple.v().newInvokeStmt(Jimple.v().newSpecialInvokeExpr(lObj, refTypeAndCtor.second().makeRef(), vFinalFrom));
			probe.add(sNewBox);
//			Stmt sInitBox = Jimple.v().newAssignStmt(lObj, NullConstant.v());
			probe.add(sInitBox);
		}
		else {
			if (!(t instanceof RefLikeType))
				System.out.println("Dbg warning: " + t + " not ref-like or primitive type");
//			assert t instanceof RefLikeType;
			
			Stmt sCopyRef = Jimple.v().newAssignStmt(lObj, vFinalFrom);//Jimple.v().newCastExpr(vFinalFrom, clsObject.getType()));
//			Stmt sCopyRef;
//			if (m.toString().equals("<net.n3.nanoxml.XMLElement: void <init>(java.lang.String,java.lang.String,java.lang.String,int)>"))
//				sCopyRef = Jimple.v().newAssignStmt(lObj, m.retrieveActiveBody().getLocals().getFirst()); // DEBUG;
//			else
//				sCopyRef = Jimple.v().newAssignStmt(lObj, NullConstant.v()); // DEBUG
			probe.add(sCopyRef);
		}
	}
	
}
