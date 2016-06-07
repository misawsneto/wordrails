app.controller('SettingsPostCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage', 'ngJcropConfig', '$sce',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage, ngJcropConfig, $sce){

	// ---------- scope initialization
	var lang = $translate.use();

	$scope.tags = [];

	$scope.landscape = true;
	$scope.automaticSave = true;

	$timeout(function(){
		$scope.openImageOptions = false;
	});

	ngJcropConfig.default.jcrop ={
	  aspectRatio: null,
	  bgColor: 'black',
	  bgOpacity: .4,
	  boxWidth: 400, 
	  boxHeight: 400
	};

	$scope.stations = angular.copy($scope.app.stations);

	// Must be [x, y, x2, y2, w, h]
   $scope.app.cropSelection = [100, 100, 200, 200];

	$scope.froalaOptions = {
		toolbarInline: false,
	      heightMin: 200,
	      language: (lang == 'en' ? 'en_gb' : lang == 'pt' ? 'pt_br' : null),
		// Set the image upload parameter.
        imageUploadParam: 'contents',

        imageCaption: true,

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
        toolbarSticky: false
    }


    $scope.showFeaturedMediaSelector = function(){
    	return !$scope.postFeaturedImage && !$scope.useVideo && !$scope.useUploadedVideo && !$scope.useUploadedAudio;
    }

	$timeout(function(){
		$(".fr-box a").each(function(){
			if($(this).html() === 'Unlicensed Froala Editor')
				$(this).remove();
		});
	});

	// ---------- /scope initialization

	// sidenav toggle
	$scope.toggleOptions = buildToggler('more-options');

	function buildToggler(navID) {
      return function() {
        $mdSidenav(navID)
          .toggle()
      }
    }
    // -----------

    // if($state.params.slug)
    // 	$scope.postState = 'Draft';

	// ---------- time config ---------- 
	function addMinutes(date, minutes) {
	    date = new Date(date.getTime() + minutes*60000);
	    return date;
	}

	function addDays(date, days) {
	    date.setDate(date.getDate() + days);
	    return date;
	}


	// --- post date
	$scope.postDateUpdateble = true;
	$scope.resetPostDate = function(){
		if(!$scope.postDateUpdateble)
			return;
		$scope.postDate = new Date();
		$scope.postDate.setSeconds(null);
		$scope.postDate.setMilliseconds(null);
	}

	$scope.changePostDate = function(date){
		$scope.postDateUpdateble = false;
	}
	// --- /post date
	
	// --- post schedule
	$scope.postScheduleUpdateble = true;
	$scope.resetScheduleDate = function(){
		if(!$scope.postScheduleUpdateble)
			return;
		$scope.postScheduleDate = new Date();
		$scope.postScheduleDate = addMinutes($scope.postScheduleDate, 15);
		$scope.postScheduleDate.setSeconds(null);
		$scope.postScheduleDate.setMilliseconds(null);
		$scope.postScheduleUpdateble = true;
	}
	// --- /post schedule

	// --- post delete
	$scope.postDeleteUpdateble = true;
	$scope.resetDeleteDate = function(){
		if(!$scope.postDeleteUpdateble)
			return;
		$scope.postDeleteDate = new Date();
		$scope.postDeleteDate = addDays($scope.postDeleteDate, 1);
		$scope.postDeleteDate.setSeconds(null);
		$scope.postDeleteDate.setMilliseconds(null);
		$scope.postDeleteUpdateble = true;
	}
	// --- /post delete

	// ---------- /time config ---------- 
	
	
	// --- slug
	$scope.customizedLink = {
		slug: ""
	}

	// --- slug

	// --- watch post change

	// see app.ctrl.js	
	$scope.app.postObjectChanged = false;

	// --- /watch post change
	

	// --- clear post
	$scope.showClearPostDialog = function(event){
		// show term alert
		
		$mdDialog.show({
			scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
          	preserveScope: true, // do not forget this if use parent scope
			controller: $scope.app.defaultDialog,
			templateUrl: 'clear-post-dialog.html',
			parent: angular.element(document.body),
			targetEvent: event,
			clickOutsideToClose:true
			// onComplete: function(){

			// }
		})
	}

	$scope.app.clearPostContent = function(){
		if($scope.app.editingPost){
			$scope.app.editingPost.title = '';
			$scope.app.editingPost.body = '';
		}
		$scope.tags = [];
		$scope.featuredImage = $scope.featuredAudio = $scope.featuredVideo = $scope.postFeaturedImage = $scope.postFeaturedAudio = $scope.postFeaturedVideo = null;
		$mdDialog.cancel();
	}

	$scope.app.clearAndResetPost = function(){
		$scope.app.clearPostContent();
		$timeout(function(){
			$scope.app.editingPost = null;
			$scope.app.postObjectChanged = false;
			$state.transitionTo('app.post', {'id': null}, {reload: false, inherit: false, notify: false});
			$scope.app.showInfoToast($filter('translate')('settings.post.messages.NEW_PUBLICATION_INFO'))
		}, 300)
	}

	// /clear post
	
	// --- post info
	$scope.showPostInfoDialog = function(event){

		$mdDialog.show({
			scope: $scope,        // use parent scope in template
          	preserveScope: true, // do not forget this if use parent scope
			controller: $scope.app.defaultDialog,
			template: 	postInfoTemplate,
			parent: angular.element(document.body),
			// targetEvent: event,
			clickOutsideToClose:false
			// onComplete: function(){

			// }
		})
	}

	$scope.app.checkState = function(state){
		// if(!$scope.app.editingPost)
		// 	return null;

		state = state ? state : $scope.app.editingPost ? $scope.app.editingPost.state : null;
		if(!state)
			return 2;
		if(state == "PUBLISHED"){
			return 1;
		}else if(state == "DRAFT"){
			return 2;
		}else if(state == "SCHEDULED"){
			return 3;
		}else if(state == "TRASH"){
			return 4;
		}else{
			return 5;
		}
	}

	$scope.app.getStateText = function(state){
		// if(!$scope.app.editingPost)
		// 	return null;

		state = state ? state : $scope.app.editingPost ? $scope.app.editingPost.state : null;
		if(!state)
			return $filter('translate')('settings.post.states.DRAFT');
		if(state == "PUBLISHED"){
			return $filter('translate')('settings.post.states.PUBLISHED');
		}else if(state == "DRAFT"){
			return $filter('translate')('settings.post.states.DRAFT');
		}else if(state == "SCHEDULED"){
			return $filter('translate')('settings.post.states.SCHEDULED');
		}else if(state == "TRASH"){
			return $filter('translate')('settings.post.states.TRASH');
		}else{
			return " - ";
		}
	}

	$scope.app.countBodyCharacters = function(){
		return $scope.app.editingPost.body.stripHtml().length
	}

	$scope.app.countTitleCharacters = function(){
		return $scope.app.editingPost.title.stripHtml().length
	}

	$scope.app.countTitleWords = function(){
		return countWords($scope.app.editingPost.title.stripHtml())
	}

	$scope.app.countBodyWords = function(){
		return countWords($scope.app.editingPost.body.stripHtml())
	}

	function countWords(str) {
		var regex = /\s+/gi;
		return str.trim().replace(regex, ' ').split(' ').length;
	}

	// --- /post info
	
	// ------------------- image uploader -------------

	var uploader = $scope.uploader = new FileUploader({
		url: TRIX.baseUrl + "/api/images/upload?imageType=POST"
	});

	$scope.uploadedImage = null;
	uploader.onAfterAddingFile = function(fileItem) {
		$scope.uploadedImage = null;
		uploader.uploadAll();
	};

	uploader.onSuccessItem = function(fileItem, response, status, headers) {
		if(response.filelink){
			$scope.featuredImage = $scope.uploadedImage = response;
			setPostFeaturedImage($scope.uploadedImage.hash)
			$mdToast.hide();
		}
	};

	uploader.onErrorItem = function(fileItem, response, status, headers) {
		if(status == 413)
			$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
		else
			$scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
	}

	$scope.clearImage = function(){ 
		$scope.uploadedImage = null;
		uploader.clearQueue();
		uploader.cancelAll()
		$scope.checkLandscape();
		$scope.postCtrl.imageHasChanged = true;
	}

	uploader.onProgressItem = function(fileItem, progress) {
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

	$scope.postFeaturedImage = null
	var setPostFeaturedImage = function(hash){
		$scope.postFeaturedImage = $filter('imageLink')({imageHash: hash}, 'large')
	}


	// ------------------- end of image uploader -------------
	
	// --- image settings
	
	$scope.toggleLandscape = function(){
		$scope.landscape = !$scope.landscape
		$localStorage.landscape = $scope.landscape;
	}

	$scope.removeFeaturedImage = function(){
		$scope.featuredImage = null;
		$scope.postFeaturedImage = null;
	}

	$scope.loadImageConfigFromStorage = function(){
		if ( angular.isDefined($localStorage.landscape) )
	    $scope.landscape = $localStorage.landscape;
	}

	$scope.showImageUploadDialog = function(){
		$("#image-buttom").trigger('click');
	}

	// load imageConfig
	$scope.loadImageConfigFromStorage();

	// --- /image settings

	// --- image crop
	$scope.showImageCropDialog = function(event){
		// show term alert
		
		$scope.app.postFeaturedImage = $scope.postFeaturedImage;

		$mdDialog.show({
			scope: $scope,        // use parent scope in template
          	preserveScope: true, // do not forget this if use parent scope
			controller: $scope.app.defaultDialog,
			template: 	postCropTemplate,
			parent: angular.element(document.body)
		})
	}

	// --- /image crop
	// --- image focuspoint

	$scope.app.imgToolsProps = {
		width: 0,
		x: 50,
		y: 50,
		onMouseDown: function (e) {
      $scope.update(e);
      $scope.app.imgToolsProps.dragging = true;
    },
    onMouseMove: function (e) {
      if ($scope.app.imgToolsProps.dragging === true) {
        $scope.update(e);
      }
    },
		onMouseUp: function (e) {
      e.preventDefault();
      $scope.app.imgToolsProps.dragging = false;
    }
	}

	$scope.update = function (e) {
    e.preventDefault();
    var offset = $scope.offset(e.target);
    $scope.app.imgToolsProps.x = Math.round(((e.pageX - offset.left) / e.target.clientWidth) * 100);
    $scope.app.imgToolsProps.y = Math.round(((e.pageY - offset.top) / e.target.clientHeight) * 100);
  };
  
  $scope.offset = function (elm) {
    try { return elm.offset(); } catch (e) { }
    var body = document.documentElement || document.body;
    return {
        left: elm.getBoundingClientRect().left + (window.pageXOffset || body.scrollLeft),
        top: elm.getBoundingClientRect().top + (window.pageYOffset || body.scrollTop)
    };
  };

	var setImgToolsImageWidth = function(){
		var featuredImage = $('#img-stub');
		if(featuredImage.length)
		var w = $(featuredImage).width();
		var h = $(featuredImage).height();

		var isLandscape = true
		if(w > h)
			isLandscape = true;
		else
			isLandscape = false;

		if(isLandscape){
			$scope.app.imgToolsProps.height = (h * 400) / w
			$scope.app.imgToolsProps.width = 400;
		}else{
			$scope.app.imgToolsProps.width = (w * 400) / h
			$scope.app.imgToolsProps.height = 400;
		}
	}

	  var intervalPromise;
	  intervalPromise = $interval(function(){
	   setImgToolsImageWidth();
	  }, 500);

	  $scope.$on('$destroy',function(){
	      if(intervalPromise)
	          $interval.cancel(intervalPromise);   
	  });

	
	$scope.showImageFocuspointDialog = function(event){
		// show term alert
		
		$scope.app.postFeaturedImage = $scope.postFeaturedImage;

		$mdDialog.show({
			scope: $scope,        // use parent scope in template
          	preserveScope: true, // do not forget this if use parent scope
			controller: $scope.app.defaultDialog,
			template: 	postFocuspointTemplate,
			parent: angular.element(document.body),
			targetEvent: event,
			clickOutsideToClose:false
			// onComplete: function(){

			// }
		})
	}
	// --- /image focuspoint
	
	var draftAutoSaveCheck = function(){
	}

	// --- auto save
	

	// --- /auto save

	// --- geolocation
	$scope.showGeoNotificationDialog = function(event){
		// show term alert
		
		$scope.app.postFeaturedImage = $scope.postFeaturedImage;

		$mdDialog.show({
			scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
          	preserveScope: true, // do not forget this if use parent scope
			controller: $scope.app.defaultDialog,
			templateUrl: 	'geolocation-dialog.html',
			parent: angular.element(document.body),
			targetEvent: event,
			clickOutsideToClose:true
			// onComplete: function(){

			// }
		})
	}
	// --- /geolocation

	// --- save post as published
	$scope.savePostAsPublished = function(){

	}
	// --- /save post as published

	// --- save post as draft
	$scope.savePostAsDraft = function(){
		
	}
	// --- save post as draft

	// --- save post as scheduled
	$scope.savePostAsScheduled = function(){
		
	}
	// --- /save post as scheduled

	// --- save post as trash
	$scope.savePostAsTrash = function(){
		
	}
	// --- /save post as trash

	// --- save post as trash
	$scope.saveVersion = function(){
		
	}
	// --- /save post as trash

	// --- resize zoom class workaround
	
	$scope.zoomActive = true;
	$timeout(function(){
		$scope.zoomActive = false;
	}, 4000);

	// --- /resize zoom class workaround

	// ------------------- update term tree ---------------

	$scope.stations.forEach(function(station){
		trix.getTermTree(null, station.categoriesTaxonomyId).success(function(response){
			station.termTree = response;
			if($scope.app.editingPost)
				$scope.app.selectTerms(station.termTree, $scope.app.editingPost.terms)
		});
	})

	$scope.selectedStation = null;
	$scope.$watch('selectedStation', function(station){
		if(station && station.termTree)
			$scope.app.selectTerms(station.termTree, $scope.app.editingPost.terms)
	})

	// function updateTermTree(){
	// 	if($scope.selectedStation)
	// 		trix.getTermTree(null, $scope.selectedStation.categoriesTaxonomyId).success(function(response){
	// 			$scope.termTree = response;
	// 			if($scope.app.editingPost)
	// 				$scope.app.selectTerms($scope.termTree, $scope.app.editingPost.terms)
	// 		});
	// }

	$scope.showVideoDialog = function(){
		$mdDialog.show({
			scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
          	preserveScope: true, // do not forget this if use parent scope
			controller: $scope.app.defaultDialog,
			templateUrl: 'video-dialog.html',
			parent: angular.element(document.body),
			targetEvent: event,
			clickOutsideToClose:true
			// onComplete: function(){

			// }
		});
	}

	$scope.showAudioDialog = function(){
		$mdDialog.show({
			scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
          	preserveScope: true, // do not forget this if use parent scope
			controller: $scope.app.defaultDialog,
			templateUrl: 'audio-dialog.html',
			parent: angular.element(document.body),
			targetEvent: event,
			clickOutsideToClose:true
			// onComplete: function(){

			// }
		});
	}

	$scope.videos = {
		// sources: [
		// 	{src: $sce.trustAsResourceUrl("http://d3a3w0au60g0o7.cloudfront.net/demo/videos/7145a35b9391bb8d568c24ef8df59d6a"), type: "video/mp4"}
		// ],
		theme: "/libs/angular/videogular-themes-default/videogular.min.css",
		plugins: {
            controls: {
                autoHide: true,
                autoHideTime: 5000
            }
        }
	}

	$scope.audios = {
		// sources: [
		// 	{src: $sce.trustAsResourceUrl("http://d3a3w0au60g0o7.cloudfront.net/demo/videos/7145a35b9391bb8d568c24ef8df59d6a"), type: "video/mp4"}
		// ],
		theme: "/libs/angular/videogular-themes-default/videogular.min.css",
		plugins: {
            controls: {
                autoHide: false
            }
        }
	}

	$scope.$watch('uploadedVideo', function(newVal, oldVal){
		if(newVal){
			$scope.videos.sources = [
				{src: $sce.trustAsResourceUrl(newVal.filelink), type: "video/mp4"}
			]
		}else{
			$scope.videos.sources = null;
		}
	});

	// $scope.audioProcessing = $scope.videoProcessing = true;

	$scope.$watch('uploadedAudio', function(newVal, oldVal){
		if(newVal){
			$scope.audios.sources = [
				{src: $sce.trustAsResourceUrl(newVal.filelink), type: "audio/mpeg"}
			]
		}else{
			$scope.audios.sources = null;
		}
	});

	// ------------------- end of update term tree ---------------

	$scope.recordAudio = false; $scope.uploadAudio = true;
	$scope.insertVideolink = true;

	// ------------- video embeded ----------------

	$scope.useVideo = false;
	$scope.app.videoUrl = null;

	$scope.videoSelected = function(videoId, provider){
		if (videoId) 
			$scope.validVideoUrl = true;
		else
			$scope.validVideoUrl = false;
	}

	$scope.insertVideo = function(){
		$scope.useVideo = true;
		$mdDialog.cancel();
	}

	$scope.insertUploadedVideo = function(){
		$scope.useUploadedVideo = true;
		$mdDialog.cancel();
	}

	$scope.removeVideo = function(){
		$scope.useVideo = false;
	}

	$scope.removeUploadedVideo = function(){
		$scope.useUploadedVideo = false;
	}

	// ------------- /video embeded ----------------


	// ------------- andio embeded ----------------


	$scope.insertUploadedAudio = function(){
		$scope.useUploadedAudio = true;
		$mdDialog.cancel();
	}

	$scope.removeUploadedAudio = function(){
		$scope.useUploadedAudio = false;
	}

	// ------------- /audio embeded ----------------
	

	// ------------- audio uploaded --------------
	
	var auploader = $scope.auploader = new FileUploader({
		url: TRIX.baseUrl + "/api/files/upload/audio"
	});

	$scope.uploadedAudio = null;
	auploader.onAfterAddingFile = function(fileItem) {
		$scope.uploadedAudio = null;
		auploader.uploadAll();
	};

	auploader.onSuccessItem = function(fileItem, response, status, headers) {
		if(response.filelink){
			$scope.featuredAudio = $scope.uploadedAudio = response;
			setPostFeaturedAudio($scope.uploadedAudio.hash)
			$mdToast.hide();
		}
		$scope.audioProcessing = false;
	};

	auploader.onErrorItem = function(fileItem, response, status, headers) {
		if(status == 413)
			$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
		else
			$scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
		$scope.audioProcessing = false;
	}

	$scope.clearImage = function(){ 
		$scope.uploadedAudio = null;
		auploader.clearQueue();
		auploader.cancelAll()
	}

	auploader.onProgressItem = function(fileItem, progress) {
		$scope.audioProcessing = true;
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

	$scope.postFeaturedAudio = null
	var setPostFeaturedAudio = function(hash){
		$scope.postFeaturedAudio = $filter('fileLink')(hash)
	}

	// ------------- /audio uploaded -------------
	
	// ------------- video uploaded --------------
	
	var vuploader = $scope.vuploader = new FileUploader({
		url: TRIX.baseUrl + "/api/files/upload/video"
	});

	$scope.uploadedVideo = null;
	vuploader.onAfterAddingFile = function(fileItem) {
		$scope.uploadedVideo = null;
		vuploader.uploadAll();
	};

	vuploader.onSuccessItem = function(fileItem, response, status, headers) {
		if(response.filelink){
			$scope.featuredVideo = $scope.uploadedVideo = response;
			setPostFeaturedVideo($scope.uploadedVideo.hash)
			$mdToast.hide();
		}
		$scope.videoProcessing = false;
	};

	vuploader.onErrorItem = function(fileItem, response, status, headers) {
		if(status == 413)
			$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
		else
			$scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
		$scope.videoProcessing = false;
	}

	$scope.clearImage = function(){ 
		$scope.uploadedVideo = null;
		vuploader.clearQueue();
		vuploader.cancelAll()
	}

	vuploader.onProgressItem = function(fileItem, progress) {
		$scope.videoProcessing = true;
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

	$scope.postFeaturedVideo = null
	var setPostFeaturedVideo = function(hash){
		$scope.postFeaturedVideo = $filter('fileLink')(hash)
	}

	// ------------- /video uploaded -------------

	// -------- load post
	
	if($state.params.id){
			$scope.app.showLoadingProgress();
	 	trix.getPost($state.params.id, "postProjection").success(function(response){
			$scope.app.editingPost = response;
			$scope.loadPostData();
			$mdDialog.cancel();
			loadVersions($state.params.id);
		}).error(function(){
			$scope.app.showErrorToast("error loading post")
			$mdDialog.cancel();
		})
	}

	var loadVersions = function(postId){
		trix.getPostVersions(postId).success(function(versions){
			$scope.revisions = formatVersions(versions)
		}).error(function(){

		})
	}

	// -------- /load post

	// -------- autoSave
	var AUTO_SAVE = null;
	$scope.$watch('app.editingPost', function(newVal, oldVal) {
	    if(oldVal && (('title' in oldVal) || ('body' in oldVal))){
	      // post has been edited

	      var newBody = newVal.body ? newVal.body.stripHtml().replace(/(\r\n|\n|\r)/gm,"") : null;
	      var oldBody = oldVal.body ? oldVal.body.stripHtml().replace(/(\r\n|\n|\r)/gm,"") : null;

	      if(newVal && (newVal.title !== oldVal.title || newBody !== oldBody)){
	        
	        if($scope.selectedStation && $scope.app.getTermList($scope.selectedStation.termTree)){ // save automatic
	        	// window.console && console.log($scope..selectedStation, $scope.app.getTermList($scope.selectedStation.termTree))
	        	if(AUTO_SAVE){
	        		$timeout.cancel(AUTO_SAVE);
	        	} 
	        	if($scope.app.checkState() == 2 && $scope.automaticSave){
	        		AUTO_SAVE = $timeout(function(){
	        			$scope.saveAsDraft(null, true)
	        		},10000)
	        	}
	        }
	      }
	    }
	  }, true);

	$scope.restoreRevision = function(post){
		$scope.app.showLoadingProgress();
		$scope.app.editingPost = post;
		$scope.loadPostData(true);
		$timeout(function(){
			$mdDialog.cancel();
		},2000)
	}


	// -------- /autoSave

	$scope.getStationFromPost = function(){
		if($scope.app.editingPost.station){
			$scope.stations.forEach(function(station){
				station.id =station.id ? station.id : station.stationId;
				if(station.id == $scope.app.editingPost.station.id){
					$scope.selectedStation = station;
				}
			})
		}
	};

	$scope.loadPostData = function(lockContent){
		$scope.getStationFromPost();
		if($scope.app.editingPost.categories && $scope.app.editingPost.categories.length)
			$scope.app.editingPost.terms = $scope.app.editingPost.categories;
		if($scope.app.editingPost.terms)
			$scope.app.editingPost.terms.forEach(function(term){
				term.checked = true;
			})

		var hash = $scope.app.editingPost.featuredImageHash ? $scope.app.editingPost.featuredImageHash : $scope.app.editingPost.imageHash ? $scope.app.editingPost.imageHash : $scope.app.editingPost.featuredImage ? $scope.app.editingPost.featuredImage.originalHash : null;
		setPostFeaturedImage(hash)
		$scope.featuredImage = $scope.app.editingPost.featuredImage
		$scope.landscape = $scope.app.editingPost.imageLandscape;
		$scope.customizedLink.slug = $scope.app.editingPost.slug;

		$scope.useHeading = $scope.app.editingPost.topper ? true:false
		$scope.useSubheading = $scope.app.editingPost.subheading ? true:false
		$scope.tags = angular.copy($scope.app.editingPost.tags);
		if(!lockContent)
			$timeout(function(){
				$scope.app.postObjectChanged = false;
			}, 1000)
	}

	// --- mock and test
	var mockPostLoad = function(){
		$scope.app.editingPost = createPostStub();
		$scope.revisions = createVersions();

		$scope.loadPostData();

		// test
		$scope.app.editingPost.id = null;
		
		var post = preparePost($scope.app.editingPost);
		$log.info(post)

		// $scope.postPost($scope.app.editingPost);
	}

	var preparePost = function(originalPost){
		var post = angular.copy(originalPost)
		post.id = post.id ? post.id : post.postId;
		post.terms = $scope.app.getTermList($scope.selectedStation.termTree);
		post.terms = $scope.app.getTermUris(post.terms);
		post.station = $scope.selectedStation;
		post.tags = $scope.tags;
		if(!post.state)
			post.state = 'PUBLISHED';
		post.imageLandscape = $scope.landscape;
		if(!post.author)
			post.author = PersonDto.getSelf($scope.app.person);
		else if(post.author.id || post.author.authorId)
			post.author = ImageDto.getSelf(post.author);

		if($scope.featuredImage)
			post.featuredImage = ImageDto.getSelf($scope.featuredImage);
		else
			post.featuredImage = null;

		if(post.station)
			post.station = StationDto.getSelf(post.station);

		return post;
		// return {"author":"http://demo.xarx.rocks/api/persons/51","body":"...","bookmarksCount":0,"commentsCount":0,"date":1462880877015,"imageLandscape":false,"lat":-8.04325205,"lng":-34.94544256,"notify":false,"readTime":0,"readsCount":0,"recommendsCount":0,"state":"PUBLISHED","station":"http://demo.xarx.rocks/api/stations/11","subheading":"","title":"Abcd55","topper":""}
	}

	$scope.saveAsDraft = function(event, confirm){
		if(confirm || !$scope.app.editingPost.id){
			var post = angular.copy($scope.app.editingPost)
			post.state = 'DRAFT';
			if(post.id)
				$scope.putPost(post);
			else
				$scope.postPost(post);
		}else{
			saveAsDraftConfirmDialog(event);
		}
	}

	$scope.publishPost = function(){
		var post = angular.copy($scope.app.editingPost)
		post.state = 'PUBLISHED';
		if(post.id){
			$scope.putPost(post);
		}else
			$scope.postPost(post);
	}

	var saveAsDraftConfirmDialog = function(event){
		$mdDialog.show({
			scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
          	preserveScope: true, // do not forget this if use parent scope
			controller: $scope.app.defaultDialog,
			templateUrl: 'save-to-draft-confirm-dialog.html',
			parent: angular.element(document.body),
			targetEvent: event,
			clickOutsideToClose:true
			// onComplete: function(){

			// }
		})
	}

	$scope.putPost = function(originalPost){
		var post = preparePost(originalPost);
		trix.putPost(post).success(function(response){
			$scope.app.showSuccessToast($filter('translate')('settings.post.UPDATE_SUCCESS'));
			$scope.app.editingPost.id = response;
			$scope.app.editingPost.state = post.state;
			// $scope.loadPostData();
			$scope.app.postObjectChanged = false;
			// $state.transitionTo('app.post', {'id': $scope.app.editingPost.id}, {reload: false, inherit: false, notify: false});
			$mdDialog.cancel();
			loadVersions($scope.app.editingPost.id);
		}).error(function(){
			$scope.app.showErrorToast($filter('translate')('settings.post.PUBLISH_ERROR'));
			$mdDialog.cancel();
		})
	}

	$scope.postPost = function(originalPost){
		var post = preparePost(originalPost);
		trix.postPost(post).success(function(response){
			if($scope.app.checkState == 2)
				$scope.app.showSuccessToast($filter('translate')('settings.post.UPDATE_SUCCESS'));
			else
				$scope.app.showSuccessToast($filter('translate')('settings.post.PUBLISH_SUCCESS'));

			$scope.app.editingPost = response;
			$scope.loadPostData();
			$scope.app.postObjectChanged = false;
			$state.transitionTo('app.post', {'id': $scope.app.editingPost.id}, {reload: false, inherit: false, notify: false});
			$mdDialog.cancel();
		}).error(function(){
			$scope.app.showErrorToast($filter('translate')('settings.post.PUBLISH_ERROR'));
			$mdDialog.cancel();
		})
	}

	var test = function(){
		mockPostLoad();
	}

	$timeout(function(){
		// test();
	}, 1000);

	settingsPostCtrl = $scope;
	// --- /mock & test
}]);

var settingsPostCtrl = null;

// --- TEST
/**
 * Post Stub
 * @return {Post} [mock]
 */
function createPostStub(){
	return {"id":6017,"state":"PUBLISHED","date":1452788863000,"scheduledDate":null,"slug":"foi-o-amor-pela-rainha-do-sul-que-tramou-el-chapo-guzman","station":{"updatedAt":1460902482000,"createdAt":null,"name":"O MUNDO","id":11,"stationSlug":"o-mundo","writable":false,"main":true,"visibility":"UNRESTRICTED","allowComments":true,"allowSocialShare":true,"allowWritersToNotify":true,"allowWritersToAddSponsors":false,"backgroundColor":"#ffffff","navbarColor":"#ffffff","primaryColor":"#5C78B0","personsStationRoles":[{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1053,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1045,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1043,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1046,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1050,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1052,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1044,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1047,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1055,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":null,"createdAt":null,"id":76,"editor":false,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1040,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1049,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047906000,"createdAt":1458047906000,"id":1057,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1054,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1048,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1056,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1042,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1041,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":1458047876000,"id":1051,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047906000,"createdAt":1458047906000,"id":1058,"editor":true,"writer":true,"admin":true,"stationId":11},{"updatedAt":1458047876000,"createdAt":null,"id":83,"editor":true,"writer":true,"admin":true,"stationId":11}],"categoriesTaxonomyId":140,"tagsTaxonomyId":183,"postsTitleSize":0,"topper":true,"subheading":true,"sponsored":false,"showAuthorSocialData":true,"showAuthorData":false,"defaultPerspectiveId":209,"primaryColors":{},"secondaryColors":{},"alertColors":{},"backgroundColors":{},"logoHash":"bf7e3ea2dda11869158953320356f1e7"},"lat":null,"lng":null,"originalSlug":null,"topper":"DROGAS","subheading":"Mensagens trocadas entre o perigoso narcotraficante, chefe do cartel Sinaloa, e a actriz mexicana Kate del Castillo, revelam um homem apaixonado.","featuredImage":{"id":5104,"originalHash":"a1eb4d302ad9413be802d5d0bf119028"},"readsCount":18,"commentsCount":0,"readTime":8,"terms":[{"updatedAt":null,"createdAt":1452788468000,"id":1297,"name":"PODER","name_parent":null,"taxonomyId":140,"taxonomyName":"Station: DEMO"}],"body":"<p>Quem vê telenovelas mexicanas sabe que o amor tanto pode ser a salvação como uma perdição. E no caso do mais famoso barão do narcotráfico Joaquin<em>El Chapo</em> Guzmán, um dos homens mais procurados da América do Norte, a paixão pela actriz Kate del Castillo acabou por revelar-se fatídica: foi a sua obsessão por ela, que o levou a organizar um encontro clandestino em plena selva para que pudessem conhecer-se melhor, que conduziu a polícia até ao seu esconderijo.\n</p><p>A vistosa actriz de 43 anos, uma das mais famosas estrelas de cinema e televisão do México , chamou a atenção do chefe do cartel de Sinaloa por causa de uma polémica mensagem que publicou no Twitter, no início de 2012, quando a guerra do Exército mexicano ao narcotráfico estava ao rubro. Era uma declaração política, mas que <em>El Chapo</em> terá interpretado como uma manifestação de afecto: dizia Kate que confiava mais nele do que no Governo, e como “seria maravilhoso se começasse a traficar com amor”.\n</p><p>Perante o impacto político das suas palavras, Castillo publicou uma série de justificações, incluindo que não estava a falar a sério, ou que a sua ironia e sarcasmo estavam a ser mal entendidas. Mas tomadas no seu todo, as suas declarações no Twitter constituem uma espécie de declaração – que se os perfis psicológicos de Joaquin Guzmán forem minimamente fidedignos, terá sido música para os ouvidos deste homem “sedutor, esplêndido e protector”, que apesar de ignorante e pouco interessado é “astuto e inteligente”, além de “compulsivo e tenaz”, que nunca desiste até conseguir o que quer.\n</p><p>As biografias de Kate também salientam a sua inteligência, a par da sua beleza e capacidade de trabalho. Filha de Éric del Castillo, uma das lendas da chamada época de ouro do cinema mexicano e estrela de telenovelas, Kate entrou para o negócio de família logo aos oito anos de idade. Nunca mais parou desde então, representando no teatro, cinema e também séries televisivas de enorme popularidade e audiência, tanto no México como nos Estados Unidos. Por várias vezes, interpretou o papel de mulheres ambiciosas, poderosas e temidas: na novela <em>A Rainha do Sul</em>, baseada no livro de Arturo Pérez-Reverte; na série <em>Erva</em> e na produção internacional <em>Donos do Paraíso</em> – todas estas protagonistas eram narcotraficantes.\n</p><p><em>El Chapo</em> estava detido na prisão de alta segurança de Altiplano quando iniciou contacto com a actriz. À sua mensagem inicial no Twitter respondeu com flores; depois iniciaram uma correspondência, ora manuscrita, ora por mensagens em código utilizando o telefone móvel do seu advogado, Andrés Granado, que se tornou mediador do diálogo entre os dois. Em Agosto de 2014 surgiu a hipótese de trabalharem juntos na produção de um <em>biopic</em>, alegadamente porque Guzmán lhe confessou o seu desejo de ver a sua história passada ao cinema.\n</p><p>A comunicação intensificou-se após a espectacular fuga da cadeia de El Chapo, em Julho de 2015, sempre usando o mesmo método, até que decidiu prescindir do intermediário: segundo revelam transcrições das mensagens, divulgadas pelo jornal <em>Milénio</em>, o narcotraficante fez chegar a Kate del Castillo o último modelo de telefone lançado pela Blackberry, para poderem falar directamente.\n</p><p>A conversa entre os dois nas mensagens entretanto publicadas é idêntica à de muitos casais apaixonados, sem referência a filmes ou negócios. No último capítulo do seu namoro electrónico, <em>El Chapo</em> já não esconde os seus sentimentos: convida-a para um encontro nas montanhas onde está escondido, e empenha-se para que tudo esteja a gosto da actriz – a proposta é para que passem três dias juntos. “Se trouxeres vinho, também bebo”, promete, informando-a que habitualmente não bebe, mas quando o faz prefere tequila e whisky.\n</p><p>A resposta de Kate é calorosa. Pode ser que nessa altura, como escreve o jornal <em>El País</em>, já tivesse cruzado definitivamente a linha que separa realidade e ficção, transmutando-se na sua personagem da <em>Rainha do Sul</em>, Teresa Mendoza, uma jovem inocente de Sinaloa que depois do assassinato do noivo se torna na líder implacável de um cartel da droga. “Fico muito comovida quando me dizes que vais cuidar de mim, nunca ninguém o fez por isso obrigada”, diz. “Não fazes ideia de como estou entusiasmada. E ansiosa por olhar-te nos olhos, em pessoa”, acrescenta.\n</p>","updatedAt":null,"notify":false,"author":{"updatedAt":1460825593000,"createdAt":1421497862000,"id":51,"name":"Demo","username":"demo","bookmarkPosts":[6316,5858,5936,4248,6912,6911],"recommendPosts":[3202,3938,3940,6181,3783,6123,6124,238,239,6191,5936,4248,6040,124,3804],"networkAdmin":true,"bio":"Biografia do Demo","email":"contato@xarx.co","lastLogin":null,"twitterHandle":null,"coverHash":"8799c1b71d31137ea4151ab4123d301a","imageHash":"f00aa53ab94a22d35a56b888a1d6357f","imageMediumHash":"5e3ec2674b6a072369ebc74857497744","coverLargeHash":"f820d321b93ea4f81716999c2a5d843a","imageSmallHash":"798fc4eac7b7a230ad1e688f55d293a4","coverMediumHash":"417dabed0fd2f342beb99f2d06527411","imageLargeHash":null},"title":"Foi o amor pela Rainha do Sul que tramou El Chapo Guzmán","imageMediumHash":"44c8067b27a4a6a8d12ffdef6b34f5d8","bookmarksCount":0,"featuredAudioHash":null,"recommendsCount":0,"imageCaptionText":"Vários DVD's da A Rainha do Sul foram encontrados no esconderijo de Guzmán DR","imageSmallHash":"44c8067b27a4a6a8d12ffdef6b34f5d8","imageCreditsText":null,"featuredVideoHash":null,"externalFeaturedImgUrl":null,"externalVideoUrl":null,"imageLargeHash":"a1eb4d302ad9413be802d5d0bf119028","imageTitleText":null,"tags":["sinaloa","el chapo","kate del castillo"],"imageLandscape":true,"_links":{"self":{"href":"http://demo.xarxlocal.com/data/posts/6017{?projection}","templated":true},"terms":{"href":"http://demo.xarxlocal.com/data/posts/6017/terms","templated":false},"featuredImage":{"href":"http://demo.xarxlocal.com/data/posts/6017/featuredImage","templated":false},"featuredAudio":{"href":"http://demo.xarxlocal.com/data/posts/6017/featuredAudio","templated":false},"station":{"href":"http://demo.xarxlocal.com/data/posts/6017/station","templated":false},"featuredVideo":{"href":"http://demo.xarxlocal.com/data/posts/6017/featuredVideo","templated":false},"author":{"href":"http://demo.xarxlocal.com/data/posts/6017/author","templated":false},"comments":{"href":"http://demo.xarxlocal.com/data/posts/6017/comments","templated":false}},"_embedded":{"author":{"name":"Demo","id":51,"username":"demo","email":"contato@xarx.co","imageMediumHash":"5e3ec2674b6a072369ebc74857497744","coverLargeHash":"f820d321b93ea4f81716999c2a5d843a","imageSmallHash":"798fc4eac7b7a230ad1e688f55d293a4","coverMediumHash":"417dabed0fd2f342beb99f2d06527411","_links":{"self":{"href":"http://demo.xarxlocal.com/data/persons/51{?projection}","templated":true}}}}}
}

function formatVersions(versions){
	var ret =[];
	if(versions){
		for (var k in versions) {
	        if (versions.hasOwnProperty(k)) {
	           ret.push({
	           		'date': parseInt(k),
	           		'payload': versions[k]
	           })
	        }
	    }
	}

	return setIndexes(ret);
}

function setIndexes(versions){
	if(versions && versions.length){
		var index = versions.length
		versions.forEach(function(v){
			v.index = index
			index --;
		})
	}
	return versions
}

function createVersions(){
	 var revisions = [{
			"date": 1458079387000,
			"tag": "",
			"payload": createPostStub()
		},
		{
			"date": 1458067270000,
			"tag": "",
			"payload": createPostStub()
		},
		{
			"date": 1458052152000,
			"tag": "",
			"payload": createPostStub()
		}]

		return revisions;
}
// --- /TEST

	var postInfoTemplate = '<md-dialog class="splash-dialog">'+
		'<div class="pos-fix top-0 right-0 p">'+
			'<md-button class="md-icon-button md-icon-button-lg" ng-click="app.cancelDialog();"><i class="mdi mdi2-close i-40"></i></md-button>'+
		'</div>'+
		'<div class="content">'+
			'<div class="row">'+
			  '<div class="m-b-sm"><code>/{{app.editingPost.slug}}</code></div>'+
			  '<div class="h3 font-bold m-b-md">{{app.editingPost.title}}</div>'+
			  '<div class="text-md m-b-sm">'+
			    '<strong>Estado da publicação:</strong>'+
			    '<span class="text-u-c m-l-sm">'+
			    	'{{app.getStateText()}}'+
		    	'</span>'+
			  '</div>'+
			  '<div class="text-md m-b-sm">'+
			    '<strong>Data de publicação:</strong>'+
			    '<span class="text-u-c m-l-sm">{{app.editingPost.date | fromNow2}}</span>'+
			  '</div>'+
			  '<div class="text-md m-b-sm" ng-if="app.editingPost.createdAt">'+
			    '<strong>Criado em:</strong>'+
			    '<span class="text-u-c m-l-sm">{{app.editingPost.createdAt | fromNow2}}</span>'+
			  '</div>'+
			  '<div class="text-md m-b-sm" ng-if="app.editingPost.updatedAt">'+
			    '<strong>Atualizado em:</strong>'+
			    '<span class="text-u-c m-l-sm">{{app.editingPost.updatedAt | fromNow2}}</span>'+
			  '</div>'+
			  '<div class="text-md m-b-sm" ng-if="app.editingPost.body">'+
			    '<strong>Título:</strong>'+
			    '<span class="m-l-sm m-r-sm">{{app.countTitleWords()}} palavras</span> | <span class="m-l-sm">{{app.countTitleCharacters()}} caracteres</span>'+
			  '</div>'+
			  '<div class="text-md m-b-sm" ng-if="app.editingPost.body">'+
			    '<strong>Texto:</strong>'+
			    '<span class="m-l-sm m-r-sm">{{app.countBodyWords()}} palavras</span> | <span class="m-l-sm">{{app.countBodyCharacters()}} caracteres</span>'+
			  '</div>'+
			'</div>'+
		'</div>'+
	'</md-dialog>';

	var postCropTemplate = '<md-dialog class="splash-dialog">'+
		'<div class="pos-fix top-0 right-0 p w-full">'+
			'<md-button class="md-icon-button md-icon-button-lg pull-right" ng-click="app.cancelDialog();"><i class="mdi mdi2-close i-40"></i></md-button>'+
			'<div translate="settings.post.CROP_IMAGE_DESC" class="m-h-auto text-center w-full text-md p font-500"></div>'+
		'</div>'+
		'<div class="block-800 min-h-400 pos-rlt min-h-400" style="width:100%">'+
			'<div class="pos-abt w-full top-0 bottom-0 left-0">'+
			 '<div class="hbox">'+
			    '<div class="col text-md font-500">'+
			      '<div class="vbox">'+
			        '<div class="row-row">'+
			          '<div class="cell">'+
			            '<div class="cell-inner">'+
			            	'<div class="center-img-container m-h-auto o-f-h">'+
											'<div ng-jcrop="app.postFeaturedImage" selection="app.cropSelection"></div>'+
										'</div>'+
			            '</div>'+
			          '</div>'+
			        '</div>'+
			      '</div>'+
			    '</div>'+
		    '</div>'+
	    '</div>'+
    '</div>'+
		'<div class="text-center p-t">'+
			'<md-button ng-click="app.cancelDialog();" class="m-0">'+
				'{{\'titles.CANCEL\' | translate}}'+
			'</md-button>'+
			'<md-button ng-click="app.clearPostContent();" class="m-0">'+
				'{{\'titles.SAVE\' | translate}}'+
			'</md-button>'+
		'</div>'+
	'</md-dialog>';

	var postFocuspointTemplate = '<md-dialog class="splash-dialog">'+
		'<div class="pos-fix top-0 right-0 p w-full">'+
			'<md-button class="md-icon-button md-icon-button-lg pull-right" ng-click="app.cancelDialog();"><i class="mdi mdi2-close i-40"></i></md-button>'+
			'<div translate="settings.post.FOCUS_IMAGE_DESC" class="m-h-auto text-center w-full text-md p font-500"></div>'+
		'</div>'+
		'<div class="block-800 min-h-400 pos-rlt min-h-400" style="width:100%">'+
			'<div class="block-800">'+
			'<div class="pos-abt w-full top-0 bottom-0 left-0">'+
			  '<div class="hbox">'+
			    '<div class="col text-md font-500">'+
			      '<div class="vbox">'+
			        '<div class="row-row">'+
			          '<div class="cell">'+
			            '<div class="cell-inner">'+
			            	'<div class="focus-point center-img-container pull-right o-f-h">'+
				            	'<div ng-style="{\'width\': app.imgToolsProps.width + \'px\', \'height\': app.imgToolsProps.height + \'px\', \'background-image\': \'url(\' + app.postFeaturedImage + \')\', \'background-position\': \'50% 50%\'}" '+
				            	'class="bg-cover center-img md-whiteframe-z2 focus-area" '+
				            	'ng-mousemove="app.imgToolsProps.onMouseMove($event)" ng-mousedown="app.imgToolsProps.onMouseDown($event)" ng-mouseup="app.imgToolsProps.onMouseUp($event)">'+
				            		'<span class="target lfy-focuspoint-button" ng-style="{\'left\': app.imgToolsProps.x + \'%\', \'top\': app.imgToolsProps.y + \'%\'}"></span>'+
				            	'</div>'+
										'</div>'+
			            '</div>'+
			          '</div>'+
			        '</div>'+
			      '</div>'+
			    '</div>'+
			    '<div class="col w-xxl text-md font-500">'+
			      '<div class="vbox">'+
			        '<div class="row-row">'+
			          '<div class="cell">'+
			            '<div class="cell-inner" style="background-image: url(/images/device-stubs.png); background-position: center bottom; background-repeat: no-repeat;">'+
			            	'<figure class="m-0 pos-abt bg-cover" id="focuspoint-tablet" '+ 
			            		'style=" width: 82px;'+
							    'height: 124px;'+
							    'border-radius: 3px;'+
							    'top: 30px;'+
							    'left: 107px;"'+
							    'ng-style="{\'background-image\': \'url(\' + app.postFeaturedImage+ \')\', \'background-position\': app.imgToolsProps.x + \'% \' + app.imgToolsProps.y + \'%\'}"></figure>'+
							'<figure class="m-0 pos-abt bg-cover" id="focuspoint-mobile" '+
								'style="width: 100px;'+
							    'height: 43px;'+
							    'border-radius: 3px;'+
							    'top: 183px;'+
							    'left: 104px;"'+
							    'ng-style="{\'background-image\': \'url(\' + app.postFeaturedImage+ \')\', \'background-position\': app.imgToolsProps.x + \'% \' + app.imgToolsProps.y + \'%\'}"></figure>'+
							'<figure class="m-0 pos-abt bg-cover" id="focuspoint-laptop" '+
								'style="width: 124px;'+
							    'height: 85px;'+
							    'border-radius: 3px;'+
							    'top: 248px;'+
							    'left: 94px;"'+
							    'ng-style="{\'background-image\': \'url(\' + app.postFeaturedImage+ \')\', \'background-position\': app.imgToolsProps.x + \'% \' + app.imgToolsProps.y + \'%\'}"></figure>'+
			            '</div>'+
			          '</div>'+
			        '</div>'+
			      '</div>'+
			    '</div>'+
		    '</div>'+
	    '</div>'+
	    '</div>'+
    '</div>'+
		'<div class="text-center p-t">'+
			'<md-button ng-click="app.cancelDialog();" class="m-0">'+
				'{{\'titles.CANCEL\' | translate}}'+
			'</md-button>'+
			'<md-button ng-click="" class="m-0">'+
				'{{\'titles.SAVE\' | translate}}'+
			'</md-button>'+
		'</div>'+
	'</md-dialog>';