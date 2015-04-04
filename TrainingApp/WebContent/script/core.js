var modelList = {};
var appList = {};

var fxnCallback = function(message) {
	$("#alert_holder").load("Prompt.jsp")
	$("#alert_holder").css("visibility: visible; navbar-fixed-top");
	$("#alert_message").text(message);
}

$(function() {	
	$("#content_holder").on("click", "span", function() {
		var key = this.title;
		
		$.ajax({
			url: 'trainingapp/download?modelKey=' + key,
			success: function() {
				alert("OKS!");
			},
			error: function() { alert("NAH!"); },
		});
	});
});