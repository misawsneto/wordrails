app.controller('UserCtrl', ['$scope', '$log', '$timeout', '$rootScope', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdToast', '$mdDialog',
	function($scope , $log ,  $timeout ,  $rootScope ,  $state ,  trix ,  FileUploader, TRIX, cfpLoadingBar, $mdToast , $mdDialog) {

	// set left side post list to initial scroll
	//$("#post-left-content .left-content-wrap").scrollTop(0);
	//
	FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
	    return true; // true|false
	};

	var cover = $scope.cover = new FileUploader({
		url: TRIX.baseUrl + "/api/files/contents/simple"
	});

	var image = $scope.image = new FileUploader({
		url: TRIX.baseUrl + "/api/files/contents/simple"
	});

	cover.onAfterAddingFile = function(fileItem) {
		$scope.userCover = null;
		cover.uploadAll();
	};

	cover.onSuccessItem = function(fileItem, response, status, headers) {
		if(response.filelink){
			$scope.userCover = response;
			var imageObject = { original: TRIX.baseUrl + "/api/files/" + $scope.userCover.id }
			trix.postImage(imageObject).success(function(imageId){
				var myCoverImage = TRIX.baseUrl + "/api/images/" + imageId;
				$scope.person.cover = myCoverImage
				trix.putPerson($scope.person).success(function(){
					$scope.app.initData.person.coverLargeId = $scope.person.coverLargeId = $scope.userCover.id;
					$scope.app.initData.person.coverMediumId = $scope.person.coverMediumId = $scope.userCover.id;
					$mdToast.hide();
				})
			})
		}
	};

	cover.onErrorItem = function(fileItem, response, status, headers) {
		if(status == 413)
			$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
		else
			$scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
	}

	cover.onProgressItem = function(fileItem, progress) {
		cfpLoadingBar.start();
		cfpLoadingBar.set(progress/10)
		if(progress == 100){
			cfpLoadingBar.complete()
			toastPromise = $mdToast.show(
				$mdToast.simple()
				.content('Processando...')
				.position('top right')
				.hideDelay(false)
				);
		}
	};

	image.onAfterAddingFile = function(fileItem) {
		$scope.userImage = null;
		image.uploadAll();
	};

	image.onSuccessItem = function(fileItem, response, status, headers) {
		if(response.filelink){
			$scope.userImage = response;
			var imageObject = { original: TRIX.baseUrl + "/api/files/" + $scope.userImage.id }
			trix.postImage(imageObject).success(function(imageId){
				var myImageImage = TRIX.baseUrl + "/api/images/" + imageId;
				$scope.person.image = myImageImage
				trix.putPerson($scope.person).success(function(){
					$scope.app.initData.person.imageMediumId = $scope.person.imageMediumId = $scope.userImage.id;
					$scope.app.initData.person.imageSmallId = $scope.person.imageSmallId = $scope.userImage.id;
					$mdToast.hide();
				})
			})
		}
	};

	image.onErrorItem = function(fileItem, response, status, headers) {
		if(status == 413)
			$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
		else
			$scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
	}

	image.onProgressItem = function(fileItem, progress) {
		cfpLoadingBar.start();
		cfpLoadingBar.set(progress/10)
		if(progress == 100){
			cfpLoadingBar.complete()
			toastPromise = $mdToast.show(
				$mdToast.simple()
				.content('Processando...')
				.position('top right')
				.hideDelay(false)
				);
		}
	};

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

	        // perspective editor dialog Controller
    function DialogController(scope, $mdDialog) {
      scope.app = $scope.app;

      scope.hide = function() {
        $mdDialog.hide();
      };

      scope.cancel = function() {
        $mdDialog.cancel();
      };

      scope.publish = function() {
        if(isTermSelected($scope.termTree)){
          createPost($mdDialog)
        }else{
          // show alert term message
        }
        //$mdDialog.hide();
      };
      // check if user has permisstion to write
    };

    $scope.showEditProfile = function(ev){
    // show term alert
      $mdDialog.show({
        controller: DialogController,
        templateUrl: 'edit_profile.html',
        targetEvent: ev,
        onComplete: function(){}
      })
      .then(function(answer) {
      //$scope.alert = 'You said the information was "' + answer + '".';
      }, function() {
      //$scope.alert = 'You cancelled the dialog.';
      });
    }

}])

app.controller('UserStatsCtrl', ['$scope', '$log', '$timeout', '$rootScope', '$state', 'trix', 'TRIX',
	function($scope ,  $log ,  $timeout ,  $rootScope ,  $state ,  trix , TRIX) {
		// http://krispo.github.io/angular-nvd3/#/multiBarChart
		$scope.chartOptions = {
			"chart": {
				"type": "multiBarChart",
				"height": 300,
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

		trix.getPersonPublicationsCount().success(function(response){
			$scope.totalPublished = response.publicationsCounts[0]
			$scope.totalDrafts = response.publicationsCounts[1]
			$scope.totalScheduled = response.publicationsCounts[2]
		})


		trix.getPersonStats(d3.time.format("%Y-%m-%d")(new Date())).success(function(response){

			var dateStats = response.dateStatsJson
			var generalStatsJson = response.generalStatsJson && response.generalStatsJson.length > 0 ? response.generalStatsJson : null;

			var readsCount = {"key": "Leituras"}
			readsCount.values = [];

			for (var property in dateStats) {
				if (dateStats.hasOwnProperty(property)) {
					readsCount.values.push({
						x: parseInt(property),
						y: dateStats[property].readsCount
					})
				}
			}

			var recommendsCount = {"key": "Recomendações"}
			recommendsCount.values = [];

			for (var property in dateStats) {
				if (dateStats.hasOwnProperty(property)) {
					recommendsCount.values.push({
						x: parseInt(property),
						y: dateStats[property].recommendsCount
					})
				}
			}

			var commentsCount = {"key": "Comentários"}
			commentsCount.values = [];

			for (var property in dateStats) {
				if (dateStats.hasOwnProperty(property)) {
					commentsCount.values.push({
						x: parseInt(property),
						y: dateStats[property].commentsCount
					})
				}
			}

			if(generalStatsJson && generalStatsJson.length > 0){
				$scope.totalPostsRead = generalStatsJson[0]
				$scope.totalComments = generalStatsJson[1]
				$scope.totalRecommends = generalStatsJson[2]
			}

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
		
	$scope.app.publicationsCtrl = {page: 0, firstLoad: false};

	$scope.datePickerOptions = {
			"locale": {
	        "format": "DD/MM/YYYY",
	        "separator": " - ",
	        "applyLabel": "Aplicar",
	        "cancelLabel": "Cancelar",
	        "fromLabel": "De",
	        "toLabel": "Até",
	        "customRangeLabel": "Personalizado",
	        "daysOfWeek": [
	            "Dom",
	            "Seg",
	            "Ter",
	            "Qua",
	            "Qui",
	            "Sex",
	            "Sab"
	        ],
	        "monthNames": [
	            "Janeiro",
	            "Fevereiro",
	            "Março",
	            "Abril",
	            "Maio",
	            "Junho",
	            "Julho",
	            "Agosto",
	            "Setembro",
	            "Outubro",
	            "Novembro",
	            "Dezembro"
	        ],
	        "firstDay": 1
	    },
		startDate: moment().add(-29, 'days'), endDate: moment(), opens: "center",
		ranges: {
	           'Hoje': [moment(), moment()],
	           'Ontem': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
	           'Últimos 7 dias': [moment().subtract(6, 'days'), moment()],
	           'Esse mês': [moment().startOf('month'), moment().endOf('month')],
	           'Último mês': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')
	           ]
	        }}

	$scope.$watch('$state.params.type', function(){
		if($state.params.type == "drafts"){
			// trix.searchPosts(null, $scope.app.publicationsCtrl.page, 10, {'personId': $scope.app.getLoggedPerson().id,
				// 'publicationType': 'DRAFT', sortByDate: true}).success(function(response){
					trix.getPersonNetworkPostsByState(null, 'DRAFT', $scope.app.publicationsCtrl.page, 10).success(function(response){
						$scope.app.publicationsCtrl.publications = response;
						$scope.app.publicationsCtrl.tabIndex = 2;
						$scope.app.publicationsCtrl.firstLoad = true;
					})
				}
				if($state.params.type == "publications"){
					trix.getPersonNetworkPostsByState(null, 'PUBLISHED', $scope.app.publicationsCtrl.page, 10).success(function(response){
						$scope.app.publicationsCtrl.publications = response;
						$scope.app.publicationsCtrl.tabIndex = 0;
						$scope.app.publicationsCtrl.firstLoad = true;
					})
				}
				if($state.params.type == "scheduled"){
					trix.getPersonNetworkPostsByState(null, 'SCHEDULED', $scope.app.publicationsCtrl.page, 10).success(function(response){
						$scope.app.publicationsCtrl.publications = response;
						$scope.app.publicationsCtrl.tabIndex = 1;
						$scope.app.publicationsCtrl.firstLoad = true;
					})
				}
			});

		$scope.$on('POST_REMOVED', function(event, postId){
			if($scope.app.publicationsCtrl && $scope.app.publicationsCtrl.publications){
				for (var i = $scope.app.publicationsCtrl.publications.length - 1; i >= 0; i--) {
					if(postId == $scope.app.publicationsCtrl.publications[i].postId)
					$scope.app.publicationsCtrl.publications.splice(i,1)
				};
			}
		})

		$scope.paginate = function(){

			if(!$scope.app.publicationsCtrl.publications || $scope.app.publicationsCtrl.publications.length == 0)
				return;

			if($scope.allLoaded)
				return;

			var type = '';
			if($state.params.type == "drafts"){
				type = 'DRAFT'
			}else if($state.params.type == "publications"){
				type = 'PUBLISHED'
			}else if($state.params.type == "scheduled"){
				type = 'SCHEDULED'
			}

			if(!$scope.loadingPage){
				$scope.loadingPage = true;
				/*trix.searchPosts(null, $scope.app.publicationsCtrl.page + 1, 10, {'personId': $scope.app.getLoggedPerson().id,
					'publicationType': type, sortByDate: true}).success(function(response){*/

					trix.getPersonNetworkPostsByState(null, type, $scope.app.publicationsCtrl.page+1, 10).success(function(response){
						var posts = response;

						$scope.loadingPage = false;
						$scope.app.publicationsCtrl.page = $scope.app.publicationsCtrl.page + 1;

						if(!posts || posts.length == 0){
							$scope.allLoaded = true;
							return;
						}

						if(!$scope.pages)
							$scope.pages = []

						posts && posts.forEach(function(element, index){
							$scope.app.publicationsCtrl.publications.push(element)
						}); 

					})
					.error(function(){
						$scope.loadingPage = false;
					})
				}
			}

}])		