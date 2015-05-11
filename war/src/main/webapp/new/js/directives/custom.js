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

 .directive('offClick', ['$rootScope', '$parse', function ($rootScope, $parse) {
  var id = 0;
  var listeners = {};

  document.addEventListener("touchend", offClickEventHandler, true);
  document.addEventListener('click', offClickEventHandler, true);

  function targetInFilter(target, elms) {
    if (!target || !elms) return false;
    var elmsLen = elms.length;
    for (var i = 0; i < elmsLen; ++i)
      if (elms[i].contains(target)) return true;
    return false;
  }

  function offClickEventHandler(event) {
    var target = event.target || event.srcElement;
    angular.forEach(listeners, function (listener, i) {
      if (!(listener.elm.contains(target) || targetInFilter(target, listener.offClickFilter))) {
        $rootScope.$evalAsync(function () {
          listener.cb(listener.scope, {
            $event: event
          });
        })
      }

    });
  }

  return {
    restrict: 'A',
    compile: function ($element, attr) {
      var fn = $parse(attr.offClick);
      return function (scope, element) {
        var elmId = id++;
        var offClickFilter;
        var removeWatcher;
        
        offClickFilter = document.querySelectorAll(scope.$eval(attr.offClickFilter));

        if (attr.offClickIf) {
          removeWatcher = $rootScope.$watch(function () {
            return $parse(attr.offClickIf)(scope);
          }, function (newVal) {
            if (newVal) {
              on();
            } else if (!newVal) {
              off();
            }
          });
        } else {
          on();
        }

        attr.$observe('offClickFilter', function (value) {
          offClickFilter = document.querySelectorAll(scope.$eval(value));
        });

        scope.$on('$destroy', function () {
          off();
          if (removeWatcher) {
            removeWatcher();
          }
        });

        function on() {
          listeners[elmId] = {
            elm: element[0],
            cb: fn,
            scope: scope,
            offClickFilter: offClickFilter
          };
        }

        function off() {
          delete listeners[elmId];
        }
      };
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
      " ng-class=\"{'splash-open': animate}\" ng-style=\"{'z-index': 1050 + index*10, display: 'block'}\" ng-click=\"close($event)\">\n" +
      "    <div class='splash-inner' modal-transclude></div>\n" +
      "</div>\n" +
      ""
      );
  }
  ]);