// tab controller
app.controller('SearchCtrl', ['$scope', '$log', '$timeout', 'trix', function($scope, $log, $timeout, trix) {
	if(!$scope.app.searchCtrl)
		$scope.app.searchCtrl = {}

	$scope.app.searchCtrl.nonemptySearch = $scope.app.searchCtrl.searchResults ? true : false;
	
	$scope.submitSearch = function(){
		if(!$scope.app.searchCtrl.searchQuery || $scope.app.searchCtrl.searchQuery.trim() == "")
			return null;
		
		$scope.app.searchCtrl.searchPage = 0;
		trix.searchPostsFromOrPromotedToStation($scope.app.currentStation.id, $scope.app.searchCtrl.searchQuery, $scope.app.searchCtrl.searchPage, 10)
		.success(function(response){
			$scope.app.searchCtrl.hits = response.hits;
			$scope.app.searchCtrl.searchResults = response.posts;
			$(".search-results").focus();
		})
	}

	$timeout(function() {
		$(".search-results").focus();
	}, 600);
}]);