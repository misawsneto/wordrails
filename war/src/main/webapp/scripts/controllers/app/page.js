app.controller('PageCtrl', ['$scope', '$rootScope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage', '$sce',
	function($scope , $rootScope,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage, $sce){

		$timeout(function() {
			$('.materialboxed').materialbox();
		}, 2000);

		pageCtrl = $scope;

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
		

		// sidenav toggle
		$scope.toggleComments = buildToggler('post-comments');

		function buildToggler(navID) {
	      return function() {
	        $mdSidenav(navID)
	          .toggle()
	      }
	    }

}]);

var pageCtrl = null;
