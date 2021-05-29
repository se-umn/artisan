package de.unipassau.abc.parsing;

import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.is;
import static org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__.out;


import de.unipassau.abc.carving.android.AndroidActivityCarver;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.exceptions.ABCException;
import java.io.File;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

/**
 * This parser generate a graph with Apache TinkerPop Gremlin. I am not sure
 * about it now, but probably there's no point to store the "Method Invocation
 * Objects" here. Possibly we can have the attributes and properties and
 * recreate the abstraction if needed?
 * 
 * @author gambi
 *
 */
public class GremlinParser extends TraceParser {

	/**
	 * SOME NOTES: Link method invocations and parameter with a PARAMETER link, with
	 * a LABEL/POSITION property
	 */

	// Store directly the vertex instead of storing the MethodInvocation and then
	// retrieve it everytime...
	private Stack<Vertex> stack;

	private Graph graph;
	private GraphTraversalSource g;
	private GremlinParsedTrace parsedTrace;

	public final static String META = "META";
	private Vertex startOfExecution;
	private Vertex endOfExecution;

	private Vertex lastMethodInvocation;
	private boolean lastReturnExceptional;

	public final static String METHOD_INVOCATION_NODE_LABEL = "method invocation";
	public final static String DATA_NODE_LABEL = "method invocation";

	public GremlinParser() {
	  super(false);
		// TODO Maybe we need to replace TinkerGraph with something else...
		graph = TinkerGraph.open();
		g = graph.traversal();
		stack = new Stack<Vertex>();

	}

	// TODO Maybe we need to call this as startOfExecution
	@Override
	public ParsedTrace setupParsedTrace(File traceFile) {
		this.parsedTrace = new GremlinParsedTrace(traceFile.getName(), graph);

		this.startOfExecution = g.addV(META).property("methodInvocation", "START").next();

		return this.parsedTrace;
	}

	@Override
	/**
	 * Link the last method invocation to the meta-node END_EXECUTION
	 */
	public void endOfExecution() {
		this.endOfExecution = g.addV(META).property("methodInvocation", "END").next();
		g//
				.addE("succ").from(lastMethodInvocation).to(endOfExecution)//
				.addE("prev").from(endOfExecution).to(lastMethodInvocation)//
				.iterate();
	}

	@Override
	public void validate() throws ABCException {
		System.out.println("GremlinParser.validate(): Vertex: " + g.V().toList().size());
		System.out.println("GremlinParser.validate(): Edges " + g.E().toList().size());

		// Starting from the initial node, print the sequences of invocations done

		System.out.println("GremlinParser.validate()" +
		// Starting from the META-START
				g.V(startOfExecution).repeat(out("succ")).until(is(endOfExecution)).path().by("methodInvocation")
						.toList());

		if (!stack.isEmpty()) {
			throw new ABCException("Stack is not empty by the end of parsing!");
		}

	}

	private void methodStart(ParsedLine tokens, //
			MethodInvocation methodInvocation) throws ABCException {

		logger.debug("Starting" + methodInvocation);
		// Add the new node
		Vertex current = g.addV(METHOD_INVOCATION_NODE_LABEL)//
				.property("methodInvocation", methodInvocation)//
				.next();

		// Unless already set the previous is START
		Vertex previous = lastMethodInvocation != null ? lastMethodInvocation : startOfExecution;

		// TODO Do we need to add an ID to match the line number in the trace?
		g//
				.addE("prev").from(current).to(previous)//
				.addE("succ").from(previous).to(current)//
				.iterate();

		// Store this as the last method that started
		lastMethodInvocation = current;

		stack.push(current);

	}

	@Override
	public void parseMethodInvocation(AtomicInteger globalInvocationCount, ParsedLine tokens) throws ABCException {
		int invocationTraceId = MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT;
		try{
			invocationTraceId = Integer.parseInt(tokens.lineNumber);
		}
		catch(Exception e){

		}
		MethodInvocation methodInvocation = new MethodInvocation(invocationTraceId, globalInvocationCount.incrementAndGet(),
				tokens.methodSignature);

		methodStart(tokens, methodInvocation);

	}

	@Override
	public void parsePrivateMethodInvocation(AtomicInteger globalInvocationCount, ParsedLine tokens)
			throws ABCException {
		int invocationTraceId = MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT;
		try{
			invocationTraceId = Integer.parseInt(tokens.lineNumber);
		}
		catch(Exception e){

		}
		MethodInvocation methodInvocation = new MethodInvocation(invocationTraceId, globalInvocationCount.incrementAndGet(),
				tokens.methodSignature);

		methodStart(tokens, methodInvocation);
	}

	@Override
	public void parseSyntheticMethodInvocation(AtomicInteger globalInvocationCount, ParsedLine tokens)
			throws ABCException {
		int invocationTraceId = MethodInvocation.INVOCATION_TRACE_ID_NA_CONSTANT;
		try{
			invocationTraceId = Integer.parseInt(tokens.lineNumber);
		}
		catch(Exception e){

		}
		MethodInvocation methodInvocation = new MethodInvocation(invocationTraceId, globalInvocationCount.incrementAndGet(),
				tokens.methodSignature);

		methodStart(tokens, methodInvocation);

	}

	@Override
	public void parseMethodReturn(AtomicInteger globalInvocationCount, ParsedLine tokens) throws ABCException {
		Vertex returning = stack.pop();
		this.lastReturnExceptional = false;

		if (!stack.isEmpty()) {
			Vertex caller = stack.peek();
			g//
					.addE("call").from(caller).to(returning)//
					.addE("return").property("exceptional", this.lastReturnExceptional).from(returning).to(caller)//
					.iterate();
		}

	}

	@Override
	public void parseMethodReturnWithException(AtomicInteger globalInvocationCount, ParsedLine tokens)
			throws ABCException {
		Vertex returning = stack.pop();
		this.lastReturnExceptional = true;

		if (!stack.isEmpty()) {
			Vertex caller = stack.peek();
			g//
					.addE("call").from(caller).to(returning)//
					.addE("return").property("exceptional", this.lastReturnExceptional).from(returning).to(caller)//
					.iterate();
		}
	}

}
