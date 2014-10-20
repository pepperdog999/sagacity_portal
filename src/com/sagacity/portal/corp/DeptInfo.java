package com.sagacity.portal.corp;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="zq_deptinfo",pkName="DeptID")
public class DeptInfo extends Model<DeptInfo>{
	private static final long serialVersionUID = 1L;
	public final static DeptInfo dao = new DeptInfo();

}
