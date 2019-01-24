<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
        <style>
            .navbar {
            margin-bottom: 0;
            border-radius: 0;
            }
            .row.content {
            height: 450px
            }
            footer {
            background-color: #555;
            color: white;
            padding: 15px;
            }
            @media screen and (max-width: 767px) {
            .row.content {
            height: auto;
            }
            }
        </style>
        <title>Страница авторизации</title>
    </head>

    <body>
        <nav class="navbar navbar-inverse">
            <div class="container-fluid">
                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    </button>
                </div>
                <div class="collapse navbar-collapse" id="myNavbar">
                    <ul class="nav navbar-nav">
                        <li><a href="/articles">Заметки</a></li>
                        <li><a href="/articles/editor">Добавить свою</a></li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <sec:authorize access="isAuthenticated()">
                            <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span> Выйти</a></li>
                        </sec:authorize>
                        <sec:authorize access="isAnonymous()">
                            <li class="active"><a href="/login"><span class="glyphicon glyphicon-log-in"></span> Залогиниться</a></li>
                        </sec:authorize>
                    </ul>
                </div>
            </div>
        </nav>
        <br/>
        <div class="container-fluid text-center">
            <div class="row content">
                <div class="col-sm-4 text-left">
                    <div class="well">
                        <h4>Залогиньтесь, используя учетные данные, или создайте новую учетную запись</h4>
                    </div>

                    <form action="/login" method="post">
                        <c:if test="${param.error != null}">
                            <div class="alert alert-danger">
                                <spring:message code="login.fail" text="default"/><br/>
                                <c:out value="${SPRING_SECURITY_LAST_EXCEPTION}"/>
                            </div>
                        </c:if>
                        <c:if test="${param.logout != null}">
                            <div class="alert alert-danger">
                                You have been logged out.
                            </div>
                        </c:if>
                        <div class="input-group">
                            <span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
                            <input id="username" type="text" class="form-control" name="username"
                                placeholder="логин" required="required"/>
                        </div>
                        <br/>
                        <div class="input-group">
                            <span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
                            <input id="password" type="password" class="form-control" name="password"
                                placeholder="пароль" required="required"/>
                        </div>
                        <input type="hidden"
                            name="${_csrf.parameterName}"
                            value="${_csrf.token}"/>
                        </br>
                        <button type="submit" class="btn btn-success">Войти</button>
                    </form>
                    <hr/>
                    <form action="/login/registration">
                        <button type="submit" class="btn btn-primary">Создать учетную запись</button>
                    </form>
                    <hr/>
                </div>
            </div>
        </div>
    </body>
</html>