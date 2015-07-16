'use strict';

var routes = require('./routes');

/**
 * @ngInject
 */
function OnConfig($stateProvider, $locationProvider, $urlRouterProvider) {

  //configura as rotas do sistema
  routes.configureRoutes($stateProvider, $locationProvider, $urlRouterProvider);

}

module.exports = OnConfig;