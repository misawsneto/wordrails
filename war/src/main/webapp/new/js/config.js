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
      prefix: 'l10n/',
      suffix: '.js'
    });
    // Tell the module what language to use by default
    $translateProvider.preferredLanguage('en');
    // Tell the module to store the language in the local storage
    $translateProvider.useLocalStorage();
  }])

  .constant('WORDRAILS', {
    baseUrl: location.protocol + '//' + location.host,
    //baseUrl: 'http://master.com',
    pageSize: 15,
    commentPageSize: 5
  })

  .config(function(redactorOptions, WORDRAILS){
    redactorOptions.lang = 'pt_br';
    redactorOptions.imageUpload = WORDRAILS.baseUrl + "/api/files/contents/simple";
    redactorOptions.toolbarExternal = "#external-toolbar"
    redactorOptions.plugins = ['fontsize', 'fontcolor', 'video', 'counter']
    redactorOptions.minHeight = 300
    redactorOptions.buttons = ['html', 'formatting', 'bold', 'italic', 'underline', 'deleted', 
                                  'fontsize', 'fontcolor', 'unorderedlist', 'orderedlist', 'outdent', 'indent',
                                  'link', 'image', 'video', 'alignment', 'horizontalrule'];
  })
  ;