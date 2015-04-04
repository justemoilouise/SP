<script type="text/javascript">
$(function() {
	var classAttr = $("#prompt_alert").attr("class");

	if(alertType == "success") {
		classAttr += " alert-success";
	} else if(alertType == "warning") {
		classAttr += " alert-warning";
	} else if(alertType == "error") {
		classAttr += " alert-danger";
	} else {
		classAttr += " alert-info";
	}
	
	$("#prompt_alert").attr("class", classAttr);
	$("#prompt_alert_message").html(alertMessage);
});
</script>
<style type="text/css">
#prompt_alert {
	position: absolute;
	z-index: 5;
}
</style>
<div class="alert alert-dismissible col-md-offset-3 col-md-6" id="prompt_alert" role="alert">
	<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	<p id="prompt_alert_message"></p>
</div>