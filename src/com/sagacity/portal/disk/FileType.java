package com.sagacity.portal.disk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class FileType {
	
	public final static FileType dao = new FileType();
	//文件扩展名
	
	String unknownFileType = "unknown";
	String[] folderType = new String[]{"folder_document","folder_message","folder_music","folder_sync","folder_video","folder"};
	
	Map<String,Object> fileTypes = new HashMap<String,Object>();
	String[] applicationTypes = new String[]{"apk","bat","exe","ipa","msi"};
	String[] archiveTypes = new String[]{"7z","cab","dmg","iso","rar","zip"};
	String[] codeTypes = new String[]{"asp","code","cpp","css","html","js","php","tpl","xml"};
	String[] documentTypes = new String[]{"chm","doc","docx","log","pdf","ppt","pptx","txt","xls","xlsx"};
	String[] imageTypes = new String[]{"gif","img","jpg","jpeg","png","bmp"};
	String[] musicTypes = new String[]{"mp3","music","wma"};
	String[] sourceTypes = new String[]{"ai","fla","psd","swf"};
	String[] videoTypes = new String[]{"3gp","avi","flv","mov","mpg","rm","rmvb","video","wmv"};
	
	public FileType(){
		fileTypes.put("application",applicationTypes);
		fileTypes.put("archive",archiveTypes);
		fileTypes.put("code",codeTypes);
		fileTypes.put("document",documentTypes);
		fileTypes.put("image",imageTypes);
		fileTypes.put("music",musicTypes);
		fileTypes.put("source",sourceTypes);
		fileTypes.put("video",videoTypes);		
	}
	
	public String getFileIconPath(String fileExt){
		Iterator iter = fileTypes.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = entry.getKey().toString();
			String[] value = (String[]) entry.getValue();
			if(Arrays.<String>asList(value).contains(fileExt)){
				return key+"/"+fileExt+".gif";
			}
		}
		return unknownFileType+".gif";
	}
	
	
}
