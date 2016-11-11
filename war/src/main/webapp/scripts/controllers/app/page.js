app.controller('PageCtrl', ['$scope', '$rootScope', '$log', '$timeout', '$mdDialog', '$state', 'TRIX', 'cfpLoadingBar', 'trixService', 'trix', '$http', '$mdToast', '$templateCache', '$location', '$interval', '$mdSidenav', '$translate', '$filter', '$localStorage', '$sce', 'station',
	function($scope , $rootScope,  $log ,  $timeout ,  $mdDialog ,  $state ,  TRIX ,  cfpLoadingBar ,  trixService ,  trix ,  $http ,  $mdToast, $templateCache  , $location, $interval, $mdSidenav, $translate, $filter, $localStorage, $sce, station){

		$scope.app.defaultStation = trixService.selectDefaultStation($scope.app.stations, $scope.app.defaultStation ? $scope.app.defaultStation.stationId : null);
      	$scope.app.stationsPermissions = trixService.getStationPermissions(angular.copy($scope.app));

	    $scope.thisStation = station ? station : $scope.app.defaultStation;

	    $scope.app.page = 1;

	    $scope.loadingPerspective = false;
	    $scope.app.perspectivePaginate = function(){
	    	if(!$scope.loadingPerspective && $scope.app.termPerspectiveView && $scope.app.termPerspectiveView.homeRow && $scope.app.termPerspectiveView.homeRow.cells && !$scope.app.termPerspectiveView.homeRow.allLoaded){
	    		$scope.app.termPerspectiveView.homeRow.page = $scope.app.page
		    	$scope.loadingPerspective = true;
		    	trix.getRowView($scope.thisStation.defaultPerspectiveId, $scope.app.termPerspectiveView.id, null, $scope.app.page, 10)
		    	.success(function(response){
		            if(response.cells && response.cells.length > 0){
		                response.cells.forEach(function(cell, index){
		                		if(cell.postView.stationId === $scope.thisStation.id)
		                   	$scope.app.termPerspectiveView.homeRow.cells.push(cell)
		               	});
		                $scope.app.page = $scope.app.termPerspectiveView.homeRow.page = $scope.app.termPerspectiveView.homeRow.page + 1
		                $scope.reloadMasonry();
		                $scope.loadingPerspective = false;
		            }else{
		                $scope.app.termPerspectiveView.homeRow.allLoaded = true;
		            }

		    	})
    		}
	    }

    if($state.includes('app.home')){
    	if(!$scope.app.termPerspectiveView || ($scope.thisStation.id !== $scope.app.termPerspectiveView.stationId)){
    		if($scope.app.termPerspectiveView && $scope.app.termPerspectiveView.featuredRow)
    			$scope.app.termPerspectiveView = null;
    		$scope.app.loadPerspective($scope.thisStation);
    	}else{
    		if($scope.app.perspectiveTerms == null)
    			$scope.app.loadPerspectiveTerms()
    	}
    	$scope.app.stationName = null;
    }else if($state.includes('app.station.stationHome')){
    	$scope.app.stationName = $scope.thisStation.name;
    }

    if($scope.app.termPerspectiveView && $scope.app.termPerspectiveView.homeRow && $scope.app.termPerspectiveView.homeRow.cells){
      $scope.app.termPerspectiveView.homeRow.allLoaded = false;
      var length = $scope.app.termPerspectiveView.homeRow.cells.length >= 10 ? 10 : $scope.app.termPerspectiveView.homeRow.cells.length;
        $scope.app.termPerspectiveView.homeRow.cells = $scope.app.termPerspectiveView.homeRow.cells.slice(0,length);
    }

	  $timeout(function(){
	    $('#scroll-box').animate({scrollTop: 0}, 0, 'easeOutQuint');

	    $('#cc-slider').carouFredSel({
		width: '100%',
		align: false,
		items: 3,
		items: {
			width: $('#cc-wrapper').width() * 0.15,
			height: 500,
			visible: 1,
			minimum: 1
		},
		scroll: {
			items: 1,
			timeoutDuration : 5000,
			onBefore: function(data) {
 
				//	find current and next slide
				var currentSlide = $('.slide.active', this),
					nextSlide = data.items.visible,
					_width = $('#cc-wrapper').width();
 
				//	resize currentslide to small version
				currentSlide.stop().animate({
					width: _width * 0.15
				});		
				currentSlide.removeClass( 'active' );
 
				//	hide current block
				data.items.old.add( data.items.visible ).find( '.slide-block' ).stop().fadeOut();					
 
				//	animate clicked slide to large size
				nextSlide.addClass( 'active' );
				nextSlide.stop().animate({
					width: _width * 0.7
				});						
			},
			onAfter: function(data) {
				//	show active slide block
				data.items.visible.last().find( '.slide-block' ).stop().fadeIn();
			}
		},
		onCreate: function(data){
 
			//	clone images for better sliding and insert them dynamacly in slider
			var newitems = $('.slide',this).clone( true ),
				_width = $('#cc-wrapper').width();
 
			$(this).trigger( 'insertItem', [newitems, newitems.length, false] );
 
			//	show images 
			$('.slide', this).fadeIn();
			$('.slide:first-child', this).addClass( 'active' );
			$('.slide', this).width( _width * 0.15 );
 
			//	enlarge first slide
			$('.slide:first-child', this).animate({
				width: _width * 0.7
			});
 
			//	show first title block and hide the rest
			$(this).find( '.slide-block' ).hide();
			$(this).find( '.slide.active .slide-block' ).stop().fadeIn();
		}
	});
 
	//	Handle click events
	$('#cc-slider').children().click(function() {
		$('#cc-slider').trigger( 'slideTo', [this] );
	});
 
	//	Enable code below if you want to support browser resizing
	$(window).resize(function(){
 
		var slider = $('#cc-slider'),
			_width = $('#cc-wrapper').width();
 
		//	show images
		slider.find( '.slide' ).width( _width * 0.15 );
 
		//	enlarge first slide
		slider.find( '.slide.active' ).width( _width * 0.7 );
 
		//	update item width config
		slider.trigger( 'configuration', ['items.width', _width * 0.15] );
	});

	  })


	pageCtrl = $scope;
}]);

var pageCtrl = null;
