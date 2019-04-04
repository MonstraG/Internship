<!DOCTYPE html>
<html>
<head>
    <title>Map</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="/resources/ui/index/style.css">
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.7.8/angular.js"></script>
    <script src="https://maps.googleapis.com/maps/api/js"></script>
    <script src="/resources/ui/index/script.js"></script>
</head>
<body ng-app="map" ng-controller="AppController">
    <div class="page">
        <div class="header">
            <div class="key-select">
                <label for="pickKey">Pick key:</label>
                <select id="pickKey" ng-model="userdata" ng-options="key.key for key in keys" ng-change="getNewMarkers()"></select>
            </div>
            <div class="login-username">{{username}}</div>
        </div>
        <div class="map" id="map"></div>
    </div>
</body>
</html>