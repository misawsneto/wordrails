app.controller('SearchCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage',
					function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage){
	$scope.searchQuery = angular.copy($state.params.q);
	$scope.app.search.show=false;

	$scope.search = {'tab': 'publications'}

	$scope.stationsPermissions = angular.copy($scope.app.stationsPermissions);
  $scope.stations = angular.copy($scope.app.stations);

	$scope.publicationsCtrl = {
    'page': 0,
    'allLoaded': false
  }

  $scope.resetPage = function(){
    $scope.publicationsCtrl.page = 0;
    $scope.publicationsCtrl.allLoaded = false;
    $scope.publications = [];
  }

  $scope.paginate = function(){
    if(!$scope.loading && !$scope.publicationsCtrl.allLoaded){
      $scope.loading = true;

      var page = getPage();
      $scope.topTagsObj = null;
    	$scope.topTags = null;
      trix.searchPosts($scope.searchQuery, null, null, 'published', null, null, null, null, page, 20, '-date', ['body', 'tags', 'categories', 'imageHash', 'state'], false).success(function(response){
        handleSuccess(response);
        $scope.loading = false;
      }).error(function(){
        $scope.loading = false;
      })
    }
  }

  var handleSuccess = function(posts){
    if(posts && posts.length > 0){
      posts.reverse();

        if(!$scope.publications)
          $scope.publications = []

        posts.forEach(function(post){
          addSnippet(post);
          $scope.publications.push(post);
          $scope.calculateTopTags();
        })
        $scope.publicationsCtrl.page++;
        $scope.publicationsCtrl.allLoaded;
    }else
      $scope.publicationsCtrl.allLoaded = true;
  }


  var getPage = function(){
      return $scope.publicationsCtrl.page;
  }

  $scope.doSearch = function(query){
    $scope.searchQuery = query;
    $scope.resetPage();
    $scope.paginate();
  }

  // ---------- /paginate posts ------

	var addSnippet = function(postObj){
    if(postObj.body)
      postObj.snippet = postObj.body.simpleSnippet();
  }

  	// --------- scroll to top
  
  var intervalPromise;
  intervalPromise = $interval(function(){
    if($('#search-container').scrollTop() > 400 && $('main').height() - 400)
      $scope.showScrollUp = true;
    else
      $scope.showScrollUp = false;
  }, 500);

  $scope.scrollToTop = function(){
    $('#search-container').animate({scrollTop: 0}, 700, 'easeOutQuint');
  }

  $scope.$on('$destroy',function(){
      if(intervalPromise)
          $interval.cancel(intervalPromise);   
  });

  // --------- /scroll to top


	$scope.calculateTopTags = function(){
  	$scope.topTagsObj = {};
		$scope.publications && $scope.publications.forEach(function(pub){
			pub && pub.tags.forEach(function(i) {
				$scope.topTagsObj[i] = ($scope.topTagsObj[i]||0)+1;
			})
		});

		$scope.topTags = $.map($scope.topTagsObj, function(value, index) {
	    return {'tag': index, 'count': value};
		});

		$scope.topTags.sort(function(objA, objB){
			if(objA.count > objB.count)
				return -1;
			if(objA.count < objB.count)
				return 1;
			else
				return 0;
		})
	}

	trix.getPersons(null, null, 'id,desc').success(function(response){
		$scope.persons = response.persons;
		$scope.loadingPerson = false
	}).error(function(){
		$scope.loadingPerson = false;
	});

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

  $timeout(function(){
    $scope.app.removeTermTabs();
  })

	searchCtrl = $scope;
}]);

var searchCtrl = null;