(function () {
    angular
        .module('directives.clamp', [])
        .directive('clamp', clampDirective);

    clampDirective.$inject = ['$timeout'];

    function clampDirective($timeout) {
        var clampDiv,
            clampCss;
        var directive = {
            restrict: 'A',
            link: linkDirective
        };

        return directive;

        function linkDirective(scope, element, attrs) {
            // Setup clamp div if not
            if ($('#clamp-directive').length) {
                clampDiv = $('#clamp-directive');
            } else {
                clampDiv = $('<div id="clamp-directive" />').css({
                    position: 'fixed',
                    bottom: 0,
                    left: 0,
                    color: 'transparent',
                    padding: 0,
                    margin: 0
                });
                $('body').append(clampDiv);
            }

            $timeout(function () {
                var lineCount = 1,
                    lineMax = +attrs.clamp;
                var lineStart = 0,
                    lineEnd = 0;
                var text = element.html().replace(/\n/g, ' ') + ' '; // add space at end, if not last word not detected
                // var maxWidth = element[0].offsetWidth;
                var maxWidth = element.width() || element.parent().width()
                var estimateTag = createElement();


                clampCss = getCssElement(element);

                // Add gutter padding
                maxWidth -= (parseInt(clampCss['padding-left'], 10) + parseInt(clampCss['padding-right'], 10));

                // Add clamp-width="max width" to force width
                if (attrs.clampWidth) maxWidth = +attrs.clampWidth;

                estimateTag.css(clampCss);
                element.empty();
                clampDiv.append(estimateTag);

                text.replace(/ /g, function (m, pos) {
                    estimateTag.html(text.slice(lineStart, pos));
                    if (estimateTag.width() >= maxWidth && lineCount < lineMax) {
                        var clampTag = createElement();
                        resetElement(clampTag);
                        clampTag.html(text.slice(lineStart, lineEnd));
                        element.append(clampTag);

                        lineCount++;
                        lineStart = lineEnd + 1;
                        estimateTag.css('text-indent', 'initial');
                    }
                    lineEnd = pos;

                    if ((pos + 1) >= text.length) {
                        var clampTag = createElement();
                        resetElement(clampTag, true);
			if(lineStart === 1){
				clampTag.html(text);
			}else{
				clampTag.html(text.slice(lineStart));
			}
                        element.append(clampTag);
                        estimateTag.remove();
                    }
                });
                estimateTag.html(text.slice(lineStart));
                resetElement(estimateTag, true);

                scope.$emit('clampCallback', element, attrs);
            });
        }
    }

    return;

    function getCssElement(element) {
        return {
            'font-family': getComputedStyle(element, 'font-family'),
            'font-size': getComputedStyle(element, 'font-size'),
            'font-weight': getComputedStyle(element, 'font-weight'),
            'text-indent': getComputedStyle(element, 'text-indent'),
            'letter-spacing': getComputedStyle(element, 'letter-spacing'),
            'word-spacing': getComputedStyle(element, 'word-spacing'),
            'padding-left': getComputedStyle(element, 'padding-left'),
            'padding-right': getComputedStyle(element, 'padding-right')
        };
    }

    function getComputedStyle(element, property) {
        var el = element[0];
        if (el.style[property])
            return el.style[property];
        if (window.getComputedStyle)
            return document.defaultView.getComputedStyle(el).getPropertyValue(property);
        return;
    }

    function createElement() {
        var tagDiv = document.createElement('div');
        (function (s) {
            // s.position = 'absolute';
            s.whiteSpace = 'pre';
            s.visibility = 'hidden';
            s.display = 'inline-block';
        })(tagDiv.style);

        return angular.element(tagDiv);
    }

    function resetElement(element, type) {
        element.css({
            // position: 'inherit',
            overflow: 'hidden',
            display: 'block',
            textOverflow: (type ? 'ellipsis' : 'clip'),
            visibility: 'inherit',
            whiteSpace: 'nowrap',
            width: '100%'
        });
    }
})();
