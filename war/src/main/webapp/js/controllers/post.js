// tab controller
app.controller('PostCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$window', '$mdSidenav',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $window, $mdSidenav) {

		FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
			return true;
		};

		$scope.baseUrl = TRIX.baseUrl;

		$scope.selectedStations = [];

		$scope.multiselectCong = {'displayProp': 'stationName', 'idProp': 'stationId'};

		var createPostObject = function(){
			$scope.app.editingPost = {};
			$scope.app.editingPost.imageLandscape = true;
			$scope.discardedMedia = null;
			$scope.app.editingPost.uploadedImage = null;
			$scope.app.editingPost.showMediaButtons = false;
			$scope.app.editingPost.editingExisting = false;
			// --------- post time info

			$scope.app.editingPost.today = function() {
				$scope.app.editingPost.customDate = new Date();
			};
			$scope.app.editingPost.today();

			$scope.app.editingPost.clear = function () {
				$scope.app.editingPost.customDate = null;
			};


		    $scope.app.editingPost.disabled = function(date, mode) {// Disable weekend selection
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
		    $scope.app.editingPost.mytime = $scope.app.editingPost.customDate

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

		    /*--------- /post time info ----*/
		}

		$scope.loadPost = function(postId){
			if(postId){
				trix.getPost(postId, 'postProjection').success(function(response){
					createPostObject();
					$scope.app.editingPost = angular.extend($scope.app.editingPost, response);
					setWritableStationById(response.station.id)
					updateTermTree();
					$timeout(function() {
						$scope.app.editingPost.editingExisting = false;
						$scope.loadMedia()
					}, 1000);
				})
			}else{
				createPostObject();
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

		$scope.loadMedia = function(){
			if($scope.app.editingPost.externalVideoUrl){
				$scope.app.editingPost.showInputVideoUrl = true
				$scope.app.editingPost.showMediaButtons = false;
				$("#video-url-input").focus();
				$scope.videoUrl = $scope.app.editingPost.externalVideoUrl;
			}else if($scope.app.editingPost.featuredImage){
				$scope.app.editingPost.uploadedImage = {filelink: TRIX.baseUrl + "/api/files/"+$scope.app.editingPost.imageLargeId+"/contents" }
				$scope.checkLandscape();
			}

			$timeout(function() {
				$scope.invertLandscapeSquare();
				$scope.invertLandscapeSquare();
			}, 50);
		}

		if($state.params && $state.params.id){
			$scope.app.editingPost = null;
			var postId = $state.params.id
			$scope.loadPost(postId);
		}

		$scope.app.checkState = function(state){
			if(!$scope.app.editingPost)
				return null;

			state = state ? state : $scope.app.editingPost.state;
			if(!state)
				return null;
			if(state == "PUBLISHED"){
				return 1;
			}else if(state == "DRAFT"){
				return 2;
			}else if(state == "SCHEDULED"){
				return 3;
			}else if(state == "TRASH"){
				return 4;
			}else{
				return null;
			}
		}

		$scope.checkState = function(state){
			return $scope.app.checkState(state);
		}


	// check if user has permisstion to write
	$scope.writableStations = trixService.getWritableStations();

	var setWritableStationById = function(id){
		$scope.writableStations && $scope.writableStations.forEach(function(station, index){
			if(station.stationId == id)
				$scope.app.editingPost.selectedStation = station;
		});
	}

	$scope.postCtrl = {}

	$scope.writableStations && $scope.writableStations.forEach(function(station, index){
		station.id = station.stationId;
		station.name = station.stationName;
	});

	if(!$scope.writableStations || $scope.writableStations.length == 0){}

		if(!$scope.app.editingPost){
			createPostObject();
		}else{
			$scope.loadMedia();
		}

		if(!$state.params.id)
			$scope.writableStations && $scope.writableStations.forEach(function(station, index){
				if(station.stationId == $scope.app.currentStation.id)
					$scope.app.editingPost.selectedStation = station;
			});

			if($scope.app.editingPost && !$scope.app.editingPost.selectedStation)
				$scope.app.editingPost.selectedStation = $scope.writableStations[0];

		$scope.showTopOptions = function(){
			if($scope.app.editingPost){
				return ($scope.app.editingPost.body && $scope.app.editingPost.title) || $scope.app.editingPost.id 
			}else 
			return false;
		}

		$scope.selectStation = function(selectedStation){
			$scope.app.editingPost.selectedStation = selectedStation
		}

	/*  $scope.$watch('app.editingPost.selectedStation', function(newVal){
	  	console.log(newVal);
	  }, true)*/

$scope.$watch('app.hidePostOptions', checkPostToolsWidth)

if(typeof $scope.app.showPostToolbar === 'undefined')
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

function doResize(){
	checkPostToolsWidth();
}

var timeoutResize;
$(window).resize(function(){
	clearTimeout(timeoutResize);
	timeoutResize = setTimeout(doResize, 100);
})

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

$scope.$watch('app.editingPost', function(newValue, oldValue){
	if(!(newValue && oldValue && oldValue.editingExisting === true && newValue.editingExisting === false) &&
		newValue && oldValue && (newValue.title || newValue.body || newValue.editingExisting)){
		$scope.app.editingPost.editingExisting = true;
}
if(newValue && newValue.editingExisting){
	window.onbeforeunload = function(){
		return 'Você possui conteúdo que ainda não foi salvo. Deseja sair desta página?';
	};
}else{
	window.onbeforeunload = null;
}
}, true)

// ------------------- image landscape settings ---------

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

// ------------------- end of image landscape settings ---------

// ------------------- image uploader -------------

var uploader = $scope.uploader = new FileUploader({
	url: TRIX.baseUrl + "/api/files/contents/simple"
});

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
		$scope.postCtrl.imageHasChanged = true
	}
};

uploader.onErrorItem = function(fileItem, response, status, headers) {
	if(status == 413)
		$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
	else
		$scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
}

$scope.clearImage = function(){ 
	$("#image-config").addClass("hide");
	$scope.app.editingPost.uploadedImage = null;
	uploader.clearQueue();
	uploader.cancelAll()
	$scope.checkLandscape();
	$scope.postCtrl.imageHasChanged = true;
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

// ------------------- end of image uploader -------------

// --------------- chip tags ------------------

 $scope.chipTags = {
 	readonly: false,
    // Lists of fruit names and Vegetable objects
    fruitNames: ['Apple', 'Banana', 'Orange'],
    roFruitNames: angular.copy(self.fruitNames),
    tags: [],
    vegObjs: [
      {
        'name' : 'Broccoli',
        'type' : 'Brassica'
      },
      {
        'name' : 'Cabbage',
        'type' : 'Brassica'
      },
      {
        'name' : 'Carrot',
        'type' : 'Umbelliferous'
      }
    ]}
 
// --------------- end of chip tags ------------

// ------------------- update term tree ---------------

$scope.$watch('app.editingPost.selectedStation', function(newVal){
	updateTermTree()
})

function updateTermTree(){
	if($scope.app.editingPost && $scope.app.editingPost.selectedStation)
		trix.getTermTree($scope.app.editingPost.selectedStation.defaultPerspectiveId).success(function(response){
			$scope.termTree = response;
			selectTerms($scope.termTree, $scope.app.editingPost.terms)
		});
}

// ------------------- end of update term tree ---------------


// ------------------- slug watch ------------
var customSlug = false;
$scope.$watch('app.editingPost.title', function(newVal){
	/*if(newVal && (!$scope.app.editingPost.slug || $scope.app.editingPost.slug.trim() == ''))
	$scope.app.editingPost.slug = newVal ? newVal.toSlug() : '';*/
	if(newVal && !customSlug)
		$scope.app.editingPost.slug = newVal ? newVal.toSlug() : '';
})

$scope.app.changeCustomSlug = function(){
	customSlug = true
}

$scope.$watch('app.editingPost.slug', function(newVal){
	if(newVal && customSlug)
		$scope.app.editingPost.slug = newVal ? newVal.toSlug() : '';
})

// ------------------- end of slug watch ------------

	/*function uncheckTerms(terms){
		terms && terms.forEach(function(term, index){
			term.checked = false

			if(term.children && term.children.length > 0)
				uncheckTerms(term.children)
		});
}*/

$scope.app.editingPost.customDate = new Date();
$timeout(function() {
	$scope.app.editingPost.customDate = new Date();
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

	// --------- schedulePost

	$scope.schedulePost = function(){
		if(isTermSelected($scope.termTree) && !$scope.app.editingPost.id){
			createPost("SCHEDULED")
		}else{
			//$scope.showMoreOptions(ev);
			$scope.app.hidePostOptions = false;
			$scope.app.showInfoToast('Escolha uma categoria.')
		}
	}

	// --------- publish post

	$scope.publishPost = function(ev){
		if(isTermSelected($scope.termTree) && !$scope.app.editingPost.id){
			createPost()
		}else if(isTermSelected($scope.termTree) && $scope.app.editingPost.id){
			updatePost("PUBLISHED")
		}else{
			//$scope.showMoreOptions(ev);
			$scope.app.hidePostOptions = false;
			$scope.app.showInfoToast('Escolha uma categoria.')
		}
	}

	$scope.app.updateToDraf = function(){
		updatePost("DRAFT")
	}

	$scope.showPostDetails = function(){
		$scope.app.openSplash('show_posts_details.html')
	}

	$scope.savePostAsDraft = function(ev){
		console.log($scope.checkState(), $scope.app.editingPost);
		if($scope.showTopOptions()){
			if($scope.checkState() == 1 || $scope.checkState() == 3){
				$scope.app.openSplash('confirm_change_to_draft.html')
			}else if($scope.checkState() == 2){
				window.console && console.log("Already a draft... updating");
				updatePost("DRAFT");
			}else{
				createPost("DRAFT")
			}
			//$scope.app.editingPost.editingExisting = false;
		}
	}

	$scope.openHelp = function(){
		$scope.app.openSplash('post_help.html')	
	};

	$scope.doAction = function(action){
		if(typeof action === 'function')
			action();
	}

	$scope.showMoreOptions = function(ev){
		// show term alert
		$mdDialog.show({
			controller: MoreOptionsController,
			templateUrl: 'tpl/post_more_options.html',
			parent: angular.element(document.body),
			targetEvent: ev,
			clickOutsideToClose:true
			// onComplete: function(){

			// }
		})
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

			// check if user has permisstion to write
			// scope.writableStations = trixService.getWritableStations();
		};

		function selectTerms(terms, termList){
			if(!termList || !terms)
				return;
			var termIds = []
			termList.forEach(function(termItem, index){
				termIds.push(termItem.id)
			});

			terms && terms.forEach(function(term, index){
				if(termIds.indexOf(term.id) > -1)
					term.checked = true;
				selectTerms(term.children, termList)
			});
		}

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

		function updatePost(state){

			var post = angular.copy($scope.app.editingPost);
			post.station = TRIX.baseUrl + "/api/stations/" + post.station.id;
			// post is ok to be created
			var termList = getTermList($scope.termTree);
			var termUris = []
			termList.forEach(function(term){
				termUris.push(TRIX.baseUrl + "/api/terms/" + term.id);
			})

			post.terms = termUris;
			post.author = TRIX.baseUrl + "/api/authors/" + post.author.id

			if(($scope.checkState() == 1 || $scope.checkState() == 3) && (!post.terms || post.terms.length == 0)){
				$scope.app.showInfoToast('Escolha uma categoria.')
				return;
			}

			if(post.featuredImage && post.featuredImage.id){
				post.featuredImage = TRIX.baseUrl + "/api/images/" + post.featuredImage.id;
			}else{
				post.featuredImage = null;
			}

			var doUpdate = function(){
				trix.convertPost(post.id, state).success(function(){
					post.state = state
					if($scope.checkState(state) == 1){
						trix.putPost(post).success(function(){
							$scope.app.showSuccessToast('Notícia atualizada com sucesso.')
							$scope.app.editingPost.state = state;
							$scope.app.cancelModal();
						});
					}else if($scope.checkState(state) == 2){
						trix.putPostDraft(post).success(function(){
							$scope.app.showSuccessToast('Notícia atualizada com sucesso.')
							$scope.app.editingPost.state = state;
							$scope.app.cancelModal();
						});
					}else if($scope.checkState(state) == 3){
						trix.putPostScheduled(post).success(function(){
							$scope.app.showSuccessToast('Notícia atualizada com sucesso.')
							$scope.app.editingPost.state = state;
							$scope.app.cancelModal();
						});
					}
				});
			}

			if($scope.app.editingPost.uploadedImage && $scope.app.editingPost.uploadedImage.id){
				var featuredImage = { original: TRIX.baseUrl + "/api/files/" + $scope.app.editingPost.uploadedImage.id }
				if($scope.app.editingPost.imageCaption)
					featuredImage.caption = $scope.app.editingPost.imageCaption

				if($scope.app.editingPost.imageTitle)
					featuredImage.caption = $scope.app.editingPost.imageTitle

				trix.postImage(featuredImage).success(function(imageId){
					post.featuredImage = TRIX.baseUrl + "/api/images/" + imageId;
					doUpdate();
				})
			}else{
				if(!$scope.app.editingPost.uploadedImage) // remove if no image
					post.featuredImage = null;
				doUpdate();
			}
			

			// post.state = state
			// trix.putPost(post).success(function(){
			// 	if(post.state != state){
			// 		trix.convertPost(post.id, state).success(function(){
			// 			$scope.app.showSuccessToast('Notícia atualizada com sucesso.')
			// 			$scope.app.editingPost.state = state;
			// 			console.log($scope.checkState());
			// 		});
			// 		console.log('change state');
			// 	}
			// })
}

function createPost(state){
	var post = {};
	post.title = $scope.app.editingPost.title
	post.body = $scope.app.editingPost.body

	if($scope.app.editingPost.externalVideoUrl)
		post.externalVideoUrl = $scope.app.editingPost.externalVideoUrl
	if($scope.app.editingPost.notify)
		post.notify = true

	if($scope.app.editingPost.topper)
		post.topper = $scope.app.editingPost.topper

	if($scope.app.editingPost.subheading)
		post.subheading = $scope.app.editingPost.subheading

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
			post.station = TRIX.baseUrl + "/api/stations/" + $scope.app.editingPost.selectedStation.stationId;
			post.author = extractSelf($scope.app.getLoggedPerson())

			if($scope.app.editingPost.uploadedImage){
				var featuredImage = { original: TRIX.baseUrl + "/api/files/" + $scope.app.editingPost.uploadedImage.id }
				if($scope.app.editingPost.imageCaption)
					featuredImage.caption = $scope.app.editingPost.imageCaption

				if($scope.app.editingPost.imageTitle)
					featuredImage.caption = $scope.app.editingPost.imageTitle

				trix.postImage(featuredImage).success(function(imageId){
					post.featuredImage = TRIX.baseUrl + "/api/images/" + imageId;
					//postPost(post);
					if(state == "DRAFT"){
						post.state = state;
						postDraft(post)
					}else{
						postPost(post);
					}
				})
			}else{
				if(state == "DRAFT"){
					post.state = state;
					postDraft(post)
				}else if(state == "SCHEDULED"){
					post.state = state;

					var start = $scope.dt
					start.setHours(0,0,0,0);

					var now = new Date()
					now.setHours(0,0,0,0);

					var scheduledDate = new Date(start.getTime() + ($scope.app.editingPost.scheduledDate.getTime() - now.getTime()))

					var diffMs = (scheduledDate - new Date());

					var diffMins = Math.round(((diffMs % 86400000) % 3600000) / 60000); // minutes

					if(diffMins < 15 && (scheduledDate.toDateString() === now.toDateString() || scheduledDate.toDateString() < now.toDateString() )){
						$scope.app.showErrorToast('Escolha um horário com mínimo<br>15 minutes do horário atual. ');
						return;
					}

					post.scheduledDate = scheduledDate;
					postSchduled(post)
				
				}else{
					postPost(post);
				}
			}

			
		} // end of final else
	}// end of createPost()

	var postSchduled = function(post){
		if(post && post.id){
			window.console && console.log('Erro ao criar post: ' + post.id);
			return false;
		}

		trix.postPostScheduled(post).success(function(postId){
			$scope.app.showSuccessToast('Agendado com sucesso.');
			// replace url withou state reload
			// $state.go($state.current.name, {'id': postId}, {location: 'replace', inherit: false, notify: false, reload: false})
			$scope.app.refreshPerspective();
			trix.getPostScheduled(postId, "postProjection").success(function(postResponse){
				$scope.schedulerPopoverOpen = false;
				$scope.app.editingPost = angular.extend($scope.app.editingPost, postResponse);
				$timeout(function() {
					$scope.app.editingPost.editingExisting = false;
					window.onbeforeunload = null;
				}, 1000);	
			}).error(function(data, status, header){
				if(status == 403)
					$scope.app.openSplash('signin_splash.html')
			})
		})
	}

	var postDraft = function(post){
		if(post && post.id){
			window.console && console.log('Erro ao criar post: ' + post.id);
			return false;
		}

		trix.postPostDraft(post).success(function(postId){
			$scope.app.showSuccessToast('Rascunho salvo.');
			// replace url withou state reload
			// $state.go($state.current.name, {'id': postId}, {location: 'replace', inherit: false, notify: false, reload: false})
			$scope.app.refreshPerspective();
			trix.getPostDraft(postId, "postProjection").success(function(postResponse){
				$scope.app.editingPost = angular.extend($scope.app.editingPost, postResponse);
				$timeout(function() {
					$scope.app.editingPost.editingExisting = false;
					window.onbeforeunload = null;
				}, 1000);	
			}).error(function(data, status, header){
				if(status == 403)
					$scope.app.openSplash('signin_splash.html')
			})
		})
	}

	var postPost = function(post){
		trix.postPost(post).success(function(postId){
			$scope.app.showSuccessToast('Notícia publicada.');
			// replace url withou state reload
			// $state.go($state.current.name, {'id': postId}, {location: 'replace', inherit: false, notify: false, reload: false})

			$scope.app.refreshPerspective();
			trix.getPost(postId, 'postProjection').success(function(response){
				createPostObject();
				$scope.app.editingPost = angular.extend($scope.app.editingPost, response);
				if($scope.app.editingPost.imageLargeId)
				$scope.app.editingPost.uploadedImage = {filelink: TRIX.baseUrl + "/api/files/"+$scope.app.editingPost.imageLargeId+"/contents" }
				setWritableStationById(response.station.id)
				updateTermTree();
				$timeout(function() {
					$scope.app.editingPost.editingExisting = false;
					window.onbeforeunload = null;
				}, 1000);
			})

		}).error(function(data, status, header){
			if(status == 403)
				$scope.app.openSplash('signin_splash.html')
		})
	}

	// {height:'auto', size:'8px', 'railVisible': true}
	
	$scope.menuState = 'closed';

	$scope.chosen = {
		effect : 'slidein',
		position : 'br',
		method : 'hover',
		action : 'fire'
	};

	$scope.buttons = [
	{
		label: 'Salvar rascunho',
		icon: 'mdi mdi-content-save',
		action: $scope.savePostAsDraft
	},
	{
		label: 'Remover/Descartar',
		icon: 'mdi mdi-delete',
		action: $scope.deleteOrDiscardPost
	}
	];

	// ------------- end of navbar buttons ---------
	

	// ---------- slim scroll --------------

	$timeout(function() {
		safeApply($scope, function(){
			$('#post-cell').slimScroll({
				height:'auto',
				size:'8px',
				'railVisible': true
			})
		})
	}, 10);

	// ---------- end of slim scroll --------------

	// ----------- scheduler date picker ------------

	$scope.today = function() {
		$scope.dt = new Date();
	};
	$scope.today();

	$scope.clear = function () {
		$scope.dt = null;
	};

	$scope.disabled = function(date, mode) {
		return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
	};

	$scope.toggleMin = function() {
		$scope.minDate = $scope.minDate ? null : new Date();
	};
	$scope.toggleMin();

	$scope.open = function($event) {
		$event.preventDefault();
		$event.stopPropagation();

		$scope.opened = true;
	};

	$scope.dateOptions = {
		formatYear: 'yy',
		startingDay: 1
	};

	$scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
	$scope.format = $scope.formats[0];

	var tomorrow = new Date();
	tomorrow.setDate(tomorrow.getDate() + 1);
	var afterTomorrow = new Date();
	afterTomorrow.setDate(tomorrow.getDate() + 2);
	$scope.events =
	[
	{
		date: tomorrow,
		status: 'full'
	},
	{
		date: afterTomorrow,
		status: 'partially'
	}
	];

	$scope.getDayClass = function(date, mode) {
		if (mode === 'day') {
			var dayToCheck = new Date(date).setHours(0,0,0,0);

			for (var i=0;i<$scope.events.length;i++){
				var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

				if (dayToCheck === currentDay) {
					return $scope.events[i].status;
				}
			}
		}

		return '';
	};
	// ----------- end of scheduler date picker ------------
	
	$scope.togglePostOptions = function(ev){
      $mdSidenav('post-options').toggle();
    }

}]);
