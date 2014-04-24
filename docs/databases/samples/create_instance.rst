.. code-block:: php

    $dbService = $client->databaseService();

    // Create the empty object:
    $instance = $dbService->instance();

    // Create your volume object, which in this case sets the size as 20GB:
    $volume = (object) array('size' => 20);

    // Execute the create API operation
    $instance->create(array(
        'name'   => 'development',
        'volume' => $volume,
        'flavor' => $flavor
    ));