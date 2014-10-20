package com.sagacity.portal.business;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="business_commercialReview",pkName="CommercialID")
public class CommercialReview extends Model<CommercialReview>{
	private static final long serialVersionUID = 1L;
	public final static CommercialReview dao = new CommercialReview();

}
