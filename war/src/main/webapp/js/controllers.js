'use strict';
/* Controllers */

angular.module('app.controllers', ['pascalprecht.translate', 'ngCookies'])

.controller('AppCtrl', function AppCtrl($q, $rootScope, $scope, $state, $modal, $translate,  $localStorage, $window, authService, wordrailsService, WORDRAILS, $filter, $splash) {
    // add 'ie' classes to html
    var isIE = !!navigator.userAgent.match(/MSIE/i);
    isIE && angular.element($window.document.body).addClass('ie');
    isSmartDevice( $window ) && angular.element($window.document.body).addClass('smart');

    // config
    $scope.app = {
      name: ' ',
      version: '1.1.1',
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
        navbarHeaderColor: 'bg-black',
        navbarCollapseColor: 'bg-white-only',
        asideColor: 'bg-black',
        headerFixed: true,
        asideFixed: true,
        asideFolded: true
      }
    }

    $scope.app.loading = false; // init loading variable that shows loading bar

    $scope.backgroundImage = function(postView, size){
      var img = $filter('pvimageLink')(postView, size);
      return img;
    }

    $scope.$on('NETWORK_LOADED', function(event, network){
      safeApply($scope, function(){
        $scope.app.name = network.name;
        $scope.app.network = network;
        $rootScope.appTitle = network.name
        $scope.app.authenticated = authService.isAuth();
      })
    });

    $scope.$on('PERSON_LOADED', function(event, person){
      safeApply($scope, function(){
        $scope.app.person = person
        $scope.app.authenticated = authService.isAuth();
      })
    });

    $scope.$on('CONNECTION_PROBLEM', function(event){
      safeApply($scope, function(){
        $scope.app.loading = false;
      })
    })

    /**
     * Global submit search function
     * @param  String searchQuery the search string
     */
     $scope.app.submitSearch = function(searchQuery){
      // if state is not app.stations.search, go to it...
      if($state.current.name != "app.stations.search"){
        $state.go("app.stations.search");
      }
    }

    /*------------------------------------------------------------------------*/
    // save settings to local storage
    if ( angular.isDefined($localStorage.settings) ) {
      $scope.app.settings = $localStorage.settings;
    } else {
      $localStorage.settings = $scope.app.settings;
    }
    $scope.$watch('app.settings', function(){ $localStorage.settings = $scope.app.settings; }, true);

    // angular translate
    $scope.langs = {en:'English', de_DE:'German', it_IT:'Italian'};
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

    $scope.signIn = function(username, password){
      authService.signIn(username, password, function(){
        //console.log(wordrailsService.getInitialData());
        var d = wordrailsService.getInitialData(true)
        d.then(function(){safeApply($scope, function(){});})
        var person = authService.getPerson()
        safeApply($scope, function(){
          $scope.app.person = person
        })
        $state.go("app.stations", null, {reload: true});
        //location.reload();
        $scope.cancelModal();
        $scope.app.authenticated = authService.isAuth();
      },
      
      function(){
        safeApply($scope, function(){
          console.log('fafa');
          $scope.signInError = true;
        });
      });
    }

    $scope.logout = function(){
      $scope.toggleMenu();
      authService.logout();
      $state.go("app.stations", {stationId: 0}, {reload: true});
      //location.reload();
      $rootScope.hidePersonInfo = true;
      $scope.app.authenticated = authService.isAuth();
      safeApply($rootScope, function(){})
    }

})

   // ---------------------- StationsCtrl
   .controller('StationsCtrl', function StationsCtrl($scope, $modal, $state, authService, WORDRAILS, wordrailsService){
    var wr = authService.getWR();
    var changeToStationId = null;

    $scope.app.authenticated = authService.isAuth();

    safeApply($scope, function(){
      $scope.app.network = wordrailsService.getNetwork();
    });
    /**
     * When a new station is added to the app.network.stations, check if there's a new to change page
     * @see AppCtrl
     */
     $scope.$watch('app.network.stations', function(){
      if(!$scope.app.network || !$scope.app.network.stations){
        return;
      }
      $scope.app.loading = true;
      if($state.params.stationId > 0){ // stationId parameter is sent, change station
        changeToStationId = $state.params.stationId
      }

      if($scope.app.network.currentStation != null && changeToStationId != null && $scope.app.network.currentStation.id == changeToStationId){
        //window.console && console.log("Same station: " + changeToStationId);
        if($scope.app.network.currentStation.perspective != null){
          perspectiveLoaded();
          return;
        }
      }else if(changeToStationId != null){
        window.console && console.log("Load station: " + changeToStationId)
        $scope.app.network.currentStation = wordrailsService.selectCurrentStation($scope.app.network, changeToStationId);
      }else{
        window.console && console.log("Load default station");
        $scope.app.network.currentStation = wordrailsService.selectCurrentStation($scope.app.network, null);
      }

      if(!$scope.app.network.currentStation){ // if no station is defined, fo to 404.
        $state.go("access.404", null, {location:'replace'});
        return;
      }
      // change the current station and therefore the current station perspective
      var promise = wordrailsService.changeStation($scope.app.network.currentStation)
      promise.then(function(){
        safeApply($scope, function(){
          perspectiveLoaded();
        })
      })
    });

function perspectiveLoaded(){
      broadCastPerspectiveLoaded(); // tell all child scopes that the perspective has loaded
      checkForRedirect()
    }
    /**
     * In case the state is app.init or app.stations ( /app/stations/ or /app/stations/:id)
     * redirect to perspective view.
     */
     function checkForRedirect(){
      if($state.current.name == "app.stations")
        $scope.changePerspective(null, 0, $scope.app.network.currentStation.perspective.id);
    }

    /*@GET("/perspectives/termPerspectiveViews")
      TermPerspectiveView getTermPerspectiveView(@Query("termPerspectiveId") Integer termPerspectiveId, 
      @Query("termId") Integer termId, @Query("stationPerspectiveId") Integer stationPerspectiveId, 
      @Query("page") int page, @Query("size") int size);*/
    //null, null, stationPerspective.id, 0, 10
    $scope.changePerspective = function(termPerspectiveId, termId, stationPerspectiveId){
      var station = $scope.app.network.currentStation
      $state.go(".perspectives", {perspectiveId: stationPerspectiveId, termId: termId}, {location:'replace'})
    }

    /**
     * broadcast to all child scope that the stationPerspective has been loaded
     */
     function broadCastPerspectiveLoaded(){
      $scope.$broadcast('PERSPECTIVE_LOADED',true);
    }

  })// ---------------------- end of StationsCtrl

app.controller('PostEditorCtrl', function PostEditorCtrl($scope, $state, authService, $rootScope, WORDRAILS, FileUploader) {
  var wr = authService.getWR();

  FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
    return true; // true|false
  };

  $scope.$watch('title', function(newValue, oldValue) {
    if(newValue && oldValue && (newValue !== oldValue))
      $scope.postChanged = true;
  })

  var uploader = $scope.uploader = new FileUploader({
    url: WORDRAILS.baseUrl + "/api/files/contents/simple"
  });
  // CALLBACKS
  uploader.onAfterAddingFile = function(fileItem) {
    $scope.uploadedImage = null;
    uploader.uploadAll();
  };
  uploader.onSuccessItem = function(fileItem, response, status, headers) {
      $scope.uploadedImage = response;
  };
  uploader.onErrorItem = function(fileItem, response, status, headers) {
    toastr.error("A imagem não pode ser maior que 4MBs.");
  };
  $scope.clearImage = function(){ 
    $scope.uploadedImage = null;
    uploader.clearQueue();
    uploader.cancelAll()
  }

  $scope.$watch('content', function(newValue, oldValue) {
    if(newValue && oldValue &&(newValue !== oldValue))
      $scope.postChanged = true;
  })

  $scope.taxonomies = null;

  var msgSuccess = 'Notícia criada';
  var msgSuccess2 = 'Notícia atualizado';
  var msgError = 'Erro ao criar notícia';
  var invalidTitle = 'Título inválido';
  var invalidContent = 'Conteúdo inválido';
  $scope.dateTouched = false;

  $scope.content = "";

  $scope.tagTaxonomy = {};

  $scope.submitCreateTerm = function(newName, postId){
    if(!newName){
      toastr.error("Termo inválido.")
      return;
    }
    // the treeTerm object is the parent term
    if(newName){
      var term = {}
      term.name = newName
      term.taxonomy = WORDRAILS.baseUrl + "/api/taxonomies/" + $scope.tagTaxonomy.id
      /**
       * Create term first and on success, update it and create the child
       */
      wr.postTerm(term, function(termId){ // on success termId is returned
        if(termId){
          var terms = [WORDRAILS.baseUrl + "/api/terms/" + termId];
          wr.patchPostTerms(postId, terms)
        }
      });
    }
  } 

/*  var filterTag = function(){
    for (var i = $scope.stringTerms.length - 1; i >= 0; i--) {
      $scope.tagTerms.forEach(function(tagTerm){
        if(tagTerm === $scope.stringTerms[i]){
          array.splice(i, 1);
        }
      });
    };
  }*/
  $scope.stringTagTerms = []
  function getTaxonomyTerms(){
    /*if($state.params.postId){
      wr.getPostTerms($state.params.postId, function(postTerms){

      })
    }*/
    wr.getTaxonomyTerms($scope.tagTaxonomy.id, function(response){

      if(response){
        safeApply($scope, function(){
          response.forEach(function(term){
            for (var i = $scope.p.termList.length - 1; i >= 0; i--) {
              if($scope.p.termList[i].id == term.id){
                //$scope.stringTagTerms.push($scope.p.termList[i].name)
                $scope.p.termList.splice(i, 1)
              }
            };
            $scope.tagTerms.push(term.name)
          });
        })
        console.log($scope.tagTerms);
        $scope.select2Options = {
        'multiple': true,
        'simple_tags': true,
        'tags': $scope.tagTerms
        };
      }
    })
  }

  // $scope.$watch("tagTerms", function(newVal){
  //   console.log(newVal);
  // })

  $scope.froalaOptions = {
    buttons : ["bold", "italic", "underline", "fontSize","sep", "align", "insertOrderedList", "insertUnorderedList", "sep", "createLink", "insertImage", "insertVideo", "table"],
    placeholder: 'Texto',
    inlineMode: false,
    imageUploadURL: WORDRAILS.baseUrl + "/api/files/contents/simple",
    imageUploadParam: "contents",
    language: 'pt_br',
    minHeight: 150,
    toolbarFixed: true
  }

  window.setTimeout(function() {
    angular.element("#post-editor .froala-editor .bttn-wrapper").clone(true, true).appendTo("#froala-custoom-toolbar");
  }, 300);

  $scope.$watch('title', function(newVal){
    if(newVal && $scope.app.network.currentStation.postsTitleSize > 0){
      if(newVal.length > $scope.app.network.currentStation.postsTitleSize){
        $scope.title = $scope.title.substring(0,$scope.app.network.currentStation.postsTitleSize)
      }
    }
  });

  $scope.p = {};
  $scope.p.currentTaxonomy = null
  $scope.p.currentStation = null 
  $scope.p.currentPerspective = null

  $scope.p.selectedTerm = null;
  $scope.p.termList = [];

  $scope.p.file = {};
  var uploadJqXHR;
  var uploading = false;
  $scope.editing = false;
  $scope.content = null;

  function isPublished(post){
    if(post.state !== "PUBLISHED")
      $scope.published = false;
    else
      $scope.published = true;
  }

  if($state.params.postId){
    $scope.editing = true;

    wr.getPost($state.params.postId, function(post){
        // safe aply post content
      safeApply($scope, function(){
        isPublished(post);
        $scope.post = post;
        $scope.date = post.date;
        $scope.title = post.title
        $scope.content = post.body
        $scope.topper = post.topper
      });
      // get featured image
      wr.getPostFeaturedImage(post.id, function(image){
        safeApply($scope, function(){
          var parts = extractSelf(image).split("/");
          if(!$scope.uploadedImage)
            $scope.uploadedImage = {};
        });
        // get image large
        wr.getImageLarge(image.id, function(large){
          console.log(image);
          safeApply($scope, function(){
            $scope.uploadedImage.link = WORDRAILS.baseUrl + "/api/files/" + large.id + "/contents";
            $scope.uploadedImage.id = image.id
            $scope.uploadedImage.old = true;
          });
        })
      });
      // get post terms
      wr.getPostTerms($state.params.postId, function(terms){
        safeApply($scope, function(){
          $scope.p.termList = []
          terms.forEach(function(term){
            $scope.p.termList.push(term)
          });
        })
      });
    }, null, null, "postProjection");
  }

     $scope.$watch('app.network.currentStation', function(newVal){
      if(newVal){
        $scope.p.currentStation = newVal;
        if(!$scope.taxonomies){
          getTaxonomies()
        }
      }
    })

     $scope.removeTerm = function(selectedTerm){
      var termList = $scope.p.termList
      for (var i = 0; i < termList.length; i++) {
        if (termList[i].id === selectedTerm.id) {
          termList[i].active = false;
          termList.splice(i--, 1);
        }
      }
    }

    function getTaxonomies(){
      wr.findByStationId($scope.app.network.currentStation.id, function(taxonomies) {

        if(taxonomies) {
          safeApply($scope, function(){ // util.js safeApply 
            $scope.taxonomies = [];
            taxonomies.forEach(function(taxonomy){
              if(taxonomy.type == "T"){
                $scope.tagTerms = [];
                $scope.tagTaxonomy = taxonomy
                getTaxonomyTerms();
              }else if(taxonomy.type == "S" || taxonomy.type == "G" || taxonomy.type == "N"){
                $scope.taxonomies.push(taxonomy);
              }
            });
          });
        }
      },null,
      function(){
        safeApply($scope, function(){
          $scope.app.loading = false;
        });
      });
    }

    // -------- term selector togggler
    $scope.showTermSelector = true;
    $scope.toggleTermSelector = function(){
      $scope.showTermSelector = !$scope.showTermSelector
    }
    // -------- end of term selector toggler

    $scope.submitDraft = function(){
      $scope.submit("DRAFT")
    }

    $scope.submit = function(state) {
      if(!state)
        state = "PUBLISHED";
      else
        $scope.published = false;

      if(uploading){
        toastr.error("Aguarde a transferência da imagem.");
        return
      }
      if(!$scope.p.termList || $scope.p.termList.length == 0){
        toastr.error("Escolha ao menos um termo.");
        return
      }
      if(!$scope.content){
        toastr.error(invalidContent);
        return;
      }else if(!$scope.title){
        toastr.error(invalidTitle);
        return;
      }else{
        var post = {
          body: $scope.content,
          title: $scope.title,
          author: extractSelf($scope.app.person),
          station: extractSelf($scope.app.network.currentStation),
          state: state
        };
        if($scope.showTimepicker || $scope.dateTouched){
          post.date = $scope.date;
        }

        if($scope.topper)
          post.topper = $scope.topper;

        console.log(post);

        $scope.postChanged = false;
        isPublished(post);
        if($scope.editing){
          $scope.app.loading = true;
          post.id = $scope.post.id
          post.date = $scope.date
          updatePost(post)
          return
        }
        //Begin Create Post//
        $scope.app.loading = true;
        createPost(post)
      }
    };

    function updatePost(post){
      var postId = post.id;
      wr.putPost(post, function(){ // post post... crea a post
        toastr.success(msgSuccess2); // if not editing show post created message

        $scope.editing = true; // switch to editing mode
        var data = "";

        $scope.stringTagTerms.forEach(function(stringTerm){
          $scope.submitCreateTerm(stringTerm, postId);
        });

        // ------------ update post terms
        $scope.p.termList.forEach(function(term){
          data += WORDRAILS.baseUrl + "/api/terms/" + term.id + "\n";
        })
        if($scope.p.termList.length > 0){
          wr._ajax({
            type: "PUT",
            url: WORDRAILS.baseUrl + "/api/posts/"+postId+"/terms",
            data: data,
            contentType: "text/uri-list",
          });
        }
        // ------------ end of update terms
        if($scope.uploadedImage){
          if($scope.uploadedImage.old){
            wr.putPostFeaturedImage(postId, WORDRAILS.baseUrl + "/api/images/" + $scope.uploadedImage.id)
          }else{
            var file = WORDRAILS.baseUrl + "/api/files/" + $scope.uploadedImage.id;
            var img = {original: file}
            if($scope.imageCaption) img.title = $scope.imageCaption;
            wr.postImage(img, function(imgId){
              wr.putPostFeaturedImage(postId, WORDRAILS.baseUrl + "/api/images/" + imgId)
            });
          }
        }else{
          wr._ajax({
            type: "DELETE",
            url: WORDRAILS.baseUrl + "/api/posts/" + postId + "/featuredImage",
          }); 
        }

      }, function(){
        toastr.error(msgError);
      }, function(){
        safeApply($scope, function(){
         $scope.app.loading = false;
       })
      }); //End Create Post//
}

$scope.$watch('stringTagTerms', function(newVal){
  console.log(newVal);
})

function createPost(post){
  wr.postPost(post, function(postId){ // post post... crea a post
    toastr.success(msgSuccess); // if not editing show post created message

    wr.getPost(postId, function(newPost){

      $scope.stringTagTerms.forEach(function(stringTerm){
        $scope.submitCreateTerm(stringTerm, postId);
      });

      // safe aply post content
      safeApply($scope, function(){
        $scope.post = newPost;
        $scope.title = newPost.title
        $scope.topper = newPost.topper
        $scope.content = newPost.body
      });
    });    

    $scope.editing = true; // switch to editing mode
    var data = "";
    // ------------ update post terms
    $scope.p.termList.forEach(function(term){
      data += WORDRAILS.baseUrl + "/api/terms/" + term.id + "\n";
    })
    if($scope.p.termList.length > 0){
      wr._ajax({
        type: "PUT",
        url: WORDRAILS.baseUrl + "/api/posts/"+postId+"/terms",
        data: data,
        contentType: "text/uri-list",
      });
    }
    // ------------ end of update terms
    
    if($scope.uploadedImage){
      var file = WORDRAILS.baseUrl + "/api/files/" + $scope.uploadedImage.id;
      var img = {original: file}
      if($scope.imageCaption) img.title = $scope.imageCaption;
      wr.postImage(img, function(imgId){
        wr.putPostFeaturedImage(postId, WORDRAILS.baseUrl + "/api/images/" + imgId)
      });
    }

  }, function(xhr){
    if(xhr.status == 501){
      toastr.error("Agendamento de notícias ainda não é permitido. Modifique a data e hora da notícia e tente novamente.");  
      return;
    }
    toastr.error(msgError);
  }, function(){
    safeApply($scope, function(){
     $scope.app.loading = false;
   })
  }); //End Create Post//
}

$scope.options = {
  height: 200,
  focus: true,
          toolbar: [//[groupname, [button list]]
          ['style', ['bold', 'italic', 'underline', 'clear', 'link']],
          ['font', ['strikethrough']],
          ['fontsize', ['fontsize']],
          ['color', ['color']],
          ['height', ['height']],
          ['misc', ['codeview']]
          ]
        }

        $scope.today = function() {
          $scope.date = new Date();
        };
        $scope.today();

        $scope.clear = function () {
          $scope.date = null;
        };

    // Disable weekend selection
    $scope.disabled = function(date, mode) {
      return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
    };

    $scope.toggleMin = function() {
      $scope.minDate = $scope.minDate ? null : new Date();
    };
    $scope.toggleMin();

    $scope.open = function($event) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope.opened = true;
    };

    $scope.dateOptions = {
      formatYear: 'yy',
      startingDay: 0,
      class: 'datepicker',
      showWeeks: false
    };

    $scope.initDate = new Date();
    $scope.formats = ['dd MMMM yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
    $scope.format = $scope.formats[0];

    $scope.showTimepicker = false;
    $scope.mytime = $scope.date

    $scope.hstep = 1;
    $scope.mstep = 15;

    $scope.options = {
      hstep: [1, 2, 3],
      mstep: [1, 5, 10, 15, 25, 30]
    };

    $scope.ismeridian = false;
    $scope.toggleMode = function() {
      $scope.ismeridian = ! $scope.ismeridian;
    };

    $scope.changed = function () {
      console.log('Time changed to: ' + $scope.mytime);
    };

    $scope.clear = function() {
      $scope.mytime = null;
    };
  })

/* end of post editor controller */

.controller('NewPostTaxonomiesCtrl', function NewPostTaxonomiesCtrl($scope, $state, authService, WORDRAILS){
  var wr = authService.getWR();

  var updateTermTree = function(taxonomy){
    $scope.termTree = null;
    $scope.p.infoFlows = null;
    $scope.p.selectedTerm = null;
    if(taxonomy){
        //taxonomyId, page, size, sort, _success, _error, _complete
        wr.findRoots(taxonomy.id, function(response){
          var terms = response;
          safeApply($scope, function(){
            $scope.termTree = terms;
          });

          if(terms){ // if terms > 0
            terms.forEach(function(term){ // term iteration
              term.nodes = null;
              recursiveFetchTerm(term);
              getTopTerms(terms);
            }); // terms foreach loop
          } // end if terms

        }).error(function(){

        }).complete(function(){

        });
      }
    };

    $scope.selectTopLevelTerm = function(){
      if($scope.termTree){
        getTopTerms($scope.termTree)
      }
    }

    $scope.selectTerm = function(term){
      var selected = term.$modelValue;
      //selected.active = !selected.active;
      addTerm(selected)
    }

    function addTerm(selectedTerm){
      var termList = $scope.p.termList;
      for (var i = 0; i < termList.length; i++) {
        if (termList[i].id === selectedTerm.id) {
          termList.splice(i--, 1);
        }
      }
      if(selectedTerm.active){
        termList.push(selectedTerm);
      }
    }

    function checkIfActive(term){
      $scope.p.termList.forEach(function(activeTerm){
        if(activeTerm.id == term.id){
          term.active = true;
        }
      })
    }

    function getTopTerms(terms){
      $scope.p.infoFlows = [];
      $scope.p.selectedTerm = {name: $scope.p.currentStation.name, id: null, station: true}
      if(terms){
        terms.forEach(function(termNode){
          $scope.p.infoFlows.push({"id": termNode.id, "name": termNode.name});
        });
      }
    }

    function recursiveFetchTerm(term){
      checkIfActive(term);
      wr.getTermChildren(term.id, function(response){
        if(response){
          safeApply($scope, function(){
            var nodes = response
            if(nodes){
              term.nodes = nodes;
              nodes.forEach(function(node){
                recursiveFetchTerm(node);
              })
            }
          });
        }
      }).error(function(){
        toastr.error("Houve um erro inesperado.");
      }).complete(function(){
       $scope.app.loading = false;
     });
    }

    $scope.collapseAll = function() {
      var scope = angular.element(document.getElementById("tree-root-" + $scope.taxonomy.id)).scope();
      if(scope) scope.collapseAll();
    };

    $scope.expandAll = function() {
      var scope = angular.element(document.getElementById("tree-root-" + $scope.taxonomy.id)).scope();
      if(scope) scope.expandAll();
    };

    updateTermTree($scope.taxonomy);

  }) // end of PostEditor

.controller('SettingsCtrl', function SettingsCtrl($scope, $state, authService, $filter, FileUploader, WORDRAILS) {

    FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
      return true; // true|false
    };

    var uploader = $scope.uploader = new FileUploader({
        url: WORDRAILS.baseUrl + "/api/files/contents/network"
    });
    // FILTERS
    uploader.filters.push({
        name: 'customFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            return this.queue.length < 10;
        }
    });
    // CALLBACKS
    uploader.onAfterAddingFile = function(fileItem) {
        //console.info('onAfterAddingFile', fileItem);
        uploader.uploadAll();
    };
    uploader.onSuccessItem = function(fileItem, response, status, headers) {
        //console.info('onSuccessItem', fileItem, response, status, headers);
        $scope.networkImg = response;
    };
    uploader.onErrorItem = function(fileItem, response, status, headers) {
        //console.info('onErrorItem', fileItem, response, status, headers);
        if(response.message && response.message.indexOf("height") > -1){
          toastr.error("A imagem não pode ter mais que 50 pixels de altura");
        }else if(response.message && response.message.indexOf("maximun") > -1){
          toastr.error("A imagem não pode ser maior que 100 Kb.");
        }
    };
    $scope.clearUpload = function(){ 
      uploader.clearQueue();
      $scope.networkImg = null;
    }

    var wr = authService.getWR();

    $scope.network = null;

    var currentNetwork = $scope.app.network;

    $scope.network = angular.extend({}, currentNetwork);

    $scope.$watch('app.network.image', function(newVal){
      if(newVal){
        var parts = $scope.app.network.image.split("/");
        if(!$scope.networkImg)
          $scope.networkImg = {};

        $scope.networkImg.link = $scope.app.network.image;
        $scope.networkImg.id = parts[parts.length - 2];
      }
    });

    $scope.submitSimple = function(){
      wr.getNetwork($scope.network.id, function(network){
        wr.getNetworkDefaultTaxonomy($scope.network.id, function(taxonomy){
          var taxonomyRef = extractSelf(taxonomy);

          network.domain = $scope.network.domain;
          network.subdomain = $scope.network.subdomain;
          network.name = $scope.network.name;
          network.trackingId = $scope.network.trackingId;
          network.defaultTaxonomy = taxonomyRef;

          safeApply($scope, function(){
            $scope.app.network = angular.extend($scope.app.network, network);
          })

          wr.putNetwork(network, function(){
            var netLogo = null;
            if($scope.networkImg){
               netLogo = WORDRAILS.baseUrl + "/api/files/" + $scope.networkImg.id;

               var img = {original: netLogo, title: $scope.network.name}
               wr.postImage(img, function(imgId){
                 wr.putNetworkLogo(currentNetwork.id, WORDRAILS.baseUrl + "/api/images/" + imgId)
               });

               wr.getNetworkLogo(networkId, function(networkLogo){
                  safeApply($scope, function(){
                    $scope.app.network.image = $filter('imageLink')(networkLogo.small.id);
                  })
                }, null, null, "imageProjection");
            }
            toastr.success("Código atualizado com sucesso.");
          }, function(){
            toastr.error("Houve um erro inesperado.");
          }, function(){})

        })
      })
    }
})

.controller('UserPageCtrl', function UserPageCtrl($scope, $state, $filter, authService, FileUploader, WORDRAILS) {

    FileUploader.FileSelect.prototype.isEmptyAfterSelection = function() {
      return true; // true|false
    };

    $scope.username = $state.params.username;
    var headshot = $scope.headshot = new FileUploader({
        url: WORDRAILS.baseUrl + "/api/files/contents/simple",
    });
    // CALLBACKS
    headshot.onAfterAddingFile = function(fileItem) {
        headshot.uploadAll();
        safeApply($scope, function(){
          $scope.app.loading = true;
        });
    };
    headshot.onSuccessItem = function(fileItem, response, status, headers) {
        //console.info('onSuccessItem', fileItem, response, status, headers);
        var personImage = WORDRAILS.baseUrl + "/api/files/" + response.id;

        var img = {original: personImage, title: $scope.app.person.name}
        wr.postImage(img, function(imgId){
           wr.putPersonImage($scope.app.person.id, WORDRAILS.baseUrl + "/api/images/" + imgId, function(){
            safeApply($scope, function(){
              $scope.app.person.imageMedium = response.link
              if($scope.app.person.id == $scope.person.id )
                $scope.person.imageMedium = response.link
              $scope.app.loading = false;
            })
           })
        });
    };
    headshot.onErrorItem = function(fileItem, response, status, headers) {
    };

    var cover = $scope.cover = new FileUploader({
        url: WORDRAILS.baseUrl + "/api/files/contents/simple",
    });
    // CALLBACKS
    cover.onAfterAddingFile = function(fileItem) {
      cover.uploadAll();
      safeApply($scope, function(){
        $scope.app.loading = true;
      });
    };
    cover.onSuccessItem = function(fileItem, response, status, headers) {
        var personCover = WORDRAILS.baseUrl + "/api/files/" + response.id;
        var img = {original: personCover, title: $scope.app.person.name}
        wr.postImage(img, function(imgId){
           wr.putPersonCover($scope.app.person.id, WORDRAILS.baseUrl + "/api/images/" + imgId, function(){
            safeApply($scope, function(){
              $scope.app.person.cover = response.link
              if($scope.app.person.id == $scope.person.id )
                $scope.person.cover = response.link
              $scope.app.loading = false;
            })
           })
        });
    };
    cover.onErrorItem = function(fileItem, response, status, headers) {
    };

  var wr = authService.getWR();

  $scope.postPage = 0;
  $scope.draftPage = 0;
  $scope.postsViews = [];
  $scope.draftsViews = [];
  var authorId;

  if($state.params.username && !$scope.person){
    wr.findByUsername($state.params.username, function(persons){
      var person = $scope.person = persons[0];
      authorId = person.id;

      /*wr.getPersonFollowing(authorId, function(followers){
        console.log(followers);
      })*/

      wr.getPersonImage(person.id, function(personImage){
          safeApply($scope, function(){
            person.imageMedium = $filter('imageLink')(personImage.medium.id);;
          })
      }, null, null, "imageProjection").complete(function(){
        safeApply($scope, function(){
          $scope.imageLoaded = true;
        })
      })
      wr.getPersonCover(person.id, function(personCover){
        safeApply($scope, function(){
          person.cover = $filter('imageLink')(personCover.large.id);;
        })
      }, null, null, "imageProjection")

      station = $scope.app.network.currentStation;
      loadData();
    });
  }

  var station = null
  var postsLoaded = false;


    //username
  function loadData(){
      $scope.loadPosts();
      $scope.loadDrafts();
  }

  $scope.loadPosts = function(){
    if(station){
      if(authorId > 0){
        wr.findPostsAndPostsPromotedByAuthorId(station.id, authorId, $scope.postPage, WORDRAILS.pageSize, null, function(data){
          postsLoaded = true;
          safeApply($scope, function(){
            $scope.postsViews = data;
          });
        }, null, function(){
          safeApply($scope, function(){
            $scope.app.loading = false;
          });
        });  
      }
    }// end if station
  }//end function

  $scope.loadDrafts = function(){
    if(station){
      wr.findPostsByStationIdAndAuthorIdAndState(station.id, authorId, "DRAFT", $scope.draftPage, WORDRAILS.pageSize, null, function(data){
        postsLoaded = true;
        safeApply($scope, function(){
            $scope.draftsViews = data;
        });
      }, null, function(){
        safeApply($scope, function(){
          $scope.app.loading = false;
        });
      });
    }
  }

})

.controller('SinglePostCtrl', function SinglePostCtrl($scope, $state, $filter, authService, $modal, WORDRAILS){
  var wr = authService.getWR();

  var postId = $state.params.postId;
  var post = null;
  var featuredImage = null;
  $scope.postView = {};
  $scope.postView.postId = postId;

  wr.getPost(postId, function(response){
    post = $scope.post = response;

    if(!$scope.app.network || !$scope.app.network.currentStation || !$scope.app.network.currentStation.id){
      wr.getPostStation(post.id, function(station){
        safeApply($scope, function(){
          $scope.app.network.currentStation = station;
        })
      })
    }

    wr.getPostFeaturedImage(post.id, function(image){
      safeApply($scope, function(){
        featuredImage = image;
      });

      wr.getImageLarge(featuredImage.id, function(large){
        safeApply($scope, function(){
          $scope.postView.largeId = large.id
        });
      });

    });
  }, function(){
    $state.go("access.404", null, {location:'replace'});
  });

})

.controller('PostsVListCtrl', function PostsVListCtrl($scope, $state, $filter, authService, $modal, WORDRAILS){
  var wr = authService.getWR();

  var postId = $state.params.postId;
  var termId = $state.params.termId;
  var perspectiveId = $state.params.perspectiveId;
  $scope.postsViews = []

  $scope.termId = termId;
  $scope.perspectiveId = perspectiveId;

  var page = 0;
  var firstPageLoaded = false;

  var currentPostId = $state.params.postId;

  $scope.directParentTerm = null

  var termPerspectiveView = null;

  function breadcrumbsRecursive(term){ // recursive
    wr.getTermParent(term.id, function(parent){
      safeApply($scope, function(){
        $scope.breadcrumbs.push(parent)
      });
      breadcrumbsRecursive(parent)

    }).complete(function(jqHXR){ // complete call back
      // ----- if no termPerspective get termPerspective from parent
      if(!$scope.directParentTerm && !termPerspectiveView){ 
        if(jqHXR.responseJSON){
          $scope.directParentTerm = jqHXR.responseJSON;
        }
        var parentTermId = $scope.directParentTerm ? $scope.directParentTerm.id : null;
        $scope.loadTermPerspectivePage(parentTermId, page, function(termPerspect){
          termPerspectiveView = termPerspect;
          getPostsFromPerspective();
          firstPageLoaded = true;
        })
      }
    // ----- if no termPerspective get termPerspective from parent
  });
  }

  $scope.breadcrumbs = []
  if(termId > 0){ // check if user send a termId  
    wr.getTerm(termId, function(term){
      safeApply($scope, function(){
        $scope.breadcrumbs.push(term)
      })
      breadcrumbsRecursive(term)
    });
  } // end of if termId

  // starts the list whit the params.postId at the top
  function getPostsFromPerspective(){
    if(termPerspectiveView){
      termPerspectiveView.ordinaryRows.forEach(function(row){
        if(row.termId == termId){
          row.cells.forEach(function(cell) {
            $scope.postsViews.push(cell.postView);
          });
          var temp = []
          var startAdding = false // start adding when cursor reaches the given postId
          $scope.postsViews.forEach(function(postView){
            if(postView.postId == postId)
              startAdding = true;

            if (startAdding)
              temp.push(postView)
          });

          safeApply($scope, function(){
            $scope.postsViews = temp;
          })
        }
      });
    }
  }

  var loadinScroll = false;
  $scope.scrollPaginate = function(){
    if(!firstPageLoaded){
      return;
    }

    if(loadinScroll) // exit if page is loading; avoid multiple loads of the save page;
    return;

    var parentTermId = $scope.directParentTerm ? $scope.directParentTerm.id : null;
    page++;
    loadinScroll = true;
    $scope.loadTermPerspectivePage(parentTermId, page, function(response){
      loadinScroll = false;
    })
  }

  $scope.loadTermPerspectivePage = function (updateTermId, page, calllback){
    var data = {
      stationPerspectiveId: perspectiveId,
      page: page,
      size: WORDRAILS.pageSize
    }
    if(updateTermId){
      data.termId = updateTermId
    }
    wr._ajax({
      url: WORDRAILS.baseUrl + "/api/perspectives/termPerspectiveViews",
      data: data,
      success: function(response){
        safeApply($scope, function(){
          calllback && calllback(response);
        });
      },
      complete: function(_jqXHR, status){
        safeApply($scope, function(){
         $scope.app.loading = false;
       })
      }
    });
  }
})

.controller('PostViewCtrl', function PostViewCtrl($scope, $state, $filter, $modal, authService, WORDRAILS){
  var wr = authService.getWR();

  $scope.post = null;
  $scope.comment = {};
  $scope.comments = [];
  $scope.taxonomies = [];
  $scope.comments_page = 0;
  $scope.commentsShow = true;
  $scope.maxSize = WORDRAILS.commentPageSize;

  var postId = $scope.postView.postId

  var perspectiveId = $scope.perspectiveId;
  var termId = $scope.termId;

  wr.getPost(postId, function(response){
    safeApply($scope, function(){
      $scope.post = response;
      wr.findPostCommentsOrderByDate(postId, $scope.comments_page, $scope.maxSize, null, function(response){ // TODO usar uma API de paginação de comentários
        $scope.comments = response; // lista de comentários
        if($scope.comments && $scope.comments.length > 0){
          $scope.comments.forEach(function(comment){ // para cada comentário, pegar o autor 
            wr.getCommentAuthor(comment.id, function(commentAuthor){ 
              // adicionar o objeto autor. uma vez que o objeto está na memória, é fácil usá-lo em comparações
              safeApply($scope, function(){
                comment.author = commentAuthor; 
              });
            })
          })
        }else{
          safeApply($scope, function(){
            $scope.commentsShow = false;
          }); 
        }
      }); 
    });
    
    wr.getPersonImage($scope.post.author.id, function(personImage){
      $scope.post.author.imageMedium = $filter('imageLink')(personImage.medium.id);;
    }, null, null, "imageProjection")

  }, null, null, "postProjection");

$scope.moreComments = function(){
  $scope.comments_page = $scope.comments_page + 1;
    wr.findPostCommentsOrderByDate(postId, $scope.comments_page, $scope.maxSize, null, function(response){ // TODO usar uma API de paginação de comentários
      var newComments = response;
      if(newComments && newComments.length > 0){
        newComments.forEach(function(comment){ // para cada comentário, pegar o autor 
          wr.getCommentAuthor(comment.id, function(commentAuthor){ 
            // adicionar o objeto autor. uma vez que o objeto está na memória, é fácil usá-lo em comparações
            safeApply($scope, function(){
              comment.author = commentAuthor; 
              $scope.comments.push(comment);
              $scope.commentsShow = true;
            });
          })
        })
      }else{
        safeApply($scope, function(){
          $scope.commentsShow = false;
        }); 
      }
    });  
  }

  $scope.submitComment = function(){
    function postComment() {
      wr.postComment(comment, function(commentId){
        $scope.comment.id = commentId;
        $scope.comment.author = $scope.app.person
        var commentCopy = angular.copy($scope.comment)
        wr.getComment(commentCopy.id, function(response){
          safeApply($scope, function(){  
            commentCopy.date = response.date;
            $scope.comments.push(commentCopy);
          });
        });
        safeApply($scope, function(){
          $scope.comment.body = "";
        });
      });
    }

    var valid = true;
    if(!$scope.comment.body){
      valid = false;
    }

    if(valid){
      var comment = {
        body: $scope.comment.body
      };

      var author = authService.getPerson();

      comment.author = extractSelf(author);
      if (comment.post) {
        postComment();
      }

      wr.getPost(postId, function(response){
        var post = response;
        comment.post = extractSelf(post);
        if (comment.author) {
          postComment();
        }
      });
    }else{
      toastr.error("Comment is required");
    }
  }

  $scope.updateComment = function(comment){
    if($scope.comments){
      $scope.comments.forEach(function(cm){
        if(comment.id == cm.id){
          var uComment = {
            id: comment.id,
            body: comment.newBody,
            date: comment.date,
            post: extractSelf($scope.post)
          }

          var author = authService.getPerson();
          uComment.author = extractSelf(author);

          wr.putComment(uComment, function(){
            safeApply($scope, function(){
              cm.editing = false;
              comment.body = comment.newBody;
              toastr.success("O comentário foi atualizado.")
            })
          })
        }
      })
    }
  }

  $scope.startEditing = function(comment){
    if($scope.comments && $scope.app.person.id == comment.author.id){
      $scope.comments.forEach(function(cm){
        if(comment.id == cm.id){
          cm.editing = !cm.editing;
          if(!cm.newBody){
            cm.newBody = cm.body;
          }
        }
      })
    }
  }

  $scope.deleteComment = function(){
    if($scope.objectId){
      wr.deleteComment($scope.objectId, function(){
        if($scope.comments){
          for (var i = $scope.comments.length - 1; i >= 0; i--) {
            var cm = $scope.comments[i]
            if(cm.id == $scope.objectId){
              safeApply($scope, function(){
                $scope.comments.splice(i, 1);
                toastr.success("O comentário foi removido")
                $scope.cancelModal();
              })
            }
          };
        }
      })
    }
  }

  $scope.openModal = function(templateId, size, objectId){
    $scope.objectId = objectId;
    $scope.modalInstance = $modal.open({
      templateUrl: templateId, // the id of the <script> template
      size: size,
      scope: $scope, // pass the current scope. no need for a new controller
    });
  }

  $scope.cancelModal = function () {
    $scope.modalInstance.dismiss('cancel');
  };

  $scope.deletePost = function(){
    if($scope.objectId){
      wr.deletePost($scope.objectId, function(){ // remove success callback

        for (var i = $scope.postsViews.length - 1; i >= 0; i--) {
          var pv = $scope.postsViews[i]
          if(pv.postId == $scope.objectId){

            $state.go($state.current, {
              perspectiveId: perspectiveId,
              termId: termId,
              postId: $scope.postsViews[i+1].postId}, 
              {location:'replace'});

            toastr.success("Notícia removida");

            break;
          }
        };

      }, function(){
        toastr.error("Erro ao remover notícia");
      }, function(jqXHR, textStatus){
        // window.console && console.log(textStatus)
        $scope.cancelModal();
      })// end of wr.getTaxonomy
    }// end of if clause
  } // end of deleteTaxonomy scope function

  $scope.stations = [];
  // ---- filter stations by permissions. see WordrailsService in services.js
  if($scope.app.network && $scope.app.network.stations){
    $scope.stations = angular.copy($scope.app.network.stations);
    for (var i = $scope.stations.length - 1; i >= 0; i--) {
      if(!$scope.stations[i].visible){
        $scope.stations.splice(i, 1);
      }
    };
  }else{
    window.console && console.error("error filteing stations")
  }
  // ----
  
  $scope.promote = {};
  $scope.promote.stationId = null;
  $scope.promote.termsToPromote = null;
  $scope.promote.taxonomies = null
  $scope.selectStation = function(){ // on station change for promotion modal
    $scope.promote.taxonomies = []; // create empty taxonomies
    $scope.promote.termsToPromote = null;

    if($scope.promote.stationId){
      wr.findByStationId($scope.promote.stationId, function(taxonomies){ //find taxonomies

        $scope.promote.taxonomies = taxonomies // add to taxonomies list

        if(taxonomies){
          taxonomies.forEach(function(taxonomy){
            taxonomy.terms = []                       // create empty terms
            wr.getTaxonomyTerms(taxonomy.id, function(terms){ // getTerms for taxonomy
              if(terms){
                safeApply($scope, function(){
                  terms.forEach(function(term){
                      taxonomy.terms.push(term); // push term to list
                    })
                });
              }// if terms
            });// getTaxonomyTerms
          })// forEach taxonomies
        }

      })
    }
  }

  $scope.selectTerms = function(){
  }

  $scope.promoteNews = function(){
    var station;
    for (var i = 0; i < $scope.stations.length; i++) {
     station = $scope.stations[i];
     if(station.id == $scope.promote.stationId){
      break;
    }
  };

  console.log(station)
  console.log($scope.stations)

  var promotion = {}
  promotion.post = extractSelf($scope.post);
  promotion.date = $scope.post.date;
  promotion.station = extractSelf(station);
  promotion.promoter = extractSelf($scope.app.person);

  wr.postPromotion(promotion, function(response){
    $scope.cancelModal();
    toastr.success("Notícia promovida.");
    var termsToPromote = angular.copy($scope.promote.termsToPromote);
    $scope.promote.termsToPromote = null;
        // only promote posts that are not in post.terms
        var patchTerms = []
        if($scope.post.terms && termsToPromote){
          for (var i = termsToPromote.length - 1; i >= 0; i--) {
            termsToPromote[i] = JSON.parse(termsToPromote[i]);
            $scope.post.terms.forEach(function(term){
              if(termsToPromote[i].id == term.id){
                termsToPromote.splice(i, 1)
                return;
              }else{
                patchTerms.push(extractSelf(termsToPromote[i]));
              }
            })
          };
      }// end of cleaning terms

      if(patchTerms){
        wr.patchPostTerms($scope.post.id, patchTerms, function(){
          wr.getPostTerms($scope.post.id, function(terms){
            safeApply($scope, function(){
              $scope.post.terms = terms
            })
          })
        })
      }
    });
}

})

.controller('SignupCtrl', function SignupCtrl($scope, $state, $filter, $modal, authService, wordrailsService, WORDRAILS){
  var wr = authService.getWR();
  var msgSuccess = "Usuário cadastrado com sucesso";
  var msgError = "Erro ao cadastrar o usuário. Tente mais tarde";
  var msgErrorUsername = "Nome de usuário indisponível"

  $scope.exists = true;
  $scope.person = {};

  $scope.$watch("person.username", function(newVal, oldVal){
    if($scope.person && $scope.person.username){
      $scope.person.username = $scope.person.username.toUsername();
    }
  });

  $scope.signup = function(){
    $scope.person.passwordReseted = true;
    $scope.person.password = $scope.password;

    var username = $scope.person.username; // store username and password
    var password = $scope.password;

    wr.postPerson($scope.person, function(personId){ // create a person

      wr.getPerson(personId, function(personResponse){ // get person that was created
        var networkRole = {
          network: extractSelf(wordrailsService.getNetwork()),
          person: extractSelf(personResponse),
          admin: false
        } 

        wr.postNetworkRole(networkRole, function(networkRoleId){ // add person to network

          toastr.success(msgSuccess);
          safeApply($scope, function(){                
            $scope.person = null;
            $scope.first_name = null;
            $scope.last_name = null;
            $scope.password = null;
            $scope.confirm_password = null;
          });

          authService.signIn(username, password,
            function(){
              var person = authService.getPerson()
              safeApply($scope, function(){
                $scope.app.person = person
              })
              $state.go("app.stations", {stationId: 0}, {reload: true});
              //location.reload();
              $scope.cancelModal();
              $scope.app.authenticated = authService.isAuth();
            },
            
            function(){
              safeApply($scope, function(){
                window.console && console.log('login error');
                $scope.signInError = true;
              });
            });

        }).error(function(){
          // add person to network error
        });
      }).error(function(){
        // get created person error
      })

    }).error(function(jqXHR){ // end of postPerson.
      if(jqXHR.status == 409){
        toastr.error("Usuário já cadastrado.");  
        return 
      }
        // create persoon error
        toastr.error(msgError);
      });  
  };// end of signup

})

.controller('SearchCtrl', function SearchCtrl($scope, $state, $filter, $modal, authService, WORDRAILS){
  var wr = authService.getWR();

  $scope.search = {} // controller scope object
  $scope.search.page = 0;
  $scope.maxSize = WORDRAILS.pageSize;

  $scope.$watch('app.searchQuery', function(newVal){
    if(newVal){
      $scope.search.page = 0;
      runSearch($scope.app.searchQuery)
    }
    
  });

  $scope.$watch('search.page', function(newVal){
    if($scope.app.searchQuery && newVal){
      runSearch($scope.app.searchQuery);
    }
  });

  var runSearch = function(query){
    wr.searchPostsFromOrPromotedToStation($scope.app.network.currentStation.id,
      query, $scope.search.page, WORDRAILS.pageSize, 
      function(result){
        safeApply($scope, function(){
          $scope.search.hits = result.hits;
          $scope.search.result = result;
        })
      }, null, function(){
        safeApply($scope, function(){
          $scope.app.loading = false;
        });
      });
  }

})
