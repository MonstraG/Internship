let markers = [];
let app = angular.module("map", []);

function init() {
    app.controller('AppController', function($scope, $rootScope, $compile, $http) {
        $http.get('/username').then(response => {
            $scope.username = response.data;
            $http.post('/keys', $scope.username).then(response => {
                $scope.keys = response.data;
            }, error => console.log(error))
        }, error => console.log(error));


        $scope.map = new google.maps.Map(document.getElementById('map'), {
            zoom: 4,
            center: {lat: 0, lng: 0}
        });

        $http.get('/location')
            .then(response => {
                const locations = response.data;
                for (let location of locations) {
                    markers.push(new google.maps.Marker({
                        position: new google.maps.LatLng(location.latitude,location.longitude),
                        map: $scope.map,
                    }));
                }
            }, error => console.log(error));


        $scope.getNewMarkers = function(){
            for(let marker of markers) {
                marker.setMap(null);
            }

            markers = [];
            $http.get('/location/' + $scope.userdata.key)
                .then(response => {
                    const locations = response.data;
                    for (let i in locations) {
                        markers.push(new google.maps.Marker({
                            position: new google.maps.LatLng(locations[i].latitude,locations[i].longitude),
                            map: $scope.map,
                        }));
                    }
                }, error => console.log(error));
        }
    });
}

init();