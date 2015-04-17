// tab controller
app.controller('PostCtrl', ['$scope', '$log', '$timeout', function($scope, $log, $timeout) {
	$scope.postCtrl = {}
	$scope.postCtrl.editingExisting = false;

	$scope.$watch('app.hidePostOptions', checkPostToolsWidth)
	$scope.$watch('app.hidePostMoreOptions', checkPostToolsWidth)

	function checkPostToolsWidth () {
		$timeout(function(){
 			$("#external-toolbar").width($("#external-toolbar").parent().width())
		}, 50);
	}

	function checkScrollVisible(scrollTop){


			safeApply($scope, function(){
				if(scrollTop == 0)
					$scope.postCtrl.showHeaderShadow = false;
				else
					$scope.postCtrl.showHeaderShadow = true;
			});

			if(scrollTop > 530){
				$("#external-toolbar").addClass('fixed-tools')
			}else{
				$("#external-toolbar").removeClass('fixed-tools')
			}

			checkPostToolsWidth();

	}

	$scope.postCtrl.redactorInit = function(){
		checkScrollVisible($("#post-cell").scrollTop())
		$("#post-cell").scroll(function(){
			checkScrollVisible(this.scrollTop);
		})
	}
}]);