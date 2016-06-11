app.controller('SettingsPerspectivesCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdDialog', '$mdToast', '$filter', '$translate', '$mdConstant', '$mdSidenav', 'station',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix, FileUploader, TRIX, cfpLoadingBar, $mdDialog, $mdToast, $filter, $translate, $mdConstant, $mdSidenav, station ){

	$scope.togglePerspectives = buildToggler('perspective-list');

	$scope.thisStation = station;

		function buildToggler(navID) {
	    return function() {
	      $mdSidenav(navID)
	        .toggle()
	    }
  }

  var addSnippet = function(postObj){
    if(postObj.body)
      postObj.snippet = postObj.body.simpleSnippet();
  }

  $scope.getSnippet = function(body){
    if(body)
      return body.simpleSnippet();
  }


  trix.getStationStationPerspectives($scope.thisStation.id).success(function(response){
    $scope.stationPerspectives = response.stationPerspectives;
      $scope.stationPerspectives.forEach(function(perspective, index){
        if(perspective.id == $scope.thisStation.defaultPerspectiveId){
          perspective.selected = true;
          $scope.currentPerspective = perspective;
        }
      getPerspectiveView(perspective)
    });
  })

var getPerspectiveView = function(perspective){
  trix.findPerspectiveView(perspective.id, null, null, 0, 50).success(function(termPerspective){
    perspective.termPerspectiveView = termPerspective;
  })
}


settingsPerspectivesCtrl = $scope;

}]);

var settingsPerspectivesCtrl = null;