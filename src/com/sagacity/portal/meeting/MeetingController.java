package com.sagacity.portal.meeting;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.jfinal.aop.Before;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.render.JsonRender;
import com.jfinal.upload.UploadFile;
import com.sagacity.portal.comm.BaseController;
import com.sagacity.portal.comm.CommOpration;
import com.sagacity.portal.utility.DateUtils;
import com.sagacity.portal.utility.PropertiesFactoryHelper;

@ControllerBind(controllerKey = "/meeting")
public class MeetingController extends BaseController implements CommOpration{
	
	@Override
	//组织通讯录首页
	public void index() {
		setAttr("nowDate",DateUtils.nowDate());
		render("index.html");
	}
	
	public void getMeetingList(){
		Object[] cookieArray = getCookieContext();
		String firstDay = getPara("firstDay");
		String lastDay = getPara("lastDay");
		String sql = SqlKit.sql("meeting.getMeetingList");
		renderJson("list",Db.find(sql,cookieArray[0],firstDay,lastDay));
	}
	
	public void meetingDetail(){
		int meetingID = getParaToInt("meetingID");
		Object[] cookieArray = getCookieContext();
		
		setAttr("userID",cookieArray[0]);
		setAttr("meetingMaster",Db.findFirst("select mm.*,u.Caption,IFNULL(mui.MarkImportantTime,'') MarkImportantTime,IFNULL(mui.MarkSignTime,'')  MarkSignTime \n"
				+ "from meeting_master mm \n"
				+ "left join sys_users u on u.UserID=mm.CreateUserID \n"
				+ "left join meeting_userindex mui on mui.MeetingID=mm.MeetingID \n"
				+ "where mm.MeetingID=? and mui.UserID=?",meetingID,cookieArray[0]));
		setAttr("meetingIndex",MeetingIndex.dao.find("select * from meeting_index where MeetingID=?",meetingID));
		setAttr("meetingAttach",MeetingAttach.dao.find("select * from meeting_attachment where MeetingID=?",meetingID));
		setAttr("resourceUrl", PropertiesFactoryHelper.getInstance()
				.getConfig("resource.url"));
		render("meetingDetail.html");
	}

	//上传附件
	public void uploadAttach() {
		boolean r = false;
		Object[] cookieArray = getCookieContext();
		JSONObject map = new JSONObject();
		String config_dir = PropertiesFactoryHelper.getInstance().getConfig("resource.dir");
		String folderName = cookieArray[0]+"/files/meeting/"+DateUtils.getLongDate();
		File f1 = new File(config_dir+folderName);
		if (!f1.exists()) {
			f1.mkdirs();
		}
		UploadFile uploadFile = getFile("attachFile", f1.getAbsolutePath());
		int meetingID = getParaToInt("meetingID");
		File nFile = uploadFile.getFile();
		if (nFile==null) {
			map.put("Result", r);
			map.put("Msg", "附件上传失败！");
		}else{
			//插入附件表
			String attachURL = folderName+"/"+nFile.getName();
			String attachName = nFile.getName();
			MeetingAttach at = new MeetingAttach().set("MeetingID", meetingID).set("AttachmentURL", attachURL)
					.set("AttachmentName", attachName).set("AddTime", DateUtils.nowDateTime());
			if(r=at.save()){
				map.put("AttachInfo", at.toJson());
				map.put("Msg", "附件上传成功！");
			}else{
				map.put("Msg", "附件上传失败！");
			}
			map.put("Result", r);
		}
		render(new JsonRender(map).forIE());
	}
	
	//获得AttachID对应的文件URL
	public void getAttachFileURL(){
		Map<String, Object> map = new HashMap<String, Object>();
		String rootUrl = PropertiesFactoryHelper.getInstance().getConfig("resource.url");
		String rootPath = PropertiesFactoryHelper.getInstance().getConfig("resource.dir");
		int attachID = getParaToInt("attachID");
		MeetingAttach ma = MeetingAttach.dao.findById(attachID);
        map.put("FileURL", rootUrl+ma.get("AttachmentURL"));
        map.put("FilePath", rootPath+ma.getStr("AttachmentURL").substring(0,ma.getStr("AttachmentURL").lastIndexOf("/")+1));
        map.put("FileName", ma.get("AttachmentName"));
		renderJson(map);
	}
	
	//删除附件
	public void delAttachFile(){
		boolean r = false;
		Map<String, Object> map = new HashMap<String, Object>();
		int attachID = getParaToInt("attachID");
		r = MeetingAttach.dao.deleteById(attachID);
		if(r){
			map.put("Msg", "附件删除成功！");
		}else{
			map.put("Msg", "附件删除失败！");
		}
		map.put("Result", r);
		renderJson(map);
	}

	@Before(Tx.class)
	public void addMeeting() {
		Object[] cookieArray = getCookieContext();
		JSONObject map = new JSONObject();
		String content = getPara("content");
		String detail = getPara("detail");
		String location = getPara("location");
		String dateTime = getPara("dateTime");
		String attachArray = getPara("attachArray");
		String indexData = getPara("indexData");
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("interface.url")+"CreateMeeting";
		try{
			urlString+="?userID="+cookieArray[0]+"&meetingContent="+URLEncoder.encode(content,"UTF-8")+"&meetingDetail="+URLEncoder.encode(detail,"UTF-8")
					+"&meetingLocation="+URLEncoder.encode(location,"UTF-8")+"&attachArray="+attachArray
					+"&meetingDate="+dateTime.substring(0,10)+"&meetingTime="+dateTime.substring(11,16)+"&indexData="+URLEncoder.encode(indexData,"UTF-8");
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

	//标记会议为重要或取消重要
	public void markImportant(){
		int meetingID = getParaToInt("meetingID");
		Object[] cookieArray = getCookieContext();
		int operateType = getParaToInt("operateType");
		if(operateType==1){
			Db.update("update meeting_userindex set MarkImportantTime=now() where MeetingID=? and UserID=?",meetingID,cookieArray[0]);
			renderJson("Msg","设置重要成功！");
		}else if(operateType==2){
			Db.update("update meeting_userindex set MarkImportantTime=null where MeetingID=? and UserID=?",meetingID,cookieArray[0]);
			renderJson("Msg","取消重要成功！");
		}
	}
	
	public void delMeeting(){
		int meetingID = getParaToInt("meetingID");
		JSONObject map = new JSONObject();
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("interface.url")+"DelMeeting";
		try{
			urlString+="?meetingID="+meetingID;
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
	
	public void markSign(){
		int meetingID = getParaToInt("meetingID");
		Object[] cookieArray = getCookieContext();
		Db.update("update meeting_userindex set MarkSignTime=now() where MeetingID=? and UserID=?",meetingID,cookieArray[0]);
		renderJson("Msg","会议签收成功！");
	}
}
