app.controller('SettingsProfileCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage', 'ngJcropConfig',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage, ngJcropConfig){

	$scope.showEditProfile = function(ev){
  	$scope.editingPerson = angular.copy($scope.app.person);
  	$mdSidenav('edit-profile').toggle();
  }

  $scope.cancelEditProfile = function(){
  	$scope.editingPerson = angular.copy($scope.app.person);
  	$mdSidenav('edit-profile').close()
  }

	// ------------------- end of update term tree ---------------

	settingsProfileCtrl = $scope;
}]);

var settingsProfileCtrl = null;

