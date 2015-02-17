<br />
<div class="input-group col-md-offset-1 col-md-10" id="input_file">
	<input type="file" width="50" class="col-md-10" />
	<button type="submit" class="btn btn-primary col-md-2">
		<span class="glyphicon glyphicon-check"></span>
	</button>
</div>
<br />
<div class="panel panel-default col-md-offset-1 col-md-10">
	<div class="panel-heading">Features to use</div>
	<div class="panel-body">
		&emsp;<input type="radio" value="IJ" name="train_feature" />&emsp;<label>Basic texture and shape features</label><br />
		&emsp;<input type="radio" value="JF" name="train_feature" />&emsp;<label>Haralick texture descriptors</label>
	</div>
</div>
<br />
<div class="panel panel-default col-md-offset-1 col-md-10">
	<div class="panel-heading">SVM Parameters</div>
	<div class="panel-body">
		<table class="table">
			<tr>
				<th>Parameter</th>
				<th>Value</th>
			</tr>
			<tr>
				<td>Cost</td>
				<td>1</td>
			</tr>
			<tr>
				<td>Gamma</td>
				<td>2</td>
			</tr>
		</table>
	</div>
</div>
<br />
<div class="btn-group col-md-offset-9 col-md-3" id="train_form">
	<button class="btn btn-primary" id="train_build_btn">Build model</button>
	<button class="btn btn-default" id="train_cancel_btn">Cancel</button>
</div>
<br />