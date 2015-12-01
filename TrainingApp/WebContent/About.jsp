<br />
<div class="col-md-offset-1 col-md-10 panel-body">
	<br /> &emsp;&emsp; Classifying radiolarians is tedious. Requiring a high level of expertise, vast number of 
	references and such eyes for details. Up until now, researchers still go through a lot of work in naming and 
	studying these species. 
	<br />
	<br /> &emsp;&emsp; RadiSS is a decision support system designed to help researchers in classifying Radiolarian 
	species given an image. It aims to minimize the effort in coming up with the possible name of the target species. 
	As a consequence, further studies on the structure and way of living of these species can be given more attention. 
	Even the dating process of the sediment on which the species was found could be done easily.
	<br /><br /><br /><br />
</div>
<div class="col-md-offset-1 col-md-10">
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3><strong><font color="white"> HOW IT WORKS: </font></strong></h3>
		</div>
		<div class="panel-body">
			<p>Training a classifier can be done in only 3 simple steps:</p>
			<ol>
				<li><h4>Upload training set</h4></li>
					<ul>
						<li>Training set should be in Excel format</li>
						<li>Each file should contain species and its corresponding set of features.</li>
						<li>One training set must be uploaded at a time.</li>
					</ul>
				<li><h4>Set training parameters</h4></li>
					<ul>
						<li>Number of PCA</li>
							<ul>
								<li>Should be any integer greater than 0</li>
								<li>Determines how many components the features will be reduced into</li>
							</ul>
						<li>SVM Parameters</li>
							<ul>
								<li>SVM Type</li>
									<ol>
										<li>C-SVC - allows the SVM algorithm to clearly separate samples that are separated by a very narrow margin</li>
										<li>Nu-SVC - the nu parameter controls training errors and the number of support vectors</li>
										<li>One-class SVM - the SVM algorithm considers the spatial distribution information for each sample to determine whether the sample belongs to the known class</li>
									</ol>
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
					</ul>
				<li><h4>Build model!</h4></li>
			</ol>
			<br />
		</div>
	</div>
	<div class="panel panel-primary">
		<div class="panel-heading">
			<h3><strong><font color="white"> WHAT ARE THE RESULTS: </font></strong></h3>
		</div>
		<div class="panel-body">
			<p>The following are the result of the training:</p>
			<ol>
				<li>Scaling factors - minimum and maximum values used to normalize each feature</li>
				<li>Principal components - features that represent the whole set to minimize the cost of processing and building the classifier model</li>
				<li>SVM Model - generated model that will be used to classify unknown Radiolarian species</li>
			</ol>
			<br />
		</div>
	</div>
</div>
<br /><br /><br /><br />
<div class="panel panel-default col-md-offset-1 col-md-10">
	<br /> &emsp;&emsp;For comments and suggestions, please email: &emsp;<b>louiseann.apostol@gmail.com</b>
	<br />
	<br />
</div>
<br />