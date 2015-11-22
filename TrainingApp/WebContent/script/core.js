var model = {};
var modelList = {};
var alertType = "info";
var alertMessage = "";

var fxnCallback = function(message) {
	alertMessage = message;
	$("#alert_holder").load("Prompt.jsp");
}

$(function() {
	$("#content_holder").load("Home.jsp");

	$("#content_holder").on("click", "[id^=collapse]", function() {
		var elementId = "#" + $(this).attr("id");
		var startIndex = elementId.indexOf("model");
		var id = "#" + elementId.substring(startIndex, elementId.length);
		var isExpand = $(elementId).hasClass("glyphicon-chevron-right");
		
		if(isExpand) {
			$(elementId).removeClass("glyphicon-chevron-right");
			$(elementId).addClass("glyphicon-chevron-down");
			$(id).show();
		}
		else {
			$(elementId).removeClass("glyphicon-chevron-down");
			$(elementId).addClass("glyphicon-chevron-right");
			$(id).hide();
		}
	});
})