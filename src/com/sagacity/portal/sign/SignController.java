package com.sagacity.portal.sign;

import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.sagacity.portal.comm.BaseController;
import com.sagacity.portal.comm.CommOpration;
import com.sagacity.portal.utility.DateUtils;

@ControllerBind(controllerKey = "/sign")
public class SignController extends BaseController implements CommOpration{
	
	@Override
	//组织通讯录首页
	public void index() {
		setAttr("nowDate",DateUtils.nowDate());
		render("index.html");
	}
	
	public void getSignList(){
		Object[] cookieArray = getCookieContext();
		String signDate = getPara("signDate");
		String sql = "select s.SignDate,s.SignCoordinate,s.SignAddress,s.SignTime from sign_info s \n"
				+ " where s.UserID=? and s.SignDate=?";
		renderJson("list",Db.find(sql,cookieArray[0],signDate));
	}
	
}
