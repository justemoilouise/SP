var model = {};
var modelList = {};
var alertType = "info";
var alertMessage = "";

var fxnCallback = function(message) {
	alertMessage = message;
	$("#alert_holder").load("Prompt.jsp");
}

var toggleProgress = function() {
	$("#progressModal").modal("toggle");
}

$(function() {
	$("#content_holder").load("Home.jsp");
})