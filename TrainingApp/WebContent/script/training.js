var model = {};

$(function() {	
	var readSVMParameters = function() {
		var params = {};
		params["type"] = $("#svm_type").val();
		params["kernel"] = $("#svm_kernel").val();
		params["cost"] = $("#svm_cost").val();
		params["gamma"] = $("#svm_gamma").val();
		params["eps"] = $("#svm_eps").val();
		params["degree"] = $("#svm_degree").val();
		params["nu"] = $("#svm_nu").val();
		params["coef"] = $("#svm_coef").val();

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
					model.isIJused = (trainFeature == "IJ") ? true : false;
					
					$('#content_holder').load('Training_Output.jsp');
				});
			},
			error: function() { alert("ERROR!"); },
		});
	};
	
	var reduceFeatures = function() {
		$.ajax({
			url: 'trainingapp/preprocess/reducefeatures',
			success: function() {
				buildModel();
			},
			error: function() { return null; },
		});
	};

	var scale = function() {
		$.ajax({
			url: 'trainingapp/preprocess/scale',
			success: function() {
				reduceFeatures();
			},
			error: function() { return null; },
		});
	};

	var preprocess = function() {
		var pca = $("#train_preprocess_pca").val();

		$.ajax({
			url: 'trainingapp/preprocess/setpca?pca=' + pca,
			success: function() {
				scale();
			},
			error: function() { return null; },
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
			success: function() {
				preprocess();
			},
			error: function() { return false; },
		});
	});

//	$("#content_holder").on("click","#train_save_btn", function() {
//		$.ajax({
//			url: "trainingapp/saveclassifiermodel",
//			method : "POST",
//			data: JSON.stringify(model),
//			dataType : "json",
//			async: false,
//			success : function(response) {
//				if (response == "true") {
//					fxnCallback("Classifier model file saved successfully.");
//				} else {
//					fxnCallback("Unable to save file. Please try again.");
//				}
//			},
//			error : function() {
//				fxnCallback("An error has occurred.");
//			}
//		});
//	});

	$("#content_holder").on("click","#train_cancel_btn", function() {
		$("#content_holder").load("Home.jsp");
	});

	$("#content_holder").on("click","#train_rebuild_btn", function() {
		$("#content_holder").load("Training_Input.jsp");
	});
});