var fileSuccess = new Array();
var fileError = new Array();

var showFileUploadResult = function() {
	if(fileSuccess.length > 0) {
		for(var i=0; i<fileSuccess.length; i++) {
			$("#files_success")
			.append("&emsp;<span class=\"glyphicon glyphicon-ok\" style=\"color:green;\"></span>&emsp;")
			.append(fileSuccess[i].name)
			.append("<br />");
		}

		$("#files_success_panel").show();
	}

	if(fileError.length > 0) {
		for(var i=0; i<fileError.length; i++) {
			$("#files_error")
			.append("&emsp;<span class=\"glyphicon glyphicon-remove\" style=\"color:red;\"></span>&emsp;")
			.append(fileError[i].name)
			.append("<br />");
		}

		$("#files_error_panel").show();
	}
};

$(function() {	
	var crossvalidate = function() {
		$.ajax({
			url: 'trainingapp/decisiontree/crossvalidate',
			async: false,
			success: function() {
				// load output page
			},
			error: function() {
				alertType = "error";
				fxnCallback("An error occurred in cross validating decision tree model.");
			},
		});
	};

	var processImage = function() {
		$.ajax({
			url: 'trainingapp/decisiontree/processimageset',
			async: false,
			success: function() {
				crossvalidate();
			},
			error: function() {
				alertType = "error";
				fxnCallback("Unable to process imageset.");
			},
		});
	};

	$("#content_holder").on("click", "#dt_train_build_btn", function() {
		$("#dt_train_build_btn").button('Cross-validating model');

		$.ajax({
			url: 'trainingapp/decisiontree/readimageset',
			async: false,
			success: function(response) {
				if(response===true)
					processImage();
				else {
					alertType = "error";
					fxnCallback("Unable to read imageset.");
				}
			},
			error: function() {
				alertType = "error";
				fxnCallback("Unable to read imageset.");
			},
		});
	});
});