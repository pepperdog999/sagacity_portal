package com.sagacity.portal.contact;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="zq_contactgroupmember",pkName="MemberID")
public class ContactGroupMember extends Model<ContactGroupMember>{
	private static final long serialVersionUID = 1L;
	public final static ContactGroupMember dao = new ContactGroupMember();
	
}
