.. code-block:: csharp

.. code-block:: java

.. code-block:: javascript

    // we need the fs module to access the local disk
    var fs = require('fs');

    // TODO provide the path to your key
    var keyPath = '/home/my-user/.ssh/id_rsa.pub';

    fs.readFile(keyPath, function(err, data) {
      client.addKey({
        name: 'my_keypair',
        'public_key': data.toString()
      }, function (err, key) {
        if (err) {
          // TODO handle as appropriate
          return;
        }

      // TODO use your key
      });
    });

.. code-block:: php

    $keypair = $service->keypair();

    $keypair->create(array(
       'name'      => 'my_keypair',
       'publicKey' => file_get_contents('~/.ssh/my_server.pub')
    ));

.. code-block:: python

.. code-block:: ruby
