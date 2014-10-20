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
		给网盘列表绑定选中事件， 单个选中	
	*
	*/
	
	/*新建文件夹*/
	$("#createFolder").bind("click", function(){
		DialogCreateFile();
	});
	
});



var addDialogFile;
function DialogCreateFile(){
	addDialogFile = new SfDialog();
	addDialogFile.init("新建文件夹", "filter/choice/addfolder.html");
	addDialogFile.beforeHideEvent(null, null, true);
	addDialogFile.render();
	addDialogFile.show(600,220, false, false);
}

var renameDialogFile;
function DialogRenameFile(){
	renameDialogFile = new SfDialog();
	renameDialogFile.init("重命名", "filter/choice/rename.html");
	renameDialogFile.beforeHideEvent(null, null, true);
	renameDialogFile.render();
	renameDialogFile.show(600,220, false, false);
}


var moveDialogFile;
function DialogMoveFile(){
	moveDialogFile = new SfDialog();
	moveDialogFile.init("移动", "filter/choice/move.html");
	moveDialogFile.beforeHideEvent(null, null, true);
	moveDialogFile.render();
	moveDialogFile.show(600,420, false, false);
}


var optionFolder = Class.create();
optionFolder.prototype = {
	initialize : function(options) {
		this.SetOptions(options);	//初始化属性值
		
		this.handler = "disk-tainer";
		this.checkboxAll = "checkboxAll";
		this.pageLocal = "page_local";
		this.operationObject ="";
		this.operationHtml = "false";
		
		this.setCheckBox();
		this.nodeClick();
		this.setCheckboxAll();
		this.setPageLocal();
		
	},
	
	//设置默认属性
	SetOptions: function(options) {
		this.options = {					//默认值
			clickFolderBackFun:"function",
			resetLsitFun:"function"
		};
		Extend(this.options, options || {});
	},

	nodeClick : function() {
		var $this = this;
		var obj  = $("#"+this.handler);
		var objChild = obj.children();
		objChild.each(function(){
			if($(this).attr("typefile") == "folder"){
				var linka = $(this).find(".disk-linka");
				linka.bind("click", function(event){
					$this.options.clickFolderBackFun.call(this);
					event.stopPropagation();	
				});
			}else if($(this).attr("typefile") == "file"){
				var linka = $(this).find(".disk-linka");
				linka.bind("click", function(event){
					window.open('../preview/filePreview?dir='+linka.parent().parent().attr("dir")+'&fileName='+linka.parent().parent().attr("name"),'_new', '');
					event.stopPropagation();	
				});
			}
		});
	},

	setCheckBox : function() {
		var $this = this;
		var checkboxobj  = $("#"+this.checkboxAll);
		var obj  = $("#"+this.handler);
		var objChild = obj.children();
		
		objChild.each(function(){
			$(this).unbind("mousedown");
			$(this).bind("mousedown", function(e){   //设置右键点击事件
				var typeFile = $(this).attr("typefile");
				if(e.which == 3){
					/*
					var x = e.pageX;
					var y = e.pageY;
					if($this.operationHtml == true){
						if(typeof($this.operationObject) == "object")
						$($this.operationObject).remove();
						$this.operationHtml = false;
					}
					$this.operationObject = $this.createHtml(typeFile);
					$($this.operationObject).css({top:Number(y)+2, left: Number(x)+2});
					$("body").append($($this.operationObject));
					$this.operationHtml = true;
					$this.setAutoClose();			//自动关闭方法
					*/
				}else if(e.which == 1){				 //设置单选事件
					var checkBox = $(this).find(".disk-checkbox");
					if(checkBox.hasClass("check")){
						checkBox.removeClass("check");
					}else{
						checkBox.addClass("check");
					}
					if(objChild.find(".disk-checkbox").length == objChild.find(".check").length){
						checkboxobj.find(".disk-checkbox").addClass("check");
					}else{
						checkboxobj.find(".disk-checkbox").removeClass("check");
					}
				}
				return false; 
			});
		});
		
	},
	
	setPageLocal : function(){
		var $this=this;
		var obj  = $("#"+this.pageLocal);
		var html = "<i>»</i><em>&nbsp;</em>";
		obj.find("a").bind("click", function(){
			var i = $(this).index();
			obj.children().each(function(k){
				if(i < k){
					$(this).remove();
				}
			});
			obj.append(html);
			$this.options.resetLsitFun.call($(this));
		})
	},
	
	reSet : function(){
		this.setCheckBox();
		this.nodeClick();
		this.setPageLocal();
	},
	
	setCheckboxAll : function(){
		var clickobj  = $("#"+this.checkboxAll);
		var obj  = $("#"+this.handler);
		clickobj.bind("click", function(){
				var checkBox = $(this).find(".disk-checkbox");
				if(checkBox.hasClass("check")){
					checkBox.removeClass("check");
					obj.find(".disk-checkbox").removeClass("check");
				}else{
					checkBox.addClass("check");
					obj.find(".disk-checkbox").addClass("check");
				}	
		});
	},
	
	createHtml : function(typeFile){
		var box = document.createElement("div");
		box.setAttribute("class", "disk-operate");
		var html = "";
		html +="<ul oncontextmenu=self.event.returnValue=false onselectstart=\"return false\">";
		if(typeFile == "folder"){
    		html +="<li><a><i class=\"d-icons open\"></i>打开</a></li>";
		}
        html +="<li><a><i class=\"d-icons down\"></i>下载</a></li>";
		if(typeFile == "file"){
        	html +="<li><a><i class=\"d-icons share\"></i>分享给好友</a></li>";
        	html +="<li><a><i class=\"d-icons send\"></i>发送</a></li>";
        	html +="<li><a><i class=\"d-icons move\"></i>移动到</a></li>";
		}
        html +="<li><a><i class=\"d-icons rename\"></i>重命名</a></li>";
        html +="<li><a><i class=\"d-icons delate\"></i>删除</a></li>";
		html +="</ul>";
		box.innerHTML = html;
		return box;
	},
	
	setAutoClose : function(){
		var $this = this;
		var isHover = false;
		
		$($this.operationObject).hover(function(){
				isHover = true;
			}, function(){
				$($this.operationObject).remove();
				$this.operationHtml	= false;
				isHover = false;
		});
		
		if($this.operationHtml == true){
			setTimeout(function(){
				if(isHover == false){
					$($this.operationObject).remove();
					$this.operationHtml	= false;
				}
			}, 2000);
		}
	}
	
}

























