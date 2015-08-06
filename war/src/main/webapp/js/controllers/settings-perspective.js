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

        // trix.getTermTree($state.params.perspectiveId).success(function(termTree){
        //   $scope.terms = termTree;
        // })

        trix.findPerspectiveView($state.params.perspectiveId, null, null, 0, 10).success(function(termPerspective){
           $scope.termPerspectiveView = termPerspective;
           $timeout(function() {
              $('.mockup-section.bg-perspective').slimScroll({
               height:'476',
               size:'8px',
               'railVisible': true
           })

          });

           $timeout(function() {
              // $('#sortable').sortable('disable');
              // $('#sortable').sortable('reload');
              // $('#sortable').bind
            },200);
       })

    }]);

app.controller('SettingsPerspectiveListCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location',
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

        $scope.changeDefaultPerspective = function(perspective){
          $scope.perspectives.forEach(function(item, index){
            if(perspective.id == item.id)
              item.selected = true;
            else
              item.selected = false;
          });
        }

        trix.getStationStationPerspectives($scope.thisStation.id).success(function(response){
            $scope.perspectives = response.stationPerspectives;
            $scope.perspectives.forEach(function(perspective, index){
              if(perspective.id == $scope.thisStation.defaultPerspectiveId)
                perspective.selected = true;
            });
        })

        $scope.openEditPerspective = function(perspective){
          $state.go('app.settings.perspectiveeditor', {'stationId': $state.params.stationId, 'perspectiveId': perspective.id})
        }

    }]);