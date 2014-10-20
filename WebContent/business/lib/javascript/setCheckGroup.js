function initCheckboxGroup(){
	var o = $("#checkboxList");
	var data = o.attr("group-ids");
	var my = new Array();
	my = data.split(",");
	$.each(my, function(index, value){
		if(value != null && value != ""){
			o.children().each(function(){
				if($(this).attr("id") == value){
					$(this).find("input:checkbox").attr("checked", true);
					return;
				}
			});
		}
	});
}
function setCheckboxGroup(){
	var o = $("#checkboxList");
	var data = o.attr("group-ids");
	var temp = new Array();
	if(data != "" && data != null)
		temp = data.split(",");

	o.children().each(function(){
		$(this).bind("click", function(){
			var id = $(this).attr("id");
			var hasId = false;
			if($(this).find("input:checkbox")[0].checked == true){
				$.each(temp, function(index, value){
					if(id == value){
						hasId = true;
						return;
					}
				});
				if(!hasId){
					temp.push(id);
				}
				
			}else{
				$.each(temp, function(index, value){
					if(id == value){
						temp.splice(index,1);
						return;
					}
				});
				
			};
			
			var newDatal ="";
			var len = temp.length;
			$.each(temp, function(index, value){
				if(len-1 == index){
					newDatal += value;
				}else{
					newDatal += value+",";
				}
			});
			 o.attr("group-ids", newDatal);
		});
	});
	
}

initCheckboxGroup();
setCheckboxGroup();
