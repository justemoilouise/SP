<%
	if(session.getAttribute("alert_type") != null) {
		String alertType = session.getAttribute("alert_type").toString();
		
		if(alertType.equalsIgnoreCase("success")) {
%>
<div class="alert alert-dismissible alert-success col-md-offset-3 col-md-6" role="alert">
<%
		} else if(alertType.equalsIgnoreCase("warning")) {
%>
<div class="alert alert-dismissible alert-warning col-md-offset-3 col-md-6" role="alert">
<%
		} else {
%>
<div class="alert alert-dismissible alert-info col-md-offset-3 col-md-6" role="alert">
<%
		}
	}
%>
	<button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	<p id="alert_message"></p>
</div>