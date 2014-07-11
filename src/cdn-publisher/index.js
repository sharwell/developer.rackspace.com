var fs = require('fs'),
	path = require('path'),
	pkgcloud = require('pkgcloud'),
	async = require('async'),
	colors = require('colors'),
  walk = require('walk'),
	config = require(process.argv[2]);

var client = pkgcloud.providers.rackspace.storage.createClient(config.pkgcloud);

var walker = walk.walk(config.srcDirectory, {
  followLinks: false
});

var files = [];

walker.on('file', function(root, fileStats, next) {
  files.push(root + '/' + fileStats.name);
  next();
});

walker.on('end', function() {
  uploadFiles();
});

function uploadFiles() {
  async.forEachLimit(files, 2, function (file, next) {
    var localPath = path.relative(config.srcDirectory, file);
    var remoteName = path.dirname(localPath) + '/' + path.basename(file.substring(1));

    // first remove any path artifacts of the local directory
    if (remoteName.indexOf('.') === 0) {
      remoteName = remoteName.substring(1);
    }

    // then ensure the key includes a preceding slash
    if (remoteName.substring(0,1) !== '/') {
      remoteName = '/' + remoteName;
    }

    console.log(
        'Uploading File ['.grey + file.green + '] to ['.grey + remoteName.yellow + ']'.grey
    );

    var stream = fs.createReadStream(file);

    var options = {
      container: config.destContainer,
      remote: remoteName,
      headers: {
        'Access-Control-Allow-Origin': '*'
      }
    };

    var dest = client.upload(options, next);

    stream.pipe(dest);

    next();

  }, function (err) {
    if (err) {
      console.error(err);
      process.exit(1);
    }
  });
}