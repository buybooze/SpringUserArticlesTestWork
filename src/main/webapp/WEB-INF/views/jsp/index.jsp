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
        <title>Пользовательские заметки</title>
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
                        <li class="active"><a href="/articles">Заметки</a></li>
                        <li><a href="/articles/editor">Добавить свою</a></li>
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
        <div class="container-fluid text-left">
            <div>
                <div class="well">
                    <div class="container-fluid text-center">
                        <td>
                            <c:choose>
                                <c:when test="${currentLoggedInUser != ''}">
                                    <h3>Добро пожаловать, ${currentLoggedInUser}!</h3>
                                </c:when>
                                <c:otherwise>
                                    <h3>Добро пожаловать! Залогиньтесь, чтобы добавлять и удалять заметки.</h3>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </div>
                    <h4> Поиск заметок</h4>
                    <br>

                    <form:form method="POST" action="/articles" modelAttribute="searchFormDto">
                        <table>
                            <tr>
                                <td>Автор: </td>
                                <td><form:select path="author">
                                      <form:option value="" label="...." />
                                      <form:options items="${authorList}" />
                                       </form:select>
                                                </td>
                                <td><form:errors path="author" cssClass="text-danger" /></td>
                            </tr>
                            <tr>
                                <td>Название содержит: </td>
                                <td><form:input type="text" class="form-control" path="title" placeholder=""></form:input>
                                                </td>
                                <td><form:errors path="title" cssClass="text-danger" /></td>
                            </tr>
                            <tr>
                                <td><button type="submit" class="btn btn-success">Поиск</button></td>
                            </tr>
                            <tr>
                        </table>
                    </form:form>
                </div>
            </div>
            <table class="table">
                <thead class="thead-light">
                    <tr>
                        <th class="col-md-1">Identicon</th>
                        <th class="col-md-3">Название</th>
                        <th class="col-md-2">Автор</th>
                        <th class="col-md-2">Дата создания</th>
                        <th class="col-md-1">Удалить</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="article" items="${articles}">
                        <tr>
                            <td>
                                <img border="0" id="myImg" name="myImg" src="data:image/svg+xml;base64,<c:out value='${article.getSvgIdenticonBase64Str()}'/>" width="40" height="40" alt="Identicon" >
                            </td>
                            <td>
                                <a href="<c:out value='/articles/show/${article.getId()}'/>">
                                    <h4>${article.getTitle()}</h4>
                                </a>
                            </td>
                            <td>${article.getAuthor()}</td>
                            <td>${article.getCreationTimestampFormatted('yyyy-MM-dd HH:mm:ss z')}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${article.getAuthor() == currentLoggedInUser}">
                                        <a href="<c:out value='/articles/delete/${article.getId()}'/>">
                                        <img border="0" src="/resources/core/images/remove-icon.png" width="30" height="30"/>
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <sec:authorize access="hasRole('ROLE_ADMIN')">
                                            <a href="<c:out value='/articles/delete/${article.getId()}'/>">
                                            <img border="0" src="/resources/core/images/remove-icon.png" width="30" height="30"/>
                                            </a>
                                        </sec:authorize>
                                    </c:otherwise>
                                 </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
        <hr>
    </body>
</html>