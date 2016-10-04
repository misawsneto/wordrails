// config

  app
  .config(['$compileProvider', function ($compileProvider) {
    $compileProvider.debugInfoEnabled(false);
  }])
  .config(
    [        '$controllerProvider', '$compileProvider', '$filterProvider', '$provide', 'cfpLoadingBarProvider',
    function ($controllerProvider,   $compileProvider,   $filterProvider,   $provide ,  cfpLoadingBarProvider) {
        
        // lazy controller, directive and service
        app.controller = $controllerProvider.register;
        app.directive  = $compileProvider.directive;
        app.filter     = $filterProvider.register;
        app.factory    = $provide.factory;
        app.service    = $provide.service;
        app.constant   = $provide.constant;
        app.value      = $provide.value;

        cfpLoadingBarProvider.includeSpinner = false;
    }
  ])
  .constant('TRIX', {
    baseUrl: location.protocol + '//' + location.host,
    //baseUrl: location.protocol + '//' + 'demo.xarxlocal.com',
    defaultMargin: 0
  })

  .config(['$translateProvider', function($translateProvider){
    // Register a loader for the static files
    // So, the module will search missing translation tables under the specified urls.
    // Those urls are [prefix][langKey][suffix].
    $translateProvider.useStaticFilesLoader({
      prefix: '/i18n/',
      suffix: '.js?' + GLOBAL_URL_HASH
    });
    // Tell the module what language to use by default
    $translateProvider.preferredLanguage('pt');
    moment.locale('pt');
    // Tell the module to store the language in the local storage
    // $translateProvider.useLocalStorage();
  }])

  .config(['trixProvider', '$mdThemingProvider', '$mdColorPalette', '$provide', '$mdColorsProvider',
    function(trixProvider, $mdThemingProvider, $mdColorPalette, $provide, $mdColorsProvider){
      trixProvider.setConfig({ url: location.protocol + '//' + location.host });
  
      $mdThemingProvider.setNonce('default')
  
      $mdThemingProvider.definePalette('myPrimary',{
          '500': '#333333', '50': '#b8b8b8', '100': '#919191', '200': '#757575', '300': '#525252', '400': '#424242', '600': '#242424', '700': '#141414', '800': '#050505', '900': '#000000', 'A100': '#b8b8b8', 'A200': '#919191', 'A400': '#424242', 'A700': '#141414', 'contrastDefaultColor': 'light', 'contrastDarkColors': '50 100 A100 A200'
      });
  
      $mdThemingProvider.definePalette('myAccent',{
          '500': '#333333', '50': '#b8b8b8', '100': '#919191', '200': '#757575', '300': '#525252', '400': '#424242', '600': '#242424', '700': '#141414', '800': '#050505', '900': '#000000', 'A100': '#b8b8b8', 'A200': '#919191', 'A400': '#424242', 'A700': '#141414', 'contrastDefaultColor': 'light', 'contrastDarkColors': '50 100 A100 A200'
      });
  
      $mdThemingProvider.definePalette('myWarn',{
          '500': '#333333', '50': '#b8b8b8', '100': '#919191', '200': '#757575', '300': '#525252', '400': '#424242', '600': '#242424', '700': '#141414', '800': '#050505', '900': '#000000', 'A100': '#b8b8b8', 'A200': '#919191', 'A400': '#424242', 'A700': '#141414', 'contrastDefaultColor': 'light', 'contrastDarkColors': '50 100 A100 A200'
      });
  
      $mdThemingProvider.definePalette('myBackground',{
          '500': '#EFEFEF', '50': '#b8b8b8', '100': '#919191', '200': '#757575', '300': '#FFFFFF', '400': '#424242', '600': '#242424', '700': '#141414', '800': '#C1C1C1', '900': '#000000', 'A100': '#FFFFFF', 'A200': '#919191', 'A400': '#424242', 'A700': '#141414', 'contrastDefaultColor': 'light', 'contrastDarkColors': '500 300 800 A100'
      });
  
      $mdThemingProvider.theme('default')
      .primaryPalette('myPrimary')
      .accentPalette('myAccent',{'default':'300', 'hue-1': '500', 'hue-2': '800', 'hue-3': 'A100'})
      .warnPalette('myWarn')
      .backgroundPalette('myBackground', {'default':'500', 'hue-1': '300', 'hue-2': '800', 'hue-3': 'A100'});
  
      if(location.pathname.slice(0, '/settings'.length) == '/settings')
        $mdThemingProvider.alwaysWatchTheme(true);
  
      $provide.value('themeProvider', $mdThemingProvider);
      $provide.value('colorsProvider', $mdColorsProvider);
      
    }])

  .config(['$mdThemingProvider', '$mdColorsProvider', function ($mdThemingProvider, $mdColorsProvider){
    createCustomMDCssTheme($mdThemingProvider, $mdColorsProvider)  
  }])
  .value('froalaConfig', {
    toolbarInline: false,
    placeholderText: 'Edit Your Content Here!'
    
  })

  .run(['$rootScope', function($rootScope){
      $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error){ 
        var errorObj = {
          event: event, 
          toState: toState, 
          toParams: toParams, 
          fromState: fromState, 
          fromParams: fromParams, 
          error: error
        }
  
        window.console && console.error(errorObj);
        window.console && console.error(error.message);
      });
    }])
  .factory('focus', ['$timeout', '$window', function($timeout, $window) {
      return function(id) {
        // timeout makes sure that it is invoked after any other event has been triggered.
        // e.g. click events that need to run before the focus or
        // inputs elements that are in a disabled state but are enabled when those events
        // are triggered.
        $timeout(function() {
          var element = $window.document.getElementById(id);
          if(element)
            element.focus();
        });
      };
    }])
  .directive('mdChips', ['$timeout', function ($timeout) {
    return {
      restrict: 'E',
      require: 'mdChips', // Extends the original mdChips directive
      link: function (scope, element, attributes, mdChipsCtrl) {
  
        var mouseUpActions = [];
  
        mdChipsCtrl.onInputBlur = function (event) {
          this.inputHasFocus = false;
  
          mouseUpActions.push((function(){
            var chipBuffer = this.getChipBuffer();
            if (chipBuffer != "") { // REQUIRED, OTHERWISE YOU'D GET A BLANK CHIP
              this.appendChip(chipBuffer);
              this.resetChipBuffer();
            }
          }).bind(this));
        };
  
        window.addEventListener('click', function(event){
          while(mouseUpActions.length>0){
            var action = mouseUpActions.splice(0,1)[0];
            $timeout(function(){
              $timeout(action);
            });
          }
        },false);
      }
    }
  }]);

  //--------------

  function createCustomMDCssTheme($mdThemingProvider, $mdColorsProvider, newThemeName) {
    var newThemeName = newThemeName ? newThemeName : 'default'
    var colorStore, palette, paletteName, parsePalette, parseTheme, primaryPalette, ref, themeStore;
    colorStore = {};
    //Function to convert hex format to a rgb color
    function rgb2hex(rgb) {
      return "#" + hex(rgb[0]) + hex(rgb[1]) + hex(rgb[2]);
    }

    function hex(x) {
      var hexDigits = new Array("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"); 
      return isNaN(x) ? "00" : hexDigits[(x - x % 16) / 16] + hexDigits[x % 16];
    }
    parsePalette = function(paletteName, palette) {
      var addHue, colors, copyColors, hueColors, paletteContrast;
      paletteContrast = palette;
      hueColors = $mdThemingProvider._THEMES[newThemeName].colors['primary'].hues;
      colors = {};
      addHue = function(hueName) {
        var contrastColor;
        contrastColor = $mdThemingProvider._rgba($mdColorsProvider.getContrastColor(palette, hueColors[hueName]));
        return colors[hueName] = {
          value: palette[hueColors[hueName]],
          contrast: contrastColor
        };
      };
      copyColors = function(colorName) {
        var contrastColor;
        if (/#([0-9A-Fa-f]{3}|[0-9A-Fa-f]{6})\b/.test(palette[colorName])) {
          contrastColor = $mdThemingProvider._rgba($mdColorsProvider.getContrastColor(palette, colorName));
          colors[colorName] = {
            value: palette[colorName],
            contrast: contrastColor
          };
        }else if(palette[colorName].contrast && palette[colorName].value){
          var colVal = palette[colorName].value;
          var colCon = palette[colorName].contrast;
          colors[colorName] = {
            value: rgb2hex(colVal),
            contrast: (colCon[3]) ? ("rgba("+colCon[0]+","+colCon[1]+","+colCon[2]+","+colCon[3]+")") : ("rgb("+colCon[0]+","+colCon[1]+","+colCon[2]+")")
          };
        }
      };
      colorStore[paletteName] = colors;
      Object.keys(palette).forEach(copyColors);
      Object.keys(hueColors).forEach(addHue);
    };
    ref = $mdThemingProvider._PALETTES;
    for (paletteName in ref) {
      palette = ref[paletteName];
      parsePalette(paletteName, palette);
    }
    themeStore = {};
    parseTheme = function(themeName) {
      var colors, defineColors, themeColorGroups;
      themeColorGroups = $mdThemingProvider._THEMES[themeName].colors;
      colors = {};
      defineColors = function(themeGroup) {
        var base, definedColors, item, ref1, value;
        if ((base = themeStore[themeName])[themeGroup] == null) {
          base[themeGroup] = {};
        }
        definedColors = colorStore[themeColorGroups[themeGroup].name];
        ref1 = themeColorGroups[themeGroup].hues;
        for (item in ref1) {
          value = ref1[item];
          themeStore[themeName][themeGroup][item] = definedColors[value];
        }
      };
      if (themeStore[themeName] == null) {
        themeStore[themeName] = {};
      }
      Object.keys(themeColorGroups).forEach(defineColors);
    };
    Object.keys($mdThemingProvider._THEMES).forEach(parseTheme);
    primaryPalette = $mdThemingProvider._THEMES[newThemeName].colors.primary.name;
    $mdColorsProvider.storeAndLoadPalettes(colorStore, themeStore, primaryPalette);
  }


var indexOf = [].indexOf || function(item) { for (var i = 0, l = this.length; i < l; i++) { if (i in this && this[i] === item) return i; } return -1; };

(function() {
  return angular.module('material.core.colors', ['material.core.theming'])
  .provider('$mdColors', ['$mdColorPalette', function($mdColorPalette) {
      var DARK_CONTRAST_COLOR, LIGHT_CONTRAST_COLOR, STRONG_LIGHT_CONTRAST_COLOR, addCustomStyle, clearStyleSheet, index, style, stylesheet;
      style = angular.element('<style id="customMDCss" type="text/css"></style>');
      document.head.appendChild(style[0]);
      stylesheet = style[0].sheet;
      index = 0;
      DARK_CONTRAST_COLOR = [0, 0, 0, 0.87];
      LIGHT_CONTRAST_COLOR = [255, 255, 255, 0.87];
      STRONG_LIGHT_CONTRAST_COLOR = [255, 255, 255];
  
      addCustomStyle = function(cssname, name, color, contrast) {
        var contrastColor = null;
        if (contrast == null) {
          contrast = '';
        }
        if (contrast) {
          contrastColor = contrast;
          contrast = "color: " + contrast;
        }
        stylesheet.insertRule(".md-" + cssname + "-" + name + ".text { " + contrast + " !important }", index); index++;
        stylesheet.insertRule(".md-" + cssname + "-" + name + ".background { background-color: " + color + "; " + contrast + " }", index ); index++;
  
        if(cssname === 'primary' && name === 'default'){
          stylesheet.insertRule(".fr-toolbar { border-color: " + color + "!important}", index); index++;
          stylesheet.insertRule(".nav-lines > li.active:after{border-bottom-color: " + color + "}", index); index++;
          stylesheet.insertRule(".md-primary-default .nav-lines > li.active:after{border-bottom-color: " + contrastColor + "}", index); index++;
          stylesheet.insertRule(".sl-item {border-color: "+ color +"}", index); index++;
          stylesheet.insertRule(".md-primary-default2.background {background-color: "+ color +"}", index); index++;
        }
  
        if(cssname === 'primary' && name === 'hue-1'){
          stylesheet.insertRule(".md-primary-hue-1 .nav-lines > li.active:after{border-bottom-color: " + contrastColor + "}", index); index++;
          stylesheet.insertRule(".b-l-primary {border-left-color: " + color + "}", index); index++;
          stylesheet.insertRule("#comment-box:focus{border: 1px solid " + color + "}", index); index++;
        }
  
        if(cssname === 'warn' && name === 'default'){
          stylesheet.insertRule("md-toast.md-" + cssname + "-" + name + " > div { background-color: " + color + "!important; color: #f5f5f5 }", index); index ++;
        }
        if(cssname === 'accent' && name === 'default'){
          stylesheet.insertRule(
            "#loading-bar {webkit-box-shadow: " + color + " 1px 0 6px 1px;" +
              "box-shadow: " + color + " 1px 0 6px 1px;"+
              "-moz-border-radius: 100%;"+
              "-webkit-border-radius: 100%;}" , index); index++;
          stylesheet.insertRule(
            "#loading-bar .bar {background-color: " + color + " }" , index); index++;
          stylesheet.insertRule(
            "#loading-bar-spinner .spinner-icon {"+
              "border-top-color: " + color + ";" +
              "border-left-color: " + color + ";" +
            "}" , index); index++;
          stylesheet.insertRule(".sub-section-tools .active i{color: " + color + "}", index); index++;
          stylesheet.insertRule(".slider .indicators .indicator-item.active {background-color: " + color + "}", index); index++;
          stylesheet.insertRule(".not-trans.md-button:not([disabled]).md-icon-button:hover{ background-color: " + color + "}", index); index++
          stylesheet.insertRule(".not-trans.md-button:not([disabled]).md-icon-button:focus{ background-color: " + color + "}", index); index++
          stylesheet.insertRule(".streamline {border-color: " + color + "}", index); index++;
          stylesheet.insertRule("#send-comment:not([disabled]){background-color: " + color + "}", index); index++;
        }
        if(cssname === 'accent' && name === 'hue-1'){
          stylesheet.insertRule(".df-tab-menu.nav-lines > li.active::after{border-bottom-color: " + color + "}", index); index++; 
        }
        if(cssname === 'background' && name === 'default'){
          stylesheet.insertRule(".mdp-datepicker .md-content,"+
                                ".mdp-datepicker md-dialog-content,"+
                                ".mdp-datepicker md-dialog-actions,"+
                                ".mdp-timepicker .md-content,"+
                                ".mdp-timepicker md-dialog-content,"+
                                ".mdp-timepicker md-dialog-actions,"+
                                ".default-bg md-content,"+
                                ".default-bg md-dialog-actions,"+
                                ".default-bg md-dialog-content{ "+
                                "   background-color: " + color + "!important; " + contrast + " }", index); index++;
          stylesheet.insertRule(".fr-toolbar .fr-command.fr-btn, .fr-popup .fr-command.fr-btn{" + contrast + "}", index); index++;
          stylesheet.insertRule(".fr-toolbar .fr-command.fr-btn.fr-dropdown::after, .fr-popup .fr-command.fr-btn.fr-dropdown::after{" + contrast + "}", index); index++;
          stylesheet.insertRule(".border-bg-color{border: 1px solid; border-color:" + contrastColor + "}", index); index++;
        }
        if(cssname === 'background' && name === 'hue-1'){
          stylesheet.insertRule(".fr-toolbar, .card, .panel, .panel-card { background-color: " + color + "!important; " + contrast + " }", index); index++;
          stylesheet.insertRule(".default-bg-text button.md-button { " + contrast + " }", index); index++;
          stylesheet.insertRule(".default-bg-text md-input-container:not(.md-input-invalid).md-input-has-value label { " + contrast + " }", index); index++;
          stylesheet.insertRule(".default-bg-text md-input-container .md-input { " + contrast + "!important }", index); index++;
        }
        if(cssname === 'background' && name === 'hue-3'){
          stylesheet.insertRule(".fr-box.fr-basic .fr-wrapper, .dropdown-menu { background-color: " + color + ";" + contrast + " }", index); index++;
          stylesheet.insertRule(".with-arrow > .arrow:after, .advanced-search-menu > .arrow:after { border-bottom-color: " + color + "!important; }", index); index++;
        }
      };
      clearStyleSheet = function() {
        var results;
        results = [];
        index = 0;
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
        clearStyleSheet: function(){
          return clearStyleSheet();
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
                if(color)
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
    }]).directive('mdStyle', ['$mdColors', '$parse', function($mdColors, $parse) {
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
    }])

    .directive('mdStyleColor', ['$mdColorPalette', '$mdThemingProvider',
      function ($mdColorPalette, $mdThemingProvider) {
        return {
          restrict: 'A',
          scope: { mdStyleColor: '=' },
          link: function (scope, element, attrs) {
            for (var p in scope.mdStyleColor) {
              if (scope.mdStyleColor.hasOwnProperty(p)) {

                var themeColors = _theme.colors;

                var split = (scope.mdStyleColor[p] || '').split('.');
                if (split.length < 2) split.unshift('primary');

                var hueR   = split[1] || 'hue-1';    // 'hue-1'
                var colorR = split[0] || 'primary';  // 'warn'

                // Absolute color: 'orange'
                var colorA = themeColors[colorR] ?
                  themeColors[colorR].name : colorR;

                // Absolute Hue: '500'
                var hueA =
                  themeColors[colorR] ?
                  themeColors[colorR].hues[hueR] || hueR :
                  hueR;

                var colorValue = $mdColorPalette[colorA][hueA] ?
                  $mdColorPalette[colorA][hueA].value :
                  $mdColorPalette[colorA]['500'].value;

                element.css(p, 'rgb('+colorValue.join(',')+')');

              }
            }
          }
        }
      }]);

})();