angular.module(
		'mymobkit.surveillance',
		[ 'ui.state', 'placeholders', 'ui.bootstrap', 'resources.streaming',
				'directives.video', 'directives.audio', 'ui.bootstrap' ])

.config(function config($stateProvider, $sceDelegateProvider) {
	$stateProvider.state('surveillance', {
		url : '/surveillance',
		views : {
			"main" : {
				controller : 'SurveillanceCtrl',
				templateUrl : 'surveillance/surveillance.tpl.html'
			}
		},
		data : {
			pageTitle : 'Surveillance'
		}
	});
	
	$sceDelegateProvider.resourceUrlWhitelist(['.*']);
})
.filter('resFilter', function() {
	return function(input, scope) {	// We passed in scope here, you can also pass in a scope variable
		if (input.width == scope.defaultRes.width && input.height == scope.defaultRes.height) {
			scope.resolutionValue = input;
		}
		return (input.width + ' x ' + input.height);
    };
})
.filter('sizeFilter', function() {
	return function(size, scope) {	// We passed in scope here, you can also pass in a scope variable
		if (size.ratio == scope.defaultCanvasSize.ratio) {
			scope.canvasSize = size;
		}
		return (size.ratio + '%');
    };
})
/*.factory("streamingServices", function ($rootScope) {
    var link = '';
    return {
        getLink: function () {
            return link;
        },         
        setLink: function (newLink) {
            link = newLink;
            console.log('set');
            $rootScope.$broadcast('updateVideoImage');
        }
    };
})*/
.controller(
	'SurveillanceCtrl',
	function SurveillanceCtrl($rootScope, $scope, $http, $location, Streaming,	$window, $timeout, $modal, $resource) {
		//var url = $location.absUrl();
		//console.log(url);
		
		$scope.imageWidth = 0;
		var isNotDefined = function(val) { 
			return (angular.isUndefined(val) || val === null);
		};	
		
		var s4 = function(){
			return Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
		};
		
		var guid = function() {
			return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
			s4() + '-' + s4() + s4() + s4();
		};
		
		var getResolutions = function(){
			// Get list of resolutions
			$scope.debugMsg = 'Connecting...';
			Streaming.getResolutions().query().$promise.then(
					function success(data, headers) {
						if (angular.isArray(data)) {
							$scope.defaultRes = data[0];
							$scope.resolutions = data;
							$scope.resolutions.splice(0,1);
						}
						$scope.debugMsg = '';
						$scope.audioSrc = '';
						$scope.autoPlay = false;
					}, function err(httpResponse) {
						$scope.debugMsg =  "Failed to connect to phone. Try refreshing the page.";
					});
		};
		
		var getCanvasSizes = function(){
			$scope.debugMsg = 'Retrieving canvas sizes...';
			Streaming.getCanvasSizes().query().$promise.then(
					function success(data, headers) {
						if (angular.isArray(data)) {
							$scope.defaultCanvasSize = data[0];
							$scope.canvasSizes = data;
							$scope.canvasSizes.splice(0,1);
							$scope.canvasSize = data[0];
						}
						$scope.debugMsg = '';
					}, function err(httpResponse) {
						$scope.debugMsg =  "Failed to connect to phone. Try refreshing the page.";
					});
		};
		
		if (!$rootScope.debug_mode) {
			var surveillance = $resource($rootScope.host + "/services/surveillance/url?id=" + guid());
			var obj  = surveillance.get(function() {
				$scope.surveillanceUrl = obj.url;
				$rootScope.surveillanceUrl = obj.url;
				
				getResolutions();
				getCanvasSizes();				
			
			});
		} else {
			
			$scope.surveillanceUrl = $rootScope.surveillance_mock_url;
			$rootScope.surveillanceUrl = $rootScope.surveillance_mock_url;
			
			getResolutions();
			getCanvasSizes();				
		}
		
		$scope.selectRes = function() {
			$scope.defaultRes = $scope.resolutionValue;
			Streaming.setResolution($scope.resolutionValue, $scope);			
		};
		
		var videoTimer = null;
		$scope.inStreaming = false;
		$scope.videoFrameCount = 0;
		$scope.mediaInfo = false;
		$scope.videoSrc = {path: 'assets/images/black.png', width: 0, height: 0, displayStyle:'' };
		
		// $scope.$on('updateVideoImage',function(){ $scope.video_src = streamingServices.getLink();});
		
		var setStreamingMethod = function() {
			var agent = $window.navigator.userAgent;
			if (agent.toLowerCase().indexOf('chrome') > -1 || agent.toLowerCase().indexOf('firefox') > -1){
				$scope.streamingMethod = 'mjpg';
			} else {
				$scope.streamingMethod = 'js';
			}
		};

		setStreamingMethod();
		
		
		
		$scope.frameStreamingVideo = function(){
			if (!$scope.inStreaming) {
				return;
			}
			videoTimer = $timeout(
					function() {
						var imgPath = $scope.surveillanceUrl  + '/video_stream/live.jpg?id=' + guid();
						var img = new Image();
						img.src = imgPath;
						img.onload = function () {
							$scope.$apply(function() {
								$scope.videoSrc.path = imgPath;
								$scope.videoSrc.width = img.width * ($scope.canvasSize.ratio / 100);
								$scope.videoSrc.height = img.height * ($scope.canvasSize.ratio / 100);
								});
							$scope.frameStreamingVideo();
							};						
					}, 
			300);
		};
		
		$scope.mjpgStreamingVideo = function(){
			$scope.videoSrc.path = $scope.surveillanceUrl  + '/video/live.mjpg';
			var img = new Image();
			img.src = $scope.videoSrc.path;
			img.onload = function () {
				$scope.imageWidth = img.width;
				$scope.$apply(function() {
					$scope.videoSrc.width = img.width * ($scope.canvasSize.ratio / 100);
					$scope.videoSrc.height = img.height * ($scope.canvasSize.ratio / 100);
					});
				};
		};
		
		$scope.playClick = function() {
			if (!$scope.inStreaming) {
				$scope.startStreaming();
			} else {
				$scope.stopStreaming();
			}
			// streamingServices.setLink($scope.video_src);
		};
		
		$scope.startStreaming = function(){
			$scope.inStreaming = true;
			
			$scope.debugMsg = 'Status: Connecting to phone';
			$http({method: 'GET', url: $rootScope.host  + '/services/surveillance/start?id=' + guid()}).
				success(function(data, status, headers, config) {
					$scope.debugMsg = 'Status: Connected to phone';
					if (isNotDefined($scope.resolutionValue)) {
						getResolutions();
					}
					if ($scope.streamingMethod == 'js') {
						$scope.frameStreamingVideo();	
					} else {
						$scope.mjpgStreamingVideo();
					}
				}).
				error(function(data, status, headers, config) {
					$scope.debugMsg =  "Status: Failed to connect to phone. Try refreshing the page.";
			});
			
			
		};
		
		$scope.stopStreaming = function(){
			$scope.inStreaming = false;
			if ($scope.streamingMethod == 'js') {
				if (!$timeout.cancel(videoTimer)){
				}
			} else {
				$scope.videoSrc.path = ' ';
				var img = new Image();
				img.src = $scope.videoSrc.path;
			}
			$scope.debugMsg = ' ';
		};
		
		$scope.showMediaInfo = function(){
			$scope.mediaInfo = !$scope.mediaInfo;
		};
		
		$scope.selectCanvasSize = function() {
			$scope.defaultCanvasSize = $scope.canvasSize;
			if ($scope.streamingMethod != 'js') {
				$scope.videoSrc.width = $scope.imageWidth * ($scope.canvasSize.ratio / 100);
			}
			// $scope.changeVideoPaneSize($scope.getWindowDimensions(), null);
		};
		
		
		$scope.selectAudio = function($event) {
			var checkbox = $event.target;
			var isChecked = checkbox.checked;
			if (isChecked){
				$scope.audioSrc = $scope.surveillanceUrl  + '/audio_stream/live.mp3?id=' + guid();
				$scope.autoPlay = true;
			} else {
				$scope.audioSrc = '';
				$scope.autoPlay = false;
			}
		};
	}
);
