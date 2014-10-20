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
		Dialog object bind clicking function
		get all of Dialog object
	*
	*	
	*/
	//jugDialogPage();
});



var dialogSendMes;
function DialogSendMes(obj){
	dialogSendMes = new SfDialog();
	dialogSendMes.init("发送消息", "filter/choice/sendMes.html?type="+$(obj).attr("Type")+"&id="+$(obj).attr("ID"));
	dialogSendMes.beforeHideEvent(DialogBackFun, DialogSucceedBack, true);
	dialogSendMes.render();
	dialogSendMes.show(600, 320, false, false);
}


var dialogFile;
function DialogSendFile(obj){
	dialogFile = new SfDialog();
	dialogFile.init("发送文件", "filter/choice/sendFile.html?type="+$(obj).attr("Type")+"&id="+$(obj).attr("ID"));
	dialogFile.beforeHideEvent(DialogBackFun, DialogSucceedBack, true);
	dialogFile.render();
	dialogFile.show(600, 320, false, false);
}

var dialogCollect;
function DialogCollectAction(obj){
	dialogCollect = new SfDialog();
	dialogCollect.init("添加到用户自定义组", "filter/choice/collect.html?id="+$(obj).attr("ID"));
	dialogCollect.beforeHideEvent(DialogBackFun, DialogSucceedBack, true);
	dialogCollect.render();
	dialogCollect.show(650, 390, false, false);
}

/*
*
*
	Dialog object bind clicking close function
*
*	
*/
function DialogBackFun(){
	console.log("关闭回调方法");
}

function DialogSucceedBack(){
	console.log("成功回调方法");
	document.getElementById("dialogIframe").contentWindow.aa();
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
				case "mes":
					DialogSendMes(this);
					break;
				case "file":
					DialogSendFile(this);
					break;
				case "collect":
					DialogCollectAction(this);
					break;	
				default:
					alert("选择出错啦！");
				}
		});	
	});
}