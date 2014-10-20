package com.sagacity.portal.meeting;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="meeting_master",pkName="MeetingID")
public class MeetingMaster extends Model<MeetingMaster>{
	private static final long serialVersionUID = 1L;
	public final static MeetingMaster dao = new MeetingMaster();
	
}
