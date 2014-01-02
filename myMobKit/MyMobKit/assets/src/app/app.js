angular.module(
		'mymobkit',
		[ 'templates-app', 'templates-common', 'mymobkit.home',
				'mymobkit.about', 'mymobkit.surveillance', 'mymobkit.contact',
				'mymobkit.gallery', 'mymobkit.messaging', 'mymobkit.gateway',
				'ui.state', 'ui.route', 'ngResource', 'ui.bootstrap'])

.config(function myAppConfig($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('/home');
})

.run(function run() {
})

.controller(
		'AppCtrl',
		function AppCtrl($rootScope, $scope, $location, $resource) {
			$rootScope.debug_mode = false;
			if (!$rootScope.debug_mode) {
				$rootScope.host = $location.protocol() + '://' + $location.host() + ':' + $location.port();
			} else {
				$rootScope.host = 'http://192.168.0.102:1688';
				$rootScope.surveillance_mock_url = 'http://192.168.0.102:6888';
			}
			
			$scope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams) {
				if (angular.isDefined(toState.data.pageTitle)) {
					$scope.pageTitle = toState.data.pageTitle + ' | myMobKit';
				}
			});
		});
