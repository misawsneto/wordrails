// tab controller
app.controller('StationsCtrl', ['$scope', '$log', '$state', '$filter', '$timeout', '$interval', 'trix', 'cfpLoadingBar', '$q',
	function($scope, $log, $state, $filter, $timeout, $interval, trix, cfpLoadingBar, $q) {

		$scope.tabs = [true, false];
		$scope.tab = function(index){
			angular.forEach($scope.tabs, function(i, v) {
				$scope.tabs[v] = false;
			});
			$scope.tabs[index] = true;
		}

		if(!$scope.app.termPerspectiveView)
			$scope.app.termPerspectiveView = initTermPerspective;
		
		// $scope.app.changeStation($scope.app.currentStation);

		$scope.selectedVerticalRow = null;
		$scope.selectVerticalRow = function(ordinaryRow){
			$scope.selectedVerticalRow = ordinaryRow
        }

        $scope.horizontalCallback = function(){
            if($scope.app.horizontalCursor){
                // $scope.app.setNowReading($scope.app.horizontalCursor.postView, $scope.app.horizontalCursor.list)
                // $scope.app.horizontalCursor = null;
                $state.go('app.read', {'slug': $scope.app.horizontalCursor.postView.slug})
            }
        }

        $scope.horizontalPaginate = function(ordinaryRow){
           if($scope.app.getCurrentStateName() != 'app.stations' || $scope.app.viewMode != 'horizontal' || ordinaryRow.allLoaded)
              return null;

          if(ordinaryRow.page == null){
              ordinaryRow.page = 0
              ordinaryRow.loading = false;
              return;
          }

          if(!ordinaryRow.loading){
              ordinaryRow.loading = true;
              return $q(function(resolve, reject) {
                 cfpLoadingBar.complete();
                 trix.getRowView($scope.app.currentStation.defaultPerspectiveId, null, ordinaryRow.termId, ordinaryRow.page + 1, 10)
                 .success(function(response){
                    if(response.cells && response.cells.length > 0){
                       response.cells.forEach(function(cell, index){
                          ordinaryRow.cells.push(cell)
                      });
                       ordinaryRow.page = ordinaryRow.page + 1
                   }else{
                       ordinaryRow.allLoaded = true;
                   }
                   $timeout(function() {
                       ordinaryRow.loading = false;
                   }, 300);
                   resolve();
               }).error(function(){
                ordinaryRow.loading = false;
                reject();
            })
           });
          }
      }

      $scope.$on('load more', function () {
      // Normally scroll case
      $scope.items.load(50);
  });
      $scope.$on('load more end', function ($evt, n, locals) {
      // For the "end" button
      if (locals.$progress == 1) {
        $scope.$apply(function () {
          $scope.items.load(50);
      });
    }
});

      $timeout(function() {
          $('#stations-list').slimScroll({
            height:'100px',
            size:'8px',
            'railVisible': true
          })
      }, 1000);

      $scope.verticalPaginate = function(ordinaryRow){

       if($scope.app.viewMode != 'vertical' || ordinaryRow.allLoaded)
          return null;

      if(ordinaryRow.page == null){
          ordinaryRow.page = 0
          ordinaryRow.loading = false;
          return;
      }

      if($scope.selectedVerticalRow && $scope.selectedVerticalRow.$$hashKey != ordinaryRow.$$hashKey)
          return;

      if(!ordinaryRow.loading){
          cfpLoadingBar.complete();
          ordinaryRow.loading = true;
          trix.getRowView($scope.app.currentStation.defaultPerspectiveId, null, ordinaryRow.termId, ordinaryRow.page + 1, 10)
          .success(function(response){
             if(response.cells && response.cells.length > 0){
                response.cells.forEach(function(cell, index){
                   ordinaryRow.cells.push(cell)
               });
                ordinaryRow.page = ordinaryRow.page + 1
            }else{
                ordinaryRow.allLoaded = true;
            }
            $timeout(function() {
                ordinaryRow.loading = false;
            }, 300);
        }).error(function(){
         ordinaryRow.loading = false;
     })
    }

}

}]);