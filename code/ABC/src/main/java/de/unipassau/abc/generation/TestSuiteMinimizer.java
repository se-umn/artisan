package de.unipassau.abc.generation;

import java.util.HashSet;
import java.util.Set;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;

public class TestSuiteMinimizer {
	
	private Set<CompilationUnit> testClasses;
	private String resetEnvironmentBy;
	private TestSuiteExecutor testSuiteExecutor;
	
	public TestSuiteMinimizer(Set<CompilationUnit> allTestClasses, String resetEnvironmentBy, TestSuiteExecutor testSuiteExecutor) {
		this.testClasses = new HashSet<>( allTestClasses );
		this.resetEnvironmentBy = resetEnvironmentBy;
		this.testSuiteExecutor = testSuiteExecutor;
	}

	public boolean isDone() {
		return testClasses.isEmpty();
	}

	public void performAMinimizationStep() {
		// TODO Auto-generated method stub
	
		Set<MethodDeclaration> failedTests = testSuiteExecutor.executeAndReportFailedTests( testClasses );
	}

}
