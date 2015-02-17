$(function() {
	$("#login_form").on("click", "#login_btn",function() {
		var username = $("#login_username").val();
		var password = $("#login_password").val();
		
		$.ajax({
			url: 'trainingapp/user/login?username=' + username + '&password=' + password,
			method: 'GET',
			success: function() { alert("Log in successful.") },
			error: function() { alert("An error occurred. Please try again."); },
			dataType: 'json'
		});
	});
	
	$("#login_form").on("click", "#logout_btn",function() {
		$.ajax({
			url: 'trainingapp/user/logout',
			success: function() { alert("Successfully logged out."); },
			error: function() { alert("An error occurred. Please try again."); },
		});
	});
	
	$("#login_form").on("click", "#login_cancel_btn",function() {
		$(this).load("Index.jsp");
	});
});