package com.sagacity.portal.business;

import com.jfinal.ext.plugin.tablebind.TableBind;
import com.jfinal.plugin.activerecord.Model;

@TableBind(tableName="business_clientInfo",pkName="ClientID")
public class ClientInfo extends Model<ClientInfo>{
	private static final long serialVersionUID = 1L;
	public final static ClientInfo dao = new ClientInfo();

}
