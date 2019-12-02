package dua.global;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.xmlpull.v1.XmlPullParserException;

import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.jimple.infoflow.android.SetupApplication;
import soot.jimple.infoflow.android.manifest.ProcessManifest;
import soot.jimple.infoflow.android.source.AndroidSourceSinkManager.LayoutMatchingMode;
import soot.jimple.infoflow.data.pathBuilders.DefaultPathBuilderFactory.PathBuilder;
import dua.Options;
import dua.cls.ClassTag;
import dua.method.CFG;
import dua.method.CFGFactory;
import dua.method.MethodTag;
import dua.method.CFG.CFGNode;
import dua.unit.StmtTag;

/**
 * Available as a singleton for convenient access from everywhere.
 * Keeps collection of CFGs for all methods, connected interprocedurally.
 * Also keeps track of Soot methods, entry method, and reachable Soot methods. These methods are used to build the CFGs.
 * CFG building is parameterized: client provides a factory object.
 */
public class ProgramFlowGraph {
	public static class EntryNotFoundException extends Exception {
		public EntryNotFoundException(String msg) { super(msg); }
	}
	
	/** Comparator to find unique ordering of methods based on lexical order */
	private static class MethodComparator implements Comparator<SootMethod> {
		public int compare(SootMethod o1, SootMethod o2) {
			return o1.toString().compareTo(o2.toString());
		}
	}
	
	/** Holds singleton instance */
	private static ProgramFlowGraph pfgSingleton = null;
	/** Returns singleton instance */
	public static ProgramFlowGraph inst() { return pfgSingleton; }
	
	//
	// Regular fields
	
	private Map<SootMethod, CFG> mCFGs = new HashMap<SootMethod, CFG>();
	/** All application classes (at Soot level), reachable or not */
	private List<SootClass> appClasses = null;
	private List<SootClass> userClasses = null;
	/** All application concrete methods, reachable or not */
	public List<SootMethod> allAppMethods = null;
	/** Methods reachable from entry, including clinits. INCLUDES methods called from catch blocks. */
	private List<SootMethod> reachableAppMethods = new ArrayList<SootMethod>();
	private List<SootMethod> entryMethods = null;
	/** Unique index for each method (same order for any execution) */
	private Map<SootMethod,Integer> mToId = new HashMap<SootMethod, Integer>();
	/** Ordered CFGs */
	private List<CFG> reachableCfgs = new ArrayList<CFG>();
	
	/** Only maps reachable stmts and methods */
	private HashMap<Stmt,SootMethod> stmtToMethod = null; // built on demand
	/** Only maps reachable nodes to CFGs */
	private HashMap<CFGNode,CFG> nodeToCFG = null; // built on demand
	
	public CFG getCFG(SootMethod m) { return mCFGs.get(m); }
	public Map<SootMethod, CFG> getMethodToCFGMap() { return mCFGs; }
	public List<SootMethod> getReachableAppMethods() { return reachableAppMethods; }
	public List<CFG> getCFGs() { return reachableCfgs; }
	public List<SootMethod> getEntryMethods() { return entryMethods; }
	public CFG getMethodCFG(SootMethod m) { return  mCFGs.get(m); }
	
	
	public static ProcessManifest processMan = null;
	public static String appPackageName="";
	static {
		if (dua.Options.analyzeAndroid) {
			try {
				processMan = new ProcessManifest(soot.options.Options.v().process_dir().get(0));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				e.printStackTrace();
			}
		}
		if (dua.Options.analyzeAndroid && null != processMan) {
			appPackageName = processMan.getPackageName();				
		}
	}
	
	/** Builds singleton instance with given parameters. Allows only one instantiation. */
	public static void createInstance(CFGFactory cfgFactory) throws EntryNotFoundException {
		if (pfgSingleton != null)
			throw new RuntimeException("Instance of ProgramFlowGraph already exists! Can't instantiate more than once...");
		
		// construct in two steps, so CFG construction has method info available
		pfgSingleton = new ProgramFlowGraph();
		pfgSingleton.initMethods();
		pfgSingleton.initCFGs(cfgFactory);
	}
	
	private static final String mainSubsig = "void main(java.lang.String[])";
	private List<SootMethod> findEntryAppMethods() throws EntryNotFoundException {
		if (entryMethods != null)
			return entryMethods;
		entryMethods = new ArrayList<SootMethod>();
		
		// get main class, and test classes
		String entryClassName = Options.entryClassName();
		List<String> entryTestClasses = Options.entryTestClasses();
		
		// search for all app class with a 'main' if no entry or test classes were provided
		if (entryTestClasses == null && entryClassName == null) {
			for (SootClass cls : ProgramFlowGraph.inst().getAppClasses()) {
				try {
					entryMethods.add(cls.getMethod(mainSubsig));
//					break;
				}
				catch (Exception e) { }
			}
			if (entryMethods.isEmpty()) {
				throw new EntryNotFoundException("No 'main' found in app classes");
			}
		}
		
		// given entry class's 'main' is the first entry method in list
		if (entryClassName != null) {
			// find entry class matching given name
			for (SootClass cls : ProgramFlowGraph.inst().getAppClasses()) {
				if (cls.getName().equals(entryClassName)) {
					// find main method in entry class
					try {
						SootMethod mEntry = cls.getMethod(mainSubsig);
						entryMethods.add(mEntry);
						break;
					}
					catch (Exception e) {
						throw new EntryNotFoundException("Entry class " + entryClassName + " has no 'main'");
					}
				}
			}
			if (entryMethods.isEmpty())
				throw new EntryNotFoundException("Entry class name " + entryClassName + " not found");
		}
		
		// finally, add all 'test*' methods in test classes to entry-methods list
		if (entryTestClasses != null) {
			for (SootClass cls : ProgramFlowGraph.inst().getAppClasses()) {
				if (entryTestClasses.contains(cls.getName())) {
					// find all test* methods inside class
					for (Iterator itM = cls.getMethods().iterator(); itM.hasNext(); ) {
						SootMethod m = (SootMethod) itM.next();
						if (m.getName().startsWith("test"))
							entryMethods.add(m);
					}
				}
			}
 		}
		
		assert !entryMethods.isEmpty();
		return entryMethods;
	}
	
	public int getMethodIdx(SootMethod m) { return mToId.get(m); }
	public int getCFGIdx(CFG cfg) { return getMethodIdx(cfg.getMethod()); }

	public static final String[] libClasses = {
	//"android.",
	"com.android.internal",
    //"dalvik.",
    //"java.",
    //"javax.",
    //"junit.",
    //"org.apache.",
    //"org.json.",
    //"org.w3c.",
    //"org.xml.",
    //"org.xmlpull.",
    //"java.lang.",
    //"android.os.",
    //"com.google.",
    "org.bouncycastle.",
    "org.codehaus.",
    "com.flurry.",
    "com.actionbarsherlock.",
    "com.burstly.lib.",
    "com.chartboost.sdk.",
    "com.comscore.",
    "com.inmobi.",
    "com.mobclix.android.",
    "oauth.signpost.",
    "org.acra.",
    //"com.amazon.",
    //"com.amazonaws.",
    //"com.android.vending.",
    "com.millennialmedia.",
    "com.tapjoy.",
    "com.mopub.mobileads.",
    "com.viewpagerindicator.",
    "com.adwhirl.",
    "com.urbanairship.",
    //"org.slf4j.",
    "com.jumptap.adtag.",
    "com.crittercism.",
    "com.applovin.",
    "com.greystripe.",
    "org.springframework.",
    "com.unity3d.player.",
    "com.urbanairship.",
    "com.admarvel.",
    "com.admob.",
    "mediba.ad.sdk.",
    "com.adobe.air."};
	
	static boolean isLibAPI(String clsname) {
		for (String prefix : libClasses) {
			if (clsname.startsWith(prefix)) return true;
		}
		return false;
	}
	
	/** Gets/creates collection of all application classes */
	public List<SootClass> getAppClasses() {
		if (appClasses == null) {
			appClasses = new ArrayList<SootClass>();
			
			for (Iterator itCls = Scene.v().getApplicationClasses().snapshotIterator(); itCls.hasNext(); ) {
				// speculatively skip non-app classes
				SootClass curcls = (SootClass) itCls.next();
				if (dua.Options.analyzeAndroid) {
					if ( !curcls.getName().contains(appPackageName) ) {
						//curcls.setLibraryClass();
						//curcls.setPhantomClass();
						//continue;
					}
					if (curcls.isInnerClass() || curcls.isInterface() || curcls.isPhantom() || curcls.isAbstract() ) continue;
					//if (curcls.getName().startsWith("com.flurry")) continue;
					if (isLibAPI(curcls.getName())) {
						continue;
					}
				}
				appClasses.add(curcls);

			}
		}
		
		return appClasses;
	}

	/** Gets/creates collection of all classes defined in user code --- classes having name prefix matching the package name of the APK */
	public List<SootClass> getUserClasses() {
		if (userClasses == null) {
			userClasses = new ArrayList<SootClass>();
			
			for (Iterator itCls = Scene.v().getApplicationClasses().snapshotIterator(); itCls.hasNext(); ) {
				// speculatively skip non-app classes
				SootClass curcls = (SootClass) itCls.next();
				if (dua.Options.analyzeAndroid) {
					if ( !curcls.getName().contains(appPackageName) ) {
						continue;
					}
				}
				userClasses.add(curcls);

			}
		}
		
		return userClasses;
	}
	
	/** Gets/creates collection of all concrete methods (i.e., methods with a body) in application classes */
	public List<SootMethod> getAppConcreteMethods() {
		if (allAppMethods == null) {
			allAppMethods = new ArrayList<SootMethod>();
			List<SootClass> appClasses = getAppClasses();
			for (SootClass cls : appClasses) {
				for (Iterator itMthd = cls.getMethods().iterator(); itMthd.hasNext(); ) {
					SootMethod m = (SootMethod) itMthd.next();
					//if (!m.isAbstract() && m.toString().indexOf(": java.lang.Class class$") == -1)
					if (!m.isAbstract() && m.isConcrete() && m.toString().indexOf(": java.lang.Class class$") == -1) // changed by hcai
						allAppMethods.add(m);
				}
			}
			
			// sort list of all methods
			Collections.sort(allAppMethods, new MethodComparator());
		}
		
		return allAppMethods;
	}
	
	/** create a dummy main method to model the life cycle of an android app */
	private List<SootMethod> createDummyMain() 
	{
		if (entryMethods != null)
			return entryMethods;
		entryMethods = new ArrayList<SootMethod>();
		
		if (!dua.Options.modelAndroidLC) {
			return entryMethods;
		}
		
		System.err.println("no entry method is found in any app classes --- try modeling the life cycle and creating dummy main.");
		/** added for analyzing android apps */
		Scene.v().addBasicClass("java.io.PrintStream",SootClass.SIGNATURES);
		Scene.v().addBasicClass("java.lang.System",SootClass.SIGNATURES);
			
		// model android life cycle via a dummy main method --- duaf would not proceed without an entry class found
		try {
			for (String apk : dua.Options.apks) {
//				ProcessManifest processMan = new ProcessManifest(apk);
//				String appPackageName = processMan.getPackageName();
//				Set<String> entrypoints = processMan.getEntryPointClasses();
//				
//				System.out.println("package name of the android app being analyzed: " + appPackageName);
//				System.out.println("entry points of the android app being analyzed: " + entrypoints);
				
				//String curdir = ProgramFlowGraph.class.getProtectionDomain().getCodeSource().getLocation().getFile();
				//File f = new File(".");
				//String curdir = f.getAbsolutePath();
				System.out.println("modeling life cycle of android app " + apk + " ...");
				
				// THIS IS HARDCODED ! 
				File androidJar = new File("/Users/gambi/Library/Android/sdk/platforms/android-19/android.jar");
				
				SetupApplication setapp = new SetupApplication( androidJar.getAbsolutePath(), apk);
				
				setapp.getConfig().setEnableCallbacks(true);
				setapp.getConfig().setEnableStaticFieldTracking(false);
				setapp.getConfig().setLayoutMatchingMode(LayoutMatchingMode.NoMatch);
				setapp.getConfig().setAccessPathLength(1);
				setapp.getConfig().setFlowSensitiveAliasing(false);
				setapp.getConfig().setComputeResultPaths(false);
				setapp.getConfig().setPathBuilder(PathBuilder.ContextInsensitiveSourceFinder);
				setapp.getConfig().setEnableArraySizeTainting(false);
				
				File sourcesAndSinks = new File("SourcesAndSinks.txt");
				
				setapp.calculateSourcesSinksEntrypoints(sourcesAndSinks.getAbsolutePath());
				
//				soot.G.reset();
				soot.options.Options.v().set_no_bodies_for_excluded(true);
				soot.options.Options.v().set_allow_phantom_refs(true);
				soot.options.Options.v().set_whole_program(true);
				soot.Main.v().autoSetOptions();
				Scene.v().loadNecessaryClasses();
				
				//Scene.v().addBasicClass(android.hardware.Sensor,HIERARCHY);
				
				SootMethod dummymain = setapp.getEntryPointCreator().createDummyMain();
				entryMethods.addAll(Scene.v().getEntryPoints());
				entryMethods.add(dummymain);
				Scene.v().setEntryPoints(entryMethods);
				System.out.println("created entry points: " + entryMethods);
			}
		} 
		catch (Exception e) {
			e.printStackTrace(System.out);
			//System.exit(-1);;
			System.err.println("Failed to create the dummy main method for the android app being analyzed.");
		}
		
		return entryMethods;
	}
	
	private void initMethods() throws EntryNotFoundException {
		List<SootMethod> appMethods = getAppConcreteMethods();
		List<SootMethod> entryMethods = ((dua.Options.analyzeAndroid)? createDummyMain() : findEntryAppMethods());
		
		this.entryMethods = entryMethods;
		
		// purely intraprocedural initialization
		// for all application Soot classes:
		//   find contextual defs and uses for each Stmt in each Method
		//   determine local kill summary for each Method
		// for each Soot Method: list defs, uses and kills
		createAppTags();
		
		// initialization requiring previously computed intra-procedural info
		for (SootMethod m : appMethods) {
			// compute initial (local) reaching ctx calls in method
			MethodTag mTag = (MethodTag) m.getTag(MethodTag.TAG_NAME);
			mTag.initCallSites();
		}
		
		// Once method tags and call sites are created, find subset of methods that are reachable from entry
		findReachableAppMethods(entryMethods);
		
		// Fill index map for reachable app methods
		int mIdx = 0;
		for (SootMethod m : reachableAppMethods)
			mToId.put(m, mIdx++);
	}
	private void initCFGs(CFGFactory cfgFactory) {
		// Create CFG for each method using provided factory
		for (SootMethod m : reachableAppMethods) {
			// compute CFG with defs, uses, and reachable uses
			CFG cfg = cfgFactory.createCFG(m);
			mCFGs.put(m, cfg);
			reachableCfgs.add(cfg);
		}
		// Use factory to perform additional required analysis for each CFG
		for (CFG cfg : reachableCfgs)
			cfg.analyze();
		
		System.out.println("Total reachable concrete methods: " + reachableCfgs.size());
	}
	
	private void createAppTags() {
		// create class tags
		for (SootClass c : ProgramFlowGraph.inst().getAppClasses()) {
			c.addTag(new ClassTag());
		}
		
		// create method tags
		for (SootMethod m : ProgramFlowGraph.inst().getAppConcreteMethods())
			m.addTag(new MethodTag(m));
	}
	
	/**
	 * Finds and stores reachable methods from given entry.
	 * Also determines index for each method, in some execution-invariant ordering.
	 * 
	 * TODO: add <clinit> methods (implicitly called)
	 */
	private void findReachableAppMethods(List<SootMethod> mEntries) {
		// 0. All app methods are reachable if no reachability is to be computed
		if (!Options.reachability()) {
			reachableAppMethods = allAppMethods;
			return;
		}
		
		// 1. Find reachable methods
		//    prepare sets for method reachability algorithm
		Set<SootMethod> reachableSet = new HashSet<SootMethod>();
		Set<SootMethod> toVisit = new HashSet<SootMethod>();
		toVisit.addAll(mEntries); // seed to-visit list with entry method
		
		// visit methods, storing them as reachable, until no more transitively reachable (called) methods are available
		Set<SootClass> processedClinitClasses = new HashSet<SootClass>();
		while (!toVisit.isEmpty()) {
			// remove one element from to-visit list and mark it as 'reachable'
			SootMethod m = toVisit.iterator().next();
			toVisit.remove(m);
			reachableSet.add(m);
			
			// add all callees to to-visit list
			PatchingChain pchain = m.retrieveActiveBody().getUnits();
			for (Iterator it = pchain.iterator(); it.hasNext(); ) {
				Stmt s = (Stmt) it.next();
				StmtTag sTag = (StmtTag) s.getTag(StmtTag.TAG_NAME);
				// if (sTag.isInCatchBlock()) // commented out and changed to the below by hcai
				if (dua.Options.ignoreCatchBlocks && sTag.isInCatchBlock())	
					continue; // skip catch blocks, FOR NOW
				if (sTag.hasAppCallees()) {
					for (SootMethod mCallee : sTag.getAppCallees()) {
						if (!reachableSet.contains(mCallee))
							toVisit.add(mCallee);
					}
				}
			}
			
			// add <clinit> for class containing m to toVisit worklist, if not processed yet
			try {
				SootClass cls = m.getDeclaringClass();
				if (!processedClinitClasses.contains(cls)) {  // add to queue if not processed uet
					SootMethod mClinit = cls.getMethodByName("<clinit>");
					toVisit.add(mClinit);
					processedClinitClasses.add(cls);
				}
			}
			catch (RuntimeException e) {} // exception thrown if clinit not found in class
		}
		
		// copy set into list and order it
		reachableAppMethods.addAll(reachableSet);
		Collections.sort(reachableAppMethods, new MethodComparator());
	}
	
	public SootMethod getContainingMethod(Stmt s) {
		// build map on demand
		if (stmtToMethod == null) {
			stmtToMethod = new HashMap<Stmt, SootMethod>();
			for (SootMethod m : reachableAppMethods)
				for (Iterator itS = m.retrieveActiveBody().getUnits().iterator(); itS.hasNext(); )
					stmtToMethod.put((Stmt)itS.next(), m);
		}
		
		return stmtToMethod.get(s);
	}
	/** Returns soot method for node. Returns special value null if node is null (a "non-method" for a "non-node"). */
	public SootMethod getContainingMethod(CFGNode n) {
		return (n != null)? getContainingMethod(n.getStmt()) : null;
	}
	
	public int getContainingMethodIdx(Stmt s) {
		return getMethodIdx(getContainingMethod(s));
	}
	public int getContainingMethodIdx(CFGNode n) {
		return getMethodIdx(getContainingCFG(n).getMethod());
	}
	
	public CFG getContainingCFG(Stmt s) {
		SootMethod m = getContainingMethod(s);
		return getMethodCFG(m);
	}
	public CFG getContainingCFG(CFGNode n) {
		// build map on demand
		if (nodeToCFG == null) {
			nodeToCFG = new HashMap<CFGNode, CFG>();
			for (CFG cfg : reachableCfgs) {
				for (CFGNode _n : cfg.getNodes())
					nodeToCFG.put(_n,cfg);
			}
		}
		
		return nodeToCFG.get(n);
	}
	public CFGNode getNode(Stmt s) {
		return getContainingCFG(s).getNode(s);
	}
	
}
