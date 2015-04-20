// tab controller
app.controller('StationsCtrl', ['$scope', '$log', '$state', '$filter', function($scope, $log, $state, $filter) {
	$scope.app.termPerspectiveView = initTermPerspective;
}]);