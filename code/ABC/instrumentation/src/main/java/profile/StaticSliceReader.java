package profile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dua.util.Pair;
import dua.util.Util;

/** Used at runtime to parse slice file and provide data to examiner. */
public class StaticSliceReader {
	/** Lightweight representation (for runtime use) of a dependence, as read from static slice file. */
	public static class RT_Dependence {
		private final boolean isData;  // whether it's a data dep; false if control dep
		private final int id;  // idx in global list of deps
		private final int src; // point id; negative if position is pre-rhs
		private final int tgt; // point id; negative if position is pre-rhs
		private final int varOrBrId; // var idx if data dependence; branch id if control dependence
		
		public int getId() { return id; }
		public int getSrc() { return src; }
		public int getTgt() { return tgt; }
		public int getVarOrBrId() { return varOrBrId; }
		public boolean isControl() { return !isData; }
		public boolean isData() { return isData; }
		
		public RT_Dependence(boolean isData, int id, int src, int tgt, int varOrBrId) {
			this.isData = isData; this.id = id; this.src = src; this.tgt = tgt; this.varOrBrId = varOrBrId; }
		
		@Override public int hashCode() { return (isData?1:0) + id + src + tgt + varOrBrId; }
		@Override public boolean equals(Object o) {
			RT_Dependence depOther = (RT_Dependence) o;
			return isData == depOther.isData && id == depOther.id && src == depOther.src && tgt == depOther.tgt && varOrBrId == depOther.varOrBrId;
		}
		@Override public String toString() { return (isData?"c":"d") + id + ": " + src + "->" + tgt + " " + varOrBrId; }
	}
	/** Lightweight representation (for runtime use) of a static dependence graph, i.e. a collection of edges dep->dep. */
	public static class RT_StaticDepGraph {
		private final int startPnt;
		/** Deps from start point. */
		private final List<Integer> startDeps = new ArrayList<Integer>();
		/** Links dep->succDeps. */
		private final List<List<Integer>> depToSuccDeps = new ArrayList<List<Integer>>();
		/** Links dep->predDeps. */
		private final List<List<Integer>> depToPredDeps = new ArrayList<List<Integer>>();
		/** Deps with at least one successor in this graph. */
		private final BitSet depsWithSuccs = new BitSet();
		
		private static final List<Integer> EMPTY_DEP_IDS_LIST = new ArrayList<Integer>();
		
		public RT_StaticDepGraph(int startPnt) { this.startPnt = startPnt; }
		
		public int getStartPnt() { return startPnt; }
		public List<Integer> getStartDeps() { return startDeps; }
		public boolean hasSuccs(int depId) { return depsWithSuccs.get(depId); }
		public List<Integer> getSuccDeps(int depId) { return (depId < depToSuccDeps.size())? depToSuccDeps.get(depId) : EMPTY_DEP_IDS_LIST; }
		
		public void setNextDeps(int depId, List<Integer> nextDeps) {
			assert nextDeps != null;
			
			// register depId and its succs and preds; for -1 special root depId, store succs as "starting points" instead
			if (depId == -1) // for depId -1 (start point), we don't store succs/preds in standard list -- we use startDeps for succs instead (and there are no preds, of course)
				startDeps.addAll(nextDeps);
			else {
				// mark dep as having successors in this graph
				if (!nextDeps.isEmpty())
					depsWithSuccs.set(depId);
				
				// make room in succ list of lists, if necessary
				makeRoomForDepId(depId, depToSuccDeps, false);
				
				// associate dep with list of succ deps
				assert depToSuccDeps.get(depId).isEmpty(); // shouldn't have been set before
				depToSuccDeps.set(depId, nextDeps);
				
				// associate to each next-dep the predecessor depId, making room in pred list of lists if necessary
				for (Integer depSuccId : nextDeps) {
					makeRoomForDepId(depSuccId, depToPredDeps, true);
					depToPredDeps.get(depSuccId).add(depId);
				}
			}
		}
		private static void makeRoomForDepId(int depId, List<List<Integer>> depToNeighbors, boolean fresh) {
			// old empty list will be replaced with new list, if dep has any successors
			// fresh list in case of predecessors, because it will be updated at different times, instead of being replaced like in the case of succs
			final int prevSize = depToNeighbors.size();
			if (prevSize <= depId) {
				for (int i = prevSize; i <= depId; ++i)
					depToNeighbors.add(fresh? new ArrayList<Integer>(): EMPTY_DEP_IDS_LIST);
			}
		}
		/** Computes array of # of deps at each distance, up to specified max distance. Distance is the index. Distance 0 (at index 0) always has 1 pseudo-dep: 'root'. */
		public int[] computeNumChainsPerDep(int maxDist) {
			final int[] chainsPerD = new int[maxDist + 1]; // allow for d0 (root)
			
			// forward traverse graph using successor deps map from starting deps (d1)
			chainsPerD[0] = 1; // by definition
			assert maxDist >= 1;
			
			BitSet depsAtPrevD = new BitSet();
			for (Integer depIdAtD1 : startDeps)
				depsAtPrevD.set(depIdAtD1);
			chainsPerD[1] = depsAtPrevD.cardinality();
			assert chainsPerD[1] == startDeps.size(); // by definition
			
			for (int d = 2; d <= maxDist; ++d) {
				// collect deps at d using deps at d-1 and successors map
				BitSet depsAtCurrD = new BitSet();
				for (int depId = depsAtPrevD.nextSetBit(0); depId >= 0; depId = depsAtPrevD.nextSetBit(depId + 1))
					for (Integer succDepId : depToSuccDeps.get(depId))
						depsAtCurrD.set(succDepId);
				chainsPerD[d] = depsAtCurrD.cardinality();
				// move on to set of deps at d to find deps at d+1 in next iteration 
				depsAtPrevD = depsAtCurrD;
			}
			
			return chainsPerD;
		}
	}
	
	private boolean includeVals = false; // default
	private boolean includeChangeCov = false; // default
	private boolean includeBrCov = false; // default
	private boolean includeDUCov = false; // default
	public static final int NO_LIMIT = -1;
	private int eventLimit = NO_LIMIT; // default
	
	private int numVars;
	private final List<RT_Dependence> deps = new ArrayList<RT_Dependence>(); // index is id of dep
	/** Map srcpnt->deps */
	private final Map<Integer,List<RT_Dependence>> srcToDeps = new HashMap<Integer, List<RT_Dependence>>();
	/** Map srcpnt->tgtpnts */
	private final Map<Integer,List<Integer>> srcToTgts = new HashMap<Integer, List<Integer>>();
	/** Map srcpnt->tgtpnt->ctrldepid */
	private final Map<Integer,Map<Integer,Integer>> cdIdForSrcAndTgt = new HashMap<Integer,Map<Integer,Integer>>();
	/** Map startpoint->depgraph */
	private final Map<Integer, RT_StaticDepGraph> depGraphs = new HashMap<Integer, RT_StaticDepGraph>();
	
	/** List of branches, each defined by pair <pntsrc,brid>, ordered by unsigned point and then brid. */
	private List<Pair<Integer,Integer>> branches = new ArrayList<Pair<Integer,Integer>>();
	/** Index of first du-pair in the dependence list. */
	private int firstDUIndex = -1;
	/** Index of last du-pair in the dependence list. */
	private int lastDUIndex = -1;
	
	public boolean includeVals() { return includeVals; }
	public boolean includeChangeCov() { return includeChangeCov; }
	public boolean includeBrCov() { return includeBrCov; }
	public boolean includeDUCov() { return includeDUCov; }
	public int getEventsLimit() { return eventLimit; }
	public int getNumVars() { return numVars; }
	public List<RT_Dependence> getDeps() { return deps; }
	public RT_Dependence getDep(int depId) { return deps.get(depId); }
	/** For src point, returns all deps starting from it. */
	public List<RT_Dependence> getDepsForSrc(int src) { return srcToDeps.get(src); }
	/** For src point, returns all tgt points that depends on it. */
	public List<Integer> getTgtsForSrc(int src) { return srcToTgts.get(src); }
	public RT_StaticDepGraph getDepGraph(int start) { return depGraphs.get(start); }
	public Map<Integer, RT_StaticDepGraph> getDepGraphs() { return depGraphs; }
	public boolean isStart(int pnt) { return depGraphs.containsKey(pnt); }
	
	/** Returns list of branches in order of (unsigned) point and brid. */
	public List<Pair<Integer,Integer>> getBranches() { return branches; }
	public int getFirstDUIdx() { return firstDUIndex; }
	public int getLastDUIndex() { return lastDUIndex; }
	public int getNumDUs() { return lastDUIndex - firstDUIndex + 1; }
	
	public int getControlDepIdForSrcTgt(int src, int tgt) {
		return cdIdForSrcAndTgt.get(src).get(tgt);
//		for (RT_Dependence rtDep : deps) {
//			if (rtDep.isData)
//				continue;
//			if (rtDep.getSrc() == src && rtDep.getTgt() == tgt)
//				return rtDep.getId();
//		}
//		assert false;
//		return Integer.MIN_VALUE;
	}
	
	/** Assumes that succs are the same in al dep graphs. */
	public int getNumSuccs(int fromDepId) {
		for (RT_StaticDepGraph depGraph : depGraphs.values()) {
			if (depGraph.hasSuccs(fromDepId)) {  // only check num succs if dep is in graph (and it has any succs at all, in general)
				List<Integer> nextDeps = depGraph.getSuccDeps(fromDepId);
				assert nextDeps != null;
				assert !nextDeps.isEmpty();
				return nextDeps.size();
			}
		}
//		assert false;
		return 0;
	}
	
	private void processArgs(String sArgsLine) {
		List<String> args = Util.parseStringList(sArgsLine, ' ');
		for (String arg : args) {
			if (arg.startsWith("VALUES:"))
				includeVals = arg.substring("VALUES:".length()).equals("YES");
			else if (arg.startsWith("CHANGE_COV:"))
				includeChangeCov = arg.substring("CHANGE_COV:".length()).equals("YES");
			else if (arg.startsWith("BR_COV:"))
				includeBrCov = arg.substring("BR_COV:".length()).equals("YES");
			else if (arg.startsWith("DU_COV:"))
				includeDUCov = arg.substring("DU_COV:".length()).equals("YES");
			else if (arg.startsWith("EV_LIMIT:"))
				eventLimit = Integer.valueOf( arg.substring("EV_LIMIT:".length()) );
			else {
				assert false; // argument not recognized
			}
		}
	}
	
	public StaticSliceReader() {
		this(dua.util.Util.getCreateBaseOutPath());
	}
	public StaticSliceReader(String basePath) {
		try {
			BufferedReader reader = new BufferedReader(
					new FileReader(basePath + (basePath.isEmpty()? "" : File.separator) + "slice.out"));
			
			// hcai: for debugging
			System.out.println("Started reading the static slice from " + basePath + (basePath.isEmpty()? "" : File.separator) + "slice.out");
			
			// read whether to include values or not
			String s = reader.readLine();
			processArgs(s);
			
			// read num vars
			s = reader.readLine();
			assert s.startsWith("VARIABLES: ");
			this.numVars = Integer.valueOf(s.substring("VARIABLES: ".length()));
			
			// read deps list
			s = reader.readLine();
			assert s.startsWith("DEPENDENCIES: ");
			final int numDeps = Integer.valueOf(s.substring("DEPENDENCIES: ".length()));
			while ((s = reader.readLine()).charAt(0) == ' ') {
				int start, end;
				// read dep id
				end = s.indexOf(' ', 1);
				final int depId = Integer.valueOf(s.substring(1, end));
				++end;
				
				// read dep type
				final boolean isData = s.charAt(end) == 'd';
				++end;
				assert s.charAt(end) == ':';
				++end;
				
				// read src
				start = end;
				end = s.indexOf('[', start);
				int src = Integer.valueOf(s.substring(start, end));
				++end;
				// read src pos
				assert s.charAt(end + 1) == ']';
				if (s.charAt(end) == '0')
					src = -src; // neg sign indicates "pre" position
				end += 2;
				
				assert s.substring(end, end + 2).equals("->");
				end += 2;
				
				// read tgt
				start = end;
				end = s.indexOf('[', start);
				int tgt = Integer.valueOf(s.substring(start, end));
				++end;
				// read tgt pos
				assert s.charAt(end + 1) == ']';
				if (s.charAt(end) == '0')
					tgt = -tgt; // neg sign indicates "pre" position
				end += 2;
				
				// read var/brid
				final int varOrBrId = Integer.valueOf(s.substring(end + 1)); //isData? Integer.valueOf(s.substring(end + 1)) : -1;
				
				// construct and store dep
				assert deps.size() == depId;
				RT_Dependence rtDep = new RT_Dependence(isData, depId, src, tgt, varOrBrId);
				deps.add(rtDep);
				// also, add dep to src->deps and tgt->deps maps
				List<RT_Dependence> depsForSrc = Util.getCreateMapValue(srcToDeps, src, ArrayList.class);
				depsForSrc.add(rtDep);
				List<Integer> tgtsForSrc = Util.getCreateMapValue(srcToTgts, src, ArrayList.class);
				tgtsForSrc.add(tgt);
				// update src->tgt->ctrldepid map
				if (!isData) {
					Map<Integer,Integer> tgtToDepId = Util.getCreateMapValue(cdIdForSrcAndTgt, src, HashMap.class);
					tgtToDepId.put(tgt, depId);
				}
				
				// store branch and du-pair info, if enabled
				if (isData) {
					if (includeDUCov) {
						if (firstDUIndex == -1)
							firstDUIndex = depId;
						lastDUIndex = depId;
					}
				}
				else {
					if (includeBrCov) {
						Pair<Integer,Integer> lastBr = branches.isEmpty()? null : branches.get(branches.size() - 1);
						Pair<Integer,Integer> br = new Pair<Integer,Integer>(src, varOrBrId);
						if (lastBr == null || !lastBr.equals(br))
							branches.add(br);
					}
				}
			}
			
			// read dep graphs (each consisting of dep->deps associations)
			while (s != null) {
				assert s.startsWith("DEP GRAPH id ");
				// read start point
				int start = "DEP GRAPH id ".length();
				int end = s.indexOf('[', start);
				int startPoint = Integer.valueOf(s.substring(start, end));
				++end;
				// read start point pos
				assert s.charAt(end + 1) == ']';
				if (s.charAt(end) == '0')
					startPoint = -startPoint; // neg sign indicates "pre" position
				assert s.length() == end + 2;
				
				// add dep graph to map, associated with start point
				RT_StaticDepGraph depGraph = new RT_StaticDepGraph(startPoint);
				depGraphs.put(startPoint, depGraph);
				
				// read edges of dep graph
				while ((s = reader.readLine()) != null && s.charAt(0) == ' ') {
					// read src dep
					end = s.indexOf(' ', 1);
					final int depSrc = Integer.valueOf(s.substring(1, end));
					
					assert s.substring(end + 1, end + 3).equals("->");
					end += 3;
					
					// read tgt deps list
					List<Integer> tgtDeps = new ArrayList<Integer>();
					final int len = s.length();
					while (end < len) {
						start = end + 1;
						end = s.indexOf(' ', start);
						if (end == -1)
							end = len;
						final int tgt = Integer.valueOf(s.substring(start, end));
						tgtDeps.add(tgt);
					}
					
					// store src->tgts entry
					depGraph.setNextDeps(depSrc, tgtDeps);
				}
			}
			
			reader.close();
			
			// hcai: for debugging
			System.out.println("Finished reading the static slice from " + basePath + (basePath.isEmpty()? "" : File.separator) + "slice.out");
		}
		catch (FileNotFoundException e) { System.err.println("Couldn't read dyn-slice file:" + e); }
		catch (SecurityException e) { System.err.println("Couldn't read dyn-slice file:" + e); }
		catch (IOException e) { System.err.println("Couldn't read dyn-slice file:" + e); }
	}
	
}
