package com.sagacity.portal.notice;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="notice_master",pkName="NoticeID")
public class NoticeMaster extends Model<NoticeMaster>{
	private static final long serialVersionUID = 1L;
	public final static NoticeMaster dao = new NoticeMaster();
	
}
