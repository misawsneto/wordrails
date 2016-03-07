// lazyload config

angular.module('app')
  .constant('MODULE_CONFIG', [
      {
          name: 'ui.select',
          module: true,
          files: [
              '/libs/angular/angular-ui-select/dist/select.min.js',
              '/libs/angular/angular-ui-select/dist/select.min.css'
          ]
      },
      {
          name: 'mdDateTime',
          module: true,
          files: [
            '/libs/angular/md-date-time/dist/md-date-time.js',
            '/libs/angular/md-date-time/dist/md-date-time.css'
          ]
      },
      {
          name: 'ngMaterial.components',
          module: true,
          files: [
            '/libs/angular/angular-material-components/dist/angular-material-components.min.js',
            '/libs/angular/angular-material-components/dist/angular-material-components.min.css'
          ]
      },
      {
          name: 'ngMaterialDatePicker',
          module: true,
          files: [
            '/libs/angular/angular-material-datetimepicker/css/material-datetimepicker.min.css',
            '/libs/angular/angular-material-datetimepicker/js/angular-material-datetimepicker.min.js'
          ]
      },
      {
          name: 'mdPickers',
          files: [
              '/libs/angular/mdPickers/dist/mdPickers.css',
              '/libs/angular/mdPickers/dist/mdPickers.js'
          ]
      },
      {
          name: 'textAngular',
          module: true,
          files: [
              '/libs/angular/textAngular/dist/textAngular-sanitize.min.js',
              '/libs/angular/textAngular/dist/textAngular.min.js'
          ]
      },
      {
          name: 'vr.directives.slider',
          module: true,
          files: [
              '/libs/angular/venturocket-angular-slider/build/angular-slider.min.js',
              '/libs/angular/venturocket-angular-slider/angular-slider.css'
          ]
      },
      {
          name: 'angularBootstrapNavTree',
          module: true,
          files: [
              '/libs/angular/angular-bootstrap-nav-tree/dist/abn_tree_directive.js',
              '/libs/angular/angular-bootstrap-nav-tree/dist/abn_tree.css'
          ]
      },
      {
          name: 'angularFileUpload',
          module: true,
          files: [
              '/libs/angular/angular-file-upload/angular-file-upload.js'
          ]
      },
      {
          name: 'ngImgCrop',
          module: true,
          files: [
              '/libs/angular/ngImgCrop/compile/minified/ng-img-crop.js',
              '/libs/angular/ngImgCrop/compile/minified/ng-img-crop.css'
          ]
      },
      {
          name: 'smart-table',
          module: true,
          files: [
              '/libs/angular/angular-smart-table/dist/smart-table.min.js'
          ]
      },
      {
          name: 'ui.map',
          module: true,
          files: [
              '/libs/angular/angular-ui-map/ui-map.js'
          ]
      },
      {
          name: 'ngGrid',
          module: true,
          files: [
              '/libs/angular/ng-grid/build/ng-grid.min.js',
              '/libs/angular/ng-grid/ng-grid.min.css',
              '/libs/angular/ng-grid/ng-grid.bootstrap.css'
          ]
      },
      {
          name: 'ui.grid',
          module: true,
          files: [
              '/libs/angular/angular-ui-grid/ui-grid.min.js',
              '/libs/angular/angular-ui-grid/ui-grid.min.css',
              '/libs/angular/angular-ui-grid/ui-grid.bootstrap.css'
          ]
      },
      {
          name: 'xeditable',
          module: true,
          files: [
              '/libs/angular/angular-xeditable/dist/js/xeditable.min.js',
              '/libs/angular/angular-xeditable/dist/css/xeditable.css'
          ]
      },
      {
          name: 'smart-table',
          module: true,
          files: [
              '/libs/angular/angular-smart-table/dist/smart-table.min.js'
          ]
      },
      {
          name: 'dataTable',
          module: false,
          files: [
              '/libs/jquery/datatables/media/js/jquery.dataTables.min.js',
              '/libs/jquery/plugins/integration/bootstrap/3/dataTables.bootstrap.js',
              '/libs/jquery/plugins/integration/bootstrap/3/dataTables.bootstrap.css'
          ]
      },
      {
          name: 'footable',
          module: false,
          files: [
              '/libs/jquery/footable/dist/footable.all.min.js',
              '/libs/jquery/footable/css/footable.core.css'
          ]
      },
      {
          name: 'easyPieChart',
          module: false,
          files: [
              '/libs/jquery/jquery.easy-pie-chart/dist/jquery.easypiechart.fill.js'
          ]
      },
      {
          name: 'sparkline',
          module: false,
          files: [
              '/libs/jquery/jquery.sparkline/dist/jquery.sparkline.retina.js'
          ]
      },
      {
          name: 'plot',
          module: false,
          files: [
              '/libs/jquery/flot/jquery.flot.js',
              '/libs/jquery/flot/jquery.flot.resize.js',
              '/libs/jquery/flot/jquery.flot.pie.js',
              '/libs/jquery/flot.tooltip/js/jquery.flot.tooltip.min.js',
              '/libs/jquery/flot-spline/js/jquery.flot.spline.min.js',
              '/libs/jquery/flot.orderbars/js/jquery.flot.orderBars.js'
          ]
      },
      {
          name: 'vectorMap',
          module: false,
          files: [
              '/libs/jquery/bower-jvectormap-2/jquery-jvectormap-2.0.0.min.js',
              '/libs/jquery/bower-jvectormap-2/jquery-jvectormap-2.0.0.css', 
              '/libs/jquery/bower-jvectormap-2/jquery-jvectormap-world-mill-en.js',
              '/libs/jquery/bower-jvectormap-2/jquery-jvectormap-us-aea-en.js'
          ]
      },
      {
          name: 'angularSpectrumColorpicker',
          module: true,
          files: [
              '/libs/theming/spectrum/spectrum.js',
              '/libs/theming/spectrum/spectrum.css',
              '/libs/theming/angular-spectrum-colorpicker/dist/angular-spectrum-colorpicker.min.js',
              '/libs/theming/tinycolor/tinycolor.js',
              '/libs/theming/angular-toArrayFilter/toArrayFilter.js'
          ]
      }
    ]
  )
  .config(['$ocLazyLoadProvider', 'MODULE_CONFIG', function($ocLazyLoadProvider, MODULE_CONFIG) {
      $ocLazyLoadProvider.config({
          debug: false,
          events: false,
          modules: MODULE_CONFIG
      });
  }]);
