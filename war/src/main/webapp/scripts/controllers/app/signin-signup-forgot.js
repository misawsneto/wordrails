app.controller('AppSigninCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage){
		$scope.person = {};
		$scope.app.invalidCredentials = false;
		$scope.signin = function(){
			$scope.app.loading = true;
			trix.login($scope.person.username, $scope.person.password).success(function(){
				if($state.params.next)
					document.location.href = $state.params.next
				else
					document.location.href = '/'
				$scope.app.loading = true;
				$scope.app.invalidCredentials = false;
			}).error(function(){
				$scope.app.loading = false;
				$scope.app.invalidCredentials = true;
			})
		}
}]);

app.controller('AppSignupCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage){

}]);

app.controller('AppForgotCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage){

}]);

app.controller('AppNetworkCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage){

		var subdomainChanged = false, personNameChanged = false;
		$scope.createNetworkObject = {}

		$scope.$watch('createNetworkObject.name', function(){
			if(!subdomainChanged && $scope.createNetworkObject.name)
				$scope.createNetworkObject.newSubdomain = $scope.createNetworkObject.name.toSlug();
		})

		$scope.changeSubdomain= function(){ subdomainChanged = true; }

		$scope.$watch('createNetworkObject.person.name', function(){
			if(!personNameChanged && $scope.createNetworkObject.person)
				$scope.createNetworkObject.person.username = $scope.createNetworkObject.person.name.toSlug();

		})

		$scope.changePersonName= function(){ personNameChanged = true; }

		$scope.$watch('createNetworkObject.newSubdomain', function(){
			if($scope.createNetworkObject.person)
			$scope.createNetworkObject.newSubdomain = $scope.createNetworkObject.newSubdomain.toSlug();
		})

		$scope.$watch('createNetworkObject.person.username', function(){
			if($scope.createNetworkObject.person)
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

}]);

app.controller('AppNetworkCreatedCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage){

		trix.tokenSignin($state.params.token).success(function(){
			trix.allInitData().success(function(response){
        initData = response;
        $scope.app.initData = angular.copy(initData);
        $scope.app.loginError = false;
        $scope.app.refreshData();
				$state.go('app.dashboard')
      })
		}).error(function(){
			$state.go('app.stations')
		})
}]);
