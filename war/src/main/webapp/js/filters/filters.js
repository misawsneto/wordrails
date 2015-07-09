'use strict';

/* Filters */
// need load the moment.js to use this filter. 
angular.module('app')
.filter('fromNow', function() {
	return function(date) {
		return moment(date).fromNow();
	}
})

.filter('fromNow2', function() {
	return function(date) {
		return moment(date).format('DD [de] MMMM [de] YYYY | HH:mm');
	}
})

.filter('fromNow3', function() {
	return function(date) {
		return moment(date).format('DD/MM/YY - HH:mm');
	}
})

.filter('myDateTimeFormat', function myDateTimeFormat() {
	return function(text) {
		return moment.utc(text, "YYYY-MM-DD HH:mm:ss").tz('America/Recife').format("DD/MM/YYYY HH:mm:ss");;
	}
})

.filter('stripHtml', function myDateTimeFormat() {
	return function(text) {
		return text.stripHtml();
	}
})

.filter('myLongTimeFormat', function myLongTimeFormat() {
	return function(longTime) {
		return moment(longTime).format("DD/MM/YYYY HH:mm:ss");
	}
})

.filter('pvimageLink', function pvimageLink(TRIX) {
	return function(postView, size) {
		if(postView && postView.largeId && size == "lg"){
			return {"background-image": "url(" + TRIX.baseUrl + "/api/files/"+ postView.largeId +"/contents)", "background-position": "50% 20%"};
		}else if(postView && postView.mediumId && size == "md"){
			return {"background-image": "url(" + TRIX.baseUrl + "/api/files/"+ postView.mediumId +"/contents)", "background-position": "50% 20%"};
		}else if(postView && postView.smallId && size == "sm"){
			return {"background-image": "url(" + TRIX.baseUrl + "/api/files/"+ postView.smallId +"/contents)", "background-position": "50% 20%"};
		}else{
			//return {"background-image": "url(img/p0.jpg)"};
			return {};
		}
	}
})

.filter('pvimageLink2', function pvimageLink(TRIX) {
	return function(post, size) {
		if(post && post.imageLargeId && size == "lg"){
			return {"background-image": "url(" + TRIX.baseUrl + "/api/files/"+ post.imageLargeId +"/contents)", "background-position": "50% 20%"};
		}else if(post && post.imageMediumId && size == "md"){
			return {"background-image": "url(" + TRIX.baseUrl + "/api/files/"+ post.imageMediumId +"/contents)", "background-position": "50% 20%"};
		}else if(post && post.imageSmallId && size == "sm"){
			return {"background-image": "url(" + TRIX.baseUrl + "/api/files/"+ post.imageSmallId +"/contents)", "background-position": "50% 20%"};
		}else{
			//return {"background-image": "url(img/p0.jpg)"};
			return {};
		}
	}
})

.filter('imageLink', function imageLink(TRIX) {
	return function(imgId) {
		if(imgId)
			return TRIX.baseUrl + "/api/files/"+ imgId +"/contents";

		return "";
	}
})

.filter('default_user_image', function imageLink(TRIX) {
	return function(imgId) {
		if(imgId)
			return TRIX.baseUrl + "/api/files/"+ imgId +"/contents";

		return "";
	}
})

.filter('bgimg2', function bgimg(){
	return function(img){
		if(img){
			return "background-image: url(" + img + ")";
		}else{
			return "";
		}
	}
})

.filter('bgimg', function bgimg(){
	return function(img){
		if(img){
			return "background-image: url(" + img + ")";
		}else{
			return "background-image: url(img/default-user.png)";
		}
	}
})

.filter('characters', function () {
	return function (input, chars, breakOnWord) {
		if (isNaN(chars)) return input;
		if (chars <= 0) return '';
		if (input && input.length > chars) {
			input = input.substring(0, chars);

			if (!breakOnWord) {
				var lastspace = input.lastIndexOf(' ');
                    //get last space
                    if (lastspace !== -1) {
                    	input = input.substr(0, lastspace);
                    }
                }else{
                	while(input.charAt(input.length-1) === ' '){
                		input = input.substr(0, input.length -1);
                	}
                }
                return input + '…';
            }
            return input;
        };
    })
.filter('splitcharacters', function() {
	return function (input, chars) {
		if (isNaN(chars)) return input;
		if (chars <= 0) return '';
		if (input && input.length > chars) {
			var prefix = input.substring(0, chars/2);
			var postfix = input.substring(input.length-chars/2, input.length);
			return prefix + '...' + postfix;
		}
		return input;
	};
})
.filter('words', function () {
	return function (input, words) {
		if (isNaN(words)) return input;
		if (words <= 0) return '';
		if (input) {
			var inputWords = input.split(/\s+/);
			if (inputWords.length > words) {
				input = inputWords.slice(0, words).join(' ') + '…';
			}
		}
		return input;
	};
});