'use strict';

module.exports = {

  'serverport': 3000,

  'backend': {
	  'context': 'atlas',
	  'http': 'http://localhost:8080'
  },
  
  'assets': {
	  'src': 'app/assets/**/*',
	  'dest': 'build/assets'
  },
  
  'styles': {
    'src' : ['app/styles/**/*.less','node_modules/bootstrap/dist/css/bootstrap.css'],
    'dest': 'build/css'
  },

  'scripts': {
    'src' : ['app/js/**/*.js'],
    'dest': 'build/js'
  },

  'images': {
    'src' : ['app/images/**/*'],
    'dest': 'build/images'
  },

  'fonts': {
    'src' : ['app/fonts/**/*','node_modules/bootstrap/dist/fonts/**/*'],
    'dest': 'build/fonts'
  },

  'views': {
    'watch': [
      'app/index.html',
      'app/views/**/*.html'
    ],
    'src': 'app/views/**/*.html',
    'dest': 'app/js'
  },

  'gzip': {
    'src': 'build/**/*.{html,xml,json,css,js,js.map}',
    'dest': 'build/',
    'options': {}
  },

  'dist': {
    'root'  : 'build'
  },

  'browserify': {
    'entries'   : ['./app/js/main.js'],
    'bundleName': 'main.js',
    'sourcemap' : true
  },

  'test': {
    'karma': 'test/karma.conf.js',
    'protractor': 'test/protractor.conf.js'
  }

};
