<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>RadiSS - Training</title>
	<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
	<script type="text/javascript" src="bootstrap/js/jquery-2.1.3.min.js"></script>
</head>
<body>

	<br />
	<div class="panel panel-default col-md-offset-1 col-md-10">
		<div class="panel-heading">
			<h3 class="panel-title">Scaling factors</h3>
		</div>
		<div class="panel-body">
			<table class="table">
				<tr>
					<th>Feature</th>
					<th>Minimum value</th>
					<th>Maximum value</th>
				</tr>
				<tr>
					<td>Entropy</td>
					<td>0.0</td>
					<td>1.0</td>
				</tr>
			</table>
		</div>
	</div>
	<br />
	<div class="panel panel-default col-md-offset-1 col-md-10">
		<div class="panel-heading">
			<h3 class="panel-title">Principal components</h3>
		</div>
		<div class="panel-body">
			<table class="table">
				<tr>
					<th>Principal component</th>
					<th>Value</th>
				</tr>
				<tr>
					<td>A</td>
					<td>1.0</td>
				</tr>
			</table>
		</div>
	</div>
	<br />
	<div class="panel panel-default col-md-offset-1 col-md-10">
		<div class="panel-heading">
			<h3 class="panel-title">SVM Model</h3>
		</div>
		<div class="panel-body">
			<table class="table">
				<tr>
					<th>Classes</th>
					<td>A, B, C</td>
				</tr>
				<tr>
					<th>Accuracy (%)</th>
					<td>1.0</td>
				</tr>
			</table>
		</div>
	</div>
	<br />
	<div class="btn-group col-md-offset-8 col-md-4">
		<button class="btn btn-primary">Save model</button>
		<button class="btn btn-default">Rebuild model</button>
	</div>
	<br />
	
</body>
</html>