.. code-block:: javascript

.. code-block:: python

  obj.change_content_type("application/json")
  obj.set_metadata({"some-key": "some-value"})

.. code-block:: ruby

  file.content_type = 'application/json'
  file.save

  # Generic metadata can be set with:
  file.metadata['some-key'] = 'some-value'
  file.save
