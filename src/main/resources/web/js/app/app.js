define([
    'angular',
    'angular-route', 'angular-resource',
	'angular-cookies', 'angular-sanitize', 'angular-animate',
	'angular-touch', 'angular-messages', 'ui.ace', 'ace','ace-ext' , 'bootstrap','angular-safe-apply',
    './controllers/index',
    './directives/index',
    './filters/index',
    './services/index'
], function (ng) { 
    'use strict';

    return ng.module('app', [
        'app.services',
        'app.controllers',
        'app.filters',
        'app.directives',
        'ngAnimate',
        'ngRoute',
        'ngCookies',
        'ngResource',
        'ngMessages',
        'ngSanitize',
        'ngAnimate',
        'ngTouch',
        'ui.ace',
        'SafeApply'
    ]);
});