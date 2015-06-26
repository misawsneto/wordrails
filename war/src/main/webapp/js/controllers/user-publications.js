app.controller('UserPublicationsCtrl', ['$scope', '$log', '$state', '$filter', '$timeout', '$interval', 'trix', 'cfpLoadingBar', '$q',
	function($scope, $log, $state, $filter, $timeout, $interval, trix, cfpLoadingBar, $q) {
		
		if(!$scope.app.publicationsCtrl)
			$scope.app.publicationsCtrl = {page: 0, firstLoad: false};

		$scope.$watch('$state.params.type', function(){
			if($state.params.type == "drafts"){
				trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': $scope.app.getLoggedPerson().id,
					'publicationType': 'DRAFT'}).success(function(response){
					$scope.app.publicationsCtrl.publications = response.posts;
					$scope.app.publicationsCtrl.tabIndex = 0;
					$scope.app.publicationsCtrl.firstLoad = true;
				})
			}
			if($state.params.type == "publications"){
				trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': $scope.app.getLoggedPerson().id,
					'publicationType': 'PUBLISHED'}).success(function(response){
					$scope.app.publicationsCtrl.publications = response.posts;
					$scope.app.publicationsCtrl.tabIndex = 1;
					$scope.app.publicationsCtrl.firstLoad = true;
				})
			}
			if($state.params.type == "scheduled"){
				trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': $scope.app.getLoggedPerson().id,
					'publicationType': 'SCHEDULED'}).success(function(response){
					$scope.app.publicationsCtrl.publications = response.posts;
					$scope.app.publicationsCtrl.tabIndex = 2;
					$scope.app.publicationsCtrl.firstLoad = true;
				})
			}
			if($state.params.type == "others"){
				trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': $scope.app.getLoggedPerson().id,
					'publicationType': 'EDITOR'}).success(function(response){
					$scope.app.publicationsCtrl.publications = response.posts;
					$scope.app.publicationsCtrl.tabIndex = 3;
					$scope.app.publicationsCtrl.firstLoad = true;
				})
			}
		});

}])		
