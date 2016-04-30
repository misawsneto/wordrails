app.controller('BookmarksCtrl', ['$scope', '$rootScope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage', '$sce',
	function($scope , $rootScope,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage, $sce){

		if($scope.app.person.bookmarkPosts && $scope.app.person.bookmarkPosts.length){
			trix.findPostsByIds($scope.app.person.bookmarkPosts).success(function(posts){
				$scope.postViews = []
				$scope.app.person.bookmarkPosts.forEach(function(pid){
					posts.forEach(function(postView){
						if(pid == postView.id)
							$scope.postViews.push(postView)
					})
				})
				if($scope.postViews && $scope.postViews.length > 0)
					$rootScope.$broadcast('masonry.reload');
			})
		}else
			$scope.noBookmarks = true;

}]);