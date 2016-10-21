app.controller('SettingsStationsCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$filter',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $filter){
		$scope.app.lastSettingState = "app.settings.stations";

		$scope.setMainStation = function(station){
			trix.setMainStation(station.id, station.main).success(function(){
				$scope.app.showSuccessToast('Alterações realizadas com sucesso.')
			})
      $scope.app.stations.forEach(function(st){
        st.main = false;
      })
      station.main = true;
		}

    $scope.editStation = function(event, station){
      $scope.app.newStation = false;
      $scope.app.editingStation = true;
      $scope.app.stationObj = angular.copy(station);
      $scope.showStationConfigDialog(event)
    }

    $scope.showStationConfigDialog = function(event){
      $scope.disabled = false;
      $mdDialog.show({
        scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'station-config-dialog.html',
        parent: angular.element(document.body),
        targetEvent: event,
        clickOutsideToClose:true
        // onComplete: function(){

        // }
      })
    }

    $scope.slugFocused = false

    $scope.app.stationNameChange = function(station){
      if(!station.id && !$scope.slugFocused){
        station.stationSlug = station.name.toSlug();
      }
    }

    $scope.app.stationSlugChange = function(station){
        station.stationSlug = station.stationSlug.toSlug();
    }

    $scope.app.stationSlugFocus = function(station){
      if(station.stationSlug)
        $scope.slugFocused = true;
    }

    $scope.app.createStation = function(station){
      station.network = TRIX.baseUrl + "/api/networks/" + $scope.app.network.id;
      if(station.name == null || station.name.trim() !== ''){
        trix.postStation(station).success(function(st){
          $scope.app.showSuccessToast($filter('translate')('settings.station.STATION_CREATION_SUCCESS'))
          $mdDialog.cancel();
          trix.getStation(st.id).success(function(stationResponse){
            $scope.app.stations.push(stationResponse);
          })
        });
      }else{
        $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
      }
    }

    $scope.app.updateStation = function(station){
      station.network = TRIX.baseUrl + "/api/networks/" + $scope.app.network.id;
      if(station.name == null || station.name.trim() !== ''){
        var stationCopy = angular.copy(station);
        stationCopy.logo = null;
        trix.putStation(stationCopy).success(function(stationId){
          $scope.app.stations.forEach(function(st){
            if(st.id === station.id){
              angular.extend(st, station)
            }
          })
          $scope.app.showSuccessToast($filter('translate')('messages.SUCCESS_MSG'))
          $mdDialog.cancel();
        });
      }else{
        $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
      }
    }

    $scope.showRemoveStationDialog = function(station){
      $scope.disabled = false;
      $scope.app.stationObj = station;
      $mdDialog.show({
        scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'station-remove-dialog.html',
        parent: angular.element(document.body),
        targetEvent: event,
        clickOutsideToClose:true
        // onComplete: function(){

        // }
      })
    }

    $scope.app.removeStation = function(id){
      trix.forceDeleteStation(id).success(function(response){
        $scope.app.showSuccessToast($filter('translate')('settings.station.STATION_REMOVED_SUCCESS'))
        $mdDialog.cancel();
        for(var i = $scope.app.stations.length - 1; i >= 0; i--) {
            if($scope.app.stations[i].id === id) {
              $scope.app.stations.splice(i, 1);
            }
        }
      });
    }

    $scope.createNewStation = function(event){
      $scope.app.newStation = true
      $scope.app.editingStation = false;
      $scope.app.stationObj = {
        'visibility': 'UNRESTRICTED',
        'writable': false,
        'main': false,
        'network': TRIX.baseUrl + '/api/networks/' + $scope.app.network.id
      };
      $scope.showStationConfigDialog(event)
    }

    $scope.adminStations = [];

   $scope.app.permissions.stationPermissions.forEach(function(perm){
      if(perm.administration){
        $scope.adminStations.push($scope.app.getStationById(perm.stationId))
      }
    });


    settingsStationsCtrl = $scope;
	}])

var settingsStationsCtrl = null;

app.controller('SettingsStationsUsersCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location){

		FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
    return true; // true|false
  };

  var uploader = $scope.uploader = new FileUploader({
  	url: TRIX.baseUrl + "/api/images/upload?imageType=PROFILE_PICTURE"
  });

  uploader.onAfterAddingFile = function(fileItem) {
  	$scope.app.editingPost.uploadedImage = null;
  	uploader.uploadAll();
  };
  uploader.onSuccessItem = function(fileItem, response, status, headers) {
  	if(response.filelink){
  		$scope.app.editingPost.uploadedImage = response;
  		$scope.app.editingPost.uploadedImage.filelink = TRIX.baseUrl + $scope.app.editingPost.uploadedImage.filelink
  		$scope.app.editingPost.showMediaButtons = false;
  		$scope.checkLandscape();
  		$("#image-config").removeClass("hide");
  		$mdToast.hide();
  		$scope.postCtrl.imageHasChanged = true
  	}
  };

  uploader.onErrorItem = function(fileItem, response, status, headers) {
  	if(status == 413)
  		$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
  	else
  		$scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
  }

  $scope.clearImage = function(){ 
  	$("#image-config").addClass("hide");
  	$scope.app.editingPost.uploadedImage = null;
  	uploader.clearQueue();
  	uploader.cancelAll()
  	$scope.checkLandscape();
  	$scope.postCtrl.imageHasChanged = true;
  }

  uploader.onProgressItem = function(fileItem, progress) {
  	cfpLoadingBar.start();
  	cfpLoadingBar.set(progress/100)
  	if(progress == 100){
  		cfpLoadingBar.complete()
  		toastPromise = $mdToast.show(
  			$mdToast.simple()
  			.content('Processando...')
  			.position('top right')
  			.hideDelay(false)
  			);
  	}
  };


  $scope.thisStation = {}
  $scope.app.initData.stations.forEach(function(station, index){
  	if($state.params.stationId == station.id){
  		$scope.stationName = station.name;
  		$scope.stationId = station.id;
  		$scope.thisStation = station;
  	}
  });	

  if($state.params.newUser){
  	$scope.person = {
  		'stationRole': {'roleString':'READER', 'writer': false, 'editor': false, 'admin': false, 'station': $scope.thisStation},
  		name: '',
  		username: '',
  		password: '',
  		email: '',
  		emailNotification: true
  	}
  	$scope.editing = false;
  	$scope.creating = true;
  }else if($state.params.userId){
  	$scope.editing = true;
  	$scope.creating = false;
  	getEditingPerson($state.params.userId)
  }else{
  	$scope.editing = false;
  	$scope.creating = false;
  }

  function getEditingPerson(id){
  	trix.findByStationIdAndPersonId($scope.thisStation.id, id, 'stationRoleProjection').success(function(response){
  		if(!response.stationRoles || response.stationRoles.length == 0){
  			return false;
  		}
  		$scope.person = response.stationRoles[0].person;
  		$scope.editingPersonLoaded = true;
  	})
  }

  $scope.createPerson = function(ev){
  	trix.createPerson($scope.person).success(function(response){
  		$scope.app.showSuccessToast('Alterações realizadas com sucesso.')
      $scope.person = response;
      $scope.editingPersonLoaded = true;
      $scope.editing = true;
      $scope.creating = false;
  		$state.go('app.settings.stationusers', {'stationId': $scope.thisStation.id, 'userId': response.id}, {location: 'replace', inherit: false, notify: false, reload: false})
  	}).error(function(data, status, headers, config){
  		if(status == 409){
  			$scope.app.conflictingData = data;
  			$scope.app.conflictingData.role = $scope.person.stationRole.roleString;
  			$scope.openAddUserToStaionSplash(ev)
  		}else
  		$scope.app.showErrorToast('Dados inválidos. Tente novamente')
  		$timeout(function() {
  			cfpLoadingBar.complete();	
  		}, 100);
  	});
  }

  $scope.app.addConflictingToStation = function(){
  	$scope.app.changeExistingUserPermission();
  	$scope.app.conflictingData.stationRole.person = '/api/persons/'+ $scope.app.conflictingData.conflictingPerson.id;
  	$scope.app.conflictingData.stationRole.station = '/api/stations/' + $scope.thisStation.id
  	trix.postStationRole($scope.app.conflictingData.stationRole).success(function(){
  		$scope.app.showSuccessToast('Alterações realizadas com sucesso.')
  		$state.go('app.settings.stationusers', {'stationId': $scope.thisStation.id, 'userId': $scope.app.conflictingData.conflictingPerson.id, 'newUser': null})
  		$mdDialog.cancel();
  	}).error(function(){
  		$timeout(function() {
  			cfpLoadingBar.complete();	
  		}, 100);
  	})
  }

  $scope.openAddUserToStaionSplash = function(ev){
  	//$scope.app.openSplash('conflicting_person.html')
    $mdDialog.show({
        scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
        preserveScope: true, // do not forget this if use parent scope
        controller: DialogController,
        templateUrl: 'conflicting_person.html',
        targetEvent: ev,
        onComplete: function(){}
      })
      .then(function(answer) {
      //$scope.alert = 'You said the information was "' + answer + '".';
      }, function() {
      //$scope.alert = 'You cancelled the dialog.';
    });
  }

  $scope.app.changeExistingUserPermission = function(){
  	$scope.app.conflictingData.stationRole = {};
  	if($scope.app.conflictingData.role == 'ADMIN'){
  		$scope.app.conflictingData.stationRole.admin = true;
  		$scope.app.conflictingData.stationRole.writer = true;
  		$scope.app.conflictingData.stationRole.editor = true;
  	}else if($scope.person.stationRole.roleString == 'EDITOR'){
  		$scope.app.conflictingData.stationRole.admin = false;
  		$scope.app.conflictingData.stationRole.writer = true;
  		$scope.app.conflictingData.stationRole.editor = true;
  	}else if($scope.person.stationRole.roleString == 'WRITER'){
  		$scope.app.conflictingData.stationRole.admin = false;
  		$scope.app.conflictingData.stationRole.editor = false;
  		$scope.app.conflictingData.stationRole.writer = true;
  	}else{
  		$scope.app.conflictingData.stationRole.admin = false;
  		$scope.app.conflictingData.stationRole.editor = false;
  		$scope.app.conflictingData.stationRole.writer = false;
  	}
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

  $scope.page = 0;
  var loading = false;
  $scope.allLoaded = false;
  $scope.beginning = true;
  $scope.window = 20

  if(!$scope.editing && !$scope.creating){
    $scope.showProgress = true;
    trix.findRolesByStationIds([$state.params.stationId], 0, $scope.window, null, 'stationRoleProjection').success(function(personsRoles){
    	$scope.personsRoles = personsRoles.stationRoles;
      $scope.showProgress = false; 
    })
  }

  trix.countRolesByStationIds([$state.params.stationId]).success(function(response){
    $scope.rolesCount = response;
  })

  $scope.paginate = function(direction){
    var page = 0;
    if(!direction)
      return;

    if(direction == 'left'){
      page = $scope.page-1;
      $scope.allLoaded = false;
    }
    else if(direction == 'right'){
      page = $scope.page+1;
      $scope.beginning = false;
    }

    if(page < 0){
      return;
    }

    if(!$scope.allLoaded){
      $scope.showProgress = true;
      trix.findAllByNetwork(initData.network.id, page, $scope.window, null, 'personProjection').success(function(response){
        if((!response.persons || response.persons.length == 0) && direction == 'right'){
          $scope.allLoaded = true;
        }else{
          if(!$scope.persons && response.persons)
            $scope.persons = response.persons;
          else if(response.persons && response.persons.length > 0){
            $scope.persons = response.persons;
            $scope.page = page;
          }

          if($scope.page == 0)
            $scope.beginning = true;

          if((($scope.page * $scope.window) + $scope.persons.length) == $scope.personsCount)
            $scope.allLoaded = true;
        }
        $scope.showProgress = false;
      }); 
    }
  }

  function DialogController(scope, $mdDialog) {
    scope.app = $scope.app;
    scope.pe = $scope.pe;
    scope.thisStation = $scope.thisStation;

    scope.hide = function() {
      $mdDialog.hide();
    };

    scope.cancel = function() {
      $mdDialog.cancel();
    };

    // check if user has permisstion to write
  };

  $scope.loadPerson = function(person){
  	$state.go('app.settings.stationusers', {'stationId': $scope.thisStation.id, 'userId': person.id})
  }

  $scope.app.applyBulkActions = function(){
  	if($scope.bulkActionSelected.id == 0)
  		return
  	else if($scope.bulkActionSelected.id == 2)
  		removeAllSelected();
  	$scope.app.cancelDialog();
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
  		$scope.app.showSuccessToast('Alterações realizadas com sucesso.')
  		$scope.app.cancelDialog();
  	})
  }		

  $scope.openDeletePersonRole = function(roleId){
  	 $scope.app.deletePersonRoleId = roleId;
    
      $mdDialog.show({
        scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
        preserveScope: true, // do not forget this if use parent scope
        controller: DialogController,
        templateUrl: 'confirm_delete_person.html',
        onComplete: function(){}
      })
      .then(function(answer) {
      //$scope.alert = 'You said the information was "' + answer + '".';
      }, function() {
      //$scope.alert = 'You cancelled the dialog.';
    });
  }

  $scope.app.deletePersonRole = function(){
  	trix.deleteStationRole($scope.app.deletePersonRoleId).success(function(response){
  		for (var i = $scope.personsRoles.length - 1; i >= 0; i--) {
  			if($scope.personsRoles[i].id == $scope.app.deletePersonRoleId)
  				$scope.personsRoles.splice(i, 1);
  		};
      $scope.rolesCount--;
  		$scope.app.showSuccessToast('Alterações realizadas com sucesso.')
  		$mdDialog.cancel();
  	})
  }

  $scope.bulkActions = [
  {name:'Alterar permissões', id:1},
  {name:'Remover permissões', id:2}
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

  $scope.openBulkActionsDialog = function(){
  	if(noPersonSelected())
  		$scope.app.openSplash('confirm_no_person_selected.html');
  	else if($scope.bulkActionSelected.id == 0)
  		return null;
  	else if($scope.bulkActionSelected.id == 2)
  		$scope.app.openSplash('confirm_bulk_delete_persons.html');
  }

  settingsStationUsersCtrl = $scope;

}]);

var settingsStationUsersCtrl = null;