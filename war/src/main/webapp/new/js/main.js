'use strict';

/* Controllers */

angular.module('app')
.controller('AppCtrl', ['$scope', '$translate', '$localStorage', '$window', '$rootScope', '$log', 'trixService', '$filter', '$splash',
  function(              $scope,   $translate,   $localStorage,   $window,   $rootScope,   $log ,  trixService,   $filter ,  $splash) {
      // add 'ie' classes to html
      var isIE = !!navigator.userAgent.match(/MSIE/i);
      isIE && angular.element($window.document.body).addClass('ie');
      isSmartDevice( $window ) && angular.element($window.document.body).addClass('smart');

      // config
      $scope.app = {
        name: 'Angulr',
        version: '2.0.1',
        // for chart colors
        color: {
          primary: '#7266ba',
          info:    '#23b7e5',
          success: '#27c24c',
          warning: '#fad733',
          danger:  '#f05050',
          light:   '#e8eff0',
          dark:    '#3a3f51',
          black:   '#1c2b36'
        },
        settings: {
          themeID: 1,
          navbarHeaderColor: 'bg-dg',
          navbarCollapseColor: 'bg-white-only',
          asideColor: 'bg-dg',
          headerFixed: true,
          asideFixed: false,
          asideFolded: false,
          asideDock: false,
          container: false
        }
      }

      // save settings to local storage
      if ( angular.isDefined($localStorage.settings) ) {
        $scope.app.settings = $localStorage.settings;
      } else {
        $localStorage.settings = $scope.app.settings;
      }
      $scope.$watch('app.settings', function(){
        if( $scope.app.settings.asideDock  &&  $scope.app.settings.asideFixed ){
          // aside dock and fixed must set the header fixed.
          $scope.app.settings.headerFixed = true;
        }
        // save to local storage
        $localStorage.settings = $scope.app.settings;
      }, true);

      /* added */
      $scope.app.settings.navbarHeaderColor = 'bg-dg';
      $scope.app.settings.navbarCollapseColor = 'bg-white-only';
      $scope.app.settings.asideColor = 'bg-dg';
      $scope.app.hidePostOptions = true;
      $scope.app.hidePostMoreOptions = true;
      $scope.app.settings.asideFolded = true; 
      $scope.app.settings.asideFixed = true;
      $scope.app.settings.asideDock = false;
      $scope.app.settings.container = false;
      $scope.app.hideAside = false;
      $scope.app.hideFooter = true;
      // ---------------------
      
      $scope.app.initData = angular.copy(initData);
      $scope.app.currentStation = trixService.selectCurrentStation($scope.app.initData.stations);
      $scope.app.writableStations = trixService.getWritableStations();

      /* end of added */

      // angular translate
      $scope.lang = { isopen: false };
      $scope.langs = {en:'English', de_DE:'German', it_IT:'Italian'};
      $scope.selectLang = $scope.langs[$translate.proposedLanguage()] || "English";
      $scope.setLang = function(langKey, $event) {
        // set the current lang
        $scope.selectLang = $scope.langs[langKey];
        // You can change the language during runtime
        $translate.use(langKey);
        $scope.lang.isopen = !$scope.lang.isopen;
      };

      function isSmartDevice( $window ){
        // Adapted from http://www.detectmobilebrowsers.com
        var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
        // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
        return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
      }

      $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
      });

      $scope.backgroundImage = function(postView, size){
        var img = $filter('pvimageLink')(postView, size);
        return img;
      }

      $scope.openModal = function(templateId, size){
        $scope.modalInstance = $modal.open({
          templateUrl: templateId, // the id of the <script> template
          size: size,
          scope: $scope, // pass the current scope. no need for a new controller
        });
      }

      $scope.openSplash = function(templateId, size){
        $scope.modalInstance = $splash.open({
          templateUrl: templateId,
          scope: $scope
        });
      }

      $scope.cancelModal = function () {
        $scope.modalInstance.dismiss('cancel');
      };

      var sheet = document.createElement('style')
      sheet.innerHTML = getCustomButtonStyle("#cc3300", "#ffffff");
      document.body.appendChild(sheet);

      $scope.app.viewMode = 'vertical';
      $scope.changeView = function(view){
        $scope.app.viewMode = view;
      }

  }]); 