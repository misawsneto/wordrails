function safeApply(scope, fn) {
	var phase = null
	if(scope.$root && scope.$root.$$phase)
		phase = scope.$root.$$phase;

	if(phase == '$apply' || phase == '$digest')
		scope.$eval(fn);
	else
		scope.$apply(fn);
}


function shadeBlend (p,c0,c1) {
	var n=p<0?p*-1:p,u=Math.round,w=parseInt;
	if(c0.length>7){
		var f=c0.split(","),t=(c1?c1:p<0?"rgb(0,0,0)":"rgb(255,255,255)").split(","),R=w(f[0].slice(4)),G=w(f[1]),B=w(f[2]);
		return "rgb("+(u((w(t[0].slice(4))-R)*n)+R)+","+(u((w(t[1])-G)*n)+G)+","+(u((w(t[2])-B)*n)+B)+")"
	}else{
		var f=w(c0.slice(1),16),t=w((c1?c1:p<0?"#000000":"#FFFFFF").slice(1),16),R1=f>>16,G1=f>>8&0x00FF,B1=f&0x0000FF;
		return "#"+(0x1000000+(u(((t>>16)-R1)*n)+R1)*0x10000+(u(((t>>8&0x00FF)-G1)*n)+G1)*0x100+(u(((t&0x0000FF)-B1)*n)+B1)).toString(16).slice(1)
	}
}

function hexToRgb(hex) {
    var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
    return result ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16)
    } : null;
}

function textColorEval (hex, hover){
	var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
	var rgb = result ? {
		r: parseInt(result[1], 16),
		g: parseInt(result[2], 16),
		b: parseInt(result[3], 16)
	} : null;

	var colors = {} 
	colors.font = 0;
  // Counting the perceptive luminance - human eye favors green color... 
  var a = 1 - ( 0.299 * rgb.r + 0.587 * rgb.g + 0.114 * rgb.b)/255;
  if (a < 0.5)
  	return hover ? "rgba(0,0,0, 0.94)" : "rgba(0,0,0, 0.84)";
  else
  	return hover ? "rgba(255,255,255, 0.96)" : "rgba(255,255,255, 0.86)";
}

function textColorEval2 (hex){
	var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
	var rgb = result ? {
		r: parseInt(result[1], 16),
		g: parseInt(result[2], 16),
		b: parseInt(result[3], 16)
	} : null;

	var colors = {} 
	colors.font = 0;
  // Counting the perceptive luminance - human eye favors green color... 
  var a = 1 - ( 0.299 * rgb.r + 0.587 * rgb.g + 0.114 * rgb.b)/255;
  if (a < 0.5)
  	return true;
  else
  	return false;
}

function getCustomStyle(color, perspective, header) {
	var style = "" +
	".btn-custom {\n"+
	"  color: "+textColorEval(color)+" !important;\n"+
	"  background-color: "+ color +"\n;"+
	"  border-color: "+ color +";\n"+
	"}\n"+

	".btn-custom:hover,"+
	".btn-custom:focus,"+
	".btn-custom:active,"+
	".btn-custom.active,"+
	".open .dropdown-toggle.btn-custom {\n"+
	"  color: "+textColorEval(color)+"!important;\n"+
	"  background-color: " + shadeBlend(-0.105, color) + ";\n"+
	"  border-color: " + shadeBlend(-0.205, color) + ";\n"+
	"}\n"+

	".btn-custom:active,"+
	".btn-custom.active,"+
	".open .dropdown-toggle.btn-custom {\n"+
	"  background-image: none;\n"+
	"}\n"+

	".btn-custom.disabled,"+
	".btn-custom[disabled],"+
	"fieldset[disabled] .btn-custom,"+
	".btn-custom.disabled:hover,"+
	".btn-custom[disabled]:hover,"+
	"fieldset[disabled] .btn-custom:hover,"+
	".btn-custom.disabled:focus,"+
	".btn-custom[disabled]:focus,"+
	"fieldset[disabled] .btn-custom:focus,"+
	".btn-custom.disabled:active,"+
	".btn-custom[disabled]:active,"+
	"fieldset[disabled] .btn-custom:active,"+
	".btn-custom.disabled.active,"+
	".btn-custom[disabled].active,"+
	"fieldset[disabled] .btn-custom.active {\n"+
	"   background-color: "+ color +";\n"+
	"   border-color: "+ color +";\n"+
	"}\n"+

	".md-input:focus,"+
	".md-input.focus {\n"+
	  "border-color: "+ color +";\n"+
	"}\n"+

	".md-input:focus ~ label,"+
	".md-input.focus ~ label {\n"+
	  "color: "+ color +";\n"+
	"}\n"+

	/*"md-tabs.md-default-theme md-tabs-ink-bar {\n"+
	  "color: "+ color +";\n"+
	  "background: "+ color +";\n"+
	"}\n"+*/

	".nav-tabs li.custom-active a{"+
	"border-bottom-color:" +color+ "!important"+
	"}"+

	".redactor-editor a, .redactor-editor a:hover {color:"+ color +"}\n"+
	".custom-fab-button{background-color: " + color + "!important; color: " + textColorEval(color) + "!important}"+

	".bg-perspective md-tabs.md-default-theme md-ink-bar {\n"+
	  "color: "+ textColorEval(perspective) +";\n"+
	  "background: "+ (textColorEval2(perspective) ? "rgba(0,0,0,0.5)" : "rgba(255,255,255,0.5)") +";\n"+
	"}\n"+

	".bg-perspective md-tab-item {\n"+
	  "opacity: 0.7;\n"+
	"}\n"+

	".bg-perspective md-tab-item.md-active {\n"+
	  "opacity: 1!important;\n"+
	"}\n"+

	".bg-perspective{\n"+
		"background: "+perspective+";\n"+
	"}\n"+
	".bg-perspective .station-perspectives{\n"+
		"color: "+ textColorEval(perspective) +";\n"+
	"}\n"+

	(textColorEval2(header) ? "" +
	"\n"
	:
	// header is dark
	".bg-perspective .station-header .navbar-nav .divider-v{\n"+
	"border-right: 1px solid rgba(255,255,255,0.3);\n"+
	"}\n"+
	"\n") + 

	(textColorEval2(perspective) ? "" +
	// perspective is bright
	"#station-sidebar .tl-wrap{\n"+
	"border-color:rgba(0, 0, 0, 0.2)!important;\n"+
	"}"+
	"#station-sidebar .tl-wrap:before{\n"+
	"background:rgb("+hexToRgb(perspective).r+", " + hexToRgb(perspective).g + ", "+ hexToRgb(perspective).b +")!important;\n"+
	"}"+
	"#station-sidebar{\n" + 
	 "background:rgba(0, 0, 0, 0.04)!important;\n"+
	"}\n"+
	"#perspective-vertical md-tabs-content-wrapper, #station-sidebar md-tabs-content-wrapper{border-top: 1px solid rgba(0,0,0,0.1); margin-top:-1px;}\n"+
	".bg-perspective .station-perspectives .b-t, .bg-perspective .station-perspectives .b-r, .bg-perspective .station-perspectives .b-b, .bg-perspective .station-perspectives .b-l{\n"+
	  "border-color:rgba(0,0,0,0.1)\n"+
	"}\n"+
	"#station-sidebar .divider{"+
	"border-top: none;"+
	"border-color:rgba(0,0,0,0.1)\n"+
	"}"+
	"\n"
	:
	// perspective is dark
	"#station-sidebar .tl-wrap{\n"+
	"border-color:rgba(255, 255, 255, 0.6)!important;\n"+
	"}"+
	"#station-sidebar .popular-posts .popular-index{\n"+
	"color: rgba(255, 255, 255, 0.86)"+
	"}"+
	"#station-sidebar .tl-wrap:before{\n"+
	"background:rgb("+hexToRgb(perspective).r+", " + hexToRgb(perspective).g + ", "+ hexToRgb(perspective).b +")!important;\n"+
	"}"+
	"#station-sidebar{\n" + 
	 "background:rgba(255,255,255, 0.04)!important;"+
	 "box-shadow: -7px 0 9px -7px rgba(0,0,0,0.4);"+
	 "border-left: 0px;"+
	"}\n"+
	".station-perspectives .angular-ripple, .station-header .angular-ripple{background-color: rgba(255,255,255,0.1);}\n"+
	"#perspective-vertical md-tabs-content-wrapper, #station-sidebar md-tabs-content-wrapper{box-shadow: inset 0 5px 5px -5px rgba(0,0,0,0.4);}\n"+
	// "#station-sidebar .md-header{\n" + 
	// "box-shadow: 0 5px 5px -5px rgba(0,0,0,0.4);"+
	// "}\n"+
	// "#perspective-vertical .md-header {\n"+
	//  //"border-bottom: 1px solid rgba(255,255,255,0.5);\n"+
	//  "box-shadow: 0 5px 5px -5px rgba(0,0,0,0.4);"+
	// "}\n"+
	"#station-sidebar .md-tab-content > .b-t{\n"+
	"border-top: 0px"+
	"}"+
	".bg-perspective .station-perspectives .b-t, .bg-perspective .station-perspectives .b-r, .bg-perspective .station-perspectives .b-b, .bg-perspective .station-perspectives .b-l{\n"+
	 "border-color:rgba(255,255,255, 0.3)\n"+
	"}\n"+
	".bg-perspective .station-perspectives a{\n"+
	  "color: rgba(255,255,255, 0.96) ;\n"+
	"}\n"+
	".bg-perspective .station-perspectives a:hover,\n"+
	".bg-perspective .station-perspectives a:focus {\n"+
	  "color: rgba(255,255,255, 0.86) ;\n"+
	  "text-decoration: none;\n"+
	"}\n"+
	"\n"
	)+

	".bg-custom, .bg-perspective md-tab-item span, .bg-perspective md-tabs.md-default-theme .md-paginator md-icon {\n"+
	"  color: "+textColorEval(perspective)+" !important;\n"+
	"}\n"+

	".bg-custom-primary {\n"+
	"  background-color: "+color+" !important;\n"+
	"  color: "+textColorEval(color)+" !important;\n"+
	"}\n"+

	".bg-custom-primary-no-image{\n"+
		"background: rgba("+hexToRgb(color).r+", " + hexToRgb(color).g + ", "+ hexToRgb(color).b +", 0.75);\n"+
		"color: "+ textColorEval(color) +";\n"+
	"}\n"+

	".text-custom-primary {\n"+
	"  color: "+textColorEval(color)+" !important;\n"+
	"}\n"+

	".a-custom-primary {\n"+
	"  color: "+color+" !important;\n"+
	"}\n"+

	".cover-abstract-mask{\n"+
	"  background-color: rgba("+hexToRgb(color).r+", " + hexToRgb(color).g + ", "+ hexToRgb(color).b +", 0.7);\n"+
	"}\n"+

	".station-header {\n"+
	"  background-color: rgba("+hexToRgb(header).r+", " + hexToRgb(header).g + ", "+ hexToRgb(header).b +", 0.95);\n"+
	"  color: " + textColorEval(header) + ";\n"+
	"}\n"+

	".header-custom{\n"+
	"  background-color: rgba("+hexToRgb(shadeBlend(0.15,header)).r+", " + hexToRgb(shadeBlend(0.15,header)).g + ", "+ hexToRgb(shadeBlend(0.15,header)).b +", 1);\n"+
	"  color: " + textColorEval(header) + ";\n"+
	"}\n"+

	".station-header .nav > li > a{\n"+
	"  color: " + textColorEval(header) + ";\n"+
	"}\n"+

	".station-header .nav > li > a:focus,\n"+
	".station-header .nav > li > a:hover{\n"+
	"  color: " + (textColorEval(header, true)) + ";\n"+
	"}\n"+

	".redactor-toolbar li span:hover,"+
	".redactor-toolbar li a:hover {\n"+
	  "outline: none;\n"+
	  "background-color:" + color + ";\n"+
	  "border-color:" + color + ";\n"+
	  "color: " + (textColorEval(color, true)) + ";\n"+
	"}\n"+

	/*".station-header .nav li .dropdown-menu {\n"+
	" background-color:" + header + ";\n"+
	"}"+*/

	"#post-right-content .post-block a{\n"+
  	"  color: rgba("+hexToRgb(color).r+", " + hexToRgb(color).g + ", "+ hexToRgb(color).b +", 0.92);\n"+
	"}\n"+
	"#post-right-content .post-block a:hover {\n"+
	  " color: rgba("+hexToRgb(color).r+", " + hexToRgb(color).g + ", "+ hexToRgb(color).b +", 1);\n"+
	  "text-decoration: underline;"+
	"}\n"+

	"#loading-bar .bar {"+
	  "background: "+shadeBlend(0.15,color)+";"+
	"}"+

	"#loading-bar .peg {"+
	  "-moz-box-shadow: "+color+" 1px 0 6px 1px;"+
	  "-ms-box-shadow: "+color+" 1px 0 6px 1px;"+
	  "-webkit-box-shadow: "+color+" 1px 0 6px 1px;"+
	  "box-shadow: "+color+" 1px 0 6px 1px;"+
	  "-moz-border-radius: 100%;"+
	  "-webkit-border-radius: 100%;"+
	  "border-radius: 100%;"+
	"}"+

	"#loading-bar-spinner .spinner-icon {"+
	  "border-top-color:  "+color+";"+
	  "border-left-color: "+color+";"+
	"}"+

	"md-checkbox.md-default-theme.md-checked .md-icon{" +
		"background-color:" + color +
	"}"+

	".form-control:focus{\n" +
		"border-color:" + color +
	"}"+

	"md-checkbox.md-default-theme.md-checked .md-icon:after{" +
	"border-color:" + textColorEval(color) +
	"}"+

	"";
	return style
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

String.prototype.getYoutubeCode = function(){
	var url = this;
	var regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=|\&v=)([^#\&\?]*).*/;
	var match = url.match(regExp);
	if (match && match[2].length == 11) {
	  return match[2];
	} else {
	  return null;
	}
}

String.prototype.stripHtml = function(){
	return this.replace(/(<([^>]+)>)/ig,"");
}

/*!
 * classie - class helper functions
 * from bonzo https://github.com/ded/bonzo
 * 
 * classie.has( elem, 'my-class' ) -> true/false
 * classie.add( elem, 'my-new-class' )
 * classie.remove( elem, 'my-unwanted-class' )
 * classie.toggle( elem, 'my-class' )
 */

/*jshint browser: true, strict: true, undef: true */
/*global define: false */

/*( function( window ) {

'use strict';

// class helper functions from bonzo https://github.com/ded/bonzo

function classReg( className ) {
  return new RegExp("(^|\\s+)" + className + "(\\s+|$)");
}

// classList support for class management
// altho to be fair, the api sucks because it won't accept multiple classes at once
var hasClass, addClass, removeClass;

if ( 'classList' in document.documentElement ) {
  hasClass = function( elem, c ) {
    return elem.classList.contains( c );
  };
  addClass = function( elem, c ) {
    elem.classList.add( c );
  };
  removeClass = function( elem, c ) {
    elem.classList.remove( c );
  };
}
else {
  hasClass = function( elem, c ) {
    return classReg( c ).test( elem.className );
  };
  addClass = function( elem, c ) {
    if ( !hasClass( elem, c ) ) {
      elem.className = elem.className + ' ' + c;
    }
  };
  removeClass = function( elem, c ) {
    elem.className = elem.className.replace( classReg( c ), ' ' );
  };
}

function toggleClass( elem, c ) {
  var fn = hasClass( elem, c ) ? removeClass : addClass;
  fn( elem, c );
}

var classie = {
  // full names
  hasClass: hasClass,
  addClass: addClass,
  removeClass: removeClass,
  toggleClass: toggleClass,
  // short names
  has: hasClass,
  add: addClass,
  remove: removeClass,
  toggle: toggleClass
};

// transport
if ( typeof define === 'function' && define.amd ) {
  // AMD
  define( classie );
} else {
  // browser global
  window.classie = classie;
}

})( window );*/