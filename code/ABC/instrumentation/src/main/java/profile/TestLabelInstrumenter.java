package profile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import soot.Body;
import soot.Local;
import soot.PatchingChain;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import dua.Options;

/** Inserts at the beginning of each 'test*' method code to print a string identifying test case.
 *  Excludes "entry" method. */
public class TestLabelInstrumenter {
	
	private static Set<SootMethod> instrumentedAlready = new HashSet<SootMethod>();
	
	public static void instrument(List<SootMethod> entryMethods) {
		List<Stmt> probe = new ArrayList<Stmt>();
		
		// get access to System.out field
		SootClass clsCommonReporter = Scene.v().getSootClass("profile.CommonReporter");
		SootMethod mReportTestName = clsCommonReporter.getMethodByName("reportTestName");
		
		SootMethod mEntryToExclude = (Options.entryClassName() == null)? null : entryMethods.get(0);
		for (SootMethod m : entryMethods) {
			// ensure method is instrumented only once
			if (instrumentedAlready.contains(m))
				continue;
			instrumentedAlready.add(m);
			
			if (m == mEntryToExclude)
				continue;
			
			Body b = m.retrieveActiveBody();
			PatchingChain<Unit> pchain = b.getUnits();
			probe.clear();
			
			Stmt sInsertLocation = (Stmt) UtilInstrum.getFirstNonIdStmt(pchain);
			
			final String testName = m.getName() + "(" + m.getDeclaringClass().getName() + ")";
			
			Local lName = UtilInstrum.getCreateLocal(b, "<m_name>", Scene.v().getSootClass("java.lang.String").getType());
			List args = new ArrayList();
			args.add(lName);
			Stmt sAssignNameToLocal = Jimple.v().newAssignStmt(lName, StringConstant.v(testName));
			probe.add(sAssignNameToLocal);
			
			Stmt sCallTestNameReporter = Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(mReportTestName.makeRef(), args));
			probe.add(sCallTestNameReporter);
			
			InstrumManager.v().insertRightBeforeNoRedirect(pchain, probe, sInsertLocation);
		}
	}
	
}
