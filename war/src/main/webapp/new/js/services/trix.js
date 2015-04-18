angular.module('app').service('trixService', function(){
	this.selectCurrentStation = function(stations, changeToStationId){
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
});