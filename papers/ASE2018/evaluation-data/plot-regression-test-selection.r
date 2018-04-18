#!/usr/bin/Rscript
args = commandArgs(trailingOnly=TRUE)
# test there are two arguments: if not, return an error
if (length(args) != 3) {
    stop("Wrong number of arguments (csv input file, 2 pdf output files).\n", call.=FALSE)
}

# https://stackoverflow.com/questions/30750049/in-r-how-to-make-a-boxplot


# Colums
# V1 N of modified src files
# V2 triggered System Tests
# V3 total system tests
# V4 System test execution time
# V5 triggered carved test
# V6 total carved test
# V7 Carved test execution time
# V8 triggered MIN carved test
# V9 total MIN carved test
# V10 MIN carved test execution time
MyData <- read.csv(file=args[1], header=FALSE, sep=",")

### Here we might need to compute statistics
agg <- aggregate(. ~ V1, MyData, FUN = "mean")

# Include default row
agg <- rbind( c(0,0,max(MyData$V3),0,0,max(MyData$V6),0,0,max(MyData$V9),0), agg)

## Compute relative values
st <- agg$V2/agg$V3 * 100
ct <- agg$V5/agg$V6 * 100
minCt <- agg$V8/agg$V9 * 100
# x <- agg$V1/max(agg$V1) * 100
x <- agg$V1

# Plot the distribution of regression tests as we modify more source files
pdf(file = args[2])

plot(x,st,type="l", col="red", xlab = "Modified Classes", ylab = "Executed Tests (%)")
lines(x,ct, col="blue", lty=2)
lines(x,minCt, col="green", lty=2)
legend(x="topleft", legend=c("System Tests", "Carved Tests", "Minimized Tests"),
col=c("red", "blue", "green"), box.lty=0, lty=1:2, cex=0.8)
dev.off()

## Conside execution time now
st <- agg$V4
ct <- agg$V7
minCt <- agg$V10

# Plot the execution time of regression tests as we modify more source files

pdf(file = args[3])
plot(x,st,type="l", col="red", xlab = "Modified Classes (%)", ylab = "Avg Execution Time (s)",
xlim=c(0, max(x)), ylim=c(0, max(c(st, ct, minCt))))
lines(x,ct, col="blue", lty=2)
lines(x,minCt, col="green", lty=2)
legend(x="topleft", legend=c("System Tests", "Carved Tests", "Minimized Tests"),
col=c("red", "blue", "green"), box.lty=0, lty=1:2, cex=0.8)
dev.off()


# Aggregate and plot boxed
# medlty = 0 Do not show median
# outlty = 0 Do not show outlayers
# https://stackoverflow.com/questions/28889977/r-boxplot-how-to-customize-the-appearance-of-the-box-and-whisker-plots-e-g-r
# with(MyData,boxplot(V2/V3*100~V1, xlab = "Modified Classes", ylab = "Executed Tests (%)", medlty = 0, outlty = 0 ))
#agg <- aggregate(. ~ V1, MyData, FUN = "mean")
#agg <- rbind( c(0,0,max(MyData$V3),0,0,max(MyData$V6),0,0,max(MyData$V9),0), agg)
#with(MyData, plot(V1, V2/V3*100, xlab = "Modified Classes", ylab = "Executed Tests (%)", type="l", col="red"))
