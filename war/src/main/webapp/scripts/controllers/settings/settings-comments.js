app.controller('SettingsCommentsCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdDialog', '$mdToast', '$filter', '$translate', '$mdConstant',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix, FileUploader, TRIX, cfpLoadingBar, $mdDialog, $mdToast, $filter, $translate, $mdConstant){

    $scope.totalCommentsCount = 0;
    $scope.comments = [];

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


    // ------------- /user edit image -------------

    $scope.commentsCtrl = {
      'page': 0,
      'allLoaded': false
    }

    $scope.resetPage = function(){
      $scope.commentsCtrl.page = 0;
      $scope.commentsCtrl.allLoaded = false;
      $scope.comments = [];
    }

    $scope.paginate = function(){
      if(!$scope.loading && !$scope.commentsCtrl.allLoaded){
        $scope.loading = true;

        trix.searchComments($scope.searchQuery, null, null, null, null, null, $scope.commentsCtrl.page, 20, '-date', ['author', 'post']).success(function(response,a,b,c){
          handleSuccess(response);
          if(response && response.length > 0){
            $scope.totalCommentsCount = c.totalElements;
          }
          $scope.loading = false;
        }).error(function(){
          $scope.loading = false;
          $scope.commentsCtrl.allLoaded = true;
        })
      }
    }

  var handleSuccess = function(comments){
    if(comments && comments.length > 0){

        if(!$scope.comments)
          $scope.comments = []

        comments.forEach(function(post){
          $scope.comments.push(post);
        })
        $scope.commentsCtrl.page++;
        $scope.commentsCtrl.allLoaded;
    }else
      $scope.commentsCtrl.allLoaded = true;
  }

  $scope.doSearch = function(){
    $scope.resetPage();
    $scope.paginate();
  }

  // ---------- /paginate posts ------

  var addSnippet = function(postObj){
    if(postObj.body)
      postObj.snippet = postObj.body.simpleSnippet();
  }

  $scope.postFeaturedImage = null
  var setPostFeaturedImage = function(hash){
    $scope.postLoaded.postFeaturedImage = $filter('imageLink')({imageHash: hash}, 'large')
  }


  $scope.commentFocused = false;
  $scope.commentFocus = function(){
    $scope.commentFocused = true;
  }
  $scope.commentBlur = function(){
    $scope.commentFocused = false;
  }

  var draftActiveId = null;
  $scope.activateDraft = function(draft){
    draftActiveId = draft.id;
  }

  $scope.isActiveDraft = function(draft){
    return draftActiveId == draft.id
  }

  // -------- toggle all
  
  $scope.toggleAll = function(toggleSelectValue){

    if(toggleSelectValue && $scope.comments){
      $scope.comments.forEach(function(comment, index){
         comment.selected = true;
      }); 
    }else if($scope.comments){
      $scope.comments.forEach(function(comment, index){
          comment.selected = false;
      }); 
    }

  }

  // -------- /toggle all
  
  $scope.getSelectedComments = function(){
    var ret = []
      $scope.comments.forEach(function(comment, index){
        if(comment.selected)
          ret.push(comment.id);
      });
      return ret;
  }

  $scope.getSelectedCommentsObjects = function(){
    var ret = []
      $scope.comments.forEach(function(comment, index){
        if(comment.selected)
          ret.push(comment);
      });
      return ret;
  }

    // -------- /invitation -------
    
    $scope.$mdConstant = $mdConstant.KEY_CODE;
    $scope.separatorKeys = []
    for(prop in $mdConstant.KEY_CODE){
      $scope.separatorKeys.push($mdConstant.KEY_CODE[prop]);
    }

    $scope.commentsToDelete = null;
    $scope.showDeleteCommentsDialog = function(event, commmets){
      $scope.commentsToDelete = commmets;
      $mdDialog.show({
        scope: $scope,        // use parent scope in template
        closeTo: {
          bottom: 1500
        },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'delete-comments-dialog.html',
        parent: angular.element(document.body),
        targetEvent: event,
        clickOutsideToClose:true
        // onComplete: function(){

        // }
      })
    }

    $scope.deleteComments = function(){
      var comments = $scope.commentsToDelete;
      if($scope.commentsToDelete && $scope.commentsToDelete.length > 0){
        trix.deleteComments(comments).success(function(){
          for (var i = 0; i < $scope.comments.length; i++) {
            for (var j = 0; j < comments.length; j++) {
              if($scope.comments[i].id == comments[j].id)
                $scope.comments.splice(i, 1);
            }
          }
          $scope.app.showSuccessToast($filter('translate')('messages.SUCCESS_MSG'))
          $scope.disabled = false;
          $mdDialog.cancel();
        }).error(function(){
          $scope.app.showErrorToast($filter('translate')('messages.ERROR_MSG'))
          $scope.disabled = false;
          $mdDialog.cancel();
        })
      }
    }

    settingsUsersCtrl = $scope;
}])

var settingsUsersCtrl = settingsUsersCtrl