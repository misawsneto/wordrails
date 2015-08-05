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
		url: TRIX.baseUrl + "/api/files/contents/simple"
	});

	var splash = $scope.splash = new FileUploader({
		url: TRIX.baseUrl + "/api/files/contents/simple"
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

		if($scope.loginImage && $scope.loginImage.id){
			var loginImage = { original: TRIX.baseUrl + "/api/files/" + $scope.loginImage.id }
			trix.postImage(loginImage).success(function(imageId){
				var myLoginImage = TRIX.baseUrl + "/api/images/" + imageId;
				$scope.network.loginImage = myLoginImage;
				trix.putNetwork($scope.network).success(function(){
					$scope.app.initData.network.loginImageId = $scope.loginImage.id
				})
			})
		}
		if($scope.splashImage && $scope.splashImage.id){
			var splashImage = { original: TRIX.baseUrl + "/api/files/" + $scope.splashImage.id }
			trix.postImage(splashImage).success(function(imageId){
				var mySplashImage = TRIX.baseUrl + "/api/images/" + imageId;
				$scope.network.splashImage = mySplashImage;
				trix.putNetwork($scope.network).success(function(){
					$scope.app.initData.network.splashImageId = $scope.splashImage.id
				})
			})
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
