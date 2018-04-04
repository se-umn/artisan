#!/usr/bin/Rscript
args = commandArgs(trailingOnly=TRUE)
# test there are two arguments: if not, return an error
if (length(args) != 2) {
    stop("Wrong number of arguments (csv input file, pdf output file).\n", call.=FALSE)
}
MyData <- read.csv(file=args[1], header=FALSE, sep=",")
agg <- aggregate(. ~ V1, MyData, sum)
agg <- rbind( c(0,0,1,0,1), agg)
st <- agg$V2/agg$V3 * 100
ct <- agg$V4/agg$V5 * 100
x <- agg$V1/max(agg$V1) * 100
pdf(file = args[2])
plot(x,st,type="l", col="red", xlab = "Modified Classes (%)", ylab = "Executed Tests (%)")
lines(x,ct, col="blue", lty=2)
legend(1, 95, legend=c("System Tests", "Carved Tests"),
col=c("red", "blue"), lty=1:2, cex=0.8,
box.lty=0)
dev.off()
