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
})

.filter('orderByAccent', function(){
   return function(items, field, reverse) {
    var filtered = [];
    angular.forEach(items, function(item) {
      filtered.push(item);
    });
    filtered.sort(function (a, b) {
      return ((b.name ===  'A200') || (b.name < a.name) ? 1 : -1) ;
    });
    if(reverse) filtered.reverse();
    return filtered;
  };
});