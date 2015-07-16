'use strict';

var controllersModule = require('./_index');

function TemplateController($scope, $http) {

	$scope.script = "";

	$scope.outScript = "";

	$http.get('rest/template').success(function(script) {
		$scope.script = script;
	}).error(function(err, status) {
		alert("error in read script");
	});

	$scope.saveScript = function() {
		$http({
			url : 'rest/template',
			method : 'POST',
			data : $scope.script,
			 headers: {
                 "Content-Type": "text/plain"
                 
             }
		}).success(function(script) {
			alert("saved script");
		}).error(function(err, status) {
			alert("error in save script");
		});
	}

	$scope.testScript = function() {
		$http({
			url : 'rest/test-script',
			method : 'POST',
			data : $scope.script
		}, {
			transformResponse : function(d, h) {
				return d;
			}
		}).success(function(data) {
			$scope.outScript = data;
		}).error(function(data) {
			$scope.outScript = data;
			alert("error in test script");
		});
	}

}

controllersModule.controller('TemplateController', TemplateController);