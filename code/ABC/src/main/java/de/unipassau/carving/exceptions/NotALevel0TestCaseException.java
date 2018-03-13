package de.unipassau.carving.exceptions;

import java.util.List;

import de.unipassau.carving.MethodInvocation;

public class NotALevel0TestCaseException extends Exception {

	private static final long serialVersionUID = -4249043711042731323L;

	private MethodInvocation subsumedMethodInvocation;
	private MethodInvocation subsumingMethodInvocation;
	private List<MethodInvocation> subsumingPath;

	public NotALevel0TestCaseException(MethodInvocation subsumedMethodInvocation,
			MethodInvocation subsumingMethodInvocation, List<MethodInvocation> subsumingPath) {
		this.subsumedMethodInvocation = subsumedMethodInvocation;
		this.subsumingMethodInvocation = subsumingMethodInvocation;
		this.subsumingPath = subsumingPath;
	}

	public MethodInvocation getSubsumedMethodInvocation() {
		return subsumedMethodInvocation;
	}

	public MethodInvocation getSubsumingMethodInvocation() {
		return subsumingMethodInvocation;
	}
	
	public List<MethodInvocation> getSubsumingPath() {
		return subsumingPath;
	}

}
