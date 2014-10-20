// JavaScript Document
$(function(){
		//processPathShow();
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
			Tab Control set style function
			return back functuon for call-back
		*
		*	
		*/
		setStyleChange($("#selections a"), "check", setProcessUrl);
		
		
		
		//显示 隐藏流程
		$("#processNodesShow").bind("click", function(){
			if(!isProcessText) return;
			openProcessNodes();
			$(this).hide();
			$("#processNodesHide").show();
		});
		
		$("#processNodesHide").bind("click", function(){
			if(!isProcessText) return;
			closeProcessNodes();
			$(this).hide();
			$("#processNodesShow").show();
		});
		
});


function closeProcessNodes(){
	var o = $("#oriented-content");
	o.find(".com-tab-hidden").hide();
	o.find(".arrowsLink").removeClass("check");
}

function openProcessNodes(){
	var o = $("#oriented-content");
	o.find(".com-tab-hidden").show();
	o.find(".arrowsLink").each(function(){
		if($(this).hasClass("check")){
			$(this).removeClass("check");
		}
	});
	
}


function processPathShow(){
	
	var scrollTop = $("body").scrollTop();
	if( /msie/.test(navigator.userAgent.toLowerCase()) ){
		$("html").css({overflow:"hidden"});
	}else{
		$("body").css({overflow:"hidden"});
	}
	
	var WinHeight = $(window).height();
	$("#pathFrame").css({height:WinHeight-34-40});
	$("#pathFrame").attr("src","../workflow/editPath.html")
	
	$("#pathLayer").show().css({top:scrollTop});
}

function processPathClose(){
	
	$("#pathLayer").hide();
	if( /msie/.test(navigator.userAgent.toLowerCase()) ){
		$("html").css({overflow:"auto"});
	}else{
		$("body").css({overflow:"auto"});
	}
	
}