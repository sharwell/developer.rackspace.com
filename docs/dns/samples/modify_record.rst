
.. code-block:: nodejs
  
  myRec.data = '192.168.1.1';
  rackspace.updateRecord(myZone, myRec, function (err){
    if (err) {
      console.dir(err);
      return;
    }
    console.log('Record ' + myRec.name + ' was successfully modified.');
  });

..codeblock:: ruby

  begin
    my_record.value = '192.168.1.1'
    my_record.save

    puts "Record #{my_record.name} was successfully modified."
  rescue Fog::Rackspace::Errors::ServiceError => e
    puts e.message
  end