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
				$('#content_holder').load('Training_Output.jsp');
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
	
	var getClassifierModel = function() {
		var model = {};
		model.createdDate = new Date();
//		model.preprocessModel = "";
//		model.svmmodel = "";
		
		// PreProcess model
		$.ajax({
			url: 'trainingapp/preprocess/getmodel',
			success: function(response) {
				alert("PREPROCESS:" + response);
				
				model.preprocessModel = JSON.parse(response);
			},
			error: function() {				
				model.preprocessModel = "";
			}
		});
		
		// SVM model
		$.ajax({
			url: 'trainingapp/svm/getmodel',
			success: function(response) {
				alert("SVM:" + response);
				model.svmmodel = response;
			},
			error: function() {
				model.svmmodel = "";
			}
		});
		
		return model;
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

	$("#content_holder").on("click","#train_save_btn", function() {
		var model = getClassifierModel();

		$.ajax({
			url: "trainingapp/saveclassifiermodel",
			method : "POST",
			data: JSON.stringify(model),
			dataType : "json",
			success : function(response) {
				if (response == "true") {
					trainingCallback("Classifier model file saved successfully.");
				} else {
					trainingCallback("Unable to save file. Please try again.");
				}
			},
			error : function() {
				trainingCallback("An error has occurred.");
			}
		});
	});

	$("#content_holder").on("click","#train_cancel_btn", function() {
		$("#content_holder").load("Home.jsp");
	});

	$("#content_holder").on("click","#train_rebuild_btn", function() {
		$("#content_holder").load("Training_Input.jsp");
	});
});