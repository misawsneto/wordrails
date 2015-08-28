app.controller('SettingsSponsorsCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location){
		$scope.app.lastSettingState = "app.settings.sponsors";

		$scope.openDeleteSponsor = function(sponsorId){
			$scope.app.openSplash('confirm_delete_sponsor.html')
			$scope.deleteSponsorId = sponsorId;
		}

		$scope.app.deleteSponsor = function(){
			trix.deleteSponsor($scope.deleteSponsorId).success(function(){
				$scope.app.getInitData();
				$scope.app.showSuccessToast('Alterações realizadas com successo.')
			});
		}

		$scope.setMainSponsor = function(sponsor){
			trix.setMainSponsor(sponsor.id, sponsor.main).success(function(){
				$scope.app.getInitData();
				$scope.app.showSuccessToast('Alterações realizadas com successo.')
			})
		}

            trix.findSponsorByNetworkId($scope.app.initData.network.id).success(function(response){
                  $scope.sponsorsLoaded = true;
                  $scope.sponsors = response.sponsors;
            }).error(function(){
                  $scope.sponsorsLoaded = true;
            })
	}])

app.controller('SettingsSponsorsConfigCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location){

		if($state.params.sponsorId){
			trix.getSponsor($state.params.sponsorId, 'sponsorProjection').success(function(sponsorResponse){
				$scope.sponsor = sponsorResponse;
			})
		}else if($state.params.newSponsor){
			$scope.creating = true;
		}

		FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
          return true; // true|false
      };

      var logo = $scope.logo = new FileUploader({
      	url: TRIX.baseUrl + "/api/files/contents/simple"
      });

      var splash = $scope.splash = new FileUploader({
      	url: TRIX.baseUrl + "/api/files/contents/simple"
      });

      var favicon = $scope.favicon = new FileUploader({
      	url: TRIX.baseUrl + "/api/files/contents/simple"
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


      $scope.createSponsor = function(){
            var sponsor = angular.copy($scope.sponsor)
            sponsor.network = TRIX.baseUrl + '/api/networks/' + $scope.app.initData.network.id;
      	trix.postSponsor(sponsor).success(function(){
      		$scope.app.getInitData();
      		$scope.app.showSuccessToast('Patrocinador criado com successo.')
      	});
      }

      $scope.updateSponsor = function(){
      	trix.putSponsor($scope.sponsor).success(function(){
      		$scope.app.getInitData();
      		$scope.app.showSuccessToast('Alterações realizadas com successo.')
      	});
      }
  }])
