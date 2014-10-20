package com.sagacity.portal.comm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.sagacity.portal.system.ModuleInfo;
import com.sagacity.portal.user.UserLog;
import com.sagacity.portal.utility.DateUtils;
import com.sagacity.portal.utility.PropertiesFactoryHelper;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.ext.interceptor.Restful;
import com.jfinal.ext.interceptor.SessionInViewInterceptor;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.ehcache.CacheKit;

/**
 * @类名字：CommonController
 * @类描述：
 * @author:Carl.Wu
 * @版本信息：
 * @日期：2013-11-14
 * @Copyright 足下 Corporation 2013 
 * @版权所有
 *
 */
@ControllerBind(controllerKey = "/")
// @Before({ Restful.class, SessionInViewInterceptor.class })
public class LoginController extends BaseController {

	public void index() {
		render("/login.html");
	}
	
	public void banner() {
		Object[] cookieArray = getCookieContext();
		String[] navInfo=getPara("navInfo").split(",");
		if(navInfo[0].equals("m")){
			setAttr("navInfo",Db.findFirst("select ModuleID ID,Caption from sys_moduleinfo where ModuleID=?",navInfo[1]));
		}else if (navInfo[0].equals("a")){
			setAttr("navInfo",Db.findFirst("select AppID ID,AppName Caption from app_baseinfo where AppID=?",navInfo[1]));
		}
		setAttr("userInfo",Db.findFirst(SqlKit.sql("user.getUserInfo"),cookieArray[0]));
		setAttr("resourceUrl", PropertiesFactoryHelper.getInstance()
				.getConfig("resource.url"));
		setAttr("headerFix", PropertiesFactoryHelper.getInstance()
				.getConfig("user.header"));
		
		setAttr("corpID", cookieArray[4]);
		setAttr("logoFix", PropertiesFactoryHelper.getInstance()
				.getConfig("org.logo"));
		
		//获得消息中心与提醒中心数据
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("interface.url")+"GetCorpChannelList";
		try{
			urlString+="?userID="+cookieArray[0]+"&corpID="+cookieArray[4];
			URL url = new URL(urlString);
			HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
			httpConn.setRequestMethod("GET");
			httpConn.connect();    
	        // 取得输入流，并使用Reader读取    
			BufferedReader reader = new BufferedReader(new InputStreamReader(    
	        		httpConn.getInputStream(),"UTF-8"));
	        JSONArray list = JSONObject.fromObject(reader.readLine()).getJSONArray("list");
	        for (int i =0;i<list.size();i++){
	        	Map o = (Map)list.get(i);
	        	if(Integer.parseInt(o.get("ChannelID").toString())==-1){
	        		setAttr("meesageCount",o.get("lNewsCount"));	        		
	        	}else if(Integer.parseInt(o.get("ChannelID").toString())==-2){
	        		setAttr("noticeCount",o.get("lNewsCount"));
	        	}
	        }
	        reader.close();
	        httpConn.disconnect();
		}catch(Exception e){
			e.printStackTrace();
		}
		render("banner.html");
	}
	
	public void footer() {
		render("footer.html");
	}
	
	public void navTree() {
		Object[] cookieArray = getCookieContext();
		setAttr("navInfo",getPara("navInfo").split(","));
		setAttr("modules",Db.find(SqlKit.sql("system.getModules")));
		setAttr("corpApps",Db.find(SqlKit.sql("application.getCorpApps"),cookieArray[4],cookieArray[4]));
		render("navTree.html");
	}
	
	public void register(){
		setAttr("interfaceUrl", PropertiesFactoryHelper.getInstance()
				.getConfig("interface.url"));
		setAttr("type", getPara("type"));
		render("register/register.html");
	}
	
	/**
	 * @方法名:login
	 * @方法描述：用户登录
	 * @author: Carl.Wu
	 * @return: void
	 * @version: 2013-11-14 下午2:47:38
	 * @throws UnsupportedEncodingException 
	 */
	public void login() throws UnsupportedEncodingException {
		String userName = getPara("userName");
		String userPass = getPara("userPass");
		String corpID = getPara("corpID");
		String validateCode = getPara("validateCode"); //验证码，暂时不用。
		Map<String,Object> data = new HashMap<String, Object>();
		if (userName != null && userPass != null && !userName.isEmpty() && !userPass.isEmpty()) {
			Record user = Db.findFirst(SqlKit.sql("user.login"), new Object[] {
					userName, userPass });
			if (user != null && user.getInt("RoleID") == 3 ) {
				/* 考虑增加用户的在线管理
				if(CacheKit.get("UserLoginCache", user.get("UserID")) != null){
					data.put("Result", false);
					data.put("Msg", "用户已登录，请等待已登录用户退出！");
				}else{
					CacheKit.put("UserLoginCache", user.get("UserID"), ul);
				}*/
				String cookiev = new StringBuilder(user.getStr("UserID")).append("&")
						.append(user.getStr("LoginName")).append("&")
						.append(URLEncoder.encode(user.getStr("Caption"),"UTF-8")).append("&")
						.append(user.getInt("RoleID")).append("&")
						.append(corpID).append("&").toString();
				this.setCookie("portal_usercookie", cookiev, 86400000);
				//把登录用户信息写入日志表及在线的ehCache
				UserLog ul = new UserLog().set("UserID", user.get("UserID")).set("LogType", 112)
						.set("OccurTime",DateUtils.nowDateTime()).set("LogDesc", "login from web");
				ul.save();
				data.put("Result", true);
				data.put("Msg", "用户验证成功！");
			}else {
				data.put("Result", false);
				data.put("Msg", "用户名或密码错误！");
			}

		}else{
			data.put("Result", false);
			data.put("Msg", "登录信息不完整！");
		}
		renderJson(data);
	}
	
	public void logout() {
		this.removeCookie("portal_usercookie");
		redirect("/login.html");
	}

}
