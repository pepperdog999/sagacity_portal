package com.sagacity.portal.notice;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="notice_index",pkName="IndexID")
public class NoticeIndex extends Model<NoticeIndex>{
	private static final long serialVersionUID = 1L;
	public final static NoticeIndex dao = new NoticeIndex();
	
}
