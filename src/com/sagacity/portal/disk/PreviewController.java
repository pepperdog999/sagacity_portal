package com.sagacity.portal.disk;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.ext.route.ControllerBind;
import com.sagacity.portal.comm.BaseController;
import com.sagacity.portal.comm.CommOpration;
import com.sagacity.portal.utility.PDFReader;
import com.sagacity.portal.utility.PPT2Png;
import com.sagacity.portal.utility.PropertiesFactoryHelper;
import com.sagacity.portal.utility.Word2Html;
import com.sagacity.portal.utility.Excel2Html;

@ControllerBind(controllerKey = "/preview")
public class PreviewController extends BaseController implements CommOpration{
	
	@Override
	public void index() {
	}
	
	public void getPDFImages(){
		boolean r = true;
		String rootPath = PropertiesFactoryHelper.getInstance().getConfig("resource.dir");
		String rootUrl  = PropertiesFactoryHelper.getInstance().getConfig("resource.url");
		Map<String,Object> result = new HashMap<String,Object>();
		List<Object> picFileList = new ArrayList<Object>();
		File file = new File(rootPath+getPara("folder"));
		if (!file.exists()) {
			r = false;
		}else{
	        File[] files = file.listFiles();
	        for (File listFile : files)
	        {
	            String fileName = listFile.getName();
	            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
	            String[] value = {"jpg","jpeg"};
				if(Arrays.<String>asList(value).contains(fileExt)){
					picFileList.add(rootUrl+getPara("folder")+listFile.getName());
				}
	        }
	        result.put("picFileList", picFileList);
		}
		result.put("Result", r);
		renderJson(result);
	}
	
	public void getPPTImages(){
		boolean r = true;
		String rootPath = PropertiesFactoryHelper.getInstance().getConfig("resource.dir");
		String rootUrl  = PropertiesFactoryHelper.getInstance().getConfig("resource.url");
		Map<String,Object> result = new HashMap<String,Object>();
		List<Object> picFileList = new ArrayList<Object>();
		File file = new File(rootPath+getPara("folder"));
		if (!file.exists()) {
			r = false;
		}else{
	        File[] files = file.listFiles();
	        for (File listFile : files)
	        {
	            String fileName = listFile.getName();
	            String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
	            String[] value = {"png"};
				if(Arrays.<String>asList(value).contains(fileExt) && fileName.contains("preview")){
					picFileList.add(rootUrl+getPara("folder")+listFile.getName());
				}
	        }
	        result.put("picFileList", picFileList);
		}
		result.put("Result", r);
		renderJson(result);
	}
	
	public void filePreview(){
		String rootPath = PropertiesFactoryHelper.getInstance().getConfig("resource.dir");
		String rootUrl  = PropertiesFactoryHelper.getInstance().getConfig("resource.url");
		String dir = getPara("dir");
		String fileName = getPara("fileName");
		String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		String parameters = "";
		try{
			switch(fileExt) {
				case "doc" :
					parameters = rootUrl+"convert/"+Word2Html.dao.convert2Html(dir+fileName,rootPath+"convert/");
					break;
				case "xls" :
					parameters = rootUrl+"convert/"+Excel2Html.dao.convert2Html(dir+fileName,rootPath+"convert/");
					break;
				case "ppt" :
					parameters = "convert/"+PPT2Png.dao.convert2Pic(dir+fileName,rootPath+"convert/");
					break;
				case "pdf" :
					parameters = "convert/"+PDFReader.dao.convert2Pic(dir+fileName,rootPath+"convert/");
				default :
					break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//根据文件名后缀判断使用的预览页
		setAttr("fileName", fileName);
		setAttr("parameters", parameters);
		switch(fileExt) {
			case "doc" :
				render("../preview/docPreview.html");
				break;
			case "pdf" :
				render("../preview/pdfPreview.html");
				break;
			case "ppt" :
				render("../preview/pptPreview.html");
				break;
			default :
				render("../preview/noSupport.html");
				break;
		}
	}
	
}
