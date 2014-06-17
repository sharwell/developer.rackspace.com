# developer.rackspace.com

Third gen of the developer.rackspace.com Portal

This repo represents a refactor of the developer.rackspace.com site.

The dev site currently builds from rackerlabs/devsite but only accepts new blog posts as of March 1 2014.

We are migrating to this repo by June-ish 2014. The areas now building are:
- `/src/docs/`: Getting Started Guides, built using Sphinx
- `/src/site_source`: Rest of the web site layout and content, built using Jekyll

In progress:
- /api/: maven, content maintained in rackerlabs/docs-api-reference
- elastic search

To do:
- assets to CDN
- feeds

## Development Setup

1. Download and install [Vagrant 1.6 or higher](http://www.vagrantup.com/downloads.html).

2. Download and install [VirtualBox](https://www.virtualbox.org/wiki/Downloads).

3. Download and install [Ansible](http://docs.ansible.com/intro_installation.html#installing-the-control-machine).
   * On Mac OSX machines with [Homebrew](http://brew.sh/) installed, you can simply run: `$ brew install ansible`

4. Ensure that you are in the root directory of this repo; e.g., `cd ~/src/developer.rackspace.com` (or wherever you've cloned it). If you haven't cloned the repo, fork it via the Github web interface and then

    ```bash
    $ git clone {your-repo-path}/developer.rackspace.com
    ```

5. Then, fetch the git submodules if you haven't already.

   ```bash
   $ git submodule update --init
   ```

6. Run Vagrant to set up a VirtualBox VM running a development environment and automatically publish changes to local files to the development VM. **Note: This command will run in the foreground and will not allow this terminal window to be used for anything else. If you need to do more commandline work, please open a new window and continue there.**

    ```bash
    $ vagrant up && vagrant rsync-auto
    ```

7. That's it! Your development environment is setup in a VirtualBox VM! It's contents are:
   * A web server running Nginx, accessible at [http://localhost:8000](http://localhost:8000).
      * To access the Getting Started Guides, go to http://localhost:8000/docs/
   * If you have problems with the watcher process, view the logs with:
     ```bash
     sudo tail -F /var/log/upstart/watcher.log
     ```

