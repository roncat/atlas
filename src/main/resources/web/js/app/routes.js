define([ './app', 'angular-route' ], function(app) {

	'use strict';
	return app.config([ '$routeProvider', function($routeProvider) {
		
		$routeProvider.when('/acls', {
			templateUrl : 'partials/acls.html',
			controller : 'AclController',
			controllerAs : 'aclController'
		});
		
		$routeProvider.when('/template', {
			templateUrl : 'partials/template.html',
			controller : 'TemplateController',
			controllerAs : 'templateController'
		});

		$routeProvider.otherwise({
			redirectTo : '/acls'
		});
	} ]);

});