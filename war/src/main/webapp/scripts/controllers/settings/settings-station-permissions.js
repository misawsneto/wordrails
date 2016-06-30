app.controller('SettingsStationPermissionsCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdDialog', '$mdToast', '$filter', '$translate', '$mdConstant', 'station',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix, FileUploader, TRIX, cfpLoadingBar, $mdDialog, $mdToast, $filter, $translate, $mdConstant, station){

$scope.station = station;

trix.getStationPermission(station.id).success(function(permissions){
	$scope.permissions = permissions.userPermissions;
})

  $scope.toggleAll = function(toggleSelectValue){

  	if(toggleSelectValue && $scope.permissions){
  		$scope.permissions.forEach(function(permission, index){
        if(permission.person.id != $scope.app.person.id)
  			 permission.selected = true;
  		}); 
  	}else if($scope.permissions){
  		$scope.permissions.forEach(function(permission, index){
        if(permission.person.id != $scope.app.person.id)
    			permission.selected = false;
  		}); 
  	}
  }

  $scope.showBulkChangePermissions = function(){
      $scope.stationRole = null;
      $mdDialog.show({
        scope: $scope,        // use parent scope in template
        closeTo: {
          bottom: 1500
        },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'change-permissions-dialog.html',
        parent: angular.element(document.body),
        targetEvent: event,
        clickOutsideToClose:true
        // onComplete: function(){

        // }
      })
    }

    $scope.personToDelete = null;
    $scope.showDeletePersonPermission = function(event,person){
    	$scope.disabled = false;
    	$scope.personToDelete = person;
      $scope.stationRole = null;
      $mdDialog.show({
        scope: $scope,        // use parent scope in template
        closeTo: {
          bottom: 1500
        },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'delete-permissions-dialog.html',
        parent: angular.element(document.body),
        targetEvent: event,
        clickOutsideToClose:true
        // onComplete: function(){

        // }
      })
    }

    $scope.deleteStationPermission = function(){
    	var person = $scope.personToDelete;
    	var stationRoleUpdates = {stationsIds: [station.id], usernames: [$scope.personToDelete.username]};
    	if(person){
    		trix.clearStationRoles(stationRoleUpdates).success(function(){
    			$scope.app.showSuccessToast($filter('translate')('messages.SUCCESS_MSG'))
    			for (var i = $scope.permissions.length - 1; i >= 0; i--) {
    				if($scope.permissions[i].person.id === person.id)
    					$scope.permissions.splice(i, 1)
    			}
    			$mdDialog.cancel();
    		}).error(function(){
    			$scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
    			$mdDialog.cancel();
    		})
    	}
    }

  $scope.getPermissionText = function(permission){
  	if(permission.administration){
  		return $filter('translate')('titles.ADMIN')
  	}else if(permission.write && permission.moderate){
  		return $filter('translate')('titles.EDIT')
  	}else if(permission.write){
  		return $filter('translate')('titles.WRITE')
  	}else if(permission.create){
  		return $filter('translate')('titles.COLABORATE')
  	}else if(permission.read){
  		return $filter('translate')('titles.READ')
  	}
  }

var settingsStationPermissionsCtrl = $scope;

}]);

settingsStationPermissionsCtrl = null;