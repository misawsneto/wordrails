app.controller('SettingsUsersCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdDialog',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix, FileUploader, TRIX, cfpLoadingBar, $mdDialog){

   FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
    return true; // true|false
  };

  var uploader = $scope.uploader = new FileUploader({
  	url: TRIX.baseUrl + "/api/files/contents/simple"
  });

  uploader.onAfterAddingFile = function(fileItem) {
  	$scope.app.editingPost.uploadedImage = null;
  	uploader.uploadAll();
  };

  $scope.app.lastSettingState = "app.settings.users";

  $scope.selectedPerson = null;

  $scope.stations = $scope.app.initData.stations;

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
  }else if($state.params.userId){
  	$scope.editing = true;
  	$scope.creating = false;
  	trix.findPersonById($state.params.userId, 'personProjection').success(function(response){
  		$scope.selectedPerson = response.persons[0];
  	})
  }else{
  	$scope.editing = false;
  	$scope.creating = false;
  }

  $scope.bulkActions = [
    {name:'Alterar permissões', id:1},
    {name:'Remover usuários', id:2}
  ]

  $scope.bulkActionSelected = null;

  $scope.page = 0;
  var loading = false;
  $scope.allLoaded = false;
  $scope.beginning = true;
  $scope.window = 20

  if(!$scope.editing && !$scope.creating){
    $scope.showProgress = true;
  	trix.findAllByNetworkExcludingPerson(initData.network.id, initData.person.id, $scope.page, $scope.window, null, 'personProjection').success(function(response){
  		$scope.persons = response.persons;
      $scope.showProgress = false;
  	});
  }

  trix.countPersonsByNetwork($scope.app.initData.network.id).success(function(response){
    $scope.personsCount = response.count;
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
      trix.findAllByNetworkExcludingPerson(initData.network.id, initData.person.id, page, $scope.window, null, 'personProjection').success(function(response){
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
  	$state.go('app.settings.users', {'userId': person.id})
  }

  $scope.openEnableDisableDialog = function(person, ev){
  	//$scope.app.openSplash('confirm_delete_person.html')
  	
    $mdDialog.show({
      controller: DialogController,
      templateUrl: 'confirm_enable_disable_person.html',
      targetEvent: ev,
      onComplete: function(){}
    })
    .then(function(answer) {
    //$scope.alert = 'You said the information was "' + answer + '".';
    }, function() {
    //$scope.alert = 'You cancelled the dialog.';
    });
    $scope.pe = {
      enableDisablePerson : person};
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

  $scope.app.enableDisablePerson = function(){
    if($scope.pe.enableDisablePerson.user.enabled)
      trix.disablePerson($scope.pe.enableDisablePerson.id).success(function(){
        $scope.app.showSuccessToast('Usuário desabilitado.');
        $scope.pe.enableDisablePerson.user.enabled = false;
        $mdDialog.cancel();
      })
    else
      trix.enablePerson($scope.pe.enableDisablePerson.id).success(function(){
        $scope.app.showSuccessToast('Usuário desabilitado.');
        $scope.pe.enableDisablePerson.user.enabled = false;
        $mdDialog.cancel();
      })
  }

  $scope.createPerson = function(){
    trix.createPerson($scope.person).success(function(response){
      $scope.app.showSuccessToast('Alterações realizadas com successo.')
      $scope.selectedPerson = response;
      $scope.editingPersonLoaded = true;
      $scope.editing = true;
      $scope.creating = false;
      $state.go('app.settings.users', {'userId': response.id}, {location: 'replace', inherit: false, notify: false, reload: false})
    }).error(function(data, status, headers, config){
      if(status == 409){
        $scope.app.conflictingData = data;
        $scope.app.conflictingData.role = $scope.person.stationRole.roleString;
        $scope.openConflictingUserSplash()
      }else
        $scope.app.showErrorToast('Dados inválidos. Tente novamente')
      
      $timeout(function() {
        cfpLoadingBar.complete(); 
      }, 100);
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

  $scope.openBulkActionsSplash = function(){
  	if(noPersonSelected())
  		$scope.app.openSplash('confirm_no_person_selected.html');
  	else if($scope.bulkActionSelected.id == 0)
  		return null;
  	else if($scope.bulkActionSelected.id == 2)
  		$scope.app.openSplash('confirm_bulk_delete_persons.html');
  }
}])
