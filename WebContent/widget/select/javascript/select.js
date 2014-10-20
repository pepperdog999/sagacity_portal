// JavaScript Document
var oEventUtil = new Object();
oEventUtil.AddEventHandler = function(oTarget,EventType,callBack, sertObject)
{
	//IE和FF的兼容性处理
	//如果是FF
	if(oTarget.addEventListener){
		oTarget.addEventListener(EventType,(function(){callBack.call(sertObject);}),false);
	} 
	//如果是IE
	else if(oTarget.attachEvent){
		oTarget.attachEvent('on'+EventType,(function(){callBack.call(sertObject);}));
	} else{
		oTarget['on'+EventType] = (function(){callBack.call(sertObject);});
	}
};
oEventUtil.RemoveEventHandler = function(oTarget,EventType){
	if(arguments = 'undefined'||[]){
		if(oTarget.addEventListener){
			oTarget.removeEventListener(EventType,false);
		} 
		//如果是IE
		else if(oTarget.attachEvent){
			oTarget.detachEvent(EventType);
		} else{
			oTarget['on' + EventType] = null;
		}
	}
};

var Extend = function(destination, source) {
	for (var property in source) {
		destination[property] = source[property];
	}
};

var Class = {
	create: function() {
		return function() { this.initialize.apply(this, arguments); }
	}
};

/*
*
*
	create select function 
	create the path
	read options for this args
*
*
*/
	
var select = Class.create();
select.prototype = {
	
	initialize: function(options) {
		this.box = null;
		this.checkBox = null;
		this.optionBox = null;
		this.font = null;
		this.span = null;
		this.a = null;
		
		this.isInsert = false;
		
		this.SetOptions(options);	//初始化属性值
	},
	
	//设置默认属性
	SetOptions: function(options) {
		this.options = {					//默认值
			width: 288,
			height:36,
			optionsHeight:123,
			handler:"object",
			delfultValue:"企业选择",
			isClick:false,
			tipTitle:"您未输入用户名、密码！请先正确输入！"
		};
		Extend(this.options, options || {});
	},
	
	createHtml : function(elementType, className) {
		var obj = document.createElement(elementType);
		if(className != null){
			obj.setAttribute("class", className);
		}
		return obj;
	},
	
	assembleModel : function(){
		this.box = this.createHtml("div", "select");
		
		this.checkBox = this.createHtml("div", "select-check");
		this.optionBox = this.createHtml("div", "select-option");
		
		this.font = this.createHtml("font", null);
		this.span = this.createHtml("span", null);
		this.a = this.createHtml("a", null);
		
		//组装
		this.a.innerHTML = "<i></i>";
		this.span.appendChild(this.a);
		this.checkBox.appendChild(this.font);
		this.checkBox.appendChild(this.span);
		
		this.box.appendChild(this.checkBox);
		this.box.appendChild(this.optionBox);
		return this.box;
	},
	
	assembleList : function(data){
		var html ="";
		html += "<ul>";
		for(var i = 0,len = data.length ; i < len ; i++){
			html += "<li>"+data[i].innerHTML+"</li>";
		}
		html += "</ul>";
		return html;
	},
	
	clickFreed : function(){
		this.options.isClick = true;
		
		//获取option数据列表 并添加到新的列表中 并隐藏 
		this.renderOptions();
		
		//绑定事件
		this.elementEvent();
	},
	
	elementEvent : function(data){
		oEventUtil.AddEventHandler(this.a, "click", this.clickElement, this);
		
		if(!this.options.isClick) return;
		var li = this.optionBox.getElementsByTagName("li");
		for(var i = 0,len=li.length; i<len;i++){
			oEventUtil.AddEventHandler(li[i], "click", this.liclickElement, [this,li[i]]);
		}
		
	},
	
	clickElement : function(){
		if(!this.options.isClick){
			alert(this.options.tipTitle);
			return;
		};
		this.isInsert = false;
		this.optionBox.style.display = "block";
		
		//绑定滑过事件
		oEventUtil.AddEventHandler(this.box, "mouseover", this.hoverOptions, this);
		oEventUtil.AddEventHandler(this.box, "mouseout", this.freeOptions, this);
	},
	
	liclickElement : function(args){
		var index = this[0].indexOf(this[1]);
		
		this[0].isInsert = false;
		this[0].optionBox.style.display = "none";
		
		this[0].font.innerHTML = this[1].innerHTML;
		
		var selectedObj = this[0].options.handler.getElementsByTagName("option");
		for(var k=0,len=selectedObj.length; k<len; k++){
			if(k == index){
				selectedObj[k].setAttribute("selected", "selected");
			}else{
				selectedObj[k].removeAttribute("selected");
			}
		}
		
	},
	
	indexOf : function(obj){
		var temp;
		var li = this.optionBox.getElementsByTagName("li");
		for(var i in li){
			if(obj == li[i]){
				temp = i;
				break;
			}
		}
		return temp;
	},
	
	hoverOptions : function(){
		this.isInsert = true;
	},
	
	freeOptions : function(){
		var $this = this;
		this.isInsert = false;
		var fun = setTimeout(function(){
			if(!$this.isInsert) {
				$this.optionBox.style.display = "none";
			}
			clearTimeout(fun);	
		},1000);
		
		//去除滑过事件
		oEventUtil.RemoveEventHandler(this.optionBox, "mouseover");
		oEventUtil.RemoveEventHandler(this.optionBox, "mouseout");
	},
	
	renderOptions : function(){
		//添加原始数据 并且设置其隐藏
		var data = this.options.handler.getElementsByTagName("option");
		if(data.length > 4){
			this.optionBox.style.height = this.options.optionsHeight+"px";
			this.optionBox.style.width = this.checkBox.offsetWidth-2+"px";
		}
		var li = this.assembleList(data);
		this.optionBox.style.display = "none";
		this.optionBox.innerHTML = li;
		
		var selected = false, selectedIndex;
		if(this.options.isClick == false){
			this.font.innerHTML = this.options.delfultValue;
		}else{
			for(var j=0,len=data.length; j<len;j++){
				if(data[j].getAttribute("selected") == "selected"){
					selected = true;
					selectedIndex = j;
					break;
				}
			}
			if(selected == false){
				selectedIndex = 0;
			}
			if(data.length <= 0){
				this.font.innerHTML = this.options.delfultValue;
			}else{
				this.font.innerHTML = data[selectedIndex].innerHTML;
			}
		}
		if(data.length <= 0 ) {
			this.optionBox.style.display = "none";
			return;
		}
	},
	
	render : function(){
		//设置原型hidden
		this.options.handler.style.display = "none";
		
		//创建新框架
		var model = this.assembleModel();
		
		this.checkBox.style.height = this.options.height+"px";
		this.checkBox.style.width = this.options.width+"px";
		//将新框架render到页面上
		this.options.handler.parentNode.appendChild(model);
		
		//获取option数据列表 并添加到新的列表中 并隐藏 
		this.renderOptions();
		
		//绑定事件
		this.elementEvent();
	}
	
	
};

