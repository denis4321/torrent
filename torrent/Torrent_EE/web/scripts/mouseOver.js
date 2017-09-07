
(function($) {
$.fn.autoMouseOver = function(settings) {
settings = $.extend({
outStr: "-out.", // default string to replace for the "out" images (eg. home-out.png)
overStr: "-over." // default string to replace for the "over" images (eg. home-over.png)
}, settings || {});
// Preload the images
var preloadImageArray = new Array();
$(this).filter("img").each(function() {
var overImg = $(this).attr("src").replace(settings.outStr, settings.overStr);
var img = new Image();
img.src = overImg;
preloadImageArray.push(img);
});
// Set the hover handler
$(this).filter("img").hover(function() {
$(this).attr("src", $(this).attr("src").replace(settings.outStr, settings.overStr));
}, function() {
$(this).attr("src", $(this).attr("src").replace(settings.overStr, settings.outStr));
});
return $;
};
})(jQuery);
