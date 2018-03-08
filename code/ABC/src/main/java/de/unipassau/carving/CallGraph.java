package de.unipassau.carving;

import java.awt.Dimension;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class CallGraph {

	private final AtomicInteger id = new AtomicInteger(0);

	private Graph<MethodInvocation, String> g;

	private Stack<MethodInvocation> stack;

	public CallGraph() {
		g = new SparseMultigraph<MethodInvocation, String>();
		stack = new Stack<MethodInvocation>();
	}

	public void push(MethodInvocation methodInvocation) {
		stack.push(methodInvocation);
		if (g.containsVertex(methodInvocation)) {
			g.addVertex(methodInvocation);
		}
	}

	public MethodInvocation pop() {
		MethodInvocation callee = stack.pop();
		if (!stack.isEmpty()) {
			MethodInvocation caller = stack.peek();
			g.addEdge("CallDependency-" + id.getAndIncrement(), caller, callee, EdgeType.DIRECTED);
		}
		return callee;
	}

	public void visualize() {
		VisualizationViewer<MethodInvocation, String> vv = new VisualizationViewer<MethodInvocation, String>(
				new KKLayout<MethodInvocation, String>(g));

		vv.setPreferredSize(new Dimension(1000, 800));
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller() {
			@Override
			public String apply(Object node) {
				if (node instanceof ObjectInstance) {
					return ((ObjectInstance) node).getObjectId();
				} else if (node instanceof MethodInvocation) {
					return ((MethodInvocation) node).getJimpleMethod();
				} else {
					return super.apply(node);
				}
			}
		});

		JFrame frame = new JFrame("Call Graph View");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);

	}
	// public boolean addVertices(String data){
	// return g.addVertex(data);
	// }
	// public boolean addEdges(String v1,String v2){
	// return g.addEdge("Edge"+Math.random(), v2, v1,EdgeType.DIRECTED);
	// }
	// public boolean verticeExists(String data){
	// return g.containsVertex(data);
	// }

	// This probably can be optimized
	public Set<MethodInvocation> getMethodInvocationsSubsumedBy(MethodInvocation methodInvocation) {
		Set<MethodInvocation> subsumedCalls = new HashSet<>();
		for( MethodInvocation mi : g.getSuccessors( methodInvocation ) ){
			subsumedCalls.addAll( getMethodInvocationsSubsumedBy(mi) );
			subsumedCalls.add(mi);
		}
		return subsumedCalls;
	}

	public MethodInvocation peek() {
		return stack.peek();
	}
}
