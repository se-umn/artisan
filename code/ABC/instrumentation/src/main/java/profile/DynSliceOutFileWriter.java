package profile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import soot.Local;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.SpecialInvokeExpr;
import dua.global.dep.DependenceFinder.ControlDependence;
import dua.global.dep.DependenceFinder.DataDependence;
import dua.global.dep.DependenceFinder.Dependence;
import dua.global.dep.DependenceFinder.NodePoint;
import dua.global.dep.DependenceGraph;
import dua.method.CFGDefUses.Variable;

/** slice.out:  -- OLD! --
    first section: vars from data dependencies and other uses ("inputs"), ordered by id
       var: type (loc/fld/arrel/obj),
            idx in locals box / field name / arrel idx / nothing (base IS object)
    second section: dependencies, ordered by id
       dep: type (du/br), src node, tgt node (if du), var id (if du)
    one section per dep graph: starting dependencies, dependencies that are linked in graph */
public class DynSliceOutFileWriter {
	private final boolean includeVals;
	private final boolean includeChangeCov;
	private final boolean includeBrCov;
	private final boolean includeDUCov;
	private final int eventLimit;
	private final int numVars;
	private final List<Dependence> allDeps;
	private final List<DependenceGraph> depGraphs;
	private final Map<Variable,Integer> cacheAllVarsToIds;
	
	public DynSliceOutFileWriter(boolean includeVals, boolean includeChangeCov, boolean includeBrCov, boolean includeDUCov, int eventLimit, int numVars,
			List<Dependence> allDeps, List<DependenceGraph> depGraphs, Map<Variable,Integer> cacheAllVarsToIds)
	{
		this.includeVals = includeVals; this.includeChangeCov = includeChangeCov; this.includeBrCov = includeBrCov; this.includeDUCov = includeDUCov; this.eventLimit = eventLimit; 
		this.numVars = numVars;
		this.allDeps = allDeps; this.depGraphs = depGraphs; this.cacheAllVarsToIds = cacheAllVarsToIds;
	}
	
	public void printOutFile() {
		// DEBUG
		final int MAX_DOT_COL = 150;
		final int ELEMS_PER_DOT = 100;
		int elemsLeft = ELEMS_PER_DOT;
		int dotsLeft = MAX_DOT_COL;
		
		System.out.println("START printing dynsl out file " + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
		
		// slice.out:
		// first section: vars from data dependencies and other uses ("inputs"), ordered by id
		//    var: type (loc/fld/arrel/obj),
		//         idx in locals box / field name / arrel idx / nothing (base IS object)
		// second section: dependencies, ordered by id
		//    dep: type (du/br), src node, tgt node (if du), var id (if du)
		// one section per dep graph: starting dependencies, dependencies that are linked in graph
		try {
			File fOut = new File(dua.util.Util.getCreateBaseOutPath() + "slice.out");
			BufferedWriter writer = new BufferedWriter(new FileWriter(fOut));
			
			// new section before 'first': whether values are monitored or not
			writer.write("VALUES:" + (includeVals? "YES" : "NO") + " BR_COV:" + (includeBrCov? "YES" : "NO") +
					" DU_COV:" + (includeDUCov? "YES" : "NO") + " CHANGE_COV:" + (includeChangeCov? "YES" : "NO") + " EV_LIMIT:" + eventLimit + "\n");
			
			// first section: vars from data dependencies and other uses ("inputs"), ordered by id
			//    var: type (loc/fld/arrel/obj),
			//         idx in locals box / field name / arrel idx / nothing (base IS object)
			writer.write("VARIABLES: " + numVars + "\n");
			
			// second section: dependencies, ordered by id
			//    dep: type (du/br), src node, tgt node (if du), var id (if du)
			writer.write("DEPENDENCIES: " + allDeps.size() + "\n");
			int depId = 0;
			Map<Dependence, Integer> depToId = new HashMap<Dependence, Integer>(); // to fill for next part
			for (Dependence dep : allDeps) {
				depToId.put(dep, depId);
				writer.write(" " + depId++ + " " + dep.toStringNoVarOrBrId());
				if (dep instanceof DataDependence)  // include varId for data deps
					writer.write(" " + cacheAllVarsToIds.get(((DataDependence)dep).getVar()));
				else
					writer.write(" " + ((ControlDependence)dep).getBrId());  // the "var" for a cd is the branch id (decision index 0..NumDec-1)
				writer.write("\n");
			}
			
			// remaining sections, one section per dep graph:
			//    starting dependencies, dependencies that are linked in graph
			for (DependenceGraph depGraph : depGraphs) {
				// set of visited deps and worklist of deps to visit
				BitSet visited = new BitSet();
				BitSet toVisit = new BitSet();
				boolean toVisitStart = true;
				NodePoint pntStart = depGraph.getStart();
				writer.write("DEP GRAPH id " + depGraph.getStart() + "\n");
				
				//int prevDepId = -1;
				while (toVisitStart || !toVisit.isEmpty()) {
					// DEBUG
					if (--elemsLeft == 0) {
						System.out.print('.');
						elemsLeft = ELEMS_PER_DOT;
						if (--dotsLeft == 0) {
							dotsLeft = MAX_DOT_COL;
							System.out.println();
						}
					}
					
					// extract next dep to visit
					Integer currDepId;
					if (toVisitStart) {
						toVisitStart = false;
						currDepId = -1; // special value representing start
					}
					else {
						currDepId = toVisit.nextSetBit(0); //prevDepId + 1); // preserves sorted property
						toVisit.set(currDepId, false);
						//prevDepId = currDepId;
					}
					if (currDepId >= 0)
						visited.set(currDepId);
					
					NodePoint pntTgt;
					boolean isPrevControl;
					if (currDepId == -1) {
						pntTgt = pntStart;
						isPrevControl = false;
					}
					else {
						Dependence depPrev = allDeps.get(currDepId);
						pntTgt = depPrev.getTgt();
						isPrevControl = depPrev instanceof ControlDependence;
					}
					
					writer.write(" " + currDepId + " ->"); // source of links
					
					// create sorted list of out deps to ensure deterministic output
					List<Dependence> outDepsListSorted = new ArrayList<Dependence>( depGraph.getOutDeps(pntTgt) );
					Collections.sort(outDepsListSorted, new Dependence.DepComp(depToId) );
					for (Dependence depOut : outDepsListSorted) {
						final int outDepId = depToId.get(depOut);
						
						// filter out pseudo-cd links when not succeeding another cd
						if (depOut instanceof ControlDependence) {
							// ignore single-edge interproc CD unless previous dep is CD
							ControlDependence cdDepOut = (ControlDependence)depOut;
							final int numAppCallees = cdDepOut.getNumAppCallees();
							final int numLibCallees = cdDepOut.getNumLibCallees();
							if (!isPrevControl && numAppCallees > 0) {
								if (numAppCallees == 1 && numLibCallees == 0) {
									System.out.println("DEBUG: skipped dep link " + currDepId + "->" + outDepId +
											": app callees = 1 and prev dep is " + currDepId); // DEBUG
									continue; // definitely no link between prev data dep and this pseudo-cd
								}
								
								// there is a link btw prev data dep and this cd only if data dep targets base var of virtual call
								InvokeExpr invExpr = depOut.getSrc().getN().getStmt().getInvokeExpr();
								if (invExpr instanceof InstanceInvokeExpr) {
									assert !(invExpr instanceof SpecialInvokeExpr); // NOTE: not a SpecialInvoke because of check above
									// defined var MUST be local if dep targets base
									DataDependence depPrev = (DataDependence) allDeps.get(currDepId);
									if (!depPrev.getVar().isLocal()) {
										System.out.println("DEBUG: skipped dep link " + currDepId + "->" + outDepId +
												": prev dep var not local (" + depPrev.getVar() + ") - prev dep is " + depPrev); // DEBUG
										continue;
									}
									Local lBase = (Local) depPrev.getVar().getValue();
									if (lBase != ((InstanceInvokeExpr)invExpr).getBase()) {
										System.out.println("DEBUG: skipped dep link " + currDepId + "->" + outDepId +
												": prev dep var is local but not base (" + depPrev.getVar() + " vs " + lBase + ")"); // DEBUG
										continue;
									}
								}
							}
						}
						
						writer.write(" " + outDepId); // print link from prev dep to next dep
						
						// ensure out dep is now in line for a visit, if not in line or visited yet
						if (!visited.get(outDepId)) { // && !toVisit.get(outDepId)) {
							//assert outDepId > currDepId; // sanity check: deps are visited in order // *** Actually, we can't easily guarantee order, but at least we guarantee determinism
							toVisit.set(outDepId); // ensure this out dep is going to be visited
						}
					}
					writer.write("\n");
				}
			}
			
			writer.flush();
			writer.close();
		}
		catch (FileNotFoundException e) { System.err.println("Couldn't write dyn-slice file:" + e); }
		catch (SecurityException e) { System.err.println("Couldn't write dyn-slice file:" + e); }
		catch (IOException e) { System.err.println("Couldn't write dyn-slice file:" + e); }
		
		// DEBUG
		System.out.println();
		System.out.println("END printing dynsl out file " + (new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date())));
	}
	
}
