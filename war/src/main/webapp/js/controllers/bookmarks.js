// tab controller
app.controller('BookmarksCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', 
												 function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast){

	// if(!$scope.app.bookmarksCtrl)
		$scope.app.bookmarksCtrl = {firstLoad:false}

	if(!$scope.app.bookmarksCtrl.query)
		$scope.app.bookmarksCtrl.query = "";

	$scope.submitSearch = function(){
		$scope.app.bookmarksCtrl.bookmarks = []
		$scope.app.bookmarksCtrl.bookmarksPage = -1;
		trix.searchBookmarks($scope.app.bookmarksCtrl.query, $scope.app.bookmarksCtrl.bookmarksPage + 1, 10)
		.success(function(posts){
			$scope.app.bookmarksCtrl.bookmarks = posts
			$scope.app.bookmarksCtrl.firstLoad = true;
		})
		.error(function(){
			$scope.searchLoading = false;
		})
	}

	$scope.submitSearch();

	$scope.paginate = function(){
		if($scope.app.getCurrentStateName() != 'app.bookmarks' || $scope.app.bookmarksCtrl.allLoaded || $scope.app.bookmarksCtrl.bookmarks.length == 0)
			return;

		console.log('asdf');

		if(!$scope.searchLoading){

			$scope.searchLoading = true;
			trix.searchBookmarks($scope.app.bookmarksCtrl.query, $scope.app.bookmarksCtrl.bookmarksPage + 1, 10)
			.success(function(posts){
				
				$scope.searchLoading = false;
				$scope.app.bookmarksCtrl.bookmarksPage = $scope.app.bookmarksCtrl.bookmarksPage + 1

				if(!posts || posts.length == 0){
					$scope.app.bookmarksCtrl.allLoaded = true;
					return;
				}

				posts && posts.forEach(function(element, index){
					$scope.app.bookmarksCtrl.bookmarks.push(element)
				}); 
				
				$(".search-results").focus();
			})
			.error(function(){
				$scope.searchLoading = false;
			})

		}
	}

}]);