let markers = [];


angular.module("map", []).controller('AppController', function($scope, $rootScope, $compile, $http) {
    const socket = new WebSocket("ws://localhost:8080/messaging-endpoint");
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {});
    let currentSubscription = null;

    $http.get('/username').then(response => {
        $scope.username = response.data;
        $http.get('/keys/' + $scope.username).then(response => {
            $scope.keys = response.data;
        }, error => console.log(error))
    }, error => console.log(error));

    $scope.map = new google.maps.Map(document.getElementById('map'), {
        zoom: 4,
        center: {lat: 0, lng: 0}
    });

    $scope.getNewMarkers = function(){
        if (currentSubscription === null) {
            currentSubscription = stompClient.subscribe('/location-updates/' + $scope.userdata.key,
                    location => $scope.addMarker(JSON.parse(location.body)));
        } else {
            stompClient.unsubscribe(currentSubscription);
            currentSubscription = stompClient.subscribe('/location-updates/' + $scope.userdata.key,
                    location => $scope.addMarker(JSON.parse(location.body)));
        }


        for(let marker of markers) {
            marker.setMap(null);
        }

        markers = [];
        $http.get('/location/' + $scope.userdata.key).then(response => {
            for (let location of response.data) {
                $scope.addMarker(location);
            }
        }, error => console.log(error));
    };

    $scope.addMarker = function(location){
        console.log(location);
        markers.push(new google.maps.Marker({
            position: new google.maps.LatLng(location.latitude,location.longitude),
            map: $scope.map,
        }));
    };
});