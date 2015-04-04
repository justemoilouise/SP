<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%! BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService(); %>

<script type="text/javascript">
$(function() {
	var pModel = model.preprocessModel;
	
	// Scaling factors
	for(var i=0; i<pModel.min.length; i++) {
		var tRow = '<tr>';
		tRow += '<td>' + pModel.features[i] + '</td>';
		tRow += '<td>' + pModel.min[i] + '</td>';
		tRow += '<td>' + pModel.max[i] + '</td>';
		tRow += '</tr>';
		$('#tbl_scale').append(tRow);
	}
	
	// Principal components
	$('#tbl_princomp_val').attr('colspan', pModel.PC);
	for(var i=0; i<pModel.principalComponents[0].length; i++) {
		var tRow = '<tr>';
		tRow += '<td>' + (i+1) + '</td>';
		for(var j=0; j<pModel.principalComponents.length; j++) {
			tRow += ('<td>' + pModel.principalComponents[j][i] + '</td>');
		}
		tRow += '</tr>';
		$('#tbl_princomp').append(tRow);
	}
	
	var sModel = model.svmmodel;

	// Classes
	var sRow = '<tr>';
	sRow += '<td>Classes</td>';
	for(var i=0; i<sModel.classes.length; i++) {
		sRow += ('<td>' + sModel.classes[i] + '</td>');
	}
	sRow += '</tr>';
	$('#tbl_svm').append(sRow);	
	
	//Accuracy
	sRow = '<tr>';
	sRow += '<td>Accuracy</td>';
	sRow += '<td colspan=' + sModel.classes.length + '>' + sModel.accuracy + '</td>';
	sRow += '</tr>';
	$('#tbl_svm').append(sRow);
	
	$("#content_holder").on("click","#train_save_btn", function() {
		var modelObj = $.param(model);
		
		$.ajax({
			url: "<%= blobstoreService.createUploadUrl("/trainingapp/uploadclassifiermodel") %>",
			method: "POST",
			contentType: false,
			processData: false,
			data: modelObj,
			dataType : "json",
			success : function(response) {
				if (response == "true") {
					alertType = "success";
					fxnCallback("Classifier model file saved successfully.");
				} else {
					alertType = "warning";
					fxnCallback("Unable to save file. Please try again.");
				}
			},
			error : function() {
				alertType = "error";
				fxnCallback("An error has occurred.");
			}
		});
	});
})
</script>
<br />
<div class="panel panel-default col-md-offset-1 col-md-10">
	<div class="panel-heading">
		<h3 class="panel-title">Scaling factors</h3>
	</div>
	<div class="panel-body">
		<table class="table" id="tbl_scale">
			<tr>
				<th>Feature</th>
				<th>Minimum value</th>
				<th>Maximum value</th>
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
		<table class="table" id="tbl_princomp">
			<tr>
				<th>Principal component</th>
				<th id="tbl_princomp_val">Values</th>
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
		<table class="table" id="tbl_svm">
			<tr>
				<th>Property</th>
				<th>Value/s</th>
			</tr>
		</table>
	</div>
</div>
<br />
<div class="btn-group col-md-offset-8 col-md-4">
	<button class="btn btn-primary" id="train_save_btn">Save model</button>
	<button class="btn btn-default" id="train_rebuild_btn">Rebuild model</button>
</div>
<br />