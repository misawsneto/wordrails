!function(a,b){b["true"]=a,function(b,c){"function"==typeof define&&define.amd?define(b):"undefined"!=typeof module&&"object"==typeof a?module.exports=b():c.rangy=b()}(function(){function a(a,b){var c=typeof a[b];return c==u||!(c!=t||!a[b])||"unknown"==c}function b(a,b){return!(typeof a[b]!=t||!a[b])}function c(a,b){return typeof a[b]!=v}function d(a){return function(b,c){for(var d=c.length;d--;)if(!a(b,c[d]))return!1;return!0}}function e(a){return a&&A(a,z)&&C(a,y)}function f(a){return b(a,"body")?a.body:a.getElementsByTagName("body")[0]}function g(b){typeof console!=v&&a(console,"log")&&console.log(b)}function h(a,b){F&&b?alert(a):g(a)}function i(a){H.initialized=!0,H.supported=!1,h("Rangy is not supported in this environment. Reason: "+a,H.config.alertOnFail)}function j(a){h("Rangy warning: "+a,H.config.alertOnWarn)}function k(a){return a.message||a.description||String(a)}function l(){if(F&&!H.initialized){var b,c=!1,d=!1;a(document,"createRange")&&(b=document.createRange(),A(b,x)&&C(b,w)&&(c=!0));var h=f(document);if(!h||"body"!=h.nodeName.toLowerCase())return void i("No body element found");if(h&&a(h,"createTextRange")&&(b=h.createTextRange(),e(b)&&(d=!0)),!c&&!d)return void i("Neither Range nor TextRange are available");H.initialized=!0,H.features={implementsDomRange:c,implementsTextRange:d};var j,l;for(var m in E)(j=E[m])instanceof p&&j.init(j,H);for(var n=0,o=K.length;o>n;++n)try{K[n](H)}catch(q){l="Rangy init listener threw an exception. Continuing. Detail: "+k(q),g(l)}}}function m(a,b,c){c&&(a+=" in module "+c.name),H.warn("DEPRECATED: "+a+" is deprecated. Please use "+b+" instead.")}function n(a,b,c,d){a[b]=function(){return m(b,c,d),a[c].apply(a,G.toArray(arguments))}}function o(a){a=a||window,l();for(var b=0,c=L.length;c>b;++b)L[b](a)}function p(a,b,c){this.name=a,this.dependencies=b,this.initialized=!1,this.supported=!1,this.initializer=c}function q(a,b,c){var d=new p(a,b,function(b){if(!b.initialized){b.initialized=!0;try{c(H,b),b.supported=!0}catch(d){var e="Module '"+a+"' failed to load: "+k(d);g(e),d.stack&&g(d.stack)}}});return E[a]=d,d}function r(){}function s(){}var t="object",u="function",v="undefined",w=["startContainer","startOffset","endContainer","endOffset","collapsed","commonAncestorContainer"],x=["setStart","setStartBefore","setStartAfter","setEnd","setEndBefore","setEndAfter","collapse","selectNode","selectNodeContents","compareBoundaryPoints","deleteContents","extractContents","cloneContents","insertNode","surroundContents","cloneRange","toString","detach"],y=["boundingHeight","boundingLeft","boundingTop","boundingWidth","htmlText","text"],z=["collapse","compareEndPoints","duplicate","moveToElementText","parentElement","select","setEndPoint","getBoundingClientRect"],A=d(a),B=d(b),C=d(c),D=[].forEach?function(a,b){a.forEach(b)}:function(a,b){for(var c=0,d=a.length;d>c;++c)b(a[c],c)},E={},F=typeof window!=v&&typeof document!=v,G={isHostMethod:a,isHostObject:b,isHostProperty:c,areHostMethods:A,areHostObjects:B,areHostProperties:C,isTextRange:e,getBody:f,forEach:D},H={version:"1.3.0",initialized:!1,isBrowser:F,supported:!0,util:G,features:{},modules:E,config:{alertOnFail:!1,alertOnWarn:!1,preferTextRange:!1,autoInitialize:typeof rangyAutoInitialize==v?!0:rangyAutoInitialize}};H.fail=i,H.warn=j;var I;({}).hasOwnProperty?(G.extend=I=function(a,b,c){var d,e;for(var f in b)b.hasOwnProperty(f)&&(d=a[f],e=b[f],c&&null!==d&&"object"==typeof d&&null!==e&&"object"==typeof e&&I(d,e,!0),a[f]=e);return b.hasOwnProperty("toString")&&(a.toString=b.toString),a},G.createOptions=function(a,b){var c={};return I(c,b),a&&I(c,a),c}):i("hasOwnProperty not supported"),F||i("Rangy can only run in a browser"),function(){var a;if(F){var b=document.createElement("div");b.appendChild(document.createElement("span"));var c=[].slice;try{1==c.call(b.childNodes,0)[0].nodeType&&(a=function(a){return c.call(a,0)})}catch(d){}}a||(a=function(a){for(var b=[],c=0,d=a.length;d>c;++c)b[c]=a[c];return b}),G.toArray=a}();var J;F&&(a(document,"addEventListener")?J=function(a,b,c){a.addEventListener(b,c,!1)}:a(document,"attachEvent")?J=function(a,b,c){a.attachEvent("on"+b,c)}:i("Document does not have required addEventListener or attachEvent method"),G.addListener=J);var K=[];G.deprecationNotice=m,G.createAliasForDeprecatedMethod=n,H.init=l,H.addInitListener=function(a){H.initialized?a(H):K.push(a)};var L=[];H.addShimListener=function(a){L.push(a)},F&&(H.shim=H.createMissingNativeApi=o,n(H,"createMissingNativeApi","shim")),p.prototype={init:function(){for(var a,b,c=this.dependencies||[],d=0,e=c.length;e>d;++d){if(b=c[d],a=E[b],!(a&&a instanceof p))throw new Error("required module '"+b+"' not found");if(a.init(),!a.supported)throw new Error("required module '"+b+"' not supported")}this.initializer(this)},fail:function(a){throw this.initialized=!0,this.supported=!1,new Error(a)},warn:function(a){H.warn("Module "+this.name+": "+a)},deprecationNotice:function(a,b){H.warn("DEPRECATED: "+a+" in module "+this.name+" is deprecated. Please use "+b+" instead")},createError:function(a){return new Error("Error in Rangy "+this.name+" module: "+a)}},H.createModule=function(a){var b,c;2==arguments.length?(b=arguments[1],c=[]):(b=arguments[2],c=arguments[1]);var d=q(a,c,b);H.initialized&&H.supported&&d.init()},H.createCoreModule=function(a,b,c){q(a,b,c)},H.RangePrototype=r,H.rangePrototype=new r,H.selectionPrototype=new s,H.createCoreModule("DomUtil",[],function(a,b){function c(a){var b;return typeof a.namespaceURI==F||null===(b=a.namespaceURI)||"http://www.w3.org/1999/xhtml"==b}function d(a){var b=a.parentNode;return 1==b.nodeType?b:null}function e(a){for(var b=0;a=a.previousSibling;)++b;return b}function f(a){switch(a.nodeType){case 7:case 10:return 0;case 3:case 8:return a.length;default:return a.childNodes.length}}function g(a,b){var c,d=[];for(c=a;c;c=c.parentNode)d.push(c);for(c=b;c;c=c.parentNode)if(K(d,c))return c;return null}function h(a,b,c){for(var d=c?b:b.parentNode;d;){if(d===a)return!0;d=d.parentNode}return!1}function i(a,b){return h(a,b,!0)}function j(a,b,c){for(var d,e=c?a:a.parentNode;e;){if(d=e.parentNode,d===b)return e;e=d}return null}function k(a){var b=a.nodeType;return 3==b||4==b||8==b}function l(a){if(!a)return!1;var b=a.nodeType;return 3==b||8==b}function m(a,b){var c=b.nextSibling,d=b.parentNode;return c?d.insertBefore(a,c):d.appendChild(a),a}function n(a,b,c){var d=a.cloneNode(!1);if(d.deleteData(0,b),a.deleteData(b,a.length-b),m(d,a),c)for(var f,g=0;f=c[g++];)f.node==a&&f.offset>b?(f.node=d,f.offset-=b):f.node==a.parentNode&&f.offset>e(a)&&++f.offset;return d}function o(a){if(9==a.nodeType)return a;if(typeof a.ownerDocument!=F)return a.ownerDocument;if(typeof a.document!=F)return a.document;if(a.parentNode)return o(a.parentNode);throw b.createError("getDocument: no document found for node")}function p(a){var c=o(a);if(typeof c.defaultView!=F)return c.defaultView;if(typeof c.parentWindow!=F)return c.parentWindow;throw b.createError("Cannot get a window object for node")}function q(a){if(typeof a.contentDocument!=F)return a.contentDocument;if(typeof a.contentWindow!=F)return a.contentWindow.document;throw b.createError("getIframeDocument: No Document object found for iframe element")}function r(a){if(typeof a.contentWindow!=F)return a.contentWindow;if(typeof a.contentDocument!=F)return a.contentDocument.defaultView;throw b.createError("getIframeWindow: No Window object found for iframe element")}function s(a){return a&&G.isHostMethod(a,"setTimeout")&&G.isHostObject(a,"document")}function t(a,b,c){var d;if(a?G.isHostProperty(a,"nodeType")?d=1==a.nodeType&&"iframe"==a.tagName.toLowerCase()?q(a):o(a):s(a)&&(d=a.document):d=document,!d)throw b.createError(c+"(): Parameter must be a Window object or DOM node");return d}function u(a){for(var b;b=a.parentNode;)a=b;return a}function v(a,c,d,f){var h,i,k,l,m;if(a==d)return c===f?0:f>c?-1:1;if(h=j(d,a,!0))return c<=e(h)?-1:1;if(h=j(a,d,!0))return e(h)<f?-1:1;if(i=g(a,d),!i)throw new Error("comparePoints error: nodes have no common ancestor");if(k=a===i?i:j(a,i,!0),l=d===i?i:j(d,i,!0),k===l)throw b.createError("comparePoints got to case 4 and childA and childB are the same!");for(m=i.firstChild;m;){if(m===k)return-1;if(m===l)return 1;m=m.nextSibling}}function w(a){var b;try{return b=a.parentNode,!1}catch(c){return!0}}function x(a){if(!a)return"[No node]";if(L&&w(a))return"[Broken node]";if(k(a))return'"'+a.data+'"';if(1==a.nodeType){var b=a.id?' id="'+a.id+'"':"";return"<"+a.nodeName+b+">[index:"+e(a)+",length:"+a.childNodes.length+"]["+(a.innerHTML||"[innerHTML not supported]").slice(0,25)+"]"}return a.nodeName}function y(a){for(var b,c=o(a).createDocumentFragment();b=a.firstChild;)c.appendChild(b);return c}function z(a,b,c){var d=H(a),e=a.createElement("div");e.contentEditable=""+!!c,b&&(e.innerHTML=b);var f=d.firstChild;return f?d.insertBefore(e,f):d.appendChild(e),e}function A(a){return a.parentNode.removeChild(a)}function B(a){this.root=a,this._next=a}function C(a){return new B(a)}function D(a,b){this.node=a,this.offset=b}function E(a){this.code=this[a],this.codeName=a,this.message="DOMException: "+this.codeName}var F="undefined",G=a.util,H=G.getBody;G.areHostMethods(document,["createDocumentFragment","createElement","createTextNode"])||b.fail("document missing a Node creation method"),G.isHostMethod(document,"getElementsByTagName")||b.fail("document missing getElementsByTagName method");var I=document.createElement("div");G.areHostMethods(I,["insertBefore","appendChild","cloneNode"]||!G.areHostObjects(I,["previousSibling","nextSibling","childNodes","parentNode"]))||b.fail("Incomplete Element implementation"),G.isHostProperty(I,"innerHTML")||b.fail("Element is missing innerHTML property");var J=document.createTextNode("test");G.areHostMethods(J,["splitText","deleteData","insertData","appendData","cloneNode"]||!G.areHostObjects(I,["previousSibling","nextSibling","childNodes","parentNode"])||!G.areHostProperties(J,["data"]))||b.fail("Incomplete Text Node implementation");var K=function(a,b){for(var c=a.length;c--;)if(a[c]===b)return!0;return!1},L=!1;!function(){var b=document.createElement("b");b.innerHTML="1";var c=b.firstChild;b.innerHTML="<br />",L=w(c),a.features.crashyTextNodes=L}();var M;typeof window.getComputedStyle!=F?M=function(a,b){return p(a).getComputedStyle(a,null)[b]}:typeof document.documentElement.currentStyle!=F?M=function(a,b){return a.currentStyle?a.currentStyle[b]:""}:b.fail("No means of obtaining computed style properties found"),B.prototype={_current:null,hasNext:function(){return!!this._next},next:function(){var a,b,c=this._current=this._next;if(this._current)if(a=c.firstChild)this._next=a;else{for(b=null;c!==this.root&&!(b=c.nextSibling);)c=c.parentNode;this._next=b}return this._current},detach:function(){this._current=this._next=this.root=null}},D.prototype={equals:function(a){return!!a&&this.node===a.node&&this.offset==a.offset},inspect:function(){return"[DomPosition("+x(this.node)+":"+this.offset+")]"},toString:function(){return this.inspect()}},E.prototype={INDEX_SIZE_ERR:1,HIERARCHY_REQUEST_ERR:3,WRONG_DOCUMENT_ERR:4,NO_MODIFICATION_ALLOWED_ERR:7,NOT_FOUND_ERR:8,NOT_SUPPORTED_ERR:9,INVALID_STATE_ERR:11,INVALID_NODE_TYPE_ERR:24},E.prototype.toString=function(){return this.message},a.dom={arrayContains:K,isHtmlNamespace:c,parentElement:d,getNodeIndex:e,getNodeLength:f,getCommonAncestor:g,isAncestorOf:h,isOrIsAncestorOf:i,getClosestAncestorIn:j,isCharacterDataNode:k,isTextOrCommentNode:l,insertAfter:m,splitDataNode:n,getDocument:o,getWindow:p,getIframeWindow:r,getIframeDocument:q,getBody:H,isWindow:s,getContentDocument:t,getRootContainer:u,comparePoints:v,isBrokenNode:w,inspectNode:x,getComputedStyleProperty:M,createTestElement:z,removeNode:A,fragmentFromNodeChildren:y,createIterator:C,DomPosition:D},a.DOMException=E}),H.createCoreModule("DomRange",["DomUtil"],function(a,b){function c(a,b){return 3!=a.nodeType&&(P(a,b.startContainer)||P(a,b.endContainer))}function d(a){return a.document||Q(a.startContainer)}function e(a){return W(a.startContainer)}function f(a){return new L(a.parentNode,O(a))}function g(a){return new L(a.parentNode,O(a)+1)}function h(a,b,c){var d=11==a.nodeType?a.firstChild:a;return N(b)?c==b.length?J.insertAfter(a,b):b.parentNode.insertBefore(a,0==c?b:S(b,c)):c>=b.childNodes.length?b.appendChild(a):b.insertBefore(a,b.childNodes[c]),d}function i(a,b,c){if(z(a),z(b),d(b)!=d(a))throw new M("WRONG_DOCUMENT_ERR");var e=R(a.startContainer,a.startOffset,b.endContainer,b.endOffset),f=R(a.endContainer,a.endOffset,b.startContainer,b.startOffset);return c?0>=e&&f>=0:0>e&&f>0}function j(a){for(var b,c,e,f=d(a.range).createDocumentFragment();c=a.next();){if(b=a.isPartiallySelectedSubtree(),c=c.cloneNode(!b),b&&(e=a.getSubtreeIterator(),c.appendChild(j(e)),e.detach()),10==c.nodeType)throw new M("HIERARCHY_REQUEST_ERR");f.appendChild(c)}return f}function k(a,b,c){var d,e;c=c||{stop:!1};for(var f,g;f=a.next();)if(a.isPartiallySelectedSubtree()){if(b(f)===!1)return void(c.stop=!0);if(g=a.getSubtreeIterator(),k(g,b,c),g.detach(),c.stop)return}else for(d=J.createIterator(f);e=d.next();)if(b(e)===!1)return void(c.stop=!0)}function l(a){for(var b;a.next();)a.isPartiallySelectedSubtree()?(b=a.getSubtreeIterator(),l(b),b.detach()):a.remove()}function m(a){for(var b,c,e=d(a.range).createDocumentFragment();b=a.next();){if(a.isPartiallySelectedSubtree()?(b=b.cloneNode(!1),c=a.getSubtreeIterator(),b.appendChild(m(c)),c.detach()):a.remove(),10==b.nodeType)throw new M("HIERARCHY_REQUEST_ERR");e.appendChild(b)}return e}function n(a,b,c){var d,e=!(!b||!b.length),f=!!c;e&&(d=new RegExp("^("+b.join("|")+")$"));var g=[];return k(new p(a,!1),function(b){if((!e||d.test(b.nodeType))&&(!f||c(b))){var h=a.startContainer;if(b!=h||!N(h)||a.startOffset!=h.length){var i=a.endContainer;b==i&&N(i)&&0==a.endOffset||g.push(b)}}}),g}function o(a){var b="undefined"==typeof a.getName?"Range":a.getName();return"["+b+"("+J.inspectNode(a.startContainer)+":"+a.startOffset+", "+J.inspectNode(a.endContainer)+":"+a.endOffset+")]"}function p(a,b){if(this.range=a,this.clonePartiallySelectedTextNodes=b,!a.collapsed){this.sc=a.startContainer,this.so=a.startOffset,this.ec=a.endContainer,this.eo=a.endOffset;var c=a.commonAncestorContainer;this.sc===this.ec&&N(this.sc)?(this.isSingleCharacterDataNode=!0,this._first=this._last=this._next=this.sc):(this._first=this._next=this.sc!==c||N(this.sc)?T(this.sc,c,!0):this.sc.childNodes[this.so],this._last=this.ec!==c||N(this.ec)?T(this.ec,c,!0):this.ec.childNodes[this.eo-1])}}function q(a){return function(b,c){for(var d,e=c?b:b.parentNode;e;){if(d=e.nodeType,V(a,d))return e;e=e.parentNode}return null}}function r(a,b){if(ea(a,b))throw new M("INVALID_NODE_TYPE_ERR")}function s(a,b){if(!V(b,a.nodeType))throw new M("INVALID_NODE_TYPE_ERR")}function t(a,b){if(0>b||b>(N(a)?a.length:a.childNodes.length))throw new M("INDEX_SIZE_ERR")}function u(a,b){if(ca(a,!0)!==ca(b,!0))throw new M("WRONG_DOCUMENT_ERR")}function v(a){if(da(a,!0))throw new M("NO_MODIFICATION_ALLOWED_ERR")}function w(a,b){if(!a)throw new M(b)}function x(a,b){return b<=(N(a)?a.length:a.childNodes.length)}function y(a){return!!a.startContainer&&!!a.endContainer&&!(X&&(J.isBrokenNode(a.startContainer)||J.isBrokenNode(a.endContainer)))&&W(a.startContainer)==W(a.endContainer)&&x(a.startContainer,a.startOffset)&&x(a.endContainer,a.endOffset)}function z(a){if(!y(a))throw new Error("Range error: Range is not valid. This usually happens after DOM mutation. Range: ("+a.inspect()+")")}function A(a,b){z(a);var c=a.startContainer,d=a.startOffset,e=a.endContainer,f=a.endOffset,g=c===e;N(e)&&f>0&&f<e.length&&S(e,f,b),N(c)&&d>0&&d<c.length&&(c=S(c,d,b),g?(f-=d,e=c):e==c.parentNode&&f>=O(c)&&f++,d=0),a.setStartAndEnd(c,d,e,f)}function B(a){z(a);var b=a.commonAncestorContainer.parentNode.cloneNode(!1);return b.appendChild(a.cloneContents()),b.innerHTML}function C(a){a.START_TO_START=ka,a.START_TO_END=la,a.END_TO_END=ma,a.END_TO_START=na,a.NODE_BEFORE=oa,a.NODE_AFTER=pa,a.NODE_BEFORE_AND_AFTER=qa,a.NODE_INSIDE=ra}function D(a){C(a),C(a.prototype)}function E(a,b){return function(){z(this);var c,d,e=this.startContainer,f=this.startOffset,h=this.commonAncestorContainer,i=new p(this,!0);e!==h&&(c=T(e,h,!0),d=g(c),e=d.node,f=d.offset),k(i,v),i.reset();var j=a(i);return i.detach(),b(this,e,f,e,f),j}}function F(b,d){function e(a,b){return function(c){s(c,Z),s(W(c),$);var d=(a?f:g)(c);(b?h:i)(this,d.node,d.offset)}}function h(a,b,c){var e=a.endContainer,f=a.endOffset;(b!==a.startContainer||c!==a.startOffset)&&((W(b)!=W(e)||1==R(b,c,e,f))&&(e=b,f=c),d(a,b,c,e,f))}function i(a,b,c){var e=a.startContainer,f=a.startOffset;(b!==a.endContainer||c!==a.endOffset)&&((W(b)!=W(e)||-1==R(b,c,e,f))&&(e=b,f=c),d(a,e,f,b,c))}var j=function(){};j.prototype=a.rangePrototype,b.prototype=new j,K.extend(b.prototype,{setStart:function(a,b){r(a,!0),t(a,b),h(this,a,b)},setEnd:function(a,b){r(a,!0),t(a,b),i(this,a,b)},setStartAndEnd:function(){var a=arguments,b=a[0],c=a[1],e=b,f=c;switch(a.length){case 3:f=a[2];break;case 4:e=a[2],f=a[3]}d(this,b,c,e,f)},setBoundary:function(a,b,c){this["set"+(c?"Start":"End")](a,b)},setStartBefore:e(!0,!0),setStartAfter:e(!1,!0),setEndBefore:e(!0,!1),setEndAfter:e(!1,!1),collapse:function(a){z(this),a?d(this,this.startContainer,this.startOffset,this.startContainer,this.startOffset):d(this,this.endContainer,this.endOffset,this.endContainer,this.endOffset)},selectNodeContents:function(a){r(a,!0),d(this,a,0,a,U(a))},selectNode:function(a){r(a,!1),s(a,Z);var b=f(a),c=g(a);d(this,b.node,b.offset,c.node,c.offset)},extractContents:E(m,d),deleteContents:E(l,d),canSurroundContents:function(){z(this),v(this.startContainer),v(this.endContainer);var a=new p(this,!0),b=a._first&&c(a._first,this)||a._last&&c(a._last,this);return a.detach(),!b},splitBoundaries:function(){A(this)},splitBoundariesPreservingPositions:function(a){A(this,a)},normalizeBoundaries:function(){z(this);var a,b=this.startContainer,c=this.startOffset,e=this.endContainer,f=this.endOffset,g=function(a){var b=a.nextSibling;b&&b.nodeType==a.nodeType&&(e=a,f=a.length,a.appendData(b.data),Y(b))},h=function(a){var d=a.previousSibling;if(d&&d.nodeType==a.nodeType){b=a;var g=a.length;if(c=d.length,a.insertData(0,d.data),Y(d),b==e)f+=c,e=b;else if(e==a.parentNode){var h=O(a);f==h?(e=a,f=g):f>h&&f--}}},i=!0;if(N(e))f==e.length?g(e):0==f&&(a=e.previousSibling,a&&a.nodeType==e.nodeType&&(f=a.length,b==e&&(i=!1),a.appendData(e.data),Y(e),e=a));else{if(f>0){var j=e.childNodes[f-1];j&&N(j)&&g(j)}i=!this.collapsed}if(i){if(N(b))0==c?h(b):c==b.length&&(a=b.nextSibling,a&&a.nodeType==b.nodeType&&(e==a&&(e=b,f+=b.length),b.appendData(a.data),Y(a)));else if(c<b.childNodes.length){var k=b.childNodes[c];k&&N(k)&&h(k)}}else b=e,c=f;d(this,b,c,e,f)},collapseToPoint:function(a,b){r(a,!0),t(a,b),this.setStartAndEnd(a,b)}}),D(b)}function G(a){a.collapsed=a.startContainer===a.endContainer&&a.startOffset===a.endOffset,a.commonAncestorContainer=a.collapsed?a.startContainer:J.getCommonAncestor(a.startContainer,a.endContainer)}function H(a,b,c,d,e){a.startContainer=b,a.startOffset=c,a.endContainer=d,a.endOffset=e,a.document=J.getDocument(b),G(a)}function I(a){this.startContainer=a,this.startOffset=0,this.endContainer=a,this.endOffset=0,this.document=a,G(this)}var J=a.dom,K=a.util,L=J.DomPosition,M=a.DOMException,N=J.isCharacterDataNode,O=J.getNodeIndex,P=J.isOrIsAncestorOf,Q=J.getDocument,R=J.comparePoints,S=J.splitDataNode,T=J.getClosestAncestorIn,U=J.getNodeLength,V=J.arrayContains,W=J.getRootContainer,X=a.features.crashyTextNodes,Y=J.removeNode;p.prototype={_current:null,_next:null,_first:null,_last:null,isSingleCharacterDataNode:!1,reset:function(){this._current=null,this._next=this._first},hasNext:function(){return!!this._next},next:function(){var a=this._current=this._next;return a&&(this._next=a!==this._last?a.nextSibling:null,N(a)&&this.clonePartiallySelectedTextNodes&&(a===this.ec&&(a=a.cloneNode(!0)).deleteData(this.eo,a.length-this.eo),this._current===this.sc&&(a=a.cloneNode(!0)).deleteData(0,this.so))),a},remove:function(){var a,b,c=this._current;!N(c)||c!==this.sc&&c!==this.ec?c.parentNode&&Y(c):(a=c===this.sc?this.so:0,b=c===this.ec?this.eo:c.length,a!=b&&c.deleteData(a,b-a))},isPartiallySelectedSubtree:function(){var a=this._current;return c(a,this.range)},getSubtreeIterator:function(){var a;if(this.isSingleCharacterDataNode)a=this.range.cloneRange(),a.collapse(!1);else{a=new I(d(this.range));var b=this._current,c=b,e=0,f=b,g=U(b);P(b,this.sc)&&(c=this.sc,e=this.so),P(b,this.ec)&&(f=this.ec,g=this.eo),H(a,c,e,f,g)}return new p(a,this.clonePartiallySelectedTextNodes)},detach:function(){this.range=this._current=this._next=this._first=this._last=this.sc=this.so=this.ec=this.eo=null}};var Z=[1,3,4,5,7,8,10],$=[2,9,11],_=[5,6,10,12],aa=[1,3,4,5,7,8,10,11],ba=[1,3,4,5,7,8],ca=q([9,11]),da=q(_),ea=q([6,10,12]),fa=document.createElement("style"),ga=!1;try{fa.innerHTML="<b>x</b>",ga=3==fa.firstChild.nodeType}catch(ha){}a.features.htmlParsingConforms=ga;var ia=ga?function(a){var b=this.startContainer,c=Q(b);if(!b)throw new M("INVALID_STATE_ERR");var d=null;return 1==b.nodeType?d=b:N(b)&&(d=J.parentElement(b)),d=null===d||"HTML"==d.nodeName&&J.isHtmlNamespace(Q(d).documentElement)&&J.isHtmlNamespace(d)?c.createElement("body"):d.cloneNode(!1),d.innerHTML=a,J.fragmentFromNodeChildren(d)}:function(a){var b=d(this),c=b.createElement("body");return c.innerHTML=a,J.fragmentFromNodeChildren(c)},ja=["startContainer","startOffset","endContainer","endOffset","collapsed","commonAncestorContainer"],ka=0,la=1,ma=2,na=3,oa=0,pa=1,qa=2,ra=3;K.extend(a.rangePrototype,{compareBoundaryPoints:function(a,b){z(this),u(this.startContainer,b.startContainer);var c,d,e,f,g=a==na||a==ka?"start":"end",h=a==la||a==ka?"start":"end";return c=this[g+"Container"],d=this[g+"Offset"],e=b[h+"Container"],f=b[h+"Offset"],R(c,d,e,f)},insertNode:function(a){if(z(this),s(a,aa),v(this.startContainer),P(a,this.startContainer))throw new M("HIERARCHY_REQUEST_ERR");var b=h(a,this.startContainer,this.startOffset);this.setStartBefore(b)},cloneContents:function(){z(this);var a,b;if(this.collapsed)return d(this).createDocumentFragment();if(this.startContainer===this.endContainer&&N(this.startContainer))return a=this.startContainer.cloneNode(!0),a.data=a.data.slice(this.startOffset,this.endOffset),b=d(this).createDocumentFragment(),b.appendChild(a),b;var c=new p(this,!0);return a=j(c),c.detach(),a},canSurroundContents:function(){z(this),v(this.startContainer),v(this.endContainer);var a=new p(this,!0),b=a._first&&c(a._first,this)||a._last&&c(a._last,this);return a.detach(),!b},surroundContents:function(a){if(s(a,ba),!this.canSurroundContents())throw new M("INVALID_STATE_ERR");var b=this.extractContents();if(a.hasChildNodes())for(;a.lastChild;)a.removeChild(a.lastChild);h(a,this.startContainer,this.startOffset),a.appendChild(b),this.selectNode(a)},cloneRange:function(){z(this);for(var a,b=new I(d(this)),c=ja.length;c--;)a=ja[c],b[a]=this[a];return b},toString:function(){z(this);var a=this.startContainer;if(a===this.endContainer&&N(a))return 3==a.nodeType||4==a.nodeType?a.data.slice(this.startOffset,this.endOffset):"";var b=[],c=new p(this,!0);return k(c,function(a){(3==a.nodeType||4==a.nodeType)&&b.push(a.data)}),c.detach(),b.join("")},compareNode:function(a){z(this);var b=a.parentNode,c=O(a);if(!b)throw new M("NOT_FOUND_ERR");var d=this.comparePoint(b,c),e=this.comparePoint(b,c+1);return 0>d?e>0?qa:oa:e>0?pa:ra},comparePoint:function(a,b){return z(this),w(a,"HIERARCHY_REQUEST_ERR"),u(a,this.startContainer),R(a,b,this.startContainer,this.startOffset)<0?-1:R(a,b,this.endContainer,this.endOffset)>0?1:0},createContextualFragment:ia,toHtml:function(){return B(this)},intersectsNode:function(a,b){if(z(this),W(a)!=e(this))return!1;var c=a.parentNode,d=O(a);if(!c)return!0;var f=R(c,d,this.endContainer,this.endOffset),g=R(c,d+1,this.startContainer,this.startOffset);return b?0>=f&&g>=0:0>f&&g>0},isPointInRange:function(a,b){return z(this),w(a,"HIERARCHY_REQUEST_ERR"),u(a,this.startContainer),R(a,b,this.startContainer,this.startOffset)>=0&&R(a,b,this.endContainer,this.endOffset)<=0},intersectsRange:function(a){return i(this,a,!1)},intersectsOrTouchesRange:function(a){return i(this,a,!0)},intersection:function(a){if(this.intersectsRange(a)){var b=R(this.startContainer,this.startOffset,a.startContainer,a.startOffset),c=R(this.endContainer,this.endOffset,a.endContainer,a.endOffset),d=this.cloneRange();return-1==b&&d.setStart(a.startContainer,a.startOffset),1==c&&d.setEnd(a.endContainer,a.endOffset),d}return null},union:function(a){if(this.intersectsOrTouchesRange(a)){var b=this.cloneRange();return-1==R(a.startContainer,a.startOffset,this.startContainer,this.startOffset)&&b.setStart(a.startContainer,a.startOffset),1==R(a.endContainer,a.endOffset,this.endContainer,this.endOffset)&&b.setEnd(a.endContainer,a.endOffset),b}throw new M("Ranges do not intersect")},containsNode:function(a,b){return b?this.intersectsNode(a,!1):this.compareNode(a)==ra},containsNodeContents:function(a){return this.comparePoint(a,0)>=0&&this.comparePoint(a,U(a))<=0},containsRange:function(a){var b=this.intersection(a);return null!==b&&a.equals(b)},containsNodeText:function(a){var b=this.cloneRange();b.selectNode(a);var c=b.getNodes([3]);if(c.length>0){b.setStart(c[0],0);var d=c.pop();return b.setEnd(d,d.length),this.containsRange(b)}return this.containsNodeContents(a)},getNodes:function(a,b){return z(this),n(this,a,b)},getDocument:function(){return d(this)},collapseBefore:function(a){this.setEndBefore(a),this.collapse(!1)},collapseAfter:function(a){this.setStartAfter(a),this.collapse(!0)},getBookmark:function(b){var c=d(this),e=a.createRange(c);b=b||J.getBody(c),e.selectNodeContents(b);var f=this.intersection(e),g=0,h=0;return f&&(e.setEnd(f.startContainer,f.startOffset),g=e.toString().length,h=g+f.toString().length),{start:g,end:h,containerNode:b}},moveToBookmark:function(a){var b=a.containerNode,c=0;this.setStart(b,0),this.collapse(!0);for(var d,e,f,g,h=[b],i=!1,j=!1;!j&&(d=h.pop());)if(3==d.nodeType)e=c+d.length,!i&&a.start>=c&&a.start<=e&&(this.setStart(d,a.start-c),i=!0),i&&a.end>=c&&a.end<=e&&(this.setEnd(d,a.end-c),j=!0),c=e;else for(g=d.childNodes,f=g.length;f--;)h.push(g[f])},getName:function(){return"DomRange"},equals:function(a){return I.rangesEqual(this,a)},isValid:function(){return y(this)},inspect:function(){return o(this)},detach:function(){}}),F(I,H),K.extend(I,{rangeProperties:ja,RangeIterator:p,copyComparisonConstants:D,createPrototypeRange:F,inspect:o,toHtml:B,getRangeDocument:d,rangesEqual:function(a,b){return a.startContainer===b.startContainer&&a.startOffset===b.startOffset&&a.endContainer===b.endContainer&&a.endOffset===b.endOffset}}),a.DomRange=I}),H.createCoreModule("WrappedRange",["DomRange"],function(a,b){var c,d,e=a.dom,f=a.util,g=e.DomPosition,h=a.DomRange,i=e.getBody,j=e.getContentDocument,k=e.isCharacterDataNode;if(a.features.implementsDomRange&&!function(){function d(a){for(var b,c=m.length;c--;)b=m[c],a[b]=a.nativeRange[b];a.collapsed=a.startContainer===a.endContainer&&a.startOffset===a.endOffset}function g(a,b,c,d,e){var f=a.startContainer!==b||a.startOffset!=c,g=a.endContainer!==d||a.endOffset!=e,h=!a.equals(a.nativeRange);(f||g||h)&&(a.setEnd(d,e),a.setStart(b,c))}var k,l,m=h.rangeProperties;c=function(a){if(!a)throw b.createError("WrappedRange: Range must be specified");this.nativeRange=a,d(this)},h.createPrototypeRange(c,g),k=c.prototype,k.selectNode=function(a){this.nativeRange.selectNode(a),d(this)},k.cloneContents=function(){return this.nativeRange.cloneContents()},k.surroundContents=function(a){this.nativeRange.surroundContents(a),d(this)},k.collapse=function(a){this.nativeRange.collapse(a),d(this)},k.cloneRange=function(){return new c(this.nativeRange.cloneRange())},k.refresh=function(){d(this)},k.toString=function(){return this.nativeRange.toString()};var n=document.createTextNode("test");i(document).appendChild(n);var o=document.createRange();o.setStart(n,0),o.setEnd(n,0);try{o.setStart(n,1),k.setStart=function(a,b){this.nativeRange.setStart(a,b),d(this)},k.setEnd=function(a,b){this.nativeRange.setEnd(a,b),d(this)},l=function(a){return function(b){this.nativeRange[a](b),d(this)}}}catch(p){k.setStart=function(a,b){try{this.nativeRange.setStart(a,b)}catch(c){this.nativeRange.setEnd(a,b),this.nativeRange.setStart(a,b)}d(this)},k.setEnd=function(a,b){try{this.nativeRange.setEnd(a,b)}catch(c){this.nativeRange.setStart(a,b),this.nativeRange.setEnd(a,b)}d(this)},l=function(a,b){return function(c){try{this.nativeRange[a](c)}catch(e){this.nativeRange[b](c),this.nativeRange[a](c)}d(this)}}}k.setStartBefore=l("setStartBefore","setEndBefore"),k.setStartAfter=l("setStartAfter","setEndAfter"),k.setEndBefore=l("setEndBefore","setStartBefore"),k.setEndAfter=l("setEndAfter","setStartAfter"),k.selectNodeContents=function(a){this.setStartAndEnd(a,0,e.getNodeLength(a))},o.selectNodeContents(n),o.setEnd(n,3);var q=document.createRange();q.selectNodeContents(n),q.setEnd(n,4),q.setStart(n,2),-1==o.compareBoundaryPoints(o.START_TO_END,q)&&1==o.compareBoundaryPoints(o.END_TO_START,q)?k.compareBoundaryPoints=function(a,b){return b=b.nativeRange||b,a==b.START_TO_END?a=b.END_TO_START:a==b.END_TO_START&&(a=b.START_TO_END),this.nativeRange.compareBoundaryPoints(a,b)}:k.compareBoundaryPoints=function(a,b){return this.nativeRange.compareBoundaryPoints(a,b.nativeRange||b)};var r=document.createElement("div");r.innerHTML="123";var s=r.firstChild,t=i(document);t.appendChild(r),o.setStart(s,1),o.setEnd(s,2),o.deleteContents(),"13"==s.data&&(k.deleteContents=function(){this.nativeRange.deleteContents(),d(this)},k.extractContents=function(){var a=this.nativeRange.extractContents();return d(this),a}),t.removeChild(r),t=null,f.isHostMethod(o,"createContextualFragment")&&(k.createContextualFragment=function(a){return this.nativeRange.createContextualFragment(a)}),i(document).removeChild(n),k.getName=function(){return"WrappedRange"},a.WrappedRange=c,a.createNativeRange=function(a){return a=j(a,b,"createNativeRange"),a.createRange()}}(),a.features.implementsTextRange){var l=function(a){var b=a.parentElement(),c=a.duplicate();c.collapse(!0);var d=c.parentElement();c=a.duplicate(),c.collapse(!1);var f=c.parentElement(),g=d==f?d:e.getCommonAncestor(d,f);return g==b?g:e.getCommonAncestor(b,g)},m=function(a){return 0==a.compareEndPoints("StartToEnd",a)},n=function(a,b,c,d,f){var h=a.duplicate();h.collapse(c);var i=h.parentElement();if(e.isOrIsAncestorOf(b,i)||(i=b),!i.canHaveHTML){var j=new g(i.parentNode,e.getNodeIndex(i));return{boundaryPosition:j,nodeInfo:{nodeIndex:j.offset,containerElement:j.node}}}var l=e.getDocument(i).createElement("span");l.parentNode&&e.removeNode(l);for(var m,n,o,p,q,r=c?"StartToStart":"StartToEnd",s=f&&f.containerElement==i?f.nodeIndex:0,t=i.childNodes.length,u=t,v=u;;){if(v==t?i.appendChild(l):i.insertBefore(l,i.childNodes[v]),h.moveToElementText(l),m=h.compareEndPoints(r,a),0==m||s==u)break;if(-1==m){if(u==s+1)break;s=v}else u=u==s+1?s:v;v=Math.floor((s+u)/2),i.removeChild(l)}if(q=l.nextSibling,-1==m&&q&&k(q)){h.setEndPoint(c?"EndToStart":"EndToEnd",a);var w;if(/[\r\n]/.test(q.data)){var x=h.duplicate(),y=x.text.replace(/\r\n/g,"\r").length;for(w=x.moveStart("character",y);-1==(m=x.compareEndPoints("StartToEnd",x));)w++,x.moveStart("character",1)}else w=h.text.length;p=new g(q,w)}else n=(d||!c)&&l.previousSibling,o=(d||c)&&l.nextSibling,p=o&&k(o)?new g(o,0):n&&k(n)?new g(n,n.data.length):new g(i,e.getNodeIndex(l));return e.removeNode(l),{boundaryPosition:p,nodeInfo:{nodeIndex:v,containerElement:i}}},o=function(a,b){var c,d,f,g,h=a.offset,j=e.getDocument(a.node),l=i(j).createTextRange(),m=k(a.node);return m?(c=a.node,d=c.parentNode):(g=a.node.childNodes,c=h<g.length?g[h]:null,d=a.node),f=j.createElement("span"),f.innerHTML="&#feff;",c?d.insertBefore(f,c):d.appendChild(f),l.moveToElementText(f),l.collapse(!b),d.removeChild(f),m&&l[b?"moveStart":"moveEnd"]("character",h),l};d=function(a){this.textRange=a,this.refresh()},d.prototype=new h(document),
d.prototype.refresh=function(){var a,b,c,d=l(this.textRange);m(this.textRange)?b=a=n(this.textRange,d,!0,!0).boundaryPosition:(c=n(this.textRange,d,!0,!1),a=c.boundaryPosition,b=n(this.textRange,d,!1,!1,c.nodeInfo).boundaryPosition),this.setStart(a.node,a.offset),this.setEnd(b.node,b.offset)},d.prototype.getName=function(){return"WrappedTextRange"},h.copyComparisonConstants(d);var p=function(a){if(a.collapsed)return o(new g(a.startContainer,a.startOffset),!0);var b=o(new g(a.startContainer,a.startOffset),!0),c=o(new g(a.endContainer,a.endOffset),!1),d=i(h.getRangeDocument(a)).createTextRange();return d.setEndPoint("StartToStart",b),d.setEndPoint("EndToEnd",c),d};if(d.rangeToTextRange=p,d.prototype.toTextRange=function(){return p(this)},a.WrappedTextRange=d,!a.features.implementsDomRange||a.config.preferTextRange){var q=function(a){return a("return this;")()}(Function);"undefined"==typeof q.Range&&(q.Range=d),a.createNativeRange=function(a){return a=j(a,b,"createNativeRange"),i(a).createTextRange()},a.WrappedRange=d}}a.createRange=function(c){return c=j(c,b,"createRange"),new a.WrappedRange(a.createNativeRange(c))},a.createRangyRange=function(a){return a=j(a,b,"createRangyRange"),new h(a)},f.createAliasForDeprecatedMethod(a,"createIframeRange","createRange"),f.createAliasForDeprecatedMethod(a,"createIframeRangyRange","createRangyRange"),a.addShimListener(function(b){var c=b.document;"undefined"==typeof c.createRange&&(c.createRange=function(){return a.createRange(c)}),c=b=null})}),H.createCoreModule("WrappedSelection",["DomRange","WrappedRange"],function(a,b){function c(a){return"string"==typeof a?/^backward(s)?$/i.test(a):!!a}function d(a,c){if(a){if(C.isWindow(a))return a;if(a instanceof r)return a.win;var d=C.getContentDocument(a,b,c);return C.getWindow(d)}return window}function e(a){return d(a,"getWinSelection").getSelection()}function f(a){return d(a,"getDocSelection").document.selection}function g(a){var b=!1;return a.anchorNode&&(b=1==C.comparePoints(a.anchorNode,a.anchorOffset,a.focusNode,a.focusOffset)),b}function h(a,b,c){var d=c?"end":"start",e=c?"start":"end";a.anchorNode=b[d+"Container"],a.anchorOffset=b[d+"Offset"],a.focusNode=b[e+"Container"],a.focusOffset=b[e+"Offset"]}function i(a){var b=a.nativeSelection;a.anchorNode=b.anchorNode,a.anchorOffset=b.anchorOffset,a.focusNode=b.focusNode,a.focusOffset=b.focusOffset}function j(a){a.anchorNode=a.focusNode=null,a.anchorOffset=a.focusOffset=0,a.rangeCount=0,a.isCollapsed=!0,a._ranges.length=0}function k(b){var c;return b instanceof F?(c=a.createNativeRange(b.getDocument()),c.setEnd(b.endContainer,b.endOffset),c.setStart(b.startContainer,b.startOffset)):b instanceof G?c=b.nativeRange:J.implementsDomRange&&b instanceof C.getWindow(b.startContainer).Range&&(c=b),c}function l(a){if(!a.length||1!=a[0].nodeType)return!1;for(var b=1,c=a.length;c>b;++b)if(!C.isAncestorOf(a[0],a[b]))return!1;return!0}function m(a){var c=a.getNodes();if(!l(c))throw b.createError("getSingleElementFromRange: range "+a.inspect()+" did not consist of a single element");return c[0]}function n(a){return!!a&&"undefined"!=typeof a.text}function o(a,b){var c=new G(b);a._ranges=[c],h(a,c,!1),a.rangeCount=1,a.isCollapsed=c.collapsed}function p(b){if(b._ranges.length=0,"None"==b.docSelection.type)j(b);else{var c=b.docSelection.createRange();if(n(c))o(b,c);else{b.rangeCount=c.length;for(var d,e=L(c.item(0)),f=0;f<b.rangeCount;++f)d=a.createRange(e),d.selectNode(c.item(f)),b._ranges.push(d);b.isCollapsed=1==b.rangeCount&&b._ranges[0].collapsed,h(b,b._ranges[b.rangeCount-1],!1)}}}function q(a,c){for(var d=a.docSelection.createRange(),e=m(c),f=L(d.item(0)),g=M(f).createControlRange(),h=0,i=d.length;i>h;++h)g.add(d.item(h));try{g.add(e)}catch(j){throw b.createError("addRange(): Element within the specified Range could not be added to control selection (does it have layout?)")}g.select(),p(a)}function r(a,b,c){this.nativeSelection=a,this.docSelection=b,this._ranges=[],this.win=c,this.refresh()}function s(a){a.win=a.anchorNode=a.focusNode=a._ranges=null,a.rangeCount=a.anchorOffset=a.focusOffset=0,a.detached=!0}function t(a,b){for(var c,d,e=ba.length;e--;)if(c=ba[e],d=c.selection,"deleteAll"==b)s(d);else if(c.win==a)return"delete"==b?(ba.splice(e,1),!0):d;return"deleteAll"==b&&(ba.length=0),null}function u(a,c){for(var d,e=L(c[0].startContainer),f=M(e).createControlRange(),g=0,h=c.length;h>g;++g){d=m(c[g]);try{f.add(d)}catch(i){throw b.createError("setRanges(): Element within one of the specified Ranges could not be added to control selection (does it have layout?)")}}f.select(),p(a)}function v(a,b){if(a.win.document!=L(b))throw new H("WRONG_DOCUMENT_ERR")}function w(b){return function(c,d){var e;this.rangeCount?(e=this.getRangeAt(0),e["set"+(b?"Start":"End")](c,d)):(e=a.createRange(this.win.document),e.setStartAndEnd(c,d)),this.setSingleRange(e,this.isBackward())}}function x(a){var b=[],c=new I(a.anchorNode,a.anchorOffset),d=new I(a.focusNode,a.focusOffset),e="function"==typeof a.getName?a.getName():"Selection";if("undefined"!=typeof a.rangeCount)for(var f=0,g=a.rangeCount;g>f;++f)b[f]=F.inspect(a.getRangeAt(f));return"["+e+"(Ranges: "+b.join(", ")+")(anchor: "+c.inspect()+", focus: "+d.inspect()+"]"}a.config.checkSelectionRanges=!0;var y,z,A="boolean",B="number",C=a.dom,D=a.util,E=D.isHostMethod,F=a.DomRange,G=a.WrappedRange,H=a.DOMException,I=C.DomPosition,J=a.features,K="Control",L=C.getDocument,M=C.getBody,N=F.rangesEqual,O=E(window,"getSelection"),P=D.isHostObject(document,"selection");J.implementsWinGetSelection=O,J.implementsDocSelection=P;var Q=P&&(!O||a.config.preferTextRange);if(Q)y=f,a.isSelectionValid=function(a){var b=d(a,"isSelectionValid").document,c=b.selection;return"None"!=c.type||L(c.createRange().parentElement())==b};else{if(!O)return b.fail("Neither document.selection or window.getSelection() detected."),!1;y=e,a.isSelectionValid=function(){return!0}}a.getNativeSelection=y;var R=y();if(!R)return b.fail("Native selection was null (possibly issue 138?)"),!1;var S=a.createNativeRange(document),T=M(document),U=D.areHostProperties(R,["anchorNode","focusNode","anchorOffset","focusOffset"]);J.selectionHasAnchorAndFocus=U;var V=E(R,"extend");J.selectionHasExtend=V;var W=typeof R.rangeCount==B;J.selectionHasRangeCount=W;var X=!1,Y=!0,Z=V?function(b,c){var d=F.getRangeDocument(c),e=a.createRange(d);e.collapseToPoint(c.endContainer,c.endOffset),b.addRange(k(e)),b.extend(c.startContainer,c.startOffset)}:null;D.areHostMethods(R,["addRange","getRangeAt","removeAllRanges"])&&typeof R.rangeCount==B&&J.implementsDomRange&&!function(){var b=window.getSelection();if(b){for(var c=b.rangeCount,d=c>1,e=[],f=g(b),h=0;c>h;++h)e[h]=b.getRangeAt(h);var i=C.createTestElement(document,"",!1),j=i.appendChild(document.createTextNode("   ")),k=document.createRange();if(k.setStart(j,1),k.collapse(!0),b.removeAllRanges(),b.addRange(k),Y=1==b.rangeCount,b.removeAllRanges(),!d){var l=window.navigator.appVersion.match(/Chrome\/(.*?) /);if(l&&parseInt(l[1])>=36)X=!1;else{var m=k.cloneRange();k.setStart(j,0),m.setEnd(j,3),m.setStart(j,2),b.addRange(k),b.addRange(m),X=2==b.rangeCount}}for(C.removeNode(i),b.removeAllRanges(),h=0;c>h;++h)0==h&&f?Z?Z(b,e[h]):(a.warn("Rangy initialization: original selection was backwards but selection has been restored forwards because the browser does not support Selection.extend"),b.addRange(e[h])):b.addRange(e[h])}}(),J.selectionSupportsMultipleRanges=X,J.collapsedNonEditableSelectionsSupported=Y;var $,_=!1;T&&E(T,"createControlRange")&&($=T.createControlRange(),D.areHostProperties($,["item","add"])&&(_=!0)),J.implementsControlRange=_,z=U?function(a){return a.anchorNode===a.focusNode&&a.anchorOffset===a.focusOffset}:function(a){return a.rangeCount?a.getRangeAt(a.rangeCount-1).collapsed:!1};var aa;E(R,"getRangeAt")?aa=function(a,b){try{return a.getRangeAt(b)}catch(c){return null}}:U&&(aa=function(b){var c=L(b.anchorNode),d=a.createRange(c);return d.setStartAndEnd(b.anchorNode,b.anchorOffset,b.focusNode,b.focusOffset),d.collapsed!==this.isCollapsed&&d.setStartAndEnd(b.focusNode,b.focusOffset,b.anchorNode,b.anchorOffset),d}),r.prototype=a.selectionPrototype;var ba=[],ca=function(a){if(a&&a instanceof r)return a.refresh(),a;a=d(a,"getNativeSelection");var b=t(a),c=y(a),e=P?f(a):null;return b?(b.nativeSelection=c,b.docSelection=e,b.refresh()):(b=new r(c,e,a),ba.push({win:a,selection:b})),b};a.getSelection=ca,D.createAliasForDeprecatedMethod(a,"getIframeSelection","getSelection");var da=r.prototype;if(!Q&&U&&D.areHostMethods(R,["removeAllRanges","addRange"])){da.removeAllRanges=function(){this.nativeSelection.removeAllRanges(),j(this)};var ea=function(a,b){Z(a.nativeSelection,b),a.refresh()};W?da.addRange=function(b,d){if(_&&P&&this.docSelection.type==K)q(this,b);else if(c(d)&&V)ea(this,b);else{var e;X?e=this.rangeCount:(this.removeAllRanges(),e=0);var f=k(b).cloneRange();try{this.nativeSelection.addRange(f)}catch(g){}if(this.rangeCount=this.nativeSelection.rangeCount,this.rangeCount==e+1){if(a.config.checkSelectionRanges){var i=aa(this.nativeSelection,this.rangeCount-1);i&&!N(i,b)&&(b=new G(i))}this._ranges[this.rangeCount-1]=b,h(this,b,ha(this.nativeSelection)),this.isCollapsed=z(this)}else this.refresh()}}:da.addRange=function(a,b){c(b)&&V?ea(this,a):(this.nativeSelection.addRange(k(a)),this.refresh())},da.setRanges=function(a){if(_&&P&&a.length>1)u(this,a);else{this.removeAllRanges();for(var b=0,c=a.length;c>b;++b)this.addRange(a[b])}}}else{if(!(E(R,"empty")&&E(S,"select")&&_&&Q))return b.fail("No means of selecting a Range or TextRange was found"),!1;da.removeAllRanges=function(){try{if(this.docSelection.empty(),"None"!=this.docSelection.type){var a;if(this.anchorNode)a=L(this.anchorNode);else if(this.docSelection.type==K){var b=this.docSelection.createRange();b.length&&(a=L(b.item(0)))}if(a){var c=M(a).createTextRange();c.select(),this.docSelection.empty()}}}catch(d){}j(this)},da.addRange=function(b){this.docSelection.type==K?q(this,b):(a.WrappedTextRange.rangeToTextRange(b).select(),this._ranges[0]=b,this.rangeCount=1,this.isCollapsed=this._ranges[0].collapsed,h(this,b,!1))},da.setRanges=function(a){this.removeAllRanges();var b=a.length;b>1?u(this,a):b&&this.addRange(a[0])}}da.getRangeAt=function(a){if(0>a||a>=this.rangeCount)throw new H("INDEX_SIZE_ERR");return this._ranges[a].cloneRange()};var fa;if(Q)fa=function(b){var c;a.isSelectionValid(b.win)?c=b.docSelection.createRange():(c=M(b.win.document).createTextRange(),c.collapse(!0)),b.docSelection.type==K?p(b):n(c)?o(b,c):j(b)};else if(E(R,"getRangeAt")&&typeof R.rangeCount==B)fa=function(b){if(_&&P&&b.docSelection.type==K)p(b);else if(b._ranges.length=b.rangeCount=b.nativeSelection.rangeCount,b.rangeCount){for(var c=0,d=b.rangeCount;d>c;++c)b._ranges[c]=new a.WrappedRange(b.nativeSelection.getRangeAt(c));h(b,b._ranges[b.rangeCount-1],ha(b.nativeSelection)),b.isCollapsed=z(b)}else j(b)};else{if(!U||typeof R.isCollapsed!=A||typeof S.collapsed!=A||!J.implementsDomRange)return b.fail("No means of obtaining a Range or TextRange from the user's selection was found"),!1;fa=function(a){var b,c=a.nativeSelection;c.anchorNode?(b=aa(c,0),a._ranges=[b],a.rangeCount=1,i(a),a.isCollapsed=z(a)):j(a)}}da.refresh=function(a){var b=a?this._ranges.slice(0):null,c=this.anchorNode,d=this.anchorOffset;if(fa(this),a){var e=b.length;if(e!=this._ranges.length)return!0;if(this.anchorNode!=c||this.anchorOffset!=d)return!0;for(;e--;)if(!N(b[e],this._ranges[e]))return!0;return!1}};var ga=function(a,b){var c=a.getAllRanges();a.removeAllRanges();for(var d=0,e=c.length;e>d;++d)N(b,c[d])||a.addRange(c[d]);a.rangeCount||j(a)};_&&P?da.removeRange=function(a){if(this.docSelection.type==K){for(var b,c=this.docSelection.createRange(),d=m(a),e=L(c.item(0)),f=M(e).createControlRange(),g=!1,h=0,i=c.length;i>h;++h)b=c.item(h),b!==d||g?f.add(c.item(h)):g=!0;f.select(),p(this)}else ga(this,a)}:da.removeRange=function(a){ga(this,a)};var ha;!Q&&U&&J.implementsDomRange?(ha=g,da.isBackward=function(){return ha(this)}):ha=da.isBackward=function(){return!1},da.isBackwards=da.isBackward,da.toString=function(){for(var a=[],b=0,c=this.rangeCount;c>b;++b)a[b]=""+this._ranges[b];return a.join("")},da.collapse=function(b,c){v(this,b);var d=a.createRange(b);d.collapseToPoint(b,c),this.setSingleRange(d),this.isCollapsed=!0},da.collapseToStart=function(){if(!this.rangeCount)throw new H("INVALID_STATE_ERR");var a=this._ranges[0];this.collapse(a.startContainer,a.startOffset)},da.collapseToEnd=function(){if(!this.rangeCount)throw new H("INVALID_STATE_ERR");var a=this._ranges[this.rangeCount-1];this.collapse(a.endContainer,a.endOffset)},da.selectAllChildren=function(b){v(this,b);var c=a.createRange(b);c.selectNodeContents(b),this.setSingleRange(c)},da.deleteFromDocument=function(){if(_&&P&&this.docSelection.type==K){for(var a,b=this.docSelection.createRange();b.length;)a=b.item(0),b.remove(a),C.removeNode(a);this.refresh()}else if(this.rangeCount){var c=this.getAllRanges();if(c.length){this.removeAllRanges();for(var d=0,e=c.length;e>d;++d)c[d].deleteContents();this.addRange(c[e-1])}}},da.eachRange=function(a,b){for(var c=0,d=this._ranges.length;d>c;++c)if(a(this.getRangeAt(c)))return b},da.getAllRanges=function(){var a=[];return this.eachRange(function(b){a.push(b)}),a},da.setSingleRange=function(a,b){this.removeAllRanges(),this.addRange(a,b)},da.callMethodOnEachRange=function(a,b){var c=[];return this.eachRange(function(d){c.push(d[a].apply(d,b||[]))}),c},da.setStart=w(!0),da.setEnd=w(!1),a.rangePrototype.select=function(a){ca(this.getDocument()).setSingleRange(this,a)},da.changeEachRange=function(a){var b=[],c=this.isBackward();this.eachRange(function(c){a(c),b.push(c)}),this.removeAllRanges(),c&&1==b.length?this.addRange(b[0],"backward"):this.setRanges(b)},da.containsNode=function(a,b){return this.eachRange(function(c){return c.containsNode(a,b)},!0)||!1},da.getBookmark=function(a){return{backward:this.isBackward(),rangeBookmarks:this.callMethodOnEachRange("getBookmark",[a])}},da.moveToBookmark=function(b){for(var c,d,e=[],f=0;c=b.rangeBookmarks[f++];)d=a.createRange(this.win),d.moveToBookmark(c),e.push(d);b.backward?this.setSingleRange(e[0],"backward"):this.setRanges(e)},da.saveRanges=function(){return{backward:this.isBackward(),ranges:this.callMethodOnEachRange("cloneRange")}},da.restoreRanges=function(a){this.removeAllRanges();for(var b,c=0;b=a.ranges[c];++c)this.addRange(b,a.backward&&0==c)},da.toHtml=function(){var a=[];return this.eachRange(function(b){a.push(F.toHtml(b))}),a.join("")},J.implementsTextRange&&(da.getNativeTextRange=function(){var c;if(c=this.docSelection){var d=c.createRange();if(n(d))return d;throw b.createError("getNativeTextRange: selection is a control selection")}if(this.rangeCount>0)return a.WrappedTextRange.rangeToTextRange(this.getRangeAt(0));throw b.createError("getNativeTextRange: selection contains no range")}),da.getName=function(){return"WrappedSelection"},da.inspect=function(){return x(this)},da.detach=function(){t(this.win,"delete"),s(this)},r.detachAll=function(){t(null,"deleteAll")},r.inspect=x,r.isDirectionBackward=c,a.Selection=r,a.selectionPrototype=da,a.addShimListener(function(a){"undefined"==typeof a.getSelection&&(a.getSelection=function(){return ca(a)}),a=null})});var M=!1,N=function(a){M||(M=!0,!H.initialized&&H.config.autoInitialize&&l())};return F&&("complete"==document.readyState?N():(a(document,"addEventListener")&&document.addEventListener("DOMContentLoaded",N,!1),J(window,"load",N))),H},this),function(b,c){"function"==typeof define&&define.amd?define(["./rangy-core"],b):"undefined"!=typeof module&&"object"==typeof a?module.exports=b(require("rangy")):b(c.rangy)}(function(a){return a.createModule("SaveRestore",["WrappedRange"],function(a,b){function c(a,b){return(b||document).getElementById(a)}function d(a,b){var c,d="selectionBoundary_"+ +new Date+"_"+(""+Math.random()).slice(2),e=o.getDocument(a.startContainer),f=a.cloneRange();return f.collapse(b),c=e.createElement("span"),c.id=d,c.style.lineHeight="0",c.style.display="none",c.className="rangySelectionBoundary",c.appendChild(e.createTextNode(r)),f.insertNode(c),c}function e(a,d,e,f){var g=c(e,a);g?(d[f?"setStartBefore":"setEndBefore"](g),p(g)):b.warn("Marker element has been removed. Cannot restore selection.")}function f(a,b){return b.compareBoundaryPoints(a.START_TO_START,a)}function g(b,c){var e,f,g=a.DomRange.getRangeDocument(b),h=b.toString(),i=q(c);return b.collapsed?(f=d(b,!1),{document:g,markerId:f.id,collapsed:!0}):(f=d(b,!1),e=d(b,!0),{document:g,startMarkerId:e.id,endMarkerId:f.id,collapsed:!1,backward:i,toString:function(){return"original text: '"+h+"', new text: '"+b.toString()+"'"}})}function h(d,f){var g=d.document;"undefined"==typeof f&&(f=!0);var h=a.createRange(g);if(d.collapsed){var i=c(d.markerId,g);if(i){i.style.display="inline";var j=i.previousSibling;j&&3==j.nodeType?(p(i),h.collapseToPoint(j,j.length)):(h.collapseBefore(i),p(i))}else b.warn("Marker element has been removed. Cannot restore selection.")}else e(g,h,d.startMarkerId,!0),e(g,h,d.endMarkerId,!1);return f&&h.normalizeBoundaries(),h}function i(b,d){var e,h,i=[],j=q(d);b=b.slice(0),b.sort(f);for(var k=0,l=b.length;l>k;++k)i[k]=g(b[k],j);for(k=l-1;k>=0;--k)e=b[k],h=a.DomRange.getRangeDocument(e),e.collapsed?e.collapseAfter(c(i[k].markerId,h)):(e.setEndBefore(c(i[k].endMarkerId,h)),e.setStartAfter(c(i[k].startMarkerId,h)));return i}function j(c){if(!a.isSelectionValid(c))return b.warn("Cannot save selection. This usually happens when the selection is collapsed and the selection document has lost focus."),null;var d=a.getSelection(c),e=d.getAllRanges(),f=1==e.length&&d.isBackward(),g=i(e,f);return f?d.setSingleRange(e[0],f):d.setRanges(e),{win:c,rangeInfos:g,restored:!1}}function k(a){for(var b=[],c=a.length,d=c-1;d>=0;d--)b[d]=h(a[d],!0);return b}function l(b,c){if(!b.restored){var d=b.rangeInfos,e=a.getSelection(b.win),f=k(d),g=d.length;1==g&&c&&a.features.selectionHasExtend&&d[0].backward?(e.removeAllRanges(),e.addRange(f[0],!0)):e.setRanges(f),b.restored=!0}}function m(a,b){var d=c(b,a);d&&p(d)}function n(a){for(var b,c=a.rangeInfos,d=0,e=c.length;e>d;++d)b=c[d],b.collapsed?m(a.doc,b.markerId):(m(a.doc,b.startMarkerId),m(a.doc,b.endMarkerId))}var o=a.dom,p=o.removeNode,q=a.Selection.isDirectionBackward,r="\ufeff";a.util.extend(a,{saveRange:g,restoreRange:h,saveRanges:i,restoreRanges:k,saveSelection:j,restoreSelection:l,removeMarkerElement:m,removeMarkers:n})}),a},this)}({},function(){return this}());
/**
 * @license AngularJS v1.3.10
 * (c) 2010-2014 Google, Inc. http://angularjs.org
 * License: MIT
 */
(function(window, angular, undefined) {'use strict';

var $sanitizeMinErr = angular.$$minErr('$sanitize');

/**
 * @ngdoc module
 * @name ngSanitize
 * @description
 *
 * # ngSanitize
 *
 * The `ngSanitize` module provides functionality to sanitize HTML.
 *
 *
 * <div doc-module-components="ngSanitize"></div>
 *
 * See {@link ngSanitize.$sanitize `$sanitize`} for usage.
 */

/*
 * HTML Parser By Misko Hevery (misko@hevery.com)
 * based on:  HTML Parser By John Resig (ejohn.org)
 * Original code by Erik Arvidsson, Mozilla Public License
 * http://erik.eae.net/simplehtmlparser/simplehtmlparser.js
 *
 * // Use like so:
 * htmlParser(htmlString, {
 *     start: function(tag, attrs, unary) {},
 *     end: function(tag) {},
 *     chars: function(text) {},
 *     comment: function(text) {}
 * });
 *
 */


/**
 * @ngdoc service
 * @name $sanitize
 * @kind function
 *
 * @description
 *   The input is sanitized by parsing the HTML into tokens. All safe tokens (from a whitelist) are
 *   then serialized back to properly escaped html string. This means that no unsafe input can make
 *   it into the returned string, however, since our parser is more strict than a typical browser
 *   parser, it's possible that some obscure input, which would be recognized as valid HTML by a
 *   browser, won't make it through the sanitizer. The input may also contain SVG markup.
 *   The whitelist is configured using the functions `aHrefSanitizationWhitelist` and
 *   `imgSrcSanitizationWhitelist` of {@link ng.$compileProvider `$compileProvider`}.
 *
 * @param {string} html HTML input.
 * @returns {string} Sanitized HTML.
 *
 * @example
   <example module="sanitizeExample" deps="angular-sanitize.js">
   <file name="index.html">
     <script>
         angular.module('sanitizeExample', ['ngSanitize'])
           .controller('ExampleController', ['$scope', '$sce', function($scope, $sce) {
             $scope.snippet =
               '<p style="color:blue">an html\n' +
               '<em onmouseover="this.textContent=\'PWN3D!\'">click here</em>\n' +
               'snippet</p>';
             $scope.deliberatelyTrustDangerousSnippet = function() {
               return $sce.trustAsHtml($scope.snippet);
             };
           }]);
     </script>
     <div ng-controller="ExampleController">
        Snippet: <textarea ng-model="snippet" cols="60" rows="3"></textarea>
       <table>
         <tr>
           <td>Directive</td>
           <td>How</td>
           <td>Source</td>
           <td>Rendered</td>
         </tr>
         <tr id="bind-html-with-sanitize">
           <td>ng-bind-html</td>
           <td>Automatically uses $sanitize</td>
           <td><pre>&lt;div ng-bind-html="snippet"&gt;<br/>&lt;/div&gt;</pre></td>
           <td><div ng-bind-html="snippet"></div></td>
         </tr>
         <tr id="bind-html-with-trust">
           <td>ng-bind-html</td>
           <td>Bypass $sanitize by explicitly trusting the dangerous value</td>
           <td>
           <pre>&lt;div ng-bind-html="deliberatelyTrustDangerousSnippet()"&gt;
&lt;/div&gt;</pre>
           </td>
           <td><div ng-bind-html="deliberatelyTrustDangerousSnippet()"></div></td>
         </tr>
         <tr id="bind-default">
           <td>ng-bind</td>
           <td>Automatically escapes</td>
           <td><pre>&lt;div ng-bind="snippet"&gt;<br/>&lt;/div&gt;</pre></td>
           <td><div ng-bind="snippet"></div></td>
         </tr>
       </table>
       </div>
   </file>
   <file name="protractor.js" type="protractor">
     it('should sanitize the html snippet by default', function() {
       expect(element(by.css('#bind-html-with-sanitize div')).getInnerHtml()).
         toBe('<p>an html\n<em>click here</em>\nsnippet</p>');
     });

     it('should inline raw snippet if bound to a trusted value', function() {
       expect(element(by.css('#bind-html-with-trust div')).getInnerHtml()).
         toBe("<p style=\"color:blue\">an html\n" +
              "<em onmouseover=\"this.textContent='PWN3D!'\">click here</em>\n" +
              "snippet</p>");
     });

     it('should escape snippet without any filter', function() {
       expect(element(by.css('#bind-default div')).getInnerHtml()).
         toBe("&lt;p style=\"color:blue\"&gt;an html\n" +
              "&lt;em onmouseover=\"this.textContent='PWN3D!'\"&gt;click here&lt;/em&gt;\n" +
              "snippet&lt;/p&gt;");
     });

     it('should update', function() {
       element(by.model('snippet')).clear();
       element(by.model('snippet')).sendKeys('new <b onclick="alert(1)">text</b>');
       expect(element(by.css('#bind-html-with-sanitize div')).getInnerHtml()).
         toBe('new <b>text</b>');
       expect(element(by.css('#bind-html-with-trust div')).getInnerHtml()).toBe(
         'new <b onclick="alert(1)">text</b>');
       expect(element(by.css('#bind-default div')).getInnerHtml()).toBe(
         "new &lt;b onclick=\"alert(1)\"&gt;text&lt;/b&gt;");
     });
   </file>
   </example>
 */
function $SanitizeProvider() {
  this.$get = ['$$sanitizeUri', function($$sanitizeUri) {
    return function(html) {
      if (typeof arguments[1] != 'undefined') {
        arguments[1].version = 'taSanitize';
      }
      var buf = [];
      htmlParser(html, htmlSanitizeWriter(buf, function(uri, isImage) {
        return !/^unsafe/.test($$sanitizeUri(uri, isImage));
      }));
      return buf.join('');
    };
  }];
}

function sanitizeText(chars) {
  var buf = [];
  var writer = htmlSanitizeWriter(buf, angular.noop);
  writer.chars(chars);
  return buf.join('');
}


// Regular Expressions for parsing tags and attributes
var START_TAG_REGEXP =
       /^<((?:[a-zA-Z])[\w:-]*)((?:\s+[\w:-]+(?:\s*=\s*(?:(?:"[^"]*")|(?:'[^']*')|[^>\s]+))?)*)\s*(\/?)\s*(>?)/,
  END_TAG_REGEXP = /^<\/\s*([\w:-]+)[^>]*>/,
  ATTR_REGEXP = /([\w:-]+)(?:\s*=\s*(?:(?:"((?:[^"])*)")|(?:'((?:[^'])*)')|([^>\s]+)))?/g,
  BEGIN_TAG_REGEXP = /^</,
  BEGING_END_TAGE_REGEXP = /^<\//,
  COMMENT_REGEXP = /<!--(.*?)-->/g,
  SINGLE_COMMENT_REGEXP = /(^<!--.*?-->)/,
  DOCTYPE_REGEXP = /<!DOCTYPE([^>]*?)>/i,
  CDATA_REGEXP = /<!\[CDATA\[(.*?)]]>/g,
  SURROGATE_PAIR_REGEXP = /[\uD800-\uDBFF][\uDC00-\uDFFF]/g,
  // Match everything outside of normal chars and " (quote character)
  NON_ALPHANUMERIC_REGEXP = /([^\#-~| |!])/g,
  WHITE_SPACE_REGEXP = /^(\s+)/;


// Good source of info about elements and attributes
// http://dev.w3.org/html5/spec/Overview.html#semantics
// http://simon.html5.org/html-elements

// Safe Void Elements - HTML5
// http://dev.w3.org/html5/spec/Overview.html#void-elements
var voidElements = makeMap("area,br,col,hr,img,wbr,input");

// Elements that you can, intentionally, leave open (and which close themselves)
// http://dev.w3.org/html5/spec/Overview.html#optional-tags
var optionalEndTagBlockElements = makeMap("colgroup,dd,dt,li,p,tbody,td,tfoot,th,thead,tr"),
    optionalEndTagInlineElements = makeMap("rp,rt"),
    optionalEndTagElements = angular.extend({},
                                            optionalEndTagInlineElements,
                                            optionalEndTagBlockElements);

// Safe Block Elements - HTML5
var blockElements = angular.extend({}, optionalEndTagBlockElements, makeMap("address,article," +
        "aside,blockquote,caption,center,del,dir,div,dl,figure,figcaption,footer,h1,h2,h3,h4,h5," +
        "h6,header,hgroup,hr,ins,map,menu,nav,ol,pre,script,section,table,ul"));

// Inline Elements - HTML5
var inlineElements = angular.extend({}, optionalEndTagInlineElements, makeMap("a,abbr,acronym,b," +
        "bdi,bdo,big,br,cite,code,del,dfn,em,font,i,img,ins,kbd,label,map,mark,q,ruby,rp,rt,s," +
        "samp,small,span,strike,strong,sub,sup,time,tt,u,var"));

// SVG Elements
// https://wiki.whatwg.org/wiki/Sanitization_rules#svg_Elements
var svgElements = makeMap("animate,animateColor,animateMotion,animateTransform,circle,defs," +
        "desc,ellipse,font-face,font-face-name,font-face-src,g,glyph,hkern,image,linearGradient," +
        "line,marker,metadata,missing-glyph,mpath,path,polygon,polyline,radialGradient,rect,set," +
        "stop,svg,switch,text,title,tspan,use");

// Special Elements (can contain anything)
var specialElements = makeMap("script,style");

var validElements = angular.extend({},
                                   voidElements,
                                   blockElements,
                                   inlineElements,
                                   optionalEndTagElements,
                                   svgElements);

//Attributes that have href and hence need to be sanitized
var uriAttrs = makeMap("background,cite,href,longdesc,src,usemap,xlink:href");

var htmlAttrs = makeMap('abbr,align,alt,axis,bgcolor,border,cellpadding,cellspacing,class,clear,'+
    'color,cols,colspan,compact,coords,dir,face,headers,height,hreflang,hspace,'+
    'id,ismap,lang,language,nohref,nowrap,rel,rev,rows,rowspan,rules,'+
    'scope,scrolling,shape,size,span,start,summary,target,title,type,'+
    'valign,value,vspace,width');

// SVG attributes (without "id" and "name" attributes)
// https://wiki.whatwg.org/wiki/Sanitization_rules#svg_Attributes
var svgAttrs = makeMap('accent-height,accumulate,additive,alphabetic,arabic-form,ascent,' +
    'attributeName,attributeType,baseProfile,bbox,begin,by,calcMode,cap-height,class,color,' +
    'color-rendering,content,cx,cy,d,dx,dy,descent,display,dur,end,fill,fill-rule,font-family,' +
    'font-size,font-stretch,font-style,font-variant,font-weight,from,fx,fy,g1,g2,glyph-name,' +
    'gradientUnits,hanging,height,horiz-adv-x,horiz-origin-x,ideographic,k,keyPoints,' +
    'keySplines,keyTimes,lang,marker-end,marker-mid,marker-start,markerHeight,markerUnits,' +
    'markerWidth,mathematical,max,min,offset,opacity,orient,origin,overline-position,' +
    'overline-thickness,panose-1,path,pathLength,points,preserveAspectRatio,r,refX,refY,' +
    'repeatCount,repeatDur,requiredExtensions,requiredFeatures,restart,rotate,rx,ry,slope,stemh,' +
    'stemv,stop-color,stop-opacity,strikethrough-position,strikethrough-thickness,stroke,' +
    'stroke-dasharray,stroke-dashoffset,stroke-linecap,stroke-linejoin,stroke-miterlimit,' +
    'stroke-opacity,stroke-width,systemLanguage,target,text-anchor,to,transform,type,u1,u2,' +
    'underline-position,underline-thickness,unicode,unicode-range,units-per-em,values,version,' +
    'viewBox,visibility,width,widths,x,x-height,x1,x2,xlink:actuate,xlink:arcrole,xlink:role,' +
    'xlink:show,xlink:title,xlink:type,xml:base,xml:lang,xml:space,xmlns,xmlns:xlink,y,y1,y2,' +
    'zoomAndPan');

var validAttrs = angular.extend({},
                                uriAttrs,
                                svgAttrs,
                                htmlAttrs);

function makeMap(str) {
  var obj = {}, items = str.split(','), i;
  for (i = 0; i < items.length; i++) obj[items[i]] = true;
  return obj;
}


/**
 * @example
 * htmlParser(htmlString, {
 *     start: function(tag, attrs, unary) {},
 *     end: function(tag) {},
 *     chars: function(text) {},
 *     comment: function(text) {}
 * });
 *
 * @param {string} html string
 * @param {object} handler
 */
function htmlParser(html, handler) {
  if (typeof html !== 'string') {
    if (html === null || typeof html === 'undefined') {
      html = '';
    } else {
      html = '' + html;
    }
  }
  var index, chars, match, stack = [], last = html, text;
  stack.last = function() { return stack[ stack.length - 1 ]; };

  while (html) {
    text = '';
    chars = true;

    // Make sure we're not in a script or style element
    if (!stack.last() || !specialElements[ stack.last() ]) {

      // White space
      if (WHITE_SPACE_REGEXP.test(html)) {
        match = html.match(WHITE_SPACE_REGEXP);

        if (match) {
          var mat = match[0];
          if (handler.whitespace) handler.whitespace(match[0]);
          html = html.replace(match[0], '');
          chars = false;
        }
      //Comment
      } else if (SINGLE_COMMENT_REGEXP.test(html)) {
        match = html.match(SINGLE_COMMENT_REGEXP);

        if (match) {
          if (handler.comment) handler.comment(match[1]);
          html = html.replace(match[0], '');
          chars = false;
        }
      // DOCTYPE
      } else if (DOCTYPE_REGEXP.test(html)) {
        match = html.match(DOCTYPE_REGEXP);

        if (match) {
          html = html.replace(match[0], '');
          chars = false;
        }
      // end tag
      } else if (BEGING_END_TAGE_REGEXP.test(html)) {
        match = html.match(END_TAG_REGEXP);

        if (match) {
          html = html.substring(match[0].length);
          match[0].replace(END_TAG_REGEXP, parseEndTag);
          chars = false;
        }

      // start tag
      } else if (BEGIN_TAG_REGEXP.test(html)) {
        match = html.match(START_TAG_REGEXP);

        if (match) {
          // We only have a valid start-tag if there is a '>'.
          if (match[4]) {
            html = html.substring(match[0].length);
            match[0].replace(START_TAG_REGEXP, parseStartTag);
          }
          chars = false;
        } else {
          // no ending tag found --- this piece should be encoded as an entity.
          text += '<';
          html = html.substring(1);
        }
      }

      if (chars) {
        index = html.indexOf("<");

        text += index < 0 ? html : html.substring(0, index);
        html = index < 0 ? "" : html.substring(index);

        if (handler.chars) handler.chars(decodeEntities(text));
      }

    } else {
      html = html.replace(new RegExp("([^]*)<\\s*\\/\\s*" + stack.last() + "[^>]*>", 'i'),
        function(all, text) {
          text = text.replace(COMMENT_REGEXP, "$1").replace(CDATA_REGEXP, "$1");

          if (handler.chars) handler.chars(decodeEntities(text));

          return "";
      });

      parseEndTag("", stack.last());
    }

    if (html == last) {
      throw $sanitizeMinErr('badparse', "The sanitizer was unable to parse the following block " +
                                        "of html: {0}", html);
    }
    last = html;
  }

  // Clean up any remaining tags
  parseEndTag();

  function parseStartTag(tag, tagName, rest, unary) {
    tagName = angular.lowercase(tagName);
    if (blockElements[ tagName ]) {
      while (stack.last() && inlineElements[ stack.last() ]) {
        parseEndTag("", stack.last());
      }
    }

    if (optionalEndTagElements[ tagName ] && stack.last() == tagName) {
      parseEndTag("", tagName);
    }

    unary = voidElements[ tagName ] || !!unary;

    if (!unary)
      stack.push(tagName);

    var attrs = {};

    rest.replace(ATTR_REGEXP,
      function(match, name, doubleQuotedValue, singleQuotedValue, unquotedValue) {
        var value = doubleQuotedValue
          || singleQuotedValue
          || unquotedValue
          || '';

        attrs[name] = decodeEntities(value);
    });
    if (handler.start) handler.start(tagName, attrs, unary);
  }

  function parseEndTag(tag, tagName) {
    var pos = 0, i;
    tagName = angular.lowercase(tagName);
    if (tagName)
      // Find the closest opened tag of the same type
      for (pos = stack.length - 1; pos >= 0; pos--)
        if (stack[ pos ] == tagName)
          break;

    if (pos >= 0) {
      // Close all the open elements, up the stack
      for (i = stack.length - 1; i >= pos; i--)
        if (handler.end) handler.end(stack[ i ]);

      // Remove the open elements from the stack
      stack.length = pos;
    }
  }
}

var hiddenPre=document.createElement("pre");
var spaceRe = /^(\s*)([\s\S]*?)(\s*)$/;
/**
 * decodes all entities into regular string
 * @param value
 * @returns {string} A string with decoded entities.
 */
function decodeEntities(value) {
  if (!value) { return ''; }

  // Note: IE8 does not preserve spaces at the start/end of innerHTML
  // so we must capture them and reattach them afterward
  var parts = spaceRe.exec(value);
  var spaceBefore = parts[1];
  var spaceAfter = parts[3];
  var content = parts[2];
  if (content) {
    hiddenPre.innerHTML=content.replace(/</g,"&lt;");
    // innerText depends on styling as it doesn't display hidden elements.
    // Therefore, it's better to use textContent not to cause unnecessary
    // reflows. However, IE<9 don't support textContent so the innerText
    // fallback is necessary.
    content = 'textContent' in hiddenPre ?
      hiddenPre.textContent : hiddenPre.innerText;
  }
  return spaceBefore + content + spaceAfter;
}

/**
 * Escapes all potentially dangerous characters, so that the
 * resulting string can be safely inserted into attribute or
 * element text.
 * @param value
 * @returns {string} escaped text
 */
function encodeEntities(value) {
  return value.
    replace(/&/g, '&amp;').
    replace(SURROGATE_PAIR_REGEXP, function(value) {
      var hi = value.charCodeAt(0);
      var low = value.charCodeAt(1);
      return '&#' + (((hi - 0xD800) * 0x400) + (low - 0xDC00) + 0x10000) + ';';
    }).
    replace(NON_ALPHANUMERIC_REGEXP, function(value) {
      // unsafe chars are: \u0000-\u001f \u007f-\u009f \u00ad \u0600-\u0604 \u070f \u17b4 \u17b5 \u200c-\u200f \u2028-\u202f \u2060-\u206f \ufeff \ufff0-\uffff from jslint.com/lint.html
      // decimal values are: 0-31, 127-159, 173, 1536-1540, 1807, 6068, 6069, 8204-8207, 8232-8239, 8288-8303, 65279, 65520-65535
      var c = value.charCodeAt(0);
      // if unsafe character encode
      if(c <= 159 ||
        c == 173 ||
        (c >= 1536 && c <= 1540) ||
        c == 1807 ||
        c == 6068 ||
        c == 6069 ||
        (c >= 8204 && c <= 8207) ||
        (c >= 8232 && c <= 8239) ||
        (c >= 8288 && c <= 8303) ||
        c == 65279 ||
        (c >= 65520 && c <= 65535)) return '&#' + c + ';';
      return value; // avoids multilingual issues
    }).
    replace(/</g, '&lt;').
    replace(/>/g, '&gt;');
}

var trim = (function() {
  // native trim is way faster: http://jsperf.com/angular-trim-test
  // but IE doesn't have it... :-(
  // TODO: we should move this into IE/ES5 polyfill
  if (!String.prototype.trim) {
    return function(value) {
      return angular.isString(value) ? value.replace(/^\s\s*/, '').replace(/\s\s*$/, '') : value;
    };
  }
  return function(value) {
    return angular.isString(value) ? value.trim() : value;
  };
})();

// Custom logic for accepting certain style options only - textAngular
// Currently allows only the color, background-color, text-align, float, width and height attributes
// all other attributes should be easily done through classes.
function validStyles(styleAttr){
	var result = '';
	var styleArray = styleAttr.split(';');
	angular.forEach(styleArray, function(value){
		var v = value.split(':');
		if(v.length == 2){
			var key = trim(angular.lowercase(v[0]));
			var value = trim(angular.lowercase(v[1]));
			if(
				(key === 'color' || key === 'background-color') && (
					value.match(/^rgb\([0-9%,\. ]*\)$/i)
					|| value.match(/^rgba\([0-9%,\. ]*\)$/i)
					|| value.match(/^hsl\([0-9%,\. ]*\)$/i)
					|| value.match(/^hsla\([0-9%,\. ]*\)$/i)
					|| value.match(/^#[0-9a-f]{3,6}$/i)
					|| value.match(/^[a-z]*$/i)
				)
			||
				key === 'text-align' && (
					value === 'left'
					|| value === 'right'
					|| value === 'center'
					|| value === 'justify'
				)
			||
                key === 'text-decoration' && (
                    value === 'underline'
                    || value === 'line-through'
                )
            || key === 'font-weight' && (
                    value === 'bold'
                )
            ||
				key === 'float' && (
					value === 'left'
					|| value === 'right'
					|| value === 'none'
				)
			||
				(key === 'width' || key === 'height') && (
					value.match(/[0-9\.]*(px|em|rem|%)/)
				)
			|| // Reference #520
				(key === 'direction' && value.match(/^ltr|rtl|initial|inherit$/))
			) result += key + ': ' + value + ';';
		}
	});
	return result;
}

// this function is used to manually allow specific attributes on specific tags with certain prerequisites
function validCustomTag(tag, attrs, lkey, value){
	// catch the div placeholder for the iframe replacement
    if (tag === 'img' && attrs['ta-insert-video']){
        if(lkey === 'ta-insert-video' || lkey === 'allowfullscreen' || lkey === 'frameborder' || (lkey === 'contenteditable' && value === 'false')) return true;
    }
    return false;
}

/**
 * create an HTML/XML writer which writes to buffer
 * @param {Array} buf use buf.jain('') to get out sanitized html string
 * @returns {object} in the form of {
 *     start: function(tag, attrs, unary) {},
 *     end: function(tag) {},
 *     chars: function(text) {},
 *     comment: function(text) {}
 * }
 */
function htmlSanitizeWriter(buf, uriValidator) {
  var ignore = false;
  var out = angular.bind(buf, buf.push);
  return {
    start: function(tag, attrs, unary) {
      tag = angular.lowercase(tag);
      if (!ignore && specialElements[tag]) {
        ignore = tag;
      }
      if (!ignore && validElements[tag] === true) {
        out('<');
        out(tag);
        angular.forEach(attrs, function(value, key) {
          var lkey=angular.lowercase(key);
          var isImage=(tag === 'img' && lkey === 'src') || (lkey === 'background');
          if ((lkey === 'style' && (value = validStyles(value)) !== '') || validCustomTag(tag, attrs, lkey, value) || validAttrs[lkey] === true &&
            (uriAttrs[lkey] !== true || uriValidator(value, isImage))) {
            out(' ');
            out(key);
            out('="');
            out(encodeEntities(value));
            out('"');
          }
        });
        out(unary ? '/>' : '>');
      }
    },
    comment: function (com) {
      out(com);
    },
    whitespace: function (ws) {
      out(encodeEntities(ws));
    },
    end: function(tag) {
        tag = angular.lowercase(tag);
        if (!ignore && validElements[tag] === true) {
          out('</');
          out(tag);
          out('>');
        }
        if (tag == ignore) {
          ignore = false;
        }
      },
    chars: function(chars) {
        if (!ignore) {
          out(encodeEntities(chars));
        }
      }
  };
}


// define ngSanitize module and register $sanitize service
angular.module('ngSanitize', []).provider('$sanitize', $SanitizeProvider);

/* global sanitizeText: false */

/**
 * @ngdoc filter
 * @name linky
 * @kind function
 *
 * @description
 * Finds links in text input and turns them into html links. Supports http/https/ftp/mailto and
 * plain email address links.
 *
 * Requires the {@link ngSanitize `ngSanitize`} module to be installed.
 *
 * @param {string} text Input text.
 * @param {string} target Window (_blank|_self|_parent|_top) or named frame to open links in.
 * @returns {string} Html-linkified text.
 *
 * @usage
   <span ng-bind-html="linky_expression | linky"></span>
 *
 * @example
   <example module="linkyExample" deps="angular-sanitize.js">
     <file name="index.html">
       <script>
         angular.module('linkyExample', ['ngSanitize'])
           .controller('ExampleController', ['$scope', function($scope) {
             $scope.snippet =
               'Pretty text with some links:\n'+
               'http://angularjs.org/,\n'+
               'mailto:us@somewhere.org,\n'+
               'another@somewhere.org,\n'+
               'and one more: ftp://127.0.0.1/.';
             $scope.snippetWithTarget = 'http://angularjs.org/';
           }]);
       </script>
       <div ng-controller="ExampleController">
       Snippet: <textarea ng-model="snippet" cols="60" rows="3"></textarea>
       <table>
         <tr>
           <td>Filter</td>
           <td>Source</td>
           <td>Rendered</td>
         </tr>
         <tr id="linky-filter">
           <td>linky filter</td>
           <td>
             <pre>&lt;div ng-bind-html="snippet | linky"&gt;<br>&lt;/div&gt;</pre>
           </td>
           <td>
             <div ng-bind-html="snippet | linky"></div>
           </td>
         </tr>
         <tr id="linky-target">
          <td>linky target</td>
          <td>
            <pre>&lt;div ng-bind-html="snippetWithTarget | linky:'_blank'"&gt;<br>&lt;/div&gt;</pre>
          </td>
          <td>
            <div ng-bind-html="snippetWithTarget | linky:'_blank'"></div>
          </td>
         </tr>
         <tr id="escaped-html">
           <td>no filter</td>
           <td><pre>&lt;div ng-bind="snippet"&gt;<br>&lt;/div&gt;</pre></td>
           <td><div ng-bind="snippet"></div></td>
         </tr>
       </table>
     </file>
     <file name="protractor.js" type="protractor">
       it('should linkify the snippet with urls', function() {
         expect(element(by.id('linky-filter')).element(by.binding('snippet | linky')).getText()).
             toBe('Pretty text with some links: http://angularjs.org/, us@somewhere.org, ' +
                  'another@somewhere.org, and one more: ftp://127.0.0.1/.');
         expect(element.all(by.css('#linky-filter a')).count()).toEqual(4);
       });

       it('should not linkify snippet without the linky filter', function() {
         expect(element(by.id('escaped-html')).element(by.binding('snippet')).getText()).
             toBe('Pretty text with some links: http://angularjs.org/, mailto:us@somewhere.org, ' +
                  'another@somewhere.org, and one more: ftp://127.0.0.1/.');
         expect(element.all(by.css('#escaped-html a')).count()).toEqual(0);
       });

       it('should update', function() {
         element(by.model('snippet')).clear();
         element(by.model('snippet')).sendKeys('new http://link.');
         expect(element(by.id('linky-filter')).element(by.binding('snippet | linky')).getText()).
             toBe('new http://link.');
         expect(element.all(by.css('#linky-filter a')).count()).toEqual(1);
         expect(element(by.id('escaped-html')).element(by.binding('snippet')).getText())
             .toBe('new http://link.');
       });

       it('should work with the target property', function() {
        expect(element(by.id('linky-target')).
            element(by.binding("snippetWithTarget | linky:'_blank'")).getText()).
            toBe('http://angularjs.org/');
        expect(element(by.css('#linky-target a')).getAttribute('target')).toEqual('_blank');
       });
     </file>
   </example>
 */
angular.module('ngSanitize').filter('linky', ['$sanitize', function($sanitize) {
  var LINKY_URL_REGEXP =
        /((ftp|https?):\/\/|(www\.)|(mailto:)?[A-Za-z0-9._%+-]+@)\S*[^\s.;,(){}<>"”’]/,
      MAILTO_REGEXP = /^mailto:/;

  return function(text, target) {
    if (!text) return text;
    var match;
    var raw = text;
    var html = [];
    var url;
    var i;
    while ((match = raw.match(LINKY_URL_REGEXP))) {
      // We can not end in these as they are sometimes found at the end of the sentence
      url = match[0];
      // if we did not match ftp/http/www/mailto then assume mailto
      if (!match[2] && !match[4]) {
        url = (match[3] ? 'http://' : 'mailto:') + url;
      }
      i = match.index;
      addText(raw.substr(0, i));
      addLink(url, match[0].replace(MAILTO_REGEXP, ''));
      raw = raw.substring(i + match[0].length);
    }
    addText(raw);
    return $sanitize(html.join(''));

    function addText(text) {
      if (!text) {
        return;
      }
      html.push(sanitizeText(text));
    }

    function addLink(url, text) {
      html.push('<a ');
      if (angular.isDefined(target)) {
        html.push('target="',
                  target,
                  '" ');
      }
      html.push('href="',
                url.replace(/"/g, '&quot;'),
                '">');
      addText(text);
      html.push('</a>');
    }
  };
}]);


})(window, window.angular);


// tests against the current jqLite/jquery implementation if this can be an element
function validElementString(string){
	try{
		return angular.element(string).length !== 0;
	}catch(any){
		return false;
	}
}
// setup the global contstant functions for setting up the toolbar

// all tool definitions
var taTools = {};
/*
	A tool definition is an object with the following key/value parameters:
		action: [function(deferred, restoreSelection)]
				a function that is executed on clicking on the button - this will allways be executed using ng-click and will
				overwrite any ng-click value in the display attribute.
				The function is passed a deferred object ($q.defer()), if this is wanted to be used `return false;` from the action and
				manually call `deferred.resolve();` elsewhere to notify the editor that the action has finished.
				restoreSelection is only defined if the rangy library is included and it can be called as `restoreSelection()` to restore the users
				selection in the WYSIWYG editor.
		display: [string]?
				Optional, an HTML element to be displayed as the button. The `scope` of the button is the tool definition object with some additional functions
				If set this will cause buttontext and iconclass to be ignored
		class: [string]?
				Optional, if set will override the taOptions.classes.toolbarButton class.
		buttontext: [string]?
				if this is defined it will replace the contents of the element contained in the `display` element
		iconclass: [string]?
				if this is defined an icon (<i>) will be appended to the `display` element with this string as it's class
		tooltiptext: [string]?
				Optional, a plain text description of the action, used for the title attribute of the action button in the toolbar by default.
		activestate: [function(commonElement)]?
				this function is called on every caret movement, if it returns true then the class taOptions.classes.toolbarButtonActive
				will be applied to the `display` element, else the class will be removed
		disabled: [function()]?
				if this function returns true then the tool will have the class taOptions.classes.disabled applied to it, else it will be removed
	Other functions available on the scope are:
		name: [string]
				the name of the tool, this is the first parameter passed into taRegisterTool
		isDisabled: [function()]
				returns true if the tool is disabled, false if it isn't
		displayActiveToolClass: [function(boolean)]
				returns true if the tool is 'active' in the currently focussed toolbar
		onElementSelect: [Object]
				This object contains the following key/value pairs and is used to trigger the ta-element-select event
				element: [String]
					an element name, will only trigger the onElementSelect action if the tagName of the element matches this string
				filter: [function(element)]?
					an optional filter that returns a boolean, if true it will trigger the onElementSelect.
				action: [function(event, element, editorScope)]
					the action that should be executed if the onElementSelect function runs
*/
// name and toolDefinition to add into the tools available to be added on the toolbar
function registerTextAngularTool(name, toolDefinition){
	if(!name || name === '' || taTools.hasOwnProperty(name)) throw('textAngular Error: A unique name is required for a Tool Definition');
	if(
		(toolDefinition.display && (toolDefinition.display === '' || !validElementString(toolDefinition.display))) ||
		(!toolDefinition.display && !toolDefinition.buttontext && !toolDefinition.iconclass)
	)
		throw('textAngular Error: Tool Definition for "' + name + '" does not have a valid display/iconclass/buttontext value');
	taTools[name] = toolDefinition;
}

angular.module('textAngularSetup', [])
.constant('taRegisterTool', registerTextAngularTool)
.value('taTools', taTools)
// Here we set up the global display defaults, to set your own use a angular $provider#decorator.
.value('taOptions',  {
	//////////////////////////////////////////////////////////////////////////////////////
    // forceTextAngularSanitize
    // set false to allow the textAngular-sanitize provider to be replaced
    // with angular-sanitize or a custom provider.
	forceTextAngularSanitize: true,
	///////////////////////////////////////////////////////////////////////////////////////
	// keyMappings
	// allow customizable keyMappings for specialized key boards or languages
	//
	// keyMappings provides key mappings that are attached to a given commandKeyCode.
	// To modify a specific keyboard binding, simply provide function which returns true
	// for the event you wish to map to.
	// Or to disable a specific keyboard binding, provide a function which returns false.
	// Note: 'RedoKey' and 'UndoKey' are internally bound to the redo and undo functionality.
	// At present, the following commandKeyCodes are in use:
	// 98, 'TabKey', 'ShiftTabKey', 105, 117, 'UndoKey', 'RedoKey'
	//
	// To map to an new commandKeyCode, add a new key mapping such as:
	// {commandKeyCode: 'CustomKey', testForKey: function (event) {
	//  if (event.keyCode=57 && event.ctrlKey && !event.shiftKey && !event.altKey) return true;
	// } }
	// to the keyMappings. This example maps ctrl+9 to 'CustomKey'
	// Then where taRegisterTool(...) is called, add a commandKeyCode: 'CustomKey' and your
	// tool will be bound to ctrl+9.
	//
	// To disble one of the already bound commandKeyCodes such as 'RedoKey' or 'UndoKey' add:
	// {commandKeyCode: 'RedoKey', testForKey: function (event) { return false; } },
	// {commandKeyCode: 'UndoKey', testForKey: function (event) { return false; } },
	// to disable them.
	//
	keyMappings : [],
	toolbar: [
		['h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'p', 'pre', 'quote'],
		['bold', 'italics', 'underline', 'strikeThrough', 'ul', 'ol', 'redo', 'undo', 'clear'],
		['justifyLeft','justifyCenter','justifyRight','justifyFull','indent','outdent'],
		['html', 'insertImage', 'insertLink', 'insertVideo', 'wordcount', 'charcount']
	],
	classes: {
		focussed: "focussed",
		toolbar: "btn-toolbar",
		toolbarGroup: "btn-group",
		toolbarButton: "btn btn-default",
		toolbarButtonActive: "active",
		disabled: "disabled",
		textEditor: 'form-control',
		htmlEditor: 'form-control'
	},
	defaultTagAttributes : {
		a: {target:""}
	},
	setup: {
		// wysiwyg mode
		textEditorSetup: function($element){ /* Do some processing here */ },
		// raw html
		htmlEditorSetup: function($element){ /* Do some processing here */ }
	},
	defaultFileDropHandler:
		/* istanbul ignore next: untestable image processing */
		function(file, insertAction){
			var reader = new FileReader();
			if(file.type.substring(0, 5) === 'image'){
				reader.onload = function() {
					if(reader.result !== '') insertAction('insertImage', reader.result, true);
				};

				reader.readAsDataURL(file);
				// NOTE: For async procedures return a promise and resolve it when the editor should update the model.
				return true;
			}
			return false;
		}
})

// This is the element selector string that is used to catch click events within a taBind, prevents the default and $emits a 'ta-element-select' event
// these are individually used in an angular.element().find() call. What can go here depends on whether you have full jQuery loaded or just jQLite with angularjs.
// div is only used as div.ta-insert-video caught in filter.
.value('taSelectableElements', ['a','img'])

// This is an array of objects with the following options:
//				selector: <string> a jqLite or jQuery selector string
//				customAttribute: <string> an attribute to search for
//				renderLogic: <function(element)>
// Both or one of selector and customAttribute must be defined.
.value('taCustomRenderers', [
	{
		// Parse back out: '<div class="ta-insert-video" ta-insert-video src="' + urlLink + '" allowfullscreen="true" width="300" frameborder="0" height="250"></div>'
		// To correct video element. For now only support youtube
		selector: 'img',
		customAttribute: 'ta-insert-video',
		renderLogic: function(element){
			var iframe = angular.element('<iframe></iframe>');
			var attributes = element.prop("attributes");
			// loop through element attributes and apply them on iframe
			angular.forEach(attributes, function(attr) {
				iframe.attr(attr.name, attr.value);
			});
			iframe.attr('src', iframe.attr('ta-insert-video'));
			element.replaceWith(iframe);
		}
	}
])

.value('taTranslations', {
	// moved to sub-elements
	//toggleHTML: "Toggle HTML",
	//insertImage: "Please enter a image URL to insert",
	//insertLink: "Please enter a URL to insert",
	//insertVideo: "Please enter a youtube URL to embed",
	html: {
		tooltip: 'Toggle html / Rich Text'
	},
	// tooltip for heading - might be worth splitting
	heading: {
		tooltip: 'Heading '
	},
	p: {
		tooltip: 'Paragraph'
	},
	pre: {
		tooltip: 'Preformatted text'
	},
	ul: {
		tooltip: 'Unordered List'
	},
	ol: {
		tooltip: 'Ordered List'
	},
	quote: {
		tooltip: 'Quote/unquote selection or paragraph'
	},
	undo: {
		tooltip: 'Undo'
	},
	redo: {
		tooltip: 'Redo'
	},
	bold: {
		tooltip: 'Bold'
	},
	italic: {
		tooltip: 'Italic'
	},
	underline: {
		tooltip: 'Underline'
	},
	strikeThrough:{
		tooltip: 'Strikethrough'
	},
	justifyLeft: {
		tooltip: 'Align text left'
	},
	justifyRight: {
		tooltip: 'Align text right'
	},
	justifyFull: {
		tooltip: 'Justify text'
	},
	justifyCenter: {
		tooltip: 'Center'
	},
	indent: {
		tooltip: 'Increase indent'
	},
	outdent: {
		tooltip: 'Decrease indent'
	},
	clear: {
		tooltip: 'Clear formatting'
	},
	insertImage: {
		dialogPrompt: 'Please enter an image URL to insert',
		tooltip: 'Insert image',
		hotkey: 'the - possibly language dependent hotkey ... for some future implementation'
	},
	insertVideo: {
		tooltip: 'Insert video',
		dialogPrompt: 'Please enter a youtube URL to embed'
	},
	insertLink: {
		tooltip: 'Insert / edit link',
		dialogPrompt: "Please enter a URL to insert"
	},
	editLink: {
		reLinkButton: {
			tooltip: "Relink"
		},
		unLinkButton: {
			tooltip: "Unlink"
		},
		targetToggle: {
			buttontext: "Open in New Window"
		}
	},
	wordcount: {
		tooltip: 'Display words Count'
	},
		charcount: {
		tooltip: 'Display characters Count'
	}
})
.factory('taToolFunctions', ['$window','taTranslations', function($window, taTranslations) {
	return {
		imgOnSelectAction: function(event, $element, editorScope){
			// setup the editor toolbar
			// Credit to the work at http://hackerwins.github.io/summernote/ for this editbar logic/display
			var finishEdit = function(){
				editorScope.updateTaBindtaTextElement();
				editorScope.hidePopover();
			};
			event.preventDefault();
			editorScope.displayElements.popover.css('width', '375px');
			var container = editorScope.displayElements.popoverContainer;
			container.empty();
			var buttonGroup = angular.element('<div class="btn-group" style="padding-right: 6px;">');
			var fullButton = angular.element('<button type="button" class="btn btn-default btn-sm btn-small" unselectable="on" tabindex="-1">100% </button>');
			fullButton.on('click', function(event){
				event.preventDefault();
				$element.css({
					'width': '100%',
					'height': ''
				});
				finishEdit();
			});
			var halfButton = angular.element('<button type="button" class="btn btn-default btn-sm btn-small" unselectable="on" tabindex="-1">50% </button>');
			halfButton.on('click', function(event){
				event.preventDefault();
				$element.css({
					'width': '50%',
					'height': ''
				});
				finishEdit();
			});
			var quartButton = angular.element('<button type="button" class="btn btn-default btn-sm btn-small" unselectable="on" tabindex="-1">25% </button>');
			quartButton.on('click', function(event){
				event.preventDefault();
				$element.css({
					'width': '25%',
					'height': ''
				});
				finishEdit();
			});
			var resetButton = angular.element('<button type="button" class="btn btn-default btn-sm btn-small" unselectable="on" tabindex="-1">Reset</button>');
			resetButton.on('click', function(event){
				event.preventDefault();
				$element.css({
					width: '',
					height: ''
				});
				finishEdit();
			});
			buttonGroup.append(fullButton);
			buttonGroup.append(halfButton);
			buttonGroup.append(quartButton);
			buttonGroup.append(resetButton);
			container.append(buttonGroup);

			buttonGroup = angular.element('<div class="btn-group" style="padding-right: 6px;">');
			var floatLeft = angular.element('<button type="button" class="btn btn-default btn-sm btn-small" unselectable="on" tabindex="-1"><i class="fa fa-align-left"></i></button>');
			floatLeft.on('click', function(event){
				event.preventDefault();
				// webkit
				$element.css('float', 'left');
				// firefox
				$element.css('cssFloat', 'left');
				// IE < 8
				$element.css('styleFloat', 'left');
				finishEdit();
			});
			var floatRight = angular.element('<button type="button" class="btn btn-default btn-sm btn-small" unselectable="on" tabindex="-1"><i class="fa fa-align-right"></i></button>');
			floatRight.on('click', function(event){
				event.preventDefault();
				// webkit
				$element.css('float', 'right');
				// firefox
				$element.css('cssFloat', 'right');
				// IE < 8
				$element.css('styleFloat', 'right');
				finishEdit();
			});
			var floatNone = angular.element('<button type="button" class="btn btn-default btn-sm btn-small" unselectable="on" tabindex="-1"><i class="fa fa-align-justify"></i></button>');
			floatNone.on('click', function(event){
				event.preventDefault();
				// webkit
				$element.css('float', '');
				// firefox
				$element.css('cssFloat', '');
				// IE < 8
				$element.css('styleFloat', '');
				finishEdit();
			});
			buttonGroup.append(floatLeft);
			buttonGroup.append(floatNone);
			buttonGroup.append(floatRight);
			container.append(buttonGroup);

			buttonGroup = angular.element('<div class="btn-group">');
			var remove = angular.element('<button type="button" class="btn btn-default btn-sm btn-small" unselectable="on" tabindex="-1"><i class="fa fa-trash-o"></i></button>');
			remove.on('click', function(event){
				event.preventDefault();
				$element.remove();
				finishEdit();
			});
			buttonGroup.append(remove);
			container.append(buttonGroup);

			editorScope.showPopover($element);
			editorScope.showResizeOverlay($element);
		},
		aOnSelectAction: function(event, $element, editorScope){
			// setup the editor toolbar
			// Credit to the work at http://hackerwins.github.io/summernote/ for this editbar logic
			event.preventDefault();
			editorScope.displayElements.popover.css('width', '436px');
			var container = editorScope.displayElements.popoverContainer;
			container.empty();
			container.css('line-height', '28px');
			var link = angular.element('<a href="' + $element.attr('href') + '" target="_blank">' + $element.attr('href') + '</a>');
			link.css({
				'display': 'inline-block',
				'max-width': '200px',
				'overflow': 'hidden',
				'text-overflow': 'ellipsis',
				'white-space': 'nowrap',
				'vertical-align': 'middle'
			});
			container.append(link);
			var buttonGroup = angular.element('<div class="btn-group pull-right">');
			var reLinkButton = angular.element('<button type="button" class="btn btn-default btn-sm btn-small" tabindex="-1" unselectable="on" title="' + taTranslations.editLink.reLinkButton.tooltip + '"><i class="fa fa-edit icon-edit"></i></button>');
			reLinkButton.on('click', function(event){
				event.preventDefault();
				var urlLink = $window.prompt(taTranslations.insertLink.dialogPrompt, $element.attr('href'));
				if(urlLink && urlLink !== '' && urlLink !== 'http://'){
					$element.attr('href', urlLink);
					editorScope.updateTaBindtaTextElement();
				}
				editorScope.hidePopover();
			});
			buttonGroup.append(reLinkButton);
			var unLinkButton = angular.element('<button type="button" class="btn btn-default btn-sm btn-small" tabindex="-1" unselectable="on" title="' + taTranslations.editLink.unLinkButton.tooltip + '"><i class="fa fa-unlink icon-unlink"></i></button>');
			// directly before this click event is fired a digest is fired off whereby the reference to $element is orphaned off
			unLinkButton.on('click', function(event){
				event.preventDefault();
				$element.replaceWith($element.contents());
				editorScope.updateTaBindtaTextElement();
				editorScope.hidePopover();
			});
			buttonGroup.append(unLinkButton);
			var targetToggle = angular.element('<button type="button" class="btn btn-default btn-sm btn-small" tabindex="-1" unselectable="on">' + taTranslations.editLink.targetToggle.buttontext + '</button>');
			if($element.attr('target') === '_blank'){
				targetToggle.addClass('active');
			}
			targetToggle.on('click', function(event){
				event.preventDefault();
				$element.attr('target', ($element.attr('target') === '_blank') ? '' : '_blank');
				targetToggle.toggleClass('active');
				editorScope.updateTaBindtaTextElement();
			});
			buttonGroup.append(targetToggle);
			container.append(buttonGroup);
			editorScope.showPopover($element);
		},
		extractYoutubeVideoId: function(url) {
			var re = /(?:youtube(?:-nocookie)?\.com\/(?:[^\/\n\s]+\/\S+\/|(?:v|e(?:mbed)?)\/|\S*?[?&]v=)|youtu\.be\/)([a-zA-Z0-9_-]{11})/i;
			var match = url.match(re);
			return (match && match[1]) || null;
		}
	};
}])
.run(['taRegisterTool', '$window', 'taTranslations', 'taSelection', 'taToolFunctions', '$sanitize', 'taOptions', function(taRegisterTool, $window, taTranslations, taSelection, taToolFunctions, $sanitize, taOptions){
	// test for the version of $sanitize that is in use
	// You can disable this check by setting taOptions.textAngularSanitize == false
	var gv = {}; $sanitize('', gv);
	/* istanbul ignore next, throws error */
	if ((taOptions.forceTextAngularSanitize===true) && (gv.version !== 'taSanitize')) {
		throw angular.$$minErr('textAngular')("textAngularSetup", "The textAngular-sanitize provider has been replaced by another -- have you included angular-sanitize by mistake?");
	}
	taRegisterTool("html", {
		iconclass: 'fa fa-code',
		tooltiptext: taTranslations.html.tooltip,
		action: function(){
			this.$editor().switchView();
		},
		activeState: function(){
			return this.$editor().showHtml;
		}
	});
	// add the Header tools
	// convenience functions so that the loop works correctly
	var _retActiveStateFunction = function(q){
		return function(){ return this.$editor().queryFormatBlockState(q); };
	};
	var headerAction = function(){
		return this.$editor().wrapSelection("formatBlock", "<" + this.name.toUpperCase() +">");
	};
	angular.forEach(['h1','h2','h3','h4','h5','h6'], function(h){
		taRegisterTool(h.toLowerCase(), {
			buttontext: h.toUpperCase(),
			tooltiptext: taTranslations.heading.tooltip + h.charAt(1),
			action: headerAction,
			activeState: _retActiveStateFunction(h.toLowerCase())
		});
	});
	taRegisterTool('p', {
		buttontext: 'P',
		tooltiptext: taTranslations.p.tooltip,
		action: function(){
			return this.$editor().wrapSelection("formatBlock", "<P>");
		},
		activeState: function(){ return this.$editor().queryFormatBlockState('p'); }
	});
	// key: pre -> taTranslations[key].tooltip, taTranslations[key].buttontext
	taRegisterTool('pre', {
		buttontext: 'pre',
		tooltiptext: taTranslations.pre.tooltip,
		action: function(){
			return this.$editor().wrapSelection("formatBlock", "<PRE>");
		},
		activeState: function(){ return this.$editor().queryFormatBlockState('pre'); }
	});
	taRegisterTool('ul', {
		iconclass: 'fa fa-list-ul',
		tooltiptext: taTranslations.ul.tooltip,
		action: function(){
			return this.$editor().wrapSelection("insertUnorderedList", null);
		},
		activeState: function(){ return this.$editor().queryCommandState('insertUnorderedList'); }
	});
	taRegisterTool('ol', {
		iconclass: 'fa fa-list-ol',
		tooltiptext: taTranslations.ol.tooltip,
		action: function(){
			return this.$editor().wrapSelection("insertOrderedList", null);
		},
		activeState: function(){ return this.$editor().queryCommandState('insertOrderedList'); }
	});
	taRegisterTool('quote', {
		iconclass: 'fa fa-quote-right',
		tooltiptext: taTranslations.quote.tooltip,
		action: function(){
			return this.$editor().wrapSelection("formatBlock", "<BLOCKQUOTE>");
		},
		activeState: function(){ return this.$editor().queryFormatBlockState('blockquote'); }
	});
	taRegisterTool('undo', {
		iconclass: 'fa fa-undo',
		tooltiptext: taTranslations.undo.tooltip,
		action: function(){
			return this.$editor().wrapSelection("undo", null);
		}
	});
	taRegisterTool('redo', {
		iconclass: 'fa fa-repeat',
		tooltiptext: taTranslations.redo.tooltip,
		action: function(){
			return this.$editor().wrapSelection("redo", null);
		}
	});
	taRegisterTool('bold', {
		iconclass: 'fa fa-bold',
		tooltiptext: taTranslations.bold.tooltip,
		action: function(){
			return this.$editor().wrapSelection("bold", null);
		},
		activeState: function(){
			return this.$editor().queryCommandState('bold');
		},
		commandKeyCode: 98
	});
	taRegisterTool('justifyLeft', {
		iconclass: 'fa fa-align-left',
		tooltiptext: taTranslations.justifyLeft.tooltip,
		action: function(){
			return this.$editor().wrapSelection("justifyLeft", null);
		},
		activeState: function(commonElement){
			/* istanbul ignore next: */
			if (commonElement && commonElement.nodeName === '#document') return false;
			var result = false;
			if (commonElement)
				result =
					commonElement.css('text-align') === 'left' ||
					commonElement.attr('align') === 'left' ||
					(
						commonElement.css('text-align') !== 'right' &&
						commonElement.css('text-align') !== 'center' &&
						commonElement.css('text-align') !== 'justify' && !this.$editor().queryCommandState('justifyRight') && !this.$editor().queryCommandState('justifyCenter')
					) && !this.$editor().queryCommandState('justifyFull');
			result = result || this.$editor().queryCommandState('justifyLeft');
			return result;
		}
	});
	taRegisterTool('justifyRight', {
		iconclass: 'fa fa-align-right',
		tooltiptext: taTranslations.justifyRight.tooltip,
		action: function(){
			return this.$editor().wrapSelection("justifyRight", null);
		},
		activeState: function(commonElement){
			/* istanbul ignore next: */
			if (commonElement && commonElement.nodeName === '#document') return false;
			var result = false;
			if(commonElement) result = commonElement.css('text-align') === 'right';
			result = result || this.$editor().queryCommandState('justifyRight');
			return result;
		}
	});
	taRegisterTool('justifyFull', {
		iconclass: 'fa fa-align-justify',
		tooltiptext: taTranslations.justifyFull.tooltip,
		action: function(){
			return this.$editor().wrapSelection("justifyFull", null);
		},
		activeState: function(commonElement){
			var result = false;
			if(commonElement) result = commonElement.css('text-align') === 'justify';
			result = result || this.$editor().queryCommandState('justifyFull');
			return result;
		}
	});
	taRegisterTool('justifyCenter', {
		iconclass: 'fa fa-align-center',
		tooltiptext: taTranslations.justifyCenter.tooltip,
		action: function(){
			return this.$editor().wrapSelection("justifyCenter", null);
		},
		activeState: function(commonElement){
			/* istanbul ignore next: */
			if (commonElement && commonElement.nodeName === '#document') return false;
			var result = false;
			if(commonElement) result = commonElement.css('text-align') === 'center';
			result = result || this.$editor().queryCommandState('justifyCenter');
			return result;
		}
	});
	taRegisterTool('indent', {
		iconclass: 'fa fa-indent',
		tooltiptext: taTranslations.indent.tooltip,
		action: function(){
			return this.$editor().wrapSelection("indent", null);
		},
		activeState: function(){
			return this.$editor().queryFormatBlockState('blockquote');
		},
		commandKeyCode: 'TabKey'
	});
	taRegisterTool('outdent', {
		iconclass: 'fa fa-outdent',
		tooltiptext: taTranslations.outdent.tooltip,
		action: function(){
			return this.$editor().wrapSelection("outdent", null);
		},
		activeState: function(){
			return false;
		},
		commandKeyCode: 'ShiftTabKey'
	});
	taRegisterTool('italics', {
		iconclass: 'fa fa-italic',
		tooltiptext: taTranslations.italic.tooltip,
		action: function(){
			return this.$editor().wrapSelection("italic", null);
		},
		activeState: function(){
			return this.$editor().queryCommandState('italic');
		},
		commandKeyCode: 105
	});
	taRegisterTool('underline', {
		iconclass: 'fa fa-underline',
		tooltiptext: taTranslations.underline.tooltip,
		action: function(){
			return this.$editor().wrapSelection("underline", null);
		},
		activeState: function(){
			return this.$editor().queryCommandState('underline');
		},
		commandKeyCode: 117
	});
	taRegisterTool('strikeThrough', {
		iconclass: 'fa fa-strikethrough',
		tooltiptext: taTranslations.strikeThrough.tooltip,
		action: function(){
			return this.$editor().wrapSelection("strikeThrough", null);
		},
		activeState: function(){
			return document.queryCommandState('strikeThrough');
		}
	});
	taRegisterTool('clear', {
		iconclass: 'fa fa-ban',
		tooltiptext: taTranslations.clear.tooltip,
		action: function(deferred, restoreSelection){
			var i;
			this.$editor().wrapSelection("removeFormat", null);
			var possibleNodes = angular.element(taSelection.getSelectionElement());
			// remove lists
			var removeListElements = function(list){
				list = angular.element(list);
				var prevElement = list;
				angular.forEach(list.children(), function(liElem){
					var newElem = angular.element('<p></p>');
					newElem.html(angular.element(liElem).html());
					prevElement.after(newElem);
					prevElement = newElem;
				});
				list.remove();
			};
			angular.forEach(possibleNodes.find("ul"), removeListElements);
			angular.forEach(possibleNodes.find("ol"), removeListElements);
			if(possibleNodes[0].tagName.toLowerCase() === 'li'){
				var _list = possibleNodes[0].parentNode.childNodes;
				var _preLis = [], _postLis = [], _found = false;
				for(i = 0; i < _list.length; i++){
					if(_list[i] === possibleNodes[0]){
						_found = true;
					}else if(!_found) _preLis.push(_list[i]);
					else _postLis.push(_list[i]);
				}
				var _parent = angular.element(possibleNodes[0].parentNode);
				var newElem = angular.element('<p></p>');
				newElem.html(angular.element(possibleNodes[0]).html());
				if(_preLis.length === 0 || _postLis.length === 0){
					if(_postLis.length === 0) _parent.after(newElem);
					else _parent[0].parentNode.insertBefore(newElem[0], _parent[0]);

					if(_preLis.length === 0 && _postLis.length === 0) _parent.remove();
					else angular.element(possibleNodes[0]).remove();
				}else{
					var _firstList = angular.element('<'+_parent[0].tagName+'></'+_parent[0].tagName+'>');
					var _secondList = angular.element('<'+_parent[0].tagName+'></'+_parent[0].tagName+'>');
					for(i = 0; i < _preLis.length; i++) _firstList.append(angular.element(_preLis[i]));
					for(i = 0; i < _postLis.length; i++) _secondList.append(angular.element(_postLis[i]));
					_parent.after(_secondList);
					_parent.after(newElem);
					_parent.after(_firstList);
					_parent.remove();
				}
				taSelection.setSelectionToElementEnd(newElem[0]);
			}
			// clear out all class attributes. These do not seem to be cleared via removeFormat
			var $editor = this.$editor();
			var recursiveRemoveClass = function(node){
				node = angular.element(node);
				if(node[0] !== $editor.displayElements.text[0]) node.removeAttr('class');
				angular.forEach(node.children(), recursiveRemoveClass);
			};
			angular.forEach(possibleNodes, recursiveRemoveClass);
			// check if in list. If not in list then use formatBlock option
			if(possibleNodes[0].tagName.toLowerCase() !== 'li' &&
				possibleNodes[0].tagName.toLowerCase() !== 'ol' &&
				possibleNodes[0].tagName.toLowerCase() !== 'ul') this.$editor().wrapSelection("formatBlock", "default");
			restoreSelection();
		}
	});


	taRegisterTool('insertImage', {
		iconclass: 'fa fa-picture-o',
		tooltiptext: taTranslations.insertImage.tooltip,
		action: function(){
			var imageLink;
			imageLink = $window.prompt(taTranslations.insertImage.dialogPrompt, 'http://');
			if(imageLink && imageLink !== '' && imageLink !== 'http://'){
				return this.$editor().wrapSelection('insertImage', imageLink, true);
			}
		},
		onElementSelect: {
			element: 'img',
			action: taToolFunctions.imgOnSelectAction
		}
	});
	taRegisterTool('insertVideo', {
		iconclass: 'fa fa-youtube-play',
		tooltiptext: taTranslations.insertVideo.tooltip,
		action: function(){
			var urlPrompt;
			urlPrompt = $window.prompt(taTranslations.insertVideo.dialogPrompt, 'https://');
			if (urlPrompt && urlPrompt !== '' && urlPrompt !== 'https://') {

				videoId = taToolFunctions.extractYoutubeVideoId(urlPrompt);

				/* istanbul ignore else: if it's invalid don't worry - though probably should show some kind of error message */
				if(videoId){
					// create the embed link
					var urlLink = "https://www.youtube.com/embed/" + videoId;
					// create the HTML
					// for all options see: http://stackoverflow.com/questions/2068344/how-do-i-get-a-youtube-video-thumbnail-from-the-youtube-api
					// maxresdefault.jpg seems to be undefined on some.
					var embed = '<img class="ta-insert-video" src="https://img.youtube.com/vi/' + videoId + '/hqdefault.jpg" ta-insert-video="' + urlLink + '" contenteditable="false" allowfullscreen="true" frameborder="0" />';
					// insert
					return this.$editor().wrapSelection('insertHTML', embed, true);
				}
			}
		},
		onElementSelect: {
			element: 'img',
			onlyWithAttrs: ['ta-insert-video'],
			action: taToolFunctions.imgOnSelectAction
		}
	});
	taRegisterTool('insertLink', {
		tooltiptext: taTranslations.insertLink.tooltip,
		iconclass: 'fa fa-link',
		action: function(){
			var urlLink;
			urlLink = $window.prompt(taTranslations.insertLink.dialogPrompt, 'http://');
			if(urlLink && urlLink !== '' && urlLink !== 'http://'){
				return this.$editor().wrapSelection('createLink', urlLink, true);
			}
		},
		activeState: function(commonElement){
			if(commonElement) return commonElement[0].tagName === 'A';
			return false;
		},
		onElementSelect: {
			element: 'a',
			action: taToolFunctions.aOnSelectAction
		}
	});
	taRegisterTool('wordcount', {
		display: '<div id="toolbarWC" style="display:block; min-width:100px;">Words: <span ng-bind="wordcount"></span></div>',
		disabled: true,
		wordcount: 0,
		activeState: function(){ // this fires on keyup
			var textElement = this.$editor().displayElements.text;
			/* istanbul ignore next: will default to '' when undefined */
			var workingHTML = textElement[0].innerHTML || '';
			var noOfWords = 0;

			/* istanbul ignore if: will default to '' when undefined */
			if (workingHTML.replace(/\s*<[^>]*?>\s*/g, '') !== '') {
				noOfWords = workingHTML.replace(/<\/?(b|i|em|strong|span|u|strikethrough|a|img|small|sub|sup|label)( [^>*?])?>/gi, '') // remove inline tags without adding spaces
										.replace(/(<[^>]*?>\s*<[^>]*?>)/ig, ' ') // replace adjacent tags with possible space between with a space
										.replace(/(<[^>]*?>)/ig, '') // remove any singular tags
										.replace(/\s+/ig, ' ') // condense spacing
										.match(/\S+/g).length; // count remaining non-space strings
			}

			//Set current scope
			this.wordcount = noOfWords;
			//Set editor scope
			this.$editor().wordcount = noOfWords;

			return false;
		}
	});
	taRegisterTool('charcount', {
		display: '<div id="toolbarCC" style="display:block; min-width:120px;">Characters: <span ng-bind="charcount"></span></div>',
		disabled: true,
		charcount: 0,
		activeState: function(){ // this fires on keyup
			var textElement = this.$editor().displayElements.text;
			var sourceText = textElement[0].innerText || textElement[0].textContent; // to cover the non-jquery use case.

			// Caculate number of chars
			var noOfChars = sourceText.replace(/(\r\n|\n|\r)/gm,"").replace(/^\s+/g,' ').replace(/\s+$/g, ' ').length;
			//Set current scope
			this.charcount = noOfChars;
			//Set editor scope
			this.$editor().charcount = noOfChars;
			return false;
		}
	});
}]);

/*
@license textAngular
Author : Austin Anderson
License : 2013 MIT
Version 1.5.0

See README.md or https://github.com/fraywing/textAngular/wiki for requirements and use.
*/

/*
Commonjs package manager support (eg componentjs).
*/

/* istanbul ignore next:  */
'undefined'!=typeof module&&'undefined'!=typeof exports&&module.exports===exports&&(module.exports='textAngular');

(function(){ // encapsulate all variables so they don't become global vars
"use strict";
// IE version detection - http://stackoverflow.com/questions/4169160/javascript-ie-detection-why-not-use-simple-conditional-comments
// We need this as IE sometimes plays funny tricks with the contenteditable.
// ----------------------------------------------------------
// If you're not in IE (or IE version is less than 5) then:
// ie === undefined
// If you're in IE (>=5) then you can determine which version:
// ie === 7; // IE7
// Thus, to detect IE:
// if (ie) {}
// And to detect the version:
// ie === 6 // IE6
// ie > 7 // IE8, IE9, IE10 ...
// ie < 9 // Anything less than IE9
// ----------------------------------------------------------
/* istanbul ignore next: untestable browser check */
var _browserDetect = {
	ie: (function(){
		var undef,
			v = 3,
			div = document.createElement('div'),
			all = div.getElementsByTagName('i');

		while (
			div.innerHTML = '<!--[if gt IE ' + (++v) + ']><i></i><![endif]-->',
			all[0]
		);

		return v > 4 ? v : undef;
	}()),
	webkit: /AppleWebKit\/([\d.]+)/i.test(navigator.userAgent)
};

// fix a webkit bug, see: https://gist.github.com/shimondoodkin/1081133
// this is set true when a blur occurs as the blur of the ta-bind triggers before the click
var globalContentEditableBlur = false;
/* istanbul ignore next: Browser Un-Focus fix for webkit */
if(_browserDetect.webkit) {
	document.addEventListener("mousedown", function(_event){
		var e = _event || window.event;
		var curelement = e.target;
		if(globalContentEditableBlur && curelement !== null){
			var isEditable = false;
			var tempEl = curelement;
			while(tempEl !== null && tempEl.tagName.toLowerCase() !== 'html' && !isEditable){
				isEditable = tempEl.contentEditable === 'true';
				tempEl = tempEl.parentNode;
			}
			if(!isEditable){
				document.getElementById('textAngular-editableFix-010203040506070809').setSelectionRange(0, 0); // set caret focus to an element that handles caret focus correctly.
				curelement.focus(); // focus the wanted element.
				if (curelement.select) {
					curelement.select(); // use select to place cursor for input elements.
				}
			}
		}
		globalContentEditableBlur = false;
	}, false); // add global click handler
	angular.element(document).ready(function () {
		angular.element(document.body).append(angular.element('<input id="textAngular-editableFix-010203040506070809" class="ta-hidden-input" aria-hidden="true" unselectable="on" tabIndex="-1">'));
	});
}

// Gloabl to textAngular REGEXP vars for block and list elements.

var BLOCKELEMENTS = /^(address|article|aside|audio|blockquote|canvas|dd|div|dl|fieldset|figcaption|figure|footer|form|h1|h2|h3|h4|h5|h6|header|hgroup|hr|noscript|ol|output|p|pre|section|table|tfoot|ul|video)$/i;
var LISTELEMENTS = /^(ul|li|ol)$/i;
var VALIDELEMENTS = /^(address|article|aside|audio|blockquote|canvas|dd|div|dl|fieldset|figcaption|figure|footer|form|h1|h2|h3|h4|h5|h6|header|hgroup|hr|noscript|ol|output|p|pre|section|table|tfoot|ul|video|li)$/i;

// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/Trim#Compatibility
/* istanbul ignore next: trim shim for older browsers */
if (!String.prototype.trim) {
	String.prototype.trim = function () {
		return this.replace(/^\s+|\s+$/g, '');
	};
}

/*
	Custom stylesheet for the placeholders rules.
	Credit to: http://davidwalsh.name/add-rules-stylesheets
*/
var sheet, addCSSRule, removeCSSRule, _addCSSRule, _removeCSSRule, _getRuleIndex;
/* istanbul ignore else: IE <8 test*/
if(_browserDetect.ie > 8 || _browserDetect.ie === undefined){
	var _sheets = document.styleSheets;
	/* istanbul ignore next: preference for stylesheet loaded externally */
	for(var i = 0; i < _sheets.length; i++){
		if(_sheets[i].media.length === 0 || _sheets[i].media.mediaText.match(/(all|screen)/ig)){
			if(_sheets[i].href){
				if(_sheets[i].href.match(/textangular\.(min\.|)css/ig)){
					sheet = _sheets[i];
					break;
				}
			}
		}
	}
	/* istanbul ignore next: preference for stylesheet loaded externally */
	if(!sheet){
		// this sheet is used for the placeholders later on.
		sheet = (function() {
			// Create the <style> tag
			var style = document.createElement("style");
			/* istanbul ignore else : WebKit hack :( */
			if(_browserDetect.webkit) style.appendChild(document.createTextNode(""));

			// Add the <style> element to the page, add as first so the styles can be overridden by custom stylesheets
			document.getElementsByTagName('head')[0].appendChild(style);

			return style.sheet;
		})();
	}

	// use as: addCSSRule("header", "float: left");
	addCSSRule = function(selector, rules) {
		return _addCSSRule(sheet, selector, rules);
	};
	_addCSSRule = function(_sheet, selector, rules){
		var insertIndex;
		var insertedRule;
		// This order is important as IE 11 has both cssRules and rules but they have different lengths - cssRules is correct, rules gives an error in IE 11
		/* istanbul ignore next: browser catches */
		if(_sheet.cssRules) insertIndex = Math.max(_sheet.cssRules.length - 1, 0);
		else if(_sheet.rules) insertIndex = Math.max(_sheet.rules.length - 1, 0);

		/* istanbul ignore else: untestable IE option */
		if(_sheet.insertRule) {
			_sheet.insertRule(selector + "{" + rules + "}", insertIndex);
		}
		else {
			_sheet.addRule(selector, rules, insertIndex);
		}
		/* istanbul ignore next: browser catches */
		if(sheet.rules) insertedRule = sheet.rules[insertIndex];
		else if(sheet.cssRules) insertedRule = sheet.cssRules[insertIndex];
		// return the inserted stylesheet rule
		return insertedRule;
	};

	_getRuleIndex = function(rule, rules) {
		var i, ruleIndex;
		for (i=0; i < rules.length; i++) {
			/* istanbul ignore else: check for correct rule */
			if (rules[i].cssText === rule.cssText) {
				ruleIndex = i;
				break;
			}
		}
		return ruleIndex;
	};

	removeCSSRule = function(rule){
		_removeCSSRule(sheet, rule);
	};
	/* istanbul ignore next: tests are browser specific */
	_removeCSSRule = function(sheet, rule){
		var rules = sheet.cssRules || sheet.rules;
		if(!rules || rules.length === 0) return;
		var ruleIndex = _getRuleIndex(rule, rules);
		if(sheet.removeRule){
			sheet.removeRule(ruleIndex);
		}else{
			sheet.deleteRule(ruleIndex);
		}
	};
}

angular.module('textAngular.factories', [])
.factory('taBrowserTag', [function(){
	return function(tag){
		/* istanbul ignore next: ie specific test */
		if(!tag) return (_browserDetect.ie <= 8)? 'P' : 'p';
		else if(tag === '') return (_browserDetect.ie === undefined)? 'div' : (_browserDetect.ie <= 8)? 'P' : 'p';
		else return (_browserDetect.ie <= 8)? tag.toUpperCase() : tag;
	};
}]).factory('taApplyCustomRenderers', ['taCustomRenderers', 'taDOM', function(taCustomRenderers, taDOM){
	return function(val){
		var element = angular.element('<div></div>');
		element[0].innerHTML = val;

		angular.forEach(taCustomRenderers, function(renderer){
			var elements = [];
			// get elements based on what is defined. If both defined do secondary filter in the forEach after using selector string
			if(renderer.selector && renderer.selector !== '')
				elements = element.find(renderer.selector);
			/* istanbul ignore else: shouldn't fire, if it does we're ignoring everything */
			else if(renderer.customAttribute && renderer.customAttribute !== '')
				elements = taDOM.getByAttribute(element, renderer.customAttribute);
			// process elements if any found
			angular.forEach(elements, function(_element){
				_element = angular.element(_element);
				if(renderer.selector && renderer.selector !== '' && renderer.customAttribute && renderer.customAttribute !== ''){
					if(_element.attr(renderer.customAttribute) !== undefined) renderer.renderLogic(_element);
				} else renderer.renderLogic(_element);
			});
		});

		return element[0].innerHTML;
	};
}]).factory('taFixChrome', function(){
	// get whaterever rubbish is inserted in chrome
	// should be passed an html string, returns an html string
	var taFixChrome = function(html){
		if(!html || !angular.isString(html) || html.length <= 0) return html;
		// grab all elements with a style attibute
		var spanMatch = /<([^>\/]+?)style=("([^"]+)"|'([^']+)')([^>]*)>/ig;
		var match, styleVal, newTag, finalHtml = '', lastIndex = 0;
		while(match = spanMatch.exec(html)){
			// one of the quoted values ' or "
			/* istanbul ignore next: quotations match */
			styleVal = match[3] || match[4];
			// test for chrome inserted junk
			if(styleVal && styleVal.match(/line-height: 1.[0-9]{3,12};|color: inherit; line-height: 1.1;/i)){
				// replace original tag with new tag
				styleVal = styleVal.replace(/( |)font-family: inherit;|( |)line-height: 1.[0-9]{3,12};|( |)color: inherit;/ig, '');
				newTag = '<' + match[1].trim();
				if(styleVal.trim().length > 0) newTag += ' style=' + match[2].substring(0,1) + styleVal + match[2].substring(0,1);
				newTag += match[5].trim() + ">";
				finalHtml += html.substring(lastIndex, match.index) + newTag;
				lastIndex = match.index + match[0].length;
			}
		}
		finalHtml += html.substring(lastIndex);
		// only replace when something has changed, else we get focus problems on inserting lists
		if(lastIndex > 0){
			// replace all empty strings
			return finalHtml.replace(/<span\s?>(.*?)<\/span>(<br(\/|)>|)/ig, '$1');
		} else return html;
	};
	return taFixChrome;
}).factory('taSanitize', ['$sanitize', function taSanitizeFactory($sanitize){

	var convert_infos = [
		{
			property: 'font-weight',
			values: [ 'bold' ],
			tag: 'b'
		},
		{
			property: 'font-style',
			values: [ 'italic' ],
			tag: 'i'
		}
	];
	
	var styleMatch = [];
	for(var i = 0; i < convert_infos.length; i++){
		var _partialStyle = '(' + convert_infos[i].property + ':\\s*(';
		for(var j = 0; j < convert_infos[i].values.length; j++){
			/* istanbul ignore next: not needed to be tested yet */
			if(j > 0) _partialStyle += '|';
			_partialStyle += convert_infos[i].values[j];
		}
		_partialStyle += ');)';
		styleMatch.push(_partialStyle);
	}
	var styleRegexString = '(' + styleMatch.join('|') + ')';
	
	function wrapNested(html, wrapTag) {
		var depth = 0;
		var lastIndex = 0;
		var match;
		var tagRegex = /<[^>]*>/ig;
		while(match = tagRegex.exec(html)){
			lastIndex = match.index;
			if(match[0].substr(1, 1) === '/'){
				if(depth === 0) break;
				else depth--;
			}else depth++;
		}
		return wrapTag +
			html.substring(0, lastIndex) +
			// get the start tags reversed - this is safe as we construct the strings with no content except the tags
			angular.element(wrapTag)[0].outerHTML.substring(wrapTag.length) +
			html.substring(lastIndex);
	}
	
	function transformLegacyStyles(html){
		if(!html || !angular.isString(html) || html.length <= 0) return html;
		var i;
		var styleElementMatch = /<([^>\/]+?)style=("([^"]+)"|'([^']+)')([^>]*)>/ig;
		var match, subMatch, styleVal, newTag, lastNewTag = '', newHtml, finalHtml = '', lastIndex = 0;
		while(match = styleElementMatch.exec(html)){
			// one of the quoted values ' or "
			/* istanbul ignore next: quotations match */
			styleVal = match[3] || match[4];
			var styleRegex = new RegExp(styleRegexString, 'i');
			// test for style values to change
			if(angular.isString(styleVal) && styleRegex.test(styleVal)){
				// remove build tag list
				newTag = '';
				// init regex here for exec
				var styleRegexExec = new RegExp(styleRegexString, 'ig');
				// find relevand tags and build a string of them
				while(subMatch = styleRegexExec.exec(styleVal)){
					for(i = 0; i < convert_infos.length; i++){
						if(!!subMatch[(i*2) + 2]){
							newTag += '<' + convert_infos[i].tag + '>';
						}
					}
				}
				// recursively find more legacy styles in html before this tag and after the previous match (if any)
				newHtml = transformLegacyStyles(html.substring(lastIndex, match.index));
				// build up html
				if(lastNewTag.length > 0){
					finalHtml += wrapNested(newHtml, lastNewTag);
				}else finalHtml += newHtml;
				// grab the style val without the transformed values
				styleVal = styleVal.replace(new RegExp(styleRegexString, 'ig'), '');
				// build the html tag
				finalHtml += '<' + match[1].trim();
				if(styleVal.length > 0) finalHtml += ' style="' + styleVal + '"';
				finalHtml += match[5] + '>';
				// update the start index to after this tag
				lastIndex = match.index + match[0].length;
				lastNewTag = newTag;
			}
		}
		if(lastNewTag.length > 0){
			finalHtml += wrapNested(html.substring(lastIndex), lastNewTag);
		}
		else finalHtml += html.substring(lastIndex);
		return finalHtml;
	}
	
	function transformLegacyAttributes(html){
		if(!html || !angular.isString(html) || html.length <= 0) return html;
		// replace all align='...' tags with text-align attributes
		var attrElementMatch = /<([^>\/]+?)align=("([^"]+)"|'([^']+)')([^>]*)>/ig;
		var match, finalHtml = '', lastIndex = 0;
		// match all attr tags
		while(match = attrElementMatch.exec(html)){
			// add all html before this tag
			finalHtml += html.substring(lastIndex, match.index);
			// record last index after this tag
			lastIndex = match.index + match[0].length;
			// construct tag without the align attribute
			var newTag = '<' + match[1] + match[5];
			// add the style attribute
			if(/style=("([^"]+)"|'([^']+)')/ig.test(newTag)){
				/* istanbul ignore next: quotations match */
				newTag = newTag.replace(/style=("([^"]+)"|'([^']+)')/i, 'style="$2$3 text-align:' + (match[3] || match[4]) + ';"');
			}else{
				/* istanbul ignore next: quotations match */
				newTag += ' style="text-align:' + (match[3] || match[4]) + ';"';
			}
			newTag += '>';
			// add to html
			finalHtml += newTag;
		}
		// return with remaining html
		return finalHtml + html.substring(lastIndex);
	}
	
	return function taSanitize(unsafe, oldsafe, ignore){
		// unsafe html should NEVER built into a DOM object via angular.element. This allows XSS to be inserted and run.
		if ( !ignore ) {
			try {
				unsafe = transformLegacyStyles(unsafe);
			} catch (e) {
			}
		}

		// unsafe and oldsafe should be valid HTML strings
		// any exceptions (lets say, color for example) should be made here but with great care
		// setup unsafe element for modification
		unsafe = transformLegacyAttributes(unsafe);
		
		var safe;
		try {
			safe = $sanitize(unsafe);
			// do this afterwards, then the $sanitizer should still throw for bad markup
			if(ignore) safe = unsafe;
		} catch (e){
			safe = oldsafe || '';
		}
		
		// Do processing for <pre> tags, removing tabs and return carriages outside of them
		
		var _preTags = safe.match(/(<pre[^>]*>.*?<\/pre[^>]*>)/ig);
		var processedSafe = safe.replace(/(&#(9|10);)*/ig, '');
		var re = /<pre[^>]*>.*?<\/pre[^>]*>/ig;
		var index = 0;
		var lastIndex = 0;
		var origTag;
		safe = '';
		while((origTag = re.exec(processedSafe)) !== null && index < _preTags.length){
			safe += processedSafe.substring(lastIndex, origTag.index) + _preTags[index];
			lastIndex = origTag.index + origTag[0].length;
			index++;
		}
		return safe + processedSafe.substring(lastIndex);
	};
}]).factory('taToolExecuteAction', ['$q', '$log', function($q, $log){
	// this must be called on a toolScope or instance
	return function(editor){
		if(editor !== undefined) this.$editor = function(){ return editor; };
		var deferred = $q.defer(),
			promise = deferred.promise,
			_editor = this.$editor();
		// pass into the action the deferred function and also the function to reload the current selection if rangy available
		var result;
		try{
			result = this.action(deferred, _editor.startAction());
			// We set the .finally callback here to make sure it doesn't get executed before any other .then callback.
			promise['finally'](function(){
				_editor.endAction.call(_editor);
			});
		}catch(exc){
			$log.error(exc);
		}
		if(result || result === undefined){
			// if true or undefined is returned then the action has finished. Otherwise the deferred action will be resolved manually.
			deferred.resolve();
		}
	};
}]);
angular.module('textAngular.DOM', ['textAngular.factories'])
.factory('taExecCommand', ['taSelection', 'taBrowserTag', '$document', function(taSelection, taBrowserTag, $document){
	var listToDefault = function(listElement, defaultWrap){
		var $target, i;
		// if all selected then we should remove the list
		// grab all li elements and convert to taDefaultWrap tags
		var children = listElement.find('li');
		for(i = children.length - 1; i >= 0; i--){
			$target = angular.element('<' + defaultWrap + '>' + children[i].innerHTML + '</' + defaultWrap + '>');
			listElement.after($target);
		}
		listElement.remove();
		taSelection.setSelectionToElementEnd($target[0]);
	};
	var selectLi = function(liElement){
		if(/(<br(|\/)>)$/i.test(liElement.innerHTML.trim())) taSelection.setSelectionBeforeElement(angular.element(liElement).find("br")[0]);
		else taSelection.setSelectionToElementEnd(liElement);
	};
	var listToList = function(listElement, newListTag){
		var $target = angular.element('<' + newListTag + '>' + listElement[0].innerHTML + '</' + newListTag + '>');
		listElement.after($target);
		listElement.remove();
		selectLi($target.find('li')[0]);
	};
	var childElementsToList = function(elements, listElement, newListTag){
		var html = '';
		for(var i = 0; i < elements.length; i++){
			html += '<' + taBrowserTag('li') + '>' + elements[i].innerHTML + '</' + taBrowserTag('li') + '>';
		}
		var $target = angular.element('<' + newListTag + '>' + html + '</' + newListTag + '>');
		listElement.after($target);
		listElement.remove();
		selectLi($target.find('li')[0]);
	};
	return function(taDefaultWrap, topNode){
		taDefaultWrap = taBrowserTag(taDefaultWrap);
		return function(command, showUI, options, defaultTagAttributes){
			var i, $target, html, _nodes, next, optionsTagName, selectedElement;
			var defaultWrapper = angular.element('<' + taDefaultWrap + '>');
			try{
				selectedElement = taSelection.getSelectionElement();
			}catch(e){}
			var $selected = angular.element(selectedElement);
			if(selectedElement !== undefined){
				var tagName = selectedElement.tagName.toLowerCase();
				if(command.toLowerCase() === 'insertorderedlist' || command.toLowerCase() === 'insertunorderedlist'){
					var selfTag = taBrowserTag((command.toLowerCase() === 'insertorderedlist')? 'ol' : 'ul');
					if(tagName === selfTag){
						// if all selected then we should remove the list
						// grab all li elements and convert to taDefaultWrap tags
						return listToDefault($selected, taDefaultWrap);
					}else if(tagName === 'li' && $selected.parent()[0].tagName.toLowerCase() === selfTag && $selected.parent().children().length === 1){
						// catch for the previous statement if only one li exists
						return listToDefault($selected.parent(), taDefaultWrap);
					}else if(tagName === 'li' && $selected.parent()[0].tagName.toLowerCase() !== selfTag && $selected.parent().children().length === 1){
						// catch for the previous statement if only one li exists
						return listToList($selected.parent(), selfTag);
					}else if(tagName.match(BLOCKELEMENTS) && !$selected.hasClass('ta-bind')){
						// if it's one of those block elements we have to change the contents
						// if it's a ol/ul we are changing from one to the other
						if(tagName === 'ol' || tagName === 'ul'){
							return listToList($selected, selfTag);
						}else{
							var childBlockElements = false;
							angular.forEach($selected.children(), function(elem){
								if(elem.tagName.match(BLOCKELEMENTS)) {
									childBlockElements = true;
								}
							});
							if(childBlockElements){
								return childElementsToList($selected.children(), $selected, selfTag);
							}else{
								return childElementsToList([angular.element('<div>' + selectedElement.innerHTML + '</div>')[0]], $selected, selfTag);
							}
						}
					}else if(tagName.match(BLOCKELEMENTS)){
						// if we get here then all the contents of the ta-bind are selected
						_nodes = taSelection.getOnlySelectedElements();
						if(_nodes.length === 0){
							// here is if there is only text in ta-bind ie <div ta-bind>test content</div>
							$target = angular.element('<' + selfTag + '><li>' + selectedElement.innerHTML + '</li></' + selfTag + '>');
							$selected.html('');
							$selected.append($target);
						}else if(_nodes.length === 1 && (_nodes[0].tagName.toLowerCase() === 'ol' || _nodes[0].tagName.toLowerCase() === 'ul')){
							if(_nodes[0].tagName.toLowerCase() === selfTag){
								// remove
								return listToDefault(angular.element(_nodes[0]), taDefaultWrap);
							}else{
								return listToList(angular.element(_nodes[0]), selfTag);
							}
						}else{
							html = '';
							var $nodes = [];
							for(i = 0; i < _nodes.length; i++){
								/* istanbul ignore else: catch for real-world can't make it occur in testing */
								if(_nodes[i].nodeType !== 3){
									var $n = angular.element(_nodes[i]);
									/* istanbul ignore if: browser check only, phantomjs doesn't return children nodes but chrome at least does */
									if(_nodes[i].tagName.toLowerCase() === 'li') continue;
									else if(_nodes[i].tagName.toLowerCase() === 'ol' || _nodes[i].tagName.toLowerCase() === 'ul'){
										html += $n[0].innerHTML; // if it's a list, add all it's children
									}else if(_nodes[i].tagName.toLowerCase() === 'span' && (_nodes[i].childNodes[0].tagName.toLowerCase() === 'ol' || _nodes[i].childNodes[0].tagName.toLowerCase() === 'ul')){
										html += $n[0].childNodes[0].innerHTML; // if it's a list, add all it's children
									}else{
										html += '<' + taBrowserTag('li') + '>' + $n[0].innerHTML + '</' + taBrowserTag('li') + '>';
									}
									$nodes.unshift($n);
								}
							}
							$target = angular.element('<' + selfTag + '>' + html + '</' + selfTag + '>');
							$nodes.pop().replaceWith($target);
							angular.forEach($nodes, function($node){ $node.remove(); });
						}
						taSelection.setSelectionToElementEnd($target[0]);
						return;
					}
				}else if(command.toLowerCase() === 'formatblock'){
					optionsTagName = options.toLowerCase().replace(/[<>]/ig, '');
					if(optionsTagName.trim() === 'default') {
						optionsTagName = taDefaultWrap;
						options = '<' + taDefaultWrap + '>';
					}
					if(tagName === 'li') $target = $selected.parent();
					else $target = $selected;
					// find the first blockElement
					while(!$target[0].tagName || !$target[0].tagName.match(BLOCKELEMENTS) && !$target.parent().attr('contenteditable')){
						$target = $target.parent();
						/* istanbul ignore next */
						tagName = ($target[0].tagName || '').toLowerCase();
					}
					if(tagName === optionsTagName){
						// $target is wrap element
						_nodes = $target.children();
						var hasBlock = false;
						for(i = 0; i < _nodes.length; i++){
							hasBlock = hasBlock || _nodes[i].tagName.match(BLOCKELEMENTS);
						}
						if(hasBlock){
							$target.after(_nodes);
							next = $target.next();
							$target.remove();
							$target = next;
						}else{
							defaultWrapper.append($target[0].childNodes);
							$target.after(defaultWrapper);
							$target.remove();
							$target = defaultWrapper;
						}
					}else if($target.parent()[0].tagName.toLowerCase() === optionsTagName && !$target.parent().hasClass('ta-bind')){
						//unwrap logic for parent
						var blockElement = $target.parent();
						var contents = blockElement.contents();
						for(i = 0; i < contents.length; i ++){
							/* istanbul ignore next: can't test - some wierd thing with how phantomjs works */
							if(blockElement.parent().hasClass('ta-bind') && contents[i].nodeType === 3){
								defaultWrapper = angular.element('<' + taDefaultWrap + '>');
								defaultWrapper[0].innerHTML = contents[i].outerHTML;
								contents[i] = defaultWrapper[0];
							}
							blockElement.parent()[0].insertBefore(contents[i], blockElement[0]);
						}
						blockElement.remove();
					}else if(tagName.match(LISTELEMENTS)){
						// wrapping a list element
						$target.wrap(options);
					}else{
						// default wrap behaviour
						_nodes = taSelection.getOnlySelectedElements();
						if(_nodes.length === 0) _nodes = [$target[0]];
						// find the parent block element if any of the nodes are inline or text
						for(i = 0; i < _nodes.length; i++){
							if(_nodes[i].nodeType === 3 || !_nodes[i].tagName.match(BLOCKELEMENTS)){
								while(_nodes[i].nodeType === 3 || !_nodes[i].tagName || !_nodes[i].tagName.match(BLOCKELEMENTS)){
									_nodes[i] = _nodes[i].parentNode;
								}
							}
						}
						if(angular.element(_nodes[0]).hasClass('ta-bind')){
							$target = angular.element(options);
							$target[0].innerHTML = _nodes[0].innerHTML;
							_nodes[0].innerHTML = $target[0].outerHTML;
						}else if(optionsTagName === 'blockquote'){
							// blockquotes wrap other block elements
							html = '';
							for(i = 0; i < _nodes.length; i++){
								html += _nodes[i].outerHTML;
							}
							$target = angular.element(options);
							$target[0].innerHTML = html;
							_nodes[0].parentNode.insertBefore($target[0],_nodes[0]);
							for(i = _nodes.length - 1; i >= 0; i--){
								/* istanbul ignore else:  */
								if(_nodes[i].parentNode) _nodes[i].parentNode.removeChild(_nodes[i]);
							}
						}
						else {
							// regular block elements replace other block elements
							for(i = 0; i < _nodes.length; i++){
								$target = angular.element(options);
								$target[0].innerHTML = _nodes[i].innerHTML;
								_nodes[i].parentNode.insertBefore($target[0],_nodes[i]);
								_nodes[i].parentNode.removeChild(_nodes[i]);
							}
						}
					}
					taSelection.setSelectionToElementEnd($target[0]);
					return;
				}else if(command.toLowerCase() === 'createlink'){
					var tagBegin = '<a href="' + options + '" target="' +
							(defaultTagAttributes.a.target ? defaultTagAttributes.a.target : '') +
							'">',
						tagEnd = '</a>',
						_selection = taSelection.getSelection();
					if(_selection.collapsed){
						// insert text at selection, then select then just let normal exec-command run
						taSelection.insertHtml(tagBegin + options + tagEnd, topNode);
					}else if(rangy.getSelection().getRangeAt(0).canSurroundContents()){
						var node = angular.element(tagBegin + tagEnd)[0];
						rangy.getSelection().getRangeAt(0).surroundContents(node);
					}
					return;
				}else if(command.toLowerCase() === 'inserthtml'){
					taSelection.insertHtml(options, topNode);
					return;
				}
			}
			try{
				$document[0].execCommand(command, showUI, options);
			}catch(e){}
		};
	};
}]).service('taSelection', ['$window', '$document', 'taDOM',
/* istanbul ignore next: all browser specifics and PhantomJS dosen't seem to support half of it */
function($window, $document, taDOM){
	// need to dereference the document else the calls don't work correctly
	var _document = $document[0];
	var rangy = $window.rangy;
	var brException = function (element, offset) {
		/* check if selection is a BR element at the beginning of a container. If so, get
		* the parentNode instead.
		* offset should be zero in this case. Otherwise, return the original
		* element.
		*/
		if (element.tagName && element.tagName.match(/^br$/i) && offset === 0 && !element.previousSibling) {
            return {
                element: element.parentNode,
                offset: 0
            };
		} else {
			return {
				element: element,
				offset: offset
			};
		}
	};
	var api = {
		getSelection: function(){
			var range = rangy.getSelection().getRangeAt(0);
			var container = range.commonAncestorContainer;
			var selection = {
				start: brException(range.startContainer, range.startOffset),
				end: brException(range.endContainer, range.endOffset),
				collapsed: range.collapsed
			};
			// Check if the container is a text node and return its parent if so
			container = container.nodeType === 3 ? container.parentNode : container;
			if (container.parentNode === selection.start.element ||
				container.parentNode === selection.end.element) {
				selection.container = container.parentNode;
			} else {
				selection.container = container;
			}
			return selection;
		},
		getOnlySelectedElements: function(){
			var range = rangy.getSelection().getRangeAt(0);
			var container = range.commonAncestorContainer;
			// Check if the container is a text node and return its parent if so
			container = container.nodeType === 3 ? container.parentNode : container;
			return range.getNodes([1], function(node){
				return node.parentNode === container;
			});
		},
		// Some basic selection functions
		getSelectionElement: function () {
			return api.getSelection().container;
		},
		setSelection: function(el, start, end){
			var range = rangy.createRange();
			
			range.setStart(el, start);
			range.setEnd(el, end);
			
			rangy.getSelection().setSingleRange(range);
		},
		setSelectionBeforeElement: function (el){
			var range = rangy.createRange();
			
			range.selectNode(el);
			range.collapse(true);
			
			rangy.getSelection().setSingleRange(range);
		},
		setSelectionAfterElement: function (el){
			var range = rangy.createRange();
			
			range.selectNode(el);
			range.collapse(false);
			
			rangy.getSelection().setSingleRange(range);
		},
		setSelectionToElementStart: function (el){
			var range = rangy.createRange();
			
			range.selectNodeContents(el);
			range.collapse(true);
			
			rangy.getSelection().setSingleRange(range);
		},
		setSelectionToElementEnd: function (el){
			var range = rangy.createRange();
			
			range.selectNodeContents(el);
			range.collapse(false);
			if(el.childNodes && el.childNodes[el.childNodes.length - 1] && el.childNodes[el.childNodes.length - 1].nodeName === 'br'){
				range.startOffset = range.endOffset = range.startOffset - 1;
			}
			rangy.getSelection().setSingleRange(range);
		},
		// from http://stackoverflow.com/questions/6690752/insert-html-at-caret-in-a-contenteditable-div
		// topNode is the contenteditable normally, all manipulation MUST be inside this.
		insertHtml: function(html, topNode){
			var parent, secondParent, _childI, nodes, i, lastNode, _tempFrag;
			var element = angular.element("<div>" + html + "</div>");
			var range = rangy.getSelection().getRangeAt(0);
			var frag = _document.createDocumentFragment();
			var children = element[0].childNodes;
			var isInline = true;
			
			if(children.length > 0){
				// NOTE!! We need to do the following:
				// check for blockelements - if they exist then we have to split the current element in half (and all others up to the closest block element) and insert all children in-between.
				// If there are no block elements, or there is a mixture we need to create textNodes for the non wrapped text (we don't want them spans messing up the picture).
				nodes = [];
				for(_childI = 0; _childI < children.length; _childI++){
					if(!(
						(children[_childI].nodeName.toLowerCase() === 'p' && children[_childI].innerHTML.trim() === '') || // empty p element
						(children[_childI].nodeType === 3 && children[_childI].nodeValue.trim() === '') // empty text node
					)){
						isInline = isInline && !BLOCKELEMENTS.test(children[_childI].nodeName);
						nodes.push(children[_childI]);
					}
				}
				for(var _n = 0; _n < nodes.length; _n++) lastNode = frag.appendChild(nodes[_n]);
				if(!isInline && range.collapsed && /^(|<br(|\/)>)$/i.test(range.startContainer.innerHTML)) range.selectNode(range.startContainer);
			}else{
				isInline = true;
				// paste text of some sort
				lastNode = frag = _document.createTextNode(html);
			}
			
			// Other Edge case - selected data spans multiple blocks.
			if(isInline){
				range.deleteContents();
			}else{ // not inline insert
				if(range.collapsed && range.startContainer !== topNode){
					if(range.startContainer.innerHTML && range.startContainer.innerHTML.match(/^<[^>]*>$/i)){
						// this log is to catch when innerHTML is something like `<img ...>`
						parent = range.startContainer;
						if(range.startOffset === 1){
							// before single tag
							range.setStartAfter(parent);
							range.setEndAfter(parent);
						}else{
							// after single tag
							range.setStartBefore(parent);
							range.setEndBefore(parent);
						}
					}else{
						// split element into 2 and insert block element in middle
						if(range.startContainer.nodeType === 3 && range.startContainer.parentNode !== topNode){ // if text node
							parent = range.startContainer.parentNode;
							secondParent = parent.cloneNode();
							// split the nodes into two lists - before and after, splitting the node with the selection into 2 text nodes.
							taDOM.splitNodes(parent.childNodes, parent, secondParent, range.startContainer, range.startOffset);
							
							// Escape out of the inline tags like b
							while(!VALIDELEMENTS.test(parent.nodeName)){
								angular.element(parent).after(secondParent);
								parent = parent.parentNode;
								var _lastSecondParent = secondParent;
								secondParent = parent.cloneNode();
								// split the nodes into two lists - before and after, splitting the node with the selection into 2 text nodes.
								taDOM.splitNodes(parent.childNodes, parent, secondParent, _lastSecondParent);
							}
						}else{
							parent = range.startContainer;
							secondParent = parent.cloneNode();
							taDOM.splitNodes(parent.childNodes, parent, secondParent, undefined, undefined, range.startOffset);
						}
						
						angular.element(parent).after(secondParent);
						// put cursor to end of inserted content
						range.setStartAfter(parent);
						range.setEndAfter(parent);
						
						if(/^(|<br(|\/)>)$/i.test(parent.innerHTML.trim())){
							range.setStartBefore(parent);
							range.setEndBefore(parent);
							angular.element(parent).remove();
						}
						if(/^(|<br(|\/)>)$/i.test(secondParent.innerHTML.trim())) angular.element(secondParent).remove();
						if(parent.nodeName.toLowerCase() === 'li'){
							_tempFrag = _document.createDocumentFragment();
							for(i = 0; i < frag.childNodes.length; i++){
								element = angular.element('<li>');
								taDOM.transferChildNodes(frag.childNodes[i], element[0]);
								taDOM.transferNodeAttributes(frag.childNodes[i], element[0]);
								_tempFrag.appendChild(element[0]);
							}
							frag = _tempFrag;
							if(lastNode){
								lastNode = frag.childNodes[frag.childNodes.length - 1];
								lastNode = lastNode.childNodes[lastNode.childNodes.length - 1];
							}
						}
					}
				}else{
					range.deleteContents();
				}
			}
			
			range.insertNode(frag);
			if(lastNode){
				api.setSelectionToElementEnd(lastNode);
			}
		}
	};
	return api;
}]).service('taDOM', function(){
	var taDOM = {
		// recursive function that returns an array of angular.elements that have the passed attribute set on them
		getByAttribute: function(element, attribute){
			var resultingElements = [];
			var childNodes = element.children();
			if(childNodes.length){
				angular.forEach(childNodes, function(child){
					resultingElements = resultingElements.concat(taDOM.getByAttribute(angular.element(child), attribute));
				});
			}
			if(element.attr(attribute) !== undefined) resultingElements.push(element);
			return resultingElements;
		},
		
		transferChildNodes: function(source, target){
			// clear out target
			target.innerHTML = '';
			while(source.childNodes.length > 0) target.appendChild(source.childNodes[0]);
			return target;
		},
		
		splitNodes: function(nodes, target1, target2, splitNode, subSplitIndex, splitIndex){
			if(!splitNode && isNaN(splitIndex)) throw new Error('taDOM.splitNodes requires a splitNode or splitIndex');
			var startNodes = document.createDocumentFragment();
			var endNodes = document.createDocumentFragment();
			var index = 0;
			
			while(nodes.length > 0 && (isNaN(splitIndex) || splitIndex !== index) && nodes[0] !== splitNode){
				startNodes.appendChild(nodes[0]); // this removes from the nodes array (if proper childNodes object.
				index++;
			}
			
			if(!isNaN(subSplitIndex) && subSplitIndex >= 0 && nodes[0]){
				startNodes.appendChild(document.createTextNode(nodes[0].nodeValue.substring(0, subSplitIndex)));
				nodes[0].nodeValue = nodes[0].nodeValue.substring(subSplitIndex);
			}
			while(nodes.length > 0) endNodes.appendChild(nodes[0]);
			
			taDOM.transferChildNodes(startNodes, target1);
			taDOM.transferChildNodes(endNodes, target2);
		},
		
		transferNodeAttributes: function(source, target){
			for(var i = 0; i < source.attributes.length; i++) target.setAttribute(source.attributes[i].name, source.attributes[i].value);
			return target;
		}
	};
	return taDOM;
});
angular.module('textAngular.validators', [])
.directive('taMaxText', function(){
	return {
		restrict: 'A',
		require: 'ngModel',
		link: function(scope, elem, attrs, ctrl){
			var max = parseInt(scope.$eval(attrs.taMaxText));
			if (isNaN(max)){
				throw('Max text must be an integer');
			}
			attrs.$observe('taMaxText', function(value){
				max = parseInt(value);
				if (isNaN(max)){
					throw('Max text must be an integer');
				}
				if (ctrl.$dirty){
					ctrl.$validate();
				}
			});
			ctrl.$validators.taMaxText = function(viewValue){
				var source = angular.element('<div/>');
				source.html(viewValue);
				return source.text().length <= max;
			};
		}
	};
}).directive('taMinText', function(){
	return {
		restrict: 'A',
		require: 'ngModel',
		link: function(scope, elem, attrs, ctrl){
			var min = parseInt(scope.$eval(attrs.taMinText));
			if (isNaN(min)){
				throw('Min text must be an integer');
			}
			attrs.$observe('taMinText', function(value){
				min = parseInt(value);
				if (isNaN(min)){
					throw('Min text must be an integer');
				}
				if (ctrl.$dirty){
					ctrl.$validate();
				}
			});
			ctrl.$validators.taMinText = function(viewValue){
				var source = angular.element('<div/>');
				source.html(viewValue);
				return !source.text().length || source.text().length >= min;
			};
		}
	};
});
angular.module('textAngular.taBind', ['textAngular.factories', 'textAngular.DOM'])
.service('_taBlankTest', [function(){
	var INLINETAGS_NONBLANK = /<(a|abbr|acronym|bdi|bdo|big|cite|code|del|dfn|img|ins|kbd|label|map|mark|q|ruby|rp|rt|s|samp|time|tt|var)[^>]*(>|$)/i;
	return function(_defaultTest){
		return function(_blankVal){
			if(!_blankVal) return true;
			// find first non-tag match - ie start of string or after tag that is not whitespace
			var _firstMatch = /(^[^<]|>)[^<]/i.exec(_blankVal);
			var _firstTagIndex;
			if(!_firstMatch){
				// find the end of the first tag removing all the
				// Don't do a global replace as that would be waaayy too long, just replace the first 4 occurences should be enough
				_blankVal = _blankVal.toString().replace(/="[^"]*"/i, '').replace(/="[^"]*"/i, '').replace(/="[^"]*"/i, '').replace(/="[^"]*"/i, '');
				_firstTagIndex = _blankVal.indexOf('>');
			}else{
				_firstTagIndex = _firstMatch.index;
			}
			_blankVal = _blankVal.trim().substring(_firstTagIndex, _firstTagIndex + 100);
			// check for no tags entry
			if(/^[^<>]+$/i.test(_blankVal)) return false;
			// this regex is to match any number of whitespace only between two tags
			if (_blankVal.length === 0 || _blankVal === _defaultTest || /^>(\s|&nbsp;)*<\/[^>]+>$/ig.test(_blankVal)) return true;
			// this regex tests if there is a tag followed by some optional whitespace and some text after that
			else if (/>\s*[^\s<]/i.test(_blankVal) || INLINETAGS_NONBLANK.test(_blankVal)) return false;
			else return true;
		};
	};
}])
.directive('taButton', [function(){
	return {
		link: function(scope, element, attrs){
			element.attr('unselectable', 'on');
			element.on('mousedown', function(e, eventData){
				/* istanbul ignore else: this is for catching the jqLite testing*/
				if(eventData) angular.extend(e, eventData);
				// this prevents focusout from firing on the editor when clicking toolbar buttons
				e.preventDefault();
				return false;
			});
		}
	};
}])
.directive('taBind', [
		'taSanitize', '$timeout', '$window', '$document', 'taFixChrome', 'taBrowserTag',
		'taSelection', 'taSelectableElements', 'taApplyCustomRenderers', 'taOptions',
		'_taBlankTest', '$parse', 'taDOM', 'textAngularManager',
		function(
			taSanitize, $timeout, $window, $document, taFixChrome, taBrowserTag,
			taSelection, taSelectableElements, taApplyCustomRenderers, taOptions,
			_taBlankTest, $parse, taDOM, textAngularManager){
	// Uses for this are textarea or input with ng-model and ta-bind='text'
	// OR any non-form element with contenteditable="contenteditable" ta-bind="html|text" ng-model
	return {
		priority: 2, // So we override validators correctly
		require: ['ngModel','?ngModelOptions'],
		link: function(scope, element, attrs, controller){
			var ngModel = controller[0];
			var ngModelOptions = controller[1] || {};
			// the option to use taBind on an input or textarea is required as it will sanitize all input into it correctly.
			var _isContentEditable = element.attr('contenteditable') !== undefined && element.attr('contenteditable');
			var _isInputFriendly = _isContentEditable || element[0].tagName.toLowerCase() === 'textarea' || element[0].tagName.toLowerCase() === 'input';
			var _isReadonly = false;
			var _focussed = false;
			var _skipRender = false;
			var _disableSanitizer = attrs.taUnsafeSanitizer || taOptions.disableSanitizer;
			var _lastKey;
			// see http://www.javascripter.net/faq/keycodes.htm for good information
			// NOTE Mute On|Off 173 (Opera MSIE Safari Chrome) 181 (Firefox)
			// BLOCKED_KEYS are special keys...
			// Tab, pause/break, CapsLock, Esc, Page Up, End, Home,
			// Left arrow, Up arrow, Right arrow, Down arrow, Insert, Delete,
			// f1, f2, f3, f4, f5, f6, f7, f8, f9, f10, f11, f12
			// NumLock, ScrollLock
			var BLOCKED_KEYS = /^(9|19|20|27|33|34|35|36|37|38|39|40|45|112|113|114|115|116|117|118|119|120|121|122|123|144|145)$/i;
			// UNDO_TRIGGER_KEYS - spaces, enter, delete, backspace, all punctuation
			// Backspace, Enter, Space, Delete, (; :) (Firefox), (= +) (Firefox),
			// Numpad +, Numpad -, (; :), (= +),
			// (, <), (- _), (. >), (/ ?), (` ~), ([ {), (\ |), (] }), (' ")
			// NOTE - Firefox: 173 = (- _) -- adding this to UNDO_TRIGGER_KEYS
			var UNDO_TRIGGER_KEYS = /^(8|13|32|46|59|61|107|109|173|186|187|188|189|190|191|192|219|220|221|222)$/i;
			var _pasteHandler;

			// defaults to the paragraph element, but we need the line-break or it doesn't allow you to type into the empty element
			// non IE is '<p><br/></p>', ie is '<p></p>' as for once IE gets it correct...
			var _defaultVal, _defaultTest;

			var _CTRL_KEY = 0x0001;
			var _META_KEY = 0x0002;
			var _ALT_KEY = 0x0004;
			var _SHIFT_KEY = 0x0008;
			// map events to special keys...
			// mappings is an array of maps from events to specialKeys as declared in textAngularSetup
			var _keyMappings = [
				//		ctrl/command + z
				{
					specialKey: 'UndoKey',
					forbiddenModifiers: _ALT_KEY + _SHIFT_KEY,
					mustHaveModifiers: [_META_KEY + _CTRL_KEY],
					keyCode: 90
				},
				//		ctrl/command + shift + z
				{
					specialKey: 'RedoKey',
					forbiddenModifiers: _ALT_KEY,
					mustHaveModifiers: [_META_KEY + _CTRL_KEY, _SHIFT_KEY],
					keyCode: 90
				},
				//		ctrl/command + y
				{
					specialKey: 'RedoKey',
					forbiddenModifiers: _ALT_KEY + _SHIFT_KEY,
					mustHaveModifiers: [_META_KEY + _CTRL_KEY],
					keyCode: 89
				},
				//		TabKey
				{
					specialKey: 'TabKey',
					forbiddenModifiers: _META_KEY + _SHIFT_KEY + _ALT_KEY + _CTRL_KEY,
					mustHaveModifiers: [],
					keyCode: 9
				},
				//		shift + TabKey
				{
					specialKey: 'ShiftTabKey',
					forbiddenModifiers: _META_KEY + _ALT_KEY + _CTRL_KEY,
					mustHaveModifiers: [_SHIFT_KEY],
					keyCode: 9
				}
			];
			function _mapKeys(event) {
				var specialKey;
				_keyMappings.forEach(function (map){
					if (map.keyCode === event.keyCode) {
						var netModifiers = (event.metaKey ? _META_KEY: 0) +
							(event.ctrlKey ? _CTRL_KEY: 0) +
							(event.shiftKey ? _SHIFT_KEY: 0) +
							(event.altKey ? _ALT_KEY: 0);
						if (map.forbiddenModifiers & netModifiers) return;
						if (map.mustHaveModifiers.every(function (modifier) { return netModifiers & modifier; })){
							specialKey = map.specialKey;
						}
					}
				});
				return specialKey;
			}

			// set the default to be a paragraph value
			if(attrs.taDefaultWrap === undefined) attrs.taDefaultWrap = 'p';
			/* istanbul ignore next: ie specific test */
			if(attrs.taDefaultWrap === ''){
				_defaultVal = '';
				_defaultTest = (_browserDetect.ie === undefined)? '<div><br></div>' : (_browserDetect.ie >= 11)? '<p><br></p>' : (_browserDetect.ie <= 8)? '<P>&nbsp;</P>' : '<p>&nbsp;</p>';
			}else{
				_defaultVal = (_browserDetect.ie === undefined || _browserDetect.ie >= 11)?
					'<' + attrs.taDefaultWrap + '><br></' + attrs.taDefaultWrap + '>' :
					(_browserDetect.ie <= 8)?
						'<' + attrs.taDefaultWrap.toUpperCase() + '></' + attrs.taDefaultWrap.toUpperCase() + '>' :
						'<' + attrs.taDefaultWrap + '></' + attrs.taDefaultWrap + '>';
				_defaultTest = (_browserDetect.ie === undefined || _browserDetect.ie >= 11)?
					'<' + attrs.taDefaultWrap + '><br></' + attrs.taDefaultWrap + '>' :
					(_browserDetect.ie <= 8)?
						'<' + attrs.taDefaultWrap.toUpperCase() + '>&nbsp;</' + attrs.taDefaultWrap.toUpperCase() + '>' :
						'<' + attrs.taDefaultWrap + '>&nbsp;</' + attrs.taDefaultWrap + '>';
			}

			/* istanbul ignore else */
			if(!ngModelOptions.$options) ngModelOptions.$options = {}; // ng-model-options support

			var _blankTest = _taBlankTest(_defaultTest);

			var _ensureContentWrapped = function(value) {
				if (_blankTest(value)) return value;
				var domTest = angular.element("<div>" + value + "</div>");
				//console.log('domTest.children().length():', domTest.children().length);
				if (domTest.children().length === 0) {
					value = "<" + attrs.taDefaultWrap + ">" + value + "</" + attrs.taDefaultWrap + ">";
				} else {
					var _children = domTest[0].childNodes;
					var i;
					var _foundBlockElement = false;
					for (i = 0; i < _children.length; i++) {
						if (_foundBlockElement = _children[i].nodeName.toLowerCase().match(BLOCKELEMENTS)) break;
					}
					if (!_foundBlockElement) {
						value = "<" + attrs.taDefaultWrap + ">" + value + "</" + attrs.taDefaultWrap + ">";
					}
					else{
						value = "";
						for(i = 0; i < _children.length; i++){
							var node = _children[i];
							var nodeName = node.nodeName.toLowerCase();
							//console.log(nodeName);
							if(nodeName === '#comment') {
								value += '<!--' + node.nodeValue + '-->';
							} else if(nodeName === '#text') {
								// determine if this is all whitespace, if so, we will leave it as it is.
								// otherwise, we will wrap it as it is
								var text = node.textContent;
								if (!text.trim()) {
									// just whitespace
									value += text;
								} else {
									// not pure white space so wrap in <p>...</p> or whatever attrs.taDefaultWrap is set to.
									value += "<" + attrs.taDefaultWrap + ">" + text + "</" + attrs.taDefaultWrap + ">";
								}
							} else if(!nodeName.match(BLOCKELEMENTS)){
								/* istanbul ignore  next: Doesn't seem to trigger on tests */
								var _subVal = (node.outerHTML || node.nodeValue);
								/* istanbul ignore else: Doesn't seem to trigger on tests, is tested though */
								if(_subVal.trim() !== '')
									value += "<" + attrs.taDefaultWrap + ">" + _subVal + "</" + attrs.taDefaultWrap + ">";
								else value += _subVal;
							} else {
								value += node.outerHTML;
							}
						}
					}
				}
				//console.log(value);
				return value;
			};

			if(attrs.taPaste) _pasteHandler = $parse(attrs.taPaste);

			element.addClass('ta-bind');

			var _undoKeyupTimeout;

			scope['$undoManager' + (attrs.id || '')] = ngModel.$undoManager = {
				_stack: [],
				_index: 0,
				_max: 1000,
				push: function(value){
					if((typeof value === "undefined" || value === null) ||
						((typeof this.current() !== "undefined" && this.current() !== null) && value === this.current())) return value;
					if(this._index < this._stack.length - 1){
						this._stack = this._stack.slice(0,this._index+1);
					}
					this._stack.push(value);
					if(_undoKeyupTimeout) $timeout.cancel(_undoKeyupTimeout);
					if(this._stack.length > this._max) this._stack.shift();
					this._index = this._stack.length - 1;
					return value;
				},
				undo: function(){
					return this.setToIndex(this._index-1);
				},
				redo: function(){
					return this.setToIndex(this._index+1);
				},
				setToIndex: function(index){
					if(index < 0 || index > this._stack.length - 1){
						return undefined;
					}
					this._index = index;
					return this.current();
				},
				current: function(){
					return this._stack[this._index];
				}
			};

			var _redoUndoTimeout;
			var _undo = scope['$undoTaBind' + (attrs.id || '')] = function(){
				/* istanbul ignore else: can't really test it due to all changes being ignored as well in readonly */
				if(!_isReadonly && _isContentEditable){
					var content = ngModel.$undoManager.undo();
					if(typeof content !== "undefined" && content !== null){
						_setInnerHTML(content);
						_setViewValue(content, false);
						if(_redoUndoTimeout) $timeout.cancel(_redoUndoTimeout);
						_redoUndoTimeout = $timeout(function(){
							element[0].focus();
							taSelection.setSelectionToElementEnd(element[0]);
						}, 1);
					}
				}
			};

			var _redo = scope['$redoTaBind' + (attrs.id || '')] = function(){
				/* istanbul ignore else: can't really test it due to all changes being ignored as well in readonly */
				if(!_isReadonly && _isContentEditable){
					var content = ngModel.$undoManager.redo();
					if(typeof content !== "undefined" && content !== null){
						_setInnerHTML(content);
						_setViewValue(content, false);
						/* istanbul ignore next */
						if(_redoUndoTimeout) $timeout.cancel(_redoUndoTimeout);
						_redoUndoTimeout = $timeout(function(){
							element[0].focus();
							taSelection.setSelectionToElementEnd(element[0]);
						}, 1);
					}
				}
			};

			// in here we are undoing the converts used elsewhere to prevent the < > and & being displayed when they shouldn't in the code.
			var _compileHtml = function(){
				if(_isContentEditable) return element[0].innerHTML;
				if(_isInputFriendly) return element.val();
				throw ('textAngular Error: attempting to update non-editable taBind');
			};

			var _setViewValue = function(_val, triggerUndo, skipRender){
				_skipRender = skipRender || false;
				if(typeof triggerUndo === "undefined" || triggerUndo === null) triggerUndo = true && _isContentEditable; // if not contentEditable then the native undo/redo is fine
				if(typeof _val === "undefined" || _val === null) _val = _compileHtml();
				if(_blankTest(_val)){
					// this avoids us from tripping the ng-pristine flag if we click in and out with out typing
					if(ngModel.$viewValue !== '') ngModel.$setViewValue('');
					if(triggerUndo && ngModel.$undoManager.current() !== '') ngModel.$undoManager.push('');
				}else{
					_reApplyOnSelectorHandlers();
					if(ngModel.$viewValue !== _val){
						ngModel.$setViewValue(_val);
						if(triggerUndo) ngModel.$undoManager.push(_val);
					}
				}
				ngModel.$render();
			};

			//used for updating when inserting wrapped elements
			scope['updateTaBind' + (attrs.id || '')] = function(){
				if(!_isReadonly) _setViewValue(undefined, undefined, true);
			};

			// catch DOM XSS via taSanitize
			// Sanitizing both ways is identical
			var _sanitize = function(unsafe){
				return (ngModel.$oldViewValue = taSanitize(taFixChrome(unsafe), ngModel.$oldViewValue, _disableSanitizer));
			};

			// trigger the validation calls
			if(element.attr('required')) ngModel.$validators.required = function(modelValue, viewValue) {
				return !_blankTest(modelValue || viewValue);
			};
			// parsers trigger from the above keyup function or any other time that the viewValue is updated and parses it for storage in the ngModel
			ngModel.$parsers.push(_sanitize);
			ngModel.$parsers.unshift(_ensureContentWrapped);
			// because textAngular is bi-directional (which is awesome) we need to also sanitize values going in from the server
			ngModel.$formatters.push(_sanitize);
			ngModel.$formatters.unshift(_ensureContentWrapped);
			ngModel.$formatters.unshift(function(value){
				return ngModel.$undoManager.push(value || '');
			});

			//this code is used to update the models when data is entered/deleted
			if(_isInputFriendly){
				scope.events = {};
				if(!_isContentEditable){
					// if a textarea or input just add in change and blur handlers, everything else is done by angulars input directive
					element.on('change blur', scope.events.change = scope.events.blur = function(){
						if(!_isReadonly) ngModel.$setViewValue(_compileHtml());
					});

					element.on('keydown', scope.events.keydown = function(event, eventData){
						/* istanbul ignore else: this is for catching the jqLite testing*/
						if(eventData) angular.extend(event, eventData);
						// Reference to http://stackoverflow.com/questions/6140632/how-to-handle-tab-in-textarea
						/* istanbul ignore else: otherwise normal functionality */
						if(event.keyCode === 9){ // tab was pressed
							// get caret position/selection
							var start = this.selectionStart;
							var end = this.selectionEnd;

							var value = element.val();
							if(event.shiftKey){
								// find \t
								var _linebreak = value.lastIndexOf('\n', start), _tab = value.lastIndexOf('\t', start);
								if(_tab !== -1 && _tab >= _linebreak){
									// set textarea value to: text before caret + tab + text after caret
									element.val(value.substring(0, _tab) + value.substring(_tab + 1));

									// put caret at right position again (add one for the tab)
									this.selectionStart = this.selectionEnd = start - 1;
								}
							}else{
								// set textarea value to: text before caret + tab + text after caret
								element.val(value.substring(0, start) + "\t" + value.substring(end));

								// put caret at right position again (add one for the tab)
								this.selectionStart = this.selectionEnd = start + 1;
							}
							// prevent the focus lose
							event.preventDefault();
						}
					});

					var _repeat = function(string, n){
						var result = '';
						for(var _n = 0; _n < n; _n++) result += string;
						return result;
					};

					// add a forEach function that will work on a NodeList, etc..
					var forEach = function (array, callback, scope) {
						for (var i= 0; i<array.length; i++) {
							callback.call(scope, i, array[i]);
						}
					};

					// handle <ul> or <ol> nodes
					var recursiveListFormat = function(listNode, tablevel){
						var _html = '';
						var _subnodes = listNode.childNodes;
						tablevel++;
						// tab out and add the <ul> or <ol> html piece
						_html += _repeat('\t', tablevel-1) + listNode.outerHTML.substring(0, 4);
						forEach(_subnodes, function (index, node) {
							/* istanbul ignore next: browser catch */
							var nodeName = node.nodeName.toLowerCase();
							if (nodeName === '#comment') {
								_html += '<!--' + node.nodeValue + '-->';
								return;
							}
							if (nodeName === '#text') {
								_html += node.textContent;
								return;
							}
							/* istanbul ignore next: not tested, and this was original code -- so not wanting to possibly cause an issue, leaving it... */
							if(!node.outerHTML) {
								// no html to add
								return;
							}
							if(nodeName === 'ul' || nodeName === 'ol') {
								_html += '\n' + recursiveListFormat(node, tablevel);
							}
							else {
								// no reformatting within this subnode, so just do the tabing...
								_html += '\n' + _repeat('\t', tablevel) + node.outerHTML;
							}
						});
						// now add on the </ol> or </ul> piece
						_html += '\n' + _repeat('\t', tablevel-1) + listNode.outerHTML.substring(listNode.outerHTML.lastIndexOf('<'));
						return _html;
					};
					// handle formating of something like:
					// <ol><!--First comment-->
					//  <li>Test Line 1<!--comment test list 1--></li>
					//    <ul><!--comment ul-->
					//      <li>Nested Line 1</li>
					//        <!--comment between nested lines--><li>Nested Line 2</li>
					//    </ul>
					//  <li>Test Line 3</li>
					// </ol>
					ngModel.$formatters.unshift(function(htmlValue){
						// tabulate the HTML so it looks nicer
						//
						// first get a list of the nodes...
						// we do this by using the element parser...
						//
						// doing this -- which is simpiler -- breaks our tests...
						//var _nodes=angular.element(htmlValue);
						var _nodes = angular.element('<div>' + htmlValue + '</div>')[0].childNodes;
						if(_nodes.length > 0){
							// do the reformatting of the layout...
							htmlValue = '';
							forEach(_nodes, function (index, node) {
								var nodeName = node.nodeName.toLowerCase();
								if (nodeName === '#comment') {
									htmlValue += '<!--' + node.nodeValue + '-->';
									return;
								}
								if (nodeName === '#text') {
									htmlValue += node.textContent;
									return;
								}
								/* istanbul ignore next: not tested, and this was original code -- so not wanting to possibly cause an issue, leaving it... */
								if(!node.outerHTML)
								{
									// nothing to format!
									return;
								}
								if(htmlValue.length > 0) {
									// we aready have some content, so drop to a new line
									htmlValue += '\n';
								}
								if(nodeName === 'ul' || nodeName === 'ol') {
									// okay a set of list stuff we want to reformat in a nested way
									htmlValue += '' + recursiveListFormat(node, 0);
								}
								else {
									// just use the original without any additional formating
									htmlValue += '' + node.outerHTML;
								}
							});
						}
						return htmlValue;
					});
				}else{
					// all the code specific to contenteditable divs
					var _processingPaste = false;
					/* istanbul ignore next: phantom js cannot test this for some reason */
					var processpaste = function(text) {
						/* istanbul ignore else: don't care if nothing pasted */
						if(text && text.trim().length){
							// test paste from word/microsoft product
							if(text.match(/class=["']*Mso(Normal|List)/i) || text.match(/content=["']*Word.Document/i)){
								var textFragment = text.match(/<!--StartFragment-->([\s\S]*?)<!--EndFragment-->/i);
								if(!textFragment) textFragment = text;
								else textFragment = textFragment[1];
								textFragment = textFragment.replace(/<o:p>[\s\S]*?<\/o:p>/ig, '').replace(/class=(["']|)MsoNormal(["']|)/ig, '');
								var dom = angular.element("<div>" + textFragment + "</div>");
								var targetDom = angular.element("<div></div>");
								var _list = {
									element: null,
									lastIndent: [],
									lastLi: null,
									isUl: false
								};
								_list.lastIndent.peek = function(){
									var n = this.length;
									if (n>0) return this[n-1];
								};
								var _resetList = function(isUl){
									_list.isUl = isUl;
									_list.element = angular.element(isUl ? "<ul>" : "<ol>");
									_list.lastIndent = [];
									_list.lastIndent.peek = function(){
										var n = this.length;
										if (n>0) return this[n-1];
									};
									_list.lastLevelMatch = null;
								};
								for(var i = 0; i <= dom[0].childNodes.length; i++){
									if(!dom[0].childNodes[i] || dom[0].childNodes[i].nodeName === "#text"){
										continue;
									} else {
										var tagName = dom[0].childNodes[i].tagName.toLowerCase();
										if(tagName !== "p" && tagName !== "h1" && tagName !== "h2" && tagName !== "h3" && tagName !== "h4" && tagName !== "h5" && tagName !== "h6"){
											continue;
										}
									}
									var el = angular.element(dom[0].childNodes[i]);
									var _listMatch = (el.attr('class') || '').match(/MsoList(Bullet|Number|Paragraph)(CxSp(First|Middle|Last)|)/i);

									if(_listMatch){
										if(el[0].childNodes.length < 2 || el[0].childNodes[1].childNodes.length < 1){
											continue;
										}
										var isUl = _listMatch[1].toLowerCase() === "bullet" || (_listMatch[1].toLowerCase() !== "number" && !(/^[^0-9a-z<]*[0-9a-z]+[^0-9a-z<>]</i.test(el[0].childNodes[1].innerHTML) || /^[^0-9a-z<]*[0-9a-z]+[^0-9a-z<>]</i.test(el[0].childNodes[1].childNodes[0].innerHTML)));
										var _indentMatch = (el.attr('style') || '').match(/margin-left:([\-\.0-9]*)/i);
										var indent = parseFloat((_indentMatch)?_indentMatch[1]:0);
										var _levelMatch = (el.attr('style') || '').match(/mso-list:l([0-9]+) level([0-9]+) lfo[0-9+]($|;)/i);
										// prefers the mso-list syntax

										if(_levelMatch && _levelMatch[2]) indent = parseInt(_levelMatch[2]);

										if ((_levelMatch && (!_list.lastLevelMatch || _levelMatch[1] !== _list.lastLevelMatch[1])) || !_listMatch[3] || _listMatch[3].toLowerCase() === "first" || (_list.lastIndent.peek() === null) || (_list.isUl !== isUl && _list.lastIndent.peek() === indent)) {
											_resetList(isUl);
											targetDom.append(_list.element);
										} else if (_list.lastIndent.peek() != null && _list.lastIndent.peek() < indent){
											_list.element = angular.element(isUl ? "<ul>" : "<ol>");
											_list.lastLi.append(_list.element);
										} else if (_list.lastIndent.peek() != null && _list.lastIndent.peek() > indent){
											while(_list.lastIndent.peek() != null && _list.lastIndent.peek() > indent){
												if(_list.element.parent()[0].tagName.toLowerCase() === 'li'){
													_list.element = _list.element.parent();
													continue;
												}else if(/[uo]l/i.test(_list.element.parent()[0].tagName.toLowerCase())){
													_list.element = _list.element.parent();
												}else{ // else it's it should be a sibling
													break;
												}
												_list.lastIndent.pop();
											}
											_list.isUl = _list.element[0].tagName.toLowerCase() === "ul";
											if (isUl !== _list.isUl) {
												_resetList(isUl);
												targetDom.append(_list.element);
											}
										}

										_list.lastLevelMatch = _levelMatch;
										if(indent !== _list.lastIndent.peek()) _list.lastIndent.push(indent);
										_list.lastLi = angular.element("<li>");
										_list.element.append(_list.lastLi);
										_list.lastLi.html(el.html().replace(/<!(--|)\[if !supportLists\](--|)>[\s\S]*?<!(--|)\[endif\](--|)>/ig, ''));
										el.remove();
									}else{
										_resetList(false);
										targetDom.append(el);
									}
								}
								var _unwrapElement = function(node){
									node = angular.element(node);
									for(var _n = node[0].childNodes.length - 1; _n >= 0; _n--) node.after(node[0].childNodes[_n]);
									node.remove();
								};

								angular.forEach(targetDom.find('span'), function(node){
									node.removeAttribute('lang');
									if(node.attributes.length <= 0) _unwrapElement(node);
								});
								angular.forEach(targetDom.find('font'), _unwrapElement);
								text = targetDom.html();
							}else{
								// remove unnecessary chrome insert
								text = text.replace(/<(|\/)meta[^>]*?>/ig, '');
								if(text.match(/<[^>]*?(ta-bind)[^>]*?>/)){
									// entire text-angular or ta-bind has been pasted, REMOVE AT ONCE!!
									if(text.match(/<[^>]*?(text-angular)[^>]*?>/)){
										var _el = angular.element("<div>" + text + "</div>");
										_el.find('textarea').remove();
										var binds = taDOM.getByAttribute(_el, 'ta-bind');
										for(var _b = 0; _b < binds.length; _b++){
											var _target = binds[_b][0].parentNode.parentNode;
											for(var _c = 0; _c < binds[_b][0].childNodes.length; _c++){
												_target.parentNode.insertBefore(binds[_b][0].childNodes[_c], _target);
											}
											_target.parentNode.removeChild(_target);
										}
										text = _el.html().replace('<br class="Apple-interchange-newline">', '');
									}
								}else if(text.match(/^<span/)){
									// in case of pasting only a span - chrome paste, remove them. THis is just some wierd formatting
									// if we remove the '<span class="Apple-converted-space"> </span>' here we destroy the spacing
									// on paste from even ourselves!
									if (!text.match(/<span class=(\"Apple-converted-space\"|\'Apple-converted-space\')>.<\/span>/ig)) {
										text = text.replace(/<(|\/)span[^>]*?>/ig, '');
									}
								}
								// Webkit on Apple tags
								text = text.replace(/<br class="Apple-interchange-newline"[^>]*?>/ig, '').replace(/<span class="Apple-converted-space">( |&nbsp;)<\/span>/ig, '&nbsp;');
							}

							if (/<li(\s.*)?>/i.test(text) && /(<ul(\s.*)?>|<ol(\s.*)?>).*<li(\s.*)?>/i.test(text) === false) {
								// insert missing parent of li element
								text = text.replace(/<li(\s.*)?>.*<\/li(\s.*)?>/i, '<ul>$&</ul>');
							}

							// parse whitespace from plaintext input, starting with preceding spaces that get stripped on paste
							text = text.replace(/^[ |\u00A0]+/gm, function (match) {
								var result = '';
								for (var i = 0; i < match.length; i++) {
									result += '&nbsp;';
								}
								return result;
							}).replace(/\n|\r\n|\r/g, '<br />').replace(/\t/g, '&nbsp;&nbsp;&nbsp;&nbsp;');

							if(_pasteHandler) text = _pasteHandler(scope, {$html: text}) || text;

							text = taSanitize(text, '', _disableSanitizer);

							taSelection.insertHtml(text, element[0]);
							$timeout(function(){
								ngModel.$setViewValue(_compileHtml());
								_processingPaste = false;
								element.removeClass('processing-paste');
							}, 0);
						}else{
							_processingPaste = false;
							element.removeClass('processing-paste');
						}
					};

					element.on('paste', scope.events.paste = function(e, eventData){
						/* istanbul ignore else: this is for catching the jqLite testing*/
						if(eventData) angular.extend(e, eventData);
						if(_isReadonly || _processingPaste){
							e.stopPropagation();
							e.preventDefault();
							return false;
						}

						// Code adapted from http://stackoverflow.com/questions/2176861/javascript-get-clipboard-data-on-paste-event-cross-browser/6804718#6804718
						_processingPaste = true;
						element.addClass('processing-paste');
						var pastedContent;
						var clipboardData = (e.originalEvent || e).clipboardData;
						if (clipboardData && clipboardData.getData && clipboardData.types.length > 0) {// Webkit - get data from clipboard, put into editdiv, cleanup, then cancel event
							var _types = "";
							for(var _t = 0; _t < clipboardData.types.length; _t++){
								_types += " " + clipboardData.types[_t];
							}
							/* istanbul ignore next: browser tests */
							if (/text\/html/i.test(_types)) {
								pastedContent = clipboardData.getData('text/html');
							} else if (/text\/plain/i.test(_types)) {
								pastedContent = clipboardData.getData('text/plain');
							}

							processpaste(pastedContent);
							e.stopPropagation();
							e.preventDefault();
							return false;
						} else {// Everything else - empty editdiv and allow browser to paste content into it, then cleanup
							var _savedSelection = $window.rangy.saveSelection(),
								_tempDiv = angular.element('<div class="ta-hidden-input" contenteditable="true"></div>');
							$document.find('body').append(_tempDiv);
							_tempDiv[0].focus();
							$timeout(function(){
								// restore selection
								$window.rangy.restoreSelection(_savedSelection);
								processpaste(_tempDiv[0].innerHTML);
								element[0].focus();
								_tempDiv.remove();
							}, 0);
						}
					});
					element.on('cut', scope.events.cut = function(e){
						// timeout to next is needed as otherwise the paste/cut event has not finished actually changing the display
						if(!_isReadonly) $timeout(function(){
							ngModel.$setViewValue(_compileHtml());
						}, 0);
						else e.preventDefault();
					});

					element.on('keydown', scope.events.keydown = function(event, eventData){
						/* istanbul ignore else: this is for catching the jqLite testing*/
						if(eventData) angular.extend(event, eventData);
						event.specialKey = _mapKeys(event);
						var userSpecialKey;
						/* istanbul ignore next: difficult to test */
						taOptions.keyMappings.forEach(function (mapping) {
							if (event.specialKey === mapping.commandKeyCode) {
								// taOptions has remapped this binding... so
								// we disable our own
								event.specialKey = undefined;
							}
							if (mapping.testForKey(event)) {
								userSpecialKey = mapping.commandKeyCode;
							}
							if ((mapping.commandKeyCode === 'UndoKey') || (mapping.commandKeyCode === 'RedoKey')) {
								// this is necessary to fully stop the propagation.
								if (!mapping.enablePropagation) {
									event.preventDefault();
								}
							}
						});
						/* istanbul ignore next: difficult to test */
						if (typeof userSpecialKey !== 'undefined') {
							event.specialKey = userSpecialKey;
						}
						/* istanbul ignore next: difficult to test as can't seem to select */
						if ((typeof event.specialKey !== 'undefined') && (
								event.specialKey !== 'UndoKey' || event.specialKey !== 'RedoKey'
							)) {
							event.preventDefault();
							textAngularManager.sendKeyCommand(scope, event);
						}
						/* istanbul ignore else: readonly check */
						if(!_isReadonly){
							if (event.specialKey==='UndoKey') {
								_undo();
								event.preventDefault();
							}
							if (event.specialKey==='RedoKey') {
								_redo();
								event.preventDefault();
							}
							/* istanbul ignore next: difficult to test as can't seem to select */
							if(event.keyCode === 13 && !event.shiftKey){
								var contains = function(a, obj) {
									for (var i = 0; i < a.length; i++) {
										if (a[i] === obj) {
											return true;
										}
									}
									return false;
								};
								var $selection;
								var selection = taSelection.getSelectionElement();
								if(!selection.tagName.match(VALIDELEMENTS)) return;
								var _new = angular.element(_defaultVal);
								// if we are in the last element of a blockquote, or ul or ol and the element is blank
								// we need to pull the element outside of the said type
								var moveOutsideElements = ['blockquote', 'ul', 'ol'];
								if (contains(moveOutsideElements, selection.parentNode.tagName.toLowerCase())) {
									if (/^<br(|\/)>$/i.test(selection.innerHTML.trim()) && !selection.nextSibling) {
										// if last element is blank, pull element outside.
										$selection = angular.element(selection);
										var _parent = $selection.parent();
										_parent.after(_new);
										$selection.remove();
										if (_parent.children().length === 0) _parent.remove();
										taSelection.setSelectionToElementStart(_new[0]);
										event.preventDefault();
									}
									if (/^<[^>]+><br(|\/)><\/[^>]+>$/i.test(selection.innerHTML.trim())) {
										$selection = angular.element(selection);
										$selection.after(_new);
										$selection.remove();
										taSelection.setSelectionToElementStart(_new[0]);
										event.preventDefault();
									}
								}
							}
						}
					});
					var _keyupTimeout;
					element.on('keyup', scope.events.keyup = function(event, eventData){
						/* istanbul ignore else: this is for catching the jqLite testing*/
						if(eventData) angular.extend(event, eventData);
						/* istanbul ignore next: FF specific bug fix */
						if (event.keyCode === 9) {
							var _selection = taSelection.getSelection();
							if(_selection.start.element === element[0] && element.children().length) taSelection.setSelectionToElementStart(element.children()[0]);
							return;
						}
						if(_undoKeyupTimeout) $timeout.cancel(_undoKeyupTimeout);
						if(!_isReadonly && !BLOCKED_KEYS.test(event.keyCode)){
							// if enter - insert new taDefaultWrap, if shift+enter insert <br/>
							if(_defaultVal !== '' && event.keyCode === 13){
								if(!event.shiftKey){
									// new paragraph, br should be caught correctly
									var selection = taSelection.getSelectionElement();
									while(!selection.tagName.match(VALIDELEMENTS) && selection !== element[0]){
										selection = selection.parentNode;
									}

									if(selection.tagName.toLowerCase() !== attrs.taDefaultWrap && selection.tagName.toLowerCase() !== 'li' && (selection.innerHTML.trim() === '' || selection.innerHTML.trim() === '<br>')){
										var _new = angular.element(_defaultVal);
										angular.element(selection).replaceWith(_new);
										taSelection.setSelectionToElementStart(_new[0]);
									}
								}
							}
							var val = _compileHtml();
							if(_defaultVal !== '' && val.trim() === ''){
								_setInnerHTML(_defaultVal);
								taSelection.setSelectionToElementStart(element.children()[0]);
							}else if(val.substring(0, 1) !== '<' && attrs.taDefaultWrap !== ''){
								/* we no longer do this, since there can be comments here and white space
								var _savedSelection = $window.rangy.saveSelection();
								val = _compileHtml();
								val = "<" + attrs.taDefaultWrap + ">" + val + "</" + attrs.taDefaultWrap + ">";
								_setInnerHTML(val);
								$window.rangy.restoreSelection(_savedSelection);
								*/
							}
							var triggerUndo = _lastKey !== event.keyCode && UNDO_TRIGGER_KEYS.test(event.keyCode);
							if(_keyupTimeout) $timeout.cancel(_keyupTimeout);
							_keyupTimeout = $timeout(function() {
								_setViewValue(val, triggerUndo, true);
							}, ngModelOptions.$options.debounce || 400);
							if(!triggerUndo) _undoKeyupTimeout = $timeout(function(){ ngModel.$undoManager.push(val); }, 250);
							_lastKey = event.keyCode;
						}
					});

					element.on('blur', scope.events.blur = function(){
						_focussed = false;
						/* istanbul ignore else: if readonly don't update model */
						if(!_isReadonly){
							_setViewValue(undefined, undefined, true);
						}else{
							_skipRender = true; // don't redo the whole thing, just check the placeholder logic
							ngModel.$render();
						}
					});

					// Placeholders not supported on ie 8 and below
					if(attrs.placeholder && (_browserDetect.ie > 8 || _browserDetect.ie === undefined)){
						var rule;
						if(attrs.id) rule = addCSSRule('#' + attrs.id + '.placeholder-text:before', 'content: "' + attrs.placeholder + '"');
						else throw('textAngular Error: An unique ID is required for placeholders to work');

						scope.$on('$destroy', function(){
							removeCSSRule(rule);
						});
					}

					element.on('focus', scope.events.focus = function(){
						_focussed = true;
						element.removeClass('placeholder-text');
						_reApplyOnSelectorHandlers();
					});

					element.on('mouseup', scope.events.mouseup = function(){
						var _selection = taSelection.getSelection();
						if(_selection.start.element === element[0] && element.children().length) taSelection.setSelectionToElementStart(element.children()[0]);
					});

					// prevent propagation on mousedown in editor, see #206
					element.on('mousedown', scope.events.mousedown = function(event, eventData){
						/* istanbul ignore else: this is for catching the jqLite testing*/
						if(eventData) angular.extend(event, eventData);
						event.stopPropagation();
					});
				}
			}

			var selectorClickHandler = function(event){
				// emit the element-select event, pass the element
				scope.$emit('ta-element-select', this);
				event.preventDefault();
				return false;
			};
			var fileDropHandler = function(event, eventData){
				/* istanbul ignore else: this is for catching the jqLite testing*/
				if(eventData) angular.extend(event, eventData);
				// emit the drop event, pass the element, preventing should be done elsewhere
				if(!dropFired && !_isReadonly){
					dropFired = true;
					var dataTransfer;
					if(event.originalEvent) dataTransfer = event.originalEvent.dataTransfer;
					else dataTransfer = event.dataTransfer;
					scope.$emit('ta-drop-event', this, event, dataTransfer);
					$timeout(function(){
						dropFired = false;
						_setViewValue(undefined, undefined, true);
					}, 100);
				}
			};

			//used for updating when inserting wrapped elements
			var _reApplyOnSelectorHandlers = scope['reApplyOnSelectorHandlers' + (attrs.id || '')] = function(){
				/* istanbul ignore else */
				if(!_isReadonly) angular.forEach(taSelectableElements, function(selector){
						// check we don't apply the handler twice
						element.find(selector)
							.off('click', selectorClickHandler)
							.on('click', selectorClickHandler);
					});
			};

			var _setInnerHTML = function(newval){
				element[0].innerHTML = newval;
			};
			var _renderTimeout;
			var _renderInProgress = false;
			// changes to the model variable from outside the html/text inputs
			ngModel.$render = function(){
				/* istanbul ignore if: Catches rogue renders, hard to replicate in tests */
				if(_renderInProgress) return;
				else _renderInProgress = true;
				// catch model being null or undefined
				var val = ngModel.$viewValue || '';
				// if the editor isn't focused it needs to be updated, otherwise it's receiving user input
				if(!_skipRender){
					/* istanbul ignore else: in other cases we don't care */
					if(_isContentEditable && _focussed){
						// update while focussed
						element.removeClass('placeholder-text');
						if(_renderTimeout) $timeout.cancel(_renderTimeout);
						_renderTimeout = $timeout(function(){
							/* istanbul ignore if: Can't be bothered testing this... */
							if(!_focussed){
								element[0].focus();
								taSelection.setSelectionToElementEnd(element.children()[element.children().length - 1]);
							}
							_renderTimeout = undefined;
						}, 1);
					}
					if(_isContentEditable){
						// WYSIWYG Mode
						if(attrs.placeholder){
							if(val === ''){
								// blank
								_setInnerHTML(_defaultVal);
							}else{
								// not-blank
								_setInnerHTML(val);
							}
						}else{
							_setInnerHTML((val === '') ? _defaultVal : val);
						}
						// if in WYSIWYG and readOnly we kill the use of links by clicking
						if(!_isReadonly){
							_reApplyOnSelectorHandlers();
							element.on('drop', fileDropHandler);
						}else{
							element.off('drop', fileDropHandler);
						}
					}else if(element[0].tagName.toLowerCase() !== 'textarea' && element[0].tagName.toLowerCase() !== 'input'){
						// make sure the end user can SEE the html code as a display. This is a read-only display element
						_setInnerHTML(taApplyCustomRenderers(val));
					}else{
						// only for input and textarea inputs
						element.val(val);
					}
				}
				if(_isContentEditable && attrs.placeholder){
					if(val === ''){
						if(_focussed) element.removeClass('placeholder-text');
						else element.addClass('placeholder-text');
					}else{
						element.removeClass('placeholder-text');
					}
				}
				_renderInProgress = _skipRender = false;
			};

			if(attrs.taReadonly){
				//set initial value
				_isReadonly = scope.$eval(attrs.taReadonly);
				if(_isReadonly){
					element.addClass('ta-readonly');
					// we changed to readOnly mode (taReadonly='true')
					if(element[0].tagName.toLowerCase() === 'textarea' || element[0].tagName.toLowerCase() === 'input'){
						element.attr('disabled', 'disabled');
					}
					if(element.attr('contenteditable') !== undefined && element.attr('contenteditable')){
						element.removeAttr('contenteditable');
					}
				}else{
					element.removeClass('ta-readonly');
					// we changed to NOT readOnly mode (taReadonly='false')
					if(element[0].tagName.toLowerCase() === 'textarea' || element[0].tagName.toLowerCase() === 'input'){
						element.removeAttr('disabled');
					}else if(_isContentEditable){
						element.attr('contenteditable', 'true');
					}
				}
				// taReadonly only has an effect if the taBind element is an input or textarea or has contenteditable='true' on it.
				// Otherwise it is readonly by default
				scope.$watch(attrs.taReadonly, function(newVal, oldVal){
					if(oldVal === newVal) return;
					if(newVal){
						element.addClass('ta-readonly');
						// we changed to readOnly mode (taReadonly='true')
						if(element[0].tagName.toLowerCase() === 'textarea' || element[0].tagName.toLowerCase() === 'input'){
							element.attr('disabled', 'disabled');
						}
						if(element.attr('contenteditable') !== undefined && element.attr('contenteditable')){
							element.removeAttr('contenteditable');
						}
						// turn ON selector click handlers
						angular.forEach(taSelectableElements, function(selector){
							element.find(selector).on('click', selectorClickHandler);
						});
						element.off('drop', fileDropHandler);
					}else{
						element.removeClass('ta-readonly');
						// we changed to NOT readOnly mode (taReadonly='false')
						if(element[0].tagName.toLowerCase() === 'textarea' || element[0].tagName.toLowerCase() === 'input'){
							element.removeAttr('disabled');
						}else if(_isContentEditable){
							element.attr('contenteditable', 'true');
						}
						// remove the selector click handlers
						angular.forEach(taSelectableElements, function(selector){
							element.find(selector).off('click', selectorClickHandler);
						});
						element.on('drop', fileDropHandler);
					}
					_isReadonly = newVal;
				});
			}

			// Initialise the selectableElements
			// if in WYSIWYG and readOnly we kill the use of links by clicking
			if(_isContentEditable && !_isReadonly){
				angular.forEach(taSelectableElements, function(selector){
					element.find(selector).on('click', selectorClickHandler);
				});
				element.on('drop', fileDropHandler);
				element.on('blur', function(){
					/* istanbul ignore next: webkit fix */
					if(_browserDetect.webkit) { // detect webkit
						globalContentEditableBlur = true;
					}
				});
			}
		}
	};
}]);

// this global var is used to prevent multiple fires of the drop event. Needs to be global to the textAngular file.
var dropFired = false;
var textAngular = angular.module("textAngular", ['ngSanitize', 'textAngularSetup', 'textAngular.factories', 'textAngular.DOM', 'textAngular.validators', 'textAngular.taBind']); //This makes ngSanitize required

textAngular.config([function(){
	// clear taTools variable. Just catches testing and any other time that this config may run multiple times...
	angular.forEach(taTools, function(value, key){ delete taTools[key];	});
}]);

textAngular.run([function(){
	/* istanbul ignore next: not sure how to test this */
	// Require Rangy and rangy savedSelection module.
	if (typeof define === 'function' && define.amd) {
		// AMD. Register as an anonymous module.
		define(function(require) {
			window.rangy = require('rangy');
			window.rangy.saveSelection = require('rangy/lib/rangy-selectionsaverestore');
		});
	} else if (typeof require ==='function' && typeof module !== 'undefined' && typeof exports === 'object') {
		// Node/CommonJS style
		window.rangy = require('rangy');
		window.rangy.saveSelection = require('rangy/lib/rangy-selectionsaverestore');
	} else {
		// Ensure that rangy and rangy.saveSelection exists on the window (global scope).
		// TODO: Refactor so that the global scope is no longer used.
		if(!window.rangy){
			throw("rangy-core.js and rangy-selectionsaverestore.js are required for textAngular to work correctly, rangy-core is not yet loaded.");
		}else{
			window.rangy.init();
			if(!window.rangy.saveSelection){
				throw("rangy-selectionsaverestore.js is required for textAngular to work correctly.");
			}
		}
	}
}]);

textAngular.directive("textAngular", [
	'$compile', '$timeout', 'taOptions', 'taSelection', 'taExecCommand',
	'textAngularManager', '$window', '$document', '$animate', '$log', '$q', '$parse',
	function($compile, $timeout, taOptions, taSelection, taExecCommand,
		textAngularManager, $window, $document, $animate, $log, $q, $parse){
		return {
			require: '?ngModel',
			scope: {},
			restrict: "EA",
			priority: 2, // So we override validators correctly
			link: function(scope, element, attrs, ngModel){
				// all these vars should not be accessable outside this directive
				var _keydown, _keyup, _keypress, _mouseup, _focusin, _focusout,
					_originalContents, _toolbars,
					_serial = (attrs.serial) ? attrs.serial : Math.floor(Math.random() * 10000000000000000),
					_taExecCommand, _resizeMouseDown, _updateSelectedStylesTimeout;

				scope._name = (attrs.name) ? attrs.name : 'textAngularEditor' + _serial;

				var oneEvent = function(_element, event, action){
					$timeout(function(){
						// shim the .one till fixed
						var _func = function(){
							_element.off(event, _func);
							action.apply(this, arguments);
						};
						_element.on(event, _func);
					}, 100);
				};
				_taExecCommand = taExecCommand(attrs.taDefaultWrap);
				// get the settings from the defaults and add our specific functions that need to be on the scope
				angular.extend(scope, angular.copy(taOptions), {
					// wraps the selection in the provided tag / execCommand function. Should only be called in WYSIWYG mode.
					wrapSelection: function(command, opt, isSelectableElementTool){
						if(command.toLowerCase() === "undo"){
							scope['$undoTaBindtaTextElement' + _serial]();
						}else if(command.toLowerCase() === "redo"){
							scope['$redoTaBindtaTextElement' + _serial]();
						}else{
							// catch errors like FF erroring when you try to force an undo with nothing done
							_taExecCommand(command, false, opt, scope.defaultTagAttributes);
							if(isSelectableElementTool){
								// re-apply the selectable tool events
								scope['reApplyOnSelectorHandlerstaTextElement' + _serial]();
							}
							// refocus on the shown display element, this fixes a display bug when using :focus styles to outline the box.
							// You still have focus on the text/html input it just doesn't show up
							scope.displayElements.text[0].focus();
						}
					},
					showHtml: scope.$eval(attrs.taShowHtml) || false
				});
				// setup the options from the optional attributes
				if(attrs.taFocussedClass)			scope.classes.focussed = attrs.taFocussedClass;
				if(attrs.taTextEditorClass)			scope.classes.textEditor = attrs.taTextEditorClass;
				if(attrs.taHtmlEditorClass)			scope.classes.htmlEditor = attrs.taHtmlEditorClass;
				if(attrs.taDefaultTagAttributes){
					try	{
						//	TODO: This should use angular.merge to enhance functionality once angular 1.4 is required
						angular.extend(scope.defaultTagAttributes, angular.fromJson(attrs.taDefaultTagAttributes));
					} catch (error) {
						$log.error(error);
					}
				}
				// optional setup functions
				if(attrs.taTextEditorSetup)			scope.setup.textEditorSetup = scope.$parent.$eval(attrs.taTextEditorSetup);
				if(attrs.taHtmlEditorSetup)			scope.setup.htmlEditorSetup = scope.$parent.$eval(attrs.taHtmlEditorSetup);
				// optional fileDropHandler function
				if(attrs.taFileDrop)				scope.fileDropHandler = scope.$parent.$eval(attrs.taFileDrop);
				else								scope.fileDropHandler = scope.defaultFileDropHandler;

				_originalContents = element[0].innerHTML;
				// clear the original content
				element[0].innerHTML = '';

				// Setup the HTML elements as variable references for use later
				scope.displayElements = {
					// we still need the hidden input even with a textarea as the textarea may have invalid/old input in it,
					// wheras the input will ALLWAYS have the correct value.
					forminput: angular.element("<input type='hidden' tabindex='-1' style='display: none;'>"),
					html: angular.element("<textarea></textarea>"),
					text: angular.element("<div></div>"),
					// other toolbased elements
					scrollWindow: angular.element("<div class='ta-scroll-window'></div>"),
					popover: angular.element('<div class="popover fade bottom" style="max-width: none; width: 305px;"></div>'),
					popoverArrow: angular.element('<div class="arrow"></div>'),
					popoverContainer: angular.element('<div class="popover-content"></div>'),
					resize: {
						overlay: angular.element('<div class="ta-resizer-handle-overlay"></div>'),
						background: angular.element('<div class="ta-resizer-handle-background"></div>'),
						anchors: [
							angular.element('<div class="ta-resizer-handle-corner ta-resizer-handle-corner-tl"></div>'),
							angular.element('<div class="ta-resizer-handle-corner ta-resizer-handle-corner-tr"></div>'),
							angular.element('<div class="ta-resizer-handle-corner ta-resizer-handle-corner-bl"></div>'),
							angular.element('<div class="ta-resizer-handle-corner ta-resizer-handle-corner-br"></div>')
						],
						info: angular.element('<div class="ta-resizer-handle-info"></div>')
					}
				};

				// Setup the popover
				scope.displayElements.popover.append(scope.displayElements.popoverArrow);
				scope.displayElements.popover.append(scope.displayElements.popoverContainer);
				scope.displayElements.scrollWindow.append(scope.displayElements.popover);

				scope.displayElements.popover.on('mousedown', function(e, eventData){
					/* istanbul ignore else: this is for catching the jqLite testing*/
					if(eventData) angular.extend(e, eventData);
					// this prevents focusout from firing on the editor when clicking anything in the popover
					e.preventDefault();
					return false;
				});

				// define the popover show and hide functions
				scope.showPopover = function(_el){
					scope.displayElements.popover.css('display', 'block');
					scope.reflowPopover(_el);
					$animate.addClass(scope.displayElements.popover, 'in');
					oneEvent($document.find('body'), 'click keyup', function(){scope.hidePopover();});
				};
				scope.reflowPopover = function(_el){
					/* istanbul ignore if: catches only if near bottom of editor */
					if(scope.displayElements.text[0].offsetHeight - 51 > _el[0].offsetTop){
						scope.displayElements.popover.css('top', _el[0].offsetTop + _el[0].offsetHeight + scope.displayElements.scrollWindow[0].scrollTop + 'px');
						scope.displayElements.popover.removeClass('top').addClass('bottom');
					}else{
						scope.displayElements.popover.css('top', _el[0].offsetTop - 54 + scope.displayElements.scrollWindow[0].scrollTop + 'px');
						scope.displayElements.popover.removeClass('bottom').addClass('top');
					}
					var _maxLeft = scope.displayElements.text[0].offsetWidth - scope.displayElements.popover[0].offsetWidth;
					var _targetLeft = _el[0].offsetLeft + (_el[0].offsetWidth / 2.0) - (scope.displayElements.popover[0].offsetWidth / 2.0);
					scope.displayElements.popover.css('left', Math.max(0, Math.min(_maxLeft, _targetLeft)) + 'px');
					scope.displayElements.popoverArrow.css('margin-left', (Math.min(_targetLeft, (Math.max(0, _targetLeft - _maxLeft))) - 11) + 'px');
				};
				scope.hidePopover = function(){
					scope.displayElements.popover.css('display', '');
					scope.displayElements.popoverContainer.attr('style', '');
					scope.displayElements.popoverContainer.attr('class', 'popover-content');
					scope.displayElements.popover.removeClass('in');
				};

				// setup the resize overlay
				scope.displayElements.resize.overlay.append(scope.displayElements.resize.background);
				angular.forEach(scope.displayElements.resize.anchors, function(anchor){ scope.displayElements.resize.overlay.append(anchor);});
				scope.displayElements.resize.overlay.append(scope.displayElements.resize.info);
				scope.displayElements.scrollWindow.append(scope.displayElements.resize.overlay);

				// define the show and hide events
				scope.reflowResizeOverlay = function(_el){
					_el = angular.element(_el)[0];
					scope.displayElements.resize.overlay.css({
						'display': 'block',
						'left': _el.offsetLeft - 5 + 'px',
						'top': _el.offsetTop - 5 + 'px',
						'width': _el.offsetWidth + 10 + 'px',
						'height': _el.offsetHeight + 10 + 'px'
					});
					scope.displayElements.resize.info.text(_el.offsetWidth + ' x ' + _el.offsetHeight);
				};
				/* istanbul ignore next: pretty sure phantomjs won't test this */
				scope.showResizeOverlay = function(_el){
					var _body = $document.find('body');
					_resizeMouseDown = function(event){
						var startPosition = {
							width: parseInt(_el.attr('width')),
							height: parseInt(_el.attr('height')),
							x: event.clientX,
							y: event.clientY
						};
						if(startPosition.width === undefined || isNaN(startPosition.width)) startPosition.width = _el[0].offsetWidth;
						if(startPosition.height === undefined || isNaN(startPosition.height)) startPosition.height = _el[0].offsetHeight;
						scope.hidePopover();
						var ratio = startPosition.height / startPosition.width;
						var mousemove = function(event){
							// calculate new size
							var pos = {
								x: Math.max(0, startPosition.width + (event.clientX - startPosition.x)),
								y: Math.max(0, startPosition.height + (event.clientY - startPosition.y))
							};

							// DEFAULT: the aspect ratio is not locked unless the Shift key is pressed.
							//
							// attribute: ta-resize-force-aspect-ratio -- locks resize into maintaing the aspect ratio
							var bForceAspectRatio = (attrs.taResizeForceAspectRatio !== undefined);
							// attribute: ta-resize-maintain-aspect-ratio=true causes the space ratio to remain locked
							// unless the Shift key is pressed
							var bFlipKeyBinding = attrs.taResizeMaintainAspectRatio;
							var bKeepRatio =  bForceAspectRatio || (bFlipKeyBinding && !event.shiftKey);
							if(bKeepRatio) {
								var newRatio = pos.y / pos.x;
								pos.x = ratio > newRatio ? pos.x : pos.y / ratio;
								pos.y = ratio > newRatio ? pos.x * ratio : pos.y;
							}
							var el = angular.element(_el);
							function roundedMaxVal(val) {
								return Math.round(Math.max(0, val));
							}
							el.css('height', roundedMaxVal(pos.y) + 'px');
							el.css('width', roundedMaxVal(pos.x) + 'px');

							// reflow the popover tooltip
							scope.reflowResizeOverlay(_el);
						};
						_body.on('mousemove', mousemove);
						oneEvent(_body, 'mouseup', function(event){
							event.preventDefault();
							event.stopPropagation();
							_body.off('mousemove', mousemove);
							// at this point, we need to force the model to update! since the css has changed!
							// this fixes bug: #862 - we now hide the popover -- as this seems more consitent.
							// there are still issues under firefox, the window does not repaint. -- not sure
							// how best to resolve this, but clicking anywhere works.
							scope.$apply(function (){
								scope.hidePopover();
								scope.updateTaBindtaTextElement();
							}, 100);
						});
						event.stopPropagation();
						event.preventDefault();
					};

					scope.displayElements.resize.anchors[3].off('mousedown');
					scope.displayElements.resize.anchors[3].on('mousedown', _resizeMouseDown);

					scope.reflowResizeOverlay(_el);
					oneEvent(_body, 'click', function(){scope.hideResizeOverlay();});
				};
				/* istanbul ignore next: pretty sure phantomjs won't test this */
				scope.hideResizeOverlay = function(){
					scope.displayElements.resize.anchors[3].off('mousedown', _resizeMouseDown);
					scope.displayElements.resize.overlay.css('display', '');
				};

				// allow for insertion of custom directives on the textarea and div
				scope.setup.htmlEditorSetup(scope.displayElements.html);
				scope.setup.textEditorSetup(scope.displayElements.text);
				scope.displayElements.html.attr({
					'id': 'taHtmlElement' + _serial,
					'ng-show': 'showHtml',
					'ta-bind': 'ta-bind',
					'ng-model': 'html',
					'ng-model-options': element.attr('ng-model-options')
				});
				scope.displayElements.text.attr({
					'id': 'taTextElement' + _serial,
					'contentEditable': 'true',
					'ta-bind': 'ta-bind',
					'ng-model': 'html',
					'ng-model-options': element.attr('ng-model-options')
				});
				scope.displayElements.scrollWindow.attr({'ng-hide': 'showHtml'});
				if(attrs.taDefaultWrap) scope.displayElements.text.attr('ta-default-wrap', attrs.taDefaultWrap);

				if(attrs.taUnsafeSanitizer){
					scope.displayElements.text.attr('ta-unsafe-sanitizer', attrs.taUnsafeSanitizer);
					scope.displayElements.html.attr('ta-unsafe-sanitizer', attrs.taUnsafeSanitizer);
				}

				// add the main elements to the origional element
				scope.displayElements.scrollWindow.append(scope.displayElements.text);
				element.append(scope.displayElements.scrollWindow);
				element.append(scope.displayElements.html);

				scope.displayElements.forminput.attr('name', scope._name);
				element.append(scope.displayElements.forminput);

				if(attrs.tabindex){
					element.removeAttr('tabindex');
					scope.displayElements.text.attr('tabindex', attrs.tabindex);
					scope.displayElements.html.attr('tabindex', attrs.tabindex);
				}

				if (attrs.placeholder) {
					scope.displayElements.text.attr('placeholder', attrs.placeholder);
					scope.displayElements.html.attr('placeholder', attrs.placeholder);
				}

				if(attrs.taDisabled){
					scope.displayElements.text.attr('ta-readonly', 'disabled');
					scope.displayElements.html.attr('ta-readonly', 'disabled');
					scope.disabled = scope.$parent.$eval(attrs.taDisabled);
					scope.$parent.$watch(attrs.taDisabled, function(newVal){
						scope.disabled = newVal;
						if(scope.disabled){
							element.addClass(scope.classes.disabled);
						}else{
							element.removeClass(scope.classes.disabled);
						}
					});
				}

				if(attrs.taPaste){
					scope._pasteHandler = function(_html){
						return $parse(attrs.taPaste)(scope.$parent, {$html: _html});
					};
					scope.displayElements.text.attr('ta-paste', '_pasteHandler($html)');
				}

				// compile the scope with the text and html elements only - if we do this with the main element it causes a compile loop
				$compile(scope.displayElements.scrollWindow)(scope);
				$compile(scope.displayElements.html)(scope);

				scope.updateTaBindtaTextElement = scope['updateTaBindtaTextElement' + _serial];
				scope.updateTaBindtaHtmlElement = scope['updateTaBindtaHtmlElement' + _serial];

				// add the classes manually last
				element.addClass("ta-root");
				scope.displayElements.scrollWindow.addClass("ta-text ta-editor " + scope.classes.textEditor);
				scope.displayElements.html.addClass("ta-html ta-editor " + scope.classes.htmlEditor);

				// used in the toolbar actions
				scope._actionRunning = false;
				var _savedSelection = false;
				scope.startAction = function(){
					scope._actionRunning = true;
					// if rangy library is loaded return a function to reload the current selection
					_savedSelection = $window.rangy.saveSelection();
					return function(){
						if(_savedSelection) $window.rangy.restoreSelection(_savedSelection);
					};
				};
				scope.endAction = function(){
					scope._actionRunning = false;
					if(_savedSelection){
						if(scope.showHtml){
							scope.displayElements.html[0].focus();
						}else{
							scope.displayElements.text[0].focus();
						}
						// $window.rangy.restoreSelection(_savedSelection);
						$window.rangy.removeMarkers(_savedSelection);
					}
					_savedSelection = false;
					scope.updateSelectedStyles();
					// only update if in text or WYSIWYG mode
					if(!scope.showHtml) scope['updateTaBindtaTextElement' + _serial]();
				};

				// note that focusout > focusin is called everytime we click a button - except bad support: http://www.quirksmode.org/dom/events/blurfocus.html
				// cascades to displayElements.text and displayElements.html automatically.
				_focusin = function(){
					scope.focussed = true;
					element.addClass(scope.classes.focussed);
					_toolbars.focus();
					element.triggerHandler('focus');
				};
				scope.displayElements.html.on('focus', _focusin);
				scope.displayElements.text.on('focus', _focusin);
				_focusout = function(e){
					// if we are NOT runnig an action and have NOT focussed again on the text etc then fire the blur events
					if(!scope._actionRunning && $document[0].activeElement !== scope.displayElements.html[0] && $document[0].activeElement !== scope.displayElements.text[0]){
						element.removeClass(scope.classes.focussed);
						_toolbars.unfocus();
						// to prevent multiple apply error defer to next seems to work.
						$timeout(function(){
							scope._bUpdateSelectedStyles = false;
							element.triggerHandler('blur');
							scope.focussed = false;
						}, 0);
					}
					e.preventDefault();
					return false;
				};
				scope.displayElements.html.on('blur', _focusout);
				scope.displayElements.text.on('blur', _focusout);

				scope.displayElements.text.on('paste', function(event){
					element.triggerHandler('paste', event);
				});

				// Setup the default toolbar tools, this way allows the user to add new tools like plugins.
				// This is on the editor for future proofing if we find a better way to do this.
				scope.queryFormatBlockState = function(command){
					// $document[0].queryCommandValue('formatBlock') errors in Firefox if we call this when focussed on the textarea
					return !scope.showHtml && command.toLowerCase() === $document[0].queryCommandValue('formatBlock').toLowerCase();
				};
				scope.queryCommandState = function(command){
					// $document[0].queryCommandValue('formatBlock') errors in Firefox if we call this when focussed on the textarea
					return (!scope.showHtml) ? $document[0].queryCommandState(command) : '';
				};
				scope.switchView = function(){
					scope.showHtml = !scope.showHtml;
					$animate.enabled(false, scope.displayElements.html);
					$animate.enabled(false, scope.displayElements.text);
					//Show the HTML view
					if(scope.showHtml){
						//defer until the element is visible
						$timeout(function(){
							$animate.enabled(true, scope.displayElements.html);
							$animate.enabled(true, scope.displayElements.text);
							// [0] dereferences the DOM object from the angular.element
							return scope.displayElements.html[0].focus();
						}, 100);
					}else{
						//Show the WYSIWYG view
						//defer until the element is visible
						$timeout(function(){
							$animate.enabled(true, scope.displayElements.html);
							$animate.enabled(true, scope.displayElements.text);
							// [0] dereferences the DOM object from the angular.element
							return scope.displayElements.text[0].focus();
						}, 100);
					}
				};

				// changes to the model variable from outside the html/text inputs
				// if no ngModel, then the only input is from inside text-angular
				if(attrs.ngModel){
					var _firstRun = true;
					ngModel.$render = function(){
						if(_firstRun){
							// we need this firstRun to set the originalContents otherwise it gets overrided by the setting of ngModel to undefined from NaN
							_firstRun = false;
							// if view value is null or undefined initially and there was original content, set to the original content
							var _initialValue = scope.$parent.$eval(attrs.ngModel);
							if((_initialValue === undefined || _initialValue === null) && (_originalContents && _originalContents !== '')){
								// on passing through to taBind it will be sanitised
								ngModel.$setViewValue(_originalContents);
							}
						}
						scope.displayElements.forminput.val(ngModel.$viewValue);
						// if the editors aren't focused they need to be updated, otherwise they are doing the updating
						scope.html = ngModel.$viewValue || '';
					};
					// trigger the validation calls
					if(element.attr('required')) ngModel.$validators.required = function(modelValue, viewValue) {
						var value = modelValue || viewValue;
						return !(!value || value.trim() === '');
					};
				}else{
					// if no ngModel then update from the contents of the origional html.
					scope.displayElements.forminput.val(_originalContents);
					scope.html = _originalContents;
				}

				// changes from taBind back up to here
				scope.$watch('html', function(newValue, oldValue){
					if(newValue !== oldValue){
						if(attrs.ngModel && ngModel.$viewValue !== newValue) ngModel.$setViewValue(newValue);
						scope.displayElements.forminput.val(newValue);
					}
				});

				if(attrs.taTargetToolbars) _toolbars = textAngularManager.registerEditor(scope._name, scope, attrs.taTargetToolbars.split(','));
				else{
					var _toolbar = angular.element('<div text-angular-toolbar name="textAngularToolbar' + _serial + '">');
					// passthrough init of toolbar options
					if(attrs.taToolbar)						_toolbar.attr('ta-toolbar', attrs.taToolbar);
					if(attrs.taToolbarClass)				_toolbar.attr('ta-toolbar-class', attrs.taToolbarClass);
					if(attrs.taToolbarGroupClass)			_toolbar.attr('ta-toolbar-group-class', attrs.taToolbarGroupClass);
					if(attrs.taToolbarButtonClass)			_toolbar.attr('ta-toolbar-button-class', attrs.taToolbarButtonClass);
					if(attrs.taToolbarActiveButtonClass)	_toolbar.attr('ta-toolbar-active-button-class', attrs.taToolbarActiveButtonClass);
					if(attrs.taFocussedClass)				_toolbar.attr('ta-focussed-class', attrs.taFocussedClass);

					element.prepend(_toolbar);
					$compile(_toolbar)(scope.$parent);
					_toolbars = textAngularManager.registerEditor(scope._name, scope, ['textAngularToolbar' + _serial]);
				}

				scope.$on('$destroy', function(){
					textAngularManager.unregisterEditor(scope._name);
					angular.element(window).off('blur');
				});

				// catch element select event and pass to toolbar tools
				scope.$on('ta-element-select', function(event, element){
					if(_toolbars.triggerElementSelect(event, element)){
						scope['reApplyOnSelectorHandlerstaTextElement' + _serial]();
					}
				});

				scope.$on('ta-drop-event', function(event, element, dropEvent, dataTransfer){
					scope.displayElements.text[0].focus();
					if(dataTransfer && dataTransfer.files && dataTransfer.files.length > 0){
						angular.forEach(dataTransfer.files, function(file){
							// taking advantage of boolean execution, if the fileDropHandler returns true, nothing else after it is executed
							// If it is false then execute the defaultFileDropHandler if the fileDropHandler is NOT the default one
							// Once one of these has been executed wrap the result as a promise, if undefined or variable update the taBind, else we should wait for the promise
							try{
								$q.when(scope.fileDropHandler(file, scope.wrapSelection) ||
									(scope.fileDropHandler !== scope.defaultFileDropHandler &&
									$q.when(scope.defaultFileDropHandler(file, scope.wrapSelection)))).then(function(){
										scope['updateTaBindtaTextElement' + _serial]();
									});
							}catch(error){
								$log.error(error);
							}
						});
						dropEvent.preventDefault();
						dropEvent.stopPropagation();
					/* istanbul ignore else, the updates if moved text */
					}else{
						$timeout(function(){
							scope['updateTaBindtaTextElement' + _serial]();
						}, 0);
					}
				});

				// the following is for applying the active states to the tools that support it
				scope._bUpdateSelectedStyles = false;
				/* istanbul ignore next: browser window/tab leave check */
				angular.element(window).on('blur', function(){
					scope._bUpdateSelectedStyles = false;
					scope.focussed = false;
				});
				// loop through all the tools polling their activeState function if it exists
				scope.updateSelectedStyles = function(){
					var _selection;
					/* istanbul ignore next: This check is to ensure multiple timeouts don't exist */
					if(_updateSelectedStylesTimeout) $timeout.cancel(_updateSelectedStylesTimeout);
					// test if the common element ISN'T the root ta-text node
					if((_selection = taSelection.getSelectionElement()) !== undefined && _selection.parentNode !== scope.displayElements.text[0]){
						_toolbars.updateSelectedStyles(angular.element(_selection));
					}else _toolbars.updateSelectedStyles();
					// used to update the active state when a key is held down, ie the left arrow
					/* istanbul ignore else: browser only check */
					if(scope._bUpdateSelectedStyles) _updateSelectedStylesTimeout = $timeout(scope.updateSelectedStyles, 200);
				};
				// start updating on keydown
				_keydown = function(){
					/* istanbul ignore next: ie catch */
					if(!scope.focussed){
						scope._bUpdateSelectedStyles = false;
						return;
					}
					/* istanbul ignore else: don't run if already running */
					if(!scope._bUpdateSelectedStyles){
						scope._bUpdateSelectedStyles = true;
						scope.$apply(function(){
							scope.updateSelectedStyles();
						});
					}
				};
				scope.displayElements.html.on('keydown', _keydown);
				scope.displayElements.text.on('keydown', _keydown);
				// stop updating on key up and update the display/model
				_keyup = function(){
					scope._bUpdateSelectedStyles = false;
				};
				scope.displayElements.html.on('keyup', _keyup);
				scope.displayElements.text.on('keyup', _keyup);
				// stop updating on key up and update the display/model
				_keypress = function(event, eventData){
					/* istanbul ignore else: this is for catching the jqLite testing*/
					if(eventData) angular.extend(event, eventData);
					scope.$apply(function(){
						if(_toolbars.sendKeyCommand(event)){
							/* istanbul ignore else: don't run if already running */
							if(!scope._bUpdateSelectedStyles){
								scope.updateSelectedStyles();
							}
							event.preventDefault();
							return false;
						}
					});
				};
				scope.displayElements.html.on('keypress', _keypress);
				scope.displayElements.text.on('keypress', _keypress);
				// update the toolbar active states when we click somewhere in the text/html boxed
				_mouseup = function(){
					// ensure only one execution of updateSelectedStyles()
					scope._bUpdateSelectedStyles = false;
					scope.$apply(function(){
						scope.updateSelectedStyles();
					});
				};
				scope.displayElements.html.on('mouseup', _mouseup);
				scope.displayElements.text.on('mouseup', _mouseup);
			}
		};
	}
]);
textAngular.service('textAngularManager', ['taToolExecuteAction', 'taTools', 'taRegisterTool', function(taToolExecuteAction, taTools, taRegisterTool){
	// this service is used to manage all textAngular editors and toolbars.
	// All publicly published functions that modify/need to access the toolbar or editor scopes should be in here
	// these contain references to all the editors and toolbars that have been initialised in this app
	var toolbars = {}, editors = {};
	// when we focus into a toolbar, we need to set the TOOLBAR's $parent to be the toolbars it's linked to.
	// We also need to set the tools to be updated to be the toolbars...
	return {
		// register an editor and the toolbars that it is affected by
		registerEditor: function(name, scope, targetToolbars){
			// targetToolbars are optional, we don't require a toolbar to function
			if(!name || name === '') throw('textAngular Error: An editor requires a name');
			if(!scope) throw('textAngular Error: An editor requires a scope');
			if(editors[name]) throw('textAngular Error: An Editor with name "' + name + '" already exists');
			// _toolbars is an ARRAY of toolbar scopes
			var _toolbars = [];
			angular.forEach(targetToolbars, function(_name){
				if(toolbars[_name]) _toolbars.push(toolbars[_name]);
				// if it doesn't exist it may not have been compiled yet and it will be added later
			});
			editors[name] = {
				scope: scope,
				toolbars: targetToolbars,
				_registerToolbar: function(toolbarScope){
					// add to the list late
					if(this.toolbars.indexOf(toolbarScope.name) >= 0) _toolbars.push(toolbarScope);
				},
				// this is a suite of functions the editor should use to update all it's linked toolbars
				editorFunctions: {
					disable: function(){
						// disable all linked toolbars
						angular.forEach(_toolbars, function(toolbarScope){ toolbarScope.disabled = true; });
					},
					enable: function(){
						// enable all linked toolbars
						angular.forEach(_toolbars, function(toolbarScope){ toolbarScope.disabled = false; });
					},
					focus: function(){
						// this should be called when the editor is focussed
						angular.forEach(_toolbars, function(toolbarScope){
							toolbarScope._parent = scope;
							toolbarScope.disabled = false;
							toolbarScope.focussed = true;
							scope.focussed = true;
						});
					},
					unfocus: function(){
						// this should be called when the editor becomes unfocussed
						angular.forEach(_toolbars, function(toolbarScope){
							toolbarScope.disabled = true;
							toolbarScope.focussed = false;
						});
						scope.focussed = false;
					},
					updateSelectedStyles: function(selectedElement){
						// update the active state of all buttons on liked toolbars
						angular.forEach(_toolbars, function(toolbarScope){
							angular.forEach(toolbarScope.tools, function(toolScope){
								if(toolScope.activeState){
									toolbarScope._parent = scope;
									toolScope.active = toolScope.activeState(selectedElement);
								}
							});
						});
					},
					sendKeyCommand: function(event){
						// we return true if we applied an action, false otherwise
						var result = false;
						if(event.ctrlKey || event.metaKey || event.specialKey) angular.forEach(taTools, function(tool, name){
							if(tool.commandKeyCode && (tool.commandKeyCode === event.which || tool.commandKeyCode === event.specialKey)){
								for(var _t = 0; _t < _toolbars.length; _t++){
									if(_toolbars[_t].tools[name] !== undefined){
										taToolExecuteAction.call(_toolbars[_t].tools[name], scope);
										result = true;
										break;
									}
								}
							}
						});
						return result;
					},
					triggerElementSelect: function(event, element){
						// search through the taTools to see if a match for the tag is made.
						// if there is, see if the tool is on a registered toolbar and not disabled.
						// NOTE: This can trigger on MULTIPLE tools simultaneously.
						var elementHasAttrs = function(_element, attrs){
							var result = true;
							for(var i = 0; i < attrs.length; i++) result = result && _element.attr(attrs[i]);
							return result;
						};
						var workerTools = [];
						var unfilteredTools = {};
						var result = false;
						element = angular.element(element);
						// get all valid tools by element name, keep track if one matches the
						var onlyWithAttrsFilter = false;
						angular.forEach(taTools, function(tool, name){
							if(
								tool.onElementSelect &&
								tool.onElementSelect.element &&
								tool.onElementSelect.element.toLowerCase() === element[0].tagName.toLowerCase() &&
								(!tool.onElementSelect.filter || tool.onElementSelect.filter(element))
							){
								// this should only end up true if the element matches the only attributes
								onlyWithAttrsFilter = onlyWithAttrsFilter ||
									(angular.isArray(tool.onElementSelect.onlyWithAttrs) && elementHasAttrs(element, tool.onElementSelect.onlyWithAttrs));
								if(!tool.onElementSelect.onlyWithAttrs || elementHasAttrs(element, tool.onElementSelect.onlyWithAttrs)) unfilteredTools[name] = tool;
							}
						});
						// if we matched attributes to filter on, then filter, else continue
						if(onlyWithAttrsFilter){
							angular.forEach(unfilteredTools, function(tool, name){
								if(tool.onElementSelect.onlyWithAttrs && elementHasAttrs(element, tool.onElementSelect.onlyWithAttrs)) workerTools.push({'name': name, 'tool': tool});
							});
							// sort most specific (most attrs to find) first
							workerTools.sort(function(a,b){
								return b.tool.onElementSelect.onlyWithAttrs.length - a.tool.onElementSelect.onlyWithAttrs.length;
							});
						}else{
							angular.forEach(unfilteredTools, function(tool, name){
								workerTools.push({'name': name, 'tool': tool});
							});
						}
						// Run the actions on the first visible filtered tool only
						if(workerTools.length > 0){
							for(var _i = 0; _i < workerTools.length; _i++){
								var tool = workerTools[_i].tool;
								var name = workerTools[_i].name;
								for(var _t = 0; _t < _toolbars.length; _t++){
									if(_toolbars[_t].tools[name] !== undefined){
										tool.onElementSelect.action.call(_toolbars[_t].tools[name], event, element, scope);
										result = true;
										break;
									}
								}
								if(result) break;
							}
						}
						return result;
					}
				}
			};
			return editors[name].editorFunctions;
		},
		// retrieve editor by name, largely used by testing suites only
		retrieveEditor: function(name){
			return editors[name];
		},
		unregisterEditor: function(name){
			delete editors[name];
		},
		// registers a toolbar such that it can be linked to editors
		registerToolbar: function(scope){
			if(!scope) throw('textAngular Error: A toolbar requires a scope');
			if(!scope.name || scope.name === '') throw('textAngular Error: A toolbar requires a name');
			if(toolbars[scope.name]) throw('textAngular Error: A toolbar with name "' + scope.name + '" already exists');
			toolbars[scope.name] = scope;
			angular.forEach(editors, function(_editor){
				_editor._registerToolbar(scope);
			});
		},
		// retrieve toolbar by name, largely used by testing suites only
		retrieveToolbar: function(name){
			return toolbars[name];
		},
		// retrieve toolbars by editor name, largely used by testing suites only
		retrieveToolbarsViaEditor: function(name){
			var result = [], _this = this;
			angular.forEach(this.retrieveEditor(name).toolbars, function(name){
				result.push(_this.retrieveToolbar(name));
			});
			return result;
		},
		unregisterToolbar: function(name){
			delete toolbars[name];
		},
		// functions for updating the toolbar buttons display
		updateToolsDisplay: function(newTaTools){
			// pass a partial struct of the taTools, this allows us to update the tools on the fly, will not change the defaults.
			var _this = this;
			angular.forEach(newTaTools, function(_newTool, key){
				_this.updateToolDisplay(key, _newTool);
			});
		},
		// this function resets all toolbars to their default tool definitions
		resetToolsDisplay: function(){
			var _this = this;
			angular.forEach(taTools, function(_newTool, key){
				_this.resetToolDisplay(key);
			});
		},
		// update a tool on all toolbars
		updateToolDisplay: function(toolKey, _newTool){
			var _this = this;
			angular.forEach(toolbars, function(toolbarScope, toolbarKey){
				_this.updateToolbarToolDisplay(toolbarKey, toolKey, _newTool);
			});
		},
		// resets a tool to the default/starting state on all toolbars
		resetToolDisplay: function(toolKey){
			var _this = this;
			angular.forEach(toolbars, function(toolbarScope, toolbarKey){
				_this.resetToolbarToolDisplay(toolbarKey, toolKey);
			});
		},
		// update a tool on a specific toolbar
		updateToolbarToolDisplay: function(toolbarKey, toolKey, _newTool){
			if(toolbars[toolbarKey]) toolbars[toolbarKey].updateToolDisplay(toolKey, _newTool);
			else throw('textAngular Error: No Toolbar with name "' + toolbarKey + '" exists');
		},
		// reset a tool on a specific toolbar to it's default starting value
		resetToolbarToolDisplay: function(toolbarKey, toolKey){
			if(toolbars[toolbarKey]) toolbars[toolbarKey].updateToolDisplay(toolKey, taTools[toolKey], true);
			else throw('textAngular Error: No Toolbar with name "' + toolbarKey + '" exists');
		},
		// removes a tool from all toolbars and it's definition
		removeTool: function(toolKey){
			delete taTools[toolKey];
			angular.forEach(toolbars, function(toolbarScope){
				delete toolbarScope.tools[toolKey];
				for(var i = 0; i < toolbarScope.toolbar.length; i++){
					var toolbarIndex;
					for(var j = 0; j < toolbarScope.toolbar[i].length; j++){
						if(toolbarScope.toolbar[i][j] === toolKey){
							toolbarIndex = {
								group: i,
								index: j
							};
							break;
						}
						if(toolbarIndex !== undefined) break;
					}
					if(toolbarIndex !== undefined){
						toolbarScope.toolbar[toolbarIndex.group].slice(toolbarIndex.index, 1);
						toolbarScope._$element.children().eq(toolbarIndex.group).children().eq(toolbarIndex.index).remove();
					}
				}
			});
		},
		// toolkey, toolDefinition are required. If group is not specified will pick the last group, if index isnt defined will append to group
		addTool: function(toolKey, toolDefinition, group, index){
			taRegisterTool(toolKey, toolDefinition);
			angular.forEach(toolbars, function(toolbarScope){
				toolbarScope.addTool(toolKey, toolDefinition, group, index);
			});
		},
		// adds a Tool but only to one toolbar not all
		addToolToToolbar: function(toolKey, toolDefinition, toolbarKey, group, index){
			taRegisterTool(toolKey, toolDefinition);
			toolbars[toolbarKey].addTool(toolKey, toolDefinition, group, index);
		},
		// this is used when externally the html of an editor has been changed and textAngular needs to be notified to update the model.
		// this will call a $digest if not already happening
		refreshEditor: function(name){
			if(editors[name]){
				editors[name].scope.updateTaBindtaTextElement();
				/* istanbul ignore else: phase catch */
				if(!editors[name].scope.$$phase) editors[name].scope.$digest();
			}else throw('textAngular Error: No Editor with name "' + name + '" exists');
		},
		// this is used by taBind to send a key command in response to a special key event
		sendKeyCommand: function(scope, event){
			var _editor = editors[scope._name];
			/* istanbul ignore else: if nothing to do, do nothing */
			if (_editor && _editor.editorFunctions.sendKeyCommand(event)) {
				/* istanbul ignore else: don't run if already running */
				if(!scope._bUpdateSelectedStyles){
					scope.updateSelectedStyles();
				}
				event.preventDefault();
				return false;
			}
		}
	};
}]);
textAngular.directive('textAngularToolbar', [
	'$compile', 'textAngularManager', 'taOptions', 'taTools', 'taToolExecuteAction', '$window',
	function($compile, textAngularManager, taOptions, taTools, taToolExecuteAction, $window){
		return {
			scope: {
				name: '@' // a name IS required
			},
			restrict: "EA",
			link: function(scope, element, attrs){
				if(!scope.name || scope.name === '') throw('textAngular Error: A toolbar requires a name');
				angular.extend(scope, angular.copy(taOptions));
				if(attrs.taToolbar)						scope.toolbar = scope.$parent.$eval(attrs.taToolbar);
				if(attrs.taToolbarClass)				scope.classes.toolbar = attrs.taToolbarClass;
				if(attrs.taToolbarGroupClass)			scope.classes.toolbarGroup = attrs.taToolbarGroupClass;
				if(attrs.taToolbarButtonClass)			scope.classes.toolbarButton = attrs.taToolbarButtonClass;
				if(attrs.taToolbarActiveButtonClass)	scope.classes.toolbarButtonActive = attrs.taToolbarActiveButtonClass;
				if(attrs.taFocussedClass)				scope.classes.focussed = attrs.taFocussedClass;

				scope.disabled = true;
				scope.focussed = false;
				scope._$element = element;
				element[0].innerHTML = '';
				element.addClass("ta-toolbar " + scope.classes.toolbar);

				scope.$watch('focussed', function(){
					if(scope.focussed) element.addClass(scope.classes.focussed);
					else element.removeClass(scope.classes.focussed);
				});

				var setupToolElement = function(toolDefinition, toolScope){
					var toolElement;
					if(toolDefinition && toolDefinition.display){
						toolElement = angular.element(toolDefinition.display);
					}
					else toolElement = angular.element("<button type='button'>");

					if(toolDefinition && toolDefinition["class"]) toolElement.addClass(toolDefinition["class"]);
					else toolElement.addClass(scope.classes.toolbarButton);

					toolElement.attr('name', toolScope.name);
					// important to not take focus from the main text/html entry
					toolElement.attr('ta-button', 'ta-button');
					toolElement.attr('ng-disabled', 'isDisabled()');
					toolElement.attr('tabindex', '-1');
					toolElement.attr('ng-click', 'executeAction()');
					toolElement.attr('ng-class', 'displayActiveToolClass(active)');

					if (toolDefinition && toolDefinition.tooltiptext) {
						toolElement.attr('title', toolDefinition.tooltiptext);
					}
					if(toolDefinition && !toolDefinition.display && !toolScope._display){
						// first clear out the current contents if any
						toolElement[0].innerHTML = '';
						// add the buttonText
						if(toolDefinition.buttontext) toolElement[0].innerHTML = toolDefinition.buttontext;
						// add the icon to the front of the button if there is content
						if(toolDefinition.iconclass){
							var icon = angular.element('<i>'), content = toolElement[0].innerHTML;
							icon.addClass(toolDefinition.iconclass);
							toolElement[0].innerHTML = '';
							toolElement.append(icon);
							if(content && content !== '') toolElement.append('&nbsp;' + content);
						}
					}

					toolScope._lastToolDefinition = angular.copy(toolDefinition);

					return $compile(toolElement)(toolScope);
				};

				// Keep a reference for updating the active states later
				scope.tools = {};
				// create the tools in the toolbar
				// default functions and values to prevent errors in testing and on init
				scope._parent = {
					disabled: true,
					showHtml: false,
					queryFormatBlockState: function(){ return false; },
					queryCommandState: function(){ return false; }
				};
				var defaultChildScope = {
					$window: $window,
					$editor: function(){
						// dynamically gets the editor as it is set
						return scope._parent;
					},
					isDisabled: function(){
						// to set your own disabled logic set a function or boolean on the tool called 'disabled'
						return ( // this bracket is important as without it it just returns the first bracket and ignores the rest
							// when the button's disabled function/value evaluates to true
							(typeof this.$eval('disabled') !== 'function' && this.$eval('disabled')) || this.$eval('disabled()') ||
							// all buttons except the HTML Switch button should be disabled in the showHtml (RAW html) mode
							(this.name !== 'html' && this.$editor().showHtml) ||
							// if the toolbar is disabled
							this.$parent.disabled ||
							// if the current editor is disabled
							this.$editor().disabled
						);
					},
					displayActiveToolClass: function(active){
						return (active)? scope.classes.toolbarButtonActive : '';
					},
					executeAction: taToolExecuteAction
				};

				angular.forEach(scope.toolbar, function(group){
					// setup the toolbar group
					var groupElement = angular.element("<div>");
					groupElement.addClass(scope.classes.toolbarGroup);
					angular.forEach(group, function(tool){
						// init and add the tools to the group
						// a tool name (key name from taTools struct)
						//creates a child scope of the main angularText scope and then extends the childScope with the functions of this particular tool
						// reference to the scope and element kept
						scope.tools[tool] = angular.extend(scope.$new(true), taTools[tool], defaultChildScope, {name: tool});
						scope.tools[tool].$element = setupToolElement(taTools[tool], scope.tools[tool]);
						// append the tool compiled with the childScope to the group element
						groupElement.append(scope.tools[tool].$element);
					});
					// append the group to the toolbar
					element.append(groupElement);
				});

				// update a tool
				// if a value is set to null, remove from the display
				// when forceNew is set to true it will ignore all previous settings, used to reset to taTools definition
				// to reset to defaults pass in taTools[key] as _newTool and forceNew as true, ie `updateToolDisplay(key, taTools[key], true);`
				scope.updateToolDisplay = function(key, _newTool, forceNew){
					var toolInstance = scope.tools[key];
					if(toolInstance){
						// get the last toolDefinition, then override with the new definition
						if(toolInstance._lastToolDefinition && !forceNew) _newTool = angular.extend({}, toolInstance._lastToolDefinition, _newTool);
						if(_newTool.buttontext === null && _newTool.iconclass === null && _newTool.display === null)
							throw('textAngular Error: Tool Definition for updating "' + key + '" does not have a valid display/iconclass/buttontext value');

						// if tool is defined on this toolbar, update/redo the tool
						if(_newTool.buttontext === null){
							delete _newTool.buttontext;
						}
						if(_newTool.iconclass === null){
							delete _newTool.iconclass;
						}
						if(_newTool.display === null){
							delete _newTool.display;
						}

						var toolElement = setupToolElement(_newTool, toolInstance);
						toolInstance.$element.replaceWith(toolElement);
						toolInstance.$element = toolElement;
					}
				};

				// we assume here that all values passed are valid and correct
				scope.addTool = function(key, _newTool, groupIndex, index){
					scope.tools[key] = angular.extend(scope.$new(true), taTools[key], defaultChildScope, {name: key});
					scope.tools[key].$element = setupToolElement(taTools[key], scope.tools[key]);
					var group;
					if(groupIndex === undefined) groupIndex = scope.toolbar.length - 1;
					group = angular.element(element.children()[groupIndex]);

					if(index === undefined){
						group.append(scope.tools[key].$element);
						scope.toolbar[groupIndex][scope.toolbar[groupIndex].length - 1] = key;
					}else{
						group.children().eq(index).after(scope.tools[key].$element);
						scope.toolbar[groupIndex][index] = key;
					}
				};

				textAngularManager.registerToolbar(scope);

				scope.$on('$destroy', function(){
					textAngularManager.unregisterToolbar(scope.name);
				});
			}
		};
	}
]);
})();