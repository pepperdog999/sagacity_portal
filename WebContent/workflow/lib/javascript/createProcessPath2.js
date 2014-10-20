
var nodes =[];
//虚拟点  设置点击事件
var links = [];

/*
*
*
	Path function 
	create the path
	write options for this args
*
*
*/
	
var Path = Class.create();
Path.prototype = {

	initialize: function(options) {
		this.SetOptions(options);	//初始化属性值
		//this.isEdit = this.options.isEdit;
	},
	
	//设置默认属性
	SetOptions: function(options) {
		this.options = {					//默认值
			isEdit:true,					//确定是否可编辑状态 
			mainFrameId:"start_frame",
			clickAfterFun:{
				editFun:"function",
				queryFun:"function"
			}
		};
		Extend(this.options, options || {});
	},
	
	
	getDataJson : function(){
		return nodes;
	},
	
	getLinkJson : function(){
		return links;
	},
	
	clearProcess : function(){
		var mainFrameObj = document.getElementById(this.options.mainFrameId);
		mainFrameObj.innerHTML = "";
		nodes.length = 0;
		links.length = 0;
	},
	
	reload : function( dataJson, linkJson ) {
		
		for( var i=0; i<dataJson.length; i++){
			var handler = {};
			handler["code"] = dataJson[i]["code"];			//把所有节点转换到nodes里面
			handler["name"] = dataJson[i]["name"];
			handler["topname"] = dataJson[i]["topname"];
			handler["typeImg"] = dataJson[i]["typeImg"];
			handler["status"] = dataJson[i]["status"];
			handler["executors"] = dataJson[i]["executors"];
			handler["parentCode"] = dataJson[i]["parentCode"];
			handler["childrenCode"] = dataJson[i]["childrenCode"];
			handler["links"] = dataJson[i]["links"];
			nodes[i] = handler;
		}
		
		for( var j=0; j<linkJson.length; j++){
			var handler = {};
			handler["code"] = linkJson[j]["code"];			//把所有节点转换到nodes里面
			handler["parentCode"] = linkJson[j]["parentCode"];
			handler["childrenCode"] = linkJson[j]["childrenCode"];
			links[j] = handler;
		}
		
		this.render(nodes);
	
	},
	
	
	//创建 囊括单个数据的table
	createIncludeSingleDataTable : function(code, topname, name, src, parentCode, childrenCode, status) {
		var table = document.createElement( "table" );
		table.setAttribute( "border", 0 );
		table.setAttribute( "cellspacing", 0 );
		table.setAttribute( "cellpadding", 0 );
		var row = table.insertRow();
		for ( var i = 0; i < 2 ; i++ ) {
			var cell = row.insertCell(0);
			cell.setAttribute( "align", "left" );
			cell.setAttribute( "valign", "middle" );
			if( i == 1){
				cell.setAttribute( "class", "c" );
				cell.appendChild( this.getListHtml(code, topname, name, src, parentCode, childrenCode, status) );
			}
			if( i == 0 ) {
				cell.setAttribute( "id", code+"_frame" );
			}
		}
		return table;
	},
	
	
	//创建 单个数据html格式
	getListHtml : function(code, topname, name, src, parentCode, childrenCode, status){
		var list = document.createElement("div");
		list.setAttribute("class", "userList01 userTip");
		list.setAttribute("id", code);
		//list.setAttribute("onclick", "this.callBack(this)");
		list.setAttribute("data-parentID", parentCode);
		list.setAttribute("data-childrenID", childrenCode);
		list.innerHTML = this.getHtml(topname, name, src, status); 
		return list;
	},
	
	//节点组装元素
	getHtml : function(topname, name, src, status){
		var html = "";
		html += "<p class=\"userList-t\">"+topname+"</p>";
		html += "<p class=\"userList-img\"><img src=\""+src+"\"/></p>";
		html += "<p class=\"userList-b\">"+name+"</p>";
		html += "<i class=\"userList-arrow\"></i>";
		html += "<b class=\"userList-status\"><img src=\""+status+"\"/></b>";
		return html;
	},
	
	
	//定图标类型
	typeImgSwitch : function(type){
		switch(type){
			case 1 :
				return "lib/img/path-start.png";
				break;
			case 2 :
				return "lib/img/path-end.png";
				break;
			case 3 :
				return "lib/img/path-comment.png";
				break;
			case 4 :
				return "lib/img/path-people.png";
				break;
			case 5 :
				return "lib/img/path-system.png";
				break;
			case 6 :
				return "lib/img/path-single.png";
				break;
			default:
				return "lib/img/path-comment.png";
				break;
		}
	},
	
	//定当前节点状态
	statusSwitch : function(type){
		switch(type){
			case 0 :
				return "lib/img/normal.png";
				break;
			case 1 :
				return "lib/img/active.png";
				break;
			case 2 :
				return "lib/img/unactive.png";
				break;
			case 3 :
				return "lib/img/see.png";
				break;
			default:
				return "lib/img/normal.png";
				break;
		}
	},
	
	
	// add elements of all and draw all of lines 
	render : function( dataJson ){
		
		var handlerArray = this.assembleDataArray( dataJson );				//循环二维数组  把数据铺在页面上
		for( var j=0; j<handlerArray.length; j++) {	
						
			var parentCode = handlerArray[j][0]["parentCode"];
			this.renderSingleData( parentCode[0], handlerArray[j], j);			//第一个参数为此组数据的父级ID   第2个位次数据集
			
		}
		
		this.findSingleGroups();											//画线方法
			
		this.clickEvent();										//给每个节点绑定click事件

	},
	
	renderSingleData : function(objCode, dataArray, index) {							//传入参数为 当前父节点的code  数组格式
		
		if( objCode.match(new RegExp("link")) == null ){
		
			var prototypeElement = this.createInsertChildTable(dataArray);		//根据子集数据  形成DOM html
			
			var frameObject = document.getElementById(objCode+"_frame");
			
			try{
				frameObject.innerHTML ="";
				frameObject.appendChild(prototypeElement);
			}catch(e){
				alert("写入开始节点的下一级TD 的id");
			}
			
		}else{
			
			var prototypeElement = this.createInsertChildTable(dataArray);		//根据子集数据  形成DOM html
			try{
				var linkArr = document.getElementById( objCode );
				var ElementPare = linkArr.parentNode.parentNode;
				var prevhtml = ElementPare.innerHTML;
			
				var table = document.createElement("table");
				table.setAttribute("border", 0);
				table.setAttribute("cellspacing", 0);
				table.setAttribute("cellpadding", 0);
				var row = table.insertRow();
				for ( var i = 0; i < 2 ; i++ ) {
					var cell = row.insertCell(0);
					cell.setAttribute("align", "left");
					cell.setAttribute("valign", "middle");
					if( i == 0 ){
						cell.appendChild(prototypeElement);
					}else{
						cell.innerHTML = prevhtml;
					}
				}
				ElementPare.innerHTML = "";
				ElementPare.appendChild(table);
			}catch(e){}
		}
		
	},
	
	
	assembleDataArray : function( nodesJson ) {						//把nodes 组装成一个2位数组
		
		var temp01 = new Array(),temp02 = new Array();
		for( var i in nodesJson){
			temp01[i] = nodesJson[i];
			temp02[i] = nodesJson[i];
		}
									
		var twoDimensionalArray = new Array();						//[{},{},{}]
		
		for( var i=0; i<temp01.length; i++) {
			
			var insetArray = new Array();							//小组数据存储
			for( var j=0; j<temp02.length; j++) {
				if( temp01[i]["parentCode"][0] == temp02[j]["parentCode"][0] ){
					insetArray.push(temp02[j]);
				}
			}
			
			var jugCode = insetArray[0]["code"];
			var isAdd = false;
			
			if(twoDimensionalArray.length > 0){
				for( var k=0; k<twoDimensionalArray.length; k++) {
					if( jugCode == twoDimensionalArray[k][0]["code"] ){
						isAdd = true;
						break;
					}
				}
			}
			
			if( !isAdd ) {
				twoDimensionalArray.push(insetArray);
			}
		}
		return twoDimensionalArray;
	},
	
	//获取对象的子集   在nodes中数据(返回object)
	findDataObject : function( code , data ) {				//传入参数为当前的 code, 数据集类型
		//if( typeof(code) != "string") return;
		for( var j=0; j<data.length; j++) {
			if( code  ==  data[j]["code"]) {
				return data[j];
				break;
			}
		}
	},
	
	//获取对象的子集   在nodes中的数据位置(返回int)
	findObjectIndex : function( code , data ) {				//传入参数为当前的 code, 数据集类型
		//if( typeof(code) != "string") return;
		for( var j=0; j<data.length; j++) {
			if( code  ==  data[j]["code"]) {
				return j;
				break;
			}
		}
	},
	
	
	//创建 包含子集 的table 
	createInsertChildTable : function(childData) {
		var len = childData.length;
		var links = childData[0]["links"];
		if( len < 1 ) return;
		var div = document.createElement("div");
		if( len == 1 ) {
			div.setAttribute("class" , "singleDiv02 s");
		} else {
			div.setAttribute("class" , "singleDiv");
		}
		
		var table = document.createElement("table");
		table.setAttribute("border", 0);
		table.setAttribute("cellspacing", 0);
		table.setAttribute("cellpadding", 0);
		table.setAttribute("class", "singleTab");
		
		if( len == 1) {
			var row = table.insertRow();
			var cell = row.insertCell(0);
			cell.setAttribute("class", "e");
			
			var code = childData[0]["code"];							//返回当前子集数据的  属性字段
			var topname = childData[0]["topname"];
			var name = childData[0]["name"];
			var src = this.typeImgSwitch(childData[0]["typeImg"]);
			var parentCode = childData[0]["parentCode"];
			var childrenCode = childData[0]["childrenCode"];
			var status = this.statusSwitch(childData[0]["status"]);
			
			cell.appendChild(this.createIncludeSingleDataTable(code, topname, name, src, parentCode, childrenCode, status));
		} else {
			
			var A = document.createElement("a");						//创建圆形节点
			A.setAttribute("class", "link");
			A.setAttribute("id", links);
			div.appendChild(A);
			
			for ( var i = 0; i < len ; i++ ) {
				var row = table.insertRow(i);
				var cell = row.insertCell(0);
				cell.setAttribute("align", "left");
				cell.setAttribute("valign", "middle");
				cell.setAttribute("class", "d");
				
				if( i == 0 ){
					var LeftDIV = document.createElement("div");						//创建线节点
					LeftDIV.setAttribute("class", "stringLine left");
					var RIGHTDIV = document.createElement("div");						
					RIGHTDIV.setAttribute("class", "stringLine right");
					cell.appendChild(LeftDIV);
					cell.appendChild(RIGHTDIV);
				}
				
				if( i == len-1 ){
					var LeftDIV = document.createElement("div");						//创建线节点
					LeftDIV.setAttribute("class", "stringLine01 left");
					var RIGHTDIV = document.createElement("div");						
					RIGHTDIV.setAttribute("class", "stringLine01 right");
					cell.appendChild(LeftDIV);
					cell.appendChild(RIGHTDIV);
				}
				if( i!=0 && i!=len-1){
					cell.setAttribute("style", "border-left:1px solid #000;border-right:1px solid #000;");
				}
				
				var code = childData[i]["code"];							//返回当前子集数据的  属性字段
				var topname = childData[i]["topname"];
				var name = childData[i]["name"];
				var src = this.typeImgSwitch(childData[i]["typeImg"]);
				var parentCode = childData[i]["parentCode"];
				var childrenCode = childData[i]["childrenCode"];
				var status = this.statusSwitch(childData[i]["status"]);

				cell.appendChild(this.createIncludeSingleDataTable(code, topname, name, src, parentCode, childrenCode, status));
			}
		}
		div.appendChild(table);
		return div;
	},
	
	
	findSingleGroups : function() {							//找出需要 画线  的节点
		
		var classGroups = getElementsByClassName( "stringLine" );
		
		if(classGroups.length < 1) return;
		
		for( var i=0; i<classGroups.length; i++ ) {
			var parentHeight = classGroups[i].parentNode.offsetHeight;
			classGroups[i].style.height = parentHeight*0.5+"px";
		}
		
		var classGroups01 = getElementsByClassName( "stringLine01" );
		
		if(classGroups01.length < 1) return;
		
		for( var i=0; i<classGroups01.length; i++ ) {
			var parentHeight01 = classGroups01[i].parentNode.offsetHeight;
			classGroups01[i].style.height = parentHeight01*0.5+"px";	
		}
		
	},
	
	
	//*******************给所有的节点 包括links节点添加click事件
	clickEvent : function(){
		var $this = this;
		var handlerElements = getElementsByClassName("userTip");
		for( var i=0,len=handlerElements.length; i<len; i++ ){
			
			if( this.options.isEdit && handlerElements[i].id !="start" && handlerElements[i].id !="end" ){
				
				oEventUtil.RemoveEventHandler( handlerElements[i], "click" );
				oEventUtil.AddEventHandler( handlerElements[i], "click", $this.clickCallBackOperation, [$this,handlerElements[i]] );  //节点操作
				
			}else if( !this.options.isEdit && handlerElements[i].id !="start" && handlerElements[i].id !="end" ){
				
				oEventUtil.RemoveEventHandler( handlerElements[i], "click" );
				oEventUtil.AddEventHandler( handlerElements[i], "click", $this.clickCallBackQuery, [$this,handlerElements[i]] );  //显示属性信息
				
			}
		}
		if( this.options.isEdit ) {
			var linkElements = getElementsByClassName("link");
			for( var j=0,len=linkElements.length; j<len; j++ ){
				
				oEventUtil.RemoveEventHandler( linkElements[j], "click" );
				oEventUtil.AddEventHandler( linkElements[j], "click", $this.clickCallBackOperation, [$this,linkElements[j]] );  //节点操作
				
			}
		}
	},
	
	
	clickCallBackOperation : function(event){									//点击事件返回节点操作方法
		var clickObject = this[1];						//htmlElement 对象
		var prototypeObject = this[0];					//当前path对象
		
		prototypeObject.options.clickAfterFun["editFun"].call(clickObject);
	},
	
	
	clickCallBackQuery : function(){						//点击事件返回节点属性方法
		var clickObject = this[1];
		var prototypeObject = this[0];
		
		prototypeObject.options.clickAfterFun["queryFun"].call(clickObject);
	},
	
	
	//操作类型 返回方法
	addNode : function( code , dataJson , isParallel){			//isParallel是否并联
		if( dataJson == null || typeof(code) != "string") return;
		var temp = dataJson;
		var len = temp.length;
		if( len==1 ) isParallel=false;
		
		if(	String(code).match(new RegExp("link")) == null ){					//点击节点是普通节点
			
			var o = this.findDataObject( code, nodes );
			var index = this.findObjectIndex( code, nodes );
			var handler_childrenCode = o["childrenCode"];
			
		}else{																	//点击节点是link节点
			
			var o = this.findDataObject( code, links );
			var pareCodes = o["parentCode"][o["parentCode"].length-1];
			var index = this.findObjectIndex( pareCodes, nodes );
			var handler_childrenCode = o["childrenCode"];
						
		}
		
		if( len > 1 && isParallel ){
			var linkMaxLen = this.linksLastNumber( links );			//  为新的link节点 定义最大的id 值
			linkMaxLen++;								
			var childArray = new Array();
			childArray.push("link"+linkMaxLen);
		}
		var thisArray = new Array();
		thisArray.push(code);
		
		var codeArray = new Array();			//汇集当前新添加节点所有code
		for( var i=0; i<temp.length; i++ ){
			codeArray.push(temp[i]["code"]);
		}
		
		//设置原有节点
		if( isParallel ) {
			o["childrenCode"] = codeArray;
		}else{
			o["childrenCode"] = new Array( temp[0]["code"] );
		}
		if( handler_childrenCode[0] != "end" ){
				if( handler_childrenCode.length > 1 ){
					for( var i=0; i<handler_childrenCode.length; i++ ){
						this.findDataObject( handler_childrenCode[i], nodes )["parentCode"] = codeArray;
						if( len > 1 ){
							if( isParallel ) {
								this.findDataObject( handler_childrenCode[i], nodes )["parentCode"] = childArray;
							}else{
								this.findDataObject( handler_childrenCode[i], nodes )["parentCode"] = new Array( temp[len-1]["code"] );
							}
						}
					}
				}else{
					if( handler_childrenCode[0].match(new RegExp("link")) == null ){

						this.findDataObject( handler_childrenCode[0], nodes )["parentCode"] = codeArray;
						if( len > 1 ){
							if( isParallel ) {
								this.findDataObject( handler_childrenCode[0], nodes )["parentCode"] = childArray;
							}else{
								this.findDataObject( handler_childrenCode[0], nodes )["parentCode"] = new Array( temp[len-1]["code"] );
							}
						}

					}else{
						var linkChild = this.findDataObject( handler_childrenCode[0], links );
						var gi = new Array();
						gi = gi.concat(linkChild["parentCode"]);
						//console.log(gi);
						for( var i=0; i<gi.length; i++ ){
							if( gi[i] == code ){
								if( len > 1 ){
									if( isParallel ) {
										gi[i] = childArray[0];
									}else{
										gi[i] = temp[len-1]["code"];
									}
								}else if( len==1 ){
									gi[i] = codeArray[0];
								}
								break;
							}
						}
						linkChild["parentCode"] = gi;
					}
				}
				console.log(links);
		}
		
		//新添加的节点组装
		if( len > 1 && isParallel ){
			var newlink = [{code:"link"+linkMaxLen, parentCode:codeArray, childrenCode:handler_childrenCode}];	//创建新的link节点
			links.push(newlink[0]);
		}
		
		index++;
		for( var j=0; j<len; j++ ){						//返回新的数据type
			temp[j]["parentCode"] = thisArray;
			temp[j]["childrenCode"] = handler_childrenCode;
			temp[j]["links"] = "";
			if( len > 1 ){
				if( isParallel ) {
					temp[j]["childrenCode"] = childArray;
					temp[j]["links"] = "link"+linkMaxLen;
				}else{
					if( j==0 ){
						temp[j]["parentCode"] = thisArray;
						temp[j]["childrenCode"] = new Array(temp[j+1]["code"]);
					}else if( j<len-1 ) {
						temp[j]["parentCode"] = new Array(temp[j-1]["code"]);
						temp[j]["childrenCode"] = new Array(temp[j+1]["code"]);
					}else if( j=len-1 ) {
						temp[j]["parentCode"] = new Array(temp[j-1]["code"]);
						temp[j]["childrenCode"] = handler_childrenCode;
					}
				}
			}
			nodes.splice( Number(index)+j, 0 , temp[j] );
		}
		
		this.render(nodes);
	},
	
	deleteNode : function( code ){
		
		var index = this.findObjectIndex( code, nodes );							//点击对象在nodes 中位置
		
		var thisParentObj = this.findDataObject( code , nodes )["parentCode"];		//点击对象 的父级对象
		var thisParentCode = thisParentObj[0];	//点击对象 的父级对象ID
		
		var thisChildrenCode = this.findDataObject( code , nodes )["childrenCode"]; //点击对象 的 子集
		
		var handlers = this.getDefultJsonGroup( thisParentCode );  //点击对象  同级节点长度
		var handlersLength = handlers.length;
		
		//设置当前节点的父节点
		if( thisParentCode != "start" ){
				if( thisParentCode.match(new RegExp("link")) == null ){
					pare = this.findDataObject( thisParentCode , nodes );
				}else{
					var pare = this.findDataObject( thisParentCode , links );
				}
				var d = pare["childrenCode"];
				for( var i=0; i<d.length; i++ ){
					if( d[i] == code ){
						d.splice( i , 1 );
						break;
					}
				}
				
				if( handlersLength > 1 ){
					if( thisChildrenCode[0].match(new RegExp("link")) == null ){
						for( var j=0; j<thisChildrenCode.length; j++ ) {
							d.push( thisChildrenCode[j] );
						}
					}
					pare["childrenCode"] = d;
					
				}else if( handlersLength == 1 ){
					pare["childrenCode"] = thisChildrenCode;
				}
				//console.log( pare["childrenCode"] );
			
		}
		//设置当前节点的子节点
		if( thisChildrenCode[0] != "end" ){
			
				if( thisChildrenCode[0].match(new RegExp("link")) == null ){
					
					for( var i=0; i<thisChildrenCode.length; i++ ){
						this.findDataObject( thisChildrenCode[i] , nodes )["parentCode"] = thisParentObj;
					}
					
				}else{
					
					var childLinkObj = this.findDataObject( thisChildrenCode[0] , links );
					var b = childLinkObj["parentCode"];

					for( var i=0; i<b.length; i++ ){
						
						if( b[i] == code ){
							b.splice( i , 1 );
							if( handlersLength == 1 ){
								b.splice( i , 1 , thisParentCode);
							}
							break;
						}
						
					}
					childLinkObj["childrenCode"] = b;
					//console.log(childLinkObj["childrenCode"]);
					if( handlersLength == 2 ){
						
						for( var k=0; k<handlers.length; k++){
							if( code != handlers[k] ){
								handlers[k]["childrenCode"] = childLinkObj["childrenCode"];
								this.removeLinksCode( thisChildrenCode[0] );
								break;
							}
						}
						
					}
				}
				
		}
		
		//删除当前点击的节点
		nodes.splice( index , 1 );
		this.render(nodes);
	},
	
	setNode : function( code , param){
		console.log("...");
	},
	
	
	linksLastNumber : function( linksData ){
		var len = linksData.length;
		if( len > 0 ){
			var last = linksData[len-1];
			var lastCode = last["code"];
			return Number( lastCode.replace( /link/g, "" ) );
		}else{
			return 1;
		}
	},
	
	getDefultJsonGroup : function( parentCode ){
		var temp = [];
		for( var i=0; i<nodes.length; i++ ){
			if( nodes[i]["parentCode"][0] == parentCode){
				temp.push(nodes[i]);
			}
		}
		return temp;
	},
	
	removeLinksCode : function( code ) {
		for( var i=0; i<links.length; i++ ){
			if( code == links[i]["code"] ){
				links.splice( i,1 );
				break;
			}
		}
		
	}
	
	
};

