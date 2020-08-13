package de.unipassau.abc.carving.android;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.SystemUtils;
import org.checkerframework.checker.units.qual.K;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lexicalscope.jewel.cli.ArgumentValidationException;
import com.lexicalscope.jewel.cli.CliFactory;
import com.lexicalscope.jewel.cli.Option;
import com.thoughtworks.xstream.XStream;

import de.unipassau.abc.carving.CarvedExecution;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.parsing.ParsedTrace;
import de.unipassau.abc.parsing.ParsedTraceImpl;
import fj.data.vector.V;
import soot.G;
import soot.Scene;
import soot.options.Options;

/**
 * Sucks up a parsed trace xml file and tries to carve out all the methods for a
 * given class.
 * 
 * TODO For the moment we output JIMPLE text files
 * 
 * @author gambi
 *
 */
public class AndroidCarver {

	// THIS Carver works on Android Decorated graphs ?
	private static final Logger logger = LoggerFactory.getLogger(AndroidCarver.class);
	// public static final String MOCKITO_JAR =
	// "/Users/gambi/.m2/repository/org/mockito/mockito-core/1.10.19/mockito-core-1.10.19.jar";

	public interface CarverCLI {

		@Option(longName = "parsed-traces")
		List<File> getParsedTraceFiles();

		@Option(longName = "apk")
		File getAPK();

		@Option(longName = "android-jar")
		File getAndroidJar();

		@Option(longName = "output-carved-tests-to")
		File getOutputDir();

		@Option(longName = "class-to-carve", description = " fully qualified name of the class to carve", defaultToNull = true)
		String getClassToCarve();

		@Option(longName = "method-invocation-to-carve", description = "", defaultToNull = true)
		String getMethodInvocationToCarve();
	}

	public static MethodInvocationMatcher getMatcherFor(final String type, final String regEx) {
		switch (type) {
		case "package":
			return MethodInvocationMatcher.byPackage(regEx);
		case "class":
			// return MethodInvocationMatcher.byClass(regEx);
			return MethodInvocationMatcher.byClassLiteral(regEx);
		case "method":
			// PAY ATTENTION TO THIS
			return MethodInvocationMatcher.byMethodLiteral(regEx);
		case "regex":
			return MethodInvocationMatcher.byMethod(regEx);
		case "invocation":
			return MethodInvocationMatcher.byMethodInvocation(regEx);
		case "return":
			return MethodInvocationMatcher.byReturnType(regEx);
		case "instance":
			// TODO how to rule out primitive, null, and possibly Strings ?
			return MethodInvocationMatcher.byInstance(new ObjectInstance(regEx));
		default:
			throw new ArgumentValidationException("Unknown matcher specification " + type + "=" + regEx);
		}
	}

	public static void main(String[] args)
			throws IOException, InterruptedException, URISyntaxException, CarvingException {
		long startTime = System.nanoTime();

		CarverCLI cli = CliFactory.parseArguments(CarverCLI.class, args);
		/*
		 * This is tricky now. If the passed file is a directory for multiple threads we
		 * need to load all the files. But consider them as one
		 */
		List<File> parsedTracesFiles = cli.getParsedTraceFiles();
		File storeCarvedTestsTo = cli.getOutputDir();

		// Validate the input
		MethodInvocationMatcher carveBy = null;

		String classToCarve = cli.getClassToCarve();
		if (classToCarve == null || (classToCarve != null && classToCarve.isEmpty())) {
			String methodInvocationToCarve = cli.getMethodInvocationToCarve();

			if (methodInvocationToCarve != null && !methodInvocationToCarve.isEmpty()) {
				carveBy = MethodInvocationMatcher.byMethodInvocation(methodInvocationToCarve);
			}
		} else {
			carveBy = MethodInvocationMatcher.byClass(classToCarve);
		}

		setupSoot(cli.getAndroidJar(), cli.getAPK());

		// Not sure what this is used...
		List<MethodInvocationMatcher> excludeBy = new ArrayList<>();
		// We cannot carve static initializers
		excludeBy.add(MethodInvocationMatcher.clinit());

		// Load the parsed traces.

		List<ParsedTraceImpl> theParsedTraces = new ArrayList<>();
		for (File parsedTraceFile : parsedTracesFiles) {
			// TODO this will not work if parsedTraceFile is file...
			theParsedTraces.add(ParsedTraceImpl.loadFromDirectory(parsedTraceFile));
		}
//        Map<String, // Files/Directories -> SystemTest
//                Map<String, // Thread
//                        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>>> parsedTraces = new HashMap<>();
//
//        XStream xStream = new XStream();
//
//        for (File parsedTraceFile : parsedTracesFiles) {
//
//            // Create the entry for this system test
//            Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> parsedTrace = new HashMap<>();
//            parsedTraces.put(parsedTraceFile.getName(), parsedTrace);
//
//            if (parsedTraceFile.isDirectory()) {
//                for (File file : parsedTraceFile.listFiles(new FilenameFilter() {
//                    @Override
//                    public boolean accept(File dir, String name) {
//                        return name.endsWith(".parsed.xml");
//                    }
//                })) {
//                    // TODO This should be the thread name instead ...
//                    logger.info("Loading parsed trace from " + parsedTraceFile + " for " + file.getName());
//                    parsedTrace.put(file.getName(),
//                            (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>) xStream.fromXML(file));
//                }
//
//            } else {
//                try {
//                    logger.info("Loading parsed trace from " + parsedTraceFile + " for " + parsedTraceFile.getName());
//                    parsedTrace.put(parsedTraceFile.getName(),
//                            (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>) xStream
//                                    .fromXML(parsedTraceFile));
//                } catch (Exception e) {
//                    logger.error("Cannot parse " + parsedTrace);
//                }
//            }
//        }

		// TODO We make the simplifying ASSUMPTION that we elaborate one system
		// test at the time - refactoring needed

		logger.info("Start carving of " + theParsedTraces.size() + " traces ");
		// TODO Load this from the files. Order does not matter

		if (carveBy == null) {
			carveBy = generateMethodInvocationMatcherForAllTheActivities(theParsedTraces);
		}

		if (carveBy == null) {
			// By default we look for the Activity classes in the APK and carve
			// them all...

			throw new CarvingException("Wrong carve by criterion");
		}

		/*
		 * At this level of abstraction carved tests are simply a number of method
		 * invocations
		 */
		Map<MethodInvocation, List<CarvedExecution>> carvedTests = new HashMap<MethodInvocation, List<CarvedExecution>>();

		long carvingTime = System.currentTimeMillis();

		int methodInvocationCount = 0;
		for (ParsedTrace parsedTrace : theParsedTraces) {
			/*
			 * The AndroidCarving is focused on Activities, so we only care about methods
			 * that are in the main thread. The methods inside the other threads will be
			 * reached later and are never the target for carving.
			 */

			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> mainThreadParsedTrace = parsedTrace
					.getUIThreadParsedTrace();

			List<MethodInvocation> methodsInvocationsToCarve = new ArrayList<MethodInvocation>(
					// WOW 3 layers, long live Demetra !!
					mainThreadParsedTrace.getFirst().getMethodInvocationsFor(carveBy,
							excludeBy.toArray(new MethodInvocationMatcher[] {})));

			// Remove the synthetic methods
			MethodInvocationMatcher.filterByInPlace(MethodInvocationMatcher.isSynthetic(), methodsInvocationsToCarve);

			methodInvocationCount += methodsInvocationsToCarve.size();

			/*
			 * There might be different strategies for Carving. We use the Activity carving
			 * one.
			 */
			AndroidActivityCarver testCarver = new AndroidActivityCarver(parsedTrace);

			carvedTests.putAll(testCarver.carve(methodsInvocationsToCarve));
		}

		// TODO Post processing of carved tests...
		/*
		 * Simplify ? Shorten ?
		 */

		/*
		 * Remove duplicates. A duplicate is code clone were the method under test is
		 * the same and the parameters
		 */

		carvingTime = System.currentTimeMillis() - carvingTime;

		logger.info("End carving");

		logger.info(">> Carved tests : " + carvedTests.size() + "/" + methodInvocationCount);

//		logger.debug("Storing carved tests to : " + storeCarvedTestsTo.getAbsolutePath());

//		XStream xStream = new XStream();
//		// TODO Store carved tests to storeCarvedTestsTo
//
//		for (List<Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> carvedExecutionsForMethod : carvedTests
//				.values()) {
//			for (Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvedTest : carvedExecutionsForMethod) {
//				MethodInvocation carvedMethodInvocation = carvedTest.getFirst().getLastMethodInvocation();
//				String methodSignature = carvedMethodInvocation.getMethodSignature();
//
//				String carvedClass = JimpleUtils.getClassNameForMethod(methodSignature);
//				String carvedMethod = JimpleUtils.getMethodName(methodSignature);
//
//				// Store to outputfile
//				File outputFile = new File(storeCarvedTestsTo,
//						carvedClass + "." + carvedMethod + "_" + carvedMethodInvocation.getInvocationCount());
//				logger.trace("Store carved test to " + outputFile.getAbsolutePath());
//				xStream.toXML(carvedTest, new FileWriter(outputFile));
//			}
//		}
		return;
	}

	private static MethodInvocationMatcher generateMethodInvocationMatcherForAllTheActivities(
			List<ParsedTraceImpl> parsedTraces) {
		// Map<String, Triplette<ExecutionFlowGraph, DataDependencyGraph,
		// CallGraph>> parsedTraces) {
		Set<String> activities = new HashSet<>();
		for (ParsedTrace parsedTrace : parsedTraces) {
			// We care ONLY about the activities, and activities are handled only inside
			// UI/Main Thread
			Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> mainThreadParsedTrace = parsedTrace
					.getUIThreadParsedTrace();
			DataDependencyGraph dataDependencyGraph = mainThreadParsedTrace.getSecond();
			for (ObjectInstance objectInstance : dataDependencyGraph.getObjectInstances()) {
				if (objectInstance.isAndroidActivity()) {
					activities.add(objectInstance.getType());
				}
			}
		}

		logger.info("Found the following activities in the application: \n" + activities);
		List<MethodInvocationMatcher> matchers = new ArrayList<>();
		for (String activityType : activities) {
			matchers.add(MethodInvocationMatcher.byClass(Pattern.quote(activityType)));
		}

		if (matchers.isEmpty()) {
			return null;
		} else if (matchers.size() == 1) {
			return matchers.get(0);
		} else {
			return MethodInvocationMatcher.or( //
					matchers.get(0), matchers.get(1),
					matchers.subList(2, matchers.size()).toArray(new MethodInvocationMatcher[] {}));
		}
	}

	public static void setupSoot(File androidJar, File apk) {
		G.reset();
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_whole_program(true);

		// Not sure this will work with the APK
		ArrayList<String> necessaryJar = new ArrayList<String>();

		// Include here entries from the classpath
		StringBuffer sootClassPath = new StringBuffer();
		sootClassPath.append(androidJar.getAbsolutePath());

		for (String cpEntry : SystemUtils.JAVA_CLASS_PATH.split(File.pathSeparator)) {
			if (cpEntry.contains("mockito-core-1.10.19.jar")) {
				necessaryJar.add(cpEntry);
			}
			if (cpEntry.contains("robolectric-4.2.jar")) {
				necessaryJar.add(cpEntry);
			}
			// if( cpEntry.contains("shadowapi-4.2.jar") ){
			// necessaryJar.add(cpEntry);
			// }
			if (cpEntry.contains("shadows-framework-4.2.jar")) {
				necessaryJar.add(cpEntry);
			}
		}
		necessaryJar.add(apk.getAbsolutePath());

		Options.v().set_soot_classpath(sootClassPath.toString());
		Options.v().set_process_dir(necessaryJar);
		Options.v().set_src_prec(soot.options.Options.src_prec_apk);

		// Soot has problems in working on mac.
		String osName = System.getProperty("os.name");
		System.setProperty("os.name", "Whatever");
		Scene.v().loadNecessaryClasses();
		System.setProperty("os.name", osName);
	}
}
