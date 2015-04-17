// tab controller
app.controller('SearchCtrl', ['$scope', '$log', '$timeout', function($scope, $log, $timeout) {
	$scope.searchCtrl = {}
	$scope.searchCtrl.nonemptySearch = $scope.app.searchResults ? true : false;
	
	$scope.submitSearch = function(){
		$scope.searchCtrl.searchResults = $scope.app.searchResults = [1,2,3];
		$(".search-results").focus();
	}

	$timeout(function() {
		$(".search-results").focus();
	}, 600);
}]);