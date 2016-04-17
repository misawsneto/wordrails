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
      var rgb2hex = function (rgb) {
        return "#" + hex(rgb[0]) + hex(rgb[1]) + hex(rgb[2]);
      }

      var hex = function (x) {
        var hexDigits = new Array("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"); 
        return isNaN(x) ? "00" : hexDigits[(x - x % 16) / 16] + hexDigits[x % 16];
      }

      $scope.app.rgb2hex = rgb2hex;
      $scope.app.hex = rgb2hex;

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

  .controller('AppDataCtrl', ['$scope', '$translate', '$localStorage', '$window', '$document', '$location', '$rootScope', '$timeout', '$mdSidenav', '$mdColorPalette', '$anchorScroll', 'appData', 'trixService', 'trix', '$filter', '$mdTheming', '$mdColors', 'themeProvider', '$injector', 'colorsProvider', '$mdToast', '$mdDialog', 'FileUploader', 'TRIX', 'cfpLoadingBar',
    function (             $scope,   $translate,   $localStorage,   $window,   $document,   $location,   $rootScope,   $timeout,   $mdSidenav,   $mdColorPalette,   $anchorScroll, appData, trixService, trix, $filter, $mdTheming, $mdColors, themeProvider, $injector, colorsProvider, $mdToast, $mdDialog, FileUploader, TRIX, cfpLoadingBar) {

      $scope.app.goToLink = function(link){
        document.location.href = link;
      }

      function startApp(){

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
            template: '<md-toast class="md-warn-default"><div class="md-toast-content">'+content+'</div></md-toast>',
            hideDelay: 3000,
            position: $scope.getToastPosition()
          });
        };

        $scope.app.showSuccessToast = function(content) {
          $mdToast.show({
            template: '<md-toast class="md-toast-success"><div class="md-toast-content">'+content+'</div></md-toast>',
            hideDelay: 3000,
            position: $scope.getToastPosition()
          });
        };

        $scope.app.showInfoToast = function(content) {
          $mdToast.show({
            template: '<md-toast class="md-toast-info"><div class="md-toast-content">'+content+'</div></md-toast>',
            hideDelay: 3000,
            position: $scope.getToastPosition()
          });
        };

        // ---------- /util-toast -------------
        
        $scope.app.defaultDialog = function(scope, $mdDialog) {
          scope.app = $scope.app;

          scope.hide = function() {
            $mdDialog.hide();
          };

          scope.cancel = function() {
            $mdDialog.cancel();
          }
        };

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

        var themeName = $scope.app.themeName = $filter('generateRandom')(4,"aA");

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
          if($scope.app.network.backgroundColors && $scope.app.network.backgroundColors['50'])
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
      }

      startApp();

      // ---------- /theming ------------------

      // ---------- imageHelper -----------
      
      $scope.getBackgroundImage = function(postView, size){
        var img = $filter('bgImageLink')(postView, size);
        if(postView.externalVideoUrl){
          img = $filter('videoThumb')(postView.externalVideoUrl);
        }
        return img;
      }

      $scope.app.hasImage = function(post){
        return post.hash || post.hashes || post.featuredImage || post.featuredImageHash || post.imageHash;
      }

      $scope.app.getBackgroundImage = function(postView, size){
        return $scope.getBackgroundImage(postView, size);
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
        cfpLoadingBar.set(progress/10)
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
        cfpLoadingBar.set(progress/10)
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
          document.location.href = '/';
        })
      }

      $scope.app.signIn = function(person){
        $scope.app.loading = true;
        trix.login(person.username, person.password).success(function(){
          trix.allInitData().success(function(response){
            appData = initData = response;
            startApp();
            $mdDialog.cancel();
            $scope.app.loading = false;
          }).error(function(){
            $scope.app.loading = false;
          });
        }).error(function(){
          $scope.app.loading = false;
        });
      }

      $scope.app.signInButton =  function(){
        $scope.app.signState = 'signin'
        if($scope.app.person.id === 0){$scope.app.showSigninDialog();}
      }

      $scope.app.showSigninDialog = function(event){
        // show term alert
        
        $mdDialog.show({
          controller: $scope.app.defaultDialog,
          templateUrl: 'signin-signup-dialog.html',
          parent: angular.element(document.body),
          targetEvent: event,
          clickOutsideToClose:true
          // onComplete: function(){

          // }
        })
      }

      // ----------- /signin-signup-forgot --------------

      $scope.app.getStationFromSlug = function(slug){

      }

      $scope.app.getStationFromId = function(id){
        
      }

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
          selectTerms(term.children, termList)
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
      
      appDataCtrl = $scope;
    }
  ]);

var appDataCtrl = null;

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
  return jQuery(this).text();
}

var getLastDigit = function(num){
  if(num){
    var temp = num.toString();
    if(/\d+(\.\d+)?/.test(temp)) { 
        return parseInt(temp[temp.length - 1]);
    }
  }
}