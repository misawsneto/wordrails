<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="co.xarx.trix.util.ListUtil"%>
<!DOCTYPE html>
<html lang="en" data-ng-app="app">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1">
  <title>${networkName}</title>
  ${requestedEntityMetas}
  <link rel="shortcut icon" href="${not empty faviconLink?faviconLink:''}" type="image/x-icon" />

  <meta name="description" content="${networkDesciption}" />
  <meta name="keywords" content="${networkKeywords}" />
  <meta name="google-signin-client_id" content="940891630461-0a0krbd2djhnpd0282rskgdsjgi0n018.apps.googleusercontent.com">
  <link rel="stylesheet" href="/bower_components/bootstrap/dist/css/bootstrap.css" type="text/css" />
  <link rel="stylesheet" href="/bower_components/animate.css/animate.min.css" type="text/css" />
  <link rel="stylesheet" href="/bower_components/font-awesome/css/font-awesome.min.css" type="text/css" />
  <link rel="stylesheet" href="/bower_components/simple-line-icons/css/simple-line-icons.css" type="text/css" />
  <link rel="stylesheet" href="/bower_components/redactor/redactor.css" />
  <link rel="stylesheet" href="/bower_components/angular-loading-bar/build/loading-bar.min.css">
  <link rel="stylesheet" href="/bower_components/angular-material/angular-material.min.css" type="text/css" />

  <link rel="stylesheet" href="/css/materialdesignicons.min.css" type="text/css" />

  <link rel="stylesheet" href="/css/font.css" type="text/css" />
  <link rel="stylesheet" href="/css/app.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="/css/override.css?${buildNumber}" type="text/css" />
  <script type="text/javascript">
  	var GLOBAL_URL_HASH = "${buildNumber}"
    var initData = ${not empty personData?personData:'\'\''};
    var initTermPerspective = ${not empty termPerspectiveView?termPerspectiveView:'\'\''};
    var angularHttp = null;
    var trixSdk = null;
  </script>
  <style type="text/css">
      [ng\:cloak], [ng-cloak], [data-ng-cloak], [x-ng-cloak], .ng-cloak, .x-ng-cloak {
      display: none !important;
    }
  </style>
  <base href="/">
</head>
<body ng-controller="AppCtrl" ng-cloak>

  ${not empty requestedEntityHiddenHtml?requestedEntityHiddenHtml:''}

  <div ng-click="contentClick()" class="app content-wrap" id="app" ng-class="{'is-logged': app.isLogged, 'mobile-screen': app.isMobile, 'app-header-fixed':app.settings.headerFixed, 'app-aside-fixed':app.settings.asideFixed, 'app-aside-folded':app.settings.asideFolded, 'app-aside-dock':app.settings.asideDock, 'container':app.settings.container, 'white-bg': $state.current.name == 'app.post', 'bg-perspective': $state.includes('app.stations'), 'bg-light-grey': $state.includes('app.settings') || $state.includes('app.bookmarks') || $state.includes('app.search') || $state.includes('app.notifications') || $state.includes('app.user') || $state.includes('app.publications') || $state.includes('app.userstats') || $state.includes('app.tagspage'), 'bg-light': $state.includes('access'), 'bg-create-network': $state.includes('access.createnetwork')}" ui-view></div>

  <!-- Google -->
  <script src="https://apis.google.com/js/platform.js" async defer></script>

  <!-- jQuery -->
  <script src="${pageContext.request.contextPath}/bower_components/jquery/dist/jquery.min.js"></script>

  <!-- Angular -->
  <script src="/bower_components/angular/angular.js"></script>
  <script src="/bower_components/angular-animate/angular-animate.js"></script>
  <script src="/bower_components/angular-aria/angular-aria.js"></script>
  <script src="/bower_components/angular-cookies/angular-cookies.min.js"></script>
  <script src="/bower_components/angular-messages/angular-messages.js"></script>
  <script src="/bower_components/angular-resource/angular-resource.js"></script>
  <script src="/bower_components/angular-sanitize/angular-sanitize.js"></script>
  <script src="/bower_components/angular-touch/angular-touch.js"></script>
  <script src="/bower_components/angular-material/angular-material.min.js"></script>

  <script src="${pageContext.request.contextPath}/bower_components/angular-ui-router/release/angular-ui-router.js"></script>
  <script src="${pageContext.request.contextPath}/bower_components/ngstorage/ngStorage.js"></script>
  <!-- <script src="${pageContext.request.contextPath}/bower_components/angular-ui-utils/ui-utils.js"></script> -->

  <!-- bootstrap -->
  <script src="${pageContext.request.contextPath}/bower_components/angular-bootstrap/ui-bootstrap-tpls.js"></script>
  <!-- lazyload -->
  <script src="${pageContext.request.contextPath}/bower_components/oclazyload/dist/ocLazyLoad.js"></script>
  <!-- translate -->
  <!-- <script src="/bower_components/angular-translate/angular-translate.js"></script>
  <script src="/bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.js"></script>
  <script src="/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.js"></script>
  <script src="/bower_components/angular-translate-storage-local/angular-translate-storage-local.js"></script> -->
  <!-- redactor -->
  <script src="${pageContext.request.contextPath}/bower_components/redactor/redactor.min.js"></script>
  <script src="${pageContext.request.contextPath}/bower_components/redactor/video.js"></script>
  <script src="${pageContext.request.contextPath}/bower_components/redactor/fontsize.js"></script>
  <script src="${pageContext.request.contextPath}/bower_components/redactor/counter.js"></script>
  <script src="${pageContext.request.contextPath}/bower_components/redactor/fontcolor.js"></script>
  <script src="${pageContext.request.contextPath}/bower_components/redactor/pt_br.js"></script>
  <script src="${pageContext.request.contextPath}/bower_components/redactor/angular-redactor.js"></script>
  <script src="${pageContext.request.contextPath}/bower_components/moment/min/moment-with-locales.min.js"></script>
  <script src="${pageContext.request.contextPath}/bower_components/angular-loading-bar/build/loading-bar.min.js"></script>

  <!-- App -->
  <script src="${pageContext.request.contextPath}/js/app.js?${buildNumber}"></script>
  <script src="${pageContext.request.contextPath}/js/config.js?${buildNumber}"></script>
  <script src="${pageContext.request.contextPath}/js/config.lazyload.js?${buildNumber}"></script>
  <script src="${pageContext.request.contextPath}/js/config.router.js?${buildNumber}"></script>
  <script src="${pageContext.request.contextPath}/js/main.js?${buildNumber}"></script>
  <script src="${pageContext.request.contextPath}/js/services/ui-load.js"></script>
  <script src="${pageContext.request.contextPath}/js/services/trix.js?${buildNumber}"></script>
  <script src="${pageContext.request.contextPath}/js/filters/filters.js?${buildNumber}"></script>
  <script src="${pageContext.request.contextPath}/js/directives/setnganimate.js"></script>
  <script src="${pageContext.request.contextPath}/js/directives/ui-butterbar.js"></script>
  <script src="${pageContext.request.contextPath}/js/directives/ui-focus.js"></script>
  <script src="${pageContext.request.contextPath}/js/directives/ui-fullscreen.js"></script>
  <script src="${pageContext.request.contextPath}/js/directives/ui-jq.js"></script>
  <script src="${pageContext.request.contextPath}/js/directives/ui-module.js"></script>
  <script src="${pageContext.request.contextPath}/js/directives/ui-nav.js"></script>
  <script src="${pageContext.request.contextPath}/js/directives/ui-scroll.js"></script>
  <script src="${pageContext.request.contextPath}/js/directives/ui-shift.js"></script>
  <script src="${pageContext.request.contextPath}/js/directives/ui-toggleclass.js"></script>
  <script src="${pageContext.request.contextPath}/js/directives/custom.js?${buildNumber}"></script>
  <script src="${pageContext.request.contextPath}/js/directives/directives.js?${buildNumber}"></script>
  <script src="${pageContext.request.contextPath}/js/controllers/bootstrap.js"></script>

  <script src="${pageContext.request.contextPath}/js/BaseTrix.js?${buildNumber}"></script>

  <!-- Sly -->
  <script src="${pageContext.request.contextPath}/js/sly/sly.plugins.js?${buildNumber}"></script>
  <script src="${pageContext.request.contextPath}/js/sly/sly.min.js?${buildNumber}"></script>
  <!-- Sly end -->
  <script src="${pageContext.request.contextPath}/js/util.js"></script>
  <!-- Lazy loading -->
<!--   <script src="/js/iframe-api.js"></script>
<script src="/js/codebird.js?${buildNumber}"></script> -->
</body>
</html>
