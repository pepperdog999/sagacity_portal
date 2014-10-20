package com.sagacity.portal.notice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import net.sf.json.JSONObject;

import com.jfinal.aop.Before;
import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.sagacity.portal.comm.BaseController;
import com.sagacity.portal.comm.CommOpration;
import com.sagacity.portal.utility.PropertiesFactoryHelper;

@ControllerBind(controllerKey = "/notice")
public class NoticeController extends BaseController implements CommOpration{
	
	@Override
	//首页
	public void index() {
		Object[] cookieArray = getCookieContext();
		setAttr("noticeSummary",Db.find(SqlKit.sql("notice.getNoticeSummary"),cookieArray[0],cookieArray[0]));
		setAttr("userContact",Db.findFirst(SqlKit.sql("user.getContactInfoByUserID"),cookieArray[4],cookieArray[0]));
		setAttr("resourceUrl", PropertiesFactoryHelper.getInstance()
				.getConfig("resource.url"));
		setAttr("headerFix", PropertiesFactoryHelper.getInstance()
				.getConfig("user.header"));
		render("index.html");
	}
	
	public void getProcessingList() {
		int pageNumber = getParaToInt("pageNumber") == null ? 1 : getParaToInt("pageNumber");
		int pageSize = getParaToInt("pageSize") == null ? 5 : getParaToInt("pageSize");
		Object[] cookieArray = getCookieContext();
		
		Page<Record> notices = Db.paginate(pageNumber, pageSize, SqlKit.sql("notice.getProcessingList-select")
				, SqlKit.sql("notice.getProcessingList-from"),cookieArray[0]);
		renderJson(notices);
	}
	
	public void getCreateList() {
		int pageNumber = getParaToInt("pageNumber") == null ? 1 : getParaToInt("pageNumber");
		int pageSize = getParaToInt("pageSize") == null ? 5 : getParaToInt("pageSize");
		Object[] cookieArray = getCookieContext();
		
		Page<Record> coordinates = Db.paginate(pageNumber, pageSize, SqlKit.sql("notice.getCreateList-select")
				, SqlKit.sql("notice.getCreateList-from"),cookieArray[0]);
		renderJson(coordinates);
	}
	
	@Before(Tx.class)
	public void addNotice() {
		Object[] cookieArray = getCookieContext();
		JSONObject map = new JSONObject();
		String contents = getPara("contents");
		String dateTime = getPara("dateTime");
		String indexData = getPara("indexData");
		String urlString =PropertiesFactoryHelper.getInstance()
				.getConfig("interface.url")+"AddNotice";
		try{
			urlString+="?userID="+cookieArray[0]+"&noticeTitle="+URLEncoder.encode(contents,"UTF-8")+"&sourceType=0"
					+"&noticeDate="+dateTime.substring(0,10)+"&noticeTime="+dateTime.substring(11,16)+"&indexData="+URLEncoder.encode(indexData,"UTF-8");
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
}
