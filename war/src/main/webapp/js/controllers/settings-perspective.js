app.controller('SettingsPerspectiveEditorCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location){

		$scope.isMobile = true;

		$scope.stationPerspective = null;

		$scope.thisStation = {}
    $scope.app.initData.stations.forEach(function(station, index){
      if($state.params.stationId == station.id){
        $scope.stationName = station.name;
        $scope.stationId = station.id;
        $scope.thisStation = station;
      }
    });

    trix.findPerspectiveView($scope.thisStation.defaultPerspectiveId, null, null, 0, 10).success(function(termPerspective){
    	$scope.termPerspectiveView = termPerspective;
    	$timeout(function() {
    		$('.mockup-section.bg-perspective').slimScroll({
					height:'476',
					size:'8px',
					'railVisible': true
				})
    	});
    })

}]);