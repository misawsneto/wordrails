app.controller('ReadCtrl', ['$scope', '$rootScope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage', '$sce', 'station', 'post',
	function($scope , $rootScope,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage, $sce, station, post){

		$scope.post = post;

    $scope.app.loadComments($scope.post)

  var findRelated = function(size, categories){
    trix.searchPosts(null, null, null, 'published', null, null, categories, null, 0, size, '-date', ['snippet', 'tags', 'categories', 'imageHash', 'state'], false).success(function(posts,a,b,c){
        if(posts && posts.length > 0){
          posts.reverse();
          if(!$scope.related)
            $scope.related = []

          posts.forEach(function(postObj){
            if(postObj.id != post.id)
              $scope.related.push(postObj);
          })

          $scope.reloadMasonry();
      }
    })
  }

  if($scope.post.terms && $scope.post.terms.length > 0){
    if($scope.post.terms.length == 1){
      findRelated(9, [$scope.post.terms[0].id]);
    }

    if($scope.post.terms.length == 2){
      findRelated(3, [$scope.post.terms[0].id]);
      findRelated(3, [$scope.post.terms[1].id]);
    }

    if($scope.post.terms.length > 2){
      findRelated(3, [$scope.post.terms[0].id]);
      findRelated(3, [$scope.post.terms[1].id]);
      findRelated(3, [$scope.post.terms[2].id]);
    }
  }

  // --------- /scroll to top
  
  var intervalPromise;
  intervalPromise = $interval(function(){
    if($('#scroll-box').scrollTop() > 400 && $('main').height() - 400)
      $scope.showScrollUp = true;
    else
      $scope.showScrollUp = false;
  }, 500);

  $scope.scrollToTop = function(){
    $('#scroll-box').animate({scrollTop: 0}, 700, 'easeOutQuint');
  }

  $timeout(function(){
    $('#scroll-box').animate({scrollTop: 0}, 700, 'easeOutQuint');
  })

  $scope.$on('$destroy',function(){
      if(intervalPromise)
          $interval.cancel(intervalPromise);   
  });

  readCtrl = $scope;
}]);

var readCtrl = null;