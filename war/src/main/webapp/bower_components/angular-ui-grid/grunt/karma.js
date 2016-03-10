var util = require('../lib/grunt/utils.js');

module.exports = function( grunt ){
  var baseConfig = {
    options: {
      configFile: 'test/karma.conf.js',
      background: true,
    },
    // dev: {
    //   singleRun: false,
    //   background: true
    // },
    single: {
      background: false,
      singleRun: true,
      reporters: ['progress'],
      reportSlowerThan: 200
    },

    travis: {
      background: false,
      singleRun: true,
      reporters: ['dots'],
    }
  };

  var core = grunt.option('core');

  if (core) {
    baseConfig.options.files = util.testDependencies.unit
    .concat(util.angularFiles(util.latestAngular()))
    .concat(util.testFiles.core_unit);
  }
  else {
    baseConfig.options.files = util.testDependencies.unit
    .concat(util.angularFiles(util.latestAngular()))
    .concat(util.testFiles.unit);
  }

  return baseConfig;
};
