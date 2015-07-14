app.controller('UserCtrl', ['$scope', '$log', '$timeout', '$rootScope', '$state', 'trix', 'FileUploader', 'TRIX',
	function($scope ,  $log ,  $timeout ,  $rootScope ,  $state ,  trix ,  FileUploader, TRIX) {

	// set left side post list to initial scroll
	//$("#post-left-content .left-content-wrap").scrollTop(0);
	//
		FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
	    return true; // true|false
	  };

	  var uploader = $scope.uploader = new FileUploader({
	  	url: TRIX.baseUrl + "/api/files/contents/simple"
	  });

	  var username = $state.params.username

	  if(username == null){
	  	$state.go('access.404')
	  }

	  trix.findByUsername(username).success(function(response){
	  	try{
	  		$scope.showProfile = true;
	  		$scope.person = response.persons[0];
	  	}catch(e){
	  		$state.go('access.404')
	  	}
	  	$scope.findUserPosts($scope.person);
	  }).error(function(){
	  	$state.go('access.404')
	  })

	  $scope.postsPage = 0;
	  $scope.postViews = [];
	  $scope.findUserPosts = function(person){
	  	trix.getPersonNetworkPosts(person.id, $scope.app.initData.network.id,$scope.postsPage, 5).success(function(posts){
	  		if(posts && posts.length > 0)
	  			$scope.postViews = posts;
	  	})

	  	trix.findRecommendsByPersonIdOrderByDate(person.id, 0, 6, null, "recommendProjection").success(function(response){
	  		if(response.recommends && response.recommends.length > 0)
	  			$scope.recommendations = response.recommends;
	  	})
	  }

	  $scope.paginate = function(){

	  	if(!$scope.postViews || $scope.postViews.length == 0)
	  		return;

	  	if($scope.allLoaded)
	  		return;

	  	if(!$scope.loadingPage){
	  		$scope.loadingPage = true;
	  		trix.getPersonNetworkPosts($scope.person.id, $scope.app.initData.network.id,$scope.postsPage + 1, 5)
	  		.success(function(posts){
	  			$scope.loadingPage = false;
	  			$scope.postsPage = $scope.postsPage + 1;

	  			if(!posts || posts.length == 0){
	  				$scope.allLoaded = true;
	  				return;
	  			}

	  			if(!$scope.pages)
	  				$scope.pages = []

	  			posts && posts.forEach(function(element, index){
	  				$scope.pages.push(element)
	  			}); 

	  			$(".search-results").focus();
	  		})
	  		.error(function(){
	  			$scope.loadingPage = false;
	  		})
	  	}
	  }

	}])

app.controller('UserStatsCtrl', ['$scope', '$log', '$timeout', '$rootScope', '$state', 'trix', 'TRIX',
	function($scope ,  $log ,  $timeout ,  $rootScope ,  $state ,  trix , TRIX) {
		// http://krispo.github.io/angular-nvd3/#/multiBarChart
		$scope.chartOptions = {
			  "chart": {
			    "type": "multiBarChart",
			    "height": 450,
			    "margin": {
			      "top": 20,
			      "right": 20,
			      "bottom": 60,
			      "left": 45
			    },
			    "clipEdge": true,
			    "staggerLabels": true,
			    "transitionDuration": 500,
			    "stacked": true,
			    "xAxis": {
			      "axisLabel": " ",
			      "showMaxMin": false,
			      "tickFormat": function(d) { 
			      	 console.log(d);
			      	//d = d * 100000000;
			      	return d3.time.format('%d/%m/%Y')(new Date(d)) 
			      }
			    },
			    "yAxis": {
			      "axisLabel": "Y Axis",
			      "axisLabelDistance": 40,
			      "tickFormat": d3.format("d")
			    },
			    "controlLabels": { "stacked": "Empilhado","grouped": "Agrupado" },
			  	"tooltipContent": function(data) {
					var date = d3.time.format('%d/%m/%Y')(new Date(data.data.x));
					var val = Math.trunc(data.data.y)
        			return '<table><thead><tr><td colspan="3"><strong class="x-value">' + val + ' em ' + date + '</strong></td></tr></thead><tbody><tr><td class="legend-color-guide"><div style="background-color: ' + data.color + ';"></div></td><td class="key">' + data.data.key + '</td><td class="value"></td></tr></tbody></table>'
      			}
			  }
			}


        trix.getPersonStats(d3.time.format("%Y-%m-%d")(new Date())).success(function(response){

        	var readsCount = {"key": "Leituras"}
        	readsCount.values = [];

        	for (var property in response) {
				    if (response.hasOwnProperty(property)) {
				    	console.log(property);
				    	readsCount.values.push({
				    		x: parseInt(property),
				    		y: response[property].readsCount
				    	})
				    }
					}

					var recommendsCount = {"key": "Recomendações"}
        	recommendsCount.values = [];

        	for (var property in response) {
				    if (response.hasOwnProperty(property)) {
				    	console.log(property);
				    	recommendsCount.values.push({
				    		x: parseInt(property),
				    		y: response[property].recommendsCount
				    	})
				    }
					}

					var commentsCount = {"key": "Comentários"}
        	commentsCount.values = [];

        	for (var property in response) {
				    if (response.hasOwnProperty(property)) {
				    	console.log(property);
				    	commentsCount.values.push({
				    		x: parseInt(property),
				    		y: response[property].commentsCount
				    	})
				    }
					}

					console.log(readsCount);

        	$scope.chartData = [readsCount, recommendsCount, commentsCount]
        })
	

		$scope.page = 0;
		$scope.firstLoad = false
		trix.searchPosts(null, $scope.page, 10, {'personId': $scope.app.getLoggedPerson().id,
			'publicationType': 'PUBLISHED', sortByDate: true}).success(function(response){
			$scope.publications = response.posts;
			$scope.firstLoad = true;
		})
	}])

app.controller('UserPublicationsCtrl', ['$scope', '$log', '$state', '$filter', '$timeout', '$interval', 'trix', 'cfpLoadingBar', '$q',
	function($scope, $log, $state, $filter, $timeout, $interval, trix, cfpLoadingBar, $q) {
		
		if(!$scope.app.publicationsCtrl)
			$scope.app.publicationsCtrl = {page: 0, firstLoad: false};

		$scope.$watch('$state.params.type', function(){
			if($state.params.type == "drafts"){
				trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': $scope.app.getLoggedPerson().id,
					'publicationType': 'DRAFT', sortByDate: true}).success(function(response){
					$scope.app.publicationsCtrl.publications = response.posts;
					$scope.app.publicationsCtrl.tabIndex = 0;
					$scope.app.publicationsCtrl.firstLoad = true;
				})
			}
			if($state.params.type == "publications"){
				trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': $scope.app.getLoggedPerson().id,
					'publicationType': 'PUBLISHED', sortByDate: true}).success(function(response){
					$scope.app.publicationsCtrl.publications = response.posts;
					$scope.app.publicationsCtrl.tabIndex = 1;
					$scope.app.publicationsCtrl.firstLoad = true;
				})
			}
			if($state.params.type == "scheduled"){
				trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': $scope.app.getLoggedPerson().id,
					'publicationType': 'SCHEDULED', sortByDate: true}).success(function(response){
					$scope.app.publicationsCtrl.publications = response.posts;
					$scope.app.publicationsCtrl.tabIndex = 2;
					$scope.app.publicationsCtrl.firstLoad = true;
				})
			}
			if($state.params.type == "others"){
				trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': $scope.app.getLoggedPerson().id,
					'publicationType': 'EDITOR', sortByDate: true}).success(function(response){
					$scope.app.publicationsCtrl.publications = response.posts;
					$scope.app.publicationsCtrl.tabIndex = 3;
					$scope.app.publicationsCtrl.firstLoad = true;
				})
			}
		});

}])		