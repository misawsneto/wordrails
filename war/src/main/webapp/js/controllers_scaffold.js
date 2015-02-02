angular.module('app')


.controller('ScaffoldCtrl', function ScaffoldCtrl($scope, $state, authService){
  $scope.app.authenticated = authService.isAuth();
  if($state.current.name == "app.scaffold")
    $state.go('app.scaffold.perspectives')
})

// ---------------------- ScaffoldCreateNetworksCtrl
.controller('ScaffoldCreateNetworksCtrl', function ScaffoldCreateNetworksCtrl($scope, $state, authService){
  // vars
  var errorMsg = "Houve um erro ao criar a rede. Verifique os dados e tente novamente."
  var successMsg = "Sua rede foi criada."
  var wr = authService.getWR();

  // submit
  $scope.submitCreateNetwork = function(networkName){
    var network = {}
    
    if(networkName) // validate
      network.name = networkName;
    else{
      toastr.error(errorMsg);
      return;
    }

    wr.postNetwork(network, function(response){
      toastr.success(successMsg)
      $state.go("app.scaffold.networks")
    }).error(function(response){
      toastr.error(errorMsg);
    });
  }
})// ---------------------- end ScaffoldCreateNetworksCtrl

 // ---------------------- ScaffoldStationsCtrl
 .controller('ScaffoldStationsCtrl', function ScaffoldStationsCtrl($scope, $modal, authService, WORDRAILS){
  var errorMsg = "Houve um erro ao alterar a estação."
  var successMsg = "Estação alterada com sucesso."

  var wr = authService.getWR();
  $scope.loading = true;

  $scope.stations = []

  var currentNetwork = $scope.app.network;

  $scope.$watch('app.network', function(newVal){
    if(newVal){
      currentNetwork = $scope.app.network;
      updateStations();
    }
  })

  $scope.visibilities = [
  {
    label: "Privada",
    value: "RESTRICTED"
  },
  {
    label: "Pública à rede",
    value: "RESTRICTED_TO_NETWORKS"
  },
  {
    label: "Pública",
    value: "UNRESTRICTED"
  }
  ];

  $scope.readWrites = [
  {
    label: "Escrita",
    value: "write"
  },
  {
    label: "Somente leitura",
    value: "read"
  }
  ];

  $scope.updateVisibility = function(stationObj){
    var station = angular.copy(stationObj);
    if(station.networks){
      for (var i = 0; i < station.networks.length; i++) {
        station.networks[i] = extractSelf(station.networks[i]);
      };
    }
    if(station.newVisibility){
      $scope.app.loading = true;
      station.visibility = station.newVisibility;
      wr.putStation(station, function(){
        safeApply($scope, function(){
          $scope.app.loading = false;
          stationObj.visibility = station.newVisibility;
          toastr.success('Visibilidade atualizada.')
        })
      })
    }
  }

  $scope.setMainStation = function(stationObj){
    if($scope.stations){
      $scope.stations.forEach(function(station){
        if(stationObj.id != station.id)
          station.main = false;
      });
      $scope.stations.forEach(function(station){
        var iStation = angular.copy(station);
        if(iStation.networks){
          for (var i = 0; i < iStation.networks.length; i++) {
            iStation.networks[i] = extractSelf(iStation.networks[i]);
          }
          wr.putStation(iStation, function(){
            safeApply($scope, function(){
              $scope.app.loading = false;
              if(stationObj.id == station.id)
                toastr.success('Estação selecionada.')
            }); 
          }); 
        }
      });
    }
  }

  $scope.updateWritePermission = function(stationObj){
    var station = angular.copy(stationObj);
    if(station.networks){
      for (var i = 0; i < station.networks.length; i++) {
        station.networks[i] = extractSelf(station.networks[i]);
      }
    }

    $scope.app.loading = true;
    if(station.readWrite == "write"){
      station.writable = true;
    }else{
      station.writable = false;
    }

    wr.putStation(station, function(){
      safeApply($scope, function(){
        $scope.app.loading = false;
        stationObj.writable = station.writable;
        toastr.success('Permissão atualizada.')
      })
    })
  }

  function updateStations(){
    wr.getNetworkStations(currentNetwork.id, function(response){
      if(response){ // check response for []
        safeApply($scope, function(){ // util.js safeApply 
          $scope.stations = response;
          $scope.stations.forEach(function(station){
            safeApply($scope, function(){
              station.newVisibility = station.visibility;
              if(station.writable){
                station.readWrite = "write";
              }else{
                station.readWrite = "read";
              }
            });

            wr.getStationNetworks(station.id, function(response) {
              if (response) {
                safeApply($scope, function(){
                  station.networks = response;
                });
              }
            });

          })// end of station forEach
        }); // end of safeApply
      }// end of if
    }, null
    ,function(){
      $scope.app.loading = false;
    });//end of getStations
  }// end of updateStations

  $scope.objectId = null
  /**
   * Modal instance returned by $modal.open();
   */
   $scope.modalInstance = null
  /**
   * Opens the modal window. Requires the $model service injection
   * @param  {String} templateId  [<script id='moda.html'><div class='modal'></div></script>]
   * @param  {String} size     [lg, sm]
   */
   $scope.openModal = function(templateId, size, objectId){
    $scope.objectId = objectId;
    $scope.modalInstance = $modal.open({
      templateUrl: templateId, // the id of the <script> template
      size: size,
      scope: $scope, // pass the current scope. no need for a new controller
    });
  }

  $scope.cancelModal = function () {
    $scope.modalInstance.dismiss('cancel');
  };

  $scope.editStation = function(newName){
    if(!newName){
      toastr.error("Nome inválido");
      return;
    }
    
    if($scope.objectId){
      var thisStation;
      $scope.stations.forEach(function(station){
        if(station.id == $scope.objectId){
          thisStation = {}
          angular.extend(thisStation, station);
          return;
        }
      });// end of foreach
      if(thisStation){
        thisStation.name = newName;
        $scope.app.loading = true;

        // ---------------- extracting sefs from entities
        if(thisStation.networks){
          for (var i = 0; i < thisStation.networks.length; i++) {
            thisStation.networks[i] = extractSelf(thisStation.networks[i]);
          };
        }
        delete thisStation['perspectives']
        thisStation.stationPermissions = extractSelf(thisStation.permission);
        // ---------------- extracting sefs from entities
        wr.putStation(thisStation, function(){// success function callback
          updateStations();// end getStations
          $scope.modalInstance.dismiss('cancel');
        }).error(function(response){ //error function callback
          if(response.status == 500){
            toastr.error(errorMsg)
          }
        }).complete(function(){
          $scope.app.loading = false;
        })
      }else
      toastr.error(errorMsg)
    }// end of if clause
  }

  $scope.deleteStation = function(){
    if($scope.objectId){
      wr.deleteStation($scope.objectId, function(){ // remove success callback
        $scope.app.loading = true;
        updateStations();// end getStations
        $scope.modalInstance.dismiss('cancel');
        toastr.success("Estação removida.");
        wordrailsService.updateStations();
      }).error(function(response){
        if(response.status == 500){
          toastr.error(errorMsg);
        }
      }).complete(function(jqXHR, textStatus){
        $scope.app.loading = false;
        $scope.cancelModal();
      })// end of wr.deleteStation
    }// end of if clause
  } // end of deleteStation scope function

})// ---------------------- end of ScaffoldStationsCtrl

// ---------------------- ScaffoldEditStationsCtrl
 .controller('ScaffoldEditStationsCtrl', function ScaffoldEditStationsCtrl($scope, $state, authService, WORDRAILS){
  var currentNetwork = $scope.app.network;
  $scope.newVisibility = "UNRESTRICTED";
  $scope.readWrite = "read";
  var wr = authService.getWR();

  $scope.$watch('app.network', function(newVal){
    if(newVal){
      currentNetwork = $scope.app.network;
    }
  })

  $scope.visibilities = [
  {
    label: "Privada",
    name: "RESTRICTED"
  },
  {
    label: "Pública à rede",
    name: "RESTRICTED_TO_NETWORKS"
  },
  {
    label: "Pública",
    name: "UNRESTRICTED"
  }
  ];
  var stationId = $state.params.stationId;
  $scope.app.network.stations && $scope.app.network.stations.forEach(function(station){
    if(station.id == stationId){
      $scope.editingStation = {};
      $scope.editingStation = angular.extend($scope.editingStation, station);
      $scope.editingStation.newVisibility = station.visibility
      $scope.readWrite = station.writable ? "write" : "read";
    }
  })

  $scope.submitEditStation = function(){
    wr.getStationNetworks($scope.editingStation.id).success(function(response){
      console.log(response);
      
      var networks = [];
      response.content && response.content.forEach(function(network){
        networks.push(WORDRAILS.baseUrl + "/api/networks/" + network.id);
      });
      $scope.editingStation.networks = networks
      $scope.editingStation.visibility = $scope.editingStation.newVisibility;
      if($scope.readWrite == "write"){
        $scope.editingStation.writable = true
      }else{
        $scope.editingStation.writable = false
      }
      wr.putStation($scope.editingStation).success(function(){
        $scope.app.network.stations && $scope.app.network.stations.forEach(function(station){
          safeApply($scope, function(){
            if(station.id == stationId){
              angular.extend(station, $scope.editingStation)
            }
          });
        })

        toastr.success("Estação atualizada.")
        $state.go("app.scaffold.stations");
      }).error(function(){

      })

    })
  }
 })
// ---------------------- end of ScaffoldCreateStationsCtrl

 // ---------------------- ScaffoldCreateStationsCtrl
 .controller('ScaffoldCreateStationsCtrl', function ScaffoldCreateStationsCtrl($scope, $state, authService, wordrailsService){
  // vars
  var errorMsg = "Houve um erro ao criar a estação. Verifique os dados e tente novamente."
  var successMsg = "Sua estação foi criada."
  var wr = authService.getWR();
  $scope.station = {};

  var currentNetwork = $scope.app.network;
  $scope.newVisibility = "UNRESTRICTED";
  $scope.readWrite = "read"

  $scope.$watch('app.network', function(newVal){
    if(newVal){
      currentNetwork = $scope.app.network;
    }
  })

  $scope.visibilities = [
  {
    label: "Privada",
    name: "RESTRICTED"
  },
  {
    label: "Pública à rede",
    name: "RESTRICTED_TO_NETWORKS"
  },
  {
    label: "Pública",
    name: "UNRESTRICTED"
  }
  ];   

  // submit
  $scope.submitCreateStation = function(stationName){
    var station = {}
    $scope.app.loading = true;

    // util.js
    var network = currentNetwork
    if(stationName && network){ // validate
      station.name = stationName;
      station.networks = [extractSelf(network)]; // util.js extractSelf 
      station.visibility = $scope.newVisibility;
      if($scope.readWrite == "write"){
        station.writable = true
      }else{
        station.writable = false
      }
    }else{
      toastr.error(errorMsg);
      return;
    }

    angular.extend($scope.station, station)

    wr.postStation($scope.station, function(response){
      toastr.success(successMsg)
      $state.go("app.scaffold.stations", null, {reload: true});
      wordrailsService.updateStations();
    }).error(function(response){ // on error callback
      console.log(response)
      if(response.status == 500){
        toastr.error(errorMsg);
      }
    }).complete(function(jqXHR, textStatus){ // on complete callback
      safeApply($scope, function(){
        $scope.app.loading = false;
      })
    });
  }
})// ---------------------- end ScaffoldCreateStationsCtrl

//  ---------------------- ScaffoldTaxonomiesCtrl
.controller('ScaffoldTaxonomiesCtrl', function ScaffoldTaxonomiesCtrl($scope, $modal,authService, WORDRAILS){
  var errorMsg = ""
  var wr = authService.getWR();

  $scope.taxonomies = [];
  $scope.app.loading = true;

  var currentNetwork = $scope.app.network;

  $scope.$watch('app.network', function(newVal){
    if(newVal){
      currentNetwork = $scope.app.network;
      updateTaxonomies();
    }
  })

  var updateTaxonomies = function(){
    wr.getNetworkTaxonomies(currentNetwork.id, function(response){
      $scope.taxonomies = [];
      if(response){ // check response for []
        response.forEach(function(taxonomy){
          taxonomy.network = currentNetwork;
          taxonomy.owningNetwork = extractSelf(currentNetwork);
          safeApply($scope, function(){
            $scope.taxonomies.push(taxonomy)
          });

          wr.getTaxonomyOwningStation(taxonomy.id, function(station){
            safeApply($scope, function(){
              taxonomy.station = station;
              taxonomy.owningStation = extractSelf(station);
            });
          });

        })
      }
    }, null,
    function(){
      safeApply($scope, function(){
        $scope.app.loading = false;
      });
    });//end of getTaxonomies
  }

  $scope.objectId = null
  /**
   * Modal instance returned by $modal.open();
   */
   $scope.modalInstance = null
  /**
   * Opens the modal window. Requires the $model service injection
   * @param  {String} templateId  [<script id='moda.html'><div class='modal'></div></script>]
   * @param  {String} size     [lg, sm]
   */
   $scope.openModal = function(templateId, size, objectId){
    $scope.objectId = objectId;
    $scope.modalInstance = $modal.open({
      templateUrl: templateId, // the id of the <script> template
      size: size,
      scope: $scope, // pass the current scope. no need for a new controller
    });
  }

  $scope.cancelModal = function () {
    $scope.modalInstance.dismiss('cancel');
  };

  $scope.editTaxonomy = function(newName){
    if(!newName){
      toastr.error("Nome inválido");
      return;
    }

    if($scope.objectId){
      var thisTaxonomy;
      $scope.taxonomies.forEach(function(taxonomy){
        if(taxonomy.id == $scope.objectId){
          thisTaxonomy = {}
          angular.extend(thisTaxonomy, taxonomy);
        }
      });// end of foreach
      if(thisTaxonomy){
        thisTaxonomy.name = newName;
        $scope.app.loading = true;
        wr.putTaxonomy(thisTaxonomy, function(){// success function callback
          updateTaxonomies();// end getTaxonomies
          $scope.modalInstance.dismiss('cancel');
        }, function(){ //error function callback
          toastr.error(errorMsg)
        }, function(){
          $scope.app.loading = false;
        })
      }else
      toastr.error(errorMsg)
    }// end of if clause
  }

  $scope.deleteTaxonomy = function(){
    $scope.app.loading = true;
    if($scope.objectId){
      wr.deleteTaxonomy($scope.objectId, function(){ // remove success callback
        updateTaxonomies();// end getTaxonomies
        $scope.modalInstance.dismiss('cancel');
      }, function(){
        toastr.error("Erro ao remover taxonomia");
      }, function(jqXHR, textStatus){
        // window.console && console.log(textStatus)
        $scope.app.loading = false;
      })// end of wr.getTaxonomy
    }// end of if clause
  } // end of deleteTaxonomy scope function

})// ---------------------- end of ScaffoldTaxonomiesCtrl

// ---------------------- Scaffold Create Taxonomies controller
.controller('ScaffoldCreateTaxonomiesCtrl', function ScaffoldCreateTaxonomiesCtrl($scope, $state, authService, WORDRAILS){
  // vars
  var errorMsg = "Houve um error ao criar a taxonomia. Verifique os dados e tente novamente."
  var successMsg = "Sua taxonomia foi criada."
  var wr = authService.getWR();

  var currentNetwork = $scope.app.network;

  $scope.$watch('app.network', function(newVal){
    if(newVal){
      currentNetwork = $scope.app.network;
      getStations();
    }
  })

  $scope.type = 'N'

  var getStations = function(){
    wr.getNetworkStations(currentNetwork.id, function(stations){
    if(stations){ // check response for []
      safeApply($scope, function(){ // util.js safeApply 
        $scope.stations = stations;
      });
    }
  }, null
  ,function(){
    safeApply($scope, function(){
      $scope.app.loading = false;
    })
  });//end of getStations
  }

  /* ---- type selector ---- */

  // submit
  $scope.submitCreateTaxonomy = function(taxonomyName, stationId){
    $scope.app.loading = true;
    var taxonomy = {}
    var type = $scope.type;
    if(!taxonomyName || (type != 'N' && type != 'S')){
      toastr.error(errorMsg);
      $scope.app.loading = false;
      return;
    }

    taxonomy.name = taxonomyName;
    taxonomy.type = type;

    if(taxonomy.type == "N"){
      taxonomy.owningNetwork = extractSelf(currentNetwork)
    }else if(taxonomy.type == "S"){
      var station = findById($scope.stations, stationId);
      taxonomy.owningStation = extractSelf(station)
    }

    var myAjax = wr.postTaxonomy(taxonomy, function(taxonomyId, status, jqXHR){
     wr.getTaxonomy(taxonomyId, function(newTaxonomy){
      var ref = extractSelf(newTaxonomy);
      var list = [ref];
      wr.patchNetworkTaxonomies(currentNetwork.id, list, function(){})
      toastr.success("Taxonomia criada com successo.")
      $state.go("app.scaffold.taxonomies");
    }, function(){
      window.console && console.error("Não foi possível pegar a taxonomia recem criada");  
    });

    }).error(function(response){ // on error ajax callback
      toastr.error("Você não tem permissão para criar taxonmias para esta estação.");
    }).complete(function(jqXHR, textStatus){ // on complete
      safeApply($scope, function(){
        $scope.app.loading = false;
      });
    });
  }
})// ---------------------- end ScaffoldCreateTaxonomyCtrl

// ---------------------- Scaffold Create Taxonomies' Terms controller
.controller('ScaffoldTaxonomiesTermsCtrl', function ScaffoldTaxonomiesTermsCtrl($scope, $state, $modal, $stateParams, authService, WORDRAILS){
  // vars
  var errorMsg = "Houve um erro ao criar a taxonomia. Verifique os dados e tente novamente."
  var successMsg = "Sua taxonomia foi criada."
  var wr = authService.getWR();

  if(!$stateParams.taxonomyId){
    toastr.error("Não foi possível encontrar a taxonomia.");
    return 
  }else
  $scope.currentTaxonomy = $stateParams.taxonomyId

  sort = []
  sort.push("id,desc")
  /**
   * [Updates the termTree variable]
   */
   $scope.termTree = []

   var updateTermTree = function(){
    if($scope.currentTaxonomy){
      //taxonomyId, page, size, sort, _success, _error, _complete
      wr.findRoots($scope.currentTaxonomy, function(response){
        if(response){ // if terms > 0
          var terms = response;
          $scope.termTree = terms;
          terms.forEach(function(term){ // term iteration
            term.nodes = null;
            recursiveFetchTerm(term);
          }); // terms foreach loop
        } // end if terms
      }, function(){

      }, function(){

      });
    }
  };

  function recursiveFetchTerm(term){
   wr._ajax({
    type: "GET",
    url: WORDRAILS.baseUrl+"/api/terms/"+term.id+"/children",
    success: function(response){
      if(response && response.content){
        safeApply($scope, function(){
          var nodes = response.content
          if(nodes){
            term.nodes = nodes;
            nodes.forEach(function(node){
              recursiveFetchTerm(node);
            })
          }
        });
      }
    },
    error: function(){
      toastr.error("Houve um erro inesperado.");
    },
    complete:function(){
      $scope.app.loading = false;
    }
  });
 }

 $scope.updateTerms = function(){
  var filter = $scope.termFilter;
  originalTerm.forEach(function(term){
    var regexFilter = new RegExp(filter,"i")
    if(term.name.match(regexFilter)){
      window.console && console.log(term.name)
    }
  });
}

$scope.collapseAll = function() {
  var scope = angular.element(document.getElementById("tree-root")).scope();
  if(scope) scope.collapseAll();
};

$scope.expandAll = function() {
  var scope = angular.element(document.getElementById("tree-root")).scope();
  if(scope) scope.expandAll();
};

updateTermTree();

  /**
   * Opens the modal models based in the template id
   * @param  {String} templateId [The id of the <script> template representing the modal]
   * @param  {[type]} size       [sm, lg]
   * @param  {[type]} treeTerm   [the treeTerm reference for the treeTerm related to the modal]
   */
   $scope.openModal = function(templateId, size, treeTerm){
    $scope.treeTerm = treeTerm;
    $scope.modalInstance = $modal.open({
      templateUrl: templateId, // the id of the <script> template
      size: size,
      scope: $scope, // pass the current scope. no need for a new controller
    });
  }

  $scope.cancelModal = function () {
    $scope.modalInstance.dismiss('cancel');
  };

  $scope.submitCreateTopTerm = function(newName){
    $scope.app.loading = true;
    if(!newName){
      toastr.error("Termo inválido.")
      $scope.app.loading = false;
      return;
    }

    var term = {}
    term.name = newName;
    term.taxonomy = WORDRAILS.baseUrl + "/api/taxonomies/" + $scope.currentTaxonomy;

    wr.postTerm(term, function(termId){ // on success termId is returned
      if(termId){
        safeApply($scope, function(){
          $scope.termTree.push({"id": termId, "name": newName, nodes: []});
          toastr.success("Novo termo adicionado.")
        })
      }
    }, function(){ // on create term error
      safeApply($scope, function(){
        $scope.app.loading = false
      })
    }, function(){ // on create term complete
      $scope.modalInstance.dismiss('cancel');
      safeApply($scope, function(){
        $scope.app.loading = false
      })
    })
  }

  $scope.submitCreateTerm = function(newName){
    $scope.app.loading = true;
    if(!newName){
      toastr.error("Termo inválido.")
      $scope.app.loading = false;
      return;
    }
    // the treeTerm object is the parent term
    if($scope.treeTerm && newName){
      var parentTerm = $scope.treeTerm.$modelValue;
      var term = {}
      term.name = newName
      term.taxonomy = WORDRAILS.baseUrl + "/api/taxonomies/" + $scope.currentTaxonomy
      /**
       * Create term first and on success, update it and create the child
       */
      wr.postTerm(term, function(termId){ // on success termId is returned
        if(termId){
          wr._ajax({
            type: "PUT",
              url: WORDRAILS.baseUrl + "/api/terms/"+termId+"/parent", // the resource to be updated
              data: WORDRAILS.baseUrl + "/api/terms/"+parentTerm.id,
              contentType: "text/uri-list",
              success: function(){
                safeApply($scope, function(){
                  parentTerm.nodes.push({"id": termId, "name": newName, nodes: []});
                  toastr.success("Novo termo adicionado.")
                })
              },
              error: function(){

              },
              complete: function(){
                safeApply($scope, function(){
                  $scope.app.loading = false
                })
              }
            });
        }else{
          safeApply($scope, function(){
            $scope.app.loading = false
          });
        }
      }, function(){ // on create term error
        safeApply($scope, function(){
          $scope.app.loading = false
        })
      }, function(){ // on create term complete
        $scope.modalInstance.dismiss('cancel');
        safeApply($scope, function(){
          $scope.app.loading = false
        })
      })
  }
}

$scope.submitEditTerm = function(newName){
  $scope.app.loading = true;
  if(!newName){
    toastr.error("Termo inválido.")
    return;
  }

  if($scope.treeTerm && newName){
    var termNode = $scope.treeTerm.$modelValue;
    var term = {};
    term.id = termNode.id;
    term.name = newName;
    term.taxonomy = WORDRAILS.baseUrl + "/api/taxonomies/" + $scope.currentTaxonomy;
    wr.putTerm(term, function(){
      safeApply($scope, function(){
        termNode.name = newName;
        toastr.success("Termo atualizado.")
        $scope.modalInstance.dismiss('cancel');
      });
    }, function(){
      toastr.error("Houve um erro ao atualizar o termo");
    }, function(){
      safeApply($scope, function(){
        $scope.app.loading = false;
      });
    });
  }
}

$scope.submitDeleteTerm = function(){
  $scope.app.loading = true;
  if($scope.treeTerm){
    var termNode = $scope.treeTerm.$modelValue;
    wr.deleteTerm(termNode.id, function(){
      safeApply($scope, function(){
        $scope.treeTerm.remove();
        toastr.success("Termo atualizado.")
        $scope.modalInstance.dismiss('cancel');
      });
    }, function(){
      toastr.error("Houve um erro ao remover o termo");
    }, function(){
      safeApply($scope, function(){
        $scope.app.loading = false;
      });
    });
    }// end if treeTerm
  }

})// ---------------------- end ScaffoldTaxonomiesTermsCtrl


// ---------------------- Scaffold Perspectives controller
.controller('ScaffoldPerspectivesCtrl', function ScaffoldPerspectivesCtrl($scope, $state, authService, WORDRAILS, wordrailsService){
  // vars
  var errorMsg = "Houve um problema com a criação da rede. Verifique os dados e tente novamente."
  var successMsg = "Sua rede foi criada."
  var wr = authService.getWR();

  $scope.stations = []
  $scope.perspectives = [];
  var network = wordrailsService.getNetwork();

  if(network && network.stations){
    network.stations.forEach(function(station){
      if(station.admin){
        wr.findStationPerspectivesByStation(station.id, function(perspectives){
          if(perspectives){
            perspectives.forEach(function(perspective){
              safeApply($scope, function(){
                perspective.station = station;
                $scope.perspectives.push(perspective);
              });
            });
          }
          // end of success
        }).error()
      }
    })// end of foreach  
  }else{
    window.console && console.error("error fetching network and stations")
  }
  
})// ---------------------- end ScaffoldPerspectivesCtrl

// ---------------------- Scaffold Create Perspectives controller
/**
 * @see  TermsSelectorCtrl & PerspectiveBuilderCtrl
 */
 .controller('ScaffoldCreatePerspectivesCtrl', function ScaffoldCreatePerspectivesCtrl($scope, $state, $modal, authService, WORDRAILS, wordrailsService){
  // vars
  var errorMsg = "Houve um erro ao criar a rede. Verifique os dados e tente novamente."
  var successMsg = "Sua rede foi criada."
  var wr = authService.getWR();

  $scope.submitReady = false; // used to enable/disable submit perspective button

  $scope.stations = [];

  $scope.p = {};
  $scope.p.name = "";
  $scope.p.currentTaxonomy = null//this will also be used in TermsSelectorCtrl & PerspectiveBuilderCtrl
  $scope.p.currentStation = null //this will also be used in TermsSelectorCtrl & PerspectiveBuilderCtrl
  $scope.p.perspectiveList = null //this will also be used in TermsSelectorCtrl & PerspectiveBuilderCtrl
  $scope.p.currentPerspective = null//this will also be used in TermsSelectorCtrl & PerspectiveBuilderCtrl

  $scope.p.termTree = null;//this will also be used in TermsSelectorCtrl & PerspectiveBuilderCtrl
  $scope.p.termPerspectiveView = null;//this will also be used in TermsSelectorCtrl & PerspectiveBuilderCtrl
  $scope.p.selectedTerm = null;//this will also be used in TermsSelectorCtrl & PerspectiveBuilderCtrl
  $scope.p.featuredPosts = [];
  $scope.p.splashedPosts = [];
  $scope.p.featuredPostNumber = 1

  var network = wordrailsService.getNetwork();

  safeApply($scope, function(){ // util.js safeApply 
    $scope.stations = network.stations;
  });

  // define if perspective is been edited or created
  $scope.p.editing = false;
  if($state.current.name.indexOf("edit") > -1 || $state.current.url.indexOf("edit") > -1){
    window.console && console.log('EDITING')
    getPerspectiveData();
    $scope.p.editing = true;
  }else{
    window.console && console.log('NOT EDITING')
    $scope.p.editing = false;
  }
  /**
   * Propagate changes to the perspective to all child controllers
   */
   $scope.$watch('p', function(){}, true);
  /**
   * Get taxonomies once a station is selected
   */
   function getStationTaxonomies(station){
    if(!station || !station.id){
      toastr.error("Erro inesperado!");
      return;
    }
    wr.findByStationId(station.id, function(response) {
      if (response) { //if response
        safeApply($scope, function(){
          station.taxonomies = response;
        });
      }// end if response
    });
  }

  $scope.$watch('p.currentTaxonomy', function(newVal){
    if(newVal){
      var taxonomy = newVal;
      wr.getTaxonomyTerms(newVal.id, function(response){
        safeApply($scope, function(){
          newVal.terms = response;
        });
      });
    }
  });

  $scope.getTaxonomyTerm = function(id){
    if($scope.p.currentTaxonomy && $scope.p.currentTaxonomy.terms){
      for (var i = 0; i < $scope.p.currentTaxonomy.terms.length; i++) {
        var term = $scope.p.currentTaxonomy.terms[i]
        if(term.id == id)
          return term;
      };
    }
  }

  // If not editing, fetch statins and taxonoies for creation 
  if(!$scope.p.editing){
    // watch for changes in station selector
    $scope.stationId = null;
    $scope.$watch('stationId', function(newVal, oldVal){
      var stationId = newVal;
      if($scope.stations && stationId){
        var selectedStation = null
        $scope.stations.forEach(function(station){
          if(stationId == station.id){
            $scope.p.currentStation = station
            safeApply($scope, function(){
              $scope.p.currentTaxonomy = null;
            })
            getStationTaxonomies(station)
          }
        }); // stations iteration
      }
    });
    // watch for changes in taxonomy selector
    $scope.taxonomyId = null;
    $scope.$watch('taxonomyId', function(newVal, oldVal){
      var taxonomyId = newVal;
      if($scope.p.currentStation && taxonomyId){
        var selectedTaxonomy = null
        $scope.p.currentStation.taxonomies.forEach(function(taxonomy){
          if(taxonomyId == taxonomy.id){
            $scope.p.currentTaxonomy = taxonomy;
          }
        }); // taxonomies iteration
      }
    });
  } // end of if editing condition

  /**
   * If editing a perspectice, get all entity relationship
   */
   function getPerspectiveData(){
    $scope.p.currentPerspective = null
    if($state.params && $state.params.perspectiveId){
      wr.getStationPerspective($state.params.perspectiveId, function(response){
        safeApply($scope, function(){
          $scope.p.currentPerspective = response;
          $scope.p.name = response.name;
          var perspectiveId = $state.params.perspectiveId
          getTermsPerspectives(perspectiveId)
        })
      }).error(function(){

      }).complete(function(){

      })
    }
  }
  /**
   * Get term perspectives and when finished call
   * getPerspectiveStation(perspectiveId)
   * getPerspectiveTaxonomy(perspectiveId);
   */
   function getTermsPerspectives(perspectiveId){
    wr.getStationPerspectivePerspectives(perspectiveId, function(response){
      safeApply($scope, function(){
        if(response){
          $scope.p.perspectiveList = response;
          $scope.p.perspectiveList.forEach(function(termPerspective){
            // for each termPerspective, get its term
            wr.getTermPerspectiveTerm(termPerspective.id, function(term){
              safeApply($scope, function(){
                termPerspective.term = term;
              })
            }).error(function(){
              //if error 404
              safeApply($scope, function(){ 
                termPerspective.term = {id: null, station: true}
              })
            });// end of ajax

          }); // end of foreach
        }
      })// end of safe apply
    }).error().complete(function(){
      getPerspectiveStation(perspectiveId)
      getPerspectiveTaxonomy(perspectiveId); // get station taxonomy only after the list of perspective
    })
  }

  /**
   * Get the taxonomy for the perspectiveId
   */
   function getPerspectiveTaxonomy(perspectiveId){
    wr.getStationPerspectiveTaxonomy(perspectiveId, function(response){
      safeApply($scope, function(){
        if(response)
          $scope.p.currentTaxonomy = response;
      })
    }).error(function(){

    }).complete(function(){

    })
  }
  /**
   * Get the station for the perspectiveId
   */
   function getPerspectiveStation(perspectiveId){
    wr.getStationPerspectiveStation(perspectiveId, function(response){
      safeApply($scope, function(){
        if(response)
          $scope.p.currentStation = response;
      })
    }).error(function(){

    }).complete(function(){

    })
  }

  /**
   * Opens the modal window. Requires the $model service injection
   * @param  {String} templateId  [<script id='moda.html'><div class='modal'></div></script>]
   * @param  {String} size     [lg, sm]
   */
   $scope.openModal = function(templateId, size){
     $scope.modalInstance = $modal.open({
      templateUrl: templateId, // the id of the <script> template
      size: size,
      scope: $scope, // pass the current scope. no need for a new controller
    });
   }

   $scope.cancelModal = function () {
    $scope.modalInstance.dismiss('cancel');
  };

  $scope.submitPerspective = function(){
    if(isOverride()){
      $scope.openModal('confirm_perspective.html', null);
      return;
    }

    createPerspective();
  }

  function isOverride(){
    var ret = false;
    $scope.p.perspectiveList.forEach(function(termPerspective){
      if(termPerspective.term.id == $scope.p.selectedTerm.id){
        $scope.p.termPerspectiveView.id = termPerspective.id
        ret = true;
      }
    })

    return ret;
  }

  $scope.confirmCreatePerspective = function(){
    if($scope.p.currentPerspective.name === $scope.p.name){
      createPerspective();
    }else{
      $scope.p.currentPerspective.name = $scope.p.name;
      wr.putStationPerspective($scope.p.currentPerspective, function(response){
        createPerspective();
      }, function(){}, function(){
        toastr.error("Houve um erro ao atualizar a perspectiva.")
      })
    }
  }

  function createPerspective(){
    var tempTermPerspectiveView = {}
    tempTermPerspectiveView = angular.copy($scope.p.termPerspectiveView);
    tempTermPerspectiveView.ordinaryRows.forEach(function(row, index){
      row.index = index
    });

    console.log($scope.p.termPerspectiveView)

    tempTermPerspectiveView.featuredPosts = $scope.p.featuredPosts;
    tempTermPerspectiveView.splashedPosts = $scope.p.splashedPosts;

    tempTermPerspectiveView.ordinaryRows.forEach(function(row){
      if(row.id == null){
        delete row['id'];
      }
      if(row.termPerspectiveId == null){
        delete row['termPerspectiveId']
      }
      delete row['term'];
      for (var i = row.cells.length - 1; i >= 0; i--) {
        var cell = row.cells[i];
        if(!cell.postView)
          continue

        if(!cell.postView.new){
          row.cells.splice(i,1);
        }else
        cell.index = i;
        delete cell['id'];
        delete cell.postView['new'];
        delete cell.postView['img'];
      };
      if(row.cells.length == 0){
        delete row['cells'];
      }
    });

    console.log(tempTermPerspectiveView.splashedPosts);

    if(tempTermPerspectiveView.featuredPosts.length > 0){ 
      tempTermPerspectiveView.featuredRow = {}
      
      if($scope.p.termPerspectiveView && $scope.p.termPerspectiveView.featuredRow)
        tempTermPerspectiveView.featuredRow = $scope.p.termPerspectiveView.featuredRow;

      tempTermPerspectiveView.featuredRow.cells = []
      tempTermPerspectiveView.featuredRow.type = "F"
      tempTermPerspectiveView.featuredPosts.forEach(function(post){
        if(post){
          tempTermPerspectiveView.featuredRow.cells.push(post)
          delete post.postView['img'];
          if(post.postView.id == null)
            delete post.postView['id'];
        }
      })
      tempTermPerspectiveView.featuredRow.termId = tempTermPerspectiveView.termId;

    }else{
      tempTermPerspectiveView.featuredRow = null;
    }

    if(tempTermPerspectiveView.splashedPosts.length > 0){ 
      tempTermPerspectiveView.splashedRow = {}
      
      if($scope.p.termPerspectiveView && $scope.p.termPerspectiveView.splashedRow)
        tempTermPerspectiveView.splashedRow = $scope.p.termPerspectiveView.splashedRow;

      tempTermPerspectiveView.splashedRow.cells = []
      tempTermPerspectiveView.splashedRow.type = "S"
      tempTermPerspectiveView.splashedPosts.forEach(function(post){
        if(post){
          tempTermPerspectiveView.splashedRow.cells.push(post)
          delete post.postView['img'];
          delete post.postView['new'];
          delete post['$$hashKey'];
          if(post.postView.id == null)
            delete post.postView['id'];
        }
      })
      tempTermPerspectiveView.splashedRow.termId = tempTermPerspectiveView.termId;
    }else{
      tempTermPerspectiveView.splashedRow = null;
    }

    delete tempTermPerspectiveView['featuredPosts']
    delete tempTermPerspectiveView['splashedPosts']
    
    if(tempTermPerspectiveView.id == null){
      delete tempTermPerspectiveView['id']
    }
    
      //delete tempTermPerspectiveView['splashedRow']
    /*if(tempTermPerspectiveView.termId == null){
      delete tempTermPerspectiveView['termId']
    }*/
    console.log(tempTermPerspectiveView);

    //return;

    if(tempTermPerspectiveView.id){ // if is update
      wr._ajax({
        url: WORDRAILS.baseUrl + "/api/perspectives/termPerspectiveDefinitions/" + tempTermPerspectiveView.id,
        data: JSON.stringify(tempTermPerspectiveView),
        type: "PUT",
        contentType: "application/json",
        success: function(response){
          toastr.success("Perspectiva atualizada.")
          $scope.cancelModal()
        }
      });

      return;
    }else{
      wr._ajax({
        url: WORDRAILS.baseUrl + "/api/perspectives/termPerspectiveDefinitions",
        data: JSON.stringify(tempTermPerspectiveView),
        type: "POST",
        contentType: "application/json",
        success: function(response){
          $scope.p.termPerspectiveView.id = response;
          $scope.cancelModal();
          toastr.success("Perspectiva atualizada.")
        }
      });
    }
  }

})// ---------------------- end ScaffoldPerspectivesCtrl

// ---------------------- Scaffold Create Perspectives's Perspective Builder controller
.controller('TermsSelectorCtrl', function TermsSelectorCtrl($scope, $state, authService, WORDRAILS){
  $scope.p.termTree = null;
  var wr = authService.getWR();


  var updateTermTree = function(){
    $scope.p.termTree = null;
    $scope.p.infoFlows = null;
    $scope.p.selectedTerm = null;
    if($scope.p.currentTaxonomy){
      //taxonomyId, page, size, sort, _success, _error, _complete
      wr.findRoots($scope.p.currentTaxonomy.id, function(response){
        var terms = response;
        safeApply($scope, function(){
          $scope.p.termTree = terms;
        });

        if(terms){ // if terms > 0
          terms.forEach(function(term){ // term iteration
            term.nodes = null;
            recursiveFetchTerm(term);
          }); // terms foreach loop
          setTopTerms(terms);
        } // end if terms

      }, function(){
      }, function(){
      });
    }
  };

  function recursiveFetchTerm(term){
    wr.getTermChildren(term.id, function(response){
      if(response){
        safeApply($scope, function(){
          var nodes = response
          if(nodes){
            term.nodes = nodes;
            nodes.forEach(function(node){
              recursiveFetchTerm(node);
            })
          }
        });
      }
    },
    function(){
      toastr.error("Houve um erro inesperado.");
    },
    function(){
      $scope.app.loading = false;
    });

  }

  /**
   * Select root station perspective
   */
   $scope.selectTopLevelTerm = function(){
    if($scope.p.termTree){
      setTopTerms($scope.p.termTree)
    }
  }
  /**
   * Select a term to create a perspective
   */
   $scope.selectTerm = function(term){
    if(term && term.$modelValue.active){
      toastr.info("Já existe uma perspectiva definida para este termo.");
    }
    $scope.p.infoFlows = [];
    // window.console && console.log(term.$modelValue);
    $scope.p.selectedTerm = term.$modelValue;
   /* // $scope.p.infoFlows = term.$modelValue.nodes;
    term.$modelValue.nodes.forEach(function(termNode){
      //window.console && console.log(termNode);
      $scope.p.infoFlows.push({"id": termNode.id, "name": termNode.name});
    });
//getInfoFlowPosts();*/
}

  /**
   * Checks if term is already associated with a perspective.
   * -- When a perspective is loaded, it's term might not be
   */
   function checkTermPerspectiveInstance(term){
    ret = null;
    $scope.p.perspectiveList.forEach(function(termPerspective){
      var checks = 0;
      function run(){
        if(!termPerspective.term){
          checks++;
          if(checks > 5){
            console.error("Erro ao resgatar termo da perspectiva.");
            return;
          }
          wait();
        }else if(termPerspective.term.id == term.id){
          term.active = true;
          ret = termPerspective
        }
      }

      function wait(){
        window.console && console.log("waiting for term to be loaded.");
        window.setTimeout(function() {
          run();
        }, 100);
      }
      run();
    });
    return ret
  }

  function setTopTerms(terms){
    $scope.p.selectedTerm = {name: $scope.p.currentStation.name, id: null, station: true}
    /*$scope.p.infoFlows = [];
    if(terms){
      terms.forEach(function(termNode){
        $scope.p.infoFlows.push({"id": termNode.id, "name": termNode.name});
      });
      //getInfoFlowPosts();
    }*/
  }

  $scope.collapseAll = function() {
    var scope = angular.element(document.getElementById("tree-root")).scope();
    if(scope) scope.collapseAll();
  };

  $scope.expandAll = function() {
    var scope = angular.element(document.getElementById("tree-root")).scope();
    if(scope) scope.expandAll();
  };

  $scope.$watch('p.currentTaxonomy', function(newVal, oldVal){
    updateTermTree();
  });

  function loadTermPerspectiveView(termId, stationPerspectiveId){
    var data = {
      stationPerspectiveId: stationPerspectiveId,
      page: 0,
      size: 15
    }
    if(termId){
      data.termId = termId
    }
    wr._ajax({
      url: WORDRAILS.baseUrl + "/api/perspectives/termPerspectiveViews",
      data: data,
      success: function(response){
        safeApply($scope, function(){
          $scope.p.termPerspectiveView = response
          if(!response.ordinaryRows)
            return;
          response.ordinaryRows.forEach(function(row){
            var term = $scope.getTaxonomyTerm(row.termId);
            row.term = term;
          })

          if(!$scope.p.termPerspectiveView.featuredRow){
            $scope.p.termPerspectiveView.featuredRow = {};
            $scope.p.termPerspectiveView.featuredRow.cells = [];
          }

          if(!$scope.p.termPerspectiveView.splashedRow){
            $scope.p.termPerspectiveView.splashedRow = {};
            $scope.p.termPerspectiveView.splashedRow.cells =[]
          }

          $scope.p.featuredPosts = $scope.p.termPerspectiveView.featuredRow.cells;
          $scope.p.splashedPosts = $scope.p.termPerspectiveView.splashedRow.cells;

          arrangeOrder($scope.p.termPerspectiveView);
        });
      }
    });
  }// end of load termPerspectiveView function

  function arrangeOrder(termPerspectiveView){
    var temp = []
    if(termPerspectiveView){
      or = termPerspectiveView.ordinaryRows
      
      if(!or)
        return;
      
      or.sort(ordinaryRowCompare);
    }

    safeApply($scope, function(){
      if($scope.p.featuredPosts.length <= 3){
        $scope.p.featuredPostNumber = $scope.p.featuredPosts.length
        if($scope.p.featuredPostNumber == 0)
          $scope.p.featuredPostNumber = 1;
      }else{
        $scope.p.featuredPostNumber = 3;
      }
    })
  }

  $scope.$watch('p.termPerspectiveView', function(newVal, oldVal){
    //console.log(newVal);
  }, true);    

  $scope.$watch('p.selectedTerm', function(newVal, oldVal){
    if(newVal != null){
      var term = newVal;
      loadTermPerspectiveView(term.id, $scope.p.currentPerspective.id)
    }
  });

})// ---------------------- end TermsSelectorCtrl

// ---------------------- Scaffold Create Perspectives's Perspective Builder controller
.controller('PerspectiveBuilderCtrl', function PerspectiveBuilderCtrl($scope, $state, $compile, authService, WORDRAILS){
  var wr = authService.getWR();

  $scope.searchQuery = "";
  $scope.searchResult = [];
  $scope.submitSearchPosts = function(query){

    $scope.app.loading = true;
    $scope.searchQuery - query;
    runSearch(query)
  }

  $scope.search = {} // controller scope object
  $scope.search.page = 0;
  $scope.maxSize = WORDRAILS.pageSize;

  $scope.$watch('search.page', function(newVal){
    if($scope.app.searchQuery && newVal){
      runSearch($scope.app.searchQuery);
    }
  });

  var runSearch = function(query){
    wr.searchPostsFromOrPromotedToStation($scope.p.currentStation.id,
      query, $scope.search.page, WORDRAILS.pageSize, 
      function(result){
        safeApply($scope, function(){
          $scope.search.hits = result.hits;
          $scope.search.result = result.posts;
        });
        bindDragToPosts();
      }, null, function(){
        safeApply($scope, function(){
          $scope.app.loading = false;
        });
      });
  }

  $scope.splashedOptions = {
    axis: "x"      
  };

  $scope.$watch('app.termPerspectiveView', function(newVal){
    //console.log(newVal);
  })

  /**
   * [removeFeaturedPost description]
   * @param  int position the position to which remove from
   */
   $scope.removeFeaturedPost = function(index){
    if($scope.p.featuredPosts)
      $scope.p.featuredPosts.splice(index, 1);
  }
  /**
   * select the number of posts to feature
   */
   $scope.changeFeaturedPostsNumber = function(number){
    $scope.p.featuredPostNumber = number
  }

  $scope.removeFixedPost = function(scope){
    var row = scope.$parent.row
    var cell = scope.cell
    for (var i = row.cells.length - 1; i >= 0; i--) {
      var icell = row.cells[i]
      if(icell == cell ){
        //console.log(cell.postView.postId)
        safeApply($scope, function(){
          row.cells.splice(i, 1);
        })
      }
    };
  }

  $scope.removeSplashedPost = function(scope){
    cell = scope.cell;
    for (var i = $scope.p.splashedPosts.length - 1; i >= 0; i--) {
      var icell = $scope.p.splashedPosts[i]
      if(icell == cell ){
        //console.log(cell.postView.postId)
        safeApply($scope, function(){
          $scope.p.splashedPosts.splice(i, 1);
        })
      }
    };
  }

  var bindDragToPosts = function(){
    $(".draggable-post" ).draggable({
      helper:'clone',
      start: function( event, ui ) {
        $(ui.helper).addClass("dragging-post");
      }
    });

    // jquery droppable
    $( ".droppable-post" ).droppable({
      drop: function( event, ui ) { // drop event callback
        // get the data from the post
        var postId = $(ui.draggable).data("post-id")
        var title = $(ui.draggable).data("post-title")
        var img = $(ui.draggable).data("post-img")
        var largeId = $(ui.draggable).data("post-large")
        var mediumId = $(ui.draggable).data("post-medium")
        var smallId = $(ui.draggable).data("post-small")
        // the post item
        if(!postId  || !title){
          return;
        }
        var postView = {
          postId: postId,
          title: title,
          sponsor: null,
          largeId: largeId,
          mediumId: mediumId,
          smallId: smallId,
          img: img
        }
        var cell = {}
        var position = $(this).data("index")
        cell.index = position;
        cell.postView = postView

        safeApply($scope, function(){
          $scope.p.featuredPosts[position] = cell;
        });
      }
    });
  }// end of bindDragToPosts functions
})

.controller('UsersStationsCtrl', function UsersStationsCtrl($scope, $http, $filter, wordrailsService, authService) {

  var wr = authService.getWR()

  $scope.stations = angular.copy($scope.app.network.stations);
  var currentNetwork = $scope.app.network;

  /**
   * Maps the person's network role and station roles to a person object.
   * The result is a list of person and it's roles.
   * @param  {[NetworkRole]} nRoles [network role]
   * @param  {[StationRole]} sRoles [station role]
   */
  var personPermissions = function(nRoles, sRoles){
    $scope.personRoles = [];
    nRoles.forEach(function(nRole){
      var person = nRole.person;
      //console.log(person.username);
      if(person.id == 1 || person.username == "wordrails")
        return;

      person.networkRole = nRole;
      person.stationRoles = [];
      sRoles.forEach(function(sRole){
        if(person.id == sRole.person.id){
          person.stationRoles.push(sRole)
        }
      })
      $scope.personRoles.push(person)
    })
    //console.log($scope.personRoles);
  }

  // ----------- assync requests to map person and roles
  if($scope.app.network && $scope.app.network.stations){
    var nRoles = [] // list of person's network roles
    var sRoles = [] // list of person's station roles
    var requests = [] // list of request to execute sequentially
    
    var nRolesReq = wr.getNetworkPersonsNetworkRoles(currentNetwork.id, function(roles){
      nRoles = roles; // list of person's network roles
    }, null, null, "networkRoleProjection");

    requests.push(nRolesReq); // add network roles request to list

    $scope.app.network.stations.forEach(function(station){
      var sRolesReq = wr.getStationPersonsStationRoles(station.id, function(personRoles){
        safeApply($scope, function(){
          personRoles.forEach(function(role){
            sRoles.push(role)
          })
        })
      }, null, null, "stationRoleProjection").error(function(jqXHR){

      });

      requests.push(sRolesReq)
    })

    var requestMap = $.when.apply($, requests) // execute all request sequentially

    requestMap.always(null, null, function(){
      safeApply($scope, function(){
        personPermissions(nRoles, sRoles);
      })
    });

  }else{
    window.console && console.error("No network or stations")
  }
  // ----------- end of assync requests to map person and roles

  $scope.station = {name: '', id:0};

  $scope.filterStations = function(personRoles, station){
    if(personRoles){
      if(station.id == 0){
        return personRoles;
      }
      var ret = [];
      personRoles.forEach(function(personRole){
        if(personRole.stationRoles){
          personRole.stationRoles.forEach(function(sRoles){
            if(sRoles.station.id == station.id){
              ret.push(personRole);
              return;
            }
          })
        }
      })

      return ret;
    }
  }

  $scope.newPerson = function(){
    angular.forEach($scope.personRoles, function(personRole) {
      personRole.selected = false;
      personRole.editing = false;
    });
    $scope.personRole = {name: '', username: '', email: ''}
    $scope.personRole.editing = true;
  }

  $scope.updateRole = function(stationRole){
    if(stationRole && stationRole.id){
      var role = angular.copy(stationRole);
      delete role['content']
      role.person = "/api/persons/" + stationRole.person.id
      role.station = "/api/stations/" + stationRole.station.id
      if(role.id){
        wr.putStationRole(role, function(){
          toastr.success('Permissões atualizadas.')
        })
      }
    }
  }

  $scope.addStation = function(station){
    var add = true;
    if(!$scope.personRole.stationRoles){
      $scope.personRole.stationRoles = [];
    }

    $scope.personRole.stationRoles.forEach(function(stationRole){
      if(stationRole.station.id == station.id){
        add = false;
      }
    });

    if(add){

      var stationRole = {
        station: angular.copy(station),
        editor: false,
        writer: false,
        admin: false
      }

      $scope.personRole.stationRoles.push(stationRole)

      if($scope.personRole && $scope.personRole.id){
        var role = angular.copy(stationRole);
        console.log($scope.personRole);
        role.person = "/api/persons/" + $scope.personRole.id
        role.station = "/api/stations/" + station.id

        wr.postStationRole(role, function(stationRoleId){
          safeApply($scope, function(){
            stationRole.id = stationRoleId
            stationRole.person = $scope.personRole
          })
          toastr.success("O usuário foi adicionado a estação")
        });
      }
    }

  }

  $scope.removeFromStation = function(personRole, stationRole){
    var removeFromArray = function(){
      for (var i = personRole.stationRoles.length - 1; i >= 0; i--) {
        var sr = personRole.stationRoles[i]
        if(sr.station.id == stationRole.station.id){
          safeApply($scope, function(){
            personRole.stationRoles.splice(i, 1);
          })
        }
      };

      if(personRole.stationRoles.length == 0){
        personRole.stationRoles = null;
      }
    }
    if(personRole && !personRole.id && personRole.stationRoles){
      removeFromArray();
      return;
    }

    wr.deleteStationRole(stationRole.id, function(){
      removeFromArray();
      toastr.success("Usuário removido da estação.");
    });
  }

  $scope.selectStation = function(station){    
    angular.forEach($scope.stations, function(station) {
      station.selected = false;
    });
    $scope.station = station;
    $scope.station.selected = true;
  };

  $scope.selectPerson = function(personRole){    
    angular.forEach($scope.personRoles, function(personRole) {
      personRole.selected = false;
      personRole.editing = false;
    });
    $scope.personRole = personRole;
    $scope.personRole.selected = true;
  };

  $scope.deleteItem = function(item){
    $scope.items.splice($scope.items.indexOf(item), 1);
    $scope.item = $filter('orderBy')($scope.items, 'first')[0];
    if($scope.item) $scope.item.selected = true;
  };

  $scope.editItem = function(item){
    if(item && item.selected){
      item.editing = true;
    }
  };

  $scope.doneEditing = function(personRole){
    if(!personRole){
      window.console && console.error("personRole null")
      return
    }
    //console.log(personRole)

    if(!personRole.id){
      createPerson(personRole);
    }
  };

  $scope.cancel = function(){
    $scope.personRole = null;
  }

  function createPerson(personRole){
    var person = angular.copy(personRole)

    delete person['editing']

    var promise = wordrailsService.createPerson(person);

    $scope.app.loading = true;

    promise.then(function(personId){
      wr.getPerson(personId, function(personResponse){
        
        var networkRole = {
          network: extractSelf(wordrailsService.getNetwork()),
          person: extractSelf(personResponse),
          admin: false
        } 

        wr.postNetworkRole(networkRole, function(networkRoleId){
          wr.getNetworkRole(networkRoleId, function(networkRole){
              personRole = personResponse;
              personRole.networkRole
              toastr.success("O usuário receberá um email com a senha de cadastro da rede")
              $scope.personRoles.push(personRole);
              $scope.personRole.id = personRole.id;

            safeApply($scope, function(){
              $scope.personRole = personRole;
              personRole.editing = false;
            })
          }).error(function(){
            toastr.error("Houve um erro inesperado, entre em contato com a equipe de suporte.")
          }).complete(function(){
            safeApply($scope, function(){
              $scope.app.loading = false;
            })
          });
        }).error(function(){
          toastr.error("Houve um erro inesperado, entre em contato com a equipe de suporte.")
          safeApply($scope, function(){
            $scope.app.loading = false;
          })
        });
      }).error(function(){
        toastr.error("Houve um erro inesperado, entre em contato com a equipe de suporte.")
        safeApply($scope, function(){
          $scope.app.loading = false;
        })
      });
    }, function(jqXHR){
      safeApply($scope, function(){
        $scope.app.loading = false;
      })
      if(jqXHR.status == 409){
        toastr.error("Já existe um usuário cadastrado com esses dados.")
      }else if(jqXHR.status == 500){
        toastr.error("Houve um erro inesperado, entre em contato com a equipe de suporte.")
      }
    });
  }
})

.controller('ScaffoldSponsorCtrl', function ScaffoldSponsorCtrl($scope, $state, authService, FileUploader, WORDRAILS) {
    $scope.p = {}
    var wr = authService.getWR();

    var logoUploader = $scope.logoUploader = new FileUploader({
        url: WORDRAILS.baseUrl + "/api/files/contents/simple"
    });
    // FILTERS
    logoUploader.filters.push({
        name: 'customFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            return this.queue.length < 10;
        }
    });
    // CALLBACKS
    logoUploader.onAfterAddingFile = function(fileItem) {
        //console.info('onAfterAddingFile', fileItem);
        logoUploader.uploadAll();
    };
    logoUploader.onSuccessItem = function(fileItem, response, status, headers) {
        //console.info('onSuccessItem', fileItem, response, status, headers);
        $scope.p.image = response;
    };
    logoUploader.onErrorItem = function(fileItem, response, status, headers) {
        //console.info('onErrorItem', fileItem, response, status, headers);
        if(response.message && response.message.indexOf("height") > -1){
          toastr.error("A imagem não pode ter mais que 50 pixels de altura");
        }else if(response.message && response.message.indexOf("maximun") > -1){
          toastr.error("A imagem não pode ser maior que 100 Kb.");
        }
    };
    $scope.clearUpload = function(){ 
      logoUploader.clearQueue();
      $scope.p.image = null;
    } 

    var publicity = $scope.publicity = new FileUploader({
        url: WORDRAILS.baseUrl + "/api/files/contents/simple"
    });
    // FILTERS
    publicity.filters.push({
        name: 'customFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            return this.queue.length < 10;
        }
    });
    // CALLBACKS
    publicity.onAfterAddingFile = function(fileItem) {
        //console.info('onAfterAddingFile', fileItem);
        console.log('abas sd');
        publicity.uploadAll();
    };
    publicity.onSuccessItem = function(fileItem, response, status, headers) {
        //console.info('onSuccessItem', fileItem, response, status, headers);
        $scope.p.img = response;
    };
    publicity.onErrorItem = function(fileItem, response, status, headers) {
        //console.info('onErrorItem', fileItem, response, status, headers);
        if(response.message && response.message.indexOf("height") > -1){
          toastr.error("A imagem não pode ter mais que 50 pixels de altura");
        }else if(response.message && response.message.indexOf("maximun") > -1){
          toastr.error("A imagem não pode ser maior que 100 Kb.");
        }
    };

    $scope.submitSponsor = function(){
      var network = extractSelf($scope.app.network);

      if(!$scope.p.name){
        toastr.error("Nome inválido")
        return;
      }

      $scope.p.network = network;

      wr.postSponsor($scope.p, function(){
        toastr.error("Patrocinador criado com sucesso.")
        $state.go('app.scaffold.sponsors');
      })
    }
})