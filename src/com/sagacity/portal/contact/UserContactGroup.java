package com.sagacity.portal.contact;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="zq_usercontactgroup",pkName="ContactGroupID")
public class UserContactGroup extends Model<UserContactGroup>{
	private static final long serialVersionUID = 1L;
	public final static UserContactGroup dao = new UserContactGroup();
	
}
