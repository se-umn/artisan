package de.unipassau.abc.generation.mocks;

import java.util.ArrayList;
import java.util.List;

import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;

public class CarvingShadow {

	private String stubbedType;
	private List<String> stubbedMethods;
	private String shadowName;

	public CarvingShadow(String stubbedType, String stubbedName) {
		this.stubbedType = stubbedType;
		this.stubbedMethods = new ArrayList<String>();
		this.shadowName = stubbedName;
	}

	public String getStubbedType() {
		return stubbedType;
	}

	public List<String> getStubbedMethods() {
		return stubbedMethods;
	}

	public String getShadowName() {
		return shadowName;
	}


}
