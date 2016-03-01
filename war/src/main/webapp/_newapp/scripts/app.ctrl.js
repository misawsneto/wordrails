'use strict';

/**
 * @ngdoc function
 * @name app.controller:AppCtrl
 * @description
 * # MainCtrl
 * Controller of the app
 */
angular.module('app')  
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
      $scope.$watch('app.setting', function(){
        $localStorage.appSetting = $scope.app.setting;
      }, true);

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
      function rgb2hex(rgb) {
        return "#" + hex(rgb[0]) + hex(rgb[1]) + hex(rgb[2]);
      }

      function hex(x) {
        var hexDigits = new Array("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"); 
        return isNaN(x) ? "00" : hexDigits[(x - x % 16) / 16] + hexDigits[x % 16];
      }

      $rootScope.$on('$stateChangeSuccess', openPage);

      function openPage() {
        $scope.app.search.content = '';
        $scope.app.search.show = false;
        $scope.closeAside();
        // goto top
        $location.hash('view');
        $anchorScroll();
        $location.hash('');
      }

      $scope.goBack = function () {
        $window.history.back();
      }

      $scope.openAside = function () {
        $timeout(function() { $mdSidenav('aside').open(); });
      }
      $scope.closeAside = function () {
        $timeout(function() { $document.find('#aside').length && $mdSidenav('aside').close(); });
      }

    }
  ])

  .controller('AppDataCtrl', ['$scope', '$translate', '$localStorage', '$window', '$document', '$location', '$rootScope', '$timeout', '$mdSidenav', '$mdColorPalette', '$anchorScroll', 'appData', 'trixService', 'trix', '$filter', '$mdTheming', '$mdColors', 'themeProvider', '$injector', 'colorsProvider', '$mdToast',
    function (             $scope,   $translate,   $localStorage,   $window,   $document,   $location,   $rootScope,   $timeout,   $mdSidenav,   $mdColorPalette,   $anchorScroll, appData, trixService, trix, $filter, $mdTheming, $mdColors, themeProvider, $injector, colorsProvider, $mdToast) {

      //window.console && console.log(appData);
      // ---------- util -------------
      // ---------- util-trix -------------
      $scope.app = angular.extend($scope.app, appData)
      $scope.app.name = $scope.app.network.name
      $scope.app.currentStation = trixService.selectDefaultStation($scope.app.stations, $scope.app.currentStation ? $scope.app.currentStation.stationId : null);
      $scope.app.stationsPermissions = trixService.getStationPermissions(angular.copy($scope.app));

      var id = $scope.app.currentStation.defaultPerspectiveId
      trix.findPerspectiveView(id, null, null, 0, 10).success(function(termPerspective){
        $scope.app.termPerspectiveView = termPerspective
      })

      $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
        $rootScope.previousState = fromState.name;
        $rootScope.currentState = toState.name;

        if(toState.data && (toState.data.title || toState.titleTranslate)){
          $("title").html($scope.app.network.name + " | " + ((toState.data.titleTranslate) ? $filter('translate')(toState.data.titleTranslate) : toState.data.title));
        }

        window.console && console.log($rootScope.currentState);
      });
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
          template: '<md-toast class="md-warn-default">'+content+'</md-toast>',
          hideDelay: 3000,
          position: $scope.getToastPosition()
        });
      };

      $scope.app.showSuccessToast = function(content) {
        $mdToast.show({
          template: '<md-toast class="md-toast-success" >'+content+'</md-toast>',
          hideDelay: 3000,
          position: $scope.getToastPosition()
        });
      };

      $scope.app.showInfoToast = function(content) {
        $mdToast.show({
          template: '<md-toast class="md-toast-info" >'+content+'</md-toast>',
          hideDelay: 3000,
          position: $scope.getToastPosition()
        });
      };

      // ---------- /util-toast -------------
      // ---------- /util -------------

      // ---------- theming -----------
      
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

      var themeName = $filter('generateRandom')(4,"aA");

      $scope.app.applyNetworkTheme = function (){

        if(!$scope.app.network.primaryColors || !$scope.app.network.primaryColors['50']){
          $scope.app.network.primaryColors =
          $scope.app.network.secondaryColors =
          $scope.app.network.alertColors =
          $scope.app.network.backgroundColors =
          {'500': '#333333', '50': '#b8b8b8', '100': '#919191', '200': '#757575', '300': '#525252', '400': '#424242', '600': '#242424', '700': '#141414', '800': '#050505', '900': '#000000', 'A100': '#b8b8b8', 'A200': '#919191', 'A400': '#424242', 'A700': '#141414', 'contrastDefaultColor': 'light', 'contrastDarkColors': '50 100 A100 A200'}
        }
        
        themeProvider.definePalette('myPrimary', $scope.app.network.primaryColors);
        themeProvider.definePalette('myAccent', $scope.app.network.secondaryColors);
        themeProvider.definePalette('myWarn', $scope.app.network.alertColors);
        if($scope.app.network.backgroundColors && $scope.app.network.backgroundColors.length > 0)
          themeProvider.definePalette('myBackground', $scope.app.network.backgroundColors);
        else
          themeProvider.definePalette('myBackground', $scope.app.makeColorsJsonObject(computeColors($scope.app.network.backgroundColor)))
  
        themeProvider.theme(themeName)
        .primaryPalette('myPrimary')
        .accentPalette('myAccent',{'default':'300', 'hue-1': '500', 'hue-2': '800', 'hue-3': 'A100'})
        .warnPalette('myWarn')
        .backgroundPalette('myBackground', {'default':'500', 'hue-1': '300', 'hue-2': '800', 'hue-3': 'A100'});
  
        themeProvider.reload($injector);
        themeProvider.setDefaultTheme(themeName)
  
        createCustomMDCssTheme(themeProvider, colorsProvider, themeName);
        $scope.app.backgroundPalette = $scope.app.network.backgroundColor
      }

      $scope.app.applyNetworkTheme();

      // ---------- /theming ----------

    }
  ]);
