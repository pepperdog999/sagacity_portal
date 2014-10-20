// JavaScript Document

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

var oEventUtil = new Object();
oEventUtil.AddEventHandler = function(oTarget,EventType,callBack, sertObject)
{
	//IE和FF的兼容性处理
	//如果是FF
	if(oTarget.addEventListener){
		oTarget.addEventListener(EventType,(function(event){callBack.call(sertObject,event);}),false);
	} 
	//如果是IE
	else if(oTarget.attachEvent){
		oTarget.attachEvent('on'+EventType,(function(event){callBack.call(sertObject,event);}));
	} else{
		oTarget['on'+EventType] = (function(event){callBack.call(sertObject,event);});
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



var faceplateExample = null;

var faceplate = Class.create();
faceplate.prototype = {

	initialize: function(options) {
		
		this.options = {						//默认值
			width:"500",					
			height:"100%",
			top:"80"
		};
		
		Extend(this.options, options || {});
		
		this.htmlElement="";
	},
	
	getHandler : function(){
		
	},
	
	render : function( content ){
		
		this.htmlElement = this.setHtmlElement( content );
		this.htmlElement.style.width="0px";
		this.htmlElement.style.right="0px";

		if( faceplateExample == null ){
			document.body.appendChild(this.htmlElement);
			this.animate(this.htmlElement);
			faceplateExample = this.htmlElement;
		}
		
	},
	
	setHtmlElement : function( content ){
		
		var div = document.createElement("div");
		div.style.position="absolute";
		div.style.right="0px";
		div.style.top=this.options.top+"px";
		div.style.zIndex="1111";
		div.style.textAlign="left";
		div.style.backgroundColor="#f9f9f7";
		div.style.borderLeft="1px solid #e1e1e1";
		div.style.width="500px";
		div.style.height="100%";
		div.style.overflow="hidden";
		div.style.boxShadow="0 1px 7px rgba(0, 0, 0, .15)";
		div.style.height="100%";
		
		div.style.width=this.options.width+"px";
		if(this.options.height.match("%") == null){
			div.style.height=this.options.height+"px";
		}else{
			div.style.height=this.options.height;
		}
		
		
		var contentFrame = this.createContent(content);	
		div.appendChild(contentFrame);
		
		return div;
		
	},
	
	createCloseBtn : function(){
		
		var closeLink = document.createElement("a");
		closeLink.setAttribute("class", "faceplate-close");
		closeLink.setAttribute("title", "关闭");
		closeLink.innerHTML = "<img src=\"lib/images/faceplate-close.png\">";
		closeLink.style.position="absolute";
		closeLink.style.right="10px";
		closeLink.style.top="10px";
		closeLink.style.zIndex="1111";
		closeLink.style.padding="8px 8px 6px 8px";
		closeLink.style.borderRadius="3px";
		closeLink.style.cursor="pointer";
		
		closeLink.onmouseover = function(){
			this.style.background = "#f2f2ea";
			this.getElementsByTagName("img")[0].src = "lib/images/faceplate-close-hover.png";
		};
		closeLink.onmouseout = function(){
			this.style.background = "none";
			this.getElementsByTagName("img")[0].src = "lib/images/faceplate-close.png";
		};
		
		var $this = this;
		oEventUtil.AddEventHandler( closeLink, "click", $this.free, $this );  
		return closeLink;
		
	},
	
	
	createContent : function(content){
		
		var contentFrame = document.createElement("div");
		contentFrame.style.overflow="hidden";
		contentFrame.style.position="relative";
		contentFrame.style.height="100%";
		contentFrame.style.width = this.options.width+"px";
		
		var closeLink = this.createCloseBtn();	
		contentFrame.appendChild(closeLink);
		
		var contentInset = document.createElement("div");
		if( typeof(content) == "object"){
			contentInset.appendChild(content);
		}else{
			contentInset.innerHTML = content;
		}
		
		contentFrame.appendChild(contentInset);
		return contentFrame;
	},
	
	
	animate : function(){
		var $this = this;
		var thisWidth = parseInt(this.options.width);
		
		var val = 0;
		var move = setInterval(function(){
			if( val >= thisWidth){
				$this.htmlElement.style.width = thisWidth+"px";
				clearInterval(move);
				return;
			}
			val = Number(val)+8;
			$this.htmlElement.style.width= val+"px";
		},1);
		
	},
	
	free : function(){
		var $this = this;
		var thisWidth = parseInt(this.options.width);
		var move = setInterval(function(){
			if( thisWidth < 0){
				document.body.removeChild($this.htmlElement);
				clearInterval(move);
				faceplateExample = null;
				return;
			}
			thisWidth = thisWidth-8;
			$this.htmlElement.style.width= thisWidth+"px";
		},1);
	}
	
	
}