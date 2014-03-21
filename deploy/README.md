## Development Setup

1. Download and install [Vagrant](http://www.vagrantup.com/downloads.html).

2. Download and install [VirtualBox](https://www.virtualbox.org/wiki/Downloads).

3. Download and install [Ansible](http://docs.ansible.com/intro_installation.html#installing-the-control-machine).
   * On Mac OSX machines with [Homebrew](http://brew.sh/) installed, you can simply run: `$ brew install ansible`

4. Change to this directory on your development machine.

    ```bash
    $ cd /path/to/developer.rackspace.com/deploy
    ```

5. Run Vagrant to set up a VirtualBox VM running a development environment.

    ```bash
    $ vagrant up
    ```

6. That's it! Your development environment is setup in a VirtualBox VM! It's contents are:
   * An elasticsearch service, accessible via a REST API at [http://localhost:9200](http://localhost:9200).

