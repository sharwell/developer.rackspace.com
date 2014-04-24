.. code-block:: javascript

.. code-block:: php

  // Get the object content (data) as a stream.
  $objectContent = $object->getContent();

  // Write object content to file on local filesystem.
  $objectContent->rewind();
  $stream = $objectContent->getStream();
  $localFilename = tempnam("/tmp", 'php-opencloud-');
  file_put_contents($localFilename, $stream);

.. code-block:: python

  # Get the data as a string
  data = obj.get()

  # Download the object locally to a file
  obj.download("/tmp")

.. code-block:: ruby

  file.body
