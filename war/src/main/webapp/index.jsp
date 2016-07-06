<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="co.xarx.trix.util.ListUtil"%>
<!DOCTYPE html>
<html lang="en" class="md-background-default background">
<head>
  <meta charset="utf-8" />
    <title>${networkName}</title>
    ${requestedEntityMetas}
    <link rel="shortcut icon" href="${faviconLink}" type="image/x-icon" />
  <meta name="description" content="" />
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1"/>
    <meta name="mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-barstyle" content="black-translucent">

  <link rel="stylesheet" href="/libs/assets/animate.css/animate.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="/libs/assets/font-awesome/css/font-awesome.css?${buildNumber}" type="text/css" />

  <link rel="stylesheet" href="/libs/angular/angular-loading-bar/build/loading-bar.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="/libs/angular/angular-material/angular-material.css?${buildNumber}" type="text/css" />

  <link rel="stylesheet" href="/libs/jquery/bootstrap/dist/css/bootstrap.css?${buildNumber}" type="text/css" />
  
  <link rel="stylesheet" href="/styles/material-design-icons.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="/styles/font.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="/styles/materialize.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="/styles/home.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="/styles/app.css?${buildNumber}" type="text/css" />

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
  ${requestedEntityHiddenHtml}
  <div class="app" ui-view ng-controller="AppCtrl" ng-class="{'bg-cover o-f-h': $state.includes('access')}" ng-style="$state.includes('access') && app.network.splashImageHash ? app.getSplash() : null"></div>
<!-- jQuery -->
  <script src="/libs/jquery/jquery/dist/jquery.js?${buildNumber}"></script>
  <script src="/libs/jquery/bootstrap/dist/js/bootstrap.js?${buildNumber}"></script>
<!-- Angular -->
  <script src="/libs/angular/angular/angular.js?${buildNumber}"></script>
  <script src="/libs/angular/angular-animate/angular-animate.js?${buildNumber}"></script>
  <script src="/libs/angular/angular-aria/angular-aria.js?${buildNumber}"></script>
  <script src="/libs/angular/angular-cookies/angular-cookies.js?${buildNumber}"></script>
  <script src="/libs/angular/angular-messages/angular-messages.js?${buildNumber}"></script>
  <script src="/libs/angular/angular-resource/angular-resource.js?${buildNumber}"></script>
  <script src="/libs/angular/angular-sanitize/angular-sanitize.js?${buildNumber}"></script>
  <script src="/libs/angular/angular-touch/angular-touch.js?${buildNumber}"></script>
<!-- <script src="../libs/angular/angular-material/angular-material.js?${buildNumber}"></script> -->
  <script src="scripts/custom-angular-material.js?${buildNumber}"></script>

  <!-- Vendor -->
  <script src="/libs/angular/angular-ui-router/release/angular-ui-router.js?${buildNumber}"></script>
  <script src="/libs/angular/ngstorage/ngStorage.js?${buildNumber}"></script>
  <script src="/libs/angular/angular-ui-utils/ui-utils.js?${buildNumber}"></script>
  
  <!-- bootstrap -->
  <script src="/libs/angular/angular-bootstrap/ui-bootstrap-tpls.js?${buildNumber}"></script>

  <!-- lazyload -->
  <script src="/libs/angular/oclazyload/dist/ocLazyLoad.js?${buildNumber}"></script>
  <!-- translate -->
  <script src="/libs/angular/angular-translate/angular-translate.js?${buildNumber}"></script>
  <script src="/libs/angular/angular-translate-loader-static-files/angular-translate-loader-static-files.js?${buildNumber}"></script>
  <script src="/libs/angular/angular-translate-storage-cookie/angular-translate-storage-cookie.js?${buildNumber}"></script>
  <script src="/libs/angular/angular-translate-storage-local/angular-translate-storage-local.js?${buildNumber}"></script>
  <!-- loading-bar -->
  <script src="/libs/angular/angular-loading-bar/build/loading-bar.js?${buildNumber}"></script>
  <script src="/libs/angular/satellizer/satellizer.min.js?${buildNumber}"></script>

<!-- App -->
  <script src="/js/BaseTrix.js?${buildNumber}"></script>
  <script src="/scripts/app.js?${buildNumber}"></script>
  <script src="/libs/jquery/moment/min/moment-with-locales.min.js?${buildNumber}"></script>
  <script src="/scripts/config.js?${buildNumber}"></script>
  <script src="/scripts/config.lazyload.js?${buildNumber}"></script>
  <script src="/scripts/config.router.js?${buildNumber}"></script>
  <script src="/scripts/app.ctrl.js?${buildNumber}"></script>

  <script src="/scripts/directives/lazyload.js?${buildNumber}"></script>
  <script src="/scripts/directives/ui-jp.js?${buildNumber}"></script>
  <script src="/scripts/directives/ui-nav.js?${buildNumber}"></script>
  <script src="/scripts/directives/ui-fullscreen.js?${buildNumber}"></script>
  <script src="/scripts/directives/ui-scroll.js?${buildNumber}"></script>
  <script src="/scripts/directives/ui-toggle.js?${buildNumber}"></script>
  <script src="/scripts/directives/directives.js?${buildNumber}"></script>
  <script src="/scripts/filters/filters.js?${buildNumber}"></script>
  <script src="/scripts/services/ngstore.js?${buildNumber}"></script>
  <script src="/scripts/services/ui-load.js?${buildNumber}"></script>

  <script async="true" src="https://platform.twitter.com/widgets.js?${buildNumber}"></script>
</body>
</html>
