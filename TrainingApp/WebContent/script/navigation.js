$(function() {
	$("#nav_home").on("click", function() {
		$("#content_holder").load("Home.jsp");
	});
	
	$("#nav_download").on("click", function() {
		$("#content_holder").load("Download.jsp");
	});
	
	$("#nav_documentation").on("click", function() {
		$("#content_holder").load("Documentation.jsp");
	});
	
	$("#nav_about").on("click", function() {
		$("#content_holder").load("About.jsp");
	});
	
	$("#nav_training").on("click", function() {
		$("#content_holder").load("Training_Input.jsp");
	});
});