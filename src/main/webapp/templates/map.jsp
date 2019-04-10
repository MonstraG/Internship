<link rel="stylesheet" type="text/css" href="../resources/css/map.css">
<div class="page">
    <header class="shadow">
        <section class="options">
            <div class="option" ng-style="optionsStyle">
                <label for="maxmarkers">Display</label>
                <input type="range" class="markeramount" list="tickmarks" id="maxmarkers" ng-model="options.displayAmount"
                       min="5" max="{{options.maxMarkerAmount}}" step="5"
                       ng-change="onMarkerChange()">
                <datalist id="tickmarks">
                    <option ng-repeat="tick in marker_amount_ticks" value="{{tick}}">
                </datalist>
                <span class="markeramountnumber">{{options.displayAmount}}</span>
            </div>
            <div class="option">
                <label for="follownewmarkers">Follow the damn train, CJ!</label>
                <input id="follownewmarkers" type="checkbox" ng-model="options.followNewMarkers"
                       ng-true-value="'true'" ng-false-value="'false'">
            </div>
        </section>
        <section class="login-register">
            {{username}}
            <a href="#!register"><button class="login-button shadow">Register</button></a>
        </section>
    </header>
    <section class="main">
        <aside class="sidebar shadow">
            <form id="pickKey">
                <ul class="keyslist">
                    <li class="keyslist-item" ng-repeat="key in keys">
                        <input id={{key.key}} class="keyradio" type="radio" ng-model="options.key" name="name"
                               value="{{key.key}}" ng-change="getNewMarkers();"/>
                        <label for="{{key.key}}"id="{{key.key}}-span" class="keyspan" ng-dblclick="centerMap();">{{key.key}}</label>
                    </li>
                </ul>
            </form>
        </aside>
        <article class="map" id="map"></article>
    </section>
</div>
