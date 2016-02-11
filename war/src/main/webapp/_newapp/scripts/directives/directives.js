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


var indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

(function() {
  return angular.module('material.core.colors', ['material.core.theming']).provider('$mdColors', function($mdColorPalette) {
    var DARK_CONTRAST_COLOR, LIGHT_CONTRAST_COLOR, STRONG_LIGHT_CONTRAST_COLOR, addCustomStyle, clearStyleSheet, index, style, stylesheet;
    style = angular.element('<style type="text/css"></style>');
    document.head.appendChild(style[0]);
    stylesheet = style[0].sheet;
    index = 0;
    DARK_CONTRAST_COLOR = [0, 0, 0, 0.87];
    LIGHT_CONTRAST_COLOR = [255, 255, 255, 0.87];
    STRONG_LIGHT_CONTRAST_COLOR = [255, 255, 255];
    addCustomStyle = function(cssname, name, color, contrast) {
      if (contrast == null) {
        contrast = '';
      }
      if (contrast) {
        contrast = "color: " + contrast;
      }
      stylesheet.insertRule(".md-" + cssname + "-" + name + ".text { " + contrast + " !important }", index);
      stylesheet.insertRule(".md-" + cssname + "-" + name + ".background { background-color: " + color + "; " + contrast + " }", index + 1);
      index += 2;
    };
    clearStyleSheet = function() {
      var results;
      results = [];
      while (stylesheet.cssRules.length > 0) {
        results.push(stylesheet.deleteRule(0));
      }
      return results;
    };
    return {
      colorNames: [],
      colorStore: {},
      colorSelected: null,
      themeNames: [],
      themeStore: {},
      getContrastColor: function(palette, hueName) {
        var contrastDarkColors, contrastDefaultColor, contrastLightColors, contrastStrongLightColors;
        contrastDefaultColor = palette.contrastDefaultColor, contrastLightColors = palette.contrastLightColors, contrastStrongLightColors = palette.contrastStrongLightColors, contrastDarkColors = palette.contrastDarkColors;
        if (angular.isString(contrastLightColors)) {
          contrastLightColors = contrastLightColors.split(' ');
        }
        if (angular.isString(contrastStrongLightColors)) {
          contrastStrongLightColors = contrastStrongLightColors.split(' ');
        }
        if (angular.isString(contrastDarkColors)) {
          contrastDarkColors = contrastDarkColors.split(' ');
        }
        if (contrastDefaultColor === 'light') {
          if ((contrastDarkColors != null ? contrastDarkColors.indexOf(hueName) : void 0) > -1) {
            return DARK_CONTRAST_COLOR;
          } else {
            if ((contrastStrongLightColors != null ? contrastStrongLightColors.indexOf(hueName) : void 0) > -1) {
              return STRONG_LIGHT_CONTRAST_COLOR;
            } else {
              return LIGHT_CONTRAST_COLOR;
            }
          }
        } else {
          if ((contrastLightColors != null ? contrastLightColors.indexOf(hueName) : void 0) > -1) {
            if ((contrastStrongLightColors != null ? contrastStrongLightColors.indexOf(hueName) : void 0) > -1) {
              return STRONG_LIGHT_CONTRAST_COLOR;
            } else {
              return LIGHT_CONTRAST_COLOR;
            }
          } else {
            return DARK_CONTRAST_COLOR;
          }
        }
      },
      storeAndLoadPalettes: function(colors, themes, primaryPalette) {
        this.colorStore = colors;
        this.themeStore = themes;
        this.colorNames = Object.keys(colors);
        this.themeNames = Object.keys(themes);
        this.loadPalette(primaryPalette);
      },
      loadPalette: function(newPalette) {
        var cleanedThemeName, color, group, groupName, name, ref, ref1, theme, themeName;
        if (this.colorSelected) {
          clearStyleSheet();
        }
        this.colorSelected = newPalette;
        ref = this.colorStore[newPalette];
        for (name in ref) {
          color = ref[name];
          addCustomStyle('fg', name, color.value, color.contrast);
          addCustomStyle('bg', name, color.value, color.contrast);
        }
        ref1 = this.themeStore;
        for (themeName in ref1) {
          theme = ref1[themeName];
          cleanedThemeName = themeName === 'default' ? '' : themeName + '-';
          for (groupName in theme) {
            group = theme[groupName];
            for (name in group) {
              color = group[name];
              addCustomStyle(cleanedThemeName + groupName, name, color.value, color.contrast);
            }
          }
        }
      },
      $get: function() {
        return {
          colorNames: this.colorNames,
          colorStore: this.colorStore,
          colorSelected: this.colorSelected,
          themeNames: this.themeNames,
          themeStore: this.themeStore,
          loadPalette: this.loadPalette
        };
      }
    };
  }).directive('mdStyle', function($mdColors, $parse) {
    return {
      restrict: 'A',
      link: function(scope, element, attrs) {
        var color, colorNames, colorObject, colorSelected, colorStore, cssName, cssValue, hue, hue2, parsedStyles, ref, results, styles, themeNames, themeStore;
        colorSelected = $mdColors.colorSelected, colorStore = $mdColors.colorStore, colorNames = $mdColors.colorNames, themeStore = $mdColors.themeStore, themeNames = $mdColors.themeNames;
        parsedStyles = $parse(attrs.mdStyle);
        styles = parsedStyles();
        results = [];
        for (cssName in styles) {
          cssValue = styles[cssName];
          ref = cssValue.split('.'), color = ref[0], hue = ref[1], hue2 = ref[2];
          if (color === 'primary' || color === 'accent' || color === 'background' || color === 'foreground' || color === 'warn') {
            color = themeStore['default'][color];
          } else if (indexOf.call(colorNames, color) < 0) {
            color = colorSelected;
            if (themeStore[color]) {
              color = themeStore[color];
              if (hue2) {
                color = color[hue][hue2];
              } else {
                color = color[hue]['default'];
              }
            }
          }
          color = colorStore[color] || color;
          colorObject = color[hue] || color["default"];
          if (colorObject) {
            if (cssName === 'background-color') {
              element.css('color', colorObject.contrast);
            }
            if (angular.isString(attrs.mdContrast)) {
              results.push(element.css(cssName, colorObject.contrast));
            } else {
              results.push(element.css(cssName, colorObject.value));
            }
          } else {
            results.push(void 0);
          }
        }
        return results;
      }
    };
  });
})();