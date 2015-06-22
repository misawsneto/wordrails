<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en" data-ng-app="app">
<head>
  <meta charset="utf-8" />
  <title>${networkName}</title>
  <meta name="description" content="${networkDesciption}" />
  <meta name="keywords" content="${networkKeywords}" />
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
  <link rel="stylesheet" href="../bower_components/bootstrap/dist/css/bootstrap.css" type="text/css" />
  <link rel="stylesheet" href="../bower_components/animate.css/animate.css" type="text/css" />
  <link rel="stylesheet" href="../bower_components/font-awesome/css/font-awesome.min.css" type="text/css" />
  <link rel="stylesheet" href="../bower_components/simple-line-icons/css/simple-line-icons.css" type="text/css" />
  <link rel="stylesheet" href="../bower_components/redactor/redactor.css" />
  <link rel="stylesheet" href="../bower_components/angular-loading-bar/build/loading-bar.min.css">
  <link rel="stylesheet" href="../bower_components/angular-material/angular-material.css" type="text/css" />

  <link rel="stylesheet" href="css/materialdesignicons.css" type="text/css" />

  <link rel="stylesheet" href="css/font.css" type="text/css" />
  <link rel="stylesheet" href="css/app.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="css/override.css?${buildNumber}" type="text/css" />
  <script type="text/javascript">
  	var GLOBAL_URL_HASH = "${buildNumber}"
    var initData = ${personData};
    var initTermPerspective = ${termPerspectiveView};
  </script>
  <style type="text/css">
      [ng\:cloak], [ng-cloak], [data-ng-cloak], [x-ng-cloak], .ng-cloak, .x-ng-cloak {
      display: none !important;
    }
  </style>
  <link rel="shortcut icon" href="data:image/x-icon;," type="image/x-icon">
</head>
<body ng-controller="AppCtrl" ng-cloak>
  <offcanvas></offcanvas>
  <div ng-click="contentClick()" class="app content-wrap" id="app" ng-class="{'app-header-fixed':app.settings.headerFixed, 'app-aside-fixed':app.settings.asideFixed, 'app-aside-folded':app.settings.asideFolded, 'app-aside-dock':app.settings.asideDock, 'container':app.settings.container, 'white-bg': $state.current.name == 'app.post', 'bg-perspective': $state.current.name.indexOf('app.stations') > -1, 'bg-light-grey': $state.current.name.indexOf('app.settings') > -1 || $state.current.name.indexOf('app.bookmarks') > -1 || $state.current.name.indexOf('app.search') > -1 || $state.current.name.indexOf('app.notifications') > -1 || $state.current.name.indexOf('app.user') > -1 || $state.current.name.indexOf('app.publications') > -1}" ui-view></div>


  <!-- jQuery -->
  <script src="../bower_components/jquery/dist/jquery.min.js"></script>

  <!-- Angular -->
  <script src="../bower_components/angular/angular.js"></script>
  <script src="../bower_components/angular-animate/angular-animate.js"></script>
  <script src="../bower_components/angular-aria/angular-aria.js"></script>
  <script src="../bower_components/angular-cookies/angular-cookies.js"></script>
  <script src="../bower_components/angular-messages/angular-messages.js"></script>
  <script src="../bower_components/angular-resource/angular-resource.js"></script>
  <script src="../bower_components/angular-sanitize/angular-sanitize.js"></script>
  <script src="../bower_components/angular-touch/angular-touch.js"></script>
  <script src="../bower_components/angular-material/angular-material.js"></script>

  <script src="../bower_components/angular-ui-router/release/angular-ui-router.js"></script> 
  <script src="../bower_components/ngstorage/ngStorage.js"></script>
  <script src="../bower_components/angular-ui-utils/ui-utils.js"></script>

  <!-- bootstrap -->
  <script src="../bower_components/angular-bootstrap/ui-bootstrap-tpls.js"></script>
  <!-- lazyload -->
  <script src="../bower_components/oclazyload/dist/ocLazyLoad.js"></script>
  <!-- translate -->
  <script src="../bower_components/angular-translate/angular-translate.js"></script>
  <script src="../bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.js"></script>
  <script src="../bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.js"></script>
  <script src="../bower_components/angular-translate-storage-local/angular-translate-storage-local.js"></script>
  <!-- redactor -->
  <script src="../bower_components/redactor/redactor.js"></script>
  <script src="../bower_components/redactor/video.js"></script>
  <script src="../bower_components/redactor/fontsize.js"></script>
  <script src="../bower_components/redactor/counter.js"></script>
  <script src="../bower_components/redactor/fontcolor.js"></script>
  <script src="../bower_components/redactor/pt_br.js"></script>
  <script src="../bower_components/redactor/angular-redactor.js"></script>
  <script src="../bower_components/moment/min/moment-with-locales.min.js"></script>
  <script src="../bower_components/angular-loading-bar/build/loading-bar.min.js"></script>

  <!-- App -->
  <script src="js/app.js?${buildNumber}"></script>
  <script src="js/config.js?${buildNumber}"></script>
  <script src="js/config.lazyload.js?${buildNumber}"></script>
  <script src="js/config.router.js?${buildNumber}"></script>
  <script src="js/main.js?${buildNumber}"></script>
  <script src="js/services/ui-load.js"></script>
  <script src="js/services/trix.js?${buildNumber}"></script>
  <script src="js/filters/filters.js?${buildNumber}"></script>
  <script src="js/directives/setnganimate.js"></script>
  <script src="js/directives/ui-butterbar.js"></script>
  <script src="js/directives/ui-focus.js"></script>
  <script src="js/directives/ui-fullscreen.js"></script>
  <script src="js/directives/ui-jq.js"></script>
  <script src="js/directives/ui-module.js"></script>
  <script src="js/directives/ui-nav.js"></script>
  <script src="js/directives/ui-scroll.js"></script>
  <script src="js/directives/ui-shift.js"></script>
  <script src="js/directives/ui-toggleclass.js"></script>
  <script src="js/directives/custom.js?${buildNumber}"></script>
  <script src="js/directives/directives.js?${buildNumber}"></script>
  <script src="js/controllers/bootstrap.js"></script>

  <script src="/js/BaseTrix.js?${buildNumber}"></script>

  <!-- Sly -->
  <script src="js/sly/sly.plugins.js?${buildNumber}"></script>
  <script src="js/sly/sly.min.js?${buildNumber}"></script>
  <!-- Sly end -->
  <script src="js/util.js"></script>
  <!-- Lazy loading -->
  <script src="js/iframe-api.js"></script>
</body>
</html>
