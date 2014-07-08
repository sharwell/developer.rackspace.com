var fs = require('fs'),
	path = require('path'),
	pkgcloud = require('pkgcloud'),
	async = require('async'),
	colors = require('colors'),
	config = require(process.argv[2]);

var client = pkgcloud.providers.rackspace.storage.createClient(config.pkgcloud);

var files = [];

function handleFile(path) {
	if (fs.lstatSync(path).isDirectory()) {
		fs.readdirSync(path).forEach(function(item) {
			handleFile(path + '/' + item);
		});
	}
	else {
		files.push(path);
	}
}
handleFile(config.srcDirectory);

async.forEachLimit(files, 2, function(file, next) {
	var remoteName = path.dirname(file.substring(1)) + '/' + path.basename(file.substring(1));

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

	stream.pipe(dest);
}, function(err) {
	if (err) {
		console.error(err);
		process.exit(1);
	}
});
