package com.sagacity.portal.system;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="sys_moduleinfo",pkName="ModuleID")
public class ModuleInfo extends Model<ModuleInfo>{
	private static final long serialVersionUID = 1L;
	public final static ModuleInfo dao = new ModuleInfo();

}
