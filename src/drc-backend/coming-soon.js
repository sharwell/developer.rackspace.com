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
  if (!req.params.email || !validator.isEmail(validator.toString(req.params.email))) {
    res.send(400, {
      message: 'Email is a required field'
    });
    return;
  }

  var collection = database.collection('developer-plus-coming-soon');

  collection.insert({
    email: validator.toString(req.params.email)
  }, function (err) {
    res.send(err ? 503 : 200);
  });
};
