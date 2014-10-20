// JavaScript Document
$(function(){
	
	/*
	*
	*
		Dialog object bind clicking function
		get all of Dialog object
	*
	*	
	*/
	jugDialogPage();
		
	/*
	*
	*
		the links 
	*
	*	
	*/
	$(".linksToProcess").each(function(){
		$(this).bind("click",function(){
			if(macthID != "") return;
			macthID = $(this).attr("data-toid");
			$("#selections").children().eq(0).click(); //主菜单点击事件触发
			macthID = "";
		});	
	});
});

