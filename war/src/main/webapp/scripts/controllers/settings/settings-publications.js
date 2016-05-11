app.controller('SettingsPublicationsCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdToast', '$translate', '$mdSidenav', '$filter',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix , FileUploader, TRIX, cfpLoadingBar, $mdToast, $translate, $mdSidenav, $filter){

		$scope.settings = {'tab': 'publications'}

		$scope.dateValue = new Date();

		$scope.advancedMenuOpen = false;

		$scope.searchFilterActive = true;

		$scope.stationsPermissions = angular.copy($scope.app.stationsPermissions);

		function buildToggler(navID) {
      return function() {
        $mdSidenav(navID)
          .toggle()
          .then(function () {
            $log.debug("toggle " + navID + " is done");
          });
      }
    }

      /**
     * Supplies a function that will continue to operate until the
     * time is up.
     */
    function debounce(func, wait, context) {
      var timer;
      return function debounced() {
        var context = $scope,
            args = Array.prototype.slice.call(arguments);
        $timeout.cancel(timer);
        timer = $timeout(function() {
          timer = undefined;
          func.apply(context, args);
        }, wait || 10);
      };
    }

    /**
     * Build handler to open/close a SideNav; when animation finishes
     * report completion in console
     */
    function buildDelayedToggler(navID) {
      return debounce(function() {
        $mdSidenav(navID)
          .toggle()
          .then(function () {
            $log.debug("toggle " + navID + " is done");
          });
      }, 200);
    }

    $scope.app.publicationsCtrl = {page: 0, firstLoad: false};

	
		$scope.drafts = [];
		$scope.publications = [];
		$scope.scheduleds = [];
		$scope.trash = [];

	$scope.$watch('settings.tab', function(){
	// 	if(/*$state.params.type == "drafts"*/ $scope.settings.tab == "drafts"){
	// // trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': $scope.app.getLoggedPerson().id,
	// 	// 'publicationType': 'DRAFT', sortByDate: true}).success(function(response){
	// 		trix.getPersonNetworkPostsByState(null, 'DRAFT', $scope.app.publicationsCtrl.page, $scope.window).success(function(response){
	// 			$scope.drafts = response;
	// 			$scope.firstLoad = true;
	// 		})
	// 	}
	// 	if(/*$state.params.type == "publications"*/ $scope.settings.tab == "publications"){
	// 		trix.getPersonNetworkPostsByState(null, 'PUBLISHED', $scope.app.publicationsCtrl.page, $scope.window).success(function(response){
	// 			$scope.publications = response;
	// 			$scope.firstLoad = true;
	// 		})
	// 	}
	// 	if(/*$state.params.type == "scheduled"*/ $scope.settings.tab == "scheduled"){
	// 		trix.getPersonNetworkPostsByState(null, 'SCHEDULED', $scope.app.publicationsCtrl.page, $scope.window).success(function(response){
	// 			$scope.scheduleds = response;
	// 			$scope.firstLoad = true;
	// 		})
	// 	}
	// 	if(/*$state.params.type == "scheduled"*/ $scope.settings.tab == "trash"){
	// 		trix.getPersonNetworkPostsByState(null, 'TRASH', $scope.app.publicationsCtrl.page, $scope.window).success(function(response){
	// 			$scope.trash = response;
	// 			$scope.firstLoad = true;
	// 		})
	// 	}
		trix.searchPosts($scope.searchQuery, null, null, tabToState().toLowerCase(), null, null, null, null, $scope.app.publicationsCtrl.page, $scope.window, '-date', ['body'], true).success(function(response){
			response.reverse();
			$scope.publications = response;
		})
	});

	var tabToState = function(){
		if($scope.settings.tab === 'publications')
			return 'PUBLISHED';
		if($scope.settings.tab === 'scheduled')
			return 'SCHEDULED';
		if($scope.settings.tab === 'drafts')
			return 'DRAFT';
		if($scope.settings.tab === 'trash')
			return 'TRASH';
	}

	$scope.$on('POST_REMOVED', function(event, postId){
		if($scope.app.publicationsCtrl && $scope.publications){
			for (var i = $scope.publications.length - 1; i >= 0; i--) {
				if(postId == $scope.publications[i].postId)
					$scope.publications.splice(i,1)
			};
		}
	})

	$scope.paginate = function(){

		if(!$scope.publications || $scope.publications.length == 0)
			return;

		if($scope.allLoaded)
			return;

	 	if(/*$state.params.type == "drafts"*/ $scope.settings.tab == "drafts"){
	    type = 'DRAFT'
	  }
	  if(/*$state.params.type == "publications"*/ $scope.settings.tab == "publications"){
	    type = 'PUBLISHED'
	  }
	  if(/*$state.params.type == "scheduled"*/ $scope.settings.tab == "scheduled"){
	    type = 'SCHEDULED'
	  }
	  if(/*$state.params.type == "scheduled"*/ $scope.settings.tab == "trash"){
	    type = 'TRASH'
	  }

		if(!$scope.loadingPage){
			$scope.loadingPage = true;

			trix.getPersonNetworkPostsByState(null, type, $scope.app.publicationsCtrl.page+1, 10).success(function(response){
				var posts = response;

				$scope.loadingPage = false;
				$scope.app.publicationsCtrl.page = $scope.app.publicationsCtrl.page + 1;

				if(!posts || posts.length == 0){
					$scope.allLoaded = true;
					return;
				}

				if(!$scope.pages)
					$scope.pages = []

				posts && posts.forEach(function(element, index){
					$scope.publications.push(element)
				}); 

			})
			.error(function(){
				$scope.loadingPage = false;
			})
		}
	}

	$scope.page = 0;
	$scope.loadingComments = false
	$scope.allLoaded = false;
	$scope.window = 20

	// sidenav toggle
	$scope.toggleComments = buildToggler('post-comments');
	$scope.togglePost = buildToggler('post-summary');

	$scope.postFeaturedImage = null
	var setPostFeaturedImage = function(hash){
		$scope.postLoaded.postFeaturedImage = $filter('imageLink')({imageHash: hash}, 'large')
	}

  $scope.showPost = function(id){
  	$scope.togglePost();
  	$scope.postLoaded = null;
  	trix.getPost(id, 'postProjection').success(function(response){
  		$scope.postLoaded = response;

  		if($scope.postLoaded.terms)
			$scope.postLoaded.terms.forEach(function(term){
				term.checked = true;
			})

			var hash = $scope.postLoaded.featuredImage ? $scope.postLoaded.featuredImage.originalHash : null;
			setPostFeaturedImage(hash)
			$scope.postLoaded.featuredImage = $scope.postLoaded.featuredImage
			$scope.postLoaded.landscape = $scope.postLoaded.imageLandscape;

			$scope.postLoaded.useHeading = $scope.postLoaded.topper ? true:false
			$scope.postLoaded.useSubtitle = $scope.postLoaded.subtitle ? true:false
  	})
  }

	$scope.showComments = function(post){
		$scope.toggleComments();
		$scope.comments = []
		$scope.postLoaded = post;
		$scope.commentsPage = 0;
		$scope.loadingComments = false
		$scope.paginateComments();
	}

	$scope.paginateComments = function(){
		if(!$scope.loadingComments && $scope.postLoaded && $scope.postLoaded.id){
			$scope.loadingComments = true;
			trix.findPostCommentsOrderByDate($scope.postLoaded.id, $scope.commentsPage, $scope.window, null, 'commentProjection').success(function(response){
				if(response.comments && response.comments.length > 0){
					response.comments.forEach(function(comment){
						$scope.comments.push(comment);
					})
					$scope.loadingComments = false
					$scope.commentsPage ++;
				}
			}).error(function(){
				$scope.comments = null;
				$scope.loadingComments = false
			})	
		}
	}

	// $scope.toggleCommentsSidebar = function(){
	//   $mdSidenav('comments-list').toggle();
	// }

	// $scope.createComment = function(){
	// 	var comment = {}
	// 	comment = angular.copy($scope.newComment);
	// 	comment.author = extractSelf($scope.app.initData.person)
	// 	comment.post =TRIX.baseUrl + '/api/posts/' + $scope.app.nowReading.postId

	// 	trix.postComment(comment).success(function(response){
	// 		if(!$scope.comments || $scope.comments.length == 0)
	// 			$scope.comments = [];

	// 		comment.author = angular.copy($scope.app.initData.person)
	// 		comment.date = new Date().getTime();
	// 		$scope.newComment = {body: ''};

	// 		$scope.comments.unshift(comment)

	// 	}).error(function(response,status){
	// 		$scope.app.showErrorToast('Houve um erro inesperado. Tente novamente.')

	// 	})
	// }

	$scope.commentFocused = false;
	$scope.commentFocus = function(){
		$scope.commentFocused = true;
	}
	$scope.commentBlur = function(){
		$scope.commentFocused = false;
	}

	var publicationActiveId = null;
	$scope.activatePublication = function(publication){
		publicationActiveId = publication.id;
	}

	$scope.isActivePublication = function(publication){
		return publicationActiveId == publication.id
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

  	if(toggleSelectValue && $scope.publications){
  		$scope.publications.forEach(function(publication, index){
  			 publication.selected = true;
  		}); 
  	}else if($scope.publications){
  		$scope.publications.forEach(function(publication, index){
    			publication.selected = false;
  		}); 
  	}

  }

	// -------- /toggle all

	settingsPublicationsCtrl = $scope;

	$scope.getTotalPublicationCount = function(){
		if($scope.settings.tab == "drafts"){
		}
		if($scope.settings.tab == "publications"){
		}
		if($scope.settings.tab == "scheduled"){
		}
		if($scope.settings.tab == "trash"){
		}
	}

	$scope.getShowingPublicationsLength = function(){
		if($scope.settings.tab == "drafts"){
		}
		if($scope.settings.tab == "publications"){
			return $scope.publications && $scope.publications.length ? $scope.publications.length : 0;
		}
		if($scope.settings.tab == "scheduled"){
		}
		if($scope.settings.tab == "trash"){
		}
	}

	$scope.getSelectedPublicationsIds = function(){
		if($scope.settings.tab == "drafts"){
			return getSelectedPublicationIds('drafts');
		}
		if($scope.settings.tab == "publications"){
			return getSelectedPublicationIds('publications');
		}
		if($scope.settings.tab == "scheduled"){
			return getSelectedPublicationIds('scheduled');
		}
		if($scope.settings.tab == "trash"){
			return getSelectedPublicationIds('trash');
		}
		return null;
	}

	var getSelectedPublicationIds = function(type){
		var ret = []
    $scope[type].forEach(function(pub, index){
      if(pub.selected)
        ret.push(pub.id);
    });
    return ret;
	}

	$scope.newComment = '';
	$scope.postComment = function(post, body){
		var comment = {
			post: PostDto.getSelf(post),
			author: PersonDto.getSelf($scope.app.person),
			body: body
		}
		trix.postComment(comment).success(function(response){
			var c = {
				post: post,
				author: $scope.app.person,
				body: body,
				date: new Date()
			}

			if(!$scope.comments || !$scope.comments.length)
				$scope.comments = [];

			$scope.comments.unshift(c);
			post.commentsCount++;
			$scope.newComment = '';
		})
	}

}]);

var settingsPublicationsCtrl = null;