package com.sagacity.portal.utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class PDFReader {
	
	public String MD5 ="";
	public final static PDFReader dao = new PDFReader();
	
	public String convert2Pic(String pdfFilePath, String convertDestPath)  
            throws FileNotFoundException, IOException {
        if( pdfFilePath == null || pdfFilePath.equals("") ) 
        	return "";
        File file = new File(pdfFilePath);
        MD5 = MD5Util.getFileMD5String(file);
        String picSavePath=convertDestPath+MD5+"/";
		if (!new File(picSavePath).exists()) {
			new File(picSavePath).mkdirs();
		}else{ //已存在转换的文档
			return MD5+"/";
		}
        if( file.exists() && file.isFile() ){  
        	convert(file.getAbsolutePath(), convertDestPath);
            return MD5+"/";
        }
        else{  
            return "";
        }
    }
	
	public void convert(String src, String convertDestPath)
			throws FileNotFoundException, IOException {
		PDDocument doc = PDDocument.load(src);
        List<PDPage> pages = doc.getDocumentCatalog().getAllPages(); 
        for(int i=0;i<pages.size();i++){
            PDPage page = pages.get(i); 
            BufferedImage image = page.convertToImage(); 
            Iterator iter = ImageIO.getImageWritersBySuffix("jpg"); 
            ImageWriter writer = (ImageWriter)iter.next(); 
            File outFile = new File(convertDestPath+"/"+MD5+"/"+(i+1)+".jpg"); 
            FileOutputStream out = new FileOutputStream(outFile); 
            ImageOutputStream outImage = ImageIO.createImageOutputStream(out); 
            writer.setOutput(outImage); 
            writer.write(new IIOImage(image,null,null)); 
        }
        doc.close(); 
	}
}
