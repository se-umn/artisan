package de.unipassau.abc;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Coverage {
    private final Map<String, Map<Integer, LineInfo>> coverage;
    private final int nCoverageFiles;

    public Coverage(Map<String, Map<Integer, LineInfo>> coverage, int nCoverageFiles) {
        this.coverage = coverage;
        this.nCoverageFiles = nCoverageFiles;
    }

    public Map<String, Map<Integer, LineInfo>> get() {
        return coverage;
    }
}