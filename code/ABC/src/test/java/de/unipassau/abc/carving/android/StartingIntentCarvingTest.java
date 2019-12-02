package de.unipassau.abc.carving.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import de.unipassau.abc.carving.CallGraph;
import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.ObjectInstance;
import de.unipassau.abc.carving.carvers.android.StartingIntentCarver;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.parsing.DuafDroidParser;
import de.unipassau.abc.parsing.ParsedTrace;


public class StartingIntentCarvingTest{
    private static File ANDROID_28 = new File("/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");

    @Test
    @Ignore
    public void testMainActivity() throws FileNotFoundException, IOException, CarvingException {
        // TODO Maybe replace this with the parsed version ...
        File traceFile = new File("src/test/resources/sampletraces/main-activity.log");
        File apk = new File("/Users/gambi/MyDroidFax/apks/Notepad-Alessio.apk");
        DuafDroidParser.setupSoot(ANDROID_28, apk);
        DuafDroidParser parser = new DuafDroidParser();
        ParsedTrace pTrace = parser.parseTrace(traceFile);
        //
        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = pTrace.getUIThreadParsedTrace();
        
        ExecutionFlowGraph executionFlowGraph = parsedTrace.getFirst();
        DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
        CallGraph callGraph = parsedTrace.getThird();

        ObjectInstance mainActivity = null;
        for (ObjectInstance objectInstance : dataDependencyGraph.getObjectInstances()) {
            if (objectInstance.isAndroidActivity()
                    && objectInstance.getObjectId().equals("com.farmerbb.notepad.activity.MainActivity@176677084")) {
                mainActivity = objectInstance;
                break;
            }
        }

        StartingIntentCarver carver = new StartingIntentCarver(executionFlowGraph, dataDependencyGraph, callGraph);
        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(mainActivity);

        Assert.assertThat(carvingResult.getFirst().getOrderedMethodInvocations(),
                IsEmptyCollection.emptyCollectionOf(MethodInvocation.class));

    }

    // StartingIntentCarvingTest.testMainActivityAfterDeviceRotation()
    // com.farmerbb.notepad.activity.MainActivity@147456286
    // StartingIntentCarvingTest.testMainActivityAfterDeviceRotation()
    // com.farmerbb.notepad.activity.MainActivity@121163626
    // StartingIntentCarvingTest.testMainActivityAfterDeviceRotation()
    // com.farmerbb.notepad.activity.MainActivity@176677084

    private ObjectInstance getObjectInstanceByObjectId(DataDependencyGraph dataDependencyGraph, String objectId) {
        for (ObjectInstance objectInstance : dataDependencyGraph.getObjectInstances()) {
            if (objectInstance.getObjectId().equals(objectId)) {
                return objectInstance;
            }
        }
        return null;
    }

    @Test
    @Ignore
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
                "com.farmerbb.notepad.activity.MainActivity@147456286");
        ObjectInstance activityAfterFirstRotation = getObjectInstanceByObjectId(dataDependencyGraph,
                "com.farmerbb.notepad.activity.MainActivity@121163626");

        StartingIntentCarver carver = new StartingIntentCarver(executionFlowGraph, dataDependencyGraph, callGraph);
        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(activityAfterFirstRotation);
        // TODO Ideally we want to assert that we repeate the lookup twice
        Assert.assertThat(carvingResult.getFirst().getOrderedMethodInvocations(),
                IsEmptyCollection.emptyCollectionOf(MethodInvocation.class));
    }

    @Test
    @Ignore
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

        StartingIntentCarver carver = new StartingIntentCarver(executionFlowGraph, dataDependencyGraph, callGraph);
        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(activityAfterSecondRotation);
        // TODO Ideally we want to assert that we repeate the lookup twice
        Assert.assertThat(carvingResult.getFirst().getOrderedMethodInvocations(),
                IsEmptyCollection.emptyCollectionOf(MethodInvocation.class));
    }

    @Test
    public void testStartedActivity() throws FileNotFoundException, IOException, CarvingException {
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

        StartingIntentCarver carver = new StartingIntentCarver(executionFlowGraph, dataDependencyGraph, callGraph);
        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(secondActivity);
        // TODO Ideally we want to assert that we repeate the lookup twice
        Assert.assertThat(
                carvingResult.getFirst().getOrderedMethodInvocations(),
                IsNot.not(IsEmptyCollection.emptyCollectionOf(MethodInvocation.class)));

    }
    @Test
    public void testStartedActivityWithResult() throws FileNotFoundException, IOException, CarvingException {
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

        StartingIntentCarver carver = new StartingIntentCarver(executionFlowGraph, dataDependencyGraph, callGraph);
        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(secondActivity);
        // TODO Ideally we want to assert that we repeate the lookup twice
        Assert.assertThat(
                carvingResult.getFirst().getOrderedMethodInvocations(),
                IsNot.not(IsEmptyCollection.emptyCollectionOf(MethodInvocation.class)));
    }

    @Test
    @Ignore
    public void testStartedActivityOfTheSameType() throws FileNotFoundException, IOException, CarvingException {
        // TODO THis is not implemented yet. Maybe just a corner case of the NOTEPAD app which can export a note
        // and import it inside "itself"
    }

}
