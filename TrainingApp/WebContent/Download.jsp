<script type="text/javascript">
$(function() {
	$.ajax({
		url: 'trainingapp/getmodellist',
		dataType: 'json',
		success: function(response) {
			modelList = response;
			for(var i=0; i<modelList.length; i++) {
				var classifier = modelList[i].model;
				var tRow = "<tr id=" + modelList[i].key.blobKey + ">" +
								"<td>" + new Date() + "</td>" +
								"<td>" + classifier.version + "</td>" +
								"<td> &nbsp; </td>" +
								"<a href=#><span class=\"glyphicon glyphicon-download-alt\" id=\"dload_btn\"></span></a>" + 
							"</tr>";
				
				$("#model_table").append(tRow);
			}
		},
		error: function() {
			alert("ERROR: Get model list");
		}
	});
});
</script>
<br />
<div class="panel panel-default col-md-offset-1 col-md-10" id="dload_model">
	<div class="panel-heading">
		<h3 class="panel-title">Classifier model</h3>
	</div>
	<div class="panel-body">
		<table class="table" id="model_table">
			<tr>
				<th>Date</th>
				<th>Version number</th>
				<th>Release notes</th>
			</tr>
			<!--
			<tr>
				<td><%= new java.util.Date() %></td>
				<td>1.0</td>
				<td>Beta</td>
			</tr>
			-->
		</table>
	</div>
</div>
<br />