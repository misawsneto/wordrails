app.controller('DashboardCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$filter', '$localStorage',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $filter, $localStorage){

		if ( angular.isDefined($localStorage.seenWelcome) ) {
      $scope.app.person.seenWelcome = $localStorage.seenWelcome;
    }

		if(!$scope.app.person.seenWelcome){
			$mdDialog.show({
	        scope: $scope,        // use parent scope in template
	        closeTo: {
	          bottom: 1500
	        },
	        preserveScope: true, // do not forget this if use parent scope
	        controller: $scope.app.defaultDialog,
	        templateUrl: 'welcolme-dialog.html',
	        parent: angular.element(document.body),
	        targetEvent: event,
	        clickOutsideToClose:false,
	        escapeToClose: false
	      })
		}

		$scope.setSeen = function(){
      $localStorage.seenWelcome = true;
		}

}]);