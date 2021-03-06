'use strict';

/**
 * @ngdoc function
 * @name app.controller:AppCtrl
 * @description
 * # MainCtrl
 * Controller of the app
 */
app
  .controller('AppCtrl', ['$scope', '$translate', '$localStorage', '$window', '$document', '$location', '$rootScope', '$timeout', '$mdSidenav', '$mdColorPalette', '$anchorScroll',
    function (             $scope,   $translate,   $localStorage,   $window,   $document,   $location,   $rootScope,   $timeout,   $mdSidenav,   $mdColorPalette,   $anchorScroll ) {
      // add 'ie' classes to html
      var isIE = !!navigator.userAgent.match(/MSIE/i) || !!navigator.userAgent.match(/Trident.*rv:11\./);
      isIE && angular.element($window.document.body).addClass('ie');
      isSmartDevice( $window ) && angular.element($window.document.body).addClass('smart');
      // config
      $scope.app = {
        name: 'Materil',
        version: '1.0.3',
        // for chart colors
        color: {
          primary: '#3f51b5',
          info:    '#2196f3',
          success: '#4caf50',
          warning: '#ffc107',
          danger:  '#f44336',
          accent:  '#7e57c2',
          white:   '#ffffff',
          light:   '#f1f2f3',
          dark:    '#475069'
        },
        setting: {
          theme: {
            primary: 'indigo',
            accent: 'purple',
            warn: 'amber'
          },
          asideFolded: false
        },
        search: {
          content: '',
          show: false
        }
      }

      $scope.setTheme = function(theme){
        $scope.app.setting.theme = theme;
      }

      // save settings to local storage
      if ( angular.isDefined($localStorage.appSetting) ) {
        $scope.app.setting = $localStorage.appSetting;
      } else {
        $localStorage.appSetting = $scope.app.setting;
      }
      // $scope.$watch('app.setting', function(){
      //   $localStorage.appSetting = $scope.app.setting;
      // }, true);

      // angular translate
      $scope.langs = {en:'English', zh_CN:'中文'};
      $scope.selectLang = $scope.langs[$translate.proposedLanguage()] || "English";
      $scope.setLang = function(langKey) {
        // set the current lang
        $scope.selectLang = $scope.langs[langKey];
        // You can change the language during runtime
        $translate.use(langKey);
      };

      function isSmartDevice( $window ) {
        // Adapted from http://www.detectmobilebrowsers.com
        var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
        // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
        return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
      };

      $scope.getColor = function(color, hue){
        if(color == "bg-dark" || color == "bg-white") return $scope.app.color[ color.substr(3, color.length) ];
        return rgb2hex($mdColorPalette[color][hue]['value']);
      }

      //Function to convert hex format to a rgb color
      var rgb2hex = function (rgb) {
        return "#" + hex(rgb[0]) + hex(rgb[1]) + hex(rgb[2]);
      }

      var hex = function (x) {
        var hexDigits = new Array("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"); 
        return isNaN(x) ? "00" : hexDigits[(x - x % 16) / 16] + hexDigits[x % 16];
      }

      $scope.app.rgb2hex = rgb2hex;
      $scope.app.hex = hex;

      $rootScope.$on('$stateChangeSuccess', openPage);

      function openPage() {
        $scope.app.search.content = '';
        $scope.app.search.show = true;
        $scope.closeAside();
        // goto top
        $location.hash('view');
        $anchorScroll();
        $location.hash('');
      }

      $scope.goBack = function () {
        $window.history.back();
      }

      $scope.app.setSeenStations = function(){
        $scope.app.setting.seenStations = true;
        $localStorage.appSetting = $scope.app.setting;
      }

      $scope.openAside = function () {
        $scope.app.setSeenStations();
        $timeout(function() { $mdSidenav('aside').open(); });
      }
      $scope.closeAside = function () {
        $timeout(function() { $document.find('#aside').length && $mdSidenav('aside').close(); });
      }

      var masonryTimeout = null;
      $scope.reloadMasonry = function(){
        if(masonryTimeout)
          $timeout.cancel(masonryTimeout)

        masonryTimeout = $timeout(function(){
          $rootScope.$broadcast('masonry.reload');
        },200)
      }

    }
  ])

  .controller('AppDataCtrl', ['$scope', '$state', '$log', '$translate', '$localStorage', '$window', '$document', '$location', '$rootScope', '$timeout', '$mdSidenav', '$mdColorPalette', '$anchorScroll', 'appData', 'trixService', 'trix', '$filter', '$mdTheming', '$mdColors', 'themeProvider', '$injector', 'colorsProvider', '$mdToast', '$mdDialog', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdMedia', 'amMoment', '$auth', '$http', '$ocLazyLoad',
    function (             $scope, $state, $log, $translate,   $localStorage,   $window,   $document,   $location,   $rootScope,   $timeout,   $mdSidenav,   $mdColorPalette,   $anchorScroll, appData, trixService, trix, $filter, $mdTheming, $mdColors, themeProvider, $injector, colorsProvider, $mdToast, $mdDialog, FileUploader, TRIX, cfpLoadingBar, $mdMedia, amMoment, $auth, $http, $ocLazyLoad) {

      $rootScope.$mdMedia = $mdMedia;
      amMoment.changeLocale('pt');
      $scope.app.getLocalTime = function(date){
        return moment(date).format('lll');
      }

      $scope.app.getTimeOnly = function(date){
        return moment(date).format('h:mm:ss');
      }

      $scope.app.getSecondsOnly = function(date){
        return moment(date).format(':ss');
      }

      $scope.app.goToLink = function(link){
        document.location.href = link;
      }

      $scope.app.goToState = function(state){
        $state.go(state);
      }

      $scope.app.isSettings = function(){
        return document.location.pathname.slice(0, '/settings'.length) == '/settings';
      }

      $scope.app.isAccess = function(){
        return document.location.pathname.slice(0, '/access'.length) == '/access';
      }

      $scope.app.getNetworkLogo = function(){
        if($scope.app.network && $scope.app.network.logoImageHash)
          return '/api/images/get/' + $scope.app.network.logoImageHash + '?size=original';
        else
          return 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=';
      }


      // trix.socialLogin(null, 'ya29.Ci8SAyz6uzLe7dGR79p5UuaUnTbjBul-aIUflX0LZ0I-nTTobetqeP5CnKfOkEngNg'
      //   , 'google');

      function startApp(){

        // ---------- util -------------
        // ---------- util-trix -------------
        $scope.app = angular.extend($scope.app, appData)
        $scope.app.name = $scope.app.network.name

        if($scope.app.network.facebookAppID){
          $scope.app.socialSignIn = function(provider) {
            if(provider === 'facebook'){
              // authenticate w facebook
              $auth.authenticate(provider).then(function(success){ 
                // load facebook user data
                $http.get('https://graph.facebook.com/me?access_token=' + success.access_token, {withCredentials: false}).success(function(response){
                  // signin with facebook
                  trix.socialLogin(response.id, success.access_token, provider).success(function(){
                    // get init data
                    trix.allInitData().success(function(response){
                      appData = initData = response;
                      startApp();
                      $mdDialog.cancel();
                      trix.setUsername(initData.person.username);
                      $scope.app.loading = false;
                    }).error(function(){
                      $scope.app.loading = false;
                    });
                  })
                }).error(function(){
                  $scope.app.loading = false;  
                })
              }).catch(function(error){
                $scope.app.loading = false;
              });
            } else if(provider === 'google') {
              $auth.authenticate(provider).then(function(success){
                window.console && console.log(success)
              }).catch(function(error){
                $scope.app.loading = false;
              });
            }
          };
        }
        
        $scope.app.showSimpleDialog = function(message){
          $scope.app.simpleDialogMessage = message;
          // show term alert
          
          $mdDialog.show({
            scope: $scope,        // use parent scope in template
            closeTo: {
              bottom: 1500
            },
            preserveScope: true, // do not forget this if use parent scope
            controller: $scope.app.defaultDialog,
            templateUrl: 'simple-dialog.html',
            parent: angular.element(document.body),
            clickOutsideToClose:true
            // onComplete: function(){

            // }
          })
        }

        // ---------- /util-trix -------------
        
        // ---------- util-toast -------------
        $scope.toastPosition = {
          bottom: false,
          top: true,
          left: false,
          right: true
        };

        $scope.getToastPosition = function() {
          return Object.keys($scope.toastPosition)
          .filter(function(pos) { return $scope.toastPosition[pos]; })
          .join(' ');
        };

        $scope.app.showSimpleToast = function(content) {
          $mdToast.show(
            $mdToast.simple()
            .content(content)
            .position($scope.getToastPosition())
            .hideDelay(3000)
            );
        };

        $scope.app.showErrorToast = function(content) {
          $mdToast.show({
            template: '<md-toast class="md-warn-default"><div class="md-warn-default background"><i class="mdi2-close-circle i-28 p-r"></i>'+content+'</div></md-toast>',
            hideDelay: 3000,
            position: $scope.getToastPosition()
          });
        };

        $scope.app.showSuccessToast = function(content) {
          $mdToast.show({
            template: '<md-toast class="md-toast-success"><div class="md-toast-content"><i class="mdi2-check-circle i-28 p-r"></i>'+content+'</div></md-toast>',
            hideDelay: 3000,
            position: $scope.getToastPosition()
          });
        };

        $scope.app.showInfoToast = function(content) {
          $mdToast.show({
            template: '<md-toast class="md-toast-info"><div class="md-toast-content"><i class="mdi2-information-outline i-28 p-r"></i>'+content+'</div></md-toast>',
            hideDelay: 3000,
            position: $scope.getToastPosition()
          });
        };

        // ---------- /util-toast -------------

        var postProcessingTemplate = '<md-dialog style="background: none!important; box-shadow:none!important">'+
          '<md-progress-circular class="m-auto" md-mode="indeterminate" md-diameter="200"></md-progress-circular>'+
        '</md-dialog>';

        $scope.app.showLoadingProgress = function(){
          $mdDialog.show({
            controller: $scope.app.defaultDialog,
            template: postProcessingTemplate,
            parent: angular.element(document.body),
            // targetEvent: event,
            clickOutsideToClose:false
            // onComplete: function(){

            // }
          })
        }
        
        $scope.app.defaultDialog = ['scope', '$mdDialog', function(scope, $mdDialog) {
          scope.app = $scope.app;

          scope.hide = function() {
            $mdDialog.hide();
          };

          scope.cancel = function() {
            $mdDialog.cancel();
          }
        }];

        $scope.app.cancelDialog = function() {
          $mdDialog.cancel();
        } 

        $scope.app.getStationById = function(id) {
          var ret = null;
          $scope.app.stations.forEach(function(station){
            if(station.id == id)
              ret = station
          })
          return ret
        }

        $scope.app.getStationBySlug = function(slug){
          var ret = null;
          $scope.app.stations.forEach(function(station){
            if(station.stationSlug == slug)
              ret = station
          })
          return ret
        }

        $scope.app.publicationToShare = null;
        $scope.app.getPublicationLink = function(){
          if($scope.app.publicationToShare)
            return TRIX.baseUrl + '/' + $scope.app.getStationById($scope.app.publicationToShare.stationId).stationSlug  + '/' + $scope.app.publicationToShare.slug;
          else
            return TRIX.baseUrl;
        }

        $scope.app.getPublicationUrl = function(stationId, post){
          if(stationId && post)
            return TRIX.baseUrl + '/' + $scope.app.getStationById(stationId).stationSlug  + '/' + post.slug;
          else
            return TRIX.baseUrl;
        }

        $scope.app.goToPublicationLink = function(stationId, slug){
          if(stationId && slug)
            $state.go('app.station.read', {'stationSlug': $scope.app.getStationById(stationId), 'postSlug': slug})
        }

        $scope.app.getMaterialColor = function(colorA, hueA){
          var colorValue = themeProvider._PALETTES[colorA][hueA] ?
          themeProvider._PALETTES[colorA][hueA].value :
          themeProvider._PALETTES[colorA]['500'].value;

          return 'rgb('+colorValue.join(',')+')';
        }

        $scope.app.getMaterialBGColor = function(colorA, hueA, alpha){
          var colorValue = themeProvider._PALETTES[colorA][hueA] ?
          themeProvider._PALETTES[colorA][hueA].value :
          themeProvider._PALETTES[colorA]['500'].value;

          var bgColor = !alpha ? 'rgb('+colorValue.join(',')+')' : 'rgba('+colorValue.join(',')+ ','+ alpha +')'

          return {'background-color': bgColor, 'color': tinycolor($scope.app.rgb2hex(colorValue)).isLight() ? 'rgba(0,0,0,0.9)' : 'rgba(255,255,255,0.9)'};
        }

        $scope.app.getCategory = function(categoryId){
          var stations = $scope.app.stations;
          for (var i = stations.length - 1; i >= 0; i--) {
            var categories = stations[i].categories;
            for (var i = categories.length - 1; i >= 0; i--) {
              (categories[i].id = categoryId)
            }
          }
        }

        $scope.app.getStation = function(stationId){
          for (var i = $scope.app.stations.length - 1; i >= 0; i--) {
            if($scope.app.stations[i] == stationId)
              return $scope.app.stations[i];
          }
        }

        $scope.app.fullCardCheck = function(index, post){
          //return $scope.app.hasImage(post) && (index%8 == 0 || index == 0) && !$scope.app.largeCardCheck(index-2, post) && $scope.app.largeCardCheck(index+3, post);
          var ind = Number(index+1).toString(14);
          var indLasDigit = ind.slice(ind.length - 1);

          return indLasDigit == 1;
        }

        $scope.app.largeCardCheck = function(index, post){
          // return $scope.app.hasImage(post) && (index%3 == 0 && index != 0) && !$scope.app.fullCardCheck(index + 1, post)
          var ind = Number(index+1).toString(14);
          var indLasDigit = ind.slice(ind.length - 1);

          return (indLasDigit == 5 || indLasDigit == 'b') && !$scope.app.fullCardCheck(index, post);
        }

        $scope.app.smallCardCheck = function(index, post){
          return !$scope.app.largeCardCheck(index, post) && !$scope.app.fullCardCheck(index, post);
        }

        $scope.app.getCategoryLink = function(stationSlug, categoryName){
          var base = $scope.app.isSettings() ? TRIX.baseUrl : '';
          return base +  '/'+stationSlug+'/cat?name='+$scope.app.getEscapedCategory(categoryName);
        }

        $scope.app.stopPropagation = function(e){
          e.stopPropagation();
          e.preventDefault();
        }

        $scope.app.focusOnSearch = function(){
          $('#search form input').focus();


        }

        $scope.app.getEscapedCategory = function(category){
          return window.encodeURIComponent(category)
        }

        // ---------- /util -------------

        // ---------- theming -----------
        
        $scope.app.getCategoryBG = function(category){
          if(category && category.color)
            return {'background-color': category.color, color: tinycolor(category.color).isLight() ? 'rgba(0,0,0,0.9)' : 'rgba(255,255,255,0.9)' }
          return null;
        }

        
        // Function to calculate all colors from base
        // These colors were determined by finding all
        // HSL values for a google palette, calculating
        // the difference in H, S, and L per color
        // change individually, and then applying these
        // here.
        var computeColors = function(hex)
        {
          // Return array of color objects.
          return [
            getColorObject(tinycolor( hex ), '500'),
            getColorObject(tinycolor( hex ).lighten( 52 ), '50'),
            getColorObject(tinycolor( hex ).lighten( 37 ), '100'),
            getColorObject(tinycolor( hex ).lighten( 26 ), '200'),
            getColorObject(tinycolor( hex ).lighten( 12 ), '300'),
            getColorObject(tinycolor( hex ).lighten( 6 ), '400'),
            getColorObject(tinycolor( hex ).darken( 6 ), '600'),
            getColorObject(tinycolor( hex ).darken( 12 ), '700'),
            getColorObject(tinycolor( hex ).darken( 18 ), '800'),
            getColorObject(tinycolor( hex ).darken( 24 ), '900'),
            getColorObject(tinycolor( hex ).lighten( 52 ), 'A100'),
            getColorObject(tinycolor( hex ).lighten( 37 ), 'A200'),
            getColorObject(tinycolor( hex ).lighten( 6 ), 'A400'),
            getColorObject(tinycolor( hex ).darken( 12 ), 'A700')
          ];
        };

        function getColorObject(value, name) {
          var c = tinycolor(value);
          return {
            name: name,
            hex: c.toHexString(),
            darkContrast: c.isLight()
          };
        }
        
        $scope.app.makeColorsJsonObject = function(colors){
          var exportable = {};
          var darkColors = [];
          angular.forEach(colors, function(value, key){
            exportable[value.name] = value.hex;
            if (value.darkContrast) {
              darkColors.push(value.name);
            }
          });
          exportable.contrastDefaultColor = 'light';
          exportable.contrastDarkColors = darkColors.join(' ');
          return exportable;
        };

        var themeName = $scope.app.themeName = $filter('generateRandom')(4,"aA");

        $scope.app.applyNetworkTheme = function (){

          if(!$scope.app.network.primaryColors || !$scope.app.network.primaryColors['50']){
            $scope.app.network.primaryColors =
            $scope.app.network.secondaryColors =
            $scope.app.network.alertColors =
            $scope.app.network.backgroundColors =
            {'500': '#333333', '50': '#b8b8b8', '100': '#919191', '200': '#757575', '300': '#525252', '400': '#424242', '600': '#242424', '700': '#141414', '800': '#050505', '900': '#000000', 'A100': '#b8b8b8', 'A200': '#919191', 'A400': '#424242', 'A700': '#141414', 'contrastDefaultColor': 'light', 'contrastDarkColors': '50 100 A100 A200'}
          }
          
          themeProvider.definePalette('myPrimary', angular.copy($scope.app.network.primaryColors));
          themeProvider.definePalette('myAccent', angular.copy($scope.app.network.secondaryColors));
          themeProvider.definePalette('myWarn', angular.copy($scope.app.network.alertColors));
          if($scope.app.network.backgroundColors && $scope.app.network.backgroundColors['50'])
            themeProvider.definePalette('myBackground', angular.copy($scope.app.network.backgroundColors));
          else
            themeProvider.definePalette('myBackground', $scope.app.makeColorsJsonObject(computeColors($scope.app.network.backgroundColor)))
    
          themeProvider.theme(themeName)
          .primaryPalette('myPrimary')
          .accentPalette('myAccent',{'default':'300', 'hue-1': '500', 'hue-2': '800', 'hue-3': 'A100'})
          .warnPalette('myWarn')
          .backgroundPalette('myBackground', {'default':'500', 'hue-1': '300', 'hue-2': '800', 'hue-3': 'A100'});

          if(!tinycolor($scope.app.network.backgroundColors['500']).isLight()){
            themeProvider.theme(themeName).dark();
          }
    
          themeProvider.reload($injector);
          themeProvider.setDefaultTheme(themeName)
    
          createCustomMDCssTheme(themeProvider, colorsProvider, themeName);
          $scope.app.backgroundPalette = $scope.app.network.backgroundColor
        }

        $scope.app.applyNetworkTheme();

        $scope.app.maxPerm = {
          admin:false,
          editor:false,
          writer:false,
          creator:false
        };
        $scope.app.permissions.stationPermissions.forEach(function(perm){
          if(perm.administration && !$scope.app.maxPerm.admin){
            $scope.app.maxPerm.admin = true;
          } else if(perm.write && perm.moderate && !$scope.app.maxPerm.editor){
            $scope.app.maxPerm.editor = true;
          } else if(perm.write && !$scope.app.maxPerm.writer){
            $scope.app.maxPerm.writer = true;
          } else if(perm.create && !$scope.app.maxPerm.create){
            $scope.app.maxPerm.creator = true;
          }
        })

        $scope.app.totalPending = 0;
        if($scope.app.maxPerm.admin || $scope.app.maxPerm.editor)
          trix.searchPosts('', null, null, 'unpublished', null, null, null, null, 0, 1).success(function(response,a,b,c){
            $scope.app.totalPending = c.totalElements;
          });

        if($scope.app.network.subdomain === 'oabpe')
          $scope.app.stations.forEach(function(station){
            if(station.stationSlug.indexOf('sub-') > -1)
              station.sub=true;
          })

      } // end of startApp

      if(!appData.network){
        window.console && console.info('no network')
        $scope.app.name = '';
        return
      }

      startApp();

      $scope.menu2 = {navigationState: 'home'};

      $scope.app.isLogged = function(){
        if($scope.app.person.id === 0)
          return false;
        else if($scope.app.person.id > 0)
          return true;
      }

      $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
        $rootScope.previousState = fromState.name;
        $rootScope.currentState = toState.name;

        window.console && console.log($rootScope.currentState);

        if($state.includes('app.post')){
          
        }

        if(toState.data && (toState.data.title || toState.titleTranslate)){
          $("title").html($scope.app.network.name + " | " + ((toState.data.titleTranslate) ? $filter('translate')(toState.data.titleTranslate) : toState.data.title));
        }

        $mdDialog.cancel();
      });

      $scope.app.getStationCategories = function(){
        var ret = null
        if($scope.app.termPerspectiveView && $scope.app.termPerspectiveView.ordinaryRows){
          ret = [];
          $scope.app.termPerspectiveView.ordinaryRows && $scope.app.termPerspectiveView.ordinaryRows.forEach(function(row){
            ret.push({
              'id':row.termId,
              'name':row.termName
            })
          })
        }
        return ret ? ret : $scope.app.perspectiveTerms;
      }

      /**
       * Watch value of app.postObjectChanged and set alert messages.
       * @param  boolean newVal
       */
      $scope.app.postObjectChanged = false;
      $scope.$watch('app.postObjectChanged', function(newVal, oldVal){
        if(newVal){
          window.onbeforeunload = function(){ return $filter('translate')('settings.post.messages.PAGE_CHANGE_ALERT') };
        }else{
          window.onbeforeunload = null;
        }
      });

      /**
       * Watch post and set the page not to change if post was edited.
       * The app.postObjectChanged flag is used here.
       * @see $scope.$watch('app.postObjectChanged'
       * @param  {[type]} true    [description]
       */
      $scope.$watch('app.editingPost', function(newVal, oldVal) {
        if(oldVal && (('title' in oldVal) || ('body' in oldVal))){
          // post has been edited

          var newBody = newVal && newVal.body ? newVal.body.stripHtml().replace(/(\r\n|\n|\r)/gm,"") : null;
          var oldBody = oldVal && oldVal.body ? oldVal.body.stripHtml().replace(/(\r\n|\n|\r)/gm,"") : null;

          if(newVal && (newVal.title !== oldVal.title || newBody !== oldBody)){
            // set post changed so $scope.watch can see app.postObjectChanged
            if(!$scope.app.postObjectChanged){
              $log.info('post changed, avoid page change...')
              $scope.app.postObjectChanged = true;
            }
          }
        }
      }, true);

      // deal with unauthorized access
      $scope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){

        if((toState.name == 'app.bookmarks' || toState.name == 'app.notifications') && !$scope.app.isLogged()){
          event.preventDefault();
          $scope.app.showUnauthorized();
          if(fromState.abstract)
            $state.go('app.stations');
        }else if(fromState.name == 'app.post'){
          if($scope.app.editingPost && $scope.app.postObjectChanged){
            if(window.confirm($filter('translate')('settings.post.messages.PAGE_CHANGE_ALERT'))){
              $timeout(function(){
                $scope.app.editingPost = null;
              }, 2000);
              window.onbeforeunload = null;
            }else{
              event.preventDefault();
            }
          }else if($scope.app.editingPost && $scope.app.editingPost.id){
            $timeout(function(){
              $scope.app.editingPost = null;
            }, 2000);
          }
        }

        $scope.app.activeCategory = null;
      })

      $scope.app.loadPerspective = function(station, callback){
        trix.findPerspectiveView(station.defaultPerspectiveId, null, null, 0, 10).success(function(termPerspective){
          $scope.app.termPerspectiveView = termPerspective;
          if($scope.app.network.subdomain === 'oabpe'){
            $scope.app.termPerspectiveView.homeRow = null;
            callback();
            return;
          }

          if($scope.app.termPerspectiveView.homeRow && $scope.app.termPerspectiveView.homeRow.cells){
            $scope.app.termPerspectiveView.homeRow.allLoaded = false;
            var length = $scope.app.termPerspectiveView.homeRow.cells.length >= 10 ? 10 : $scope.app.termPerspectiveView.homeRow.cells.length;
              $scope.app.termPerspectiveView.homeRow.cells = $scope.app.termPerspectiveView.homeRow.cells.slice(0,length);
          }
          $scope.app.loadPerspectiveTerms();
        })
      }

      $scope.app.removeTermTabs = function(){
        $scope.app.loadingTabs = true;
        $scope.app.perspectiveTerms = null;
        $timeout(function(){
          $(window).trigger('resize');
          $scope.app.loadingTabs = false;
        }, 100)
      }

      $scope.app.loadPerspectiveTerms = function(){
        $scope.app.loadingTabs = true;
        $scope.app.perspectiveTerms = null
        $timeout(function(){
          $(window).trigger('resize');
          $timeout(function(){
            $scope.app.perspectiveTerms = $scope.app.getStationCategories();
              if($scope.app.perspectiveTerms && $scope.app.perspectiveTerms.length){
                $timeout(function(){
                  $(window).trigger('resize');
                  $scope.app.loadingTabs = false;
                })
              }
          },700);
        })
      }

      // ---------- /theming ------------------

      // ---------- imageHelper -----------
      
      $scope.getBackgroundImage = function(object, size){
        var img = null;
        // if(object.externalVideoUrl){
        //   img = {"background-image": "url(" + object.externalVideoUrl +")", "background-position": "50% 20%"}; //$filter('videoThumb')(object.externalVideoUrl);
        // }else{
          img = $filter('bgImageLink')(object, size);
        // }
        return img;
      }

      $scope.app.getImageLink = $scope.getImageLink = function(object, size){
        var img = null; 
        // if(object.externalVideoUrl){
        //   img = object.externalVideoUrl; // $filter('videoThumb')(object.externalVideoUrl);
        // }else{
          img = $filter('imageLink')(object, size);
        // }
        return img;
      }

      $scope.app.getImagesPerson = function (post, id, size, type, bg) {
        if(post)
          return $scope.app.hasAuthorImage(post) ? $filter('getImagesPerson')(id, size, type, bg) : null;
        else
          return $filter('getImagesPerson')(id, size, type, bg);
      };

      $scope.app.getPostsImage =  function(id, size, bg) {
        return $filter('getPostsImage')(id, size, bg);
      }

      $scope.app.hasImage = function(post){
        if(post)
          return post.hash || post.hashes || post.featuredImage || post.featuredImageHash || post.imageHash;
        else 
          false;
      }

      $scope.app.hasProfilePicture = function(person){
        return person.imageOriginalHash || person.imageSmallHash || person.imageHash;
      }

      $scope.app.hasProfileCover = function(person){
        return post.coverOriginalHash || post.coverMediumHash || post.coverHash;
      }

      $scope.app.hasAuthorImage = function(post){
        if(post.author)
          return post.author.imageHash || post.author.imageSmallHash || post.author.image || post.author.profilePicture; 
        return post.authorImageHash || post.authorImageSmallHash || post.authorImage || post.authorProfilePicture; 
      }

      $scope.app.getSplash = function(){
        return $scope.app.getBackgroundImage({imageHash: $scope.app.network.splashImageHash}, 'large');
      }

      $scope.app.getBackgroundImage = function(object, size){
        return $scope.getBackgroundImage(object, size);
      }

      $scope.app.userImageSmall = null
      $scope.app.userImageMedium = null
      $scope.app.userImageLarge = null
      var setUserImage = function(person){
        $scope.app.userImageSmall = $filter('userImage')(person, 'small')
        $scope.app.userImageMedium = $filter('userImage')(person, 'medium')
        $scope.app.userImageLarge = $filter('userImage')(person, 'large')
      }

      $scope.app.coverImageSmall = null
      $scope.app.coverImageMedium = null
      $scope.app.coverImageLarge = null
      var setCoverImage = function(person){
        $scope.app.coverImageSmall = $filter('coverImage')(person, 'small')
        $scope.app.coverImageMedium = $filter('coverImage')(person, 'medium')
        $scope.app.coverImageLarge = $filter('coverImage')(person, 'large')
      }

      $scope.app.getUserImage = function(person, size, type, bg){
        if(person.imageHash)
          return $filter('getImagesPerson')(person.id, size, type, bg)
        else
          return null;
      }
      

      $scope.$watch('app.person', function(newVal, oldVal){
        if(newVal){
          setUserImage(newVal);
          setCoverImage(newVal);
        }
      });

      setUserImage($scope.app.person);
      setCoverImage($scope.app.person);

      // ---------- /imageHelper ----------

      // ------------------- image userImageUploader -------------
      // user image upload
      var toastPromise;
      var userImageUploader = $scope.app.userImageUploader = new FileUploader({
        url: TRIX.baseUrl + "/api/images/upload?imageType=PROFILE_PICTURE"
      });

      $scope.uploadedImage = null;
      userImageUploader.onAfterAddingFile = function(fileItem) {
        $scope.uploadedImage = null;
        userImageUploader.uploadAll();
      };

      userImageUploader.onSuccessItem = function(fileItem, response, status, headers) {
        if(response.filelink){
          $scope.uploadedUserImage = response;
          trix.getPerson($scope.app.person.id).success(function(personResponse){
            
            personResponse.image = TRIX.baseUrl + "/api/images/" + $scope.uploadedUserImage.id

            trix.putPerson(personResponse).success(function(){
              $scope.app.person.imageHash = response.hash
              setUserImage($scope.app.person)
            })

          })
          $mdToast.hide();
        }
      };

      userImageUploader.onErrorItem = function(fileItem, response, status, headers) {
        if(status == 413)
          $scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
        else
          $scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
      }

      $scope.clearImage = function(){ 
        $scope.uploadedImage = null;
        userImageUploader.clearQueue();
        userImageUploader.cancelAll()
        $scope.checkLandscape();
        $scope.postCtrl.imageHasChanged = true;
      }

      userImageUploader.onProgressItem = function(fileItem, progress) {
        cfpLoadingBar.start();
        cfpLoadingBar.set(progress/100)
        if(progress == 100){
          cfpLoadingBar.complete()
          toastPromise = $mdToast.show(
            $mdToast.simple()
            .content('Processando...')
            .position('top right')
            .hideDelay(false)
            );
        }
      };

      // cover upload
      var coverImageUploader = $scope.app.coverImageUploader = new FileUploader({
        url: TRIX.baseUrl + "/api/images/upload?imageType=COVER"
      });

      $scope.uploadedImage = null;
      coverImageUploader.onAfterAddingFile = function(fileItem) {
        $scope.uploadedImage = null;
        coverImageUploader.uploadAll();
      };

      coverImageUploader.onSuccessItem = function(fileItem, response, status, headers) {
         if(response.filelink){
          $scope.uploadedCoverImage = response;
          trix.getPerson($scope.app.person.id).success(function(personResponse){
            
            personResponse.cover = TRIX.baseUrl + "/api/images/" + $scope.uploadedCoverImage.id

            trix.putPerson(personResponse).success(function(){
              $scope.app.person.coverHash = response.hash
              setCoverImage($scope.app.person)
            })

          })
          $mdToast.hide();
        }
      };

      coverImageUploader.onErrorItem = function(fileItem, response, status, headers) {
        if(status == 413)
          $scope.app.showErrorToast("A imagem não pode ser maior que 6MBs.");
        else
          $scope.app.showErrorToast("Não foi possível procesar a imagem. Por favor, tente mais tarde.");
      }

      $scope.clearImage = function(){ 
        $scope.uploadedImage = null;
        coverImageUploader.clearQueue();
        coverImageUploader.cancelAll()
        $scope.checkLandscape();
        $scope.postCtrl.imageHasChanged = true;
      }

      coverImageUploader.onProgressItem = function(fileItem, progress) {
        cfpLoadingBar.start();
        cfpLoadingBar.set(progress/100)
        if(progress == 100){
          cfpLoadingBar.complete()
          toastPromise = $mdToast.show(
            $mdToast.simple()
            .content('Processando...')
            .position('top right')
            .hideDelay(false)
            );
        }
      };

      // ------------------- end of image userImageUploader -------------

      // ----------- signin-signup-forgot --------------
      $scope.app.signOut = function(){
        trix.logout().success(function(){
           trix.allInitData().success(function(response){
            appData = initData = response;
            startApp();
            $mdDialog.cancel();
            trix.setUsername(initData.person.username);
            $scope.app.loading = false;

            if($scope.app.isSettings())
              document.location.href = '/';
            else
              $state.go('app.home')

          }).error(function(){
            $scope.app.loading = false;
          });
          trix.resetUsername('');
        })
      }

      $scope.app.signIn = function(person, goToHome){
        $scope.app.loading = true;
        $scope.app.invalidCredentials = false;
        $scope.app.invalidSignup = false
        $scope.app.userExists = false;
        trix.login(person.username, person.password).success(function(){
          trix.allInitData().success(function(response){
            appData = initData = response;
            startApp();
            $mdDialog.cancel();
            trix.setUsername(initData.person.username);
            $scope.app.loading = false;
          }).error(function(){
            $scope.app.loading = false;
          });
        }).error(function(){
          $scope.app.loading = false;
          $scope.app.invalidCredentials = true;
        });
      }

      $scope.app.signUp = function(person, goToHome){
        $scope.app.loading = true;
        $scope.app.invalidCredentials = false;
        $scope.app.invalidSignup = false
        $scope.app.userExists = false;
        trix.createPerson(person).success(function(){
          $scope.app.signIn(person, goToHome);
        }).error(function(data, status, headers, config){
          $scope.app.loading = false;
          if(status == 409){
            $scope.app.userExists = true;
          }else
            $scope.app.invalidSignup = true
        });
      }

      if($scope.app.person && $scope.app.person.id > 0)
        trix.setUsername($scope.app.person.username);

      $scope.app.signInButton =  function(){
        $scope.app.signState = 'signin'
        if($scope.app.person.id === 0){$scope.app.showSigninDialog();}
      }

      $scope.app.showSigninDialog = function(){
        // show term alert
        $scope.app.loading = false;
        $mdDialog.show({
          scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
          preserveScope: true, // do not forget this if use parent scope
          controller: $scope.app.defaultDialog,
          templateUrl: 'signin-signup-dialog.html',
          parent: angular.element(document.body),
          clickOutsideToClose:true
          // onComplete: function(){

          // }
        })
      }

      // ----------- /signin-signup-forgot --------------

      $scope.app.selectTerms = function (terms, termList){
        if(!termList || !terms)
          return;
        var termIds = []
        termList.forEach(function(termItem, index){
          termIds.push(termItem.id)
        });

        terms && terms.forEach(function(term, index){
          if(termIds.indexOf(term.id) > -1)
            term.checked = true;
          $scope.app.selectTerms(term.children, termList)
        });
      }

      $scope.app.getTermList = function (terms, retTerms){
        if(!retTerms)
          retTerms = []

        terms && terms.forEach(function(term, index){
          if(term.checked)
            retTerms.push(term)
          var ts = $scope.app.getTermList(term.children)
          ts.forEach(function(t){
            retTerms.push(t)
          });
        });
        return retTerms;
      }

      $scope.app.getTermUris = function(terms){
        var termList = $scope.app.getTermList(terms)
        var termUris = []
        termList.forEach(function(term){
          term.id = term.id ? term.id : term.termId;
          if(term.id)
            termUris.push(TRIX.baseUrl + "/api/terms/" + term.id);
        })
        return termUris;
      }

      $timeout(function() {
        $("#aside-scroller").perfectScrollbar({
          wheelSpeed: 1,
          wheelPropagation: true,
          minScrollbarLength: 20
        });
      });

      // -------------------------- 
      
      $scope.app.goToSearch =function(query){
        $state.go('app.search', {q: query});
      }

      $scope.actionButtonColors = $scope.app.getMaterialColor('myBackground', '700');
      
      $scope.app.date = new Date();

      // --------------------------
      
      // --------- generic post tab
      
      var setPostFeaturedImage = function(hash){
        $scope.app.postLoaded.postFeaturedImage = $filter('imageLink')({imageHash: hash}, 'large')
      }

        function buildToggler(navID) {
          return function() {
            $mdSidenav(navID)
              .toggle()
              .then(function () {
                $log.debug("toggle " + navID + " is done");
              });
          }
        }
      $scope.togglePost = buildToggler('post-summary');

      $scope.app.showPost = function(post){
          $scope.app.postLoaded = post;
          var hash = $scope.app.postLoaded.imageHash;
          setPostFeaturedImage(hash)
          $scope.togglePost();
          post.terms = post.categories;
      }

      $scope.app.showPostContent = function(post){
          trix.getPost(post.id, 'postProjection').success(function(response){
            $scope.app.postLoaded = response;
            var hash = $scope.app.postLoaded.imageHash ? $scope.app.postLoaded.imageHash : $scope.app.postLoaded.featuredImage ? $scope.app.postLoaded.featuredImage.originalHash : null;
            setPostFeaturedImage(hash)
            $scope.togglePost();
          })
      }

      // --------- /generic post tab

      // --------- generic comment tab
      
      function buildToggler(navID) {
        return function() {
          $mdSidenav(navID)
            .toggle()
            .then(function () {
              $log.debug("toggle " + navID + " is done");
            });
        }
      }

      $scope.app.page = 0;
      $scope.app.loadingComments = false
      $scope.app.commentsAllLoaded = false;
      $scope.app.window = 20;

      // sidenav toggle
      $scope.app.toggleComments = buildToggler('post-comments');

      $scope.app.showComments = function(post){
        post.id = post.id?post.id:post.postId
        $scope.app.toggleComments();
        $scope.app.comments = []
        $scope.app.postLoaded = post;
        $scope.app.commentsPage = 0;
        $scope.app.loadingComments = $scope.app.commentsAllLoaded = false
        $scope.app.paginateComments();
      }

      $scope.app.paginateComments = function(){
        if(!$scope.app.loadingComments && $scope.app.postLoaded && $scope.app.postLoaded.id && !$scope.app.commentsAllLoaded){
          $scope.app.loadingComments = true;
          trix.findPostCommentsOrderByDate($scope.app.postLoaded.id, $scope.app.commentsPage, $scope.app.window, null, 'commentProjection').success(function(response){
            if(response.comments && response.comments.length > 0){
              response.comments.forEach(function(comment){
                if(!$scope.app.comments)
                  $scope.app.comments = [];
                $scope.app.comments.push(comment);
              })
              $scope.app.commentsPage ++;
            }else{
              $scope.app.commentsAllLoaded = true
            }
            $scope.app.loadingComments = false
          }).error(function(){
            $scope.app.comments = null;
            $scope.app.loadingComments = false
          })  
        }
      }

      $scope.app.loadComments = function(post){

        if(post.showComments)
          return;

        post.showComments = true; 
        $scope.reloadMasonry();

        post.page = 0;
        post.loadingComments = false
        post.commentsAllLoaded = false;
        post.window = 20;
        post.comments = []
        post.commentsPage = 0;
        post.loadingComments = false

        post.paginateComments = function(post){
          if(!post.loadingComments && post.id && !post.commentsAllLoaded){
            post.loadingComments = post.loadingBar = true;
            trix.findPostCommentsOrderByDate(post.id, post.commentsPage, post.window, null, 'commentProjection').success(function(response){
              if(response.comments && response.comments.length > 0){
                response.comments.forEach(function(comment){
                  post.comments.push(comment);
                })
                post.commentsPage ++;
              }else{
                post.commentsAllLoaded = true
              }
              post.loadingComments = false

              $timeout(function(){
                post.loadingBar = false;
              }, Math.floor((Math.random() * 500) + 100));

              $scope.reloadMasonry();
              $timeout(function(){
                $('#comment-list-' + post.id).perfectScrollbar({
                  wheelSpeed: 1,
                  wheelPropagation: true,
                  minScrollbarLength: 40
                });
              }, 100);
            }).error(function(){
              post.comments = null;
              post.loadingComments = false
            })  
          }
        }

        post.paginateComments(post);

        post.newComment = '';
        post.postComment = function(postObj, body){

          if($scope.app.person.id == 0){
            $scope.app.signInButton();
            return;
          }

          postObj.newComment = '';
          var comment = {
            post: PostDto.getSelf(postObj),
            author: PersonDto.getSelf($scope.app.person),
            body: body
          }
          trix.postComment(comment).success(function(response){
            var c = {
              post: postObj,
              author: $scope.app.person,
              body: body,
              date: new Date()
            }

            if(!post.comments || !post.comments.length)
              post.comments = [];

            post.comments.unshift(c);
            post.commentsCount++;
            $scope.reloadMasonry();
            $timeout(function(){
                $('#comment-list-' + post.id).perfectScrollbar({
                  wheelSpeed: 1,
                  wheelPropagation: true,
                  minScrollbarLength: 40
                });
              }, 100);
          })
        }
      }

      $scope.app.commentFocused = false;
      $scope.app.commentFocus = function(){
        $scope.app.commentFocused = true;
      }
      $scope.app.commentBlur = function(){
        $scope.app.commentFocused = false;
      }


      $scope.app.newComment = '';
      $scope.app.postComment = function(post, body){
        if($scope.app.person.id == 0){
          $scope.app.signInButton();
          return;
        }
        var comment = {
          post: PostDto.getSelf(post),
          author: PersonDto.getSelf($scope.app.person),
          body: body
        }
        trix.postComment(comment).success(function(response){
          var c = {
            post: post,
            author: $scope.app.person,
            body: body,
            date: new Date()
          }

          if(!$scope.app.comments || !$scope.app.comments.length)
            $scope.app.comments = [];

          $scope.app.comments.unshift(c);
          post.commentsCount++;
          $scope.app.newComment = '';
        })
      }

      $scope.app.showSharesPostDialog = function(event, post){
        // show term alert
        
        if(post.station)
          post.stationId = post.station.id

        $scope.app.publicationToShare = post;
        $mdDialog.show({
          scope: $scope,        // use parent scope in template
            closeTo: {
              bottom: 1500
            },
          preserveScope: true, // do not forget this if use parent scope
          controller: $scope.app.defaultDialog,
          templateUrl: 'social-share-dialog.html',
          parent: angular.element(document.body),
          targetEvent: event,
          clickOutsideToClose:true
          // onComplete: function(){

          // }
        })
      }

      // --------- /generic comment tab

      // --------- generic bookmark

      $scope.app.isBookmarked = function(post){
        if($scope.app.person.bookmarkPosts)
          return $scope.app.person.bookmarkPosts.indexOf(post.id) > -1;
        else
          return false
      }

      $scope.bookmarkApply = false;
      $scope.app.toggleBookmark = function(post){

        if($scope.app.person.id == 0){
          $scope.app.signInButton();
          return;
        }

        if(!$scope.bookmarkApply){
          $scope.bookmarkApply = true;
          trix.toggleBookmark(post.id).success(function(person){
            if($scope.app.isBookmarked(post)){
              for (var i = $scope.app.person.bookmarkPosts.length - 1; i >= 0; i--) {
                if($scope.app.person.bookmarkPosts[i] == post.id){
                  $scope.app.person.bookmarkPosts.splice(i, 1);
                }
              }
            }else{
              if(!$scope.app.person.bookmarkPosts)
                $scope.app.person.bookmarkPosts = [];
              $scope.app.person.bookmarkPosts.unshift(post.id)
            }
            $mdDialog.cancel();
            $scope.disabled = $scope.bookmarkApply = false;
          })
        }
      }
      // --------- /generic bookmark
      // --------- generic recommend

      $scope.app.isRecommended = function(post){
        if($scope.app.person.recommendPosts)
          return $scope.app.person.recommendPosts.indexOf(post.id) > -1;
        else
          return false;
      }

      $scope.recommendApply = false;
      $scope.app.toggleRecommend = function(post){

        if($scope.app.person.id == 0){
          $scope.app.signInButton();
          return;
        }

        if(!$scope.recommendApply){
          $scope.recommendApply = true;
          trix.toggleRecommend(post.id).success(function(response){
            if(!response.response){
              for (var i = $scope.app.person.recommendPosts.length - 1; i >= 0; i--) {
                if($scope.app.person.recommendPosts[i] == post.id){
                  $scope.app.person.recommendPosts.splice(i, 1);
                  post.recommendsCount--;
                }
              }
            }else{
              if(!$scope.app.person.recommendPosts)
                $scope.app.person.recommendPosts = []
              $scope.app.person.recommendPosts.push(post.id)
              post.recommendsCount++;
            }
            $mdDialog.cancel();
            $scope.disabled = $scope.recommendApply = false;
          })
        }
      }

      appDataCtrl = $scope;

      // --------- /generic bookmark
  
      $scope.app.loadingNotifications = false;
      $scope.page = 0;
      $scope.size = 20;
      $scope.app.nnotifications = [
        {
          person: {
            email:"contato@xarx.co",
            id: 51,
            imageHash:"bc3c2042f9c3474ccbebd9b8b40533c4",
            imageLargeHash:"bc3c2042f9c3474ccbebd9b8b40533c4",
            imageMediumHash:"481f14dff0ce2699429706515b9fb73b",
            imageSmallHash:"3a10d25d632ec336bc31bc1e6f17f8e1",
            name:"Demo",
            username:"demo"
          },
          message: "Isso é um texto",
          type: "POST_ADDED"
        }
      ] 
      $scope.app.getNotification = function(){
        $scope.app.loadingNotifications = true;
        // trix.searchNotifications(null, $scope.page, $scope.size).success(function(response){
        //   $scope.app.loadingNotifications = false;

        // })
      }

      $scope.app.getNotification();
  }])

  .controller('AppNetworkCtrl', ['$scope', '$state', '$log', '$translate', '$localStorage', '$window', '$document', '$location', '$rootScope', '$timeout', '$mdSidenav', '$mdColorPalette', '$anchorScroll', 'appData', 'trixService', 'trix', '$filter', '$mdTheming', '$mdColors', 'themeProvider', '$injector', 'colorsProvider', '$mdToast', '$mdDialog', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdMedia', 'amMoment',
    function (             $scope, $state, $log, $translate,   $localStorage,   $window,   $document,   $location,   $rootScope,   $timeout,   $mdSidenav,   $mdColorPalette,   $anchorScroll, appData, trixService, trix, $filter, $mdTheming, $mdColors, themeProvider, $injector, colorsProvider, $mdToast, $mdDialog, FileUploader, TRIX, cfpLoadingBar, $mdMedia, amMoment) {
  }]);

var appDataCtrl = null;

String.prototype.trim = function(){
  var str = this;
  str = str.replace(/^\s+|\s+$/g, '');
  return str;
}

String.prototype.toSlug = function(){
  var str = this;
  str = str.replace(/^\s+|\s+$/g, ''); // trim
  str = str.toLowerCase();

  // remove accents, swap ñ for n, etc
  var from = "ãàáäâẽèéëêìíïîõòóöôùúüûñç·/_,:;";
  var to   = "aaaaaeeeeeiiiiooooouuuunc------";
  for (var i=0, l=from.length ; i<l ; i++) {
    str = str.replace(new RegExp(from.charAt(i), 'g'), to.charAt(i));
  }

  str = str.replace(/[^a-z0-9 -]/g, '') // remove invalid chars
    .replace(/\s+/g, '-') // collapse whitespace and replace by -
    .replace(/-+/g, '-'); // collapse dashes

    return str;
}

String.prototype.getYoutubeCode = function(){
  var url = this;
  var regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
  var match = url.match(regExp);
  if (match && match[2].length == 11) {
    return match[2];
  } else {
    return null;
  }
}

String.prototype.stripHtml = function(){
  var string = '<i>' + this + '</i>';
  return jQuery(string).text();
}

String.prototype.simpleSnippet = function(){
  var string = this.stripHtml();
  var splitPhrase = string.split("\\s+");

  var newArray = [];
  for (var i = 0; i < splitPhrase.length || i < 100; i++) {
    newArray[i] = splitPhrase[i];
  }

  return newArray.join(' ');
}

var getLastDigit = function(num){
  if(num){
    var temp = num.toString();
    if(/\d+(\.\d+)?/.test(temp)) { 
        return parseInt(temp[temp.length - 1]);
    }
  }
}