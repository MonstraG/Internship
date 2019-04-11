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
            <span class="loggedInUsername not-selectable" ng-click="switchLogoutWindow();">{{username}}</span>
            <div class="logout-window shadow hidden" id="logoutWindow" hidden>
                <a class="logout-link" href="/login?logout">Log out?</a>
            </div>
            <a href="#!register"><button class="login-btn button shadow">Register</button></a>
        </section>
    </header>
    <section class="main">
        <aside class="sidebar shadow">
            <form id="pickKey">
                <button class="button install-key-btn shadow" ng-click="onInstallNewKeyBtn();">Install new key</button>
                <div class="scroll-list">
                    <input ng-if="keyInstaller.installNewKey" id="keyinstaller" ng-model="keyInstaller.keyToInstall"
                           class="input-field shadow" placeholder="Key" >
                    <ul class="keyslist">
                        <li class="keyslist-item" ng-repeat="key in keys">
                            <input id={{key.key}} class="keyradio" type="radio" ng-model="options.key" name="name"
                                   value="{{key.key}}" ng-change="getNewMarkers();"/>
                            <label for="{{key.key}}"id="{{key.key}}-span" class="keyspan not-selectable" ng-dblclick="centerMap();">{{key.key}}</label>
                        </li>
                    </ul>
                </div>
            </form>
        </aside>
        <article class="map" id="map"></article>
    </section>
</div>
