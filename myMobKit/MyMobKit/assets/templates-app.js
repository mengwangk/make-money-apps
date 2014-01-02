angular.module('templates-app', ['about/about.tpl.html', 'contact/contact.tpl.html', 'gallery/gallery.tpl.html', 'gateway/gateway.tpl.html', 'home/home.tpl.html', 'messaging/messaging.tpl.html', 'surveillance/surveillance.tpl.html']);

angular.module("about/about.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("about/about.tpl.html",
    "<div class=\"row-fluid\">\n" +
    "  <h1 class=\"page-header\">\n" +
    "    About\n" +
    "  </h1>\n" +
    "  <p>\n" +
    "   <a href=\"http://www.makemoneywithmyapps.com/\">MyMobKit</a> is an Android application that can be used to provide extended functions to your smart phone.\n" +
    "  </p>\n" +
    "\n" +
    " </div>\n" +
    "\n" +
    "");
}]);

angular.module("contact/contact.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("contact/contact.tpl.html",
    "<div class=\"row-fluid\">\n" +
    "  <h1 class=\"page-header\">\n" +
    "    Contact\n" +
    "  </h1>\n" +
    "  \n" +
    "  <p>\n" +
    "   \n" +
    "  </p>\n" +
    "\n" +
    "</div>\n" +
    "\n" +
    "");
}]);

angular.module("gallery/gallery.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("gallery/gallery.tpl.html",
    "<div class=\"row-fluid\">\n" +
    "  <h1 class=\"page-header\">\n" +
    "    Gallery\n" +
    "  </h1>\n" +
    "  \n" +
    "  <p>\n" +
    "   \n" +
    "  </p>\n" +
    "\n" +
    "</div>\n" +
    "\n" +
    "");
}]);

angular.module("gateway/gateway.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("gateway/gateway.tpl.html",
    "<div class=\"row-fluid\">\n" +
    "  <h1 class=\"page-header\">\n" +
    "    Messaging\n" +
    "  </h1>\n" +
    "  <p>\n" +
    "   \n" +
    "  </p>\n" +
    "\n" +
    "</div>\n" +
    "\n" +
    "");
}]);

angular.module("home/home.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("home/home.tpl.html",
    "<div class=\"marketing\">\n" +
    "	<div class=\"row\">\n" +
    "		<div class=\"span4\">\n" +
    "			<h4>\n" +
    "				<i class=\"icon-eye-open\"></i><a href=\"#/surveillance\"> Surveillance</a>\n" +
    "			</h4>\n" +
    "			<p>Use your phone camera as a remote video surveillance tool.</p>\n" +
    "		</div>\n" +
    "		<div class=\"span4\">\n" +
    "			<h4>\n" +
    "				<i class=\"icon-globe\"></i><a href=\"#/gateway\"> Messaging</a>\n" +
    "			</h4>\n" +
    "			<p>Use your phone as a SMS messaging gateway, exposing HTTP\n" +
    "				services to allow sending and receiving SMS.</p>\n" +
    "		</div>\n" +
    "		<div class=\"span4\">\n" +
    "			<h4>\n" +
    "				<i class=\"icon-info-sign\"></i><a href=\"#/about\"> About</a>\n" +
    "			</h4>\n" +
    "			<p>What is MyMobKit?</p>\n" +
    "		</div>\n" +
    "		<!--   <div class=\"span4\">\n" +
    "      <h4><i class=\"icon-file-text-alt\"></i><a href=\"#/messaging\"> Messaging</a></h4>\n" +
    "      <p>\n" +
    "        Send, receive and backup SMS from your phone.\n" +
    "      </p>\n" +
    "    </div>\n" +
    "    <div class=\"span4\">\n" +
    "      <h4><i class=\"icon-male\"></i><a href=\"#/contact\"> Contact Management</a></h4>\n" +
    "      <p>\n" +
    "       Create, edit and delete contacts in your phone.\n" +
    "      </p>\n" +
    "    </div> -->\n" +
    "	</div>\n" +
    "	<div class=\"row\">\n" +
    "		<!--   <div class=\"span4\">\n" +
    "      <h4><i class=\"icon-picture\"></i><a href=\"#/gallery\"> Gallery</a></h4>\n" +
    "      <p>\n" +
    "        Manage pictures and video in your phone.\n" +
    "      </p>\n" +
    "    </div> -->\n" +
    "\n" +
    "	</div>\n" +
    "</div>\n" +
    "\n" +
    "");
}]);

angular.module("messaging/messaging.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("messaging/messaging.tpl.html",
    "<div class=\"row-fluid\">\n" +
    "  <h1 class=\"page-header\">\n" +
    "    Messaging\n" +
    "  </h1>\n" +
    "  \n" +
    "  <p>\n" +
    "   \n" +
    "  </p>\n" +
    "\n" +
    "</div>\n" +
    "\n" +
    "");
}]);

angular.module("surveillance/surveillance.tpl.html", []).run(["$templateCache", function($templateCache) {
  $templateCache.put("surveillance/surveillance.tpl.html",
    "<form name=\"surveillance_form\">\n" +
    "	<div class=\"row-fluid\">\n" +
    "		<!-- <div class=\"video_view\" id=\"video_plane\" ng-style=\"style()\" resize video-src=\"videoSrc\"></div>\n" +
    " -->\n" +
    "		<div class=\"video_view\" id=\"video_plane\" videoplayer\n" +
    "			video-src=\"videoSrc\"></div>\n" +
    "		<div class=\"audio_view\" ng-show=\"enableAudio\">\n" +
    "			<div id=\"jquery_jplayer_1\" class=\"jp-jplayer\" data-audio=\"audioSrc\"\n" +
    "				data-autoplay=\"autoPlay\" data-pauseothers=\"true\" jplayer></div>\n" +
    "\n" +
    "			<div id=\"jp_container_1\" class=\"jp-audio\">\n" +
    "				<div class=\"jp-type-single\">\n" +
    "					<div class=\"jp-gui jp-interface\">\n" +
    "						<ul class=\"jp-controls\">\n" +
    "							<li><a href=\"javascript:;\" class=\"jp-play\" tabindex=\"1\">play</a></li>\n" +
    "							<li><a href=\"javascript:;\" class=\"jp-pause\" tabindex=\"1\"\n" +
    "								style=\"display: block\">pause</a></li>\n" +
    "							<li><a href=\"javascript:;\" class=\"jp-stop\" tabindex=\"1\">stop</a></li>\n" +
    "							<li><a href=\"javascript:;\" class=\"jp-mute\" tabindex=\"1\"\n" +
    "								title=\"mute\" style=\"display: block\">mute</a></li>\n" +
    "							<li><a href=\"javascript:;\" class=\"jp-unmute\" tabindex=\"1\"\n" +
    "								title=\"unmute\" style=\"display: block\">unmute</a></li>\n" +
    "							<li><a href=\"javascript:;\" class=\"jp-volume-max\"\n" +
    "								tabindex=\"1\" title=\"max volume\">max volume</a></li>\n" +
    "						</ul>\n" +
    "						<div class=\"jp-progress\">\n" +
    "							<div class=\"jp-seek-bar\">\n" +
    "								<div class=\"jp-play-bar\"></div>\n" +
    "							</div>\n" +
    "						</div>\n" +
    "						<div class=\"jp-volume-bar\">\n" +
    "							<div class=\"jp-volume-bar-value\"></div>\n" +
    "						</div>\n" +
    "						<div class=\"jp-time-holder\">\n" +
    "							<div class=\"jp-current-time\"></div>\n" +
    "							<div class=\"jp-duration\"></div>\n" +
    "\n" +
    "							<ul class=\"jp-toggles\">\n" +
    "								<li><a href=\"javascript:;\" class=\"jp-repeat\" tabindex=\"1\"\n" +
    "									title=\"repeat\">repeat</a></li>\n" +
    "								<li><a href=\"javascript:;\" class=\"jp-repeat-off\"\n" +
    "									tabindex=\"1\" title=\"repeat off\" style=\"display: block\">repeat\n" +
    "										off</a></li>\n" +
    "							</ul>\n" +
    "						</div>\n" +
    "					</div>\n" +
    "					<div class=\"jp-no-solution\">\n" +
    "						<span>Update Required</span> To play the media you will need to\n" +
    "						either update your browser to a recent version or update your <a\n" +
    "							href=\"http://get.adobe.com/flashplayer/\" target=\"_blank\">Flash\n" +
    "							plugin</a>.\n" +
    "					</div>\n" +
    "				</div>\n" +
    "			</div>\n" +
    "		</div>\n" +
    "\n" +
    "	</div>\n" +
    "\n" +
    "	<div class=\"control_view\">\n" +
    "\n" +
    "		<div class=\"control_view\">\n" +
    "\n" +
    "			<table width='100%'>\n" +
    "				<tr>\n" +
    "					<td align='left'><input type=\"radio\"\n" +
    "						ng-model=\"streamingMethod\" value=\"js\" ng-disabled=\"inStreaming\">\n" +
    "						Frame update (supported on all platforms)</td>\n" +
    "					<td align='right'><label class=\"control-label\"\n" +
    "						for=\"resolutionValue\">Video size:</label> <select\n" +
    "						class=\"chzn-select\" ng-model=\"resolutionValue\" ng-options=\"res | resFilter:this for res in resolutions\"\n" +
    "						ng-change=\"selectRes()\">\n" +
    "					</select></td>\n" +
    "					<td align='right'>&nbsp;&nbsp;&nbsp; <label\n" +
    "						class=\"control-label\" for=\"canvasSize\">Canvas size:</label> <select\n" +
    "						class=\"chzn-select\" ng-model=\"canvasSize\" required\n" +
    "						ng-options=\"size | sizeFilter:this for size in canvasSizes\"\n" +
    "						ng-change=\"selectCanvasSize()\">\n" +
    "					</select>\n" +
    "					</td>\n" +
    "					<td align='right'><input type=\"checkbox\" id=\"checkbox-audio\"\n" +
    "						ng-model=\"enableAudio\" ng-click=\"selectAudio($event)\" /> <label\n" +
    "						for=\"checkbox-audio\">Enable audio (experimental)</label></td>\n" +
    "				</tr>\n" +
    "				<tr>\n" +
    "					<td align='left'><input type=\"radio\"\n" +
    "						ng-model=\"streamingMethod\" value=\"mjpg\" ng-disabled=\"inStreaming\">\n" +
    "						Built-in browser (supported on Chrome and Firefox)</td>\n" +
    "					<td colspan='3' align='right'>\n" +
    "						<button class=\"btn\" ng-click=\"showMediaInfo()\">\n" +
    "							<i class='icon-eye-open'></i> Media Information\n" +
    "						</button>\n" +
    "					</td>\n" +
    "				</tr>\n" +
    "			</table>\n" +
    "		</div>\n" +
    "\n" +
    "		<div class=\"control_view\">\n" +
    "			<input id=\"btn-camera\" type=\"button\" class=\"btn btn-large btn-block\"\n" +
    "				value=\"{{ !inStreaming ? 'Turn on' : 'Turn off'}}\" ng-click=\"playClick()\"\n" +
    "				ng-disabled=\"surveillance_form.$invalid\" />\n" +
    "		</div>\n" +
    "		\n" +
    "			<div class=\"control_view\">\n" +
    "			{{debugMsg}}\n" +
    "		</div>\n" +
    "		\n" +
    "	</div>\n" +
    "\n" +
    "\n" +
    "<p><br/>\n" +
    "	<div ng-show=\"mediaInfo\">\n" +
    "	\n" +
    "	<h4>Media Information</h4>\n" +
    "	Video: {{videoSrc.path}}<br/>\n" +
    "	Audio: {{audioSrc}}\n" +
    "	\n" +
    "	<br/><br/>\n" +
    "	Motion JPEG: {{ surveillanceUrl  + 'video/live.mjpg'}}\n" +
    "	</div>\n" +
    "</p>\n" +
    "</form>");
}]);
