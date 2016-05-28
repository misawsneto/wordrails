<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="co.xarx.trix.util.ListUtil"%>
<!DOCTYPE html>
<html lang="en" class="md-background-default background">
<head>
  <meta charset="utf-8" />
    <title>${networkName}</title>
    ${requestedEntityMetas}
    <link rel="shortcut icon" href="${faviconLink}" type="image/x-icon" />
  <meta name="description" content="material, material design, angular material, app, web app, responsive, responsive layout, admin, admin panel, admin dashboard, flat, flat ui, ui kit, AngularJS, ui route, charts, widgets, components" />
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />

  <link rel="stylesheet" href="/libs/assets/animate.css/animate.css" type="text/css" />
  <link rel="stylesheet" href="/libs/assets/font-awesome/css/font-awesome.css" type="text/css" />

  <link rel="stylesheet" href="/libs/angular/angular-loading-bar/build/loading-bar.css" type="text/css" />
  <link rel="stylesheet" href="/libs/angular/angular-material/angular-material.css" type="text/css" />

  <link rel="stylesheet" href="/libs/jquery/bootstrap/dist/css/bootstrap.css" type="text/css" />
  
  <link rel="stylesheet" href="/styles/material-design-icons.css" type="text/css" />
  <link rel="stylesheet" href="/styles/font.css" type="text/css" />
  <link rel="stylesheet" href="/styles/materialize.css" type="text/css" />
  <link rel="stylesheet" href="/styles/home.css" type="text/css" />
  <link rel="stylesheet" href="/styles/app.css" type="text/css" />

  <script type="text/javascript">
      var GLOBAL_URL_HASH = ${buildNumber};
      var initData = ${personData};
  </script>

  <style type="text/css">
      [ng\:cloak], [ng-cloak], [data-ng-cloak], [x-ng-cloak], .ng-cloak, .x-ng-cloak {
          display: none !important;
      }
  </style>
  <base href="/">

</head>
<body ng-app="app">
  <div class="app" ui-view ng-controller="AppCtrl" ng-class="{'bg-cover o-f-h': $state.includes('access')}" ng-style="$state.includes('access') && app.network.splashImageHash ? app.getSplash() : null"></div>
<!-- jQuery -->
  <script src="/libs/jquery/jquery/dist/jquery.js"></script>
  <script src="/libs/jquery/bootstrap/dist/js/bootstrap.js"></script>
<!-- Angular -->
  <script src="/libs/angular/angular/angular.js"></script>
  <script src="/libs/angular/angular-animate/angular-animate.js"></script>
  <script src="/libs/angular/angular-aria/angular-aria.js"></script>
  <script src="/libs/angular/angular-cookies/angular-cookies.js"></script>
  <script src="/libs/angular/angular-messages/angular-messages.js"></script>
  <script src="/libs/angular/angular-resource/angular-resource.js"></script>
  <script src="/libs/angular/angular-sanitize/angular-sanitize.js"></script>
  <script src="/libs/angular/angular-touch/angular-touch.js"></script>
<!-- <script src="../libs/angular/angular-material/angular-material.js"></script> -->
  <script src="scripts/custom-angular-material.js"></script>

  <!-- Vendor -->
  <script src="/libs/angular/angular-ui-router/release/angular-ui-router.js"></script>
  <script src="/libs/angular/ngstorage/ngStorage.js"></script>
  <script src="/libs/angular/angular-ui-utils/ui-utils.js"></script>
  
  <!-- bootstrap -->
  <script src="/libs/angular/angular-bootstrap/ui-bootstrap-tpls.js"></script>

  <!-- lazyload -->
  <script src="/libs/angular/oclazyload/dist/ocLazyLoad.js"></script>
  <!-- translate -->
  <script src="/libs/angular/angular-translate/angular-translate.js"></script>
  <script src="/libs/angular/angular-translate-loader-static-files/angular-translate-loader-static-files.js"></script>
  <script src="/libs/angular/angular-translate-storage-cookie/angular-translate-storage-cookie.js"></script>
  <script src="/libs/angular/angular-translate-storage-local/angular-translate-storage-local.js"></script>
  <!-- loading-bar -->
  <script src="/libs/angular/angular-loading-bar/build/loading-bar.js"></script>

<!-- App -->
  <script src="/js/BaseTrix.js"></script>
  <script src="/scripts/app.js"></script>
  <script src="/libs/jquery/moment/min/moment-with-locales.min.js"></script>
  <script src="/scripts/config.js"></script>
  <script src="/scripts/config.lazyload.js"></script>
  <script src="/scripts/config.router.js"></script>
  <script src="/scripts/app.ctrl.js"></script>

  <script src="/scripts/directives/lazyload.js"></script>
  <script src="/scripts/directives/ui-jp.js"></script>
  <script src="/scripts/directives/ui-nav.js"></script>
  <script src="/scripts/directives/ui-fullscreen.js"></script>
  <script src="/scripts/directives/ui-scroll.js"></script>
  <script src="/scripts/directives/ui-toggle.js"></script>
  <script src="/scripts/directives/directives.js"></script>
  <script src="/scripts/filters/filters.js"></script>
  <script src="/scripts/services/ngstore.js"></script>
  <script src="/scripts/services/ui-load.js"></script>

  <script async="true" src="https://platform.twitter.com/widgets.js"></script>
</body>
</html>