// config

var app =  
angular.module('app')
.config(
  [        '$controllerProvider', '$compileProvider', '$filterProvider', '$provide', 'cfpLoadingBarProvider', '$httpProvider',
  function ($controllerProvider,   $compileProvider,   $filterProvider,   $provide ,  cfpLoadingBarProvider ,  $httpProvider) {
    
        // lazy controller, directive and service
        app.controller = $controllerProvider.register;
        app.directive  = $compileProvider.directive;
        app.filter     = $filterProvider.register;
        app.factory    = $provide.factory;
        app.service    = $provide.service;
        app.constant   = $provide.constant;
        app.value      = $provide.value;

        cfpLoadingBarProvider.includeSpinner = false;

        $httpProvider.interceptors.push(function (){
          return {
            'request': function(config) {
              if(config.url.indexOf("tpl") > -1 || config.url.indexOf(".css") > -1 || config.url.indexOf(".js") > -1)
                config.url = config.url + "?" + GLOBAL_URL_HASH;

              return config;
            }
          };
        });
      }
      ])
  // .config(['$translateProvider', function($translateProvider){
  //   // Register a loader for the static files
  //   // So, the module will search missing translation tables under the specified urls.
  //   // Those urls are [prefix][langKey][suffix].
  //   $translateProvider.useStaticFilesLoader({
  //     prefix: 'l10n/',
  //     suffix: '.js'
  //   });
  //   // Tell the module what language to use by default
  //   $translateProvider.preferredLanguage('en');
  //   // Tell the module to store the language in the local storage
  //   $translateProvider.useLocalStorage();
  // }])

.constant('TRIX', {
  baseUrl: location.protocol + '//' + location.host,
    //baseUrl: 'http://master.com',
    pageSize: 15,
    commentPageSize: 5
  })

.config(function(trixProvider, redactorOptions, TRIX){
  redactorOptions.lang = 'pt_br';
  redactorOptions.imageUpload = TRIX.baseUrl + "/api/files/contents/simple";
  redactorOptions.toolbarExternal = "#external-toolbar"
  redactorOptions.plugins = ['fontsize', 'fontcolor', 'video', 'counter']
  redactorOptions.minHeight = 100,
  redactorOptions.buttons = ['html', 'formatting', 'bold', 'italic', 'underline', 'deleted', 
  'fontsize', 'fontcolor', 'unorderedlist', 'orderedlist', 'outdent', 'indent',
  'link', 'image', 'video', 'alignment', 'horizontalrule'];
  trixProvider.setConfig({ url: location.protocol + '//' + location.host });
})
.run(function($rootScope){
  var backgroundColor = initData.network.backgroundColor
  var navbarColor = initData.network.navbarColor
  var mainColor = initData.network.mainColor
  /* define application's custom style based on the network's configuration */
  var $style = $('style#custom-style').length ? $('style#style#custom-style') : $('<style id="custom-style">').appendTo('body');
  $style.html(getCustomStyle(mainColor, backgroundColor, navbarColor));

  $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error){ 
    window.console && console.log(toState);
    window.console && console.log(fromState);
    window.console && console.error(event)
  });

  window.fbAsyncInit = function() {
    FB.init({
      appId: 1540807289473122,
      status: true, 
      cookie: true, 
      xfbml: true,
      version: 'v2.4'
    });
  };

  (function(d, s, id){
   var js, fjs = d.getElementsByTagName(s)[0];
   if (d.getElementById(id)) {return;}
   js = d.createElement(s); js.id = id;
   js.src = "//connect.facebook.net/en_US/sdk.js";
   fjs.parentNode.insertBefore(js, fjs);
 }(document, 'script', 'facebook-jssdk'));

  window.twttr = (function(d, s, id) {
    var js, fjs = d.getElementsByTagName(s)[0],
    t = window.twttr || {};
    if (d.getElementById(id)) return t;
    js = d.createElement(s);
    js.id = id;
    js.src = "https://platform.twitter.com/widgets.js";
    fjs.parentNode.insertBefore(js, fjs);
    
    t._e = [];
    t.ready = function(f) {
      t._e.push(f);
    };
    
    return t;
  }(document, "script", "twitter-wjs"));

});