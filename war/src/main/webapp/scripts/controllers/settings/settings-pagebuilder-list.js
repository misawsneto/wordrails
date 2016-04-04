app.controller('PageBuilderListCtrl', ['$scope', '$rootScope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage',
	function($scope , $rootScope,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage){

		// trix.getPages().success(function(response){
		// 	console.log(response);
		// })

		$scope.stations = angular.copy($scope.app.stations);

		$scope.stations.forEach(function(station){
			trix.getStationPages(station.id).success(function(response){
				station.pages = response.pages;
				$rootScope.$broadcast('masonry.reload');
			})
		})

		$timeout(function(){
			$scope.showHomePage = true;
		})

		// var i = 0;
		// for(;i < 6;i++){
		// 	var j=0;
		// 	for(;j < 5;j++){
		// 		var page = {title: 'page ' + (j+1),
		// 			station: TRIX.baseUrl + '/api/stations/' + $scope.app.stations[i].id,
		// 			sections: {
		// 				'1':{size: 10, isPageable: false, title: 'Sec 1', 'sectionType': 'QueryableListSection'},
		// 				'2':{size: 10, isPageable: false, title: 'Sec 2', 'sectionType': 'QueryableListSection'},
		// 				'3':{size: 10, isPageable: false, title: 'Sec 3', 'sectionType': 'QueryableListSection'}
		// 			}
		// 		}

		// 		trix.postPage(page)
		// 	}
		// }
}]);