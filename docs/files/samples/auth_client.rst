.. code-block:: javascript

.. code-block:: python

  import pyrax
  pyrax.set_setting("identity_type", "rackspace")
  pyrax.set_default_region("ORD")
  pyrax.set_credentials(os.environ["OS_USERNAME"], os.environ["OS_PASSWORD"])

.. code-block:: ruby

  require 'fog'

  # ":rackspace_region" determines which region your data will be stored in.
  # Valid choices include :dfw (Dallas), :ord (Chicago), :iad (Virginia),
  # :syd (Sydney), :hkg (Hong Kong), and :lon (London, only for UK accounts).

  @client = Fog::Storage.new(
    :provider => 'rackspace',
    :rackspace_username => '{username}',
    :rackspace_api_key => '{apikey}',
    :rackspace_region => :ord
  )
