app.controller('PageBuilderListCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage){

		trix.getPages().success(function(response){
			console.log(response);
		})

		var i = 0;
		for(;i < 6;i++){
			var j=0;
			for(;j < 5;j++){
				var page = {title: 'page ' + (j+1),
					station: TRIX.baseUrl + '/api/stations/' + $scope.app.stations[i].id,
					sections: {
						'1':{size: 10, isPageable: false, title: 'Sec 1', 'sectionType': 'co.xarx.trix.domain.page.QueryableListSection'},
						'2':{size: 10, isPageable: false, title: 'Sec 2', 'sectionType': 'co.xarx.trix.domain.page.QueryableListSection'},
						'3':{size: 10, isPageable: false, title: 'Sec 3', 'sectionType': 'co.xarx.trix.domain.page.QueryableListSection'}
					}
				}

				trix.postPage(page)
			}
		}
}]);