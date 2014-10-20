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
			Dialog object bind clicking function
			get all of Dialog object
		*
		*	
		*/
		jugDialogPage();
		
});

var upImgDialog;
function upHandlerImg(){
	upImgDialog = new SfDialog();
	upImgDialog.init("上传个人头像", "filter/choice/upUserImg.html");
	upImgDialog.beforeHideEvent(null, null, true);
	upImgDialog.render();
	upImgDialog.show(780, 480, true, false);
}



function jugDialogPage(){
	$(".bindDialog").each(function(){
		$(this).unbind("click");
		$(this).bind("click", function(){
				if($(this).hasClass("disable")){
					return false;
				}
				var type = $(this).attr("data-type");
				switch(type)
				{
				case "upImg":
					upHandlerImg();
					break;
				default:
					alert("选择出错啦！");
				}
		});	
	});
}