// tab controller
app.controller('NotificationsCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', 
					function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state , TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast){

		if(!$scope.app.notificationsCtrl)
			$scope.app.notificationsCtrl = {page: 0, firstLoad: false};

		console.log($scope.app.notificationsCtrl);

		trix.searchNotifications('',0,5);

}]);