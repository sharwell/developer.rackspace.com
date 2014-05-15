#!/bin/bash

#
# This script builds the various parts of the site (Getting Started guides,
# blog, etc.) and combines them into a single directory, `site_html`.
#
PROJECT_ROOT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
BUILD_DIR=/usr/local/src/developer.rackspace.com
WORK_DIR=$BUILD_DIR/_work
TARGET_DIR=/var/www/html/developer.rackspace.com

# Create temporary work directory
rm -rf $WORK_DIR
mkdir $WORK_DIR

# Copy the site source into the work directory
rsync -Ca $PROJECT_ROOT/src/site_source/ $WORK_DIR/

# Build the Getting Started guides in the `docs/` directory using Sphinx
cd $PROJECT_ROOT/src/docs
/usr/local/bin/sphinx-build . $WORK_DIR/docs

# Build the web site HTML
cd $WORK_DIR
/usr/local/bin/jekyll build --source . --destination $TARGET_DIR
