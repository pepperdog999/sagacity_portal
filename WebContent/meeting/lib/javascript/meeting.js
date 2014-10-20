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
function DialogUserChoice(args){
	dialogUser = new SfDialog();
	dialogUser.init("选人", "filter/choice/departusers.html");
	dialogUser.initClickObject(args);
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
					DialogUserChoice(this);
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

var setCellContent = function( date, content, Type){
	
	$("#dateTable .meet-tab-x").each(function(){
		
		//表格内容日期格式化
		var time = $(this).attr("data-thisdate");
		var ThisMonth = time.substring(5,time.lastIndexOf ('-'));  
		var ThisDay = time.substring(time.length,time.lastIndexOf ('-')+1);  
		var ThisYear = time.substring(0,time.indexOf ('-')); 
		
		//输入日期格式化
		var newMonth = date.substring(5,date.lastIndexOf ('-'));  
		var newDay = date.substring(date.length,date.lastIndexOf ('-')+1);  
		var newYear = date.substring(0,date.indexOf ('-')); 
		
		if(Number(newYear) == Number(ThisYear) && Number(newMonth) == Number(ThisMonth) && Number(newDay) == Number(ThisDay)){
			
			
			var classType = "";
			//判断会议记录类型
			switch(Type)
			{
				case 0 :
					classType = "";
					break;
				case 1 :
					classType = "important";
					break;
				case 2 :
					classType = "urgency";
					break;
				default:
					classType = "";
			}
			
			//插入内容
			$(this).find(".meet-tab-con").addClass(classType).html(content);
			
		}		
		
	});
	
}


function setMeetingListLink(){
	
	$(".openDeal").each(function(){
		$(this).click(function(){
			var dataID = $(this).attr("data-id");
			var HtmlElement = createDivElement("../meeting/meetingDetail?meetingID="+dataID);
			var Myfaceplate = new faceplate({
					width:"700",					
					height:"777",
					top:"118"
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