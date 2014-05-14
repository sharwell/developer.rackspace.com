#!/bin/bash

#
# This script builds the various parts of the site (Getting Started guides,
# blog, etc.) and combines them into a single directory, `site_html`.
#

BUILD_DIR=/usr/local/src/developer.rackspace.com
WORK_DIR=$BUILD_DIR/_work
TARGET_DIR=$BUILD_DIR/_site

# Create temporary work directory
rm -rf $WORK_DIR
mkdir $WORK_DIR

# Copy the site source into the work directory
rsync -Ca /vagrant_data/src/site_source/ $WORK_DIR/

# Build the Getting Started guides in the `docs/` directory using Sphinx
cd /vagrant_data/src/docs
/usr/local/bin/sphinx-build . $WORK_DIR/docs

# Build the web site HTML
cd $WORK_DIR
/usr/local/bin/jekyll build --source . --destination $TARGET_DIR

# Copy site to build directory
rsync -Ca $TARGET_DIR/ /var/www/html/developer.rackspace.com

