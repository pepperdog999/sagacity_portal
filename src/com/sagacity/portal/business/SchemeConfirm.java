package com.sagacity.portal.business;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="business_schemeConfirm",pkName="SchemeID")
public class SchemeConfirm extends Model<SchemeConfirm>{
	private static final long serialVersionUID = 1L;
	public final static SchemeConfirm dao = new SchemeConfirm();

}
