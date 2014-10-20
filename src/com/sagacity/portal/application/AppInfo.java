package com.sagacity.portal.application;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="app_baseinfo",pkName="AppID")
public class AppInfo extends Model<AppInfo>{
	private static final long serialVersionUID = 1L;
	public final static AppInfo dao = new AppInfo();
	
}
