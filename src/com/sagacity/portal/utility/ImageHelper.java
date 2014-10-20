package com.sagacity.portal.utility;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageHelper {

	/**
	 * @Description: 将srcImageFile裁剪后生成destImageFile
	 * @param srcImageFile
	 *            原始图
	 * @param destImageFile
	 *            目标图
	 * @param minSize
	 *            目标图宽度或者高度
	 * @return
	 * @throws IOException
	 */
	public static String cut(String srcImageFile, String destImageFile,
			int cutSize) throws IOException {
		Image image = ImageIO.read(new File(srcImageFile));
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		int x = 0;
		int y = 0;
		if (width > height) {
			width = (cutSize * width) / height;
			height = cutSize;
			x = (width - height) / 2;
		} else {
			height = (cutSize * height) / width;
			width = cutSize;
			y = (height - width) / 2;
		}

		BufferedImage bImage = makeThumbnail(image, width, height);
		Rectangle rect = new Rectangle(x, y, cutSize, cutSize);

		// 把原始图片输出
		// ImageIO.write(bImage, "jpg",new File("img/src2.jpg"));
		saveSubImage(bImage, rect, new File(destImageFile));
		return destImageFile;
	}
	
	public static String cut(String srcImageFile, String destImageFile,
			int cutWidth, int cutHeight) throws IOException {
		Image image = ImageIO.read(new File(srcImageFile));
		int width = image.getWidth(null);
		int height = image.getHeight(null);
		int x = 0;
		int y = 0;
		//首先确定缩放基准
		if(cutWidth>cutHeight){
			if (width > height) {
				width = (cutWidth * width) / height;
				height = cutWidth;
				x = (width - height) / 2;
			} else {
				height = (cutWidth * height) / width;
				width = cutWidth;
				y = (height - width) / 2;
			}
		}else{
			if (width > height) {
				width = (cutHeight * width) / height;
				height = cutHeight;
				x = (width - height) / 2;
			} else {
				height = (cutHeight * height) / width;
				width = cutHeight;
				y = (height - width) / 2;
			}
		}

		BufferedImage bImage = makeThumbnail(image, width, height);
		Rectangle rect = new Rectangle(x, y, cutWidth, cutHeight);

		// 把原始图片输出
		// ImageIO.write(bImage, "jpg",new File("img/src2.jpg"));
		saveSubImage(bImage, rect, new File(destImageFile));
		return destImageFile;
	}

	/**
	 * @Description: 将srcImageFile裁剪后生成destImageFile
	 * @param srcImageFile
	 *            原始图
	 * @param destImageFile
	 *            目标图
	 * @param width
	 *            原始图预处理后width
	 * @param height
	 *            原始图预处理后height
	 * @param rect
	 *            目标图输出的格式(rect.x, rect.y, rect.width, rect.height)
	 * @throws IOException
	 */
	public static void cut(File srcImageFile, File destImageFile, int width,
			int height, Rectangle rect) throws IOException {
		Image image = ImageIO.read(srcImageFile);
		BufferedImage bImage = makeThumbnail(image, width, height);

		saveSubImage(bImage, rect, destImageFile);

	}

	/**
	 * @Description: 对原始图片根据(x, y, width, height) = (0, 0, width,
	 *               height)进行缩放，生成BufferImage
	 * @param img
	 * @param width
	 *            预处理后图片的宽度
	 * @param height
	 *            预处理后图片高度
	 * @return
	 * @throws IOException
	 */
	private static BufferedImage makeThumbnail(Image img, int width, int height)
			throws IOException {
		BufferedImage tag = new BufferedImage(width, height, 1);
		Graphics g = tag.getGraphics();
		g.drawImage(img.getScaledInstance(width, height, 4), 0, 0, null);
		g.dispose();
		return tag;
	}

	/**
	 * @Description: 对BufferImage按照(x, y, width, height) = (subImageBounds.x,
	 *               subImageBounds.y, subImageBounds.width,
	 *               subImageBounds.height)进行裁剪
	 *               如果subImageBounds范围过大，则用空白填充周围的区域。
	 * 
	 * @param image
	 * @param subImageBounds
	 * @param destImageFile
	 * @throws IOException
	 */
	private static void saveSubImage(BufferedImage image,
			Rectangle subImageBounds, File destImageFile) throws IOException {
		String fileName = destImageFile.getName();
		String formatName = fileName.substring(fileName.lastIndexOf('.') + 1);
		BufferedImage subImage = new BufferedImage(subImageBounds.width,
				subImageBounds.height, 1);
		Graphics g = subImage.getGraphics();

		if ((subImageBounds.width > image.getWidth())
				|| (subImageBounds.height > image.getHeight())) {
			int left = subImageBounds.x;
			int top = subImageBounds.y;
			if (image.getWidth() < subImageBounds.width)
				left = (subImageBounds.width - image.getWidth()) / 2;
			if (image.getHeight() < subImageBounds.height)
				top = (subImageBounds.height - image.getHeight()) / 2;
			g.setColor(Color.white);
			g.fillRect(0, 0, subImageBounds.width, subImageBounds.height);
			g.drawImage(image, left, top, null);
			ImageIO.write(image, formatName, destImageFile);
		} else {
			g.drawImage(image.getSubimage(subImageBounds.x, subImageBounds.y,
					subImageBounds.width, subImageBounds.height), 0, 0, null);
		}
		g.dispose();
		ImageIO.write(subImage, formatName, destImageFile);
	}

	public static void main(String[] args) throws IOException {
		String picPath = "C:\\Users\\CarlWu\\Desktop\\images (1).jpg";
		String destPath = "C:\\Users\\CarlWu\\Desktop\\result.jpg";
		ImageHelper.cut(picPath, destPath, 200);
	}

}
