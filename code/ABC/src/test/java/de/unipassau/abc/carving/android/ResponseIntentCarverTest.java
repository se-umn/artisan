package de.unipassau.abc.carving.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.hamcrest.collection.IsEmptyCollection;
import org.hamcrest.core.IsNot;
import org.junit.Assert;
import org.junit.Test;

import de.unipassau.abc.carving.MethodInvocation;
import de.unipassau.abc.carving.carvers.android.ResponseIntentCarver;
import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Pair;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.parsing.DuafDroidParser;
import de.unipassau.abc.parsing.ParsedTrace;

public class ResponseIntentCarverTest {

    private static File ANDROID_28 = new File("/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");

    private ObjectInstance getObjectInstanceByObjectId(DataDependencyGraph dataDependencyGraph, String objectId) {
        for (ObjectInstance objectInstance : dataDependencyGraph.getObjectInstances()) {
            if (objectInstance.getObjectId().equals(objectId)) {
                return objectInstance;
            }
        }
        return null;
    }

    @Test
    public void testReturnActivityWithResult() throws FileNotFoundException, IOException, CarvingException {
        // TODO Replace with parsed traces instead ?
        File traceFile = new File("src/test/resources/sampletraces/start-another-activity-with-result.log");
        File apk = new File("/Users/gambi/MyDroidFax/apks/filtered-apps/org.ametro/apk/org.ametro_40.apk");

        DuafDroidParser.setupSoot(ANDROID_28, apk);
        DuafDroidParser parser = new DuafDroidParser();
        ParsedTrace pTrace = parser.parseTrace(traceFile);
        //
        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = pTrace.getUIThreadParsedTrace();
        //
        //
        ExecutionFlowGraph executionFlowGraph = parsedTrace.getFirst();
        DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
        CallGraph callGraph = parsedTrace.getThird();

        ObjectInstance secondActivity = getObjectInstanceByObjectId(dataDependencyGraph,
                "org.ametro.ui.activities.MapList@239795619");

        ResponseIntentCarver carver = new ResponseIntentCarver(executionFlowGraph, dataDependencyGraph, callGraph);
        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(secondActivity);

        // TODO Ideally we want to assert that we repeate the lookup twice
        Assert.assertThat(carvingResult.getFirst().getOrderedMethodInvocations(),
                IsNot.not(IsEmptyCollection.emptyCollectionOf(MethodInvocation.class)));
    }
    
    @Test
    public void testReturnActivityWithoutIntentResult() throws FileNotFoundException, IOException, CarvingException {
        // TODO Replace with parsed traces instead ?
        File traceFile = new File("src/test/resources/sampletraces/start-another-activity-with-result.log");
        File apk = new File("/Users/gambi/MyDroidFax/apks/filtered-apps/org.ametro/apk/org.ametro_40.apk");

        DuafDroidParser.setupSoot(ANDROID_28, apk);
        DuafDroidParser parser = new DuafDroidParser();
        ParsedTrace pTrace = parser.parseTrace(traceFile);
        //
        Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> parsedTrace = pTrace.getUIThreadParsedTrace();
        //
        //
        ExecutionFlowGraph executionFlowGraph = parsedTrace.getFirst();
        DataDependencyGraph dataDependencyGraph = parsedTrace.getSecond();
        CallGraph callGraph = parsedTrace.getThird();

        ObjectInstance secondActivity = getObjectInstanceByObjectId(dataDependencyGraph,
                "org.ametro.ui.activities.CityList@102756942");

        ResponseIntentCarver carver = new ResponseIntentCarver(executionFlowGraph, dataDependencyGraph, callGraph);
        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(secondActivity);

        // TODO Ideally we want to assert that we repeate the lookup twice
        Assert.assertThat(carvingResult.getFirst().getOrderedMethodInvocations(), IsEmptyCollection.emptyCollectionOf(MethodInvocation.class));
    }
    
    @Test
    public void testReturnActivityWithoutResult() throws FileNotFoundException, IOException, CarvingException {
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
                "org.ametro.ui.activities.Map@96819702");

        ResponseIntentCarver carver = new ResponseIntentCarver(executionFlowGraph, dataDependencyGraph, callGraph);
        Pair<ExecutionFlowGraph, DataDependencyGraph> carvingResult = carver.carve(secondActivity);
        
        // TODO Ideally we want to assert that we repeate the lookup twice
        Assert.assertNull( carvingResult );
    }

}
