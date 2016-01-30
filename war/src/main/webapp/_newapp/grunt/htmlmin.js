module.exports = {
	min: {
      files: [{
          expand: true,
          cwd: 'app/views/',
          src: ['*.html', '**/*.html'],
          dest: 'angular/views/',
          ext: '.html',
          extDot: 'first'
      }]
  }
}