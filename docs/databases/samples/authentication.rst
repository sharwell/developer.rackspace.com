.. code-block:: javascript

  pkgcloud = require('pkgcloud');
  var rackspace = pkgcloud.dns.createClient({
    provider: 'rackspace',
    username: '{username}',
    apiKey: '{apiKey}'
  });

.. code-block:: php

    require 'vendor/autoload.php';

    use OpenCloud\Rackspace;

    $client = new Rackspace(Rackspace::US_IDENTITY_ENDPOINT, array(
        'username' => '{username}',
        'apiKey'   => '{apiKey}'
    ));

.. code-block:: ruby

  require 'fog'

  service = Fog::DNS.new :provider => :rackspace,
    rackspace_username => RACKSPACE_USERNAME,
    rackspace_api_key => RACKSPACE_API_KEY,
    rackspace_region => RACKSPACE_REGION

.. code-block:: python

  import pyrax
  pyrax.set_default_region("ORD")
  pyrax.set_credentials(os.environ["OS_USERNAME"], os.environ["OS_PASSWORD"])
  cdb = pyrax.cloud_databases
