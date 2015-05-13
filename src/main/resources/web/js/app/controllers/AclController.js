define([ './controllers', 'jquery' ], function(controllers, $) {
	// 'use strict';

	controllers.controller('AclController', function($scope) {
		
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
			$.ajax({
				url : "/acls",
				type : "POST",
				data : JSON.stringify($scope.rule),
				contentType : "application/json; charset=utf-8",
				dataType : "json"
			}).done(function() {
				$scope.refresh();
				$('#edit').modal('hide');
			})
			.fail(function() {
				alert('failed communication edit')
			});
		}

		// confirm remove acls
		$scope.confirmRemove = function() {
			$.ajax({
				url : "/acls"+$scope.rule.appId,
				type : "DELETE"
			}).done(function() {
				$scope.refresh();
				$('#removeAcl').modal('hide');
			})
			.fail(function() {
				alert('failed communication delete')
			});
			
			
		}

		// refresh data rules
		$scope.refresh = function() {
			$.get("/rules").then(function(data) {
				$scope.rules = data;
				$scope.$safeApply();
			});
		}
		
		$scope.applyConfiguration = function(){
			$.ajax({
				url : "/update-notify",
				type : "POST"
			}).done(function() {
				alert("'configuration applied");
			})
			.fail(function() {
				alert("'fail to configuration apply");
			});
		}

		$scope.refresh();
	});

});