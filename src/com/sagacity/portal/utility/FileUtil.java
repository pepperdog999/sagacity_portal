package com.sagacity.portal.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUtil {
	public static boolean deleteFile(File file){  
        File[] files = file.listFiles();  
        for(File deleteFile : files){  
            if(deleteFile.isDirectory()){  
                //如果是文件夹，则递归删除下面的文件后再删除该文件夹  
                if(!deleteFile(deleteFile)){  
                    //如果失败则返回  
                    return false;  
                }  
            } else {  
                if(!deleteFile.delete()){  
                    //如果失败则返回  
                    return false;  
                }  
            }  
        }  
        return file.delete();  
    }
	
	public static File copyFile(File srcFile, File destDir, String newFileName) {  
        if (!srcFile.exists()) {  
            System.out.println("源文件不存在");
            return null; 
        } else if (!destDir.exists()) {  
            System.out.println("目标目录不存在");
            return null; 
        } else if (newFileName == null) {  
        	newFileName = srcFile.getName().substring(0,srcFile.getName().lastIndexOf("."));  
        } 
        try {  
            	File newfile=new File(destDir,newFileName+srcFile.getName().substring(srcFile.getName().lastIndexOf(".")));
                FileChannel fcin = new FileInputStream(srcFile).getChannel();  
                FileChannel fcout = new FileOutputStream(newfile).getChannel();  
                fcin.transferTo(0, fcin.size(), fcout);  
                fcin.close();  
                fcout.close(); 
                srcFile.delete();
                return newfile;
        } catch (FileNotFoundException e) {  
                e.printStackTrace();
                return null;
        } catch (IOException e) {  
                e.printStackTrace();
                return null;
        }     
    }
}
