.. code-block:: nodejs

.. code-block:: python

  # Delete an empty container
  container_deleted = pyrax.cloudfiles.delete_container("gallery")

  # Delete all the objects in the container and delete the container
  container_deleted = pyrax.cloudfiles.delete_container("gallery",
                                                        del_objects=True)
