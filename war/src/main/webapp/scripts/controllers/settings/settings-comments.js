app.controller('SettingsCommentsCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdDialog', '$mdToast', '$filter', '$translate', '$mdConstant',
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


    // ------------- /user edit image -------------

    $scope.commentsCtrl = {
      'page': 0,
      'allLoaded': false
    }

    $scope.paginate = function(){
    if(!$scope.loading && !$scope.commentsCtrl.allLoaded){
      $scope.loading = true;

      trix.searchComments($scope.searchQuery, null, null, null, null, null, $scope.commentsCtrl.page, 20, '-date', ['author', 'post']).success(function(response){
        handleSuccess(response);
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
  
  $scope.getSelectedComments = function(type){
    var ret = []
      $scope.comments.forEach(function(comment, index){
        if(comment.selected)
          ret.push(comment.id);
      });
      return ret;
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