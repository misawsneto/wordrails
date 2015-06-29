app.controller('UserCtrl', ['$scope', '$log', '$timeout', '$rootScope', '$state', 'trix',
					function($scope ,  $log ,  $timeout ,  $rootScope ,  $state ,  trix) {
	
	// set left side post list to initial scroll
	//$("#post-left-content .left-content-wrap").scrollTop(0);

	var username = $state.params.username

	if(username == null){
		$state.go('access.404')
	}
	
	trix.findByUsername(username).success(function(response){
		try{
			$scope.showProfile = true;
			$scope.person = response.persons[0];
		}catch(e){
			$state.go('access.404')
		}
			$scope.findUserPosts($scope.person);
	}).error(function(){
		$state.go('access.404')
	})

	$scope.postsPage = 0;
	$scope.postViews = [];
	$scope.findUserPosts = function(person){
		trix.getPersonNetworkPosts(person.id, $scope.app.initData.network.id,$scope.postsPage, 5).success(function(posts){
			if(posts && posts.length > 0)
				$scope.postViews = posts;
		})

		trix.findRecommendsByPersonIdOrderByDate(person.id, 0, 6, null, "recommendProjection").success(function(response){
			if(response.recommends && response.recommends.length > 0)
				$scope.recommendations = response.recommends;
		})
	}

	$scope.paginate = function(){

		if(!$scope.postViews || $scope.postViews.length == 0)
			return;

		if($scope.allLoaded)
			return;

		if(!$scope.loadingPage){
			$scope.loadingPage = true;
			trix.getPersonNetworkPosts($scope.person.id, $scope.app.initData.network.id,$scope.postsPage + 1, 5)
			.success(function(posts){
				$scope.loadingPage = false;
				$scope.postsPage = $scope.postsPage + 1;

				if(!posts || posts.length == 0){
					$scope.allLoaded = true;
					return;
				}

				if(!$scope.pages)
					$scope.pages = []

				posts && posts.forEach(function(element, index){
					$scope.pages.push(element)
				}); 

				$(".search-results").focus();
			})
			.error(function(){
				$scope.loadingPage = false;
			})
		}
	}

}])