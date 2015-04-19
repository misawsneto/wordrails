<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en" data-ng-app="app">
<head>
  <meta charset="utf-8" />
  <title>Angular version | Angulr</title>
  <meta name="description" content="Angularjs, Html5, Music, Landing, 4 in 1 ui kits package" />
  <meta name="keywords" content="AngularJS, angular, bootstrap, admin, dashboard, panel, app, charts, components,flat, responsive, layout, kit, ui, route, web, app, widgets" />
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />
  <link rel="stylesheet" href="../bower_components/bootstrap/dist/css/bootstrap.css" type="text/css" />
  <link rel="stylesheet" href="../bower_components/animate.css/animate.css" type="text/css" />
  <link rel="stylesheet" href="../bower_components/font-awesome/css/font-awesome.min.css" type="text/css" />
  <link rel="stylesheet" href="../bower_components/simple-line-icons/css/simple-line-icons.css" type="text/css" />
  <link rel="stylesheet" href="../bower_components/redactor/redactor.css" />
  <link rel="stylesheet" href="css/font.css" type="text/css" />
  <link rel="stylesheet" href="css/app.css" type="text/css" />
  <link rel="stylesheet" href="css/override.css" type="text/css" />
  <script type="text/javascript">
  	var GLOBAL_URL_HASH = "${buildNumber}"
    var initData = <%=request.getSession().getAttribute("personData")%>;
  </script>
</head>
<body ng-controller="AppCtrl">
  <div class="app" id="app" ng-class="{'app-header-fixed':app.settings.headerFixed, 'app-aside-fixed':app.settings.asideFixed, 'app-aside-folded':app.settings.asideFolded, 'app-aside-dock':app.settings.asideDock, 'container':app.settings.container, 'bg-white': $state.current.name == 'app.search' || $state.current.name == 'app.post'}" ui-view></div>


  <!-- jQuery -->
  <script src="../bower_components/jquery/dist/jquery.min.js"></script>

  <!-- Angular -->
  <script src="../bower_components/angular/angular.js"></script>
  
  <script src="../bower_components/angular-animate/angular-animate.js"></script>
  <script src="../bower_components/angular-cookies/angular-cookies.js"></script>
  <script src="../bower_components/angular-resource/angular-resource.js"></script>
  <script src="../bower_components/angular-sanitize/angular-sanitize.js"></script>
  <script src="../bower_components/angular-touch/angular-touch.js"></script>

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

  <!-- App -->
  <script src="js/app.js"></script>
  <script src="js/config.js"></script>
  <script src="js/config.lazyload.js"></script>
  <script src="js/config.router.js"></script>
  <script src="js/main.js"></script>
  <script src="js/services/ui-load.js"></script>
  <script src="js/services/trix.js"></script>
  <script src="js/filters/filters.js"></script>
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
  <script src="js/directives/custom.js"></script>
  <script src="js/controllers/bootstrap.js"></script>
  <script src="js/util.js"></script>
  <!-- Lazy loading -->
</body>
</html>
