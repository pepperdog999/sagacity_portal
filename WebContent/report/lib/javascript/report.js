// JavaScript Document
$(function(){
	
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

function setReportListLink(){
	
	var list = $("#reportList .rep-list");
	list.each(function(){
		$(this).hover(function(){
			$(this).css({"background-color":"#f7f7f4"});
		},function(){
			$(this).css({"background-color":"#fbfbfb"});
		});	
	});
	
	$(".openDeal").each(function(){
		$(this).click(function(){
			var dataID = $(this).attr("data-id");
			var HtmlElement = createDivElement("../report/reportDetail?reportID="+dataID);
			var Myfaceplate = new faceplate({
					width:"700",					
					height:"630",
					top:"80"
				});
			Myfaceplate.render(HtmlElement);
			return false;
		});	
	})
	
}

function createDivElement(pageSrc){
	var div = document.createElement("div");
	$(div).load(pageSrc);
	return div;
}