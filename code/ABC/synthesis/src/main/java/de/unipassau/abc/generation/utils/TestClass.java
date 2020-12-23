package de.unipassau.abc.generation.utils;

import java.util.Set;

import de.unipassau.abc.generation.data.CarvedTest;

public class TestClass {

	// List of tests to generate
	private Set<CarvedTest> carvedTests;
	// Simple Name of the Test Class
	private String name;
	// Package of the Test Class
	private String packageName;

	public TestClass(String packageName, String name, Set<CarvedTest> carvedTests) {
		super();
		this.packageName = packageName;
		this.name = name;
		this.carvedTests = carvedTests;
	}

	public String getPackageName() {
		return this.packageName;
	}

	public String getName() {
		return name;
	}

	public Set<CarvedTest> getCarvedTests() {
		return carvedTests;
	}
}
