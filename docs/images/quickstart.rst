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
* Network
* Image

Server
------

A virtual machine, hosted on a physical device in one of our top-notch data centers. Your application, web server, and file system runs on a server.

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

Image
-----

A collection of files for a specific operating system (OS) 
that you use to create or rebuild a server. Rackspace offers pre-built Linux and Windows images: Ubuntu 14.04, Red Hat 6, Windows, and so on. 
You can also create custom images, or snapshots, from servers that you have launched. 
You can use custom images for data backups or as *gold* images to launch additional servers. 

Image entity
------------

An image entity is represented by a JSON-encoded data structure and its raw binary data.

An image entity has an identifier (ID) that is guaranteed to be unique within its endpoint. The ID is used as a token in request URIs to interact with that specific image.

An image is always guaranteed to have the following attributes: 
* id
* status
* visibility
* protected
* tags
* created_at
* file 
* self 

The other attributes defined in the image schema are guaranteed, but are only returned with an image entity if you set them explicitly.

A client can set arbitrarily-named attributes on their images if the image json-schema allows it. 
These user-defined attributes appear like any other image attributes.

Image identifiers
-----------------

Images are uniquely identified by a URI that matches this signature:

''{image server location}/v2/images/{image_ID}''

Where:

* ''{image server location}'' is the resource location of the Cloud Images service that knows about an image.
* ''{image_ID}'' is the image identifier, which is a UUID, making it globally unique.

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

Authenticate
============

You must authenticate before you can complete any Rackspace API interaction.

To authenticate, you need a user name and API key. Find your API key in the control panel on the "Account Settings" page.

Once you've retrieved your details, you pass them into the client:

.. include:: samples/authentication.rst

Use images
==========

To see which images are available, you can list all images or get details for a specified image.
Then, you can update an image and use image tasks to import and export images.

Task 1: List images and get image details
-----------------------------------------

An image, or operating system, forms the basis of your server. 
Each image has a unique ID, which you can use to get more details about the image.

To list images:

.. include:: samples/list_images.rst

Once you know the image ID, you can get more details about the image like this:

.. include:: samples/get_image.rst

Once you've found the perfect operating system, and its ID, you can move on to picking your hardware.

Task 2: Update an image
-----------------------

.. include:: samples/update_image.rst

Task 3: Import and export images
--------------------------------

An image task is a request to perform an asynchronous image-related operation, such as importing or exporting an image. The request results in the creation of a disposable task resource that can be polled for information about the status of the operation.

After you initiate an image import or export, poll the status of the created task by using the instructions in Section 2.1.6, “Get details for a task”. When the task resource reaches a final status of success or failure, the poll response includes an expiration date and time stamp. After that expiration date and time, the disposable task resource itself expires and is subject to deletion. 
However, the result of the task, such as an imported or exported image, does not expire.

.. include:: samples/import_image.rst

.. include:: samples/export_image.rst

Share images
============

.. include:: samples/share_images.rst

