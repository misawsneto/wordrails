app.controller('PageCtrl', ['$scope', '$rootScope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage', '$sce',
	function($scope , $rootScope,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage, $sce){

		$timeout(function() {
			$('.materialboxed').materialbox();
		}, 2000);

		pageCtrl = $scope;

		$scope.reloadMasonry = function(){
			$rootScope.$broadcast('masonry.reload');
		}

		$scope.loadComments = function(cell){
			if(!cell.showComments){
				cell.loadingComments = true;
				$timeout(function(){
					cell.showComments = true; 
					$scope.reloadMasonry()
					cell.loadingComments = false;
				},500)
			}
		}

		$scope.sendComment = function(cell){

		}

		// --- clear post
		$scope.showSharesPostDialog = function(event){
			// show term alert
			
			$mdDialog.show({
				scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
      	preserveScope: true, // do not forget this if use parent scope
				controller: $scope.app.defaultDialog,
				templateUrl: 'social-share.html',
				parent: angular.element(document.body),
				targetEvent: event,
				clickOutsideToClose:true
				// onComplete: function(){

				// }
			})
		}

}]);

var pageCtrl = null;
