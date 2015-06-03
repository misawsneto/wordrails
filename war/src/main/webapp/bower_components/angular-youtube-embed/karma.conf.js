module.exports = function(config) {
	config.set({
		// base path, that will be used to resolve files and exclude
		basePath: '.',

		frameworks: ['jasmine'],

		// list of files to exclude
		exclude: [],

		// possible values: 'dots', 'progress'
		reporters: ['progress'],

		// coffeescript unit test preprocessor
		preprocessors: {
		    '**/*.coffee': ['coffee']		// compile coffeescript
		},

		// web server port
		port: 9876,

		// cli runner port
		runnerPort: 9100,

		// enable / disable colors in the output (reporters and logs)
		colors: true,

		// level of logging
		// possible values: config.LOG_DISABLE || config.LOG_ERROR || config.LOG_WARN || config.LOG_INFO || config.LOG_DEBUG
		logLevel: config.LOG_INFO,

		// Start these browsers, currently available:
		// - Chrome
		// - ChromeCanary
		// - Firefox
		// - Opera
		// - Safari (only Mac)
		// - PhantomJS
		// - IE (only Windows)
		browsers: ['PhantomJS'],

		// Auto run tests on start (when browsers are captured) and exit
		singleRun: true,

		// watch file changes and run tests
		autoWatch: false,

		// report which specs are slower than 500ms
		// CLI --report-slower-than 500
		reportSlowerThan: 500,

		plugins: [
			'karma-jasmine',
			'karma-junit-reporter',
			'karma-commonjs',
			'karma-phantomjs-launcher',
			'karma-coffee-preprocessor'
		]
	});
};