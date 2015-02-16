$(function() {
	$("#login_cancel_btn").on("click", function() {
		alert("test alert");
	});
	
	$("#login_btn").click(function() {
		var username = $("#login_username").val();
		var password = $("#login_password").val();
		
		$.ajax({
			url: 'trainingapp/user/login?username=' + username + '&password=' + password,
			method: 'GET'
			success: function(response) {
				if(response)
					alert("Successfully logged in.");
				else
					alert("An error occurred. Please try again.");
			},
			error: function() { alert("An error occurred. Please try again."); },
			dataType: 'json'
		});
	});
	
	$("#logout_btn").click(function() {
		$.ajax({
			url: 'trainingapp/user/logout',
			success: function() { alert("Successfully logged out."); },
			error: function() { alert("An error occurred. Please try again."); },
		});
	});
});