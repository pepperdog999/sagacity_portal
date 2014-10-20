package com.sagacity.portal.business;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="business_superiorReview",pkName="SuperiorID")
public class SuperiorReview extends Model<SuperiorReview>{
	private static final long serialVersionUID = 1L;
	public final static SuperiorReview dao = new SuperiorReview();

}
