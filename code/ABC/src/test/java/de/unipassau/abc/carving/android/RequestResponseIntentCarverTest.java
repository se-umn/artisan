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
import de.unipassau.abc.carving.carvers.android.RequestResponseIntentsCarver;
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

public class RequestResponseIntentCarverTest {

    private static File ANDROID_28 = new File("/Users/gambi/Library/Android/sdk/platforms/android-28/android.jar");

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
    public void testActivityThatDoesNotYetStartAnotherActivity() throws FileNotFoundException, IOException, CarvingException {
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

        ObjectInstance activity = getObjectInstanceByObjectId(dataDependencyGraph,
                "org.ametro.ui.activities.Map@96819702");
        
        MethodInvocation from = getMethodInvocation(dataDependencyGraph, activity,
                "<org.ametro.ui.activities.Map: boolean onCreateOptionsMenu(android.view.Menu)>_821");


        RequestResponseIntentsCarver carver = new RequestResponseIntentsCarver(executionFlowGraph, dataDependencyGraph, callGraph);
        // Return a list of carved starting/returning intents, they are not separated !
        List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvingResult = carver.carve(activity, from);
        Assert.assertEquals(0, carvingResult.size());
        // TODO Ideally we want to assert that we repeate the lookup twice
//        Assert.assertThat(carvingResult.getFirst().getOrderedMethodInvocations(),
//                IsNot.not(IsEmptyCollection.emptyCollectionOf(MethodInvocation.class)));
    }

    // This is after the other activity is started but before it ends
    // TODO Test from left over methods ? 1675    838     [>];org.ametro.ui.activities.Map@96819702;<org.ametro.ui.activities.Map: void onPause()>;();

    
    @Test
    public void testActivityThatDoesStartAnotherActivity() throws FileNotFoundException, IOException, CarvingException {
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

        ObjectInstance activity = getObjectInstanceByObjectId(dataDependencyGraph,
                "org.ametro.ui.activities.Map@96819702");

        // This is when the results are received
        // 11837   5919    [>];org.ametro.ui.activities.Map@96819702;<org.ametro.ui.activities.Map: void onActivityResult(int,int,android.content.Intent)>;(1,-1,android.content.Intent@19629614);

        MethodInvocation from = getMethodInvocation(dataDependencyGraph, activity,
                "<org.ametro.ui.activities.Map: void onActivityResult(int,int,android.content.Intent)>_5919");


        RequestResponseIntentsCarver carver = new RequestResponseIntentsCarver(executionFlowGraph, dataDependencyGraph, callGraph);
        // Return a list of carved starting/returning intents, they are not separated !
        List<Pair<ExecutionFlowGraph, DataDependencyGraph>> carvingResult = carver.carve(activity, from);
        Assert.assertEquals(1, carvingResult.size());
        
        Pair<ExecutionFlowGraph, DataDependencyGraph> carved = carvingResult.get(0);
        Set<ObjectInstance > intents = new HashSet<>();
        for( ObjectInstance objectInstance : carved.getSecond().getObjectInstances()){
            if( objectInstance.getType().equals("android.content.Intent")) {
                intents.add( objectInstance );
            }
        }
        Assert.assertEquals("Wrong intents: " + intents, 2, intents.size()); 
        // TODO Ideally we want to assert that we repeate the lookup twice
//        Assert.assertThat(carvingResult.getFirst().getOrderedMethodInvocations(),
//                IsNot.not(IsEmptyCollection.emptyCollectionOf(MethodInvocation.class)));
    }
    
}
