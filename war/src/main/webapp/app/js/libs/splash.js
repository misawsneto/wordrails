// Module for the demo
angular.module('splashDemo', ['ui.splash'])
.controller('MainCtrl', ['$splash', function ($splash) {
  this.openSplash = function () {
    $splash.open({
      title: 'Hi there!',
      message: "This sure is a fine modal, isn't it?"
    });
  };
}]);

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
        opts.backdrop = false,
        opts.windowTemplateUrl = 'splash/index.html';
        console.log(opts);
        return $modal.open(opts);
      }
    };
  }
])
.run([
  '$templateCache',
  function ($templateCache) {
    $templateCache.put('splash/index.html',
      '<section class="splash" ng-class="{\'splash-open\': animate}" ng-style="{\'z-index\': 1040, display: \'block\'}" ng-click="close($event)">' +
      '  <div class="splash-inner" ng-transclude></div>' +
      '</section>'
    );
    $templateCache.put('splash/content.html',
      '<div class="splash-content text-center">' +
      '  <h1 ng-bind="title"></h1>' +
      '  <p class="lead" ng-bind="message"></p>' +
      '  <button class="btn btn-lg btn-outline" ng-bind="btnText || \'Ok, cool\'" ng-click="$close()"></button>' +
      '</div>'
    );
  }
]);