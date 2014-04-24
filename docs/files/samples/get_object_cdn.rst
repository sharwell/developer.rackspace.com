.. code-block:: javascript

.. code-block:: python

  import urllib
  import urlparse
  encoded_name = urllib.quote(obj.name)
  cdn_url = urlparse.urljoin(container.cdn_uri, encoded_name)

.. code-block:: ruby

  require 'open-uri'

  open(file.public_url) { |f| f.read }
