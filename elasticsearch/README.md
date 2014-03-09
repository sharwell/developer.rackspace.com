## Development Setup

1. Download and install [Vagrant](http://www.vagrantup.com/downloads.html).

2. Download and install [VirtualBox](https://www.virtualbox.org/wiki/Downloads).

3. Download and install [Ansible](http://docs.ansible.com/intro_installation.html#installing-the-control-machine).
   * On Mac OSX machines with [Homebrew](http://brew.sh/) installed, you can simply run: `$ brew install ansible`

4. Change to this directory on your development machine.

    ```bash
    $ cd /path/to/developer.rackspace.com/elastic_search
    ```

5. Run Vagrant to set up a VirtualBox VM running an ElasticSearch node.

    ```bash
    $ vagrant up
    ```

6. That's it! The Elasticsearch REST API will be available at [`http://localhost:9200`](http://localhost:9200).

## TODO
* Create ansible playbook(s) for prod setup. 
* Create river for ingesting GitHub issues from various SDK repos.
  - One index per SDK repo?
  - Should issues be a type within the index?
* Create river for ingesting getting started docs (TBD).
* Create push hook for github issues.
* Create push hook for getting started docs (part of Jenkins build process?)
* Create push hook for developer blog posts (part of Jenkins build process?)
 
## DONE
* Create river for ingesting developer blog.
