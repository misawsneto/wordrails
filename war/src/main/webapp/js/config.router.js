'use strict';

/**
 * Config for the router
 */
angular.module('app')
  .run(
    [          '$rootScope', '$state', '$stateParams', 
      function ($rootScope,   $state,   $stateParams) {
          $rootScope.$state = $state;
          $rootScope.$stateParams = $stateParams;
      }
    ]
  )
  .config(
    [          '$stateProvider', '$urlRouterProvider', '$locationProvider', 'JQ_CONFIG', 
      function ($stateProvider ,  $urlRouterProvider ,  $locationProvider , JQ_CONFIG) {
          
          $locationProvider.html5Mode({
            enabled: true,
          });

          $urlRouterProvider
              .otherwise('/');
          $stateProvider
              .state('app', {
                  abstract: true,
                  url: '',
                  templateUrl: 'tpl/layout.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                          return $ocLazyLoad.load(['720kb.socialshare', 'videosharing-embed', 'toaster', 'afkl.lazyImage', 'angularRipple', 'infinite-scroll']).then(function(){
                            // you might call this after your module initalization
                              angular.module('infinite-scroll').value('THROTTLE_MILLISECONDS', 1000)
                          });
                      }]
                  }
              })
              .state('app.stations', {
                  url: '/?stationId',
                  templateUrl: 'tpl/stations.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                          return $ocLazyLoad.load(['angular-carousel', 'slick', 'js/controllers/stations.js?' + GLOBAL_URL_HASH]);
                      }]
                  },
                  controller: 'StationsCtrl'
              })
              .state('app.post', {
                  url: '/post?id',
                  templateUrl: 'tpl/post.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                          return $ocLazyLoad.load([
                            // 'com.2fdevs.videogular', 
                            // 'com.2fdevs.videogular.plugins.controls', 
                            // 'com.2fdevs.videogular.plugins.overlayplay',
                            // 'com.2fdevs.videogular.plugins.poster',
                            // 'com.2fdevs.videogular.plugins.buffering',
                            // 'js/app/music/ctrl.js', 
                            // 'js/app/music/theme.css',
                            // '../bower_components/leaflet/dist/leaflet.js',
                            // '../bower_components/leaflet/dist/leaflet.css'
                          ]).then(function(){
                            return $ocLazyLoad.load(['ui.slimscroll', 'angularFileUpload','js/controllers/post.js?' + GLOBAL_URL_HASH])
                          });
                    }]
                  },
                  controller:'PostCtrl'
              })
              .state('app.search', {
                  url: '/search',
                  templateUrl: 'tpl/search.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['js/controllers/search.js?' + GLOBAL_URL_HASH]);
                    }]
                  },
                  controller:'SearchCtrl'
              })
              .state('app.tagspage', {
                  url: '/tag/{tagName}',
                  templateUrl: 'tpl/tags_page.html?' + GLOBAL_URL_HASH,
                  // use resolve to load other dependences
                   resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load([
                            'js/controllers/tags.js?' + GLOBAL_URL_HASH])
                      }]
                  },
                  controller: 'TagsPageCtrl'
              })
              .state('app.about', {
                  url: '/about',
                  templateUrl: 'tpl/about.html?' + GLOBAL_URL_HASH,
                  // use resolve to load other dependences
                   resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load([
                            'js/controllers/tags.js?' + GLOBAL_URL_HASH])
                      }]
                  },
                  controller: 'TagsPageCtrl'
              })
              .state('app.tagspage.read', {
                  url: '/:slug?invitation?redirect',
                  template: '',
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['js/controllers/read.js?' + GLOBAL_URL_HASH]);
                    }]
                  },
                  controller:'ReadCtrl'
              })
              .state('app.search.read', {
                  url: '/:slug',
                  template: '',
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['js/controllers/read.js?' + GLOBAL_URL_HASH]);
                    }]
                  },
                  controller:'ReadCtrl'
              })
              .state('app.user.read', {
                  url: '/:slug',
                  template: '',
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['js/controllers/read.js?' + GLOBAL_URL_HASH]);
                    }]
                  },
                  controller:'ReadCtrl'
              })
              .state('app.settings', {
                  abstract: true,
                  url: '/settings',
                  templateUrl: 'tpl/settings.html?' + GLOBAL_URL_HASH,
              })
              .state('app.settings.stations', {
                  url: '/stations',
                  templateUrl: 'tpl/settings-stations.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['angularFileUpload', 'js/controllers/settings-stations.js?' + GLOBAL_URL_HASH]);
                    }]
                  }
                  , controller:'SettingsStationsCtrl'
              })
              .state('app.settings.stationconfig', {
                  url: '/stationconfig?stationId?newStation',
                  templateUrl: 'tpl/settings-stations-config.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['angularFileUpload', 'js/controllers/settings-stations.js?' + GLOBAL_URL_HASH]);
                    }]
                  }
                  , controller:'SettingsStationsConfigCtrl'
              })
              .state('app.settings.stationstatistics', {
                  url: '/stationstatistics?stationId',
                  templateUrl: 'tpl/settings-stations-statistics.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['angularFileUpload', 'js/controllers/settings-stations.js?' + GLOBAL_URL_HASH]);
                    }]
                  }
                  , controller:'SettingsStationsStatisticsCtrl'
              })
              .state('app.settings.stationusers', {
                  url: '/stationusers?stationId?newUser?userId',
                  templateUrl: 'tpl/settings-stations-users.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['ng-mfb', 'angularFileUpload', 'js/controllers/settings-stations.js?' + GLOBAL_URL_HASH]);
                    }]
                  }
                  , controller:'SettingsStationsUsersCtrl'
              })
              .state('app.settings.stationcategories', {
                  url: '/stationcategories?stationId?newStation',
                  templateUrl: 'tpl/settings-categories.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['angularFileUpload', 'js/controllers/settings-stations.js?' + GLOBAL_URL_HASH]);
                    }]
                  }
                  , controller:'SettingsStationsCategoriesCtrl'
              })
              .state('app.settings.stationperspectives', {
                  url: '/stationperspectives?stationId?newStation',
                  templateUrl: 'tpl/settings-stations-perspectives.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['angularFileUpload', 'js/controllers/settings-stations.js?' + GLOBAL_URL_HASH]);
                    }]
                  }
                  , controller:'SettingsStationsPerspectivesCtrl'
              })
              .state('app.settings.perspectiveeditor', {
                  url: '/perspectiveeditor?stationId?perspectiveId',
                  templateUrl: 'tpl/settings-perspective-editor.html?' + GLOBAL_URL_HASH,
                  /*resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['angularFileUpload', 'js/controllers/settings-perspective.js?' + GLOBAL_URL_HASH]);
                    }]
                  }*/
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['../bower_components/modernizr/modernizr.js', '../bower_components/interact/interact.min.js'])
                        .then(function(){
                            return $ocLazyLoad.load(['angular-carousel','ui.slimscroll', 'ui.sortable','color-selector', 'js/controllers/settings-perspective.js?' + GLOBAL_URL_HASH])
                          });
                    }]
                  }
                  , controller:'SettingsPerspectiveEditorCtrl'
              })//app.settings.perspectives
              .state('app.settings.perspectives', {
                  url: '/perspectives?stationId',
                  templateUrl: 'tpl/settings-perspectives.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['js/controllers/settings-perspective.js?' + GLOBAL_URL_HASH]);
                    }]
                  }, controller:'SettingsPerspectiveListCtrl'
              })
              .state('app.settings.sponsors', {
                  url: '/sponsor',
                  templateUrl: 'tpl/settings-sponsors.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['angularFileUpload', 'js/controllers/settings-sponsors.js?' + GLOBAL_URL_HASH]);
                    }]
                  }
                  , controller:'SettingsSponsorsCtrl'
              })
              .state('app.settings.sponsorconfig', {
                  url: '/sponsorconfig?sponsorId?newSponsor',
                  templateUrl: 'tpl/settings-sponsors-config.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['angularFileUpload', 'js/controllers/settings-sponsors.js?' + GLOBAL_URL_HASH]);
                    }]
                  }
                  , controller:'SettingsSponsorsConfigCtrl'
              })
              .state('app.settings.users', {
                  url: '/users?userId?newUser',
                  templateUrl: 'tpl/settings-users.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['ng-mfb', 'angularFileUpload', 'angularFileUpload', 'js/controllers/settings-users.js?' + GLOBAL_URL_HASH]);
                    }]
                  }
                  , controller:'SettingsUsersCtrl'
              })
              .state('app.settings.categories', {
                  url: '/categories',
                  templateUrl: 'tpl/settings-categories.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['angularFileUpload', 'js/controllers/settings-categories.js?' + GLOBAL_URL_HASH]);
                    }]
                  }
                  , controller:'SettingsCategoriesCtrl'
              })
              // .state('app.settings.statistics', {
              //     url: '/statistics',
              //     templateUrl: 'tpl/settings-statistics.html?' + GLOBAL_URL_HASH,
              //     resolve: {
              //       deps: ['$ocLazyLoad',
              //         function( $ocLazyLoad ){
              //           return $ocLazyLoad.load(['angularFileUpload', 'js/controllers/settings-statistics.js?' + GLOBAL_URL_HASH]);
              //       }]
              //     }
              //     , controller:'SettingsStatisticsCtrl'
              // })
              .state('app.settings.colors', {
                  url: '/colors',
                  templateUrl: 'tpl/settings-colors.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['../bower_components/modernizr/modernizr.js', '../bower_components/interact/interact.min.js'])
                        .then(function(){
                            return $ocLazyLoad.load(['color-selector', 'colorpicker.module', 'js/controllers/settings-colors.js?' + GLOBAL_URL_HASH])
                          });
                    }]
                  }
                  , controller:'SettingsColorsCtrl'
              })
              .state('app.settings.network', {
                  url: '/network',
                  templateUrl: 'tpl/settings-network.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['angularFileUpload', 'js/controllers/settings-network.js?' + GLOBAL_URL_HASH]);
                    }]
                  }
                  , controller:'SettingsNetworkCtrl'
              })
              .state('app.bookmarks', {
                url: '/bookmarks',
                  templateUrl: 'tpl/bookmarks.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['js/controllers/bookmarks.js?' + GLOBAL_URL_HASH]);
                    }]
                  }
                  , controller:'BookmarksCtrl'
              })
              .state('app.notifications', {
                url: '/notifications',
                  templateUrl: 'tpl/notifications.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['js/controllers/notifications.js?' + GLOBAL_URL_HASH]);
                    }]
                  }
                  , controller:'NotificationsCtrl'
              })
              .state('app.bookmarks.read', {
                  url: '/:slug',
                  template: '',
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['js/controllers/read.js?' + GLOBAL_URL_HASH]);
                    }]
                  },
                  controller:'ReadCtrl'
              })
              .state('app.notifications.read', {
                  url: '/:slug',
                  template: '',
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['js/controllers/read.js?' + GLOBAL_URL_HASH]);
                    }]
                  },
                  controller:'ReadCtrl'
              })
              .state('app.userstats', {
                  url: '/mystats',
                  templateUrl: 'tpl/user_stats.html?' + GLOBAL_URL_HASH,
                  // use resolve to load other dependences
                   resolve: {
                      deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['../bower_components/d3/d3.min.js', '../bower_components/nvd3/build/nv.d3.min.css'])
                        .then(function(){
                            return $ocLazyLoad.load(['nvd3', 'js/controllers/user.js?' + GLOBAL_URL_HASH])
                          });
                      }]
                          
                  },
                  controller: 'UserStatsCtrl'
              })
              .state('app.settings.statistics', {
                  url: '/networkstats',
                  templateUrl: 'tpl/network_stats.html?' + GLOBAL_URL_HASH,
                  // use resolve to load other dependences
                   resolve: {
                      deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['../bower_components/d3/d3.min.js', '../bower_components/nvd3/build/nv.d3.min.css'])
                        .then(function(){
                            return $ocLazyLoad.load(['nvd3', 'js/controllers/settings-network.js?' + GLOBAL_URL_HASH])
                          });
                      }]
                          
                  },
                  controller: 'NetworkStatsCtrl'
              })
              .state('app.publications', {
                  url: '/publications/@:username?type',
                  templateUrl: 'tpl/user_publications.html?' + GLOBAL_URL_HASH,
                  // use resolve to load other dependences
                   resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load([
                            'js/controllers/user.js?' + GLOBAL_URL_HASH])
                      }]
                  },
                  reloadOnSearch: false,
                  controller: 'UserPublicationsCtrl'
              })
              .state('app.user', {
                  url: '/@:username',
                  templateUrl: 'tpl/user_profile.html?' + GLOBAL_URL_HASH,
                  // use resolve to load other dependences
                   resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load([
                            'angularFileUpload',
                            'js/controllers/user.js?' + GLOBAL_URL_HASH])
                      }]
                  },
                  controller: 'UserCtrl'
              })
              .state('app.publications.read', {
                  url: '/:slug',
                  template: '',
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['js/controllers/read.js?' + GLOBAL_URL_HASH]);
                    }]
                  },
                  controller:'ReadCtrl'
              })
              .state('app.read', {
                url: '/:slug',
                controller: 'ReadCtrl',
                templateUrl: 'tpl/read.html?' + GLOBAL_URL_HASH,
                resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['js/controllers/read.js?' + GLOBAL_URL_HASH]);
                    }]
                  },
              })
              /*.state('app.stations.read', {
                url: ':slug',
                controller: 'ReadCtrl',
                template: '',
                resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['js/controllers/read.js?' + GLOBAL_URL_HASH]);
                    }]
                  },
              })*/
              .state('app.dashboard-v1', {
                  url: '/dashboard-v1',
                  templateUrl: 'tpl/app_dashboard_v1.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['js/controllers/chart.js']);
                    }]
                  }
              })
              .state('app.dashboard-v2', {
                  url: '/dashboard-v2',
                  templateUrl: 'tpl/app_dashboard_v2.html?' + GLOBAL_URL_HASH,
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['js/controllers/chart.js']);
                    }]
                  }
              })
              .state('app.ui', {
                  url: '/ui',
                  template: '<div ui-view class="fade-in-up"></div>'
              })
              .state('app.ui.buttons', {
                  url: '/buttons',
                  templateUrl: 'tpl/ui_buttons.html?' + GLOBAL_URL_HASH
              })
              .state('app.ui.icons', {
                  url: '/icons',
                  templateUrl: 'tpl/ui_icons.html?' + GLOBAL_URL_HASH
              })
              .state('app.ui.grid', {
                  url: '/grid',
                  templateUrl: 'tpl/ui_grid.html?' + GLOBAL_URL_HASH
              })
              .state('app.ui.widgets', {
                  url: '/widgets',
                  templateUrl: 'tpl/ui_widgets.html?' + GLOBAL_URL_HASH
              })          
              .state('app.ui.bootstrap', {
                  url: '/bootstrap',
                  templateUrl: 'tpl/ui_bootstrap.html?' + GLOBAL_URL_HASH
              })
              .state('app.ui.sortable', {
                  url: '/sortable',
                  templateUrl: 'tpl/ui_sortable.html?' + GLOBAL_URL_HASH
              })
              .state('app.ui.scroll', {
                  url: '/scroll',
                  templateUrl: 'tpl/ui_scroll.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad){
                          return uiLoad.load('js/controllers/scroll.js');
                      }]
                  }
              })
              .state('app.ui.portlet', {
                  url: '/portlet',
                  templateUrl: 'tpl/ui_portlet.html?' + GLOBAL_URL_HASH
              })
              .state('app.ui.timeline', {
                  url: '/timeline',
                  templateUrl: 'tpl/ui_timeline.html?' + GLOBAL_URL_HASH
              })
              .state('app.ui.tree', {
                  url: '/tree',
                  templateUrl: 'tpl/ui_tree.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load('angularBootstrapNavTree').then(
                              function(){
                                 return $ocLazyLoad.load('js/controllers/tree.js');
                              }
                          );
                        }
                      ]
                  }
              })
              .state('app.ui.toaster', {
                  url: '/toaster',
                  templateUrl: 'tpl/ui_toaster.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad){
                          return $ocLazyLoad.load('toaster').then(
                              function(){
                                 return $ocLazyLoad.load('js/controllers/toaster.js');
                              }
                          );
                      }]
                  }
              })
              .state('app.ui.jvectormap', {
                  url: '/jvectormap',
                  templateUrl: 'tpl/ui_jvectormap.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad){
                          return $ocLazyLoad.load('js/controllers/vectormap.js');
                      }]
                  }
              })
              .state('app.ui.googlemap', {
                  url: '/googlemap',
                  templateUrl: 'tpl/ui_googlemap.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad ){
                          return uiLoad.load( [
                            'js/app/map/load-google-maps.js',
                            'js/app/map/ui-map.js',
                            'js/app/map/map.js'] ).then(
                              function(){
                                return loadGoogleMaps(); 
                              }
                            );
                      }]
                  }
              })
              .state('app.chart', {
                  url: '/chart',
                  templateUrl: 'tpl/ui_chart.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad){
                          return uiLoad.load('js/controllers/chart.js');
                      }]
                  }
              })
              // table
              .state('app.table', {
                  url: '/table',
                  template: '<div ui-view></div>'
              })
              .state('app.table.static', {
                  url: '/static',
                  templateUrl: 'tpl/table_static.html?' + GLOBAL_URL_HASH
              })
              .state('app.table.datatable', {
                  url: '/datatable',
                  templateUrl: 'tpl/table_datatable.html?' + GLOBAL_URL_HASH
              })
              .state('app.table.footable', {
                  url: '/footable',
                  templateUrl: 'tpl/table_footable.html?' + GLOBAL_URL_HASH
              })
              .state('app.table.grid', {
                  url: '/grid',
                  templateUrl: 'tpl/table_grid.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load('ngGrid').then(
                              function(){
                                  return $ocLazyLoad.load('js/controllers/grid.js');
                              }
                          );
                      }]
                  }
              })
              .state('app.table.uigrid', {
                  url: '/uigrid',
                  templateUrl: 'tpl/table_uigrid.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load('ui.grid').then(
                              function(){
                                  return $ocLazyLoad.load('js/controllers/uigrid.js');
                              }
                          );
                      }]
                  }
              })
              .state('app.table.editable', {
                  url: '/editable',
                  templateUrl: 'tpl/table_editable.html?' + GLOBAL_URL_HASH,
                  controller: 'XeditableCtrl',
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load('xeditable').then(
                              function(){
                                  return $ocLazyLoad.load('js/controllers/xeditable.js');
                              }
                          );
                      }]
                  }
              })
              .state('app.table.smart', {
                  url: '/smart',
                  templateUrl: 'tpl/table_smart.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load('smart-table').then(
                              function(){
                                  return $ocLazyLoad.load('js/controllers/table.js');
                              }
                          );
                      }]
                  }
              })
              // form
              .state('app.form', {
                  url: '/form',
                  template: '<div ui-view class="fade-in"></div>',
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad ){
                          return uiLoad.load('js/controllers/form.js');
                      }]
                  }
              })
              .state('app.form.components', {
                  url: '/components',
                  templateUrl: 'tpl/form_components.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['uiLoad', '$ocLazyLoad',
                        function( uiLoad, $ocLazyLoad ){
                          return uiLoad.load( JQ_CONFIG.daterangepicker )
                          .then(
                              function(){
                                return uiLoad.load('js/controllers/form.components.js');
                              }
                          ).then(
                              function(){
                                return $ocLazyLoad.load('ngBootstrap');
                              }
                          );
                        }
                      ]
                  }
              })
              .state('app.form.elements', {
                  url: '/elements',
                  templateUrl: 'tpl/form_elements.html?' + GLOBAL_URL_HASH
              })
              .state('app.form.validation', {
                  url: '/validation',
                  templateUrl: 'tpl/form_validation.html?' + GLOBAL_URL_HASH
              })
              .state('app.form.wizard', {
                  url: '/wizard',
                  templateUrl: 'tpl/form_wizard.html?' + GLOBAL_URL_HASH
              })
              .state('app.form.fileupload', {
                  url: '/fileupload',
                  templateUrl: 'tpl/form_fileupload.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad){
                          return $ocLazyLoad.load('angularFileUpload').then(
                              function(){
                                 return $ocLazyLoad.load('js/controllers/file-upload.js');
                              }
                          );
                      }]
                  }
              })
              .state('app.form.imagecrop', {
                  url: '/imagecrop',
                  templateUrl: 'tpl/form_imagecrop.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad){
                          return $ocLazyLoad.load('ngImgCrop').then(
                              function(){
                                 return $ocLazyLoad.load('js/controllers/imgcrop.js');
                              }
                          );
                      }]
                  }
              })
              .state('app.form.select', {
                  url: '/select',
                  templateUrl: 'tpl/form_select.html?' + GLOBAL_URL_HASH,
                  controller: 'SelectCtrl',
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load('ui.select').then(
                              function(){
                                  return $ocLazyLoad.load('js/controllers/select.js');
                              }
                          );
                      }]
                  }
              })
              .state('app.form.slider', {
                  url: '/slider',
                  templateUrl: 'tpl/form_slider.html?' + GLOBAL_URL_HASH,
                  controller: 'SliderCtrl',
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load('vr.directives.slider').then(
                              function(){
                                  return $ocLazyLoad.load('js/controllers/slider.js');
                              }
                          );
                      }]
                  }
              })
              .state('app.form.editor', {
                  url: '/editor',
                  templateUrl: 'tpl/form_editor.html?' + GLOBAL_URL_HASH,
                  controller: 'EditorCtrl',
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load('textAngular').then(
                              function(){
                                  return $ocLazyLoad.load('js/controllers/editor.js');
                              }
                          );
                      }]
                  }
              })
              .state('app.form.xeditable', {
                  url: '/xeditable',
                  templateUrl: 'tpl/form_xeditable.html?' + GLOBAL_URL_HASH,
                  controller: 'XeditableCtrl',
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load('xeditable').then(
                              function(){
                                  return $ocLazyLoad.load('js/controllers/xeditable.js');
                              }
                          );
                      }]
                  }
              })
              // pages
              .state('app.page', {
                  url: '/page',
                  template: '<div ui-view class="fade-in-down"></div>'
              })
              .state('app.page.profile', {
                  url: '/profile',
                  templateUrl: 'tpl/page_profile.html?' + GLOBAL_URL_HASH
              })
              .state('app.page.post', {
                  url: '/post',
                  templateUrl: 'tpl/page_post.html?' + GLOBAL_URL_HASH
              })
              .state('app.page.search', {
                  url: '/search',
                  templateUrl: 'tpl/page_search.html?' + GLOBAL_URL_HASH
              })
              .state('app.page.invoice', {
                  url: '/invoice',
                  templateUrl: 'tpl/page_invoice.html?' + GLOBAL_URL_HASH
              })
              .state('app.page.price', {
                  url: '/price',
                  templateUrl: 'tpl/page_price.html?' + GLOBAL_URL_HASH
              })
              .state('app.docs', {
                  url: '/docs',
                  templateUrl: 'tpl/docs.html?' + GLOBAL_URL_HASH
              })
              // others
              .state('lockme', {
                  url: '/lockme',
                  templateUrl: 'tpl/page_lockme.html?' + GLOBAL_URL_HASH
              })
              .state('access', {
                  url: '/access',
                  template: '<div ui-view class="fade-in-right-big smooth"></div>'
              })
              .state('access.signin', {
                  url: '/signin',
                  templateUrl: 'tpl/page_signin.html?' + GLOBAL_URL_HASH,
                  controller: 'AppCtrl'
              })
              .state('access.createnetwork', {
                  url: '/createnetwork',
                  templateUrl: 'tpl/network_create.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad ){
                          return uiLoad.load( ['js/controllers/settings.js'] );
                      }]
                  },
                  controller: 'SettingsCtrl'
              })
              .state('access.networkcreated', {
                  url: '/networkcreated?token',
                  template: '<div></div>',
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad ){
                          return uiLoad.load( ['js/controllers/settings.js'] );
                      }]
                  },
                  controller: 'NetworkCreatedCtrl'
              })
              .state('access.signup', {
                  url: '/signup',
                  templateUrl: 'tpl/page_signup.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad ){
                          return uiLoad.load( ['js/controllers/signup.js'] );
                      }]
                  }
              })
              .state('access.forgotpwd', {
                  url: '/forgotpwd',
                  templateUrl: 'tpl/page_forgotpwd.html?' + GLOBAL_URL_HASH
              })
              .state('access.404', {
                  url: '/404',
                  templateUrl: 'tpl/page_404.html?' + GLOBAL_URL_HASH
              })

              // fullCalendar
              .state('app.calendar', {
                  url: '/calendar',
                  templateUrl: 'tpl/app_calendar.html?' + GLOBAL_URL_HASH,
                  // use resolve to load other dependences
                  resolve: {
                      deps: ['$ocLazyLoad', 'uiLoad',
                        function( $ocLazyLoad, uiLoad ){
                          return uiLoad.load(
                            JQ_CONFIG.fullcalendar.concat('js/app/calendar/calendar.js')
                          ).then(
                            function(){
                              return $ocLazyLoad.load('ui.calendar');
                            }
                          )
                      }]
                  }
              })

              // mail
              .state('app.mail', {
                  abstract: true,
                  url: '/mail',
                  templateUrl: 'tpl/mail.html?' + GLOBAL_URL_HASH,
                  // use resolve to load other dependences
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad ){
                          return uiLoad.load( ['js/app/mail/mail.js',
                                               'js/app/mail/mail-service.js',
                                               JQ_CONFIG.moment] );
                      }]
                  }
              })
              .state('app.mail.list', {
                  url: '/inbox/{fold}',
                  templateUrl: 'tpl/mail.list.html?' + GLOBAL_URL_HASH
              })
              .state('app.mail.detail', {
                  url: '/{mailId:[0-9]{1,4}}',
                  templateUrl: 'tpl/mail.detail.html?' + GLOBAL_URL_HASH
              })
              .state('app.mail.compose', {
                  url: '/compose',
                  templateUrl: 'tpl/mail.new.html?' + GLOBAL_URL_HASH
              })

              .state('layout', {
                  abstract: true,
                  url: '/layout',
                  templateUrl: 'tpl/layout.html?' + GLOBAL_URL_HASH
              })
              .state('layout.fullwidth', {
                  url: '/fullwidth',
                  views: {
                      '': {
                          templateUrl: 'tpl/layout_fullwidth.html?' + GLOBAL_URL_HASH
                      },
                      'footer': {
                          templateUrl: 'tpl/layout_footer_fullwidth.html?' + GLOBAL_URL_HASH
                      }
                  },
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad ){
                          return uiLoad.load( ['js/controllers/vectormap.js'] );
                      }]
                  }
              })
              .state('layout.mobile', {
                  url: '/mobile',
                  views: {
                      '': {
                          templateUrl: 'tpl/layout_mobile.html?' + GLOBAL_URL_HASH
                      },
                      'footer': {
                          templateUrl: 'tpl/layout_footer_mobile.html?' + GLOBAL_URL_HASH
                      }
                  }
              })
              .state('layout.app', {
                  url: '/app',
                  views: {
                      '': {
                          templateUrl: 'tpl/layout_app.html?' + GLOBAL_URL_HASH
                      },
                      'footer': {
                          templateUrl: 'tpl/layout_footer_fullwidth.html?' + GLOBAL_URL_HASH
                      }
                  },
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad ){
                          return uiLoad.load( ['js/controllers/tab.js'] );
                      }]
                  }
              })
              .state('apps', {
                  abstract: true,
                  url: '/apps',
                  templateUrl: 'tpl/layout.html?' + GLOBAL_URL_HASH
              })
              .state('apps.note', {
                  url: '/note',
                  templateUrl: 'tpl/apps_note.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad ){
                          return uiLoad.load( ['js/app/note/note.js',
                                               JQ_CONFIG.moment] );
                      }]
                  }
              })
              .state('apps.contact', {
                  url: '/contact',
                  templateUrl: 'tpl/apps_contact.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad ){
                          return uiLoad.load( ['js/app/contact/contact.js'] );
                      }]
                  }
              })
              .state('app.weather', {
                  url: '/weather',
                  templateUrl: 'tpl/apps_weather.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load(
                              {
                                  name: 'angular-skycons',
                                  files: ['js/app/weather/skycons.js',
                                          'js/app/weather/angular-skycons.js',
                                          'js/app/weather/ctrl.js',
                                          JQ_CONFIG.moment ] 
                              }
                          );
                      }]
                  }
              })
              .state('app.todo', {
                  url: '/todo',
                  templateUrl: 'tpl/apps_todo.html?' + GLOBAL_URL_HASH,
                  resolve: {
                      deps: ['uiLoad',
                        function( uiLoad ){
                          return uiLoad.load( ['js/app/todo/todo.js',
                                               JQ_CONFIG.moment] );
                      }]
                  }
              })
              .state('app.todo.list', {
                  url: '/{fold}'
              })
              .state('music', {
                  url: '/music',
                  templateUrl: 'tpl/music.html?' + GLOBAL_URL_HASH,
                  controller: 'MusicCtrl',
                  resolve: {
                      deps: ['$ocLazyLoad',
                        function( $ocLazyLoad ){
                          return $ocLazyLoad.load([
                            'com.2fdevs.videogular', 
                            'com.2fdevs.videogular.plugins.controls', 
                            'com.2fdevs.videogular.plugins.overlayplay',
                            'com.2fdevs.videogular.plugins.poster',
                            'com.2fdevs.videogular.plugins.buffering',
                            'js/app/music/ctrl.js', 
                            'js/app/music/theme.css'
                          ]);
                      }]
                  }
              })
                .state('music.home', {
                    url: '/home',
                    templateUrl: 'tpl/music.home.html?' + GLOBAL_URL_HASH
                })
                .state('music.genres', {
                    url: '/genres',
                    templateUrl: 'tpl/music.genres.html?' + GLOBAL_URL_HASH
                })
                .state('music.detail', {
                    url: '/detail',
                    templateUrl: 'tpl/music.detail.html?' + GLOBAL_URL_HASH
                })
                .state('music.mtv', {
                    url: '/mtv',
                    templateUrl: 'tpl/music.mtv.html?' + GLOBAL_URL_HASH
                })
                .state('music.mtvdetail', {
                    url: '/mtvdetail',
                    templateUrl: 'tpl/music.mtv.detail.html?' + GLOBAL_URL_HASH
                })
                .state('music.playlist', {
                    url: '/playlist/{fold}',
                    templateUrl: 'tpl/music.playlist.html?' + GLOBAL_URL_HASH
                })
      }
    ]
  );
