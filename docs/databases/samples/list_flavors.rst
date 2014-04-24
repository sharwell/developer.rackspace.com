.. code-block:: java

    // List your flavors and get the first.
    FlovorApi flavorApi = troveApi.getFlavorApiForZone("{region}");
    Flavor flavor = Iterables.getFirst(flavorApi.list(), null);

.. code-block:: python

    flavors = cdb.list_flavors()