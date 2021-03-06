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

  <!-- <link rel="stylesheet" href="/libs/angular/angular-material/angular-material.min.css?${buildNumber}" type="text/css" /> -->
  <link rel="stylesheet" href="/styles/app.min.css?${buildNumber}" type="text/css" />

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
  <script src="scripts/app.min.js?${buildNumber}"></script>

<script>
    ${trackingId}
</script>

</body>
</html>