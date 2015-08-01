<script type="text/javascript">
$(function() {
	$.ajax({
		url: 'trainingapp/getmodellist',
		dataType: 'json',
		success: function(response) {
			
			modelList = response;
			if(modelList.length > 0) {
				for(var i=0; i<modelList.length; i++) {
					if(modelList[i].hasOwnProperty('model')) {
						var classifier = modelList[i].model;						
						var features = classifier.isIJUsed 
								? "Shape and basic texture features" 
								: "Shape and Haralick texture descriptors";
						var dloadURL = "trainingapp/download?modelKey=" + modelList[i].key.blobKey;
						var modelId = "modelInfo_" + classifier.version;
						
						var tRow = "<tr>" +
										"<td><a href=\"#\">" +
											"<span class=\"glyphicon glyphicon-chevron-right\" id=\"collapse_icon_" + modelId + "\"></span>" +
										"</a></td>" +
										"<td>" + classifier.createdDate + "</td>" +
										"<td>" + classifier.version + "</td>" +
										"<td>" + classifier.notes + "</td>" +
										"<td><a href=" + dloadURL + ">" +
											"<span class=\"glyphicon glyphicon-download-alt\"></span>" +
										"</a></td>" + 
									"</tr>";
						
						var iRow = "<tr id=" + modelId + ">" +
										"<td>&nbsp;</td>" +
										"<td colspan=3>" +
											"<table class=table>" +
												"<tr>" +
													"<th>Classifier</th>" +
													"<th>Classes</th>" +
													"<th>Features</th>" +
													"<th>Accuracy</th>" +
												"</tr>";
						
						if(classifier.decisionTreeModel != null || classifier.hasOwnProperty('decisionTreeModel')) {
							var row = "<tr>" +
										"<td>Decision tree</td>" +
										"<td>" + classifier.decisionTreeModel.classes + "</td>" +
										"<td> - </td>" +
										"<td>" + classifier.decisionTreeModel.accuracy + "</td>" +
									  "</tr>";
									  
							iRow += row;
						}
												
						if(classifier.svmmodel != null) {
							var row = "<tr>" +
										"<td>SVM</td>" +
										"<td>" + classifier.svmmodel.classes + "</td>" +
										"<td>" + features + "</td>" +
										"<td>" + classifier.svmmodel.accuracy + "</td>" +
									  "</tr>";
							
							iRow += row;
						}
												
						iRow += "</table><td>&nbsp;</td></td>/tr>";

						$("#model_table").append(tRow);
						$("#model_table").append(iRow);
					}
					
					$("#model_table").find("[id^=modelInfo]").hide();
				}
			}
			else {
				var tRow = "<tr>" +
								"<td colspan=4><em><center> No classifier models available for download. </center></em></td>" +
							"</tr>";
				$("#model_table").append(tRow);
			}
		},
		error: function() {
			alertType = "warning";
			fxnCallback("An error occurred in retrieving models. Please refresh page.");
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
				<th>&nbsp;</th>
				<th>Date</th>
				<th>Version number</th>
				<th>Release notes</th>
				<th>&nbsp;</th>
			</tr>
		</table>
	</div>
</div>
<br />