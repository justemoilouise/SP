<script type="text/javascript">
$(function() {
	$.ajax({
		url: 'trainingapp/getmodellist',
		dataType: 'json',
		success: function(response) {
			modelList = response;
			for(var i=0; i<modelList.length; i++) {
				var classifier = modelList[i].model;
				var tRow = "<tr>" +
								"<td>" + classifier.createdDate + "</td>" +
								"<td>" + classifier.version + "</td>" +
								"<td>" + classifier.notes + "</td>" +
								"<td><a href=#>" +
									"<span class=\"glyphicon glyphicon-download-alt\" id=" + modelList[i].key.blobKey + ">" +
									"</span>" +
								"</a></td>" + 
							"</tr>";
				
				$("#model_table").append(tRow);
			}
		},
		error: function() {
			alertType = "warning";
			fxnCallback("An error occurred in retriving models. Please refresh page.");
		},
		complete: function() {
			$('html, body').animate({scrollTop: 0}, 'fast');
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
				<th>&nbsp;</th>
			</tr>
		</table>
	</div>
</div>
<br />