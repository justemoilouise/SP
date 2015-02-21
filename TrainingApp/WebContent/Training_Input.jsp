<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%! BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService(); %>

<br />
<div class="input-group col-md-offset-1 col-md-10">
	 <form action="<%= blobstoreService.createUploadUrl("/trainingapp/upload") %>" method="post" enctype="multipart/form-data">
		<input type="file" width="50" class="col-md-10" id="input_file" name="dataset" />
		<input type="submit" class="btn btn-primary col-md-2" name="submit" value="Upload" />
	</form>
	<!--
	<input type="file" width="50" class="col-md-10" id="input_file" />
	<button type="submit" class="btn btn-primary col-md-2" id="input_file_btn">
		<span class="glyphicon glyphicon-check"></span>
	</button>
	-->
</div>
<br />
<div class="panel panel-default col-md-offset-1 col-md-10">
	<div class="panel-heading">Features to use</div>
	<div class="panel-body">
		&emsp;<input type="radio" value="IJ" name="train_feature" />&emsp;<label>Basic texture and shape features</label><br />
		&emsp;<input type="radio" value="JF" name="train_feature" />&emsp;<label>Haralick texture descriptors</label>
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
			</tr>
			<tr>
				<td>Cost</td>
				<td id="svm_cost">1</td>
			</tr>
			<tr>
				<td>Gamma</td>
				<td id="svm_gamma">1</td>
			</tr>
			<tr>
				<td>Epsilon</td>
				<td id="svm_eps">1</td>
			</tr>
			<tr>
				<td>Degree</td>
				<td id="svm_degree">1</td>
			</tr>
			<tr>
				<td>Nu</td>
				<td id="svm_nu">1</td>
			</tr>
			<tr>
				<td>Coefficient</td>
				<td id="svm_coef">1</td>
			</tr>
		</table>
	</div>
</div>
<br />
<div class="btn-group col-md-offset-9 col-md-3" id="train_form">
	<button class="btn btn-primary" id="train_build_btn">Build model</button>
	<button class="btn btn-default" id="train_cancel_btn">Cancel</button>
</div>
<br />