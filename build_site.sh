#!/bin/bash

set -u

#
# This script builds the various parts of the site (Getting Started guides,
# blog, etc.) and combines them into a single directory, `site_html`.
#
PROJECT_ROOT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
BUILD_DIR=/tmp/developer.rackspace.com
WORK_DIR=$BUILD_DIR/_work
TARGET_DIR=$PROJECT_ROOT/_site

ALL_PIDS=$(pgrep build_site.sh)
NUM_ALL_PIDS=$(echo "$ALL_PIDS" | wc -l)

if (( $NUM_ALL_PIDS > 1 )); then

  # If there's clones of me already running, kill all of them
  # so I'm the only one running. This isn't perfect because
  # there is a chance that two of these processes could kill
  # each other but, short of implementing a semaphore, there
  # is no way to ensure that they don't.
  for pid in $ALL_PIDS; do
    if [ $pid != $$ ]; then
      echo "Killing my clone with PID = $pid"
      kill $pid
    fi
  done

fi

# Create temporary work directory
rm -rf $WORK_DIR
mkdir -p $WORK_DIR

# Copy the site source into the work directory
rsync -Ca $PROJECT_ROOT/src/site_source/ $WORK_DIR/

# Use the default configuration unless a configuration has already been
# specified.
[ -f $WORK_DIR/_config.yml ] || cp $WORK_DIR/_config.default.yml $WORK_DIR/_config.yml

# Build the Getting Started guides in the `docs/` directory using Sphinx
cd $PROJECT_ROOT/src/docs
/usr/local/bin/sphinx-build . $WORK_DIR/docs

# Build the web site HTML
export LANG=en_US.UTF-8
export LC_ALL=en_US.UTF-8
cd $WORK_DIR
/usr/local/bin/jekyll build --source . --destination $BUILD_DIR/_site

# Copy to target dir
rsync -Ca $BUILD_DIR/_site/ $TARGET_DIR/
