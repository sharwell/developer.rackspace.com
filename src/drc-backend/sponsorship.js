var restify = require('restify'),
  async = require('async'),
  config = require('./config.json'),
  fs = require('fs'),
  path = require('path'),
  logging = require('./logging'),
  Mailgun = require('mailgun-js'),
  pkgcloud = require('pkgcloud'),
  marked = require('marked'),
  retry = require('retry'),
  _ = require('underscore');

// Configure out logging
var log = logging.getLogger(config.logLevel || 'debug');

// configure our pkgcloud storage client
var client = pkgcloud.storage.createClient(config.pkgcloudConfig);

// wire up logging for pkgcloud
client.on('log::*', logging.logFunction);

// create our mailgun client
var mailgun = new Mailgun({apiKey: config.mailgun.apiKey, domain: config.mailgun.domain});

var markdownTemplate, plaintextTemplate;

exports.init = function(callback) {
  function readMarkdown(next) {
    fs.readFile(path.join(path.dirname(process.argv[1]), '/sponsor-email.md'), function (err, data) {
      if (err) {
        return next(err);
      }

      markdownTemplate = marked(data.toString());
      next();
    });
  }

  function readPlaintext(next) {
    fs.readFile(path.join(path.dirname(process.argv[1]), '/sponsor-email.txt'), function (err, data) {
      if (err) {
        return next(err);
      }

      plaintextTemplate = data.toString();
      next();
    });
  }

  async.parallel([ readMarkdown, readPlaintext ], function (err) {
    if (err) {
      log.error(err);
      callback(err);
      return;
    }

    callback();
  });
};

exports.save = function (req, res, next) {

  // Define our standard form values that would need to be returned over the wire
  // in case of an error
  var fields = ['event_name', 'num_attendees', 'start_date', 'end_date', 'location', 'venue',
    'event_url', 'code_of_conduct_url', 'event_twitter_handle', 'type_of_sponsorship',
    'contact_name', 'contact_email', 'charitable_event',
    'is_online_only', 'speaking_opportunity'];

  // construct our data payload to be used in saving our archive
  var data = _.pick(req.params, fields);

  data = _.extend({
    id: data.event_name + '-' + data.contact_email + '-' + new Date().toISOString()
  }, data);

  // eagerly return to caller
  // TODO Deal with timeouts from Cloud Files
  // see https://github.com/rackerlabs/developer.rackspace.com/issues/529

  res.header('Location', '/community?success=true');
  res.send(302);

  /** this is where the magic happens
   *
   * first, upload the prospectus (if we have one) and the data to cloud files
   *
   * after the response, we send two notification emails, one to the requestor,
   *   and one to rackspace DRG team
   */
  async.series([ uploadProspectus, uploadData ], function (err) {
    if (err) {
      log.error('Error uploading to cloud files', err);
      log.error('Error during save', data);

      // Update json data to reference that it failed to archive for the notification email
      data.failedToArchive = true;
    }

    log.info('success...');

    async.parallel([ sendNotificationEmail, sendResponseEmail], function (err) {
      if (err) {
        log.error('Unable to send notifications', err);
      }

      // clean up our prospectus, if any
      fs.unlink(req.files.prospectus.path);
    });
  });

  function uploadProspectus(callback) {
    // Skip if we have no prospectus
    if (!req.files || !req.files.prospectus || !req.files.prospectus.name) {
      callback();
      return;
    }

    data.prospectus = req.files.prospectus.name;
    data.prospectus_path = data.id + '-file';

    log.info('Uploading Prospectus...');

    client.upload({
      container: config.containerName,
      remote: data.prospectus_path,
      local: req.files.prospectus.path
    }, callback);
  }

  function uploadData(callback) {
    log.info('Uploading data...');

    var stream = client.upload({
      container: config.containerName,
      remote: data.id,
      headers: {
        'content-type': 'application/json'
      }
    }, callback);

    // Serialize our data payload to json over the wire
    stream.write(JSON.stringify(data, null, '  '));
    stream.end();
  }

  function sendNotificationEmail(callback) {
    var emailData = {
      from: config.fromAddress,
      to: config.notificationAddress,
      subject: 'Sponsorship Request: ' + data.event_name,
      text: JSON.stringify(data, null, '  ')
    };

    if (req.files && req.files.prospectus && req.files.prospectus.name) {
      var file = fs.readFileSync(req.files.prospectus.path);

      emailData.attachment = new Mailgun.Attachment(file, req.files.prospectus.name);
    }

    faultTolerantSendEmail(emailData, callback);
  }

  function sendResponseEmail(callback) {
    var emailData = {
      from: config.fromAddress,
      to: data.contact_email,
      subject: 'Your Rackspace Sponsorship Request: ' + data.event_name,
      text: plaintextTemplate.replace('%%%NAME%%%', data.contact_name),
      html: markdownTemplate.replace('%%%NAME%%%', data.contact_name)
    };

    faultTolerantSendEmail(emailData, callback);
  }
};

function faultTolerantSendEmail(data, callback) {
  var operation = retry.operation({
    retries: 10,
    factor: 3,
    minTimeout: 1 * 1000,
    maxTimeout: 60 * 1000,
    randomize: true
  });

  operation.attempt(function (currentAttempt) {

    log.info('Starting Message', {
      to: data.to,
      subject: data.subject
    });

    mailgun.messages().send(data, function (err) {
      if (operation.retry(err)) {
        log.warn('Retrying Message', {
          to: data.to,
          subject: data.subject
        });
        return;
      }

      log.info('Finished Message', {
        to: data.to,
        subject: data.subject,
        success: err ? false : true
      });

      callback(err ? operation.mainError() : null);
    });
  });
}
