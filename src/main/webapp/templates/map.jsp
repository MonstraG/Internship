<link rel="stylesheet" type="text/css" href="../resources/css/map.css">
<div class="page">
    <div class="header">
        <div class="options">
            <div class="option" ng-style="optionsStyle">
                <label for="maxmarkers">Display</label>
                <input type="range" class="markeramount" list="tickmarks" id="maxmarkers" ng-model="options.displayAmount"
                       min="5" max="{{options.maxMarkerAmount}}" step="5"
                       ng-change="onMarkerChange()">
                <datalist id="tickmarks">
                    <option value="{{marker_amount_ticks[0]}}">
                    <option value="{{marker_amount_ticks[1]}}">
                    <option value="{{marker_amount_ticks[2]}}">
                    <option value="{{marker_amount_ticks[3]}}">
                    <option value="{{marker_amount_ticks[4]}}">
                </datalist>
                <span class="markeramountnumber">{{options.displayAmount}}</span>
            </div>
            <div class="option" ng-style="optionsStyle">
                <label for="follownewmarkers">Follow the damn train, CJ!</label>
                <input id="follownewmarkers" type="checkbox" ng-model="options.followNewMarkers"
                       ng-true-value="'true'" ng-false-value="'false'">
            </div>
        </div>
        <div class="login-username">
            {{username}}
            <a href="#!register" ng-click="clearDeviceUpdateInterval();"><button class="login-button">Register</button></a>
        </div>
    </div>
    <div class="main">
        <div class="sidebar">
            <form id="pickKey">
                <ul class="keyslist">
                    <li class="keyslist-item" ng-repeat="key in keys">
                        <label>
                            <input id={{key.key}} class="keyradio" type="radio" ng-model="options.key" name="name"
                                   value="{{key.key}}" ng-change="getNewMarkers();"/>
                            <span id="{{key.key}}-span" class="keyspan" ng-dblclick="centerMap();">{{key.key}}</span>
                        </label>
                    </li>
                </ul>
            </form>
        </div>
        <div class="map" id="map"></div>
    </div>
</div>
