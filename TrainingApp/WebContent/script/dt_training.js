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
			success: function() {
				processImage();
			},
			error: function() {
				alertType = "error";
				fxnCallback("Unable to read imageset.");
			},
		});
	});
});