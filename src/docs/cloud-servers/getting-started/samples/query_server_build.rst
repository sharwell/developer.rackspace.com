.. code-block:: csharp

.. code-block:: java

  ServerPredicates.awaitActive(serverApi).apply(serverCreated.getId())

.. code-block:: javascript

.. code-block:: php

  use OpenCloud\Compute\Constants\ServerState;

  $callback = function ($server) {
    if (isset($server->error)) {
      var_dump($server->error); die;
    } else {
      $progress = isset($server->progress) ? $server->progress : 0;
      printf("Waiting on %s [%s]: %4s%%", $server->name(), $server->status(), $progress);  
    }
  };

  $server->waitFor(ServerState::ACTIVE, 600, $callback);

.. code-block:: python

  pyrax.utils.wait_for_build(server, verbose=True)

.. code-block:: ruby

  server.wait_for { active }

.. code-block:: sh

  # from resulting json below see "status"
  curl -X GET $ENDPOINT/{serverId} \
    -H "X-Auth-Token: $TOKEN" | python -m json.tool
