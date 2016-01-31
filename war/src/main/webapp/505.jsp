<%@ page isErrorPage="true" import="java.io.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="utf-8" />
  <title>Materil | Responsive Web Admin App with AngularJS And Bootstrap</title>
  <meta name="description" content="app, web app, responsive, responsive layout, admin, admin panel, admin dashboard, flat, flat ui, ui kit, AngularJS, ui route, charts, widgets, components" />
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1" />

  <link rel="stylesheet" href="../libs/assets/animate.css/animate.css" type="text/css" />
  <link rel="stylesheet" href="../libs/assets/font-awesome/css/font-awesome.css" type="text/css" />
  <link rel="stylesheet" href="../libs/jquery/bootstrap/dist/css/bootstrap.css" type="text/css" />
  <link rel="stylesheet" href="../libs/jquery/waves/dist/waves.css" type="text/css" />

  <link rel="stylesheet" href="styles/material-design-icons.css" type="text/css" />
  <link rel="stylesheet" href="styles/font.css" type="text/css" />
  <link rel="stylesheet" href="styles/app.css" type="text/css" />

</head>
<body>
<div class="app">
  

<div class="deep-purple-700 bg-big">
  <div class="text-center">
    <h1 class="text-shadow no-margin text-white text-4x p-v-lg">
      <span class="text-2x font-bold m-t-lg block">505</span>
    </h1>
    <h2 class="h1 m-v-lg">OUCH!</h2>
    <p class="h4 m-v-lg text-u-c font-bold">Don't worry, we will fix it soon.</p>
    <div class="p-v-lg">
      Thanks!
    </div>
  </div>
</div>



</div>

<div style="display: none">
    <%
        response.getWriter().println("<div style=\"display: none\">");
        exception.printStackTrace(response.getWriter());
        response.getWriter().println("</div>");
    %>
</div>

<script src="../libs/jquery/jquery/dist/jquery.js"></script>
<script src="../libs/jquery/bootstrap/dist/js/bootstrap.js"></script>
<script src="../libs/jquery/waves/dist/waves.js"></script>

<script src="scripts/ui-load.js"></script>
<script src="scripts/ui-jp.config.js"></script>
<script src="scripts/ui-jp.js"></script>
<script src="scripts/ui-nav.js"></script>
<script src="scripts/ui-toggle.js"></script>
<script src="scripts/ui-waves.js"></script>

</body>
</html>