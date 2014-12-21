'use strict';

/* Services */

// Demonstrate how to register services
angular.module('app.services', []);

angular.module('app').service('authService', function authService($q, $http, $location, $rootScope, $cookieStore, WORDRAILS){
	// local state indicating user is authenticated
	var wr = null;

	var person = {};
	var instance = this;

	this.isAuth = function(){
		var p = instance.getPerson();
		if(p && p.username && p.username != "wordrails")
			return true;
		else
			return false
	};

	this.getPerson = function(){
		return person;
	}

	this.setPerson = function(p){
		person = p;
		if(p && p.username && p.username != "wordrails"){
			$cookieStore.put('username', person.username);
		}
	}

	this.signIn = function(username, password, success, error){
		wr = BaseWordRails(WORDRAILS.baseUrl, username, password);
		wr.logIn(function(jqXHQ, text){
			console.log('aqui!');
			if(text == "success"){
				wr.findByUsername(username, function(response){
					person = response[0];
					instance.setPerson(person)
					$rootScope.$broadcast('PERSON_CHANGE', {})
				}).success(function(){
					success && success();
				}).error(function(){
					error && error();
				});
			}else{
				error && error();
			}
		});
		return wr;
	}

	var username = null;
	this.getUsername = function(){
		return username;
	}

	this.setUsername = function(newUsername){
		username = newUsername;
	}

	this.initWR = function(username, password){
		return wr = BaseWordRails(WORDRAILS.baseUrl, username, password);
	}
	
	this.getWR = function(){
		return wr;
	}

	var that = this;

	/** removes user from memory and invokes signout request to the backend
	*/
	this.logout = function(){
		person = null;
		$cookieStore.put('username', null);
	}

	return this;
});

angular.module('app').service('connectionService', function connectionService($rootScope, $http, authService){
	function setDefaultToastrOptions(){
		// defaul notification position
		toastr.options = {"closeButton": true,  "positionClass": "toast-bottom-right"}
	}

	var requestTimeout = null;

	var showingConnectionMessage = false

	function showConnectionProblemMessage(){
		toastr.options.onShown = function() { showingConnectionMessage = true }
		toastr.options.onHidden = function() { showingConnectionMessage = false }
		toastr.info("Não é possível estabelecer conexão o servidor...")
		setDefaultToastrOptions();
		$rootScope.$broadcast("CONNECTION_PROBLEM");
	}

	this.init = function(){
		$(document).ajaxStart(function(event, jqXHR, ajaxOptions){
			if(!showingConnectionMessage){
				requestTimeout = setTimeout(function() {
					showConnectionProblemMessage();
				}, 15000);
			}
	    });

		$(document).ajaxComplete(function(event, jqXHR, ajaxOptions){
        	if(jqXHR.status == 403){
            	$rootScope.$broadcast("UNATHORIZED", jqXHR);
	            window.console && console.log("UNATHORIZED");
	            toastr.info("Você não tem privilégios para executar esta operação.")
	        }else if (jqXHR.status === 0) {
	        	if(!showingConnectionMessage){
	        		showingConnectionMessage = true;
                	showConnectionProblemMessage();
	        	}
            } 

	        clearTimeout(requestTimeout);
	    });
	}
})

angular.module('app').service('wordrailsService', function wordrailsService($q, $http, $filter, $state, $location, $rootScope, $cookieStore, authService, WORDRAILS){
	// local state indicating user is authenticated
	var instance = this;
	
	var network = null;
	var stations = null;
	var deferred = null

	var wr = authService.getWR();

	this.setNetwork = function(newNetwork){
		network = newNetwork;
	}

	this.getNetwork = function(){
		return network;
	}

	this.selectCurrentStation = function(networkObject, changeToStationId){
		var ret = null;
		if(networkObject.stations){
			networkObject.stations.forEach(function(station){
				if(station.main && !changeToStationId){
					ret = station;
					return;
				}
			});

			if(!ret){
				for (var i = 0; i < networkObject.stations.length; i++) {
					var station = networkObject.stations[i]
					if(changeToStationId){
						if(station.id == changeToStationId){
							ret = station;
						break; // exit foreach
					}
				}else{
					ret = station;
					break;
				}
			};}
		}
		return ret;
	}

	var customStyle = {}
	var setInitialStyle = function(network){
		customStyle.backgroundColor = network.backgroundColor ? network.backgroundColor : "#F3F5F9";
		customStyle.navbarColor = network.navbarColor ? network.navbarColor : "#242424";
		customStyle.navbarSecondaryColor = network.navbarSecondaryColor ? network.navbarSecondaryColor : "#505050";
		customStyle.mainColor = network.mainColor ? network.mainColor : "#111111";
		customStyle.primaryFont = network.primaryFont ? network.primaryFont : "'Lato', sans-serif";
		customStyle.secondaryFont = network.secondaryFont ? network.secondaryFont : "'PT Serif', sans-serif";
		customStyle.titleFontSize = network.titleFontSize ? network.titleFontSize : 4.0;
		customStyle.newsFontSize = network.newsFontSize ? network.newsFontSize : 1.0;
	}

	this.getCustomStyle = function(){
		return customStyle;
	}

	var personLoaded = function(){
		var networkId = NETWORK_ID;

		var person = authService.getPerson();

		var stationPermissions = wr.getNetworkPersonPermissions(networkId, function(response){
			person.stationPermissions = response.stationPermissions
			//console.log(response);
		});

		var personPermissions = wr.getPersonPersonsStationPermissions(person.id, function(permissions){
			person.permissions = permissions;
		}, null, null, "stationRoleProjection");
		
		var personRoles = wr.getPersonPersonsNetworkRoles(person.id, function(networkRoles){
			person.networkRoles = networkRoles;
		});

		var network = wr.getNetwork(networkId, function(response){
			instance.setNetwork(response);
			setInitialStyle(response);
			wr.getNetworkLogo(networkId, function(networkLogo){
				var net = instance.getNetwork()
				net.image = $filter('imageLink')(networkLogo.small.id);
			}, null, null, "imageProjection");
		});

		var stations = wr.getNetworkStations(networkId, function(response){
			stations = response;
		});

		var requestMap = $.when(stationPermissions, personPermissions, personRoles, network, stations)

		requestMap.done(function(){
			var myNetwork = instance.getNetwork();
			myNetwork.stations = stations;
			myNetwork.currentStation = instance.selectCurrentStation(myNetwork);
			$rootScope.$broadcast("NETWORK_LOADED", myNetwork);

			instance.checkAllStationPermissions();
			instance.isNetworkAdmin()

			deferred.resolve(network)

		}).fail(function(){
			window.console && console.log("Error getting initial data");
			//location.reload();
			$state.go("app.stations", {stationId: 0}, {reload: true});
			deferred.reject(); // error
		})
	};

	this.getInitialData = function(force){
		deferred = $q.defer();
		var username = authService.getUsername();

		if(instance.getNetwork() && !force){
			deferred.resolve(instance.getNetwork())
			return deferred.promise;
		}

		if($cookieStore.get('username')){
			username = $cookieStore.get('username')
			wr = authService.initWR(username, null);
			wr.getCurrentPerson(function(response){
				wr.findByUsername(username , function(persons){
					authService.setPerson(persons[0]);
					$rootScope.$broadcast("PERSON_LOADED", persons[0]);
					wr.getPersonImage(persons[0].id, function(personImage){
						persons[0].imageMedium = $filter('imageLink')(personImage.medium.id);;
					}, null, null, "imageProjection")
					wr.getPersonCover(persons[0].id, function(personCover){
						persons[0].cover = $filter('imageLink')(personCover.large.id);;
					}, null, null, "imageProjection")
				}).success(personLoaded);
			})
			.error(function(jqXHR){
			  	if(jqXHR.status == 401){
					deferred.reject();
					authService.logout();
			  		location.reload();
			  		return;
			  	}else{
			  		window.console && console.log('should not enter here. status: ' + jqXHR.status);
			  	}

			  	authService.logout();
			  		location.reload();
			  		return;
			});
		}else{
			wr = authService.initWR("wordrails", "wordrails");
			var login = wr.logIn().success(function(){
				wr.findByUsername("wordrails", function(response){
					authService.setPerson(response[0]);
					$rootScope.$broadcast("PERSON_LOADED", response[0]);
				}).success(personLoaded);
			}).error(function(){
				$rootScope.$broadcast("AUTHENTICATION_ERROR", myNetwork);
				window.console && console.error("login error")
			});
		}

		return deferred.promise;
	}

	this.isNetworkAdmin = function(){
		var network = instance.getNetwork()
  		var person = authService.getPerson();

		person.stationPermissions && person.stationPermissions.forEach(function(permissions){
			if(permissions.admin){
				network.stationAdmin = true;return;
			}
		})
		person.networkRoles && person.networkRoles.forEach(function(role){
			if(role.admin)
				network.admin = true;
		})
	}

	this.changeStation = function(station){
	  /**
       * Gets all station perspectives and set a default stationPerspective in $scope.app.network.currentStation.perspective
       */
      var deferred = $q.defer();
      wr.findStationPerspectivesByStation(station.id,function(response){
       	if(response){
       		station.perspectives = response;
            station.perspective = response[0] // sets  station perspective. for now, the first 1;
        }
      }).error(function(){

      }).complete(function(){
      	deferred.resolve()
      });

      return deferred.promise;
  	}

  	this.checkAllStationPermissions = function(){
  		var network = instance.getNetwork()
  		var person = authService.getPerson();
		network.stations.forEach(function(iStation){
			person.stationPermissions && person.stationPermissions.forEach(function(permissions, index){
				if(iStation.id == permissions.stationId){
					iStation.permissions = permissions;
					if(permissions.writable || permissions.writer){
						iStation.canWrite = true;
					}else{
						iStation.canWrite = false;
					}
					if(permissions.editor){
						iStation.editor = true;
					}
					if(permissions.admin){
						iStation.admin = true;
					}
					iStation.visible = true;
  				}
			});
		});
  	}

  	this.getStationPermissions = function(station){
  		var person = authService.getPerson();
  		if(person){
  			person.permissions.forEach(function(permissions){
  				if(station.id == permissions.station.id){
  					return permissions;
  				}
			});
  		}else{
  			window.console && console.error("Couldn't get person")
  		}
  	}

  	this.createPerson = function(person){
  		var deferred = $q.defer()
  		wr.postPerson(person, function(response){
  			deferred.resolve(response)
  		}).error(function(jqXHQ){
  			deferred.reject(jqXHQ);
  		})
  		return deferred.promise;
  	}

  return this;
});

angular.module('app').service('perspectivesService', function perspectivesService($http, $location, $rootScope){
	// local state indicating user is authenticated
	return this;
});
