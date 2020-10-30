package de.unipassau.abc.carving.android;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;

import de.unipassau.abc.carving.exceptions.CarvingException;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.MethodInvocationMatcher;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.ObjectInstanceFactory;
import de.unipassau.abc.data.Pair;
import edu.emory.mathcs.backport.java.util.Arrays;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import soot.Scene;
import soot.SootClass;

/**
 * Represent a simplified call graph of the activity navigation
 * 
 * @author gambi
 *
 */
public class ActivityGraph {

	private enum RelationType {
		// Define parent and child activities for "Up" navigation. ?
		LIFE_CYCLE, // Links two instances of the same type after a restart
		START, // always considered different (mutst be differnt type or
				// different instance or both)
		START_WITH_RESULT, // always considered different (mutst be differnt
							// type or different instance or both)
		RETURN, // with result - This is the same as going back
		RETURN_WITH_RESULT, // with result
		LEFT_OVER, // Life-cycle events on the activity which runs in parallel
					// with a second activity, onPause, on Stop, etc...
		SAME, // Logically the same activity before and after
				// navigation/return/back
		//
		BEFORE; // Temporal information
		// Data dependencies
	}

	private final AtomicInteger id = new AtomicInteger(0);
	private final static Logger logger = LoggerFactory.getLogger(ActivityGraph.class);

	// Call graph informations - level of nesting - matching activities
	// The Activity with the context: First lifecycle - Last lifecycle
	private Stack<Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>>> stack;
	//
	private Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> startOfMainActivity;
	private Graph<Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>>, String> graph;
	private Map<Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>>, Pair<ObjectInstance, MethodInvocation>> requestIntents;

	// Context associate a level to a starting-ending of an activity ?

	private CallGraph callGraph;
	private ExecutionFlowGraph executionFlowGraph;
	private DataDependencyGraph dataDependencyGraph;

	public ActivityGraph(CallGraph callGraph, ExecutionFlowGraph executionFlowGraph,
			DataDependencyGraph dataDependencyGraph) {
		graph = new DirectedSparseMultigraph<Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>>, String>();
		stack = new Stack<Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>>>();
		requestIntents = new HashMap<Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>>, Pair<ObjectInstance, MethodInvocation>>();
		//
		this.callGraph = callGraph;
		this.executionFlowGraph = executionFlowGraph;
		this.dataDependencyGraph = dataDependencyGraph;
	}

	// Build up the navigation/activity graph
	// TODO Include reference to starting methods, intents, return values, etc
	public void add(ObjectInstance activityToAdd, MethodInvocation context) throws CarvingException {
		if (Arrays.asList(new String[] { "onPause", "onStop", "onSaveInstanceState" })
				.contains(JimpleUtils.getMethodName(context.getMethodSignature()))) {

			logger.info(context + "is a leftover from " + activityToAdd);

			// Current node:
			// Add a LEFT_OVER NODE or update the one that is already there. Two
			// cases: either the current activity is on the top of the stack or
			// is the element under it. This is because two activities run in
			// "parallel"

			Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> currentNode = stack.peek();

			// We check for equality this is inaccurate since an activity might
			// have been restarted in the meanwhile...
			if (currentNode.getFirst().equals(activityToAdd)) {
				// logger.info("Found matching instance for LEFT OVER " +
				// currentNode );
			} else {
				// We need to look one step inside the stack
				Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> previousNode = stack.pop();
				currentNode = stack.peek();
				stack.push(previousNode);

				if (!currentNode.getFirst().equals(activityToAdd)) {
					throw new CarvingException("Cannot find MATCHING instance for LEFT OVER");
				}
			}

			Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> leftOverNode = null;
			for (String outEdge : graph.getOutEdges(currentNode)) {
				if (outEdge.startsWith(RelationType.LEFT_OVER.name())) {
					leftOverNode = graph.getOpposite(currentNode, outEdge);
					// Update the context of this node
					leftOverNode.getSecond().setSecond(context);
					logger.info("Updating the context of LEFT OVER : " + leftOverNode);
					return;
				}
			}
			// Add the LEFT_OVER NODE
			leftOverNode = new Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>>(activityToAdd,
					new Pair<MethodInvocation, MethodInvocation>(context, context));
			graph.addVertex(leftOverNode);
			boolean added = graph.addEdge(RelationType.LEFT_OVER + "_" + id.getAndIncrement(), //
					currentNode, leftOverNode, EdgeType.DIRECTED);
			if (!added) {
				throw new CarvingException("Cannot add " + (RelationType.LEFT_OVER + "_" + id.get()) + " " + currentNode
						+ " " + leftOverNode);
			}
			logger.info("Adding LEFT OVER : " + leftOverNode);
			return;
		}

		if (stack.isEmpty()) {
			// Root/Main activity:
			// TODO Check this is the initial start
			Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> data = new Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>>(
					activityToAdd, new Pair<MethodInvocation, MethodInvocation>(context, context));

			// Register the activity in the graph
			graph.addVertex(data);
			// Push the activity with the context in the stack
			stack.push(data);

			if (startOfMainActivity == null) {
				logger.info("Starting of MAIN activity" + data);
				startOfMainActivity = data;
			} else {
				// This is another Root activity, but not the initial one.
				logger.info("Starting of a Root activity" + data);
				boolean added = graph.addEdge(RelationType.BEFORE + "_" + id.getAndIncrement(), //
						startOfMainActivity, data, EdgeType.DIRECTED);
				if (!added) {
					throw new CarvingException("Cannot add " + (RelationType.BEFORE + "_" + id.get()) + " "
							+ startOfMainActivity + " -> " + data);
				}
			}
			requestIntents.put(data,
					new Pair<ObjectInstance, MethodInvocation>(ObjectInstanceFactory.getSystemIntent(), context));
		} else {
			ObjectInstance parentActivity = stack.peek().getFirst();
			String parentActivityType = parentActivity.getType();
			Pair<MethodInvocation, MethodInvocation> parentActivityContext = stack.peek().getSecond();

			if (parentActivity.equals(activityToAdd)) {
				parentActivityContext.setSecond(context);
				// Update the context.
				logger.info(context + " is a life cycle event of " + activityToAdd + " Updated activity context "
						+ parentActivityContext);
				/*
				 * TODO if (!isLifeCycleOfSameActivityTypeComplete(executionFlowGraph,
				 * dataDependencyGraph)) { logger.warn(
				 * "Carved test contains more instances of the same activity but activities do not complete their lifecycle"
				 * ); throw new CarvingException("Inconsistent activity lifecycle !"); }
				 * 
				 */
			} else {

				/*
				 * The lifecycle of different activities might be interleaved: for example when
				 * an activity starts the other stops, so the onPause, onStop, onDestroy, etc
				 * might be called after onCreate of the second activity. Those
				 * "interleaved methods" must be linked as life_cycle event of the previous one.
				 * 
				 * 
				 * activity2 starts: A1.onPause, A1.onStop, A1.onSavedInstance of activity1 does
				 * count only as life-cycle for A1, not return to A1
				 */

				// Look up using the last method of the previous invocation as
				// context to narrow down the research of the starting method
				Pair<ObjectInstance, MethodInvocation> currentInstance = new Pair<ObjectInstance, MethodInvocation>(
						activityToAdd, context);
				// We use the starting point instead of the last one to ensure
				// we capture the start method: parallel activity licecycles.
				Pair<ObjectInstance, MethodInvocation> previousInstance = new Pair<ObjectInstance, MethodInvocation>(
						parentActivity, parentActivityContext.getFirst());
				// TODO We might need to consider the synchpoints here !
				// otherwise we cannot find the intent !
				// This identifies the Intent used to start the activity and the
				// starting method
				Pair<ObjectInstance, MethodInvocation> intentWithStartingMethod = findTheStartingIntentBetweenInstances(
						currentInstance, previousInstance);

				// Data of the new activity
				Pair<MethodInvocation, MethodInvocation> newActivityContext = new Pair<MethodInvocation, MethodInvocation>(
						context, context);
				Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> newData = new Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>>(
						activityToAdd, newActivityContext);

				if (parentActivityType.equals(activityToAdd.getType()) && intentWithStartingMethod == null) {
					// TODO Is this even possible !? When an activity navigate
					// to another activity of the same type ?!
					// if
					// (!isLifeCycleOfSameActivityTypeComplete(executionFlowGraph,
					// dataDependencyGraph)) {
					// logger.warn(
					// "Carved test contains more instances of the same activity
					// but activities do not complete their lifecycle");
					// throw new CarvingException("Inconsistent activity
					// lifecycle !");
					// }

					// Weird life-cycle events - activity was destroyed and
					// recreated, but logically it's a navigation event.
					logger.info("Activity " + parentActivityType + " restarted as " + activityToAdd);
					// Add to graph and link the two nodes
					//
					graph.addVertex(newData);
					// Remove from the stack the old data !
					Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> oldData = stack.pop();
					//
					boolean added = graph.addEdge(RelationType.LIFE_CYCLE + "_" + id.getAndIncrement(), //
							oldData, newData, EdgeType.DIRECTED);
					if (!added) {
						throw new CarvingException("Cannot add " + (RelationType.LIFE_CYCLE + "_" + id.get()) + " "
								+ oldData + " " + newData);
					}
					// Swap stack top
					stack.push(newData);
				} else if (!parentActivityType.equals(activityToAdd.getType()) && intentWithStartingMethod != null) {
					// Different activities, and intent : starting of new
					// activity
					logger.info("Activity " + parentActivity + " started " + activityToAdd + " with Intent "
							+ intentWithStartingMethod);
					//
					graph.addVertex(newData);
					// Get reference of the previous data
					Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> oldData = stack.peek();
					// Link the two
					boolean added = graph.addEdge(RelationType.START + "_" + id.getAndIncrement(), oldData, newData,
							EdgeType.DIRECTED);
					if (!added) {
						throw new CarvingException(
								"Cannot add " + (RelationType.START + "_" + id.get()) + " " + oldData + " " + newData);
					}
					//
					requestIntents.put(newData, intentWithStartingMethod);
					//
					stack.push(newData);

				} else if (!parentActivityType.equals(activityToAdd.getType()) && intentWithStartingMethod == null) {
					// This can be either A1 returning after A2 or, left over of
					// A1:
					// activity2 starts: A1.onPause, A1.onStop,
					// A1.onSavedInstance of activity1 does count only as
					// life-cycle for A1, not return to A1

					// Check for left overs ! TODO There might be more

					// Different activities, but no intent : returning into
					// previous activity
					logger.info("Activity " + activityToAdd + " returned from " + parentActivity + " with " + context);
					// Remove parent activity from the stack
					Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> oldData = stack.pop();
					// At the top pf the stack there's the original calling
					// activity, but the context is the calling context, not the
					// returning one,
					// So we need to consider this in the following
					Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> callingContext = stack.pop();
					//
					stack.push(newData);
					// Link the return into.
					graph.addVertex(newData);
					// Link the Activity returning with the activity in the
					// returning Context (newData)
					boolean added = graph.addEdge(RelationType.RETURN + "_" + id.getAndIncrement(),
							// Pay attention this is reversed because we are
							// returning...
							oldData, newData, EdgeType.DIRECTED);
					if (!added) {
						throw new CarvingException(
								"Cannot add " + (RelationType.RETURN + "_" + id.get()) + " " + newData + " " + oldData);
					}
					// Link the Activity before and after the invocation, this
					// might be actually two different instances...
					// Link is bidirectional
					added = graph.addEdge(RelationType.SAME + "_" + id.getAndIncrement(),
							// Pay attention this is reversed because we are
							// returning...
							newData, callingContext, EdgeType.DIRECTED);

					if (!added) {
						throw new CarvingException("Cannot add " + (RelationType.SAME + "_" + id.get()) + " " + newData
								+ " " + callingContext);
					}
					added = graph.addEdge(RelationType.SAME + "_" + id.getAndIncrement(),
							// Pay attention this is reversed because we are
							// returning...
							callingContext, newData, EdgeType.DIRECTED);

					if (!added) {
						throw new CarvingException("Cannot add " + (RelationType.SAME + "_" + id.get()) + " "
								+ callingContext + " " + newData);
					}
				} else {
					// ERROR
					logger.error("Cannot find a proper relation between " + parentActivity + " and " + activityToAdd);
				}
			}
		}
	}

	private Pair<ObjectInstance, MethodInvocation> findTheStartingIntentBetweenInstances(
			Pair<ObjectInstance, MethodInvocation> currentInstance,
			Pair<ObjectInstance, MethodInvocation> previousInstance) throws CarvingException {

		/*
		 * Get all the methods in between the context of both activities.
		 */
		List<MethodInvocation> startActivityInContext = executionFlowGraph
				.getOrderedMethodInvocationsBefore(currentInstance.getSecond());

		/*
		 * Keep only the after (and including previousInstance) We do not filter by
		 * activity since start activity method might be inside other components, e.g.,
		 * fragments
		 */
		MethodInvocationMatcher.keepMatchingInPlace(MethodInvocationMatcher.after(previousInstance.getSecond()),
				startActivityInContext);
		/*
		 * Keep only the startActivity* methods </br> TODO In theory we need the
		 * signature because other methods might match as well
		 */
		MethodInvocationMatcher.keepMatchingInPlace(
				MethodInvocationMatcher.byMethodName("startActivity|startActivityForResult"), startActivityInContext);
		// Since the method invocations are ordered we need only the last
		// occurrence of the startActivity method, if any
		if (startActivityInContext.isEmpty()) {
			return null;
		} else {
			// If there's one we need the Intent used as parameter, which is the
			// first one
			MethodInvocation startActivity = startActivityInContext.get(startActivityInContext.size() - 1);
			DataNode intent = startActivity.getActualParameterInstances().get(0);
			if (intent instanceof ObjectInstance) {

				SootClass childClass = Scene.v().getSootClass(((ObjectInstance) intent).getType());
				SootClass intentClass = Scene.v().getSootClass("android.content.Intent");

				if (Scene.v().getActiveHierarchy().isClassSubclassOfIncluding(childClass, intentClass)) {
					return new Pair<ObjectInstance, MethodInvocation>((ObjectInstance) intent, startActivity);
				}
			}
			logger.error("Expecting Intent Object but found " + intent + " instead !");
			throw new CarvingException("Expecting Intent Object but found " + intent + " instead !");
		}
	}

	public boolean isEmpty() {
		return getActivityCount() == 0;
	}

	public int getActivityCount() {
		// BUild activity set:
		return getActivities().size();

	}

	public Set<ObjectInstance> getActivities() {
		Set<ObjectInstance> activities = new HashSet<>();
		for (Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> node : graph.getVertices()) {
			activities.add(node.getFirst());
		}
		return activities;
	}

	// Mostly debgu

	private Map<ObjectInstance, Color> buildColorMap(Set<ObjectInstance> activities)
			throws IllegalArgumentException, IllegalAccessException, CarvingException {
		// Create a copy
		Set<ObjectInstance> _activities = new HashSet<>(activities);

		Map<ObjectInstance, Color> colorMap = new HashMap<>();
		// Colors are named with upper and lower cases...
		Set<String> colorNames = new HashSet<>();
		Class clazz = Color.class;
		Field[] colorFields = clazz.getDeclaredFields();
		for (Field cf : colorFields) {
			int modifiers = cf.getModifiers();
			if (!Modifier.isPublic(modifiers))
				continue;

			Color color = (Color) cf.get(null);
			if (colorNames.contains(color.toString().toLowerCase())) {
				continue;
			}

			Iterator<ObjectInstance> iterator = _activities.iterator();
			if (iterator.hasNext()) {
				ObjectInstance activity = _activities.iterator().next();
				colorMap.put(activity, color);
				System.out.println("\t\t " + activity + " --> " + color + " (" + color.toString() + ")");
				_activities.remove(activity);
				colorNames.add(color.toString().toLowerCase());
			} else {
				break;
			}
		}

		if (!_activities.isEmpty()) {
			throw new CarvingException("Not enough colors for those activities: " + activities);
		}

		return colorMap;
	}

	// This must be improved as the same activity in start and return actually
	// results in two different nodes ....
	public void visualize() throws IllegalArgumentException, IllegalAccessException, CarvingException {

		final Map<ObjectInstance, Color> activityColorMap = buildColorMap(getActivities());

		VisualizationViewer<Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>>, String> vv = new VisualizationViewer<Pair<ObjectInstance, // The
																																						// Activity
				Pair<MethodInvocation, MethodInvocation>>, String>(
						// new TreeLayout<>((Forest<MethodInvocation, String>
						// )graph));
						// // Does not work..
						new KKLayout<Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>>, String>(graph));

		vv.setPreferredSize(new Dimension(1000, 800));

		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller() {
			@Override
			public String apply(Object node) {
				Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> theNode = (Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>>) node;
				return theNode.getFirst().getType() + "[" + theNode.getSecond().getFirst() + ":"
						+ theNode.getSecond().getSecond() + "]";
			}
		});
		vv.getRenderContext().setVertexFillPaintTransformer(
				new Function<Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>>, Paint>() {
					@Override
					public Paint apply(Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> node) {
						ObjectInstance activity = node.getFirst();
						if (activityColorMap.containsKey(activity)) {
							return activityColorMap.get(activity);
						} else {
							return Color.BLACK;
						}
					}
				});
		vv.getRenderContext().setEdgeDrawPaintTransformer(new Function<String, Paint>() {
			@Override
			public Paint apply(String edge) {
				if (edge.startsWith(RelationType.LIFE_CYCLE.name())) {
					return Color.RED;
				} else if (edge.startsWith(RelationType.RETURN.name())) {
					return Color.BLUE;
				} else if (edge.startsWith(RelationType.RETURN_WITH_RESULT.name())) {
					return Color.CYAN;
				} else if (edge.startsWith(RelationType.START.name())) {
					return Color.GRAY;
				} else if (edge.startsWith(RelationType.START_WITH_RESULT.name())) {
					return Color.LIGHT_GRAY;
				} else if (edge.startsWith(RelationType.BEFORE.name())) {
					return Color.WHITE;
				} else {
					return Color.BLACK;
				}
			}
		});

		JFrame frame = new JFrame("Activity Navigation View");
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().add(vv);
		frame.pack();
		frame.setVisible(true);

	}

	public Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> locate(
			MethodInvocation methodInvocationToCarve) {
		for (Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> node : graph.getVertices()) {
			if (node.getFirst().equals(methodInvocationToCarve.getOwner())
					&& isInBetween(methodInvocationToCarve, node.getSecond())) {
				return node;
			}
		}
		logger.warn("Cannot locate " + methodInvocationToCarve);
		return null;
	}

	private boolean isInBetween(MethodInvocation methodInvocationToCarve,
			Pair<MethodInvocation, MethodInvocation> second) {
		return methodInvocationToCarve.getInvocationCount() >= second.getFirst().getInvocationCount()
				&& methodInvocationToCarve.getInvocationCount() <= second.getSecond().getInvocationCount();
	}

	public boolean isStartingOfActivity(Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> location) {
		// There's no activities before me
		// There's an activity AND an intent
		if (graph.getInEdges(location).isEmpty()) {
			logger.debug("No incoming edges to " + location + " main/root activity");
			return true;
		} else {
			for (String inEdge : graph.getInEdges(location)) {
				if (inEdge.startsWith(RelationType.START.name())
						|| inEdge.startsWith(RelationType.START_WITH_RESULT.name())) {
					logger.debug("Incoming edge to " + location + " " + inEdge);
					return true;
				}
			}
		}
		return false;
	}

	public boolean isReturnFromActivity(Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> location) {
		for (String inEdge : graph.getInEdges(location)) {
			if (inEdge.startsWith(RelationType.RETURN.name())
					|| inEdge.startsWith(RelationType.RETURN_WITH_RESULT.name())) {
				logger.debug("Incoming edge to " + location + " " + inEdge);
				return true;
			}
		}
		return false;
	}

	public boolean isLeftOverFromStartingOfActivity(
			Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> location) {
		for (String inEdge : graph.getInEdges(location)) {
			if (inEdge.startsWith(RelationType.LEFT_OVER.name())
					&& isStartingOfActivity(graph.getOpposite(location, inEdge))) {
				logger.debug("Incoming edge to " + location + " " + inEdge);
				return true;
			}
		}
		return false;
	}

	public boolean isLeftOverFromReturnFromActivity(
			Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> location) {
		for (String inEdge : graph.getInEdges(location)) {
			if (inEdge.startsWith(RelationType.LEFT_OVER.name()) &&
			// The node which precede this, must be a return node
					isReturnFromActivity(graph.getOpposite(location, inEdge))) {
				logger.debug("Incoming edge to " + location + " " + inEdge);
				return true;
			}
		}
		return false;
	}

	public Pair<ObjectInstance, MethodInvocation> getRequestIntentWithContextForActivity(
			Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> location) {
		return requestIntents.get(location);
	}

	// Return the activities (starting and returning) before this location
	private Set<String> getEdgeInto(Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> location) {
		// Get InEdges but remove the links which we do not want to follow,
		// SAME.
		Set<String> inEdges = new HashSet<>(graph.getInEdges(location));
		for (Iterator<String> iterator = inEdges.iterator(); iterator.hasNext();) {
			String inEdge = iterator.next();
			if (inEdge.startsWith(RelationType.SAME.name())) {
				iterator.remove();
			}
		}
		return inEdges;
	}

	public Set<Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>>> getActivitiesBefore(
			Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> location) {
		Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> cursor = location;
		Set<Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>>> activitiesBefore = new HashSet<>();
		//
		Set<String> inEdges = getEdgeInto(cursor);
		while (!inEdges.isEmpty()) {
			// System.out.println("ActivityGraph.getActivitiesBefore() Found the
			// following inEDGES " + inEdges );
			if (inEdges.size() > 1) {
				throw new RuntimeException(
						"I found more incoming edges than expected for " + location + " : " + inEdges);
			}
			cursor = graph.getOpposite(cursor, inEdges.iterator().next());
			activitiesBefore.add(cursor);
			//
			inEdges = getEdgeInto(cursor);
		}

		return activitiesBefore;
	}

	public Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> getLeftOverFor(
			Pair<ObjectInstance, Pair<MethodInvocation, MethodInvocation>> location) {
		for (String outEdge : graph.getOutEdges(location)) {
			if (outEdge.startsWith(RelationType.LEFT_OVER.name())) {
				return graph.getOpposite(location, outEdge);
			}
		}
		return null;
	}

}
