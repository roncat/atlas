'use strict';

var controllersModule = require('./_index');

  
function AclController($scope,$http) {
	
	$scope.rules = [];
	$scope.rule = {};

	// select a copy of selected rule
	$scope.selectRule = function(rule) {
		$scope.rule = {
			appId : rule.appId,
			acl : rule.acl
		};
	}

	// confirm edit acls
	$scope.confirmEditAcl = function() {
		$http({
	      url: 'rest/acls',
	      method: 'POST',
	      data:JSON.stringify($scope.rule)
	    }).success(function(data) {
	    	$scope.refresh();
			$('#edit').modal('hide');
	    }).error(function(err, status) {
	    	alert('failed communication edit')
	    });
	}

	// confirm remove acls
	$scope.confirmRemove = function() {
		$http({
	      url: 'rest/acls'+$scope.rule.appId,
	      method: 'DELETE'
	      
	    }).success(function(data) {
	    	$scope.refresh();
			$('#removeAcl').modal('hide');
	    }).error(function(err, status) {
	    	alert('failed communication delete')
	    });
	}

	// refresh data rules
	$scope.refresh = function() {
		$http.get('rest/rules')
		.success(function(data) {
			$scope.rules = data;
	    }).error(function(err, status) {
	    	alert("fail retrive rules");
	    });
	}
	
	$scope.applyConfiguration = function(){
		
		$http({
	      url: 'rest/update-notify',
	      method: 'POST'
	    }).success(function(data) {
	    	alert("configuration applied");
	    }).error(function(err, status) {
	    	alert("fail to configuration apply");
	    });

	}

	$scope.refresh();
};

controllersModule.controller('AclController', AclController);