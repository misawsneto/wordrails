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

app.controller('SettingsStationsConfigCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location){
		if($state.params.stationId){
			trix.getStation($state.params.stationId, 'stationProjection').success(function(stationResponse){
				$scope.station = stationResponse;
			})
		}else if($state.params.newStation){
			$scope.creating = true;
			$scope.station = {
				'visibility': 'UNRESTRICTED',
				'writable': false,
				'main': false,
				'networks': [TRIX.baseUrl + '/api/networks/' + $scope.app.initData.network.id]
			};
		}

		$scope.updateStation = function(){
			trix.putStation($scope.station).success(function(){
				$scope.app.getInitData();
				$scope.app.showSuccessToast('Alterações realizadas com successo.')
			});
		}

		$scope.createStation = function(){
			if($scope.station.name == null || $scope.station.name.trim() !== ''){
				trix.postStation($scope.station).success(function(){
					$scope.app.showSuccessToast('Estação criada com successo.')
				});
			}
		}

	}])
