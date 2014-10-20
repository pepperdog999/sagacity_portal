package com.sagacity.portal.report;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="reporting_master",pkName="ReportingID")
public class ReportingMaster extends Model<ReportingMaster>{
	private static final long serialVersionUID = 1L;
	public final static ReportingMaster dao = new ReportingMaster();
	
}
