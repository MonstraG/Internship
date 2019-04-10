<!DOCTYPE HTML>
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Login</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" type="text/css" href="../resources/css/login.css">
</head>
<body>
<div class="login-page">
    <form name="f" th:action="@{/login}" th:object="${user}" method="post">
        <div th:if="${param.error}" class="login-error">
            Invalid username and password.
        </div>
        <div>
            <input type="text" id="username" name="username" placeholder="Username" class="input-field"/>
        </div>
        <div>
            <input type="password" id="password" name="password" placeholder="Password" class="input-field"/>
        </div>
        <div>
            <button type="submit" class="btn">LOGIN</button>
        </div>
    </form>
</div>
</body>
</html>