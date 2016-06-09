app.controller('SettingsCategoriesCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'FileUploader', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$filter', 'station',
                                          function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state ,  FileUploader ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $filter, station){

  $scope.editing = true;
  $scope.parentCategory = null;
  $scope.termTree = [];

  $scope.thisStation = station
  $scope.stationName = station.name;
  $scope.stationId = station.id;

  $scope.showAddCategoryDialog = function(parent, ev){
    $scope.parentCategory = parent;
    $mdDialog.show({
        scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
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

  $scope.showDeleteCategoryDialog = function(category, ev){
    $scope.app.toDeleteCategory = category;
    $scope.parentCategory = parent;
    $mdDialog.show({
        scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
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

  $scope.toUpdateCategory = null;
  $scope.showCategoryDescriptionDialog = function(category, ev){
    $scope.toUpdateCategory = category;
    $mdDialog.show({
        scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
        preserveScope: true, // do not forget this if use parent scope
        controller: $scope.app.defaultDialog,
        templateUrl: 'category_description_dialog.html',
        targetEvent: ev,
        onComplete: function(){}
      })
      .then(function(answer) {
      //$scope.alert = 'You said the information was "' + answer + '".';
      }, function() {
      //$scope.alert = 'You cancelled the dialog.';
    });
  }

  $scope.app.deleteCategory = function(){
    trix.deleteTerm($scope.app.toDeleteCategory.id).success(function(){
      $scope.app.showSuccessToast('Alterações realizadas com sucesso.')
      $mdDialog.cancel();
      trix.getTermTree(null, $scope.thisStation.categoriesTaxonomyId).success(function(response){
        $scope.termTree = response;
      });
    })
  }

  $scope.updateCategory = function(category){
    category.name = category.editingName ? category.editingName : category.name;
    category.taxonomy = TRIX.baseUrl + "/api/taxonomies/" + $scope.thisStation.categoriesTaxonomyId;
    var term = angular.copy(category);
    delete term['children']
    if(term.image && term.image.id)
      term.image = ImageDto.getSelf(term.image);

    trix.putTerm(term).success(function(){
      category.editing=false;
      updating = true;
      $scope.app.showSuccessToast('Alterações realizadas com sucesso.');
      $scope.app.cancelDialog();
      $scope.disabled = false;
    }).error(function(){
      updating = true;
      $timeout(function() {
        cfpLoadingBar.complete(); 
      }, 100);
      $scope.app.cancelDialog();
      $scope.disabled = false;
    });
  }

  var updating = false;
  $scope.changeColor = function(color, category) {
    updating = true;
    category.editingName = category.name;
    $scope.updateCategory(category)
  }

  $scope.app.addCategory = function(newCategoryName){
    if(!newCategoryName || newCategoryName.trim() == ""){
      $scope.app.showErrorToast("Categoria Inválida");
      $scope.app.cancelDialog();
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

  $scope.getCategoryImage = function(category){
    return $filter('categoryImage')(category,'medium',true);
  }

  // ------------------- image uploader -------------

  var uploader = $scope.uploader = new FileUploader({
    url: TRIX.baseUrl + "/api/images/upload?imageType=POST"
  });

  $scope.uploadedImage = null;
  uploader.onAfterAddingFile = function(fileItem) {
    $scope.uploadedImage = null;
    uploader.uploadAll();
  };

  uploader.onSuccessItem = function(fileItem, response, status, headers) {
    if(response.filelink){
      $scope.uploadImageCategory.image = ImageDto.getSelf(response);
      $scope.updateCategory($scope.uploadImageCategory);
      $scope.uploadImageCategory.imageHash = response.hash;
    }
  };

  uploader.onErrorItem = function(fileItem, response, status, headers) {
    if(status == 413)
      $scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
    else
      $scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
  }

  $scope.clearImage = function(){ 
    $scope.uploadedImage = null;
    uploader.clearQueue();
    uploader.cancelAll()
    $scope.checkLandscape();
    $scope.postCtrl.imageHasChanged = true;
  }

  uploader.onProgressItem = function(fileItem, progress) {
    cfpLoadingBar.start();
    cfpLoadingBar.set(progress/100)
    if(progress == 100){
      cfpLoadingBar.complete()
      toastPromise = $mdToast.show(
        $mdToast.simple()
        .content('Processando...')
        .position('top right')
        .hideDelay(false)
        );
    }
  };

  $scope.postFeaturedImage = null
  var setPostFeaturedImage = function(hash){
    $scope.postFeaturedImage = $filter('imageLink')({imageHash: hash}, 'large')
  }


  // ------------------- end of image uploader -------------
  
  $scope.uploadImageCategory = null;
  $scope.setUploadImageCategory = function(category){
    $scope.uploadImageCategory = category;
  }

}])
