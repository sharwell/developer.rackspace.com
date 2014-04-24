.. code-block:: csharp

.. code-block:: java

.. code-block:: javascript

.. code-block:: php

.. code-block:: python

.. code-block:: ruby

  # The :size parameter is specified in GB, with a minimum of 100GB.

  volume = @client.volumes.create(:size => '100')
  volume.wait_for { ready? }
