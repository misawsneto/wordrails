'use strict';

/* Filters */
// need load the moment.js to use this filter. 
angular.module('app')
  .filter('fromNow', function() {
    return function(date) {
      return moment(date).fromNow();
    }
  })

/**
 * Filters for modifying colors
 */
.filter('darken', function(){
    return function(color){
        return tinycolor( color ).darken( 5 );
    };
})

.filter('lighten', function(){
    return function(color){
        return tinycolor( color ).lighten( 5 );
    };
});
