
.. code-block:: nodejs
  rackspace.getZones({ name: 'domain.com' }, function (err, zones) {
    if (err) {
      console.dir(err);
      return;
    }
    console.log('Zone ID: ' + zones[0] + '\n');
    console.log('Zone admin email: ' + zones[0] + '\n');
  });

..codeblock:: ruby

  begin
    my_zone = service.zones.all.find {|z| z.name == 'domain.com' }

    puts "Zone ID: #{my_zone.id}"
    puts "Zone admin email: #{my_zone.email}"
  rescue Fog::Rackspace::Errors::ServiceError => e
    puts e.message
  end