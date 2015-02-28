$(function() {
	var readSVMParameters = function() {
		var params = {};
		params["cost"] = $("#svm_cost").html();
		params["gamma"] = $("#svm_gamma").html();
		params["eps"] = $("#svm_eps").html();
		params["degree"] = $("#svm_degree").html();
		params["nu"] = $("#svm_nu").html();
		params["coef"] = $("#svm_coef").html();

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
		var preprocessedData = scale(data);

		if(preprocessedData  != null)
			preprocessedData = reduceFeatures(preprocessedData);

		return preprocessedData;
	};

	var buildModel = function(dataset) {
		$.ajax({
			url: 'trainingapp/svm/buildmodel',
			data: data,
			success: function(response) { return response; },
			error: function() { return null; },
		});
	};
	
	$("#content_holder").on("click","#train_build_btn", function() {
		var dataset = null;
		
		$.ajax({
			url: 'trainingapp/readdataset',
			dataType: "json",
			success: function(response) { dataset = response; },
			error: function() { return null; },
		});
//		var preprocessedDataset = preprocess(dataset);
//		var model = buildModel(dataset);
//
//		if(model != null) {
//			$("#content_holder").load("Training_Output.jsp");
//		}
	});

	$("#content_holder").on("click","#train_save_btn", function() {
		$.ajax({
			url: "trainingapp/saveclassifiermodel",
			method : "POST",
			contentType : false,
			processData : false,
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