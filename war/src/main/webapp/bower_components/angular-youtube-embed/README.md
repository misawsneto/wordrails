# angular-youtube-embed [![Build Status](https://travis-ci.org/yoshokatana/angular-youtube-embed.svg?branch=master)](https://travis-ci.org/yoshokatana/angular-youtube-embed)

Lazy load youtube video embeds.

# Usage

```
bower install angular-youtube-embed
```

Pretty simple, huh? Then just include the minified js in your app. After that, add it as a dependency in your angular app:

```js
var app = angular.module('app', ['keats.youtube']);
```

You can either add the included css file (`dist/angular-youtube-embed.css`) which has a few neat hacks to get iframes to scale correctly, or you can just grab the styles and include them in your own css/less/sass/scss/stylus files.

## HTML

```html
<youtube video="videoID" class="youtube"></youtube>
```

By default, clicking on the youtube video will autoplay it in the default mode (swf until youtube switches over) and will display recommended videos at the end. You can change these options by adding some attributes, e.g.:

```html
<youtube video="dQw4w9WgXcQ" class="youtube" autoplay="false" html5video="true" recommended="false"></youtube>
```

The above example won't autoplay or show recommended videos at the end, but *will* use html5 video.

# License & Credits

Licensed under MIT

Forked with ♥ from [Jan Antala](http://www.janantala.com)