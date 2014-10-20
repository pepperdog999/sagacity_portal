// JavaScript Document
function  setStyleChange(obj, style, callBack){
		if($(obj).length <= 1 ) return;
		var passValue = {
			num:null
		};
		$(obj).each(function(index){
			$(this).bind("click",function(){
				$(this).siblings().removeClass(style);
				$(this).addClass(style);
				passValue.num = index;
				if(typeof(callBack) == "function"){
					callBack.call(passValue);
				}
			});	
		});
}

//index
function processingNoticeList(){
	$("#memberList").load("works/processing.html");
}
function createNoticeList(){
	$("#memberList").load("works/create.html");
}
/****
 set works url
***/
function setWorksUrl(){
	switch(this.num)
	{
	case 0:
		processingNoticeList();
		break;
	case 1:
		createNoticeList();
		break;
	default:
		alert("链接出错啦！");
	}
}

//弹出层 选择人
var dialogUser;
function DialogUserChoice(){
	dialogUser = new SfDialog();
	dialogUser.init("选人", "filter/choice/departusers.html");
	dialogUser.beforeHideEvent(DialogBackFun,chooseUserConfirm,true);
	dialogUser.render();
	dialogUser.show(600, 500, true, false);
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
				case "user":
					DialogUserChoice();
					break;
				default:
					alert("选择出错啦！");
				}
		});	
	});
}

function DialogBackFun(){
	console.log("关闭按钮的回调方法");
}