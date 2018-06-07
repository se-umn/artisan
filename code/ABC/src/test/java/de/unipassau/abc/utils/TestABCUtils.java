package de.unipassau.abc.utils;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import de.unipassau.abc.ABCUtils;

public class TestABCUtils {

	@Test
	public void testGetTraceJarInTheEnd(){
		System.setProperty("java.class.path", System.getProperty("java.class.path") +File.pathSeparatorChar+"/tmp/random/trace.jar");
		Assert.assertEquals("/tmp/random/trace.jar" , ABCUtils.getTraceJar());
	}
	
	@Test
	public void testGetTraceJarInFront(){
		System.setProperty("java.class.path", "/tmp/random/trace.jar" +File.pathSeparatorChar+System.getProperty("java.class.path"));
		Assert.assertEquals("/tmp/random/trace.jar" , ABCUtils.getTraceJar());
	}
}
