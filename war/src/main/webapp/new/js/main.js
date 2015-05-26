'use strict';

/* Controllers */

angular.module('app')
.controller('AppCtrl', ['$scope', '$translate', '$localStorage', '$window', '$rootScope', '$log', 'trixService', '$filter', '$splash', '$modal', 'trix', '$state', '$http', 'JQ_CONFIG', 'uiLoad', '$timeout',
  function(              $scope,   $translate,   $localStorage,   $window,   $rootScope,   $log ,  trixService ,  $filter ,  $splash ,  $modal ,  trix ,  $state ,  $http ,  JQ_CONFIG ,  uiLoad ,  $timeout) {
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
       
      function loadPopular(){
        trix.findPopularPosts($scope.app.currentStation.id, 0, 10)
        .success(function(response){
        })
      }

      function loadRecent(){
        trix.findRecentPosts($scope.app.currentStation.id, 0, 10)
        .success(function(response){
        })
      }
      
      $scope.app.initData = angular.copy(initData);
      $scope.app.currentStation = trixService.selectDefaultStation($scope.app.initData.stations);
      $("title").html($scope.app.currentStation ? $scope.app.initData.network.name + " | " + $scope.app.currentStation.name : $scope.app.initData.network.name);

      $scope.app.checkIfLogged = function(){
        $scope.app.isLogged = trixService.isLoggedIn();
        $scope.app.writableStations = trixService.getWritableStations();
      }
      $scope.app.checkIfLogged();

      uiLoad.load(JQ_CONFIG.screenfull)
      $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
        if(typeof screenfull !== 'undefined' && screenfull){ screenfull.exit(); }
        if(toState.name == "app.stations.read"){
          $("body").addClass("show-post")
        }else{

          $('.station-header').removeClass('nav-up').addClass('nav-down');
          $("body").removeClass("show-post")
        }
      });

      $scope.getBackgroundImage = function(postView, size){
        var img = $filter('pvimageLink')(postView, size);
        return img;
      }

      $scope.getImageLink = function(id){
        var img = $filter('imageLink')(id);
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

      if ( angular.isDefined($localStorage.viewMode) ) {
        $scope.app.viewMode = $localStorage.viewMode
      }else{
        $scope.app.viewMode = 'vertical';
      }
      
      $scope.changeView = function(view){
        $localStorage.viewMode = $scope.app.viewMode = view;
      }

      $scope.app.getInitData = function(){
        trix.allInitData().success(function(response){
          initData = response;
          $scope.app.initData = angular.copy(initData);
        })
      }

      $scope.app.signIn = function(username, password){
        trix.login(username, password).success(function(){
          trix.allInitData().success(function(response){
            initData = response;
            $scope.app.initData = angular.copy(initData);
            $scope.cancelModal();
            $scope.app.checkIfLogged()
          })
        })
      };

      $scope.app.signOut = function(username, password){
        trix.logout().success(function(){
          trix.allInitData().success(function(response){
            initData = response;
            $scope.app.initData = angular.copy(initData);
            $scope.app.checkIfLogged();
            $scope.app.profilepopover.open = false;
            if($scope.$state.current.name != "app.stations" && $scope.$state.current.name != "app.search"){
              $state.go("app.stations");
            }
          })
        })
      };

      $scope.app.changeStation = function(stationId){

      }

      moment.locale('pt')
      loadPopular();
      loadRecent();

      trix.findPerspectiveView($scope.app.currentStation.defaultPerspectiveId)

      $scope.app.clodePostRead = function(){
        $state.go('^')
        $timeout(function(){
          $scope.app.nowReading = null;
        }, 300)
      }

      /**
       * helper function, see sly-scroll directive 
       */
      $scope.app.setHorizontalCursor = function(postView, cells){
        $scope.app.horizontalCursor = {
          postView: postView,
          cells: cells
        }
      }

      /**
       * manage the post that is been read and the view behavior
       * @param {[type]} postView [description]
       * @param {[type]} cells    [description]
       */
      $scope.app.setNowReading = function(postView, cells){
        if($scope.app.nowReading && $scope.app.nowReading.postId == postView.postId)
          return;

        if(cells)
          $scope.app.currentCells = cells;

        if(!$scope.app.nowReading && postView){
          $scope.app.nowReadingAuthor = {
            authorId: postView.authorId,
            imageSmallId: postView.authorImageSmallId,
            coverMediumId: postView.authorCoverMediumId,
            authorName: postView.authorName
          }
        }else if(postView && $scope.app.nowReading && postView.authorId != $scope.app.nowReading.authorId){
          $scope.app.nowReadingAuthor = {
            authorId: postView.authorId,
            imageSmallId: postView.authorImageSmallId,
            coverMediumId: postView.authorCoverMediumId,
            authorName: postView.authorName
          }
        }

        var oldId = $scope.app.nowReading ? $scope.app.nowReading.postId : null;
        $scope.app.nowReading = null;
        $scope.app.incomingPostDirection = null;

        $timeout(function() {
          if($scope.app.currentCells && oldId && oldId > postView.postId)
            $scope.app.incomingPostDirection = "slideInRight";
          else
            $scope.app.incomingPostDirection = "slideInLeft";

          $scope.app.nowReading = postView;
          $state.go('app.stations.read',{slug: postView.slug}); 

        }, 50);
      }

      /* end of added */
  }]); 