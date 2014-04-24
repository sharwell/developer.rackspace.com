.. code-block:: javascript

  client.createContainer({
    name: 'sample-container-test'
  }, function (err, container) {
    if (err) {
      // TODO handle as appropriate
      return;
    }

    // TODO use your container
  });

.. code-block:: python

  container = pyrax.cloudfiles.create_container("gallery")

.. code-block:: ruby

  # Fog calls containers "directories."

  directory = @client.directories.create(:key => 'sample-container-test')
