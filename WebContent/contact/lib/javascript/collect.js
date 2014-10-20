// JavaScript Document

function collectHanderEvent(){
	
	$("#collectList").children().each(function(){
			
			//添加滑过样式事件
			$(this).hover(function(){
				if(!$(this).hasClass("check")){
					$(this).find("a.col-to").show();
				}else{
					$(this).find("a.col-to").hide();
				}
					
			}, function(){
				$(this).find("a.col-to").hide();
			});
			
			
			//绑定选中事件
			$(this).unbind("click");
			$(this).bind("click", function(){
				if(!$(this).hasClass("add")){
					if($(this).hasClass("check")){
						$(this).removeClass("check");
						selContactGroupID=0;
					}else{
						$(this).siblings().removeClass("check");
						$(this).addClass("check");
						$(this).find("a.col-to").hide();
						selContactGroupID=$(this).attr("data-id");
					}
				}
				
			});
			
			
	});
	
}