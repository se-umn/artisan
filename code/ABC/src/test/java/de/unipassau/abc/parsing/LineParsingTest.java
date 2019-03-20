package de.unipassau.abc.parsing;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

import de.unipassau.abc.tracing.Trace;
import de.unipassau.abc.utils.JimpleUtils;

public class LineParsingTest {

    // [>];com.farmerbb.notepad.activity.MainActivity@106000899;<com.farmerbb.notepad.activity.MainActivity:
    // void <init>()>;();
    @Test
    public void parseMethodStart() {

        String expectedToken = Trace.METHOD_START_TOKEN;
        String expectedMethodOwner = "com.farmerbb.notepad.activity.MainActivity@106000899";
        String expectedMethodSignature = "<com.farmerbb.notepad.activity.MainActivity: void <init>()>";

        String traceLine = expectedToken + ";" + expectedMethodOwner + ";" + expectedMethodSignature + ";(Bla, bli);";

        String[] parsedLine = Trace.parseLine(traceLine);
        
        String actualToken = parsedLine[0];
        String actualMethodOwner = parsedLine[1];
        String actualMethodSignature = parsedLine[2];

        Assert.assertEquals(expectedToken, actualToken);
        Assert.assertEquals(expectedMethodOwner, actualMethodOwner);
        Assert.assertEquals(expectedMethodSignature, actualMethodSignature);

    }

    @Test
    public void parseStaticMethodStart() {

        String expectedToken = Trace.METHOD_START_TOKEN;
        // Static methods do not have an owner
        String expectedMethodOwner = "";
        String expectedMethodSignature = "<com.farmerbb.notepad.activity.MainActivity: void <init>()>";

        String traceLine = Trace.METHOD_START_TOKEN + ";" + expectedMethodOwner + ";" + expectedMethodSignature + ";();";

        String[] parsedLine = Trace.parseLine(traceLine);
        
        String actualToken = parsedLine[0];
        String actualMethodOwner = parsedLine[1];
        String actualMethodSignature = parsedLine[2];

        Assert.assertEquals(expectedToken, actualToken);
        Assert.assertEquals(expectedMethodOwner, actualMethodOwner);
        Assert.assertEquals(expectedMethodSignature, actualMethodSignature);
    }

    @Test
    public void testGetActualParametersForVoidMethod() {
        String traceLine = "[>];android.content.SharedPreferences@119059907;<android.content.SharedPreferences: java.lang.String getString()>;();";

        String[] expectedParameters = new String[0];

        String[] parsedLine = Trace.parseLine(traceLine);

        String methodSignature = parsedLine[2];

        String parameterString = parsedLine[3];

        String[] actualParametersOrReturnValue = Trace.getActualParameters(methodSignature, parameterString);

        Assert.assertArrayEquals(expectedParameters, actualParametersOrReturnValue);
    }

    @Test
    public void parserStartMethodWithStringParameters() {

        String expectedString = "This is it \n bla";

        String expectedStringAsBytes = Arrays.toString(expectedString.getBytes());

        String traceLine = "[>];android.content.SharedPreferences@119059907;<android.content.SharedPreferences: java.lang.String getString(java.lang.String,java.lang.String)>;("
                + expectedStringAsBytes + ",[]);";

        String[] expectedParameters = new String[] { expectedStringAsBytes, "[]" };

        String[] parsedLine = Trace.parseLine(traceLine);

        String methodSignature = parsedLine[2];

        String parameterString = parsedLine[3];

        String[] actualParametersOrReturnValue = Trace.getActualParameters(methodSignature, parameterString);

        Assert.assertArrayEquals(expectedParameters, actualParametersOrReturnValue);
    }

    @Test
    public void parseEndMethod() {
        String traceLine = "[>>];android.support.v7.app.b$a@244927587;<android.support.v7.app.b$a: void <init>(android.content.Context)>;(com.farmerbb.notepad.activity.MainActivity@89520409);";
        
        String[] parsedLine = Trace.parseLine(traceLine);
        Assert.assertEquals(4, parsedLine.length);
        
        // Flag the call as library or user
        String expectedOpeningToken = Trace.LIB_METHOD_START_TOKEN;
        
        // Method owner if any or empty string otherwise
        String methodOwner = "android.support.v7.app.b$a@244927587";
                
        // Method Signature - a la JIMPLE
        String methodSignature = "<android.support.v7.app.b$a: void <init>(android.content.Context)>";

        String parameterString = "com.farmerbb.notepad.activity.MainActivity@89520409";

        Assert.assertEquals( expectedOpeningToken, parsedLine[0]);
        Assert.assertEquals( methodOwner, parsedLine[1]);
        Assert.assertEquals( methodSignature, parsedLine[2]);
        Assert.assertEquals( parameterString, parsedLine[3]);
        
        String[] expectedActualParameters = new String[] {"com.farmerbb.notepad.activity.MainActivity@89520409"};
        String[] actualParametersOrReturnValue = Trace.getActualParameters(methodSignature, parameterString);
        Assert.assertArrayEquals(expectedActualParameters, actualParametersOrReturnValue);
    }

    @Test
    public void parserStartMethodWithStringParametersButNullValue() {
        String traceLine = "[>];android.content.SharedPreferences@119059907;<android.content.SharedPreferences: java.lang.String getString(java.lang.String,java.lang.String)>;([110, 111, 114, 109, 97, 108],);";

        String[] expectedParameters = new String[] { "[110, 111, 114, 109, 97, 108]", null };

        Matcher matcher = Trace.compoundPattern.matcher(traceLine);

        boolean found = matcher.find();

        // Flag the call as library or user
        String openingToken = matcher.group(1);
        // Method owner if any or empty string otherwise
        String methodOwner = matcher.group(2);
        // Method Signature - a la JIMPLE
        String methodSignature = matcher.group(3);

        // Count formalParameters
        String[] formalParameters = JimpleUtils.getParameterList(methodSignature);
        Assert.assertEquals(expectedParameters.length, formalParameters.length);

        // At this point take the remainder of the String and parse the
        // parameters or the return value
        String parameterString = traceLine.substring(matcher.end(), traceLine.length());
        // Remove the opening ( and closing );
        parameterString = parameterString.substring(1, parameterString.length() - 2);

        String[] actualParametersOrReturnValue = Trace.getActualParameters(methodSignature, parameterString);

        Assert.assertArrayEquals(expectedParameters, actualParametersOrReturnValue);
    }

    @Test
    public void parserStartMethodWithStringParametersButNullValueAsFirst() {
        String traceLine = "[>];android.content.SharedPreferences@119059907;<android.content.SharedPreferences: java.lang.String getString(java.lang.String,java.lang.String)>;(,[110, 111, 114, 109, 97, 108]);";

        String[] expectedParameters = new String[] { null, "[110, 111, 114, 109, 97, 108]" };

        Matcher matcher = Trace.compoundPattern.matcher(traceLine);

        boolean found = matcher.find();

        // Flag the call as library or user
        String openingToken = matcher.group(1);
        // Method owner if any or empty string otherwise
        String methodOwner = matcher.group(2);
        // Method Signature - a la JIMPLE
        String methodSignature = matcher.group(3);

        // Count formalParameters
        String[] formalParameters = JimpleUtils.getParameterList(methodSignature);
        Assert.assertEquals(expectedParameters.length, formalParameters.length);

        // At this point take the remainder of the String and parse the
        // parameters or the return value
        String parameterString = traceLine.substring(matcher.end(), traceLine.length());
        // Remove the opening ( and closing );
        parameterString = parameterString.substring(1, parameterString.length() - 2);

        String[] actualParametersOrReturnValue = Trace.getActualParameters(methodSignature, parameterString);

        Assert.assertArrayEquals(expectedParameters, actualParametersOrReturnValue);
    }
}
