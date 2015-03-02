$(function() {
	var readDataset = function() {
		$.ajax({
			url: 'trainingapp/readdataset',
			success: function(response) { return response; },
			error: function() { return null; },
		});
	};
	
	var readSVMParameters = function() {
		var params = {};
		params["cost"] = $("#svm_cost").val();
		params["gamma"] = $("#svm_gamma").val();
		params["eps"] = $("#svm_eps").val();
		params["degree"] = $("#svm_degree").val();
		params["nu"] = $("#svm_nu").val();
		params["coef"] = $("#svm_coef").val();

		return params;
	};

	var scale = function(dataset) {
		$.ajax({
			url: 'trainingapp/preprocess/scale',
			data: data,
			success: function(response) { return response; },
			error: function() { return null; },
		});
	};

	var reduceFeatures = function(dataset) {
		$.ajax({
			url: 'trainingapp/preprocess/scale',
			data: data,
			success: function(response) { return response; },
			error: function() { return null; },
		});
	};

	var preprocess = function(dataset) {
		var pca = $("#train_preprocess_pca").val();
		
		$.ajax({
			url: 'trainingapp/preprocess/setpca/pca=' + pca,
			success: function(response) { return response; },
			error: function() { return null; },
		});
		
		var preprocessedData = scale(data);

		if(preprocessedData  != null)
			preprocessedData = reduceFeatures(preprocessedData);

		return preprocessedData;
	};

	var buildModel = function(dataset) {
		var data = {};
		data["dataset"] = dataset;
		data["svm_parameters"] = readSVMParameters();
		
		$.ajax({
			url: 'trainingapp/svm/buildmodel',
			data: data,
			success: function(response) { return response; },
			error: function() { return null; },
		});
	};
	
	$("#content_holder").on("click","#train_build_btn", function() {
		var dataset = readDataset();

		if(dataset != null) {
			var preprocessedDataset = preprocess(dataset);
			var model = buildModel(dataset);

			if(model != null) {
				$("#content_holder").load("Training_Output.jsp");
			}
		}
	});

	$("#content_holder").on("click","#train_save_btn", function() {
		var notes = "";
		
		$.ajax({
			url: "trainingapp/saveclassifiermodel",
			method : "POST",
			data: notes;
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