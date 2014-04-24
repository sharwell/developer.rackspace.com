.. code-block:: csharp

.. code-block:: java

.. code-block:: javascript

.. code-block:: php

    // To retrieve all flavors:
    $flavors = $service->flavorList();

    // or to retrieve a subset of flavors, you can specify the
    // minimum disk storage (in GB) or minimum RAM (in MB)
    $flavors = $service->flavorList(false, array(
        'minDisk' => 100,
        'minRam'  => 16 * 1024
    ));

    // Now choose the first flavor in the collection
    foreach ($flavors as $flavor) {
        $flavorId = $flavor->id;
        break;
    }

.. code-block:: python

.. code-block:: ruby
