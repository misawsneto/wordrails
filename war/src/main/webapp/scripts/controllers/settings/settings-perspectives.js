app.controller('SettingsPerspectivesCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdDialog', '$mdToast', '$filter', '$translate', '$mdConstant', '$mdSidenav',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix, FileUploader, TRIX, cfpLoadingBar, $mdDialog, $mdToast, $filter, $translate, $mdConstant, $mdSidenav){

$scope.togglePerspectives = buildToggler('perspective-list');

	function buildToggler(navID) {
    return function() {
      $mdSidenav(navID)
        .toggle()
    }
  }


var settingsPerspectivesCtrl = $scope;

}]);

settingsPerspectivesCtrl = null;