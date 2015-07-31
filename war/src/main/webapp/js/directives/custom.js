 angular.module('app')
 .directive('focusMe', function($timeout, $parse) {
  return {
    //scope: true,   // optionally create a child scope
    link: function(scope, element, attrs) {
      var model = $parse(attrs.focusMe);
      scope.$watch(model, function(value) {
        if(value === true) { 
          $timeout(function() {
            element[0].focus(); 
          });
        }
      });
    }
  };
});

 angular.module('app').directive("offcanvas", function(){
  return{
    restrict: 'E',
    link: function(scope, element, attrs){
      var bodyEl = document.body,
      content = document.querySelector( '.content-wrap' ),
      openbtn = document.getElementById( 'open-button' ),
      closebtn = document.getElementById( 'close-button' ),
      isOpen = false;

      function init() {
        initEvents();
      }

      function initEvents() {
        /*openbtn.addEventListener( 'click', toggleMenu );
        if( closebtn ) {
          closebtn.addEventListener( 'click', toggleMenu );
        }*/

        // close the menu element if the target it´s not the menu element or one of its descendants..
        content.addEventListener( 'click', function(ev) {
          var target = ev.target;
          if( isOpen && target !== openbtn ) {
            toggleMenu();
          }
        } );
      }

      scope.toggleMenu = function(event, close){
        toggleMenu(close);
        if(event)
          event.stopPropagation();
      }

      scope.contentClick = function(){
        if(isOpen)
          toggleMenu(); 
      };

      function toggleMenu(close) {
        if(close){
          // classie.remove( bodyEl, 'show-menu' );
          $('body').removeClass('show-menu')
          isOpen = false;
          return;
        }
        if( isOpen ) {
          $('body').removeClass('show-menu')
        }
        else {
          $('body').addClass('show-menu')
        }

        isOpen = !isOpen;
      }

      init();
    },
    templateUrl: 'tpl/blocks/offcanvas.html'
  }// end of return 
})

 .directive('clickOutside', ['$document',function ($document) {

   return {
    restrict: 'A',
    scope: {
      clickOutside: '&'
    },
    link: function ($scope, elem, attr) {
      var classList = (attr.outsideIfNot !== undefined) ? attr.outsideIfNot.replace(', ', ',').split(',') : [];
      if (attr.id !== undefined) classList.push(attr.id);

      $document.on('click', function (e) {
        var i = 0,
        element;

        if (!e.target) return;

        for (element = e.target; element; element = element.parentNode) {
          var id = element.id;
          var classNames = element.className;

          if (id !== undefined) {
            for (i = 0; i < classList.length; i++) {
              if (id.indexOf(classList[i]) > -1 || classNames.indexOf(classList[i]) > -1) {
                return;
              }
            }
          }
        }

        $scope.$eval($scope.clickOutside);
      });
    }
  };
}])

// Module for the demo
angular.module('splash', ['ui.splash']);

// Re-usable $splash module
angular.module('ui.splash', ['ui.bootstrap'])
.service('$splash', [
  '$modal',
  '$rootScope',
  function($modal, $rootScope) {
    return {
      open: function (opts) {
        if(!opts){
          opts = {};
        }
        opts.backdrop = false;
        opts.windowTemplateUrl = 'splash_index.html';

        return $modal.open(opts);
      }
    };
  }
  ])
.run([
  '$templateCache',
  function ($templateCache) {
    $templateCache.put('splash_index.html',
      "<div modal-render=\"{{$isRendered}}\" tabindex=\"-1\" role=\"dialog\" class=\"splash\"\n" +
      "    modal-animation-class=\"fade\"\n" +
      " ng-style=\"{'z-index': 2050 + index*10, display: 'block'}\" ng-click=\"close($event);\">\n" +
      "    <div class='splash-inner' modal-transclude></div>\n" +
      "</div>\n" +
      ""
      );
  }
  ]);