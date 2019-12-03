package de.unipassau.abc.data;

import soot.Value;

/**
 * Wrap a parameter into a literal parameter matcher...
 * 
 * @author gambi
 *
 */
public class ParameterMatcherLiteralValue implements ValueNode {

	private int uniqueId;
	private DataNode parameter;

	public ParameterMatcherLiteralValue(int id, DataNode parameter) {
		this.parameter = parameter;
		this.uniqueId = id;
	}

	// THIS IS THE MOST GENERIC FORM SO OBJECT MATCH ANYTHING NULL INCLUDED
	public String getAnyParameterMatcher() {
		if (parameter instanceof PrimitiveValue) {
			String type = ((PrimitiveValue) parameter).getType();

			switch (type) {
			case "java.lang.String":
				return "org.mockito.ArgumentMatchers.any()";
			default:
				String capitalizedType = type.substring(0, 1).toUpperCase() + type.substring(1);
				/*
				 * anyBoolean(): Any boolean or non-null Boolean
				 */
				return "org.mockito.ArgumentMatchers.any" + capitalizedType + "()";
			}
		} else {
			/*
			 * any(): Matches anything, including nulls and varargs.
			 */
			return "org.mockito.ArgumentMatchers.any()";
		}
	}

	public String getEqParameterMatcher() {
		// Ideally this return Mockito.eq( ParameterValue )
		throw new RuntimeException("Not implemented");
	}

	@Override
	public Value getData() {
		throw new RuntimeException("Not implemented");
	}

}
