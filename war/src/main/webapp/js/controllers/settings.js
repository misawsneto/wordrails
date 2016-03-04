app.controller('SettingsCtrl', ['$scope', '$log', '$state', '$filter', '$timeout', '$interval', 'trix', 'cfpLoadingBar', '$q', '$mdToast',
	function($scope, $log, $state, $filter, $timeout, $interval, trix, cfpLoadingBar, $q, $mdToast) {
		var subdomainChanged = false, personNameChanged = false;
		$scope.createNetworkObject = {}

		$scope.$watch('createNetworkObject.name', function(){
			if(!subdomainChanged)
				$scope.createNetworkObject.newSubdomain = $scope.createNetworkObject.name.toSlug();
		})

		$scope.changeSubdomain= function(){ subdomainChanged = true; }

		$scope.$watch('createNetworkObject.person.name', function(){
			if(!personNameChanged)
				$scope.createNetworkObject.person.username = $scope.createNetworkObject.person.name.toSlug();

		})

		$scope.changePersonName= function(){ personNameChanged = true; }

		$scope.$watch('createNetworkObject.newSubdomain', function(){
			$scope.createNetworkObject.newSubdomain = $scope.createNetworkObject.newSubdomain.toSlug();
		})

		$scope.$watch('createNetworkObject.person.username', function(){
			$scope.createNetworkObject.person.username = $scope.createNetworkObject.person.username.toSlug();
		})

		function checkValue(val){
			return val != null && val.length > 0;
		}

		$scope.app.createNetwork = function(createNetworkObject){
			if(!(checkValue(createNetworkObject.name) && checkValue(createNetworkObject.newSubdomain) &&
							checkValue(createNetworkObject.person.name) && checkValue(createNetworkObject.person.username)&& 
							checkValue(createNetworkObject.person.email) && checkValue(createNetworkObject.person.password)) ){

				$scope.showErrorToast('Preencha todos os campos antes de confirmar a criação da rede.')
				return;
			}

			trix.createNetwork(createNetworkObject).success(function(data){
				window.location.href = location.protocol + '//' + createNetworkObject.newSubdomain + '.' + location.hostname.split('.').reverse()[1] + 
				'.' + location.hostname.split('.').reverse()[0] + '/access/networkcreated?token=' + data.token;
			}).error(function(data, status){
				if(status == 400){
					if(data.error && data.error.message && data.error.message.indexOf('Duplicate') > -1
						&& data.error.message.indexOf($scope.createNetworkObject.newSubdomain) > -1){
						$scope.showErrorToast('O subdomínio \"' + $scope.createNetworkObject.newSubdomain + '\" já foi cadastrado.')
					}
					else if(data.errors){
						var errorMsg = ''
						data.errors.forEach(function(error, index){
							if(error.field == 'username')
								errorMsg = errorMsg + 'Usuário inválido. O usuário deve conter entre 3 e 50 caracteres e deve serguir a seguinte expressão regular: <i>^[a-z0-9\\._-]{3,50}$</i>.<br>'
						});

						$scope.showErrorToast(errorMsg);
					}
				}
			});
		}

		$scope.showErrorToast = function(content) {
			$mdToast.show({
				template: '<md-toast style="background-color: rgba(204,34,0,0.95)" ><span flex>'+content+'</span></md-toast>',
				hideDelay: 3000,
				position: $scope.getToastPosition()
			});
		};

		$scope.toastPosition = {
			bottom: false,
			top: true,
			left: false,
			right: true
		};

		$scope.getToastPosition = function() {
			return Object.keys($scope.toastPosition)
			.filter(function(pos) { return $scope.toastPosition[pos]; })
			.join(' ');
		};
	}])


app.controller('NetworkCreatedCtrl', ['$scope', '$log', '$state', '$filter', '$timeout', '$interval', 'trix', 'cfpLoadingBar', '$q', '$mdToast',
	function($scope, $log, $state, $filter, $timeout, $interval, trix, cfpLoadingBar, $q, $mdToast) {

		trix.tokenSignin($state.params.token).success(function(){
			trix.allInitData().success(function(response){
          initData = response;
          $scope.app.initData = angular.copy(initData);
          $scope.app.loginError = false;
          $scope.app.refreshData();
					$state.go('app.settings.stations')
        })
		}).error(function(){
			$state.go('app.stations')
		})
}])