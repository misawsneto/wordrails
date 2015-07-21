var bla = 0;

angular.module('app')
.directive("slyScroll",function($state, $timeout) {
 return {
   restrict: 'A',
   scope: {
    scroll: '&',
    active: '&'
  },
  link: function(scope,element,attrs) {
    $timeout(function () {
      executeSly()
    });

          /**
           * function to be executed after ng-repeat is finished
           */
           function executeSly(){
            var elem = element
            // var job = function(){
              var $wrap  = $(elem);
              var $frame = $(elem).find('.info-flow');
              // Call Sly on frame
              $frame.sly({
                horizontal: 1,
                itemNav: 'basic',
                smart: false,
                activateOn: 'click',
                mouseDragging: 1,
                touchDragging: 1,
                releaseSwing: 1,
                startAt: null,
                scrollBar: $wrap.find('.scrollbar'),
                scrollSource: "null",
                scrollBy: 1,
                activatePageOn: 'click',
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

                var sly = this;

                /* see main.js $scope.app.setHorizontalCursor */

                if(sly.items && sly.items.length - 1){
                  sly.activate(sly.items && sly.items.length - 1);
                }

                safeApply(scope, function(){
                  scope.active();
                })

              }).sly('on', 'move', function(eventName, index){
                if (this.pos.dest > this.pos.end - 200) {
                  var sly = this;
                  safeApply(scope, function(){
                    var promise = scope.scroll();
                    if(promise)
                      promise.then(function(){
                        sly.reload();
                      })
                  })
                  // var termId = $(elem).data('term-id')
                  // // updateRowPage is defined in the parent scope
                  // scope.updateRowPage && scope.updateRowPage(termId, function(){
                  //   sly.reload();
                  // });
            }
          });
          }// execute finished
        }// end of link
     } // end of object to return
 }) // end of sly directive

.directive('scrollIf', function () {
  return function (scope, element, attributes) {
    setTimeout(function () {
      if (scope.$eval(attributes.scrollIf)) {
        window.scrollTo(0, element[0].offsetTop - 100)
      }
    });
  }
})

.directive('clamp', function ($timeout) {

  function resetElement(element, type) {
    element.css({
      position: 'inherit',
      overflow: 'hidden',
      display: 'block',
      textOverflow: (type ? 'ellipsis' : 'clip'),
      visibility: 'inherit',
      whiteSpace: 'nowrap',
      width: '100%'
    });
  }

  function createElement() {
    var tagDiv = document.createElement('div');
    (function(s) {
      s.position = 'absolute';
      s.whiteSpace = 'pre';
      s.visibility = 'hidden';
      s.display = 'inline-block';
    })(tagDiv.style);

    return angular.element(tagDiv);
  }

  return{
    restrict: 'A',
    link: function (scope, element, attrs) {
      $timeout(function() {
        var lineCount = 1, lineMax = +attrs.clamp;
        var lineStart = 0, lineEnd = 0;
        var text = element.html().replace(/\n/g, ' ');
        var maxWidth = element[0].offsetWidth;
        var estimateTag = createElement();

        element.empty();
        element.append(estimateTag);

        text.replace(/ /g, function(m, pos) {
          if (lineCount >= lineMax) {
            return;
          } else {
            estimateTag.html(text.slice(lineStart, pos));
            if (estimateTag[0].offsetWidth > maxWidth) {
              estimateTag.html(text.slice(lineStart, lineEnd));
              resetElement(estimateTag);
              lineCount++;
              lineStart = lineEnd + 1;
              estimateTag = createElement();
              element.append(estimateTag);
            }
            lineEnd = pos;
          }
        });
        estimateTag.html(text.slice(lineStart));
        resetElement(estimateTag, true);

        scope.$emit('clampCallback', element, attrs);
      });
}
}
})

.directive('nodeTree', function() {
  return {
    template: '<node ng-repeat="node in tree"></node>',
    replace: true,
    transclude: true,
    restrict: 'E',
    scope: {
      tree: '=ngModel'
    }
  };
})

.directive('focusMe', function($timeout) {
  return {
    scope: { trigger: '=focusMe' },
    link: function(scope, element) {
      scope.$watch('trigger', function(value) {
        if(value === true) { 
          //console.log('trigger',value);
          //$timeout(function() {
            element[0].focus();
            scope.trigger = false;
          //});
    }
  });
    }
  };
})

.directive('node', function($compile) {
  return { 
    restrict: 'E',
    replace:true,
    templateUrl: 'tpl/tree-view.html',
    link: function(scope, elm, attrs) {

      //$(elm).parent('ul').find('span.leaf').on('click', function (e) {
       $(elm).find('span.leaf').on('click', function (e) {

         var children = $(elm).find('li');

         if (children.is(":visible")) {
          children.hide('fast');
          $(elm).find('span.leaf i.icon-minus-sign').addClass('icon-plus-sign').removeClass('icon-minus-sign');
        }
        else{

          children.show('fast');
          $(elm).find('span.leaf i.icon-plus-sign').addClass('icon-minus-sign').removeClass('icon-plus-sign');
        }
        e.stopPropagation();
      });

       scope.nodeClicked = function(node) {
        node.checked = !node.checked;
        /*function checkChildren(c) {
          angular.forEach(c.children, function(c) {
            c.checked = node.checked;
            checkChildren(c);
          });
        }
        checkChildren(node);*/
      };
      
      scope.switcher = function(booleanExpr, trueValue, falseValue) {
        return booleanExpr ? trueValue : falseValue;
      };
      
      scope.isLeaf = function(_data) {
        if (_data.children.length == 0) {
          return true;
        }
        return false;
      };

      if (scope.node.children.length > 0) {
        var childNode = $compile('<ul><node-tree ng-model="node.children"></node-tree></ul>')(scope)
        elm.append(childNode);
      }
    }
  };
});