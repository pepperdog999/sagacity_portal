// JavaScript Document
function getElementsByClassName(n) { 
	var classElements = [],allElements = document.getElementsByTagName('*'); 
	for (var i=0; i< allElements.length; i++ ) 
	{ 
		//if (allElements[i].className == n ) { 
		if (hasClass(allElements[i], n) ) { 
			classElements[classElements.length] = allElements[i]; 
		} 
	} 
	return classElements; 
}


function hasClass(ele,cls) {
  return ele.className.match(new RegExp('(\\s|^)'+cls+'(\\s|$)'));
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


var getMax = function(array) { 
	if(typeof(array) != "object") return;
	var max = array[0];  
	var len = array.length;  
	for (var i = 1; i < len; i++){   
		if (array[i] > max){   
			max = array[i];   
		}   
	}
	return max;  
}


var getMin = function(array) { 
	if(typeof(array) != "object") return;
	var min = array[0];  
	var len = array.length;  
	for (var i = 1; i < len; i++){   
		if (array[i] < min){   
			min = array[i];   
		}   
	}
	return min;  
}


var getTypeId = function (id) {
	return "string" == typeof id ? document.getElementById(id) : id;
}


var Extend = function(destination, source) {
	for (var property in source) {
		destination[property] = source[property];
	}
}


var Class = {
	create: function() {
		return function() { this.initialize.apply(this, arguments); }
	}
}


function insertAfter(newEl, targetEl)
{
      var parentEl = targetEl.parentNode;

      if(parentEl.lastChild == targetEl)
      {
           parentEl.appendChild(newEl);
      }else
      {
           parentEl.insertBefore(newEl,targetEl.nextSibling);
      }            
}