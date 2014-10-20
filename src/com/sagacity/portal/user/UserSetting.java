package com.sagacity.portal.user;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="sys_usersetting",pkName="SettingID")
public class UserSetting extends Model<UserSetting>{
	private static final long serialVersionUID = 1L;
	public final static UserSetting dao = new UserSetting();

}
