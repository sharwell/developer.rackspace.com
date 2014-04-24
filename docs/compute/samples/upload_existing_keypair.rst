.. code-block:: csharp

.. code-block:: java

.. code-block:: javascript

.. code-block:: php

    $keypair = $service->keypair();

    $keypair->create(array(
       'name'      => 'my_keypair',
       'publicKey' => file_get_contents('~/.ssh/my_server.pub')
    ));

.. code-block:: python

.. code-block:: ruby
