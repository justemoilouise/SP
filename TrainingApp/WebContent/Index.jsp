<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>RadiSS - Training</title>
	<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="bootstrap/js/jquery-2.1.3.min.js"></script>
	<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="script/navigation.js"></script>
	<script type="text/javascript" src="script/core.js"></script>
	<script type="text/javascript" src="script/training.js"></script>
	<script type="text/javascript" src="script/user.js"></script>
	<script type="text/javascript" src="script/validation.js"></script>
</head>
<body>
	<div id="alert_holder"></div>
	
	<nav class="navbar navbar-inverse">
	<div class="container-fluid">

		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed"
				data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span> <span
					class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#" id="nav_home">RadiSS</a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a href="#" id="nav_download">Downloads</a></li>
				<% //if(session.getAttribute("admin") != null) { %>
				<li><a href="#" id="nav_training">Train</a></li>
				<% //} %>
				<li><a href="#" id="nav_about">About</a></li>
			</ul>
			
			<% if(session.getAttribute("admin") != null) { %>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="#" id="logout_btn">Log out</a></li>
			</ul>
			<% } %>
		</div>
	</div>
	</nav>
	
	<div id="content_holder"></div>
	<br /><br />
</body>
</html>