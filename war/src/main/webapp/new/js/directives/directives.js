angular.module('app')
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