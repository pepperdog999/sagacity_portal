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


public class Word2Html {
	
	public String imagePath="image";
	public String MD5 ="";
	public final static Word2Html dao = new Word2Html();	
	
	public String convert2Html(String wordFilePath, String convertDestPath)  
	            throws FileNotFoundException, TransformerException, IOException,  
	            ParserConfigurationException {
	        if( wordFilePath == null || wordFilePath.equals("") ) 
	        	return "";
	        File file = new File(wordFilePath);
	        MD5 = MD5Util.getFileMD5String(file);
	        String htmlSavePath=convertDestPath+MD5+"/";
	        if (!new File(htmlSavePath).exists()) {
				new File(htmlSavePath).mkdirs();
			}else{ //已存在转换的文档
				return MD5+"/"+MD5+".html";
			}
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
		
		HWPFDocument wordDocument = new HWPFDocument(is);  
        WordToHtmlConverter converter = new WordToHtmlConverter(  
                DocumentBuilderFactory.newInstance().newDocumentBuilder()  
                        .newDocument());
        converter.setPicturesManager( new PicturesManager()
	    {
             public String savePicture( byte[] content,
                     PictureType pictureType, String suggestedName,
                     float widthInches, float heightInches )
             {
            	 suggestedName=suggestedName.toLowerCase();
            	 //将文档中特殊的图片格式链接修改为转换后的图片格式链接
            	 suggestedName= suggestedName.endsWith("wmf")?StringUtils.replace(suggestedName, "wmf", "svg"):suggestedName;
            	 suggestedName= suggestedName.endsWith("emf")?StringUtils.replace(suggestedName, "emf", "jpg"):suggestedName;
                 return imagePath+"/"+suggestedName;
             }
         } );
        converter.processDocument(wordDocument);
		//save pictures
		List<Picture> pics=wordDocument.getPicturesTable().getAllPictures();
		if(pics != null && pics.size()>0){
			for(Picture pic : pics){
				pic.suggestFileExtension();
				try {
					String picSavePath=convertDestPath+MD5+"/"+imagePath+"/";
					if (!new File(picSavePath).exists()) {
						new File(picSavePath).mkdirs();
					}
					pic.writeImageContent(new FileOutputStream(picSavePath+ pic.suggestFullFileName()));
					String tempname=pic.suggestFullFileName().toLowerCase();
					
					//处理特殊图片文件转换
					if (tempname.endsWith("wmf")) {
						net.arnx.wmf2svg.Main.main(new String[]{picSavePath+ pic.suggestFullFileName(),picSavePath+StringUtils.replace(tempname, "wmf", "svg")});
					}else if(tempname.endsWith("emf")) {
						EMFReader.dao.convert(picSavePath+ pic.suggestFullFileName(), picSavePath+StringUtils.replace(tempname, "emf", "jpg"));
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}  
			}
		}
		Document htmlDocument = converter.getDocument();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DOMSource domSource = new DOMSource(htmlDocument);
		StreamResult streamResult = new StreamResult(out);

		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer serializer = tf.newTransformer();
		serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		serializer.setOutputProperty(OutputKeys.METHOD, "html");
		serializer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "-//W3C//DTD HTML 4.01 Transitional//EN");
		serializer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "http://www.w3.org/TR/html4/loose.dtd");
		serializer.transform(domSource, streamResult);
		out.close();
		writeFile(new String(out.toByteArray(),"UTF-8"), convertDestPath+"/"+MD5+"/"+MD5+".html");			
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
        	Word2Html.dao.convert2Html("/Users/mulaliu/resource/2.doc","/Users/mulaliu/resource/convert/");  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
	
}
