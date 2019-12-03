package de.unipassau.abc.carving.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Assume;
import org.junit.Test;

import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.carvers.android.ActivityMethodsCarver;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.parsing.DuafDroidParser;
import de.unipassau.abc.parsing.ParsedTrace;

public class ActivityMethodsCarverTest {

    public final static File ANDROID_28 = new File("/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");

    private ObjectInstance getObjectInstanceByObjectId(DataDependencyGraph dataDependencyGraph, String objectId) {
        for (ObjectInstance objectInstance : dataDependencyGraph.getObjectInstances()) {
            if (objectInstance.getObjectId().equals(objectId)) {
                return objectInstance;
            }
        }
        return null;
    }

    private MethodInvocation getMethodInvocation(DataDependencyGraph dataDependencyGraph, ObjectInstance owner,
            String methodInvocationReference) {
        List<MethodInvocation> methods = new ArrayList<>(dataDependencyGraph.getMethodInvocationsForOwner(owner));
        MethodInvocationMatcher
                .keepMatchingInPlace(MethodInvocationMatcher.byMethodInvocation(methodInvocationReference), methods);

        Assume.assumeTrue(methods.size() == 1);
        return methods.get(0);
    }

    @Test
    public void testMainActivityFromOnCreate() throws FileNotFoundException, IOException, CarvingException {
        // TODO Maybe replace this with the parsed version ...
        File traceFile = new File("src/test/resources/sampletraces/main-activity.log");
        File apk = new File("/Users/gambi/MyDroidFax/apks/Notepad-Alessio.apk");
        DuafDroidParser.setupSoot(ANDROID_28, apk);
        DuafDroidParser parser = new DuafDroidParser();
        ParsedTrace pTrace = parser.parseTrace(traceFile);
        //
        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = pTrace.getUIThreadParsedTrace();
        //
        ExecutionFlowGraph executionFlowGraph = parsedTrace.getFirst();
        DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
        CallGraph callGraph = parsedTrace.getThird();

        ObjectInstance mainActivity = getObjectInstanceByObjectId(dataDependencyGraph,
                "com.farmerbb.notepad.activity.MainActivity@176677084");

        ActivityMethodsCarver carver = new ActivityMethodsCarver(executionFlowGraph, dataDependencyGraph, callGraph);
        MethodInvocation from = getMethodInvocation(dataDependencyGraph, mainActivity,
                "<com.farmerbb.notepad.activity.MainActivity: void onCreate(android.os.Bundle)>_5");

        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(mainActivity, from);

        // There must be one owner only
        Set<ObjectInstance> owners = new HashSet<>();
        for (MethodInvocation m : carvingResult.getFirst().getOrderedMethodInvocations()) {
            owners.add(m.getOwner());
        }
        Assert.assertEquals(1, owners.size());

    }

    @Test
    public void testMainActivityFromOnDestroy() throws FileNotFoundException, IOException, CarvingException {
        // TODO Maybe replace this with the parsed version ...
        File traceFile = new File("src/test/resources/sampletraces/main-activity.log");
        File apk = new File("/Users/gambi/MyDroidFax/apks/Notepad-Alessio.apk");
        DuafDroidParser.setupSoot(ANDROID_28, apk);
        DuafDroidParser parser = new DuafDroidParser();
        ParsedTrace pTrace = parser.parseTrace(traceFile);
        //
        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = pTrace.getUIThreadParsedTrace();
        //
        ExecutionFlowGraph executionFlowGraph = parsedTrace.getFirst();
        DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
        CallGraph callGraph = parsedTrace.getThird();

        ObjectInstance mainActivity = getObjectInstanceByObjectId(dataDependencyGraph,
                "com.farmerbb.notepad.activity.MainActivity@176677084");

        ActivityMethodsCarver carver = new ActivityMethodsCarver(executionFlowGraph, dataDependencyGraph, callGraph);
        MethodInvocation from = getMethodInvocation(dataDependencyGraph, mainActivity,
                "<com.farmerbb.notepad.activity.MainActivity: void onDestroy()>_993");

        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(mainActivity, from);

        // System.out.println( carvingResult.getFirst().prettyPrint());

        Set<ObjectInstance> owners = new HashSet<>();
        for (MethodInvocation m : carvingResult.getFirst().getOrderedMethodInvocations()) {
            owners.add(m.getOwner());
        }

        // There must be one owner only
        Assert.assertEquals(1, owners.size());

    }

    @Test
    public void testMainActivityAfterFirstDeviceRotation() throws FileNotFoundException, IOException, CarvingException {
        // TODO Replace with parsed traces instead ?
        File traceFile = new File("src/test/resources/sampletraces/device-rotation.log");
        File apk = new File("/Users/gambi/MyDroidFax/apks/Notepad-Alessio.apk");
        DuafDroidParser.setupSoot(ANDROID_28, apk);
        DuafDroidParser parser = new DuafDroidParser();
        ParsedTrace pTrace = parser.parseTrace(traceFile);
        //
        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = pTrace.getUIThreadParsedTrace();
        //
        ExecutionFlowGraph executionFlowGraph = parsedTrace.getFirst();
        DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
        CallGraph callGraph = parsedTrace.getThird();

        ObjectInstance originalActivity = getObjectInstanceByObjectId(dataDependencyGraph,
                "com.farmerbb.notepad.activity.MainActivity@176677084");
        ObjectInstance activityAfterFirstRotation = getObjectInstanceByObjectId(dataDependencyGraph,
                "com.farmerbb.notepad.activity.MainActivity@121163626");

        ActivityMethodsCarver carver = new ActivityMethodsCarver(executionFlowGraph, dataDependencyGraph, callGraph);
        MethodInvocation from = getMethodInvocation(dataDependencyGraph, activityAfterFirstRotation,
                "<com.farmerbb.notepad.activity.MainActivity: void onCreate(android.os.Bundle)>_415");

        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(activityAfterFirstRotation, from);

        Set<ObjectInstance> owners = new HashSet<>();
        for (MethodInvocation m : carvingResult.getFirst().getOrderedMethodInvocations()) {
            owners.add(m.getOwner());
        }

        // There must be one owner only
        Assert.assertEquals(2, owners.size());

        Assert.assertTrue(owners.contains(originalActivity));
        Assert.assertTrue(owners.contains(activityAfterFirstRotation));
    }

    @Test
    public void testMainActivityAfterSecondDeviceRotation()
            throws FileNotFoundException, IOException, CarvingException {
        // TODO Replace with parsed traces instead ?
        File traceFile = new File("src/test/resources/sampletraces/device-rotation.log");
        File apk = new File("/Users/gambi/MyDroidFax/apks/Notepad-Alessio.apk");
        DuafDroidParser.setupSoot(ANDROID_28, apk);
        DuafDroidParser parser = new DuafDroidParser();
        ParsedTrace pTrace = parser.parseTrace(traceFile);
        //
        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = pTrace.getUIThreadParsedTrace();
        //
        ExecutionFlowGraph executionFlowGraph = parsedTrace.getFirst();
        DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
        CallGraph callGraph = parsedTrace.getThird();

        ObjectInstance originalActivity = getObjectInstanceByObjectId(dataDependencyGraph,
                "com.farmerbb.notepad.activity.MainActivity@176677084");
        ObjectInstance activityAfterFirstRotation = getObjectInstanceByObjectId(dataDependencyGraph,
                "com.farmerbb.notepad.activity.MainActivity@121163626");
        ObjectInstance activityAfterSecondRotation = getObjectInstanceByObjectId(dataDependencyGraph,
                "com.farmerbb.notepad.activity.MainActivity@147456286");

        ActivityMethodsCarver carver = new ActivityMethodsCarver(executionFlowGraph, dataDependencyGraph, callGraph);
        MethodInvocation from = getMethodInvocation(dataDependencyGraph, activityAfterSecondRotation,
                "<com.farmerbb.notepad.activity.MainActivity: void onCreate(android.os.Bundle)>_827");

        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(activityAfterSecondRotation, from);

        Set<ObjectInstance> owners = new HashSet<>();
        for (MethodInvocation m : carvingResult.getFirst().getOrderedMethodInvocations()) {
            owners.add(m.getOwner());
        }

        // There must be one owner only
        Assert.assertEquals(3, owners.size());

        Assert.assertTrue(owners.contains(originalActivity));
        Assert.assertTrue(owners.contains(activityAfterFirstRotation));
        Assert.assertTrue(owners.contains(activityAfterSecondRotation));
    }

    @Test
    public void testStartedActivityOnCreate() throws FileNotFoundException, IOException, CarvingException {
        // TODO Replace with parsed traces instead ?
        File traceFile = new File("src/test/resources/sampletraces/start-another-activity.log");
        File apk = new File("/Users/gambi/MyDroidFax/apks/Notepad-Alessio.apk");
        DuafDroidParser.setupSoot(ANDROID_28, apk);
        DuafDroidParser parser = new DuafDroidParser();
        ParsedTrace pTrace = parser.parseTrace(traceFile);
        //
        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = pTrace.getUIThreadParsedTrace();
        //
        ExecutionFlowGraph executionFlowGraph = parsedTrace.getFirst();
        DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
        CallGraph callGraph = parsedTrace.getThird();

        ObjectInstance secondActivity = getObjectInstanceByObjectId(dataDependencyGraph,
                "com.farmerbb.notepad.activity.SettingsActivity@106729580");

        MethodInvocation from = getMethodInvocation(dataDependencyGraph, secondActivity,
                "<com.farmerbb.notepad.activity.SettingsActivity: void onCreate(android.os.Bundle)>_389");

        ActivityMethodsCarver carver = new ActivityMethodsCarver(executionFlowGraph, dataDependencyGraph, callGraph);

        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(secondActivity, from);

        Set<ObjectInstance> owners = new HashSet<>();
        for (MethodInvocation m : carvingResult.getFirst().getOrderedMethodInvocations()) {
            owners.add(m.getOwner());
        }

        // There must be one owner only
        Assert.assertEquals(1, owners.size());

        Assert.assertTrue(owners.contains(secondActivity));
    }

    @Test
    public void testStartedActivityOnDestroy() throws FileNotFoundException, IOException, CarvingException {
        // TODO Replace with parsed traces instead ?
        File traceFile = new File("src/test/resources/sampletraces/start-another-activity.log");
        File apk = new File("/Users/gambi/MyDroidFax/apks/Notepad-Alessio.apk");
        DuafDroidParser.setupSoot(ANDROID_28, apk);
        DuafDroidParser parser = new DuafDroidParser();
        ParsedTrace pTrace = parser.parseTrace(traceFile);
        //
        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = pTrace.getUIThreadParsedTrace();
        //
        ExecutionFlowGraph executionFlowGraph = parsedTrace.getFirst();
        DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
        CallGraph callGraph = parsedTrace.getThird();

        ObjectInstance secondActivity = getObjectInstanceByObjectId(dataDependencyGraph,
                "com.farmerbb.notepad.activity.SettingsActivity@106729580");

        MethodInvocation from = getMethodInvocation(dataDependencyGraph, secondActivity,
                "<com.farmerbb.notepad.activity.SettingsActivity: void onDestroy()>_671");

        ActivityMethodsCarver carver = new ActivityMethodsCarver(executionFlowGraph, dataDependencyGraph, callGraph);

        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(secondActivity, from);

        Set<ObjectInstance> owners = new HashSet<>();
        for (MethodInvocation m : carvingResult.getFirst().getOrderedMethodInvocations()) {
            owners.add(m.getOwner());
        }

        // There must be one owner only
        Assert.assertEquals(1, owners.size());

        Assert.assertTrue(owners.contains(secondActivity));
    }

    @Test
    public void testStartedActivityWithResultOnCreate() throws FileNotFoundException, IOException, CarvingException {
        // TODO Replace with parsed traces instead ?
        File traceFile = new File("src/test/resources/sampletraces/start-another-activity-with-result.log");
        File apk = new File("/Users/gambi/MyDroidFax/apks/filtered-apps/org.ametro/apk/org.ametro_40.apk");

        DuafDroidParser.setupSoot(ANDROID_28, apk);
        DuafDroidParser parser = new DuafDroidParser();
        ParsedTrace pTrace = parser.parseTrace(traceFile);
        //
        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = pTrace.getUIThreadParsedTrace();
        //
        ExecutionFlowGraph executionFlowGraph = parsedTrace.getFirst();
        DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
        CallGraph callGraph = parsedTrace.getThird();

        ObjectInstance secondActivity = getObjectInstanceByObjectId(dataDependencyGraph,
                "org.ametro.ui.activities.MapList@239795619");
        
        MethodInvocation from = getMethodInvocation(dataDependencyGraph, secondActivity,
                "<org.ametro.ui.activities.MapList: void onCreate(android.os.Bundle)>_842");

        ActivityMethodsCarver carver = new ActivityMethodsCarver(executionFlowGraph, dataDependencyGraph, callGraph);

        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(secondActivity, from);

        Set<ObjectInstance> owners = new HashSet<>();
        for (MethodInvocation m : carvingResult.getFirst().getOrderedMethodInvocations()) {
            owners.add(m.getOwner());
        }

        // There must be one owner only
        Assert.assertEquals(1, owners.size());

        Assert.assertTrue(owners.contains(secondActivity));
//        10792
    }
    
    @Test
    public void testStartedActivityWithResultOnDestroy() throws FileNotFoundException, IOException, CarvingException {
        // TODO Replace with parsed traces instead ?
        File traceFile = new File("src/test/resources/sampletraces/start-another-activity-with-result.log");
        File apk = new File("/Users/gambi/MyDroidFax/apks/filtered-apps/org.ametro/apk/org.ametro_40.apk");

        DuafDroidParser.setupSoot(ANDROID_28, apk);
        DuafDroidParser parser = new DuafDroidParser();
        ParsedTrace pTrace = parser.parseTrace(traceFile);
        //
        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = pTrace.getUIThreadParsedTrace();
        //
        ExecutionFlowGraph executionFlowGraph = parsedTrace.getFirst();
        DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
        CallGraph callGraph = parsedTrace.getThird();

        ObjectInstance secondActivity = getObjectInstanceByObjectId(dataDependencyGraph,
                "org.ametro.ui.activities.MapList@239795619");
        
        MethodInvocation from = getMethodInvocation(dataDependencyGraph, secondActivity,
                "<org.ametro.ui.activities.MapList: void onDestroy()>_10793");

        ActivityMethodsCarver carver = new ActivityMethodsCarver(executionFlowGraph, dataDependencyGraph, callGraph);

        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(secondActivity, from);

        Assert.assertTrue(carvingResult.getFirst().getOrderedMethodInvocations().size() > 1 );
        
        Set<ObjectInstance> owners = new HashSet<>();
        for (MethodInvocation m : carvingResult.getFirst().getOrderedMethodInvocations()) {
            owners.add(m.getOwner());
        }

        // There must be one owner only
        Assert.assertEquals(1, owners.size());
        Assert.assertTrue(owners.contains(secondActivity));
    }
    //
    // @Test
    // @Ignore
    // public void testStartedActivityOfTheSameType() throws
    // FileNotFoundException, IOException, CarvingException {
    // // TODO THis is not implemented yet. Maybe just a corner case of the
    // NOTEPAD app which can export a note
    // // and import it inside "itself"
    // }

}
