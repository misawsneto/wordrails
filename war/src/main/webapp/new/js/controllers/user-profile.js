app.controller('UserProfile', ['$scope', '$log', '$timeout', '$rootScope', '$state', 'trix',
					function($scope, $log, $timeout, $rootScope, $state, trix) {
	
	// set left side post list to initial scroll
	//$("#post-left-content .left-content-wrap").scrollTop(0);

	var username = $state.params.username

	if(username == null){
		$state.go('access.404')
	}
	
	trix.findByUsername(username).success(function(response){
		try{
			$scope.person = response.persons ? response.persons[0] : null;
		}catch(e){
			$state.go('access.404')
		}
	}).error(function(){
		$state.go('access.404')
	})

}])