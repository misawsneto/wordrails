app.controller('CategoryCtrl', ['$scope', '$rootScope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage', '$sce', 'category',
	function($scope , $rootScope,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage, $sce, category){

	$scope.category = category;
	$scope.postsPage = 0
	trix.findPostsByTerm(category.termId, $scope.postsPage, 20, null).success(function(response){
      $scope.postViews = response;
      if($scope.postViews && $scope.postViews.length > 0)
        $scope.postsPage++;
    })

    $scope.reloadMasonry = function(){
		$rootScope.$broadcast('masonry.reload');
	}

    $scope.loadComments = function(post){
		if(!post.showComments){
			post.loadingComments = true;
			$timeout(function(){
				post.showComments = true; 
				$scope.reloadMasonry()
				post.loadingComments = false;
			},500)
		}
	}
}]);