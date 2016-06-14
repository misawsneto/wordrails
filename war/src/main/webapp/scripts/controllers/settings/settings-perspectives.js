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

  var loadPerspectives = function(){
    trix.getStationStationPerspectives($scope.thisStation.id).success(function(response){
      $scope.stationPerspectives = response.stationPerspectives;
        $scope.stationPerspectives && $scope.stationPerspectives.forEach(function(perspective, index){
          if(perspective.id == $scope.thisStation.defaultPerspectiveId){
            perspective.selected = true;
            $scope.currentPerspective = perspective;
          }
        getPerspectiveView(perspective)
      });
    })
  }

  loadPerspectives();

  var getPerspectiveView = function(perspective){
    trix.findPerspectiveView(perspective.id, null, null, 0, 10).success(function(termPerspective){
      perspective.termPerspectiveView = termPerspective;
      perspective.termPerspectiveView.ordinaryRows && perspective.termPerspectiveView.ordinaryRows.forEach(function(row){
        row.category = $scope.getCategory(row.termId);
      })
    })
  }

  $scope.showAddPerspectiveDialog = function(event){
    $mdDialog.show({
      scope: $scope,        // use parent scope in template
      closeTo: {
        bottom: 1500
      },
      preserveScope: true, // do not forget this if use parent scope
      controller: $scope.app.defaultDialog,
      templateUrl: 'add-perspective-dialog.html',
      parent: angular.element(document.body),
      targetEvent: event,
      clickOutsideToClose:true
      // onComplete: function(){
        // }
    })
  }

  $scope.addPerspective = function(perspective){
    var stationPerspective = {};
    stationPerspective.name = perspective.name;
    stationPerspective.station = TRIX.baseUrl + "/api/stations/" + $scope.thisStation.id;
    stationPerspective.taxonomy = TRIX.baseUrl + "/api/stations/" + $scope.thisStation.categoriesTaxonomyId;


    trix.postStationPerspective(stationPerspective).success(function(response){
      loadPerspectives();
      $mdDialog.cancel();
      $scope.app.showSuccessToast('Perspectiva criada com sucesso.')
    }).error(function(response){
      $scope.app.showErrorToast('Erro ao criar perspectiva.')
    })
  }

settingsPerspectivesCtrl = $scope;

}]);

var settingsPerspectivesCtrl = null;