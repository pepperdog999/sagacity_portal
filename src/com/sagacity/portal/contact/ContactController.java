package com.sagacity.portal.contact;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.sagacity.portal.contact.ContactGroupMember;
import com.sagacity.portal.comm.BaseController;
import com.sagacity.portal.comm.CommOpration;
import com.sagacity.portal.corp.DeptInfo;
import com.sagacity.portal.utility.DateUtils;
import com.sagacity.portal.utility.PropertiesFactoryHelper;

@ControllerBind(controllerKey = "/contact")
public class ContactController extends BaseController implements CommOpration{
	
	@Override
	//组织通讯录首页
	public void index() {
		Object[] cookieArray = getCookieContext();
		setAttr("contactSummary",Db.find(SqlKit.sql("contact.getContactSummary"),cookieArray[4],cookieArray[0]));
		setAttr("userContact",Db.findFirst(SqlKit.sql("user.getContactInfoByUserID"),cookieArray[4],cookieArray[0]));
		setAttr("resourceUrl", PropertiesFactoryHelper.getInstance()
				.getConfig("resource.url"));
		setAttr("headerFix", PropertiesFactoryHelper.getInstance()
				.getConfig("user.header"));
		render("index.html");
	}
	
	//收藏通讯录首页
	public void groupIndex() {
		Object[] cookieArray = getCookieContext();
		setAttr("contactSummary",Db.find(SqlKit.sql("contact.getContactSummary"),cookieArray[4],cookieArray[0]));
		setAttr("userContact",Db.findFirst(SqlKit.sql("user.getContactInfoByUserID"),cookieArray[4],cookieArray[0]));
		setAttr("resourceUrl", PropertiesFactoryHelper.getInstance()
				.getConfig("resource.url"));
		setAttr("headerFix", PropertiesFactoryHelper.getInstance()
				.getConfig("user.header"));
		render("groupIndex.html");
	}
	
	public void contactDetail() {
		int contactID = getParaToInt("contactID");
		setAttr("userContact",Db.findFirst(SqlKit.sql("user.getContactInfoByCCID"),contactID));
		setAttr("resourceUrl", PropertiesFactoryHelper.getInstance()
				.getConfig("resource.url"));
		setAttr("headerFix", PropertiesFactoryHelper.getInstance()
				.getConfig("user.header"));
		render("contactDetail.html");
	}
	
	public void getFullPath(){
		Object[] cookieArray = getCookieContext();
		Map<String,Object> result = new HashMap<String,Object>();
		List<Hashtable> pathList = new ArrayList<Hashtable>();
		Record uc = Db.findFirst(SqlKit.sql("user.getContactInfoByUserID"),cookieArray[4],cookieArray[0]);
		String defaultPathInfo="{\"pathType\":\"dept\",\"pathID\":\""+uc.get("DeptID")+"\"}";
		JSONObject jo = getPara("pathInfo")==null?JSONObject.fromObject(defaultPathInfo):JSONObject.fromObject(getPara("pathInfo"));
		if(jo.getString("pathType").equals("dept")){
			getDeptFullPath(jo.getInt("pathID"),pathList);
		}
		Hashtable<String, Object> hash = new Hashtable<String, Object>();
		hash.put("pathType", "corp");
		hash.put("pathID",uc.get("CorpID"));
		hash.put("pathName", uc.get("CorpName"));
		pathList.add(hash);
		result.put("path_list", pathList);
		renderJson(result);
	}
	
	private void getDeptFullPath(int deptID,List<Hashtable> pathList){
		DeptInfo d = DeptInfo.dao.findById(deptID);
		Hashtable<String, Object> hash = new Hashtable<String, Object>();
		hash.put("pathType", "dept");
		hash.put("pathID", d.get("DeptID"));
		hash.put("pathName", d.get("DeptName"));
		pathList.add(hash);
		if(d.getInt("PDeptID")>0){
			getDeptFullPath(d.getInt("PDeptID"),pathList);
		}else{
			return;
		}
	}
	
	public void getContactList(){
		int pageNumber = getParaToInt("pageNumber",1);
		int pageSize = getParaToInt("pageSize",12);
		String pathType = getPara("pathType");
		String pathID = getPara("pathID");
		Map<String,Object> result = new HashMap<String,Object>();
		List<Record> dataList = new ArrayList<Record>();
		if(pathType.equals("corp")){ //处理节点选择为公司节点的数据
			//获取当前公司的一级部门
			String deptInfoSQL="select 1 Type,d.DeptID ID,d.DeptName Name,IFNULL(cc.CCount,0) Info1,IFNULL(dc.DCount,0) Info2 \n"
					+"from zq_deptinfo d \n"
					+"left join (select c.DeptID,count(c.CContactID) CCount from zq_corpcontact c group by c.deptID) cc on cc.DeptID=d.DeptID \n"
					+"left join (select d.PDeptID,d.CorpID,count(d.DeptID) DCount from zq_deptinfo d group by d.PDeptID,d.CorpID) dc on dc.PDeptID=d.DeptID \n"
					+"where d.PDeptID=0 and d.CorpID=? \n"
					+"order by d.LevelIndex";
			List<Record> dr= Db.find(deptInfoSQL,pathID);
			dataList.addAll(dr);
		}else if(pathType.equals("dept")){ //处理节点选择为部门节点的数据
			//获取当前部门的下级部门
			String deptInfoSQL="select 1 Type,d.DeptID ID,d.DeptName Name,IFNULL(cc.CCount,0) Info1,IFNULL(dc.DCount,0) Info2 \n"
					+"from zq_deptinfo d \n"
					+"left join (select c.DeptID,count(c.CContactID) CCount from zq_corpcontact c group by c.deptID) cc on cc.DeptID=d.DeptID \n"
					+"left join (select d.PDeptID,d.CorpID,count(d.DeptID) DCount from zq_deptinfo d group by d.PDeptID,d.CorpID) dc on dc.PDeptID=d.DeptID \n"
					+"where d.PDeptID=? \n"
					+"order by d.LevelIndex";
			List<Record> dr= Db.find(deptInfoSQL,pathID);
			dataList.addAll(dr);
			//获取当前部门的通讯录数据
			String contactInfoSQL="select 2 Type,cc.CContactID ID,ur.UserID,cc.Name Name,cc.OfficeMail Info2 \n"
					+ ",case when IFNULL(cc.V_NO,'') !='' then CONCAT(cc.MobilePhone1,' (',cc.V_NO,')') else cc.MobilePhone1 end Info1 \n"
					+"from zq_corpcontact cc \n"
					+"left join sys_usercorprelation ur on ur.CContactID=cc.CContactID \n"
					+"where cc.DeptID=? \n"
					+"order by cc.ContactIndex";
			List<Record> cr = Db.find(contactInfoSQL,pathID);
			dataList.addAll(cr);
		}
		result.put("TotalCount", dataList.size());
		result.put("PageNumber", pageNumber);
		result.put("DataList", dataList.subList((pageNumber-1)*pageSize, pageNumber*pageSize>dataList.size()?dataList.size():pageNumber*pageSize));
		renderJson(result);
		
	}
	
	public void getGroupContactList(){
		Object[] cookieArray = getCookieContext();
		int pageNumber = getParaToInt("pageNumber",1);
		int pageSize = getParaToInt("pageSize",12);
		String pathType = getPara("pathType");
		String pathID = getPara("pathID");
		Map<String,Object> result = new HashMap<String,Object>();
		List<Record> dataList = new ArrayList<Record>();
		if(pathType.equals("all")){ //处理节点选择为【所有组】节点的数据
			String groupInfoSQL="select 3 Type,ug.ContactGroupID ID,ug.GroupName Name,IFNULL(cc.CCount,0) Info1 \n"
					+"from zq_usercontactgroup ug \n"
					+"left join (select c.ContactGroupID,count(c.MemberID) CCount from zq_contactgroupmember c group by c.ContactGroupID) cc on cc.ContactGroupID=ug.ContactGroupID \n"
					+"where ug.UserID=?";
			List<Record> dr= Db.find(groupInfoSQL,cookieArray[0]);
			dataList.addAll(dr);
		}else if(pathType.equals("group")){ //处理节点选择为用户组节点的数据
			//获取当前用户组的通讯录数据
			String contactInfoSQL="select 2 Type,cc.CContactID ID,ur.UserID,cc.Name Name,cc.MobilePhone1 Info1,cc.OfficeMail Info2 \n"
					+"from zq_corpcontact cc \n"
					+"left join sys_usercorprelation ur on ur.CContactID=cc.CContactID \n"
					+"left join zq_contactgroupmember gm on gm.CContactID=cc.CContactID \n"
					+"where gm.ContactGroupID=?";
			List<Record> cr = Db.find(contactInfoSQL,pathID);
			dataList.addAll(cr);
		}
		result.put("TotalCount", dataList.size());
		result.put("PageNumber", pageNumber);
		result.put("DataList", dataList.subList((pageNumber-1)*pageSize, pageNumber*pageSize>dataList.size()?dataList.size():pageNumber*pageSize));
		renderJson(result);
		
	}
	
	public void getContactInfoByID(){
		renderJson(CorpContact.dao.findById(getPara("id")));
	}
	
	public void getDeptContactByID(){
		renderJson("Contacts",CorpContact.dao.find("select * from zq_corpcontact where DeptID=?",getPara("id")));
	}

	public void getGroupContactByID(){
		renderJson("Contacts",CorpContact.dao.find("select cc.* from zq_corpcontact cc \n"
				+ "left join zq_contactgroupmember gm on gm.CContactID=cc.CContactID \n"
				+ "where gm.ContactGroupID=?",getPara("id")));
	}

	public void getUserContactGroup() {
		int contactID = getParaToInt("contactID");
		Object[] cookieArray = getCookieContext();
		renderJson("Groups",UserContactGroup.dao.find("select * from zq_usercontactgroup where UserID=?",cookieArray[0]));
	}
	
	public void addUserContactGroup(){
		boolean r = false;
		Map<String,Object> result = new HashMap<String,Object>();
		String groupName = getPara("groupName");
		Object[] cookieArray = getCookieContext();
		UserContactGroup ug = new UserContactGroup().set("UserID", cookieArray[0]).set("GroupName", groupName)
		.set("CreateTime", DateUtils.nowDateTime());
		r= ug.save();
		if(r){
			result.put("Msg", "用户组创建成功！");
			result.put("ContactGroupID", ug.get("ContactGroupID"));
		}else{
			result.put("Msg", "用户组创建失败！");
		}
		result.put("Result", r);
		renderJson(result);		
	}
	
	public void removeUserContactGroup(){
		boolean r = false;
		Map<String,Object> result = new HashMap<String,Object>();
		int contactGroupID = getParaToInt("contactGroupID");
		Db.update("delete from zq_contactgroupmember where ContactGroupID=?",contactGroupID);
		r = UserContactGroup.dao.deleteById(contactGroupID);				
		if(r){
			result.put("Msg", "用户组删除成功！");
		}else{
			result.put("Msg", "用户组删除失败！");
		}
		result.put("Result", r);
		renderJson(result);	
	}
	
	public void addContactGroupMember(){
		boolean r = false;
		Map<String,Object> result = new HashMap<String,Object>();
		int contactID = getParaToInt("contactID");
		int contactGroupID = getParaToInt("contactGroupID");
		if(!(Db.find("select MemberID from zq_contactgroupmember where ContactGroupID=? and CContactID=?",contactGroupID,contactID).size()>0)){
			r = new ContactGroupMember().set("ContactGroupID", contactGroupID).set("CContactID", contactID).set("AddTime", DateUtils.nowDateTime()).save();
		}
		if(r){
			result.put("Msg","通讯录收藏成功！");
		}else{
			result.put("Msg","通讯录收藏失败！");
		}
		result.put("Result", r);
		renderJson(result);
		
	}
	
	public void removeContactGroupMember(){
		Map<String,Object> result = new HashMap<String,Object>();
		int contactID = getParaToInt("contactID");
		int contactGroupID = getParaToInt("contactGroupID");
		if(Db.update("delete from zq_contactgroupmember where ContactGroupID=? and CContactID=?",contactGroupID,contactID)>0){
			result.put("Result", true);
			result.put("Msg","通讯录收藏移除成功！");
		}else{
			result.put("Result", false);
			result.put("Msg","通讯录收藏移除失败！");
		}
		renderJson(result);
		
	}
}
