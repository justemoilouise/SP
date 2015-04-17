package ImageHandlers;

import ij.ImagePlus;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import CoreHandler.Prompt;

public class ProcessImage {
	
	public static File upload() {
		File f = null;
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("PNG files", "png");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(filter);
		fc.showDialog(null, "Choose image");
		fc.setVisible(true);

		try {
			f = fc.getSelectedFile();
		}
		catch(Exception e) {
			Prompt.PromptError("ERROR_UPLOAD_IMAGE");
		}
		
		return f;
	}
	
	public static BufferedImage getROI(ImagePlus ip) {
		Rectangle rect = ip.getRoi().getBounds();
		ImageIcon roiImg = new ImageIcon(ip.getImage());
		int iWidth = roiImg.getIconWidth();
		int iHeight = roiImg.getIconHeight();
		BufferedImage bi = new BufferedImage(iWidth, iHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics g = bi.createGraphics();
		g.drawImage(roiImg.getImage(), 0, 0, iWidth, iHeight, null);
		g.dispose();
		bi = bi.getSubimage(rect.x, rect.y, rect.width, rect.height);
		
		return bi;
	}
	
	public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
		int original_width = imgSize.width;
		int original_height = imgSize.height;
		int bound_width = boundary.width;
		int bound_height = boundary.height;
		int new_width = original_width;
		int new_height = original_height;

		if (original_width > bound_width) {
			new_width = bound_width;
			new_height = (new_width * original_height) / original_width;
		}

		if (new_height > bound_height) {
			new_height = bound_height;
			new_width = (new_height * original_width) / original_height;
		}

		return new Dimension(new_width, new_height);
	}

	public static ImageIcon getScaledImage(String imgName, Dimension dim) {
		ImageIcon img = null;
		
		try {
			img = new ImageIcon(ProcessImage.class.getResource(imgName));
		} catch(Exception e) {
			img = new ImageIcon(imgName);
		}
		
		Dimension d = getScaledDimension(new Dimension(img.getIconWidth(), img.getIconHeight()), dim);

		BufferedImage bi = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
		Graphics g1 = bi.createGraphics();
		g1.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
		g1.dispose();
		
		return (new ImageIcon(bi));
	}
	
	public static void saveImage(BufferedImage img, String imgName) {
		try {
			ScreenImage.writeImage(img, imgName);
		} catch (Exception e) {
			Prompt.PromptError("ERROR_SAVE_IMAGE");
		}
	}
}
