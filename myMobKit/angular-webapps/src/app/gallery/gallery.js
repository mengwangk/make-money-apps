angular.module( 'mymobkit.gallery', [
  'ui.state',
  'placeholders',
  'ui.bootstrap'
])

.config(function config( $stateProvider ) {
  $stateProvider.state( 'gallery', {
    url: '/gallery',
    views: {
      "main": {
        controller: 'GalleryCtrl',
        templateUrl: 'gallery/gallery.tpl.html'
      }
    },
    data:{ pageTitle: 'Gallery' }
  });
})

.controller( 'GalleryCtrl', function GalleryCtrl( $scope ) {
  $scope.dropdownDemoItems = [
    "The first choice!",
    "And another choice for you.",
    "but wait! A third!"
  ];
})

;
