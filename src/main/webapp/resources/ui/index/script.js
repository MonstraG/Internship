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
        max_marker_amount: 50,
        followNewMarkers: false,
        opacitymode: false
    };

    $scope.activeKeys = new Set();

    $scope.optionsStyle = {'opacity': 0.5};

    const socket = new WebSocket("ws://localhost:8080/messaging-endpoint");
    const stompClient = Stomp.over(socket);
    stompClient.connect({}, function () {
        //general device update subscription
        stompClient.subscribe('/location-updates-any/',
            payload => $scope.activeKeys.add(payload.body));
    });

    $scope.updateActiveDevices = function () {
        for(let key of $scope.keys) {
            let label = document.getElementById(key.key+'-span');
            if (label !== null) {
                if ($scope.activeKeys.has(key.key)) {
                    label.classList.add('activeKey')
                } else {
                    label.classList.remove('activeKey')
                }
            }
        }
        $scope.activeKeys = new Set();
    };

    setInterval(function() {$scope.updateActiveDevices()}, 10000);


    $http.get('/userdata').then(response => {
        $scope.username = response.data.username;
        //get keys
        $http.get('/keys/' + $scope.username).then(response => {
            $scope.keys = response.data;
        }, error => console.error(error));

        $scope.options.max_marker_amount = response.data.markerAmount;
        const ticks = [
            5,
            (5 + $scope.options.max_marker_amount) / 4,
            (5 + $scope.options.max_marker_amount) / 2,
            3 * (5 + $scope.options.max_marker_amount) / 4,
            $scope.options.max_marker_amount
        ];
        $scope.marker_amount_ticks = ticks.map(tick => Math.ceil(tick / 5) * 5)
    }, error => console.error(error));

    $scope.getNewMarkers = function(){
        $scope.optionsStyle = {'opacity': 1.0};
        if (currentKeySubscription !== null) {
            stompClient.unsubscribe(currentKeySubscription.id);
        }
        currentKeySubscription = stompClient.subscribe('/location-updates/' + $scope.options.key + '/',
            location => {
                $scope.addMarkerUnshift(JSON.parse(location.body));
                while(markers.length > $scope.options.max_marker_amount) {
                    markers[markers.length - 1].setMap(null);
                    markers.pop();
                }
                $scope.onMarkerAmountChange()
            });

        $scope.forgetAllMarkers();

        $http.get('/location/' + $scope.options.key + '/' + $scope.options.max_marker_amount).then(response => {
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
        for(let marker of markers) {
            marker.setMap(null);
        }

        markers = [];
    };
});