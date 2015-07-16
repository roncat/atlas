'use strict';

var config  = require('../config');
var http    = require('http');
var express = require('express');
var gulp    = require('gulp');
var gutil   = require('gulp-util');
var morgan  = require('morgan');
var httpProxy = require('http-proxy');

gulp.task('server', function() {

  var server = express();
  
  var router = express.Router();

  server.get("/",function(req,res){
	  res.redirect("/"+config.backend.context);
  });
  
  server.use("/"+config.backend.context,express.static(config.dist.root));
  

  var apiProxy = httpProxy.createProxyServer();
  apiProxy.on('error', function(ex) {
	  console.log('servidor backend fora do ar');
	  console.log(ex, ex.stack.split("\n"));
  });
  
  server.all("/"+config.backend.context+"/rest/*", function(req, res){ 
	  apiProxy.web(req, res, { target: config.backend.http });
  });
  
  var s = http.createServer(server);
  s.on('error', function(err){
    if(err.code === 'EADDRINUSE'){
      gutil.log('Development server is already started at port ' + config.serverport);
    }
    else {
      throw err;
    }
  });

  s.listen(config.serverport);

});