<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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
        <title>Редактор заметок</title>
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
                        <li class="active"><a href="/articles/editor">Добавить свою</a></li>
                    </ul>
                    <ul class="nav navbar-nav navbar-right">
                        <sec:authorize access="isAuthenticated()">
                            <li><a href="/logout"><span class="glyphicon glyphicon-log-out"></span> Выйти</a></li>
                        </sec:authorize>
                        <sec:authorize access="isAnonymous()">
                            <li><a href="/login"><span class="glyphicon glyphicon-log-in"></span> Залогиниться</a></li>
                        </sec:authorize>
                    </ul>
                </div>
            </div>
        </nav>
        <br/>
        <div class="container-fluid text-center">
            <div class="row content">
                <div class="col-sm-8 text-left">
                    <form:form method="POST" action="/articles/editor" modelAttribute="articleDto">
                        <h1>Создание заметки</h1>
                        <div class="form-group">
                            <form:label path="title">Название</form:label>
                            <form:input type="text" class="form-control" path="title" required = "required"></form:input>
                            <form:errors path="title" cssClass="text-danger"/>
                        </div>
                        <div class="form-group">
                            <form:label path="content">Текст</form:label>
                            <form:textarea class="form-control" rows="20" id="content" path="content" required="required"></form:textarea>
                            <form:errors path="content" cssClass="text-danger"/>
                        </div>
                        <button type="submit" class="btn btn-success">Сохранить</button>
                    </form:form>
                    <hr/>
                </div>
            </div>
        </div>
    </body>
</html>