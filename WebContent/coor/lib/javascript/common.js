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

function processText(coordinateID,nodeID){
	isProcessText = true;
	$("#frameProcess").load("../coor/processText?coordinateID="+coordinateID+"&nodeID="+nodeID);
	$("#selectionsArrows").css({"padding-left":"48px"});
}
function processPath(coordinateID, nodeID){
	isProcessText = false;
	$("#frameProcess").load("../coor/processPath?coordinateID="+coordinateID+"&nodeID="+nodeID);
	$("#selectionsArrows").css({"padding-left":"160px"});
}
function processComment(coordinateID,nodeID){
	isProcessText = false;
	$("#frameProcess").load("../coor/processComment?coordinateID="+coordinateID+"&nodeID="+nodeID);
	$("#selectionsArrows").css({"padding-left":"281px"});
}

/****
 set process url
***/
function setProcessUrl(){
	switch(this.num)
	{
	case 0:
		processText(coordinateID, nodeID);
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