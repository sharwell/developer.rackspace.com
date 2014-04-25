.. code-block:: csharp

.. code-block:: java

    ServerApi serverApi = novaApi.getServerApiForZone("{region}");
    serverApi.delete("{serverId}");

.. code-block:: javascript

    client.destroyServer(server, function(err) {
      // TODO handle err as appropriate
    });

.. code-block:: php

    $server->delete();

.. code-block:: python

.. code-block:: ruby

    server.destroy
