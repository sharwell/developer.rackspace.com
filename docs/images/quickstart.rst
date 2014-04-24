==================================
Getting Started with Cloud Images
==================================

This guide helps you get started with the Rackspace Cloud Images service.
In a nutshell, this service offers retrieval, storage, and metadata
assignment for Rackspace Cloud Server images through a simple Representational 
State Transfer (REST) web service interface.

Concepts
========

Before you jump in, it helps to understand these core Images service concepts:

* Server
* Flavor
* Image
* Network

Server
------

A virtual machine, hosted on a physical device in one of our top-notch data centers. Your application, web server, and file system runs on a server.

Image
-----

A collection of files for a specific operating system (OS) 
that you use to create or rebuild a server. Rackspace offers pre-built Linux and Windows images: Ubuntu 12.04, Red Hat 6, Windows, and so on. 
You can also create custom images, or snapshots, from servers that you have launched. 
You can use custom images for data backups or as *gold* images to launch additional servers. 

Image entity
------------

An image entity is represented by a JSON-encoded data structure and its raw binary data.

An image entity has an identifier (ID) that is guaranteed to be unique within its endpoint. The ID is used as a token in request URIs to interact with that specific image.

An image is always guaranteed to have the following attributes: id, status, visibility, protected, tags, created_at, file and self. The other attributes defined in the image schema below are guaranteed to be defined, but will only be returned with an image entity if they have been explicitly set.

A client may set arbitrarily-named attributes on their images if the image json-schema allows it. These user-defined attributes will appear like any other image attributes.

Image identifiers
-----------------

Images are uniquely identified by a URI that matches this signature:

''{image server location}/v2/images/{image_ID}''

Where:

* ''{image server location}'' is the resource location of the Cloud Images service that knows about an image.
* ''{image_ID}'' is the image identifier, which is a UUID, making it globally unique.
    
Flavor
------

A type of hardware configuration that describes the parameters of the various virtual machine images that are available to users.
Includes parameters such as CPU, storage, and memory. Alternative term for *instance type*.
Rackspace offers a set of standard hardware configurations for you to choose from. 
You might want a 2 GB RAM flavor for a smaller project, or a 32 GB RAM performance 
flavor for heavy computational needs.

Network
-------

The virtual space where your servers live. 
Rackspace provides these default networks: 

* PublicNet. The Internet. Enables public Internet connectivity.
* ServiceNet. The Rackspace internal network. Enables internal connectivity with your other servers.

Although you can create as many isolated networks as you like, servers are connected by default to PublicNet and ServiceNet.



Common image properties
=======================

To help end users use your images, you can put additional common properties, or metadata, on your images.

The available properties and their expected values include:

**os_distro**

    The common name of the operating system distribution
    [Important]	Important

    The common name must be all lowercase and entered exactly as shown here.

    The allowed values are:

    **arch**

        Arch Linux
    **centos**

        Community Enterprise Operating System
    **debian**

        Debian
    **fedora**

        Fedora
    **freebsd**

        FreeBSD
    **gentoo**

        Gentoo Linux
    **mandrake**

        Mandrakelinux (MandrakeSoft)
    **mandriva**

        Mandriva Linux
    **mes**

        Mandriva Enterprise Server
    **msdos**

        Microsoft Disk Operating System
    **netbsd**

        NetBSD
    **netware**

        Novell NetWare
    **openbsd**

        OpenBSD
    **opensolaris**

        OpenSolaris
    **opensuse**

        openSUSE
    **rhel**

        Red Hat Enterprise Linux
    **sled**

        SUSE Linux Enterprise Desktop
    **ubuntu**

        Ubuntu
    **windows**

        Microsoft Windows

**os_version**

    The operating system version as specified by the distributor

Authenticating
==============

Okay, we've covered the concepts, so now we need to deal with authentication. Authentication is a required step for all API interactions.

In order to authenticate, you need a username and API key which you can find in the control panel by visiting the "Account Settings" page.

Once you've retrieved your details, you pass them into the client:

.. include:: samples/authentication.rst

Setting up your first server
============================

Once you've finished your initial setup, you can begin the process of creating your first server.

In order to do this, you need to decide which **Image** and **Flavor** you want to use.

Task 1: Finding an Operating System
-----------------------------------

An Image, or operating system, will form the basis of your server. Each one has a unique ID, which is used to retrieve more details from the API. If you already know the ID, you can retrieve more details about the image like this:

.. include:: samples/get_image.rst

Alternatively, you can traverse through the list:

.. include:: samples/list_images.rst

Once you've found the perfect operating system, and its ID, you can move on to picking your hardware.

Task 2: Finding the right hardware
----------------------------------

Flavors, or hardware configurations, will dictate how powerful your servers are. Like images, each flavor has its own UUID. If you already know which flavor to use, you can retrieve its details like this:

.. include:: samples/get_flavor.rst

Alternatively, you can traverse through the standard list Rackspace provides:

.. include:: samples/list_flavors.rst

Task 3: Putting the pieces together
-----------------------------------

Now that we have our image ID and flavor ID, you can create your server:

.. include:: samples/create_server.rst

This is an asyncronous operation, meaning that it will _not_ block your request until the process is complete. It will provision your VM behind the scenes, allowing you to optionally query its status. Once the build reaches a ``COMPLETE`` state, it will be available for you to use.

Some SDKs allow you to check on the status of the build:

.. include:: samples/query_server_build.rst

Managing your existing servers
==============================

After you create your server, you use various operations to control its behaviour or state.

Upgrade the RAM
---------------

In order to upgrade the hardware of your server, you need to find a more powerful flavor. Once you know your new flavor's ID, you can resize your server like so:

.. include:: samples/resize_server.rst

Deleting your server
--------------------

If you've finished working with your server, you can permanently delete it like so:

.. include:: samples/delete_server.rst

Managing key pairs
==================

By default, servers will use password-based authentication. When a server is created, the HTTP response will contain a root password that is required for all subsequent SSH connections. You do have the option, however, of using keypairs instead.

Registering your keypair
------------------------

In order to use keypair-based authentication, the API needs to know about it. You have two options: upload your existing key, or have the API create a new one for you. We'll cover both.

In order to upload an existing keypair:

.. include:: samples/upload_existing_keypair.rst

In order to have the API create on for you:

.. include:: samples/create_new_keypair.rst

Using keypairs
--------------

If you want an existing server to use keypair-based auth, you will need to configure this yourself.

However, getting new servers to acknowledge keypairs is easy. You just need to supply the name of the pre-existing keypair when you do the create server operation, like this:

.. include:: samples/create_server_with_keypair.rst

This server, after being spun up, will respond to that keypair.
