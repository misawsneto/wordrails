// tab controller
app.controller('PostCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar',
										function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar) {

  FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
    return true; // true|false
  };

	$scope.postCtrl = {}
	$scope.postCtrl.editingExisting = false;
	$scope.imageLandscape = true;
	$scope.discardedMedia = null;

	$scope.uploadedImage = null;
	if(!$scope.app.editingPost)
		$scope.app.editingPost = {};

	$scope.$watch('app.hidePostOptions', checkPostToolsWidth)

	$scope.app.showPostToolbar = false;
	$scope.showMediaButtons = false;

	function checkPostToolsWidth () {
		$timeout(function(){
			$("#external-toolbar").width($("#external-toolbar").parent().width())
		}, 50);
	}

	function checkScrollVisible(scrollTop){
		safeApply($scope, function(){
			if(scrollTop == 0)
				$scope.postCtrl.showHeaderShadow = false;
			else
				$scope.postCtrl.showHeaderShadow = true;
		});

		var toolbar_m_t = $("#post-title-box").height() + 15 + $("#post-media-box").height()

		if(scrollTop > toolbar_m_t){
			$("#external-toolbar").addClass('fixed-tools')
			$("#toggle-toolbars").addClass('fixed-tools-button')
		}else{
			$("#external-toolbar").removeClass('fixed-tools')
			$("#toggle-toolbars").removeClass('fixed-tools-button')
		}

		checkPostToolsWidth();

	}

	$scope.closeNewPost = function(){
		$state.go('app.stations')
	}

	function doResize(){
		checkPostToolsWidth();
	}

	var timeoutResize;
	$(window).resize(function(){
		clearTimeout(timeoutResize);
		timeoutResize = setTimeout(doResize, 100);
	})

	// $scope.app.editingPost.body  = null;
	$scope.postCtrl.redactorInit = function(){
		checkScrollVisible($("#post-cell").scrollTop())
		$("#post-cell").scroll(function(){
			checkScrollVisible(this.scrollTop);
		})
	}

	$("#post-placeholder").click(function(){
		$(".redactor-editor").focus();
	})

	$scope.showAdvanced = function(ev) {
		$mdDialog.show({
			controller: DialogController,
			templateUrl: 'tpl/post_more_options.html',
			targetEvent: ev
		})
		.then(function(answer) {
			$scope.alert = 'You said the information was "' + answer + '".';
		}, function() {
			$scope.alert = 'You cancelled the dialog.';
		});
	};

	function DialogController($scope, $mdDialog) {
		$scope.hide = function() {
			$mdDialog.hide();
		};

		$scope.cancel = function() {
			$mdDialog.cancel();
		};

		$scope.answer = function(answer) {
			$mdDialog.hide(answer);
		};
	};

	$scope.$watch('app.editingPost', function(newValue){
	}, true)

	$scope.printPost = function(){
		window.console && console.log($scope.app.editingPost);
	};

	var uploader = $scope.uploader = new FileUploader({
    url: TRIX.baseUrl + "/api/files/contents/simple"
  });
  // CALLBACKS
  uploader.onAfterAddingFile = function(fileItem) {
    $scope.uploadedImage = null;
    uploader.uploadAll();
  };
  uploader.onSuccessItem = function(fileItem, response, status, headers) {
  	if(response.filelink){
      $scope.uploadedImage = response;
      $scope.uploadedImage.filelink = TRIX.baseUrl + $scope.uploadedImage.filelink
      $scope.showMediaButtons = false;
      $scope.checkLandscape();
  	}
  };

  $scope.invertLandscapeSquare = function(){
  	$scope.imageLandscape = !$scope.imageLandscape; 
  	$scope.checkLandscape();
  }

  $scope.checkLandscape = function(){
  	console.log($scope.imageLandscape);
  	if($scope.imageLandscape){
  	  $("#post-media-box").css('background-image', 'url(' + $scope.uploadedImage.filelink + ')');
      $("#post-media-box").css('max-width', "200%");
      $("#post-media-box").css('margin', 0);
      $("#post-media-box").css('height', '500px');
      $("#post-media-box").css('margin', '-20px -50px 0 -50px')
      $("#post-media-box").css('background-position', '50% 30%')
      $("#post-media-box").css('background-size', 'cover')
  	}else{
  	  $("#post-media-box").removeAttr('style')
  	}
  }

  uploader.onErrorItem = function(fileItem, response, status, headers) {
  	//if(status == 413)
    // toastr.error("A imagem não pode ser maior que 6MBs.");
  }

  $scope.clearImage = function(){ 
    $scope.uploadedImage = null;
    uploader.clearQueue();
    uploader.cancelAll()
  }

  uploader.onProgressItem = function(fileItem, progress) {
      window.console && console.log(progress);
  		cfpLoadingBar.start();
      cfpLoadingBar.set(progress/10)
      if(progress == 100)
      	cfpLoadingBar.complete()
  };

  $scope.discardImage = function(){
  }

}]);