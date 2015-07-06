app.controller('SettingsNetworkCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix',
		function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix){
	$scope.app.lastSettingState = "app.settings.network";
	$scope.network = angular.copy($scope.app.initData.network)

	$scope.$watch('network', function(newVal){
		console.log(newVal);
	})

	$scope.saveChanges = function(){
		trix.getNetwork($scope.network.id).success(function(networkResponse){
			trix.putNetwork(networkResponse).success(function(response){
				console.log(response);
			})
		})
	}
}])
