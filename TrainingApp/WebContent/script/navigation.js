$(function() {
	$("#nav_logo").on("click", function() {
		$("#content_holder").load("Home.jsp");
	});
	
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
	
	$("#nav_svm").on("click", function() {
		$("#content_holder").load("SVM_Training_Input.jsp");
	});
});