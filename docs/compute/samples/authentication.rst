.. code-block:: csharp

.. code-block:: java

.. code-block:: javascript

    pkgcloud = require('pkgcloud');

    // each client is bound to a specific service and provider
    var client = pkgcloud.compute.createClient({
      provider: 'rackspace',
      username: '{username}',
      apiKey: '{apiKey}',
      region: '{region}'
    });

.. code-block:: php

    require 'vendor/autoload.php';

    use OpenCloud\Rackspace;

    $client = new Rackspace(Rackspace::US_IDENTITY_ENDPOINT, array(
        'username' => '{username}',
        'apiKey'   => '{apiKey}'
    ));

.. code-block:: python

.. code-block:: ruby
