#!/bin/bash

#
# This script builds the various parts of the site (Getting Started guides,
# blog, etc.) and combines them into a single directory, `site_html`.
#
PROJECT_ROOT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
BUILD_DIR=/tmp/developer.rackspace.com
WORK_DIR=$BUILD_DIR/_work
TARGET_DIR=$PROJECT_ROOT/_site

# Create temporary work directory
rm -rf $WORK_DIR
mkdir -p $WORK_DIR

# Copy the site source into the work directory
rsync -Ca $PROJECT_ROOT/src/site_source/ $WORK_DIR/

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
