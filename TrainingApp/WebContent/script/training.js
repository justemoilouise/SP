$(function() {
	var readFileInput = function() {
//		var file = $("#input_file").get(0).files[0];
//		
//		var data = new FormData();
//		data.append("file", file);
		
//		$.ajax({
//			url: 'trainingapp/readdataset',
//			contentType: false,
//			processData: false,
//			method: "POST",
//			dataType: "json",
//			data: data,
//			success: function(response) {
//				console.log(response);
//				return response;
//			},
//			error: function() { return null; },
//		});
		$.ajax({
			url: 'trainingapp/read',
			dataType: "json",
			success: function(response) {
				return response;
			},
			error: function() { return null; },
		});
	};

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
		var dataset = readFileInput();
		console.log(dataset);
//		var preprocessedDataset = preprocess(dataset);
//		var model = buildModel(dataset);
//
//		if(model != null) {
//			$("#content_holder").load("Training_Output.jsp");
//		}
	});

	$("#content_holder").on("click","#train_cancel_btn", function() {
		$("#content_holder").load("Home.jsp");
	});

	$("#content_holder").on("click","#train_save_btn", function(data) {
		$.ajax({
			url: 'trainingapp/saveclassifiermodel',
			data: data,
			dataType: "json",
			success: function(response) { return response; },
			error: function() { return null; },
		});
	});

	$("#content_holder").on("click","#train_rebuild_btn", function() {
		$("#content_holder").load("Training_Input.jsp");
	});
});