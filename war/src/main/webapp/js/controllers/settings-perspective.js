app.controller('SettingsPerspectiveEditorCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location){

		$scope.isMobile = true;
    $scope.isVertical = false;

  // scope variable used in the perspective editor dialog
    $scope.pe = {
      'searchedPosts': [],
    }
		$scope.pe.stationPerspective = null;

		$scope.thisStation = {}
    $scope.app.initData.stations.forEach(function(station, index){
      if($state.params.stationId == station.id){
        $scope.stationName = station.name;
        $scope.stationId = station.id;
        $scope.thisStation = $scope.pe.thisStation = station;
      }
    });

    trix.findPerspectiveView($state.params.perspectiveId, null, null, 0, 50).success(function(termPerspective){
      $scope.termPerspectiveView = $scope.buffedPerspective = termPerspective;
      $timeout(function() {
        $('.mockup-section').slimScroll({
         height:'476',
         size:'8px',
         'railVisible': true
         })
      })

      trix.getStationPerspective($state.params.perspectiveId, 'stationPerspectiveProjection').success(function(stationPerspective){
        $scope.pe.stationPerspective = stationPerspective;
      })

      //console.log($scope.termPerspectiveView);
      trix.findAllCategories($scope.thisStation.id).success(function(response){
        $scope.pe.taxonomies = response
        $scope.pe.taxonomies.forEach(function(tax, index){
          if($scope.termPerspectiveView.taxonomyId == tax.id){
            $scope.pe.terms = tax.terms;
            $scope.termPerspectiveView.ordinaryRows && $scope.termPerspectiveView.ordinaryRows.forEach(function(row, index){
              $scope.pe.terms && $scope.pe.terms.forEach(function(term, index){
                if(row.termId == term.id)
                  term.checked = true;
              });
            });
          }
        });
      })
    });

    $scope.removeFeaturedPost = function(postView){
      if($scope.termPerspectiveView.featuredRow && $scope.termPerspectiveView.featuredRow.cells){ 
        for (var i = $scope.termPerspectiveView.featuredRow.cells.length - 1; i >= 0; i--) {
          if($scope.termPerspectiveView.featuredRow.cells[i].postView.postId == postView.postId){
            $scope.termPerspectiveView.featuredRow.cells.splice(i, 1);
          }
        } // for
      } // if 1
    }// function

    $scope.log = function(data){
      console.log(data);
    }

    $scope.showAddFeaturedPost = function(ev){
    // show term alert
      $mdDialog.show({
        controller: DialogController,
        templateUrl: 'add_post.html',
        targetEvent: ev,
        onComplete: function(){}
      })
      .then(function(answer) {
      //$scope.alert = 'You said the information was "' + answer + '".';
      }, function() {
      //$scope.alert = 'You cancelled the dialog.';
      });

      $scope.cellType = 'FEATURED_POST';
    }

    $scope.showConfigCategory = function(ev){
      $mdDialog.show({
        controller: DialogController,
        templateUrl: 'config_category.html',
        targetEvent: ev,
        onComplete: function(){}
      })
      .then(function(answer) {
      //$scope.alert = 'You said the information was "' + answer + '".';
      }, function() {
      //$scope.alert = 'You cancelled the dialog.';
      });
    }

    $scope.pe.taxonomies = []
    $scope.pe.searchPage = 0;
    $scope.pe.nonemptySearch = $scope.pe.searchResults ? true : false;
    // -------------

    $scope.pe.addCell = function (postView){ // search related add cell to position
      if($scope.cellType == 'FEATURED_POST'){ // if is featured post
        if(!$scope.cellAddValidation(postView)){
          $scope.app.showErrorToast('Esta publicação já está em destaque')
          return false;
        }
        $scope.termPerspectiveView.featuredRow.cells.push({
          'postView': postView
        })
      } // end of if is featured post
    }

    $scope.pe.removeCell = function (postView){ // search related add cell to position
      if($scope.cellType == 'FEATURED_POST'){ // if is featured post
        $scope.removeFeaturedPost(postView)
      } // end of if is featured post
    }

    $scope.cellType = null;
    $scope.cellData = {rowIndex: null, cellIndex: null};


    $scope.cellAddValidation = function(postView){
      if(!$scope.termPerspectiveView.featuredRow || !$scope.termPerspectiveView.featuredRow.cells){
        $scope.termPerspectiveView.featuredRow = {
          cells: [],
          type: 'F',
          termId: null
        }
      }
      if($scope.cellType == 'FEATURED_POST' && $scope.termPerspectiveView.featuredRow.cells){
        var cells = $scope.termPerspectiveView.featuredRow.cells;
        for (var i = cells.length - 1; i >= 0; i--) {
          if(cells[i].postView && cells[i].postView.postId == postView.postId)
            return false;
        };

        // $scope.termPerspectiveView.featuredRow.cells.forEach(function(cell, index){
        //   if(cell.postView && cell.postView.postId == postView.postId)
        //     return false;
        // });
      }

      return true;
    }

    $scope.pe.cellAddValidation = function(postView){
      return $scope.cellAddValidation(postView);
    }

    // perspective editor dialog Controller
    function DialogController(scope, $mdDialog) {
      scope.app = $scope.app;
      scope.pe = $scope.pe;

      scope.hide = function() {
        $mdDialog.hide();
      };

      scope.cancel = function() {
        $mdDialog.cancel();
      };

      scope.publish = function() {
        if(isTermSelected($scope.termTree)){
          createPost($mdDialog)
        }else{
          // show alert term message
        }
        //$mdDialog.hide();
      };
      // check if user has permisstion to write
    };

    $scope.removeCategory = function(termId){
      var perspective = $scope.termPerspectiveView
      if(perspective.ordinaryRows){
        var rows = perspective.ordinaryRows
        for (var i = rows.length - 1; i >= 0; i--) {
          if(rows[i].termId == termId)
            rows.splice(i, 1)
        };
      }

      $scope.pe.terms && $scope.pe.terms.forEach(function(term, index){
        if(term.id == termId && term.checked)
          term.checked = false;
      });

      perspective.ordinaryRows && perspective.ordinaryRows.forEach(function(row, index){
        row.index = index;
      });
    }

    function addCategory(rows, term){
      var addedRow ={
        index: rows.length,
        termId: term.id,
        termName: term.name,
        termPerspectiveId: $scope.termPerspectiveView.id,
        type: 'O'
      }
      term.checked = true;
      trix.getRowView($state.params.perspectiveId, $scope.termPerspectiveView.id, term.id, 0, 10)
          .success(function(response){
            if(response.cells && response.cells.length > 0){
                   addedRow.cells = response.cells;
                   rows.push(addedRow)
            }
        }).error(function(){
          trix.findPostsByTagAndStationId(term.name, $scope.stationId, 0, 10).success(function(posts){
            addedRow.cells = []
            posts && posts.forEach(function(post, index){
               addedRow.cells.push({
                 'index': index,
                 'postView': post,
               });
            });
            rows.push(addedRow);
          })
      })
    }    

    $scope.$watch('pe.terms', function(newVal){
      console.log(newVal);
    },true)

    $scope.pe.checkCategory = function(term){
      var perspective = $scope.termPerspectiveView
      if(term.checked)
        addCategory(perspective.ordinaryRows, term);
      else
        $scope.removeCategory(term.id)
    }

//  -------------- end of perspective editor dialog controller

    // submit search
    $scope.pe.submitSearch = function(){
      if(!$scope.pe.searchQuery || $scope.pe.searchQuery.trim() == "")
        return null;

      $scope.pe.searchPage = 0;
      $scope.pe.allLoaded = false;
      trix.searchPostsFromStation($scope.stationId, $scope.pe.searchQuery, $scope.pe.searchPage, 10)
      .success(function(response){
        $scope.pe.searchHits = response.hits;
        $scope.pe.searchResults = response.posts;
        $(".search-results").focus();
      })
    }

  // ---------  end of submit search

  // paginate search
  $scope.pe.paginateSearch = function(){

    if($scope.app.getCurrentStateName() != 'app.search' || !$scope.pe.searchResults || $scope.pe.searchResults.length == 0)
      return;

    if($scope.pe.allLoaded)
      return;

    if(!$scope.searchLoading){

      $scope.searchLoading = true;
      trix.searchPostsFromStation($scope.stationId, $scope.pe.searchQuery, $scope.pe.searchPage + 1, 10)
      .success(function(response){

        $scope.searchLoading = false;
        $scope.pe.searchPage = $scope.pe.searchPage + 1

        if(!response.posts || response.posts.length == 0){
          $scope.pe.allLoaded = true;
          return;
        }

        response.posts && response.posts.forEach(function(element, index){
          $scope.pe.searchResults.push(element)
        }); 

        $(".search-results").focus();
      })
      .error(function(){
        $scope.searchLoading = false;
        $timeout(function() {
          cfpLoadingBar.complete(); 
        }, 100);
      })

    }
  }

      // ---------------- paginate search

  $scope.carouselPopoverOpen = false;

  $scope.$watch('carouselPopoverOpen', function(newVal){
    console.log(newVal);
  });

  $scope.webconfEditorActive = false;
  $scope.termEditorActive = false;
  $scope.adsEditorActive = false;

  $scope.activateTermsPanel = function(){
    $scope.termEditorActive = !$scope.termEditorActive;
    $scope.webconfEditorActive = false;
    $scope.adsEditorActive = false;
  }

  $scope.activateTabsPanel = function(){
    $scope.webconfEditorActive = !$scope.webconfEditorActive;
    $scope.termEditorActive = false;
    $scope.adsEditorActive = false;
  }

  $scope.$watch('termEditorActive', function(){
    console.log($scope.termEditorActive);
  })

  // ---------- hide popovers ------------------

  var hidePopover = function(element) {
    //Set the state of the popover in the scope to reflect this
    var elementScope = angular.element($(element)).scope()
      elementScope.$parent.isOpen = false;
      if(elementScope.$parent.$parent)
      for (var key in elementScope.$parent.$parent) {
        if (elementScope.$parent.$parent.hasOwnProperty(key) && key.indexOf('Open') > -1) {
          elementScope.$parent.$parent[key] = false;
        }
      }
      //elementScope.$parent.$parent.$apply();
      //Remove the popover element from the DOM
      $(element).remove();
  };

  $scope.pe.clickOutside = function(){
     $('*[popover-template-popup]').each(function() {
        //Only do this for all popovers other than the current one that cause this event,
        hidePopover(this);
    });
  } 
  // --------------- end of hide popovers ------------------

  $scope.updatePerspective = function(){
      
      if($scope.termPerspectiveView.featuredRow && $scope.termPerspectiveView.featuredRow.cells){
        $scope.termPerspectiveView.featuredRow.cells.forEach(function(cell, index){
          cell.index = index;
          cell.id = null;
        });
      }

      var perspective = angular.copy($scope.termPerspectiveView);

      perspective.ordinaryRows && perspective.ordinaryRows.forEach(function(row, index){
        row.index = index;
        for (var i = row.cells.length - 1; i >= 0; i--) {
          /*test*/
          // if(i == 0)
          //   row.cells[0].new = true;
          /*/test*/
          if(!(row.cells[i].id || row.cells[i].new))
            row.cells.splice(i, 1)
        };
      });

      if(!perspective.id){
        createTermPerspective(perspective)
        return;
      }

      trix.putTermView(perspective).success(function(){
        $scope.app.showSuccessToast('Perspectiva atualizada.')
      })
      .error(function(){
        $timeout(function() {
          cfpLoadingBar.complete(); 
        }, 100);
      });
  }

  var createTermPerspective = function(perspective){
    trix.postTermView(perspective).success(function(){
      trix.findPerspectiveView($state.params.perspectiveId, null, null, 0, 50).success(function(termPerspective){
        $scope.app.showSuccessToast('Perspectiva atualizada.')
        $scope.termPerspectiveView = termPerspective;
      });
    }).error(function(){
      $timeout(function() {
          cfpLoadingBar.complete(); 
        }, 100);
    })
  }


  }]);

 // ----------------------------------------------------------------

app.controller('SettingsPerspectiveListCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location',
  function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location){

    $scope.isMobile = true;

    $scope.stationPerspective = null;

    $scope.thisStation = {}
    $scope.app.initData.stations.forEach(function(station, index){
      if($state.params.stationId == station.id){
        $scope.stationName = station.name;
        $scope.stationId = station.id;
        $scope.thisStation = station;
      }
    });

    $scope.changeDefaultPerspective = function(perspective){
      $scope.perspectives.forEach(function(item, index){
        if(perspective.id == item.id)
          item.selected = true;
        else
          item.selected = false;
      });
    }

    trix.getStationStationPerspectives($scope.thisStation.id).success(function(response){
      $scope.perspectives = response.stationPerspectives;
      $scope.perspectives.forEach(function(perspective, index){
        if(perspective.id == $scope.thisStation.defaultPerspectiveId)
          perspective.selected = true;
      });

      console.log(response);
    })

    $scope.openEditPerspective = function(perspective){
      $state.go('app.settings.perspectiveeditor', {'stationId': $state.params.stationId, 'perspectiveId': perspective.id})
    }

    $scope.pe = {};
    $scope.pe.newPerspective = function(perspective){

      //trix.postStationPerspective
    }

    // perspective editor dialog Controller
    function DialogController(scope, $mdDialog) {
      scope.app = $scope.app;
      scope.pe = $scope.pe;
      scope.thisStation = $scope.thisStation;

      scope.hide = function() {
        $mdDialog.hide();
      };

      scope.cancel = function() {
        $mdDialog.cancel();
      };

      scope.publish = function() {
        if(isTermSelected($scope.termTree)){
          createPost($mdDialog)
        }else{
          // show alert term message
        }
        //$mdDialog.hide();
      };
      // check if user has permisstion to write
    };

    $scope.showAddPerspective = function(ev){
    // show term alert
      $mdDialog.show({
        controller: DialogController,
        templateUrl: 'add_perspective.html',
        targetEvent: ev,
        onComplete: function(){}
      })
      .then(function(answer) {
      //$scope.alert = 'You said the information was "' + answer + '".';
      }, function() {
      //$scope.alert = 'You cancelled the dialog.';
      });

      $scope.cellType = 'FEATURED_POST';
    }

  }]);