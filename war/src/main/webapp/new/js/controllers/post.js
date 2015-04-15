// tab controller
app.controller('PostCtrl', ['$scope', '$log', function($scope, $log) {
	console.log($scope.$state);
	$scope.app.hideLeftAside = false;
}]);