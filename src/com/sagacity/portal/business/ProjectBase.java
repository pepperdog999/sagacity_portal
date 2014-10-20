package com.sagacity.portal.business;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="business_projectBase",pkName="ProjectID")
public class ProjectBase extends Model<ProjectBase>{
	private static final long serialVersionUID = 1L;
	public final static ProjectBase dao = new ProjectBase();

}
