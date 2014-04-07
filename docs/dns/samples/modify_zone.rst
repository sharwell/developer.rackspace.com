
.. code-block:: nodejs
	myZone.emailAddress = 'changed@domain.com';
  rackspace.updateZone(myZone, function (err) {
    if (err) {
      console.dir(err);
      return;
    }
    console.log('Zone was successfully modified');
  });

..codeblock:: ruby

  begin
    my_zone.email = 'changed@domain.com'
    my_zone.save

    puts 'Zone was successfully modified.'
  rescue Fog::Rackspace::Errors::ServiceError => e
    puts e.message
  end