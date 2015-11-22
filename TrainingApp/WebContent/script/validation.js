var isValidNum = function(num) {
	return $.isNumeric(num);
}

var hasValue = function(val) {
	return val.length;
}

var validateObj = function(data) {
	for (var prop in data) {
	    if(!isValidNum(data[prop]) || !hasValue(data[prop])) {
	    	return false;
	    }
	}
	
	return true;
}

var validate = function(data) {
	if(!isValidNum(data) || !hasValue(data)) {
    	return false;
    }
	
	return true;
}