angular.module( 'mymobkit.about', [
  'ui.state',
  'placeholders',
  'ui.bootstrap'
])

.config(function config( $stateProvider ) {
  $stateProvider.state( 'about', {
    url: '/about',
    views: {
      "main": {
        controller: 'AboutCtrl',
        templateUrl: 'about/about.tpl.html'
      }
    },
    data:{ pageTitle: 'About myMobKit' }
  });
})

.controller( 'AboutCtrl', function AboutCtrl( $scope ) {
  $scope.dropdownDemoItems = [
    "About myMobKit"
  ];
})

;
