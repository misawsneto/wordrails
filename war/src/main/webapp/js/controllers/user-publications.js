app.controller('UserPublicationsCtrl', ['$scope', '$log', '$state', '$filter', '$timeout', '$interval', 'trix', 'cfpLoadingBar', '$q',
	function($scope, $log, $state, $filter, $timeout, $interval, trix, cfpLoadingBar, $q) {
		
		if(!$scope.app.publicationsCtrl)
			$scope.app.publicationsCtrl = {page: 0, firstLoad: false};

		$scope.$watch('$state.params.type', function(){
			if($state.params.type == "drafts"){
				trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': $scope.app.getLoggedPerson().id,
					'publicationType': 'DRAFT', sortByDate: true}).success(function(response){
						$scope.app.publicationsCtrl.publications = response.posts;
						$scope.app.publicationsCtrl.tabIndex = 0;
						$scope.app.publicationsCtrl.firstLoad = true;
					})
				}
				if($state.params.type == "publications"){
					trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': $scope.app.getLoggedPerson().id,
						'publicationType': 'PUBLISHED', sortByDate: true}).success(function(response){
							$scope.app.publicationsCtrl.publications = response.posts;
							$scope.app.publicationsCtrl.tabIndex = 1;
							$scope.app.publicationsCtrl.firstLoad = true;
						})
					}
					if($state.params.type == "scheduled"){
						trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': $scope.app.getLoggedPerson().id,
							'publicationType': 'SCHEDULED', sortByDate: true}).success(function(response){
								$scope.app.publicationsCtrl.publications = response.posts;
								$scope.app.publicationsCtrl.tabIndex = 2;
								$scope.app.publicationsCtrl.firstLoad = true;
							})
						}
						if($state.params.type == "others"){
							trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': $scope.app.getLoggedPerson().id,
								'publicationType': 'EDITOR', sortByDate: true}).success(function(response){
									$scope.app.publicationsCtrl.publications = response.posts;
									$scope.app.publicationsCtrl.tabIndex = 3;
									$scope.app.publicationsCtrl.firstLoad = true;
								})
							}
						});

	$scope.paginate = function(){

		if(!$scope.postViews || $scope.postViews.length == 0)
			return;

		if($scope.allLoaded)
			return;

		var type = '';
		if($state.params.type == "drafts"){
			type = 'DRAFT'
		}else if($state.params.type == "publications"){
			type = 'PUBLISHED'
		}else if($state.params.type == "scheduled"){
			type = 'SCHEDULED'
		}

		if(!$scope.loadingPage){
			$scope.loadingPage = true;
			trix.searchPosts(null, $scope.app.publicationsCtrl.page + 1, 10, {'personId': $scope.app.getLoggedPerson().id,
						'publicationType': type, sortByDate: true}).success(function(response){

				var posts = response.posts;

				$scope.loadingPage = false;
				$scope.app.publicationsCtrl.page = $scope.app.publicationsCtrl.page + 1;

				if(!posts || posts.length == 0){
					$scope.allLoaded = true;
					return;
				}

				if(!$scope.pages)
					$scope.pages = []

				posts && posts.forEach(function(element, index){
					$scope.app.publicationsCtrl.publications.push(element)
				}); 

			})
			.error(function(){
				$scope.loadingPage = false;
			})
		}
	}


}])		
