package de.unipassau.abc;

public class LineInfo {

    private int coverageCount = 0;
    private int coveredInstructionsCount = 0;

    public LineInfo(int coverageCount, int coveredInstructionsCount){
        this.coverageCount=coverageCount;
        this.coveredInstructionsCount=coveredInstructionsCount;
    }

    public int getCoverageCount() {
        return coverageCount;
    }

    public int getCoveredInstructionsCount() {
        return coveredInstructionsCount;
    }

}
