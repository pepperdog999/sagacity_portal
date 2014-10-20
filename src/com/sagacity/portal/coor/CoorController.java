package com.sagacity.portal.coor;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
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
import com.sagacity.portal.user.UserController;
import com.sagacity.portal.utility.FileUtil;
import com.sagacity.portal.utility.ImageHelper;
import com.sagacity.portal.utility.DateUtils;
import com.sagacity.portal.comm.BaseController;
import com.sagacity.portal.comm.CommOpration;
import com.sagacity.portal.utility.PropertiesFactoryHelper;

@ControllerBind(controllerKey = "/coor")
public class CoorController extends BaseController implements CommOpration{
	
	@Override
	//首页
	public void index() {
		Object[] cookieArray = getCookieContext();
		setAttr("coorSummary",Db.find(SqlKit.sql("coor.getCoorSummary"),cookieArray[0],cookieArray[0],cookieArray[0],cookieArray[0]));
		setAttr("userContact",Db.findFirst(SqlKit.sql("user.getContactInfoByUserID"),cookieArray[4],cookieArray[0]));
		render("index.html");
	}	
	
	//已发协同列表
	public void getCreatedList() {
		int pageNumber = getParaToInt("pageNumber") == null ? 1 : getParaToInt("pageNumber");
		int pageSize = getParaToInt("pageSize") == null ? 5 : getParaToInt("pageSize");
		Object[] cookieArray = getCookieContext();
		
		Page<Record> coordinates = Db.paginate(pageNumber, pageSize, SqlKit.sql("coor.getCreatedList-select")
				, SqlKit.sql("coor.getCreatedList-from"),cookieArray[0]);
		renderJson(coordinates);
	}
	
	//已办协同列表
	public void getProcessedList() {
		int pageNumber = getParaToInt("pageNumber") == null ? 1 : getParaToInt("pageNumber");
		int pageSize = getParaToInt("pageSize") == null ? 5 : getParaToInt("pageSize");
		Object[] cookieArray = getCookieContext();
		
		Page<Record> coordinates = Db.paginate(pageNumber, pageSize, SqlKit.sql("coor.getProcessedList-select")
				, SqlKit.sql("coor.getProcessedList-from"),cookieArray[0]);
		renderJson(coordinates);
	}
	
	//待办协同列表
	public void getProcessingList() {
		int pageNumber = getParaToInt("pageNumber") == null ? 1 : getParaToInt("pageNumber");
		int pageSize = getParaToInt("pageSize") == null ? 5 : getParaToInt("pageSize");
		Object[] cookieArray = getCookieContext();
		
		Page<Record> coordinates = Db.paginate(pageNumber, pageSize, SqlKit.sql("coor.getProcessingList-select")
				, SqlKit.sql("coor.getProcessingList-from"),cookieArray[0]);
		renderJson(coordinates);
	}
	
	//新增页
	public void addCoor(){
		Object[] cookieArray = getCookieContext();
		setAttr("userContact",Db.findFirst(SqlKit.sql("user.getContactInfoByUserID"),cookieArray[4],cookieArray[0]));
		render("addCoor.html");
	}
	
	//处理页
	public void handleCoor() {
		int coordinateID=getParaToInt("coordinateID");
		int nodeID=getParaToInt("nodeID");
		//获取协同信息
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("workflow.url")+"QueryCoordinateInfo";
		try{
			urlString+="?coordinateID="+coordinateID;
			URL url = new URL(urlString);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.connect();    
	        // 取得输入流，并使用Reader读取    
			BufferedReader reader = new BufferedReader(new InputStreamReader(    
	        		httpConn.getInputStream(),"UTF-8"));
	        JSONObject jo = JSONObject.fromObject(reader.readLine());
	        setAttr("coordinateInfo",jo);
	        reader.close();
	        httpConn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//获取节点信息
		urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("workflow.url")+"QueryNodeInfo";
		try{
			urlString+="?nodeID="+nodeID;
			URL url = new URL(urlString);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.connect();    
	        // 取得输入流，并使用Reader读取    
			BufferedReader reader = new BufferedReader(new InputStreamReader(    
	        		httpConn.getInputStream(),"UTF-8"));
	        JSONObject jo = JSONObject.fromObject(reader.readLine());
	        setAttr("nodeInfo",jo);
	        reader.close();
	        httpConn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		setAttr("action",getPara("action"));
		render("handleCoor.html");	
	}
	
	//流程正文区域
	public void processText(){
		int coordinateID = getParaToInt("coordinateID");
		int nodeID = getParaToInt("nodeID");
		Object[] cookieArray = getCookieContext();
        setAttr("userID",cookieArray[0]);
		if(coordinateID>0){
			//读取协同主数据
			String urlString =PropertiesFactoryHelper.getInstance()
					.getConfig("workflow.url")+"QueryCoordinateInfo";
			try{
				urlString+="?coordinateID="+coordinateID;
				URL url = new URL(urlString);
				HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
				httpConn.setRequestMethod("GET");
				httpConn.connect();    
		        //取得输入流，并使用Reader读取    
				BufferedReader reader = new BufferedReader(new InputStreamReader(    
		        		httpConn.getInputStream(),"UTF-8"));
		        JSONObject jo = JSONObject.fromObject(reader.readLine());
		        setAttr("coordinateInfo",jo);
		        reader.close();
		        httpConn.disconnect();
			}catch(Exception e){
				e.printStackTrace();
			}
			//读取协同附件数据
			urlString =PropertiesFactoryHelper.getInstance()
					.getConfig("workflow.url")+"QueryCoordinateAttach";
			try{
				urlString+="?coordinateID="+coordinateID;
				URL url = new URL(urlString);
				HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
				httpConn.setRequestMethod("GET");
				httpConn.connect();    
		        //取得输入流，并使用Reader读取    
				BufferedReader reader = new BufferedReader(new InputStreamReader(    
		        		httpConn.getInputStream(),"UTF-8"));
		        JSONArray ja = JSONArray.fromObject(reader.readLine());
		        setAttr("attachments",ja);
		        reader.close();
		        httpConn.disconnect();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		render("process/processText.html");
	}
	
	//流程意见区域
	public void processComment(){
		int coordinateID = getParaToInt("coordinateID");
		int nodeID = getParaToInt("nodeID");
		if(coordinateID >0 ){
			String urlString =PropertiesFactoryHelper.getInstance()
					.getConfig("workflow.url")+"GetCoordinateComment";
			try{
				urlString+="?coordinateID="+coordinateID;
				URL url = new URL(urlString);
				HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
				httpConn.setRequestMethod("GET");
				httpConn.connect();    
		        // 取得输入流，并使用Reader读取    
				BufferedReader reader = new BufferedReader(new InputStreamReader(    
		        		httpConn.getInputStream(),"UTF-8"));
		        JSONObject jo = JSONObject.fromObject(reader.readLine());
		        setAttr("commentInfo",jo.get("Data"));
		        reader.close();
		        httpConn.disconnect();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		render("process/processComment.html");
	}
	
	//流程图展示区
	public void processPath(){
		int coordinateID = getParaToInt("coordinateID");
		if(coordinateID >0 ){
			setAttr("coordinateID",coordinateID);
		}
		render("process/processPath.html");
	}
	
	//初始化协同
	public void initCoor(){
		boolean r = false;
		Map<String, Object> map = new HashMap<String, Object>();
		Object[] cookieArray = getCookieContext();
		int blnSubmit = getParaToInt("blnSubmit");
		String dataJson = getPara("dataJson");
		String linkJson = getPara("linkJson");
		String coordinateName = getPara("coorName");
		String coordinateContent = getPara("coorContent");
		String extraContent = getPara("extraContent");
		String attachArray = getPara("attachArray");
		int coordinateID = 0;
		//调用流程引擎部分，初始化流程
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("workflow.url")+"CreateCoordinate";
		try{
			String params ="userID="+cookieArray[0]+"&coordinateName="+URLEncoder.encode(coordinateName,"UTF-8")+"&coordinateContent="+URLEncoder.encode(coordinateContent,"UTF-8")
					+"&extraContent="+URLEncoder.encode(extraContent,"UTF-8")+"&dataJson="+URLEncoder.encode(dataJson,"UTF-8")+"&linkJson="+linkJson+"&attachArray="+attachArray+"&blnSubmit="+blnSubmit;
			URL url = new URL(urlString);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			/*httpConn.setRequestMethod("GET");
			httpConn.connect();*/    
	        // 取得输入流，并使用Reader读取
			httpConn.setRequestMethod("POST");// 提交模式
			httpConn.setDoOutput(true);// 是否输入参数
	        byte[] bypes = params.getBytes();
	        httpConn.getOutputStream().write(bypes);// 输入参数
			BufferedReader reader = new BufferedReader(new InputStreamReader(    
	        		httpConn.getInputStream(),"UTF-8"));
	        JSONObject jo = JSONObject.fromObject(reader.readLine());
	        if(jo.getInt("ExecuteState")==1){
	        	r = true;
	        	coordinateID=jo.getInt("CoordinateID");
	        }
	        reader.close();
	        httpConn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		if(r){
			map.put("Msg", "协同创建成功！");
			map.put("CoordinateID", coordinateID);
		}else{
			map.put("Msg", "协同创建失败！");
		}
		map.put("Result", r);
		renderJson(map);
	}
	
	//提交流程
	@Before(Tx.class)
	public void submitCoor(){
		boolean r =false;
		Map<String, Object> map = new HashMap<String, Object>();
		Object[] cookieArray = getCookieContext();
		int coordinateID = getParaToInt("coordinateID");
		int nodeID = getParaToInt("nodeID");
		String coorContent = getPara("coorContent");
		int coorOpinion = getParaToInt("coorOpinion");
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("workflow.url")+"UserSubmit";
		try{
			urlString+="?coordinateID="+coordinateID+"&nodeID="+nodeID+"&userID="+cookieArray[0]
					+"&coorOpinon="+coorOpinion+"&coorContent="+URLEncoder.encode(coorContent,"UTF-8");
			URL url = new URL(urlString);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.connect();    
	        // 取得输入流，并使用Reader读取    
			BufferedReader reader = new BufferedReader(new InputStreamReader(    
	        		httpConn.getInputStream(),"UTF-8"));
	        JSONObject jo = JSONObject.fromObject(reader.readLine());
	        if(jo.getInt("ExecuteState")==1){
	        	r = true;
	        }
	        reader.close();
	        httpConn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		if(r){
			map.put("Msg", "协同处理成功！");
		}else{
			map.put("Msg", "协同处理失败！");
		}
		map.put("Result", r);
		renderJson(map);
	}
	
	//删除协同
	public void delCoor(){
		boolean r = false;
		Map<String, Object> map = new HashMap<String, Object>();
		int coordinateID = getParaToInt("coordinateID");
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("workflow.url")+"DeleteCoordinateByID";
		try{
			urlString+="?coordinateID="+coordinateID;
			URL url = new URL(urlString);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.connect();    
	        // 取得输入流，并使用Reader读取    
			BufferedReader reader = new BufferedReader(new InputStreamReader(    
	        		httpConn.getInputStream(),"UTF-8"));
	        JSONObject jo = JSONObject.fromObject(reader.readLine());
	        if(jo.getInt("ExecuteState")==1){
	        	r = true;
	        }
	        reader.close();
	        httpConn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		if(r){
			map.put("Msg", "协同删除成功！");
		}else{
			map.put("Msg", "协同删除失败！");
		}
		map.put("Result", r);
		renderJson(map);
	}
	
	//更新协同附言
	
	public void updateExtraContent(){
		boolean r = false;
		Map<String, Object> map = new HashMap<String, Object>();
		int coordinateID = getParaToInt("coordinateID");
		String extraContent = getPara("extraContent");
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("workflow.url")+"UpdateCoorExtraContent";
		try{
			urlString+="?coordinateID="+coordinateID+"&extraContent="+URLEncoder.encode(extraContent,"UTF-8");;
			URL url = new URL(urlString);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.connect();    
	        // 取得输入流，并使用Reader读取    
			BufferedReader reader = new BufferedReader(new InputStreamReader(    
	        		httpConn.getInputStream(),"UTF-8"));
			JSONObject jo = JSONObject.fromObject(reader.readLine());
	        if(jo.getInt("ExecuteState")==1){
	        	r = true;
	        }
	        reader.close();
	        httpConn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		if(r){
			map.put("Msg", "附言更新成功！");
		}else{
			map.put("Msg", "附言更新失败！");
		}
		map.put("Result", r);
		renderJson(map);
	}
	
	//上传附件
	public void uploadAttach() {
		boolean r = false;
		Object[] cookieArray = getCookieContext();
		JSONObject map = new JSONObject();
		String config_dir = PropertiesFactoryHelper.getInstance().getConfig("resource.dir");
		String folderName = cookieArray[0]+"/files/coor/"+DateUtils.getLongDate();
		File f1 = new File(config_dir+folderName);
		if (!f1.exists()) {
			f1.mkdirs();
		}
		UploadFile uploadFile = getFile("attachFile", f1.getAbsolutePath());
		int coordinateID = getParaToInt("coordinateID");
		File nFile = uploadFile.getFile();
		if (nFile==null) {
			map.put("Result", r);
			map.put("Msg", "附件上传失败！");
		}else{
			String attachURL = folderName+"/"+nFile.getName();
			String attachName = nFile.getName();
			String urlString =PropertiesFactoryHelper.getInstance()
			.getConfig("workflow.url")+"AddCoorAttachInfo";
			try{
				urlString+="?coordinateID="+coordinateID+"&attachURL="+URLEncoder.encode(attachURL,"UTF-8")
						+"&attachName="+URLEncoder.encode(attachName,"UTF-8");
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
		        r = true;
			}catch(Exception e){
				e.printStackTrace();
			}
			if(r){
				map.put("Msg", "附件上传成功！");
			}else{
				map.put("Msg", "附件上传失败！");
			}
			map.put("Result", r);
		}
		render(new JsonRender(map).forIE());
	}

	//获得AttachID对应的文件URL
	public void getAttachFileURL(){
		boolean r =false;
		Map<String, Object> map = new HashMap<String, Object>();
		String rootUrl = PropertiesFactoryHelper.getInstance().getConfig("resource.url");
		String rootPath = PropertiesFactoryHelper.getInstance().getConfig("resource.dir");
		int attachID = getParaToInt("attachID");
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("workflow.url")+"GetAttachFileInfo";
		try{
			urlString+="?attachID="+attachID;
			URL url = new URL(urlString);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.connect();    
	        // 取得输入流，并使用Reader读取    
			BufferedReader reader = new BufferedReader(new InputStreamReader(    
	        		httpConn.getInputStream(),"UTF-8"));
	        JSONObject jo = JSONObject.fromObject(reader.readLine());
	        map.put("FileURL", rootUrl+jo.get("AttachmentURL"));
	        map.put("FilePath", rootPath+jo.getString("AttachmentURL").substring(0,jo.getString("AttachmentURL").lastIndexOf("/")+1));
	        map.put("FileName", jo.get("AttachmentName"));
	        r = true;
	        reader.close();
	        httpConn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		map.put("Result", r);
		renderJson(map);
	}
	
	//删除附件
	public void delAttachFile(){
		boolean r = false;
		Map<String, Object> map = new HashMap<String, Object>();
		int attachID = getParaToInt("attachID");
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("workflow.url")+"DelAttachFile";
		try{
			urlString+="?attachID="+attachID;
			URL url = new URL(urlString);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.connect();    
	        // 取得输入流，并使用Reader读取    
			BufferedReader reader = new BufferedReader(new InputStreamReader(    
	        		httpConn.getInputStream(),"UTF-8"));
			JSONObject jo = JSONObject.fromObject(reader.readLine());
	        if(jo.getInt("ExecuteState")==1){
	        	r = true;
	        }
	        reader.close();
	        httpConn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		if(r){
			map.put("Msg", "附件删除成功！");
		}else{
			map.put("Msg", "附件删除失败！");
		}
		map.put("Result", r);
		renderJson(map);
	}

	//协同正文中的图片上传
	public void imageUploader(){
		 HashMap<String, String> extMap = new HashMap<String, String>();
		 extMap.put("image", "gif,jpg,jpeg,png,bmp");
		 long maxSize = 1024*1024; //1M
		 String resource_dir = PropertiesFactoryHelper.getInstance().getConfig("resource.dir");
		 String resource_url = PropertiesFactoryHelper.getInstance().getConfig("resource.url");
		 String dirName = getPara("dir")==null?"image":getPara("dir");
		 Map<String, Object> result = new HashMap<String, Object>();
		 
		 File f1 = new File(ROOTPATH + "/imgTemp/");
		 File f2 = new File(resource_dir+"coor/"+dirName+"/"+DateUtils.getLongDate());
		 if (!f1.exists() || !f2.exists()) {
			f1.mkdirs();
			f2.mkdirs();
		 }
		 UploadFile uploadFile = getFile("imgFile", f1.getAbsolutePath());
		 if(uploadFile!=null){
			 String fileName = uploadFile.getFile().getName();
			 File file = uploadFile.getFile();
			 String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
			 if(!Arrays.<String>asList(extMap.get(dirName).split(",")).contains(fileExt)){
				 result.put("error", 1);
				 result.put("message", "上传文件格式错误！\n只允许" + extMap.get(dirName) + "格式！");
			     file.delete();
			 }else if(file.length() > maxSize){
				 result.put("error", 1);
				 result.put("message", "上传大小超过限制！");
			     file.delete();
			}else{
				 File nFile=FileUtil.copyFile(file,f2,System.currentTimeMillis()+"");
				 try {
					 StringBuilder smallFieName= new StringBuilder(nFile.getAbsolutePath());
					 smallFieName.insert(smallFieName.indexOf("."), ".small");
					 //ImageUtil.resize(nFile, nFile, 550, 1);
					 ImageHelper.cut(nFile.getAbsolutePath(), smallFieName.toString(), 200);
				 } catch (IOException e) {
					 e.printStackTrace();
					 result.put("error", 1);
					 result.put("message", "切图失败！");
				 }
				 if(nFile !=null){
					 result.put("error", 0);
					 result.put("url", resource_url+"coor/"+dirName+"/"+DateUtils.getLongDate()+"/"+nFile.getName());
				 }
			 }
	     }else{
			 result.put("error", 1);
			 result.put("message", "请选择文件!");
	     }
		 render(new JsonRender(result).forIE());
	}
}
