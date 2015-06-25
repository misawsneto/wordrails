app.controller('UserPublicationsCtrl', ['$scope', '$log', '$state', '$filter', '$timeout', '$interval', 'trix', 'cfpLoadingBar', '$q',
	function($scope, $log, $state, $filter, $timeout, $interval, trix, cfpLoadingBar, $q) {
		
		if(!$scope.app.publicationsCtrl)
			$scope.app.publicationsCtrl = {page: 0};

		console.log($state.params.publicationType);

		if($state.params.publicationType == "publications")
			$scope.app.publicationsCtrl.tabIndex = 0;
		if($state.params.publicationType == "drafts")
			$scope.app.publicationsCtrl.tabIndex = 1;
		if($state.params.publicationType == "scheduled")
			$scope.app.publicationsCtrl.tabIndex = 2;
		if($state.params.publicationType == "others")
			$scope.app.publicationsCtrl.tabIndex = 3;

		var personId = $scope.app.getLoggedPerson().id;
		trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': personId, 'publicationType': 'PUBLISHED'}).success(function(response){
			$scope.app.publicationsCtrl.publications = response.posts;
		})
}])		
