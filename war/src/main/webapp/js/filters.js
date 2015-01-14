'use strict';

/* Filters */
// need load the moment.js to use this filter. 
angular.module('app.filters', []).filter('fromNow', function() {
    return function(date) {
      return moment(date).fromNow();
    }
})

.filter('myDateTimeFormat', function myDateTimeFormat() {
	return function(text) {
	  return moment.utc(text, "YYYY-MM-DD HH:mm:ss").tz('America/Recife').format("DD/MM/YYYY HH:mm:ss");;
	}
})

.filter('myLongTimeFormat', function myLongTimeFormat() {
	return function(longTime) {
	  return moment(longTime).format("DD/MM/YYYY HH:mm:ss");
	}
})

.filter('unique', function () {

  return function (items, filterOn) {

    if (filterOn === false) {
      return items;
    }

    if ((filterOn || angular.isUndefined(filterOn)) && angular.isArray(items)) {
      var hashCheck = {}, newItems = [];

      var extractValueToCompare = function (item) {
        if (angular.isObject(item) && angular.isString(filterOn)) {
          return item[filterOn];
        } else {
          return item;
        }
      };

      angular.forEach(items, function (item) {
        var valueToCheck, isDuplicate = false;

        for (var i = 0; i < newItems.length; i++) {
          if (angular.equals(extractValueToCompare(newItems[i]), extractValueToCompare(item))) {
            isDuplicate = true;
            break;
          }
        }
        if (!isDuplicate) {
          newItems.push(item);
        }

      });
      items = newItems;
    }
    return items;
  };
})

.filter('pvimageLink', function pvimageLink(WORDRAILS) {
	return function(postView, size) {
		if(postView && postView.largeId && size == "lg"){
	  	return {"background-image": "url(" + WORDRAILS.baseUrl + "/api/files/"+ postView.largeId +"/contents)"};
		}else if(postView && postView.mediumId && size == "md"){
	  	return {"background-image": "url(" + WORDRAILS.baseUrl + "/api/files/"+ postView.mediumId +"/contents)"};
		}else{
			return {"background-image": "url(img/p0.jpg)"};
		}
	}
})

.filter('imageLink', function imageLink(authService, WORDRAILS) {
	return function(imgId) {
		if(imgId)
 			return WORDRAILS.baseUrl + "/api/files/"+ imgId +"/contents";

 		return "";
	}
})
 
.filter('bgimg', function bgimg(){
  return function(img){
    if(img){
      return "background-image: url(" + img + ")";
    }else{
      return "background-image: url(img/default_user_image.jpg)";
    }
  }
});