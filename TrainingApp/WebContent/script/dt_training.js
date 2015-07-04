$(function() {	
//	$("#input_files").on("click", "#input_files_btn", function(){
//		var files = $("#input_files_fld").get(0);
//		var fileCount = 0;
//		
//		for(var file in files) {
//			var data = new FormData();
//			data.append("imageset", file);
//			
//			$.ajax({
//				url: '/trainingapp/decisiontree/upload',
//				method: "POST",
//				contentType: false,
//				processData: false,
//				data: data,
//				dataType: "json",
//				async: false,
//				success: function(response) {
//					if(response==="true")
//						fileCount++;
//				},
//				complete: function() {
//					alertType = "success";
//					fxnCallback(fileCount + " files uploaded successfully.");
//					$('html, body').animate({scrollTop: 0}, 'fast');
//				}
//			});
//		}
//	});
});