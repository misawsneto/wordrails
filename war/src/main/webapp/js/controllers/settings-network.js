app.controller('SettingsNetworkCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix',
		function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix){
	$scope.app.lastSettingState = "app.settings.network";
	$scope.network = angular.copy($scope.app.initData.network)

	$scope.saveChanges = function(){
		trix.putNetwork($scope.network).success(function(response){
			$scope.app.getInitData();
			$scope.app.showSuccessToast('Alterações salvas com successo.')
		})
	}
}])
