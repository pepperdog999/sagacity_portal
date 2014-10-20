package com.sagacity.portal.user;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="sys_users",pkName="UserID")
public class UserInfo extends Model<UserInfo>{
	private static final long serialVersionUID = 1L;
	public final static UserInfo dao = new UserInfo();

}
