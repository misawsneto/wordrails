// tab controller
app.controller('BookmarksCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', 
												 function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast){

	if(!$scope.app.bookmarksCtrl)
		$scope.app.bookmarksCtrl = {}

	if(!$scope.app.bookmarksCtrl.query)
		$scope.app.bookmarksCtrl.query = "";

	$scope.submitSearch = function(){
		$scope.app.bookmarksCtrl.bookmarks = []
		$scope.app.bookmarksCtrl.bookmarksPage = -1;
		trix.searchBookmarks($scope.app.bookmarksCtrl.query, $scope.app.bookmarksCtrl.bookmarksPage + 1, 10)
		.success(function(posts){
			$scope.app.bookmarksCtrl.bookmarks = posts
		})
		.error(function(){
			$scope.searchLoaging = false;
		})
	}

	$scope.submitSearch();

	$scope.paginate = function(){
		if($state.current.name != 'app.bookmarks' || $scope.app.bookmarksCtrl.allLoaded || $scope.app.bookmarksCtrl.bookmarks.length == 0)
			return;

		if(!$scope.searchLoaging){

			$scope.searchLoaging = true;
			trix.searchBookmarks($scope.app.bookmarksCtrl.query, $scope.app.bookmarksCtrl.bookmarksPage + 1, 10)
			.success(function(posts){
				
				$scope.searchLoaging = false;
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
				$scope.searchLoaging = false;
			})

		}
	}

}]);