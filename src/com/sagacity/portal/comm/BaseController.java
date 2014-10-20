package com.sagacity.portal.comm;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.sagacity.portal.utility.PropertiesFactoryHelper;
import com.sagacity.portal.utility.StringTool;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Record;

/**
 * @类名字：BaseController
 * @类描述：
 * @author:Carl.Wu
 * @版本信息：
 * @日期：2013-9-11
 * @Copyright 足下 Corporation 2013 
 * @版权所有
 *
 */
public abstract class BaseController extends Controller {
	protected int pageSize=20;
	protected static String ROOTPATH =new File(BaseController.class.getClassLoader().getResource("").getPath()).getParentFile().getParent();

	public abstract void index();
	
	@Override
	public void render(String view) {
		super.render(view);
	}
	/**
	 * @方法名:getCurrentUser
	 * @方法描述：获取当前系统操作人
	 * @author: Carl.Wu
	 * @return: Record
	 * @version: 2013-9-11 上午11:33:49
	 */
	public Record getCurrentUser(){
		String user_token=getCookie("portal_usercookie");
		System.out.println("当前操作人："+user_token);
		//return (Record)MemcacheTool.mcc.get(user_token);
		return null;
	}
	
	public String getDefaultAppCode(){
		String appCode = PropertiesFactoryHelper.getInstance().getConfig("default.appCode");
		return appCode;
	}
	
	public Object[] getCookieContext(){
		String userCookie=getCookie("portal_usercookie");
		if (StringTool.notBlank(userCookie)&&StringTool.notNull(userCookie)) {
			return userCookie.split("&");
		}
		return null;
	}
}