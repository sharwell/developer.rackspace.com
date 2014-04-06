.. _quickstart:

Quickstart Guide
=================

Cloud Files
---------------

Rackspace offers a flexible and scalable solution to object storage through its Cloud Files service.

Authentication and client creation
----------------------------------
First, we need to authenticate and create an instance of the appropriate API client.

.. code-block:: node
  
  var rackspace = pkgcloud.storage.createClient({
    provider: 'rackspace',
    username: 'rackspace_id',
    apiKey: '1234567890poiiuytrrewq',
    region: 'IAD' // storage requires region or else assumes default
  });

Create Container
-----------------

Before we can upload any objects to Cloud Files, we first must create a container. In its simplest form a container can be created as follows:@Sh

.. include:: samples/create_container.rst
    

Set Container as CDN
----------------------

Once we have created a container, we can set it as CDN (Content Delivery Network). That implies that any objects within the container will be publicly readable. To set a container as CDN can be accomplished as follows:

.. include:: samples/set_container_as_cnd.rst
  

Unset Container as CDN
-----------------------

If you no longer wish to have your container set as CDN, you may do as as described in the code below:

  .. include:: samples/unset_container_as_cnd.rst


Delete Container
-----------------

In that event that you need to delete a DNS record, you can do so as shown below:

.. include:: samples/delete_record.rst


Uploading Objects to Container
-------------------------------

If you wish or need to delete a DNS zone it can be accomplished as follows:

.. include:: samples/delete_zone.rst


**WARNING: deleting a zone will also delete all the records within in. Please use with care.**

Change Object Metadata
------------------------

Once we have a zone, we to add at least one record to it in order to make it do something useful. You can create a DNS zone record as follows:

.. include:: samples/create_record.rst


Get Object via Temporary URL
-----------------------------

Once we have have created a record, we can retrieve it for inspection or manipulation as follows:

.. include:: samples/get_record.rst


Get Object Directly (i.e. download via SDK)
---------------------------------------------

If you wish to modify a DNS record, you can do so as follows:

.. include:: samples/modify_record.rst


Get Object via CDN URL
-------------------------

In that event that you need to delete a DNS record, you can do so as shown below:

.. include:: samples/delete_record.rst

Delete Objects
---------------

In that event that you need to delete a DNS record, you can do so as shown below:

.. include:: samples/delete_record.rst


