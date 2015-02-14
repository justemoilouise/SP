$(function() {
	$("#nav_download").click(function() {
		$("#content_holder").load("download.jsp");
	});
})

$(function() {
	$("#nav_documentation").click(function() {
		$("#content_holder").load("documentation.jsp");
	});
})

$(function() {
	$("#nav_about").click(function() {
		$("#content_holder").load("about.jsp");
	});
})

$(function() {
	$("#nav_training").click(function() {
		$("#content_holder").load("training_input.jsp");
	});
})

$(function() {
	$("#build_model").click(function() {
		$("#content_holder").load("training_output.jsp");
	});
})