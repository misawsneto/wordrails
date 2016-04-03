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
                'libs/jquery/bootstrap/dist/css/bootstrap.css',
                'styles/font.css',
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
