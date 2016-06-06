app.controller('ProfileCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage){

  // --- profile settings ----------
	// $scope.showEditProfile = function(ev){
 //  	$scope.editingPerson = angular.copy($scope.app.person);
 //  	$mdSidenav('edit-profile').toggle();
 //  }

 //  $scope.cancelEditProfile = function(){
 //  	$scope.editingPerson = angular.copy($scope.app.person);
 //  	$mdSidenav('edit-profile').close()
 //  }

    $scope.showEditProfile = function(event){
      // show term alert
      $mdDialog.show({
        scope: $scope,        // use parent scope in template
        closeTo: {
          bottom: 1500
        },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'edit-profile-dialog.html',
        parent: angular.element(document.body),
        targetEvent: event,
        clickOutsideToClose:true
        // onComplete: function(){

        // }
      })
  }

	// -------------------  ---------------

	// $interval(function(){
 //    if($state.includes('app.profile')){
 //      var sidenav = $mdSidenav('edit-profile')
 //      if(sidenav && !sidenav.isOpen())
 //        $scope.editingPerson = angular.copy($scope.app.person);
 //    }	
 //   }, 500);

  $scope.editingPerson = angular.copy($scope.app.person);

  $scope.saveProfile = function(){
  	trix.updatePerson($scope.editingPerson).error(function(data, status, header){
  		var error = data.error;

  		if(error.indexOf('Password no equal') > -1)
  			$scope.app.showErrorToast('Confirme que as senhas sejam iguais e tente novamente')

  		if(error.indexOf('UnauthorizedException') > -1)
  			$scope.app.showErrorToast('Você não tem permissão para realizar esta operação')

  		if(error.indexOf('Invalid Password') > -1)
  			$scope.app.showErrorToast('A senha deve ter mais de 5 caractéres')

  		if(error.indexOf('Not email') > -1)
  			$scope.app.showErrorToast('Email inválido')

  		if(error.indexOf('Invalid username') > -1)
  			$scope.app.showErrorToast('Nome do usuário deve ter ao menos 3 caractéres,<br> não possuir espaços ou caractéres caractéres especiais')
  	}).success(function(){
  		$scope.editingPerson.password = null;
  		$scope.editingPerson.passwordConfirm = null;
  		$scope.person = $scope.app.person = $scope.editingPerson;
			$scope.app.showSuccessToast('Alterações realizadas com sucesso')
			$scope.editingPerson = angular.copy($scope.app.person);
      $mdDialog.cancel();
  	});
  }

  // --- /profile settings ----------

  // --- publications ----------

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
    'allLoaded': false
  }

  $scope.resetPage = function(){
    $scope.publicationsCtrl.page = 0;
    $scope.publicationsCtrl.allLoaded = false;
    $scope.publications = [];
  }

  $scope.paginate = function(){
    if(!$scope.loading && !$scope.publicationsCtrl.allLoaded){
      $scope.loading = true;

      var page = getPage();
      trix.searchPosts($scope.searchQuery, [$scope.app.person.id], null, tabToState().toLowerCase(), null, null, null, null, page, 20, '-date', ['body', 'tags', 'categories', 'imageHash', 'state'], false).success(function(response){
        handleSuccess(response);
        $scope.loading = false;
      }).error(function(){
        $scope.loading = false;
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
        $scope.publicationsCtrl.allLoaded;
    }else
      $scope.publicationsCtrl.allLoaded = true;
  }


  var getPage = function(){
    return $scope.publicationsCtrl.page;
  }

  $scope.doSearch = function(){
    $scope.resetPage();
    $scope.paginate();
  }

  // ---------- /paginate posts ------

  $scope.page = 0;
  $scope.loadingComments = true
  $scope.allLoaded = false;
  $scope.beginning = true;
  $scope.window = 20

  $scope.showComments = function(postId){
    $scope.toggleCommentsSidebar();
    $scope.comments = []
    $scope.loadingComments = true
    if(postId)
      trix.findPostCommentsOrderByDate(postId, $scope.page, $scope.window, null, 'commentProjection').success(function(response){
        $scope.comments = response.comments;
        $scope.loadingComments = false
      }).error(function(){
        $scope.comments = null;
        $scope.loadingComments = false
      })
  }

  $scope.toggleCommentsSidebar = function(){
    $mdSidenav('comments-list').toggle();
  }

  $scope.commentFocused = false;
  $scope.commentFocus = function(){
    $scope.commentFocused = true;
  }
  $scope.commentBlur = function(){
    $scope.commentFocused = false;
  }

  // --- /publications ------------


  var addSnippet = function(postObj){
    if(postObj.body)
      postObj.snippet = postObj.body.simpleSnippet();
  }

  $scope.showPost = function(postObj){
    // postObj.loading = true;
    var id = postObj.id
    postObj.terms = postObj.categories;
    // trix.getPost(id, 'postProjection').success(function(response){
    //   postObj = response;

    var hash = postObj.imageHash;
    postObj.featuredImage = postObj.featuredImage;
    postObj.landscape = postObj.imageLandscape;

    postObj.postFeaturedImage = $filter('imageLink')({imageHash: hash}, 'large')

    postObj.useHeading = postObj.topper ? true:false
    postObj.useSubheading = postObj.subheading ? true:false;
    //   postObj.loading = false;
    // }).error(function(){
    //   postObj.loading = false;
    // })
  }

  // --------- scroll to top
  
  var intervalPromise;
  intervalPromise = $interval(function(){
    if($('#scroll-box').scrollTop() > 400)
      $scope.showScrollUp = true;
    else
      $scope.showScrollUp = false;
  }, 500);

  $scope.scrollToTop = function(){
    $('#scroll-box').animate({scrollTop: 0}, 700, 'easeOutQuint');
  }

  $scope.$on('$destroy',function(){
      if(intervalPromise)
          $interval.cancel(intervalPromise);   
  });

  // --------- /scroll to top

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
  
  function buildToggler(navID) {
      return function() {
        $mdSidenav(navID)
          .toggle()
          .then(function () {
            $log.debug("toggle " + navID + " is done");
          });
      }
    }

  $scope.app.toggleComments = buildToggler('post-comments');

	settingsProfileCtrl = $scope;
}]);

var settingsProfileCtrl = null;

