app.controller('UserPublicationsCtrl', ['$scope', '$log', '$state', '$filter', '$timeout', '$interval', 'trix', 'cfpLoadingBar', '$q',
	function($scope, $log, $state, $filter, $timeout, $interval, trix, cfpLoadingBar, $q) {
		
		if(!$scope.app.publicationsCtrl)
			$scope.app.publicationsCtrl = {page: 0};

		var personId = $scope.app.getLoggedPerson().id;
		trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': personId, 'publicationType': 'PUBLISHED'}).success(function(response){
			$scope.app.publicationsCtrl.publications = response.posts;
		})
}])		
