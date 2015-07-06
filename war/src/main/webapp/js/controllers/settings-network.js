app.controller('SettingsNetworkCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state',
		function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state){
	$scope.app.lastSettingState = "app.settings.network";
	$scope.network = angular.copy($scope.app.initData.network)
}])
