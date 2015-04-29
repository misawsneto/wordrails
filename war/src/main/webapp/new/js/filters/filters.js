'use strict';

/* Filters */
// need load the moment.js to use this filter. 
angular.module('app')
  .filter('fromNow', function() {
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

.filter('pvimageLink', function pvimageLink(WORDRAILS) {
	return function(postView, size) {
		if(postView && postView.largeId && size == "lg"){
	  		return {"background-image": "url(" + WORDRAILS.baseUrl + "/api/files/"+ postView.largeId +"/contents)"};
		}else if(postView && postView.mediumId && size == "md"){
	  		return {"background-image": "url(" + WORDRAILS.baseUrl + "/api/files/"+ postView.smallId +"/contents)"};
	  	}else if(postView && postView.mediumId && size == "md"){
	  		return {"background-image": "url(" + WORDRAILS.baseUrl + "/api/files/"+ postView.smallId +"/contents)"};
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