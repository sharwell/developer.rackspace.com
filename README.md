Workflow
================

Needed:
* Stage 1: build jekyll site; if no errors; put into directory pending push to nginx
* Stage 2: build sphinx docs; if no errors; put into stage 1 directory tree under /docs
* Stage 3: build API index; if no errors; put into stage 1 directory tree under /apis
* Stage 4: build feeds (everett); if no errors; put into stage 1 directory tree under /???
* Stage 4: Push assets to cdn (tbd)
* Stage 5: Push stage 1 static dir to nginx 

Continuous Integration
================
* staging-jenkins (+1 slave node. note: slave nodes need the exact same software stack as master node. Unlike Travis, Jenkins needs the environment to be setup ahead of time.)
* 2-stage build process following workflow above
  * Job "Build Site" performs Stage 1 related tasks
  * Then, if "Build Site" succeeds, triggers job "Build Sphinx Docs" which runs the tasks deliniated in Stage 2 above.

