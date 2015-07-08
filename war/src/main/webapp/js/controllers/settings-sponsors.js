app.controller('SettingsSponsorsCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location',
		function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location){
			$scope.app.lastSettingState = "app.settings.stations";

			$scope.openDeleteStation = function(stationId){
				$scope.app.openSplash('confirm_delete_station.html')
				$scope.deleteStationId = stationId;
			}

			$scope.app.deleteStation = function(){
				trix.deleteStation($scope.deleteStationId).success(function(){
					$scope.app.getInitData();
					$scope.app.showSuccessToast('Alterações realizadas com successo.')
				});
			}

			$scope.setMainStation = function(station){
				trix.setMainStation(station.id, station.main).success(function(){
					$scope.app.getInitData();
					$scope.app.showSuccessToast('Alterações realizadas com successo.')
				})
			}
}])

app.controller('SettingsSponsorsConfigCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location',
		function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location){
			trix.getStation($state.params.stationId, 'stationProjection').success(function(stationResponse){
					$scope.station = stationResponse;
			})

			$scope.updateStation = function(){
				trix.putStation($scope.station).success(function(){
					$scope.app.getInitData();
					$scope.app.showSuccessToast('Alterações realizadas com successo.')
				});
			}
}])
