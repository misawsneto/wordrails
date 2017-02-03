app.controller('CategoryCtrl', ['$scope', '$rootScope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage', '$sce', 'category',
	function($scope , $rootScope,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage, $sce, category){

	$scope.category = category;
	$scope.app.activeCategory = category;
	$scope.postsPage = 0
	var allLoaded = false;
	var loading = false;
	// trix.findPostsByTerm(category.termId, $scope.postsPage, 20, null).success(function(response){
 //      $scope.postViews = response;
 //      if($scope.postViews && $scope.postViews.length > 0)
 //        $scope.postsPage++;
 //      else
 //      	allLoaded = true;
 //    })

 	$scope.servicesCat = category.id == 1974 ||  category.id == 1973 || category.id == 1970 || category.id == 1953 || category.id == 1981;

 	$scope.menu2.navigationState = category.id;
    $scope.paginate = function(){
    	if(!loading && !allLoaded){
    		loading = true;
	    	trix.findPostsByTerm(category.termId, $scope.postsPage, 20, null).success(function(response){
	    		if(!$scope.postViews)
	    			$scope.postViews = []
		      if(response && response.length > 0){
		      	response.forEach(function(post){
		      		$scope.postViews.push(post);
		      	})
		        $scope.postsPage++;
		        $scope.reloadMasonry();
		      }else{
		      	allLoaded = true;
		      }
		      loading = false;
		    }).error(function(){
	    		loading = false;
		    })	
		}
    }

	// --------- scroll to top
  
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

  // --------- /scroll to top
}]);