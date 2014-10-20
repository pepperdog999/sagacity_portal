package com.sagacity.portal.utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import net.arnx.wmf2svg.Main;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.PicturesManager;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.PictureType;
import org.w3c.dom.Document;


public class Excel2Html {
	
	public String MD5 ="";
	public final static Excel2Html dao = new Excel2Html();	
	
	public String convert2Html(String wordFilePath, String convertDestPath)  
	            throws FileNotFoundException, TransformerException, IOException,  
	            ParserConfigurationException {
	        if( wordFilePath == null || wordFilePath.equals("") ) 
	        	return "";
	        File file = new File(wordFilePath);
	        MD5 = MD5Util.getFileMD5String(file);
	        if( file.exists() && file.isFile() ){  
	            convert2Html(new FileInputStream(file), convertDestPath);
	            return MD5+"/"+MD5+".html";
	        }
	        else{  
	            return "";
	        }
	    }
	
	public void convert2Html(InputStream is, String convertDestPath)
			throws TransformerException, IOException,
			ParserConfigurationException{
					
	}
	
	public void writeFile(String content, String path) {
		FileOutputStream fos = null;
		BufferedWriter bw = null;
		try {
			File file = new File(path);
			fos = new FileOutputStream(file);
			bw = new BufferedWriter(new OutputStreamWriter(fos,"UTF-8"));
			bw.write(content);
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fos != null)
					fos.close();
			} catch (IOException ie) {
			}
		}
	}
	
	public static void main(String argv[]) {  
        try {  
        	Excel2Html.dao.convert2Html("/Users/mulaliu/resource/2.excel","/Users/mulaliu/resource/convert/");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
	
}
