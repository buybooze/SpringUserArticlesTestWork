<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
        <div class="container-fluid text-center">
            <div class="row content">
                <div class="col-sm-8 text-left">
                    <c:if test="${article != null}">
                        <div class="row">
                            <div class="col-sm-1">
                                <img id="myImg" name="myImg" src="data:image/svg+xml;base64,<c:out value='${article.getSvgIdenticonBase64Str()}'/>" width="50" height="50" alt="Identicon" >
                            </div>
                            <div class="col-sm-5">
                                <h3>${article.getTitle()}</h3>
                                <h5>автор: <b>${article.getAuthor()}</b></h5>
                                <h5>Дата создания: ${article.getCreationTimestampFormatted('yyyy-MM-dd HH:mm:ss z')}</h5>
                            </div>
                        </div>
                        <p>${article.getContent()}</p>
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
                        <hr/>
                    </c:if>
                </div>
            </div>
        </div>
        <hr>
    </body>
</html>