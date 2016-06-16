app.controller('PageCtrl', ['$scope', '$rootScope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage', '$sce', 'station',
	function($scope , $rootScope,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage, $sce, station){

			$scope.app.defaultStation = trixService.selectDefaultStation($scope.app.stations, $scope.app.defaultStation ? $scope.app.defaultStation.stationId : null);
      $scope.app.stationsPermissions = trixService.getStationPermissions(angular.copy($scope.app));

	    $scope.thisStation = station ? station : $scope.app.defaultStation;

	    $scope.app.page = 1;

	    $scope.loadingPerspective = false;
	    $scope.app.perspectivePaginate = function(){
	    	if(!$scope.loadingPerspective && $scope.app.termPerspectiveView && $scope.app.termPerspectiveView.homeRow && $scope.app.termPerspectiveView.homeRow.cells && !$scope.app.termPerspectiveView.homeRow.allLoaded){
	    		$scope.app.termPerspectiveView.homeRow.page = $scope.app.page
		    	$scope.loadingPerspective = true;
		    	trix.getRowView($scope.thisStation.defaultPerspectiveId, $scope.app.termPerspectiveView.id, null, $scope.app.page, 20)
		    	.success(function(response){
		            if(response.cells && response.cells.length > 0){
		                response.cells.forEach(function(cell, index){
		                		if(cell.postView.stationId === $scope.thisStation.id)
		                   	$scope.app.termPerspectiveView.homeRow.cells.push(cell)
		               	});
		                $scope.app.page = $scope.app.termPerspectiveView.homeRow.page = $scope.app.termPerspectiveView.homeRow.page + 1
		                $scope.reloadMasonry();
		                $scope.loadingPerspective = false;
		            }else{
		                $scope.app.termPerspectiveView.homeRow.allLoaded = true;
		            }

		    	})
    		}
	    }

	    if($state.includes('app.home')){
	    	if(!$scope.app.termPerspectiveView || ($scope.thisStation.id !== $scope.app.termPerspectiveView.stationId)){
	    		if($scope.app.termPerspectiveView && $scope.app.termPerspectiveView.featuredRow)
	    			$scope.app.termPerspectiveView = null;
	    		$scope.app.loadPerspective($scope.thisStation);
	    	}else{
	    		if($scope.app.perspectiveTerms == null)
	    			$scope.app.loadPerspectiveTerms()
	    	}
	    }

	    $timeout(function(){
    $('#scroll-box').animate({scrollTop: 0}, 0, 'easeOutQuint');
  })

	pageCtrl = $scope;
}]);

var pageCtrl = null;
