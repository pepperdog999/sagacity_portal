package com.sagacity.portal.application;

import com.sagacity.portal.utility.PropertiesFactoryHelper;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sagacity.portal.comm.BaseController;
import com.sagacity.portal.utility.StringTool;

@ControllerBind(controllerKey = "/application")
public class ApplicationController extends BaseController {

	@Override
	public void index() {
		Object[] cookieArray = getCookieContext();
		setAttr("userContact",Db.findFirst(SqlKit.sql("user.getContactInfoByUserID"),cookieArray[4],cookieArray[0]));
		render("index.html");	
	}
	
	public void userApp(){
		Object[] cookieArray = getCookieContext();
		setAttr("userContact",Db.findFirst(SqlKit.sql("user.getContactInfoByUserID"),cookieArray[4],cookieArray[0]));
		setAttr("resourceUrl", PropertiesFactoryHelper.getInstance()
				.getConfig("resource.url"));
		setAttr("headerFix", PropertiesFactoryHelper.getInstance()
				.getConfig("user.header"));
		setAttr("userApps",Db.find(SqlKit.sql("application.getCorpApps"),cookieArray[4],cookieArray[4]));
		render("part/user.html");
	}
	
	public void platformApp(){
		Object[] cookieArray = getCookieContext();
		render("part/platform.html");
	}
	
	public void corpAppIndex(){
		String searchKey = getPara("key");
		String appClass = "enterprise";
		String sqlSelect = "select distinct a.AppID,a.AppCode,a.AppName,a.Desc,a.ICON_URL,a.WEB_URL";
		String sqlFrom = "from app_baseinfo a \n"
				+"left join app_corpassign ca on ca.AppCode=a.AppCode \n"
				+"where a.appClass=? and (a.CorpID=? or ca.CorpID=? or a.blnDefault=1) and (a.WEB_URL is not null) and a.intState=1";
		if (StringTool.notNull(searchKey) && !StringTool.isBlank(searchKey)) {
			sqlFrom += " and (a.AppName like '%" + searchKey + "%' or a.AppCode like '%" + searchKey + "%')";
		}
		sqlFrom +=" order by a.AppCode";
		Page<Record> corpApps = Db.paginate(getParaToInt("pageIndex",0) + 1,
				getParaToInt("pageSize",8),sqlSelect, sqlFrom, appClass, getPara("corpID"), getPara("corpID"));		
		renderJson(corpApps);
	}
	
	public void platformAppIndex(){
		String searchKey = getPara("key");
		String appClass = "person";
		String sqlSelect = "select a.AppID,a.AppName,a.AppCode,a.ICON_URL,a.AppType,a.Desc,a.blnDefault,a.intState";
		String sqlFrom = "from app_baseinfo a \n"
				+ "where a.appClass=? and CorpID='0'";
		if (StringTool.notNull(searchKey) && !StringTool.isBlank(searchKey)) {
			sqlFrom += " and (a.AppName like '%" + searchKey + "%' or a.AppCode like '%" + searchKey + "%')";
		}
		sqlFrom +=" order by a.AppCode";
		Page<Record> platformApps = Db.paginate(getParaToInt("pageIndex",0) + 1,
				getParaToInt("pageSize",8),sqlSelect, sqlFrom, appClass);		
		renderJson(platformApps);
	}

}
