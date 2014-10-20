package com.sagacity.portal.meeting;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="meeting_index",pkName="IndexID")
public class MeetingIndex extends Model<MeetingIndex>{
	private static final long serialVersionUID = 1L;
	public final static MeetingIndex dao = new MeetingIndex();
	
}
