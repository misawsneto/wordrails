app.controller('SettingsUsersCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdDialog',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix, FileUploader, TRIX, cfpLoadingBar, $mdDialog){

  FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
    return true; // true|false
  };

  $scope.stationsPermissions = angular.copy($scope.app.stationsPermissions);

  // ------------- person file upload
  var uploader = $scope.uploader = new FileUploader({
  	url: TRIX.baseUrl + "/api/images/upload?imageType=PROFILE_PICTURE"
  });

  uploader.onAfterAddingFile = function(fileItem) {
  	$scope.app.editingPost.uploadedImage = null;
  	uploader.uploadAll();
  };

  // ------------ person file upload

  var personActiveId = null;
  // $scope.activatePerson = function(Person){
  //   personActiveId = Person.id;
  // }

  // $scope.isActivePerson = function(person){
  //   return personActiveId == person.id
  // }


  // if($state.params.newUser){
  // 	$scope.person = {
  // 		name: '',
  // 		username: '',
  // 		password: '',
  // 		email: '',
  // 		emailNotification: true
  // 	}
  // 	$scope.editing = false;
  // 	$scope.creating = true;
  // }else if($state.params.username){
  // 	$scope.editing = true;
  // 	$scope.creating = false;
  // 	trix.findByUsername($state.params.username, 'personProjection').success(function(response){
  // 		$scope.selectedPerson = response.persons[0];
  // 	})
  // }else{
  // 	$scope.editing = false;
  // 	$scope.creating = false;
  // }

  // -------- users pagination ---------
  $scope.page = 0;
  var loading = false;
  $scope.allLoaded = false;
  $scope.beginning = true;
  $scope.window = 20


  trix.countPersonsByNetwork().success(function(response){
    $scope.personsCount = response;
  })

  var useSearchField = false
  $scope.doSearch = function(){
    if($scope.search || ($scope.search && $scope.search.trim()))
      useSearchField = true;
    $scope.persons = [];
    $scope.showProgress = false;
    $scope.page = 0;
    loading = false
    $scope.allLoaded = false
    $scope.paginate()
  }

  $scope.paginate = function(){
    if(!loading){
      loading = true;
      if(!$scope.allLoaded && !useSearchField){
        $scope.showProgress = true;
        trix.getPersons($scope.page, $scope.window, null, 'personProjection').success(getPersonsSuccess).error(getPersonsError); 
      }else if(!$scope.allLoaded && useSearchField){
        trix.findPersons($scope.search, $scope.page, $scope.window, null, 'personProjection').success(getPersonsSuccess).error(getPersonsError); 
      }
    }
  }

  var getPersonsError = function(response){

  }

  var getPersonsSuccess = function(response){
      if(!$scope.persons || !$scope.persons.length){
        $scope.persons = [];
      }
      
      if(response.persons && response.persons.length > 0){
        response.persons.forEach(function(p){
          $scope.persons.push(p);
        })
        $scope.page++;
      }else{
        $scope.allLoaded = true;
      }

    $scope.showProgress = false;
    loading = false;
  }

  $scope.showProgress = true;
  // get initial users
  trix.getPersons($scope.page, $scope.window, null, 'personProjection').success(getPersonsSuccess);

  // -------- /users pagination ---------

  $scope.loadPerson = function(person){
    trix.getPerson(person.id, 'personProjection').success(function(personProjection){

    })
  }

  $scope.app.enableDisablePerson = function(person){
    if(person)
      $scope.enableDisablePerson = person;

    if(!$scope.enableDisablePerson.enabled)
      trix.disablePerson($scope.enableDisablePerson.id).success(function(){
        $scope.app.showSuccessToast('Usuário desativado.');
        $scope.enableDisablePerson.enabled = false;
      }).error(function(){
        $scope.app.showErrorToast('Erro ao alterar usuário.');
        $scope.enableDisablePerson.enabled = !$scope.enableDisablePerson.enabled;
      })
    else
      trix.enablePerson($scope.enableDisablePerson.id).success(function(){
        $scope.app.showSuccessToast('Usuário ativado.');
        $scope.enableDisablePerson.enabled = true;
      }).error(function(){
        $scope.app.showErrorToast('Erro ao alterar usuário.');
        $scope.enableDisablePerson.enabled = !$scope.enableDisablePerson.enabled;
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
        scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
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
      $scope.app.cancelDialog();
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
    $scope.app.cancelDialog();
  }

  $scope.openBulkActionsDialog = function(ev){

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

  $scope.enablePersons = function(){
    ids = getSelectedPersonIds();
    trix.enablePersons(ids).success(function(){
      $scope.app.showSuccessToast('Usuários ativados.');
      $mdDialog.cancel();
      $scope.persons.forEach(function(person, index){
        if(ids.indexOf(person.id) > -1 && person.id != $scope.person.id)
          person.enabled = true;
      });
    }).error(function(){
      $scope.app.showSuccessToast('Houve um problema ao executar a operação.');
      $mdDialog.cancel();
    })
  }

  $scope.disablePersons = function(){
    var ids = getSelectedPersonIds();
    trix.disablePersons(ids).success(function(){
      $scope.app.showSuccessToast('Usuário desativados.');
      $mdDialog.cancel();
      $scope.persons.forEach(function(person, index){
        if(ids.indexOf(person.id) > -1 && person.id != $scope.person.id)
          person.enabled = false;
      });
    }).error(function(){
      $scope.app.showSuccessToast('Houve um problema ao executar a operação.');
      $mdDialog.cancel();
    })
  }

  $scope.func = null;
  $scope.bulkChangePermissions = function(ev, func){
    $scope.func = func;
    $mdDialog.show({
        scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
        preserveScope: true, // do not forget this if use parent scope
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
        scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
        preserveScope: true, // do not forget this if use parent scope
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
        scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
        preserveScope: true, // do not forget this if use parent scope
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

    $scope.applyPermissions = function(stations, permissions){
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

    // ---------- add user funcs ----------
    $scope.showAddUserDialog = function(){

    }
    // ---------- /add user funcs ----------

    // ---------- invite users funcs ----------
    $scope.showInviteUserDialog = function(){
      
    }
    // ---------- /invite users funcs ----------

    // ---------- add user funcs ----------
    $scope.showAddUserDialog = function(){
      
    }
    // ---------- /add user funcs ----------
}])
