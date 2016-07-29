app.controller('SettingsPerspectivesCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdDialog', '$mdToast', '$filter', '$translate', '$mdConstant', '$mdSidenav', 'station',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix, FileUploader, TRIX, cfpLoadingBar, $mdDialog, $mdToast, $filter, $translate, $mdConstant, $mdSidenav, station ){

    // ---- init

	$scope.togglePerspectives = buildToggler('perspective-list');

	$scope.thisStation = angular.copy(station);

		function buildToggler(navID) {
	    return function() {
	      $mdSidenav(navID)
	        .toggle()
	    }
  }

  var addSnippet = function(postObj){
    if(postObj.body)
      postObj.snippet = postObj.body.simpleSnippet();
  }

  $scope.getSnippet = function(body){
    if(body)
      return body.simpleSnippet();
  }

  $scope.getCategory = function(id){
    var ret = null;
    $scope.thisStation.categories && $scope.thisStation.categories.forEach(function(category){
      if(id === category.id)
        ret = category;
    })
    return ret;
  }

  var loadPerspectives = function(){
    trix.getStationStationPerspectives($scope.thisStation.id).success(function(response){
      $scope.stationPerspectives = response.stationPerspectives;
        $scope.stationPerspectives && $scope.stationPerspectives.forEach(function(perspective){
          if(perspective.id == $scope.thisStation.defaultPerspectiveId){
            perspective.selected = true;
            $scope.currentPerspective = perspective;
          }
        getPerspectiveView(perspective)
      });
    })
  }

  loadPerspectives();

  var getPerspectiveView = function(perspective){
    trix.findPerspectiveView(perspective.id, null, null, 0, 10).success(function(termPerspective){
      perspective.termPerspectiveView = termPerspective;
      perspective.termPerspectiveView.ordinaryRows && perspective.termPerspectiveView.ordinaryRows.forEach(function(row){
        row.category = $scope.getCategory(row.termId);
      })
      loadCategories(perspective.termPerspectiveView);
    }).error(function(){
      $mdDialog.cancel();
    })
  }

  var loadCategories = function(termPerspectiveView){
    termPerspectiveView.allCategories = angular.copy($scope.thisStation.categories);
    termPerspectiveView.ordinaryRows && termPerspectiveView.ordinaryRows.forEach(function(row, index){
      termPerspectiveView.allCategories && termPerspectiveView.allCategories.forEach(function(category, index){
        if(row.termId == category.id)
          category.checked = true;
      });
    });
  }

  // ------ init

  // ------ dialogs

  $scope.showAddPerspectiveDialog = function(event){
    $scope.disabled = false;
    $scope.perspectiveName = null;
    $mdDialog.show({
      scope: $scope,        // use parent scope in template
      closeTo: {
        bottom: 1500
      },
      preserveScope: true, // do not forget this if use parent scope
      controller: $scope.app.defaultDialog,
      templateUrl: 'add-perspective-dialog.html',
      parent: angular.element(document.body),
      targetEvent: event,
      clickOutsideToClose:true
      // onComplete: function(){
        // }
    })
  }

   $scope.showActivatePerspectiveDialog = function(event, perspective){
    $scope.perspectiveToActivate = perspective;
    $scope.disabled = false;
    $mdDialog.show({
      scope: $scope,        // use parent scope in template
      closeTo: {
        bottom: 1500
      },
      preserveScope: true, // do not forget this if use parent scope
      controller: $scope.app.defaultDialog,
      templateUrl: 'activate-perspective-dialog.html',
      parent: angular.element(document.body),
      targetEvent: event,
      clickOutsideToClose:true
      // onComplete: function(){
        // }
    })
  }

  $scope.activatePerspective = function(){
    trix.getStation($scope.thisStation.id).success(function(stationResponse){
      $scope.currentPerspective = $scope.perspectiveToActivate;
      $scope.thisStation.defaultPerspectiveId = stationResponse.defaultPerspectiveId = $scope.currentPerspective.id;
      trix.putStation(stationResponse).success(function(){
        $mdDialog.cancel();
      })
    })
  }

   $scope.showDeletePerspectiveDialog = function(event, perspective){
    $scope.perspectiveToDelete = perspective;
    $scope.disabled = false;
    $mdDialog.show({
      scope: $scope,        // use parent scope in template
      closeTo: {
        bottom: 1500
      },
      preserveScope: true, // do not forget this if use parent scope
      controller: $scope.app.defaultDialog,
      templateUrl: 'delete-perspective-dialog.html',
      parent: angular.element(document.body),
      targetEvent: event,
      clickOutsideToClose:true
      // onComplete: function(){
        // }
    })
  }

  $scope.editingFeaturedRow = null;

  $scope.showFeaturedPostsDialog = function(event){
    $scope.disabled = false;
    $scope.resetPostSearch();
    var fRow = $scope.currentPerspective.termPerspectiveView.featuredRow;
    if(fRow && fRow.cells.length > 0)
      $scope.editingFeaturedRow = fRow ? angular.copy(fRow) : null;
    else
      $scope.editingFeaturedRow = {cells: []}
    
    $mdDialog.show({
      scope: $scope,        // use parent scope in template
      closeTo: {
        bottom: 1500
      },
      preserveScope: true, // do not forget this if use parent scope
      controller: $scope.app.defaultDialog,
      templateUrl: 'featured-post-dialog.html',
      parent: angular.element(document.body),
      targetEvent: event,
      clickOutsideToClose:true
      // onComplete: function(){
        // }
    })
  }

  $scope.postToPinIndex = null;
  $scope.cellToPin = null;
  $scope.showPinPostDialog = function(event, index, cellToPin){
    $scope.resetPostSearch();
    $scope.cellToPin = cellToPin;
    $scope.postToPinIndex = index;
    $scope.disabled = false;
    $mdDialog.show({
      scope: $scope,        // use parent scope in template
      closeTo: {
        bottom: 1500
      },
      preserveScope: true, // do not forget this if use parent scope
      controller: $scope.app.defaultDialog,
      templateUrl: 'pin-post-dialog.html',
      parent: angular.element(document.body),
      targetEvent: event,
      clickOutsideToClose:true
      // onComplete: function(){
        // }
    })
  }

  $scope.showCategoriesConfigDialog = function(event){
    $scope.disabled = false;
    $mdDialog.show({
      scope: $scope,        // use parent scope in template
      closeTo: {
        bottom: 1500
      },
      preserveScope: true, // do not forget this if use parent scope
      controller: $scope.app.defaultDialog,
      templateUrl: 'categories-config-dialog.html',
      parent: angular.element(document.body),
      targetEvent: event,
      clickOutsideToClose:true
      // onComplete: function(){
        // }
    })
  }

  $scope.showRemovePinDialog = function(homeIndex){
    $scope.toRemoveIndex = homeIndex;
    $scope.disabled = false;
    $mdDialog.show({
      scope: $scope,        // use parent scope in template
      closeTo: {
        bottom: 1500
      },
      preserveScope: true, // do not forget this if use parent scope
      controller: $scope.app.defaultDialog,
      templateUrl: 'remove-pin-dialog.html',
      parent: angular.element(document.body),
      targetEvent: event,
      clickOutsideToClose:true
      // onComplete: function(){
        // }
    })
  }
  // -------- /dialogs

  // ------ perspective operations

  $scope.removePinnedPublication = function(){
    var cells = $scope.currentPerspective.termPerspectiveView.homeRow.cells
    for (var i = 0; i < cells.length ; i++) {
      if(i == $scope.toRemoveIndex)
        $scope.currentPerspective.termPerspectiveView.homeRow.cells.splice(i,1);
    }
    $scope.toRemoveIndex = null;
    $scope.perspectiveChanged = true;
    $mdDialog.cancel();
  }

  $scope.addPerspective = function(perspectiveName){
    var stationPerspective = {};
    stationPerspective.name = perspectiveName;
    stationPerspective.station = TRIX.baseUrl + "/api/stations/" + $scope.thisStation.id;
    stationPerspective.taxonomy = TRIX.baseUrl + "/api/stations/" + $scope.thisStation.categoriesTaxonomyId;

    trix.postStationPerspective(stationPerspective).success(function(response){
      $scope.stationPerspectives.push(response)
      getPerspectiveView(response);
      $mdDialog.cancel();
      $scope.app.showSuccessToast('Perspectiva criada com sucesso.')
    }).error(function(response){
      $scope.app.showErrorToast('Erro ao criar perspectiva.')
      $mdDialog.cancel();
    })
  }

  $scope.deletePerspective = function(perspective){
    trix.deleteStationPerspective(perspective.id).success(function(){
      for (var i = $scope.stationPerspectives.length - 1; i >= 0; i--) {
        if($scope.stationPerspectives[i].id === perspective.id)
          $scope.stationPerspectives.splice(i, 1)
      }
      $mdDialog.cancel();
    }).error(function(){
      $mdDialog.cancel();
    })
  }

  $scope.setCurrentPerspective = function(perspective){
    $scope.currentPerspective = perspective
    reloadCarousel();
  }

    // $scope.editingFeaturedRow
    $scope.perspectiveChanged = false;

    $scope.applyFeaturedPosts = function(){
      $mdDialog.cancel();
      $scope.perspectiveChanged = true;
      $scope.currentPerspective.termPerspectiveView.featuredRow = $scope.editingFeaturedRow;
      reloadCarousel();
    }

    $scope.applyCategories = function(){
      $mdDialog.cancel();
      $scope.perspectiveChanged = true;
      // $scope.currentPerspective.termPerspectiveView.featuredRow = $scope.editingFeaturedRow;
      // reloadCarousel(); 
    }

    var reloadCarousel = function(){
      $scope.reloadingCarousel = true;
      $timeout(function(){
        $scope.reloadingCarousel = false;
      },100)
    }

    $scope.addFeaturedPosts = function(post){
      if(!$scope.app.hasImage(post)){
        $scope.app.showErrorToast('Apenas publicações que contém imagens podem ser adicionadas aos destaques');
        return;
      }

      $scope.editingFeaturedRow.cells.push({postView: post})
    }

    $scope.addPinPost = function(postView){
      $scope.cellToPin.postView = postView;
      $scope.cellToPin.fixed = true;
      $timeout(function(){
        $scope.reloadMasonry();
      },1000);
      $mdDialog.cancel();
      $scope.perspectiveChanged = true;
    }

    $scope.removeFeaturedPost = function(post){
      if($scope.editingFeaturedRow && $scope.editingFeaturedRow.cells && $scope.editingFeaturedRow.cells.length > 0){
        var cells = $scope.editingFeaturedRow.cells;
        for (var i = cells.length - 1; i >= 0; i--) {
          if(cells[i].postView && cells[i].postView.id === post.id)
            cells.splice(i, 1);
        }
      }
    }

    $scope.updatePerspecitve = function(){
      var perspective = angular.copy($scope.currentPerspective.termPerspectiveView);

      if(perspective.featuredRow && perspective.featuredRow.cells){

        if(!perspective.featuredRow.termPerspectiveId){
          perspective.featuredRow = {
            termId: null,
            termName: null,
            termPerspectiveId: $scope.currentPerspective.termPerspectiveView.id,
            type: 'F',
            cells: perspective.featuredRow.cells
          }
        }

        row = perspective.featuredRow
        for (var i = 0; i < row.cells.length; i++) {
          row.cells[i].postView.postId = row.cells[i].postView.id;
          row.cells[i].index = i;
        };
      }

      perspective.ordinaryRows && perspective.ordinaryRows.forEach(function(row, index){
        row.index = index;
        for (var i = row.cells.length - 1; i >= 0; i--) {
          if(!(row.cells[i].id || row.cells[i].new))
            row.cells.splice(i, 1)
        };
      });

      if(perspective.homeRow && perspective.homeRow.cells){
        row = perspective.homeRow
        for (var i = row.cells.length - 1; i >= 0; i--) {
          row.cells[i].postView.postId = row.cells[i].postView.id;
          if(!(row.cells[i].id || row.cells[i].fixed))
            row.cells.splice(i, 1)
        };
      }

      trix.putTermView(perspective).success(function(){
        $scope.app.showSuccessToast('Perspectiva atualizada.')
      })
    }

  // ------ /perspective operations

  // -------- search post ---------
  $scope.postSearchCtrl = {
    'page': 0,
    'allLoaded': false,
    'window': 20
  }

  $scope.resetPostSearch = function(){
    $scope.postSearchCtrl.page = 0;
    $scope.postSearchCtrl.allLoaded = false;
    $scope.postSearchResults = [];
  }

  $scope.paginateSearch = function(){
    if(!$scope.loadingSearch && !$scope.postSearchCtrl.allLoaded){
      $scope.loadingSearch = true;

      trix.searchPosts($scope.searchQuery, null, [$scope.thisStation.id], 'published', null, null, null, null, $scope.postSearchCtrl.page, 20, '-date', ['body', 'tags', 'categories', 'imageHash', 'state'], false).success(function(response,a,b,c){
        handleSuccess(response);
        $scope.searchTotalElements = c.totalElements;
        $scope.loadingSearch = false;
      }).error(function(){
        $scope.loadingSearch = false;
        $scope.postSearchCtrl.allLoaded = true;
      })
    }
  }

  var handleSuccess = function(posts, a,b){
    if(posts && posts.length > 0){
        if(!$scope.postSearchResults)
          $scope.postSearchResults = []

        posts.forEach(function(post){
          addSnippet(post);
          $scope.postSearchResults.push(post);
        })
        $scope.postSearchCtrl.page++;
        // if(posts.length < $scope.postSearchCtrl.window)
        //   $scope.postSearchCtrl.allLoaded = true;
    }else
      $scope.postSearchCtrl.allLoaded = true;
  }


  $scope.doSearchPosts = function(){
    $scope.resetPostSearch();
    $scope.paginateSearch();
  }
  // -------- /search post --------
  // -------- category definition ---------
  
   function addCategory(rows, term){
      var addedRow ={
        index: rows.length,
        termId: term.id,
        termName: term.name,
        category: {name: term.name, id: term.id},
        termPerspectiveId: $scope.currentPerspective.termPerspectiveView.id,
        type: 'O'
      }
      term.checked = true;
      trix.getRowView($scope.currentPerspective.id, $scope.currentPerspective.termPerspectiveView.id, term.id, 0, 10)
          .success(function(response){
            if(response.cells && response.cells.length > 0){
                   addedRow.cells = response.cells;
                   rows.push(addedRow)
            }else{
              rowViewNotFound(rows, term, addedRow);
            }
        }).error(function(){
          rowViewNotFound(rows, term, addedRow) 
        })
    }

    var rowViewNotFound = function(rows, term, addedRow){
      trix.findPostsByTerm(term.id, 0, 10).success(function(posts){
        addedRow.cells = []
        posts && posts.forEach(function(post, index){
           addedRow.cells.push({
             'index': index,
             'postView': post,
           });
        });
        rows.push(addedRow);
      })
    }

    $scope.removeCategory = function(termId){
      var perspective = $scope.currentPerspective.termPerspectiveView
      if(perspective.ordinaryRows){
        var rows = perspective.ordinaryRows
        for (var i = rows.length - 1; i >= 0; i--) {
          if(rows[i].termId == termId)
            rows.splice(i, 1)
        };
      }

      $scope.currentPerspective.termPerspectiveView.allCategories && 
      $scope.currentPerspective.termPerspectiveView.allCategories.forEach(function(category, index){
        if(category.id == termId && category.checked)
          category.checked = false;
      });

      perspective.ordinaryRows && perspective.ordinaryRows.forEach(function(row, index){
        row.index = index;
      });
    }

    $scope.checkCategory = function(term){
      var perspective = $scope.currentPerspective.termPerspectiveView
      if(term.checked)
        addCategory(perspective.ordinaryRows, term);
      else
        $scope.removeCategory(term.id)
    }

  // -------- /category deifinition --------

settingsPerspectivesCtrl = $scope;

}]);

var settingsPerspectivesCtrl = null;