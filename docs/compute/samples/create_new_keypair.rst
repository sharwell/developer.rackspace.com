.. code-block:: csharp

.. code-block:: java

.. code-block:: javascript

    client.addKey({ name: 'my_keypair' }, function (err, key) {
      if (err) {
        // TODO handle as appropriate
        return;
      }

      // TODO use your key
    });

.. code-block:: php

    // Get the API to generate a new keypair
    $keypair = $service->keypair();
    $keypair->create(array(
       'name' => 'my_keypair'
    ));

    // Save to local filesystem and set appropriate permissions
    $localPath = '~/.ssh/my_server';
    file_put_contents($localPath, $keypair->getPrivateKey());
    chmod($localPath, 0600);

.. code-block:: python

.. code-block:: ruby

