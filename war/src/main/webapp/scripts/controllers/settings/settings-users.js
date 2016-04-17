app.controller('SettingsUsersCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdDialog',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix, FileUploader, TRIX, cfpLoadingBar, $mdDialog){

    $scope.pe = {};
    $scope.pe.adminStations = angular.copy($scope.app.adminStations);
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

  $scope.app.lastSettingState = "app.settings.users";

  $scope.selectedPerson = null;

  $scope.stations = $scope.app.stations;
  $scope.person = $scope.app.person;

  if($state.params.newUser){
  	$scope.person = {
  		name: '',
  		username: '',
  		password: '',
  		email: '',
  		emailNotification: true
  	}
  	$scope.editing = false;
  	$scope.creating = true;
  }else if($state.params.username){
  	$scope.editing = true;
  	$scope.creating = false;
  	trix.findByUsername($state.params.username, 'personProjection').success(function(response){
  		$scope.selectedPerson = response.persons[0];
  	})
  }else{
  	$scope.editing = false;
  	$scope.creating = false;
  }

  $scope.bulkActions = [
    {name:'Convites', id:4},
    {name:'Alterar permissões', id:1},
    {name:'Ativar usuários', id:2},
    {name:'Desativar usuários', id:3}
  ]

  $scope.bulkActionSelected = null;

  $scope.page = 0;
  var loading = false;
  $scope.allLoaded = false;
  $scope.beginning = true;
  $scope.window = 20

  if(!$scope.editing && !$scope.creating){
    $scope.showProgress = true;
  	trix.getPersons($scope.page, $scope.window, null, 'personProjection').success(function(response){
  		$scope.persons = response.persons;
      $scope.showProgress = false;
  	});
  }

  trix.countPersonsByNetwork().success(function(response){
    $scope.personsCount = response;
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
      trix.getPersons(page, $scope.window, null, 'personProjection').success(function(response){
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

  $scope.loadPerson = function(person){
  	$state.go('app.settings.users', {'username': person.username})
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

  $scope.app.enableDisablePerson = function(person){
    if(person)
      $scope.pe.enableDisablePerson = person;

    if(!$scope.pe.enableDisablePerson.user.enabled)
      trix.disablePerson($scope.pe.enableDisablePerson.id).success(function(){
        $scope.app.showSuccessToast('Usuário desativado.');
        $scope.pe.enableDisablePerson.user.enabled = false;
      }).error(function(){
        $scope.app.showErrorToast('Erro ao alterar usuário.');
        $scope.pe.enableDisablePerson.user.enabled = !$scope.pe.enableDisablePerson.user.enabled;
      })
    else
      trix.enablePerson($scope.pe.enableDisablePerson.id).success(function(){
        $scope.app.showSuccessToast('Usuário ativado.');
        $scope.pe.enableDisablePerson.user.enabled = true;
      }).error(function(){
        $scope.app.showErrorToast('Erro ao alterar usuário.');
        $scope.pe.enableDisablePerson.user.enabled = !$scope.pe.enableDisablePerson.user.enabled;
      })
  }

  $scope.createPerson = function(ev){
    trix.createPerson($scope.person).success(function(response){
      $scope.app.showSuccessToast('Alterações realizadas com sucesso.')
      $scope.selectedPerson = response;
      $scope.editingPersonLoaded = true;
      $scope.editing = true;
      $scope.creating = false;
      $state.go('app.settings.users', {'username': response.username}, {location: 'replace', inherit: false, notify: false, reload: false})
    }).error(function(data, status, headers, config){
      if(status == 409){
        $scope.app.conflictingData = data;
        $scope.openConflictingUserSplash(ev)
      }else
        $scope.app.showErrorToast('Dados inválidos. Tente novamente')
      
      $timeout(function() {
        cfpLoadingBar.complete(); 
      }, 100);
    });
  }

  $scope.openConflictingUserSplash = function(ev){
    //$scope.app.openSplash('conflicting_person.html')
    $mdDialog.show({
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

  var deletePersons = function(){
    var ids = []
    $scope.persons.forEach(function(person, index){
      if(person.selected)
        ids.push(person.id)
    });

    trix.deletePersons(ids).success(function(){
      $scope.app.showSuccessToast('Usuário removido com sucesso.');
      $scope.app.cancelModal();
      for (var i = $scope.persons.length - 1; i >= 0; i--) {
        if(ids.indexOf($scope.persons[i].id) > -1){
          $scope.persons.splice(i,1)
          $scope.personsCount--;
        }
      };
    })
  }

  $scope.toggleAll = function(toggleSelectValue){

  	if(toggleSelectValue && $scope.persons){
  		$scope.persons.forEach(function(person, index){
  			person.selected = true;
  		}); 
  	}else if($scope.persons){
  		$scope.persons.forEach(function(person, index){
  			person.selected = false;
  		}); 
  	}
  }

  function noPersonSelected(){
  	var ret = true
  	$scope.persons.forEach(function(person, index){
  		if(person.selected)
  			ret = false;
  	});
  	return ret;
  }

  function getSelectedPersonIds(){
    var ret = []
    $scope.persons.forEach(function(person, index){
      if(person.selected)
        ret.push(person.id);
    });
    return ret;
  }

  $scope.selectBulkAction = function(bulkActionSelected){
    $scope.bulkActionSelected = bulkActionSelected;
  }

  $scope.app.applyBulkActions = function(){
    if($scope.bulkActionSelected.id == 0)
      return
    else if($scope.bulkActionSelected.id == 2)
      deletePersons();
    $scope.app.cancelModal();
  }

  $scope.openBulkActionsDialog = function(ev){
    $scope.pe.bulkActionSelected = $scope.bulkActionSelected;

    if($scope.bulkActionSelected == 4){
      
      return;
    }

  	if(noPersonSelected())
  		$scope.openNoPersonSelected(ev);
  	else if($scope.bulkActionSelected == 0)
  		return null;
    else if($scope.bulkActionSelected == 1)
      $scope.bulkChangePermissions(ev);
  	else if($scope.bulkActionSelected == 2 || $scope.bulkActionSelected == 3)
  		$scope.confirmBulkAction(ev)
  }

  $scope.pe.activateDeactivateUsers = function(){
    ids = getSelectedPersonIds();
    if($scope.bulkActionSelected == 2)
      trix.enablePersons(ids).success(function(){
        $scope.app.showSuccessToast('Usuários ativados.');
        $mdDialog.cancel();
        $scope.persons.forEach(function(person, index){
          if(ids.indexOf(person.id) > -1 && person.id != $scope.person.id)
            person.user.enabled = true;
        });
      }).error(function(){
        $scope.app.showSuccessToast('Houve um problema ao executar a operação.');
        $mdDialog.cancel();
      })
    if($scope.bulkActionSelected == 3)
      trix.disablePersons(ids).success(function(){
        $scope.app.showSuccessToast('Usuário desativados.');
        $mdDialog.cancel();
        $scope.persons.forEach(function(person, index){
          if(ids.indexOf(person.id) > -1 && person.id != $scope.person.id)
            person.user.enabled = false;
        });
      }).error(function(){
        $scope.app.showSuccessToast('Houve um problema ao executar a operação.');
        $mdDialog.cancel();
      })
  }

  $scope.bulkChangePermissions = function(ev){
    $mdDialog.show({
        controller: DialogController,
        templateUrl: 'bulk_change_permissions.html',
        targetEvent: ev,
        onComplete: function(){}
      })
      .then(function(answer) {
      //$scope.alert = 'You said the information was "' + answer + '".';
      }, function() {
      //$scope.alert = 'You cancelled the dialog.';
    });
  }

  $scope.openNoPersonSelected = function(ev){
      $mdDialog.show({
        controller: DialogController,
        templateUrl: 'confirm_no_person_selected.html',
        targetEvent: ev,
        onComplete: function(){}
      })
      .then(function(answer) {
      //$scope.alert = 'You said the information was "' + answer + '".';
      }, function() {
      //$scope.alert = 'You cancelled the dialog.';
      });
    }

    $scope.confirmBulkAction = function(ev){
      $mdDialog.show({
        controller: DialogController,
        templateUrl: 'confirm_bulk_action.html',
        targetEvent: ev,
        onComplete: function(){}
      })
      .then(function(answer) {
      //$scope.alert = 'You said the information was "' + answer + '".';
      }, function() {
      //$scope.alert = 'You cancelled the dialog.';
      });
    }

    $scope.pe.applyPermissions = function(stations, permissions){
      var stationRoleUpdates = {stationsIds: [], personsIds: []};
      stationRoleUpdates.personsIds = getSelectedPersonIds();

      if(stations){
        stations.forEach(function(station){
          if(station.selected)
            stationRoleUpdates.stationsIds.push(station.stationId)
        })
      }

      if(permissions == 'ADMIN'){
        stationRoleUpdates.admin = true;
        stationRoleUpdates.writer = true;
        stationRoleUpdates.editor = true;
      }else if(permissions == 'EDITOR'){
        stationRoleUpdates.admin = false;
        stationRoleUpdates.writer = true;
        stationRoleUpdates.editor = true;
      }else if(permissions == 'WRITER'){
        stationRoleUpdates.admin = false;
        stationRoleUpdates.editor = false;
        stationRoleUpdates.writer = true;
      }else{
        stationRoleUpdates.admin = false;
        stationRoleUpdates.editor = false;
        stationRoleUpdates.writer = false;
      }

      trix.updateStationRoles(stationRoleUpdates).success(function(){
        $scope.app.showSuccessToast('Permissões atualizadas.');
        $mdDialog.cancel();
      }).error(function(){
        $scope.app.showSuccessToast('Houve um problema ao executar a operação.');
        $mdDialog.cancel();
      })
    }
}])
