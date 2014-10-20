// JavaScript Document
//固定搜索过滤条
$.extend($.fn, {
	fixScroll: function() {
		fixScroll.init.apply(this, arguments);
	}
});

var fixScroll = $.extend({}, {

	init: function(options, node){
		var fixInt = $.extend(true,{
			defultTop:0,
			center:true
		},options||{});
		if(!node) node = this;
		
		
		BodyWidth = $('body').width();
		scrollData = 0;
		
		
		return $(node).each(function(){
			
			var $this = $(this);
			var IntTop, IntLeft, IntWidth, scrollData;
			IntTop = $this.offset().top, IntLeft = $this.offset().left;
			IntWidth = $this.width();
			
			var pare = $this.parent();
			//console.log(pare);
			
			IntLeft = pare.offset().left;
			$(window).scroll(function(){
				scrollData = $(this).scrollTop();
				fixScroll.setFixed(fixInt, $this, scrollData, IntLeft, IntTop, IntWidth);		
			})
			
			$(window).resize(function(){
				BodyWidth = $('body').width();
				
				var newIntLeft = pare.offset().left;
				
				if( $(window).scrollTop() >= IntWidth){
					if($.support.msie && ($.support.version == "6.0") && !$.support.style){
						$this.css({'left':(BodyWidth-IntWidth)*.5-5});
						if(!fixInt.center){
							$this.css({'left':newIntLeft});
						}
					}else{
						$this.css({'left':(BodyWidth-IntWidth)*.5-5});
						if(!fixInt.center){
							$this.css({'left':newIntLeft});
						}
					}
				}
			});
			
		});
		
	},
	
	setFixed : function(fixInt, node, scrollTop, left, top, width){
		if(scrollTop >= top){
		    if($.support.msie && ($.support.version == "6.0") && !$.support.style){
				console.log('a');
			    $(node).css({'position':'absolute','top':top, 'left':(BodyWidth-width)*.5-5, 'z-index':111});
				if(!fixInt.center){
					$(node).css({'left':left});
				}
				if($(node).attr("data-border") == "true"){
					$(node).css({'border':' 2px solid #e4e4e4'});
				}
			}else{
			    $(node).css({'position':'fixed','top':fixInt.defultTop,'left':(BodyWidth-width)*.5-5,'z-index':111,'width':width,'border-bottom':'0px'});
				if(!fixInt.center){
					$(node).css({'left':left});
				}
				if($(node).attr("data-border") == "true"){
					$(node).css({'border':' 2px solid #e4e4e4'});
				}
			}
		}else{
			
		    $(node).css({'position':'relative','left':'0','top':'0','width':'auto','border':'0px', 'border-left':'1px dashed #dcdcdc'});
		}
	}
		
})
