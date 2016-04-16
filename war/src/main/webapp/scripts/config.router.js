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
    [          '$stateProvider', '$urlRouterProvider', 'MODULE_CONFIG', '$translateProvider', '$locationProvider',
      function ( $stateProvider,   $urlRouterProvider,  MODULE_CONFIG ,  $translateProvider ,  $locationProvider) {

        $locationProvider.html5Mode({
          enabled: true,
        });

        var layout='',aside='',content='';

        var path = location.pathname

        var isSettigns = path.slice(0, '/settings'.length) == '/settings';

        if(isSettigns){
          layout = '/views/layout.html';aside = '/views/aside.html';content= '/views/content.html';
        }else{
          layout = '/views/layout.h.html';aside = '/views/aside.h.html';content= '/views/content.h.html';
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
                   if(initData.person.id == 0){
                     document.location.href = '/access/signin?next=/settings';
                   }else{
                    deferred.resolve(initData);
                   }
                  return deferred.promise;
                },
                deps:load( ['infinite-scroll', 'angularFileUpload', '/scripts/services/trix.js', '/libs/theming/tinycolor/tinycolor.js', 'mdPickers', 'afkl.lazyImage', 'angularMoment', 'ui.materialize'] ).deps
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
                templateUrl: '/views/settings/settings-post.html',
                data : { titleTranslate: 'titles.POST', title: 'Publicação', folded: true },
                resolve: load([
                  'com.2fdevs.videogular','com.2fdevs.videogular.plugins.controls','com.2fdevs.videogular.plugins.overlayplay','com.2fdevs.videogular.plugins.poster',
                  'recorderServiceProvider', 'angularAudioRecorder','videosharing-embed','/libs/angular/lifely-focuspoint/dist/focuspoint.css', 'leaflet-directive', 'ngJcrop', 'froala', 'monospaced.elastic', 'angularFileUpload', '/scripts/controllers/settings/settings-post.js', '/scripts/controllers/settings/settings-post-geolocation.js']),
                controller: 'SettingsPostCtrl'
              })

              .state('app.stations', {
                url: '/stations',
                templateUrl: '/views/settings/settings-stations.html',
                data : { titleTranslate: 'titles.STATIONS', title: 'Estações', folded: false },
                resolve: load(['angularFileUpload', '/scripts/controllers/settings/settings-stations.js']),
                controller: 'SettingsStationsCtrl'
              })

              .state('app.categories', {
                url: '/categories?slug',
                templateUrl: '/views/settings/settings-categories.html',
                data : { titleTranslate: 'titles.CATEGORIES', title: 'Categorias', folded: false },
                resolve: load(['angularFileUpload', '/scripts/controllers/settings/settings-categories.js']),
                controller: 'SettingsCategoriesCtrl'
              })

              .state('app.users', {
                  url: '/users?username?newUser',
                  templateUrl: '/views/settings/settings-users.html?',
                  data : { titleTranslate: 'titles.USERS', title: 'Usuários', folded: false },
                  resolve: {
                    deps: ['$ocLazyLoad',
                      function( $ocLazyLoad ){
                        return $ocLazyLoad.load(['angularFileUpload', 'angularFileUpload', '/scripts/controllers/settings/settings-users.js?']);
                    }]
                  },
                  controller:'SettingsUsersCtrl'
              })
              .state('app.profile', {
                url: '/profile',
                templateUrl: '/views/settings/settings-profile.html',
                data : { titleTranslate: 'settings.aside.MY_PROFILE', title: 'Perfil', folded: true },
                resolve: load(['/scripts/controllers/settings/settings-profile.js', 'ngJcrop']),
                controller:'SettingsProfileCtrl'
              })

              // chart
              .state('app.chart', {
                url: '/chart',
                templateUrl: '/views/ui/chart/chart.html',
                data : { title: 'Charts' },
                resolve: load('/scripts/controllers/chart.js')
              })
              .state('app.network', {
                url: '/network',
                templateUrl: '/views/settings/settings-network.html',
                data : { titleTranslate: 'titles.NETWORK', title: 'Rede', folded: false },
                resolve: load(['angularFileUpload', '/scripts/controllers/settings/settings-network.js']),
                controller: 'SettingsNetworkCtrl'
              })
              .state('app.publications', {
                url: '/publications',
                reloadOnSearch: false,
                templateUrl: '/views/settings/settings-publications.html',
                data : { titleTranslate: 'titles.PUBLICATIONS', title: 'Publicações', folded: false },
                // resolve: load(['mdDateTime', 'angularFileUpload', '/scripts/controllers/settings/settings-publications.js']),
                resolve: {
                  deps: ['$ocLazyLoad', '$templateCache',
                    function( $ocLazyLoad, $templateCache ){
                      return $ocLazyLoad.load(['mdDateTime', 'angularFileUpload', '/scripts/controllers/settings/settings-publications.js'])
                      .then(function(){
                        $templateCache.put('md-date-time.tpl.html', '<div ng-class="modeClass()" class="time-date"><div ng-click="modeSwitch()" aria-label="Switch to {{modeSwitchText()}}" class="display"><div class="p-t-xs p-b-xs md-primary-default background">{{display.title()}}</div><div class="p-t p-b md-primary-hue-1 background"><div class="text-md text-u-c">{{display.super()}}</div><div ng-bind-html="display.main()" class="text-3x"></div><div class="text-md">{{display.sub()}}</div></div></div><div class="control"><div class="full-title">{{display.fullTitle()}}</div><div class="slider"><div class="date-control"><div class="title"><md-button ng-click="calendar._incMonth(-1)" aria-label="Previous Month" style="float: left" ng-class="{\'visuallyhidden\': calendar.isVisibleMonthButton(\'mindate\')}"><i class="fa fa-caret-left"></i></md-button><span class="month-part">{{date | date:\'MMMM\'}}<select ng-model="calendar._month" ng-change="calendar.monthChange()" ng-options="calendar._allMonths.indexOf(month) as month for month in calendar._months"></select></span><input ng-model="calendar._year" ng-change="calendar.monthChange()" type="number" min="{{restrictions.mindate ? restrictions.mindate.getFullYear() : 0}}" max="{{restrcitions.maxdate ? restrictions.maxdate.getFullYear() : NaN}}" class="year-part"><md-button ng-click="calendar._incMonth(1)" aria-label="Next Month" style="float: right" ng-class="{\'visuallyhidden\': calendar.isVisibleMonthButton(\'maxdate\')}"><i class="fa fa-caret-right"></i></md-button></div><div class="headers"><div ng-repeat="day in _weekdays track by $index" class="day-cell">{{day}}</div></div><div class="days"><md-button ng-style="{\'margin-left\': calendar.offsetMargin()}" ng-class="calendar.class(1)" ng-disabled="calendar.isDisabled(1)" ng-show="calendar.isVisible(1)" ng-click="calendar.select(1)" class="day-cell">1</md-button><md-button ng-class="calendar.class(2)" ng-show="calendar.isVisible(2)" ng-disabled="calendar.isDisabled(2)" ng-click="calendar.select(2)" class="day-cell">2</md-button><md-button ng-class="calendar.class(3)" ng-show="calendar.isVisible(3)" ng-disabled="calendar.isDisabled(3)" ng-click="calendar.select(3)" class="day-cell">3</md-button><md-button ng-class="calendar.class(4)" ng-show="calendar.isVisible(4)" ng-disabled="calendar.isDisabled(4)" ng-click="calendar.select(4)" class="day-cell">4</md-button><md-button ng-class="calendar.class(5)" ng-show="calendar.isVisible(5)" ng-disabled="calendar.isDisabled(5)" ng-click="calendar.select(5)" class="day-cell">5</md-button><md-button ng-class="calendar.class(6)" ng-show="calendar.isVisible(6)" ng-disabled="calendar.isDisabled(6)" ng-click="calendar.select(6)" class="day-cell">6</md-button><md-button ng-class="calendar.class(7)" ng-show="calendar.isVisible(7)" ng-disabled="calendar.isDisabled(7)" ng-click="calendar.select(7)" class="day-cell">7</md-button><md-button ng-class="calendar.class(8)" ng-show="calendar.isVisible(8)" ng-disabled="calendar.isDisabled(8)" ng-click="calendar.select(8)" class="day-cell">8</md-button><md-button ng-class="calendar.class(9)" ng-show="calendar.isVisible(9)" ng-disabled="calendar.isDisabled(9)" ng-click="calendar.select(9)" class="day-cell">9</md-button><md-button ng-class="calendar.class(10)" ng-show="calendar.isVisible(10)" ng-disabled="calendar.isDisabled(10)" ng-click="calendar.select(10)" class="day-cell">10</md-button><md-button ng-class="calendar.class(11)" ng-show="calendar.isVisible(11)" ng-disabled="calendar.isDisabled(11)" ng-click="calendar.select(11)" class="day-cell">11</md-button><md-button ng-class="calendar.class(12)" ng-show="calendar.isVisible(12)" ng-disabled="calendar.isDisabled(12)" ng-click="calendar.select(12)" class="day-cell">12</md-button><md-button ng-class="calendar.class(13)" ng-show="calendar.isVisible(13)" ng-disabled="calendar.isDisabled(13)" ng-click="calendar.select(13)" class="day-cell">13</md-button><md-button ng-class="calendar.class(14)" ng-show="calendar.isVisible(14)" ng-disabled="calendar.isDisabled(14)" ng-click="calendar.select(14)" class="day-cell">14</md-button><md-button ng-class="calendar.class(15)" ng-show="calendar.isVisible(15)" ng-disabled="calendar.isDisabled(15)" ng-click="calendar.select(15)" class="day-cell">15</md-button><md-button ng-class="calendar.class(16)" ng-show="calendar.isVisible(16)" ng-disabled="calendar.isDisabled(16)" ng-click="calendar.select(16)" class="day-cell">16</md-button><md-button ng-class="calendar.class(17)" ng-show="calendar.isVisible(17)" ng-disabled="calendar.isDisabled(17)" ng-click="calendar.select(17)" class="day-cell">17</md-button><md-button ng-class="calendar.class(18)" ng-show="calendar.isVisible(18)" ng-disabled="calendar.isDisabled(18)" ng-click="calendar.select(18)" class="day-cell">18</md-button><md-button ng-class="calendar.class(19)" ng-show="calendar.isVisible(19)" ng-disabled="calendar.isDisabled(19)" ng-click="calendar.select(19)" class="day-cell">19</md-button><md-button ng-class="calendar.class(20)" ng-show="calendar.isVisible(20)" ng-disabled="calendar.isDisabled(20)" ng-click="calendar.select(20)" class="day-cell">20</md-button><md-button ng-class="calendar.class(21)" ng-show="calendar.isVisible(21)" ng-disabled="calendar.isDisabled(21)" ng-click="calendar.select(21)" class="day-cell">21</md-button><md-button ng-class="calendar.class(22)" ng-show="calendar.isVisible(22)" ng-disabled="calendar.isDisabled(22)" ng-click="calendar.select(22)" class="day-cell">22</md-button><md-button ng-class="calendar.class(23)" ng-show="calendar.isVisible(23)" ng-disabled="calendar.isDisabled(23)" ng-click="calendar.select(23)" class="day-cell">23</md-button><md-button ng-class="calendar.class(24)" ng-show="calendar.isVisible(24)" ng-disabled="calendar.isDisabled(24)" ng-click="calendar.select(24)" class="day-cell">24</md-button><md-button ng-class="calendar.class(25)" ng-show="calendar.isVisible(25)" ng-disabled="calendar.isDisabled(25)" ng-click="calendar.select(25)" class="day-cell">25</md-button><md-button ng-class="calendar.class(26)" ng-show="calendar.isVisible(26)" ng-disabled="calendar.isDisabled(26)" ng-click="calendar.select(26)" class="day-cell">26</md-button><md-button ng-class="calendar.class(27)" ng-show="calendar.isVisible(27)" ng-disabled="calendar.isDisabled(27)" ng-click="calendar.select(27)" class="day-cell">27</md-button><md-button ng-class="calendar.class(28)" ng-show="calendar.isVisible(28)" ng-disabled="calendar.isDisabled(28)" ng-click="calendar.select(28)" class="day-cell">28</md-button><md-button ng-class="calendar.class(29)" ng-show="calendar.isVisible(29)" ng-disabled="calendar.isDisabled(29)" ng-click="calendar.select(29)" class="day-cell">29</md-button><md-button ng-class="calendar.class(30)" ng-show="calendar.isVisible(30)" ng-disabled="calendar.isDisabled(30)" ng-click="calendar.select(30)" class="day-cell">30</md-button><md-button ng-class="calendar.class(31)" ng-show="calendar.isVisible(31)" ng-disabled="calendar.isDisabled(31)" ng-click="calendar.select(31)" class="day-cell">31</md-button></div></div><md-button ng-click="modeSwitch()" aria-label="Switch to {{modeSwitchText()}}" class="switch-control"><i class="fa fa-clock-o"></i><i class="fa fa-calendar"></i><span class="visuallyhidden">{{modeSwitchText()}}</span></md-button><div class="time-control"><div class="time-inputs"><input type="number" min="{{_hours24 ? 0 : 1}}" max="{{_hours24 ? 23 : 12}}" ng-model="clock._hours"><md-button ng-click="clock._incHours(1)" aria-label="Increment Hours" class="hours up"><i class="fa fa-caret-up"></i></md-button><md-button ng-click="clock._incHours(-1)" aria-label="Decrement Hours" class="hours down"><i class="fa fa-caret-down"></i></md-button><input type="number" min="0" max="59" ng-model="clock._minutes"><md-button ng-click="clock._incMinutes(1)" aria-label="Increment Minutes" class="minutes up"><i class="fa fa-caret-up"></i></md-button><md-button ng-click="clock._incMinutes(-1)" aria-label="Decrement Minutes" class="minutes down"><i class="fa fa-caret-down"></i></md-button></div><div ng-if="!_hours24" class="buttons"><md-button ng-click="clock.setAM()" aria-label="Switch AM/PM">{{date | date:\'a\'}}</md-button></div></div></div></div><div class="buttons"><md-button ng-click="setNow()" aria-label="Set To Current {{ _dispalyMode == \'date\' ? \'Date\' : \'Date/Time\'}}" md-style="{\'color\': \'primary.500\'}">Now</md-button><md-button ng-click="cancel()" aria-label="Cancel">Cancel</md-button><md-button ng-click="save()" aria-label="Save">Save</md-button></div></div>');
                      });
                  }]
                },
                controller: 'SettingsPublicationsCtrl'
              })
              .state('app.colors', {
                url: '/colors',
                templateUrl: '/views/settings/settings-colors.html',
                data : { titleTranslate: 'titles.THEMING', title: 'Aparência', folded: false },
                resolve: load(['angularFileUpload', '/scripts/controllers/settings/settings-network.js', 'angularSpectrumColorpicker', '/scripts/controllers/color-generator.js',
                    '/styles/theming.css', '/libs/jquery/slimScroll/jquery.slimscroll.min.js']),
                controller: 'ColorGeneratorCtrl'
              })
              .state('app.dashboard', {
                url: '/dashboard',
                templateUrl: '/views/pages/dashboard.html',
                data : { title: 'Dashboard', folded: false },
                resolve: load(['/scripts/controllers/chart.js','/scripts/controllers/vectormap.js'])
              })
              .state('app.theming', {
                url: '/theming',
                templateUrl: '/views/pages/dashboard.theming.html',
                data : { title: 'Theme builder', folded: false },
                resolve: load(['angularSpectrumColorpicker', '/scripts/controllers/color-generator.js',
                    '/styles/theming.css']),
                controller: 'ColorGeneratorCtrl'
              })
              .state('app.pagebuilder', {
                url: '/pagebuilder',
                templateUrl: '/views/settings/settings-pagebuilder.html',
                data : { title: 'Page Builder', folded: true },
                resolve: load(['wu.masonry', 'ui.ace'])
              })
              .state('app.pagebuilder.list', {
                url: '/list',
                templateUrl: '/views/settings/settings-pagebuilder-list.html',
                data : { title: 'Page Builder', folded: true },
                resolve: load(['angularFileUpload', '/scripts/controllers/settings/settings-pagebuilder-list.js']),
                controller: 'PageBuilderListCtrl'
              })
              .state('app.pagebuilder.editor', {
                url: '/editor',
                templateUrl: '/views/settings/settings-pagebuilder-editor.html',
                data : { title: 'Page Builder', folded: true },
                resolve: load(['angularFileUpload', '/scripts/controllers/settings/settings-pagebuilder-editor.js']),
                controller: 'PageBuilderEditorCtrl'
              })
              .state('app.pagebuilder.stationcolors', {
                url: '/stationcolors',
                templateUrl: '/views/settings/settings-colors.html',
                data : { title: 'Page Builder', folded: true },
                  resolve: load(['angularFileUpload', '/scripts/controllers/settings/settings-network.js', 'angularSpectrumColorpicker', '/scripts/controllers/color-generator.js',
                    '/styles/theming.css', '/libs/jquery/slimScroll/jquery.slimscroll.min.js']),
                controller: 'ColorGeneratorCtrl'
              })
              // .state('app.colors', {
              //   url: '/colors',
              //   templateUrl: '/views/settings/settings-colors.html',
              //   data : { titleTranslate: 'titles.THEMING', title: 'Aparência', folded: false },
              //   resolve: load(['angularFileUpload', '/scripts/controllers/settings/settings-network.js', 'angularSpectrumColorpicker', '/scripts/controllers/color-generator.js',
              //       '/styles/theming.css', '/libs/jquery/slimScroll/jquery.slimscroll.min.js']),
              //   controller: 'ColorGeneratorCtrl'
              // })
              .state('app.analysis', {
                url: '/analysis',
                templateUrl: '/views/pages/dashboard.analysis.html',
                data : { title: 'Analysis' },
                resolve: load(['/scripts/controllers/chart.js','/scripts/controllers/vectormap.js'])
              })
              .state('app.wall', {
                url: '/wall',
                templateUrl: '/views/pages/dashboard.wall.html',
                data : { title: 'Wall', folded: false }
              })
              .state('app.todo', {
                url: '/todo',
                templateUrl: '/apps/todo/todo.html',
                data : { title: 'Todo', theme: { primary: 'indigo-800'} },
                controller: 'TodoCtrl',
                resolve: load('/apps/todo/todo.js')
              })
              .state('app.todo.list', {
                  url: '/{fold}'
              })
              .state('app.note', {
                url: '/note',
                templateUrl: '/apps/note/main.html',
                data : { theme: { primary: 'blue-grey'} }
              })
              .state('app.note.list', {
                url: '/list',
                templateUrl: '/apps/note/list.html',
                data : { title: 'Note'},
                controller: 'NoteCtrl',
                resolve: load(['/apps/note/note.js', '/libs/jquery/moment/min/moment-with-locales.min.js'])
              })
              .state('app.note.item', {
                url: '/{id}',
                views: {
                  '': {
                    templateUrl: '/apps/note/item.html',
                    controller: 'NoteItemCtrl',
                    resolve: load(['/apps/note/note.js', '/libs/jquery/moment/min/moment-with-locales.min.js'])
                  },
                  'navbar@': {
                    templateUrl: '/apps/note/navbar.html',
                    controller: 'NoteItemCtrl'
                  }
                },
                data : { title: '', child: true }
              })
              .state('app.inbox', {
                  url: '/inbox',
                  templateUrl: '/apps/inbox/inbox.html',
                  data : { title: 'Inbox', folded: false },
                  resolve: load( ['/apps/inbox/inbox.js','/libs/jquery/moment/min/moment-with-locales.min.js'] )
              })
              .state('app.inbox.list', {
                  url: '/inbox/{fold}',
                  templateUrl: '/apps/inbox/list.html'
              })
              .state('app.inbox.detail', {
                  url: '/{id:[0-9]{1,4}}',
                  templateUrl: '/apps/inbox/detail.html'
              })
              .state('app.inbox.compose', {
                  url: '/compose',
                  templateUrl: '/apps/inbox/new.html',
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
                  templateUrl: '/views/ui/component/arrow.html',
                  data : { title: 'Arrows' }
                })
                .state('ui.component.badge-label', {
                  url: '/badge-label',
                  templateUrl: '/views/ui/component/badge-label.html',
                  data : { title: 'Badges & Labels' }
                })
                .state('ui.component.button', {
                  url: '/button',
                  templateUrl: '/views/ui/component/button.html',
                  data : { title: 'Buttons' }
                })
                .state('ui.component.color', {
                  url: '/color',
                  templateUrl: '/views/ui/component/color.html',
                  data : { title: 'Colors' }
                })
                .state('ui.component.grid', {
                  url: '/grid',
                  templateUrl: '/views/ui/component/grid.html',
                  data : { title: 'Grids' }
                })
                .state('ui.component.icon', {
                  url: '/icons',
                  templateUrl: '/views/ui/component/icon.html',
                  data : { title: 'Icons' }
                })
                .state('ui.component.list', {
                  url: '/list',
                  templateUrl: '/views/ui/component/list.html',
                  data : { title: 'Lists' }
                })
                .state('ui.component.nav', {
                  url: '/nav',
                  templateUrl: '/views/ui/component/nav.html',
                  data : { title: 'Navs' }
                })
                .state('ui.component.progressbar', {
                  url: '/progressbar',
                  templateUrl: '/views/ui/component/progressbar.html',
                  data : { title: 'Progressbars' }
                })
                .state('ui.component.streamline', {
                  url: '/streamline',
                  templateUrl: '/views/ui/component/streamline.html',
                  data : { title: 'Streamlines' }
                })
                .state('ui.component.timeline', {
                  url: '/timeline',
                  templateUrl: '/views/ui/component/timeline.html',
                  data : { title: 'Timelines' }
                })
                .state('ui.component.uibootstrap', {
                  url: '/uibootstrap',
                  templateUrl: '/views/ui/component/uibootstrap.html',
                  resolve: load('/scripts/controllers/bootstrap.js'),
                  data : { title: 'UI Bootstrap' }
                })
              // material routers
              .state('ui.material', {
                url: '/material',
                template: '<div ui-view></div>',
                resolve: load('/scripts/controllers/material.js')
              })
                .state('ui.material.button', {
                  url: '/button',
                  templateUrl: '/views/ui/material/button.html',
                  data : { title: 'Buttons' }
                })
                .state('ui.material.color', {
                  url: '/color',
                  templateUrl: '/views/ui/material/color.html',
                  data : { title: 'Colors' }
                })
                .state('ui.material.icon', {
                  url: '/icon',
                  templateUrl: '/views/ui/material/icon.html',
                  data : { title: 'Icons' }
                })
                .state('ui.material.card', {
                  url: '/card',
                  templateUrl: '/views/ui/material/card.html',
                  data : { title: 'Card' }
                })
                .state('ui.material.form', {
                  url: '/form',
                  templateUrl: '/views/ui/material/form.html',
                  data : { title: 'Form' }
                })
                .state('ui.material.list', {
                  url: '/list',
                  templateUrl: '/views/ui/material/list.html',
                  data : { title: 'List' }
                })
                .state('ui.material.ngmaterial', {
                  url: '/ngmaterial',
                  templateUrl: '/views/ui/material/ngmaterial.html',
                  data : { title: 'NG Material' }
                })
              // form routers
              .state('ui.form', {
                url: '/form',
                template: '<div ui-view></div>'
              })
                .state('ui.form.layout', {
                  url: '/layout',
                  templateUrl: '/views/ui/form/layout.html',
                  data : { title: 'Layouts' }
                })
                .state('ui.form.element', {
                  url: '/element',
                  templateUrl: '/views/ui/form/element.html',
                  data : { title: 'Elements' }
                })              
                .state('ui.form.validation', {
                  url: '/validation',
                  templateUrl: '/views/ui/form/validation.html',
                  data : { title: 'Validations' }
                })
                .state('ui.form.select', {
                  url: '/select',
                  templateUrl: '/views/ui/form/select.html',
                  data : { title: 'Selects' },
                  controller: 'SelectCtrl',
                  resolve: load(['ui.select','/scripts/controllers/select.js'])
                })
                .state('ui.form.editor', {
                  url: '/editor',
                  templateUrl: '/views/ui/form/editor.html',
                  data : { title: 'Editor' },
                  controller: 'EditorCtrl',
                  resolve: load(['textAngular','/scripts/controllers/editor.js'])
                })
                .state('ui.form.slider', {
                  url: '/slider',
                  templateUrl: '/views/ui/form/slider.html',
                  data : { title: 'Slider' },
                  controller: 'SliderCtrl',
                  resolve: load('/scripts/controllers/slider.js')
                })
                .state('ui.form.tree', {
                  url: '/tree',
                  templateUrl: '/views/ui/form/tree.html',
                  data : { title: 'Tree' },
                  controller: 'TreeCtrl',
                  resolve: load('/scripts/controllers/tree.js')
                })
                .state('ui.form.file-upload', {
                  url: '/file-upload',
                  templateUrl: '/views/ui/form/file-upload.html',
                  data : { title: 'File upload' },
                  controller: 'UploadCtrl',
                  resolve: load(['angularFileUpload', '/scripts/controllers/upload.js'])
                })
                .state('ui.form.image-crop', {
                  url: '/image-crop',
                  templateUrl: '/views/ui/form/image-crop.html',
                  data : { title: 'Image Crop' },
                  controller: 'ImgCropCtrl',
                  resolve: load(['ngImgCrop','/scripts/controllers/imgcrop.js'])
                })
                .state('ui.form.editable', {
                  url: '/editable',
                  templateUrl: '/views/ui/form/xeditable.html',
                  data : { title: 'Xeditable' },
                  controller: 'XeditableCtrl',
                  resolve: load(['xeditable','/scripts/controllers/xeditable.js'])
                })
              // table routers
              .state('ui.table', {
                url: '/table',
                template: '<div ui-view></div>'
              })
                .state('ui.table.static', {
                  url: '/static',
                  templateUrl: '/views/ui/table/static.html',
                  data : { title: 'Static', theme: { primary: 'blue'} }
                })
                .state('ui.table.smart', {
                  url: '/smart',
                  templateUrl: '/views/ui/table/smart.html',
                  data : { title: 'Smart' },
                  controller: 'TableCtrl',
                  resolve: load(['smart-table', '/scripts/controllers/table.js'])
                })
                .state('ui.table.datatable', {
                  url: '/datatable',
                  data : { title: 'Datatable' },
                  templateUrl: '/views/ui/table/datatable.html'
                })
                .state('ui.table.footable', {
                  url: '/footable',
                  data : { title: 'Footable' },
                  templateUrl: '/views/ui/table/footable.html'
                })
                .state('ui.table.nggrid', {
                  url: '/nggrid',
                  templateUrl: '/views/ui/table/nggrid.html',
                  data : { title: 'NG Grid' },
                  controller: 'NGGridCtrl',
                  resolve: load(['ngGrid','/scripts/controllers/nggrid.js'])
                })
                .state('ui.table.uigrid', {
                  url: '/uigrid',
                  templateUrl: '/views/ui/table/uigrid.html',
                  data : { title: 'UI Grid' },
                  controller: "UiGridCtrl",
                  resolve: load(['ui.grid', '/scripts/controllers/uigrid.js'])
                })
                .state('ui.table.editable', {
                  url: '/editable',
                  templateUrl: '/views/ui/table/editable.html',
                  data : { title: 'Editable' },
                  controller: 'XeditableCtrl',
                  resolve: load(['xeditable','/scripts/controllers/xeditable.js'])
                })
              // chart
              .state('ui.chart', {
                url: '/chart',
                templateUrl: '/views/ui/chart/chart.html',
                data : { title: 'Charts' },
                resolve: load('/scripts/controllers/chart.js')
              })
              // map routers
              .state('ui.map', {
                url: '/map',
                template: '<div ui-view></div>'
              })
                .state('ui.map.google', {
                  url: '/google',
                  templateUrl: '/views/ui/map/google.html',
                  data : { title: 'Gmap' },
                  controller: 'GoogleMapCtrl',
                  resolve: load(['ui.map', '/scripts/controllers/load-google-maps.js', '/scripts/controllers/googlemap.js'], function(){ return loadGoogleMaps(); })
                })
                .state('ui.map.vector', {
                  url: '/vector',
                  templateUrl: '/views/ui/map/vector.html',
                  data : { title: 'Vector' },
                  controller: 'VectorMapCtrl',
                  resolve: load('/scripts/controllers/vectormap.js')
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
                templateUrl: '/views/pages/settings.html',
                data : { title: 'Settings' }
              })
              .state('page.blank', {
                url: '/blank',
                templateUrl: '/views/pages/blank.html',
                data : { title: 'Blank' }
              })
              .state('page.document', {
                url: '/document',
                templateUrl: '/views/pages/document.html',
                data : { title: 'Document' }
              })
              .state('404', {
                url: '/404',
                templateUrl: '/views/pages/404.html'
              })
              .state('505', {
                url: '/505',
                templateUrl: '/views/pages/505.html'
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
                deps:load( ['infinite-scroll', '/scripts/services/trix.js', '/libs/theming/tinycolor/tinycolor.js', 'mdPickers', 'afkl.lazyImage', 'angularMoment', 'ui.materialize'] ).deps
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
                templateUrl: '/views/pages/dashboard.html',
                data : { title: 'Dashboard', folded: false },
                resolve: load(['/scripts/controllers/chart.js','/scripts/controllers/vectormap.js'])
              })
              .state('app.analysis', {
                url: '/analysis',
                templateUrl: '/views/pages/dashboard.analysis.html',
                data : { title: 'Analysis' },
                resolve: load(['/scripts/controllers/chart.js','/scripts/controllers/vectormap.js'])
              })
              .state('app.home', {
                url: '/',
                templateUrl: '/views/pages/home.html',
                data : { title: 'Home', folded: false },
                resolve: load(['angularFileUpload', 'wu.masonry', '/scripts/controllers/app/page.js']),
                controller: 'PageCtrl'

              })
              .state('app.stationHome', {
                url: '/{stationSlug}/home',
                templateUrl: '/views/pages/home.html',
                data : { title: 'Home', folded: false },
                resolve: load(['angularFileUpload', 'wu.masonry', '/scripts/controllers/app/page.js'])
              })
              .state('app.wall', {
                url: '/wall',
                templateUrl: '/views/pages/dashboard.wall.html',
                data : { title: 'Wall', folded: false }
              })
              .state('app.todo', {
                url: '/todo',
                templateUrl: '/apps/todo/todo.html',
                data : { title: 'Todo', theme: { primary: 'indigo-800'} },
                controller: 'TodoCtrl',
                resolve: load('/apps/todo/todo.js')
              })
              .state('app.todo.list', {
                  url: '/{fold}'
              })
              .state('app.note', {
                url: '/note',
                templateUrl: '/apps/note/main.html',
                data : { theme: { primary: 'blue-grey'} }
              })
              .state('app.note.list', {
                url: '/list',
                templateUrl: '/apps/note/list.html',
                data : { title: 'Note'},
                controller: 'NoteCtrl',
                resolve: load(['/apps/note/note.js', '/libs/jquery/moment/min/moment-with-locales.min.js'])
              })
              .state('app.note.item', {
                url: '/{id}',
                views: {
                  '': {
                    templateUrl: '/apps/note/item.html',
                    controller: 'NoteItemCtrl',
                    resolve: load(['/apps/note/note.js', '/libs/jquery/moment/min/moment-with-locales.min.js'])
                  },
                  'navbar@': {
                    templateUrl: '/apps/note/navbar.html',
                    controller: 'NoteItemCtrl'
                  }
                },
                data : { title: '', child: true }
              })
              .state('app.inbox', {
                  url: '/inbox',
                  templateUrl: '/apps/inbox/inbox.html',
                  data : { title: 'Inbox', folded: false },
                  resolve: load( ['/apps/inbox/inbox.js','/libs/jquery/moment/min/moment-with-locales.min.js'] )
              })
              .state('app.inbox.list', {
                  url: '/inbox/{fold}',
                  templateUrl: '/apps/inbox/list.html'
              })
              .state('app.inbox.detail', {
                  url: '/{id:[0-9]{1,4}}',
                  templateUrl: '/apps/inbox/detail.html'
              })
              .state('app.inbox.compose', {
                  url: '/compose',
                  templateUrl: '/apps/inbox/new.html',
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
                  templateUrl: '/views/ui/component/arrow.html',
                  data : { title: 'Arrows' }
                })
                .state('ui.component.badge-label', {
                  url: '/badge-label',
                  templateUrl: '/views/ui/component/badge-label.html',
                  data : { title: 'Badges & Labels' }
                })
                .state('ui.component.button', {
                  url: '/button',
                  templateUrl: '/views/ui/component/button.html',
                  data : { title: 'Buttons' }
                })
                .state('ui.component.color', {
                  url: '/color',
                  templateUrl: '/views/ui/component/color.html',
                  data : { title: 'Colors' }
                })
                .state('ui.component.grid', {
                  url: '/grid',
                  templateUrl: '/views/ui/component/grid.html',
                  data : { title: 'Grids' }
                })
                .state('ui.component.icon', {
                  url: '/icons',
                  templateUrl: '/views/ui/component/icon.html',
                  data : { title: 'Icons' }
                })
                .state('ui.component.list', {
                  url: '/list',
                  templateUrl: '/views/ui/component/list.html',
                  data : { title: 'Lists' }
                })
                .state('ui.component.nav', {
                  url: '/nav',
                  templateUrl: '/views/ui/component/nav.html',
                  data : { title: 'Navs' }
                })
                .state('ui.component.progressbar', {
                  url: '/progressbar',
                  templateUrl: '/views/ui/component/progressbar.html',
                  data : { title: 'Progressbars' }
                })
                .state('ui.component.streamline', {
                  url: '/streamline',
                  templateUrl: '/views/ui/component/streamline.html',
                  data : { title: 'Streamlines' }
                })
                .state('ui.component.timeline', {
                  url: '/timeline',
                  templateUrl: '/views/ui/component/timeline.html',
                  data : { title: 'Timelines' }
                })
                .state('ui.component.uibootstrap', {
                  url: '/uibootstrap',
                  templateUrl: '/views/ui/component/uibootstrap.html',
                  resolve: load('/scripts/controllers/bootstrap.js'),
                  data : { title: 'UI Bootstrap' }
                })
              // material routers
              .state('ui.material', {
                url: '/material',
                template: '<div ui-view></div>',
                resolve: load('/scripts/controllers/material.js')
              })
                .state('ui.material.button', {
                  url: '/button',
                  templateUrl: '/views/ui/material/button.html',
                  data : { title: 'Buttons' }
                })
                .state('ui.material.color', {
                  url: '/color',
                  templateUrl: '/views/ui/material/color.html',
                  data : { title: 'Colors' }
                })
                .state('ui.material.icon', {
                  url: '/icon',
                  templateUrl: '/views/ui/material/icon.html',
                  data : { title: 'Icons' }
                })
                .state('ui.material.card', {
                  url: '/card',
                  templateUrl: '/views/ui/material/card.html',
                  data : { title: 'Card' }
                })
                .state('ui.material.form', {
                  url: '/form',
                  templateUrl: '/views/ui/material/form.html',
                  data : { title: 'Form' }
                })
                .state('ui.material.list', {
                  url: '/list',
                  templateUrl: '/views/ui/material/list.html',
                  data : { title: 'List' }
                })
                .state('ui.material.ngmaterial', {
                  url: '/ngmaterial',
                  templateUrl: '/views/ui/material/ngmaterial.html',
                  data : { title: 'NG Material' }
                })
              // form routers
              .state('ui.form', {
                url: '/form',
                template: '<div ui-view></div>'
              })
                .state('ui.form.layout', {
                  url: '/layout',
                  templateUrl: '/views/ui/form/layout.html',
                  data : { title: 'Layouts' }
                })
                .state('ui.form.element', {
                  url: '/element',
                  templateUrl: '/views/ui/form/element.html',
                  data : { title: 'Elements' }
                })              
                .state('ui.form.validation', {
                  url: '/validation',
                  templateUrl: '/views/ui/form/validation.html',
                  data : { title: 'Validations' }
                })
                .state('ui.form.select', {
                  url: '/select',
                  templateUrl: '/views/ui/form/select.html',
                  data : { title: 'Selects' },
                  controller: 'SelectCtrl',
                  resolve: load(['ui.select','/scripts/controllers/select.js'])
                })
                .state('ui.form.editor', {
                  url: '/editor',
                  templateUrl: '/views/ui/form/editor.html',
                  data : { title: 'Editor' },
                  controller: 'EditorCtrl',
                  resolve: load(['textAngular','/scripts/controllers/editor.js'])
                })
                .state('ui.form.slider', {
                  url: '/slider',
                  templateUrl: '/views/ui/form/slider.html',
                  data : { title: 'Slider' },
                  controller: 'SliderCtrl',
                  resolve: load('/scripts/controllers/slider.js')
                })
                .state('ui.form.tree', {
                  url: '/tree',
                  templateUrl: '/views/ui/form/tree.html',
                  data : { title: 'Tree' },
                  controller: 'TreeCtrl',
                  resolve: load('/scripts/controllers/tree.js')
                })
                .state('ui.form.file-upload', {
                  url: '/file-upload',
                  templateUrl: '/views/ui/form/file-upload.html',
                  data : { title: 'File upload' },
                  controller: 'UploadCtrl',
                  resolve: load(['angularFileUpload', '/scripts/controllers/upload.js'])
                })
                .state('ui.form.image-crop', {
                  url: '/image-crop',
                  templateUrl: '/views/ui/form/image-crop.html',
                  data : { title: 'Image Crop' },
                  controller: 'ImgCropCtrl',
                  resolve: load(['ngImgCrop','/scripts/controllers/imgcrop.js'])
                })
                .state('ui.form.editable', {
                  url: '/editable',
                  templateUrl: '/views/ui/form/xeditable.html',
                  data : { title: 'Xeditable' },
                  controller: 'XeditableCtrl',
                  resolve: load(['xeditable','/scripts/controllers/xeditable.js'])
                })
              // table routers
              .state('ui.table', {
                url: '/table',
                template: '<div ui-view></div>'
              })
                .state('ui.table.static', {
                  url: '/static',
                  templateUrl: '/views/ui/table/static.html',
                  data : { title: 'Static', theme: { primary: 'blue'} }
                })
                .state('ui.table.smart', {
                  url: '/smart',
                  templateUrl: '/views/ui/table/smart.html',
                  data : { title: 'Smart' },
                  controller: 'TableCtrl',
                  resolve: load(['smart-table', '/scripts/controllers/table.js'])
                })
                .state('ui.table.datatable', {
                  url: '/datatable',
                  data : { title: 'Datatable' },
                  templateUrl: '/views/ui/table/datatable.html'
                })
                .state('ui.table.footable', {
                  url: '/footable',
                  data : { title: 'Footable' },
                  templateUrl: '/views/ui/table/footable.html'
                })
                .state('ui.table.nggrid', {
                  url: '/nggrid',
                  templateUrl: '/views/ui/table/nggrid.html',
                  data : { title: 'NG Grid' },
                  controller: 'NGGridCtrl',
                  resolve: load(['ngGrid','/scripts/controllers/nggrid.js'])
                })
                .state('ui.table.uigrid', {
                  url: '/uigrid',
                  templateUrl: '/views/ui/table/uigrid.html',
                  data : { title: 'UI Grid' },
                  controller: "UiGridCtrl",
                  resolve: load(['ui.grid', '/scripts/controllers/uigrid.js'])
                })
                .state('ui.table.editable', {
                  url: '/editable',
                  templateUrl: '/views/ui/table/editable.html',
                  data : { title: 'Editable' },
                  controller: 'XeditableCtrl',
                  resolve: load(['xeditable','/scripts/controllers/xeditable.js'])
                })
              // chart
              .state('ui.chart', {
                url: '/chart',
                templateUrl: '/views/ui/chart/chart.html',
                data : { title: 'Charts' },
                resolve: load('/scripts/controllers/chart.js')
              })
              // map routers
              .state('ui.map', {
                url: '/map',
                template: '<div ui-view></div>'
              })
                .state('ui.map.google', {
                  url: '/google',
                  templateUrl: '/views/ui/map/google.html',
                  data : { title: 'Gmap' },
                  controller: 'GoogleMapCtrl',
                  resolve: load(['ui.map', '/scripts/controllers/load-google-maps.js', '/scripts/controllers/googlemap.js'], function(){ return loadGoogleMaps(); })
                })
                .state('ui.map.vector', {
                  url: '/vector',
                  templateUrl: '/views/ui/map/vector.html',
                  data : { title: 'Vector' },
                  controller: 'VectorMapCtrl',
                  resolve: load('/scripts/controllers/vectormap.js')
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
                templateUrl: '/views/pages/profile.html',
                data : { title: 'Profile', theme: { primary: 'green'} }
              })
              .state('page.settings', {
                url: '/settings',
                templateUrl: '/views/pages/settings.html',
                data : { title: 'Settings' }
              })
              .state('page.blank', {
                url: '/blank',
                templateUrl: '/views/pages/blank.html',
                data : { title: 'Blank' }
              })
              .state('page.document', {
                url: '/document',
                templateUrl: '/views/pages/document.html',
                data : { title: 'Document' }
              })
              .state('404', {
                url: '/404',
                templateUrl: '/views/pages/404.html'
              })
              .state('505', {
                url: '/505',
                templateUrl: '/views/pages/505.html'
              })
              
            ;
        }

        if(isSettigns)
          createSettingsRoutes();
        else
          createHomeRoutes();

         $stateProvider
          .state('access', {
            url: '/access',
            template: '<div class="md-background-default background bg-big"><div ui-view class="fade-in-down smooth"></div></div>',
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
              deps:load( ['angularFileUpload', '/scripts/services/trix.js', '/libs/theming/tinycolor/tinycolor.js', 'mdPickers', 'afkl.lazyImage'] ).deps
            },
            controller: 'AppDataCtrl'
          })
          .state('access.signin', {
            url: '/signin?next',
            templateUrl: '/views/pages/signin.html',
            resolve: load(['/scripts/controllers/app/signin-signup-forgot.js']),
            controller: 'AppSigninCtrl'
          })
          .state('access.signup', {
            url: '/signup',
            templateUrl: '/views/pages/signup.html',
            resolve: load(['/scripts/controllers/app/signin-signup-forgot.js']),
            controller: 'AppSignupCtrl'
          })
          .state('access.forgot-password', {
            url: '/forgot-password',
            templateUrl: '/views/pages/forgot-password.html',
            resolve: load(['/scripts/controllers/app/signin-signup-forgot.js']),
            controller: 'AppForgotCtrl'
          })
          .state('access.lockme', {
            url: '/lockme',
            templateUrl: '/views/pages/lockme.html'
          })
        
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
