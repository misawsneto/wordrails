app.controller('SettingsNetworkCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdToast', '$translate', '$mdSidenav',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix , FileUploader, TRIX, cfpLoadingBar, $mdToast, $translate, $mdSidenav){

		$scope.toggleOptions = buildToggler('palette-selector');

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

	if($scope.app.network.splashImageHash){
		$scope.splashImage = {
		link: $scope.app.network.splashImageHash ? TRIX.baseUrl + "/api/images/get/" + $scope.app.network.splashImageHash + "?size=large" : null,
		id: $scope.app.network.splashImageId}
	}

	if($scope.app.network.logoImageHash){
		$scope.logoImage = {
		link: $scope.app.network.logoImageHash ? TRIX.baseUrl + "/api/images/get/" + $scope.app.network.logoImageHash + "?size=medium" : null,
		id: $scope.app.network.logoImageId}
	}

	if($scope.app.network.faviconHash){
		$scope.faviconImage = {
		link: $scope.app.network.faviconHash ? TRIX.baseUrl + "/api/images/get/" + $scope.app.network.faviconHash + "?size=favicon" : null,
		id: $scope.app.network.faviconId}
	}


	FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
	    return true; // true|false
	};


	// ------- upload files -----------

	var splash = $scope.splash = new FileUploader({
		url: TRIX.baseUrl + "/api/images/upload?imageType=SPLASH"
	});

	var logo = $scope.logo = new FileUploader({
		url: TRIX.baseUrl + "/api/images/upload?imageType=LOGO"
	});

	var favicon = $scope.favicon = new FileUploader({
		url: TRIX.baseUrl + "/api/images/upload?imageType=FAVICON"
	});

	logo.onAfterAddingFile = function(fileItem) {
		$scope.logoImage = null;
		logo.uploadAll();
	};

	logo.onSuccessItem = function(fileItem, response, status, headers) {
		if(response.filelink){
			$scope.logoImage = response;
			$mdToast.hide();
		}
	};

	logo.onErrorItem = function(fileItem, response, status, headers) {
		if(status == 413)
			$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
		else
			$scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
	}

	logo.onProgressItem = function(fileItem, progress) {
		cfpLoadingBar.start();
		cfpLoadingBar.set(progress/100)
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
		cfpLoadingBar.set(progress/100)
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
		cfpLoadingBar.set(progress/100)
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

	// ------- /upload files -----------

	// -------- remove files -----------
	$scope.removeSplash = function(){
		$scope.splashImage = null;
	}

	$scope.removeLogo = function(){
		$scope.logoImage = null;
	}

	$scope.removeFavicon = function(){
		$scope.faviconImage = null;
	}
	// -------- /remove files -----------


	$scope.saveChanges = function(){
		$scope.disabled = true;
		if($scope.logoImage && $scope.logoImage.id){
			$scope.network.logoImage = ImageDto.getSelf($scope.logoImage)
		}else
			$scope.network.logoImage = null;

		if($scope.splashImage && $scope.splashImage.id){
			$scope.network.splashImage = ImageDto.getSelf($scope.splashImage)
		}else
			$scope.network.splashImage = null;

		if($scope.faviconImage && $scope.faviconImage.id){
			$scope.network.favicon = ImageDto.getSelf($scope.faviconImage)
		}else
			$scope.network.favicon = null;

		trix.putNetwork($scope.network).success(function(response){
			$scope.app.showSuccessToast('Alterações realizadas com sucesso.')
			$scope.disabled = false;

		}).error(function(){
			$scope.disabled = false;
		});
		$scope.saveAuthCredentials();
	}

	$scope.setLanguage = function(languageRef){
		switch(languageRef) {
			case 'en':
				$translate.use(languageRef)
				moment.locale(languageRef);
				break;
			case 'pt':
				$translate.use(languageRef)
				moment.locale(languageRef);
				break;
			default:
				$scope.app.showErrorToast($filter('translate')('messages.settings.network.NO_LANGUAGE'))
				break;
		}
	}

	$scope.authCredentials = {
		facebookAppID: $scope.network.facebookAppID,
		facebookAppSecret: $scope.network.facebookAppSecret,
		googleAppID: $scope.network.googleAppID,
		googleAppSecret: $scope.network.facebookAppSecret
	};
	trix.authCredentials().success(function(response){
		$scope.authCredentials = response;
		if(!$scope.authCredentials)
			$scope.authCredentials = {};
	})
	$scope.saveAuthCredentials = function(){
		trix.updateAuthCredential($scope.authCredentials).success(function(response){
			console.log(response);
		}).error(function(){

		})
	}

	settingsNetworkCtrl	= $scope;
}])

var settingsNetworkCtrl = null;