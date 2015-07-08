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
	}])

app.controller('SettingsSponsorsConfigCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location){

		if($state.params.sponsorId){
			trix.getSponsor($state.params.sponsorId, 'sponsorProjection').success(function(sponsorResponse){
				$scope.sponsor = sponsorResponse;
			})
		}else if($state.params.newSponsor){
			$scope.creating = true;
			console.log('new sponsor');
		}

		$scope.createSponsor = function(){
			trix.postSponsor($scope.sponsor).success(function(){
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
