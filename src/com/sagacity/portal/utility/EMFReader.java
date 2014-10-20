package com.sagacity.portal.utility;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.freehep.graphicsio.emf.EMFInputStream;
import org.freehep.graphicsio.emf.EMFRenderer;

public class EMFReader {
	
	public final static EMFReader dao = new EMFReader();
	
	public void convert(String src, String dest){
		try{
			EMFInputStream inputStream = new EMFInputStream(new FileInputStream(src), EMFInputStream.DEFAULT_VERSION);
		    EMFRenderer emfRenderer = new EMFRenderer(inputStream);
		    // create buffered image object from EMF render
		    final int width = (int)inputStream.readHeader().getBounds().getWidth();
		    final int height = (int)inputStream.readHeader().getBounds().getHeight();
		    final BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		    Graphics2D g2 = (Graphics2D)result.createGraphics();
		    emfRenderer.paint(g2);
		    File outputfile = new File(dest);
		    ImageIO.write(result, "jpg", outputfile);		    
	    }catch(FileNotFoundException e){
	    	e.printStackTrace();
	    }catch(IOException e){
	    	e.printStackTrace();
	    }
	}
}
