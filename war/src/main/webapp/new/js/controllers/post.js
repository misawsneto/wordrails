// tab controller
app.controller('PostCtrl', ['$scope', '$log', '$timeout', '$mdDialog', function($scope, $log, $timeout, $mdDialog) {
	$scope.postCtrl = {}
	$scope.postCtrl.editingExisting = false;

	$scope.$watch('app.hidePostOptions', checkPostToolsWidth)

	$scope.app.showPostToolbar = false;
	$scope.showMediaButtons = false;

	function checkPostToolsWidth () {
		$timeout(function(){
			$("#external-toolbar").width($("#external-toolbar").parent().width())
		}, 50);
	}

	function checkScrollVisible(scrollTop){
		safeApply($scope, function(){
			if(scrollTop == 0)
				$scope.postCtrl.showHeaderShadow = false;
			else
				$scope.postCtrl.showHeaderShadow = true;
		});

		var toolbar_m_t = $("#post-title-box").height() + 15 + $("#post-media-box").height()

		if(scrollTop > toolbar_m_t){
			$("#external-toolbar").addClass('fixed-tools')
			$("#toggle-toolbars").addClass('fixed-tools-button')
		}else{
			$("#external-toolbar").removeClass('fixed-tools')
			$("#toggle-toolbars").removeClass('fixed-tools-button')
		}

		checkPostToolsWidth();

	}

	function doResize(){
		checkPostToolsWidth();
	}

	var timeoutResize;
	$(window).resize(function(){
		clearTimeout(timeoutResize);
		timeoutResize = setTimeout(doResize, 100);
	})

	$scope.content = null;
	$scope.postCtrl.redactorInit = function(){
		$scope.content = null;
		checkScrollVisible($("#post-cell").scrollTop())
		$("#post-cell").scroll(function(){
			checkScrollVisible(this.scrollTop);
		})
	}

	$("#post-placeholder").click(function(){
		$(".redactor-editor").focus();
	})

	$scope.showAdvanced = function(ev) {
		$mdDialog.show({
			controller: DialogController,
			templateUrl: 'advanced-dialog.html',
			targetEvent: ev
		})
		.then(function(answer) {
			$scope.alert = 'You said the information was "' + answer + '".';
		}, function() {
			$scope.alert = 'You cancelled the dialog.';
		});
	};

	function DialogController($scope, $mdDialog) {
		
		
	  $scope.hide = function() {
	    $mdDialog.hide();
	  };

	  $scope.cancel = function() {
	    $mdDialog.cancel();
	  };

	  $scope.answer = function(answer) {
	    $mdDialog.hide(answer);
	  };
	};

}]);