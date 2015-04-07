var model = {};

$(function() {	
	var readSVMParameters = function() {
		var params = {};
		params["type"] = $("#svm_type").val();
		params["kernel"] = $("#svm_kernel").val();
		params["cost"] = $("#svm_cost").val();
		params["gamma"] = $("#svm_gamma").val();
		params["epsilon"] = $("#svm_eps").val();
		params["degree"] = $("#svm_degree").val();
		params["nu"] = $("#svm_nu").val();
		params["coefficient"] = $("#svm_coef").val();

		return params;
	};
	
	var buildModel = function() {
		var data = readSVMParameters();

		$.ajax({
			url: 'trainingapp/svm/buildmodel',
			method: 'POST',
			data: JSON.stringify(data),
			contentType: 'json',
			success: function() {
				$.when(getPreprocessModel(), getSVMModel()).done(function() {
					var trainFeature = $('input[name=train_feature]:checked').val();
					model.isIJUsed = (trainFeature == "IJ") ? true : false;
					console.log(model);
					$('#content_holder').load('Training_Output.jsp');
				});
			},
			error: function() {
				alertType = "error";
				fxnCallback("An error occurred in building SVM model.");
			},
		});
	};
	
	var reduceFeatures = function() {
		$.ajax({
			url: 'trainingapp/preprocess/reducefeatures',
			success: function() {
				buildModel();
			},
			error: function() {
				alertType = "warning";
				fxnCallback("An error occurred in preprocessing the data set.");
			},
		});
	};

	var scale = function() {
		$.ajax({
			url: 'trainingapp/preprocess/scale',
			crossDomain: true,
			success: function() {
				reduceFeatures();
			},
			error: function() {
				alertType = "warning";
				fxnCallback("An error occurred in preprocessing the data set.");
			},
		});
	};

	var preprocess = function() {
		var pca = $("#train_preprocess_pca").val();

		$.ajax({
			url: 'trainingapp/preprocess/setpca?pca=' + pca,
			success: function() {
				scale();
			},
			error: function() {
				alertType = "warning";
				fxnCallback("An error occurred in preprocessing the data set.");
			},
		});
	};
	
	var getPreprocessModel = function() {
		$.ajax({
			url: 'trainingapp/preprocess/getmodel',
			dataType: 'json',
			async: false,
			success: function(response) {
				model.preprocessModel = response;
			},
			error: function(response) {
				model.preprocessModel = "";
			}
		});
	};
	
	var getSVMModel = function() {
		$.ajax({
			url: 'trainingapp/svm/getmodel',
			dataType: 'json',
			async: false,
			success: function(response) {
				model.svmmodel = response;
			},
			error: function() {
				model.svmmodel = "";
			}
		});
	};
	
	$("#content_holder").on("click","#train_build_btn", function() {
		$.ajax({
			url: 'trainingapp/readdataset',
			async: false,
			success: function() {
				preprocess();
			},
			error: function() {
				alertType = "error";
				fxnCallback("Unable to read dataset.");
			},
		});
	});

	$("#content_holder").on("click","#train_cancel_btn", function() {
		$("#content_holder").load("Home.jsp");
	});

	$("#content_holder").on("click","#train_rebuild_btn", function() {
		$("#content_holder").load("Training_Input.jsp");
	});
});