$(function() {
	$("#content_holder").on("click","#train_build_btn", function() {
		alert("Build model");
		
		var dataset = readFile();
		var preprocessedDataset = preprocess(dataset);
		buildModel(dataset);
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

function readFile() {
	var filename = $("#input_file").val();
	
	$.ajax({
		url: 'trainingapp/readdataset',
		method: "GET",
		success: function(response) { return response; },
		error: function() { return null; },
	});
}

function readSVMParameters() {
	var params = {};
	params["cost"] = $("#svm_cost").html();
	params["gamma"] = $("#svm_gamma").html();
	params["eps"] = $("#svm_eps").html();
	params["degree"] = $("#svm_degree").html();
	params["nu"] = $("#svm_nu").html();
	params["coef"] = $("#svm_coef").html();
	
	return params;
}

function preprocess(data) {
	var preprocessedData = scale(data);
	
	if(preprocessedData  != null)
		preprocessedData = reduceFeatures(preprocessedData);
	
	return preprocessedData;
}

function scale(data) {
	$.ajax({
		url: 'trainingapp/preprocess/scale',
		data: data,
		success: function(response) { return response; },
		error: function() { return null; },
	});
}

function reduceFeatures(data) {
	$.ajax({
		url: 'trainingapp/preprocess/scale',
		data: data,
		success: function(response) { return response; },
		error: function() { return null; },
	});
}

function buildModel(data) {
	$.ajax({
		url: 'trainingapp/svm/buildmodel',
		data: data,
		success: function(response) { return response; },
		error: function() { return null; },
	});
}