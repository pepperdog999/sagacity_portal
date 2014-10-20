package com.sagacity.portal.business;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="business_contractInfo",pkName="ContractID")
public class ContractInfo extends Model<ContractInfo>{
	private static final long serialVersionUID = 1L;
	public final static ContractInfo dao = new ContractInfo();

}
