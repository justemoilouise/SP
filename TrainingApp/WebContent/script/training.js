$(function() {
	$("#content_holder").on("click", "#train_cancel_btn", function() {
		alert("CANCEEEEL!");
		
		$("#content_holder").load("Home.jsp");
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