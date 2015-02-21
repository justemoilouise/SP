$(function() {
	var callback = function(message) {
		$("#user_alert_holder").load("Prompt.jsp")
		$("#user_alert_holder").css("visibility: visible; navbar-fixed-top");
		$("#alert_message").text(message);
	}
	
	$("#login_form").on("click", "#login_btn",function() {
		var username = $("#login_username").val();
		var password = $("#login_password").val();
		
		$.ajax({
			url: 'trainingapp/user/login?username=' + username + '&password=' + password,
			success: function(response) {
				if(response.indexOf("true") >= 0) {
					callback("Log in successful.");
					window.location = "/";
					
				}  else {
					callback("Invalid username and/or password.");
				}
			},
			error: function() {
				callback("An error has occurred.");
			}
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
		window.location = "/";
	});
});