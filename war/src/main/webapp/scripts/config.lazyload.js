// lazyload config

app
  .constant('MODULE_CONFIG', [
      {
          name: 'dndLists',
          files: [
            '/libs/angular/angular-drag-and-drop-lists/angular-drag-and-drop-lists.min.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'angular-carousel',
          files: [
              '/libs/angular/angular-carousel/dist/angular-carousel.min.css?' + GLOBAL_URL_HASH + '',
              '/libs/angular/angular-carousel/dist/angular-carousel.min.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'duScroll',
          module: true,
          files: [
              '/libs/angular/angular-scroll/angular-scroll.min.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'ui.select',
          module: true,
          files: [
              '/libs/angular/angular-ui-select/dist/select.min.js?' + GLOBAL_URL_HASH + '',
              '/libs/angular/angular-ui-select/dist/select.min.css?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'angular-sly',
          module: true,
          files: [
              '/libs/angular/sly/dis/sly.min.js?' + GLOBAL_URL_HASH + '',
              '/libs/angular/angular-sly/dis/angular-sly.min.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'digitalfondue.dftabmenu',
          module: true,
          files: [
            '/libs/angular/df-tab-menu/build/df-tab-menu.min.js?' + GLOBAL_URL_HASH + '',
            '/libs/angular/df-tab-menu/css/df-tab-menu.css?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: '720kb.socialshare',
          module: true,
          files: [
            '/libs/angular/angularjs-socialshare/dist/angular-socialshare.min.js?' + GLOBAL_URL_HASH + '',
          ]
      },
      {
          name: 'perfect_scrollbar',
          module: true,
          files: [
            '/libs/angular/perfect-scrollbar/js/perfect-scrollbar.min.js?' + GLOBAL_URL_HASH + '',
            '/libs/angular/perfect-scrollbar/js/perfect-scrollbar.jquery.min.js?' + GLOBAL_URL_HASH + '',
            '/libs/angular/perfect-scrollbar/css/perfect-scrollbar.min.css?' + GLOBAL_URL_HASH + '',
            '/libs/angular/angular-perfect-scrollbar/src/angular-perfect-scrollbar.js?' + GLOBAL_URL_HASH + '',
          ]
      },
      {
        name: 'wavesurfer.angular',
        module: true,
        files: [
            '/libs/angular/underscore/underscore-min.js?' + GLOBAL_URL_HASH + '',
            '/libs/angular/wavesurfer.js?' + GLOBAL_URL_HASH + '/dist/wavesurfer.min.js?' + GLOBAL_URL_HASH + '',
            '/libs/angular/wavesurfer-angular/dist/js/wavesurfer-angular.min.js?' + GLOBAL_URL_HASH + '',
            '/libs/angular/wavesurfer-angular/dist/css/wavesurfer.angular.min.css?' + GLOBAL_URL_HASH + ''
        ]
      },
      {
        name: 'angularMoment',
        module: true,
        files:[
          '/libs/angular/angular-moment/angular-moment.min.js?' + GLOBAL_URL_HASH + '',
        ]
      },
      {
        name: 'ui.materialize',
        module: true,
        files:[
          '/libs/angular/materialize/dist/js/materialize.min.js?' + GLOBAL_URL_HASH + '',
          '/libs/angular/angular-materialize/src/angular-materialize.js?' + GLOBAL_URL_HASH + ''
        ]
      },
      {
        name:'com.2fdevs.videogular',
        module: true,
        files: [
            '/scripts/videogular.all.js?' + GLOBAL_URL_HASH + '',
            '/libs/angular/videogular-themes-default/videogular.min.css?' + GLOBAL_URL_HASH + ''
        ]
      },
      {
        name:'com.2fdevs.videogular.plugins.controls',
        module: true,
        files: [
            '/scripts/videogular.all.js?' + GLOBAL_URL_HASH + ''
        ]
      },
      {
        name:'com.2fdevs.videogular.plugins.overlayplay',
        module: true,
        files: [
            '/scripts/videogular.all.js?' + GLOBAL_URL_HASH + ''
        ]
      },
      {
        name:'com.2fdevs.videogular.plugins.poster',
        module: true,
        files: [
            '/scripts/videogular.all.js?' + GLOBAL_URL_HASH + ''
        ]
      },
      {
          name: 'videosharing-embed',
          module: true,
          files: [
              '/libs/angular/ng-videosharing-embed/build/ng-videosharing-embed.min.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'angularAudioRecorder',
          module: true,
          files: [
              '/libs/angular/angularAudioRecorder/dist/angular-audio-recorder.min.js?' + GLOBAL_URL_HASH + '',
              '/libs/angular/wavesurfer.js?' + GLOBAL_URL_HASH + '/dist/wavesurfer.min.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
        name: 'recorderServiceProvider',
        module: true,
          files: [
              '/libs/angular/angularAudioRecorder/dist/angular-audio-recorder.min.js?' + GLOBAL_URL_HASH + '',
          ]
      },
      {
          name: 'afkl.lazyImage',
          module: true,
          files: [
              '/libs/angular/ng-directive-lazy-image/release/lazy-image.min.js?' + GLOBAL_URL_HASH + '',
              '/libs/angular/ng-directive-lazy-image/release/lazy-image-style.min.css?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'infinite-scroll',
          module: true,
          files: [
              '/libs/angular/ngInfiniteScroll/build/ng-infinite-scroll.min.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
        name: 'leaflet-directive',
        module: true,
          files: [
            '/libs/angular/angular-leaflet-directive/dist/angular-leaflet-directive.min.js?' + GLOBAL_URL_HASH + '',
            '/styles/leafletcss.all.css?' + GLOBAL_URL_HASH + '',
            '/scripts/leafletjs.all.js?' + GLOBAL_URL_HASH + '',
          ]
      },
      {
        name: 'ui.ace',
        module: true,
          files: [
            '/libs/angular/ace-builds/src-min-noconflict/ace.js?' + GLOBAL_URL_HASH + '',
            '/libs/angular/angular-ui-ace/ui-ace.min.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
        name: 'wu.masonry',
        module: true,
          files: [
            '/scripts/masonry.all.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
        name: 'ngJcrop',
        module: true,
          files: [
            '/libs/jquery/jcrop/css/jquery.Jcrop.min.css?' + GLOBAL_URL_HASH + '',
            '/libs/jquery/jcrop/js/jquery.Jcrop.min.js?' + GLOBAL_URL_HASH + '',
            '/libs/angular/ng-jcrop/ng-jcrop.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'mdDateTime',
          module: true,
          files: [
            '/libs/angular/md-date-time/dist/md-date-time.js?' + GLOBAL_URL_HASH + '',
            '/libs/angular/md-date-time/dist/md-date-time.css?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'monospaced.elastic',
          module: true,
          files: [
            '/libs/angular/angular-elastic/elastic.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'froala',
          module: true,
          files: [
            // froala style
            '/styles/froala.all.css?' + GLOBAL_URL_HASH + '',
            '/scripts/froala.all.js?' + GLOBAL_URL_HASH + '',
            '/libs/angular/angular-froala/src/angular-froala.js?' + GLOBAL_URL_HASH + '',
            '/libs/angular/angular-froala/src/froala-sanitize.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'ngMaterial.components',
          module: true,
          files: [
            '/libs/angular/angular-material-components/dist/angular-material-components.min.js?' + GLOBAL_URL_HASH + '',
            '/libs/angular/angular-material-components/dist/angular-material-components.min.css?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'ngMaterialDatePicker',
          module: true,
          files: [
            '/libs/angular/angular-material-datetimepicker/css/material-datetimepicker.min.css?' + GLOBAL_URL_HASH + '',
            '/libs/angular/angular-material-datetimepicker/js/angular-material-datetimepicker.min.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'mdPickers',
          files: [
              '/libs/angular/mdPickers/dist/mdPickers.min.css?' + GLOBAL_URL_HASH + '',
              '/libs/angular/mdPickers/dist/mdPickers.min.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'textAngular',
          module: true,
          files: [
              '/libs/angular/textAngular/dist/textAngular-sanitize.min.js?' + GLOBAL_URL_HASH + '',
              '/libs/angular/textAngular/dist/textAngular.min.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'vr.directives.slider',
          module: true,
          files: [
              '/libs/angular/venturocket-angular-slider/build/angular-slider.min.js?' + GLOBAL_URL_HASH + '',
              '/libs/angular/venturocket-angular-slider/angular-slider.css?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'angularBootstrapNavTree',
          module: true,
          files: [
              '/libs/angular/angular-bootstrap-nav-tree/dist/abn_tree_directive.js?' + GLOBAL_URL_HASH + '',
              '/libs/angular/angular-bootstrap-nav-tree/dist/abn_tree.css?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'angularFileUpload',
          module: true,
          files: [
              '/libs/angular/angular-file-upload/angular-file-upload.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'ngImgCrop',
          module: true,
          files: [
              '/libs/angular/ngImgCrop/compile/minified/ng-img-crop.js?' + GLOBAL_URL_HASH + '',
              '/libs/angular/ngImgCrop/compile/minified/ng-img-crop.css?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'smart-table',
          module: true,
          files: [
              '/libs/angular/angular-smart-table/dist/smart-table.min.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'ui.map',
          module: true,
          files: [
              '/libs/angular/angular-ui-map/ui-map.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'ngGrid',
          module: true,
          files: [
              '/libs/angular/ng-grid/build/ng-grid.min.js?' + GLOBAL_URL_HASH + '',
              '/libs/angular/ng-grid/ng-grid.min.css?' + GLOBAL_URL_HASH + '',
              '/libs/angular/ng-grid/ng-grid.bootstrap.css?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'ui.grid',
          module: true,
          files: [
              '/libs/angular/angular-ui-grid/ui-grid.min.js?' + GLOBAL_URL_HASH + '',
              '/libs/angular/angular-ui-grid/ui-grid.min.css?' + GLOBAL_URL_HASH + '',
              '/libs/angular/angular-ui-grid/ui-grid.bootstrap.css?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'xeditable',
          module: true,
          files: [
              '/libs/angular/angular-xeditable/dist/js/xeditable.min.js?' + GLOBAL_URL_HASH + '',
              '/libs/angular/angular-xeditable/dist/css/xeditable.css?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'smart-table',
          module: true,
          files: [
              '/libs/angular/angular-smart-table/dist/smart-table.min.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'pgwslider',
          module: false,
          files:[
              '/libs/jquery/pgwslider/pgwslider.min.css?' + GLOBAL_URL_HASH + '',
              '/libs/jquery/pgwslider/pgwslider.min.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'dataTable',
          module: false,
          files: [
              '/libs/jquery/datatables/media/js/jquery.dataTables.min.js?' + GLOBAL_URL_HASH + '',
              '/libs/jquery/plugins/integration/bootstrap/3/dataTables.bootstrap.js?' + GLOBAL_URL_HASH + '',
              '/libs/jquery/plugins/integration/bootstrap/3/dataTables.bootstrap.css?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'footable',
          module: false,
          files: [
              '/libs/jquery/footable/dist/footable.all.min.js?' + GLOBAL_URL_HASH + '',
              '/libs/jquery/footable/css/footable.core.css?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'easyPieChart',
          module: false,
          files: [
              '/libs/jquery/jquery.easy-pie-chart/dist/jquery.easypiechart.fill.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'sparkline',
          module: false,
          files: [
              '/libs/jquery/jquery.sparkline/dist/jquery.sparkline.retina.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'plot',
          module: false,
          files: [
              '/libs/jquery/flot/jquery.flot.js?' + GLOBAL_URL_HASH + '',
              '/libs/jquery/flot/jquery.flot.resize.js?' + GLOBAL_URL_HASH + '',
              '/libs/jquery/flot/jquery.flot.pie.js?' + GLOBAL_URL_HASH + '',
              '/libs/jquery/flot.tooltip/js/jquery.flot.tooltip.min.js?' + GLOBAL_URL_HASH + '',
              '/libs/jquery/flot-spline/js/jquery.flot.spline.min.js?' + GLOBAL_URL_HASH + '',
              '/libs/jquery/flot.orderbars/js/jquery.flot.orderBars.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'vectorMap',
          module: false,
          files: [
              '/libs/jquery/bower-jvectormap-2/jquery-jvectormap-2.0.0.min.js?' + GLOBAL_URL_HASH + '',
              '/libs/jquery/bower-jvectormap-2/jquery-jvectormap-2.0.0.css?' + GLOBAL_URL_HASH + '', 
              '/libs/jquery/bower-jvectormap-2/jquery-jvectormap-world-mill-en.js?' + GLOBAL_URL_HASH + '',
              '/libs/jquery/bower-jvectormap-2/jquery-jvectormap-us-aea-en.js?' + GLOBAL_URL_HASH + ''
          ]
      },
      {
          name: 'angularSpectrumColorpicker',
          module: true,
          files: [
              '/libs/theming/spectrum/spectrum.js?' + GLOBAL_URL_HASH + '',
              '/libs/theming/spectrum/spectrum.css?' + GLOBAL_URL_HASH + '',
              '/libs/theming/angular-spectrum-colorpicker/dist/angular-spectrum-colorpicker.min.js?' + GLOBAL_URL_HASH + '',
              '/libs/theming/tinycolor/tinycolor.js?' + GLOBAL_URL_HASH + '',
              '/libs/theming/angular-toArrayFilter/toArrayFilter.js?' + GLOBAL_URL_HASH + ''
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
