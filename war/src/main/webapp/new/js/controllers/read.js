app.controller('ReadCtrl', ['$scope', '$log', '$timeout', '$rootScope', '$state', 'trix',
	function($scope, $log, $timeout, $rootScope, $state, trix) {

	var slug = $state.params.slug;

	if(slug){
		trix.findBySlug(slug, 'postProjection').success(function(response){
			if(response && response.posts){
				$scope.app.nowReading = response.posts[0]
				$scope.app.nowReading.postId = $scope.app.nowReading.id;
				$scope.app.nowReadingAuthor = {
		            authorId: $scope.app.nowReading.author.id,
		            imageSmallId: $scope.app.nowReading.author.imageSmallId,
		            coverMediumId: $scope.app.nowReading.author.coverMediumId,
		            authorName: $scope.app.nowReading.author.name
		        }
			}
			$("title").html($scope.app.nowReading.title);
		})// end of success
	}else{
		// TODO error
	}

	$scope.$watch('app.nowReading', function(postView){
		if(postView){
			$("body").addClass("show-post")
		}
	})

}])
