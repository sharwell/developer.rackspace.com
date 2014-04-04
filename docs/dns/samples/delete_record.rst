  
..codeblock:: nodejs
 
  rackspace.deleteRecord(myZone, myRec, function (err){
    if (err) {
      console.dir(err);
      return;
    }
    console.log('DNS Record was successfully deleted.');
  });

..codeblock:: ruby

 begin
   my_record.destroy

   puts 'DNS Record was successfully deleted.'
 rescue Fog::Rackspace::Errors::ServiceError => e
   puts e.message
 end
