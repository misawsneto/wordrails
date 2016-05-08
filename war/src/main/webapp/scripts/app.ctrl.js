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
      $scope.app.hex = hex;

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

  .controller('AppDataCtrl', ['$scope', '$state', '$log', '$translate', '$localStorage', '$window', '$document', '$location', '$rootScope', '$timeout', '$mdSidenav', '$mdColorPalette', '$anchorScroll', 'appData', 'trixService', 'trix', '$filter', '$mdTheming', '$mdColors', 'themeProvider', '$injector', 'colorsProvider', '$mdToast', '$mdDialog', 'FileUploader', 'TRIX', 'cfpLoadingBar', '$mdMedia', 'amMoment',
    function (             $scope, $state, $log, $translate,   $localStorage,   $window,   $document,   $location,   $rootScope,   $timeout,   $mdSidenav,   $mdColorPalette,   $anchorScroll, appData, trixService, trix, $filter, $mdTheming, $mdColors, themeProvider, $injector, colorsProvider, $mdToast, $mdDialog, FileUploader, TRIX, cfpLoadingBar, $mdMedia, amMoment) {

      $scope.reloadMasonry = function(){
        $rootScope.$broadcast('masonry.reload');
      }

      $rootScope.$mdMedia = $mdMedia;
      amMoment.changeLocale('pt');
      $scope.app.getLocalTime = function(date){
        return moment(date).format('lll');
      }

      $scope.app.goToLink = function(link){
        document.location.href = link;
      }

      $scope.app.goToState = function(state){
        $state.go(state);
      }

      function startApp(){

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
            templateUrl: 'simple_dialog.html',
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
            template: '<md-toast class="md-warn-default"><div class="md-toast-content"><i class="mdi2-close-circle i-28 p-r"></i>'+content+'</div></md-toast>',
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
            template: '<md-toast class="md-toast-info"><div class="md-toast-content"><i class="mdi2-info-circle i-28 p-r"></i>'+content+'</div></md-toast>',
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
          return $scope.app.hasImage(post) && (index%8 == 0 || index == 0) && !$scope.app.largeCardCheck(index-2, post) && $scope.app.largeCardCheck(index+3, post);
        }

        $scope.app.largeCardCheck = function(index, post){
          return $scope.app.hasImage(post) && (index%3 == 0 && index != 0) && !$scope.app.fullCardCheck(index + 1, post)
        }

        $scope.app.smallCardCheck = function(index){
          return !$scope.app.largeCardCheck(index);
        }

        $scope.app.getCategoryLink = function(stationSlug, categoryName){
          return '/'+stationSlug+'/cat?name='+categoryName;
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
    
          themeProvider.reload($injector);
          themeProvider.setDefaultTheme(themeName)
    
          createCustomMDCssTheme(themeProvider, colorsProvider, themeName);
          $scope.app.backgroundPalette = $scope.app.network.backgroundColor
        }

        $scope.app.applyNetworkTheme();
      } // end of startApp

      startApp();

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
        if(oldVal && ('title' in oldVal) && ('body' in oldVal)){
          // post has been edited

          if(newVal && (newVal.title !== oldVal.title || 
                      newVal.body.stripHtml().replace(/(\r\n|\n|\r)/gm,"") !== oldVal.body.stripHtml().replace(/(\r\n|\n|\r)/gm,""))){

            // TODO: save draft

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
      })

      // ---------- /theming ------------------

      // ---------- imageHelper -----------
      
      $scope.getBackgroundImage = function(object, size){
        var img = $filter('bgImageLink')(object, size);
        if(object.externalVideoUrl){
          img = $filter('videoThumb')(object.externalVideoUrl);
        }
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
          document.location.href = '/';
        })
      }

      $scope.app.signIn = function(person, goToHome){
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
          scope: $scope,        // use parent scope in template
          closeTo: {
            bottom: 1500
          },
          preserveScope: true, // do not forget this if use parent scope
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
      
      appDataCtrl = $scope;
      $scope.app.date = new Date();

      // --------------------------
      
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

      $scope.app.showSharesPostDialog = function(event){
        // show term alert
        
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
        return $scope.app.person.bookmarkPosts.indexOf(post.id) > -1;
      }

      $scope.bookmarkApply = false;
      $scope.app.toggleBookmark = function(post){

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
              $scope.app.person.bookmarkPosts.push(post.id)
            }
            $mdDialog.cancel();
            $scope.disabled = $scope.bookmarkApply = false;
          })
        }
      }
      // --------- /generic bookmark
      // --------- generic recommend

      $scope.app.isRecommended = function(post){
        return $scope.app.person.recommendPosts.indexOf(post.id) > -1;
      }

      $scope.recommendApply = false;
      $scope.app.toggleRecommend = function(post){

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
              $scope.app.person.recommendPosts.push(post.id)
              post.recommendsCount++;
            }
            $mdDialog.cancel();
            $scope.disabled = $scope.recommendApply = false;
          })
        }
      }

      // --------- /generic bookmark
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

var getLastDigit = function(num){
  if(num){
    var temp = num.toString();
    if(/\d+(\.\d+)?/.test(temp)) { 
        return parseInt(temp[temp.length - 1]);
    }
  }
}