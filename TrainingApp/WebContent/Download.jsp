<script type="text/javascript">
$(function() {
	$.ajax({
		url: 'trainingapp/getmodellist',
		dataType: 'json',
		success: function(response) {
			modelList = response;
			
			if(modelList.length > 0) {
				for(var i=0; i<modelList.length; i++) {
					var classifier = modelList[i].model;
					var features = classifier.isIJUsed 
							? "Shape and basic texture features" 
							: "Shape and Haralick texture descriptors";
					var dloadURL = "trainingapp/download?modelKey=" + modelList[i].key.blobKey;
					
					var tRow = "<tr>" +
									"<td>" + classifier.createdDate + "</td>" +
									"<td>" + classifier.version + "</td>" +
									"<td>" + classifier.svmmodel.accuracy + "</td>" +
									"<td>" + features + "</td>" +
									"<td>" + classifier.notes + "</td>" +
									"<td><a href=" + dloadURL + ">" +
										"<span class=\"glyphicon glyphicon-download-alt\"></span>" +
									"</a></td>" + 
								"</tr>";
					
					$("#model_table").append(tRow);
				}
			}
			else {
				var tRow = "<tr>" +
								"<td colspan=6><em><center> No classifier models available for download. </center></em></td>" +
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
				<th>Classification Accuracy</th>
				<th>Features used</th>
				<th>Release notes</th>
				<th>&nbsp;</th>
			</tr>
		</table>
	</div>
</div>
<br />