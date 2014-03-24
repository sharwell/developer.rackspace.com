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
   * A web server running Nginx, accessible at [http://localhost:8000](http://localhost:8000).
   * An elasticsearch service, accessible via a REST API at [http://localhost:9200](http://localhost:9200).

## Folder Layout

* **Vagrantfile**: Vagrant file used to setup a development environment VM.
* **hosts.dev**: Ansible inventory file specifying development environment machines.
* **hosts.staging**: Ansible inventory file specifying staging environment machines.
* **hosts.prod**: Ansible inventory file specifying production environment machines.
* **site.yml**: Master Ansible playbook.
* **elasticsearch.yml**: Ansible playbook to setup Elasticsearch service tier.
* **web.yml**: Ansible playbook to setup Nginx web tier.
* **roles/**:
   * **bennojoy.nginx/**: See https://galaxy.ansible.com/list#/roles/2
