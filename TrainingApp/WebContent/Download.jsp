<script type="text/javascript">
$(function() {
	var model = {};
	
	$.ajax({
		url: 'trainingapp/getmodellist,
		success: function(response) {
			model = JSON.parse(response);
		}
	});
}
</script>
<br />
<div class="panel panel-default col-md-offset-1 col-md-10" id="dload_model">
	<div class="panel-heading">
		<h3 class="panel-title">Classifier model</h3>
	</div>
	<div class="panel-body">
		<table class="table" id="model_table">
			<tr>
				<th>Date</th>
				<th>Version number</th>
				<th>Release notes</th>
			</tr>
			<tr>
				<th><%= new java.util.Date() %></th>
				<th>1.0</th>
				<th>Beta</th>
			</tr>
		</table>
	</div>
</div>
<script type="text/javascript">
	for(int i=0; i<model.length; i++) {
		$("#model_table tr:last")
		.after(
			"<tr id=" + model[i] + ">" +
				"<td>" + model[i].createdDate + "</td>" +
				"<td>" + model[i].version + "</td>" +
				"<td>" + model[i].notes + "</td>" +
				"<a href=#><span class=\"glyphicon glyphicon-download-alt\" id=\"dload_btn\"></span></a>"
			"</tr>"		
		);
	}
</script>
<br />