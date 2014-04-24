.. code-block:: nodejs


.. code-block:: python

  import pyrax
  pyrax.set_setting("identity_type", "rackspace")
  pyrax.set_default_region("ORD")
  pyrax.set_credentials(os.environ["OS_USERNAME"], os.environ["OS_PASSWORD"])
