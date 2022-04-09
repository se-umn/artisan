
package de.unipassau.abc.parsing;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import soot.G;
import soot.IntType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.jimple.infoflow.android.manifest.ProcessManifest;
import soot.options.Options;
import soot.tagkit.IntegerConstantValueTag;
import soot.tagkit.Tag;

public class ParsingUtils {

    private static final Logger logger = LoggerFactory.getLogger(ParsingUtils.class);

    //
    public static boolean isArrayMethod(String methodSignature) {
        return JimpleUtils.getClassNameForMethod(methodSignature).endsWith("[]");
    }

    public static SootMethod getSootMethodFor(String methodSignature) {
        if (isArrayMethod(methodSignature)) {
            return null;
        }

        try {

            return Scene.v().getMethod(methodSignature);
        } catch (Throwable e) {
            SootClass sootClass = Scene.v().getSootClass(JimpleUtils.getClassNameForMethod(methodSignature));
            logger.warn("Cannot find method " + methodSignature + " in class " + sootClass);
            for (SootMethod sootMethod : sootClass.getMethods()) {
                System.out.println("- " + sootMethod);
            }
            return null;
        }
    }

    public static String findApkPackageName(File apk) {
        // process ids from apk package first
        String apkPackageName = "";
        try {
            ProcessManifest processMan = new ProcessManifest(apk.getAbsolutePath());
            apkPackageName = processMan.getPackageName();
        } catch (Exception e) {
            logger.debug("Exception while finding APK package name", e);
        }

        return apkPackageName;
    }

    public static Map<Integer, String> getIdsMap(File apk) {
        Map<Integer, String> idsInApk = new HashMap<Integer, String>();

        final String apkPackageName = findApkPackageName(apk);

        List<SootClass> orderedClasses = new ArrayList<SootClass>();
        for (SootClass sc : Scene.v().getApplicationClasses()) {
            orderedClasses.add(sc);
        }

        if (!apkPackageName.equals("")) {

            // Sort classes alfabetically, but process first classes that belong to the APK
//		    Collections.sort(orderedClasses, new Comparator<SootClass>() {
//			    
//				@Override
//				public int compare(SootClass o1, SootClass o2) {
//					if(o1.getPackageName().equals(o2.getPackageName()) && o1.getPackageName().startsWith(finaApkPackageName)) {
//						return 0;
//					}
//					else if(o1.getPackageName().startsWith(finaApkPackageName)){
//						return -1;
//					}
//					else if(o2.getPackageName().startsWith(finaApkPackageName)){
//						return 1;
//					}
//					else{
//						return 0;
//					}
//				}
//			});

            Collections.sort(orderedClasses, new Comparator<SootClass>() {

                @Override
                public int compare(SootClass o1, SootClass o2) {
                    return o1.getJavaStyleName().compareTo(o2.getJavaStyleName());
                }
            });

            List<SootClass> sortedClassesWithApk = new ArrayList<SootClass>();
            List<SootClass> otherSortedClasses = new ArrayList<SootClass>();
            for (SootClass sootClass : orderedClasses) {
                if (sootClass.getPackageName().equals(apkPackageName)) {
                    sortedClassesWithApk.add(sootClass);
                } else {
                    otherSortedClasses.add(sootClass);
                }
            }
            // Merge back the two sub-sorted lists in the oderedClasses
            orderedClasses.clear();
            orderedClasses.addAll(sortedClassesWithApk);
            orderedClasses.addAll(otherSortedClasses);

        }

        // process classes (first we process classes from the apk)
        for (SootClass sc : orderedClasses) {
            String className = sc.getShortName();
            // taken from
            // https://developer.android.com/guide/topics/resources/available-resources
            if (className.equals("R$id") || className.equals("R$anim") || className.equals("R$drawable")
                    || className.equals("R$color") || className.equals("R$layout") || className.equals("R$menu")
                    || className.equals("R$string") || className.equals("R$style") || className.equals("R$font")
                    || className.equals("R$bool") || className.equals("R$dimen")) {
                for (SootField sf : sc.getFields()) {
                    if (sf.getType() instanceof IntType) {
                        List<Tag> fieldTags = sf.getTags();
                        for (Tag fieldTag : fieldTags) {
                            if (fieldTag instanceof IntegerConstantValueTag) {
                                // the integer is initialized
                                IntegerConstantValueTag intTag = (IntegerConstantValueTag) fieldTag;
                                Integer value = Integer.valueOf(intTag.getIntValue());
                                if (value.intValue() == 0) {
                                    continue;
                                }
                                // get field name
                                String fieldName = sc.getName() + "." + sf.getName();
                                if (fieldName.contains("R$")) {
                                    fieldName = fieldName.replace("R$", "R.");
                                }
                                // check if the value is already in map
                                if (idsInApk.containsKey(value)) {
                                    String fieldNameInMap = idsInApk.get(value);
                                    if (fieldNameInMap.contains(sf.getName())) {
                                        continue;
                                    }
                                    throw new RuntimeException("Two fields mapping to the same integer (1) "
                                            + fieldNameInMap + "=" + value + " and (2) " + fieldName + "=" + value);
                                }
                                // put value and field in map
                                idsInApk.put(value, fieldName);
                                break;
                            }
                        }
                    }
                }
            }
        }
        return idsInApk;
    }

    // TODO Do we really need soot to parse the trace ?
    public static void setupSoot(File androidJar, File apk) {
        try {
            G.reset();
            // Generic options
            Options.v().set_allow_phantom_refs(true);
            Options.v().set_whole_program(true);
            Options.v().set_prepend_classpath(true);
            // Read (APK Dex-to-Jimple) Options
            // get android folder from jar
            String androidPlatformsPath = "";
            Map<String, String> env = System.getenv();
            for (String envKey : env.keySet()) {
                if (envKey.equals("ANDROID_HOME")) {
                    androidPlatformsPath = env.get(envKey) + File.separator + "platforms";
                }
            }
            if (androidPlatformsPath.equals("")) {
                String androidJarPath = androidJar.getAbsolutePath();
                if (androidJarPath.endsWith(File.separator + "android.jar")) {
                    File potentialAndroidSpecificPlatformFolder = androidJar.getParentFile();
                    if (potentialAndroidSpecificPlatformFolder.exists() && potentialAndroidSpecificPlatformFolder
                            .getAbsolutePath().contains(File.separator + "android-")) {
                        File androidPlatformsFile = potentialAndroidSpecificPlatformFolder.getParentFile();
                        if (androidPlatformsFile.exists()
                                && androidPlatformsFile.getAbsolutePath().endsWith(File.separator + "platforms")) {
                            androidPlatformsPath = androidPlatformsFile.getAbsolutePath();
                        }
                    }
                }
            }
            Options.v().set_android_jars(androidPlatformsPath); // The path to Android Platforms
            // get target sdk
            ProcessManifest processMan = new ProcessManifest(apk.getAbsolutePath());
            // TODO Change this using some option to force it ... or some way to
            // automatically enable it?
            if (processMan.getMinSdkVersion() < 22 || processMan.targetSdkVersion() < 22) {
                // Soot breaks if the minSdkVersion is smaller than 22 because of multidex
                // However, with multidex disabled, an instrumented APK will crash on start
                throw new RuntimeException(
                        String.format("The SDK level of the APK is %d. Must be >= 22", processMan.getMinSdkVersion()));
            }
            // We must set it manually, otherwise Soot will assume the default API version =
            // 15
            Options.v().set_android_api_version(processMan.targetSdkVersion());
            Options.v().set_src_prec(Options.src_prec_apk); // Determine the input is an APK
            Options.v().set_process_dir(Collections.singletonList(apk.getAbsolutePath())); // Provide paths to the APK
            Options.v().set_process_multiple_dex(true); // Inform Dexpler that the APK may have more than one .dex files
            Options.v().set_include_all(true);
            // Resolve required classes
            Scene.v().loadNecessaryClasses();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Could not initialize soot");
        }
    }

    public static String prettyPrint(List<MethodInvocation> subsumingMethods) {
        StringBuffer stringBuffer = new StringBuffer();
        for (MethodInvocation methodInvocation : subsumingMethods) {
            stringBuffer.append(methodInvocation).append("\n");
        }
        return null;
    }

}
