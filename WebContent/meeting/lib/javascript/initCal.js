// JavaScript Document

$(function(){
	
	/**
		-------------
		形成日期会议
		-------------
	**/
	$("#dateTable").initCal({thisDate:"2014-07-01"});
	
});

$.extend($.fn, {
	initCal: function() {
		initCal.init.apply(this, arguments);
	}
});

var initCal = $.extend({}, {

	init: function(options, node){
		var optionInt = $.extend(true,{
			thisDate:"2014-07-01"
		},options||{});

		if(!node) node = this;
		var $body = $('body');
		
		
		var defultDate = initCal.formatDate(optionInt.thisDate);
		
		var nextMonth = Number(defultDate.getMonth()) + 1;
		if(nextMonth > 11){
			nextMonth = 0;
		}

		var title = initCal.getTitleHtml();
		var content = initCal.createTrContent(optionInt);

		$(node).html(title+content);
		
		//计算式
		var minutes = 1000 * 60;
		var hours = minutes * 60;
		var days = hours * 24;
		var years = days * 365;
				
		return this;
	},
	
	//+---------------------------------------------------  
	//| 解析数据 获取当前日期的年份
	//+--------------------------------------------------- 
	getDateYear:function(DateTime){
		var Year = DateTime.substring(0,DateTime.indexOf ('-')); 
		return  Year;
	},
	
	//+---------------------------------------------------  
	//| 解析数据 获取有数据的具体日期
	//+--------------------------------------------------- 
	getDateMonth:function(DateTime){
		var Month = DateTime.substring(5,DateTime.lastIndexOf ('-')); 
		return  Month;
	},
	
	
	//+---------------------------------------------------  
	//| 创建表格内部的TD
	//+--------------------------------------------------- 
	createTrContent:function(optionInt){
		var myDate = new Date();
		var myDay = myDate.getDate();
		var myYear = myDate.getYear();
		var myMonth = myDate.getMonth();
		
		var oneYear = initCal.getDateYear(optionInt.thisDate);
		var oneMonth = initCal.getDateMonth(optionInt.thisDate);
		
		var num = 0;//记录上个月开始日期
		var thisNum = 0; //几率当月开始日期
		var isThisMonth = false; //判断是否是当前月份
		var prevTotalDay = 0;    //几率上一个月有多少天
		
		var html = "";
		
		//循环一个月的TR 数
		for(var i=0; i<6 ; i++){
			
			html += "<tr>";
			
			//循环星期
			for(var j=0; j<7 ; j++){
				
				//判断是否是周末.............
				if(j==0 || j==6){
					html +="<td class=\"gray\">";
				}else{
					html +="<td>";
				}
				
				//判断当月从星期几开始铺数据
				if(i == 0){
					//星级数     计算出上一个月的排布
					var day = initCal.prevMonthGetDay(optionInt.thisDate);

					if(day != 0){
						if(j == 0){
							prevTotalDay = initCal.prevMonthDate(optionInt.thisDate, true);
							num = prevTotalDay - day;
						}
					}
					
					num++;
					var recordMonth = oneMonth, recordYear = oneYear;
					if(!isThisMonth){
						if(oneMonth == 1){
							recordMonth = 12;
							recordYear = oneYear-1;
						}else{
							recordMonth = oneMonth - 1;
							recordYear = oneYear;
						}
					}
					//在第一行   从当月月份开始计算
					if(num > prevTotalDay){
						isThisMonth = true;
						num =  1;
						recordMonth = oneMonth;
						recordYear = oneYear;
					}
					var time = recordYear+"-"+recordMonth+"-"+num;
					//判断是否是今天
					var isToday = false;
					if(Number(recordMonth) == Number(myMonth)+1 && Number(num) == Number(myDay) ){
						isToday = true;
					}
					//填充数据在表格
					html += initCal.getHtml(num, '', isThisMonth, time, isToday);	
									
				}else{
					
					var MaxMonth = initCal.prevMonthDate(optionInt.thisDate, false);
					
					num++;
					var recordMonth = Number(oneMonth) + 1, recordYear = oneYear;
					if(isThisMonth){
						if(oneMonth == 12){
							recordMonth = 1;
							recordYear = Number(oneYear)+1;
						}else{
							recordMonth = Number(oneMonth);
							recordYear = oneYear;
						}
					}
					if(num > MaxMonth){			//   计算出下一个月的排布
						isThisMonth = false;
						num =  1;
						recordYear = oneYear;
						recordMonth = Number(oneMonth) + 1;
					}
					var time = recordYear+"-"+recordMonth+"-"+num;
					//判断是否是今天
					var isToday = false;		
					if(Number(recordMonth) == Number(myMonth)+1 && Number(num) == Number(myDay) ){
						isToday = true;
					}
					
					html += initCal.getHtml(num, '', isThisMonth, time, isToday);
				}
				
				html +="</td>";
			}
			html += "</tr>";
		}
		
		return html;
	},
	
	//+---------------------------------------------------  
	//| 标注表格内部DIV  会议内容
	//+--------------------------------------------------- 
	getHtml:function(day, contentTitle, isThisMonth, date, isToday){
		date = this.stingToDate(date);
		var html = "";
		html += "<div class=\"meet-tab-x\" data-thisdate=\""+date+"\">";
		if(isThisMonth){
        	html += "<div class=\"meet-tab-b\">";
		}else{
			html += "<div class=\"meet-tab-b meet-gray\">";
		}
		if(isToday){
			html += "<h5><i style=\"color:red; font-style:normal;\">(今天)</i>&nbsp;"+day+"日</h5>";
		}else{
			html += "<h5>"+day+"日</h5>";
		}
		html += "<div class=\"meet-tab-con\">"+contentTitle+"</div>";	
        html += "</div>";
        html += "</div>";
		return html;
	},
	
	//+---------------------------------------------------  
	//| 标注表格表头的星期
	//+---------------------------------------------------  
	getTitleHtml:function(){
		var html = "";              
		html += "<tr>";
		html += "<th>周日</th>";
        html += "<th>周一</th>";
        html += "<th>周二</th>";
        html += "<th>周三</th>";
        html += "<th>周四</th>";
        html += "<th>周五</th>";	
        html += "<th>周六</th>";
        html += "</tr>";
		return html;
	},
	
	
	//格式化日期时间
	formatDate : function(date){
		//var s = "2009-06-22 14:22:53";
		var dt = Date.parse(date.replace(/-/g,"/"));
		var a = new Date(dt);
		return a;
	},
	
	
	//+---------------------------------------------------  
	//| 求两个时间的天数差 日期格式为 YYYY-MM-dd   
	//+---------------------------------------------------  
	daysBetween : function(DateOne,DateTwo) {   
		var OneMonth = DateOne.substring(5,DateOne.lastIndexOf ('-'));  
		var OneDay = DateOne.substring(DateOne.length,DateOne.lastIndexOf ('-')+1);  
		var OneYear = DateOne.substring(0,DateOne.indexOf ('-'));  
	  
		var TwoMonth = DateTwo.substring(5,DateTwo.lastIndexOf ('-'));  
		var TwoDay = DateTwo.substring(DateTwo.length,DateTwo.lastIndexOf ('-')+1);  
		var TwoYear = DateTwo.substring(0,DateTwo.indexOf ('-'));  
	  
		var cha=((Date.parse(OneMonth+'/'+OneDay+'/'+OneYear)- Date.parse(TwoMonth+'/'+TwoDay+'/'+TwoYear))/86400000);   
		return Math.abs(cha);  
	},
	
	
	//---------------------------------------------------  
	// 判断闰年  
	//---------------------------------------------------  
	isLeapYear : function() {   
		return (0==this.getYear()%4&&((this.getYear()%100!=0)||(this.getYear()%400==0)));   
	},
	
	
	//+---------------------------------------------------  
	//| 取得当前日期所在月的最大天数  
	//+---------------------------------------------------  
	stingToDate : function(myDate){   
		var date01 = new Date(myDate);
		return date01.Format('yyyy-MM-dd');
	} ,
	
	
	//+---------------------------------------------------  
	//| 获取上一个月的剩余天数  或者   获取当月月的剩余天数  
	//+---------------------------------------------------  
	prevMonthDate : function(DateOne, isPrev){
		var OneMonth = DateOne.substring(5,DateOne.lastIndexOf ('-'));  
		var OneDay = DateOne.substring(DateOne.length,DateOne.lastIndexOf ('-')+1);  
		var OneYear = DateOne.substring(0,DateOne.indexOf ('-')); 
		 
		var result;
		if(isPrev){
			OneMonth = OneMonth-1;
			if(OneMonth == 0){
				OneYear = OneYear - 1;
				OneMonth = 12;
			}
			if(OneMonth < 10){
				OneMonth = "0"+OneMonth;
			}
			result = OneYear+"-"+OneMonth+"-"+OneDay;
		}else{
			OneMonth = Number(OneMonth)+1;
			if(OneMonth > 12){
				OneYear = Number(OneYear) + 1;
				OneMonth = 1;
			}
			if(OneMonth < 10){
				OneMonth = "0"+OneMonth;
			}
			result = OneYear+"-"+OneMonth+"-"+OneDay;
		}
		//计算出上一个月的天数
		var cha = initCal.daysBetween(result, DateOne);
		return cha;		
	},
	
	
	//+---------------------------------------------------  
	//| 获取上一个月月末  星期几  
	//+---------------------------------------------------  
	prevMonthGetDay : function(DateOne){
		var myDate = new Date(DateOne);
		var myDay = myDate.getDay();
		return myDay;
	}  

	
	
})

Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}