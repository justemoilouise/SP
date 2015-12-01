<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%! BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService(); %>

<script type="text/javascript">
$(function() {
	var svm_type = ["C-SVC", "nu-SVC", "one-class SVM", "epsilon-SVR", "nu-SVR"];
	var svm_kernel = ["Linear", "Polynomial", "Radial basis", "Sigmoid"];
	
	for(var i=0; i<svm_type.length; i++) {
		var option = "<option value=" + i + ">" + svm_type[i] + "</option>";
		$("#train_svm_type").append(option);
	}
	
	for(var i=0; i<svm_kernel.length; i++) {
		var option = "<option value=" + i + ">" + svm_kernel[i] + "</option>";
		$("#train_svm_kernel").append(option);
	}
	
	$("#input_file").on("click", "#input_file_btn", function(){
		toggleProgress();
		var file = $("#input_file_fld").get(0).files[0];
		var data = new FormData();
		data.append("file", file);
		
		$.ajax({
			url: '<%= blobstoreService.createUploadUrl("/trainingapp/upload") %>',
			method: "POST",
			contentType: false,
			processData: false,
			data: data,
			dataType: "json",
			async: false,
			success: function(response) {
				alertType = "success";
				fxnCallback("File uploaded successfully.");
				$("#parametersPanel").show();
			},
			error: function() {
				alertType = "error";
				fxnCallback("Unable to upload file. Please try again.");
			},
			complete: function() {
				$('html, body').animate({scrollTop: 0}, 'fast');
				toggleProgress();
			}
		});
	});
	$("#parametersPanel").toggle();
});
</script>

<br />
<div class="input-group col-md-offset-1 col-md-10 col-xs-offset-1 col-xs-10" id="input_file">
	<input type="file" accept="application/vnd.ms-excel, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" class="col-md-10 col-xs-10" id="input_file_fld" />
	<button type="submit" class="btn btn-primary col-md-2 col-xs-2" id="input_file_btn">
		<span class="glyphicon glyphicon-check"></span>
	</button>
</div>
<br />
<div id="parametersPanel">
	<div class="panel panel-default col-md-offset-1 col-md-10">
		<div class="panel-heading">Features to use</div>
		<div class="panel-body">
			&emsp;<input type="radio" value="IJ" name="train_feature" />&emsp;<label>Shape and basic texture features</label><br />
			&emsp;<input type="radio" value="JF" name="train_feature" />&emsp;<label>Shape and Haralick texture descriptors</label>
		</div>
	</div>
	<br />
	<div class="panel panel-default col-md-offset-1 col-md-10">
		<div class="panel-heading">Preprocessing</div>
		<div class="panel-body">
			<table class="table">
				<tr>
					<th>No. of principal components</th>
					<td><input type="text" id="train_preprocess_pca" /></td>
					<td><em>(suggested: 4)</em></td>
				</tr>
			</table>
		</div>
	</div>
	<br />
	<div class="panel panel-default col-md-offset-1 col-md-10">
		<div class="panel-heading">SVM Parameters</div>
		<div class="panel-body">
			<table class="table">
				<tr>
					<th>Parameter</th>
					<th>Value</th>
					<th></th>
				</tr>
				<tr>
					<td>SVM Type</td>
					<td><select id="train_svm_type"></select></td>
					<td><em>(suggested: C-SVC)</em></td>
				</tr>
				<tr>
					<td>Kernel function</td>
					<td><select id="train_svm_kernel"></select></td>
					<td><em>(suggested: Radial basis)</em></td>
				</tr>
				<tr>
					<td>Cost</td>
					<td><input type="text" id="svm_cost" /></td>
					<td><em>(suggested: 60 for basic texture features, 251 for Haralick)</em></td>
				</tr>
				<tr>
					<td>Gamma</td>
					<td><input type="text" id="svm_gamma" /></td>
					<td><em>(suggested: 0.42 for basic texture features, 0.08 for Haralick)</em></td>
				</tr>
				<tr>
					<td>Epsilon</td>
					<td><input type="text" id="svm_eps" /></td>
					<td><em>(suggested: 0.59 for basic texture features, 0.53 for Haralick)</em></td>
				</tr>
				<tr>
					<td>Degree</td>
					<td><input type="text" id="svm_degree" /></td>
					<td><em>(suggested: -1.7 for basic texture features, 1.6 for Haralick)</em></td>
				</tr>
				<tr>
					<td>Nu</td>
					<td><input type="text" id="svm_nu" /></td>
					<td><em>(suggested: 0.99 for basic texture features, 0.70 for Haralick)</em></td>
				</tr>
				<tr>
					<td>Coefficient</td>
					<td><input type="text" id="svm_coef" /></td>
					<td><em>(suggested: 0.14 for basic texture features, 0.69 for Haralick)</em></td>
				</tr>
			</table>
		</div>
	</div>
	<br />
	<div class="btn-group col-md-offset-9 col-md-3 col-xs-offset-9 col-xs-3" id="train_form">
		<button class="btn btn-primary" id="train_build_btn">Build model</button>
		<button class="btn btn-default" id="train_cancel_btn">Cancel</button>
	</div>
</div>
<br /><br />