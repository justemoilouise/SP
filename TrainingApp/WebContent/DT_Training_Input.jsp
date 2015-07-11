<%@ page import="com.google.appengine.api.blobstore.BlobstoreServiceFactory" %>
<%@ page import="com.google.appengine.api.blobstore.BlobstoreService" %>
<%! BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService(); %> 

<script type="text/javascript">
$(function() {	
	$("#input_files").on("click", "#input_files_btn", function(){
		var files = $("#input_files_fld").get(0).files;
		fileCount = 0;
		
		for(var i=0; i<files.length; i++) {
			var data = new FormData();
			data.append("imageset", files[i]);

			$.ajax({
				url: '<%= blobstoreService.createUploadUrl("/trainingapp/decisiontree/upload") %>',
				method: "POST",
				contentType: false,
				processData: false,
				data: data,
				dataType: "json",
				async: false,
				success: function(response) {
					if(response===true) {
						fileSuccess.push(files[i]);
					}
					else {
						fileError.push(files[i]);
					}
				},
				error: function(response) {
					fileError.push(files[i]);
				}
			});
		}
		showFileUploadResult();
	});
	
	$("#files_success_panel").hide();
	$("#files_error_panel").hide();
});
</script>
 
<br />
<div class="input-group col-md-offset-1 col-md-10 col-xs-offset-1 col-xs-10" id="input_files">
	<input type="file" name="files[]" class="col-md-10 col-xs-10" id="input_files_fld" multiple="multiple" webkitdirectory />
	<button type="submit" class="btn btn-primary col-md-2 col-xs-2" id="input_files_btn">
		<span class="glyphicon glyphicon-check"></span>
	</button>
</div>
<br />
<div class="panel panel-default col-md-offset-1 col-md-10" id="files_success_panel">
	<div class="panel-heading">Successul file upload:</div>
	<div class="panel-body" id="files_success">
	</div>
</div>
<br />
<div class="panel panel-default col-md-offset-1 col-md-10" id="files_error_panel">
	<div class="panel-heading">Unsuccessful file upload:</div>
	<div class="panel-body" id="files_error">
	</div>
</div>
<br />
<div class="btn-group col-md-offset-9 col-md-3 col-xs-offset-9 col-xs-3" id="train_form">
	<button class="btn btn-primary" id="dt_train_build_btn">Train model</button>
	<button class="btn btn-default" id="dt_train_cancel_btn">Cancel</button>
</div>
<br /><br />