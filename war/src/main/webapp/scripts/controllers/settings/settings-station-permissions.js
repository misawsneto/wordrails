app.controller('SettingsStationPermissionsCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdDialog', '$mdToast', '$filter', '$translate', '$mdConstant', 'station',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix, FileUploader, TRIX, cfpLoadingBar, $mdDialog, $mdToast, $filter, $translate, $mdConstant, station){

$scope.station = station;

  var loadPermissions = function(){
    trix.getStationPermission(station.id).success(function(permissions){
      $scope.permissions = permissions.userPermissions;
    })
  }

  loadPermissions();

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
      $scope.disabled = false;
      $mdDialog.show({
        scope: $scope,        // use parent scope in template
        closeTo: {
          bottom: 1500
        },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'bulk-change-permissions-dialog.html',
        parent: angular.element(document.body),
        targetEvent: event,
        clickOutsideToClose:true
        // onComplete: function(){

        // }
      })
    }

     $scope.showChangePermissions = function(permission){
     	$scope.disabled = false;
      $scope.stationRole = null;
      $scope.personPermissionToChange = permission;
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
  		return $filter('translate')('settings.users.permissions.ADMIN')
  	}else if(permission.write && permission.moderate){
  		return $filter('translate')('settings.users.permissions.EDITOR')
  	}else if(permission.write){
  		return $filter('translate')('settings.users.permissions.WRITER')
  	}else if(permission.create){
  		return $filter('translate')('settings.users.permissions.COLABORATOR')
  	}else if(permission.read){
  		return $filter('translate')('settings.users.permissions.READER')
  	}
  }

  function getSelectedPersonIds(){
    var ret = []
    $scope.permissions && $scope.permissions.length > 0 && $scope.permissions.forEach(function(permission, index){
      if(permission.selected)
        ret.push(permission.person.id);
    });
    return ret;
  }

  function getSelectedPersonUsernames(){
    var ret = []
    $scope.permissions && $scope.permissions.length > 0 && $scope.permissions.forEach(function(permissions, index){
      if(permissions.selected)
        ret.push(permissions.person.username);
    });
    return ret;
  }

  $scope.getSelectedPersonIds = getSelectedPersonIds;

    $scope.applyBulkUserPermissions = function(permissions){
      var stationRoleUpdates = {stationsIds: [station.id], usernames: []};
      stationRoleUpdates.usernames = getSelectedPersonUsernames();

      updatePermissions(stationRoleUpdates, permissions)
    }

    $scope.applyUserPermissions = function(permissions){
      var stationRoleUpdates = {stationsIds: [station.id], usernames: [$scope.personPermissionToChange.person.username]};
      updatePermissions(stationRoleUpdates, permissions)
    }

    var updatePermissions = function(stationRoleUpdates, permissions, reload){
    	if(permissions == 'ADMIN'){
        stationRoleUpdates.admin = true;
        stationRoleUpdates.writer = true;
        stationRoleUpdates.editor = true;
        stationRoleUpdates.colaborator = true;
      }else if(permissions == 'EDITOR'){
        stationRoleUpdates.admin = false;
        stationRoleUpdates.writer = true;
        stationRoleUpdates.editor = true;
        stationRoleUpdates.colaborator = true;
      }else if(permissions == 'WRITER'){
        stationRoleUpdates.admin = false;
        stationRoleUpdates.editor = false;
        stationRoleUpdates.writer = true;
        stationRoleUpdates.colaborator = true;
      }else if(permissions == 'COLABORATOR'){
        stationRoleUpdates.admin = false;
        stationRoleUpdates.editor = false;
        stationRoleUpdates.writer = false;
        stationRoleUpdates.colaborator = true;
      }else{
        stationRoleUpdates.admin = false;
        stationRoleUpdates.editor = false;
        stationRoleUpdates.writer = false;
        stationRoleUpdates.colaborator = false;
      }

      trix.updateStationRoles(stationRoleUpdates).success(function(){
        $scope.app.showSuccessToast($filter('translate')('settings.users.PERMISSIONS_UPDATES'));
        updateTable(stationRoleUpdates)
        $mdDialog.cancel();
        if(reload)
          loadPermissions();
      }).error(function(){
        $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'));
        $mdDialog.cancel();
      })
    }

    var updateTable = function(stationRoleUpdates){
    	if($scope.permissions && $scope.permissions.length > 0){
    		$scope.permissions.forEach(function(permission){
    			stationRoleUpdates.usernames.forEach(function(username){
    				if(username === permission.username){
    					if(stationRoleUpdates.admin == true){
    						permission.administration = permission.create = permission.write = permission.delete = 
    						permission.moderate = permission.read = true;
			        }else if(stationRoleUpdates.editor == true){
    						permission.create = permission.write = permission.delete = 
    						permission.moderate = permission.read = true;

    						permission.administration = false;

    					} else if(stationRoleUpdates.writer == true){
    						permission.create = permission.write = permission.read = true;

    						permission.administration = permission.moderate = permission.delete = false;

			        }else if(stationRoleUpdates.colaborator == true){
			        	permission.create = permission.read = true;
			        	permission.administration = permission.moderate = permission.delete = permission.write = false;
			      	}else{
			        	permission.read = true;
			        	permission.create = permission.administration = permission.moderate = permission.delete = permission.write = false;
			        }
    				}
    			})
    		})
    	}
    }

    var showNoPersonSelectedDialog = function(event){
    $mdDialog.show({
        scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'no-person-selected-dialog.html',
        targetEvent: event,
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
        controller: $scope.app.defaultDialog,
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

    // ---------- add user funcs ----------
    $scope.showAddUserDialog = function(event){
      $scope.addingPerson = null;
       // show term alert
      $mdDialog.show({
        scope: $scope,        // use parent scope in template
        closeTo: {
          bottom: 1500
        },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'add-profile-dialog.html',
        parent: angular.element(document.body),
        targetEvent: event,
        clickOutsideToClose:true
        // onComplete: function(){

        // }
      })
    }

    $scope.stationRole = 'READER';
    $scope.userStationRole = 'READER';

    $scope.addingPerson = null;
    $scope.createPerson = function(){
      if(!$scope.addingPerson || !$scope.addingPerson.name || !$scope.addingPerson.name.trim() ||
        !$scope.addingPerson.username || !$scope.addingPerson.username.trim() ||
        !$scope.addingPerson.email || !$scope.addingPerson.email.trim()){

        $scope.app.showErrorToast($filter('translate')('settings.users.REQUIRED_FIELDS'))
        return;
      }

      if($scope.addingPerson.password && $scope.addingPerson.password.trim() && ($scope.newPassword !== $scope.newPasswordConfirm)){
        $scope.app.showSimpleDialog($filter('translate')('settings.profile.PASSWORDS_DONT_MATCH') + "")
        return;
      }

      trix.createPerson($scope.addingPerson).success(function(response){
        $mdDialog.cancel();
        $scope.app.showSuccessToast($filter('translate')('messages.SUCCESS_MSG'))
        $scope.personsCount++;
        $scope.allLoaded = false;
        applyPermissions($scope.stationRole)
        $scope.addingPerson = null;
      }).error(function(data, status, headers, config){
        if(status == 409){
          if(data.message && data.message.toLowerCase().indexOf('username') > -1)
            $scope.app.showErrorToast($filter('translate')('settings.users.USERNAME_IN_USE'))
          if(data.message && data.message.toLowerCase().indexOf('email') > -1)
            $scope.app.showErrorToast($filter('translate')('settings.users.EMAIL_IN_USE'))
        }else{
          $scope.app.showErrorToast($filter('translate')('messages.INVALID_SIGNUP'))
        }
      });
    }

    var applyPermissions = function(permissions){
      var username = $scope.addingPerson.username
      var person = angular.copy($scope.addingPerson);
      var stationRoleUpdates = {stationsIds: [$scope.station.id], usernames: [username]};

      stationRoleUpdates.read = true;
      if(permissions == 'ADMIN'){
        stationRoleUpdates.admin = true;
        stationRoleUpdates.writer = true;
        stationRoleUpdates.editor = true;
        stationRoleUpdates.colaborator = true;
      }else if(permissions == 'EDITOR'){
        stationRoleUpdates.admin = false;
        stationRoleUpdates.writer = true;
        stationRoleUpdates.editor = true;
        stationRoleUpdates.colaborator = true;
      }else if(permissions == 'WRITER'){
        stationRoleUpdates.admin = false;
        stationRoleUpdates.editor = false;
        stationRoleUpdates.writer = true;
        stationRoleUpdates.colaborator = true;
      }else if(permissions == 'COLABORATOR'){
        stationRoleUpdates.admin = false;
        stationRoleUpdates.editor = false;
        stationRoleUpdates.writer = false;
        stationRoleUpdates.colaborator = true;
      }else{
        stationRoleUpdates.admin = false;
        stationRoleUpdates.editor = false;
        stationRoleUpdates.writer = false;
        stationRoleUpdates.colaborator = false;
      }

      trix.updateStationRoles(stationRoleUpdates).success(function(){
        stationRoleUpdates.username = username;
        stationRoleUpdates.administration = stationRoleUpdates.admin
        stationRoleUpdates.delete = stationRoleUpdates.editor || stationRoleUpdates.admin
        stationRoleUpdates.moderate = stationRoleUpdates.editor;
        stationRoleUpdates.write = stationRoleUpdates.writer
        stationRoleUpdates.create = stationRoleUpdates.editor || stationRoleUpdates.writer || stationRoleUpdates.colaborator
        stationRoleUpdates.read = true
        stationRoleUpdates.person = person;
        $scope.permissions.push(stationRoleUpdates)
        // $scope.app.showSuccessToast($filter('translate')('settings.users.PERMISSIONS_UPDATES'));
        // $mdDialog.cancel();
      }).error(function(){
        // $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'));
        // $mdDialog.cancel();
      })
    }

		// -------- users search and pagination ---------

	$scope.showAddPermissionDialog = function(event){
    $scope.doSearchUsers();
		$mdDialog.show({
			scope: $scope,        // use parent scope in template
			closeTo: {
				bottom: 1500
			},
			preserveScope: true, // do not forget this if use parent scope
			controller: $scope.app.defaultDialog,
			templateUrl: 'add-permission-dialog.html',
			parent: angular.element(document.body),
			targetEvent: event,
			clickOutsideToClose:true
			// onComplete: function(){

			// }
		})
	}

	$scope.doSearchUsers = function(){
		if($scope.searchUsers || ($scope.searchUsers && $scope.searchUsers.trim()))
			useSearchField = true;
		$scope.persons = [];
		$scope.showProgress = false;
		$scope.page = 0;
		loading = false
		$scope.allLoaded = false
		$scope.paginateUsers()
	}

	$scope.page = 0;
	var loading = false;
	var useSearchField = false;
	$scope.allLoaded = false;
	$scope.beginning = true;
	$scope.window = 20

	$scope.personsCount = 0;
	trix.countPersonsByNetwork().success(function(response){
		$scope.personsCount = response;
	})

	$scope.paginateUsers = function(){
		if(!loading){
			loading = true;
			if(!$scope.allLoaded && !useSearchField){
				$scope.showProgress = true;
				trix.getPersons($scope.page, $scope.window, null, 'personProjection').success(getPersonsSuccess).error(getPersonsError);
			}else if(!$scope.allLoaded && useSearchField){
				trix.findPersons($scope.searchUsers, $scope.page, $scope.window, null, 'personProjection').success(getPersonsSuccess).error(getPersonsError);
			}
		}
	}

	var getPersonsError = function(response){
	}

	var getPersonsSuccess = function(response){
		if(!$scope.persons || !$scope.persons.length){
			$scope.persons = [];
		}

    $scope.showProgress = false;
    loading = false;

		if(response.persons && response.persons.length > 0){
			response.persons.forEach(function(p){
        var insert = true;
        for (var i = $scope.permissions.length - 1; i >= 0; i--) {
          if($scope.permissions[i].person.id == p.id)
            insert = false;
        }
        if(insert)
	        $scope.persons.push(p);
			})
      $scope.page++;
      if($scope.persons && $scope.persons.length < 10)
        $scope.paginateUsers();
		}else{
			$scope.allLoaded = true;
		}
	}

  $scope.toggleAllUser = function(toggleSelectValue){

    if(toggleSelectValue && $scope.persons){
      $scope.persons.forEach(function(person, index){
        if(person.id != $scope.app.person.id)
         person.selected = true;
      }); 
    }else if($scope.persons){
      $scope.persons.forEach(function(person, index){
        if(person.id != $scope.app.person.id)
          person.selected = false;
      }); 
    }
  }

  function getSelectedUsers(){
    var ret = []
    $scope.persons && $scope.persons.length > 0 && $scope.persons.forEach(function(person, index){
      if(person.selected)
        ret.push(person.username);
    });
    return ret;
  }


  $scope.applyBulkPermissions = function(permissions){
    var stationRoleUpdates = {stationsIds: [station.id], usernames: []};
    stationRoleUpdates.usernames = getSelectedUsers();

    updatePermissions(stationRoleUpdates, permissions, true)
  }


	$scope.showProgress = true;
	// get initial users
	// trix.getPersons($scope.page, $scope.window, null, 'personProjection').success(getPersonsSuccess);

	// -------- /users pagination ---------

var settingsStationPermissionsCtrl = $scope;

}]);

settingsStationPermissionsCtrl = null;