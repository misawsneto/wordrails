'use strict';

/* Directives */
// All the directives rely on jQuery.

angular.module('app.directives', ['ui.load'])
  .directive('uiModule', ['MODULE_CONFIG','uiLoad', '$compile', function(MODULE_CONFIG, uiLoad, $compile) {
    return {
      restrict: 'A',
      compile: function (el, attrs) {
        var contents = el.contents().clone();
        return function(scope, el, attrs){
          el.contents().remove();
          uiLoad.load(MODULE_CONFIG[attrs.uiModule])
          .then(function(){
            $compile(contents)(scope, function(clonedElement, scope) {
              el.append(clonedElement);
            });
          });
        }
      }
    };
  }])
  
  .directive('uiShift', ['$timeout', function($timeout) {
    return {
      restrict: 'A',
      link: function(scope, el, attr) {
        // get the $prev or $parent of this el
        var _el = $(el),
            _window = $(window),
            prev = _el.prev(),
            parent,
            width = _window.width()
            ;

        !prev.length && (parent = _el.parent());
        
        function sm(){
          $timeout(function () {
            var method = attr.uiShift;
            var target = attr.target;
            _el.hasClass('in') || _el[method](target).addClass('in');
          });
        }
        
        function md(){
          parent && parent['prepend'](el);
          !parent && _el['insertAfter'](prev);
          _el.removeClass('in');
        }

        (width < 768 && sm()) || md();

        _window.resize(function() {
          if(width !== _window.width()){
            $timeout(function(){
              (_window.width() < 768 && sm()) || md();
              width = _window.width();
            });
          }
        });
      }
    };
  }])
  .directive('uiToggleClass', ['$timeout', '$document', function($timeout, $document) {
    return {
      restrict: 'AC',
      link: function(scope, el, attr) {
        el.on('click', function(e) {
          e.preventDefault();
          var classes = attr.uiToggleClass.split(','),
              targets = (attr.target && attr.target.split(',')) || Array(el),
              key = 0;
          angular.forEach(classes, function( _class ) {
            var target = targets[(targets.length && key)];            
            ( _class.indexOf( '*' ) !== -1 ) && magic(_class, target);
            $( target ).toggleClass(_class);
            key ++;
          });
          $(el).toggleClass('active');

          function magic(_class, target){
            var patt = new RegExp( '\\s' + 
                _class.
                  replace( /\*/g, '[A-Za-z0-9-_]+' ).
                  split( ' ' ).
                  join( '\\s|\\s' ) + 
                '\\s', 'g' );
            var cn = ' ' + $(target)[0].className + ' ';
            while ( patt.test( cn ) ) {
              cn = cn.replace( patt, ' ' );
            }
            $(target)[0].className = $.trim( cn );
          }
        });
      }
    };
  }])
  .directive('uiNav', ['$timeout', function($timeout) {
    return {
      restrict: 'AC',
      link: function(scope, el, attr) {
        var _window = $(window);
        var _mb = 768;
        // unfolded
        $(el).on('click', 'a', function(e) {
          var _this = $(this);
          _this.parent().siblings( ".active" ).toggleClass('active');
          _this.parent().toggleClass('active');
          _this.next().is('ul') && e.preventDefault();
          _this.next().is('ul') || ( ( _window.width() < _mb ) && $('.app-aside').toggleClass('show') );
        });

        // folded
        var wrap = $('.app-aside'), next;
        $(el).on('mouseenter', 'a', function(e){
          if ( !$('.app-aside-fixed.app-aside-folded').length || ( _window.width() < _mb )) return;
          var _this = $(this);

          next && next.trigger('mouseleave.nav');

          if( _this.next().is('ul') ){
             next = _this.next();
          }else{
            return;
          }
          
          next.appendTo(wrap).css('top', _this.offset().top - _this.height());
          next.on('mouseleave.nav', function(e){
            next.appendTo(_this.parent());
            next.off('mouseleave.nav');
            _this.parent().removeClass('active');
          });
          _this.parent().addClass('active');
          
        });

        wrap.on('mouseleave', function(e){
          next && next.trigger('mouseleave.nav');
        });
      }
    };
  }])
  .directive('uiScroll', ['$location', '$anchorScroll', function($location, $anchorScroll) {
    return {
      restrict: 'AC',
      link: function(scope, el, attr) {
        el.on('click', function(e) {
          $location.hash(attr.uiScroll);
          $anchorScroll();
        });
      }
    };
  }])
  .directive('uiFullscreen', ['uiLoad', function(uiLoad) {
    return {
      restrict: 'AC',
      template:'<i class="fa fa-expand fa-fw text"></i><i class="fa fa-compress fa-fw text-active"></i>',
      link: function(scope, el, attr) {
        el.addClass('hide');
        uiLoad.load('js/libs/screenfull.min.js').then(function(){
          if (screenfull.enabled) {
            el.removeClass('hide');
          }
          el.on('click', function(){
            var target;
            attr.target && ( target = $(attr.target)[0] );            
            el.toggleClass('active');
            screenfull.toggle(target);
          });
        });
      }
    };
  }])

  .directive('uiButterbar', ['$rootScope', '$location', '$anchorScroll', function($rootScope, $location, $anchorScroll) {
     return {
      restrict: 'AC',
      template:'<span class="bar"></span>',
      link: function(scope, el, attrs) {        
        el.addClass('butterbar hide');        
        scope.$on('$stateChangeStart', function(event) {
          $anchorScroll();
          el.removeClass('hide').addClass('active');
        });
        scope.$on('$stateChangeSuccess', function( event, toState, toParams, fromState ) {
          event.targetScope.$watch('$viewContentLoaded', function(){
            el.addClass('hide').removeClass('active');
          })          
        });
      }
     };
  }])

.directive('sortablePosts', function ($http, $rootScope, $timeout, $compile) {
    return {
      restrict: 'E',
      link: function (scope, element, attrs) {

          scope.flowOptions = {
            handle: '.posts-flows-handle',
            scroll: false,
            axis: "y"
          };

          scope.itemOptions = {
            cancel: '.old-item',
            axis: "x"
          };

          scope.$watch('p.termPerspectiveView.ordinaryRows', function(newVal){
            if(!newVal)
              return;

            function applyDrop(){
              $( ".droppable-post-position" ).droppable({
              drop: function( event, ui ) {
                // get the data from the post
                var termId = $(this).data("term-id");

                var postId = $(ui.draggable).data("post-id")
                var title = $(ui.draggable).data("post-title")
                var img = $(ui.draggable).data("post-img")
                var largeId = $(ui.draggable).data("post-large")
                var mediumId = $(ui.draggable).data("post-medium")
                var smallId = $(ui.draggable).data("post-small")
                // the post item
                if(!postId  || !title){
                  return;
                }
                var postView = {
                  new: true,
                  postId: postId,
                  title: title,
                  sponsor: null,
                  largeId: largeId,
                  mediumId: mediumId,
                  smallId: smallId,
                  img: img
                }

                var cell = {}
                cell.index = null;
                cell.postView = postView

                if(!termId){
                  safeApply(scope, function(){
                    scope.p.splashedPosts.unshift(cell)
                    // console.log(scope.p.splashedPosts);
                  })
                } else if(scope.p.termPerspectiveView.ordinaryRows){
                    scope.p.termPerspectiveView.ordinaryRows.forEach(function(row){
                      if(row.termId == termId){
                        if(row.cells == null){
                          row.cells = [];
                        }
                        safeApply(scope, function(){
                          row.cells.unshift(cell)
                        })// end safeApply
                      }
                    });// end foreach
                }// end if
              }
            });
          } // end of applyDrop function

          setTimeout(function() {
            applyDrop();
          }, 300);

        });

      },
      templateUrl: 'tpl/app/sortable-posts.html'
    };
})

.directive("offcanvas", function(){
  return{
    restrict: 'E',
    link: function(scope, element, attrs){
        var bodyEl = document.body,
        content = document.querySelector( '.content-wrap' ),
        openbtn = document.getElementById( 'open-button' ),
        closebtn = document.getElementById( 'close-button' ),
        isOpen = false;

      function init() {
        initEvents();
      }

      function initEvents() {
        /*openbtn.addEventListener( 'click', toggleMenu );
        if( closebtn ) {
          closebtn.addEventListener( 'click', toggleMenu );
        }*/

        // close the menu element if the target it´s not the menu element or one of its descendants..
        content.addEventListener( 'click', function(ev) {
          var target = ev.target;
          if( isOpen && target !== openbtn ) {
            toggleMenu();
          }
        } );
      }

      scope.toggleMenu = function(){
          toggleMenu();
      }

      scope.contentClick = function(){
        if(isOpen)
          toggleMenu(); 
      };

      function toggleMenu() {
        if( isOpen ) {
          classie.remove( bodyEl, 'show-menu' );
        }
        else {
          classie.add( bodyEl, 'show-menu' );
        }
        isOpen = !isOpen;
      }

      init();
    },
    templateUrl: 'tpl/app/offcanvas.html'
  }// end of return 
})

.directive("slyScroll",function($state, $timeout) {
     return {
       restrict: 'A',
       link: function(scope,element,attrs) {
          if (scope.$last === true) {
            $timeout(function () {
                executeSly()
            });
          }
          /**
           * function to be executed after ng-repeat is finished
           */
          function executeSly(){
            var batch = []

            $(".wrap.info-flow-wrap").each(function(){
                var elem = this
                var job = function(){
                  var $wrap  = $(elem);
                  var $frame = $(elem).find('.info-flow');
                  // Call Sly on frame
                  $frame.sly({
                    horizontal: 1,
                    itemNav: 'basic',
                    smart: 1,
                    activateOn: 'click',
                    mouseDragging: 1,
                    touchDragging: 1,
                    releaseSwing: 1,
                    startAt: 0,
                    scrollBar: $wrap.find('.scrollbar'),
                    scrollSource: "null",
                    scrollBy: 1,
                    speed: 300,
                    elasticBounds: 1,
                    easing: 'easeOutExpo',
                    dragHandle: 1,
                    dynamicHandle: 1,
                    clickBar: 1,
                    // Buttons
                    backward: $wrap.siblings('.backward'),
                    forward: $wrap.siblings('.forward'),
                    moveBy:1000
                  }).sly('on', 'active', function(eventName, index){
                    var perspectiveId = null
                    if(scope.app.network.currentStation){
                      perspectiveId = scope.app.network.currentStation.perspective.id;
                    }

                    var domElem = this.getPos(index)
                    var postId = $(domElem.el).find('div.news-item').data('post-id')
                    var termId = $(elem).data('term-id')
                    $state.go("^.vposts", {perspectiveId: perspectiveId, termId: termId, postId: postId});
                  }).sly('on', 'move', function(eventName, index){
                    if (this.pos.dest > this.pos.end - 200) {
                      var sly = this;
                      var termId = $(elem).data('term-id')
                      // updateRowPage is defined in the parent scope
                      scope.updateRowPage && scope.updateRowPage(termId, function(){
                        sly.reload();
                      });
                    }
                  });
                  }// end of function
                  batch.push(job)
              });
            
            var count = 0;

            function run(){
              setTimeout(function() {
                if(count < batch.length){
                  batch[count]();
                  count ++;
                  run();
                  $(window).trigger('resize');
                }
              }, 300);
            }

            run();
          }// execute finished
        }// end of link
     } // end of object to return
 }); // end of sly directive