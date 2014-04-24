.. code-block:: csharp

.. code-block:: java

.. code-block:: javascript

  rackspace.deleteZone(myZone, function (err) {
    if (err) {
      console.dir(err);
      return;
    }
    console.log('Zone successfully deleted.');
  });

.. code-block:: php

	$domain->delete();

.. code-block:: python

.. code-block:: ruby

  zone.destroy
