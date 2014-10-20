package com.sagacity.portal.user;

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

@ControllerBind(controllerKey = "/user")
public class UserController extends BaseController implements CommOpration{
	
	@Override
	public void index() {
	
	}
	
	public void userCustomized(){
		Object[] cookieArray = getCookieContext();
		UserSetting us = UserSetting.dao.findFirst("select * from sys_userSetting where UserID=?",cookieArray[0]);
		String[] navInof= "m,2".split(",");
		if(us != null){
			navInof=us.getStr("DefaultModule").split(",");
		}
		if(navInof[0].equals("m")){ //平台模块
			redirect(Db.findFirst("select ModuleID ID,URL from sys_moduleinfo where ModuleID=?",navInof[1]).getStr("URL"));
		}else if(navInof[0].equals("a")){ //应用
			redirect(Db.findFirst("select AppID ID,WEB_URL URL from app_baseinfo where AppID=?",navInof[1]).getStr("URL"));
		}else{
			redirect("/error/error.html");
		}
		
	}
	
	public void getCorpListByLoginName(){
		String loginName = getPara("loginName");
		//Map<Object, Object> data = new HashMap<Object, Object>();
		List<Record> r = Db.find(SqlKit.sql("user.getCorpListByLoginName"),loginName);
		renderJson("Data", r);
	}
	
	public void getDetpUsers(){
		String deptID=getPara("deptID");
		List<Record> r = Db.find(SqlKit.sql("user.getDeptUsers"),deptID);
		renderJson("Data", r);
	}
	
	public void getDetpContacts(){
		String deptID=getPara("deptID");
		List<Record> r = Db.find(SqlKit.sql("user.getDeptContacts"),deptID);
		renderJson("Data", r);
	}
}
