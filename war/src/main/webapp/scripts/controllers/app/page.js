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
			cell.loadingComments = true;
			$timeout(function(){
				cell.showComments = true; 
				$scope.reloadMasonry()
				cell.loadingComments = false;
			},3)
		}

}]);

var pageCtrl = null;
