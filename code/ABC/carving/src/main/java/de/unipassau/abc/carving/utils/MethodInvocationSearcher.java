package de.unipassau.abc.carving.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.Triplette;
import de.unipassau.abc.parsing.ParsedTrace;

/**
 * 
 * Utility class that looks for specific methods
 * 
 * @author gambitemp
 *
 */
public class MethodInvocationSearcher {

	/**
	 * Return the list of method invocations that can be carved. Carvable method
	 * invocations are non-synthetic public method calls that belong to the app
	 * 
	 * @param parsedTrace
	 * @return
	 */
	public Collection<MethodInvocation> findAllCarvableMethodInvocations(ParsedTrace parsedTrace) {
		Collection<MethodInvocation> carvableMethodInvocations = new HashSet<MethodInvocation>();

		for (Entry<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> entry : parsedTrace
				.getParsedTrace().entrySet()) {
			entry.getValue().getFirst().getOrderedMethodInvocations().parallelStream()
					.filter(mi -> !mi.isPrivate() && !mi.isSynthetic() && !mi.isLibraryCall())
					.forEachOrdered(carvableMethodInvocations::add);
		}
		return carvableMethodInvocations;

	}
}
