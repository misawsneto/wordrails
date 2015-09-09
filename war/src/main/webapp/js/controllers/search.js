// tab controller
app.controller('SearchCtrl', ['$state', '$scope', '$log', '$timeout', 'trix', function($state, $scope, $log, $timeout, trix) {
	if(!$scope.app.searchCtrl)
		$scope.app.searchCtrl = {searchPage: 0}

	$scope.app.searchCtrl.nonemptySearch = $scope.app.searchCtrl.searchResults ? true : false;
	
	$scope.submitSearch = function(){
		if(!$scope.app.searchCtrl.searchQuery || $scope.app.searchCtrl.searchQuery.trim() == "")
			return null;
		
		$scope.app.searchCtrl.searchPage = 0;
		$scope.app.searchCtrl.allLoaded = false;
		trix.searchPosts($scope.app.searchCtrl.searchQuery, $scope.app.searchCtrl.searchPage, 10)
		.success(function(response){
			$scope.app.searchCtrl.hits = response.hits;
			$scope.app.searchCtrl.searchResults = response.posts;
			$(".search-results").focus();
		})
	}

	$timeout(function() {
		$(".search-results").focus();
	}, 600);

	$scope.paginateSearch = function(){

		if($scope.app.getCurrentStateName() != 'app.search' || !$scope.app.searchCtrl.searchResults || $scope.app.searchCtrl.searchResults.length == 0)
			return;

		if($scope.app.searchCtrl.allLoaded)
			return;

		if(!$scope.searchLoading){

			$scope.searchLoading = true;
			trix.searchPosts($scope.app.searchCtrl.searchQuery, $scope.app.searchCtrl.searchPage + 1, 10)
			.success(function(response){
				
				$scope.searchLoading = false;
				$scope.app.searchCtrl.searchPage = $scope.app.searchCtrl.searchPage + 1

				if(!response.posts || response.posts.length == 0){
					$scope.app.searchCtrl.allLoaded = true;
					return;
				}

				response.posts && response.posts.forEach(function(element, index){
					$scope.app.searchCtrl.searchResults.push(element)
				}); 
				
				$(".search-results").focus();
			})
			.error(function(){
				$scope.searchLoading = false;
			})

		}
	}
}]);