angular.module('resources.streaming', [ 'ngResource' ]);
angular.module('resources.streaming').factory(
		'Streaming',
		[
				'$rootScope',
				'$resource',
				'$location',
				'$http',
				function($rootScope, $resource, $location, $http) {
					var streamingService = {};
					streamingService.getResolutions = function() {

						if ($rootScope.debug_mode) {
							return $resource('assets/json/resolution.json', {},
									{
										get : {
											method : 'GET',
											isArray : true
										}
									});
						} else {
							var path = $rootScope.surveillanceUrl + "/processor/query";
							return $resource(path, {},
									{
										get : {
											method : 'GET',
											isArray : true
										}
									});
						}

					};
					streamingService.getCanvasSizes = function() {
							return $resource('assets/config/canvas_size.json', {},
									{
										get : {
											method : 'GET',
											isArray : true
										}
									});
					};
					
					streamingService.setResolution = function(resolution, scope){
						//if (!$rootScope.debug_mode) {
							var path = $rootScope.surveillanceUrl + "/processor/setup?width=" + resolution.width + "&height="+ resolution.height;
							$http({method: 'GET', url: path}).
								success(function(data, status, headers, config) {
									scope.imageWidth = scope.resolutionValue.width;
									if (scope.streamingMethod != 'js') {
										scope.videoSrc.width = scope.imageWidth * (scope.canvasSize.ratio / 100);
									}
								}).
								error(function(data, status, headers, config) {
									
								});							
						//}
					};
					return streamingService;
				} ]);