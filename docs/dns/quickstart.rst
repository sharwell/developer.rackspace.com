.. _quickstart:

Quickstart Guide
=================

Cloud DNS
---------------

Rackspace offers a flexible and scalable solution to DNS management through its CloudDNS offering.

Authentication and client creation
----------------------------------
Before we can use the API, we need to authenticate and create and isntance of API client.

.. code-block:: node
  
  pkgcloud = require('pkgcloud');
  var rackspace = pkgcloud.dns.createClient({
    provider: 'rackspace',
    username: 'rax-user-id',
    apiKey: '1234567890asdbchehe'
  });

Create Zone
-----------------

The first step to manage your domains and subdomains is to create a DNS zone, which you can think of as being the "root" level. So, say, you have a domain called domain.com, you want to create a zone called domain.com as follows:

.. code-block:: node
    
  var details = {
  name: 'domain.com',
  email: 'admin@domain.com',
  ttl: 300,
  comment: 'root level for domain.com'
  };

  rackspace.createZone(details, function (err, zone) {
    if (err) {
      console.dir(err);
      return;
    }
    console.log(zone.id + ' ' + zone.name + ' ' + zone.ttl);
  });

Get Zone
------------------

Once we have created a zone, we can retrieve it and inspect its details as follows:

.. code-block::  node
  
  rackspace.getZones({ name: 'domain.com' }, function (err, zones) {
      if (err) {
        console.dir(err);
        return;
      }
      console.log('Zone ID: ' + zones[0] + '\n');
      console.log('Zone admin email: ' + zones[0] + '\n');
  });

Modify Zone
-------------------

We can also modify our DNS zone if any of the details change, with the provision that values passed are valid (i.e. correct email address format, TTL > 300s, etc.); however, the zone name is immutable. Thus if you need to modify zone name, you'll need to delete the zone (explained below) and start anew.

.. code-block:: node

  myZone.emailAddress = 'changed@domain.com';
  rackspace.updateZone(myZone, function (err) {
    if (err) {
      console.dir(err);
      return;
    }
    console.log('Zone successfully modified');
  });

Delete Zone
---------------

If you wish or need to delete a DNS zone it can be accomplished as follows:

.. code-block:: node

  rackspace.deleteZone(myZone, function (err) {
    if (err) {
      console.dir(err);
      return;
    }
    console.log('Zone successfully deleted');
  });

WARNING: deleting a zone will also delete all the records within in. Please use with care.

Create Record
--------------

Once we have a zone, we to add at least one record in order to make it do something useful. You can create a DNS zone record as follows:

.. record-code:: node

  var recDetails = {
    name: 'subdomain',
    data: '127.0.0.1',
    type: 'A'
  };

  rackspace.createRecord(myZone, function (err, rec) {
    if (err) {
      console.dir(err);
      return;
    }
    console.log('Record ' + rec.name + ' was successfully created.');
  });

Get Record
-------------

Once we have have created a record, we can retrieve it for inspection or manipulation as follows:

.. code-block:: node

  rackspace.getRecord(myZone, 'myRecord-id', function (err, rec) {
      if (err) {
        console.dir(err);
        return;
      }
      console.log('Record ' + rec.name + ' was successfully retrieved.');
  });

Update Record
---------------

If you wish to modify a DNS record, you can do so as follows:

.. code-block:: node

  myRec.data = '192.168.1.1';
  rackspace.updateRecord(myZone, myRec, function (err){
      if (err) {
        console.dir(err);
        return;
      }
      console.log('Record ' + myRec.name + ' was sucessfully modified.');
  });

Delete Record
----------------

In that event that you need to delete a DNS record, you can do so as shown below:

.. code-block:: node

  rackspace.deleteRecord(myZone, myRec, function (err){
      if (err) {
        console.dir(err);
        return;
      }
      console.log('DNS Record was successfully deleted.');
  });