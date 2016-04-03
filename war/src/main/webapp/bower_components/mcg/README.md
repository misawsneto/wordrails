# Material Design Color Generator
A tool for generating a color palette for Material Design.
###<a href="http://mcg.mbitson.com/">Click here to view the tool!</a>

# What's New?
* 12/18/15 - GitHub user Simon04 added support for automatic text-color contrast calculations. Thank you!
* 10/16/15 - Updated angular, it's dependencies, and other third party tools. Added SpeedDial from Material Design library.
* 10/9/15 - Import from AngularJS Material Design default palettes or from your own AngularJS Material Design Color Palette definition code!
* 10/9/15 - Added support for tinycolor.js instead of manual color manipulation.
* 7/22/15 - Improved Performance with more than 2 palettes.
* 7/14/15 - Support for <a href="http://www.COLOURlovers.com">COLOURlovers.com</a> palettes. You may now select a top palette and instantly have a material design theme!
* 4/16/15 - Modify any one palette color.
* 4/14/15 - Export your color choices to the AngularJS Material Design theme declaration code.

# Planned Future Enhancements
* (TOP) Determine which palette is primary, secondary, and so on. Drag and drop palettes.
* (TOP) Support export to Materialize (CSS, Universal)
* (TOP) Support export to Polymer Theme
* (TOP) Support export to Material-UI (React)
* (TOP) Support export for mobile app development.
* (TOP) Offer material design previews. AngularJS MD + Mobile App preview.
* (TOP) Add ability to quickly add 'functional' palettes - Success (Green), Warning(Red), Danger(Yellow)
* (MED) Remove individual color's hex details and create tooltip styled with all color details. (Hoverintent, populated only once it's opening, includes hex, rgb, cmyk, saturation, brightness. Possibly obtained using tinycolor)
* (MED) Change pre tag in code views to code editor with syntax highlighting.
* (MED) Add copy-to-clipboard button to code editor.
* (MED) Change color logic to preserve saturation/brightness and only adjust hue. (TinyColor.js)
* (MED) Improve performance by dynamically creating and destroying the color picker button and UI instead of loading a ton of Divs into the DOM for each color on the screen and hiding them.
* (MED) Allow less than one palette by dynamically adjusting the code generated.
* (MED) Allow for nameless (generic named) palettes to set a default MD theme.
* (LOW) Add reset buttons for each individual color in a palette (based on main palette color)
* (LOW) Create UI for more than 5 palettes (and/or alert user about dangers of using this many palettes)

# Contributions
* fireflight1 - Initial concept, starting code base.
* tkh44 - Optimizations/fixes
* simon04 - Contrast detection, bug fixes.