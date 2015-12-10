<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>RaDSS - Training</title>
	<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
</head>
<body style="background-image:url('img/radiolaria_dark_by_fullerenedream.jpg'); color: #000; background-attachment: fixed; padding-top: 50px;">
	<div id="alert_holder"></div>
	<nav class="navbar navbar-inverse navbar-fixed-top">
	<div class="container-fluid">

		<!-- Brand and toggle get grouped for better mobile display -->
		<div class="navbar-header">
			<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
				<span class="sr-only">Toggle navigation</span> 
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span> 
				<span class="icon-bar"></span>
			</button>
			<a class="navbar-brand" href="#" id="nav_home">RaDSS</a>
		</div>

		<!-- Collect the nav links, forms, and other content for toggling -->
		<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
			<ul class="nav navbar-nav">
				<li><a href="#" id="nav_download">Downloads</a></li>
				<li><a href="#" id="nav_training">Train</a></li>
				<li>
					<a id="nav_about" href="#" class="dropdown-toggle" data-toggle="dropdown">About <span class="caret"></span></a>
					<ul class="dropdown-menu">
						<li><a href="#howItWorks">How it works</a></li>
						<li><a href="#results">What are the results</a></li>
					</ul>
				</li>
			</ul>
			<ul class="nav navbar-nav navbar-right" id="nav_logout">
				<li><a href="#" id="logout_btn">Log out</a></li>
			</ul>
		</div>
	</div>
	</nav>
	<div id="content_holder"></div>
	<br /><br />
	<!-- Progress modal -->	
	<div class="modal fade" id="progressModal" tabindex="-1" role="dialog">
	    <div class="modal-dialog" role="document">
	        <div class="modal-content col-lg-offset-2 col-lg-8 col-md-offset-2 col-md-8">
	        	<br /><br />
	            <h2>&emsp;Loading.. </h2>
	            <br /><br />
	        </div>
	    </div>
	</div>
	<!-- Login modal -->	
	<div class="modal fade" id="loginModal" tabindex="-1" role="dialog">
	    <div class="modal-dialog" role="document">
	        <div class="modal-content col-lg-offset-2 col-lg-8 col-md-offset-2 col-md-8" id="login_form">
	        	<br /><br />
	            <label>Username: </label><input id="login_username" type="text" class="form-control" />
				<br />
				<label>Password: </label><input id="login_password" type="password" class="form-control" />
				<br />
				<div class="btn-group col-md-offset-7 col-md-6">
					<button class="btn btn-primary" id="login_btn">Log in</button>
					<button class="btn btn-default" id="login_cancel_btn">Cancel</button>
					<br /><br />
				</div>
				<br /><br />
	        </div>
	    </div>
	</div>
	<script type="text/javascript" src="bootstrap/js/jquery-2.1.3.min.js"></script>
	<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="script/navigation.js"></script>
	<script type="text/javascript" src="script/core.js"></script>
	<script type="text/javascript" src="script/training.js"></script>
	<script type="text/javascript" src="script/user.js"></script>
	<script type="text/javascript" src="script/validation.js"></script>
</body>
</html>