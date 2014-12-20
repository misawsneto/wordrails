'use strict';
// Declare app level module which depends on filters, and services
var app = angular.module('app', [
    'ngAnimate',
    'ngCookies',
    'ngStorage',
    'ui.router',
    'ui.bootstrap',
    'ui.load',
    'ui.jq',
    'ui.validate',
    'ui.tree',
    'ui.sortable',
    'colorpicker.module',
    'pascalprecht.translate',
    'app.filters',
    'app.services',
    'app.directives',
    'app.controllers',
    'froala',
    'angular-redactor',
    'infinite-scroll',
    'ui.splash',
    'ngSanitize',
    'oc.lazyLoad',
  ])
.value('froalaConfig', {
            inlineMode: false,
            events : {
                align : function(e, editor, alignment){
                    console.log(alignment + ' aligned');
                }
            }
        })
.run(
  [          '$rootScope', '$state', '$stateParams',
    function ($rootScope,   $state,   $stateParams) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;        
    }
  ]
)
.config(
  [          '$stateProvider', '$urlRouterProvider', '$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
    function ($stateProvider,   $urlRouterProvider,   $controllerProvider,   $compileProvider,   $filterProvider,   $provide) {
        
        // lazy controller, directive and service
        app.controller = $controllerProvider.register;
        app.directive  = $compileProvider.directive;
        app.filter     = $filterProvider.register;
        app.factory    = $provide.factory;
        app.service    = $provide.service;
        app.constant   = $provide.constant;
        app.value      = $provide.value;

        $urlRouterProvider.otherwise('/s');

        $stateProvider
            .state('app', {
                abstract: true,
                url: '',
                templateUrl: 'tpl/app.html',
                resolve: { // resolve waits for the data to be loaded and for the resolve signal form the defered object
                    wordrailsService: 'wordrailsService', // get service
                    network: function(wordrailsService){ // injects network in all child controllers
                        return wordrailsService.getInitialData(); // get initial data from the API.
                    }
                }
            })
            .state('app.stations', {
                url: '/s?stationId',
                template: '<div ui-view class="fade-in"></div>',
                controller: 'StationsCtrl'
            })
            .state('app.user', {
                url: '/@:username',
                templateUrl: 'tpl/posts/user_page.html',
                controller: 'UserPageCtrl',
                // use resolve to load other dependences
                 resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load('angularFileUpload')
                    }]
                }
            })
            //my posts
            .state('app.user.drafts', {
                url: '/drafts',
                templateUrl: 'tpl/posts/user_page_drafts.html',
                resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load('angularFileUpload')
                    }]
                }
            })
            .state('app.signup', {
                url: '/signup',
                templateUrl: 'tpl/signup.html',
                controller: 'SignupCtrl'
            })
            .state('app.post', {
                url: '/{postId}',
                templateUrl: 'tpl/posts/single_post.html'
            })
            //search
            .state('app.stations.search', {
                url: '/search?q&page',
                templateUrl: 'tpl/app/app_search.html',
                controller: 'SearchCtrl'
            })
            // new post
            .state('app.stations.editor', {
                url: '/editor?postId',
                templateUrl: 'tpl/app/app_post_editor.html',
                // use resolve to load other dependences
                resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load('angularFileUpload')
                    }]
                }
            })
            // perspectives
            .state('app.stations.perspectives', {
                url: '/?perspectiveId&termId',
                templateUrl: 'tpl/app/app_perspectives.html'
            })
            // news-items
            .state('app.stations.vposts', {
                url: '/vposts/?perspectiveId&termId&postId',
                templateUrl: 'tpl/posts/vposts.html'
            })
            // scaffold
            .state('app.scaffold', {
                url: '/settings',
                //template: '<div ui-view class="fade-in"></div>',
                templateUrl: 'tpl/app/app_scaffold.html',
                controller: 'ScaffoldCtrl'
            })
            .state('app.scaffold.sponsors', {
                url: '/sponsors',
                templateUrl: 'tpl/scaffold/scaffold_sponsors.html'
            })
            .state('app.scaffold.sponsors_create', {
                url: '/sponsors/create',
                templateUrl: 'tpl/scaffold/scaffold_sponsors_create.html',
                controller: 'ScaffoldSponsorCtrl',
                resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load('angularFileUpload')
                    }]
                }
            })
            .state('app.scaffold.users', {
                url: '/users',
                templateUrl: 'tpl/scaffold/scaffold_users.html'
                
            })
            .state('app.scaffold.networks', {
                url: '/networks',
                templateUrl: 'tpl/scaffold/scaffold_networks.html'
                
            })
            .state('app.scaffold.networks_create', {
                url: '/networks/create',
                templateUrl: 'tpl/scaffold/scaffold_networks_create.html'
                
            })
            .state('app.scaffold.stations', {
                url: '/stations',
                templateUrl: 'tpl/scaffold/scaffold_stations.html'
                
            })
            .state('app.scaffold.stations_create', {
                url: '/stations/create',
                templateUrl: 'tpl/scaffold/scaffold_stations_create.html'
                
            })
            .state('app.scaffold.stations_edit', {
                url: '/stations/edit/{stationId}',
                templateUrl: 'tpl/scaffold/scaffold_stations_edit.html',
                controller: "ScaffoldEditStationsCtrl"
            })
            .state('app.scaffold.taxonomies', {
                url: '/taxonomies',
                templateUrl: 'tpl/scaffold/scaffold_taxonomies.html'
                
            })
            .state('app.scaffold.taxonomies_addterms', {
                url: '/taxonomies/:taxonomyId/terms',
                templateUrl: 'tpl/scaffold/scaffold_taxonomies_addterms.html'
                
            })
            .state('app.scaffold.taxonomies_create', {
                url: '/taxonomies/create',
                templateUrl: 'tpl/scaffold/scaffold_taxonomies_create.html'
                
            })
            .state('app.scaffold.perspectives', {
                url: '/perspectives',
                templateUrl: 'tpl/scaffold/scaffold_perspectives.html',
                controller: 'ScaffoldPerspectivesCtrl'
                
            })
            .state('app.scaffold.perspectives_create', {
                url: '/perspectives/create',
                templateUrl: 'tpl/scaffold/scaffold_perspectives_create.html'
                ,
            })
            .state('app.scaffold.perspectives_edit', {
                url: '/perspectives/edit/:perspectiveId',
                templateUrl: 'tpl/scaffold/scaffold_perspectives_create.html'
                ,
            })
            // statistics
            .state('app.statistics', {
                url: '/statistics',
                templateUrl: 'tpl/statistics/statistics_main.html'
                
            })
            // settings
            .state('app.scaffold.settings', {
                url: '/settings',
                templateUrl: 'tpl/app/app_settings.html',
                resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load('angularFileUpload')
                    }]
                }
            })
            .state('access', {
                url: '/access',
                template: '<div ui-view class="fade-in-right-big smooth"></div>'
            })
            .state('access.signup', {
                url: '/signup',
                templateUrl: 'tpl/signup.html',
                controller: 'SignupCtrl'
            })

            .state('access.404', {
                url: '/404',
                templateUrl: 'tpl/page_404.html'
            })

    }
  ]
)

.config(['$translateProvider', function($translateProvider){

  // Register a loader for the static files
  // So, the module will search missing translation tables under the specified urls.
  // Those urls are [prefix][langKey][suffix].
  $translateProvider.useStaticFilesLoader({
    prefix: 'l10n/',
    suffix: '.json'
  });

  // Tell the module what language to use by default
  $translateProvider.preferredLanguage('en');

  // Tell the module to store the language in the local storage
  $translateProvider.useLocalStorage();

}])

// oclazyload config
.config(function($ocLazyLoadProvider) {
    // We configure ocLazyLoad to use the lib script.js as the async loader
    $ocLazyLoadProvider.config({
        debug: false,
        events: true,
        modules: [
            {
                name: 'angularFileUpload',
                files: ['js/modules/angular-file-upload/angular-file-upload.js']
            }
        ]
    });
})

.config(function ($httpProvider) {
    //$httpProvider.interceptors.push('httpRequestInterceptor'); //httpRequestInterceptor factories.js
    $httpProvider.defaults.useXDomain = true; // used for cross domain requests
    $httpProvider.defaults.withCredentials = true; // used for cross domail requests
    delete $httpProvider.defaults.headers.common['X-Requested-With']; // remove headers
    $httpProvider.interceptors.push(function (){
        return {
            'request': function(config) {
                if(config.url.indexOf("tpl") > -1)
                    config.url = config.url + "?" + GLOBAL_URL_HASH;

                return config;
            }
        };
    });
  })

/**
 * jQuery plugin config use ui-jq directive , config the js and css files that required
 * key: function name of the jQuery plugin
 * value: array of the css js file located
 */
.constant('JQ_CONFIG', {
    easyPieChart:   ['js/jquery/charts/easypiechart/jquery.easy-pie-chart.js'],
    sparkline:      ['js/jquery/charts/sparkline/jquery.sparkline.min.js'],
    plot:           ['js/jquery/charts/flot/jquery.flot.min.js', 
                        'js/jquery/charts/flot/jquery.flot.resize.js',
                        'js/jquery/charts/flot/jquery.flot.tooltip.min.js',
                        'js/jquery/charts/flot/jquery.flot.spline.js',
                        'js/jquery/charts/flot/jquery.flot.orderBars.js',
                        'js/jquery/charts/flot/jquery.flot.pie.min.js'],
    slimScroll:     ['js/jquery/slimscroll/jquery.slimscroll.min.js'],
    //sortable:       ['js/jquery/sortable/jquery.sortable.js'],
    nestable:       ['js/jquery/nestable/jquery.nestable.js',
                        'js/jquery/nestable/nestable.css'],
    filestyle:      ['js/jquery/file/bootstrap-filestyle.min.js'],
    slider:         ['js/jquery/slider/bootstrap-slider.js',
                        'js/jquery/slider/slider.css'],
    chosen:         ['js/jquery/chosen/chosen.jquery.min.js',
                        'js/jquery/chosen/chosen.css'],
    TouchSpin:      ['js/jquery/spinner/jquery.bootstrap-touchspin.min.js',
                        'js/jquery/spinner/jquery.bootstrap-touchspin.css'],
    wysiwyg:        ['js/jquery/wysiwyg/bootstrap-wysiwyg.js',
                        'js/jquery/wysiwyg/jquery.hotkeys.js'],
    dataTable:      ['js/jquery/datatables/jquery.dataTables.min.js',
                        'js/jquery/datatables/dataTables.bootstrap.js',
                        'js/jquery/datatables/dataTables.bootstrap.css'],
    vectorMap:      ['js/jquery/jvectormap/jquery-jvectormap.min.js', 
                        'js/jquery/jvectormap/jquery-jvectormap-world-mill-en.js',
                        'js/jquery/jvectormap/jquery-jvectormap-us-aea-en.js',
                        'js/jquery/jvectormap/jquery-jvectormap.css'],
    footable:       ['js/jquery/footable/footable.all.min.js',
                        'js/jquery/footable/footable.core.css']
    }
)


.constant('MODULE_CONFIG', {
    select2:        ['js/jquery/select2/select2.css',
                        'js/jquery/select2/select2-bootstrap.css',
                        'js/jquery/select2/select2.min.js',
                        'js/modules/ui-select2.js']
    }
)

.constant('WORDRAILS', {
    baseUrl: location.protocol + '//' + location.host,
    pageSize: 15,
    commentPageSize: 5
})

.config(function(redactorOptions, WORDRAILS){
    redactorOptions.lang = 'pt_br';
    redactorOptions.imageUpload = WORDRAILS.baseUrl + "/api/files/contents/simple";
    redactorOptions.toolbarExternal = "#custom-toolbar"
    redactorOptions.plugins = ['fontsize', 'fontcolor', 'video', 'counter']
    redactorOptions.minHeight = 300
    redactorOptions.focus = true
    redactorOptions.buttons = ['html', 'formatting', 'bold', 'italic', 'underline', 'deleted', 
                                'fontsize', 'fontcolor', 'unorderedlist', 'orderedlist', 'outdent', 'indent',
                                'link', 'image', 'video', 'alignment', 'horizontalrule'];
})

.run(function($rootScope, $location, authService, connectionService, $state, $stateParams) {
    $rootScope.$state = $state;
    $rootScope.$stateParams = $stateParams; 

    moment.locale('pt');

    $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error){ 
        console.log(toState);
        console.log(fromState);
        console.error(event)
    });

    connectionService.init();

})
;