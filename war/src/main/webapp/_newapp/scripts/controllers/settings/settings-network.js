app.controller('SettingsNetworkCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdToast', '$translate', '$mdSidenav',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix , FileUploader, TRIX, cfpLoadingBar, $mdToast, $translate, $mdSidenav){

		$scope.toggleRight = buildToggler('palette-selector');

		$scope.settings = {'tab': 'settings'} 


		function buildToggler(navID) {
	      return function() {
	        $mdSidenav(navID)
	          .toggle()
	          .then(function () {
	            $log.debug("toggle " + navID + " is done");
	          });
	      }
	    }

	    $scope.toggleLeft = buildDelayedToggler('left');
	        /**
     * Supplies a function that will continue to operate until the
     * time is up.
     */
    function debounce(func, wait, context) {
      var timer;
      return function debounced() {
        var context = $scope,
            args = Array.prototype.slice.call(arguments);
        $timeout.cancel(timer);
        timer = $timeout(function() {
          timer = undefined;
          func.apply(context, args);
        }, wait || 10);
      };
    }
	    /**
	     * Build handler to open/close a SideNav; when animation finishes
	     * report completion in console
	     */
	    function buildDelayedToggler(navID) {
	      return debounce(function() {
	        $mdSidenav(navID)
	          .toggle()
	          .then(function () {
	            $log.debug("toggle " + navID + " is done");
	          });
	      }, 200);
	    }

		$scope.app.lastSettingState = "app.settings.network";
		$scope.network = angular.copy($scope.app.network)

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
			$scope.app.showSuccessToast('Alterações realizadas com sucesso.')
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

	$scope.setLanguage = function(languageRef){
		switch(languageRef) {
			case 'en':
				$translate.use(languageRef)
				break;
			case 'pt':
				$translate.use(languageRef)
				break;
			default:
				$scope.app.showErrorToast($filter('translate')('messages.settings.network.NO_LANGUAGE'))
				break;
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

	$scope.lineInView = function(inview, inviewpart, event) {
		// var inViewReport = inview ? '<strong>enters</strong>' : '<strong>exit</strong>';
		// if (typeof(inviewpart) != 'undefined') {
		// 	inViewReport = '<strong>' + inviewpart + '</strong> part ' + inViewReport;
		// }
		// $scope.inviewLogs.unshift({ id: logId++, message: $sce.trustAsHtml('Line <em>#' + index + '</em>: ' + inViewReport) });
		console.log(event);
		return false;
	}

	// --------------------------------------------
	
		$scope.app.lastSettingState = "app.settings.colors";
	$scope.isMobile = true;

  	var navbarColor = initData.network.navbarColor
	var backgroundColor = initData.network.backgroundColor
  	var mainColor = initData.network.mainColor

	// if(!$scope.app.testColors){
	// 	$scope.app.testColors = {
	// 		'navbar': navbarColor,
	// 		'backgroundColor': backgroundColor,
	// 		'mainColor': mainColor
	// 	}
	// }

	$("#paint-navbar").attr('style', 'background-color: ' + navbarColor) 
	$("#paint-background").attr('style', 'background-color: ' + backgroundColor)
	$("#paint-main").attr('style', 'background-color: ' + mainColor)

	$scope.colors = {}
	$scope.colors.one = "#333"
	$scope.colors.two = "#f0f0f0"
	$scope.colors.three = navbarColor
	$scope.colors.four = backgroundColor
	$scope.colors.five = mainColor

	// function updateColors (){
	// 	var $style = $('style#custom-colors').length ? $('style#style#custom-colors') : $('<style id="custom-colors">').appendTo('body');
	// 	var colorStyles = '' +
	// 	'.color-1 {background-color: '+$scope.colors.one+'!important; color: '+$scope.colors.one+ '!important}\n' + 
	// 	'.color-2 {background-color: '+$scope.colors.two+'!important; color: '+$scope.colors.two+ '!important}\n' + 
	// 	'.color-3 {background-color: '+$scope.colors.three+'!important; color: '+$scope.colors.three+ '!important}\n' + 
	// 	'.color-4 {background-color: '+$scope.colors.four+'!important; color: '+$scope.colors.four+ '!important}\n' + 
	// 	'.color-5 {background-color: '+$scope.colors.five+'!important; color: '+$scope.colors.five+ '!important}'
	// 	console.log(colorStyles);
	// 	$style.html(colorStyles);
	// }

		function updateColors (){
		$('.color-1').attr('style', 'background-color: '+$scope.colors.one+'; color: '+$scope.colors.one);
		$('.color-2').attr('style', 'background-color: '+$scope.colors.two+'; color: '+$scope.colors.two) 
		$('.color-3').attr('style', 'background-color: '+$scope.colors.three+'; color: '+$scope.colors.three) 
		$('.color-4').attr('style', 'background-color: '+$scope.colors.four+'; color: '+$scope.colors.four) 
		$('.color-5').attr('style', 'background-color: '+$scope.colors.five+'; color: '+$scope.colors.five)
	}

	updateColors();

	$scope.$watch('colors', function(newVal){
		updateColors();
	}, true)

	$scope.saveColors = function(){
		safeApply($scope, function(){
			$scope.network = angular.copy($scope.app.initData.network)

			var navbar = $scope.network.navbarColor = rbgToHex2($("#paint-navbar").css('backgroundColor'))
			var background = $scope.network.backgroundColor = rbgToHex2($("#paint-background").css('backgroundColor'))
			var main = $scope.network.mainColor = rbgToHex2($("#paint-main").css('backgroundColor'))

			trix.putNetwork($scope.network).success(function(response){
				$scope.app.showSuccessToast('Alterações realizadas com sucesso.')
				$scope.app.getInitData();
				$('style#custom-style').remove();
				$timeout(function() {
					$('<style id="custom-style">').appendTo('body').html(getCustomStyle(main, background, navbar));
				});
			})
		})
	}

	// ----------------------------------

	var docElem = $("html"),
		// transition end event name
		transEndEventNames = { 'WebkitTransition': 'webkitTransitionEnd', 'MozTransition': 'transitionend', 'OTransition': 'oTransitionEnd', 'msTransition': 'MSTransitionEnd', 'transition': 'transitionend' },
		transEndEventName = transEndEventNames[ Modernizr.prefixed( 'transition' ) ];

	function scrollX() { return window.pageXOffset || docElem.scrollLeft(); }
	function scrollY() { return window.pageYOffset || docElem.scrollTop(); }
	function getOffset(el) {
		var offset = el.getBoundingClientRect();
		return { top : offset.top + scrollY(), left : offset.left + scrollX() };
	}

	function dragMoveListener(event) {
		var target = event.target,
			// keep the dragged position in the data-x/data-y attributes
			x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
			y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

		// translate the element
		target.style.transform = target.style.webkitTransform = 'translate(' + x + 'px, ' + y + 'px)';
		target.style.zIndex = 10000;

		// update the posiion attributes
		target.setAttribute('data-x', x);
		target.setAttribute('data-y', y);
	}

	function revertDraggable(el) {
		el.style.transform = el.style.webkitTransform = 'none';
		el.style.zIndex = 1;
		el.setAttribute('data-x', 0);
		el.setAttribute('data-y', 0);
	}

	function init() {

		// target elements with the "drag-element" class
		interact('.drag-element').draggable({
			// enable inertial throwing
			inertia: true,
			// call this function on every dragmove event
			onmove: dragMoveListener,
			onend: function (event) {
				if(!$(event.target).hasClass('drag-element--dropped') && !$(event.target).hasClass('drag-element--dropped-text')) {
					revertDraggable(event.target);
				}
			}
		});

		// enable draggables to be dropped into this
		interact('.paint-area').dropzone({
			// only accept elements matching this CSS selector
			accept: '.drag-element',
			// Require a 75% element overlap for a drop to be possible
			overlap: 0.75,
			ondragenter: function (event) {
				$(event.target).addClass('paint-area--highlight');
			},
			ondragleave: function (event) {
				$(event.target).removeClass('paint-area--highlight');
			},
			ondrop: function (event) {
				var type = 'area';
				if($(event.target).hasClass('paint-area--text')) {
					type = 'text';
				}

				var draggableElement = event.relatedTarget;

				$(draggableElement).addClass( type === 'text' ? 'drag-element--dropped-text' : 'drag-element--dropped');

				var onEndTransCallbackFn = function(ev) {
					this.removeEventListener( transEndEventName, onEndTransCallbackFn );
					if( type === 'area' ) {
						paintArea(event.dragEvent, event.target, draggableElement.getAttribute('data-color'));
					}
					setTimeout(function() {
						revertDraggable(draggableElement);
						$(draggableElement).removeClass(type === 'text' ? 'drag-element--dropped-text' : 'drag-element--dropped');
					}, type === 'text' ? 0 : 250);
				};
				if( type === 'text' ) {
					paintArea(event.dragEvent, event.target, draggableElement.getAttribute('data-color'));
				}
				draggableElement.querySelector('.drop').addEventListener(transEndEventName, onEndTransCallbackFn);
			},
			ondropdeactivate: function (event) {
				// remove active dropzone feedback
				$(event.target).removeClass('paint-area--highlight');
			}
		});

		// reset colors
		document.querySelector('button.reset-button').addEventListener('click', resetColors);
	}

	function paintArea(ev, el, color) {
		var type = 'area';
		if($(el).hasClass('paint-area--text')) {
			type = 'text';
		}

		if( type === 'area' ) {
			// create SVG element
			var dummy = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
			dummy.setAttributeNS(null, 'version', '1.1');
			dummy.setAttributeNS(null, 'width', '100%');
			dummy.setAttributeNS(null, 'height', '100%');
			dummy.setAttributeNS(null, 'class', 'paint');

			var g = document.createElementNS('http://www.w3.org/2000/svg', 'g');
			var offLeft = getOffset(el).left;
			var offTop = getOffset(el).top
			var xTrans = Number(ev.pageX - offLeft)
			var yTrans = Number(ev.pageY - offTop)

			var transformString = 'translate(' + xTrans + ', ' + yTrans + ')'

			g.setAttributeNS(null, 'transform', transformString);

			var circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
			circle.setAttributeNS(null, 'cx', 0);
			circle.setAttributeNS(null, 'cy', 0);
			circle.setAttributeNS(null, 'r', Math.sqrt(Math.pow(el.offsetWidth,2) + Math.pow(el.offsetHeight,2)));
			circle.setAttributeNS(null, 'fill', color);

			dummy.appendChild(g);
			g.appendChild(circle);
			el.appendChild(dummy);
		}

		setTimeout(function() {
			$(el).addClass( 'paint--active');

			if( type === 'text' ) {
				el.style.color = color;
				var onEndTransCallbackFn = function(ev) {
					if( ev.target != this ) return;
					this.removeEventListener( transEndEventName, onEndTransCallbackFn );
					$(el).removeClass('paint--active');
				};

				el.addEventListener(transEndEventName, onEndTransCallbackFn);
			}
			else {
				var onEndTransCallbackFn = function(ev) {
					if( ev.target != this || ev.propertyName === 'fill-opacity' ) return;
					this.removeEventListener(transEndEventName, onEndTransCallbackFn);
					// set the color
					el.style.backgroundColor = color;
					// remove SVG element
					el.removeChild(dummy);

					setTimeout(function() { $(el).removeClass( 'paint--active'); }, 25);
				};

				circle.addEventListener(transEndEventName, onEndTransCallbackFn);
			}
		}, 25);
	}

	function resetColors() {
		[].slice.call(document.querySelectorAll('.paint-area')).forEach(function(el) {
			el.style[$(el).hasClass( 'paint-area--text') ? 'color' : 'background-color'] = '';
		});
	}

	$timeout(function() {
		init();
	}, 50);
	
}])
