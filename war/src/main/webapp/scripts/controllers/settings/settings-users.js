app.controller('SettingsUsersCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdDialog', '$mdToast', '$filter', '$translate', '$mdConstant',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix, FileUploader, TRIX, cfpLoadingBar, $mdDialog, $mdToast, $filter, $translate, $mdConstant){

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


  // -------- users pagination ---------
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
  // trix.getPersons($scope.page, $scope.window, null, 'personProjection').success(getPersonsSuccess);

  // -------- /users pagination ---------


  // ------------ enable / disable person ---------
  $scope.app.enableDisablePerson = function(person){
    if(person)
      $scope.enableDisablePerson = person;

    if(!$scope.enableDisablePerson.enabled)
      trix.disablePerson($scope.enableDisablePerson.id).success(function(){
        $scope.app.showSuccessToast($filter('translate')('messages.SUCCESS_MSG'))
        $scope.enableDisablePerson.enabled = false;
      }).error(function(){
        $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
        $scope.enableDisablePerson.enabled = !$scope.enableDisablePerson.enabled;
      })
    else
      trix.enablePerson($scope.enableDisablePerson.id).success(function(){
        $scope.app.showSuccessToast($filter('translate')('messages.SUCCESS_MSG'))
        $scope.enableDisablePerson.enabled = true;
      }).error(function(){
        $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
        $scope.enableDisablePerson.enabled = !$scope.enableDisablePerson.enabled;
      })
  }

  $scope.enablePersons = function(){
    ids = getSelectedPersonIds();

    trix.enablePersons(ids).success(function(){
      $scope.app.showSuccessToast($filter('translate')('messages.SUCCESS_MSG'))
      $mdDialog.cancel();
      $scope.persons.forEach(function(person, index){
        if(ids.indexOf(person.id) > -1 && person.id != $scope.app.person.id)
          person.enabled = true;
      });
    }).error(function(){
      $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
      $mdDialog.cancel();
    })
  }

  $scope.disablePersons = function(){
    var ids = getSelectedPersonIds();

    trix.disablePersons(ids).success(function(){
      $scope.app.showSuccessToast($filter('translate')('messages.SUCCESS_MSG'))
      $mdDialog.cancel();
      $scope.persons.forEach(function(person, index){
        if(ids.indexOf(person.id) > -1 && person.id != $scope.app.person.id)
          person.enabled = false;
      });
    }).error(function(){
      $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
      $mdDialog.cancel();
    })
  }


  // ------------ enable / disable person ---------
  
  $scope.enableOrDisableOption = null;
  // $scope.bulkChangePermissions = function(ev, func){
    $scope.bulkChangeAccess = function(ev, enableOrDisableOption){
      ids = getSelectedPersonIds();
      if(!ids || !ids.length){
        showNoPersonSelectedDialog(ev);
        return;
    }

    $scope.enableOrDisableOption = enableOrDisableOption;
    $mdDialog.show({
        scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'bulk_change_access_dialog.html',
        targetEvent: ev,
        onComplete: function(){}
      })
      .then(function(answer) {
      //$scope.alert = 'You said the information was "' + answer + '".';
      }, function() {
      //$scope.alert = 'You cancelled the dialog.';
    });
  }

  $scope.toggleAll = function(toggleSelectValue){

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

  function getSelectedPersonIds(){
    var ret = []
    $scope.persons.forEach(function(person, index){
      if(person.selected)
        ret.push(person.id);
    });
    return ret;
  }

  $scope.getSelectedPersonIds = getSelectedPersonIds;

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
        $scope.app.showSuccessToast($filter('translate')('messages.SUCCESS_MSG'))
        $mdDialog.cancel();
      }).error(function(){
        $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
        $mdDialog.cancel();
      })
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
    // ---------- /add user funcs ----------

    $scope.editingPerson = null;
    $scope.showEditProfile = function(event, person){
      // show term alert
      $scope.editingPerson = angular.copy(person);
      $scope.uploadedUserImage = null;
      $scope.uploadedCoverImage = null;
      $mdDialog.show({
        scope: $scope,        // use parent scope in template
        closeTo: {
          bottom: 1500
        },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'edit-person-dialog.html',
        parent: angular.element(document.body),
        targetEvent: event,
        clickOutsideToClose:true
        // onComplete: function(){

        // }
      })
    }    

    // ------- user edit image ----------
    var personImageUploader = $scope.personImageUploader = new FileUploader({
      url: TRIX.baseUrl + "/api/images/upload?imageType=PROFILE_PICTURE"
    });

    $scope.uploadedImage = null;
    personImageUploader.onAfterAddingFile = function(fileItem) {
      $scope.uploadedImage = null;
      personImageUploader.uploadAll();
    };

    personImageUploader.onSuccessItem = function(fileItem, response, status, headers) {
      if(response.filelink){
        $scope.uploadedUserImage = response;
        $scope.editingPerson.imageHash = response.hash;
        $mdToast.hide();
      }
    };

    personImageUploader.onErrorItem = function(fileItem, response, status, headers) {
      if(status == 413)
        $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
      else
        $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
    }

    $scope.clearImage = function(){ 
      $scope.uploadedImage = null;
      personImageUploader.clearQueue();
      personImageUploader.cancelAll()
      $scope.checkLandscape();
      $scope.postCtrl.imageHasChanged = true;
    }

    personImageUploader.onProgressItem = function(fileItem, progress) {
      cfpLoadingBar.start();
      cfpLoadingBar.set(progress/100)
      if(progress == 100){
        cfpLoadingBar.complete()
        toastPromise = $mdToast.show(
          $mdToast.simple()
          .content('Processando...')
          .position('top right')
          .hideDelay(false)
          );
      }
    };

    // cover upload
    var personCoverUploader = $scope.personCoverUploader = new FileUploader({
      url: TRIX.baseUrl + "/api/images/upload?imageType=COVER"
    });

    $scope.uploadedImage = null;
    personCoverUploader.onAfterAddingFile = function(fileItem) {
      $scope.uploadedImage = null;
      personCoverUploader.uploadAll();
    };

    personCoverUploader.onSuccessItem = function(fileItem, response, status, headers) {
       if(response.filelink){
        $scope.uploadedCoverImage = response;
        $scope.editingPerson.coverHash = response.hash;
        $mdToast.hide();
      }
    };

    personCoverUploader.onErrorItem = function(fileItem, response, status, headers) {
      if(status == 413)
        $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
      else
        $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
    }

    $scope.clearImage = function(){ 
      $scope.uploadedImage = null;
      personCoverUploader.clearQueue();
      personCoverUploader.cancelAll()
      $scope.checkLandscape();
      $scope.postCtrl.imageHasChanged = true;
    }

    personCoverUploader.onProgressItem = function(fileItem, progress) {
      cfpLoadingBar.start();
      cfpLoadingBar.set(progress/100)
      if(progress == 100){
        cfpLoadingBar.complete()
        toastPromise = $mdToast.show(
          $mdToast.simple()
          .content('Processando...')
          .position('top right')
          .hideDelay(false)
          );
      }
    };

    // ------------- /user edit image -------------

    $scope.saveProfile = function(person){
      // console.log(person)
      // $mdDialog.cancel();
      if($scope.uploadedUserImage && $scope.uploadedUserImage.id)
        person.image = TRIX.baseUrl + "/api/images/" + $scope.uploadedUserImage.id
      if($scope.uploadedCoverImage && $scope.uploadedCoverImage.id)
        person.cover = TRIX.baseUrl + "/api/images/" + $scope.uploadedCoverImage.id

      var personAuth = {
        id: person.id,
        username: person.username,
        password: null,
        passwordConfirm: null,
        email: person.email
      }

      // ---- update person auth data
      trix.updatePersonAuthData(personAuth).success(function(){

        // ---- update person object
        trix.putPerson(person).success(function(){
          $scope.persons.forEach(function(p){
            if(p.id == person.id)
              angular.extend(p,person);
          })
          $scope.app.showSuccessToast($filter('translate')('messages.SUCCESS_MSG'))
          $mdDialog.cancel();
          // ---- /update person object
        }).error(function(){
          $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
          $mdDialog.cancel();
        })

      // ---- /update person auth data
      }).error(function(){
        $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
        $mdDialog.cancel();
      })
    }

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
        if($scope.persons.length < 20 || $scope.persons.length%20 > 0){
          trix.getPerson(response.id, 'personProjection').success(function(p){
            $scope.persons.push(p)
          })
        }
        $scope.addingPerson = null;
      }).error(function(data, status, headers, config){
        if(status == 409){
          if(data.message.indexOf('username') > -1)
            $scope.app.showErrorToast($filter('translate')('settings.users.USERNAME_IN_USE'))
          if(data.message.indexOf('email') > -1)
            $scope.app.showErrorToast($filter('translate')('settings.users.EMAIL_IN_USE'))
        }else{
          $scope.app.showErrorToast('Dados inválidos. Tente novamente')
          $mdDialog.cancel();
          $scope.addingPerson = null;
        }
        
      });
    }

    $scope.changePasswordDialog = function(event, person){
      $scope.editingPerson = angular.copy(person);
      $mdDialog.show({
        scope: $scope,        // use parent scope in template
        closeTo: {
          bottom: 1500
        },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'change_password_dialog.html',
        parent: angular.element(document.body),
        targetEvent: event,
        clickOutsideToClose:true
        // onComplete: function(){

        // }
      })
    }

    $scope.disabled = false;
    $scope.changePassword = function(){

      $scope.disabled = true;
      var person = angular.copy($scope.editingPerson);

      if(!$scope.newPassword || !$scope.newPassword.trim() || $scope.newPassword !== $scope.newPasswordConfirm){
        $scope.disabled = false;
        $mdDialog.cancel();
        $scope.app.showSimpleDialog($filter('translate')('settings.profile.PASSWORDS_DONT_MATCH') + "")
        return;
      }

      var personAuth = {
        id: person.id,
        username: person.username,
        password: $scope.newPassword,
        passwordConfirm: $scope.newPasswordConfirm,
        email: person.email
      }
       // ---- update person auth data
      trix.updatePersonAuthData(personAuth).success(function(){

        // ---- update person object
        trix.putPerson(person).success(function(){
          $scope.persons.forEach(function(p){
            if(p.id == person.id)
              angular.extend(p,person);
          })
          $scope.app.showSuccessToast($filter('translate')('messages.SUCCESS_MSG'))
          $mdDialog.cancel();
          $scope.disabled = false;
          // ---- /update person object
        }).error(function(){
          $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
          $mdDialog.cancel();
          $scope.disabled = false;
        })

      // ---- /update person auth data
      }).error(function(){
        $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
        $mdDialog.cancel();
        $scope.disabled = false;
      })

      $scope.newPassword = null;
      $scope.newPasswordConfirm = null;
    }

    // -------- invitation -------

    // ---------- invite users funcs ----------
    $scope.showInviteUserDialog = function(event){
      $scope.invitationTemplate = angular.copy($scope.originalInvitationTemplate);
      $scope.invitations = [];
       // show term alert
      $mdDialog.show({
        scope: $scope,        // use parent scope in template
        closeTo: {
          bottom: 1500
        },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'invitation-dialog.html',
        parent: angular.element(document.body),
        targetEvent: event,
        clickOutsideToClose:false,
        escapeToClose: false
        // onComplete: function(){

        // }
      })
      $timeout(function(){
        $(".fr-box a").each(function(){
          if($(this).html() === 'Unlicensed Froala Editor')
            $(this).remove();
        });
      }, 1);
    }
    // ---------- /invite users funcs ----------

    trix.getInvitationTemplate().success(function(invitationTemplate){
      $scope.originalInvitationTemplate = invitationTemplate.response
    })

    var lang = $translate.use();

    $scope.froalaOptions = {
      heightMin: 200,
      language: (lang == 'en' ? 'en_gb' : lang == 'pt' ? 'pt_br' : null),
      fontSizeDefaultSelection: '18',
  // Set the image upload parameter.
      imageUploadParam: 'contents',

      // Set the image upload URL.
      imageUploadURL: '/api/images/upload?imageType=POST',

      // Set request type.
      imageUploadMethod: 'POST',

      // Set max image size to 5MB.
      imageMaxSize: 8 * 1024 * 1024,

      // Allow to upload PNG and JPG.
      imageAllowedTypes: ['jpeg', 'jpg', 'png'],

      // Set the file upload parameter.
      fileUploadParam: 'contents',

      // Set the file upload URL.
      fileUploadURL: '/api/files/upload/doc',

      // Set request type.
      fileUploadMethod: 'POST',

      // Set max file size to 20MB.
      fileMaxSize: 20 * 1024 * 1024,

      // Allow to upload any file.
      fileAllowedTypes: ['*'],

      toolbarInline: false,
      toolbarButtons: ["bold", "italic", "underline", "|", "fontSize", "color", "align", "formatOL", "formatUL", "|", "html"],
      toolbarButtonsMD: ["bold", "italic", "underline", "|", "fontSize", "color", "align", "formatOL", "formatUL", "|", "html"],
      toolbarButtonsSM: ["bold", "italic", "underline", "|", "fontSize", "color", "align", "formatOL", "formatUL", "|", "html"],
      toolbarButtonsXS: ["bold", "italic", "underline", "|", "fontSize", "color", "align", "formatOL", "formatUL", "|", "html"],
      charCounterCount: false,
      height: 300,
      toolbarSticky: false
    }

      $scope.invitatePeople = function(){
        var invitation = {
          emailTemplate: $scope.invitationTemplate,
          emails: angular.copy($scope.invitations)
        }
        trix.invitePeople(invitation).success(function(conflicts){
          $scope.app.showSuccessToast($filter('translate')('settings.users.INVITATIONS_SENT'))
          $mdDialog.cancel();
        })
      }

    // -------- /invitation -------
    
    $scope.$mdConstant = $mdConstant.KEY_CODE;
    $scope.separatorKeys = []
    for(prop in $mdConstant.KEY_CODE){
      $scope.separatorKeys.push($mdConstant.KEY_CODE[prop]);
    }

    settingsUsersCtrl = $scope;
}])

var settingsUsersCtrl = settingsUsersCtrl