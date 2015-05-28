app.controller('ReadCtrl', ['$scope', '$log', '$timeout', '$rootScope', '$state', 'trix',
					function($scope, $log, $timeout, $rootScope, $state, trix) {
	
	$("#post-left-content .left-content-wrap").scrollTop();
	console.log('sadf');

	$scope.$watch('app.nowReading', function(postView){
		if(postView){
			trix.getPost($scope.app.nowReading.postId).success(function(response){
				$scope.app.nowReading.body = response.body;
			})
		}

	})

}])