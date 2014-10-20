package com.sagacity.portal.utility;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;


public class PPT2Png {
	
	public String MD5 ="";
	public final static PPT2Png dao = new PPT2Png();
	
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
        	convert(new FileInputStream(file.getAbsolutePath()), convertDestPath);
            return MD5+"/";
        }
        else{  
            return "";
        }
    }
	
	public void convert(FileInputStream is, String convertDestPath)
			throws FileNotFoundException, IOException {
		HSLFSlideShow show = new HSLFSlideShow(is);
		SlideShow ppt = new SlideShow(show);
		Dimension pgsize = ppt.getPageSize();
		Slide[] slide = ppt.getSlides();

		for (int i = 0; i < slide.length; i++) {
			TextRun[] textRuns = slide[i].getTextRuns();
			for (TextRun textRun : textRuns) {
				for (RichTextRun richTextRun : textRun.getRichTextRuns()) {
					richTextRun.setFontName("宋体");// 
				}
			}

			BufferedImage img = new BufferedImage(pgsize.width, pgsize.height,
					BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics = img.createGraphics();
			// clear the drawing area
			int backgroundColor = slide[i].getColorScheme()
					.getAccentAndHyperlinkColourRGB();
			graphics.setPaint(new Color(backgroundColor));
			graphics.fill(new Rectangle2D.Float(0, 0, pgsize.width,
					pgsize.height));

			// render the background
			slide[i].getBackground().draw(graphics);
			// render the content
			slide[i].draw(graphics);

			String outFile = convertDestPath+"/"+MD5+"/orignal_"+(i+1)+".png";
			FileOutputStream out = null;
			try {
				out = new FileOutputStream(outFile);
				javax.imageio.ImageIO.write(img, "png", out);
				String resizedFile = convertDestPath+"/"+MD5+"/preview_"+(i+1)+".png";
				ImageUtil.resize(new File(outFile), new File(resizedFile), 96, new Float(0.8));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					out.close();
				} catch (Exception e) {
					// Nothing
				}
			}
		}
	}
}
