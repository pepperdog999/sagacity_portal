package com.sagacity.portal.disk;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import net.sf.json.JSONArray;

import com.jfinal.ext.plugin.sqlinxml.SqlKit;
import com.jfinal.ext.route.ControllerBind;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.render.JsonRender;
import com.jfinal.upload.UploadFile;
import com.sagacity.portal.user.UserController;
import com.sagacity.portal.comm.BaseController;
import com.sagacity.portal.comm.CommOpration;
import com.sagacity.portal.utility.DateUtils;
import com.sagacity.portal.utility.PropertiesFactoryHelper;

@ControllerBind(controllerKey = "/disk")
public class DiskController extends BaseController implements CommOpration{
	
	private int fileCount=0;
	
	@Override
	//首页
	public void index() {
		Object[] cookieArray = getCookieContext();
		setAttr("userContact",Db.findFirst(SqlKit.sql("user.getContactInfoByUserID"),cookieArray[4],cookieArray[0]));
		countFiles();
		setAttr("fileCount",fileCount);
		render("index.html");
	}
	
	public void uploadFile(){
		Object[] cookieArray = getCookieContext();
		boolean r= true;
		Map<String,Object> result = new HashMap<String,Object>();
		String dirName = getPara("dir") == null? "" : getPara("dir");
		String rootPath = PropertiesFactoryHelper.getInstance().getConfig("resource.dir")+cookieArray[0]+"/"+dirName+"/";
		UploadFile uploadFile = getFile("upFile", rootPath, 1024*1024*50); //单个文件50M限制
		String path = getPara("path") != null ? getPara("path") : "";
		String currentPath = rootPath + path;
		File f1 = new File(currentPath);
		if (!f1.exists()) {
			f1.mkdirs();
		}
		File nFile = uploadFile.getFile();
		File cFile = new File(f1+"/"+nFile.getName());
		if(cFile.exists() && cFile.isFile()){ //判断是否有同名文件
			r=cFile.renameTo(new File(f1+"/"+nFile.getName()+"."+DateUtils.getLongDateTime()));
		}
		if (nFile!=null && !path.equals("")) {
			try {
				FileUtils.moveFileToDirectory(nFile, f1, true);
			} catch (IOException e) {
				e.printStackTrace();
				r=false;
			}
		}
		if(r){
			result.put("Msg", "文件上传成功！");
			result.put("FileName", nFile.getName());
			String fileExt = nFile.getName().substring(nFile.getName().lastIndexOf(".") + 1).toLowerCase();
			String iconPath=FileType.dao.getFileIconPath(fileExt);
			result.put("IconPath", iconPath);
		}else{
			result.put("Msg", "文件上传失败！");
		}
		result.put("Result", r);
		render(new JsonRender(result).forIE());
	}
	
	public void downFiles(){
		Object[] cookieArray = getCookieContext();
		List<Object> downFileList = new ArrayList<Object>();
		boolean r= false;
		int fileCount=0;
		int folderCount=0;
		Map<String,Object> result = new HashMap<String,Object>();
		String dirName = getPara("dir") == null? "" : getPara("dir");
		String rootPath = PropertiesFactoryHelper.getInstance().getConfig("resource.dir")+cookieArray[0]+"/"+dirName+"/";
		String rootUrl  = PropertiesFactoryHelper.getInstance().getConfig("resource.url")+cookieArray[0]+"/"+dirName+"/";
		String path = getPara("path") != null ? getPara("path") : "";
		String currentPath = rootPath + path;
		String currentUrl = rootUrl + path;
		JSONArray jo =JSONArray.fromObject(getPara("files"));
		for(int i=0;i<jo.size();i++){ 
			Map o=(Map)jo.get(i);
			switch(o.get("Type").toString()){
			case  "file" :
				downFileList.add(o.get("Name"));
				fileCount++;
				break;
			case  "folder" :
				folderCount++;
				break;
			}
		}
		if(fileCount>1 || folderCount>0){
			result.put("Msg", "多个文件，或者包含目录，压缩下载！");
		}else{
			result.put("Msg", "单个文件，直接下载！");
		}
		result.put("DownFileList", downFileList);
		result.put("CurrentUrl", currentUrl);
		renderJson(result);
	}
	
	//对指定目录统计文件数
	public int countFiles(){
		Object[] cookieArray = getCookieContext();
		String rootPath = PropertiesFactoryHelper.getInstance().getConfig("resource.dir")+cookieArray[0]+"/";
		String path = getPara("path") != null ? getPara("path") : "";
		String currentPath = rootPath + path;
		File currentPathFile = new File(currentPath);
		if(!currentPathFile.isDirectory()){
			currentPathFile.mkdir();
		}
        for( File file: new File( currentPath ).listFiles( ) ){
            if( file.isFile( ) ) 
            	fileCount++;
            else if(file.isDirectory() )
            	countFiles(file);
        }
        return fileCount;
	}
	
	private void countFiles(File dir){
		for( File file: dir.listFiles( ) ){
            if( file.isFile( ) ) 
            	fileCount++;
            else if(file.isDirectory() )
            	countFiles(file);
        }
	}
	
	public void delFiles(){
		Object[] cookieArray = getCookieContext();
		boolean r= false;
		Map<String,Object> result = new HashMap<String,Object>();
		String dirName = getPara("dir") == null? "" : getPara("dir");
		String rootPath = PropertiesFactoryHelper.getInstance().getConfig("resource.dir")+cookieArray[0]+"/"+dirName+"/";
		
		String path = getPara("path") != null ? getPara("path") : "";
		String currentPath = rootPath + path;
		JSONArray jo =JSONArray.fromObject(getPara("files"));
		for(int i=0;i<jo.size();i++){ 
			Map o=(Map)jo.get(i);
			switch(o.get("Type").toString()){
			case  "file" :
				r= deleteFile(currentPath+o.get("Name").toString());
				break;
			case  "folder" :
				r= deleteDirectory(currentPath+o.get("Name").toString());
				break;
			}
		}
		if(r){
			result.put("Msg", "删除成功！");
		}else{
			result.put("Msg", "删除失败！");
		}
		result.put("Result", r);
		renderJson(result);		
	}
	
	public boolean deleteFile(String sPath) {  
	    boolean flag = false;  
	    File file = new File(sPath);  
	    // 路径为文件且不为空则进行删除  
	    if (file.isFile() && file.exists()) {  
	        file.delete();  
	        flag = true;  
	    }  
	    return flag;  
	}
	
	public boolean deleteDirectory(String sPath) {
		boolean flag = false;
	    //如果sPath不以文件分隔符结尾，自动添加文件分隔符  
	    if (!sPath.endsWith(File.separator)) {  
	        sPath = sPath + File.separator;  
	    }  
	    File dirFile = new File(sPath);  
	    //如果dir对应的文件不存在，或者不是一个目录，则退出  
	    if (!dirFile.exists() || !dirFile.isDirectory()) {  
	        return flag;  
	    }  
	    //删除文件夹下的所有文件(包括子目录)
	    flag = true;
	    File[] files = dirFile.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        //删除子文件  
	        if (files[i].isFile()) {  
	            flag = deleteFile(files[i].getAbsolutePath());  
	            if (!flag) break;  
	        } //删除子目录  
	        else {  
	            flag = deleteDirectory(files[i].getAbsolutePath());  
	            if (!flag) break;  
	        }  
	    }  
	    if (!flag) return false;  
	    //删除当前目录  
	    if (dirFile.delete()) {  
	        return true;  
	    } else {  
	        return false;  
	    }  
	} 
	
	public void addFolder(){
		Object[] cookieArray = getCookieContext();
		Map<String,Object> result = new HashMap<String,Object>();
		String dirName = getPara("dir") == null? "" : getPara("dir");
		String rootPath = PropertiesFactoryHelper.getInstance().getConfig("resource.dir")+cookieArray[0]+"/"+dirName+"/";
		
		String path = getPara("path") != null ? getPara("path") : "";
		String currentPath = rootPath + path;
		String folderName = getPara("folderName") == null? "新建文件夹" : getPara("folderName");
		
		File newPathFile = new File(currentPath+folderName);
		if(!newPathFile.isDirectory()){
			newPathFile.mkdir();
			result.put("Result", true);
			result.put("Msg", "文件夹创建成功！");
		}else{
			result.put("Result", false);
			result.put("Msg", "文件夹创建失败！");
		}		
		renderJson(result);	
	}
	
	public void fileManager(){
		Object[] cookieArray = getCookieContext();
		int pageNumber = getParaToInt("pageNumber",1);
		int pageSize = getParaToInt("pageSize",10);
		String dirName = getPara("dir") == null? "" : getPara("dir");
		String rootPath = PropertiesFactoryHelper.getInstance().getConfig("resource.dir")+cookieArray[0]+"/"+dirName+"/";
		String rootUrl  = PropertiesFactoryHelper.getInstance().getConfig("resource.url")+cookieArray[0]+"/"+dirName+"/";
		
		//根据path参数，设置各路径和URL
		String path = getPara("path") != null ? getPara("path") : "";
		String currentPath = rootPath + path;
		String currentUrl = rootUrl + path;
		
		/*
		String currentDirPath = path;
		String moveupDirPath = "";
		if (!path.equals("")) {
			String str = currentDirPath.substring(0, currentDirPath.length() - 1);
			moveupDirPath = str.lastIndexOf("/") >= 0 ? str.substring(0, str.lastIndexOf("/") + 1) : "";
		}*/

		//排序形式，name or size or type
		String order = getPara("order") != null ? getPara("order").toLowerCase() : "name";

		//不允许使用..移动到上一级目录
		if (path.indexOf("..") >= 0) {
			System.out.println("Access is not allowed.");
			return;
		}
		//最后一个字符不是/
		if (!path.equals("") && !path.endsWith("/")) {
			System.out.println("Parameter is not valid.");
			return;
		}
		//目录不存在或不是目录
		File currentPathFile = new File(currentPath);
		if(!currentPathFile.isDirectory()){
			//System.out.println("Directory does not exist.");
			//return;
			currentPathFile.mkdir();
		}

		//遍历目录取的文件信息
		List<Hashtable> fileList = new ArrayList<Hashtable>();
		if(currentPathFile.listFiles() != null) {
			for (File file : currentPathFile.listFiles()) {
				Hashtable<String, Object> hash = new Hashtable<String, Object>();
				String fileName = file.getName();
				if(file.isDirectory() && fileName.equalsIgnoreCase("files")){ //针对系统目录特殊处理
					continue;
				}					
				String fileExt = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
				String fileIconPath=FileType.dao.getFileIconPath(fileExt);
				String folderIconPath="folder/folder.gif";
				DecimalFormat df = new DecimalFormat("#.00 KB");
				if(file.isDirectory()) {
					hash.put("is_dir", true);
					hash.put("has_file", (file.listFiles() != null));
					hash.put("filesize", 0L);
					//判断是否系统文件夹【files】，用特殊图标显示。
					//folderIconPath = fileName.equals("files") ?  "folder/folder_sync.gif" : "folder/folder.gif";
					hash.put("icon_path", folderIconPath);
				} else if(file.isFile()){
					hash.put("is_dir", false);
					hash.put("has_file", false);
					hash.put("filesize", df.format(file.length()/1024));
					hash.put("icon_path", fileIconPath);
				}
				hash.put("filename", fileName);
				hash.put("datetime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified()));
				fileList.add(hash);
			}
		}

		if ("size".equals(order)) {
			Collections.sort(fileList, new SizeComparator());
		} else if ("type".equals(order)) {
			Collections.sort(fileList, new TypeComparator());
		} else {
			Collections.sort(fileList, new NameComparator());
		}
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("moveup_dir_path", "");
        result.put("current_dir_path",currentPath);
		result.put("current_url", currentUrl);
        result.put("total_count", fileList.size());
        result.put("page_number", pageNumber);
        result.put("file_list", fileList.subList((pageNumber-1)*pageSize
        		, pageNumber*pageSize>fileList.size()?fileList.size():pageNumber*pageSize));
        renderJson(result);

	}
	
	public class NameComparator implements Comparator {
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable)a;
			Hashtable hashB = (Hashtable)b;
			if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
				return 1;
			} else {
				return ((String)hashA.get("filename")).compareTo((String)hashB.get("filename"));
			}
		}
	}
	public class SizeComparator implements Comparator {
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable)a;
			Hashtable hashB = (Hashtable)b;
			if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
				return 1;
			} else {
				if (((Long)hashA.get("filesize")) > ((Long)hashB.get("filesize"))) {
					return 1;
				} else if (((Long)hashA.get("filesize")) < ((Long)hashB.get("filesize"))) {
					return -1;
				} else {
					return 0;
				}
			}
		}
	}
	public class TypeComparator implements Comparator {
		public int compare(Object a, Object b) {
			Hashtable hashA = (Hashtable)a;
			Hashtable hashB = (Hashtable)b;
			if (((Boolean)hashA.get("is_dir")) && !((Boolean)hashB.get("is_dir"))) {
				return -1;
			} else if (!((Boolean)hashA.get("is_dir")) && ((Boolean)hashB.get("is_dir"))) {
				return 1;
			} else {
				return ((String)hashA.get("filetype")).compareTo((String)hashB.get("filetype"));
			}
		}
	}
	
}
