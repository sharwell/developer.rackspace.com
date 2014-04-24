.. code-block:: javascript

.. code-block:: python

  container = pyrax.cloudfiles.create_container("gallery")
  obj = container.store_object("thumbnail", data)

.. code-block:: ruby

  # :body can also be an open IO object like a File, to stream up

  file = directory.files.create(
    :key => 'somefile.txt',
    :body => 'Rackspace is awesome!'
  )
