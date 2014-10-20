<!--
	var zTree;
	var demoIframe;

	var setting = {
		view: {
			dblClickExpand: false,
			showLine: true,
			selectedMulti: false
		},
		data: {
			simpleData: {
				enable:true,
				idKey: "id",
				pIdKey: "pId",
				rootPId: ""
			}
		},
		callback: {
			beforeClick: function(treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("tree");
				if (treeNode.isParent) {
					zTree.expandNode(treeNode);
					getDeptUser(treeNode.id, treeNode.name);
					return false;
				} else {
					//回调执行方法
					getDeptUser(treeNode.id, treeNode.name);
					return true;
				}
			}
		}
	};
	
function getDepartList(dataJson){
	var fiterUl = $("#departFiterUL");
	var listUl = $("#departUL");

	var html = "";
	for(i in dataJson){
		 var temp = jugListHasObject(dataJson[i].id, fiterUl.children());
		 if(!temp){
			 html += "<li id="+dataJson[i].ID+" data-name='"+dataJson[i].Name+"'><a><i class=\"checkbox-ico\"></i>"+dataJson[i].Name+"</a></li>";
		 }else{
			
			 html += "<li id="+dataJson[i].ID+" data-name='"+dataJson[i].Name+"' class=\"check\"><a><i class=\"checkbox-ico\"></i>"+dataJson[i].Name+"</a></li>";
		 }
	}
	listUl.html(html);
	
	listUl.children().each(function(){
		$(this).bind("click",function(){
			if($(this).hasClass("check")){
				$(this).removeClass("check");	
			}else{
				$(this).addClass("check");
			}
		});	
	});
	
	//这里执行  向右移动  向左移动  上下移动
	setMoveListOperation();
}


function jugListHasObject(id, objectList){
	if($(objectList).length <= 0) return false;
	var temp = false;
	$(objectList).each(function(){
		if($(this).attr("data-id") == id){
			temp = true;
			return false;
		}else{
			temp = false;
		}
	});
	return temp;
}

function handlerBindClick(obj, style){
	$(obj).children().unbind("click");
	$(obj).children().each(function(){
		$(this).bind("click",function(){
			if($(this).hasClass(style)){
				$(this).removeClass(style);	
			}else{
				$(this).addClass(style);
			}
		});	
	});
}
  //-->
  
function setMoveListOperation(){
	//操作对象
	var toRight = $("#toRight"),toLeft = $("#toLeft"),toUp = $("#toUp"),toDown = $("#toDown");
	//列表对象
	var fiterUl = $("#departFiterUL");
	var listUl = $("#departUL");
	<!-- 向右移动数据 //-->
	toRight.bind("click", function() {		
		if(listUl.children().length <= 0) return true;
		listUl.children().each(function(){
			if($(this).hasClass("check")){
				var id = $(this).attr("id");
				var name = $(this).attr("data-name");
				
				var temp = jugListHasObject(id, fiterUl.children());
				if(!temp){
					var o = "<li data-id="+id+" data-name="+name+"><a><i class=\"checkbox-ico\"></i>"+name+"</a></li>";
					fiterUl.append(o);
				}
			}
		});
		
		//右边列表添加 选中 或者 取消选中 事件
		handlerBindClick(fiterUl, "check");
	});
	
	<!-- 向左移动数据//-->
	toLeft.bind("click", function() {
		if(fiterUl.children().length <= 0) return true;
		fiterUl.children().each(function(){
			if($(this).hasClass("check")){
				$(this).remove();
				
				var id = $(this).attr("data-id");
				listUl.children().each(function(){
					if($(this).attr("id") == id){
						$(this).removeClass("check");
						return;
					}
				});
			}
		});
	});
	
	<!-- 向上排序 //-->
	toUp.bind("click", function() {
		if(fiterUl.children().length <= 0) return true;
	});
	
	<!-- 向下排序 //-->
	toDown.bind("click", function() {
		if(fiterUl.children().length <= 0) return true;
	});
	
}