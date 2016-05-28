angular.module('app')
  .directive('eventFocus', function(focus) {
    return function(scope, elem, attr) {
      elem.on(attr.eventFocus, function() {
        focus(attr.eventFocusId);
      });

      // Removes bound events in the element itself
      // when the scope is destroyed
      scope.$on('$destroy', function() {
        elem.off(attr.eventFocus);
      });
    };
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

  .directive('categoryLeaf', function() {
    return {
      template: '<category-tree ng-repeat="node in tree"></category-tree>',
      replace: true,
      transclude: true,
      restrict: 'E',
      scope: {
        tree: '=ngModel'
      }
    };
  })

  .directive('node', function($compile) {
    return { 
      replace: true,
      restrict: 'E',
      templateUrl: '/views/partials/tree-view.html',
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
  })

.directive('categoryTree', function($compile) {
    return { 
      replace: true,
      restrict: 'E',
      templateUrl: '/views/partials/category-tree.html',
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
          var childNode = $compile('<ul><category-leaf ng-model="node.children"></category-leaf></ul>')(scope)
          elm.append(childNode);
        }
      }
    };
  })
  ;

(function(angular) {
    'use strict';
    angular.module('app').factory('preLoader', function () {
        return function (url, successCallback, errorCallback) {
            //Thank you Adriaan for this little snippet: http://www.bennadel.com/members/11887-adriaan.htm
            angular.element(new Image()).bind('load', function () {
                successCallback();
            }).bind('error', function () {
                errorCallback();
            }).attr('src', url);
        }
    }).directive('preloadImage', ['preLoader', function (preLoader) {
        return {
            restrict: 'A',
            terminal: true,
            priority: 100,
            link: function (scope, element, attrs) {
                scope.default = attrs.defaultImage || "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=";
                attrs.$observe('ngSrc', function () {
                    var url = attrs.ngSrc;
                    attrs.$set('src', scope.default);
                    preLoader(url, function () {
                        attrs.$set('src', url);
                    }, function () {
                        if (attrs.fallbackImage != undefined) {
                            attrs.$set('src', attrs.fallbackImage);
                        }
                    });
                })
    
            }
        };
    }]).directive('preloadBgImage', ['preLoader', function (preLoader) {
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                if (attrs.preloadBgImage != undefined) {
                    //Define default image
                    scope.default = attrs.defaultImage || "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAQAAAC1HAwCAAAAC0lEQVR42mNkYAAAAAYAAjCB0C8AAAAASUVORK5CYII=";
    
                    attrs.$observe('preloadBgImage', function () {
                        element.css({
                            'background-image': 'url("' + scope.default + '")'
                        });
                        preLoader(attrs.preloadBgImage, function () {
                            element.css({
                                'background-image': 'url("' + attrs.preloadBgImage + '")'
                            });
                        }, function () {
                            if (attrs.fallbackImage != undefined) {
                                element.css({
                                    'background-image': 'url("' + attrs.fallbackImage + '")'
                                });
                            }
                        });
                    });
                }
            }
        };
    }]);
})(angular);

(function() {

  angular.module('mdxUtil', ['ngMaterial'])
  .directive('mdxPaintFg',function(mdx) {
    "use strict";
    return {
      restrict : 'A',
      link     : function(scope, element, attributes) {
        setRGB(element,'color',mdx.mdxThemeColors,attributes.mdxPaintFg,'mdx-paint-fg');
      }
    }
  })
  .directive('mdxPaintBg',function(mdx) {
    "use strict";
    return {
      restrict : 'A',
      link     : function(scope, element, attributes) {
        setRGB(element,'background-color',mdx.mdxThemeColors,attributes.mdxPaintBg,'mdx-paint-bg');
      }
    }
  })
  .directive('mdxPaintSvg',function(mdx) {
      "use strict";
      return {
        restrict : 'A',
        link     : function(scope, element, attributes) {
          setRGB(element,'fill',mdx.mdxThemeColors,attributes.mdxPaintSvg,'mdx-paint-svg');
        }
      }
    })

  .directive('hashToLink', function(TRIX){
    return function(scope, element, attrs){
      attrs.$observe('hashToLink', function(obj) {
        if(obj){
          element.css({
            "background-image": "url(" + TRIX.baseUrl + "/api/images/get/"+ obj.hash  + "?size=" +obj.size+ ")", "background-position": "50% 20%"
          });
        }
      });
    };
  })

  .directive('userHashToLink', function(TRIX){
    return function(scope, element, attrs){
      attrs.$observe('userHashToLink', function(obj) {
        if(obj){
          element.css({
            "background-image": "url(" + TRIX.baseUrl + "/api/images/get/"+ obj.hash  + "?size=" +obj.size+ ")", "background-position": "50% 20%"
          });
        }else{
          element.css({
            'background-image': 'url(img/default-user.png)'
          });
        }
      });
    };
  })

  .directive('coverHashToLink', function(TRIX){
    return function(scope, element, attrs){
      attrs.$observe('coverHashToLink', function(obj) {
        if(obj){
          element.css({
            "background-image": "url(" + TRIX.baseUrl + "/api/images/get/"+ obj.hash  + "?size=" +obj.size+ ")", "background-position": "50% 20%"
          });
        }else{
          element.css({
            'background-image': 'url(../img/abstract-cover-orig.png)'
          });
        }
      });
    };
  })

  .directive('backImgCover', function(TRIX){
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

  .directive('backImg', function(TRIX){
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

  .directive('backImgCover', function(TRIX){
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

  // Couldn't get access to _PALETTES any other way?
  .provider('mdx',function($mdThemingProvider){
    return {
      $get : function() {
        "use strict";
        return {
          mdxThemeColors : $mdThemingProvider
        }
      }
    }
  });

  function setRGB(element,styled,themeProvider,input,directiveName) {
    "use strict";
    var themeName     = 'default';
    var hueName       = 'default';
    var intentionName = 'primary';
    var hueKey,theme,hue,intention;

    // Do our best to parse out the attributes
    angular.forEach(input.split(" "), function(value, key) {
      if (0 === key && 'default' === value) {
        themeName = value;
      } else
      if ({primary:'primary',accent:'accent',warn:'warn',background:'background'}[value]) {
        intentionName = value;
      } else if ({default:'default','hue-1':'hue-1','hue-2':'hue-2','hue-3':'hue-3'}[value]) {
        hueName = value;
      } else if ({'50' :'50' ,'100':'100','200':'200','300':'300','400':'400',
                  '500':'500','600':'600','700':'700','800':'800','A100':'A100',
                  'A200':'A200','A400':'A400','A700':'A700'}[value]) {
        hueKey = value;
      }
    });

    // Lookup and assign the right values
    if ((theme = themeProvider._THEMES[themeName])) {
      if ((intention = theme.colors[intentionName]) ) {
        if (!hueKey) {
          hueKey = intention.hues[hueName];
        }
        if ((hue = themeProvider._PALETTES[intention.name][hueKey]) ) {
          element.css(styled,'rgb('+hue.value[0]+','+hue.value[1]+','+hue.value[2]+')');
          return;
        }
      }
    }
    reportError( "%s='%s' bad or missing attributes", directiveName, input );
  }

  function reportError(errString,name,input) {
    "use strict";
    console.error(errString,name,input);
    console.log('  usage %s="[theme] intention [hue]"',name);
    console.log('    acceptable intentions : primary,accent,warn,background');
    console.log('    acceptable hues       : default,hue-1,hue-2,hue-3');
  }
}());


(function() {
  angular.module('app')
    .directive('clamp', clampDirective);

  clampDirective.$inject = ['$timeout'];
  function clampDirective($timeout) {
    var directive = {
      restrict: 'A',
      link: linkDirective
    };

    return directive;

    function linkDirective(scope, element, attrs) {
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

  return;

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
})();