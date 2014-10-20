/*
*
*
	operation function 
	create the element 
	create htmlelement and inset the body  
*
*
*/
	
var operation = Class.create();
operation.prototype = {

	initialize: function(options) {
		
		this.SetOptions(options);				//初始化属性值
		
		this.createElement = '';
		
		this.outHandler = false;
		this.outCreateElement = false;
	
	},
	
	//设置默认属性
	SetOptions: function(options) {
		
		this.options = {						//默认值
			handler:"",					
			callBack:"function"
		};
		
		Extend(this.options, options || {});
		
	},
	
	getHandlerPosition : function(){
		
		var handler = this.options.handler;
		var objLeft = handler.offsetLeft, objTop = handler.offsetTop, objWidth = handler.offsetWidth, objHeight = handler.offsetHeight;
		
		while( handler.tagName != "BODY" ){
			handler = handler.offsetParent;
			objLeft += handler.offsetLeft;
			objTop += handler.offsetTop;
		}
		
		return {
			left : objLeft+Number(objWidth*0.5),
			top :objTop+Number(objHeight*0.5)
		}
	
	},
	
	getHandler : function(){
		
		return this.options.handler;
		
	},
	
	getCreateObject : function (){
		
		 return this.createElement;
		
	},
	
	removeElement : function( leave ){
		var go = true;
		if(	leave ){
			if( !(this.outHandler && this.outCreateElement) ){
				go = false;
				return;
			}
		}
		
		if(go){
			try{
				document.body.removeChild(this.createElement);
				this.createElement= "";
				this.outHandler = false;
				this.outCreateElement = false;
			}catch(e){}
		}
	},
	
	closeElement : function( e, htmlElement ){
		var $this = this;
		e = e || window.event;   
		var target = e.target || e.srcElement;
		var _save = target;
		var jug = false;

		
		while(_save.tagName != 'BODY'){
			if( _save == htmlElement ){
				jug=true;
				break;
			}
			_save = _save.parentNode;	
		};
		
		
		if(jug == false){
			$this.removeElement();
			
			if(e && e.stopPropagation){
				 e.stopPropagation();
			}else{
				 window.event.cancelBubble = true;
			}
			e.preventDefault();
			
			oEventUtil.RemoveEventHandler(document.body,'mousedown');
		}
	},
	
	mouseEvent : function(){
		var $this = this;
		
		document.body.onmousedown = function(e){
			$this.closeElement( e, $this.createElement );
		}
		
		
		oEventUtil.RemoveEventHandler( this.options.handler, "mouseleave" );
		oEventUtil.AddEventHandler( this.options.handler, "mouseleave", function(event){
			
			$this.outHandler = true;
			$this.removeElement(true);
			
		}, this );
		
		
		oEventUtil.RemoveEventHandler( this.createElement, "mouseleave" );
		oEventUtil.AddEventHandler( this.createElement, "mouseleave", function(){
			
			$this.outCreateElement = true;
			$this.removeElement(true);
				  
		}, this );
		
	},
	
	getHtml : function( content, left, top){
		
		var div = document.createElement("div");
		div.style.position = "absolute";
		div.style.left = left+"px";
		div.style.top = top+"px";
		div.style.border = "1px solid #dcdcdc";
		div.style.borderRadius = "3px";
		div.style.zIndex = "1111";
		div.style.background = "#ffffff";
		div.style.boxShadow = "0px 1px 3px rgba(0, 0, 0, .05)";
		div.innerHTML = content;
		return div;
		
	},
	
	render : function( content ){
		
		var being = this.createElement != "" ? true : false ;
		if( being ){
			this.removeElement();
		}
		
		var left = this.getHandlerPosition()["left"];
		var top = this.getHandlerPosition()["top"];
		this.createElement = this.getHtml( content, left, top );
		
		document.body.appendChild(this.createElement);
		
		this.mouseEvent();								//绑定滑出事件.....
		
	}
	

	
}