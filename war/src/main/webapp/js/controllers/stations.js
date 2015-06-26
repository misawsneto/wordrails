// tab controller
app.controller('StationsCtrl', ['$scope', '$log', '$state', '$filter', '$timeout', '$interval', 'trix', 'cfpLoadingBar', '$q',
	function($scope, $log, $state, $filter, $timeout, $interval, trix, cfpLoadingBar, $q) {

		$scope.tabs = [true, false];
		$scope.tab = function(index){
	    angular.forEach($scope.tabs, function(i, v) {
      	$scope.tabs[v] = false;
	    });
	    $scope.tabs[index] = true;
		}

		// $scope.app.termPerspectiveView = initTermPerspective;
		
		 $scope.app.termPerspectiveView = null;

		trix.findPerspectiveView($scope.app.currentStation.defaultPerspectiveId, null, null, 0, 10).success(function(termPerspective){
			$scope.app.termPerspectiveView = termPerspective
		})

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
		        $('body').removeClass('nav-down').addClass('nav-up');
		      } else {
		        // Scroll Up
		        if(scrollTop + $(window).height() < $(document).height()) {
		        	$('body').removeClass('nav-up').addClass('nav-down');
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

    $scope.selectedVerticalRow = null;
    $scope.selectVerticalRow = function(ordinaryRow){
    	$scope.selectedVerticalRow = ordinaryRow
    	// console.log(ordinaryRow);
    }

    $scope.horizontalCallback = function(){
    	if($scope.app.horizontalCursor){
        $scope.app.setNowReading($scope.app.horizontalCursor.postView, $scope.app.horizontalCursor.list)
        $scope.app.horizontalCursor = null;
      }
    }

    $scope.horizontalPaginate = function(ordinaryRow){
    	if($scope.app.getCurrentStateName() != 'app.stations' || $scope.app.viewMode != 'horizontal' || ordinaryRow.allLoaded)
    		return null;

    	if(ordinaryRow.page == null){
    		ordinaryRow.page = 0
    		ordinaryRow.loading = false;
    		return;
    	}

    	if(!ordinaryRow.loading){
	    	ordinaryRow.loading = true;
    		return $q(function(resolve, reject) {
	    		cfpLoadingBar.complete();
		    	trix.getRowView($scope.app.currentStation.defaultPerspectiveId, null, ordinaryRow.termId, ordinaryRow.page + 1, 10)
		    	.success(function(response){
		    		if(response.cells && response.cells.length > 0){
		    			response.cells.forEach(function(cell, index){
		    				ordinaryRow.cells.push(cell)
		    			});
		    			ordinaryRow.page = ordinaryRow.page + 1
		    		}else{
		    			ordinaryRow.allLoaded = true;
		    		}
		    		$timeout(function() {
		    			ordinaryRow.loading = false;
		    		}, 300);
		    		resolve();
		    	}).error(function(){
		    		ordinaryRow.loading = false;
		    		reject();
		    	})
		    });
		  }
    }

    $scope.verticalPaginate = function(ordinaryRow){

    	if($scope.app.viewMode != 'vertical' || ordinaryRow.allLoaded)
    		return null;

    	if(ordinaryRow.page == null){
    		ordinaryRow.page = 0
    		ordinaryRow.loading = false;
    		return;
    	}

    	if($scope.selectedVerticalRow.$$hashKey != ordinaryRow.$$hashKey)
    		return;

    	if(!ordinaryRow.loading){
    		cfpLoadingBar.complete();
	    	ordinaryRow.loading = true;
	    	trix.getRowView($scope.app.currentStation.defaultPerspectiveId, null, ordinaryRow.termId, ordinaryRow.page + 1, 10)
	    	.success(function(response){
	    		if(response.cells && response.cells.length > 0){
	    			response.cells.forEach(function(cell, index){
	    				ordinaryRow.cells.push(cell)
	    			});
	    			ordinaryRow.page = ordinaryRow.page + 1
	    		}else{
	    			ordinaryRow.allLoaded = true;
	    		}
	    		$timeout(function() {
	    			ordinaryRow.loading = false;
	    		}, 300);
	    	}).error(function(){
	    		ordinaryRow.loading = false;
	    	})
	    }

    }

}]);