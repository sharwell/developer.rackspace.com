#!/bin/bash

#
# This script builds the various parts of the site (Getting Started guides,
# blog, etc.) and combines them into a single directory, `site_html`.
#

WORK_DIR=$(pwd)/_work
TARGET_DIR=$(pwd)/_site

# Create temporary work directory
rm -rf $WORK_DIR
mkdir $WORK_DIR

# Copy the site source into the work directory
rsync site_source/ $WORK_DIR/

# Build the Getting Started guides in the `docs/` directory using Sphinx
cd ./docs
/usr/local/bin/sphinx-build . $WORK_DIR/site_source/docs
cd -

# Build the web site HTML
cd $WORK_DIR/site_source
/usr/local/bin/jekyll --verbose --source . --destination $TARGET_DIR
cd -
