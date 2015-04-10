var alertType = "info";
var alertMessage = "YAY!";

$(function() {
	var callback = function(message) {
		alertMessage = message;
		$("#user_alert_holder").load("Prompt.jsp");
	}
	
	$("#login_form").on("click", "#login_btn",function() {
		var username = $("#login_username").val();
		var password = $("#login_password").val();
		
		$.ajax({
			url: 'trainingapp/user/login?username=' + username + '&password=' + password,
			success: function(response) {
				if(response.indexOf("true") >= 0) {
					alertType = "success";
					callback("Log in successful.");
					window.location = "/";
					
				} else {
					alertType = "warning";
					callback("Invalid username and/or password.");
				}
			},
			error: function() {
				alertType = "error";
				callback("An error has occurred.");
			}
		});
	});
	
	$("#logout_btn").on("click", function() {
		$.ajax({
			url: 'trainingapp/user/logout',
			success: function() {
				alertType = "success";
				callback("You have successfully logged out of the system.");
				window.location = "/";
			},
			error: function() {
				alertType = "error";
				callback("An error has occurred.");
			}
		});
	});
	
	$("#login_form").on("click", "#login_cancel_btn",function() {
		window.location = "/";
	});
});