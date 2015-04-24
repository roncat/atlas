define([ './controllers' ], function(controllers) {
	//'use strict';

	controllers.controller('TemplateController', TemplateController);
	function TemplateController($scope) {
		
		$scope.script = "";
		
		$scope.outScript = "";
		
		$.get("/template").done(function(script){
			$scope.script = script;
			$scope.$safeApply();
		})
		
		$scope.saveScript = function(){
			$.ajax({
				url : "/template",
				type : "POST",
				data : $scope.script,
				contentType : "text/plain; charset=utf-8",
				dataType : "text"
			}).done(function(){
				alert("Template script saved");
			}).fail(function(){
				alert("error in save script");
			});
		}
		
		
		$scope.testScript = function(){
			$.ajax({
				url : "/test-script",
				type : "POST",
				data : $scope.script,
				contentType : "text/plain; charset=utf-8",
				dataType : "text"
			})
			.always(function(jqXHR, textStatus) {
				if (textStatus == 'success') 
					$scope.outScript = jqXHR;
				else
					$scope.outScript = jqXHR.responseText;
				$scope.$safeApply();
            });
		}
		
	}

});