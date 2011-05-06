library(maps, lib.loc="/home/pb/tmp/tools/R/packages/")
library(bitops, lib.loc="/home/pb/tmp/tools/R/packages/")
library(RCurl, lib.loc="/home/pb/tmp/tools/R/packages/")

cmd = commandArgs(trailingOnly = TRUE)

auth <- getURL("http://127.0.0.1:8080/auth/v1.0", header = TRUE,
       httpheader = c('X-Storage-User' = "test:tester",
         'X-Storage-Pass' = "testing"))

a <- strsplit(auth, "\\\"")
aa <- strsplit(a[[1]][1], "\\r\\n")

token = aa[[1]][4]
url = a[[1]][10]

data(world.cities)
map("world")

cons <- strsplit(getURL(url, httpheader = c(token)), "\\n")
for (i in 1:length(cons[[1]])) {
    if (grepl(cmd, cons[[1]][i])) {
      objs <- strsplit(getURL(paste(url, "/",
                                    cons[[1]][i], sep = ""),
                              httpheader = c(token)), "\\n")
      for (j in 1:length(objs[[1]])) {
        recs <- strsplit(getURL(paste(url, "/",
                                      cons[[1]][i], "/",
                                      objs[[1]][j], sep = ""),
                                httpheader = c(token)), "\\n")
        for (n in 2:length(recs[[1]])) {
          rec <- strsplit(recs[[1]][n], ",")
          if (grepl(rec[[1]][3], "F")) {
            color <- "red"
          } else {
            color <- "green"
          }

          ip <- strsplit(rec[[1]][1], "\\.")
          lo = world.cities$long[as.numeric(ip[[1]][1])]
          la = world.cities$lat[as.numeric(ip[[1]][1])]
          
          points(lo, la, col = color, bg = color, cex = 1.0, pch = 21)
        }
      }
    }
}
