package com.sagacity.portal.business;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="business_constructInfo",pkName="ConstructID")
public class ConstructInfo extends Model<ConstructInfo>{
	private static final long serialVersionUID = 1L;
	public final static ConstructInfo dao = new ConstructInfo();

}
