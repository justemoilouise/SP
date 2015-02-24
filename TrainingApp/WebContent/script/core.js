$(function() {
	var fxnCallback = function(message) {
		$("#alert_holder").load("Prompt.jsp")
		$("#alert_holder").css("visibility: visible; navbar-fixed-top");
		$("#alert_message").text(message);
	}
	
	$("#content_holder").on("load","#dload_app", function() {
		$.ajax({
			url: 'trainingapp/getapplist',
			dataType: "json",
			success: function(response) { return response; },
			error: function() { return null; },
		});
	});
	
	$("#content_holder").on("load","#dload_model", function() {
		$.ajax({
			url: 'trainingapp/getmodellist',
			dataType: "json",
			success: function(response) { return response; },
			error: function() { return null; },
		});
	});
});