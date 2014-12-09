'use strict';

angular.module('app')
.controller('AppPerspectiveCtrl', function AppPerspectiveCtrl($scope, $state, authService, WORDRAILS){
  var termId = $scope.termId = $state.params.termId > 0 ? $state.params.termId : 0;
  var stationPerspectiveId = $state.params.perspectiveId;
  var wr = authService.getWR();
  $scope.app.loading = true;

  $scope.$watch('app.network.currentStation.perspective', function(newVal){
    if(newVal){
      loadTermPerspectiveView();
    }
  });

  function loadTermPerspectiveView(){
    var data = {
      stationPerspectiveId: stationPerspectiveId,
      page: 0,
      size: 15
    }
    /*if(termPerspectiveId){
      data.termPerspectiveId = termPerspectiveId
    }*/
    if(termId){
      data.termId = termId
    }
    wr._ajax({
      url: WORDRAILS.baseUrl + "/api/perspectives/termPerspectiveViews",
      data: data,
      success: function(response){
        safeApply($scope, function(){
          if(response.ordinaryRows){

            response.ordinaryRows.sort(ordinaryRowCompare);
            response.ordinaryRows.forEach(function(or){
              or.page = 0;
            })
          }

          $scope.app.network.currentStation.currentTermPerspectiveView = response;
          $scope.termPerspectiveView = $scope.app.network.currentStation.currentTermPerspectiveView;// alias
        })
      },
      complete: function(_jqXHR, status){
        safeApply($scope, function(){
          $scope.app.loading = false;
        })
      }
    });
  }// end of load termPerspectiveView function
  
  // -------------------------------------------------------

  if(termId){
    $scope.breadcrumbs = []
    
    var breadcrumbsRecursive = function(term){
      wr.getTermParent(term.id, function(parent){
        safeApply($scope, function(){
          $scope.breadcrumbs.push(parent)
        });
        breadcrumbsRecursive(parent)
      })
    }

    wr.getTerm(termId, function(term){
      safeApply($scope, function(){
        $scope.breadcrumbs.push(term)
      })
      breadcrumbsRecursive(term)
    });
  }

  if(app.network && app.network.currentStation && app.network.currentStation.perspective){
    loadTermPerspectiveView();
  }

  $scope.$watch('termPerspectiveView', function(newVal){
    if(newVal && newVal.ordinaryRows){
      newVal.ordinaryRows.forEach(function(row){
        wr.getTerm(row.termId, function(response){
          safeApply($scope, function(){
            row.term = response;
          })
        }, function(){}, function(){})

      });
    }
  });

  var termLoading = [];

  $scope.updateRowPage = function(updateTermId, calllback){
    
    var tv = $scope.termPerspectiveView
    var termRow = null
    tv.ordinaryRows.forEach(function(row){
      if(row.termId == updateTermId){
        termRow = row;
      }
    });

    if(termRow.cells && termRow.cells.length <= WORDRAILS.pageSize - 1){ // chech if there's a need to paginate
      return;
    }

    if(!termLoading[updateTermId]){

      termLoading[updateTermId] = termLoading
      loadTermPerspectivePage(termRow.termId, termRow.page + 1, function(row){
        if(row.cells){
          safeApply($scope, function(){
            row.cells.forEach(function(cell){
              termRow.cells.push(cell)
            });
            termRow.page++;
          });

          setTimeout(function() {
            if(calllback){
              calllback();
            }
          }, 200); // timeout wai for js to add elements
        }
        termLoading.splice(updateTermId);
      });
    }
  }

  function loadTermPerspectivePage(updateTermId, page, calllback){
    safeApply($scope, function(){
      $scope.app.loading = true;
    })

    var data = {
      stationPerspectiveId: stationPerspectiveId,
      page: page,
      size: WORDRAILS.pageSize, // defined in app.js
    }

    if($scope.termPerspectiveView && $scope.termPerspectiveView.id){
      data.termPerspectiveId = $scope.termPerspectiveView.id
    }

    if(updateTermId){
      data.childTermId = updateTermId
    }

    wr.getRowView(data, function(response){
      safeApply($scope, function(){
        calllback && calllback(response);
      });
    }).complete(function(){
      safeApply($scope, function(){
        $scope.app.loading = false;
      })
    })
  }

})