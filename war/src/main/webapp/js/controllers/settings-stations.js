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
				trix.postStation($scope.station).success(function(response){
					trix.getStation(response).success(function(responseStation){
						$scope.app.getInitData();
						$scope.app.showSuccessToast('Estação criada com successo.')
						$scope.creating = false;
						$scope.station = responseStation;
						$state.go('app.settings.stationconfig', {'stationId': response}, {location: 'replace', inherit: false, notify: false, reload: false})
					})
				});
			}
		}

	}])

app.controller('SettingsStationsUsersCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location){

		$scope.app.initData.stations.forEach(function(station, index){
			if($state.params.stationId == station.id)
				$scope.stationName = station.name;
		});	

		trix.findByStationIds([$state.params.stationId], 0, 50, null, 'stationRoleProjection').success(function(personsRoles){
			$scope.personsRoles = personsRoles.stationRoles;
		})

		$scope.stopPropagation = function($event){
			//$event.preventDefault();
			$event.stopPropagation();
		}

		$scope.loadUser = function(){
			console.log('load user');
		}

		$scope.bulkActions = [
			{name:'Ações em grupo', id:0},
			{name:'Alterar selecionados', id:1},
			{name:'Remover selecionados', id:2}
		]

		$scope.bulkActionSelected = $scope.bulkActions[0];

		/*<option value="0">Ações em grupo</option>
							<option value="1">Alterar selecionados</option>
							<option value="1">Remover selecionados</option>*/

		$scope.toggleAll = function(){
			if($scope.toggleSelectValue && $scope.personsRoles){
				$scope.personsRoles.forEach(function(role, index){
					role.selected = true;
				}); 
			}else if($scope.personsRoles){
				$scope.personsRoles.forEach(function(role, index){
					role.selected = false;
				}); 
			}
		}

		$scope.changePersonStation

}]);