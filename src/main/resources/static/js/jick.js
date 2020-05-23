$(function(){
	var path=window.location.pathname;
	console.log("#current path:",path);
	if (path.indexOf("/jnode")==0){
		$("#menu-spark").find(".menu").removeClass("active");
		$("#menu-jnode").addClass("active");
	}
	if (path.indexOf("/model")==0){
		$("#menu-spark").find(".menu").removeClass("active");
		$("#menu-model").addClass("active");
	}
	if (path.indexOf("/project")==0){
		$("#menu-spark").find(".menu").removeClass("active");
		$("#menu-project").addClass("active");
	}

});  