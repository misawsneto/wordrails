app.controller('ProfileCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage', 'person',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage, person){

    $scope.person = person;

  // ---------- paginate posts ------

  
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
      trix.searchPosts($scope.searchQuery, [$scope.person.id], null, 'published', null, null, null, null, page, 10, '-date', ['snippet', 'tags', 'categories', 'imageHash', 'state'], false).success(function(response){
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
          //addSnippet(post);
          $scope.publications.push(post);
          console.log($scope.app.getStationById(post.stationId).stationSlug);
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

	settingsProfileCtrl = $scope;
}]);

var settingsProfileCtrl = null;

