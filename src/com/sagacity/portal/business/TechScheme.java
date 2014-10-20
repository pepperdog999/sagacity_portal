package com.sagacity.portal.business;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="business_techScheme",pkName="TechID")
public class TechScheme extends Model<TechScheme>{
	private static final long serialVersionUID = 1L;
	public final static TechScheme dao = new TechScheme();

}
