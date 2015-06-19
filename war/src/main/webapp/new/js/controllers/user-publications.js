app.controller('UserPublicationsCtrl', ['$scope', '$log', '$state', '$filter', '$timeout', '$interval', 'trix', 'cfpLoadingBar', '$q',
	function($scope, $log, $state, $filter, $timeout, $interval, trix, cfpLoadingBar, $q) {
		
		if(!$scope.app.publicationsCtrl)
			$scope.app.publicationsCtrl = {page: 0};

		var personId = $scope.app.getLoggedPerson().id;
		trix.searchPostsFromOrPromotedToStation($scope.app.currentStation.id, null, $scope.app.publicationsCtrl.page, 10, {'personId': personId}).success(function(response){
			console.log(response.posts);
		})
}])		
