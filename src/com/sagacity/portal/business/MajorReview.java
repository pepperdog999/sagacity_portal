package com.sagacity.portal.business;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="business_majorReview",pkName="MajorID")
public class MajorReview extends Model<MajorReview>{
	private static final long serialVersionUID = 1L;
	public final static MajorReview dao = new MajorReview();

}
