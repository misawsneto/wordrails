// config

var app =  
angular.module('app')
  .config(
    [        '$controllerProvider', '$compileProvider', '$filterProvider', '$provide',
    function ($controllerProvider,   $compileProvider,   $filterProvider,   $provide) {
        
        // lazy controller, directive and service
        app.controller = $controllerProvider.register;
        app.directive  = $compileProvider.directive;
        app.filter     = $filterProvider.register;
        app.factory    = $provide.factory;
        app.service    = $provide.service;
        app.constant   = $provide.constant;
        app.value      = $provide.value;
    }
  ])
  .constant('TRIX', {
    baseUrl: location.protocol + '//' + location.host})

  .config(['$translateProvider', function($translateProvider){
    // Register a loader for the static files
    // So, the module will search missing translation tables under the specified urls.
    // Those urls are [prefix][langKey][suffix].
    $translateProvider.useStaticFilesLoader({
      prefix: '/i18n/',
      suffix: '.js'
    });
    // Tell the module what language to use by default
    $translateProvider.preferredLanguage('pt');
    // Tell the module to store the language in the local storage
    // $translateProvider.useLocalStorage();
  }])

  .config(function(trixProvider, $mdThemingProvider, $mdColorPalette, $provide, $mdColorsProvider){
    trixProvider.setConfig({ url: location.protocol + '//' + 'demo.xarxlocal.com' });

    $mdThemingProvider.definePalette('clear', { "500": "#FFFFFF", "50": "#FFFFFF", "100": "#FFFFFF", "200": "#FFFFFF", "300": "#FFFFFF", "400": "#FFFFFF", "600": "#cbcaca", "700": "#aeadad", "800": "#919090", "900": "#747474", "A100": "#f8f8f8", "A200": "#f4f3f3", "A400": "#ecebeb", "A700": "#aeadad" } );
    $mdThemingProvider.theme('default').primaryPalette('red').accentPalette('clear');
    $mdThemingProvider.theme('dark').primaryPalette('red').accentPalette( 'clear' ).dark();

    // Set default palettes as themes for use in UI.
    $mdThemingProvider.theme('red').primaryPalette('red').accentPalette('clear');
    $mdThemingProvider.theme('pink').primaryPalette('pink').accentPalette('clear');
    $mdThemingProvider.theme('purple').primaryPalette('purple').accentPalette('clear');
    $mdThemingProvider.theme('deep-purple').primaryPalette('deep-purple').accentPalette('clear');
    $mdThemingProvider.theme('indigo').primaryPalette('indigo').accentPalette('clear');
    $mdThemingProvider.theme('blue').primaryPalette('blue').accentPalette('clear');
    $mdThemingProvider.theme('light-blue').primaryPalette('light-blue').accentPalette('clear');
    $mdThemingProvider.theme('cyan').primaryPalette('cyan').accentPalette('clear');
    $mdThemingProvider.theme('teal').primaryPalette('teal').accentPalette('clear');
    $mdThemingProvider.theme('green').primaryPalette('green').accentPalette('clear');
    $mdThemingProvider.theme('light-green').primaryPalette('light-green').accentPalette('clear');
    $mdThemingProvider.theme('lime').primaryPalette('lime').accentPalette('clear');
    $mdThemingProvider.theme('yellow').primaryPalette('yellow').accentPalette('clear');
    $mdThemingProvider.theme('amber').primaryPalette('amber').accentPalette('clear');
    $mdThemingProvider.theme('orange').primaryPalette('orange').accentPalette('clear');
    $mdThemingProvider.theme('deep-orange').primaryPalette('deep-orange').accentPalette('clear');
    $mdThemingProvider.theme('brown').primaryPalette('brown').accentPalette('clear');
    $mdThemingProvider.theme('grey').primaryPalette('grey').accentPalette('clear');
    $mdThemingProvider.theme('blue-grey').primaryPalette('blue-grey').accentPalette('clear');

    $mdThemingProvider.definePalette('myPrimary',{
     '500': '#333333', '50': '#b8b8b8', '100': '#919191', '200': '#757575', '300': '#525252', '400': '#424242', '600': '#242424', '700': '#141414', '800': '#050505', '900': '#000000', 'A100': '#b8b8b8', 'A200': '#919191', 'A400': '#424242', 'A700': '#141414', 'contrastDefaultColor': 'light', 'contrastDarkColors': '50 100 A100 A200'
    });

    $mdThemingProvider.definePalette('myAccent', $mdColorPalette.indigo);

    $mdThemingProvider.definePalette('myWarn', $mdColorPalette.red);

    $mdThemingProvider.theme('default').primaryPalette('myPrimary').accentPalette('myAccent').warnPalette('myWarn');

    $provide.value('themeProvider', $mdThemingProvider);
    $provide.value('colorsProvider', $mdColorsProvider);
    
  })

  .config(createCustomMDCssTheme)

  .run(function($rootScope){
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
  });

  //--------------
  
  function createCustomMDCssTheme($mdThemingProvider, $mdColorsProvider) {
    var colorStore, palette, paletteName, parsePalette, parseTheme, primaryPalette, ref, themeStore;
    colorStore = {};
    parsePalette = function(paletteName, palette) {
      var addHue, colors, copyColors, hueColors, paletteContrast;
      paletteContrast = palette;
      hueColors = $mdThemingProvider._THEMES['default'].colors['primary'].hues;
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
    primaryPalette = $mdThemingProvider._THEMES['default'].colors.primary.name;
    $mdColorsProvider.storeAndLoadPalettes(colorStore, themeStore, primaryPalette);
  }
