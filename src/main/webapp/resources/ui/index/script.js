angular.module("map", []).controller('AppController', function($scope, $rootScope, $compile, $http) {
    $scope.map = new google.maps.Map(document.getElementById('map'), {
        zoom: 4,
        center: {lat: 0, lng: 0}
    });
    let currentKeySubscription = null;
    let markers = []; //0 is always newest.
    let markerPath = new google.maps.Polyline({
        geodesic: true,
        strokeColor: '#FF0000',
        strokeOpacity: 1.0,
        strokeWeight: 2,
        map: $scope.map
    });

    $scope.options = {
        key: "",
        displayamount: 5,
        maxMarkerAmount: 50,
        followNewMarkers: false,
    };

    $scope.activeKeys = new Set();

    const socket = new WebSocket("ws://localhost:8080/messaging-endpoint");
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        //general device update subscription
        stompClient.subscribe('/location-updates-any/',
            payload => $scope.activeKeys.add(payload.body));
    });

    $scope.updateActiveDevices = function () {
        $scope.keys.map(key => {
            let label = document.getElementById(key.key+'-span');
            if ($scope.activeKeys.has(key.key)) {
                label.classList.add('activeKey')
            } else {
                label.classList.remove('activeKey')
            }
        });
        $scope.activeKeys = new Set();
    };
    setInterval(function() {$scope.updateActiveDevices()}, 10000);

    $http.get('/userdata').then(response => {
        $scope.username = response.data.username;
        //get keys
        $http.get('/keys/' + $scope.username).then(response => {
            $scope.keys = response.data;
        }, error => console.error(error));

        $scope.options.maxMarkerAmount = response.data.markerAmount;
        const ticks = [
            5,
            (5 + $scope.options.maxMarkerAmount) / 4,
            (5 + $scope.options.maxMarkerAmount) / 2,
            3 * (5 + $scope.options.maxMarkerAmount) / 4,
            $scope.options.maxMarkerAmount
        ];
        $scope.marker_amount_ticks = ticks.map(tick => Math.ceil(tick / 5) * 5)
    }, error => console.error(error));

    $scope.getNewMarkers = function(){
        if (currentKeySubscription !== null) {
            stompClient.unsubscribe(currentKeySubscription.id);
        }
        currentKeySubscription = stompClient.subscribe('/location-updates/' + $scope.options.key + '/',
            location => {
                $scope.addMarkerUnshift(JSON.parse(location.body));
                while(markers.length > $scope.options.maxMarkerAmount) {
                    markers[markers.length - 1].setMap(null);
                    markers.pop();
                }
                $scope.onMarkerAmountChange()
            });

        $scope.forgetAllMarkers();

        $http.get('/location/' + $scope.options.key + '/' + $scope.options.maxMarkerAmount).then(response => {
            for (let location of response.data) {
                $scope.addMarkerPush(location);
            }
            $scope.onMarkerAmountChange()
        }, error => console.error(error));
    };

    $scope.onMarkerAmountChange = function() {
        if (markers.length > 0) {
            const path = [];
            for(let i = 0; i < markers.length; i++) {
                if (i < $scope.options.displayamount) {
                    path.push(markers[i].position)
                }
                markers[i].setMap(null)
            }
            markerPath.setPath(path);
            markers[0].setMap($scope.map);

            if ($scope.options.followNewMarkers === 'true') {
                $scope.centerMap();
            }
        }
    };

    $scope.centerMap = function() {
        $scope.map.setCenter(markers[0].position);
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
        markers.map(marker => marker.setMap(null));
        markers = [];
    };
});

/*TODO: add register page where new users would be added, and go there via button in header. all is controlled by
    ngroute.
    So, total list:
    - Using ngRoute
    - New registration page
    - New controller method to register (which may include checking existence).
    - (maybe) also add key adding interface because we actually have controller method for this?
*/