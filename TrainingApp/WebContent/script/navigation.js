$(function() {
	$("#nav_download").click(function() {
		$("#content_holder").load("Download.jsp");
	});
})

$(function() {
	$("#nav_documentation").click(function() {
		$("#content_holder").load("Documentation.jsp");
	});
})

$(function() {
	$("#nav_about").click(function() {
		$("#content_holder").load("About.jsp");
	});
})

$(function() {
	$("#nav_training").click(function() {
		$("#content_holder").load("Training_Input.jsp");
	});
})

$(function() {
	$("#build_model").click(function() {
		$("#content_holder").load("Training_Output.jsp");
	});
})