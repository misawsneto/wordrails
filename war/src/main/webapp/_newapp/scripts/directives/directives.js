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

  .directive('focusPoint', function () {
      'use strict';
      
      return {
          restrict: 'A',
          replace: true,
          scope: {
              model: '=ngModel'
          },
          template: '<div class="focus-point">' +
          '<div class="focus-area">' +
          '<span class="target" style="left: {{ x }}%; top: {{ y }}%"></span>' +
          '<img src="{{ src }}" alt="" ng-mousemove="onMouseMove($event)" ng-mousedown="onMouseDown($event)" ng-mouseup="onMouseUp($event)" draggable="false" class="source" />' +
          '</div>' +
          '<span style="background-image: url(\'{{ src }}\'); background-position: {{ x }}% {{ y }}%" class="preview-portrait"></span>' +
          '<span style="background-image: url(\'{{ src }}\'); background-position: {{ x }}% {{ y }}%" class="preview"></span>' +
          '<span style="background-image: url(\'{{ src }}\'); background-position: {{ x }}% {{ y }}%" class="preview-landscape"></span>' +
          '<span style="background-image: url(\'{{ src }}\'); background-position: {{ x }}% {{ y }}%" class="preview-wide"></span>' +
          '<div class="info">{{ x }}% / {{ y }}%</div>' +
          '</div>',
          link: function (scope, element, attr) {
              var dragging = false;
              scope.src = attr.src;
              
              scope.onMouseDown = function (e) {
                  scope.update(e);
                  dragging = true;
              };
              
              scope.onMouseMove = function (e) {
                  if (dragging === true) {
                      scope.update(e);
                  }
              };
              
              scope.onMouseUp = function (e) {
                  e.preventDefault();
                  dragging = false;
              };
              
              scope.update = function (e) {
                  e.preventDefault();
                  var offset = scope.offset(e.target);
                  scope.x = Math.round(((e.pageX - offset.left) / e.target.clientWidth) * 100);
                  scope.y = Math.round(((e.pageY - offset.top) / e.target.clientHeight) * 100);
              };
              
              scope.offset = function (elm) {
                  try { return elm.offset(); } catch (e) { }
                  var body = document.documentElement || document.body;
                  return {
                      left: elm.getBoundingClientRect().left + (window.pageXOffset || body.scrollLeft),
                      top: elm.getBoundingClientRect().top + (window.pageYOffset || body.scrollTop)
                  };
              };
          }
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

