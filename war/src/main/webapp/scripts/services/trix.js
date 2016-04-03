angular.module('app').service('trixService', function(){
	var that = this;

	/**
	 * select a station based on the properties or the id of a stations
	 */
	 this.selectDefaultStation = function(stations, changeToStationId){
	 	var ret = null;
	 	if(stations){
	 		stations.forEach(function(station){
	 			if(station.main && !changeToStationId){
	 				ret = station;
	 				return;
	 			}
	 		});

	 		if(!ret){
	 			for (var i = 0; i < stations.length; i++) {
	 				var station = stations[i]
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

	/**
	 * check if user is station writer
	 */
	 this.stationIsWritable = function(stationId){
	 	if (!stationId) {return false};
	 	var writable = false;
	 	initData.personPermissions.stationPermissions.forEach(function(permissions, index){
	 		if(permissions.stationId == stationId && (permissions.writable || permissions.writer))
	 			writable = true;
	 	});
	 	return writable;
	 }

	/**
	 * check if user is station admin
	 */
	 this.stationIsAdmin = function(stationId){
	 	if (!stationId) {return false};
	 	var isAdmin = false;
	 	initData.personPermissions.stationPermissions.forEach(function(permissions, index){
	 		if(permissions.stationId == stationId && permission.admin)
	 			isAdmin = true;
	 	});
	 	return isAdmin;
	 }

	 /**
	 * check if user is station admin
	 */
	 this.stationIsEditor = function(stationId){
	 	if (!stationId) {return false};
	 	var isEditor = false;
	 	initData.personPermissions.stationPermissions.forEach(function(permissions, index){
	 		if(permissions.stationId == stationId && permission.editor)
	 			isEditor = true;
	 	});
	 	return isEditor;
	 }

	 this.isLoggedIn = function(){
	 	if (initData.person && initData.person.username != "wordrails")
	 		return true;
	 	return false;
	 }

	/**
	 * get a list of all stations that the user has permission to write
	 */
	 this.getWritableStations = function(){
	 	var stations = [];
	 	initData.personPermissions.stationPermissions.forEach(function(permissions, index){
	 		if(permissions.writable || permissions.writer)
	 			stations.push(permissions)
	 	});
	 	return stations;
	 }

	 this.isNetworkAdmin = function(){
	 	return initData.networkRole ? initData.networkRole.admin : false;
	 }

	 	/**
	 * get a list of all stations that the user has permission to write
	 */
	 this.getAdminStations = function(){
	 	var stations = [];
	 	initData.personPermissions.stationPermissions.forEach(function(permissions, index){
	 		if(permissions.admin)
	 			stations.push(permissions)
	 	});
	 	return stations;
	 }

	 /**
	 * get a list of all stations that the user has permission to write
	 */
	 this.getEditorStations = function(){
	 	var stations = [];
	 	initData.personPermissions.stationPermissions.forEach(function(permissions, index){
	 		if(permissions.editor)
	 			stations.push(permissions)
	 	});
	 	return stations;
	 }

	 this.getStationPermissions = function(initData){
		 	return initData.personPermissions.stationPermissions;
	 }

	});