function safeApply(scope, fn) {
	var phase = null
	if(scope.$root && scope.$root.$$phase)
		phase = scope.$root.$$phase;

	if(phase == '$apply' || phase == '$digest')
		scope.$eval(fn);
	else
		scope.$apply(fn);
}

String.prototype.toSlug = function(){
	var str = this;
  str = str.replace(/^\s+|\s+$/g, ''); // trim
  str = str.toLowerCase();

  // remove accents, swap ñ for n, etc
  var from = "ãàáäâẽèéëêìíïîõòóöôùúüûñç·/_,:;";
  var to   = "aaaaaeeeeeiiiiooooouuuunc------";
  for (var i=0, l=from.length ; i<l ; i++) {
  	str = str.replace(new RegExp(from.charAt(i), 'g'), to.charAt(i));
  }

  str = str.replace(/[^a-z0-9 -]/g, '') // remove invalid chars
    .replace(/\s+/g, '-') // collapse whitespace and replace by -
    .replace(/-+/g, '-'); // collapse dashes

    return str;
}

String.prototype.toUsername = function(){
	var str = this;
  str = str.replace(/^\s+|\s+$/g, ''); // trim
  str = str.toLowerCase();

  // remove accents, swap ñ for n, etc
  var from = "ãàáäâẽèéëêìíïîõòóöôùúüûñç·/_,:;";
  var to   = "aaaaaeeeeeiiiiooooouuuunc------";
  for (var i=0, l=from.length ; i<l ; i++) {
  	str = str.replace(new RegExp(from.charAt(i), 'g'), to.charAt(i));
  }

  str = str.replace(/[^a-z0-9 -]/g, '') // remove invalid chars
    .replace(/\s+/g, '-') // collapse whitespace and replace by -
    .replace(/-+/g, '-'); // collapse dashes

  str = str.replace(/-/, '')
    return str;
}

function extractSelf(object){
	var self = null;
	if(object && object.links){
		object.links.forEach(function(elem){
			if(elem.rel && elem.rel == "self")
				self = elem.href
		})
	}

	return self;
}

function generateRel(href, rel){
	return{
		href: href,
		rel: rel
	}
};

function extractProperty(object, property){
	var self = null;
	if(object && object.links){
		object.links.forEach(function(elem){
			if(elem.rel && elem.rel == property)
				self = elem.href
		})
	}

	return self;
}

function findById(objects, id){
	var object = null
	if(objects){
		objects.forEach(function(elem){
			if(elem && elem.id && elem.id == id){
				object = elem;
			}
		});
	}

	return object;
}

function ordinaryRowCompare(rowA, rowB){
	if(rowA.index == rowB.index)
		return 0;
	if(rowA.index == null)
		return - 1;
	if(rowB.index == null)
		return - 1;
	if (rowA.index < rowB.index)
		return -1;
	if (rowA.index > rowB.index)
		return 1;
	return 0;
}

function getParameterByName(name) {
    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
        results = regex.exec(location.search);
    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
}