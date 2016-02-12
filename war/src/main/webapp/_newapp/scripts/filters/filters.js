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
})

.filter('generateRandom', function(){
  return function(length, chars){
    var mask = "";
    if (chars.indexOf("a") > -1) mask += "abcdefghijklmnopqrstuvwxyz";
    if (chars.indexOf("A") > -1) mask += "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    if (chars.indexOf("#") > -1) mask += "0123456789";
    if (chars.indexOf("!") > -1) mask += "~`!@#$%^&*()_+-={}[]:\";\'<>?,./|\\";
    if (chars.indexOf("u") > -1) mask += "~!@$^*()_+-=:\";\',.|"; //unsafe -> < > # % { } | \ ^ ~ [ ] `
    var result = "";
    var i = length
    for (; i > 0; --i) {
      var index = Math.round(Math.random() * (mask.length - 1));
      result += mask[index];
    }

    return result;
  };
});