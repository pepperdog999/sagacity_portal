package com.sagacity.portal.contact;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="zq_corpcontact",pkName="CContactID")
public class CorpContact extends Model<CorpContact>{
	private static final long serialVersionUID = 1L;
	public final static CorpContact dao = new CorpContact();
	
}
