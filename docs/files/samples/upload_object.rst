.. code-block:: nodejs

.. code-block:: python

  container = pyrax.cloudfiles.create_container("gallery")
  object = container.store_object("thumbnail", data)
