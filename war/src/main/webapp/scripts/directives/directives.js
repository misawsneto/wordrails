angular.module('app')
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
  });

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

