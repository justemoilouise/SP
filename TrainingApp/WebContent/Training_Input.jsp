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
	<div class="input-group col-md-offset-1 col-md-10" id="input_file">
		<input type="file" width="50" class="col-md-10" />
		<button type="submit" class="btn btn-primary col-md-2"><span class="glyphicon glyphicon-check"></span></button>
	</div>
	<br />
	<div class="panel panel-default col-md-offset-1 col-md-10">
		<div class="panel-heading"> SVM Parameters</div>
		<div class="panel-body">
			<table class="table">
				<tr>
					<th>Parameter</th>
					<th>Value</th>
				</tr>
				<tr>
					<td>Cost</td>
					<td>1</td>
				</tr>
				<tr>
					<td>Gamma</td>
					<td>2</td>
				</tr>
			</table>
		</div>
	</div>
	<br />
	<div class="btn-group col-md-offset-9 col-md-3">
		<button class="btn btn-primary">Build model</button>
		<button class="btn btn-default">Cancel</button>
	</div>
	<br />

</body>
</html>