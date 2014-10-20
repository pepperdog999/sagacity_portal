package com.sagacity.portal.application;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="app_corpassign",pkName="AssignID")
public class CorpAssign extends Model<CorpAssign>{
	private static final long serialVersionUID = 1L;
	public final static CorpAssign dao = new CorpAssign();
	
}
