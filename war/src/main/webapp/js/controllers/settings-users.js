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
  	trix.getPerson($state.params.userId, 'personProjection').success(function(person){
  		$scope.selectedPerson = person;
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

  if(!$scope.editing && !$scope.creating){
  	trix.findAllByNetwork(initData.network.id, 0, 15, null, 'personProjection').success(function(response){
  		$scope.persons = response.persons;
  	});
  }

  $scope.loadPerson = function(person){
  	$state.go('app.settings.users', {'userId': person.id})
  }

  $scope.openDeletePerson = function(person){
  	$scope.app.openSplash('confirm_delete_person.html')
  	$scope.deletePerson = person;
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
