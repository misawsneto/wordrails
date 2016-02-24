module.exports = {
    libs:{
        files:[
            {
                src:  [
                    'angular/angular.js',
                    'angular-animate/angular-animate.js',
                    'angular-aria/angular-aria.js',
                    'angular-cookies/angular-cookies.js',
                    'angular-messages/angular-messages.js',
                    'angular-resource/angular-resource.js',
                    'angular-sanitize/angular-sanitize.js',
                    'angular-touch/angular-touch.js',
                    'angular-bootstrap/ui-bootstrap-tpls.js',
                    'angular-bootstrap-nav-tree/dist/**',
                    'angular-file-upload/angular-file-upload.js',
                    'angular-loading-bar/build/**',
                    'angular-material/angular-material.js',
                    'angular-material/angular-material.css',
                    'angular-smart-table/dist/**',
                    'angular-translate/angular-translate.js',
                    'angular-translate-loader-static-files/angular-translate-loader-static-files.js',
                    'angular-translate-storage-cookie/angular-translate-storage-cookie.js',
                    'angular-translate-storage-local/angular-translate-storage-local.js',
                    'angular-ui-grid/ui-grid.*',
                    'angular-ui-map/ui-map.js',
                    'angular-ui-router/release/**',
                    'angular-ui-select/dist/**',
                    'angular-ui-utils/ui-utils.js',
                    'angular-xeditable/dist/**',
                    'jquery.easy-pie-chart/dist/angular.easypiechart.js',
                    'ng-grid/build/**',
                    'ng-grid/ng-grid.min.css',
                    'ngImgCrop/compile/minified/**',
                    'ngstorage/ngStorage.js',
                    'oclazyload/dist/**',
                    'textAngular/dist/**',
                    'venturocket-angular-slider/build/**'
                ],
                dest: 'libs/angular',
                cwd:  'bower_components',
                expand: true
            },
            {
                src:  [
                    'jquery/dist/jquery.js',
                    'bootstrap/dist/**',
                    'datatables/media/js/jquery.dataTables.min.js',
                    'plugins/integration/bootstrap/3/**',
                    'plugins/integration/bootstrap/images/**',
                    'footable/dist/footable.all.min.js',
                    'footable/css/footable.core.css',
                    'footable/css/fonts/**',
                    'bower-jvectormap/*.js',
                    'flot/jquery.flot.js',
                    'flot/jquery.flot.resize.js',
                    'flot/jquery.flot.pie.js',
                    'flot.tooltip/js/jquery.flot.tooltip.min.js',
                    'flot-spline/js/jquery.flot.spline.min.js',
                    'flot.orderbars/js/jquery.flot.orderBars.js',
                    'moment/moment.js',
                    'moment/min/moment-with-locales.min.js',
                    'waves/dist/**',
                    'screenfull/**',
                    'jquery.easy-pie-chart/**',
                    'jquery.sparkline/**',
                    'bower-jvectormap-2/**',
                    'slimScroll/jquery.slimscroll.min.js'
                ],
                dest: 'libs/jquery',
                cwd:  'bower_components',
                expand: true
            },
            {
                src: [
                    'spectrum/spectrum.css',
                    'spectrum/spectrum.js',
                    'angular-spectrum-colorpicker/dist/angular-spectrum-colorpicker.min.js',
                    'tinycolor/tinycolor.js',
                    'angular-toArrayFilter/toArrayFilter.js'
                ],
                dest: 'libs/theming',
                cwd:  'bower_components',
                expand: true
            },
            {
                src:  [
                    'animate.css/animate.css',
                    'font-awesome/css/**',
                    'font-awesome/fonts/**'
                ],
                dest: 'libs/assets',
                cwd:  'bower_components',
                expand: true
            },
            {src: '**', cwd: 'bower_components/bootstrap/dist/fonts', dest: 'fonts', expand: true}
        ]
    },
    // webapp: {
    //     files: [
    //         {expand: true, src: '**', cwd: 'api',     dest: 'angular/api'},
    //         {expand: true, src: '**', cwd: 'apps',    dest: 'angular/apps'},
    //         {expand: true, src: '**', cwd: 'fonts',   dest: 'angular/fonts'},
    //         {expand: true, src: '**', cwd: 'i18n',    dest: 'angular/i18n'},
    //         {expand: true, src: '**', cwd: 'images',  dest: 'angular/images'},
    //         {expand: true, src: '**', cwd: 'scripts', dest: 'angular/scripts'},
    //         {expand: true, src: '**', cwd: 'styles',  dest: 'angular/styles'},
    //         {expand: true, src: '**', cwd: 'views',   dest: 'angular/views'},
    //         {src: 'index.min.html', dest: 'angular/index.html'}
    //     ]
    // },
    html: {
        files: [
            {expand: true, src: '**', cwd: 'api',       dest: 'html/api'},
            {expand: true, src: '**', cwd: 'fonts/',    dest: 'html/fonts/'},
            {expand: true, src: '**', cwd: 'images/',   dest: 'html/images/'},
            {expand: true, src: '**', cwd: 'styles/',   dest: 'html/styles/'},
            {expand: true, src: '**', cwd: 'swig/scripts/', dest: 'html/scripts/'}
        ]
    }
};
