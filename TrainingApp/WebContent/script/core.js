var modelList = {};
var appList = {};

$(function() {
	var fxnCallback = function(message) {
		$("#alert_holder").load("Prompt.jsp")
		$("#alert_holder").css("visibility: visible; navbar-fixed-top");
		$("#alert_message").text(message);
	}
	
	$("#content_holder").on("click","#dload_btn", function() {
		var key = "key";
		
		$.ajax({
			url: 'trainingapp/download?modelKey=' + key,
			success: function() {
				alert("OKS!");
			},
			error: function() { alert("NAH!"); },
		});
	});
	
	$("#content_holder").on("load","#dload_app", function() {
		$.ajax({
			url: 'trainingapp/getapplist',
			dataType: 'json',
			success: function(response) {
				appList = response;
			},
			error: function() {
				alert("ERROR: Get app list");
			}
		});
	});
});