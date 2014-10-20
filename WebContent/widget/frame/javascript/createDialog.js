/*
*
*
	basic function 
	set funtion for using
*
*
*/
var isIE = (document.all) ? true : false;

var sfTypeId = function (id) {
	return "string" == typeof id ? document.getElementById(id) : id;
};

var Class = {
	create: function() {
		return function() { this.initialize.apply(this, arguments); }
	}
}

var Extend = function(destination, source) {
	for (var property in source) {
		destination[property] = source[property];
	}
}

var Bind = function(object, fun) {
	return function() {
		return fun.apply(object, arguments);
	}
}

var BindAsEventListener = function(object, fun) {
	return function(event) {
		return fun.call(object, (event || window.event));
	}
}

var CurrentStyle = function(element){
	return element.currentStyle || document.defaultView.getComputedStyle(element, null);
}

function addEventHandler(oTarget, sEventType, fnHandler) {
	if (oTarget.addEventListener) {
		oTarget.addEventListener(sEventType, fnHandler, false);
	} else if (oTarget.attachEvent) {
		oTarget.attachEvent("on" + sEventType, fnHandler);
	} else {
		oTarget["on" + sEventType] = fnHandler;
	}
};

function removeEventHandler(oTarget, sEventType, fnHandler) {
    if (oTarget.removeEventListener) {
        oTarget.removeEventListener(sEventType, fnHandler, false);
    } else if (oTarget.detachEvent) {
        oTarget.detachEvent("on" + sEventType, fnHandler);
    } else { 
        oTarget["on" + sEventType] = null;
    }
};

/*
*
*
	Dialog function 
	set object for draging
	write options for this args
*
*
*/
var SfDialogLevel = 300;
var SfDialog = Class.create();
SfDialog.prototype = {
	initialize : function() {
		this.shadow = null;
		this.objDiv = null;
		this.conDiv = null;
		this.titleDiv = null;
		this.closeA = null;
		this.subBtn = null;
		this.dragObj = null;
		this.BtnBox = null;
		this.callback = null;
		this.successCallback = null;
		this.isdrag = false;
		this.urbackerId = null;
		this.urvalue = null;
		this.cancelBtn = null;
		this.contentIframe = null;
		this.clicker = null;
	},

	setBackerId : function(urbackerId, urvalue) {
		this.urbackerId = urbackerId;
		this.urvalue = urvalue;
	},

	createShadow : function() {
		var thisDiv = document.createElement("div");
		thisDiv.id = "dialogShadow_" + SfDialogLevel;
		thisDiv.style.position = "absolute";
		thisDiv.style.zIndex = SfDialogLevel;
		thisDiv.style.backgroundColor = "#888888";
		thisDiv.style.filter = "alpha(Opacity=20)";
		thisDiv.style.opacity = "0.2";
		thisDiv.style.top = "0px";
		thisDiv.style.left = "0px";
		thisDiv.style.display = "none";
		return thisDiv;
	},

	createDiv : function() {
		var obj = document.createElement("div");
		obj.className = "dialogDiv";
		obj.style.zIndex = SfDialogLevel + 2;
		return obj;
	},

	createChildDiv : function(type, className, innerH) {
		var obj = document.createElement(type);
		if (className != null) {
			obj.className = className;
		}
		if (innerH != null) {
			obj.innerHTML = innerH;
		}
		return obj;
	},
	
	createIframe : function(id, src) {
		var iframe = document.createElement("iframe");
		iframe.id = id;
		iframe.name = id;
		iframe.width = "100%";
		iframe.height = "100%";
		iframe.setAttribute("marginwidth","0");
		iframe.setAttribute("marginheight","0");
		iframe.setAttribute("hspace","0");
		iframe.setAttribute("vspace","0");
		iframe.setAttribute("frameborder","0");
		iframe.scrolling = "no";
		iframe.setAttribute("allowtransparency","true");
		iframe.setAttribute("style","background-color:transparent;");
		iframe.src = src;
		return iframe;
	},

	removeObj : function(obj) {
		document.body.removeChild(obj);
	},

	getBodyElmentValue : function() {
		var _browser_scroll_x = 0;
		var _browser_scroll_y = 0;
		if (typeof (window.pageyoffset) == 'number') {
			// netscape compliant
			_browser_scroll_y = window.pageyoffset;
			_browser_scroll_x = window.pagexoffset;
		} else if (document.body && (document.body.clientWidth || document.body.clientHeight)) {
			// dom compliant
			_browser_scroll_y = document.body.clientHeight;
			_browser_scroll_x = document.body.clientWidth;
		} else if (document.documentElement && (document.documentElement.clientWidth || document.documentElement.clientHeight)) {
			// ie6 standards compliant mode
			_browser_scroll_y = document.documentElement.clientHeight;
			_browser_scroll_x = document.documentElement.clientWidth;
		}
		return {
			_browser_scroll_x : _browser_scroll_x,
			_browser_scroll_y : _browser_scroll_y
		};
	},

	show : function(width, height, isShowBtn, shadowBindClose) {
		if (typeof (width) != "number")
			return;
		if (typeof (height) != "number")
			return;

		var val = this.getBodyElmentValue();
		var _w = val._browser_scroll_x;
		var _h = val._browser_scroll_y;
		var winW = document.documentElement.clientHeight;
		if (winW > _h) {
			_h = winW;
		}

		this.shadow.style.width = _w + "px";
		this.shadow.style.height = _h + "px";

		this.objDiv.style.width = width + "px";
		this.objDiv.style.height = height + "px";
		
		var scrollTop;
		if(document.documentElement.scrollTop){
			scrollTop = document.documentElement.scrollTop;
		}else{
			scrollTop = document.body.scrollTop;
		}
		this.objDiv.style.left = (_w - width) * 0.5 + "px";
		this.objDiv.style.top = (winW - height) * 0.5+scrollTop + "px";
		
		/* return this height */
		var top_h = this.dragObj.offsetHeight;
		var bottom_h = this.BtnBox.offsetHeight;
		/*if (isIE) {
			this.conDiv.style.width = width - 6 + "px";
			top_h += 6;
		} else {
			this.conDiv.style.width = width + "px";
		}*/
		
		if (isShowBtn) {
			this.conDiv.style.height = height - top_h - bottom_h + "px";
		} else {
			this.conDiv.style.height = height - top_h + "px";
			this.BtnBox.style.display = "none";
		}

		this.objDiv.style.display = "block";
		this.shadow.style.display = "block";

		var oo = this;
		this.closeA.onclick = function() {
			oo.freeAndClose();
		};
		this.subBtn.onclick = function() {
			//oo.freeAndClose();
			oo.submitBack();
		};
		this.cancelBtn.onclick = function() {
			oo.freeAndClose();
		};
		if(shadowBindClose){
			this.shadow.onclick = function() {
				oo.freeAndClose();
			};
		}
		
		if (this.isdrag) {
			var dragObj = this.dragObj;
			var moveObj = this.objDiv;
			dragObj.style.cursor = "move";
			new Drag(moveObj, {
				mxContainer : document.body,
				Handle : dragObj,
				Limit : true,
				onStart : function() {
				},
				onMove : function() {
				},
				onStop : function() {
				}
			});
		}

		/*
		 * var myonresize = function() { var val = oo.getBodyElmentValue(); _w =
		 * val._browser_scroll_x; _h = val._browser_scroll_y; winW =
		 * document.documentElement.clientHeight; if (winW > _h) { _h = winW; }
		 * oo.variable.objDiv.style.left = (_w - width) * 0.5 + "px";
		 * oo.variable.objDiv.style.top = (_h - height) * 0.5 + "px";
		 * oo.variable.shadow.style.width = _w + "px";
		 * oo.variable.shadow.style.height = _h + "px"; };
		 */
	},
	
	initClickObject : function(obj){
		if( typeof obj != "undefined" && typeof obj != null ){
			this.clicker = obj;
		}
	},
	
	getClickObject : function(){
		return this.clicker;
	},

	beforeHideEvent : function(fn, submitfn, isdrag) {
		this.callback = fn;
		this.successCallback = submitfn;
		this.isdrag = isdrag;
	},
	

	freeAndClose : function() {
		try {
			if (this.callback != null) {
				var _callback = this.callback;
				var oo = this;
				this.callback = null;
				var t = _callback(oo, this.urbackerId, this.urvalue);
				if (t > 0) {
					if (t == 1) {
						this.hide();
					}
					return;
				}
			}
		} catch (e) {
		}
		this.free();
	},
	
	submitBack : function() {
		try {
			if (this.successCallback != null) {
				var _callback = this.successCallback;
				var oo = this;
				this.successCallback = null;
				var t = _callback(oo, this.urbackerId, this.urvalue);
				oo.freeAndClose();
				return;
			}
		} catch (e) {
		}
	},

	free : function() {
		if (this.shadow != null) {
			try {
				this.removeObj(this.shadow);
			} catch (e) {
			}
			this.shadow = null;
		}
		if (this.objDiv != null) {
			try {
				this.removeObj(this.objDiv);
			} catch (e) {
			}
			this.objDiv = null;
		}
	},

	hide : function() {
		if (this.objDiv != null) {
			try {
				this.objDiv.style.display = "none";
			} catch (e) {
			}
		}
	},

	restore : function() {
		if (this.objDiv != null) {
			try {
				this.objDiv.style.display = "";
			} catch (e) {
			}
		}
	},

	render : function() {
		if (this.objDiv == null)
			return;
		document.body.appendChild(this.shadow);
		document.body.appendChild(this.objDiv);
	},

	drag : function(obj) {
	},

	init : function(title, src) {
		if (typeof (title) != "string")
			return;
		if (arguments.length != arguments.callee.length) {
			content = arguments[0];
			title = "";
		}
		/* 添加遮罩层 */
		this.shadow = this.createShadow();
		/* 添加显示层 */
		this.objDiv = this.createDiv();
	
		this.objDiv = this.createChildDiv("div", "frame", null);
		
		/* create drag object*/
		this.dragObj = this.createChildDiv("div", "frame-title", null);
		this.titleDiv = this.createChildDiv("font", null, title);
		var span = this.createChildDiv("span", null, null);
		this.closeA = this.createChildDiv("a", null, null);
		span.appendChild(this.closeA);

		this.dragObj.appendChild(this.titleDiv);
		this.dragObj.appendChild(span);
		
		/* create content object*/
		this.conDiv = this.createChildDiv("div", "frame-con", null);
		this.contentIframe = this.createIframe("dialogIframe",src);
		this.conDiv.appendChild(this.contentIframe);
		
		/* create btn object*/
		this.BtnBox = this.createChildDiv("div", "frame-btn", null);
		this.subBtn = this.createChildDiv("a", "submit", "确定");
		this.cancelBtn = this.createChildDiv("a", "cancel", "取消");
		this.BtnBox.appendChild(this.subBtn);
		this.BtnBox.appendChild(this.cancelBtn);
		
		/* appendChild all object*/		
		this.objDiv.appendChild(this.dragObj);
		this.objDiv.appendChild(this.conDiv);
		this.objDiv.appendChild(this.BtnBox);

		SfDialogLevel += 3;
		if (SfDialogLevel > 500) {
			SfDialogLevel = 300;
		}
	},

	getConWidth : function() {
		try {
			return this.conDiv.offsetWidth;
		} catch (e) {
		}
	},

	getConHeight : function() {
		try {
			return this.conDiv.offsetHeight;
		} catch (e) {
		}
	},

	setHeader : function(innerH) {
		try {
			this.titleDiv.innerHTML = innerH;
		} catch (e) {
		}
	}
};


/*
*
*
	Drag function 
	set object for draging
	write options for this args
*
*
*/
var Drag = Class.create();
Drag.prototype = {
	//拖放对象
	initialize: function(drag, options) {
		this.Drag = sfTypeId(drag);//拖放对象
		this._x = this._y = 0;//记录鼠标相对拖放对象的位置
		this._marginLeft = this._marginTop = 0;//记录margin
		//事件对象(用于绑定移除事件)
		this._fM = BindAsEventListener(this, this.Move);
		this._fS = Bind(this, this.Stop);
		
		this.SetOptions(options);
		
		this.Limit = !!this.options.Limit;
		this.mxLeft = parseInt(this.options.mxLeft);
		this.mxRight = parseInt(this.options.mxRight);
		this.mxTop = parseInt(this.options.mxTop);
		this.mxBottom = parseInt(this.options.mxBottom);
		
		this.LockX = !!this.options.LockX;
		this.LockY = !!this.options.LockY;
		this.Lock = !!this.options.Lock;
		
		this.onStart = this.options.onStart;
		this.onMove = this.options.onMove;
		this.onStop = this.options.onStop;
		
		this._Handle = sfTypeId(this.options.Handle) || this.Drag;
		this._mxContainer = sfTypeId(this.options.mxContainer) || null;
		
		this.Drag.style.position = "absolute";
		//透明
		if(isIE && !!this.options.Transparent){
			//填充拖放对象
			with(this._Handle.appendChild(document.createElement("div")).style){
				width = height = "100%"; backgroundColor = "#fff"; filter = "alpha(opacity:0)"; fontSize = 0;
			}
		}
		//修正范围
		this.Repair();
		addEventHandler(this._Handle, "mousedown", BindAsEventListener(this, this.Start));
	},
	//设置默认属性
	SetOptions: function(options) {
		this.options = {//默认值
			Handle:			"",//设置触发对象（不设置则使用拖放对象）
			Limit:			false,//是否设置范围限制(为true时下面参数有用,可以是负数)
			mxLeft:			0,//左边限制
			mxRight:		9999,//右边限制
			mxTop:			0,//上边限制
			mxBottom:		9999,//下边限制
			mxContainer:	"",//指定限制在容器内
			LockX:			false,//是否锁定水平方向拖放
			LockY:			false,//是否锁定垂直方向拖放
			Lock:			false,//是否锁定
			Transparent:	false,//是否透明
			onStart:		function(){},//开始移动时执行
			onMove:			function(){},//移动时执行
			onStop:			function(){}//结束移动时执行
		};
		Extend(this.options, options || {});
	},
	//准备拖动
	Start: function(oEvent) {
		if(this.Lock){ return; }
		this.Repair();
		//记录鼠标相对拖放对象的位置
		this._x = oEvent.clientX - this.Drag.offsetLeft;
		this._y = oEvent.clientY - this.Drag.offsetTop;
		//记录margin
		this._marginLeft = parseInt(CurrentStyle(this.Drag).marginLeft) || 0;
		this._marginTop = parseInt(CurrentStyle(this.Drag).marginTop) || 0;
		//mousemove时移动 mouseup时停止
		addEventHandler(document, "mousemove", this._fM);
		addEventHandler(document, "mouseup", this._fS);
		if(isIE){
			//焦点丢失
			addEventHandler(this._Handle, "losecapture", this._fS);
			//设置鼠标捕获
			this._Handle.setCapture();
		}else{
			//焦点丢失
			addEventHandler(window, "blur", this._fS);
			//阻止默认动作
			oEvent.preventDefault();
		};
		//附加程序
		this.onStart();
	},
	//修正范围
	Repair: function() {
		if(this.Limit){
			//修正错误范围参数
			this.mxRight = Math.max(this.mxRight, this.mxLeft + this.Drag.offsetWidth);
			this.mxBottom = Math.max(this.mxBottom, this.mxTop + this.Drag.offsetHeight);
			//如果有容器必须设置position为relative或absolute来相对或绝对定位，并在获取offset之前设置
			!this._mxContainer || CurrentStyle(this._mxContainer).position == "relative" || CurrentStyle(this._mxContainer).position == "absolute" || (this._mxContainer.style.position = "relative");
		}
	},
	//拖动
	Move: function(oEvent) {
		//判断是否锁定
		if(this.Lock){ this.Stop(); return; };
		//清除选择
		window.getSelection ? window.getSelection().removeAllRanges() : document.selection.empty();
		//设置移动参数
		var iLeft = oEvent.clientX - this._x, iTop = oEvent.clientY - this._y;
		//设置范围限制
		if(this.Limit){
			//设置范围参数
			var mxLeft = this.mxLeft, mxRight = this.mxRight, mxTop = this.mxTop, mxBottom = this.mxBottom;
			//如果设置了容器，再修正范围参数
			if(!!this._mxContainer){
				mxLeft = Math.max(mxLeft, 0);
				mxTop = Math.max(mxTop, 0);
				mxRight = Math.min(mxRight, this._mxContainer.clientWidth);
				mxBottom = Math.min(mxBottom, this._mxContainer.clientHeight);
			};
			//修正移动参数
			iLeft = Math.max(Math.min(iLeft, mxRight - this.Drag.offsetWidth), mxLeft);
			iTop = Math.max(Math.min(iTop, mxBottom - this.Drag.offsetHeight), mxTop);
		}
		//设置位置，并修正margin
		if(!this.LockX){ this.Drag.style.left = iLeft - this._marginLeft + "px"; }
		if(!this.LockY){ this.Drag.style.top = iTop - this._marginTop + "px"; }
		//附加程序
		this.onMove();
	},
	//停止拖动
	Stop: function() {
		//移除事件
		removeEventHandler(document, "mousemove", this._fM);
		removeEventHandler(document, "mouseup", this._fS);
		if(isIE){
			removeEventHandler(this._Handle, "losecapture", this._fS);
			this._Handle.releaseCapture();
		}else{
			removeEventHandler(window, "blur", this._fS);
		};
		//附加程序
		this.onStop();
	}
};