<link rel="stylesheet" type="text/css" href="../resources/css/register.css">
<section class="page shadow">
    <h2>Registration</h2>
    <form name="form" ng-submit="register()">
        <div class="form-group">
            <label for="login" class="form-label">Login</label>
            <input id="login" ng-model="userData.username" name="username" class="input-field"/>
        </div>
        <div class="form-group">
            <label for="password" class="form-label">Password</label>
            <input id="password" ng-model="userData.password" name="password" type="password" class="input-field"/>
        </div>
        <div class="form-footer">
            <div>
                <button type="submit" class="btn-submit shadow">Submit</button>
            </div>
            <div class="btn-cancel">
                <a href="#/login">Cancel</a>
            </div>
            <div class="form-messages">
                <div class="message shadow" ng-if="userExists">
                    <span class="error e-user-exist">User already exists!</span>
                </div>
                <div class="message shadow" ng-if="formNotValid">
                    <span class="error e-non-valid">Form is not valid!</span>
                </div>
                <div class="message shadow" ng-if="userSuccessfullyCreated">
                    <span class="success">User successfully created!</span>
                </div>
            </div>
        </div>
    </form>
</section>
