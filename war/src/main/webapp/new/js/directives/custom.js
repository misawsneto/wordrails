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