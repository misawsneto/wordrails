app.controller('DashboardCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$filter', '$localStorage',
						 function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast , $templateCache  ,  $location ,  $filter ,  $localStorage){

	if ( angular.isDefined($localStorage.seenWelcome) ) {
      $scope.app.person.seenWelcome = $localStorage.seenWelcome;
    }

	if(!$scope.app.person.seenWelcome){
		$mdDialog.show({
        scope: $scope,        // use parent scope in template
        closeTo: {
          bottom: 1500
        },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'welcolme-dialog.html',
        parent: angular.element(document.body),
        targetEvent: event,
        clickOutsideToClose:false,
        escapeToClose: false
      })
	}

	$scope.setSeen = function(){
      $localStorage.seenWelcome = true;
	}

	$scope.loadingPerson = true;
	$scope.persons = [];
	trix.getPersons(0, 6, 'id,desc').success(function(response){
		$scope.persons = response.persons;
		$scope.loadingPerson = false
	}).error(function(){
		$scope.loadingPerson = false;
	});

	$scope.loadingPublished = true;
	trix.searchPosts(null, null, null, 'PUBLISHED', null, null, null, null, 0, 5, '-date', ['body', 'tags', 'categories', 'imageHash', 'state'], false).success(function(posts){
	    if(posts && posts.length > 0){
	      posts.reverse();
	      $scope.publications = posts;
	    }

	    $scope.loadingPublished = false;
	  }).error(function(){
	    $scope.loadingPublished = false;
	  })

	  $scope.loadingScheduled = true;
	trix.searchPosts(null, null, null, 'SCHEDULED', null, null, null, null, 0, 5, '-date', ['body', 'tags', 'categories', 'imageHash', 'state'], false).success(function(posts){
	    if(posts && posts.length > 0){
	      posts.reverse();
	      $scope.scheduled = posts;
	    }

	    $scope.loadingScheduled = false;
	  }).error(function(){
	    $scope.loadingScheduled = false;
	  })

	  $scope.loadingDrafts = true;
	trix.searchPosts(null, null, null, 'DRAFT', null, null, null, null, 0, 5, '-date', ['body', 'tags', 'categories', 'imageHash', 'state'], false).success(function(posts){
	    if(posts && posts.length > 0){
	      posts.reverse();
	      $scope.drafts = posts;
	    }

	    $scope.loadingDrafts = false;
	  }).error(function(){
	    $scope.loadingDrafts = false;
	  })

}]);