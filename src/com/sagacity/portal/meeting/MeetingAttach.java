package com.sagacity.portal.meeting;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="meeting_attachment",pkName="AttachID")
public class MeetingAttach extends Model<MeetingAttach>{
	private static final long serialVersionUID = 1L;
	public final static MeetingAttach dao = new MeetingAttach();
	
}
