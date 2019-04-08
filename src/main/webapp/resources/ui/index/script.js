angular.module("map", []).controller('AppController', function($scope, $rootScope, $compile, $http) {
    $scope.map = new google.maps.Map(document.getElementById('map'), {
        zoom: 4,
        center: {lat: 0, lng: 0}
    });
    let currentSubscription = null;
    let markers = []; //0 is always newest.
    let currMarkerAmount = 5;
    let markerPath = new google.maps.Polyline({
        geodesic: true,
        strokeColor: '#FF0000',
        strokeOpacity: 1.0,
        strokeWeight: 2,
        map: $scope.map
    });



    const socket = new WebSocket("ws://localhost:8080/messaging-endpoint");
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {});

    $http.get('/userdata').then(response => {
        $scope.username = response.data.username;
        //get keys
        $http.get('/keys/' + $scope.username).then(response => {
            $scope.keys = response.data;
        }, error => console.error(error));

        $scope.max_marker_amount = response.data.markerAmount;
        const ticks = [
            5,
            (5 + $scope.max_marker_amount) / 4,
            (5 + $scope.max_marker_amount) / 2,
            3 * (5 + $scope.max_marker_amount) / 4,
            $scope.max_marker_amount
        ];
        $scope.marker_amount_ticks = ticks.map(tick => Math.ceil(tick / 5) * 5)
    }, error => console.error(error));



    $scope.getNewMarkers = function(){
        if (currentSubscription !== null) {
            stompClient.unsubscribe(currentSubscription);
        }
        currentSubscription = stompClient.subscribe('/location-updates/' + $scope.userdata.key,
            location => {
                $scope.addMarkerUnshift(JSON.parse(location.body));
                while(markers.length > $scope.max_marker_amount) {
                    markers[markers.length - 1].setMap(null);
                    markers.pop();
                }
                $scope.onMarkerAmountChange(currMarkerAmount)
            });

        $scope.forgetAllMarkers();

        $http.get('/location/' + $scope.userdata.key + '/' + $scope.max_marker_amount).then(response => {
            for (let location of response.data) {
                $scope.addMarkerPush(location);
            }
            $scope.onMarkerAmountChange(currMarkerAmount)
        }, error => console.error(error));
    };

    $scope.onMarkerAmountChange = function(markeramount) {
        currMarkerAmount = markeramount;
        const path = [];
        for(let i = 0; i < markers.length; i++) {
            if (i < markeramount) {
                markers[i].setMap($scope.map);
                path.push(markers[i].position)
            } else {
                markers[i].setMap(null)
            }
        }
        markerPath.setPath(path)
    };

    $scope.addMarkerUnshift = function(location){
        markers.unshift(new google.maps.Marker({
            position: new google.maps.LatLng(location.latitude, location.longitude),
            map: $scope.map,
        }));
    };

    $scope.addMarkerPush = function(location){
        markers.push(new google.maps.Marker({
            position: new google.maps.LatLng(location.latitude, location.longitude),
            map: $scope.map,
        }));
    };

    $scope.forgetAllMarkers = function () {
        for(let marker of markers) {
            marker.setMap(null);
        }

        markers = [];
    };
});