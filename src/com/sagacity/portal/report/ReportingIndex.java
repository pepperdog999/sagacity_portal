package com.sagacity.portal.report;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="reporting_index",pkName="IndexID")
public class ReportingIndex extends Model<ReportingIndex>{
	private static final long serialVersionUID = 1L;
	public final static ReportingIndex dao = new ReportingIndex();
	
}
