// JavaScript Document
$(function(){
		
		/*
		*
		*
			close menu function
			open menu functuon
		*
		*	
		*/
		var menu = $("#memuFrame");
		
		$("#setMenuFrame").bind("click",function(){
				if(menu.is(":hidden")){
					menu.show();
					$(this).addClass("close").attr("title","关闭菜单");
				}else{
					menu.hide();
					$(this).removeClass("close").attr("title","打开菜单");
				}
		});
		
		
		/*
		*
		*
			Tab Control set style function
			return back functuon for call-back
		*
		*	
		*/
		setStyleChange($("#TabMenu a"), "ckeck", setWorksUrl);
	
		
});