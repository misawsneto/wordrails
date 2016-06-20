app.controller('ReadCtrl', ['$scope', '$rootScope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage', '$sce', 'station', 'post',
	function($scope , $rootScope,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage, $sce, station, post){

		$scope.post = post;

    post.body = '<br><video src="http://d3a3w0au60g0o7.cloudfront.net/demo/videos/19435ba18199c4b4cf967391768b3fc3" controls=""></video><br><br><video src="http://d3a3w0au60g0o7.cloudfront.net/demo/videos/f9052abae769c6f2aaa9041e2a459f48" controls=""></video><br>'
    $scope.post.body = $sce.trustAsHtml(post.body)

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
    $('#scroll-box').animate({scrollTop: 0}, 0);
  })

  $scope.$on('$destroy',function(){
      if(intervalPromise)
          $interval.cancel(intervalPromise);   
  });

  readCtrl = $scope;
}]);

var readCtrl = null;