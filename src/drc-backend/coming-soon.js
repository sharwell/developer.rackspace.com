var MongoClient = require('mongodb').MongoClient,
  config = require('./config.json'),
  logging = require('./logging'),
  validator = require('validator'),
  format = require('util').format;

var database;

exports.init = function(callback) {
  MongoClient.connect(config.mongodb.host, function (err, db) {
    if (err) {
      callback(err);
      return;
    }

    database = db;
    callback();
  });
};

exports.save = function(req, res, next) {

  var input = validator.trim(validator.toString(req.params.email));

  if (!validator.isEmail(input)) {
    res.send(400, {
      message: 'Email is a required field'
    });
    return;
  }

  var collection = database.collection('developerPlusComingSoon');

  collection.insert({
    email: input
  }, function (err) {
    res.send(err ? 503 : 200);
  });
};
