app.controller('SettingsPublicationsCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdToast', '$translate', '$mdSidenav',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix , FileUploader, TRIX, cfpLoadingBar, $mdToast, $translate, $mdSidenav){

		$scope.toggleAdvancedSearch = buildToggler('advanced-search');

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

	    $scope.toggleLeft = buildDelayedToggler('left');

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
		// trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': $scope.app.getLoggedPerson().id,
			// 'publicationType': 'DRAFT', sortByDate: true}).success(function(response){
				trix.getPersonNetworkPostsByState(null, 'DRAFT', $scope.app.publicationsCtrl.page, 10).success(function(response){
					$scope.drafts = response;
					$scope.firstLoad = true;
				})
			}
			if(/*$state.params.type == "publications"*/ $scope.settings.tab == "publications"){
				trix.getPersonNetworkPostsByState(null, 'PUBLISHED', $scope.app.publicationsCtrl.page, 10).success(function(response){
					$scope.publications = response;
					$scope.firstLoad = true;
				})
			}
			if(/*$state.params.type == "scheduled"*/ $scope.settings.tab == "scheduled"){
				trix.getPersonNetworkPostsByState(null, 'SCHEDULED', $scope.app.publicationsCtrl.page, 10).success(function(response){
					$scope.scheduleds = response;
					$scope.firstLoad = true;
				})
			}
			if(/*$state.params.type == "scheduled"*/ $scope.settings.tab == "trash"){
				trix.getPersonNetworkPostsByState(null, 'TRASH', $scope.app.publicationsCtrl.page, 10).success(function(response){
					$scope.scheduleds = response;
					$scope.firstLoad = true;
				})
			}
		});

$scope.$on('POST_REMOVED', function(event, postId){
	if($scope.app.publicationsCtrl && $scope.app.publicationsCtrl.publications){
		for (var i = $scope.app.publicationsCtrl.publications.length - 1; i >= 0; i--) {
			if(postId == $scope.app.publicationsCtrl.publications[i].postId)
				$scope.app.publicationsCtrl.publications.splice(i,1)
		};
	}
})

$scope.paginate = function(){

	if(!$scope.app.publicationsCtrl.publications || $scope.app.publicationsCtrl.publications.length == 0)
		return;

	if($scope.allLoaded)
		return;

	var type = '';
	if($state.params.type == "drafts"){
		type = 'DRAFT'
	}else if($state.params.type == "publications"){
		type = 'PUBLISHED'
	}else if($state.params.type == "scheduled"){
		type = 'SCHEDULED'
	}

	if(!$scope.loadingPage){
		$scope.loadingPage = true;
			/*trix.searchPosts(null, $scope.app.publicationsCtrl.page + 1, 10, {'personId': $scope.app.getLoggedPerson().id,
				'publicationType': type, sortByDate: true}).success(function(response){*/

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
					$scope.app.publicationsCtrl.publications.push(element)
				}); 

			})
			.error(function(){
				$scope.loadingPage = false;
			})
		}
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

	$scope.createComment = function(){
		var comment = {}
		comment = angular.copy($scope.newComment);
		comment.author = extractSelf($scope.app.initData.person)
		comment.post =TRIX.baseUrl + '/api/posts/' + $scope.app.nowReading.postId

		trix.postComment(comment).success(function(response){
			if(!$scope.comments || $scope.comments.length == 0)
				$scope.comments = [];

			comment.author = angular.copy($scope.app.initData.person)
			comment.date = new Date().getTime();
			$scope.newComment = {body: ''};

			$scope.comments.unshift(comment)

		}).error(function(response,status){
			$scope.app.showErrorToast('Houve um erro inesperado. Tente novamente.')

		})
	}

	$scope.commentFocused = false;
	$scope.commentFocus = function(){
		$scope.commentFocused = true;
	}
	$scope.commentBlur = function(){
		$scope.commentFocused = false;
	}

}]);