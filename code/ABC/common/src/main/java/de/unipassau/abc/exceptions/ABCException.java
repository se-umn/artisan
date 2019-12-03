package de.unipassau.abc.exceptions;

import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Pair;

public class ABCException extends Exception {

	private static final long serialVersionUID = 2527692252566022668L;

	private Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest;
	private ObjectInstance brokenInstance;

	public ABCException() {
		super();
	}

	public ABCException(String message) {
		super(message);
	}

	public ABCException(String message, Throwable cause) {
		super(message, cause);
	}

	public void setCarvedTest(Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest) {
		this.carvedTest = carvedTest;
	}

	public Pair<ExecutionFlowGraph, DataDependencyGraph> getCarvedTest() {
		return carvedTest;
	}

	public void setBrokenInstance(ObjectInstance instance) {
		this.brokenInstance = instance;
	}

	public ObjectInstance getBrokenInstance() {
		return brokenInstance;
	}
}
