app.controller('ReadCtrl', ['$scope', '$log', '$timeout', '$rootScope', '$state', 'trix', '$mdToast', '$mdSidenav', 'TRIX',
	function($scope, $log, $timeout, $rootScope, $state, trix, $mdToast, $mdSidenav, TRIX) {
	var slug = $state.params.slug;

	var invitation = $state.params.invitation;
	var redirect = $state.params.redirect
	$scope.newComment = {body: null}

	if(slug){
		trix.findBySlug(slug, 'postProjection').success(function(response){
			if(response){
				$("html, body").animate({ scrollTop: 0 }, 0);
				$scope.app.nowReading = response.posts[0]
				$scope.app.nowReading.postId = $scope.app.nowReading.id;
				$scope.app.addPostRead($scope.app.nowReading.postId)
				$scope.app.nowReadingAuthor = {
		            authorId: $scope.app.nowReading.author.id,
		            imageSmallId: $scope.app.nowReading.author.imageSmallId,
		            coverMediumId: $scope.app.nowReading.author.coverMediumId,
		            authorName: $scope.app.nowReading.author.name
		        }

		        $scope.showComments($scope.app.nowReading.postId);
			}
			$("title").html($scope.app.nowReading.title);
			//console.log($scope.app.nowReading);
		})// end of success
	}else{
		// TODO error
	}

	// $scope.$watch('app.nowReading', function(postView){
	// 	if(postView){
	// 		$("body").addClass("show-post")
	// 	}
	// })

	$scope.page = 0;
	$scope.loadingComments = true
	$scope.allLoaded = false;
	$scope.beginning = true;
	$scope.window = 20

	$scope.showComments = function(postId){
		$scope.comments = []
		$scope.loadingComments = true
		if(postId)
			trix.findPostCommentsOrderByDate(postId, $scope.page, $scope.window, null, 'commentProjection').success(function(response){
				$scope.comments = response.comments;
				$scope.loadingComments = false
			}).error(function(){
				$scope.comments = null;
				$scope.loadingComments = false
			})
	}

	$scope.togglePostOptions = function(ev){
	  $mdSidenav('comments-list').toggle();
	}

	$scope.app.newComment = "null";

	$scope.createComment = function(){
		var comment = {}
		comment = angular.copy($scope.newComment);
		comment.author = extractSelf($scope.app.initData.person)
		comment.post =TRIX.baseUrl + '/api/posts/' + $scope.app.nowReading.postId

		trix.postComment(comment).success(function(response){
			if(!$scope.comments || $scope.comments.length == 0)
				$scope.comments = [];

			comment.author = angular.copy($scope.app.initData.person)
			comment.date = new Date().getTime();
			$scope.newComment = {body: ''};

			$scope.comments.unshift(comment)

		}).error(function(response,status){
			$scope.app.showErrorToast('Houve um erro inesperado. Tente novamente.')

		})
	}

	$scope.commentFocused = false;
	$scope.commentFocus = function(){
		$scope.commentFocused = true;
	}
	$scope.commentBlur = function(){
		$scope.commentFocused = false;
	}
}])
