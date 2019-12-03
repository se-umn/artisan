package profile;

import java.io.PrintStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import profile.StaticSliceReader.RT_Dependence;
import dua.util.Pair;
import dua.util.Util;

public class DynSliceExaminer {
	/** Holds singleton instance of this class. Guaranteed to be non-null. */
	private static DynSliceExaminer inst;
	public static DynSliceExaminer inst() { return inst; }
	
	/** Guaranteed to instantiate singleton object of this class. Calls other initialization for classes {@link DataEvent} and {@link ControlEvent}. */
	public static void initialize() {
		if (inst != null) {
			if (inst.activated)
				return;
			inst.activated = true;
		}
		try { initialize_IMPL(); }
		finally { if (inst != null) inst.activated = false; }
	}
	private static void initialize_IMPL() {
		inst = new DynSliceExaminer();
		DataEvent.initialize();
		ControlEvent.initialize();
	}
	
	/** Indicates whether to report monitoring info, which depends on a property value NOT being provided by invoking process. */
	private final static boolean enabled;
	static {
		final String propVal = System.getProperty("duaf.monitor");
		enabled = !(propVal != null && (propVal.equals("no") || propVal.equals("off") || propVal.equals("false")));
		
		initialize();
	}
	
	/** Saves stdout in case it is changed during subject's execution. */
	private static PrintStream ORIG_STDOUT = System.out;
	/** Saves stderr in case it is changed during subject's execution. */
	private static PrintStream ORIG_STDERR = System.err;
	
	/** Stores temporarily all what has to be sent to STDOUT when STDOUT becomes again the default out stream. */
	private static StringBuffer BUF_STDOUT = new StringBuffer();
	/** Stores temporarily all what has to be sent to STDERR when STDERR becomes again the default ERR stream. */
	private static StringBuffer BUF_STDERR = new StringBuffer();
	
	/** Calls {@link printOut} with message and eol appended to it. */
	private static void printOutLn(String s) {
		printOut(s + "\n");
	}
	/** Buffers text to be printed if stream has been changed by the subject. Buffered text is then sent to the stream when it's set back. */
	private static void printOut(String s) {
		// hcai: for running all test cases in a single JVM process
		//printToStream(s, ORIG_STDOUT, System.out, BUF_STDOUT);
		System.out.print(s);
	}
	/** Calls {@link printErr} with message and eol appended to it. */
	private static void printErrLn(String s) {
		printErr(s + "\n");
	}
	/** Buffers text to be printed if stream has been changed by the subject. Buffered text is then sent to the stream when it's set back. */
	private static void printErr(String s) {
		// hcai: for running all test cases in a single JVM process
		// printToStream(s, ORIG_STDERR, System.err, BUF_STDERR);
		System.err.print(s);
	}
	/** Central point for either "printing" to stream (e.g., stdout, stderr) if enabled or storing data until original stream is enabled again. */
	private static void printToStream(String s, PrintStream origStream, PrintStream currStream, StringBuffer buffer) {
		assert inst != null; // sanity check; ensures {@link finalize()} is executed with a final flush
		// buffer message instead of printing it right away
		if (origStream != currStream) {
			buffer.append(s);
		}
		else {
			flushStreamBuffer(origStream, buffer);
			origStream.print(s);
		}
	}
	/** Dumps buffer into stream, ensuring stream is flushed and clearing the buffer. */
	private static void flushStreamBuffer(PrintStream origStream, StringBuffer buffer) {
		// do the printing
		origStream.print(buffer);
		origStream.flush(); // make sure contents are printed; program might be terminating
		// clear buffer and memory locked by it 
		buffer.setLength(0);
		buffer.trimToSize();
	}
	
	/** Makes sure that any buffered output data is flushed into stdout and stderr.
	 *  Guaranteed to execute because an instance of this class exists (singleton)
	 *  @see DynSliceExaminer#inst */
	@Override protected void finalize() {
		flushStreamBuffer(ORIG_STDOUT, BUF_STDOUT);
		flushStreamBuffer(ORIG_STDERR, BUF_STDERR);
	}
	
	/** Whether the examiner has been invoked already. Used to avoid re-entrance when reporting due to, for example, toString on an instrumented object. */
	private boolean activated = false;
	private DynSliceExaminer() {
		updateMemUse(); // make sure there is garbage collection if free memory is low (due to an earlier test execution)
	}
	
	/** Convenient abstraction for the integer pair <depId,instId> that tells apart instances (occurrences) of a covered dep. */
	public static class DepInst extends Pair<Integer,Integer> {
		public Integer getDepId() { return this.first(); }
		public Integer getInstId() { return this.second(); }
		public DepInst(Integer depId, Integer instId) { super(depId, instId); }
	}
	
	/** Definition event at a given point of a variable (including base and array index, when necessary). */
	public static class DataEvent {
		public final int pnt;
		public final int var;
		public final boolean interproc;
		public final WeakReference<Object> objBase; // non-null if var has a base object or containing array
		public final int arridx; // -1 if not an array element
		public final int frameId; // useful if local-var intraproc dep
		
		/** @param interproc Needed because not all interproc vars have a base instance (e.g., returned variables). */
		public DataEvent(int pnt, int var, boolean interproc, Object objBase, int idx, int frameId) {
			this.pnt = pnt;
			this.var = var;
			this.interproc = interproc;
			this.objBase = new WeakReference<Object>(objBase);
			this.arridx = idx;
			this.frameId = frameId;
			
			List<DataEvent> evsForFrame = Util.getCreateMapValue(frameToEvs, frameId, ArrayList.class);
			evsForFrame.add(this);
		}
		
		/** Helps garbage-collect events from destroyed frames. */
		public static Map<Integer,List<DataEvent>> frameToEvs;
		/** Keeps data events of heap variables that might remain live after their original frame was destroyed. */
		public static Set<DataEvent> framelessEvs;
		/** Collects those new frameless events that should not removed until all events for current frame & point have been reported. */
		public static Set<DataEvent> latestFramelessDefEvs = new HashSet<DataEvent>();
		
		static {
			initialize();
		}
		private static void initialize() {
			frameToEvs = new HashMap<Integer, List<DataEvent>>();
			framelessEvs = new HashSet<DataEvent>();
		}
		
		@Override public int hashCode() { return pnt + var + /*objBase.get().hashCode() +*/ arridx + frameId; }
		@Override public boolean equals(Object other) {
			if (!(other instanceof DataEvent))
				return false;
			DataEvent evOther = (DataEvent) other;
			return pnt == evOther.pnt && var == evOther.var && objBase.get() == evOther.objBase.get() && arridx == evOther.arridx && frameId == evOther.frameId;
		}
		@Override public String toString() {
			Object oBase = objBase.get();
			return "pnt " + pnt + " var " + var + " base " + ((oBase == null)? "null" : "@"+Integer.toHexString(System.identityHashCode(oBase))) + " idx " + arridx + " frame " + frameId;
		}
	}
	/** Branching (control-dep) event at a given point. */
	public static class ControlEvent {
		/** Source of branch. No need to distinguish pnt's instance within frame because branches are stacked for each cd-tgt. */
		public final int srcPnt;
		/** A unique identifier for the actual edge from src point:
		 *    For branches: index in list of outgoing branches from predicate.
		 *    For virtual calls: global id of callee. */
		public final int ctrlId;
		/** Distinguishes multiple open branches */
		public final int frameId;
		
		public ControlEvent(int srcPnt, int ctrlId, int frameId) {
			this.srcPnt = srcPnt;
			this.ctrlId = ctrlId;
			this.frameId = frameId;
			
			List<ControlEvent> evsForFrame = Util.getCreateMapValue(frameToEvs, frameId, ArrayList.class);
			evsForFrame.add(this);
		}
		
		/** Helps garbage-collect events from destroyed frames. */
		public static Map<Integer,List<ControlEvent>> frameToEvs;
		
		static {
			initialize();
		}
		private static void initialize() {
			frameToEvs = new HashMap<Integer, List<ControlEvent>>();
		}
		
		@Override public int hashCode() { return srcPnt + ctrlId + frameId; }
		@Override public boolean equals(Object other) {
			if (!(other instanceof ControlEvent))
				return false;
			ControlEvent evOther = (ControlEvent) other;
			return srcPnt == evOther.srcPnt && ctrlId == evOther.ctrlId && frameId == evOther.frameId;
		}
		@Override public String toString() { return "pnt " + srcPnt + " ctrlId " + ctrlId + " frame " + frameId; }
	}
	
	/** A slice contains (1) the deps executed, (2) the last covered deps that can be succeeded by next deps,
	 *  and (3) the "open" deps whose src occurred but we are waiting for their tgt to occur. */
	public static class FwdSlice {
		/** Start point. */
		public final int start;
//		/** (1) the deps executed */
//		public final List<Integer> depsCovered = new ArrayList<Integer>();
		/** (2) the last covered data/ctrl dep-instances <depId,instId> for "current points" (usually 1 except for ENTRY) that can be succeeded by next deps */
		public final Map<Integer,Set<DepInst>> lastDepInstsForCurrPntInsts = new HashMap<Integer, Set<DepInst>>();
		/** (3.1) the "open" data deps whose src occurred but we are waiting for their tgt(s) to occur -- defs as tuples <point,var,baseobj,arridx> */
		public final Set<DataEvent> openDefs = new HashSet<DataEvent>();
		/** (3.2) for each cd-tgt, the stack of "open" ctrl deps that will reach it.
		 *        NOTE: tgt will be reached by an open br, because any other br opening later is also cd on the previously open brs. */
		public final Map<Integer,List<ControlEvent>> openBrsForTgts = new HashMap<Integer, List<ControlEvent>>();
		/** Keeps track of last callsite that is a cd src. Will necessarily match next virtual-call tgt.
		 *  There can be only one at a time, for all slices. Set to 1 when not open (cs pnt ids are always negative). */
		public int openCSSrc = NO_CS;
		public static final int NO_CS = 1;
		
		/** Keeps track of which dep-instances are predecessors of a definition, which lets us associate preds to next dep if def reaches a matching use. */
		public Map<DataEvent,List<DepInst>> defPredDepInsts = new HashMap<DataEvent, List<DepInst>>();
		/** Keeps track of which dep-instances are predecessors of a branch, which lets us associate preds to next dep when program reaches cd target of branch. */
		public Map<ControlEvent,List<DepInst>> brPredDepInsts = new HashMap<ControlEvent, List<DepInst>>();
		/** Keeps track of of predecessor dep-instances for last callsite (the only one being tracked). To be used by call target reached next.
		 *  EMPTY when not open. */
		public List<DepInst> csPredDepInsts = new ArrayList<DepInst>();
		
		public FwdSlice(int start) { this.start = start; }
		
		/** Resets open CS when we know current one is no longer open and gets ready with fresh new list of pred dep-insts for next open CS src pnt */
		public void closeOpenCS() { openCSSrc = NO_CS; csPredDepInsts.clear(); }
	}
	
	/** For each start point, we keep a list of (ongoing) slices, one per occurrence of the start point.
	 *  A slice contains (1) the deps executed, (2) the "ends" of the last deps that can be succeeded by next deps,
	 *  and (3) the "open" deps whose src occurred but we are waiting for their tgt to occur. */
	private Map<Integer, List<FwdSlice>> startToSlices = new HashMap<Integer, List<FwdSlice>>();
	/** Callstack of frameids, which allows to figure out which frames have been destroyed and which open def to match with a reported use for param/return link data deps.
	 *  Invariant: every frameId is succeeded only by a greater frameId in the stack. */
	private List<Integer> frameStack = new ArrayList<Integer>();
	/** Id to be used for the next observation (instance) of a dependence. */
	private int nextDepInstId = 0;
	/** Used to determine if the a new point instance is occurring. */
	private int lastFrame = -1;
	/** Keeps track of last observed point, to decide when to increment the global pntInst id. */
	private int lastPnt = 0;
	/** Identifies "position" (not to confuse with pre/post) of the current reporting stage for a given point.
	 *  For each point, use probes execute first, def probes next, and ctrl probes last.
	 *  Note: an enum is IMPLICITLY STATIC. */
	private enum PntReportPos { CDTGT, USE, DEF, CDSRC };
	/** Keeps track of last observed location for the last observed (reported) point. Needed to decide when to increment the global pntInst id. */
	private PntReportPos lastPntPos = PntReportPos.CDTGT;
	/** Whether any of the possibly many start points has been reached. */
	private boolean someStartReached = false;
	/** Set of start points that have already been reached. */
	private Set<Integer> startPntsReached = new HashSet<Integer>();
	/** Only for DU coverage tracking. */
	private final Set<DataEvent> openDefsForDUCov = new HashSet<DataEvent>();
	/** To avoid reporting too many branches covered. */
	private final Set<Pair<Integer,Integer>> brsCov = new HashSet<Pair<Integer,Integer>>();
	
	/** DEBUG: max memory used during monitoring. */
	private long maxMemUsed = 0;
	private long maxMemThreshold = (Runtime.getRuntime().maxMemory() * 9) / 10; //1000000000; // default: 1GB
	private int memReportCountdown = 0;
	private long GC_PERIOD = 500; // GC in no less than this # of ms after previous GC
	private long lastGCTime = 0; // keeps track of last millisecond time measured, for GC purposes
	private static final int MEM_REPORT_FREQ = 10;
	private static final double MIN_FREEMEM_RATIO = 0.03d;
	private boolean memFull = false;
	
	/** Returns true if free mem too low to continue. */
	private boolean updateMemUse() {
		final long totMem = Runtime.getRuntime().totalMemory(); // can't use maxMemory() because freeMemory() returns the difference with totalMemory, not maxMemory!
		final long freeMem = Runtime.getRuntime().freeMemory(); // freeMemory() returns the difference with totalMemory, not maxMemory!
		maxMemUsed = Math.max(maxMemUsed, totMem - freeMem);
		if (maxMemUsed > maxMemThreshold) {
			if (--memReportCountdown <= 0) {
				memReportCountdown = MEM_REPORT_FREQ;
				if (enabled) printOutLn("Max mem used: " + maxMemUsed);
			}
		}
		
		if (((double)freeMem)/totMem < MIN_FREEMEM_RATIO && !memFull) {
			final long currMsTime = System.currentTimeMillis();
			if (lastGCTime > currMsTime || currMsTime - lastGCTime > GC_PERIOD) {
				System.gc(); // give it one more chance
				lastGCTime = currMsTime;
			}
		}
		return ((double)freeMem)/totMem < MIN_FREEMEM_RATIO;
	}
	
//	// DEBUG: trick to ensure that, at the end of the execution, the max memory usage is reported
//	private final MemReporter memReporter = new MemReporter();
//	private class MemReporter {
//		@Override protected void finalize() throws Throwable { if (enabled) printOutLn("Max mem used: " + maxMemUsed); super.finalize(); }
//	}
	
	/** Provides info about all possible dependencies that can occur dynamically. (It actually correspond to static info!) */
	// hcai: make the reader static so that loading the static slice will be done once for all test cases 
	// private StaticSliceReader statSliceReader = null;
	private static StaticSliceReader statSliceReader = null;
	private StaticSliceReader getCreateSliceFileReader() {
		if (statSliceReader == null) {
			statSliceReader = new StaticSliceReader();
			eventsLeft = statSliceReader.getEventsLimit();
		}
		return statSliceReader;
	}
	private int eventsLeft = StaticSliceReader.NO_LIMIT;
	
	/** Updates set of defs that have became frameless at given frame and node. */
	private void updateLatestFramelessDefs(int frameId, int nId) {
		// clear set of latest frameless def events (left by a returned call) iff we moved on from returning point (frame and node)
		if (frameId != lastFrame || nId != Math.abs(lastPnt))
			DataEvent.latestFramelessDefEvs.clear();
	}
	/** Adds id to stack, unless frameId is already there, in which case removes all frames on top of frameId.
	 *  @param nId Current node, used to recognize whether we just returned from a callee (if so, we keep alive retval definitions while we are still at this point). */
	private void setCurrentFrameStack(Integer frameId, int nId) {
		// update set of defs that just became frameless; they need to stay uncollected while we are still at the return point
		updateLatestFramelessDefs(frameId, nId);
		
		// update frame stack while doing some immediate garbage-collection
		final int idxInStack = frameStack.indexOf(frameId);
		if (idxInStack == -1)
			frameStack.add(frameId); // add frameId
		else {
			// remove all frames on top of frameId (if there are any on top of it)
			for (int i = frameStack.size() - 1; i > idxInStack; --i) {
				final int remFrId = frameStack.remove(i);
				
				// garbage collect data for removed frame: kill open defs/brs whose frameId was removed
				List<DataEvent> defEvsToRemove = DataEvent.frameToEvs.remove(remFrId); // free up cache too
//				assert defEvsToRemove != null || brEvsToRemove != null;
				if (defEvsToRemove != null) {
					for (List<FwdSlice> fwdSlices : startToSlices.values()) {
						for (FwdSlice fwdSlice : fwdSlices) {
							for (DataEvent defEvToRem : defEvsToRemove) {
								// move to "frameless" cache if heap or ret-val (i.e., interproc) event; remove otherwise
								if (defEvToRem.interproc) {
									DataEvent.framelessEvs.add(defEvToRem);
									DataEvent.latestFramelessDefEvs.add(defEvToRem);
								}
								else {
									// "local" event: remove definitely
									fwdSlice.defPredDepInsts.remove(defEvToRem);
									fwdSlice.openDefs.remove(defEvToRem);
								}
							}
						}
					}
				}
				List<ControlEvent> brEvsToRemove = ControlEvent.frameToEvs.remove(remFrId);
				if (brEvsToRemove != null) {
					for (List<FwdSlice> fwdSlices : startToSlices.values()) {
						for (FwdSlice fwdSlice : fwdSlices) {
							for (ControlEvent brEvToRem : brEvsToRemove)
								fwdSlice.brPredDepInsts.remove(brEvToRem);
							for (Integer pntTgt : new ArrayList<Integer>(fwdSlice.openBrsForTgts.keySet())) {
								List<ControlEvent> brEvList = fwdSlice.openBrsForTgts.get(pntTgt);
								brEvList.removeAll(brEvsToRemove);
								if (brEvList.isEmpty())
									fwdSlice.openBrsForTgts.remove(pntTgt);
							}
						}
					}
				}
			}
		}
		
		// garbage-collect "expired" def events (i.e., dead objects, old ret-val defs)
		List<DataEvent> toCollect = new ArrayList<DataEvent>();
		for (DataEvent ev : DataEvent.framelessEvs) {
			if (!DataEvent.latestFramelessDefEvs.contains(ev)) {
				Object oBase = ev.objBase.get();
				if (oBase == null) {
					toCollect.add(ev);
					// remove frameless def event (object or ret val) from opendefs sets
					for (List<FwdSlice> fwdSlices : startToSlices.values())
						for (FwdSlice fwdSlice : fwdSlices)
							fwdSlice.openDefs.remove(ev);
					openDefsForDUCov.remove(ev);
				}
			}
		}
		DataEvent.framelessEvs.removeAll(toCollect);
	}
	
	/** Returns the next instance id for this dep, creating entry with inst-id 0 in map if there was no previous instance for this dep. */
	private int createNewDepInstId(int depId) {
		return nextDepInstId++;
	}
	/** If the current instance id for this point changes, advances to next point instance and clears last dep-instances from slices. */
	private void setCurrReportPntInst(int pnt, boolean entry, PntReportPos pntPos) {
		// update current point instance if there is a change in frame, point location, or point-report position
		final int currFrameId = frameStack.get(frameStack.size()-1);  // we are already keeping track of frame id in frame stack
		final boolean diffFrameOrPnt = currFrameId != lastFrame || (!entry && pnt != lastPnt);
		if (diffFrameOrPnt || pntPos != lastPntPos) {
			final boolean movedPosBack = pntPos.ordinal() < lastPntPos.ordinal(); // detects new pnt instance if the frame and pnt id are still the same!
			
			// update point instance data to new instance date
			lastFrame = currFrameId;
			lastPnt = pnt;
			lastPntPos = pntPos;
			
			// if we have moved past the end of the use-def-ctrl report order for a point instance, then we are definitely in a new point instance
			//   empty last-dep sets for all slices, since we already abandoned their target pntInst (if prev pnt was a dep target)
			if (diffFrameOrPnt || movedPosBack) {
				for (List<FwdSlice> fwdSlices : startToSlices.values())
					for (FwdSlice fwdSlice : fwdSlices)
						fwdSlice.lastDepInstsForCurrPntInsts.clear();
			}
		}
	}
	
	/** Helper for reporter methods. Returns an array-like result of the toString values of given variables. Handles case of exception thrown by call to toString.
	 *  Note: does not remove object id address after @ (the typical value when toString is not implemented), which is handled later by whoever parses this output. */
	private static String usedVarsToString(Object[] usedVarValues) {
		// create array-like output of toString values of variables; make sure that any exception thrown by toString is caught and becomes value of var
		String strUsedVars = "[";
		if (usedVarValues != null) {
			boolean first = true;
			for (Object oUsedVar : usedVarValues) {
				String strVal;
				try { strVal = oUsedVar.toString(); }  // ensure we catch exceptions thrown by toString call on subject's object
				catch (Throwable e) { strVal = "toString exception: " + e.toString(); }
				strUsedVars += (first? "" : ",") + strVal; // concat value, prepending a comma if not the first element
				first = false;
			}
		}
		strUsedVars += "]";
		
		return strUsedVars.replace("\n", "\\n").replace("\r", "\\r").replace(",", "COMMA"); // return result with some filtered characters
	}
	
	public void reportChange(int nId, boolean pre) {
		if (activated)
			return;
		activated = true;
		try { reportChange_IMPL(nId, pre); }
		finally { activated = false; }
	}
	private void reportChange_IMPL(int nId, boolean pre) {
		if (someStartReached) {
			if (eventsLeft == 0)
				return;
			if (eventsLeft != StaticSliceReader.NO_LIMIT)
				--eventsLeft;
		}
		
		getCreateSliceFileReader();
		
		if (statSliceReader.includeChangeCov()) {
			final int pntChange = pre? -nId : nId;
			if (enabled) printOutLn("CHANGE_COV " + pntChange);
		}
	}
	/** This is actually a wrapper that invokes {@link #reportDef} for each element in the array (index between 0 and size-1). */
	public void reportArrDef(int nId, boolean pre, int frameId, boolean entry, int varGroupId, Object oBase, int size, Object[] usedVarValues) {
		if (someStartReached) {
			if (eventsLeft == 0)
				return;
			if (eventsLeft != StaticSliceReader.NO_LIMIT)
				--eventsLeft;
		}
		
		for (int i = 0; i < size; ++i)
			reportDef(nId, pre, frameId, entry, varGroupId, true, oBase, i, usedVarValues); // always interproc
	}
	
	/** This wrapper makes it safer to manage activation/deactivation flag that avoids infinitely re-entrant reporting. */
	public void reportDef(int nId, boolean pre, int frameId, boolean entry, int varGroupId, boolean interproc, Object oBase, int idx, Object[] usedVarValues) {
		if (activated)
			return;
		activated = true;
		try {
			for (int varId : VarIdGroupReader.inst().getVarIds(varGroupId))
				reportDef_IMPL(nId, pre, frameId, entry, varId, interproc, oBase, idx, usedVarValues);
		}
		finally { activated = false; }
	}
	private void reportDef_IMPL(int nId, boolean pre, int frameId, boolean entry, int varId, boolean interproc, Object oBase, int idx, Object[] usedVarValues) {
		if (memFull || updateMemUse()) {
			memFull = true;
			return;
		}
		
		if (someStartReached) {
			if (eventsLeft == 0)
				return;
			if (eventsLeft != StaticSliceReader.NO_LIMIT)
				--eventsLeft;
		}
		
//		try{
		
		setCurrentFrameStack(frameId, nId);
		getCreateSliceFileReader();
		
		final int pntDef = pre? -nId : nId;
		setCurrReportPntInst(pntDef, entry, PntReportPos.DEF);
		
		if (statSliceReader.isStart(pntDef))
			someStartReached = true;
		
		if (!someStartReached)
			return; // no tracking of dep for ANY kind of coverage (chain, prop, du, etc.)
		
		// report event + USED VARS
		if (enabled) printOut("DYN SLICE EXAMINER: reportDef " + (pre? -nId : nId) + " " + frameId + " "); //+ varId + " " + oBase + " " + idx + " ");
		if (usedVarValues == null && statSliceReader.includeVals())
			{ if (enabled) printOutLn("<PURE KILL>"); }
		else { if (enabled) printOutLn( usedVarsToString(usedVarValues) ); }
		
		// before advancing existing slices, KILL open defs of same var
		for (List<FwdSlice> slicesForStart : startToSlices.values()) {
			for (FwdSlice fwdSlice : slicesForStart) {
				for (DataEvent openDef : new ArrayList<DataEvent>(fwdSlice.openDefs)) {
					if (openDef.var == varId && openDef.arridx == idx) {
						// remove open def if base was garbage-collected or matches this (re)defined var's base
						Object oOpenBase = openDef.objBase.get();
						if (oOpenBase == null /*was garb-collected*/ || oOpenBase == oBase)
							fwdSlice.openDefs.remove(openDef);
					}
				}
			}
		}
		if (statSliceReader.includeDUCov()) {
			for (DataEvent openDef : new ArrayList<DataEvent>(openDefsForDUCov)) {
				if (openDef.var == varId && openDef.arridx == idx) {
					// remove open def if base was garbage-collected or matches this (re)defined var's base
					Object oOpenBase = openDef.objBase.get();
					if (oOpenBase == null /*was garb-collected*/ || oOpenBase == oBase)
						openDefsForDUCov.remove(openDef);
				}
			}
		}
		
		// create data event for this def
		DataEvent defEvent = new DataEvent(pntDef, varId, interproc, oBase, idx, frameId);
		
		// begin slice if start point (note that there might be no dependencies covered for this new slice!)
		if (statSliceReader.isStart(pntDef)) {
			// get/create list for start point
			List<FwdSlice> slicesForStartPnt = Util.getCreateMapValue(startToSlices, pntDef, ArrayList.class);
			
			// begin new slice for start point
			FwdSlice fwdSlice = new FwdSlice(pntDef);
			slicesForStartPnt.add(fwdSlice);
			
			// set def as first open event for slice
			fwdSlice.openDefs.add(defEvent);
		}
		
		// for each slice in which a predecessor dep of def was last covered
		//   store data event as "open" def
		for (List<FwdSlice> slicesForStart : startToSlices.values()) {
			for (FwdSlice fwdSlice : slicesForStart) {
				// do two things:
				//   1) determine whether there is any predecessor dep for defEv, in which case defEv is added to open set for this slice
				//   2) build list of predecessor deps for this defEv in this slice
				List<DepInst> predDepInsts = new ArrayList<DepInst>();
				fwdSlice.defPredDepInsts.put(defEvent, predDepInsts);
				
				// check last non-branch deps
				Set<DepInst> lastDepInsts = fwdSlice.lastDepInstsForCurrPntInsts.get(pntDef);
				if (lastDepInsts != null) {
					for (DepInst lastDepInst : lastDepInsts) {
						// check if last dep's tgt pnt is this def's pnt
						RT_Dependence rtDepLast = statSliceReader.getDeps().get(lastDepInst.getDepId()); // should NOT crash because it's iteration's key
						// NOTE: it's ok to open def by matching last dep's tgt; when def->use dep is complete, we'll check if dep is succ of prev dep
						if (rtDepLast.getTgt() == pntDef) {
							fwdSlice.openDefs.add(defEvent); // 1) ensure def is in open set
							predDepInsts.add(lastDepInst);   // 2) associate this dep-inst with predecessors for potential future dep based on this def
						}
					}
				}
			}
		}
//		} catch (RuntimeException e) {
//			e.printStackTrace();
//		}
		
		// DU_COV monitoring
		if (statSliceReader.includeDUCov())
			openDefsForDUCov.add(defEvent);
		
		updateMemUse();
	}
	
	public void reportUse(int nId, boolean pre, int frameId, boolean entry, int varGroupId, Object oBase, int idx) {
		if (activated)
			return;
		activated = true;
		try {
			for (int varId : VarIdGroupReader.inst().getVarIds(varGroupId))
				reportUse_IMPL(nId, pre, frameId, entry, varId, oBase, idx);
		}
		finally { activated = false; }
	}
	private void reportUse_IMPL(int nId, boolean pre, int frameId, boolean entry, int varId, Object oBase, int idx) {
		if (memFull || updateMemUse()) {
			memFull = true;
			return;
		}
		
		if (someStartReached) {
			if (eventsLeft == 0)
				return;
			if (eventsLeft != StaticSliceReader.NO_LIMIT)
				--eventsLeft;
		}
		
		setCurrentFrameStack(frameId, nId);
		getCreateSliceFileReader();
		
		final int pntUse = pre? -nId : nId;
		setCurrReportPntInst(pntUse, entry, PntReportPos.USE);
		
		if (!someStartReached)
			return; // no tracking of dep for ANY kind of coverage (chain, prop, du, etc.)
		
//		try{
		// for each slice, find whether an "open" def matches this use
		for (Integer start : startToSlices.keySet()) {
			List<FwdSlice> slicesForStart = startToSlices.get(start);
			for (int sliceIdx = 0; sliceIdx < slicesForStart.size(); ++sliceIdx) {
				FwdSlice fwdSlice = slicesForStart.get(sliceIdx);
				DataEvent defMatched = null;
				for (DataEvent defEv : fwdSlice.openDefs) {
					if (defEv.var == varId) {
						// TODO: check frameId!
						
						// retrieve and keep base
						Object oDefBase = defEv.objBase.get();
						if (oDefBase == oBase && defEv.arridx == idx) {
							// mark complete dep in slice
							assert defMatched == null; // there can't be more than one def 'open' for this var+base+arridx
							defMatched = defEv;
						}
					}
				}
				if (defMatched == null)
					continue;
				
				// IMPORTANT: don't remove open def here because there might be more uses of it!
				
				// find corresponding dep and add it as covered in this slice
				int depCoveredId = -1;
				List<RT_Dependence> depsForSrc = statSliceReader.getDepsForSrc(defMatched.pnt);
				if (depsForSrc == null)
					continue; // it is possible that, because of an exception, a definition flows into this use without being detected statically
				for (RT_Dependence rtDep : depsForSrc) {
					if (rtDep.getVarOrBrId() == varId && rtDep.getSrc() == defMatched.pnt && rtDep.getTgt() == pntUse) {  // def and use match this dep
						// get id
						assert depCoveredId == -1; // there can only be one dep for this def and use (in separate calls to this method, other def-uses for other vars can occur)
						depCoveredId = rtDep.getId();
						
						// now, look for all edges to this dep from last deps (if any) in this slice
						List<DepInst> predDepInsts = fwdSlice.defPredDepInsts.get(defMatched); // retrieve predecessor deps collected previouly at def event
						if (fwdSlice.defPredDepInsts.get(defMatched).isEmpty()) {
							predDepInsts = new ArrayList<DepInst>();
//							// if no last (open) deps in slice, this dep initiates slice if def is slice's start point (slice instance is 'startIdx')
//							if (defMatched.pnt == fwdSlice.start)
//								predDepInsts.add(new DepInst(-1, sliceIdx)); // id -1 for 'start' dep; instance is always
						}
						
						DepInst depInstCovered = new DepInst(depCoveredId, createNewDepInstId(depCoveredId));
						if (enabled) printOutLn("DYNSLICE " + fwdSlice.start + ":" + sliceIdx + " COV DEP " + depInstCovered + " <- " + predDepInsts);
						Set<DepInst> lastDepInsts = Util.getCreateMapValue(fwdSlice.lastDepInstsForCurrPntInsts, pntUse, HashSet.class);
						lastDepInsts.add(depInstCovered);
						// NOTE: we keep checking to ensure that there are NO other matches between def and use
					}
				}
			}
		}
//		} catch (RuntimeException e) {
//			e.printStackTrace();
//		}
		
		// DU_COV monitoring: check which def is matched by this use (it can be none, since the def might occur before the start point)
		if (statSliceReader.includeDUCov()) {
			boolean duFound = false; // used to ensure that only ONE def matches use
			for (DataEvent defEv : openDefsForDUCov) {
				if (defEv.var == varId) {
					// TODO: check frameId!
					
					Object oDefBase = defEv.objBase.get();
					if (oDefBase == oBase && defEv.arridx == idx) {
						List<RT_Dependence> depsFromDef = statSliceReader.getDepsForSrc(defEv.pnt);
						if (depsFromDef != null) {
							for (RT_Dependence rtDep : depsFromDef) {
								if (rtDep.getVarOrBrId() == varId && rtDep.getSrc() == defEv.pnt && rtDep.getTgt() == pntUse) {
									assert !duFound;
									duFound = true;
									if (enabled) printOutLn("DU_COV " + rtDep.getId());
									// keep going to make that only one def matches
								}
							}
						}
					}
				}
			}
		}
		
		updateMemUse();
	}
	
	/** This wrapper makes it safer to manage activation/deactivation flag that avoids infinitely re-entrant reporting. */
	public void reportBranch(int nId, boolean pre, int brId, int frameId, Object[] usedVarValues) {
		if (activated)
			return;
		activated = true;
		try { reportBranch_IMPL(nId, pre, brId, frameId, usedVarValues); }
		finally { activated = false; }
	}
	/** This method only reports the branch and the frame in which it occurs; we later need to determine the frame of the cd-targets.
	 *  A branch guarantees that the targets will occur; however, to know on which br the tgt is dyn-cd, we keep a stack of br's for each cd tgt. */
	private void reportBranch_IMPL(int nId, boolean pre, int brId, int frameId, Object[] usedVarValues) {
		if (memFull || updateMemUse()) {
			memFull = true;
			return;
		}
		
		if (someStartReached) {
			if (eventsLeft == 0)
				return;
			if (eventsLeft != StaticSliceReader.NO_LIMIT)
				--eventsLeft;
		}
		
		setCurrentFrameStack(frameId, nId);
		getCreateSliceFileReader();
		
		final int brSrcPnt = (pre? -nId : nId); //rtDepBranch.getSrc();
		setCurrReportPntInst(brSrcPnt, false, PntReportPos.CDSRC);
		
		if (statSliceReader.isStart(brSrcPnt))
			someStartReached = true;
		
		if (!someStartReached)
			return; // no tracking of dep for ANY kind of coverage (chain, prop, du, etc.)
		
//		try{
		if (enabled) printOutLn("DYN SLICE EXAMINER: reportBranch " + brSrcPnt + " " + frameId + " " + usedVarsToString(usedVarValues));
		
		ControlEvent brEvent = new ControlEvent(brSrcPnt, brId, frameId);
		
		// before advancing existing slices, begin slice if branch's src is a start point
		if (statSliceReader.isStart(brSrcPnt)) {
			// get/create list of slices for start point
			List<FwdSlice> slicesForStartPnt = Util.getCreateMapValue(startToSlices, brSrcPnt, ArrayList.class);
			
			// begin new slice for start point
			FwdSlice fwdSlice = new FwdSlice(brSrcPnt);
			slicesForStartPnt.add(fwdSlice);
			
			// add control event to set of open control events for all tgts of br (which will be reached, but later and at perhaps some other frame)
			updateTgtPntsOpenBrStacks(fwdSlice, brEvent);
		}
		
		// for each slice in which a predecessor dep of br's src was last covered
		//   find list of pred deps for this opening branch
		for (List<FwdSlice> slicesForStart : startToSlices.values()) {
			for (int sliceIdx = 0; sliceIdx < slicesForStart.size(); ++sliceIdx) {
				FwdSlice fwdSlice = slicesForStart.get(sliceIdx);
				
				// do two things:
				//   1) determine whether there is any predecessor dep for brEv, in which case brEv is added to stack of open cds for each cd-target of br
				//   2) build list of predecessor deps for this brEv in this slice
				List<DepInst> predDepInsts = new ArrayList<DepInst>();
				fwdSlice.brPredDepInsts.put(brEvent, predDepInsts);
				boolean addEvToTgtStacks = false; // used to update cd-tgt stacks with brEv only once
				// check last data deps
				Set<DepInst> lastDepInsts = fwdSlice.lastDepInstsForCurrPntInsts.get(brSrcPnt);
				if (lastDepInsts != null) {
					for (DepInst lastDepInst : lastDepInsts) {
						// checking if last dep's tgt pnt is this br's pnt is NOT ENOUGH; need to check frame ids too
						RT_Dependence rtDepLast = statSliceReader.getDeps().get(lastDepInst.getDepId()); // should NOT crash because it's iteration's key
						if (rtDepLast.getTgt() == brSrcPnt) {
							addEvToTgtStacks = true;
							predDepInsts.add(lastDepInst);
						}
					}
				}
				if (addEvToTgtStacks)
					updateTgtPntsOpenBrStacks(fwdSlice, brEvent);
			}
		}
//		} catch (RuntimeException e) {
//			e.printStackTrace();
//		}
		
		if (statSliceReader.includeBrCov()) {
			// report br src/id pair only once
			Pair<Integer,Integer> brPntAndId = new Pair<Integer,Integer>(brSrcPnt, brId);
			if (brsCov.add(brPntAndId))
				if (enabled) printOutLn("BR_COV " + brSrcPnt + " " + brId);
		}
		
		updateMemUse();
	}
	/** Helper for {@code reportBranch}. */
	private void updateTgtPntsOpenBrStacks(FwdSlice fwdSlice, ControlEvent brEvent) {
		for (Integer tgt : statSliceReader.getTgtsForSrc(brEvent.srcPnt)) {
			if (statSliceReader.getDep( statSliceReader.getControlDepIdForSrcTgt(brEvent.srcPnt, tgt) ).getVarOrBrId() != brEvent.ctrlId)
				continue; // don't open br's with a different id!
			
			List<ControlEvent> brEventsForTgt = Util.getCreateMapValue(fwdSlice.openBrsForTgts, tgt, ArrayList.class);
//			assert !brEventsForTgt.contains(brEvent); // safety check... that would work except for exceptions that can mess with it
			brEventsForTgt.add(brEvent); // adds br-event to stack for tgt point
		}
	}
	
	/** This wrapper makes it safer to manage activation/deactivation flag that avoids infinitely re-entrant reporting. */
	public void reportCallSrc(int nId, int frameId, Object[] usedVarValues) {
		if (activated)
			return;
		activated = true;
		try { reportCallSrc_IMPL(nId, frameId, usedVarValues); }
		finally { activated = false; }
	}
	/** Handles first part of a multi-target virtual-call edge. Position is always 'pre'. */
	private void reportCallSrc_IMPL(int nId, int frameId, Object[] usedVarValues) {
		if (memFull || updateMemUse()) {
			memFull = true;
			return;
		}
		
		if (someStartReached) {
			if (eventsLeft == 0)
				return;
			if (eventsLeft != StaticSliceReader.NO_LIMIT)
				--eventsLeft;
		}
		
		setCurrentFrameStack(frameId, nId);
		getCreateSliceFileReader();
		
		final int csSrcPnt = -nId; // always in 'pre' position
		setCurrReportPntInst(csSrcPnt, false, PntReportPos.CDSRC);
		
		// before advancing existing slices, begin slice if callsite is a start point
		if (statSliceReader.isStart(csSrcPnt)) {
			someStartReached = true;
			
			// get/create list of slices for start point
			List<FwdSlice> slicesForStartPnt = Util.getCreateMapValue(startToSlices, csSrcPnt, ArrayList.class);
			
			// begin new slice for start point
			FwdSlice fwdSlice = new FwdSlice(csSrcPnt);
			slicesForStartPnt.add(fwdSlice);
			
			// set cs as opening first part of call-edge event
			fwdSlice.openCSSrc = csSrcPnt;
			assert fwdSlice.csPredDepInsts.isEmpty();
		}
		
		if (!someStartReached)
			return; // no tracking of dep for ANY kind of coverage (chain, prop, du, etc.)
		
		if (enabled) printOutLn("DYN SLICE EXAMINER: reportCallSrc " + csSrcPnt + " " + frameId + " " + usedVarsToString(usedVarValues));
		
		// for each slice in which a predecessor dep of cs was last covered
		//   find list of pred deps for this opening cd
		for (List<FwdSlice> slicesForStart : startToSlices.values()) {
			for (int sliceIdx = 0; sliceIdx < slicesForStart.size(); ++sliceIdx) {
				FwdSlice fwdSlice = slicesForStart.get(sliceIdx);
				
				// first of all: close open CS
				fwdSlice.closeOpenCS();
				
				// do two things:
				//   1) determine whether there is any predecessor dep for csEv, in which case we store preds for csEv to later (at csTgt) add to stack of open cds for each cd-target
				//   2) build list of predecessor deps for this csEv in this slice
				// check last data deps
				Set<DepInst> lastDepInsts = fwdSlice.lastDepInstsForCurrPntInsts.get(csSrcPnt);
				if (lastDepInsts != null) {
					for (DepInst lastDepInst : lastDepInsts) {
						// checking if last dep's tgt pnt is this def's pnt is NOT ENOUGH; need to check frame ids too
						RT_Dependence rtDepLast = statSliceReader.getDeps().get(lastDepInst.getDepId()); // should NOT crash because it's iteration's key
						if (rtDepLast.getTgt() == csSrcPnt) {
							fwdSlice.openCSSrc = csSrcPnt;                // 1) ensure cs-src is open to match next at cs-tgt report
							fwdSlice.csPredDepInsts.add(lastDepInst); // 2) add predecessor deps for this csEv in this slice
						}
					}
				}
			}
		}
		
		updateMemUse();
	}
	
	/** Handles second part of a multi-target virtual-call edge. */
	public void reportCallTgt(int mtdId, int frameId) {
		if (activated)
			return;
		activated = true;
		try { reportCallTgt_IMPL(mtdId, frameId); }
		finally { activated = false; }
	}
	private void reportCallTgt_IMPL(int mtdId, int frameId) {
		if (memFull || updateMemUse()) {
			memFull = true;
			return;
		}
		
		setCurrentFrameStack(-1, frameId); // node id doesn't matter in this case because frame id is new
		getCreateSliceFileReader();
		
		if (!someStartReached)
			return; // no tracking of dep for ANY kind of coverage (chain, prop, du, etc.)
		
		ControlEvent callEvent = null; // will be created the first time we find open cs for any slice
		
		for (List<FwdSlice> slicesForStart : startToSlices.values()) {
			for (int sliceIdx = 0; sliceIdx < slicesForStart.size(); ++sliceIdx) {
				FwdSlice fwdSlice = slicesForStart.get(sliceIdx);
				if (fwdSlice.openCSSrc != FwdSlice.NO_CS) {
					// create call (ctrl) event if this is the first slice for which we find a cs-src open
					if (callEvent == null)
						callEvent = new ControlEvent(fwdSlice.openCSSrc, mtdId, frameId); // use frameId of call-tgt to characterize this event
					else
						assert callEvent.srcPnt == fwdSlice.openCSSrc; // just in case
					
					// update ctrlEv stack for each cd target point to be reached in this slice
					updateTgtPntsOpenBrStacks(fwdSlice, callEvent);
					
					// copy list of predecessor deps from cs-src to this new callEv in this slice
					assert fwdSlice.csPredDepInsts != null;
					fwdSlice.brPredDepInsts.put(callEvent, fwdSlice.csPredDepInsts);
					
					// "close" cs-src for this slice
					fwdSlice.closeOpenCS();
				}
			}
		}
		
		updateMemUse();
	}
	
	public void reportCDTgt(int nId, boolean pre, int frameId, boolean entry) {
		if (activated)
			return;
		activated = true;
		try { reportCDTgt_IMPL(nId, pre, frameId, entry); }
		finally { activated = false; }
	}
	private void reportCDTgt_IMPL(int nId, boolean pre, int frameId, boolean entry) {
		if (memFull || updateMemUse()) {
			memFull = true;
			return;
		}
		
		if (someStartReached) {
			if (eventsLeft == 0)
				return;
			if (eventsLeft != StaticSliceReader.NO_LIMIT)
				--eventsLeft;
		}
		
		setCurrentFrameStack(frameId, nId);
		getCreateSliceFileReader();
		
		final int pntTgt = pre? -nId : nId;
		setCurrReportPntInst(pntTgt, entry, PntReportPos.CDTGT);
		
		if (!someStartReached)
			return; // no tracking of dep for ANY kind of coverage (chain, prop, du, etc.)
		
//		try{
		// for each slice, find whether an "open" branch matches this cd-tgt in the stack for this tgt
		ControlEvent _check_lastOpenBrForTgt = null;
		for (List<FwdSlice> slicesForStart : startToSlices.values()) {
			for (int sliceIdx = 0; sliceIdx < slicesForStart.size(); ++sliceIdx) {
				FwdSlice fwdSlice = slicesForStart.get(sliceIdx);
				
				// find last br event for this tgt in this slice (for those slices in which a br event exists, it should be the same!)
				List<ControlEvent> brEvsForTgt = fwdSlice.openBrsForTgts.get(pntTgt);
				if (brEvsForTgt == null)
					continue;
				assert !brEvsForTgt.isEmpty();
				ControlEvent lastOpenBrForTgt = brEvsForTgt.remove(brEvsForTgt.size() - 1); // extract and remove
				// sanity check: for those slices in which a br event exists, it should be the same!
				// TODO: FIX!!!  *** fails on print_tokens1, tests 1448, 1781, and 2064
//				assert _check_lastOpenBrForTgt == null || _check_lastOpenBrForTgt.equals(lastOpenBrForTgt);
				_check_lastOpenBrForTgt = lastOpenBrForTgt;
				// also, clean up stack if emptied
				if (brEvsForTgt.isEmpty())
					fwdSlice.openBrsForTgts.remove(pntTgt);
				
				final int depCoveredId = statSliceReader.getControlDepIdForSrcTgt(lastOpenBrForTgt.srcPnt, pntTgt);
				DepInst depInstCovered = new DepInst(depCoveredId, createNewDepInstId(depCoveredId));
				List<DepInst> predDepInsts = fwdSlice.brPredDepInsts.get(lastOpenBrForTgt);
				assert predDepInsts != null;
				if (enabled) printOutLn("DYNSLICE " + fwdSlice.start + ":" + sliceIdx + " COV DEP " + depInstCovered + " <- " + predDepInsts);
				Set<DepInst> lastDepInsts = Util.getCreateMapValue(fwdSlice.lastDepInstsForCurrPntInsts, pntTgt, HashSet.class);
				lastDepInsts.add(depInstCovered);
			}
		}
//		} catch (RuntimeException e) {
//			e.printStackTrace();
//		}
		
		updateMemUse();
	}
	
}
