function uploadPBAttach(){
	$("#attFile1").click();
}

function uploadTSAttach(){
	$("#attFile2").click();
}

function uploadSCAttach1(){
	$("#attFile3").click();
}

function uploadSCAttach2(){
	$("#attFile4").click();
}

function uploadSCAttach3(){
	$("#attFile5").click();
}

function uploadCRAttach(){
	$("#attFile6").click();
}

function uploadMRAttach(){
	$("#attFile7").click();
}

function uploadSRAttach1(){
	$("#attFile8").click();
}

function uploadSRAttach2(){
	$("#attFile9").click();
}

function uploadCCAttach1(){
	$("#attFile10").click();
}

function uploadCCAttach2(){
	$("#attFile11").click();
}

function uploadIIAttach1(){
	$("#attFile12").click();
}

function uploadIIAttach2(){
	$("#attFile13").click();
}

function uploadIIAttach3(){
	$("#attFile14").click();
}

function uploadCL(){
	$("#attFile15").click();
}

$(document).ready(function () {
	//各环节上传按钮的事件处理
	$("#attFile1").change(function() {
		var fileInfo = $("#attFile1").val().split('.');
        var fileType=fileInfo[fileInfo.length-1].toLowerCase();
        if(fileType =='doc' || fileType=='docx'){
        	$("#attForm1").ajaxSubmit({
                url: "../attachment/uploadAttach?moduleName=business&fileName=attFile1",
                type: "post",
                dataType: "json",
                resetForm: "true",
                beforeSubmit: function() {
                	$("#attUpload1").hide();
                },
                success: function(data) {
                    if(data.Result){
                    	alert(data.Msg);
                    	$("#attUpload1").show();
						var html='<li><t>'+data.FileName+'</t><a class="process-icons down" title="下载申请表"></a><a class="process-icons delate" title="删除申请表"></a></li>';
						$("#projectBase_attach").html(html);
						$("#pb_AttachURL").val(data.FileURL);
                    }else{
                    	alert(data.Msg);
                    	$("#attUpload1").show();
                    }
                },
                error: function(jqXHR, errorMsg, errorThrown) {
                    alert(errorMsg);
                }
            });	
        }else{
        	alert("请选择正确的文件格式！");
        	return;
        }
	});
	
	$("#attFile2").change(function() {
		var fileInfo = $("#attFile2").val().split('.');
        var fileType=fileInfo[fileInfo.length-1].toLowerCase();
        if(fileType =='doc' || fileType=='docx'){
        	$("#attForm2").ajaxSubmit({
                url: "../attachment/uploadAttach?moduleName=business&fileName=attFile2",
                type: "post",
                dataType: "json",
                resetForm: "true",
                beforeSubmit: function() {
                	$("#attUpload2").hide();
                },
                success: function(data) {
                    if(data.Result){
                    	alert(data.Msg);
                    	$("#attUpload2").show();
						var html='<li><t>'+data.FileName+'</t><a class="process-icons down" title="下载估算表"></a><a class="process-icons delate" title="删除估算表"></a></li>';
						$("#techScheme_attach").html(html);
						$("#ts_AttachURL").val(data.FileURL);
                    }else{
                    	alert(data.Msg);
                    	$("#attUpload2").show();
                    }
                },
                error: function(jqXHR, errorMsg, errorThrown) {
                    alert(errorMsg);
                }
            });	
        }else{
        	alert("请选择正确的文件格式！");
        	return;
        }
	});
	
	$("#attFile3").change(function() {
		var fileInfo = $("#attFile3").val().split('.');
        var fileType=fileInfo[fileInfo.length-1].toLowerCase();
        if(fileType =='doc' || fileType=='docx'){
        	$("#attForm3").ajaxSubmit({
                url: "../attachment/uploadAttach?moduleName=business&fileName=attFile3",
                type: "post",
                dataType: "json",
                resetForm: "true",
                beforeSubmit: function() {
                	$("#attUpload3").hide();
                },
                success: function(data) {
                    if(data.Result){
                    	alert(data.Msg);
                    	$("#attUpload3").show();
						var html='<li><t>'+data.FileName+'</t><a class="process-icons down" title="下载评估表"></a><a class="process-icons delate" title="删除评估表"></a></li>';
						$("#schemeConfirm_attach1").html(html);
						$("#sc_AttachURL1").val(data.FileURL);
                    }else{
                    	alert(data.Msg);
                    	$("#attUpload3").show();
                    }
                },
                error: function(jqXHR, errorMsg, errorThrown) {
                    alert(errorMsg);
                }
            });	
        }else{
        	alert("请选择正确的文件格式！");
        	return;
        }
	});
	
	$("#attFile4").change(function() {
		var fileInfo = $("#attFile4").val().split('.');
        var fileType=fileInfo[fileInfo.length-1].toLowerCase();
        if(fileType =='ppt' || fileType=='pptx'){
        	$("#attForm4").ajaxSubmit({
                url: "../attachment/uploadAttach?moduleName=business&fileName=attFile4",
                type: "post",
                dataType: "json",
                resetForm: "true",
                beforeSubmit: function() {
                	$("#attUpload4").hide();
                },
                success: function(data) {
                    if(data.Result){
                    	alert(data.Msg);
                    	$("#attUpload4").show();
						var html='<li><t>'+data.FileName+'</t><a class="process-icons down" title="下载演示PPT"></a><a class="process-icons delate" title="删除演示PPT"></a></li>';
						$("#schemeConfirm_attach2").html(html);
						$("#sc_AttachURL2").val(data.FileURL);
                    }else{
                    	alert(data.Msg);
                    	$("#attUpload4").show();
                    }
                },
                error: function(jqXHR, errorMsg, errorThrown) {
                    alert(errorMsg);
                }
            });	
        }else{
        	alert("请选择正确的文件格式！");
        	return;
        }
	});
	
	$("#attFile5").change(function() {
		var fileInfo = $("#attFile5").val().split('.');
        var fileType=fileInfo[fileInfo.length-1].toLowerCase();
        if(fileType =='doc' || fileType=='docx'){
        	$("#attForm5").ajaxSubmit({
                url: "../attachment/uploadAttach?moduleName=business&fileName=attFile5",
                type: "post",
                dataType: "json",
                resetForm: "true",
                beforeSubmit: function() {
                	$("#attUpload5").hide();
                },
                success: function(data) {
                    if(data.Result){
                    	alert(data.Msg);
                    	$("#attUpload5").show();
						var html='<li><t>'+data.FileName+'</t><a class="process-icons down" title="下载演示WORD"></a><a class="process-icons delate" title="删除演示WORD"></a></li>';
						$("#schemeConfirm_attach3").html(html);
						$("#sc_AttachURL3").val(data.FileURL);
                    }else{
                    	alert(data.Msg);
                    	$("#attUpload5").show();
                    }
                },
                error: function(jqXHR, errorMsg, errorThrown) {
                    alert(errorMsg);
                }
            });	
        }else{
        	alert("请选择正确的文件格式！");
        	return;
        }
	});
	
	$("#attFile6").change(function() {
		var fileInfo = $("#attFile6").val().split('.');
        var fileType=fileInfo[fileInfo.length-1].toLowerCase();
        if(fileType =='doc' || fileType=='docx'){
        	$("#attForm6").ajaxSubmit({
                url: "../attachment/uploadAttach?moduleName=business&fileName=attFile6",
                type: "post",
                dataType: "json",
                resetForm: "true",
                beforeSubmit: function() {
                	$("#attUpload6").hide();
                },
                success: function(data) {
                    if(data.Result){
                    	alert(data.Msg);
                    	$("#attUpload6").show();
						var html='<li><t>'+data.FileName+'</t><a class="process-icons down" title="下载会议纪要"></a><a class="process-icons delate" title="删除会议纪要"></a></li>';
						$("#commercialReview_attach").html(html);
						$("#cr_AttachURL").val(data.FileURL);
                    }else{
                    	alert(data.Msg);
                    	$("#attUpload6").show();
                    }
                },
                error: function(jqXHR, errorMsg, errorThrown) {
                    alert(errorMsg);
                }
            });	
        }else{
        	alert("请选择正确的文件格式！");
        	return;
        }
	});
	
	$("#attFile7").change(function() {
		var fileInfo = $("#attFile7").val().split('.');
        var fileType=fileInfo[fileInfo.length-1].toLowerCase();
        if(fileType =='doc' || fileType=='docx'){
        	$("#attForm7").ajaxSubmit({
                url: "../attachment/uploadAttach?moduleName=business&fileName=attFile7",
                type: "post",
                dataType: "json",
                resetForm: "true",
                beforeSubmit: function() {
                	$("#attUpload7").hide();
                },
                success: function(data) {
                    if(data.Result){
                    	alert(data.Msg);
                    	$("#attUpload7").show();
						var html='<li><t>'+data.FileName+'</t><a class="process-icons down" title="下载会议纪要"></a><a class="process-icons delate" title="删除会议纪要"></a></li>';
						$("#majorReview_attach").html(html);
						$("#mr_AttachURL").val(data.FileURL);
                    }else{
                    	alert(data.Msg);
                    	$("#attUpload7").show();
                    }
                },
                error: function(jqXHR, errorMsg, errorThrown) {
                    alert(errorMsg);
                }
            });	
        }else{
        	alert("请选择正确的文件格式！");
        	return;
        }
	});
	
	$("#attFile8").change(function() {
		var fileInfo = $("#attFile8").val().split('.');
        var fileType=fileInfo[fileInfo.length-1].toLowerCase();
        if(fileType =='doc' || fileType=='docx'){
        	$("#attForm8").ajaxSubmit({
                url: "../attachment/uploadAttach?moduleName=business&fileName=attFile8",
                type: "post",
                dataType: "json",
                resetForm: "true",
                beforeSubmit: function() {
                	$("#attUpload8").hide();
                },
                success: function(data) {
                    if(data.Result){
                    	alert(data.Msg);
                    	$("#attUpload8").show();
						var html='<li><t>'+data.FileName+'</t><a class="process-icons down" title="下载签报表"></a><a class="process-icons delate" title="删除签报表"></a></li>';
						$("#superiorReview_attach1").html(html);
						$("#sr_AttachURL1").val(data.FileURL);
                    }else{
                    	alert(data.Msg);
                    	$("#attUpload8").show();
                    }
                },
                error: function(jqXHR, errorMsg, errorThrown) {
                    alert(errorMsg);
                }
            });	
        }else{
        	alert("请选择正确的文件格式！");
        	return;
        }
	});
	
	$("#attFile9").change(function() {
		var fileInfo = $("#attFile9").val().split('.');
        var fileType=fileInfo[fileInfo.length-1].toLowerCase();
        if(fileType =='doc' || fileType=='docx'){
        	$("#attForm9").ajaxSubmit({
                url: "../attachment/uploadAttach?moduleName=business&fileName=attFile9",
                type: "post",
                dataType: "json",
                resetForm: "true",
                beforeSubmit: function() {
                	$("#attUpload9").hide();
                },
                success: function(data) {
                    if(data.Result){
                    	alert(data.Msg);
                    	$("#attUpload9").show();
						var html='<li><t>'+data.FileName+'</t><a class="process-icons down" title="下载会议纪要"></a><a class="process-icons delate" title="删除会议纪要"></a></li>';
						$("#superiorReview_attach2").html(html);
						$("#sr_AttachURL2").val(data.FileURL);
                    }else{
                    	alert(data.Msg);
                    	$("#attUpload9").show();
                    }
                },
                error: function(jqXHR, errorMsg, errorThrown) {
                    alert(errorMsg);
                }
            });	
        }else{
        	alert("请选择正确的文件格式！");
        	return;
        }
	});
	
	$("#attFile10").change(function() {
		var fileInfo = $("#attFile10").val().split('.');
        var fileType=fileInfo[fileInfo.length-1].toLowerCase();
        if(fileType =='pdf'){
        	$("#attForm10").ajaxSubmit({
                url: "../attachment/uploadAttach?moduleName=business&fileName=attFile10",
                type: "post",
                dataType: "json",
                resetForm: "true",
                beforeSubmit: function() {
                	$("#attUpload10").hide();
                },
                success: function(data) {
                    if(data.Result){
                    	alert(data.Msg);
                    	$("#attUpload10").show();
						var html='<li><t>'+data.FileName+'</t><a class="process-icons down" title="下载电子版"></a><a class="process-icons delate" title="删除电子版"></a></li>';
						$("#contractInfo_attach1").html(html);
						$("#cc_AttachURL1").val(data.FileURL);
                    }else{
                    	alert(data.Msg);
                    	$("#attUpload10").show();
                    }
                },
                error: function(jqXHR, errorMsg, errorThrown) {
                    alert(errorMsg);
                }
            });	
        }else{
        	alert("请选择正确的文件格式！");
        	return;
        }
	});
	
	$("#attFile11").change(function() {
		var fileInfo = $("#attFile11").val().split('.');
        var fileType=fileInfo[fileInfo.length-1].toLowerCase();
        if(fileType =='doc' || fileType=='docx'){
        	$("#attForm11").ajaxSubmit({
                url: "../attachment/uploadAttach?moduleName=business&fileName=attFile11",
                type: "post",
                dataType: "json",
                resetForm: "true",
                beforeSubmit: function() {
                	$("#attUpload11").hide();
                },
                success: function(data) {
                    if(data.Result){
                    	alert(data.Msg);
                    	$("#attUpload11").show();
						var html='<li><t>'+data.FileName+'</t><a class="process-icons down" title="下载扫描版"></a><a class="process-icons delate" title="删除扫描版"></a></li>';
						$("#contractInfo_attach2").html(html);
						$("#cc_AttachURL2").val(data.FileURL);
                    }else{
                    	alert(data.Msg);
                    	$("#attUpload11").show();
                    }
                },
                error: function(jqXHR, errorMsg, errorThrown) {
                    alert(errorMsg);
                }
            });	
        }else{
        	alert("请选择正确的文件格式！");
        	return;
        }
	});
	
	$("#attFile12").change(function() {
		var fileInfo = $("#attFile12").val().split('.');
        var fileType=fileInfo[fileInfo.length-1].toLowerCase();
        if(fileType =='doc' || fileType=='docx'){
        	$("#attForm12").ajaxSubmit({
                url: "../attachment/uploadAttach?moduleName=business&fileName=attFile12",
                type: "post",
                dataType: "json",
                resetForm: "true",
                beforeSubmit: function() {
                	$("#attUpload12").hide();
                },
                success: function(data) {
                    if(data.Result){
                    	alert(data.Msg);
                    	$("#attUpload12").show();
						var html='<li><t>'+data.FileName+'</t><a class="process-icons down" title="下载立项申请"></a><a class="process-icons delate" title="删除立项申请"></a></li>';
						$("#constructInfo_attach1").html(html);
						$("#ii_AttachURL1").val(data.FileURL);
                    }else{
                    	alert(data.Msg);
                    	$("#attUpload12").show();
                    }
                },
                error: function(jqXHR, errorMsg, errorThrown) {
                    alert(errorMsg);
                }
            });	
        }else{
        	alert("请选择正确的文件格式！");
        	return;
        }
	});
	
	$("#attFile13").change(function() {
		var fileInfo = $("#attFile13").val().split('.');
        var fileType=fileInfo[fileInfo.length-1].toLowerCase();
        if(fileType =='doc' || fileType=='docx'){
        	$("#attForm13").ajaxSubmit({
                url: "../attachment/uploadAttach?moduleName=business&fileName=attFile13",
                type: "post",
                dataType: "json",
                resetForm: "true",
                beforeSubmit: function() {
                	$("#attUpload13").hide();
                },
                success: function(data) {
                    if(data.Result){
                    	alert(data.Msg);
                    	$("#attUpload13").show();
						var html='<li><t>'+data.FileName+'</t><a class="process-icons down" title="下载立项批复"></a><a class="process-icons delate" title="删除立项批复"></a></li>';
						$("#constructInfo_attach2").html(html);
						$("#ii_AttachURL2").val(data.FileURL);
                    }else{
                    	alert(data.Msg);
                    	$("#attUpload13").show();
                    }
                },
                error: function(jqXHR, errorMsg, errorThrown) {
                    alert(errorMsg);
                }
            });	
        }else{
        	alert("请选择正确的文件格式！");
        	return;
        }
	});
	
	$("#attFile14").change(function() {
		var fileInfo = $("#attFile14").val().split('.');
        var fileType=fileInfo[fileInfo.length-1].toLowerCase();
        if(fileType =='doc' || fileType=='docx'){
        	$("#attForm14").ajaxSubmit({
                url: "../attachment/uploadAttach?moduleName=business&fileName=attFile14",
                type: "post",
                dataType: "json",
                resetForm: "true",
                beforeSubmit: function() {
                	$("#attUpload14").hide();
                },
                success: function(data) {
                    if(data.Result){
                    	alert(data.Msg);
                    	$("#attUpload14").show();
						var html='<li><t>'+data.FileName+'</t><a class="process-icons down" title="下载设计批复"></a><a class="process-icons delate" title="删除设计批复"></a></li>';
						$("#constructInfo_attach3").html(html);
						$("#ii_AttachURL3").val(data.FileURL);
                    }else{
                    	alert(data.Msg);
                    	$("#attUpload14").show();
                    }
                },
                error: function(jqXHR, errorMsg, errorThrown) {
                    alert(errorMsg);
                }
            });	
        }else{
        	alert("请选择正确的文件格式！");
        	return;
        }
	});
    
	$("#attFile15").change(function() {
		var fileInfo = $("#attFile15").val().split('.');
        var fileType=fileInfo[fileInfo.length-1].toLowerCase();
        if(fileType =='xls' || fileType=='xlsx'){
        	$("#attForm15").ajaxSubmit({
                url: "../business/importCode",
                type: "post",
                dataType: "json",
                resetForm: "true",
                beforeSubmit: function() {
                	$("#attUpload15").hide();
                },
                success: function(data) {
                    if(data.Result){
                    	alert(data.Msg);
                    	$("#attUpload15").show();
                    	//返回值写入展示表
                    	setCodeTable(data.CodeJson);
                    	$("#cl_CodeJson").val(JSON.stringify(data.CodeJson));                    	
                    }else{
                    	alert(data.Msg);
                    	$("#attUpload15").show();
                    }
                },
                error: function(jqXHR, errorMsg, errorThrown) {
                    alert(errorMsg);
                }
            });	
        }else{
        	alert("请选择正确的文件格式！");
        	return;
        }
	});
	
});