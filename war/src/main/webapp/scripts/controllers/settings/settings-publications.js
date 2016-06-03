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

 // ---------- paginate posts ------

  $scope.$watch('settings.tab', function(){
    $scope.doSearch();
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
  
  $scope.publicationsCtrl = {
    'page': 0,
    'allLoaded': false,
    'window': 20
  }

  $scope.resetPage = function(){
    $scope.publicationsCtrl.page = 0;
    $scope.publicationsCtrl.allLoaded = false;
    $scope.publications = [];
  }

  $scope.paginate = function(){
    if(!$scope.loading && !$scope.publicationsCtrl.allLoaded){
      $scope.loading = true;

      trix.searchPosts($scope.searchQuery, null, null, tabToState().toLowerCase(), null, null, null, null, $scope.publicationsCtrl.page, 20, '-date', ['body', 'tags', 'categories', 'imageHash', 'state'], false).success(function(response){
        handleSuccess(response);
        $scope.loading = false;
      }).error(function(){
        $scope.loading = false;
        $scope.publicationsCtrl.allLoaded = true;
      })
    }
  }

  var handleSuccess = function(posts){
    if(posts && posts.length > 0){
      posts.reverse();

        if(!$scope.publications)
          $scope.publications = []

        posts.forEach(function(post){
          addSnippet(post);
          $scope.publications.push(post);
        })
        $scope.publicationsCtrl.page++;
        // if(posts.length < $scope.publicationsCtrl.window)
        //   $scope.publicationsCtrl.allLoaded = true;
    }else
      $scope.publicationsCtrl.allLoaded = true;
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
	    $scope.publications.forEach(function(pub, index){
	      if(pub.selected)
	        ret.push(pub.id);
	    });
	    return ret;
	}

	  // --------- move to state
  
    $scope.toState = null;
    var intToState = function(state){
      // if(!$scope.app.editingPost)
      //  return null;
      if(state == 1){
        return "PUBLISHED";
      }else if(state == 2){
        return "DRAFT";
      }else if(state == 3){
        return "SCHEDULED";
      }else if(state == 4){
        return "TRASH";
      }else{
        return 5;
      }
    }


  $scope.toMovePublication = null;
  $scope.showMoveToDialog = function(event, publication){
    $scope.toState = null;
    $scope.toMovePublication = publication;
    $mdDialog.show({
        scope: $scope,        // use parent scope in template
        closeTo: {
          bottom: 1500
        },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'move-to-dialog.html',
        parent: angular.element(document.body),
        targetEvent: event,
        clickOutsideToClose:true
        // onComplete: function(){

        // }
      })
  }

  $scope.movePublicationToState = function(state){
    trix.getPost($scope.toMovePublication.id).success(function(response){
      response.state = intToState(state);
      trix.putPost(response).success(function(){
        if($scope.publications)
        for (var i = $scope.publications.length - 1; i >= 0; i--) {
          if($scope.publications[i].id == $scope.toMovePublication.id)
            $scope.publications.splice(i,1);
        }
        $scope.app.showSuccessToast($filter('translate')('messages.SUCCESS_MSG'))
        $mdDialog.cancel();
      })
    })
  }
  // --------- /move to state

}]);

var settingsPublicationsCtrl = null;