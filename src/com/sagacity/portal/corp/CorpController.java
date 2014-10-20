package com.sagacity.portal.corp;

import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.sagacity.portal.comm.BaseController;

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
@ControllerBind(controllerKey = "/corp")

public class CorpController extends BaseController {

	public void index() {
	}
	
	public void getDetpTreeInfo() {
		Object[] cookieArray = getCookieContext();
		renderJson("DeptTree",Db.find(SqlKit.sql("corp.deptList"),cookieArray[4],cookieArray[4]));
	}
	
	public void getDeptInfoByID(){
		renderJson(DeptInfo.dao.findById(getPara("id")));
	}

}
