package de.unipassau.abc.carving.exceptions;

import de.unipassau.abc.carving.DataDependencyGraph;
import de.unipassau.abc.carving.ExecutionFlowGraph;
import de.unipassau.abc.carving.ObjectInstance;
import de.unipassau.abc.data.Pair;

public class CarvingException extends Exception {

	private static final long serialVersionUID = -7157628861264610513L;
	private Pair<ExecutionFlowGraph, DataDependencyGraph> carvedTest;
	private ObjectInstance brokenInstance;

	public CarvingException() {
		super();
	}

	public CarvingException(String message) {
		super(message);
	}

	public CarvingException(String message, Throwable cause) {
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
