'use strict';

/**
 * @ngdoc function
 * @name app.config:uiRouter
 * @description
 * # Config
 * Config for the router
 */
angular.module('app')
  .run(
    [           '$rootScope', '$state', '$stateParams',
      function ( $rootScope,   $state,   $stateParams ) {
        $rootScope.$state = $state;
        $rootScope.$stateParams = $stateParams;
      }
    ]
  )
  .config(
    [          '$stateProvider', '$urlRouterProvider', 'MODULE_CONFIG', '$translateProvider', '$locationProvider', '$authProvider',
      function ( $stateProvider,   $urlRouterProvider,  MODULE_CONFIG ,  $translateProvider ,  $locationProvider ,  $authProvider) {

        $locationProvider.html5Mode({
          enabled: true,
        });

        var layout='',aside='',content='';

        var path = location.pathname

        var isSettigns = path.slice(0, '/settings'.length) == '/settings';

        if(isSettigns){
          layout = '/views/layout.html?' + GLOBAL_URL_HASH;aside = '/views/aside.html?' + GLOBAL_URL_HASH;content= '/views/content.html?' + GLOBAL_URL_HASH;
        }else{
          layout = '/views/layout.h.html?' + GLOBAL_URL_HASH;aside = '/views/aside.h.html?' + GLOBAL_URL_HASH;content= '/views/content.h.html?' + GLOBAL_URL_HASH;
        }

        if(initData && initData.network && initData.network.facebookAppID){
          $authProvider.facebook({
            clientId: initData.network.facebookAppID,
            responseType: 'token'
          });
        }

        if(initData && initData.network && initData.network.googleAppID){
          $authProvider.google({
            clientId: initData.network.googleAppID,
            responseType: 'token'
          });
        }

        var stationDep = function($stateParams, trix){
          var stationObj = null;

          initData.stations.forEach(function(station){
            if(station.stationSlug == $stateParams.stationSlug)
              stationObj = station;
          })

          if(stationObj)
            return stationObj;
          else
            document.location.href = '/404';

           
          return stationObj;
        }

        var createSettingsRoutes = function(){
          $urlRouterProvider
          .otherwise('/settings/dashboard');

          $stateProvider
            .state('app', {
              abstract: true,
              resolve: {
                appData: function($stateParams, $q, trix){
                  var deferred = $q.defer();
                  if(initData && initData.person.id == 0){
                    document.location.href = '/access/signin?next=/settings';
                  }else{
                    deferred.resolve(initData);
                  }
                  return deferred.promise;
                },
                deps:load( ['/styles/home.css?' + GLOBAL_URL_HASH, '720kb.socialshare', 'infinite-scroll', 'angularFileUpload', '/scripts/services/trix.js?' + GLOBAL_URL_HASH , '/libs/theming/tinycolor/tinycolor.js?' + GLOBAL_URL_HASH , 'mdPickers', 'afkl.lazyImage', 'angularMoment', 'ui.materialize', 'perfect_scrollbar', 'monospaced.elastic'] ).deps
              },
              url: '/settings',
              views: {
                '': {
                  templateUrl: layout
                },
                'aside': {
                  templateUrl: aside
                },
                'content': {
                  templateUrl: content,
                  controller: 'AppDataCtrl'
                }
              }
            })

              .state('app.post', {
                url: '/post?id',
                templateUrl: '/views/settings/settings-post.html?' + GLOBAL_URL_HASH,
                data : { titleTranslate: 'titles.POST', title: 'Publicação', folded: true },
                resolve: load([
                  'com.2fdevs.videogular','com.2fdevs.videogular.plugins.controls','com.2fdevs.videogular.plugins.overlayplay','com.2fdevs.videogular.plugins.poster',
                  /*'recorderServiceProvider', 'angularAudioRecorder',*/'videosharing-embed','/libs/angular/lifely-focuspoint/dist/focuspoint.css?' + GLOBAL_URL_HASH, 'leaflet-directive', 'ngJcrop', 'froala', 'monospaced.elastic', 'angularFileUpload', '/scripts/controllers/settings/settings-post.js?' + GLOBAL_URL_HASH, '/scripts/controllers/settings/settings-post-geolocation.js?' + GLOBAL_URL_HASH ]),
                controller: 'SettingsPostCtrl'
              })

              .state('app.preview', {
                url: '/preview?id',
                templateUrl: '/views/pages/read.html?' + GLOBAL_URL_HASH,
                data : { titleTranslate: 'titles.POST', title: 'Publicação', folded: true },
                resolve: {
                  post: function($stateParams, $q, trix){
                    var deferred = $q.defer();
                    if($stateParams.id){
                      trix.getPost($stateParams.id, 'postProjection').success(function(post){
                        deferred.resolve(post);
                      }).error(function(){
                        return deferred.reject('Error loadin post');
                      })
                    }
                    return deferred.promise;
                  },
                  station: function($stateParams, $q, trix){
                    var deferred = $q.defer();
                    deferred.resolve(null);
                    return deferred.promise;
                  },
                  deps:load(['wu.masonry', '/scripts/controllers/app/read.js?' + GLOBAL_URL_HASH , '/libs/angular/froala-wysiwyg-editor/css/froala_style.min.css?' + GLOBAL_URL_HASH, 'videosharing-embed']).deps
                },
                controller: 'ReadCtrl'
              })

              .state('app.stations', {
                url: '/stations',
                templateUrl: '/views/settings/settings-stations.html?' + GLOBAL_URL_HASH,
                data : { titleTranslate: 'titles.STATIONS', title: 'Estações', folded: false },
                resolve: load(['/scripts/controllers/settings/settings-stations.js?' + GLOBAL_URL_HASH ]),
                controller: 'SettingsStationsCtrl'
              })

              .state('app.categories', {
                url: '/{stationSlug}/categories',
                templateUrl: '/views/settings/settings-categories.html?' + GLOBAL_URL_HASH,
                data : { titleTranslate: 'titles.CATEGORIES', title: 'Categorias', folded: false },
                resolve:{
                  station: stationDep,
                  deps:load(['/scripts/controllers/settings/settings-categories.js?' + GLOBAL_URL_HASH ,'angularSpectrumColorpicker']).deps
                },
                controller: 'SettingsCategoriesCtrl'
              })

              .state('app.perspectives', {
                url: '/{stationSlug}/perspectives',
                templateUrl: '/views/settings/settings-perspectives.html?' + GLOBAL_URL_HASH,
                data : { titleTranslate: 'titles.PERSPECTIVES', title: 'Perspectives', folded: true },
                resolve:{
                  station: stationDep,
                  deps: load(['wu.masonry',  'dndLists', 'angular-carousel', '/scripts/controllers/settings/settings-perspectives.js?' + GLOBAL_URL_HASH , '/scripts/custom-pgwslider.js?' + GLOBAL_URL_HASH , '/libs/jquery/pgwslider/pgwslider.min.css?' + GLOBAL_URL_HASH]).deps
                },
                controller: 'SettingsPerspectivesCtrl'
              })
              .state('app.permissions', {
                url: '/{stationSlug}/permissions',
                templateUrl: '/views/settings/settings-station-permissions.html?' + GLOBAL_URL_HASH,
                data : { titleTranslate: 'titles.PERMISSIONS', title: 'Permissions', folded: false },
                resolve:{
                  station: stationDep, 
                  deps: load(['/scripts/controllers/settings/settings-station-permissions.js?' + GLOBAL_URL_HASH ]).deps
                },
                controller: 'SettingsStationPermissionsCtrl'
              })

              .state('app.users', {
                  url: '/users?username?newUser',
                  templateUrl: '/views/settings/settings-users.html?',
                  data : { titleTranslate: 'titles.USERS', title: 'Usuários', folded: false },
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['/scripts/controllers/settings/settings-users.js?' + GLOBAL_URL_HASH + '?', 'froala']);
                    }]
                  },
                  controller:'SettingsUsersCtrl'
              })
              .state('app.profile', {
                url: '/profile',
                templateUrl: '/views/settings/settings-profile.html?' + GLOBAL_URL_HASH,
                data : { titleTranslate: 'settings.aside.MY_PROFILE', title: 'Perfil', folded: true },
                resolve: load(['/scripts/controllers/settings/settings-profile.js?' + GLOBAL_URL_HASH , 'ngJcrop']),
                controller:'SettingsProfileCtrl'
              })

              // chart
              .state('app.chart', {
                url: '/chart',
                templateUrl: '/views/ui/chart/chart.html?' + GLOBAL_URL_HASH,
                data : { title: 'Charts' },
                resolve: load('/scripts/controllers/chart.js?' + GLOBAL_URL_HASH )
              })
              .state('app.network', {
                url: '/network',
                templateUrl: '/views/settings/settings-network.html?' + GLOBAL_URL_HASH,
                data : { titleTranslate: 'titles.NETWORK', title: 'Rede', folded: false },
                resolve: load(['/scripts/controllers/settings/settings-network.js?' + GLOBAL_URL_HASH ]),
                controller: 'SettingsNetworkCtrl'
              })
              .state('app.publications', {
                url: '/publications?tab',
                reloadOnSearch: false,
                templateUrl: '/views/settings/settings-publications.html?' + GLOBAL_URL_HASH,
                data : { titleTranslate: 'titles.PUBLICATIONS', title: 'Publicações', folded: true },
                // resolve: load(['/scripts/controllers/settings/settings-publications.js?' + GLOBAL_URL_HASH ]),
                resolve: {
                  deps: ['$ocLazyLoad', '$templateCache',
                    function( $ocLazyLoad, $templateCache ){
                      return $ocLazyLoad.load(['/scripts/controllers/settings/settings-publications.js?' + GLOBAL_URL_HASH ])
                  }]
                },
                controller: 'SettingsPublicationsCtrl'
              })
              .state('app.comments', {
                url: '/comments',
                reloadOnSearch: false,
                templateUrl: '/views/settings/settings-comments.html?' + GLOBAL_URL_HASH,
                data : { titleTranslate: 'titles.COMMENTS', title: 'Comentários', folded: false },
                // resolve: load(['/scripts/controllers/settings/settings-publications.js?' + GLOBAL_URL_HASH ]),
                resolve: {
                  deps: ['$ocLazyLoad', '$templateCache',
                    function( $ocLazyLoad, $templateCache ){
                      return $ocLazyLoad.load(['/scripts/controllers/settings/settings-comments.js?' + GLOBAL_URL_HASH ])
                  }]
                },
                controller: 'SettingsCommentsCtrl'
              })
              .state('app.media-library', {
                url: '/media',
                reloadOnSearch: false,
                templateUrl: '/views/settings/settings-media-library.html?' + GLOBAL_URL_HASH,
                data : { titleTranslate: 'titles.COMMENTS', title: 'Comentários', folded: false },
                // resolve: load(['/scripts/controllers/settings/settings-publications.js?' + GLOBAL_URL_HASH ]),
                resolve: {
                  deps: ['$ocLazyLoad', '$templateCache',
                    function( $ocLazyLoad, $templateCache ){
                      return $ocLazyLoad.load(['/scripts/controllers/settings/settings-media-library.js?' + GLOBAL_URL_HASH ])
                  }]
                },
                controller: 'SettingsMediaLibraryCtrl'
              })
              .state('app.colors', {
                url: '/colors',
                templateUrl: '/views/settings/settings-colors.html?' + GLOBAL_URL_HASH,
                data : { titleTranslate: 'titles.THEMING', title: 'Aparência', folded: false },
                resolve: load(['/scripts/controllers/settings/settings-network.js?' + GLOBAL_URL_HASH , 'angularSpectrumColorpicker', '/scripts/controllers/color-generator.js?' + GLOBAL_URL_HASH ,
                    '/styles/theming.css?' + GLOBAL_URL_HASH, '/libs/jquery/slimScroll/jquery.slimscroll.min.js?' + GLOBAL_URL_HASH ]),
                controller: 'ColorGeneratorCtrl'
              })
              .state('app.dashboard', {
                url: '/dashboard',
                templateUrl: '/views/pages/dashboard.html?' + GLOBAL_URL_HASH,
                data : { title: 'Dashboard', folded: false },
                resolve: {
                  dashboard: function(){
                    var isColaboratorOnly = true
                    initData.permissions.stationPermissions.forEach(function(perm){
                      if(perm.write && isColaboratorOnly)
                        isColaboratorOnly = false;
                    })
                    if(isColaboratorOnly && path !== '/settings/profile')
                      document.location.href = '/settings/profile';
                    return true
                  },
                  deps: load(['/scripts/controllers/settings/settings-dashboard.js?' + GLOBAL_URL_HASH ]).deps
                },
                controller: 'DashboardCtrl'
              })
              .state('app.pagebuilder', {
                url: '/pagebuilder',
                templateUrl: '/views/settings/settings-pagebuilder.html?' + GLOBAL_URL_HASH,
                data : { title: 'Page Builder', folded: true },
                resolve: load(['wu.masonry', 'ui.ace', 'digitalfondue.dftabmenu'])
              })
              .state('app.pagebuilder.list', {
                url: '/list',
                templateUrl: '/views/settings/settings-pagebuilder-list.html?' + GLOBAL_URL_HASH,
                data : { title: 'Page Builder', folded: true },
                resolve: load(['/scripts/controllers/settings/settings-pagebuilder-list.js?' + GLOBAL_URL_HASH ]),
                controller: 'PageBuilderListCtrl'
              })
              .state('app.pagebuilder.editor', {
                url: '/editor',
                templateUrl: '/views/settings/settings-pagebuilder-editor.html?' + GLOBAL_URL_HASH,
                data : { title: 'Page Builder', folded: true },
                resolve: load(['/scripts/controllers/settings/settings-pagebuilder-editor.js?' + GLOBAL_URL_HASH ]),
                controller: 'PageBuilderEditorCtrl'
              })
              .state('app.pagebuilder.stationcolors', {
                url: '/stationcolors',
                templateUrl: '/views/settings/settings-colors.html?' + GLOBAL_URL_HASH,
                data : { title: 'Page Builder', folded: true },
                  resolve: load(['/scripts/controllers/settings/settings-network.js?' + GLOBAL_URL_HASH , 'angularSpectrumColorpicker', '/scripts/controllers/color-generator.js?' + GLOBAL_URL_HASH ,
                    '/styles/theming.css?' + GLOBAL_URL_HASH, '/libs/jquery/slimScroll/jquery.slimscroll.min.js?' + GLOBAL_URL_HASH ]),
                controller: 'ColorGeneratorCtrl'
              })
              .state('app.pagebuilder.header', {
                url: '/header',
                templateUrl: '/views/settings/settings-pagebuilder-header.html?' + GLOBAL_URL_HASH,
                data : { title: 'Header', folded: true },
                resolve: load(['/scripts/controllers/settings/settings-pagebuilder-menus.js?' + GLOBAL_URL_HASH ]),
                controller: 'PageBuilderHeaderCtrl'
              })
              .state('app.pagebuilder.sidemenu', {
                url: '/header',
                templateUrl: '/views/settings/settings-pagebuilder-sidemenu.html?' + GLOBAL_URL_HASH,
                data : { title: 'Sidemenu', folded: true },
                resolve: load(['/scripts/controllers/settings/settings-pagebuilder-menus.js?' + GLOBAL_URL_HASH ]),
                controller: 'PageBuilderSidemenuCtrl'
              })
              .state('app.pagebuilder.footer', {
                url: '/header',
                templateUrl: '/views/settings/settings-pagebuilder-footer.html?' + GLOBAL_URL_HASH,
                data : { title: 'Page Builder', folded: true },
                resolve: load(['/scripts/controllers/settings/settings-pagebuilder-menus.js?' + GLOBAL_URL_HASH ]),
                controller: 'PageBuilderFooterCtrl'
              })
              .state('app.analysis', {
                url: '/analysis',
                templateUrl: '/views/pages/dashboard.analysis.html?' + GLOBAL_URL_HASH,
                data : { title: 'Analysis' },
                resolve: load(['/scripts/controllers/chart.js?' + GLOBAL_URL_HASH ,'/scripts/controllers/vectormap.js?' + GLOBAL_URL_HASH ])
              })
              .state('app.wall', {
                url: '/wall',
                templateUrl: '/views/pages/dashboard.wall.html?' + GLOBAL_URL_HASH,
                data : { title: 'Wall', folded: false }
              })
              .state('app.todo', {
                url: '/todo',
                templateUrl: '/apps/todo/todo.html?' + GLOBAL_URL_HASH,
                data : { title: 'Todo', theme: { primary: 'indigo-800'} },
                controller: 'TodoCtrl',
                resolve: load('/apps/todo/todo.js?' + GLOBAL_URL_HASH )
              })
              .state('app.todo.list', {
                  url: '/{fold}'
              })
              .state('app.note', {
                url: '/note',
                templateUrl: '/apps/note/main.html?' + GLOBAL_URL_HASH,
                data : { theme: { primary: 'blue-grey'} }
              })
              .state('app.note.list', {
                url: '/list',
                templateUrl: '/apps/note/list.html?' + GLOBAL_URL_HASH,
                data : { title: 'Note'},
                controller: 'NoteCtrl',
                resolve: load(['/apps/note/note.js?' + GLOBAL_URL_HASH , '/libs/jquery/moment/min/moment-with-locales.min.js?' + GLOBAL_URL_HASH ])
              })
              .state('app.note.item', {
                url: '/{id}',
                views: {
                  '': {
                    templateUrl: '/apps/note/item.html?' + GLOBAL_URL_HASH,
                    controller: 'NoteItemCtrl',
                    resolve: load(['/apps/note/note.js?' + GLOBAL_URL_HASH , '/libs/jquery/moment/min/moment-with-locales.min.js?' + GLOBAL_URL_HASH ])
                  },
                  'navbar@': {
                    templateUrl: '/apps/note/navbar.html?' + GLOBAL_URL_HASH,
                    controller: 'NoteItemCtrl'
                  }
                },
                data : { title: '', child: true }
              })
              .state('app.inbox', {
                  url: '/inbox',
                  templateUrl: '/apps/inbox/inbox.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Inbox', folded: false },
                  resolve: load( ['/apps/inbox/inbox.js?' + GLOBAL_URL_HASH ,'/libs/jquery/moment/min/moment-with-locales.min.js?' + GLOBAL_URL_HASH ] )
              })
              .state('app.inbox.list', {
                  url: '/inbox/{fold}',
                  templateUrl: '/apps/inbox/list.html?' + GLOBAL_URL_HASH
              })
              .state('app.inbox.detail', {
                  url: '/{id:[0-9]{1,4}}',
                  templateUrl: '/apps/inbox/detail.html?' + GLOBAL_URL_HASH
              })
              .state('app.inbox.compose', {
                  url: '/compose',
                  templateUrl: '/apps/inbox/new.html?' + GLOBAL_URL_HASH,
                  resolve: load( ['textAngular', 'ui.select'] )
              })
            .state('ui', {
              url: '/ui',
              abstract: true,
              views: {
                '': {
                  templateUrl: layout
                },
                'aside': {
                  templateUrl: aside
                },
                'content': {
                  templateUrl: content
                }
              }
            })
              // components router
              .state('ui.component', {
                url: '/component',
                abstract: true,
                template: '<div ui-view></div>'
              })
                .state('ui.component.arrow', {
                  url: '/arrow',
                  templateUrl: '/views/ui/component/arrow.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Arrows' }
                })
                .state('ui.component.badge-label', {
                  url: '/badge-label',
                  templateUrl: '/views/ui/component/badge-label.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Badges & Labels' }
                })
                .state('ui.component.button', {
                  url: '/button',
                  templateUrl: '/views/ui/component/button.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Buttons' }
                })
                .state('ui.component.color', {
                  url: '/color',
                  templateUrl: '/views/ui/component/color.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Colors' }
                })
                .state('ui.component.grid', {
                  url: '/grid',
                  templateUrl: '/views/ui/component/grid.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Grids' }
                })
                .state('ui.component.icon', {
                  url: '/icons',
                  templateUrl: '/views/ui/component/icon.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Icons' }
                })
                .state('ui.component.list', {
                  url: '/list',
                  templateUrl: '/views/ui/component/list.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Lists' }
                })
                .state('ui.component.nav', {
                  url: '/nav',
                  templateUrl: '/views/ui/component/nav.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Navs' }
                })
                .state('ui.component.progressbar', {
                  url: '/progressbar',
                  templateUrl: '/views/ui/component/progressbar.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Progressbars' }
                })
                .state('ui.component.streamline', {
                  url: '/streamline',
                  templateUrl: '/views/ui/component/streamline.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Streamlines' }
                })
                .state('ui.component.timeline', {
                  url: '/timeline',
                  templateUrl: '/views/ui/component/timeline.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Timelines' }
                })
                .state('ui.component.uibootstrap', {
                  url: '/uibootstrap',
                  templateUrl: '/views/ui/component/uibootstrap.html?' + GLOBAL_URL_HASH,
                  resolve: load('/scripts/controllers/bootstrap.js?' + GLOBAL_URL_HASH ),
                  data : { title: 'UI Bootstrap' }
                })
              // material routers
              .state('ui.material', {
                url: '/material',
                template: '<div ui-view></div>',
                resolve: load('/scripts/controllers/material.js?' + GLOBAL_URL_HASH )
              })
                .state('ui.material.button', {
                  url: '/button',
                  templateUrl: '/views/ui/material/button.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Buttons' }
                })
                .state('ui.material.color', {
                  url: '/color',
                  templateUrl: '/views/ui/material/color.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Colors' }
                })
                .state('ui.material.icon', {
                  url: '/icon',
                  templateUrl: '/views/ui/material/icon.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Icons' }
                })
                .state('ui.material.card', {
                  url: '/card',
                  templateUrl: '/views/ui/material/card.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Card' }
                })
                .state('ui.material.form', {
                  url: '/form',
                  templateUrl: '/views/ui/material/form.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Form' }
                })
                .state('ui.material.list', {
                  url: '/list',
                  templateUrl: '/views/ui/material/list.html?' + GLOBAL_URL_HASH,
                  data : { title: 'List' }
                })
                .state('ui.material.ngmaterial', {
                  url: '/ngmaterial',
                  templateUrl: '/views/ui/material/ngmaterial.html?' + GLOBAL_URL_HASH,
                  data : { title: 'NG Material' }
                })
              // form routers
              .state('ui.form', {
                url: '/form',
                template: '<div ui-view></div>'
              })
                .state('ui.form.layout', {
                  url: '/layout',
                  templateUrl: '/views/ui/form/layout.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Layouts' }
                })
                .state('ui.form.element', {
                  url: '/element',
                  templateUrl: '/views/ui/form/element.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Elements' }
                })              
                .state('ui.form.validation', {
                  url: '/validation',
                  templateUrl: '/views/ui/form/validation.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Validations' }
                })
                .state('ui.form.select', {
                  url: '/select',
                  templateUrl: '/views/ui/form/select.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Selects' },
                  controller: 'SelectCtrl',
                  resolve: load(['ui.select','/scripts/controllers/select.js?' + GLOBAL_URL_HASH ])
                })
                .state('ui.form.editor', {
                  url: '/editor',
                  templateUrl: '/views/ui/form/editor.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Editor' },
                  controller: 'EditorCtrl',
                  resolve: load(['textAngular','/scripts/controllers/editor.js?' + GLOBAL_URL_HASH ])
                })
                .state('ui.form.slider', {
                  url: '/slider',
                  templateUrl: '/views/ui/form/slider.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Slider' },
                  controller: 'SliderCtrl',
                  resolve: load('/scripts/controllers/slider.js?' + GLOBAL_URL_HASH )
                })
                .state('ui.form.tree', {
                  url: '/tree',
                  templateUrl: '/views/ui/form/tree.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Tree' },
                  controller: 'TreeCtrl',
                  resolve: load('/scripts/controllers/tree.js?' + GLOBAL_URL_HASH )
                })
                .state('ui.form.file-upload', {
                  url: '/file-upload',
                  templateUrl: '/views/ui/form/file-upload.html?' + GLOBAL_URL_HASH,
                  data : { title: 'File upload' },
                  controller: 'UploadCtrl',
                  resolve: load(['/scripts/controllers/upload.js?' + GLOBAL_URL_HASH ])
                })
                .state('ui.form.image-crop', {
                  url: '/image-crop',
                  templateUrl: '/views/ui/form/image-crop.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Image Crop' },
                  controller: 'ImgCropCtrl',
                  resolve: load(['ngImgCrop','/scripts/controllers/imgcrop.js?' + GLOBAL_URL_HASH ])
                })
                .state('ui.form.editable', {
                  url: '/editable',
                  templateUrl: '/views/ui/form/xeditable.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Xeditable' },
                  controller: 'XeditableCtrl',
                  resolve: load(['xeditable','/scripts/controllers/xeditable.js?' + GLOBAL_URL_HASH ])
                })
              // table routers
              .state('ui.table', {
                url: '/table',
                template: '<div ui-view></div>'
              })
                .state('ui.table.static', {
                  url: '/static',
                  templateUrl: '/views/ui/table/static.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Static', theme: { primary: 'blue'} }
                })
                .state('ui.table.smart', {
                  url: '/smart',
                  templateUrl: '/views/ui/table/smart.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Smart' },
                  controller: 'TableCtrl',
                  resolve: load(['smart-table', '/scripts/controllers/table.js?' + GLOBAL_URL_HASH ])
                })
                .state('ui.table.datatable', {
                  url: '/datatable',
                  data : { title: 'Datatable' },
                  templateUrl: '/views/ui/table/datatable.html?' + GLOBAL_URL_HASH
                })
                .state('ui.table.footable', {
                  url: '/footable',
                  data : { title: 'Footable' },
                  templateUrl: '/views/ui/table/footable.html?' + GLOBAL_URL_HASH
                })
                .state('ui.table.nggrid', {
                  url: '/nggrid',
                  templateUrl: '/views/ui/table/nggrid.html?' + GLOBAL_URL_HASH,
                  data : { title: 'NG Grid' },
                  controller: 'NGGridCtrl',
                  resolve: load(['ngGrid','/scripts/controllers/nggrid.js?' + GLOBAL_URL_HASH ])
                })
                .state('ui.table.uigrid', {
                  url: '/uigrid',
                  templateUrl: '/views/ui/table/uigrid.html?' + GLOBAL_URL_HASH,
                  data : { title: 'UI Grid' },
                  controller: "UiGridCtrl",
                  resolve: load(['ui.grid', '/scripts/controllers/uigrid.js?' + GLOBAL_URL_HASH ])
                })
                .state('ui.table.editable', {
                  url: '/editable',
                  templateUrl: '/views/ui/table/editable.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Editable' },
                  controller: 'XeditableCtrl',
                  resolve: load(['xeditable','/scripts/controllers/xeditable.js?' + GLOBAL_URL_HASH ])
                })
              // chart
              .state('ui.chart', {
                url: '/chart',
                templateUrl: '/views/ui/chart/chart.html?' + GLOBAL_URL_HASH,
                data : { title: 'Charts' },
                resolve: load('/scripts/controllers/chart.js?' + GLOBAL_URL_HASH )
              })
              // map routers
              .state('ui.map', {
                url: '/map',
                template: '<div ui-view></div>'
              })
                .state('ui.map.google', {
                  url: '/google',
                  templateUrl: '/views/ui/map/google.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Gmap' },
                  controller: 'GoogleMapCtrl',
                  resolve: load(['ui.map', '/scripts/controllers/load-google-maps.js?' + GLOBAL_URL_HASH , '/scripts/controllers/googlemap.js?' + GLOBAL_URL_HASH ], function(){ return loadGoogleMaps(); })
                })
                .state('ui.map.vector', {
                  url: '/vector',
                  templateUrl: '/views/ui/map/vector.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Vector' },
                  controller: 'VectorMapCtrl',
                  resolve: load('/scripts/controllers/vectormap.js?' + GLOBAL_URL_HASH )
                })

            .state('page', {
              url: '/page',
              views: {
                '': {
                  templateUrl: layout
                },
                'aside': {
                  templateUrl: aside
                },
                'content': {
                  templateUrl: content
                }
              }
            })
              
              .state('page.settings', {
                url: '/settings',
                templateUrl: '/views/pages/settings.html?' + GLOBAL_URL_HASH,
                data : { title: 'Settings' }
              })
              .state('page.blank', {
                url: '/blank',
                templateUrl: '/views/pages/blank.html?' + GLOBAL_URL_HASH,
                data : { title: 'Blank' }
              })
              .state('page.document', {
                url: '/document',
                templateUrl: '/views/pages/document.html?' + GLOBAL_URL_HASH,
                data : { title: 'Document' }
              })
              .state('404', {
                url: '/404',
                templateUrl: '/views/pages/404.html?' + GLOBAL_URL_HASH
              })
              .state('505', {
                url: '/505',
                templateUrl: '/views/pages/505.html?' + GLOBAL_URL_HASH
              })
        }

        var createHomeRoutes = function(){

          $urlRouterProvider
          .otherwise('/');

          $stateProvider
            .state('app', {
              abstract: true,
              resolve: {
                appData: function($stateParams, $q, trix){
                  var deferred = $q.defer();
                  // if(initData.person.id == 0){
                  //   document.location.href = '/';
                  // }else{
                    deferred.resolve(initData);
                  // }
                  return deferred.promise;
                },
                //deps:load( ['digitalfondue.dftabmenu','720kb.socialshare','monospaced.elastic','angularFileUpload','infinite-scroll', '/scripts/services/trix.js?' + GLOBAL_URL_HASH , '/libs/theming/tinycolor/tinycolor.js?' + GLOBAL_URL_HASH , 'mdPickers', 'afkl.lazyImage', 'angularMoment', 'ui.materialize','perfect_scrollbar'] ).deps
                deps:load( ['/scripts/home.all.js?' + GLOBAL_URL_HASH , '/styles/home.all.min.css?' + GLOBAL_URL_HASH, 'angularFileUpload'] ).deps
              },
              url: '',
              views: {
                '': {
                  templateUrl: layout
                },
                'aside': {
                  templateUrl: aside
                },
                'content': {
                  templateUrl: content,
                  controller: 'AppDataCtrl'
                }
              }
            })
              .state('app.dashboard', {
                url: '/dashboard',
                templateUrl: '/views/pages/dashboard.html?' + GLOBAL_URL_HASH,
                data : { title: 'Dashboard', folded: false },
                resolve: load(['/scripts/controllers/chart.js?' + GLOBAL_URL_HASH ,'/scripts/controllers/vectormap.js?' + GLOBAL_URL_HASH ])
              })
              .state('app.analysis', {
                url: '/analysis',
                templateUrl: '/views/pages/dashboard.analysis.html?' + GLOBAL_URL_HASH,
                data : { title: 'Analysis' },
                resolve: load(['/scripts/controllers/chart.js?' + GLOBAL_URL_HASH ,'/scripts/controllers/vectormap.js?' + GLOBAL_URL_HASH ])
              })
              .state('app.home', {
                url: '/',
                templateUrl: '/views/pages/home.html?' + GLOBAL_URL_HASH,
                data : { title: 'Home', folded: false },
                resolve: {
                  station: function(){return null;},
                  deps:load(['/scripts/controllers/app/page.js?' + GLOBAL_URL_HASH , '/scripts/custom-pgwslider.js?' + GLOBAL_URL_HASH , '/libs/jquery/pgwslider/pgwslider.min.css?' + GLOBAL_URL_HASH, 'angular-carousel']).deps
                },
                controller: 'PageCtrl'
              })
              .state('app.station', {
                url: '/{stationSlug}',
                abstract: true,
                template: '<div ui-view></div>',
                data : { title: 'Home', folded: false },
                controller: 'StationCtrl',
                resolve: {
                  termPerspectiveView: function($stateParams, trix, $q){
                    var deferred = $q.defer()
                    var station = stationDep($stateParams);
                    trix.findPerspectiveView(station.defaultPerspectiveId, null, null, 0, 10).success(function(termPerspective){
                      termPerspective.station = station;
                      deferred.resolve(termPerspective);
                    }).error(function(){
                      document.location.href = '/404';
                    })          
                    return deferred.promise;
                  },
                  deps:load(['/scripts/controllers/app/station.js?' + GLOBAL_URL_HASH ]).deps
                }
              })
              .state('app.station.stationHome', {
                url: '/home',
                templateUrl: '/views/pages/home.html?' + GLOBAL_URL_HASH,
                data : { title: 'Home', folded: false },
                resolve: {
                  station: stationDep,
                  deps:load(['/scripts/controllers/app/page.js?' + GLOBAL_URL_HASH , '/scripts/custom-pgwslider.js?' + GLOBAL_URL_HASH , '/libs/jquery/pgwslider/pgwslider.min.css?' + GLOBAL_URL_HASH, 'angular-carousel']).deps
                },
                controller: 'PageCtrl'
              })
              .state('app.station.categoryPage', {
                url: '/cat?name',
                templateUrl: '/views/pages/category.html?' + GLOBAL_URL_HASH,
                data : { title: 'Category', folded: false },
                controller: 'CategoryCtrl',
                resolve: {
                  category: function($stateParams, $q, trix){
                    var deferred = $q.defer();
                     initData.stations.forEach(function(station){
                        if(station.stationSlug == $stateParams.stationSlug)
                          station.categories && station.categories.forEach(function(category){
                            if(category.name == $stateParams.name){
                              category.station = station;
                              deferred.resolve(category);
                            }
                          })
                     })
                     
                    return deferred.promise;
                  },
                  deps:load(['/scripts/controllers/app/category.js?' + GLOBAL_URL_HASH ]).deps
                }
              })
              .state('app.wall', {
                url: '/wall',
                templateUrl: '/views/pages/dashboard.wall.html?' + GLOBAL_URL_HASH,
                data : { title: 'Wall', folded: false }
              })
              .state('app.todo', {
                url: '/todo',
                templateUrl: '/apps/todo/todo.html?' + GLOBAL_URL_HASH,
                data : { title: 'Todo', theme: { primary: 'indigo-800'} },
                controller: 'TodoCtrl',
                resolve: load('/apps/todo/todo.js?' + GLOBAL_URL_HASH )
              })
              .state('app.todo.list', {
                  url: '/{fold}'
              })
              .state('app.note', {
                url: '/note',
                templateUrl: '/apps/note/main.html?' + GLOBAL_URL_HASH,
                data : { theme: { primary: 'blue-grey'} }
              })
              .state('app.note.list', {
                url: '/list',
                templateUrl: '/apps/note/list.html?' + GLOBAL_URL_HASH,
                data : { title: 'Note'},
                controller: 'NoteCtrl',
                resolve: load(['/apps/note/note.js?' + GLOBAL_URL_HASH , '/libs/jquery/moment/min/moment-with-locales.min.js?' + GLOBAL_URL_HASH ])
              })
              .state('app.note.item', {
                url: '/{id}',
                views: {
                  '': {
                    templateUrl: '/apps/note/item.html?' + GLOBAL_URL_HASH,
                    controller: 'NoteItemCtrl',
                    resolve: load(['/apps/note/note.js?' + GLOBAL_URL_HASH , '/libs/jquery/moment/min/moment-with-locales.min.js?' + GLOBAL_URL_HASH ])
                  },
                  'navbar@': {
                    templateUrl: '/apps/note/navbar.html?' + GLOBAL_URL_HASH,
                    controller: 'NoteItemCtrl'
                  }
                },
                data : { title: '', child: true }
              })
              .state('app.inbox', {
                  url: '/inbox',
                  templateUrl: '/apps/inbox/inbox.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Inbox', folded: false },
                  resolve: load( ['/apps/inbox/inbox.js?' + GLOBAL_URL_HASH ,'/libs/jquery/moment/min/moment-with-locales.min.js?' + GLOBAL_URL_HASH ] )
              })
              .state('app.inbox.list', {
                  url: '/inbox/{fold}',
                  templateUrl: '/apps/inbox/list.html?' + GLOBAL_URL_HASH
              })
              .state('app.inbox.detail', {
                  url: '/{id:[0-9]{1,4}}',
                  templateUrl: '/apps/inbox/detail.html?' + GLOBAL_URL_HASH
              })
              .state('app.inbox.compose', {
                  url: '/compose',
                  templateUrl: '/apps/inbox/new.html?' + GLOBAL_URL_HASH,
                  resolve: load( ['textAngular', 'ui.select'] )
              })
            .state('ui', {
              url: '/ui',
              abstract: true,
              views: {
                '': {
                  templateUrl: layout
                },
                'aside': {
                  templateUrl: aside
                },
                'content': {
                  templateUrl: content
                }
              }
            })
              // components router
              .state('ui.component', {
                url: '/component',
                abstract: true,
                template: '<div ui-view></div>'
              })
                .state('ui.component.arrow', {
                  url: '/arrow',
                  templateUrl: '/views/ui/component/arrow.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Arrows' }
                })
                .state('ui.component.badge-label', {
                  url: '/badge-label',
                  templateUrl: '/views/ui/component/badge-label.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Badges & Labels' }
                })
                .state('ui.component.button', {
                  url: '/button',
                  templateUrl: '/views/ui/component/button.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Buttons' }
                })
                .state('ui.component.color', {
                  url: '/color',
                  templateUrl: '/views/ui/component/color.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Colors' }
                })
                .state('ui.component.grid', {
                  url: '/grid',
                  templateUrl: '/views/ui/component/grid.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Grids' }
                })
                .state('ui.component.icon', {
                  url: '/icons',
                  templateUrl: '/views/ui/component/icon.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Icons' }
                })
                .state('ui.component.list', {
                  url: '/list',
                  templateUrl: '/views/ui/component/list.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Lists' }
                })
                .state('ui.component.nav', {
                  url: '/nav',
                  templateUrl: '/views/ui/component/nav.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Navs' }
                })
                .state('ui.component.progressbar', {
                  url: '/progressbar',
                  templateUrl: '/views/ui/component/progressbar.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Progressbars' }
                })
                .state('ui.component.streamline', {
                  url: '/streamline',
                  templateUrl: '/views/ui/component/streamline.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Streamlines' }
                })
                .state('ui.component.timeline', {
                  url: '/timeline',
                  templateUrl: '/views/ui/component/timeline.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Timelines' }
                })
                .state('ui.component.uibootstrap', {
                  url: '/uibootstrap',
                  templateUrl: '/views/ui/component/uibootstrap.html?' + GLOBAL_URL_HASH,
                  resolve: load('/scripts/controllers/bootstrap.js?' + GLOBAL_URL_HASH ),
                  data : { title: 'UI Bootstrap' }
                })
              // material routers
              .state('ui.material', {
                url: '/material',
                template: '<div ui-view></div>',
                resolve: load('/scripts/controllers/material.js?' + GLOBAL_URL_HASH )
              })
                .state('ui.material.button', {
                  url: '/button',
                  templateUrl: '/views/ui/material/button.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Buttons' }
                })
                .state('ui.material.color', {
                  url: '/color',
                  templateUrl: '/views/ui/material/color.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Colors' }
                })
                .state('ui.material.icon', {
                  url: '/icon',
                  templateUrl: '/views/ui/material/icon.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Icons' }
                })
                .state('ui.material.card', {
                  url: '/card',
                  templateUrl: '/views/ui/material/card.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Card' }
                })
                .state('ui.material.form', {
                  url: '/form',
                  templateUrl: '/views/ui/material/form.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Form' }
                })
                .state('ui.material.list', {
                  url: '/list',
                  templateUrl: '/views/ui/material/list.html?' + GLOBAL_URL_HASH,
                  data : { title: 'List' }
                })
                .state('ui.material.ngmaterial', {
                  url: '/ngmaterial',
                  templateUrl: '/views/ui/material/ngmaterial.html?' + GLOBAL_URL_HASH,
                  data : { title: 'NG Material' }
                })
              // form routers
              .state('ui.form', {
                url: '/form',
                template: '<div ui-view></div>'
              })
                .state('ui.form.layout', {
                  url: '/layout',
                  templateUrl: '/views/ui/form/layout.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Layouts' }
                })
                .state('ui.form.element', {
                  url: '/element',
                  templateUrl: '/views/ui/form/element.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Elements' }
                })              
                .state('ui.form.validation', {
                  url: '/validation',
                  templateUrl: '/views/ui/form/validation.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Validations' }
                })
                .state('ui.form.select', {
                  url: '/select',
                  templateUrl: '/views/ui/form/select.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Selects' },
                  controller: 'SelectCtrl',
                  resolve: load(['ui.select','/scripts/controllers/select.js?' + GLOBAL_URL_HASH ])
                })
                .state('ui.form.editor', {
                  url: '/editor',
                  templateUrl: '/views/ui/form/editor.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Editor' },
                  controller: 'EditorCtrl',
                  resolve: load(['textAngular','/scripts/controllers/editor.js?' + GLOBAL_URL_HASH ])
                })
                .state('ui.form.slider', {
                  url: '/slider',
                  templateUrl: '/views/ui/form/slider.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Slider' },
                  controller: 'SliderCtrl',
                  resolve: load('/scripts/controllers/slider.js?' + GLOBAL_URL_HASH )
                })
                .state('ui.form.tree', {
                  url: '/tree',
                  templateUrl: '/views/ui/form/tree.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Tree' },
                  controller: 'TreeCtrl',
                  resolve: load('/scripts/controllers/tree.js?' + GLOBAL_URL_HASH )
                })
                .state('ui.form.file-upload', {
                  url: '/file-upload',
                  templateUrl: '/views/ui/form/file-upload.html?' + GLOBAL_URL_HASH,
                  data : { title: 'File upload' },
                  controller: 'UploadCtrl',
                  resolve: load(['angularFileUpload', '/scripts/controllers/upload.js?' + GLOBAL_URL_HASH ])
                })
                .state('ui.form.image-crop', {
                  url: '/image-crop',
                  templateUrl: '/views/ui/form/image-crop.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Image Crop' },
                  controller: 'ImgCropCtrl',
                  resolve: load(['ngImgCrop','/scripts/controllers/imgcrop.js?' + GLOBAL_URL_HASH ])
                })
                .state('ui.form.editable', {
                  url: '/editable',
                  templateUrl: '/views/ui/form/xeditable.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Xeditable' },
                  controller: 'XeditableCtrl',
                  resolve: load(['xeditable','/scripts/controllers/xeditable.js?' + GLOBAL_URL_HASH ])
                })
              // table routers
              .state('ui.table', {
                url: '/table',
                template: '<div ui-view></div>'
              })
                .state('ui.table.static', {
                  url: '/static',
                  templateUrl: '/views/ui/table/static.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Static', theme: { primary: 'blue'} }
                })
                .state('ui.table.smart', {
                  url: '/smart',
                  templateUrl: '/views/ui/table/smart.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Smart' },
                  controller: 'TableCtrl',
                  resolve: load(['smart-table', '/scripts/controllers/table.js?' + GLOBAL_URL_HASH ])
                })
                .state('ui.table.datatable', {
                  url: '/datatable',
                  data : { title: 'Datatable' },
                  templateUrl: '/views/ui/table/datatable.html?' + GLOBAL_URL_HASH
                })
                .state('ui.table.footable', {
                  url: '/footable',
                  data : { title: 'Footable' },
                  templateUrl: '/views/ui/table/footable.html?' + GLOBAL_URL_HASH
                })
                .state('ui.table.nggrid', {
                  url: '/nggrid',
                  templateUrl: '/views/ui/table/nggrid.html?' + GLOBAL_URL_HASH,
                  data : { title: 'NG Grid' },
                  controller: 'NGGridCtrl',
                  resolve: load(['ngGrid','/scripts/controllers/nggrid.js?' + GLOBAL_URL_HASH ])
                })
                .state('ui.table.uigrid', {
                  url: '/uigrid',
                  templateUrl: '/views/ui/table/uigrid.html?' + GLOBAL_URL_HASH,
                  data : { title: 'UI Grid' },
                  controller: "UiGridCtrl",
                  resolve: load(['ui.grid', '/scripts/controllers/uigrid.js?' + GLOBAL_URL_HASH ])
                })
                .state('ui.table.editable', {
                  url: '/editable',
                  templateUrl: '/views/ui/table/editable.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Editable' },
                  controller: 'XeditableCtrl',
                  resolve: load(['xeditable','/scripts/controllers/xeditable.js?' + GLOBAL_URL_HASH ])
                })
              // chart
              .state('ui.chart', {
                url: '/chart',
                templateUrl: '/views/ui/chart/chart.html?' + GLOBAL_URL_HASH,
                data : { title: 'Charts' },
                resolve: load('/scripts/controllers/chart.js?' + GLOBAL_URL_HASH )
              })
              // map routers
              .state('ui.map', {
                url: '/map',
                template: '<div ui-view></div>'
              })
                .state('ui.map.google', {
                  url: '/google',
                  templateUrl: '/views/ui/map/google.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Gmap' },
                  controller: 'GoogleMapCtrl',
                  resolve: load(['ui.map', '/scripts/controllers/load-google-maps.js?' + GLOBAL_URL_HASH , '/scripts/controllers/googlemap.js?' + GLOBAL_URL_HASH ], function(){ return loadGoogleMaps(); })
                })
                .state('ui.map.vector', {
                  url: '/vector',
                  templateUrl: '/views/ui/map/vector.html?' + GLOBAL_URL_HASH,
                  data : { title: 'Vector' },
                  controller: 'VectorMapCtrl',
                  resolve: load('/scripts/controllers/vectormap.js?' + GLOBAL_URL_HASH )
                })

            .state('page', {
              url: '/page',
              views: {
                '': {
                  templateUrl: layout
                },
                'aside': {
                  templateUrl: aside
                },
                'content': {
                  templateUrl: content
                }
              }
            })
              .state('page.profile', {
                url: '/profile',
                templateUrl: '/views/pages/profile.html?' + GLOBAL_URL_HASH,
                data : { title: 'Profile', theme: { primary: 'green'} }
              })
              .state('page.settings', {
                url: '/settings',
                templateUrl: '/views/pages/settings.html?' + GLOBAL_URL_HASH,
                data : { title: 'Settings' }
              })
              .state('page.blank', {
                url: '/blank',
                templateUrl: '/views/pages/blank.html?' + GLOBAL_URL_HASH,
                data : { title: 'Blank' }
              })
              .state('page.document', {
                url: '/document',
                templateUrl: '/views/pages/document.html?' + GLOBAL_URL_HASH,
                data : { title: 'Document' }
              })
              .state('404', {
                url: '/404',
                templateUrl: '/views/pages/404.html?' + GLOBAL_URL_HASH
              })
              .state('505', {
                url: '/505',
                templateUrl: '/views/pages/505.html?' + GLOBAL_URL_HASH
              })
              
            ;
        }

        if(isSettigns)
          createSettingsRoutes();
        else
          createHomeRoutes();


        $stateProvider
        .state('app.search', {
                url: '/s?q',
                templateUrl: '/views/pages/search.html?' + GLOBAL_URL_HASH,
                data : { titleTranslate: 'titles.SEARCH', title: 'Busca', folded: true },
                resolve: load(['/scripts/controllers/app/search.js?' + GLOBAL_URL_HASH , 'wu.masonry']),
                controller: 'SearchCtrl'
              })
        .state('app.bookmarks', {
            url: '/@{username}/bookmarks',
            templateUrl: '/views/pages/bookmarks.html?' + GLOBAL_URL_HASH,
            data : { title: 'Bookmarks', folded: false },
            controller: 'BookmarksCtrl',
            resolve: {

              person: function($stateParams, $q, trix){
                var deferred = $q.defer();
                if(initData && initData.person.id == 0){
                  document.location.href = '/access/signin';
                  window.console && console.error('user is not logged')
                }else if(initData.person.username !== $stateParams.username){
                  document.location.href = '/';
                  window.console && console.error('user is not owner')
                }else if(initData.person.username === $stateParams.username){
                  deferred.resolve(initData.person)
                }else{
                  document.location.href = '/404';
                }
                return deferred.promise;
              },
              deps:load(['wu.masonry', '/scripts/controllers/app/bookmarks.js?' + GLOBAL_URL_HASH ]).deps
            }
          })
        .state('app.userprofile', {
            url: '/@{username}',
            templateUrl: '/views/pages/profile.html?' + GLOBAL_URL_HASH,
            data : { title: 'Profle', folded: false },
            controller: 'ProfileCtrl',
            resolve: {

              person: function($stateParams, $q, trix){
                var deferred = $q.defer();
                trix.findByUsername($stateParams.username, 'personProjection').success(function(response){
                  if(response.persons && response.persons.length > 0)
                    deferred.resolve(response.persons[0])
                  else
                    document.location.href = '/404';
                }).error(function(){
                  document.location.href = '/404';
                });
                return deferred.promise;
              },
              deps:load(['wu.masonry', '/scripts/controllers/app/profile.js?' + GLOBAL_URL_HASH ]).deps
            }
          })
          .state('access', {
            url: '/access',
            templateUrl: '/views/pages/access.html?' + GLOBAL_URL_HASH,
            resolve: {
              appData: function($stateParams, $q, trix){
                var deferred = $q.defer();
                // if(initData.person.id == 0){
                //   document.location.href = '/';
                // }else{
                  deferred.resolve(initData);
                // }
                return deferred.promise;
              },
              deps:load( ['angularFileUpload', '/scripts/services/trix.js?' + GLOBAL_URL_HASH , '/libs/theming/tinycolor/tinycolor.js?' + GLOBAL_URL_HASH , 'mdPickers', 'afkl.lazyImage', 'perfect_scrollbar', 'angularMoment'] ).deps
            },
            controller: 'AppDataCtrl'
          })
          .state('access.signin', {
            url: '/signin?next',
            templateUrl: '/views/pages/signin.html?' + GLOBAL_URL_HASH,
            resolve: load(['/scripts/controllers/app/signin-signup-forgot.js?' + GLOBAL_URL_HASH ]),
            controller: 'AppSigninCtrl'
          })
          .state('access.signup', {
            url: '/signup',
            templateUrl: '/views/pages/signup.html?' + GLOBAL_URL_HASH,
            resolve: load(['/scripts/controllers/app/signin-signup-forgot.js?' + GLOBAL_URL_HASH ]),
            controller: 'AppSigninCtrl'
          })
          .state('access.forgot-password', {
            url: '/forgot-password',
            templateUrl: '/views/pages/forgot-password.html?' + GLOBAL_URL_HASH,
            resolve: load(['/scripts/controllers/app/signin-signup-forgot.js?' + GLOBAL_URL_HASH ]),
            controller: 'AppForgotCtrl'
          })
          .state('access.createnetwork', {
            url: '/createnetwork',
            templateUrl: '/views/pages/create-network.html?' + GLOBAL_URL_HASH,
            resolve: load(['/scripts/controllers/app/signin-signup-forgot.js?' + GLOBAL_URL_HASH ]),
            controller: 'AppNetworkCtrl'
          })
          .state('access.networkcreated', {
            url: '/networkcreated?token',
            template: '<div></div>',
            resolve: load(['/scripts/controllers/app/signin-signup-forgot.js?' + GLOBAL_URL_HASH ]),
            controller: 'AppNetworkCreatedCtrl'
          })
          .state('access.lockme', {
            url: '/lockme',
            templateUrl: '/views/pages/lockme.html?' + GLOBAL_URL_HASH
          })

          if(!isSettigns){

            var postDep = function($stateParams, $q, trix){
              var deferred = $q.defer();

              trix.findBySlug($stateParams.postSlug, "postProjection")
              .success(function(response){
                if(response.posts && response.posts.length > 0)
                  deferred.resolve(response.posts[0]);
                else
                  document.location.href = '/404';
              })
              .error(function(){
                document.location.href = '/404';
              })

              return deferred.promise;
            }

            $stateProvider
            .state('app.station.read', {
                url: '/{postSlug}',
                templateUrl: '/views/pages/read.html?' + GLOBAL_URL_HASH,
                data : { title: 'Read', folded: false },
                controller: 'ReadCtrl',
                resolve: {
                  post: postDep,
                  station: stationDep,
                  deps:load(['wu.masonry', '/scripts/controllers/app/read.js?' + GLOBAL_URL_HASH , '/libs/angular/froala-wysiwyg-editor/css/froala_style.min.css?' + GLOBAL_URL_HASH, 'videosharing-embed']).deps
                }
              })
          }
        
          function load(srcs, callback) {
            return {
                deps: ['$ocLazyLoad', '$q',
                  function( $ocLazyLoad, $q ){
                    var deferred = $q.defer();
                    var promise  = false;
                    srcs = angular.isArray(srcs) ? srcs : srcs.split(/\s+/);
                    if(!promise){
                      promise = deferred.promise;
                    }
                    angular.forEach(srcs, function(src) {
                      promise = promise.then( function(){
                        angular.forEach(MODULE_CONFIG, function(module) {
                          if( module.name == src){
                            if(!module.module){
                              name = module.files;
                            }else{
                              name = module.name;
                            }
                          }else{
                            name = src;
                          }
                        });
                        return $ocLazyLoad.load(name);
                      } );
                    });
                    deferred.resolve();
                    return callback ? promise.then(function(){ return callback(); }) : promise;
                }]
            }
          }

          function getParams(name) {
            name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
            var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
                results = regex.exec(location.search);
            return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
          }

      }
    ]
  );
