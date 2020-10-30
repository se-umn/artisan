package de.unipassau.abc.exceptions;

import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.Triplette;

public class ABCException extends Exception {

	private static final long serialVersionUID = 2527692252566022668L;

	private Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvedTest;
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

	public void setCarvedTest(Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> carvedTest) {
		this.carvedTest = carvedTest;
	}

	public Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph> getCarvedTest() {
		return carvedTest;
	}

	public void setBrokenInstance(ObjectInstance instance) {
		this.brokenInstance = instance;
	}

	public ObjectInstance getBrokenInstance() {
		return brokenInstance;
	}
}
