app.controller('ReadCtrl', ['$scope', '$log', '$timeout', '$rootScope', '$state', 'trix', '$mdToast',
	function($scope, $log, $timeout, $rootScope, $state, trix, $mdToast) {

	var slug = $state.params.slug;

	if(slug){
		trix.findBySlug(slug, 'postProjection').success(function(response){
			console.log(response);
			if(response && response.posts){
				$scope.app.nowReading = response.posts[0]
				$scope.app.nowReading.postId = $scope.app.nowReading.id;
				$scope.app.nowReadingAuthor = {
		            authorId: $scope.app.nowReading.author.id,
		            imageSmallId: $scope.app.nowReading.author.imageSmallId,
		            coverMediumId: $scope.app.nowReading.author.coverMediumId,
		            authorName: $scope.app.nowReading.author.name
		        }
		        console.log(response.posts[0]);
			}
			$("title").html($scope.app.nowReading.title);
			//console.log($scope.app.nowReading);
		})// end of success
	}else{
		// TODO error
	}

	$scope.$watch('app.nowReading', function(postView){
		if(postView){
			$("body").addClass("show-post")
		}
	})

	$scope.app.bookmark = function(postId){
		if($scope.app.isLogged){
          trix.toggleBookmark(postId).success(function(reponse){
          	if(reponse.content && reponse.content.response)
          		$scope.app.showInfoToast('A notícia foi adicionado a sua lista.')
          	else
          		$scope.app.showInfoToast('A notícia foi removida da sua lista.')
          }).error(function(){
          	console.log('error');
          })
        }else
          $scope.openSplash('signin_splash.html')
	}

	$scope.app.recommend = function(postId){
		if($scope.app.isLogged){
          trix.toggleRecommend(postId).success(function(reponse){
          	if(reponse.content && reponse.content.response)
          		$scope.app.showInfoToast('Você recomendou essa hostória.')
          	else
          		$scope.app.showInfoToast('Recomendação removida.')
          }).error(function(){
          	console.log('error');
          })
        }else
          $scope.openSplash('signin_splash.html')
	}

}])
