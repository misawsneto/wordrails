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
        name: 'wavesurfer.angular',
        module: true,
        files: [
            '/libs/angular/underscore/underscore-min.js',
            '/libs/angular/wavesurfer.js/dist/wavesurfer.min.js',
            '/libs/angular/wavesurfer-angular/dist/js/wavesurfer-angular.min.js',
            '/libs/angular/wavesurfer-angular/dist/css/wavesurfer.angular.min.css'
        ]
      },
      {
        name: 'angularMoment',
        module: true,
        files:[
          '/libs/angular/angular-moment/angular-moment.min.js',
        ]
      },
      {
        name: 'ui.materialize',
        module: true,
        files:[
          '/libs/angular/materialize/dist/js/materialize.min.js',
          '/libs/angular/angular-materialize/src/angular-materialize.js'
        ]
      },
      {
        name:'com.2fdevs.videogular',
        module: true,
        files: [
            '/scripts/videogular.all.js',
            '/libs/angular/videogular-themes-default/videogular.min.css'
        ]
      },
      {
        name:'com.2fdevs.videogular.plugins.controls',
        module: true,
        files: [
            '/scripts/videogular.all.js'
        ]
      },
      {
        name:'com.2fdevs.videogular.plugins.overlayplay',
        module: true,
        files: [
            '/scripts/videogular.all.js'
        ]
      },
      {
        name:'com.2fdevs.videogular.plugins.poster',
        module: true,
        files: [
            '/scripts/videogular.all.js'
        ]
      },
      {
          name: 'videosharing-embed',
          module: true,
          files: [
              '/libs/angular/ng-videosharing-embed/build/ng-videosharing-embed.min.js'
          ]
      },
      {
          name: 'angularAudioRecorder',
          module: true,
          files: [
              '/libs/angular/angularAudioRecorder/dist/angular-audio-recorder.min.js',
              '/libs/angular/wavesurfer.js/dist/wavesurfer.min.js'
          ]
      },
      {
        name: 'recorderServiceProvider',
        module: true,
          files: [
              '/libs/angular/angularAudioRecorder/dist/angular-audio-recorder.min.js',
          ]
      },
      {
          name: 'afkl.lazyImage',
          module: true,
          files: [
              '/libs/angular/ng-directive-lazy-image/release/lazy-image.min.js',
              '/libs/angular/ng-directive-lazy-image/release/lazy-image-style.min.css'
          ]
      },
      {
          name: 'infinite-scroll',
          module: true,
          files: [
              '/libs/angular/ngInfiniteScroll/build/ng-infinite-scroll.min.js'
          ]
      },
      {
        name: 'leaflet-directive',
        module: true,
          files: [
            '/libs/angular/angular-leaflet-directive/dist/angular-leaflet-directive.min.js',
            '/styles/leafletcss.all.css',
            '/scripts/leafletjs.all.js',
          ]
      },
      {
        name: 'ui.ace',
        module: true,
          files: [
            '/libs/angular/ace-builds/src-min-noconflict/ace.js',
            '/libs/angular/angular-ui-ace/ui-ace.min.js'
          ]
      },
      {
        name: 'wu.masonry',
        module: true,
          files: [
            '/scripts/masonry.all.js'
          ]
      },
      {
        name: 'ngJcrop',
        module: true,
          files: [
            '/libs/jquery/jcrop/css/jquery.Jcrop.min.css',
            '/libs/jquery/jcrop/js/jquery.Jcrop.min.js',
            '/libs/angular/ng-jcrop/ng-jcrop.js'
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
          name: 'monospaced.elastic',
          module: true,
          files: [
            '/libs/angular/angular-elastic/elastic.js'
          ]
      },
      {
          name: 'froala',
          module: true,
          files: [
            // froala style
            '/libs/angular/froala-wysiwyg-editor/css/froala_editor.min.css',
            '/libs/angular/froala-wysiwyg-editor/css/froala_style.min.css',
            // plugins styles
            '/libs/angular/froala-wysiwyg-editor/css/plugins/char_counter.css',
            '/libs/angular/froala-wysiwyg-editor/css/plugins/code_view.css',
            '/libs/angular/froala-wysiwyg-editor/css/plugins/colors.css',
            '/libs/angular/froala-wysiwyg-editor/css/plugins/emoticons.css',
            '/libs/angular/froala-wysiwyg-editor/css/plugins/file.css',
            '/libs/angular/froala-wysiwyg-editor/css/plugins/fullscreen.css',
            '/libs/angular/froala-wysiwyg-editor/css/plugins/image_manager.css',
            '/libs/angular/froala-wysiwyg-editor/css/plugins/image.css',
            '/libs/angular/froala-wysiwyg-editor/css/plugins/line_breaker.css',
            '/libs/angular/froala-wysiwyg-editor/css/plugins/table.css',
            '/libs/angular/froala-wysiwyg-editor/css/plugins/video.css',
            '/scripts/froala.all.js',
            '/libs/angular/angular-froala/src/angular-froala.js',
            '/libs/angular/angular-froala/src/froala-sanitize.js'
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
              '/libs/angular/mdPickers/dist/mdPickers.min.css',
              '/libs/angular/mdPickers/dist/mdPickers.min.js'
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
