// config

var app =  
angular.module('app')
  .config(
    [        '$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
    function ($controllerProvider,   $compileProvider,   $filterProvider,   $provide) {
        
        // lazy controller, directive and service
        app.controller = $controllerProvider.register;
        app.directive  = $compileProvider.directive;
        app.filter     = $filterProvider.register;
        app.factory    = $provide.factory;
        app.service    = $provide.service;
        app.constant   = $provide.constant;
        app.value      = $provide.value;
    }
  ])
  .config(['$translateProvider', function($translateProvider){
    // Register a loader for the static files
    // So, the module will search missing translation tables under the specified urls.
    // Those urls are [prefix][langKey][suffix].
    $translateProvider.useStaticFilesLoader({
      prefix: '/i18n/',
      suffix: '.js'
    });
    // Tell the module what language to use by default
    $translateProvider.preferredLanguage('en');
    // Tell the module to store the language in the local storage
    $translateProvider.useLocalStorage();
  }])

  .config(function(trixProvider){
    trixProvider.setConfig({ url: location.protocol + '//' + 'demo.xarxlocal.com' });
  })

  .run(function($rootScope){
    $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error){ 
      // window.console && console.log(toState);
      // window.console && console.log(fromState);
      var errorObj = {
        event: event, 
        toState: toState, 
        toParams: toParams, 
        fromState: fromState, 
        fromParams: fromParams, 
        error: error
      }

      window.console && console.error(errorObj);
      window.console && console.error(error.message);
    });
  })
  
  ;
