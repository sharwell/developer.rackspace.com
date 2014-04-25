.. code-block:: csharp

.. code-block:: java

    ImageApi imageApi = novaApi.getImageApiForZone("{region}");
    Image image = imageApi.get("{imageId}");

.. code-block:: javascript

    client.getImage(imageId, function(err, image) {
      if (err) {
        // TODO handle as appropriate
        return;
      }

      // TODO use your image here
    });

.. code-block:: php

    $imageId = '{imageId}';
    $image = $service->image($imageId);

.. code-block:: python

.. code-block:: ruby

    image = @client.images.get('{imageId}')
