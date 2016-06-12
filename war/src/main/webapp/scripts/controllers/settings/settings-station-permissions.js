app.controller('SettingsStationPermissionsCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdDialog', '$mdToast', '$filter', '$translate', '$mdConstant', 'station',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix, FileUploader, TRIX, cfpLoadingBar, $mdDialog, $mdToast, $filter, $translate, $mdConstant, station){

$scope.station = station;

trix.getStationPermission(station.id).success(function(permissions){
	$scope.permissions = permissions.userPermissions;
})

var settingsStationPermissionsCtrl = $scope;

}]);

settingsStationPermissionsCtrl = null;