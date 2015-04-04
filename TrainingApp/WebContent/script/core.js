var modelList = {};
var alertType = "info";
var alertMessage = "";

var fxnCallback = function(message) {
	alertMessage = message;
	$("#alert_holder").load("Prompt.jsp");
}

$(function() {	
	$("#content_holder").on("click", "span", function() {
		var key = this.id;
		
		$.ajax({
			url: 'trainingapp/download?modelKey=' + key,
			success: function() {
				alertType = "success";
				fxnCallback("Successfully downloading model.");
			},
			error: function() {
				alertType = "error";
				fxnCallback("An error occurred while retrieving the model. Please try again.");
			},
		});
	});
});