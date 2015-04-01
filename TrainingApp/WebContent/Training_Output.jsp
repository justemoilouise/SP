<script type="text/javascript">
$(function() {
	var pModel = model.preprocessModel;
	
	//Scaling factors
	for(var i=0; i<pModel.min.length; i++) {
		var tRow = '<tr>';
		tRow += '<td>' + (i+1) + '</td>';
		tRow += '<td>' + pModel.min[i] + '</td>';
		tRow += '<td>' + pModel.max[i] + '</td>';
		tRow += '</tr>';
		$('#tbl_scale').append(tRow);
	}
	
	// Principal components
	$('#tbl_princomp_val').attr('colspan', pModel.PC);
	for(var i=0; i<pModel.principalComponents[0].length; i++) {
		var tRow = '<tr>';
		tRow += '<td>' + (i+1) + '</td>';
		for(var j=0; j<pModel.principalComponents.length; j++) {
			tRow += ('<td>' + pModel.principalComponents[i][j] + '</td>');
		}
		tRow += '</tr>';
		$('#tbl_princomp').append(tRow);
	}
})
</script>
<br />
<div class="panel panel-default col-md-offset-1 col-md-10">
	<div class="panel-heading">
		<h3 class="panel-title">Scaling factors</h3>
	</div>
	<div class="panel-body">
		<table class="table" id="tbl_scale">
			<tr>
				<th>Feature</th>
				<th>Minimum value</th>
				<th>Maximum value</th>
			</tr>
		</table>
	</div>
</div>
<br />
<div class="panel panel-default col-md-offset-1 col-md-10">
	<div class="panel-heading">
		<h3 class="panel-title">Principal components</h3>
	</div>
	<div class="panel-body">
		<table class="table" id="tbl_princomp">
			<tr>
				<th>Principal component</th>
				<th id="tbl_princomp_val">Values</th>
			</tr>
		</table>
	</div>
</div>
<br />
<div class="panel panel-default col-md-offset-1 col-md-10">
	<div class="panel-heading">
		<h3 class="panel-title">SVM Model</h3>
	</div>
	<div class="panel-body">
		<table class="table" id="tbl_svm">
			<tr>
				<th>Classes</th>
				<td>A, B, C</td>
			</tr>
			<tr>
				<th>Accuracy (%)</th>
				<td>1.0</td>
			</tr>
		</table>
	</div>
</div>
<br />
<div class="btn-group col-md-offset-8 col-md-4">
	<button class="btn btn-primary" id="train_save_btn">Save model</button>
	<button class="btn btn-default" id="train_rebuild_btn">Rebuild
		model</button>
</div>
<br />