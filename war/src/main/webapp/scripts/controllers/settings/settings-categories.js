app.controller('SettingsCategoriesCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location',
                                          function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location){

  $scope.editing = true;
  $scope.parentCategory = null;
  $scope.termTree = [];

  $scope.thisStation = {}
  $scope.app.stations.forEach(function(station, index){
    if($state.params.slug == station.stationSlug){
      $scope.stationName = station.name;
      $scope.stationId = station.id;
      $scope.thisStation = station;
    }
  });

  $scope.showAddCategorySplash = function(parent, ev){
    $scope.parentCategory = parent;
    $mdDialog.show({
        scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
        preserveScope: true, // do not forget this if use parent scope
        controller: DialogController,
        templateUrl: 'add_category.html',
        targetEvent: ev,
        onComplete: function(){}
      })
      .then(function(answer) {
      //$scope.alert = 'You said the information was "' + answer + '".';
      }, function() {
      //$scope.alert = 'You cancelled the dialog.';
    });
  }

  $scope.app.toDeleteCategory = null;
  $scope.showDeleteCategoryDialog = function(category, ev){
    $scope.app.toDeleteCategory = category;
    $scope.parentCategory = parent;
    $mdDialog.show({
        scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
        preserveScope: true, // do not forget this if use parent scope
        controller: DialogController,
        templateUrl: 'delete_category.html',
        targetEvent: ev,
        onComplete: function(){}
      })
      .then(function(answer) {
      //$scope.alert = 'You said the information was "' + answer + '".';
      }, function() {
      //$scope.alert = 'You cancelled the dialog.';
    });
  }

  function DialogController(scope, $mdDialog) {
    scope.app = $scope.app;
    scope.pe = $scope.pe;

    scope.hide = function() {
      $mdDialog.hide();
    };

    scope.cancel = function() {
      $mdDialog.cancel();
    };

    // check if user has permisstion to write
  };

  $scope.app.deleteCategory = function(){
    trix.deleteTerm($scope.app.toDeleteCategory.id).success(function(){
      $scope.app.showSuccessToast('Alterações realizadas com sucesso.')
      $mdDialog.cancel();
      trix.getTermTree(null, $scope.thisStation.categoriesTaxonomyId).success(function(response){
        $scope.termTree = response;
      });
    })
  }

  $scope.updateCategory = function(node){
    node.name = node.editingName;
    node.taxonomy = TRIX.baseUrl + "/api/taxonomies/" + $scope.thisStation.categoriesTaxonomyId;
    var term = angular.copy(node);
    delete term['children']
    if(term.image && term.image.id)
      term.image = ImageDto.getSelf(term.image);

    trix.putTerm(term).success(function(){
      node.editing=false;
      updating = true;
      $scope.app.showSuccessToast('Alterações realizadas com sucesso.');
    }).error(function(){
      updating = true;
      $timeout(function() {
        cfpLoadingBar.complete(); 
      }, 100);
    });
  }

  var updating = false;
  $scope.changeColor = function(color, node) {
    updating = true;
    node.editingName = node.name;
    $scope.updateCategory(node)
  }

  $scope.getColorButtonStyle = function(category){
    if(category && category.color)
      return {'background-color': category.color, color: tinycolor(category.color).isLight() ? 'rgba(0,0,0,0.9)' : 'rgba(255,255,255,0.9)' }
    return null;
  }

  $scope.app.addCategory = function(newCategoryName){
    if(!newCategoryName || newCategoryName.trim() == ""){
      $scope.app.showErrorToast("Categoria Inválida");
      $scope.app.cancelModal();
      return;
    }

    var term = {
      name: newCategoryName,
      taxonomy: TRIX.baseUrl + "/api/taxonomies/" + $scope.thisStation.categoriesTaxonomyId
    }

    if($scope.parentCategory)
      term.parent = TRIX.baseUrl + "/api/terms/" + $scope.parentCategory.id

    trix.postTerm(term).success(function(){
      $mdDialog.cancel();
      trix.getTermTree(null, $scope.thisStation.categoriesTaxonomyId).success(function(response){
        $scope.app.showSuccessToast('Categoria criada com sucesso.')
        $scope.termTree = response;
      })
    }).error(function(data, status){
      $mdDialog.cancel();
      $scope.app.showErrorToast('Esta categoria já existe')
    });;
  }

  trix.getTermTree(null, $scope.thisStation.categoriesTaxonomyId).success(function(response){
    $scope.termTree = response;
  });

}])
