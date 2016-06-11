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

  $scope.getCategory = function(id){
    var ret = null;
    $scope.thisStation.categories && $scope.thisStation.categories.forEach(function(category){
      if(id === category.id)
        ret = category;
    })
    return ret;
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
  trix.findPerspectiveView(perspective.id, null, null, 0, 10).success(function(termPerspective){
    perspective.termPerspectiveView = termPerspective;
    perspective.termPerspectiveView.ordinaryRows && perspective.termPerspectiveView.ordinaryRows.forEach(function(row){
      row.category = $scope.getCategory(row.termId);
    })
  })
}


settingsPerspectivesCtrl = $scope;

}]);

var settingsPerspectivesCtrl = null;