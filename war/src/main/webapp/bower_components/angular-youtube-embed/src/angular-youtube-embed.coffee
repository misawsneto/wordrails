keats = angular.module 'keats.youtube', []

keats.directive 'youtube', ->
    directiveObject =
        template: '<div class="youtube-wrapper"><a class="youtube-link" target="_blank"></a></div>'
        replace: true
        restrict: 'E'

        link: (scope, elem, attrs) ->
            # set the background images for the preloader
            videoImage = 'http://img.youtube.com/vi/' + attrs.video + '/0.jpg'
            playImage = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFUAAAA8CAYAAAAXDvbIAAAABmJLR0QA/wD/AP+gvaeTAAAACXBIWXMAAABIAAAASABGyWs+AAAACXZwQWcAAABVAAAAPAA6Yl2yAAAEB0lEQVR42u2cT28TRxjGn3fWDapE4qAiGpeYWGoFEn+EGwWp4YJPVW/1oX+O5RuQCs4o4hyk7RVLxkcCbeWeilAPQYIElcjafgOXuHUSUSWLc6kT78vBO87G2RDHcTQzZn8Xr2dX62d/Gs++9u4O4YCUEokxsJXyIFIEpNrXMziFkPYwmGmYCOmDZnj3PuEQ8Xqn2wqiXdsyUBbwyqBGebxa/fugGWg/gQ0vNrV94JTppQBz4LnmCxVRrxUm1tbcd20dKrWUSIx5XswGUVb14WgHY53BBapvTO8ld5fU0sjoNY9FEYRh1fl1hhmOJTazYcPDDqmlU6cve2TNRUI7gxkO1WuZ9h4rgm8aZBUioZ1DhDQGjhfa21tSF0fO/NDrM/F7AVH25cfJr4NNwZ46rTqfqRCRHXwvgOZYig5ry4hQUr5DAL5UFlZWdSrTCTpsSmVOqw5lOgzOyGV/TKVMNzuKCEIZuSRKp05fjsqo3lAaGb0GAAKChlWH6TcEQ2RUh+g3xOF3ESGRHVQEz1oRvSHqqUeAFlI/zd9D4uYUrKEh1VEOhaz3tZBqxYfwya0fcfHPZ0jcnFIdp3v80lToVPjH4vGW3I+++0Z1nK7Roqe2cyyZROqnu8bJZW7W/FpKlZgmV/4frbVUSVBu/KsvVcfZFyOkSo4lk/jsfg5nf3mA45NfqI6zJ0ZJlQxencS5X2e1lWukVImuco2WKgnK/fDCedVx+kOqZPDqJM7/8TvG7BkMjI4qy9FXUiUnv/8Wl14+V/brLKZawFGw5bqo3L6D/x7+rOTz+0rqlutiNZfHai6Pxps3ynL0hVRdZEqMl/p69hEqt+9oIVNirNTXs49QnbFRr1RUR9mFcVJr8wso37ilpUyJMVJr8wv4d8bGxsIL1VH2JQagDI1vTjNJJjMcAIgBXAYopTpQO/8vLaE6YyurNbtBPhWj3dffRJntaCN1y3VRvWtjNZdXHaV7GOuAJlJXcnlszL/QqtbsBiJyAE2kuo+fqI7QU4Q8Y0X0DhH2bGbE4RDcrFMjeoDH7ACAEF7DUR2mX7D8OpUAYHHkDKsO1A9MLL8iYPtySll1IPPxH19HS+p2Q0R3EGhOLgsAEJ5nqw5lOuQ1inJZAMD46j9/IRoCuoYZju8QQPASNcNWHc5UiHa6a02isHjiRBwDg+XoQbWDwQznysqrz4NtrZ46sbbmCvKyqkMaBWPd4sb19uYdd6iML1eeMnNW/oUVsTfMcAQ3MsGxVLLrtp8rK0u/CbGZBrigOriuEDBN9VqoUH/93jSnUvogC3AGRGlofC3r6OE5MBWF2CzuNwEYdbpLIDB7Gos0+Se0sBnTjmJmtJ7KCWsNmU1NzqQ2vlx5qjr1e89bUmtx22uCSEAAAAAASUVORK5CYII='

            # womp womp. you need the video attr
            throw new Error 'The video attribute is required.' if attrs.video is undefined

            # set up the preloader
            elem[0].setAttribute 'style', 'background-image: url(' + playImage + '), url(' + videoImage + ');'
            elem[0].setAttribute 'href', 'https://www.youtube.com/watch?v=' + attrs.video

            scope.videoLoaded = false

            # set autoplay, related, and html5 options
            autoplay = (attrs.autoplay == 'false' or attrs.autoplay == '0') ? '0' : '1'       # defaults to true
            related  = (attrs.recommended == 'false' or attrs.recommended == '0') ? '0' : '1' # defaults to true
            html5vid = (attrs.html5video == 'true' or attrs.html5video == '1') ? '1' : '0'    # defaults to false

            elem.bind 'click', (e) ->
                e.preventDefault()

                scope.videoLoaded = true

                height = elem[0].offsetHeight
                elem[0].innerHTML = '<div class="youtube-wrapper"><iframe src="https://www.youtube.com/embed/' + attrs.video + '?autoplay=' + autoplay + '&rel=' + related + '&html5=' + html5vid + '" frameborder="0" allowfullscreen /></div>'