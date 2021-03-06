'use strict';

/* Filters */
// need load the moment.js to use this filter. 
app.filter('fromNow', [function() {
      return function(date) {
        return moment(date).fromNow();
      }
    }])

  .filter('fromNow', [function() {
    return function(date) {
      return moment(date).fromNow();
    }
  }])

.filter('fromNow2', [function() {
  return function(date) {
    return moment(date).format('DD [de] MMMM [de] YYYY | HH:mm');
  }
}])

.filter('fromNow3', [function() {
  return function(date) {
    return moment(date).format('DD/MM/YY - HH:mm');
  }
}])

.filter('permissions', [function() {
  return function(permissions, search) {
    if(!search || search == '')
      return permissions;

    return permissions.filter(function(permission, index, array) {
      if(permission && permission.person){
        if(permission.person.username.toLowerCase().indexOf(search.toLowerCase()) > -1 || permission.person.email.toLowerCase().indexOf(search.toLowerCase()) > -1 ||
          permission.person.name.toLowerCase().indexOf(search.toLowerCase()) > -1)
          return true;
        return false;
      }else{
        return false;
      }
    });
  }
}])

/**
 * Filters for modifying colors
 */
.filter('darken', [function(){
    return function(color){
        return tinycolor( color ).darken( 5 );
    };
}])

.filter('lighten', [function(){
    return function(color){
        return tinycolor( color ).lighten( 5 );
    };
}])

.filter('orderByBase', [function(){
   return function(items, field, reverse) {
    var filtered = [];
    angular.forEach(items, function(item) {
      filtered.push(item);
    });
    filtered.sort(function (a, b) {
      return ((b.name ===  '500') ? 1 : -1) ;
    });
    if(reverse) filtered.reverse();
    return filtered;
  };
}])

.filter('orderByAccent', [function(){
   return function(items, field, reverse) {
    var filtered = [];
    angular.forEach(items, function(item) {
      filtered.push(item);
    });
    filtered.sort(function (a, b) {
      return ((b.name ===  '300') || (b.name < a.name) ? 1 : -1) ;
    });
    if(reverse) filtered.reverse();
    return filtered;
  };
}])

.filter('generateRandom', [function(){
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
}])

.filter('myDateTimeFormat', [function myDateTimeFormat() {
  return function(text) {
    return moment.utc(text, "YYYY-MM-DD HH:mm:ss").tz('America/Recife').format("DD/MM/YYYY HH:mm:ss");;
  }
}])

.filter('stripHtml', [function myDateTimeFormat() {
  return function(text) {
    return text.stripHtml();
  }
}])

.filter('myLongTimeFormat', [function myLongTimeFormat() {
  return function(longTime) {
    return moment(longTime).format("DD/MM/YYYY HH:mm:ss");
  }
}])

.filter('videoThumb', [function videoThumb(){
  return function(url) {
    if(url && url.indexOf('www.youtube.com') > -1){
      var url = 'https://i.ytimg.com/vi/' + url.replace('https://www.youtube.com/watch?v=', '') +  '/hqdefault.jpg'  // '/sddefault_live.jpg';
      return {"background-image": "url(" + url +")", "background-position": "50% 20%"};
    }else {
      return {};
    }
  }
}])

.filter('bgImageLink', ['TRIX', function pvimageLink(TRIX) {
  return function(post, size) {
    if(post && (post.featuredImage || post.featuredImageHash || post.imageHash)){
      var hash = ((post.featuredImageHash) ? post.featuredImageHash : post.imageHash ? post.imageHash : post.featuredImage.originalHash)
      return {"background-image": "url(" + TRIX.baseUrl + "/api/images/get/"+ hash + "?size=" +size+ ")", "background-position": "50% 20%"};
    }
    else
      return {};
  }
}])

.filter('imageLink', ['TRIX', function imageLink(TRIX) {
  return function(post, size) {
    if(post && (post.featuredImage || post.featuredImageHash || post.imageHash)){
      var hash = ((post.featuredImageHash) ? post.featuredImageHash : post.imageHash ? post.imageHash : post.featuredImage.originalHash)
      return TRIX.baseUrl + "/api/images/get/"+ hash + "?size=" +size;
    }else
      return "";
  }
}])

.filter('getImagesPerson', ['TRIX', 'trix', function getImagesPerson(TRIX, trix) {
  return function(id, size, type, bg) {
    if(bg)
      return {'background-image': 'url( '+ trix.getImagesPerson(id, size, type) + ')'};
    return trix.getImagesPerson(id, size, type);
  }
}])

.filter('getPostsImage', ['TRIX', 'trix', function getPostsImage(TRIX, trix) {
  return function(id, size, bg) {
    if(bg)
      return {'background-image': 'url( '+ trix.getImagesPost(id, size, type) + ')'};
    return trix.getImagesPost(id, size);
  }
}])

.filter('fileLink', ['TRIX', function imageLink(TRIX) {
  return function(hash, size) {
    if(hash)
      return TRIX.baseUrl + "/api/files/get/"+ hash;

    return "";
  }
}])

.filter('coverImage', ['TRIX', function imageLink(TRIX) {
  return function(person, size) {
    if(person && (person.coverHash || person.coverOriginalHash)){
      if(!person.coverHash)
        person.coverHash = person.coverOriginalHash
      return TRIX.baseUrl + "/api/images/get/"+ (person.coverHash ? person.coverHash : person.coverOriginalHash)  + "?size=" + size;
    }else{
      if(person){
        return TRIX.baseUrl + "/images/"+getLastDigit(person.id)+".jpg"
      }
    }

    return "";
  }
}])

.filter('categoryImage', ['TRIX', function imageLink(TRIX) {
  return function(category, size, bg) {
    var hash = null;
    if(category)
      category.id = category.id ? category.id : category.termId;
    if(category && (category.imageHash))
      hash = category.imageHash;
    
    if(hash){
      if(bg)
        return {'background-image': 'url( '+ TRIX.baseUrl + "/api/images/get/"+ hash  + "?size=" + size + ')'};
      return TRIX.baseUrl + "/api/images/get/"+ hash  + "?size=" + size;
    }else{
      if(bg)
        return {'background-image': 'url( '+ TRIX.baseUrl + "/images/categories/1"+getLastDigit(category.id)+".jpg" + ')'};
      return TRIX.baseUrl + "/images/categories/1"+getLastDigit(category.id)+".jpg";
    }
    return null;
  }
}])

.filter('userImage', ['TRIX', function imageLink(TRIX) {
  return function(person, size) {
    if(person && (person.imageHash || person.imageOriginalHash)){
      if(!person.imageHash)
        person.imageHash = person.imageOriginalHash
      return TRIX.baseUrl + "/api/images/get/"+ (person.imageHash ? person.imageHash : person.imageOriginalHash)  + "?size=" + size;
    }

    return "";
  }
}])

.filter('default_user_image', ['TRIX', function imageLink(TRIX) {
  return function(imgId) {
    if(imgId)
      return TRIX.baseUrl + "/api/files/"+ imgId +"/contents";

    return "";
  }
}])

.filter('bgimg2', [function bgimg(){
  return function(img){
    if(img){
      return {"background-image": "url(" + img + ")"};
    }else{
      return "";
    }
  }
}])

.filter('bgimg', [function bgimg(){
  return function(img){
    if(img){
      return {"background-image": "url(" + img + ")"};
    }else{
      return {"background-image": "url(img/default-user.png)"};
    }
  }
}])

.filter('characters', [function () {
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
    }])
.filter('splitcharacters', [function() {
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
}])
.filter('words', [function () {
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
}])

;