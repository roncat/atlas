requirejs.config({
	paths : {
		'bootstrap' : 'vendors/bootstrap/dist/js/bootstrap.min',
		'jquery' : 'vendors/jquery/dist/jquery.min',
		'angular' : 'vendors/angular/angular',
		'angular-route' : 'vendors/angular-route/angular-route.min',
		'angular-resource' : 'vendors/angular-resource/angular-resource.min',
		'angular-cookies' : 'vendors/angular-cookies/angular-cookies.min',
		'angular-sanitize' : 'vendors/angular-sanitize/angular-sanitize.min',
		'angular-animate' : 'vendors/angular-animate/angular-animate.min',
		'angular-touch' : 'vendors/angular-touch/angular-touch.min',
		'angular-messages' : 'vendors/angular-messages/angular-messages.min',
		'ui.ace' : 'vendors/angular-ui-ace/ui-ace.min',
		'domReady' : 'vendors/requirejs-domready/domReady'
	},
	shim : {
		"bootstrap" : {
			"deps" : [ 'jquery' ]
		},
		'angular' : {
			exports : 'angular'
		},
		'angular-route' : {
			deps : [ 'angular' ]
		},
		'angular-resource' : {
			deps : [ 'angular' ]
		},
		'angular-cookies' : {
			deps : [ 'angular' ]
		},
		'angular-sanitize' : {
			deps : [ 'angular' ]
		},
		'angular-animate' : {
			deps : [ 'angular' ]
		},
		'angular-messages' : {
			deps : [ 'angular' ] 
		},
		'angular-touch' : {
			deps : [ 'angular' ]
		},
		'ui.ace' : {
			deps : [ 'angular' ]
		}
	}
});

define([ 'require', 'angular', 'app/app', 'app/routes' ],
		function(require, ng) {
			require([ 'domReady!' ], function(document) {
				ng.bootstrap(document, [ 'app' ]);
			});
		});