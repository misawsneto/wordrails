app.controller('SettingsNetworkCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdToast',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix , FileUploader, TRIX, cfpLoadingBar, $mdToast){
		$scope.app.lastSettingState = "app.settings.network";
		$scope.network = angular.copy($scope.app.initData.network)

		$scope.loginImage = {
			link: $scope.network.loginImageId ? TRIX.baseUrl + "/api/files/" + $scope.network.loginImageId + "/contents" : null}

		$scope.splashImage = {
			link: $scope.network.splashImageId ? TRIX.baseUrl + "/api/files/" + $scope.network.splashImageId + "/contents" : null}

		$scope.faviconImage = {
			link: $scope.network.faviconId ? TRIX.baseUrl + "/api/files/" + $scope.network.faviconId + "/contents" : null}


	FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
	    return true; // true|false
	};

	var login = $scope.login = new FileUploader({
		url: TRIX.baseUrl + "/api/images/upload?imageType=LOGIN"
	});

	var splash = $scope.splash = new FileUploader({
		url: TRIX.baseUrl + "/api/images/upload?imageType=SPLASH"
	});

	var favicon = $scope.favicon = new FileUploader({
		url: TRIX.baseUrl + "/api/files/contents/simple"
	});

	login.onAfterAddingFile = function(fileItem) {
		$scope.loginImage = null;
		login.uploadAll();
	};

	login.onSuccessItem = function(fileItem, response, status, headers) {
		if(response.filelink){
			$scope.loginImage = response;
			$mdToast.hide();
		}
	};

	login.onErrorItem = function(fileItem, response, status, headers) {
		if(status == 413)
			$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
		else
			$scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
	}

	login.onProgressItem = function(fileItem, progress) {
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

	splash.onAfterAddingFile = function(fileItem) {
		$scope.splashImage = null;
		splash.uploadAll();
	};

	splash.onSuccessItem = function(fileItem, response, status, headers) {
		if(response.filelink){
			$scope.splashImage = response;
			$mdToast.hide();
		}
	};

	splash.onErrorItem = function(fileItem, response, status, headers) {
		if(status == 413)
			$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
		else
			$scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
	}

	splash.onProgressItem = function(fileItem, progress) {
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
	favicon.onAfterAddingFile = function(fileItem) {
		$scope.faviconImage = null;
		favicon.uploadAll();
	};

	favicon.onSuccessItem = function(fileItem, response, status, headers) {
		if(response.filelink){
			$scope.faviconImage = response;
			$mdToast.hide();
		}
	};

	favicon.onErrorItem = function(fileItem, response, status, headers) {
		if(status == 413)
			$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
		else
			$scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
	}

	favicon.onProgressItem = function(fileItem, progress) {
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


	$scope.saveChanges = function(){
		trix.putNetwork($scope.network).success(function(response){
			$scope.app.getInitData();
			$scope.app.showSuccessToast('Alterações realizadas com successo.')
		})

		if($scope.loginImage && $scope.loginImage.imageId){
			// var loginImage = { original: TRIX.baseUrl + "/api/files/" + $scope.loginImage.id }
			// trix.postImage(loginImage).success(function(imageId){
				var myLoginImage = TRIX.baseUrl + "/api/images/" + $scope.loginImage.imageId;
				$scope.network.loginImage = myLoginImage;
				trix.putNetwork($scope.network).success(function(){
					$scope.app.initData.network.loginImageId = $scope.loginImage.id
				})
			// })
		}
		if($scope.splashImage && $scope.splashImage.imageId){
			// var splashImage = { original: TRIX.baseUrl + "/api/files/" + $scope.splashImage.id }
			// trix.postImage(splashImage).success(function(imageId){
				var mySplashImage = TRIX.baseUrl + "/api/images/" + $scope.splashImage.imageId;
				$scope.network.splashImage = mySplashImage;
				trix.putNetwork($scope.network).success(function(){
					$scope.app.initData.network.splashImageId = $scope.splashImage.id
				})
			// })
		}
		if($scope.faviconImage && $scope.faviconImage.id){
			var favicon = { original: TRIX.baseUrl + "/api/files/" + $scope.faviconImage.id }
			trix.postImage(favicon).success(function(imageId){
				var myFavicon = TRIX.baseUrl + "/api/images/" + imageId;
				$scope.network.favicon = myFavicon;
				trix.putNetwork($scope.network).success(function(){
					$scope.app.initData.network.faviconImageId = $scope.faviconImage.id
				})
			})
		}
	}
}])

app.controller('NetworkStatsCtrl', ['$scope', '$log', '$timeout', '$rootScope', '$state', 'trix', 'TRIX',
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

		trix.getNetworkPublicationsCount().success(function(response){
			$scope.totalPublished = response.publicationsCounts[0]
			$scope.totalDrafts = response.publicationsCounts[1]
			$scope.totalScheduled = response.publicationsCounts[2]
		})


		trix.getNetworkStats(d3.time.format("%Y-%m-%d")(new Date())).success(function(response){

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
				$scope.usersAndroid = generalStatsJson[3]
				$scope.usersIOS = generalStatsJson[4]
			}

			$scope.chartData = [readsCount, recommendsCount, commentsCount]
		})

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
		startDate: moment().add(-29, 'days'), endDate: moment(),
		ranges: {
	           'Hoje': [moment(), moment()],
	           'Ontem': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
	           'Últimos 7 dias': [moment().subtract(6, 'days'), moment()],
	           'Esse mês': [moment().startOf('month'), moment().endOf('month')],
	           'Último mês': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')
	           ]
	        }}
		//$scope.datePickerValue = moment().add(-30, 'days').format('DD/MM/YYYY') + ' - ' + moment().format('DD/MM/YYYY');

$scope.usersCount = 0;
$scope.usersAndroid = 0
$scope.usersIOS = 0
$scope.totalPublished = 0;
$scope.totalScheduled = 0
$scope.totalDrafts = 0;
$scope.imagesCount = 0;

trix.countPersonsByNetwork().success(function(response){
	$scope.usersCount = response;
})

$scope.page = 0;
$scope.firstLoad = false

trix.searchPosts(null, $scope.page, 10, {'personId': $scope.app.getLoggedPerson().id,
	'publicationType': 'PUBLISHED', sortByDate: true}).success(function(response){
		$scope.publications = response.posts;
		$scope.firstLoad = true;
	})
}])
