.. code-block:: php

    $user = $instance->user();

    // Create user and assign them to some DBs
    $user->create(array(
        'name'      => 'jane_doe',
        'password'  => '6hUH!$Hu-77Ca=reVacH',
        'databases' => array('wordpress', 'other_db')
    ));