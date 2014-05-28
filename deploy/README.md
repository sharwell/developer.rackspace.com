# Production Setup

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

4. Make sure the `drg.pem` file is in your `~/.ssh` directory (ask @ycombinator for it). Make sure the corresponding public key has been uploaded to the "SSH Keys" section of your Rackspace Cloud Control Panel for the region(s) where you wish to setup production infrastructure. Modify file access rights:

    ```bash
$ chmod 600 ~/.ssh/drg.pem
  ```


5. Change to this directory on your development machine.

    ```bash
    $ cd /path/to/developer.rackspace.com/deploy
    ```

6. Make sure the `publisher.id_rsa` file is in the `roles/jenkins_masters/files/var/lib/jenkins/` directory (ask @ycombinator for it). This is the private key used by Jenkins jobs to publish the generated web site to the various web servers serving developer.rackspace.com.

7. There are 2 Ansible playbooks for the production environment, one each for the web site and the Jenkins setup. These three playbooks are named `prod_web.yml` and `jenkins.yml`. Each playbook sets things up in a particular region, which is to be specified via the `RAX_REGION` environment variable when running the playbook.

   a. The example below shows how to setup the production web site in DFW:

      ```bash
      $ RAX_REGION=dfw ansible-playbook prod_web.yml -i inventory/prod
      ```

   b. The example below shows how to setup a Jenkins setup in DFW:

      ```bash
      $ RAX_REGION=dfw ansible-playbook jenkins.yml -i inventory/prod
      ```

      **IMPORTANT!** The `prod_jenkins.yml` playbook MUST be run after the `prod_web.yml` playbook has run for the same `RAX_REGION`. Here's why: The `prod_jenkins.yml` playbook creates a 'Build Site' Jenkins job. This job generates the content for the developer.rackspace.com web site. The `prod_jenkins.yml` playbook will automatically lookup the private (ServiceNet) IP addresses of the developer.rackspace.com web servers in the specified region (via the `RAX_REGION` environment variable) and use them to configure the 'Build Site' job. This lets the 'Build Site' job publish the generated content to the developer.rackspace.com web servers in its region.

8. Depending on the playbook you ran, here is what you should see in your Rackspace Cloud control panel:

   a. If you successfully ran the production web site playbook, you should see the following:
      * 2 cloud servers in named `webserver_1` and `webserver_2`, and
      * 1 cloud load balancer named `developer.rackspace.com` with the 2 cloud servers from above as its nodes. Note the public IP address of this load balancer.
      * If you visit the public IP address of the load balancer in your browser (port 80), you should see the developer.rackspace.com web site.

   b. If you successfully ran the Jenkins setup playbook, you should see the following:
      * 1 Jenkins master server named `jenkins_master`,
      * If you visit the public IP address of the Jenkins master server in your browser (on port 8080), you should see the Jenkins login screen.
      * Once you log in, you should see various jobs that build and publish content to the web servers in the same region.

9. Each cloud server has nginx installed, configured and running.

10. Each cloud server has a `publisher` user, which can be used to upload static content to the site. The SSH public key for this user is already added to the user's `authorized_keys` file. The SSH private key is... well, private (ask @ycombinator for it). Static content should be uploaded over SSH (via scp/sftp/rsync) to `/var/www/html/developer.rackspace.com`.

11. The web site can be accessed via the public IP address of the cloud load balancer, as noted in step 6, over HTTP on port 80.

## Folder Layout

* **Vagrantfile**: Vagrant file used to setup a development environment VM.
* **inventory**:
  * **dev**:
   * **hosts**: Ansible inventory file for development environment.
  * **prod**:
   * **hosts**: Ansible inventory file for production environment.
   * **rax.py**: Ansible dynamic inventory file (since we are working with the Rackspace cloud). See http://docs.ansible.com/guide_rax.html#host-inventory.
* **prod_web.yml**: Ansible playbook to setup and configure developer.rackspace.com infrastructure in a given region.
* **prod_jenkins.yml**: Ansible playbook to setup and configure Jenkins infrastructure in a given region.
* **roles/**: Various Ansible roles referenced in the playbooks.
