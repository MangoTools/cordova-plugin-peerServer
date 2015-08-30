var cordova = require('cordova');
 argscheck = require('cordova/argscheck');


var PeerServer = function() {};

PeerServer.prototype.startServer = function(options, success, error) {
	  var defaults = {
			    'port': 9000,
			  };

	  // Merge optional settings into defaults.
	  for (var key in defaults) {
	    if (typeof options[key] !== 'undefined') {
	      defaults[key] = options[key];
	    }
	  }

 	return cordova.exec(success, error, "PeerServer", "startServer", [ defaults ]);
};

PeerServer.prototype.stopServer = function(success, error) {
	  return cordova.exec(success, error, "PeerServer", "stopServer", []);
};

module.exports = new PeerServer();