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
<script src="scripts/app.src.js"></script>

</body>
</html>