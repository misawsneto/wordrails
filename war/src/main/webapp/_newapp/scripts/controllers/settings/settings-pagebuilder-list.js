app.controller('PageBuilderListCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage){

		trix.getPages().success(function(response){
			console.log(response);
		})

		trix.postPage({
			sections: [
				{'1': {'title': "Page 1", "@class":"co.xarx.trix.domain.page.QueryableListSection"}},
				{'2': {'title': "Page 2", "@class":"co.xarx.trix.domain.page.QueryableListSection"}},
				{'3': {'title': "Page 3", "@class":"co.xarx.trix.domain.page.QueryableListSection"}}
			]
		})
}]);