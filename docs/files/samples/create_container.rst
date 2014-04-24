.. code-block:: javascript

  rackspace.createContainer({
    name: 'sample-container-test',
    metadata: {
      callme: 'maybe'
    }
  }, function (err, container) {
    if (err) {
      console.dir(err);
      return;
    }

    console.log(container.name);
    console.log(container.metadata);
  });

.. code-block:: python

  container = pyrax.cloudfiles.create_container("gallery")

.. code-block:: ruby

  # Fog calls containers "directories."

  directory = @client.directories.create(:key => 'sample-container-test')
