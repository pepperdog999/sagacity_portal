// JavaScript Document
$(function(){
	/*
	set disabled
	*/
	//setProcessFromDisable(["oriented02","oriented06"]);
	/*
	*
	*
		Dialog object bind clicking function
		get all of Dialog object
	*
	*	
	*/
	jugDialogPage();
	
	/*
	*
	*
		set clicking event
		list of table is show or hidden
	*
	*	
	*/
	setTablistIsHidden();
	
	//setProcessHiddenIndex("oriented05");
	/*
	*
	*
		children of oriented bind EventListenerEvent
	*
	*	
	*/
	setProcessListClick();
	
});

(function($) {
    var a, b;
    $.uaMatch = function(a) {
        a = a.toLowerCase();
        var b = /(chrome)[ \/]([\w.]+)/.exec(a) || /(webkit)[ \/]([\w.]+)/.exec(a) || /(opera)(?:.*version|)[ \/]([\w.]+)/.exec(a) || /(msie) ([\w.]+)/.exec(a) || a.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec(a) || [];
        return {
            browser: b[1] || "",
            version: b[2] || "0"
        }
    },
    a = $.uaMatch(navigator.userAgent),
    b = {},
    a.browser && (b[a.browser] = !0, b.version = a.version),
    b.chrome ? b.webkit = !0 : b.webkit && (b.safari = !0),
    $.browser = b,
    $.sub = function() {
        function a(b, c) {
            return new a.fn.init(b, c)
        }
        $.extend(!0, a, this),
        a.superclass = this,
        a.fn = a.prototype = this(),
        a.fn.constructor = a,
        a.sub = this.sub,
        a.fn.init = function c(c, d) {
            return d && d instanceof p && !(d instanceof a) && (d = a(d)),
            $.fn.init.call(this, c, d, b)
        },
        a.fn.init.prototype = a.fn;
        var b = a(e);
        return a
   };
})(jQuery);

var dialogMent;
function addPartMent(){
	dialogMent = new SfDialog();
	dialogMent.init("选择部门", "filter/choice/department.html");
	dialogMent.beforeHideEvent(DialogBackFun,null,true);
	dialogMent.render();
	dialogMent.show(545, 500, true, false);
}

var dialogUsers;
function addPartUsers(){
	dialogUsers = new SfDialog();
	dialogUsers.init("选择人员", "filter/choice/departusers.html");
	dialogUsers.beforeHideEvent(DialogBackFun,chooseUserConfirm,true);
	dialogUsers.render();
	dialogUsers.show(600, 500, true, false);
}

var dialogComment;
function addPartComment(){
	dialogComment = new SfDialog();
	dialogComment.init("提交发送", "filter/choice/comment.html");
	dialogComment.beforeHideEvent(DialogBackFun,null,true);
	dialogComment.render();
	dialogComment.show(600, 500, false, false);
}


var dialogOpinion;
function addPartOpinion(){
	dialogOpinion = new SfDialog();
	dialogOpinion.init("流程意见", "filter/choice/onlyOpinion.html");
	dialogOpinion.beforeHideEvent(DialogBackFun,null,true);
	dialogOpinion.render();
	dialogOpinion.show(600, 340, false, false);
}

var dialogReply;
function addPartReply(){
	dialogReply = new SfDialog();
	dialogReply.init("回复流程意见", "filter/choice/reply.html");
	dialogReply.beforeHideEvent(DialogBackFun,null,true);
	dialogReply.render();
	dialogReply.show(600, 300, false, false);
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

/*
*
*
	lists of table show or hide
	click the arrows
*
*	
*/

function setTablistIsHidden(){
	$(".arrowsLink").each(function(){
		$(this).bind("click",function(){
				var pare = $(this).parent().parent().parent();
				if($(this).hasClass("check")){
					pare.find(".com-tab-hidden").hide();
					$(this).removeClass("check");
				}else{
					pare.find(".com-tab-hidden").show();
					$(this).addClass("check");
				}
		});	
	});
}


function scrolloriented(){
	var scrollTop = $(document).scrollTop();
	
	var list = $("#oriented-content").children();
	var listMenu = $("#orientedFrame").children();
	var save = 0, len = list.length;
	list.each(function(index){
		if(index < len-1 && index != 1){
			var thisOffset = $(this).offset().top;
			var next = list.eq(Number(index)+1);
			var nextTop = next.offset().top;
			if(thisOffset <= scrollTop && scrollTop<nextTop){
				listMenu.removeClass("check");
				listMenu.eq(index+1).addClass("check");
				return false;
			}
		}
	});
}

function setProcessListClick(){
	$("#orientedFrame").children().each(function(){
		$(this).bind("click", function(){
			var objID = $(this).attr("data-id");
			var offsetTopVal = $("#"+objID).offset().top;
			var p = $("#"+objID).find(".arrowsLink");
			if(p.hasClass("check") == false){
				p.click();
			}
			if($.browser.msie || $.browser.mozilla){
				$("html").stop(true, true).animate({scrollTop : parseInt(offsetTopVal)}, "slow");
			}else{
				$("body").stop(true, true).animate({scrollTop : parseInt(offsetTopVal)}, "slow");
			}
			$(this).siblings().removeClass("check");
			$(this).addClass("check");
		});
	});
}

function setProcessHiddenIndex(str){
	if(typeof(str) != "string") return;
	var list = $("#orientedFrame");
	list.children().each(function(){
		if($(this).attr("data-id") == str){
			$(this).click();
			return false;
		}
	});

}

function setProcessFromDisable(args){
	var box = $("#oriented-content");
	box.find("input").attr("disabled","disabled");
	box.find("textarea").attr("disabled","disabled");
	box.find(".up-ul .delate").hide();
	box.find(".up-add").hide();
	
	box.find(".bindDialog").addClass("disable");
	
	if(typeof(args) != "string" && args.length > 0){
		for(var i=0; i<args.length; i++) {
			box.children().each(function(j){
				if(args[i] == $(this).attr('id')){
					$(this).find("input").removeAttr("disabled");
					$(this).find("textarea").removeAttr("disabled");
					$(this).find(".up-ul .delate").show();
					$(this).find(".up-add").show();
					$(this).find(".bindDialog").removeClass("disable");
					return;
				}
			});
		};
	}
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
				case "ment":
					addPartMent();
					break;
				case "user":
					addPartUsers();
					break;
				case "comment":
					addPartComment();
					break;
				case "opinion":
					addPartOpinion();
					break;
				case "reply":
					addPartReply();
					break;	
				default:
					alert("选择出错啦！");
				}
		});	
	});
}