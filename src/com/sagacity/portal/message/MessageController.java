package com.sagacity.portal.message;

import java.util.HashMap;
import java.util.Map;

import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sagacity.portal.utility.StringTool;
import com.sagacity.portal.comm.BaseController;
import com.sagacity.portal.comm.CommOpration;
import com.sagacity.portal.utility.DateUtils;

@ControllerBind(controllerKey = "/message")
public class MessageController extends BaseController implements CommOpration{
	
	@Override
	//组织通讯录首页
	public void index() {
		render("index.html");
	}
	
	public void getMessageSummary(){
		Object[] cookieArray = getCookieContext();
		renderJson(Db.findFirst(SqlKit.sql("message.GetMessageSummary"),cookieArray[0],cookieArray[4]));
	}
	
	public void getUserGroupingMessage(){
		Object[] cookieArray = getCookieContext();
		String SqlSelect=SqlKit.sql("message.GetUserGroupingMessageList_Select");
		String SqlFrom=SqlKit.sql("message.GetUserGroupingMessageList_From");
		if (StringTool.notNull(getPara("pageNumber"))&&StringTool.notBlank(getPara("pageNumber"))){
			renderJson(Db.paginate(getParaToInt("pageNumber"), getParaToInt("pageSize",10), SqlSelect, SqlFrom,cookieArray[0],cookieArray[0],cookieArray[0],cookieArray[0]));
		}else{
			renderJson("list",Db.find(SqlSelect+" \n"+SqlFrom,cookieArray[0],cookieArray[0],cookieArray[0],cookieArray[0]));
		}
	}
	
	public void getInteractMessage(){
		setAttr("authorID",getPara("authorID"));
		setAttr("nowDate",DateUtils.nowDate());
		setAttr("urCount",getPara("urCount"));
		render("../message/interactMessage.html");
	}
	
	public void getLastInteractDateByAuthorID() {
		Object[] cookieArray = getCookieContext();
		Map<Object, Object> data = new HashMap<Object, Object>();
		Record r = Db.findFirst(SqlKit.sql("message.GetLastInteractDateByAuthorID"),cookieArray[0],getPara("authorID"),getPara("authorID"),cookieArray[0]);
		if(r.getStr("LastDate") != null){
			data.put("Success", true);
			data.put("LastDate", r.getStr("LastDate"));
		}else
			data.put("Success", false);
		renderJson(data);
	}
	
	public void getNextInteractDateByAuthorID() {
		Object[] cookieArray = getCookieContext();
		Map<Object, Object> data = new HashMap<Object, Object>();
		Record r = Db.findFirst(SqlKit.sql("message.GetNextInteractDateByAuthorID"),cookieArray[0],getPara("authorID"),getPara("authorID"),cookieArray[0],getPara("lastDate"));
		if(r.getStr("NextDate") != null){
			data.put("Success", true);
			data.put("NextDate", r.getStr("NextDate"));
		}else
			data.put("Success", false);
		renderJson(data);
	}
	
	public void getInteractMessageByAuthorID() {
		Object[] cookieArray = getCookieContext();
		Map<Object, Object> data = new HashMap<Object, Object>();
		data.put("List", Db.find(SqlKit.sql("message.GetInteractMessageListByAuthorID"),cookieArray[0],getPara("yearMonth"),cookieArray[0],getPara("authorID"),getPara("authorID"),cookieArray[0]));
		renderJson(data);
	}
	
}
