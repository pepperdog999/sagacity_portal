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
function processingWorkList(){
	$("#memberList").load("works/processing.html");
}
function processedWorkList(){
	$("#memberList").load("works/processed.html");
}
function createdWorkList(){
	$("#memberList").load("works/created.html");
}

/****
 set works url
***/
function setWorksUrl(){
	switch(this.num)
	{
	case 0:
		processingWorkList();
		break;
	case 1:
		processedWorkList();
		break;
	case 2:
		createdWorkList();
		break;
	default:
		alert("链接出错啦！");
	}
}

//addprocess
var isProcessText = true;
function processText(dataID,processCode){
	isProcessText = true;
	$("#frameProcess").load("../business/processText?projectID="+dataID+"&processCode="+processCode);
	$("#selectionsArrows").css({"padding-left":"48px"});
}
function processPath(){
	isProcessText = false;
	$("#frameProcess").load("../business/processPath?coordinateID="+coordinateID+"&nodeID="+nodeID);
	$("#selectionsArrows").css({"padding-left":"160px"});
}
function processComment(){
	isProcessText = false;
	$("#frameProcess").load("../business/processComment?coordinateID="+coordinateID+"&nodeID="+nodeID);
	$("#selectionsArrows").css({"padding-left":"281px"});
}

//临时增加一个用于演示的页面
function processText2(){
	isProcessText = true;
	$("#frameProcess").load("process/processText2.html");
	$("#selectionsArrows").css({"padding-left":"48px"});
}

/****
 set process url
***/
function setProcessUrl(){
	switch(this.num)
	{
	case 0:
		processText(projectID,processCode);
		break;
	case 1:
		processPath(coordinateID, nodeID);
		break;
	case 2:
		processComment(coordinateID, nodeID);
		break;
	default:
		alert("链接出错啦！");
	}
}


var processCode = "";
function getProcessListClickIndex(){
	$("#orientedFrame").children().each(function(){
		var thisID = $(this).attr("data-id");
		if(thisID == processCode){
			$(this).click();
			processCode = "";
			return false;
		}
	});
}