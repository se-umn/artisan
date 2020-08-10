package de.unipassau.abc.utils;

import java.io.File;

import org.junit.Test;

import com.thoughtworks.xstream.XStream;

import de.unipassau.abc.data.DataDependencyGraphImpl;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.Pair;

public class VisualizeTest {

    @Test
    public void loadTestAndVisualizeIt(){
//        "com.farmerbb.notepad.activity.MainActivity.getCabArray_190"
//        com.farmerbb.notepad.activity.MainActivity.getCabArray_277
        // com.farmerbb.notepad.activity.MainActivity.getPreferences_11
        
        File carvedTestFile = new File("src/test/resources/android-28-carved-tests/com.farmerbb.notepad.activity.MainActivity.getPreferences_11");
        XStream xStream = new XStream();
        
        Pair<ExecutionFlowGraph, DataDependencyGraphImpl> carvedTest = (Pair<ExecutionFlowGraph, DataDependencyGraphImpl>) xStream.fromXML(carvedTestFile);
        
        ExecutionFlowGraph executionFlowGraph = carvedTest.getFirst();
        System.out.println("VisualizeTest.loadTestAndVisualizeIt() " + executionFlowGraph );
        System.out.println("VisualizeTest.loadTestAndVisualizeIt() " + executionFlowGraph.getOrderedMethodInvocations() );
//        carvedTest.getSecond().visualize();
        
    }
}
