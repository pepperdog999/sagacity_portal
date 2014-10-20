package com.sagacity.portal.system;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sagacity.portal.comm.BaseController;
import com.sagacity.portal.comm.CommOpration;
import com.sagacity.portal.user.UserInfo;
import com.sagacity.portal.user.UserSetting;
import com.sagacity.portal.utility.PropertiesFactoryHelper;

@ControllerBind(controllerKey = "/system")
public class SystemController extends BaseController implements CommOpration{
	
	@Override
	public void index() {
	
	}
	
	public void userSet() {
		Object[] cookieArray = getCookieContext();
		Map<String, Object> map = new HashMap<String, Object>();
		setAttr("resourceUrl", PropertiesFactoryHelper.getInstance()
				.getConfig("resource.url"));
		setAttr("headerFix", PropertiesFactoryHelper.getInstance()
				.getConfig("user.header"));
		setAttr("userInfo",Db.findFirst(SqlKit.sql("user.getUserInfo"),cookieArray[0]));
		setAttr("userSetting", Db.findFirst("select * from sys_usersetting where UserID=?",cookieArray[0]));
		setAttr("modules",Db.find(SqlKit.sql("system.getModules")));
		setAttr("corpApps",Db.find(SqlKit.sql("system.getCorpApps"),cookieArray[4]));
		render("userSet.html");
	}
	
	public void updateUserSet() {
		Map<String,Object> data = new HashMap<String,Object>();
		boolean r = false;
		
		UserInfo ui = getModel(UserInfo.class,"userInfo");
		UserSetting us = getModel(UserSetting.class,"userSetting");
		r = ui.update();
		if(us.getInt("SettingID")==0){
			r = us.set("UserID", ui.get("UserID")).save();
		}else {
			r = us.update();
		}
		if(r){
			data.put("Msg", "个人设置更新成功！");
			data.put("SettingID", us.getInt("SettingID"));
		}else{
			data.put("Msg", "个人设置更新失败！");
		}
		data.put("Result", r);
		renderJson(data);		
	}
	
	public void checkOldPwd(){
		Map<Object, Object> data = new HashMap<Object, Object>();
		Record user=Db.findFirst("select u.UserID from sys_users u where u.UserID=? and u.Password=?",getPara("userID"),getPara("oldPwd"));
		if(user != null){
			data.put("Result", true);
			data.put("Msg", "原始密码输入正确！");
		}else{
			data.put("Result", false);
			data.put("Msg", "原始密码输入错误！");
		}
		renderJson(data);
	}
	
	public void getAppInfo(){
		String appCode = getDefaultAppCode();
		List<Record> r = Db.find("select av.VersionCode,av.OSType,av.DownloadURL \n"
				+"from sys_appversion av \n"
				+"left join sys_appinfo ai on ai.AppCode=av.AppCode \n"
				+"where ai.AppCode=? and av.blnRelease=1",appCode);
		renderJson("appInfo",r);
	}
	
	public void changePwd(){
		JSONObject map = new JSONObject();
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("interface.url")+"ChangePassword";
		try{
			urlString+="?userID="+getPara("userID")+"&newPwd="+getPara("newPwd")
					+"&oldPwd="+getPara("oldPwd");
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
	
	public void sendMsg(){
		Object[] cookieArray = getCookieContext();
		JSONObject map = new JSONObject();
		String contents = getPara("contents");
		String indexData = getPara("indexData");
		int blnIsPrefix = getParaToInt("blnIsPrefix",1);
		int blnIsSMS = getParaToInt("blnIsSMS",1);
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("interface.url")+"SendMessage";
		try{
			String params = "userID="+cookieArray[0]+"&corpID="+cookieArray[4]+"&content="+URLEncoder.encode(contents,"UTF-8")
					+"&indexData="+URLEncoder.encode(indexData,"UTF-8")+"&blnIsPrefix="+blnIsPrefix+"&blnIsSMS="+blnIsSMS;
			URL url = new URL(urlString);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			/*
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			*/
			httpConn.setRequestMethod("POST");// 提交模式
			httpConn.setDoOutput(true);// 是否输入参数
	        byte[] bypes = params.getBytes();
	        httpConn.getOutputStream().write(bypes);// 输入参数
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
	
	//根据coordinateID获得流程定义的json数据
	public void getWorkflowRuntime(){
		JSONObject map = new JSONObject();
		boolean r = false;
		int coordinateID = getParaToInt("coordinateID");
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("workflow.url")+"GetWorkflowRuntime";
		try{
			urlString+="?coordinateID="+coordinateID;
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
	        map.put("Msg", "接口调用失败！");
		}
		map.put("Result", r);
		renderJson(map);
	}

}
