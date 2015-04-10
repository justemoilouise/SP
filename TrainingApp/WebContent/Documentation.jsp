<br />
<div class="panel panel-default col-md-offset-1 col-md-10">
	<div class="panel-heading">
		<h3 class="panel-title">Training - input</h3>
	</div>
	<div class="panel-body">
		<ol>
			<li>File upload</li>
				<ul>
					<li>Only Excel files are accepted as input.</li>
					<li>Each file should contain species and its corresponding set of features.</li>
					<li>One training set must be uploaded at a time.</li>
				</ul>
			<li>SVM Parameters</li>
				<ul>
					<li>SVM Type</li>
					<li>Kernel function - used in mapping the training vectors to a high dimensional space</li>
					<li>Kernel function parameters: </li>
						<ol>
							<li>Cost</li>
							<li>Gamma</li>
							<li>Epsilon</li>
							<li>Degree</li>
							<li>Nu</li>
							<li>Coefficient</li>
						</ol>
				</ul>
		</ol>
	</div>
</div>
<br />
<div class="panel panel-default col-md-offset-1 col-md-10">
	<div class="panel-heading">
		<h3 class="panel-title">Training - output</h3>
	</div>
	<div class="panel-body">
		<ol>
			<li>Scaling factors - minimum and maximum values used to normalize each feature</li>
			<li>Principal components - features that represent the whole set to minimize the cost of processing and building the classifier model</li>
			<li>SVM Model - generated model that will be used to classify unknown Radiolarian species</li>
		</ol>
	</div>
</div>
<br />