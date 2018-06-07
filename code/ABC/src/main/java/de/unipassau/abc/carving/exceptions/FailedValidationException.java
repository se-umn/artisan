package de.unipassau.abc.carving.exceptions;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FailedValidationException extends CarvingException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6275219783235104002L;
	
	Set<String> invalidTestCases = new HashSet();
	
	public FailedValidationException(String message) {
		super(message);
	}

	public void addInvalidTestCases(List<String> invalidTestCases ){
		this.invalidTestCases.addAll( invalidTestCases );
	} 
	
	public Set<String> getInvalidTestCases() {
		return invalidTestCases;
	}

}
