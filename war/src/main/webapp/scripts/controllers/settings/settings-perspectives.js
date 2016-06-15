app.controller('SettingsPerspectivesCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdDialog', '$mdToast', '$filter', '$translate', '$mdConstant', '$mdSidenav', 'station',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix, FileUploader, TRIX, cfpLoadingBar, $mdDialog, $mdToast, $filter, $translate, $mdConstant, $mdSidenav, station ){

    // ---- init

	$scope.togglePerspectives = buildToggler('perspective-list');

	$scope.thisStation = station;

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
        $scope.stationPerspectives && $scope.stationPerspectives.forEach(function(perspective, index){
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
    }).error(function(){
      $mdDialog.cancel();
    })
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
    $scope.perspectiveToActigate = perspective;
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
  $scope.postToPin = null;
  $scope.showPinPostDialog = function(event, index, post){
    $scope.resetPostSearch();
    $scope.postToPin = post;
    $scope.disabled = false;
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

  // -------- /dialogs

  // ------ perspective operations
    // $scope.editingFeaturedRow
    $scope.perspectiveChanged = false;

    $scope.applyFeaturedPosts = function(){
      $mdDialog.cancel();
      $scope.perspectiveChanged = true;
      $scope.currentPerspective.termPerspectiveView.featuredRow = $scope.editingFeaturedRow;
      reloadCarousel();
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

    $scope.removeFeaturedPost = function(post){
      if($scope.editingFeaturedRow && $scope.editingFeaturedRow.cells && $scope.editingFeaturedRow.cells.length > 0){
        var cells = $scope.editingFeaturedRow.cells;
        for (var i = cells.length - 1; i >= 0; i--) {
          if(cells[i].postView && cells[i].postView.id === post.id)
            cells.splice(i, 1);
        }
      }
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

      trix.searchPosts($scope.searchQuery, null, null, 'published', null, null, null, null, $scope.postSearchCtrl.page, 20, '-date', ['body', 'tags', 'categories', 'imageHash', 'state'], false).success(function(response,a,b,c){
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
      posts.reverse();

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

settingsPerspectivesCtrl = $scope;

}]);

var settingsPerspectivesCtrl = null;