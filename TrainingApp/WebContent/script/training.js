$(function() {
	$("#train_cancel_btn").on("click", function() {
		alert("CANCEEEEL!");
	});
});

//function preprocess(data) {
//	var preprocessedData = scale(data);
//	
//	if(preprocessedData  != null)
//		preprocessedData = reduceFeatures(preprocessedData);
//	
//	return preprocessedData;
//}
//
//function scale(data) {
//	$.ajax({
//		url: 'trainingapp/preprocess/scale',
//		data: data,
//		success: function(response) { return response; },
//		error: function() { return null; },
//	});
//}
//
//function reduceFeatures(data) {
//	$.ajax({
//		url: 'trainingapp/preprocess/scale',
//		data: data,
//		success: function(response) { return response; },
//		error: function() { return null; },
//	});
//}
//
//function buildModel(data) {
//	
//}