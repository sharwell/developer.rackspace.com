
.. code-block:: nodejs

  rackspace.getRecord(myZone, 'myRecord-id', function (err, rec) {
    if (err) {
      console.dir(err);
      return;
    }
    console.log('Record ' + rec.name + ' was successfully retrieved.');
  });

..codeblock:: ruby

  begin
    my_record = my_zone.records.get RECORD_ID

    puts "Record #{my_record.name} was successfully retrieved."
  rescue Fog::Rackspace::Errors::ServiceError => e
    puts e.message
  end