// tab controller
app.controller('NotificationsCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', 
					function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state , TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast){

		if(!$scope.app.notificationsCtrl)
			$scope.app.notificationsCtrl = {page: 0, firstLoad: false};


		trix.searchNotifications('',0,5).success(function(data){
			if(data && data.length > 0)
			$scope.app.notificationsCtrl.notifications = data
		});

}]);