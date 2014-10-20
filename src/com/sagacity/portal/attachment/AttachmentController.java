package com.sagacity.portal.attachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.render.JsonRender;
import com.jfinal.upload.UploadFile;
import com.sagacity.portal.utility.PropertiesFactoryHelper;
import com.sagacity.portal.user.UserController;
import com.sagacity.portal.comm.BaseController;
import com.sagacity.portal.utility.DateUtils;

/**
 * @类名字：CommonController
 * @类描述：
 * @author:Carl.Wu
 * @版本信息：
 * @日期：2013-11-14
 * @Copyright 足下 Corporation 2013 
 * @版权所有
 *
 */
@ControllerBind(controllerKey = "/attachment")

public class AttachmentController extends BaseController {
	
	private static String ROOTPATH =new File(UserController.class.getClassLoader().getResource("").getPath()).getParentFile().getParent();

	public void index() {
	}
	
	//此方法用于各应用上传附件
	public void uploadAttach() {
		Object[] cookieArray = getCookieContext();
		String config_dir = PropertiesFactoryHelper.getInstance().getConfig("resource.dir");
		String fileName= getPara("fileName");
		String folderName = getPara("moduleName") == null ? cookieArray[0]+"/files/innerApp/"+DateUtils.getLongDate() 
				: cookieArray[0]+"/files/"+getPara("moduleName")+"/"+DateUtils.getLongDate();
		Map<String, Object> map = new HashMap<String, Object>();
		File f1 = new File(config_dir+folderName);
		if (!f1.exists()) {
			f1.mkdirs();
		}
		UploadFile uploadFile = getFile(fileName, f1.getAbsolutePath());
		File nFile = uploadFile.getFile();
		if (nFile==null) {
			map.put("Result", false);
			map.put("Msg", "附件上传失败！");
		}else{
			map.put("Result", true);
			map.put("Msg", "附件上传成功！");
			map.put("FileURL", folderName+"/"+nFile.getName());
			map.put("FileName", nFile.getName());
		}
		render(new JsonRender(map).forIE());
	}

}
