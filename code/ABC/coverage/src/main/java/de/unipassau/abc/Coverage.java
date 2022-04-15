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
    private final Map<String, Map<Integer, Integer>> coverage;
    private final int nCoverageFiles;

    public Coverage(Map<String, Map<Integer, Integer>> coverage, int nCoverageFiles) {
        this.coverage = coverage;
        this.nCoverageFiles = nCoverageFiles;
    }

    public Map<String, Map<Integer, Integer>> get() {
        return coverage;
    }

    public void writeToDir(final IWriter writer, boolean ignoreUncovered) throws IOException {
        writer.toDir(this, ignoreUncovered);
    }

    public void writeToFile(final IWriter writer, boolean ignoreUncovered) throws IOException {
        writer.toFile(this, ignoreUncovered);
    }

    public Map<String, Map<Integer, Integer>> getFlakyLines() {
        Map<String, Map<Integer, Integer>> flakyCoverage = new TreeMap<>();
        coverage.forEach((className, lineCoverage) -> {

            Map<Integer, Integer> flakyLines = lineCoverage.entrySet().stream().filter(lineEntry -> {
                int nTimesCovered = lineEntry.getValue();
                return nTimesCovered != 0 && nTimesCovered != nCoverageFiles;
            }).collect(Collectors.toMap(Entry::getKey, Entry::getValue, Math::addExact, TreeMap::new));

            if (!flakyLines.isEmpty()) {
                flakyCoverage.put(className, flakyLines);
            }
        });

        return flakyCoverage;
    }

    /**
     * Compute the symmetric difference to another coverage object.
     */
    public Coverage diff(final Coverage other) {
        Map<String, Map<Integer, Integer>> diffCoverage = new TreeMap<>();

        Set<String> allClassNames = new HashSet<>(coverage.keySet());
        allClassNames.addAll(other.coverage.keySet());

        for (String className : allClassNames) {
            Set<Integer> thisCoveredLines;
            Set<Integer> otherCoveredLines;
            if (coverage.containsKey(className)) {
                 thisCoveredLines = coverage.get(className).keySet();
            } else {
                thisCoveredLines = new HashSet<>();
            }

            if (other.coverage.containsKey(className)) {
                otherCoveredLines = other.coverage.get(className).keySet();
            } else {
                otherCoveredLines = new HashSet<>();
            }

            Set<Integer> diff12 = new HashSet<>(thisCoveredLines);
            Set<Integer> diff21 = new HashSet<>(otherCoveredLines);
            diff12.removeAll(otherCoveredLines);
            diff21.removeAll(thisCoveredLines);
            diff12.addAll(diff21);

            Map<Integer, Integer> lineCoverage = new HashMap<>();
            for (Integer line : diff12) {
                int count;
                if (thisCoveredLines.contains(line)) {
                    count = coverage.get(className).get(line);
                } else {
                    count = other.coverage.get(className).get(line);
                }

                lineCoverage.put(line, count);
            }

            diffCoverage.put(className, lineCoverage);
        }

        return new Coverage(diffCoverage, -1);
    }
}