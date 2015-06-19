<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.wordrails.business.Network" %>
<!DOCTYPE html>
<html lang="en" data-ng-app="app">
<head>
  <meta charset="utf-8" />
  <title><%=((Network)request.getSession().getAttribute("network")).getName()%></title>
  <meta name="description" content="app, web app, responsive, responsive layout, admin, admin panel, admin dashboard, flat, flat ui, ui kit, AngularJS, ui route, charts, widgets, components" />
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />

  <link href='http://fonts.googleapis.com/css?family=Oswald:400,700,300' rel='stylesheet' type='text/css'>
  <link href='http://fonts.googleapis.com/css?family=PT+Serif:400,700,400italic,700italic' rel='stylesheet' type='text/css'>

  <!-- sly -->
  <link rel="stylesheet" href="css/horizontal.css?${buildNumber}">
  <!-- medium editor -->
  <!-- <link rel="stylesheet" href="css/medium-editor.min.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="css/medium-editor-plugins.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="css/me-themes/bootstrap.min.css?${buildNumber}" type="text/css" /> -->
<!-- 
  <link rel="stylesheet" href="css/jq-ui.css?${buildNumber}" type="text/css" />
 -->
  <!-- froala -->
  <link href="css/froala_editor.min.css" rel="stylesheet" type="text/css" />
  <link href="css/splash.css" rel="stylesheet" type="text/css">

  <link rel="stylesheet" href="css/bootstrap.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="css/colorpicker.css?${buildNumber}">
  <link rel="stylesheet" href="css/ui-tree.min.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="css/animate.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="css/font-awesome.min.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="css/simple-line-icons.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="css/font.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="css/app.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" href="css/override.css?${buildNumber}" type="text/css" />
  <!-- toastr -->
  <link rel="stylesheet" href="css/toastr.min.css?${buildNumber}" type="text/css" />
  <link rel="stylesheet" type="text/css" href="offcanvas/css/demo.css" />
  <link rel="stylesheet" type="text/css" href="offcanvas/css/menu_sideslide.css" />

  <link href="css/froala_content.min.css" rel="stylesheet" type="text/css" />
  <link href="css/froala_style.min.css" rel="stylesheet" type="text/css" />
  <link href="js/libs/redactor/redactor.css" rel="stylesheet" type="text/css" />
  <link href='http://fonts.googleapis.com/css?family=Roboto:400,100,100italic,400italic,700,700italic' rel='stylesheet' type='text/css'>
  <link href='http://fonts.googleapis.com/css?family=Ubuntu:400,100,100italic,400italic,700,700italic' rel='stylesheet' type='text/css'>
  <link href='http://fonts.googleapis.com/css?family=Lato:400,300,300italic,400italic,700,700italic' rel='stylesheet' type='text/css'>
  <link href='http://fonts.googleapis.com/css?family=Open+Sans:400italic,400,300,700' rel='stylesheet' type='text/css'>
  <link href='http://fonts.googleapis.com/css?family=PT+Sans:400,700,400italic,700italic' rel='stylesheet' type='text/css'>
  <style>
    [ng\:cloak], [ng-cloak], [data-ng-cloak], [x-ng-cloak], .ng-cloak, .x-ng-cloak {
      display: none !important;
      /* font-family: {{!network.primaryFont ? "'Oswald', sans-serif" : network.primaryFont;}}!important */
    }
  </style>
  <script type="text/javascript">
    var GLOBAL_URL_HASH = "${buildNumber}"
    var NETWORK_ID = <%=((Network)request.getSession().getAttribute("network")).getId()%>;
  </script>
</head>
<body ng-controller="AppCtrl" ng-style="app.customStyle.primaryFont">
  <offcanvas></offcanvas>
  <div ng-click="contentClick()" class="app content-wrap" id="app" ng-class="{'app-header-fixed':app.settings.headerFixed, 'app-aside-fixed':app.settings.asideFixed} " ui-view></div>
  <!-- jQuery -->
  <script src="js/jquery/jquery.min.js?${buildNumber}"></script>
  <!-- summernote -->
  <script src="js/summernote/summernote.min.js?${buildNumber}"></script>
  <script src="js/summernote/summernote-pt-BR.js?${buildNumber}"></script>
  <!-- file upload -->
  <script src="js/jquery/jq-ui.js?${buildNumber}"></script>
  <script src="js/jquery/jquery.fileupload.js?${buildNumber}"></script>
  <!-- medium editor -->
  <!-- <script src="js/libs/medium-editor.min.js?${buildNumber}"></script>
  <script src="js/libs/medium-editor-plugins.js?${buildNumber}"></script> -->
  <!-- moment, livestamp, locales-->
  <!-- froala. -->
  <script src="js/libs/redactor/redactor.js"></script>
  <script src="js/libs/redactor/video.js"></script>
  <script src="js/libs/redactor/fontsize.js"></script>
  <script src="js/libs/redactor/counter.js"></script>
  <script src="js/libs/redactor/fontcolor.js"></script>
  <script src="js/libs/redactor/pt_br.js"></script>
  <script src="js/libs/froala/froala_editor.min.js"></script>
  <script src="js/libs/froala/plugins/font_size.min.js"></script>
  <script src="js/libs/froala/plugins/video.min.js"></script>
  <script src="js/libs/froala/plugins/lists.min.js"></script>
  <script src="js/libs/froala/plugins/tables.min.js"></script>
  <script src="js/libs/froala/plugins/colors.min.js"></script>
  <script src="js/libs/froala/langs/pt_br.js"></script>
  <!--[if lt IE 9]>
      <script src="../js/libs/froala/froala_editor_ie8.min.js"></script>
  <![endif]-->

  <script src="js/libs/moment.min.js?${buildNumber}"></script>
  <script src="js/libs/locales.min.js?${buildNumber}"></script>
  <script src="js/jquery/livestamp/livestamp.min.js?${buildNumber}"></script>
  <!-- Angular -->
  <script src="js/angular/angular.min.js?${buildNumber}"></script>
  <script src="js/angular/angular-locale_pt-br.js?${buildNumber}"></script>
  <script src="js/angular/angular-cookies.min.js?${buildNumber}"></script>
  <script src="js/angular/angular-animate.min.js?${buildNumber}"></script>
  <script src="js/angular/angular-ui-router.min.js?${buildNumber}"></script>
  <script src="js/angular/angular-translate.js?${buildNumber}"></script>
  <script src="js/angular/angular-infinite-scroll.min.js?${buildNumber}"></script>
  <script src="js/angular/angular-colorpicker.js?${buildNumber}"></script>
  <script src="js/angular/ngStorage.min.js?${buildNumber}"></script>
  <script src="js/angular/ui-load.js?${buildNumber}"></script>
  <script src="js/angular/ui-jq.js?${buildNumber}"></script>
  <script src="js/angular/ui-validate.js?${buildNumber}"></script>
  <script src="js/angular/ui-bootstrap-tpls.min.js?${buildNumber}"></script>
  <script src="js/angular/ui-tree.min.js?${buildNumber}"></script>
  <script src="js/angular/angular-sortable.js?${buildNumber}"></script>
  <!-- <script src="js/angular/jquery.fileupload-angular.js?${buildNumber}"></script> -->
  <script src="js/libs/angular/ocLazyLoad.js"></script>
  <script src="js/libs/splash.js?${buildNumber}"></script>
  <!-- <script src="js/angular/angular-medium-editor.min.js?${buildNumber}"></script> -->
  <!-- angular-summernote -->
  <script src="js/angular/angular-froala.js?${buildNumber}"></script>
  <script src="js/angular/froala-sanitize.js?${buildNumber}"></script>
  <script src="js/angular/angular-redactor.js?${buildNumber}"></script>
  <!-- Wordrails -->
  <script src="js/BaseWordRails.js?${buildNumber}"></script>
  <!-- App -->
  <script src="js/app.js?${buildNumber}"></script>
  <script src="js/services.js?${buildNumber}"></script>
  <script src="js/controllers.js?${buildNumber}"></script>
  <script src="js/filters.js?${buildNumber}"></script>
  <script src="js/directives.js?${buildNumber}"></script>
  <!-- Lazy loading -->

  <!-- controllers -->
  <script src="js/controllers_scaffold.js?${buildNumber}"></script>
  <script src="js/controllers_perspectives.js?${buildNumber}"></script>
  <!-- Sly -->
  <script src="js/sly/sly.plugins.js?${buildNumber}"></script>
  <script src="js/sly/sly.min.js?${buildNumber}"></script>
  <script src="js/sly/horizontal.js?${buildNumber}"></script>
  
  <!-- util -->
  <script src="js/toastr.min.js?${buildNumber}"></script>
  <script src="js/util.js?${buildNumber}"></script>
<!-- 
  <script src="js/jquery/custom-jq-ui.js?${buildNumber}"></script>
 -->

  <script src="offcanvas/js/classie.js"></script>
 
</body>
</html>