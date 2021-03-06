---
layout: post
title: "Configure Keystone to Utilize Apache 2"
date: 2014-11-06 17:20
comments: true
author: Matt Dorn
published: true
categories:
 - OpenStack
 - openstack-keystone
 - apache
 - python
bio:
 Matt Dorn is a Cloud Technology Instructor with Rackspace focused on helping IT teams around the world build private clouds with OpenStack. You can find his blog at http://www.madorn.com/
---

Keystone has the ability to leverage Apache as its front end.  This configuration is becoming more popular with OpenStack deployments lately.  Here is a straight forward guide on how to setup Keystone to utilize Apache in your existing OpenStack deployment.

<!-- more -->

Stop Keystone Service (It is not necessary to run this service with this config)

    sudo service keystone stop

Install Apache

    sudo apt-get install -y apache2

Install Python WSGI module for Apache

    sudo apt-get install -y libapache2-mod-wsgi

Make cgi-bin directory for Keystone

    sudo mkdir -p /var/www/cgi-bin/keystone/

Create Python script for Apache (admin and main)

    ( cat | sudo tee /var/www/cgi-bin/keystone/admin /var/www/cgi-bin/keystone/main ) <<EOF
    import logging
    import os

    from paste import deploy

    from keystone.openstack.common import gettextutils
    # NOTE(dstanek): gettextutils.enable_lazy() must be called before
    # gettextutils._() is called to ensure it has the desired lazy #lookup behavior. This includes cases, like keystone.exceptions, #where gettextutils._() is called at import time.
    gettextutils.enable_lazy()

    from keystone.common import dependency
    from keystone.common import environment
    from keystone.common import sql
    from keystone import config
    from keystone.openstack.common import log
    from keystone import service


    CONF = config.CONF

    config.configure()
    sql.initialize()
    config.set_default_for_default_log_levels()

    CONF(project='keystone')
    config.setup_logging()

    environment.use_stdlib()
    name = os.path.basename(__file__)

    if CONF.debug:
        CONF.log_opt_values(log.getLogger(CONF.prog), logging.DEBUG)


    drivers = service.load_backends()

    # NOTE(ldbragst): 'application' is required in this context by WSGI spec.
    # The following is a reference to Python Paste Deploy documentation
    # http://pythonpaste.org/deploy/
    application = deploy.loadapp('config:%s' % config.find_paste_config(),
                             name=name)

    dependency.resolve_future_dependencies()
    EOF

Configure Apache to listen on ports 35357(admin) and 5000(main)

    ( cat | sudo tee /etc/apache2/ports.conf ) <<EOF
    Listen 35357
    Listen 5000
    EOF

Configure Keystone Virtual Hosts

    ( cat | sudo tee /etc/apache2/sites-available/keystone-httpd.conf ) <<EOF
    WSGIDaemonProcess keystone user=keystone group=nogroup processes=3 threads=10

    <VirtualHost *:5000>
        LogLevel  info
        ErrorLog  /var/log/keystone/keystone-apache-error.log
        CustomLog /var/log/keystone/ssl_access.log combined
        Options +FollowSymLinks

    #SSLEngine on
    #SSLCertificateFile /etc/ssl/certs/mycert.pem
    #SSLCertificateKeyFile /etc/ssl/private/mycert.key
    #SSLVerifyClient optional
    #SSLVerifyDepth 10
    #SSLProtocol all -SSLv2
    #SSLCipherSuite ALL:!ADH:!EXPORT:!SSLv2:RC4+RSA:+HIGH:+MEDIUM:+LOW
    #SSLOptions +StdEnvVars +ExportCertData

        WSGIScriptAlias /  /var/www/cgi-bin/keystone/main
        WSGIProcessGroup keystone
    </VirtualHost>

    <VirtualHost *:35357>
        LogLevel  info
        ErrorLog  /var/log/keystone/keystone-apache-error.log
        CustomLog /var/log/keystone/ssl_access.log combined
        Options +FollowSymLinks

    #SSLEngine on
    #SSLCertificateFile /etc/ssl/certs/mycert.pem
    #SSLCertificateKeyFile /etc/ssl/private/mycert.key
    #SSLVerifyClient optional
    #SSLVerifyDepth 10
    #SSLProtocol all -SSLv2
    #SSLCipherSuite ALL:!ADH:!EXPORT:!SSLv2:RC4+RSA:+HIGH:+MEDIUM:+LOW
    #SSLOptions +StdEnvVars +ExportCertData

        WSGIScriptAlias / /var/www/cgi-bin/keystone/admin
        WSGIProcessGroup keystone
    </VirtualHost>
    EOF

Enable Keystone site

    sudo a2ensite keystone-httpd

Reload Apache

    sudo service apache2 reload