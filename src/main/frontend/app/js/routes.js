'use strict';

function configureRoutes($stateProvider, $locationProvider, $urlRouterProvider){

 $locationProvider.html5Mode(false);

  $stateProvider
  .state('acls', {
    url: '/acls',
    templateUrl: 'acls.html',
    controller: 'AclController',
    title: 'Acls',
  });
  
  $stateProvider
  .state('template', {
    url: '/template',
    templateUrl: 'template.html',
    controller: 'TemplateController',
    title: 'Template',
  });


  $urlRouterProvider.otherwise('/acls');
}

module.exports = { configureRoutes : configureRoutes };