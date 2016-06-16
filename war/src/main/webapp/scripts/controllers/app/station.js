app.controller('StationCtrl', ['$scope', '$rootScope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage', '$sce', 'termPerspectiveView',
	function($scope , $rootScope,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage, $sce, termPerspectiveView){

    if(!$scope.app.termPerspectiveView || (termPerspectiveView.id !== $scope.app.termPerspectiveView.id)){
      $scope.app.termPerspectiveView = null;
      $scope.app.termPerspectiveView = termPerspectiveView;
      if($scope.app.termPerspectiveView.homeRow && $scope.app.termPerspectiveView.homeRow.cells){
        $scope.app.termPerspectiveView.homeRow.allLoaded = false;
        var length = $scope.app.termPerspectiveView.homeRow.cells.length >= 10 ? 10 : $scope.app.termPerspectiveView.homeRow.cells.length;
          $scope.app.termPerspectiveView.homeRow.cells = $scope.app.termPerspectiveView.homeRow.cells.slice(0,length);
      }
      $scope.app.loadPerspectiveTerms();
    }else{
      if($scope.app.perspectiveTerms == null)
        $scope.app.loadPerspectiveTerms()
    }

    $timeout(function(){
    $('#scroll-box').animate({scrollTop: 0}, 0, 'easeOutQuint');
  })

  // --------- /scroll to top
}]);