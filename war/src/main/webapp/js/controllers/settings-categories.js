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


		$scope.showAddCategorySplash = function(parent){
			$scope.parentCategory = parent;
			$scope.app.openSplash('add_category.html')
		}

		$scope.app.toDeleteCategory = null;
		$scope.showDeleteCategorySplash = function(category){
			$scope.app.toDeleteCategory = category;
			$scope.app.openSplash('delete_category.html')
		}

		$scope.app.deleteCategory = function(){
			trix.deleteTerm($scope.app.toDeleteCategory.id).success(function(){
				$scope.app.showSuccessToast('Alterações realizadas com successo.')
				$scope.app.cancelModal();
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
				$scope.app.cancelModal();
				trix.getTermTree(null, $scope.taxonomyId).success(function(response){
					$scope.termTree = response;
				});
			});
		}

	}])