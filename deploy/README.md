# Production Setup

0. Initialize and update git submodules

  ```bash
  git submodule update --init
  ```

1. Download and install [Ansible](http://docs.ansible.com/intro_installation.html#installing-the-control-machine).
   * On Mac OSX machines with [Homebrew](http://brew.sh/) installed, you can simply run: `$ brew install ansible`


2. Install pyrax.

   ```bash
   $ sudo pip install pyrax
   ```

3. Create a file named `~/.rackspace_cloud_credentials` with the following contents:

    ```
    [rackspace_cloud]
    username = <REPLACE WITH YOUR RACKSPACE CLOUD USERNAME>
    api_key = <REPLACE WITH YOUR RACKSPACE CLOUD API KEY>
    ```

4. Make sure the `drg.pem` file is in your `~/.ssh` directory (ask @ycombinator for it). The corresponding public key needs to be uploaded as the `drg` public key in the "SSH Keys" section of your Rackspace Cloud Control Panel for the region(s) where you wish to setup production infrastructure. Modify file permissions:

    ```bash
    $ chmod 600 ~/.ssh/drg.pem
    ```

5. Change to this directory on your development machine.

    ```bash
    $ cd /path/to/developer.rackspace.com/deploy
    ```

6. Make sure the `publisher.id_rsa` file is in the `roles/jenkins_masters/files/var/lib/jenkins/` directory (ask @ycombinator for it). This is the private key used by Jenkins jobs to publish the generated web site to the various web servers serving developer.rackspace.com. Modify file permissions:

    ```bash
    $ chmod 600 roles/jenkins_masters/files/var/lib/jenkins/publisher.id_rsa
    ```

7. There are 3 Ansible playbooks for setting up a region, and they must be run in order:

   a. Setting up the production web servers and load balancer:

      ```bash
      $ RAX_REGION=dfw ansible-playbook prod_web.yml -i inventory/prod
      ```

   b. Setting up the staging web servers and load balancer:

      ```bash
      $ RAX_REGION=dfw ansible-playbook staging_web.yml -i inventory/staging
      ```

   c. Setting up Jenkins:

      ```bash
      $ RAX_REGION=dfw ansible-playbook jenkins.yml -i inventory/jenkins
      ```

9. Each cloud server has nginx installed, configured and running.

10. Each cloud server has a `publisher` user. The Jenkins jobs `build_prod_site` and `build_staging_site` use this key to publish content to the production and staging web sites respectively. Content is published over rsync + ssh.


## Folder Layout

* **Vagrantfile**: Vagrant file used to setup a development environment VM.
* **inventory**:
  * **dev**:
   * **hosts**: Ansible inventory file for development environment.
  * **prod**:
   * **hosts**: Ansible inventory file for production environment.
   * **rax.py**: Ansible dynamic inventory file (since we are working with the Rackspace cloud). See http://docs.ansible.com/guide_rax.html#host-inventory.
* **prod_web.yml**: Ansible playbook to setup and configure developer.rackspace.com infrastructure in a given region.
* **jenkins.yml**: Ansible playbook to setup and configure Jenkins infrastructure in a given region.
* **roles/**: Various Ansible roles referenced in the playbooks.
