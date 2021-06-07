package de.unipassau.abc.carving.utils;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

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
	 * invocations are non-synthetic public method calls that belong to the app.
	 * Any method that contains $ will be ignored as well, as those are usually dynamically generated
	 * 
	 * For example, the static method:
	 * 		<abc.basiccalculator.ResultActivity: android.widget.TextView access$000(abc.basiccalculator.ResultActivity)>;(abc.basiccalculator.ResultActivity@107229557);

	 * 
	 * @param parsedTrace
	 * @return
	 */
	public Set<MethodInvocation> findAllCarvableMethodInvocations(ParsedTrace parsedTrace) {
		Set<MethodInvocation> carvableMethodInvocations = new HashSet<MethodInvocation>();

		for (Entry<String, Triplette<ExecutionFlowGraph, DataDependencyGraph, CallGraph>> entry : parsedTrace
				.getParsedTrace().entrySet()) {
			entry.getValue().getFirst().getOrderedMethodInvocations().parallelStream()
					.filter(mi -> !mi.isPrivate() && !mi.isSynthetic() && !mi.isLibraryCall())
					.filter( mi -> ! mi.getMethodSignature().contains("$")) // Inner Classes
					.filter( mi -> ! mi.getMethodSignature().contains("clinit")) // Static constructor
					.forEachOrdered(carvableMethodInvocations::add);
		}
		return carvableMethodInvocations;

	}
}
