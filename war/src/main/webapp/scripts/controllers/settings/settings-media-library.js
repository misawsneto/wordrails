app.controller('SettingsMediaLibraryCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage',
					function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage){
	$scope.query = $state.params.q;
	$scope.app.search.show=false;

	$scope.settings = {'tab': 'publications'}

	$scope.stationsPermissions = angular.copy($scope.app.stationsPermissions);

	$scope.imagePagination = {'page': 0, 'size': 21};
	$scope.videoPagination = {'page': 0, 'size': 21};
	$scope.audioPagination = {'page': 0, 'size': 21};
	$scope.docPagination = {'page': 0, 'size': 21};

	trix.findImagesOrderByDate($scope.imagePagination.page, $scope.imagePagination.size, null, 'imageProjection');
	trix.findVideosOrderByDate($scope.videoPagination.page, $scope.videoPagination.size, null, 'videoProjection');
	trix.findAudiosOrderByDate($scope.audioPagination.page, $scope.audioPagination.size, null, 'audioProjection');
	trix.findDocumentsOrderByDate($scope.docPagination.page, $scope.docPagination.size, null, 'imageProjection');
}]);