package com.sagacity.portal.user;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="sys_userlog",pkName="LogID")
public class UserLog extends Model<UserLog>{
	//11-手机端登录；12-手机端更改密码；32-用户检查客户端更新； 99-手机端注册；112-web端登录;
	
	private static final long serialVersionUID = 1L;
	public final static UserLog dao = new UserLog();

}
