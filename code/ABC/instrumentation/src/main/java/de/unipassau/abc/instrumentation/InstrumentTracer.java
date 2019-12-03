package de.unipassau.abc.instrumentation;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;

import de.unipassau.abc.ABCUtils;
import de.unipassau.abc.data.MethodInvocationMatcher;
import soot.PackManager;
import soot.Scene;
import soot.SootMethod;
import soot.Transform;
import soot.options.Options;

/**
 * TODO: handling array store/load as actions handling assignment/read to public
 * fields as actions
 * 
 * @author gambi
 *
 */
public class InstrumentTracer {

	private static final Logger logger = LoggerFactory.getLogger(InstrumentTracer.class);
	private static final List<Pattern> pureMethods = new ArrayList<Pattern>();

	public interface InstrumentTracerCLI {
		@Option(longName = "project-jar")
		List<File> getProjectJar();

		@Option(longName = "output-to", defaultValue = "./sootOutput")
		File getOutputDir();

		@Option(longName = "output-type", defaultValue = "class") // class,
																	// jimple
		String getOutputType();

		// Those values are space separated
		@Option(longName = "exclude", defaultToNull = true)
		List<String> getExcludes();

		@Option(longName = "include", defaultToNull = true)
		List<String> getIncludes();

		// This identifies the classes/packages that define the boundary of the
		// carving. Methods in this list
		// will be not instrumented, i.e., soot will not go inside them.
		// What do we need external for here ?
		@Option(longName = "external", defaultToNull = true)
		List<String> getExternalInterfaces();

		// Some calls can be omitted by the tracing since are considered not to
		// have side effects
		@Option(longName = "pure-methods", defaultToNull = true)
		List<String> getPureMethods();

	}

	public static void main(String[] args) throws URISyntaxException {
		List<File> projectJars = new ArrayList<>();
		File outputDir = null;
		String outputType = null;
		// This contains a list of package? classes? not sure that tell soot
		// which classes it MUST not instrument.
		List<String> sootExclude = new ArrayList<>();
		{
			// TODO - this is for XML DUMP. Ideally we should shade those
			// dependencies
			// sootExclude .add("com.thoughtworks.xstream.*");
			sootExclude.add("de.unipassau.abc.*");
			// TODO Shall we also exclude loggers and such ? Again Shaded deps ?
			// NOTE THAT SOOT COMES WITH ITS OWN DEPS IN THE JAR !
			///// Sure about this ?
			// sootExclude.add("java.io.*");
			// sootExclude.add("java.nio.*");
		}
		List<String> sootInclude = new ArrayList<>();
		{
			// No default include
		}
		List<String> externalInterfaces = new ArrayList<>();
		{
			// External interfaces are not instrumented by soot
			// externalInterfaces.add("java.util.Scanner");
		}

		// Default pure methods. Pure methods ARE not tracked !!
		{
			// m.getDeclaringClass().getName().equals("java.lang.StringBuilder")
			// ||
			// m.getDeclaringClass().getName().equals("java.util.Scanner") || //

//			pureMethods.add(Pattern.compile(Pattern.quote("<java.lang.Object: void <init>()>")));
			// pureMethods.add(Pattern.compile("<java.io.PrintStream: void
			// println\\(.*>"));

			//
			// pureMethods.add(Pattern.compile("<java.nio.file.Path: File
			// toFile()>"));

			// We treat Strings as primitives, i.e., we pass around them by
			// value. This means that we can omit to track calls to to
			// StringBuilder ... (I hope so! -> not really, the moment those are
			// parameters of any carved invocation this will fail !
			// pureMethods.add(Pattern.compile("<java.lang.StringBuilder: .*"));
			// Boxed type and Strings "cannot" be modified
			// pureMethods.add(Pattern.compile("<java.lang.String: .*"));
			///
			// pureMethods.add(Pattern.compile("<java.lang.Boolean: .*"));
			// pureMethods.add(Pattern.compile("<java.lang.Byte: .*"));
			// pureMethods.add(Pattern.compile("<java.lang.Character: .*"));
			// pureMethods.add(Pattern.compile("<java.lang.Float: .*"));
			// pureMethods.add(Pattern.compile("<java.lang.Integer: .*"));
			// pureMethods.add(Pattern.compile("<java.lang.Long: .*"));
			// pureMethods.add(Pattern.compile("<java.lang.Short: .*"));
			// pureMethods.add(Pattern.compile("<java.lang.Double: .*"));
		}

		try {
			InstrumentTracerCLI commandLineOptions = CliFactory.parseArguments(InstrumentTracerCLI.class, args);
			projectJars.addAll(commandLineOptions.getProjectJar());
			outputDir = commandLineOptions.getOutputDir();
			outputType = commandLineOptions.getOutputType();
			//
			externalInterfaces.addAll(
					(commandLineOptions.getExternalInterfaces() != null) ? commandLineOptions.getExternalInterfaces()
							: new ArrayList<String>());
			//
			sootExclude.addAll((commandLineOptions.getExcludes() != null) ? commandLineOptions.getExcludes()
					: new ArrayList<String>());
			// We exclude from soot analysis the external interfaces as well
			sootExclude.addAll(externalInterfaces);
			//
			sootInclude.addAll((commandLineOptions.getIncludes() != null) ? commandLineOptions.getIncludes()
					: new ArrayList<String>());
			//
			if (commandLineOptions.getPureMethods() != null) {
				for (String pure : commandLineOptions.getPureMethods()) {
					if (MethodInvocationMatcher.jimpleMethodInvocationPattern.matcher(pure).matches()) {
						// Literal matching for the methods
						pureMethods.add(Pattern.compile(Pattern.quote(pure)));
					} else {
						// RegEx for the others
						pureMethods.add(Pattern.compile(pure));
					}
				}
			}

		} catch (ArgumentValidationException e) {
			throw e;
		}

		String[] sootArguments = new String[] { "-f", outputType };

		long startTime = System.nanoTime();

		// Setup the Soot settings
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_whole_program(true);

		// TODO: the problem with this one is that soot tries to instrument java
		// as well ?
		// Options.v().set_soot_classpath(System.getProperty("java.path"));
		//
		Options.v().set_output_dir(outputDir.getAbsolutePath());

		// Packages that Soot shall not look into can be brutally filtered out
		// at
		// this level as well. As long as the allow_phantom_ref is true,
		// everything is fine.
		// The more we remove, the faster the analysis gets.
		// For example, we can include here all the external interfaces already
		// !
		logger.info("Excluding: " + sootExclude);
		Options.v().set_exclude(sootExclude);
		Options.v().set_no_bodies_for_excluded(true);

		logger.info("Including: " + sootInclude);
		Options.v().set_include(sootInclude);

		// Split Assignments
		PackManager.v().getPack("jtp")
				.add(new Transform("jtp.01.split.assignments", new SplitAssignmentBodyTransformer()));

		// Capture the return value of invokes which do not have one
		PackManager.v().getPack("jtp")
				.add(new Transform("jtp.02.capture.return.values", new CaptureReturnValueBodyTransformer()));

		// Prepare parameters for the invocation
		PackManager.v().getPack("jtp")
				.add(new Transform("jtp.03.prepare.invocation.parameters", new PrepareInvocationParameters()));

		// Tracing instrumentation
		PackManager.v().getPack("jtp").add(
				new Transform("jtp.04.instrument.artificial.invocations", new ABCInstrumentArtificialInvocations()));
		PackManager.v().getPack("jtp")
				.add(new Transform("jtp.05.instrument.regular.invocations", new ABCBodyTranformer()));

		// Supporting jars are Xstream and trace
		// TODO HardCoded !
		// String xStreamJar = "./src/main/resources/xstream-1.4.9.jar";
		// AT THE MOMENT I CANNOT FIND A WAY TO HAVE SOOT USING THE SINGLE TRACE
		// CLASS

		String traceJar = ABCUtils.getTraceJar();

		// This is the application under analysis. 1 jar -> 1 entry
		// There can be more jars (libraries, tests, etc.)
		ArrayList<String> list = new ArrayList<String>();
		for (File projectJar : projectJars) {
			list.add(projectJar.getAbsolutePath());
		}
		// TODO Most likely project dependencies shall be listed here
		// Supporting jar. TODO Pretty sure this can be done in a different way
		// !
		list.add(traceJar);
		//
		logger.info("Project dir: " + list);
		Options.v().set_process_dir(list);

		// Patch for Mac OS.
		System.setProperty("os.name", "Whatever");
		Scene.v().loadNecessaryClasses();
		System.setProperty("os.name", "Mac OS X");

		// Run Soot - Probablu we can also execut Packs().run or something.
		soot.Main.main(sootArguments);

		long endTime = System.nanoTime();
		logger.debug("System test instrumentation took " + (endTime - startTime) + " ns");

	}

	// Supporting methods
	/**
	 * Avoid tracing of library methods or methods which implement external
	 * interfaces.
	 * 
	 * @param m
	 * @return
	 */
	public static boolean filterMethod(SootMethod m) {
		return m.isJavaLibraryMethod() || //
		// Skip framework methods
				m.getSignature().contains("de.unipassau.abc.tracing.Trace") || //
				//
				false;
	}

	/*
	 * TODO : We pretend those calls never happened. This is wrong, many calls
	 * return an Object, or an Array, and this is then used for the computation.]
	 * PURE METHODS MAKE SENSE ONLY ON THE CUT/MUT !
	 */
	public static boolean doNotTraceCallsTo(SootMethod m) {
		return m.getSignature().startsWith("<java.lang.Object:") || //
				m.getSignature().contains("de.unipassau.abc.tracing.Trace") || //
				false;
	}
	// for (Pattern purePattern : pureMethods) {
	// if (purePattern.matcher(m.toString()).matches()) {
	// logger.trace(m + " is declared PURE. Do not trace this call");
	// return true;
	// }
	// }
	// return false;
	// }

	/**
	 * Check if the current method matches any of the external library calls or it
	 * belongs to a class marked as implementing an external library
	 * 
	 * @param containerMethod
	 * @return
	 */
	public static boolean isExternalLibrary(SootMethod containerMethod) {
		// TODO Auto-generated method stub
		// return containerMethod.getSignature().contains("microsoft") || //
		// containerMethod.getSignature().contains("java.sql.");
		return false;
	}
}
