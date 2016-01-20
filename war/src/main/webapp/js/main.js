'use strict';

/* Controllers */

angular.module('app')
.controller('AppCtrl', ['$scope', '$localStorage', '$window', '$rootScope', '$log', 'trixService', '$filter', '$splash', '$modal', 'trix', '$state', '$http', 'JQ_CONFIG', 'uiLoad', '$timeout', '$mdDialog', '$interval', '$mdToast', 'TRIX', 'cfpLoadingBar', '$q', '$mdSidenav',
  function(              $scope,   $localStorage,   $window,   $rootScope,   $log ,  trixService ,  $filter ,  $splash ,  $modal ,  trix ,  $state ,  $http ,  JQ_CONFIG ,  uiLoad ,  $timeout ,  $mdDialog ,  $interval ,  $mdToast, TRIX   ,  cfpLoadingBar ,  $q ,  $mdSidenav) {
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

    $scope.app.isMobile = false;

    $interval(function() {
      if($('body').width() < 768)
        $scope.app.isMobile = true;
      else
        $scope.app.isMobile = false;

    }, 300);

    $interval(function(){
      if(trixService.isLoggedIn()){
        $scope.app.hideAside = false;
        $scope.app.isLogged = trixService.isLoggedIn();
      }else{
        $scope.app.hideAside = true;
        $scope.app.isLogged = trixService.isLoggedIn();
      }
    }, 200);

    $scope.$on('HTTP_ERROR', function(data){
      safeApply($scope, function(){
        cfpLoadingBar.complete()
      })
    })

    if(!initData)
      return;

    $scope.app.initData = angular.copy(initData);
    $scope.app.termPerspectiveView = angular.copy(initTermPerspective);

    angularHttp = $http;
    trixSdk = trix

    function loadPopular(){
      trix.findPopularPosts($scope.app.currentStation.id, 0, 10)
      .success(function(response){
        $scope.app.initData.popular = response
      })
    }

    function loadRecent(){
      trix.findRecentPosts($scope.app.currentStation.id, 0, 10)
      .success(function(response){
        $scope.app.initData.recent = response
      })
    }

    $scope.app.stopPropagation = function($event){
      $event.stopPropagation();
    }

    $scope.app.stopPropagationAndPrevent = function($event){
      $event.stopPropagation();
      $event.preventDefault();
    }

    $scope.app.changeStation = function(station, dontChangePage){

      var stationObject = null;
      $scope.app.initData.stations.forEach(function(st){
        if(station.stationId == st.id)
          stationObject = st
      });

      $scope.app.termPerspectiveView = null;

      trix.findPerspectiveView(stationObject.defaultPerspectiveId, null, null, 0, 10).success(function(termPerspective){
        $scope.app.termPerspectiveView = termPerspective
      })

      $scope.app.currentStation = stationObject;

      if(!dontChangePage)
        $state.go('app.stations')

      loadPopular();
      loadRecent();
      $scope.app.checkIfLogged();

      $scope.app.closeSelectStation();
    }

    $scope.app.refreshPerspective = function(perspectiveId){
      $scope.app.termPerspectiveView = null;

      var id = perspectiveId ? perspectiveId : $scope.app.currentStation.defaultPerspectiveId

      trix.findPerspectiveView(id, null, null, 0, 10).success(function(termPerspective){
        $scope.app.termPerspectiveView = termPerspective
      })

      $scope.app.checkIfLogged();
    }

    function checkLoginImage(){
      if(initData.network.loginImageId)
        initData.network.loginImageLink = TRIX.baseUrl + "/api/files/" + initData.network.loginImageId + "/contents"
    }

    checkLoginImage();

    $scope.app.getLoggedPerson = function(){
      return $scope.app.initData.person;
    }

    $scope.app.checkIfLogged = function(){
      $scope.app.isLogged = trixService.isLoggedIn();
      $scope.app.writableStations = trixService.getWritableStations();
      $scope.app.adminStations = trixService.getAdminStations();
      $scope.app.networkAdmin = trixService.isNetworkAdmin();
      $scope.app.editorStations = trixService.getEditorStations();
    }

    $scope.app.stationIsAdmin = function(stationId){
      return trixService.stationIsAdmin();
    }

    uiLoad.load(JQ_CONFIG.screenfull)

    $rootScope.previousState;
    $rootScope.currentState;

    $rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
      $rootScope.previousState = fromState.name;
      $rootScope.currentState = toState.name;

      window.console && console.log($rootScope.currentState);

        // on change state, exit if in fullscreen mode.
        if(typeof screenfull !== 'undefined' && screenfull){ screenfull.exit(); }

        if(fromState.name == "app.stations.read"){
          if($scope.app.nowReading.externalVideoUrl)
            $scope.app.nowReading.externalVideoUrl = $scope.app.nowReading.externalVideoUrl.substring(0, $scope.app.nowReading.externalVideoUrl.length - 1) + "0";
        }

        if(toState.name != "app.read"){
          $timeout(function(){
            $scope.app.nowReading = null;
          }, 300)
        }

        if(toState.name == "app.stations"){
          $("title").html($scope.app.currentStation ? $scope.app.initData.network.name + " | " + $scope.app.currentStation.name : $scope.app.initData.network.name);
        }
        else if(toState.name.indexOf("app.post") > -1){
          $("title").html($scope.app.initData.network.name + " | Editor");
        }
        else if(toState.name == "app.notifications"){
          $("title").html($scope.app.initData.network.name + " | Notificações");
        }
        else if(toState.name == "app.bookmarks"){
          $("title").html($scope.app.initData.network.name + " | Minhas Leituras");
        }
        else if(toState.name.indexOf("app.settings") > -1){
          $("title").html($scope.app.initData.network.name + " | Configurações");
        }
        else if(toState.name == "app.search"){
          $("title").html($scope.app.initData.network.name + " | Busca");
        }
        else if(toState.name == "app.user"){
          $("title").html($scope.app.initData.network.name + " | @"+toParams.username);
        }
        else if(toState.name == "app.publications"){
          $("title").html($scope.app.initData.network.name + " | Publicações");
        }

        else if(toState.name == "app.mystats"){
          $("title").html($scope.app.initData.network.name + " | Publicações");
        }
        // check state read
        if(toState.name != "app.stations.read"){
            // show to navbar
            $('body').removeClass('nav-up').addClass('nav-down');
            $("body").removeClass("show-post") // this class is added to the body element in read.js
          }

        });

    // deal with unauthorized access
    $scope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){

      if(!(toState.name.indexOf('access.') > -1) && !trixService.isLoggedIn() && initData.noVisibleStation){
          $mdToast.show({
              template: '<md-toast style="background-color: rgba(0,85,187,0.95)" ><span flex>Está estação é restrita!<br>Autentique-se para acessar o conteúdo.</span></md-toast>',
              hideDelay: 3000,
              position: 'top right'
            });
          $timeout(function() {
            $state.go('access.signin');
          });
      }else if((toState.name == 'app.bookmarks' || toState.name == 'app.notifications' || toState.name.indexOf('app.settings') > -1) && !trixService.isLoggedIn()){
        event.preventDefault();
        $scope.app.showInfoToast('Autentique-se para acessar esta função.')
        if(fromState.abstract)
          $state.go('app.stations');
      }else if(toState.name == 'app.post' && (!trixService.getWritableStations() || trixService.getWritableStations().length == 0)){
        event.preventDefault();
        $scope.app.showInfoToast('Você não possui permissão para criar notícias.')
        if(fromState.abstract)
          $state.go('app.stations');
      }else if( (toState.name.indexOf('app.user') > -1 && (toParams.username === "wordrails")) ||
        (toState.name == 'app.publications' &&
          ((!trixService.getWritableStations() || trixService.getWritableStations().length == 0) ||
            ($scope.app.initData && $scope.app.initData.person && $scope.app.initData.person.username !== toParams.username))
          )
        ){
        event.preventDefault();
        $scope.app.showInfoToast('Permissão negada.')
        if(fromState.abstract)
          $state.go('app.stations');
      }else if(fromState.name == 'app.post'){
        if($scope.app.editingPost && $scope.app.editingPost.editingExisting){
          if(confirm('Este conteúdo está sendo editado. Deseja descartar as alterações?')){
            $scope.app.editingPost = null;
            window.onbeforeunload = null;
          }else{
            event.preventDefault();
          }
        }else if($scope.app.editingPost && $scope.app.editingPost.id){
          $scope.app.editingPost = null;
        }
      }
    })

      $scope.app.goBack = function(){
        if($rootScope.previousState)
          $window.history.back();
        else
          $state.go('app.stations');
      }

      $scope.app.socialLogin = function(){
        return $scope.app.initData.network.allowSocialLogin && $scope.app.currentStation.visibility == 'UNRESTRICTED';
      }

      $scope.app.goToState = function(state, param){
        $state.go(state,param);
      }

      $scope.app.goToPage = function(url){
        window.open(url,'_blank');
      }

      $scope.app.goToEditPost = function(postId, $event){
        $event.preventDefault();
        $event.stopPropagation();
        $state.go('app.post',{'id': postId});
      }

      $scope.app.goToProfile = function($event, username){
        $event.preventDefault();
        $event.stopPropagation();
        $state.go('app.user', {username: username})
        if($scope.app.profilepopover)
          $scope.app.profilepopover.open = false;
      }

      $scope.app.goToUserStats = function($event){
        $event.preventDefault();
        $event.stopPropagation();
        $state.go('app.userstats')
        if($scope.app.profilepopover)
          $scope.app.profilepopover.open = false;
      }

      $scope.app.goToUserPublications = function($event){
        $event.preventDefault();
        $event.stopPropagation();

        if($scope.app.publicationsCtrl)
          $scope.app.publicationsCtrl.page = 0;

        $state.go('app.publications', {username: $scope.app.getLoggedPerson().username, type: 'publications'})
        if($scope.app.profilepopover)
          $scope.app.profilepopover.open = false;
      }

      $scope.app.goToUserDrafts = function($event){
        $event.preventDefault();
        $event.stopPropagation();

        if($scope.app.publicationsCtrl)
          $scope.app.publicationsCtrl.page = 0;

        $state.go('app.publications', {username: $scope.app.getLoggedPerson().username, type: 'drafts'})
        if($scope.app.profilepopover)
          $scope.app.profilepopover.open = false;
      }

      $scope.app.goToUserScheduled = function($event){
        $event.preventDefault();
        $event.stopPropagation();

        if($scope.app.publicationsCtrl)
          $scope.app.publicationsCtrl.page = 0;

        $state.go('app.publications', {username: $scope.app.getLoggedPerson().username, type: 'scheduled'})
        if($scope.app.profilepopover)
          $scope.app.profilepopover.open = false;
      }

      $scope.app.goToUserOtherPosts = function($event){
        $event.preventDefault();
        $event.stopPropagation();

        if($scope.app.publicationsCtrl)
          $scope.app.publicationsCtrl.page = 0;

        $state.go('app.publications', {username: $scope.app.getLoggedPerson().username, type: 'others'})
        if($scope.app.profilepopover)
          $scope.app.profilepopover.open = false;
      }

      $scope.getBackgroundImage = function(postView, size){
        var img = $filter('pvimageLink')(postView, size);
        if(postView.externalVideoUrl){
          img = $filter('videoThumb')(postView.externalVideoUrl);
        }
        return img;
      }

      $scope.app.getBackgroundImage = function(postView, size){
        return $scope.getBackgroundImage(postView, size);
      }

      $scope.getBackgroundImage2 = function(post, size){
        var img = $filter('pvimageLink2')(post, size);
        return img;
      }

      $scope.app.getBackgroundImage2 = function(post, size){
        return $scope.getBackgroundImage2(post, size);
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
        if(templateId === "signin_splash.html"){

          $timeout(function(){
            $("#username-input").focus();
          }, 300);

          if($scope.app.isMobile){
            $state.go('access.signin')
            return
          }
        }
        $scope.modalInstance = $splash.open({
          templateUrl: templateId,
          scope: $scope
        });
        $timeout(function(){
          $(".splash").addClass('splash-open')
        })
      }

      $scope.cancelModal = function () {
        $(".splash").removeClass('splash-open')
        $timeout(function(){
          if($scope.modalInstance){
            $scope.modalInstance.dismiss('cancel');
          }
        },150)
      };

      $scope.app.cancelModal = function () {
        $scope.cancelModal();
      };

      $scope.app.openSplash = function(templateId, size){
        $scope.openSplash(templateId, size)
      }

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
          template: '<md-toast style="background-color: rgba(204,34,0,0.95)" ><span flex>'+content+'</span></md-toast>',
          hideDelay: 3000,
          position: $scope.getToastPosition()
        });
      };

      $scope.app.showSuccessToast = function(content) {
        $mdToast.show({
          template: '<md-toast style="background-color: rgba(0,128,0,0.95)" ><span flex>'+content+'</span></md-toast>',
          hideDelay: 3000,
          position: $scope.getToastPosition()
        });
      };

      $scope.app.showInfoToast = function(content) {
        $mdToast.show({
          template: '<md-toast style="background-color: rgba(0,85,187,0.95)" ><span flex>'+content+'</span></md-toast>',
          hideDelay: 3000,
          position: $scope.getToastPosition()
        });
      };

      $scope.app.converToDraf = function(){
        if($scope.app.editingPost && $scope.app.editingPost.id){
          trix.convertPost($scope.app.editingPost.id, "DRAFT").success(function(){
            $scope.app.editingPost.state = "DRAFT";
            $scope.app.cancelModal();
          });
        }
      }

      $scope.app.openDeletePost = function(id, $event, dontChangeState){
        if($event){
          $event.preventDefault();
          $event.stopPropagation();
        }
        $scope.app.deletingPostId = id;
        $scope.app.changeStateOnDelete = dontChangeState;
        $scope.app.openSplash('confirm_delete_post.html')
      };

      $scope.app.deletePost = function(){
        trix.deletePost($scope.app.deletingPostId).success(function(){
          $scope.$broadcast('POST_REMOVED', $scope.app.deletingPostId)

          $scope.app.deletingPostId = null;
          $scope.app.editingPost = null;
          window.onbeforeunload = null;
          $scope.app.showSuccessToast('Notícia removida.')
          $scope.app.cancelModal();
          if($state.current.name != 'app.stations' && !$scope.app.changeStateOnDelete)
            $state.go('app.stations');


          $scope.app.refreshPerspective();
        });
      }

      if ( angular.isDefined($localStorage.viewMode) ) {
        $scope.app.viewMode = $localStorage.viewMode
      }else{
        $scope.app.viewMode = 'vertical';
      }

      $scope.app.viewMode = 'vertical';

      $scope.app.changeMobileViewMode = function(){
        if($scope.app.viewMode == 'vertical') {
          $scope.app.viewMode = $localStorage.viewMode = 'horizontal'
        }else{
          $scope.app.viewMode = $localStorage.viewMode = 'vertical'
        }
      }

      $scope.changeView = function(view){
        $localStorage.viewMode = $scope.app.viewMode = view;
      }

      if ( angular.isDefined($localStorage.nightMode) ) {
        $scope.app.nightMode = $localStorage.nightMode
      }else{
        $scope.app.nightMode = false;
      }

      $scope.toggleNightMode = function(bool){
        $localStorage.nightMode = $scope.app.nightMode = bool;
      }

      $scope.app.getInitData = function(){
        trix.allInitData().success(function(response){
          initData = response;
          checkLoginImage();
          $scope.app.initData = angular.copy(initData);
          $scope.app.loginError = false;
          $scope.app.refreshData();
        })
      }

      $scope.app.signIn = function(username, password){
        trix.login(username, password).success(function(){
          if($state.current.name == 'access.signin'){
            window.location.href=window.location.protocol + '//' + window.location.host
            return;
          }
          handleLoginSuccess();
        }).error(function(){
          $scope.app.loginError = true;
          $scope.app.refreshData();
        })
      };

      $scope.app.signup = function(user){
        trix.createPerson(user).success(function(response){
          window.location.href=window.location.protocol + '//' + window.location.host + "?signupSuccess=true"
          return;
        }).error(function(data, status, headers, config){
          if(status == 409){
            $scope.app.conflictingData = data;

            if($scope.app.conflictingData.value && user.email && $scope.app.conflictingData.value.indexOf(user.email) > -1){
              $scope.app.showErrorToast('Este email está sendo utilizado. <br>Tente novamente.')
            }else if($scope.app.conflictingData.value && user.username && $scope.app.conflictingData.value.indexOf(user.username) > -1){
              $scope.app.showErrorToast('Este userário está sendo utilizado. <br>Tente novamente.')
            }
            $scope.app.showErrorToast('Dados inválidos. Tente novamente')
          }else
            $scope.app.showErrorToast('Dados inválidos. Tente novamente')
          $timeout(function() {
            cfpLoadingBar.complete(); 
          }, 100);
        });
      }

      function handleLoginSuccess (){
        trix.allInitData().success(function(response){
          initData = response;
          $scope.app.initData = angular.copy(initData);
          $scope.app.loginError = false;
          $scope.app.refreshData();
        })
      }

      $scope.app.changeToSettings = function(){
        if(!$scope.app.lastSettingState)
          $scope.app.lastSettingState = 'app.settings.stations'
        $state.go($scope.app.lastSettingState);
      }

      $scope.app.refreshData = function(){
        $scope.app.currentStation = trixService.selectDefaultStation($scope.app.initData.stations, $scope.app.currentStation ? $scope.app.currentStation.stationId : null);
        $scope.app.stationsPermissions = trixService.getStationPermissions();
        if(!$scope.app.loginError)
          $scope.cancelModal();

        $scope.app.checkIfLogged();
        //window.console && console.log($scope.app.currentStation, $scope.app.stationsPermissions);
        trix.getAllTerms($scope.app.currentStation.defaultPerspectiveId).success(function(terms){
          $scope.app.currentStation.perspectiveTerms = terms;
          terms && terms.forEach(function(term){
            // $http.get(TRIX.baseUrl + "/api/terms/" + term.termId + "/image").success(function(data, status, headers){
            //   if(status == 304)
            //     console.log(header);
            // });
            // isImage(TRIX.baseUrl + "/api/terms/" + term.termId + "/image").then(function(test) {
            //     console.log(test);
            // });
          })
        })
      }

      function isImage(src) {
        var deferred = $q.defer();
        var image = new Image();
        image.onerror = function() {
            deferred.resolve(false);
        };
        image.onload = function() {
            deferred.resolve(true);
        };
        image.src = src;
        return deferred.promise;
      }

      $scope.app.signOut = function(username, password){
        trix.logout().success(function(){
          trix.allInitData().success(function(response){
            initData = response;
            checkLoginImage();
            $scope.app.initData = angular.copy(initData);
            $scope.app.profilepopover.open = false;
            if($scope.$state.current.name != "app.stations" && $scope.$state.current.name != "app.search"){
              $state.go("app.stations");
            }
            $scope.app.currentStation = null;
            $scope.app.refreshData();
            if(initData && initData.noVisibleStation)
              $state.go('access.signin');
          })
        })
      };


    // close post read and go back to previous state
    $scope.app.closePostRead = function(){
        // $state.go('^')
        $window.history.back();
        $timeout(function(){
          $scope.app.nowReading = null;
        }, 300)
      }

    /**
     * helper function, see sly-scroll directive
     */
     $scope.app.setHorizontalCursor = function(postView, list){
      $scope.app.horizontalCursor = {
        postView: postView,
        list: list
      }
    }

    $scope.app.cellsToPostViews = function(cells){
      return cells.map(function(cell){ return cell.postView; });
    }

    $scope.app.reading = function(slug){
      $state.go('app.read', {'slug': slug})
    }

    /**
     * manage the post that is been read and the view behavior
     * @param {[type]} postView [description]
     * @param {[type]} list    [description]
     */
     $scope.app.setNowReading = function(postView, list, listMeta, baseState){

        // if($scope.app.nowReadingAuthor && postView && postView.authorId && $scope.app.nowReadingAuthor.authorId != postView.authorId){
        //   $("#left-profile-cover").removeClass("slideInRight");
        //   setTimeout(function() {
        //     $("#left-profile-cover").addClass("slideInRight");
        //   });
        // }

        if($state.current.name == "app.stations.read" && $scope.app.nowReading && $scope.app.nowReading.postId == postView.postId)
          return;

        if(list)
          $scope.app.readList = list;

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
            // if(baseState)
            $scope.app.incomingPostDirection = "fadeIn"
            // else if($scope.app.readList && oldId && oldId > postView.postId)
            //   $scope.app.incomingPostDirection = "slideInRight";
            // else
            //   $scope.app.incomingPostDirection = "slideInLeft";

            $scope.app.nowReading = postView;
            if(!$scope.app.nowReadingMeta)
              $scope.app.nowReadingMeta = {};

            $scope.app.nowReadingMeta.baseState = baseState;
            $scope.app.nowReadingMeta.listMeta = listMeta;
            if(baseState)
              $state.go(baseState + '.read',{slug: postView.slug});
            else
              $state.go('app.stations.read',{slug: postView.slug});

          });
      }

      /*  angular-material design temp fix */
      /*$interval(function(){
        $("body").removeAttr("style");
      },2)*/

      $scope.app.doSearch = function(query){
        $state.go('app.search', {'q': query})
      }

      $scope.goToBookmars = function(){
        if($scope.app.isLogged)
          $state.go('app.bookmarks')
        else{
          $scope.openSplash('signin_splash.html')
        }
      }

      $scope.goToNotifications = function(){
        if($scope.app.isLogged)
          $state.go('app.notifications')
        else
          $scope.openSplash('signin_splash.html')
      }

      $scope.app.addBookmarked = function(postId){
        if(!$scope.app.initData.bookmarks || $scope.app.initData.bookmarks.length == 0)
          $scope.app.initData.bookmarks = [];
        if(!$scope.app.bookmarked(postId)){
          $scope.app.initData.bookmarks.push(postId);
          $scope.app.showSimpleToast('Enviado a Minha Lista.')
        }
      }

      $scope.app.removeBookmarked = function(postId){
        if($scope.app.initData.bookmarks && $scope.app.initData.bookmarks.length > 0){
          var index = $scope.app.initData.bookmarks.indexOf(postId)
          if(index > -1)
            $scope.app.initData.bookmarks.splice(index, 1);
        }
      }

      $scope.app.bookmarked = function(postId){
        var bool = $scope.app.initData.bookmarks && $scope.app.initData.bookmarks.length > 0 ?  $scope.app.initData.bookmarks.indexOf(postId) > -1 : false;
        return bool;
      }

      $scope.app.addPostRead = function(postId){
        if(!$scope.app.initData.postsRead || $scope.app.initData.postsRead.length == 0)
          $scope.app.initData.postsRead = [];
        if(!$scope.app.postRead(postId))
          $scope.app.initData.postsRead.push(postId);
      }

      $scope.app.postRead = function(postId){
        var bool = $scope.app.initData.postsRead && $scope.app.initData.postsRead.length > 0 ? $scope.app.initData.postsRead.indexOf(postId) > -1 : false;
        return bool;
      }

      $scope.app.addRecommended = function(postId){
        if(!$scope.app.initData.recommends || $scope.app.initData.recommends.length == 0)
          $scope.app.initData.recommends = [];
        if(!$scope.app.recommended(postId)){
          $scope.app.initData.recommends.push(postId);
          $scope.app.showSimpleToast('Você recomendou essa publicação.')
        }
      }

      $scope.app.removeRecommended = function(postId){
        if($scope.app.initData.recommends && $scope.app.initData.recommends.length > 0){
          var index = $scope.app.initData.recommends.indexOf(postId)
          if(index > -1)
            $scope.app.initData.recommends.splice(index, 1);
        }
      }

      $scope.app.recommended = function(postId){
        var bool = $scope.app.initData.recommends && $scope.app.initData.recommends.length > 0 ? $scope.app.initData.recommends.indexOf(postId) > -1 : false;
        return bool;
      }

      $scope.app.bookmark = function(postId){
        if($scope.app.isLogged){
          trix.toggleBookmark(postId).success(function(response){
            if(response.response){
              $scope.app.addBookmarked(postId)
            }else{
              $scope.app.removeBookmarked(postId)
            }
            console.log(response);
          }).error(function(){
            console.log('error');
          })
        }else
        $scope.openSplash('signin_splash.html')
      }

      $scope.app.recommend = function(postId){
        if($scope.app.isLogged){
          trix.toggleRecommend(postId).success(function(response){
            if(response.response){
              $scope.app.addRecommended(postId)
            }else{
              $scope.app.removeRecommended(postId)
            }
          }).error(function(){
            console.log('error');
          })
        }else
        $scope.openSplash('signin_splash.html')
      }

      $scope.app.stripHtml = function(text){
        return text ? text.stripHtml : null;
      }

      $scope.app.getCurrentStateName = function(){
        return $state.current.name;
      }

      $scope.app.movePostToTrash = function(){

      }

      $scope.app.facebookSignIn = function () {
          FB.getLoginStatus(function (response) {
              if (response.status === 'connected') {
                  // the user is logged in and has authenticated your
                  // app, and response.authResponse supplies
                  // the user's ID, a valid access token, a signed
                  // request, and the time the access token
                  // and signed request each expire
                  var uid = response.authResponse.userID;
                  var accessToken = response.authResponse.accessToken;
                  handleFacebookLogin(response);
              } else {
                  // the user isn't logged in to Facebook.
                  FB.login(function (response) {
                      if (response.authResponse) {
                          handleFacebookLogin(response);
                      } else {
                          $scope.app.cancelModal();
                      }
                  }, {scope: 'public_profile,email'});
              }
          });

      }

      $scope.app.twitterSignIn = function(){
      }

      function handleFacebookLogin(authResponse){
        if(authResponse)
          trix.socialLogin(authResponse.authResponse.userID, authResponse.authResponse.accessToken, "facebook").success(function(){
            handleLoginSuccess();
          })

        FB.api('/me?fields=id,name,email', function(me) {
          console.log(me);
        },{scope: 'public_profile,email', fields: 'id,name,email'});
      }

      $scope.app.refreshData();
      moment.locale('pt')
      /* end of added */

      /* header observer */

      var lastScrollTop = 0
    var didScroll = false;
    $timeout(function(){
      lastScrollTop = $(window).scrollTop() ? $(window).scrollTop() : 0;
    }, 20)

    function checkStationHeaderVisible(scrollTop){
        // Make sure they scroll more than delta
        if(Math.abs(lastScrollTop - scrollTop) <= 5)
          return;
        // If they scrolled down and are past the navbar, add class .nav-up.
        // This is necessary so you never see what is "behind" the navbar.
        if (scrollTop > lastScrollTop && scrollTop > 50){
            // Scroll Down
            $('body').removeClass('nav-down').addClass('nav-up');
        } else {
            // Scroll Up
            if(scrollTop + $(window).height() < $(document).height()) {
              $('body').removeClass('nav-up').addClass('nav-down');
            }
        }
        lastScrollTop = scrollTop;
    }

    $timeout(function(){
      checkStationHeaderVisible(lastScrollTop);
      $(window).scroll(function(){
        didScroll = true;
      })
    }, 70);

    $interval(function() {
      if (didScroll) {
        checkStationHeaderVisible($(window).scrollTop());
        didScroll = false;
      }
    }, 250);

    $scope.app.toggleSelectStation = function(){
      $mdSidenav('stations-selector').toggle();
    }

    $scope.app.closeSelectStation = function(){
      $mdSidenav('stations-selector').close();
    }

    $scope.app.goToSearch = function(searchQuery){
      if(searchQuery && searchQuery.trim() != "")
        $state.go('app.search', {'q': searchQuery});
    }

    var homeSettingsAnimateTimeout = null;

    $scope.app.animateHomeSettings = function(){
      if(homeSettingsAnimateTimeout){
        homeSettingsAnimateTimeout.cancel();
        $scope.app.homeSettingsAnimate = false
      }

      $scope.app.homeSettingsAnimate = true
      $timeout(function() {
        $scope.app.homeSettingsAnimate = false;
      }, 1000);
    }

    $scope.app.goToHomeOrSettings = function(){
      if($state.includes('app.settings'))
        $state.go('app.stations')
      else
        $scope.app.changeToSettings()
    }

    $scope.app.year = moment().format('YYYY')

    $scope.app.mediaUrl = function(hash){
      return mediaUrl(hash); 
    }
}]);