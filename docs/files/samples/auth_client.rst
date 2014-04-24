.. code-block:: javascript

.. code-block:: python

  import pyrax
  pyrax.set_setting("identity_type", "rackspace")
  pyrax.set_default_region("ORD")
  pyrax.set_credentials(os.environ["OS_USERNAME"], os.environ["OS_PASSWORD"])

.. code-block:: ruby

  require 'fog'

  # ":rackspace_region" determines which geographic region your data will be
  # stored in. Valid choices include :dfw (Dallas), :ord (Chicago), :iad
  # (Virginia), :syd (Sydney), and :hkg (Hong Kong).

  @client = Fog::Storage.new(
    :provider => 'rackspace',
    :rackspace_username => '{username}',
    :rackspace_api_key => '{apikey}',
    :rackspace_region => :'{region}'
  )

  # If you're a UK customer, specify a :rackspace_auth_url as well, and use the
  # :lon (London) region.

  @client = Fog::Storage.new(
    :provider => 'rackspace',
    :rackspace_username => '{username}',
    :rackspace_api_key => '{apikey}',
    :rackspace_region => :lon,
    :rackspace_auth_url => Fog::Rackspace::UK_AUTH_ENDPOINT
  )
