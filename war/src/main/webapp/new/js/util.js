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

function textColorEval (hex){
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
  	return "#444444";
  else
  	return "#ffffff";
}

function getCustomButtonStyle(color, perspective) {
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

	"md-tabs.md-default-theme md-tabs-ink-bar {\n"+
	  "color: "+ textColorEval(perspective) +";\n"+
	  "background: "+ textColorEval(perspective) +";\n"+
	"}\n"+

	"md-tabs.md-default-theme md-tab .md-ripple-container {"+
	  "color: "+shadeBlend(0.8, color)+";"+
	"}\n"+

	".bg-perspective{\n"+
		"background: "+perspective+";\n"+
	"}\n"+

	".bg-custom {\n"+
	"  color: "+textColorEval(perspective)+" !important;\n"+
	"}\n"+

	".bg-custom-primary {\n"+
	"  background-color: "+color+" !important;\n"+
	"  color: "+textColorEval(color)+" !important;\n"+
	"}\n"+

	+"";
	return style
}