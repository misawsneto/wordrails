// tab controller
app.controller('PostCtrl', ['$scope', '$log', function($scope, $log) {
	$scope.postCtrl = {}
	$scope.postCtrl.editingExisting = false;

	function checkScrollVisible(){

		safeApply($scope, function(){
			if($("#post-cell").scrollTop() == 0)
				$scope.postCtrl.showHeaderShadow = false;
			else
				$scope.postCtrl.showHeaderShadow = true;
		});

	}


	$scope.postCtrl.redactorInit = function(){
		checkScrollVisible()
		$("#post-cell").scroll(function(){
			checkScrollVisible();
			if(this.scrollTop > 530){
				$("#external-toolbar").addClass('fixed-tools')
			}else{
				$("#external-toolbar").removeClass('fixed-tools')
			}
		})
	}
}]);