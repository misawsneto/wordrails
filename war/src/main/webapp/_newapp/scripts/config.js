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
  .constant('TRIX', {
    baseUrl: location.protocol + '//' + location.host})

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

  .config(function(trixProvider, $mdThemingProvider){
    trixProvider.setConfig({ url: location.protocol + '//' + 'demo.xarxlocal.com' });

    $mdThemingProvider.definePalette('clear', { "50": "#FFFFFF", "100": "#FFFFFF", "200": "#FFFFFF", "300": "#FFFFFF", "400": "#FFFFFF", "500": "#FFFFFF", "600": "#cbcaca", "700": "#aeadad", "800": "#919090", "900": "#747474", "A100": "#f8f8f8", "A200": "#f4f3f3", "A400": "#ecebeb", "A700": "#aeadad" } );
    $mdThemingProvider.theme('default').primaryPalette('red').accentPalette('clear');
    $mdThemingProvider.theme('dark').primaryPalette('red').accentPalette( 'clear' ).dark();

    // Set default palettes as themes for use in UI.
    $mdThemingProvider.theme('red').primaryPalette('red').accentPalette('clear');
    $mdThemingProvider.theme('pink').primaryPalette('pink').accentPalette('clear');
    $mdThemingProvider.theme('purple').primaryPalette('purple').accentPalette('clear');
    $mdThemingProvider.theme('deep-purple').primaryPalette('deep-purple').accentPalette('clear');
    $mdThemingProvider.theme('indigo').primaryPalette('indigo').accentPalette('clear');
    $mdThemingProvider.theme('blue').primaryPalette('blue').accentPalette('clear');
    $mdThemingProvider.theme('light-blue').primaryPalette('light-blue').accentPalette('clear');
    $mdThemingProvider.theme('cyan').primaryPalette('cyan').accentPalette('clear');
    $mdThemingProvider.theme('teal').primaryPalette('teal').accentPalette('clear');
    $mdThemingProvider.theme('green').primaryPalette('green').accentPalette('clear');
    $mdThemingProvider.theme('light-green').primaryPalette('light-green').accentPalette('clear');
    $mdThemingProvider.theme('lime').primaryPalette('lime').accentPalette('clear');
    $mdThemingProvider.theme('yellow').primaryPalette('yellow').accentPalette('clear');
    $mdThemingProvider.theme('amber').primaryPalette('amber').accentPalette('clear');
    $mdThemingProvider.theme('orange').primaryPalette('orange').accentPalette('clear');
    $mdThemingProvider.theme('deep-orange').primaryPalette('deep-orange').accentPalette('clear');
    $mdThemingProvider.theme('brown').primaryPalette('brown').accentPalette('clear');
    $mdThemingProvider.theme('grey').primaryPalette('grey').accentPalette('clear');
    $mdThemingProvider.theme('blue-grey').primaryPalette('blue-grey').accentPalette('clear');

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
