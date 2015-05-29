// tab controller
app.controller('PostCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', function($scope, $log, $timeout, $mdDialog, $state, FileUploader, TRIX) {

  FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
    return true; // true|false
  };

	$scope.postCtrl = {}
	$scope.postCtrl.editingExisting = false;

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
      $scope.uploadedImage = response;
  };
  uploader.onErrorItem = function(fileItem, response, status, headers) {
    toastr.error("A imagem não pode ser maior que 4MBs.");
  };
  $scope.clearImage = function(){ 
    $scope.uploadedImage = null;
    uploader.clearQueue();
    uploader.cancelAll()
  }

  $scope.openImageUpload = function(){
  	/*safeApply($scope, function(){
  		$("#image-upload").click();
  	})*/
  }

}]);