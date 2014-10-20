package com.sagacity.portal.report;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="reporting_attachment",pkName="AttachID")
public class ReportingAttach extends Model<ReportingAttach>{
	private static final long serialVersionUID = 1L;
	public final static ReportingAttach dao = new ReportingAttach();
	
}
