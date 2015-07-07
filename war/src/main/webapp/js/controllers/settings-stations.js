app.controller('SettingsStationsCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location',
		function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location){
			$scope.app.lastSettingState = "app.settings.stations";

			$scope.openDeleteStation = function(stationId){
				$scope.app.openSplash('confirm_delete_station.html')
				$scope.deleteStationId = stationId;
			}

			$scope.app.deleteStation = function(){
				trix.deleteStation($scope.deleteStationId).success(function(){
					$scope.app.getInitData();
					$scope.app.showSuccessToast('Alterações salvas com successo.')
				});
			}
}])

app.controller('SettingsStationsConfigCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location',
		function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location){
			$scope.app.lastSettingState = "app.settings.stationsconfig";
			trix.getStation($state.params.stationId, 'stationProjection').success(function(stationResponse){
					$scope.station = stationResponse;
			})
}])
