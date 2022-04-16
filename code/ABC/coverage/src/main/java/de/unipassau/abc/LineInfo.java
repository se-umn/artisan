package de.unipassau.abc;

public class LineInfo {

    private int coverageType = 0;//0 not covered; 1 ICounter.FULLY_COVERED or ICounter.PARTLY_COVERED;
    private int coveredInstructionsCount = 0;
    private int coveredBranchCount = 0;

    public LineInfo(int coverageCount, int coveredInstructionsCount, int coveredBranchCount){
        this.coverageType =coverageCount;
        this.coveredInstructionsCount=coveredInstructionsCount;
        this.coveredBranchCount=coveredBranchCount;
    }

    public int getCoverageType() {
        return coverageType;
    }

    public int getCoveredInstructionsCount() {
        return coveredInstructionsCount;
    }

    public int getCoveredBranchCount() {
        return coveredBranchCount;
    }

}
