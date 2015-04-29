// tab controller
app.controller('StationsCtrl', ['$scope', '$log', '$state', '$filter', '$timeout', '$interval',
	function($scope, $log, $state, $filter, $timeout, $interval) {

		$scope.tabs = [true, false];
	  $scope.tab = function(index){
	    angular.forEach($scope.tabs, function(i, v) {
	      $scope.tabs[v] = false;
	    });
	    $scope.tabs[index] = true;
	  }

		$scope.app.termPerspectiveView = initTermPerspective;
		var lastScrollTop = 0
		var didScroll = false;
		$timeout(function(){
			lastScrollTop = $(window).scrollTop() ? $(window).scrollTop() : 0;
		}, 20)

		function checkStationHeaderVisibel(scrollTop){
	    // Make sure they scroll more than delta
	    if(Math.abs(lastScrollTop - scrollTop) <= 5)
	    	return;
	    // If they scrolled down and are past the navbar, add class .nav-up.
	    // This is necessary so you never see what is "behind" the navbar.
	    if (scrollTop > lastScrollTop && scrollTop > 50){
	        // Scroll Down
	        $('.station-header').removeClass('nav-down').addClass('nav-up');
	      } else {
	        // Scroll Up
	        if(scrollTop + $(window).height() < $(document).height()) {
	        	$('.station-header').removeClass('nav-up').addClass('nav-down');
	        }
      }
      lastScrollTop = scrollTop;
    }

    $timeout(function(){
    	checkStationHeaderVisibel(lastScrollTop);
    	$(window).scroll(function(){
    		didScroll = true;
    	})
    }, 70);

    $interval(function() {
    	if (didScroll) {
    		checkStationHeaderVisibel($(window).scrollTop());
    		didScroll = false;
    	}
    }, 250);

    $scope.loadPost = function(postId){
    } 

  }]);