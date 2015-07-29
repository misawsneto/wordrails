app.controller('SettingsUsersCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix, FileUploader, TRIX, cfpLoadingBar){

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
  	trix.findPersonById($state.params.userId, 'personProjection').success(function(response){
  		$scope.selectedPerson = response.persons[0];
  	})
  }else{
  	$scope.editing = false;
  	$scope.creating = false;
  }

  $scope.bulkActions = [
  {name:'Ações em grupo', id:0},
  {name:'Alterar permissões', id:1},
  {name:'Remover permissões', id:2}
  ]

  $scope.bulkActionSelected = $scope.bulkActions[0];

  $scope.page = 0;
  var loading = false;
  $scope.allLoaded = false;
  $scope.beginning = true;
  $scope.window = 20

  if(!$scope.editing && !$scope.creating){
    $scope.showProgress = true;
  	trix.findAllByNetwork(initData.network.id, $scope.page, $scope.window, null, 'personProjection').success(function(response){
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

  $scope.loadPerson = function(person){
  	$state.go('app.settings.users', {'userId': person.id})
  }

  $scope.openDeletePerson = function(person){
  	$scope.app.openSplash('confirm_delete_person.html')
  	$scope.deletePerson = person;
  }

  $scope.app.deletePerson = function(){
    trix.deletePerson($scope.deletePerson.id).success(function(){
      $scope.app.showSuccessToast('Usuário removido com sucesso.');
      $scope.app.cancelModal();
      for (var i = $scope.persons.length - 1; i >= 0; i--) {
        console.log(($scope.persons[i].id + " - " + $scope.deletePerson.id));
        if($scope.persons[i].id == $scope.deletePerson.id){
          $scope.persons.slice(i,1)
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

  $scope.openBulkActionsSplash = function(){
  	if(noPersonSelected())
  		$scope.app.openSplash('confirm_no_person_selected.html');
  	else if($scope.bulkActionSelected.id == 0)
  		return null;
  	else if($scope.bulkActionSelected.id == 2)
  		$scope.app.openSplash('confirm_bulk_delete_persons.html');
  }
}])
