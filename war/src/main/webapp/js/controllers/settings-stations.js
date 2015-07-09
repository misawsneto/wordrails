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

		$scope.thisStation = {}
		$scope.app.initData.stations.forEach(function(station, index){
			if($state.params.stationId == station.id){
				$scope.stationName = station.name;
				$scope.stationId = station.id;
				$scope.thisStation = station;
			}
		});	

		if($state.params.newUser){
			$scope.creating = true;
			$scope.person = {
				'stationRole': {'roleString':'READER', 'writer': false, 'editor': false, 'admin': false, 'station': $scope.thisStation},
				name: '',
				username: '',
				password: '',
				email: '',
				emailNotification: true
			}
		}else
			$scope.creating = false;

		$scope.createPerson = function(){
			trix.createPerson($scope.person).success(function(){
				$scope.app.showSuccessToast('Alterações realizadas com successo.')
			});
		}

		$scope.changePermission = function(){
			if($scope.person.stationRole.roleString == 'ADMIN'){
				$scope.person.stationRole.admin = true;
				$scope.person.stationRole.writer = true;
				$scope.person.stationRole.editor = true;
			}else if($scope.person.stationRole.roleString == 'EDITOR'){
				$scope.person.stationRole.admin = false;
				$scope.person.stationRole.writer = true;
				$scope.person.stationRole.editor = true;
			}else if($scope.person.stationRole.roleString == 'WRITER'){
				$scope.person.stationRole.admin = false;
				$scope.person.stationRole.editor = false;
				$scope.person.stationRole.writer = true;
			}else{
				$scope.person.stationRole.admin = false;
				$scope.person.stationRole.editor = false;
				$scope.person.stationRole.writer = false;
			}
			console.log($scope.person.stationRole);
		}

		trix.findByStationIds([$state.params.stationId], 0, 50, null, 'stationRoleProjection').success(function(personsRoles){
			$scope.personsRoles = personsRoles.stationRoles;
			for (var i = $scope.personsRoles.length - 1; i >= 0; i--) {
				if($scope.personsRoles[i].person.id == $scope.app.initData.person.id)
					$scope.personsRoles.splice(i, 1);
			};
		})

		$scope.loadPerson = function(){

		}

		$scope.app.applyBulkActions = function(){
			if($scope.bulkActionSelected.id == 0)
				return
			else if($scope.bulkActionSelected.id == 2)
				removeAllSelected();
			$scope.app.cancelModal();
		}

		var removeAllSelected = function(){
			var ids = [];
			$scope.personsRoles.forEach(function(role, index){
				if(role.selected)
					ids.push(role.id);
			});

			trix.deletePersonStationRoles(ids).success(function(){
				for (var i = $scope.personsRoles.length - 1; i >= 0; i--) {
					if(ids.indexOf($scope.personsRoles[i].id) > -1)
						$scope.personsRoles.splice(i, 1);
				};
				$scope.app.showSuccessToast('Alterações realizadas com successo.')
				$scope.app.cancelModal();
			})
		}		

		$scope.openDeletePersonRole = function(roleId){
			$scope.app.openSplash('confirm_delete_person.html')
			$scope.deletePersonRoleId = roleId;
		}

		$scope.app.deletePersonRole = function(){
			trix.deleteStationRole($scope.deletePersonRoleId).success(function(response){
				for (var i = $scope.personsRoles.length - 1; i >= 0; i--) {
					if($scope.personsRoles[i].id == $scope.deletePersonRoleId)
						$scope.personsRoles.splice(i, 1);
				};
				$scope.app.showSuccessToast('Alterações realizadas com successo.')
				$scope.app.cancelModal();
			})
		}

		$scope.stopPropagation = function($event){
			$event.stopPropagation();
		}

		$scope.bulkActions = [
			{name:'Ações em grupo', id:0},
			{name:'Alterar selecionados', id:1},
			{name:'Remover selecionados', id:2}
		]

		$scope.bulkActionSelected = $scope.bulkActions[0];

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

		function noPersonSelected(){
			var ret = true
			$scope.personsRoles.forEach(function(role, index){
				if(role.selected)
					ret = false;
			});
			return ret;
		}

		$scope.openBulkActionsSplash = function(){
			if(noPersonSelected())
				$scope.app.openSplash('confirm_no_person_selected.html');
			else if($scope.bulkActionSelected.id == 0)
				return null;
			else if($scope.bulkActionSelected.id == 2)
				$scope.app.openSplash('confirm_bulk_delete_persons.html');
		}

		// $scope.changePersonStation

}]);