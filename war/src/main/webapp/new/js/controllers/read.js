app.controller('ReadCtrl', ['$scope', '$log', '$timeout', '$rootScope', '$state', 'trix',
					function($scope, $log, $timeout, $rootScope, $state, trix) {
	
	$("#post-right-content cell").scrollTop();

	console.log($scope.app.nowReading);

	trix.getPost($scope.app.nowReading.postId).success(function(response){
		console.log(response);
		$scope.app.nowReading.body = response.body;
	})
}])