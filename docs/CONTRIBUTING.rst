Documentation Coventions
========================

Code blocks go in this order::

  .. code-block:: csharp

  .. code-block:: java

  .. code-block:: javascript

  .. code-block:: php

  .. code-block:: python

  .. code-block:: ruby

If starting a new narrative/code section, be sure to add all the code block sections to each `.rst` you create for PR sanity.

Use neutral language instead of gerunds:

* **GOOD**: Set up your xxxx.
* **BAD**: Setting up your xxxx.

Use comments in code samples when each sample comprises multiple steps.

Use TODO in code samples instead of printing out strings.

Limit lines to 120 characters.

When using a value the developer needs to input, surround the value in curly brackets. e.g. ``{username}``, ``{apiKey}``, ``{region}``. The convention is to name them as lowercased with camelCasing.
