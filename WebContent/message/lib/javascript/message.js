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
	
	
	/***
	----------------
	左边列表拖动
	----------------
	***/
	//setMouseMove("scrollList");
	
	
});


function setMouseMove(obj) {
	
	
	var Drag = { 
        //当前被drag的对象 
		obj: null, 
		//初始化 
		init: function(id){ 
			var o = document.getElementById(id); 
			//当onmousedown开始拖拽 
			o.onmousedown = Drag.start; 
		}, 
		start: function(e){ 
			var o = Drag.obj = this; 
			//lastMouseX，lastMouseY记录上次鼠标的位置 
			o.lastMouseX = Drag.getEvent(e).x; 
			o.lastMouseY = Drag.getEvent(e).y; 
			//当onmousemove开始移动 
			document.onmousemove = Drag.move; 
			//当onmouseup终止拖拽 
			document.onmouseup = Drag.end; 
		}, 
		move: function(e){ 
			var o = Drag.obj; 
			//dx, dy表示鼠标移动的距离 
			var dx = Drag.getEvent(e).x - o.lastMouseX; 
			var dy = Drag.getEvent(e).y - o.lastMouseY; 
			//因为element.style.left通常返回的结果是'200px'的形式，所以要用parseInt转化 
			var left = parseInt(o.style.left) + dx ; 
			var top = parseInt(o.style.top) + dy; 
			//o.style.left = left+"px"; 
			o.style.top = top+"px"; 
			o.lastMouseX = Drag.getEvent(e).x; 
			o.lastMouseY = Drag.getEvent(e).y; 
		}, 
		end: function(e){ 
			document.onmousemove = null; 
			document.onMouseup = null; 
			
			var newTop = parseInt(Drag.obj.style.top);
			var DragHeight = $(Drag.obj).height();
			var pareHeight = $(Drag.obj).parent().height();
			
			if( newTop > 0){
				$(Drag.obj).animate({top:0},"fast");
			}
			
			if( Number(pareHeight) + Math.abs(newTop) > DragHeight){
				$(Drag.obj).animate({top:pareHeight - DragHeight },"fast");
			}
			
			
			Drag.obj = null;
		}, 
		//辅助函数，处理IE和FF不同的event模型 
		getEvent: function(e){ 
			if (typeof e == 'undefined'){ 
					e = window.event; 
				} 
				if(typeof e.x == 'undefined'){ 
					e.x = e.layerX;				//复制了n遍，到了csdn就变成ee.x了 
				} 
				if(typeof e.y == 'undefined'){ 
					e.y = e.layerY;				//复制了n遍，到了csdn就变成ee.x了 
				} 
				return e; 
			} 
		}; 
		
		var objHeight = $("#"+obj).height();
		var pareHeight = $("#"+obj).parent().height();
		if(objHeight > pareHeight){
			$("#"+obj).css({ "cursor":"move"});
			Drag.init(obj);
		}
		
}












