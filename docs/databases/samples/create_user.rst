.. code-block:: csharp


.. code-block:: java


.. code-block:: javascript


.. code-block:: php

    $user = $instance->user();

    // Create user and assign them to some DBs
    $user->create(array(
        'name'      => 'jane_doe',
        'password'  => '6hUH!$Hu-77Ca=reVacH',
        'databases' => array('wordpress', 'other_db')
    ));

.. code-block:: python

    # Create a user by specifying the username and password, and give them
    # access to two databases.
    inst.create_user("username", "password", ["db1, db3"])

.. code-block:: ruby

.. code-block:: java

    // Use the instance API to get the user API for a specific instance by providing the instance ID.
    UserApi userApi = troveApi.getUserApiForZoneAndInstance("{region}", myInstanceId);

    // Create the user by providing the user name, 
    // user password, and the database the user is allowed to access.
    boolean result = userApi.create("myusername", "mypasswordwhichcouldbebetter", "myDatabase");
