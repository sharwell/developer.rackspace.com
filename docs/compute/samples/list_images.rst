.. code-block:: csharp

.. code-block:: java

.. code-block:: javascript

    client.getImages(function(err, images) {
      if (err) {
        // TODO handle as appropriate
        return;
      }

      // TODO figure out which image to use
      // just grab the first image id
      var imageId = images[0].id;
    });

.. code-block:: php

    // To retrieve all images:
    $images = $service->imageList();

    // Choose the first image in the collection
    foreach ($images as $image) {
        $imageId = $image->id;
        break;
    }

.. code-block:: python

.. code-block:: ruby
