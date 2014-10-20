// JavaScript Document
$(function(){
	setLoadOptions();
	// JavaScript Document
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
		
});

function loadUserApp(){
	$("#appContent").load("../application/userApp");
}

function loadPlatformApp(){
	$("#appContent").load("../application/platformApp");
}

function setLoadOptions(){
	$("#appUl").children().each(function(){
		var src = $(this).attr("data-src");
		$(this).bind("click",function(){
			$(this).siblings().removeClass("check");
			$(this).addClass("check");
			switch(src)
			{
			case "user":
				loadUserApp();
				break;
			case "platform":
				loadPlatformApp();
				break;
			default:
				loadUserApp();
			}	
		});	
	});
}