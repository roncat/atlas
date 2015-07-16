'use strict';

// instala vendor libs
window.jQuery = require('jquery');
window.$ = require('jquery');
require('bootstrap');

// modulos do angular
var angular = require('angular');

require('angular-ui-ace')

require('angular-ui-router');
require('angular-bootstrap');
require('angular-animate');
require('angular-messages');

// configura app no angular
require('./templates');
require('./controllers/_index');
require('./services/_index');
require('./directives/_index');
require('./filters/_index');



// cria a aplicação 
angular.element(document).ready(function() {

  // modulos angular requeridos
  var requires = [
    'ui.router',
    'ui.bootstrap',
    'ngAnimate',
    'ngMessages',
    'templates',
    'app.controllers',
    'app.services',
    'app.filters',
    'app.directives',
    'ui.ace'
  ];

  // configura a app na window para testes
  window.app = angular.module('app', requires);

  // configura angular
  angular.module('app').constant('AppSettings', require('./constants'));
  angular.module('app').config(require('./on_config'));
  angular.module('app').run(require('./on_run'));

  // inicia app
  angular.bootstrap(document, ['app']);
 
});