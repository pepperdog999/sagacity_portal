package com.sagacity.portal.report;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.jfinal.aop.Before;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.render.JsonRender;
import com.jfinal.upload.UploadFile;
import com.sagacity.portal.utility.FileUtil;
import com.sagacity.portal.utility.ImageHelper;
import com.sagacity.portal.comm.BaseController;
import com.sagacity.portal.comm.CommOpration;
import com.sagacity.portal.utility.DateUtils;
import com.sagacity.portal.utility.PropertiesFactoryHelper;

@ControllerBind(controllerKey = "/report")
public class ReportController extends BaseController implements CommOpration{
	
	@Override
	//组织通讯录首页
	public void index() {
		Object[] cookieArray = getCookieContext();
		setAttr("reportSummary",Db.find(SqlKit.sql("report.getReportSummary"),cookieArray[0],cookieArray[0]));
		setAttr("resourceUrl", PropertiesFactoryHelper.getInstance()
				.getConfig("resource.url"));
		setAttr("headerFix", PropertiesFactoryHelper.getInstance()
				.getConfig("user.header"));
		render("index.html");
	}
	
	//创建的简报列表
	public void getCreatedList() {
		int pageNumber = getParaToInt("pageNumber") == null ? 1 : getParaToInt("pageNumber");
		int pageSize = getParaToInt("pageSize") == null ? 7 : getParaToInt("pageSize");
		Object[] cookieArray = getCookieContext();
		
		Page<Record> coordinates = Db.paginate(pageNumber, pageSize, SqlKit.sql("report.getCreatedList-select")
				, SqlKit.sql("report.getCreatedList-from"),cookieArray[0]);
		renderJson(coordinates);
	}
	
	//收到的简报列表
	public void getProcessingList() {
		int pageNumber = getParaToInt("pageNumber") == null ? 1 : getParaToInt("pageNumber");
		int pageSize = getParaToInt("pageSize") == null ? 7 : getParaToInt("pageSize");
		Object[] cookieArray = getCookieContext();
		Page<Record> coordinates = Db.paginate(pageNumber, pageSize, SqlKit.sql("report.getProcessingList-select")
				, SqlKit.sql("report.getProcessingList-from"),cookieArray[0]);
		renderJson(coordinates);
	}
	
	public void reportDetail(){
		int reportID = getParaToInt("reportID");
		Object[] cookieArray = getCookieContext();
		
		setAttr("userID",cookieArray[0]);
		setAttr("reportMaster",Db.findFirst("select rm.*,IFNULL(rui.MarkImportantTime,'') MarkImportantTime \n"
				+ "from reporting_master rm \n"
				+ "left join reporting_userindex rui on rui.ReportingID=rm.ReportingID \n"
				+ "where rm.ReportingID=? and rui.UserID=?",reportID,cookieArray[0]));
		String strSQL = "select ra.AttachID,CONCAT(REVERSE(RIGHT(REVERSE(ra.AttachURL),LENGTH(ra.AttachURL)-INSTR(REVERSE(ra.AttachURL),'/')+1)),'/preview',REVERSE(LEFT(REVERSE(ra.AttachURL),INSTR(REVERSE(ra.AttachURL),'/')))) AttachURL \n"
				+ "from reporting_attachment ra \n"
				+ "where ra.ReportingID=?";
		setAttr("reportAttach",Db.find(strSQL,reportID));
		setAttr("resourceUrl", PropertiesFactoryHelper.getInstance()
				.getConfig("resource.url"));
		render("reportDetail.html");
	}
	
	//标记简报为重要或取消重要
	public void markImportant(){
		int reportID = getParaToInt("reportID");
		Object[] cookieArray = getCookieContext();
		int operateType = getParaToInt("operateType");
		if(operateType==1){
			Db.update("update reporting_userindex set MarkImportantTime=now() where ReportingID=? and UserID=?",reportID,cookieArray[0]);
			renderJson("Msg","设置重要成功！");
		}else if(operateType==2){
			Db.update("update reporting_userindex set MarkImportantTime=null where ReportingID=? and UserID=?",reportID,cookieArray[0]);
			renderJson("Msg","取消重要成功！");
		}
	}
	
	//获得重要简报列表
	public void getImportantReportList(){
		Object[] cookieArray = getCookieContext();
		renderJson("list",Db.find(SqlKit.sql("report.getImportantReportList"),cookieArray[0]));
	}
	
	public void delReporting(){
		int reportID = getParaToInt("reportID");
		JSONObject map = new JSONObject();
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("interface.url")+"DelReporting";
		try{
			urlString+="?reportingID="+reportID;
			URL url = new URL(urlString);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.connect();    
	        // 取得输入流，并使用Reader读取    
			BufferedReader reader = new BufferedReader(new InputStreamReader(    
	        		httpConn.getInputStream(),"UTF-8"));
	        map = JSONObject.fromObject(reader.readLine());
	        reader.close();
	        httpConn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
			map.put("ExecuteState", 0);
	        map.put("Msg", "接口调用失败！");
		}
		renderJson(map);
	}
	
	@Before(Tx.class)
	public void addReporting() {
		Object[] cookieArray = getCookieContext();
		JSONObject map = new JSONObject();
		String title = getPara("title");
		String content = getPara("content");
		String attachArray = getPara("attachArray");
		String indexData = getPara("indexData");
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("interface.url")+"CreateReporting";
		try{
			urlString+="?userID="+cookieArray[0]+"&reportingTitle="+URLEncoder.encode(title,"UTF-8")+"&reportingContent="+URLEncoder.encode(content,"UTF-8")
					+"&attachArray="+attachArray+"&indexData="+URLEncoder.encode(indexData,"UTF-8");
			URL url = new URL(urlString);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.connect();    
	        // 取得输入流，并使用Reader读取    
			BufferedReader reader = new BufferedReader(new InputStreamReader(    
	        		httpConn.getInputStream(),"UTF-8"));
	        map = JSONObject.fromObject(reader.readLine());
	        reader.close();
	        httpConn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
			map.put("ExecuteState", 0);
	        map.put("Msg", "接口调用失败！");
		}
		renderJson(map);
	}
	
	//上传附件
	public void uploadAttach() {
		boolean r = false;
		Object[] cookieArray = getCookieContext();
		JSONObject map = new JSONObject();
		String config_dir = PropertiesFactoryHelper.getInstance().getConfig("resource.dir");
		String folderName = cookieArray[0]+"/files/reporting/"+DateUtils.getLongDate();
		File f1 = new File(config_dir+folderName);
		if (!f1.exists()) {
			f1.mkdirs();
		}
		UploadFile uploadFile = getFile("attachFile", f1.getAbsolutePath());
		int reportingID = getParaToInt("reportingID");
		File nFile = uploadFile.getFile();
		if (nFile==null) {
			map.put("Result", r);
			map.put("Msg", "图片上传失败！");
		}else{
			//处理图片
			String fileName = nFile.getName();
			String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            String[] value = {"jpg","jpeg","gif","bmp","png"};
			if(Arrays.<String>asList(value).contains(fileExt)){
				//处理图片
				 File f2o = new File(config_dir+folderName);
				 File f2p = new File(config_dir+folderName+"/preview");
				 if (!f2o.exists() || !f2p.exists()) {
					f2o.mkdirs();
					f2p.mkdirs();
				 }
				 File nFile1=FileUtil.copyFile(nFile,f2o,System.currentTimeMillis()+"");
				 try {
					 ImageHelper.cut(nFile1.getAbsolutePath(), 
							 f2p.getAbsolutePath()+"/"+nFile1.getName(), 150);
					} catch (IOException e) {
						e.printStackTrace();
					}
				//写入附件表
				ReportingAttach rt = new ReportingAttach().set("ReportingID", reportingID).set("AttachType", 1)
						.set("AttachURL", folderName+"/"+nFile1.getName()).set("CreateTime", DateUtils.nowDateTime());
				if(r=rt.save()){
					map.put("AttachInfo", rt.toJson());
					map.put("resourceUrl", PropertiesFactoryHelper.getInstance()
							.getConfig("resource.url"));
					map.put("Msg", "图片上传成功！");
				}else{
					map.put("Msg", "图片上传失败！");
				}
			}
			map.put("Result", r);
		}
		render(new JsonRender(map).forIE());
	}
	
	//删除附件
	public void delAttachFile(){
		boolean r = false;
		Map<String, Object> map = new HashMap<String, Object>();
		int attachID = getParaToInt("attachID");
		r = ReportingAttach.dao.deleteById(attachID);
		if(r){
			map.put("Msg", "图片删除成功！");
		}else{
			map.put("Msg", "图片删除失败！");
		}
		map.put("Result", r);
		renderJson(map);
	}
}
