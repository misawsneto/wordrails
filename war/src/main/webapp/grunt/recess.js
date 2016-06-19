module.exports = {
	less: {
        files: [
            {'styles/app.css': ['less/app.less']},
            {'styles/material-design-icons.css': ['less/md.icon.less']}
        ],
        options: {
          compile: true
        }
    },
    webapp: {
        files: {
            'styles/app.min.css': [
            // 'angular/styles/app.min.css': [
                // 'libs/jquery/bootstrap/dist/css/bootstrap.css',
                // 'styles/font.css',
                // 'styles/app.css'

              'libs/assets/animate.css/animate.css',
              'libs/assets/font-awesome/css/font-awesome.css',

              'libs/angular/angular-loading-bar/build/loading-bar.css',

              'libs/jquery/bootstrap/dist/css/bootstrap.css',
              
              'styles/material-design-icons.css',
              'styles/font.css',
              'styles/materialize.css',
              'styles/home.css',
              'styles/app.css'

            ]
        },
        options: {
            compress: true
        }
    },
    html: {
        files: {
            'html/styles/app.min.css': [
                'libs/jquery/bootstrap/dist/css/bootstrap.css',
                'styles/font.css',
                'styles/app.css'
            ]
        },
        options: {
            compress: true
        }
    }
}
