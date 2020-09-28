package de.unipassau.analysis;

import soot.G;
import soot.jimple.JimpleBody;
import soot.options.Options;
import soot.toolkits.graph.ClassicCompleteUnitGraph;
import soot.toolkits.graph.UnitGraph;
import soot.tagkit.Tag;
import soot.tagkit.AnnotationTag;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationStringElem;
import soot.tagkit.AnnotationClassElem;
import soot.tagkit.VisibilityAnnotationTag;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.util.Chain;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Iterator;
import java.util.Collection;

public class ShadowAnalysis {

    public static List<String> processDir = new ArrayList<String>(Arrays.asList(
                System.getProperty("user.home") + "/shadowanalysis/jars/robolectric-4.4.jar", 
                System.getProperty("user.home") + "/shadowanalysis/jars/shadowapi-4.4.jar", 
                System.getProperty("user.home") + "/shadowanalysis/jars/resources-4.4.jar", 
                System.getProperty("user.home") + "/shadowanalysis/jars/utils-4.4.jar", 
                System.getProperty("user.home") + "/shadowanalysis/jars/utils-reflector-4.4.jar", 
                System.getProperty("user.home") + "/shadowanalysis/jars/buildSrc.jar", 
                System.getProperty("user.home") + "/shadowanalysis/jars/processor-4.4.jar", 
                System.getProperty("user.home") + "/shadowanalysis/jars/annotations-4.4.jar", 
                System.getProperty("user.home") + "/shadowanalysis/jars/shadows-framework-4.4.jar"));

    public static void setupSoot() {
        G.reset(); 
        Options.v().set_allow_phantom_refs(true);
        Options.v().set_prepend_classpath(true);
        Options.v().set_process_dir(processDir);
        Scene.v().loadNecessaryClasses();
    }

    public static void main(String[] args) {
        setupSoot();
        JSONObject jsonClasses = new JSONObject();
        Iterator<SootClass> classes = Scene.v().getApplicationClasses().iterator();
        while (classes.hasNext()) {
            JSONObject jsonClassDesc = new JSONObject();
            SootClass nextClass = classes.next();
            VisibilityAnnotationTag tag = (VisibilityAnnotationTag) nextClass.getTag("VisibilityAnnotationTag");
            if (tag != null) {
                for (AnnotationTag annotation : tag.getAnnotations()) {
                    if (annotation.getType().equals("Lorg/robolectric/annotation/Implements;")) {
                        jsonClassDesc.put("shadow_class_name", nextClass.getName());
                        for (AnnotationElem elem : annotation.getElems()) {
                            if (elem instanceof AnnotationStringElem && elem.getName().equals("className")) {
                                jsonClassDesc.put("android_class", ((AnnotationStringElem) elem).getValue());
                            } else if (elem instanceof AnnotationClassElem && elem.getName().equals("value")) {
                                String desc = ((AnnotationClassElem) elem).getDesc();
                                jsonClassDesc.put("android_class", desc.substring(1, desc.length() - 1).replace("/","."));
                            }
                        }
                        List<String> methodList = new ArrayList<String>();
                        for (SootMethod method : nextClass.getMethods()) {
                            VisibilityAnnotationTag methodTag = (VisibilityAnnotationTag) method.getTag("VisibilityAnnotationTag"); 
                            if (methodTag != null) {
                                for (AnnotationTag methodAnnotation : methodTag.getAnnotations()) {
                                    if (methodAnnotation.getType().equals("Lorg/robolectric/annotation/Implementation;")) {
                                        methodList.add(method.getSubSignature());
                                    }
                                }
                            }
                        }
                        jsonClassDesc.put("methods", methodList.toArray());
                        System.out.println(jsonClassDesc.toString(4));
                    }
                }
            }
        }
    }
}
