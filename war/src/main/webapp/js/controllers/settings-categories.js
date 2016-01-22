app.controller('SettingsCategoriesCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', 'trix', 'TRIX', 'cfpLoadingBar',
	function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state, trix, TRIX, cfpLoadingBar){
		$scope.app.lastSettingState = "app.settings.categories";
		$scope.editing = true;

		trix.findNetworkCategories($scope.app.initData.network.id).success(function(response){
			$scope.taxonomyId = response.taxonomies[0].id;
			trix.getTermTree(null, $scope.taxonomyId).success(function(response){
				$scope.termTree = response;
			});
		})


		$scope.showAddCategorySplash = function(parent, ev){
			$scope.parentCategory = parent;
	    $mdDialog.show({
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
		$scope.showDeleteCategorySplash = function(category, ev){
			$scope.app.toDeleteCategory = category;
			$scope.parentCategory = parent;
	    $mdDialog.show({
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

		$scope.app.deleteCategory = function(){
			trix.deleteTerm($scope.app.toDeleteCategory.id).success(function(){
				$scope.app.showSuccessToast('Alterações realizadas com successo.')
				$mdDialog.cancel();
				trix.getTermTree(null, $scope.taxonomyId).success(function(response){
					$scope.termTree = response;
				});
			})
		}

		$scope.updateCategory = function(node){
			node.name = node.editingName;
			node.taxonomy = TRIX.baseUrl + "/api/taxonomies/" + $scope.taxonomyId;
			var term = angular.copy(node);
			console.log(term);
			delete term['children']
			trix.putTerm(term).success(function(){
				node.editing=false;
				$scope.app.showSuccessToast('Alterações realizadas com successo.');
			}).error(function(){
				$timeout(function() {
					cfpLoadingBar.complete(); 
				}, 100);
			});
		}

		$scope.app.addCategory = function(newCategoryName){
			if(!newCategoryName || newCategoryName.trim() == ""){
				$scope.app.showErrorToast("Categoria Inválida");
				$scope.app.cancelModal();
				return;
			}

			var term = {
				name: newCategoryName,
				taxonomy: TRIX.baseUrl + "/api/taxonomies/" + $scope.taxonomyId
			}

			if($scope.parentCategory)
				term.parent = TRIX.baseUrl + "/api/terms/" + $scope.parentCategory.id

			trix.postTerm(term).success(function(){
				$mdDialog.cancel();
				trix.getTermTree(null, $scope.taxonomyId).success(function(response){
					$scope.app.showSuccessToast('Categoria criada com successo.')
					$scope.termTree = response;
				});
			}).error(function(data, status){
        $mdDialog.cancel();
        $scope.app.showErrorToast('Esta categoria já existe')
      });;
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

}])
