app.controller('GeoLocationCtrl', ['$scope', 'leafletData', '$log', '$timeout', function ($scope, leafletData, $log, $timeout) {

    // --- define current lat/long
    $scope.pos = null;
    var geoSuccess = function(startPos) {
      $scope.pos = startPos;
      $scope.center.lat = startPos.coords.latitude;
      $scope.center.lng =  startPos.coords.longitude;
      $scope.center.zoom = 12;
    };

    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(geoSuccess);
      $timeout(function(){
        $log.info('Capturing user location', $scope.pos);
      }, 1000);
    } else {
      $log.info('Geolocation is not supported for this Browser/OS version yet.');
    }
    // --- define current lat/long

    $scope.savedItems = [/*{
        "id": 721,
            "geoJSON": {
            "type": "Feature",
                "geometry": {
                "type": "Point",
                    "coordinates": [-0.626220703125,
                48.1367666796927]
            }
        }
    }, {
        "id": 724,
            "geoJSON": {
            "type": "Feature",
                "geometry": {
                "type": "Point",
                    "coordinates": [-0.274658203125,
                49.13859653703879]
            }
        }
    }*/];
    var drawnItems = new L.FeatureGroup();
    for (var i = 0; i < $scope.savedItems.length; i++) {
        L.geoJson($scope.savedItems[i].geoJSON, {
            style: function(feature) {
                return {
                    color: '#bada55'
                };
            },
            onEachFeature: function (feature, layer) {
                drawnItems.addLayer(layer);
            }
        });
    }

    angular.extend($scope, {
        center: {
            lat: 0,
            lng: 0,
            zoom: 2
        },
        controls: {
        	draw: {
              draw: {
                position: 'topleft',
                polygon: {
                  shapeOptions: {
                    color: "#555555"
                  }
                },
                polyline: false,
                rectangle: false,
                circle: {
                  shapeOptions: {
                    color: "#555555"
                  }
                },
                marker: false
              },
      	     edit: {
    				  featureGroup: drawnItems,
              edit: {   // this property shouldn't be needed
                selectedPathOptions: {
                  color: "#555555",
                  fillColor: '#555555'
                }
              }
    				}
    			}
        }
    });


    leafletData.getMap().then(function (map) {

        var drawnItems = $scope.controls.draw.edit.featureGroup;

        $scope.map = map;
        $scope.drawnItems = drawnItems;

        drawnItems.addTo(map);

        map.on('draw:created', function (e) {
            var layer = e.layer;
            if($scope.app.network.primaryColors)
                if($scope.app.network.primaryColors['300'].value)
                    layer.options.color = $scope.app.rgb2hex($scope.app.network.primaryColors['300'].value)
                else
                    layer.options.color = $scope.app.network.primaryColors['300'];

            drawnItems.addLayer(layer);

            $scope.savedItems.push({
                id: layer._leaflet_id,
                geoJSON: layer.toGeoJSON()
            });
        });

        map.on('draw:edited', function (e) {
            var layers = e.layers;
            layers.eachLayer(function (layer) {

                for (var i = 0; i < $scope.savedItems.length; i++) {
                    if ($scope.savedItems[i].id == layer._leaflet_id) {
                        $scope.savedItems[i].geoJSON = layer.toGeoJSON();
                    }
                }
            });
        });

        map.on('draw:deleted', function (e) {
            var layers = e.layers;
            layers.eachLayer(function (layer) {
                for (var i = 0; i < $scope.savedItems.length; i++) {
                    if ($scope.savedItems[i].id == layer._leaflet_id) {
                        $scope.savedItems.splice(i, 1);
                    }
                }
            });
        });
    });

    geoLocationCtrl = $scope;
}])


var geoLocationCtrl = null;