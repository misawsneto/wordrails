app.controller('SettingsProfileCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage', 'ngJcropConfig',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage, ngJcropConfig){

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

      $scope.app.publicationsCtrl = {page: 0, firstLoad: false};

      
    $scope.$watch('settings.tab', function(){
      if(/*$state.params.type == "drafts"*/ $scope.settings.tab == "drafts"){
        // trix.findByAuthorUsernameAndStateOrderByDateDesc($scope.app.person.username, 'DRAFT', $scope.app.publicationsCtrl.page, 10).success(function(response){
        //   $scope.drafts = response.posts;
        //   $scope.firstLoad = true;
        // })
      }
      if(/*$state.params.type == "publications"*/ $scope.settings.tab == "publications"){
        // trix.findByAuthorUsernameAndStateOrderByDateDesc($scope.app.person.username, 'PUBLISHED', $scope.app.publicationsCtrl.page, 10).success(function(response){
        //   $scope.publications = response.posts;
        //   $scope.firstLoad = true;
        // })
      }
      if(/*$state.params.type == "scheduled"*/ $scope.settings.tab == "scheduled"){
        // trix.findByAuthorUsernameAndStateOrderByDateDesc($scope.app.person.username, 'SCHEDULED', $scope.app.publicationsCtrl.page, 10).success(function(response){
        //   $scope.scheduleds = response.posts;
        //   $scope.firstLoad = true;
        // })
      }
      if(/*$state.params.type == "scheduled"*/ $scope.settings.tab == "trash"){
        // trix.findByAuthorUsernameAndStateOrderByDateDesc($scope.app.person.username, 'TRASH', $scope.app.publicationsCtrl.page, 10).success(function(response){
        //   $scope.scheduleds = response.posts;
        //   $scope.firstLoad = true;
        // })
      }
    });

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

  var type = '';

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

  // if(!$scope.loadingPage){
  //   $scope.loadingPage = true;
  //     /*trix.searchPosts(null, $scope.app.publicationsCtrl.page + 1, 10, {'personId': $scope.app.getLoggedPerson().id,
  //       'publicationType': type, sortByDate: true}).success(function(response){*/

  //     trix.findByAuthorUsernameAndStateOrderByDateDesc($scope.app.person.username, type, $scope.app.publicationsCtrl.page+1, 10).success(function(response){
  //       var posts = response.posts;

  //       $scope.loadingPage = false;
  //       $scope.app.publicationsCtrl.page = $scope.app.publicationsCtrl.page + 1;

  //       if(!posts || posts.length == 0){
  //         $scope.allLoaded = true;
  //         return;
  //       }

  //       if(!$scope.pages)
  //         $scope.pages = []

  //       posts && posts.forEach(function(element, index){
  //         $scope.publications.push(element)
  //       }); 

  //     })
  //     .error(function(){
  //       $scope.loadingPage = false;
  //     })
  //   }
  }

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

  // $scope.createComment = function(){
  //   var comment = {}
  //   comment = angular.copy($scope.newComment);
  //   comment.author = extractSelf($scope.app.initData.person)
  //   comment.post =TRIX.baseUrl + '/api/posts/' + $scope.app.nowReading.postId

  //   trix.postComment(comment).success(function(response){
  //     if(!$scope.comments || $scope.comments.length == 0)
  //       $scope.comments = [];

  //     comment.author = angular.copy($scope.app.initData.person)
  //     comment.date = new Date().getTime();
  //     $scope.newComment = {body: ''};

  //     $scope.comments.unshift(comment)

  //   }).error(function(response,status){
  //     $scope.app.showErrorToast('Houve um erro inesperado. Tente novamente.')

  //   })
  // }

  $scope.commentFocused = false;
  $scope.commentFocus = function(){
    $scope.commentFocused = true;
  }
  $scope.commentBlur = function(){
    $scope.commentFocused = false;
  }

  // --- /publications ------------

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
  
  trix.searchPosts($scope.searchQuery, null, null, tabToState().toLowerCase(), null, null, null, null, $scope.app.publicationsCtrl.page, $scope.window, '-date', null, true).success(function(response){
    response.reverse();
    $scope.publications = response;
  })

	settingsProfileCtrl = $scope;
}]);

var settingsProfileCtrl = null;

