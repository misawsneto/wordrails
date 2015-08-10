app.controller('TagsPageCtrl', ['$scope', '$log', '$timeout', '$rootScope', '$state', 'trix', '$mdToast',
	function($scope, $log, $timeout, $rootScope, $state, trix, $mdToast) {
	var tagName = $scope.tagName = $state.params.tagName;
	$scope.postsPage = 0;

	if(tagName){
		
		trix.findPostsByTagAndStationId(tagName, $scope.app.currentStation.id, $scope.postsPage, 10).success(function(response){
			$scope.postViews = response;
			if($scope.postViews && $scope.postViews.length > 0)
				$scope.postsPage++;
		})

	}else{
		// TODO error
	}

	$scope.paginate = function(){

		if(!$scope.postViews || $scope.postViews.length == 0)
			return;

		if($scope.allLoaded)
			return;

		if(!$scope.loadingPage){
			$scope.loadingPage = true;
			trix.findPostsByTagAndStationId(tagName, $scope.app.currentStation.id, $scope.postsPage, 10).success(function(posts){
				$scope.loadingPage = false;
				$scope.postsPage = $scope.postsPage + 1;

				if(!posts || posts.length == 0){
					$scope.allLoaded = true;
					return;
				}

				if(!$scope.postViews)
					$scope.postViews = []

				posts && posts.forEach(function(element, index){
					$scope.postViews.push(element)
				}); 

			})
			.error(function(){
				$scope.loadingPage = false;
			})
		}
	}


	/*$scope.$watch('app.nowReading', function(postView){
		if(postView){
			$("body").addClass("show-post")
		}
	})*/

}])
