// JavaScript Document


function inputFocus(obj){
	var font = obj.getAttribute("data-fontID");
	var fontObj = document.getElementById(font);
	fontObj.style.display="none";
}
function inputBlur(obj){
	var font = obj.getAttribute("data-fontID");
	var fontObj = document.getElementById(font);
	if(obj.value == "" || obj.value == null){
		fontObj.style.display="block";
	}
}

var companySelect;

window.onload = function(){
	
	var company = document.getElementById("CorpList");
	
	companySelect = new select();
	companySelect.SetOptions({
		width: 271,
		handler:company,
		isClick:false,
		tipTitle:"您未输入用户名、密码！请先正确输入,再选择！"
	});
	companySelect.render();
	
	//此方法是加载数据后重新注册事件
	//companySelect.clickFreed();
}