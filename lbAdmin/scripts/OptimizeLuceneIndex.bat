@echo off
java -Xmx200m -cp "..\runtime\lbPatch.jar;..\runtime\lbRuntime.jar" org.lexevs.dao.index.operation.tools.OptimizeLuceneIndexLauncher %*