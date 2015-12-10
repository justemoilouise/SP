$(function() {
	var callback = function(message) {
		alertMessage = message;
		$("#user_alert_holder").load("Prompt.jsp");
	}
	
	$("#login_form").on("click", "#login_btn",function() {
		var login = {};
		login.username = $("#login_username").val();
		login.password = $("#login_password").val();
		
		$.ajax({
			url: 'trainingapp/user/login',
			data: JSON.stringify(login),
			method: "POST",
			success: function(response) {
				if(response.indexOf("true") >= 0) {
					isLoggedIn = true;
					alertType = "success";
					callback("Log in successful.");
					$("#loginModal").modal("toggle");
					toggleLogout();
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
				isLoggedIn = false;
				alertType = "success";
				callback("You have successfully logged out of the system.");
				toggleLogout();
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