var restify = require('restify'),
  async = require('async'),
  config = require('./config.json'),
  logging = require('./logging'),
  os = require('os'),
  sponsorship = require('./sponsorship'),
  comingSoon = require('./coming-soon');

// create our API Server
var server = restify.createServer();

// configure for uploading files
server.use(restify.bodyParser({
  mapParams: true,
  mapFiles: true,
  uploadDir: os.tmpdir()
}));

// Configure out logging
var log = logging.getLogger(config.logLevel || 'debug');

server.post('/api/sponsorship', sponsorship.save);
server.post('/api/developer-plus/coming-soon', comingSoon.save);

async.parallel([ sponsorship.init, comingSoon.init ], function(err) {
  if (err) {
    log.error(err);
    process.exit(1);
    return;
  }

  server.listen(8111, function () {
    log.info('Server listening at ' + server.url);
  });
});

