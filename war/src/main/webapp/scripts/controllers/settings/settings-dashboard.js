app.controller('DashboardCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$filter', '$localStorage', '$mdDialog', '$mdSidenav',
						 function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast , $templateCache  ,  $location ,  $filter ,  $localStorage ,  $mdDialog ,  $mdSidenav){

	if ( angular.isDefined($localStorage.seenWelcome) ) {
      $scope.app.person.seenWelcome = $localStorage.seenWelcome;
    }

	// if(!$scope.app.person.seenWelcome){
	// 	$mdDialog.show({
 //        scope: $scope,        // use parent scope in template
 //        closeTo: {
 //          bottom: 1500
 //        },
 //        preserveScope: true, // do not forget this if use parent scope
 //        controller: $scope.app.defaultDialog,
 //        templateUrl: 'welcolme-dialog.html',
 //        parent: angular.element(document.body),
 //        clickOutsideToClose:false,
 //        escapeToClose: false
 //      })
	// }

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
	trix.searchPosts(null, null, null, 'PUBLISHED', null, null, null, null, 0, 11, '-date', ['body', 'tags', 'categories', 'imageHash', 'state'], false).success(function(posts){
	    if(posts && posts.length > 0){
	      $scope.publications = posts;
	    }

	    $scope.loadingPublished = false;
	  }).error(function(){
	    $scope.loadingPublished = false;
	  })

	  $scope.loadingScheduled = true;
	trix.searchPosts(null, null, null, 'SCHEDULED', null, null, null, null, 0, 5, '-date', ['body', 'tags', 'categories', 'imageHash', 'state'], false).success(function(posts){
	    if(posts && posts.length > 0){
	      $scope.scheduled = posts;
	    }

	    $scope.loadingScheduled = false;
	  }).error(function(){
	    $scope.loadingScheduled = false;
	  })

	  $scope.loadingDrafts = true;
	trix.searchPosts(null, null, null, 'DRAFT', null, null, null, null, 0, 5, '-date', ['body', 'tags', 'categories', 'imageHash', 'state'], false).success(function(posts){
	    if(posts && posts.length > 0){
	      $scope.drafts = posts;
	    }

	    $scope.loadingDrafts = false;
	  }).error(function(){
	    $scope.loadingDrafts = false;
	  })

	  $scope.showPerson = function(event,person){
	  	// show term alert
	      $scope.loadedPerson = angular.copy(person);
	      $scope.uploadedUserImage = null;
	      $scope.uploadedCoverImage = null;
	      $mdDialog.show({
	        scope: $scope,        // use parent scope in template
	        closeTo: {
	          bottom: 1500
	        },
	        preserveScope: true, // do not forget this if use parent scope
	        controller: $scope.app.defaultDialog,
	        templateUrl: 'person-dialog.html',
	        parent: angular.element(document.body),
	        targetEvent: event,
	        clickOutsideToClose:true
	        // onComplete: function(){

	        // }
	    })
	}

	var start = null;
	var startTime = start;
	var end = moment().format('YYYY-MM-DD');
	var endTime = new Date().getTime();
	var field = 'authorId';
	var page = 0;
	var size = 100;
	var authorId = 51;
	var postIds = [6017];
	var postId = 6017;
	var stationId = 11;

	$scope.networkStats = {};

	trix.getMostCommonTerm(startTime,endTime,field,page,size).success(function(response){

    })

    //@Path("/popularNetworks")
    trix.getPopularNetworks(page,size).success(function(response){
        
    })

        //@Path("/post")
    trix.postStats(end, start, postId).success(function(response){
        
    })

    //@Path("/author")
    trix.authorStats(end, start, authorId).success(function(response){
        
    })

    //@Path("/network")
    $scope.loadingNetworkStats = true;
    $scope.networkStats = null;
    $scope.androidCount = 0;
    $scope.iosCount = 0;
    trix.networkStats(end, start).success(function(response){
        $scope.networkStats = response;
        $scope.loadingNetworkStats = false;
        $scope.androidCount = $scope.networkStats.generalStatsJson[3];
        $scope.iosCount = $scope.networkStats.generalStatsJson[4];
    }).error(function(){
    	$scope.loadingNetworkStats = false;
    })

        //@Path("/station")
    trix.stationStats(end, start, stationId).success(function(response){
        
    })

        //@Path("/storage")
    trix.getNetworkUsedSpace().success(function(response){
        
    })

    //  @Path("/countPostReads")
    trix.countReadsByPostIds(postIds).success(function(response){
        
    })

        //@Path("/countReadersByStation")
    trix.countReadersByStation(stationId).success(function(response){
        
    })

    //@Path("/dashboardStats")
    $scope.loadingDashStats = true
    $scope.publicationsCount = 0;
    $scope.commentsCount = 0;
    trix.dashboardStats().success(function(response){
    	$scope.publicationsCount = response.post;
        $scope.commentsCount = response.comment;
        $scope.loadingDashStats = false;
    }).error(function(){
    	$scope.loadingDashStats = false;
    })

    $scope.registeredUsers = 0;
    $scope.loadingRegisteredUsers = true;
    trix.countPersonsByNetwork().success(function(response){
    	$scope.registeredUsers = response;
    	$scope.loadingRegisteredUsers = false;
    }).error(function(){
    	$scope.loadingRegisteredUsers = false;
    })

 	// iosStore;
	// androidStore;
}]);