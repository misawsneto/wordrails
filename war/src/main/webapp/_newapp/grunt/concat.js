module.exports = {
	webapp:{
    src:[
      'libs/jquery/jquery/dist/jquery.js',
      'libs/jquery/bootstrap/dist/js/bootstrap.js',

      'libs/angular/angular/angular.js',
      'libs/angular/angular-animate/angular-animate.js',
      'libs/angular/angular-aria/angular-aria.js',
      'libs/angular/angular-cookies/angular-cookies.js',
      'libs/angular/angular-messages/angular-messages.js',
      'libs/angular/angular-resource/angular-resource.js',
      'libs/angular/angular-sanitize/angular-sanitize.js',
      'libs/angular/angular-touch/angular-touch.js',
      'libs/angular/angular-material/angular-material.js',

      'libs/angular/angular-ui-router/release/angular-ui-router.js', 
      'libs/angular/ngstorage/ngStorage.js',
      'libs/angular/angular-ui-utils/ui-utils.js',

      'libs/angular/angular-bootstrap/ui-bootstrap-tpls.js',
      'libs/angular/oclazyload/dist/ocLazyLoad.js',

      'libs/angular/angular-translate/angular-translate.js',
      'libs/angular/angular-translate-loader-static-files/angular-translate-loader-static-files.js',
      'libs/angular/angular-translate-storage-cookie/angular-translate-storage-cookie.js',
      'libs/angular/angular-translate-storage-local/angular-translate-storage-local.js',
     
      'libs/angular/angular-loading-bar/build/loading-bar.js',
      
      'scripts/app.js',
      'scripts/*.js',
      'scripts/directives/*.js',
      'scripts/services/*.js',
      'scripts/filters/*.js'
    ],
    // dest:'angular/scripts/app.src.js'
    dest:'scripts/app.src.js'
  },
  html:{
    src:[
      'libs/jquery/jquery/dist/jquery.js',
      'libs/jquery/bootstrap/dist/js/bootstrap.js',
      'libs/jquery/waves/dist/waves.js',
      'html/scripts/*.js'
    ],
    dest:'html/scripts/app.src.js'
  }
}
