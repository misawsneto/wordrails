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

	    $scope.page = 1;

	    if($scope.app.termPerspectiveView.homeRow && $scope.app.termPerspectiveView.homeRow.cells){
	    	$scope.app.termPerspectiveView.homeRow.allLoaded = false;
	    	var length = $scope.app.termPerspectiveView.homeRow.cells.length >= 10 ? 10 : $scope.app.termPerspectiveView.homeRow.cells.length;
	    	$scope.app.termPerspectiveView.homeRow.cells = $scope.app.termPerspectiveView.homeRow.cells.slice(0,length);
	    }

	    $scope.loadingPerspective = false;
	    $scope.app.perspectivePaginate = function(){
	    	$scope.app.termPerspectiveView.homeRow.page = $scope.page
	    	if(!$scope.loadingPerspective && $scope.app.termPerspectiveView.homeRow && $scope.app.termPerspectiveView.homeRow.cells && !$scope.app.termPerspectiveView.homeRow.allLoaded){
		    	$scope.loadingPerspective = true;
		    	trix.getRowView($scope.app.currentStation.defaultPerspectiveId, $scope.app.termPerspectiveView.id, null, $scope.page, 20)
		    	.success(function(response){
		            if(response.cells && response.cells.length > 0){
		                response.cells.forEach(function(cell, index){
		                   $scope.app.termPerspectiveView.homeRow.cells.push(cell)
		               	});
		                $scope.page = $scope.app.termPerspectiveView.homeRow.page = $scope.app.termPerspectiveView.homeRow.page + 1
		                $scope.reloadMasonry();
		                $scope.loadingPerspective = false;
		            }else{
		                $scope.app.termPerspectiveView.homeRow.allLoaded = true;
		            }

		    	})
    		}
	    }

}]);

var pageCtrl = null;
