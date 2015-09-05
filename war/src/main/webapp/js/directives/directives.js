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
                scrollSource: "null",
                scrollBy: 1,
                activatePageOn: 'click',
                speed: 1000,
                elasticBounds: 1,
                easing: 'easeOutExpo',
                dragHandle: 1,
                dynamicHandle: 1,
                clickBar: 1,
                // Buttons
                backward: $wrap.siblings('.backward'),
                forward: $wrap.siblings('.forward'),

                // prev: $wrap.find('.prev'),
                // next: $wrap.find('.next'),
                // prevPage: $wrap.find('.backward'),
                // nextPage: $wrap.find('.forward'),

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

app.directive('backImg', function(TRIX){
  return function(scope, element, attrs){
    attrs.$observe('backImg', function(value) {
      if(value){
        element.css({
          'background-image': 'url(' + TRIX.baseUrl + "/api/files/"+ value +"/contents" +')'
        });
      }else{
        element.css({
          'background-image': 'url(img/default-user.png)'
        });
      }
    });
  };
})

app.directive('backImgCover', function(TRIX){
  return function(scope, element, attrs){
    attrs.$observe('backImgCover', function(value) {
      if(value){
        element.css({
          'background-image': 'url(' + TRIX.baseUrl + "/api/files/"+ value +"/contents" +')'
        });
      }else{
        element.css({
          'background-image': 'url(../img/abstract-cover-orig.png)'
        });
      }
    });
  };
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

.directive('dropdownMultiselect', function () {
  return {
    restrict: 'E',
    scope: {
      model: '=',
      options: '=',
    },
    template:
    "<div class='btn-group' data-ng-class='{open: open}'>" +
    "<div class='r b b-drk wrapper-sm bg-white text-md' ng-click='openDropdown()' ng-class=\"{'text-danger': model.length == 0}\">"+
      "{{printSelected()}} <span class='caret'></span>"+
    "</div>" +
      "<ul class='dropdown-menu' aria-labelledby='dropdownMenu'>" +
        "<li><a data-ng-click='selectAll()'> <span class='' aria-hidden='true'></span> Marcar Todas</a></li>" +
        "<li><a data-ng-click='deselectAll();'> <span class='' aria-hidden='true'></span> Desmarcar Todas</a></li>" +
        "<li class='divider'></li>" +
        "<li data-ng-repeat='option in options'> <a data-ng-click='toggleSelectItem(option)'>"+
        "<span data-ng-class='getClassName(option)' aria-hidden='true'></span> {{option.name}}</a></li>" +
      "</ul>" +
    "</div>",
    controller: function ($scope) {
      $scope.openDropdown = function () {
        $scope.open = !$scope.open;
      };

      $scope.printSelected = function(){
        var names = []
        $scope.model && $scope.model.forEach(function(model, index){
          $scope.options && $scope.options.forEach(function(option, index){
            if(option.id == model)
              names.push(option.name)
          });
        });
        return names.length > 0 ? names.join(", ") : 'Escolha ao menos 1 estação';
      }

      $scope.selectAll = function () {
        $scope.model = [];
        angular.forEach($scope.options, function (item, index) {
          $scope.model.push(item.id);
        });
      };

      $scope.deselectAll = function () {
        $scope.model = [];
      };

      $scope.toggleSelectItem = function (option) {
        var intIndex = -1;
        angular.forEach($scope.model, function (item, index) {
          if (item == option.id) {
            intIndex = index;
          }
        });

        if (intIndex >= 0) {
          $scope.model.splice(intIndex, 1);
        }
        else {
          $scope.model.push(option.id);
        }
      };

      $scope.getClassName = function (option) {
        var varClassName = 'fa fa-fw fa-bla';
        angular.forEach($scope.model, function (item, index) {
          if (item == option.id) {
            varClassName = 'mdi fa-fw mdi-check';
          }
        });
        return (varClassName);
      };
    },
    link: function(scope, element){
      $(document).bind('click', function(event){
        var isClickedElementChildOfPopup = element.find(event.target).length > 0;

        if (isClickedElementChildOfPopup)
            return;

        scope.$apply(function(){
            scope.open = false;
        });
      });
    }
  }
})

.directive('popoverClose', function($timeout){
  return{
    scope: {
      excludeClass: '@'
    },
    link: function(scope, element, attrs) {
      var trigger = document.getElementsByClassName('trigger');
      
      function closeTrigger(i) {
        $timeout(function(){ 
          angular.element(trigger[0]).triggerHandler('click').removeClass('trigger'); 
          //$(trigger[0]).on('click').removeClass('trigger'); 
        });
      }
      
      element.on('click', function(event){
        var etarget = angular.element(event.target);
        var tlength = trigger.length;
        if(!etarget.hasClass('trigger') && !etarget.hasClass(scope.excludeClass)) {
          for(var i=0; i<tlength; i++) {
            closeTrigger(i)
          }
        }
      });
    }
  };
})

.directive('popoverElem', function(){
  return{
    link: function(scope, element, attrs) {
      element.on('click', function(){
        element.addClass('trigger');
      });
    }
  };
})

.directive('popoverToggle', ['$timeout', function($timeout) {
    return {
        restrict: 'A',
        link: link
    };

    function link($scope, $element, $attrs) {
        $attrs.popoverTrigger = 'popoverToggleShow';

        $scope.$watch($attrs.popoverToggle, function(newValue) {
            $timeout(function(){
                if(newValue) {
                    $element.triggerHandler('popoverToggleShow');
                } else {
                    $element.triggerHandler('popoverToggleHide');
                }
            });
        })
    }
}])

// **************************************************
        // *** /My AngularJS Directive(s) Application *******
        // **************************************************

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
          // $(elm).find('span.leaf i.icon-minus-sign').addClass('icon-plus-sign').removeClass('icon-minus-sign');
        }
        else{

          children.show('fast');
          // $(elm).find('span.leaf i.icon-plus-sign').addClass('icon-minus-sign').removeClass('icon-plus-sign');
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