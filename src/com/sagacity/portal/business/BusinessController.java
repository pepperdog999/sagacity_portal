package com.sagacity.portal.business;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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
import com.sagacity.portal.user.UserController;
import com.sagacity.portal.user.UserInfo;
import com.sagacity.portal.utility.StringTool;
import com.sagacity.portal.comm.BaseController;
import com.sagacity.portal.comm.CommOpration;
import com.sagacity.portal.utility.DateUtils;
import com.sagacity.portal.utility.PropertiesFactoryHelper;

@ControllerBind(controllerKey = "/business")
public class BusinessController extends BaseController implements CommOpration{
	
	@Override
	//首页
	public void index() {
		Object[] cookieArray = getCookieContext();
		setAttr("projectSummary",Db.find(SqlKit.sql("business.getProjectSummary"),cookieArray[0],cookieArray[0],cookieArray[0]));
		setAttr("userContact",Db.findFirst(SqlKit.sql("user.getContactInfoByUserID"),cookieArray[4],cookieArray[0]));
		render("index.html");
	}
	
	public void library() {
		Object[] cookieArray = getCookieContext();
		setAttr("projectSummary",Db.find(SqlKit.sql("business.getProjectSummary"),cookieArray[0],cookieArray[0],cookieArray[0]));
		setAttr("userContact",Db.findFirst(SqlKit.sql("user.getContactInfoByUserID"),cookieArray[4],cookieArray[0]));
		render("library.html");
	}
	
	//已上报列表
	public void getCreatedList() {
		int pageNumber = getParaToInt("pageNumber") == null ? 1 : getParaToInt("pageNumber");
		int pageSize = getParaToInt("pageSize") == null ? 5 : getParaToInt("pageSize");
		Object[] cookieArray = getCookieContext();
		
		Page<Record> coordinates = Db.paginate(pageNumber, pageSize, SqlKit.sql("business.getCreatedList-select")
				, SqlKit.sql("business.getCreatedList-from"),cookieArray[0]);
		renderJson(coordinates);
	}
	
	//已处理列表
	public void getProcessedList() {
		int pageNumber = getParaToInt("pageNumber") == null ? 1 : getParaToInt("pageNumber");
		int pageSize = getParaToInt("pageSize") == null ? 5 : getParaToInt("pageSize");
		Object[] cookieArray = getCookieContext();
		
		Page<Record> coordinates = Db.paginate(pageNumber, pageSize, SqlKit.sql("business.getProcessedList-select")
				, SqlKit.sql("business.getProcessedList-from"),cookieArray[0]);
		renderJson(coordinates);
	}
	
	//待处理列表
	public void getProcessingList() {
		int pageNumber = getParaToInt("pageNumber") == null ? 1 : getParaToInt("pageNumber");
		int pageSize = getParaToInt("pageSize") == null ? 5 : getParaToInt("pageSize");
		Object[] cookieArray = getCookieContext();
		
		Page<Record> coordinates = Db.paginate(pageNumber, pageSize, SqlKit.sql("business.getProcessingList-select")
				, SqlKit.sql("business.getProcessingList-from"),cookieArray[0]);
		renderJson(coordinates);
	}
	
	//项目库
	public void getProjectList() {
		int pageNumber = getParaToInt("pageNumber") == null ? 1 : getParaToInt("pageNumber");
		int pageSize = getParaToInt("pageSize") == null ? 5 : getParaToInt("pageSize");
		Object[] cookieArray = getCookieContext();
		
		Page<Record> projects = Db.paginate(pageNumber, pageSize, SqlKit.sql("business.getProjectList-select")
				, SqlKit.sql("business.getProjectList-from"));
		renderJson(projects);
	}
		
	//项目处理页
	public void handlePorject() {
		int coordinateID=getParaToInt("coordinateID");
		int nodeID=getParaToInt("nodeID");
		
		//获取流程信息
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
		render("handleProject.html");	
	}
	
	//删除项目
	@Before(Tx.class)
	public void delProject(){
		boolean r=false;
		Map<String, Object> map = new HashMap<String, Object>();
		int coordinateID = getParaToInt("coordinateID");
		int projectID = 0;
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
	        projectID = jo.getInt("FormDataID");
	        reader.close();
	        httpConn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		//删除项目相关表
		if (projectID != 0){
			Db.update("delete from business_clientinfo where ProjectID=?",projectID);
			Db.update("delete from business_techscheme where ProjectID=?",projectID);
			Db.update("delete from business_schemeconfirm where ProjectID=?",projectID);
			Db.update("delete from business_commercialreview where ProjectID=?",projectID);
			Db.update("delete from business_majorreview where ProjectID=?",projectID);
			Db.update("delete from business_superiorreview where ProjectID=?",projectID);
			Db.update("delete from business_contractinfo where ProjectID=?",projectID);
			Db.update("delete from business_constructinfo where ProjectID=?",projectID);
			Db.update("delete from business_codelist where ProjectID=?",projectID);
			r= ProjectBase.dao.deleteById(projectID);
		}
		//删除流程相关表
		//r=Engine.dao.deleteCoordinate(coordinateID);
		urlString =PropertiesFactoryHelper.getInstance()
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
			r = false;
		}
		if(r){
			map.put("Msg", "删除成功！");
		}else{
			map.put("Msg", "删除失败！");
		}
		map.put("Result", r);
		renderJson(map);
	}
	
	//创建项目
	@Before(Tx.class)
	public void saveProjectBase(){
		boolean r = false;
		Map<String, Object> map = new HashMap<String, Object>();
		String code = PropertiesFactoryHelper.getInstance().getConfig("project.prefix")+StringTool.generateNumberString(5);
		Object[] cookieArray = getCookieContext();
		JSONObject pbj =JSONObject.fromObject(getPara("pb_json"));
		ProjectBase pb=new ProjectBase().set("Name", pbj.get("Name")).set("Code", code).set("Memo", pbj.get("Memo")).set("Income", pbj.get("Income"))
				.set("Require",pbj.get("Require")).set("AttachURL", pbj.get("AttachURL")).set("UserID", cookieArray[0])
				.set("AddTime", DateUtils.nowDateTime());
		r = pb.save();
		//调用流程引擎部分，初始化流程
		//r=CoordinateInfo.dao.createCoordinateByTemplate(1, pb.getStr("Name"), cookieArray[0].toString(), pb.getInt("ProjectID"));
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("workflow.url")+"CreateCoordinateByTemplate";
		try{
			urlString+="?templateID="+1+"&coordinateName="+URLEncoder.encode(pb.getStr("Name"),"UTF-8")+"&userID="+cookieArray[0]
					+"&formDataID="+pb.getInt("ProjectID");
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
			r = false;
		}
		if(r){
			map.put("Msg", "项目信息保存成功！");
			map.put("Data", pb);
		}else{
			map.put("Msg", "项目信息保存失败！");
		}
		map.put("Result", r);
		renderJson(map);	
	}
	
	//提交项目
	@Before(Tx.class)
	public void submitProcess(){
		boolean r =false;
		Map<String, Object> map = new HashMap<String, Object>();
		Object[] cookieArray = getCookieContext();
		int coordinateID = getParaToInt("coordinateID");
		int nodeID = getParaToInt("nodeID");
		//通过接口查询流程信息与节点信息
		int projectID = 0;
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
	        projectID = jo.getInt("FormDataID");
	        reader.close();
	        httpConn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		//根据NodeID的节点保存对应的数据
		String formCode="";
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
	        formCode = jo.getString("FormCode");
	        reader.close();
	        httpConn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		switch (formCode){
		case "business_clientInfo":
			JSONObject cij = JSONObject.fromObject(getPara("ci_json"));
			r=new ClientInfo().set("ProjectID", projectID).set("Name", cij.get("Name")).set("Code", cij.get("Code")).set("ContactPerson", cij.get("ContactPerson"))
				.set("ContactInfo", cij.get("ContactInfo")).set("ClientManager", cij.get("ClientManager")).set("UserID", cookieArray[0])
			    .set("AddTime", DateUtils.nowDateTime()).save();
			break;
		case "business_techScheme":
			JSONObject tsj = JSONObject.fromObject(getPara("ts_json"));
			r=new TechScheme().set("ProjectID", projectID).set("Budget", tsj.get("Budget")).set("Estimate", tsj.get("Estimate")).set("Memo", tsj.get("Memo"))
			.set("AttachURL", tsj.get("AttachURL")).set("UserID", cookieArray[0]).set("AddTime", DateUtils.nowDateTime()).save();
			break;
		case "business_schemeConfirm":
			JSONObject scj = JSONObject.fromObject(getPara("sc_json"));
			r=new SchemeConfirm().set("ProjectID", projectID).set("blnConfirm", scj.getInt("blnConfirm")).set("AttachURL1", scj.get("AttachURL1")).set("AttachURL2", scj.get("AttachURL2"))
			.set("AttachURL3", scj.get("AttachURL3")).set("Memo", scj.get("Memo")).set("UserID", cookieArray[0]).set("AddTime", DateUtils.nowDateTime()).save();
			break;
		case "business_commercialReview":
			JSONObject crj = JSONObject.fromObject(getPara("cr_json"));
			r=new CommercialReview().set("ProjectID", projectID).set("blnAgree", crj.getInt("blnAgree")).set("Key1", crj.get("Key1")).set("Key2", crj.get("Key2")).set("Key3", crj.get("Key3"))
			.set("Key4", crj.get("Key4")).set("Key5", crj.get("Key5")).set("Key6", crj.get("Key6")).set("Key7", crj.get("Key7")).set("Key8", crj.get("Key8"))
			.set("Key9", crj.get("Key9")).set("Key10", crj.get("Key10")).set("Key11", crj.get("Key11")).set("CommercialType", crj.get("CommercialType"))
			.set("AttachURL", crj.get("AttachURL")).set("Memo", crj.get("Memo")).set("UserID", cookieArray[0]).set("AddTime", DateUtils.nowDateTime()).save();
			break;
		case "business_majorReview":
			JSONObject mrj = JSONObject.fromObject(getPara("mr_json"));
			r=new MajorReview().set("ProjectID", projectID).set("Conclusion", mrj.get("Conclusion")).set("AttachURL", mrj.get("AttachURL"))
			.set("Memo", mrj.get("Memo")).set("UserID", cookieArray[0]).set("AddTime", DateUtils.nowDateTime()).save();
			break;
		case "business_superiorReview":
			JSONObject srj = JSONObject.fromObject(getPara("sr_json"));
			r=new SuperiorReview().set("ProjectID", projectID).set("Conclusion", srj.get("Conclusion")).set("AttachURL1", srj.get("AttachURL1")).set("AttachURL2", srj.get("AttachURL2"))
			.set("Memo", srj.get("Memo")).set("UserID", cookieArray[0]).set("AddTime", DateUtils.nowDateTime()).save();
			break;
		case "business_contractInfo":
			JSONObject ccj = JSONObject.fromObject(getPara("cc_json"));
			r=new ContractInfo().set("ProjectID", projectID).set("AttachURL1", ccj.get("AttachURL1")).set("AttachURL2", ccj.get("AttachURL2"))
			.set("Memo", ccj.get("Memo")).set("UserID", cookieArray[0]).set("AddTime", DateUtils.nowDateTime()).save();
			break;
		case "business_constructInfo":
			JSONObject iij = JSONObject.fromObject(getPara("ii_json"));
			r=new ConstructInfo().set("ProjectID", projectID).set("AttachURL1", iij.get("AttachURL1")).set("AttachURL2", iij.get("AttachURL2")).set("AttachURL3", iij.get("AttachURL3"))
			.set("MISCode", iij.get("MISCode")).set("Memo", iij.get("Memo")).set("UserID", cookieArray[0]).set("AddTime", DateUtils.nowDateTime()).save();
			break;
		case "business_codeList":
			JSONObject clj = JSONObject.fromObject(getPara("cl_json"));
			r=new CodeList().set("ProjectID", projectID).set("CodeJson", clj.get("CodeJson").toString())
			.set("Memo", clj.get("Memo")).set("UserID", cookieArray[0]).set("AddTime", DateUtils.nowDateTime()).save();
			break;
		default:
		}
		// 流程处理部分
		String coorContent = getPara("coorContent");
		urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("workflow.url")+"UserSubmit";
		try{
			urlString+="?coordinateID="+coordinateID+"&nodeID="+nodeID+"&userID="+cookieArray[0]
					+"&coorContent="+URLEncoder.encode(coorContent,"UTF-8");
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
			r = false;
		}
		if(r){
			map.put("Msg", "项目信息处理成功！");
		}else{
			map.put("Msg", "项目信息处理失败！");
		}
		map.put("Result", r);
		renderJson(map);
	}
	
	//取得项目信息，嵌套的表单页面
	public void processText(){
		int projectID=getParaToInt("projectID");
		String processCode=getPara("processCode");
		if(projectID>0){
			setAttr("pb",ProjectBase.dao.findById(projectID));
			
			Record ci = Db.findFirst("select * from business_clientinfo where ProjectID=?",projectID);
			if(ci!=null)
				ci.set("UserName", UserInfo.dao.findById(ci.get("UserID")).get("Caption"));	
			setAttr("ci",ci);
			
			Record ts = Db.findFirst("select * from business_techscheme where ProjectID=?",projectID);
			if(ts!=null)
				ts.set("UserName", UserInfo.dao.findById(ts.get("UserID")).get("Caption"));	
			setAttr("ts",ts);
			
			Record sc = Db.findFirst("select * from business_schemeconfirm where ProjectID=?",projectID);
			if(sc!=null)
				sc.set("UserName", UserInfo.dao.findById(sc.get("UserID")).get("Caption"));	
			setAttr("sc",sc);
			
			Record cr = Db.findFirst("select * from business_commercialreview where ProjectID=?",projectID);
			if(cr!=null)
				cr.set("UserName", UserInfo.dao.findById(cr.get("UserID")).get("Caption"));	
			setAttr("cr",cr);
			
			Record mr = Db.findFirst("select * from business_majorreview where ProjectID=?",projectID);
			if(mr!=null)
				mr.set("UserName", UserInfo.dao.findById(mr.get("UserID")).get("Caption"));	
			setAttr("mr",mr);
			
			Record sr = Db.findFirst("select * from business_superiorreview where ProjectID=?",projectID);
			if(sr!=null)
				sr.set("UserName", UserInfo.dao.findById(sr.get("UserID")).get("Caption"));	
			setAttr("sr",sr);
			
			Record cc=Db.findFirst("select * from business_contractinfo where ProjectID=?",projectID);
			if(cc!=null)
				cc.set("UserName", UserInfo.dao.findById(cc.get("UserID")).get("Caption"));	
			setAttr("cc",cc);
			
			Record ii=Db.findFirst("select * from business_constructinfo where ProjectID=?",projectID);
			if(ii!=null)
				ii.set("UserName", UserInfo.dao.findById(cc.get("UserID")).get("Caption"));
			setAttr("ii",ii);
			
			Record cl=Db.findFirst("select * from business_codelist where ProjectID=?",projectID);
			if(cl!=null)
				cl.set("UserName", UserInfo.dao.findById(cc.get("UserID")).get("Caption"));
			setAttr("cl",cl);
		}
		setAttr("processCode",processCode);
		render("process/processText.html");
	}
	
	//流程意见区域
	public void processComment(){
		int coordinateID = getParaToInt("coordinateID");
		int nodeID = getParaToInt("nodeID");
		if(coordinateID >0 ){
			String urlString =PropertiesFactoryHelper.getInstance()
					.getConfig("workflow.url")+"GetWorkflowComment";
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

	public void importCode(){
		Map<String, Object> map = new HashMap<String, Object>();
		List<Object> codeJson= new ArrayList<Object>();
		for(int i=1;i<5;i++){
			Map<String, Object> o = new HashMap<String, Object>();
			o.put("Code", "C"+i);
			o.put("Desc", "描述"+i);
			codeJson.add(o);
		}
		File f1 = new File(ROOTPATH+"/tempFiles/"+DateUtils.getLongDate());
		if (!f1.exists()) {
			f1.mkdirs();
		}
		UploadFile uploadFile = getFile("attFile15", f1.getAbsolutePath());
		File nFile = uploadFile.getFile();
		if (nFile==null) {
			map.put("Result", false);
			map.put("Msg", "计费代码导入失败！");
		}else{
			map.put("Result", true);
			map.put("Msg", "计费代码导入成功！");
			map.put("CodeJson", codeJson);
		}
		render(new JsonRender(map).forIE());
	}
}
