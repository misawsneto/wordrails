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
  },
  leafletjs:{
    src:[
    'libs/angular/leaflet/dist/leaflet.js',
    'libs/angular/leaflet-draw/dist/leaflet.draw.js',
    'libs/angular/angular-leaflet-directive/dist/angular-leaflet-directive.min.js',
    ],
    dest:'scripts/leafletjs.all.js'
  },
  leafletcss:{
    src:[
    'libs/angular/leaflet-draw/dist/leaflet.draw.css',
    'libs/angular/leaflet/dist/leaflet.css',
    ],
    dest:'styles/leafletcss.all.css'
  },
  froala:{
    src:[
    // froala js
    'libs/angular/froala-wysiwyg-editor/js/froala_editor.min.js',
    // froala plugins js
    'libs/angular/froala-wysiwyg-editor/js/plugins/align.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/char_counter.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/code_beautifier.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/code_view.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/colors.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/emoticons.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/entities.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/file.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/font_family.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/font_size.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/fullscreen.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/image.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/image_manager.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/inline_style.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/line_breaker.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/link.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/lists.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/paragraph_format.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/paragraph_style.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/quote.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/save.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/table.min.js',
    'libs/angular/froala-wysiwyg-editor/js/plugins/video.min.js',
    //'libs/angular/froala-image-caption/js/plugins/image_caption.js',
    // langueges
    'libs/angular/froala-wysiwyg-editor/js/languages/en_gb.js',
    'libs/angular/froala-wysiwyg-editor/js/languages/pt_br.js',
    ],
    // dest:'angular/scripts/app.src.js'
    dest:'scripts/froala.all.js'
  },
  froalacss:{
    src:[
      'libs/angular/froala-wysiwyg-editor/css/froala_editor.min.css',
      'libs/angular/froala-wysiwyg-editor/css/froala_style.min.css',
              // plugins styles
      'libs/angular/froala-wysiwyg-editor/css/plugins/char_counter.css',
      'libs/angular/froala-wysiwyg-editor/css/plugins/code_view.css',
      'libs/angular/froala-wysiwyg-editor/css/plugins/colors.css',
      'libs/angular/froala-wysiwyg-editor/css/plugins/emoticons.css',
      'libs/angular/froala-wysiwyg-editor/css/plugins/file.css',
      'libs/angular/froala-wysiwyg-editor/css/plugins/fullscreen.css',
      'libs/angular/froala-wysiwyg-editor/css/plugins/image_manager.css',
      'libs/angular/froala-wysiwyg-editor/css/plugins/image.css',
      'libs/angular/froala-wysiwyg-editor/css/plugins/line_breaker.css',
      'libs/angular/froala-wysiwyg-editor/css/plugins/table.css',
      'libs/angular/froala-wysiwyg-editor/css/plugins/video.css',
      //'libs/angular/froala-image-caption/css/image_caption.css'
      ],
    dest:'styles/froala.all.css'  
  },
  masonry: {
    src:[
      'libs/angular/jquery-bridget/jquery-bridget.js',
      'libs/angular/ev-emitter/ev-emitter.js',
      'libs/angular/desandro-matches-selector/matches-selector.js',
      'libs/angular/fizzy-ui-utils/utils.js',
      'libs/angular/get-size/get-size.js',
      'libs/angular/outlayer/item.js',
      'libs/angular/outlayer/outlayer.js',
      'libs/angular/masonry/masonry.js',
      'libs/angular/imagesloaded/imagesloaded.js',
      'libs/angular/angular-masonry/angular-masonry.js'
    ],
    dest:'scripts/masonry.all.js'
  },
  videogular:{
    src:[
      'libs/angular/videogular/videogular.min.js',
      'libs/angular/videogular-buffering/vg-buffering.min.js',
      'libs/angular/videogular-controls/vg-controls.min.js',
      'libs/angular/videogular-ima-ads/vg-ima-ads.min.js',
      'libs/angular/videogular-overlay-play/vg-overlay-play.min.js',
      'libs/angular/videogular-poster/vg-poster.min.js'
    ],
    dest:'scripts/videogular.all.js'
  }
}
