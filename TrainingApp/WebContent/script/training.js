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
			data: data,
			success: function(response) {
				if(response == "true") {
					$('#content_holder').load('Training_Output.jsp');
				}
			},
			error: function() { return null; },
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
			url: 'trainingapp/preprocess/setpca/pca=' + pca,
			success: function() {
				scale();
			},
			error: function() { return null; },
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

	$("#content_holder").on("click","#train_save_btn", function() {
		var notes = "";

		$.ajax({
			url: "trainingapp/saveclassifiermodel",
			method : "POST",
			data: notes,
			dataType : "json",
			success : function(response) {
				if (response.indexOf("true") >= 0) {
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