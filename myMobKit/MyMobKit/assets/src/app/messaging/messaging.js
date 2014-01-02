angular.module( 'mymobkit.messaging', [
  'ui.state',
  'placeholders',
  'ui.bootstrap'
])

.config(function config( $stateProvider ) {
  $stateProvider.state( 'messaging', {
    url: '/messaging',
    views: {
      "main": {
        controller: 'MessagingCtrl',
        templateUrl: 'messaging/messaging.tpl.html'
      }
    },
    data:{ pageTitle: 'Messaging' }
  });
})

.controller( 'MessagingCtrl', function MessagingCtrl( $scope ) {
  $scope.dropdownDemoItems = [
    "The first choice!",
    "And another choice for you.",
    "but wait! A third!"
  ];
})

;
