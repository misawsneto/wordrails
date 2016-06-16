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
					$scope.reloadMasonry();
			})
		}else
			$scope.noBookmarks = true;

		$scope.postLoaded = null;

		$scope.showRemoveBookmarkDialog = function(event, post){
			$scope.postLoaded = post;
			$mdDialog.show({
				scope: $scope,        // use parent scope in template
        	closeTo: {
          bottom: 1500
        },
      	preserveScope: true, // do not forget this if use parent scope
				controller: $scope.app.defaultDialog,
				templateUrl: 'remove-bookmark-dialog.html',
				parent: angular.element(document.body),
				targetEvent: event,
				clickOutsideToClose:true
				// onComplete: function(){

				// }
			})
		}

		$scope.disabled = false;
		$scope.removeBookmark = function(post){
			trix.getPerson($scope.app.person.id).success(function(person){
				for (var i = person.bookmarkPosts.length - 1; i >= 0; i--) {
					if(person.bookmarkPosts[i] == post.id){
						person.bookmarkPosts.splice(i, 1);
					}
				}

				for (var i = $scope.app.person.bookmarkPosts.length - 1; i >= 0; i--) {
					if($scope.app.person.bookmarkPosts[i] == post.id){
						$scope.app.person.bookmarkPosts.splice(i, 1);
					}
				}

				trix.putPerson(person).success(function(){
					for (var i = $scope.postViews.length - 1; i >= 0; i--) {
						if($scope.postViews[i].id == post.id){
							$scope.postViews.splice(i, 1);
							$scope.reloadMasonry();
						}
					}
					if($scope.postViews.length == 0){
						$timeout(function() {
							$scope.noBookmarks = true;
						}, 200);
					}
					$mdDialog.cancel();
					$scope.disabled = false;
				}).error(function(){
					$scope.disabled = false;
					$mdDialog.cancel();
				})
			})
		}

	  $timeout(function(){
		$scope.app.removeTermTabs();
	  })

	bookmarksCtrl = $scope;

}]);

var bookmarksCtrl = null;