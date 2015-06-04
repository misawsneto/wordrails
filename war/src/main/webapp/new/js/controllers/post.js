// tab controller
app.controller('PostCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', 
										function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast){

	// check if user has permisstion to write
  $scope.writableStations = trixService.getWritableStations();
  console.log($scope.writableStations);

  FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
    return true; // true|false
  };

	$scope.postCtrl = {}

	if(!$scope.app.editingPost){
		$scope.app.editingPost = {};
		$scope.app.editingPost.imageLandscape = true;
		$scope.discardedMedia = null;
		$scope.app.editingPost.uploadedImage = null;
		$scope.app.editingPost.showMediaButtons = false;
		$scope.postCtrl.editingExisting = false;
	}else{
		$scope.postCtrl.editingExisting = true;
		if($scope.app.editingPost.externalVideoUrl)
			$scope.videoUrl = $scope.app.editingPost.externalVideoUrl;
		$timeout(function() {
			$scope.invertLandscapeSquare();
			$scope.invertLandscapeSquare();
		}, 50);
	}

	$scope.$watch('app.hidePostOptions', checkPostToolsWidth)
	$scope.app.showPostToolbar = false;

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

	$scope.showTree = false;

	$scope.toggleMediaButtons = function(){
		$scope.app.editingPost.showMediaButtons = !$scope.app.editingPost.showMediaButtons
		if($scope.app.editingPost.showMediaButtons)
			$scope.app.editingPost.showInputVideoUrl = false;
	}

	$scope.$watch('videoUrl', function(newVal){
		if(newVal && newVal.indexOf('youtube') > -1){
			var youtubeCode = newVal.getYoutubeCode();
			if(youtubeCode){
				$scope.app.editingPost.externalVideoUrl = newVal;
			}else{
				$scope.app.editingPost.externalVideoUrl = null;
			}
		}// end if
		else{
			$scope.app.editingPost.externalVideoUrl = null;
		}
	})

	$scope.removeVideo = function(){
		$scope.app.editingPost.externalVideoUrl = null;
		if($scope.videoUrl)
			$scope.videoUrl = $scope.videoUrl.substring(0, $scope.videoUrl.length - 1) + "0";
		$scope.app.editingPost.showInputVideoUrl = false
		$timeout(function() {
			$scope.videoUrl = null
		});
	}

	$scope.toggleVideoUrl = function(){
		$scope.app.editingPost.showInputVideoUrl = !$scope.app.editingPost.showInputVideoUrl;
		if($scope.app.editingPost.showInputVideoUrl)
			$scope.app.editingPost.showMediaButtons = false;

		if($scope.app.editingPost.showInputVideoUrl)
			$("#video-url-input").focus();
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

	$scope.$watch('app.editingPost.topper', function(newVal){
		if(newVal){
			$scope.app.editingPost.topper= '' +$scope.app.editingPost.topper.replace(/ +(?= )/g,'')
			$scope.app.editingPost.topper= $scope.app.editingPost.topper = $scope.app.editingPost.topper.replace(/(?:\r\n|\r|\n)/g, '');
		}
	})

	$scope.$watch('app.editingPost', function(newValue){
		// console.log(newValue);
	}, true)

	$scope.printPost = function(){
		window.console && console.log($scope.app.editingPost);
	};

	var uploader = $scope.uploader = new FileUploader({
    url: TRIX.baseUrl + "/api/files/contents/simple"
  });
  // CALLBACKS
  uploader.onAfterAddingFile = function(fileItem) {
    $scope.app.editingPost.uploadedImage = null;
    uploader.uploadAll();
  };
  uploader.onSuccessItem = function(fileItem, response, status, headers) {
  	if(response.filelink){
      $scope.app.editingPost.uploadedImage = response;
      $scope.app.editingPost.uploadedImage.filelink = TRIX.baseUrl + $scope.app.editingPost.uploadedImage.filelink
      $scope.app.editingPost.showMediaButtons = false;
      $scope.checkLandscape();
      $("#image-config").removeClass("hide");
      $mdToast.hide();
  	}
  };

  $scope.invertLandscapeSquare = function(){
  	$scope.app.editingPost.imageLandscape = !$scope.app.editingPost.imageLandscape; 
  	$scope.checkLandscape();
  }

  $scope.checkLandscape = function(){
  	if($scope.app.editingPost.uploadedImage){
  	  $("#post-media-box").css('background-image', 'url(' + $scope.app.editingPost.uploadedImage.filelink + ')');
  	}else{
  	  $("#post-media-box").removeAttr('style')
  	}
  }

  uploader.onErrorItem = function(fileItem, response, status, headers) {
  	if(status == 413)
    	$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
  }

  $scope.clearImage = function(){ 
    $("#image-config").addClass("hide");
    $scope.app.editingPost.uploadedImage = null;
    uploader.clearQueue();
    uploader.cancelAll()
    $scope.checkLandscape();
  }

  uploader.onProgressItem = function(fileItem, progress) {
  		cfpLoadingBar.start();
      cfpLoadingBar.set(progress/10)
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

	trix.getTermTree($scope.app.currentStation.defaultPerspectiveId).success(function(response){
		$scope.termTree = response;
	});

	$scope.$watch('app.editingPost.title', function(newVal){
		$scope.app.editingPost.slug = newVal ? newVal.toSlug() : '';
	})

	$scope.$watch('app.editingPost.slug', function(newVal){
		$scope.app.editingPost.slug = newVal ? newVal.toSlug() : '';
	})

	/*function uncheckTerms(terms){
		terms && terms.forEach(function(term, index){
			term.checked = false

			if(term.children && term.children.length > 0)
				uncheckTerms(term.children)
		});
	}*/

	$scope.app.editingPost.date = new Date();
	$timeout(function() {
		$scope.app.editingPost.date = new Date();
	}, 1000);

	// $scope.$watch('termTree', function(n,o){
	// 	console.log(n);
	// }, true)

	function isTermSelected(terms){
		var selected = false;
		terms && terms.forEach(function(term, index){
			if(term.checked || isTermSelected(term.children)){
				selected = true;
				return;
			}
		});
		return selected;
	}	

	// --------- post time info

	 $scope.app.editingPost.today = function() {
      $scope.app.editingPost.date = new Date();
    };
    $scope.app.editingPost.today();

    $scope.app.editingPost.clear = function () {
      $scope.app.editingPost.date = null;
    };

    // Disable weekend selection
    $scope.app.editingPost.disabled = function(date, mode) {
      return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
    };

    $scope.app.editingPost.toggleMin = function() {
      $scope.app.editingPost.minDate = $scope.app.editingPost.minDate ? null : new Date();
    };
    $scope.app.editingPost.toggleMin();

    $scope.app.editingPost.open = function($event) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope.app.editingPost.opened = true;
    };

    $scope.app.editingPost.dateOptions = {
      formatYear: 'yy',
      startingDay: 0,
      class: 'datepicker',
      showWeeks: false
    };

    $scope.app.editingPost.initDate = new Date();
    $scope.app.editingPost.formats = ['dd MMMM yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.app.editingPost.format = $scope.app.editingPost.formats[0];

    $scope.app.editingPost.showTimepicker = false;
    $scope.app.editingPost.mytime = $scope.app.editingPost.date

    $scope.app.editingPost.hstep = 1;
    $scope.app.editingPost.mstep = 15;

    $scope.app.editingPost.options = {
      hstep: [1, 2, 3],
      mstep: [1, 5, 10, 15, 25, 30]
    };

    $scope.app.editingPost.ismeridian = false;
    $scope.app.editingPost.toggleMode = function() {
      $scope.app.editingPost.ismeridian = ! $scope.app.editingPost.ismeridian;
    };

    $scope.app.editingPost.changed = function () {
      window.console && console.log('Time changed to: ' + $scope.app.editingPost.mytime);
    };

    $scope.app.editingPost.clear = function() {
      $scope.app.editingPost.mytime = null;
    };

	// --------- /post time info

	// --------- publish post

	$scope.publishPost = function(ev){
		if(isTermSelected($scope.termTree)){
			createPost()
		}else{
			//$scope.showMoreOptions(ev);
			$scope.app.hidePostOptions = false;
			$scope.app.showInfoToast('Escolha uma categoria.')
		}
	}

	$scope.showMoreOptions = function(ev){
		// show term alert
		$mdDialog.show({
			controller: MoreOptionsController,
			templateUrl: 'tpl/post_more_options.html',
			targetEvent: ev,
			onComplete: function(){
					
			}
		})
		.then(function(answer) {
			//$scope.alert = 'You said the information was "' + answer + '".';
		}, function() {
			//$scope.alert = 'You cancelled the dialog.';
		});
	}

	function MoreOptionsController(scope, $mdDialog) {
		scope.termTree = $scope.termTree;
		scope.app = $scope.app;
		scope.hide = function() {
			$mdDialog.hide();
		};

		scope.cancel = function() {
			$mdDialog.cancel();
		};

		scope.publish = function() {
			if(isTermSelected($scope.termTree)){
				createPost($mdDialog)
			}else{
				// show alert term message
			}
			//$mdDialog.hide();
		};
	};

	function getTermList(terms, retTerms){
		if(!retTerms)
			retTerms = []

		terms && terms.forEach(function(term, index){
				if(term.checked)
					retTerms.push(term)
				var ts = getTermList(term.children)
				ts.forEach(function(t){
					retTerms.push(t)
				});
		});
		return retTerms;
	}	

	function createPost(){
		var post = {};
		post.title = $scope.app.editingPost.title
		post.body = $scope.app.editingPost.body
		if($scope.app.editingPost.externalVideoUrl)
			post.externalVideoUrl = $scope.app.editingPost.externalVideoUrl
		if($scope.app.editingPost.notify)
			post.notify = true

		if(!post.title || post.title.trim() === "")
			$scope.app.showErrorToast('Título inválido.');
		else if(!post.body || post.body.trim() === "")
			$scope.app.showErrorToast('Texto inválido.');
		else if((!post.body || post.body.trim() === "") && (!post.title || post.title.trim() === ""))
			$scope.app.showErrorToast('Título e texto inválidos.');
		else{
			// post is ok to be created
			var termList = getTermList($scope.termTree);
			var termUris = []
			termList.forEach(function(term){
	      termUris.push(TRIX.baseUrl + "/api/terms/" + term.id);
	    })

	    post.terms = termUris;
	    post.station = extractSelf($scope.app.currentStation)
	    post.author = extractSelf($scope.app.getLoggedPerson())

			trix.postPost(post).success(function(){
				$scope.app.showSuccessToast('História publicada.');
			})
		} // end of final else
	}// end of createPost()


}]);