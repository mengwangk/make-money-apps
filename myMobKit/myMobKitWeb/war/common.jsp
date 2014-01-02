<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.google.appengine.api.users.User" %>
<%@ page import="com.google.appengine.api.users.UserService" %>
<%@ page import="com.google.appengine.api.users.UserServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreServiceFactory" %>
<%@ page import="com.google.appengine.api.datastore.DatastoreService" %>
<%@ page import="com.google.appengine.api.datastore.Query" %>
<%@ page import="com.google.appengine.api.datastore.Entity" %>
<%@ page import="com.google.appengine.api.datastore.FetchOptions" %>
<%@ page import="com.google.appengine.api.datastore.Key" %>
<%@ page import="com.google.appengine.api.datastore.KeyFactory" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<div class="navbar navbar-fixed-top">
	<div class="navbar-inner">
		<div class="container">
	
			<a class="brand pull-left" href="#"><img src="img/mymobkit.png" alt="" border='0'></a>
	
			<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
			</a>
			
			<div class="nav-collapse collapse">
				<ul id="nav-list" class="nav pull-right">
			<%
			    UserService userService = UserServiceFactory.getUserService();
			    User user = userService.getCurrentUser();
			    if (user != null) {
			      pageContext.setAttribute("user", user);
			%>
			<li style="margin-top:25px;margin-right:30px;color:white">Hello, ${fn:escapeXml(user.nickname)}!</li>
			<li style="margin-top:10px">
			<div class="btn-group open">
				  <button class="btn btn-primary">My Workspace</button>
				  <button data-toggle="dropdown" class="btn btn-primary dropdown-toggle"><span class="caret"></span></button>
				  <ul class="dropdown-menu">
				   	  <li><a a href="#" onclick="document.location='/myworkspace?mode=0'">Connect Webcam</a></li>
					  <li class="divider"></li>
					  <li><a a href="#" onclick="document.location='/myworkspace?mode=1'">Access Phone Camera</a></li>
					 <!--  <li class="divider"></li>
					  <li><a href="/myworkspace?mode=2">Video Call</a></li> -->
				  </ul>
			</div>
			</li>
			<li><a href="#" onclick="document.location='<%=userService.createLogoutURL(request.getRequestURI()) %>'" style="padding-right:0"><span class="btn btn-large btn-success">Logout</span></a></li>
			<%
			    } else {
			%>
			<li><a href="#" onclick="document.location='<%= userService.createLoginURL(request.getRequestURI()) %>'" style="padding-right:0"><span class="btn btn-large btn-success">Login</span></a></li>
			<%
			    }
			%>
			<li><a href="#" onclick="document.location='index.jsp#home';" style="margin-top:15px">Home </a></li>
			<li><a href="#" onclick="document.location='index.jsp#screenshots';" style="margin-top:15px">Screenshots </a></li>
			<li><a href="#" onclick="document.location='index.jsp#contact';" style="margin-top:15px">Contact </a></li>
					
			</ul>
			</div>
		
		</div>
	</div>
</div>
