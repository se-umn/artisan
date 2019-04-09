package de.unipassau.abc;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import de.unipassau.abc.utils.JimpleUtils;
import soot.Scene;
import soot.SootClass;

public class VariousTest {

    public class A {

        @Override
        public String toString() {
            return "this is A";
        }
    }

    public class B extends A {
        @Override
        public String toString() {
            return "this is B";
        }
    }

    @Test
    public void stringAsByteArrayRemoveNewLine() {
//        System.out.println("VariousTest " + ((byte) '\n') );
//        System.out.println("VariousTest " + ((byte) '\\') );
//        System.out.println("VariousTest " + ((byte) 'n') );
        
        byte[] cities = {'n', 'e', 'w', 'y', 'o', 'r', 'k', 10, 'd', 'c'};
        
        System.out.println( JimpleUtils.generateStringContent(Arrays.toString( cities )));

        
        
    }
    
    
    @Test
    public void arrayAsList() {
        Integer[] theArray = new Integer[] { 1, 2, 3 };

        // The list is backed up by the array: if the array changes the list
        // change
        List<Integer> theList = Arrays.asList(theArray);

        System.out.println(theList.get(0));

        theArray[0] = 5;

        System.out.println(theList.get(0));

        // But the list itself cannot change

        try {
            theList.remove(0);
            Assert.fail("Exception expected");
        } catch (UnsupportedOperationException e) {
            // Pass
        } catch (Throwable e) {
            Assert.fail("Unexpected exception" + e);
        }
    }

    @Test
    public void callSuper() {
        A a = new A();

        B b = new B();

        A c = (A) b;

        System.out.println("VariousTest.callSuper() " + a);
        System.out.println("VariousTest.callSuper() " + b);

        System.out.println("VariousTest.callSuper() " + c);

        c = a;

        System.out.println("VariousTest.callSuper() " + c);

    }

    @Test
    public void convertBytesToString() {
        byte[] stringContent = new byte[] { 100, 105, 114, 101, 99, 116, 95, 101, 100, 105, 116 };
        String s = new String(stringContent);
        System.out.println("VariousTest.convertBytesToString() " + s);
    }

    @Test
    public void multipleMatchingMocks() {
        // Create a mock
        java.util.List<Integer> aList = Mockito.mock(java.util.List.class);
        // Mock the same method twice with isAny()

        // The last mock is the one which wins unless refined
        // Mockito.when( aList.get(Mockito.anyInt())).thenReturn(-1);
        // Mockito.when( aList.get(Mockito.anyInt())).thenReturn(-2);
        // -> -2
        // Mockito.when( aList.get(Mockito.eq(0))).thenReturn(-1);
        // Mockito.when( aList.get(Mockito.anyInt())).thenReturn(-2);
        // -> -2

        //
        // Mockito.when( aList.get(Mockito.anyInt())).thenReturn(-2);
        // Mockito.when( aList.get(Mockito.eq(0))).thenReturn(-1);
        // -> -1, -2

        Mockito.doReturn(-2).when(aList).get(Mockito.anyInt());
        Mockito.doReturn(-1).when(aList).get(Mockito.eq(0));

        // Call it twice or thrice and check what's returned
        System.out.println("VariousTest.multipleMatchingMocks() " + aList.get(1));
        System.out.println("VariousTest.multipleMatchingMocks() " + aList.get(0));
        System.out.println("VariousTest.multipleMatchingMocks() " + aList.get(2));
    }

    @Test
    public void typeInference() {
        // Soot has problems in working on mac.
        String osName = System.getProperty("os.name");
        System.setProperty("os.name", "Whatever");
        Scene.v().loadNecessaryClasses();
        System.setProperty("os.name", osName);

        SootClass theArraysClass = Scene.v().getSootClass(Arrays.class.getName());
        //
        // private static class ArrayList<E> extends AbstractList<E>

    }
}
