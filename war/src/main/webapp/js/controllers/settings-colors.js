app.controller('SettingsColorsCtrl', ['$scope', '$log', '$timeout', '$mdDialog', '$state', '$window', 'trix',
		function($scope ,  $log ,  $timeout ,  $mdDialog ,  $state,  $window, trix){
	$scope.app.lastSettingState = "app.settings.colors";
	$scope.isMobile = true;

  	var navbarColor = initData.network.navbarColor
	var backgroundColor = initData.network.backgroundColor
  	var mainColor = initData.network.mainColor

	// if(!$scope.app.testColors){
	// 	$scope.app.testColors = {
	// 		'navbar': navbarColor,
	// 		'backgroundColor': backgroundColor,
	// 		'mainColor': mainColor
	// 	}
	// }

	$("#paint-navbar").attr('style', 'background-color: ' + navbarColor) 
	$("#paint-background").attr('style', 'background-color: ' + backgroundColor)
	$("#paint-main").attr('style', 'background-color: ' + mainColor)

	$scope.colors = {}
	$scope.colors.one = "#333"
	$scope.colors.two = "#f0f0f0"
	$scope.colors.three = navbarColor
	$scope.colors.four = backgroundColor
	$scope.colors.five = mainColor

	// function updateColors (){
	// 	var $style = $('style#custom-colors').length ? $('style#style#custom-colors') : $('<style id="custom-colors">').appendTo('body');
	// 	var colorStyles = '' +
	// 	'.color-1 {background-color: '+$scope.colors.one+'!important; color: '+$scope.colors.one+ '!important}\n' + 
	// 	'.color-2 {background-color: '+$scope.colors.two+'!important; color: '+$scope.colors.two+ '!important}\n' + 
	// 	'.color-3 {background-color: '+$scope.colors.three+'!important; color: '+$scope.colors.three+ '!important}\n' + 
	// 	'.color-4 {background-color: '+$scope.colors.four+'!important; color: '+$scope.colors.four+ '!important}\n' + 
	// 	'.color-5 {background-color: '+$scope.colors.five+'!important; color: '+$scope.colors.five+ '!important}'
	// 	console.log(colorStyles);
	// 	$style.html(colorStyles);
	// }

		function updateColors (){
		$('.color-1').attr('style', 'background-color: '+$scope.colors.one+'; color: '+$scope.colors.one);
		$('.color-2').attr('style', 'background-color: '+$scope.colors.two+'; color: '+$scope.colors.two) 
		$('.color-3').attr('style', 'background-color: '+$scope.colors.three+'; color: '+$scope.colors.three) 
		$('.color-4').attr('style', 'background-color: '+$scope.colors.four+'; color: '+$scope.colors.four) 
		$('.color-5').attr('style', 'background-color: '+$scope.colors.five+'; color: '+$scope.colors.five)
	}

	updateColors();

	$scope.$watch('colors', function(newVal){
		updateColors();
	}, true)

	$scope.saveColors = function(){
		safeApply($scope, function(){
			$scope.network = angular.copy($scope.app.initData.network)

			var navbar = $scope.network.navbarColor = rbgToHex2($("#paint-navbar").css('backgroundColor'))
			var background = $scope.network.backgroundColor = rbgToHex2($("#paint-background").css('backgroundColor'))
			var main = $scope.network.mainColor = rbgToHex2($("#paint-main").css('backgroundColor'))

			trix.putNetwork($scope.network).success(function(response){
				$scope.app.showSuccessToast('Alterações realizadas com sucesso.')
				$scope.app.getInitData();
				$('style#custom-style').remove();
				$timeout(function() {
					$('<style id="custom-style">').appendTo('body').html(getCustomStyle(main, background, navbar));
				});
			})
		})
	}

	// ----------------------------------

	var docElem = $("html"),
		// transition end event name
		transEndEventNames = { 'WebkitTransition': 'webkitTransitionEnd', 'MozTransition': 'transitionend', 'OTransition': 'oTransitionEnd', 'msTransition': 'MSTransitionEnd', 'transition': 'transitionend' },
		transEndEventName = transEndEventNames[ Modernizr.prefixed( 'transition' ) ];

	function scrollX() { return window.pageXOffset || docElem.scrollLeft(); }
	function scrollY() { return window.pageYOffset || docElem.scrollTop(); }
	function getOffset(el) {
		var offset = el.getBoundingClientRect();
		return { top : offset.top + scrollY(), left : offset.left + scrollX() };
	}

	function dragMoveListener(event) {
		var target = event.target,
			// keep the dragged position in the data-x/data-y attributes
			x = (parseFloat(target.getAttribute('data-x')) || 0) + event.dx,
			y = (parseFloat(target.getAttribute('data-y')) || 0) + event.dy;

		// translate the element
		target.style.transform = target.style.webkitTransform = 'translate(' + x + 'px, ' + y + 'px)';
		target.style.zIndex = 10000;

		// update the posiion attributes
		target.setAttribute('data-x', x);
		target.setAttribute('data-y', y);
	}

	function revertDraggable(el) {
		el.style.transform = el.style.webkitTransform = 'none';
		el.style.zIndex = 1;
		el.setAttribute('data-x', 0);
		el.setAttribute('data-y', 0);
	}

	function init() {

		// target elements with the "drag-element" class
		interact('.drag-element').draggable({
			// enable inertial throwing
			inertia: true,
			// call this function on every dragmove event
			onmove: dragMoveListener,
			onend: function (event) {
				if(!$(event.target).hasClass('drag-element--dropped') && !$(event.target).hasClass('drag-element--dropped-text')) {
					revertDraggable(event.target);
				}
			}
		});

		// enable draggables to be dropped into this
		interact('.paint-area').dropzone({
			// only accept elements matching this CSS selector
			accept: '.drag-element',
			// Require a 75% element overlap for a drop to be possible
			overlap: 0.75,
			ondragenter: function (event) {
				$(event.target).addClass('paint-area--highlight');
			},
			ondragleave: function (event) {
				$(event.target).removeClass('paint-area--highlight');
			},
			ondrop: function (event) {
				var type = 'area';
				if($(event.target).hasClass('paint-area--text')) {
					type = 'text';
				}

				var draggableElement = event.relatedTarget;

				$(draggableElement).addClass( type === 'text' ? 'drag-element--dropped-text' : 'drag-element--dropped');

				var onEndTransCallbackFn = function(ev) {
					this.removeEventListener( transEndEventName, onEndTransCallbackFn );
					if( type === 'area' ) {
						paintArea(event.dragEvent, event.target, draggableElement.getAttribute('data-color'));
					}
					setTimeout(function() {
						revertDraggable(draggableElement);
						$(draggableElement).removeClass(type === 'text' ? 'drag-element--dropped-text' : 'drag-element--dropped');
					}, type === 'text' ? 0 : 250);
				};
				if( type === 'text' ) {
					paintArea(event.dragEvent, event.target, draggableElement.getAttribute('data-color'));
				}
				draggableElement.querySelector('.drop').addEventListener(transEndEventName, onEndTransCallbackFn);
			},
			ondropdeactivate: function (event) {
				// remove active dropzone feedback
				$(event.target).removeClass('paint-area--highlight');
			}
		});

		// reset colors
		document.querySelector('button.reset-button').addEventListener('click', resetColors);
	}

	function paintArea(ev, el, color) {
		var type = 'area';
		if($(el).hasClass('paint-area--text')) {
			type = 'text';
		}

		if( type === 'area' ) {
			// create SVG element
			var dummy = document.createElementNS('http://www.w3.org/2000/svg', 'svg');
			dummy.setAttributeNS(null, 'version', '1.1');
			dummy.setAttributeNS(null, 'width', '100%');
			dummy.setAttributeNS(null, 'height', '100%');
			dummy.setAttributeNS(null, 'class', 'paint');

			var g = document.createElementNS('http://www.w3.org/2000/svg', 'g');
			var offLeft = getOffset(el).left;
			var offTop = getOffset(el).top
			var xTrans = Number(ev.pageX - offLeft)
			var yTrans = Number(ev.pageY - offTop)

			var transformString = 'translate(' + xTrans + ', ' + yTrans + ')'

			g.setAttributeNS(null, 'transform', transformString);

			var circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
			circle.setAttributeNS(null, 'cx', 0);
			circle.setAttributeNS(null, 'cy', 0);
			circle.setAttributeNS(null, 'r', Math.sqrt(Math.pow(el.offsetWidth,2) + Math.pow(el.offsetHeight,2)));
			circle.setAttributeNS(null, 'fill', color);

			dummy.appendChild(g);
			g.appendChild(circle);
			el.appendChild(dummy);
		}

		setTimeout(function() {
			$(el).addClass( 'paint--active');

			if( type === 'text' ) {
				el.style.color = color;
				var onEndTransCallbackFn = function(ev) {
					if( ev.target != this ) return;
					this.removeEventListener( transEndEventName, onEndTransCallbackFn );
					$(el).removeClass('paint--active');
				};

				el.addEventListener(transEndEventName, onEndTransCallbackFn);
			}
			else {
				var onEndTransCallbackFn = function(ev) {
					if( ev.target != this || ev.propertyName === 'fill-opacity' ) return;
					this.removeEventListener(transEndEventName, onEndTransCallbackFn);
					// set the color
					el.style.backgroundColor = color;
					// remove SVG element
					el.removeChild(dummy);

					setTimeout(function() { $(el).removeClass( 'paint--active'); }, 25);
				};

				circle.addEventListener(transEndEventName, onEndTransCallbackFn);
			}
		}, 25);
	}

	function resetColors() {
		[].slice.call(document.querySelectorAll('.paint-area')).forEach(function(el) {
			el.style[$(el).hasClass( 'paint-area--text') ? 'color' : 'background-color'] = '';
		});
	}

	$timeout(function() {
		init();
	}, 50);

}])
