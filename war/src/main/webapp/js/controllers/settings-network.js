app.controller('SettingsNetworkCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix , FileUploader, TRIX){
		$scope.app.lastSettingState = "app.settings.network";
		$scope.network = angular.copy($scope.app.initData.network)

		FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
	    return true; // true|false
	  };

	  $scope.baseUrl = TRIX.baseUrl;

	  var loginUploader = $scope.loginUploader = new FileUploader({
	  	url: TRIX.baseUrl + "/api/files/contents/simple"
	  });

	  var splashUploader = $scope.splashUploader = new FileUploader({
	  	url: TRIX.baseUrl + "/api/files/contents/simple"
	  });

	  var faviconUploader = $scope.faviconUploader = new FileUploader({
	  	url: TRIX.baseUrl + "/api/files/contents/simple"
	  });

	  loginUploader.onAfterAddingFile = function(fileItem) {
	  	$scope.app.editingPost.uploadedImage = null;
	  	loginUploader.uploadAll();
	  };

	  loginUploader.onSuccessItem = function(fileItem, response, status, headers) {
	  	if(response.filelink){
	  		$scope.app.editingPost.uploadedImage = response;
	  		$scope.app.editingPost.uploadedImage.filelink = TRIX.baseUrl + $scope.app.editingPost.uploadedImage.filelink
	  		$scope.app.editingPost.showMediaButtons = false;
	  		$scope.checkLandscape();
	  		$("#image-config").removeClass("hide");
	  		$mdToast.hide();
	  		$scope.postCtrl.imageHasChanged = true
	  	}
	  };

	  loginUploader.onErrorItem = function(fileItem, response, status, headers) {
	  	if(status == 413)
	  		$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
	  	else
	  		$scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
	  }

	  $scope.clearImage = function(){ 
	  	$("#image-config").addClass("hide");
	  	$scope.app.editingPost.uploadedImage = null;
	  	loginUploader.clearQueue();
	  	loginUploader.cancelAll()
	  	$scope.checkLandscape();
	  	$scope.postCtrl.imageHasChanged = true;
	  }

	  loginUploader.onProgressItem = function(fileItem, progress) {
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

	  // -------------------- splash

	  splashUploader.onAfterAddingFile = function(fileItem) {
	  	$scope.app.editingPost.uploadedImage = null;
	  	splashUploader.uploadAll();
	  };

	  splashUploader.onSuccessItem = function(fileItem, response, status, headers) {
	  	if(response.filelink){
	  		$scope.app.editingPost.uploadedImage = response;
	  		$scope.app.editingPost.uploadedImage.filelink = TRIX.baseUrl + $scope.app.editingPost.uploadedImage.filelink
	  		$scope.app.editingPost.showMediaButtons = false;
	  		$scope.checkLandscape();
	  		$("#image-config").removeClass("hide");
	  		$mdToast.hide();
	  		$scope.postCtrl.imageHasChanged = true
	  	}
	  };

	  splashUploader.onErrorItem = function(fileItem, response, status, headers) {
	  	if(status == 413)
	  		$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
	  	else
	  		$scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
	  }

	  $scope.clearImage = function(){ 
	  	$("#image-config").addClass("hide");
	  	$scope.app.editingPost.uploadedImage = null;
	  	splashUploader.clearQueue();
	  	splashUploader.cancelAll()
	  	$scope.checkLandscape();
	  	$scope.postCtrl.imageHasChanged = true;
	  }

	  splashUploader.onProgressItem = function(fileItem, progress) {
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

	  // -------- login image

	  faviconUploader.onAfterAddingFile = function(fileItem) {
	  	$scope.app.editingPost.uploadedImage = null;
	  	faviconUploader.uploadAll();
	  };
	  
	  faviconUploader.onSuccessItem = function(fileItem, response, status, headers) {
	  	if(response.filelink){
	  		$scope.app.editingPost.uploadedImage = response;
	  		$scope.app.editingPost.uploadedImage.filelink = TRIX.baseUrl + $scope.app.editingPost.uploadedImage.filelink
	  		$scope.app.editingPost.showMediaButtons = false;
	  		$scope.checkLandscape();
	  		$("#image-config").removeClass("hide");
	  		$mdToast.hide();
	  		$scope.postCtrl.imageHasChanged = true
	  	}
	  };

	  faviconUploader.onErrorItem = function(fileItem, response, status, headers) {
	  	if(status == 413)
	  		$scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
	  	else
	  		$scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
	  }

	  $scope.clearImage = function(){ 
	  	$("#image-config").addClass("hide");
	  	$scope.app.editingPost.uploadedImage = null;
	  	faviconUploader.clearQueue();
	  	faviconUploader.cancelAll()
	  	$scope.checkLandscape();
	  	$scope.postCtrl.imageHasChanged = true;
	  }

	  faviconUploader.onProgressItem = function(fileItem, progress) {
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
	  }
	}])
