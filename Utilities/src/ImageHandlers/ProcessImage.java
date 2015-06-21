package ImageHandlers;

import ij.ImagePlus;
import ij.Undo;
import ij.plugin.ImageCalculator;
import ij.plugin.filter.BackgroundSubtracter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.plugin.filter.RankFilters;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

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
	
	public static ImagePlus topHatTransform(ImagePlus imp) {		
		Fast_Filters ff = new Fast_Filters();
		PlugInFilterRunner pfr = new PlugInFilterRunner(ff, "Fast filters", "");		
//		FloatProcessor fp = null;
//		ImageProcessor ip = imp.duplicate().getProcessor();
//		ip.snapshot();
//		processOneImage(ip, fp, true, 8);
//		Undo.setup(Undo.FILTER, imp);
//		ip.resetBinaryThreshold();
//		imp.changes = true;
//		imp.updateAndDraw();
		
		return imp;
	}

	public static ImagePlus getImageDifference(ImagePlus img1, ImagePlus img2) {
		ImageCalculator ic = new ImageCalculator();
		ImagePlus ip = ic.run("difference create", img1, img2); 
		ip.setTitle("Difference - " + img1.getTitle() + " & " + img2.getTitle());
		
		return ip;
	}
	
	public static ImagePlus getImageSubtract(ImagePlus img1, ImagePlus img2) {
		ImageCalculator ic = new ImageCalculator();
		ImagePlus ip = ic.run("subtract create", img1, img2); 
		ip.setTitle("Subtract - " + img1.getTitle() + " & " + img2.getTitle());
		
		return ip;
	}
	
	public static ImagePlus subtractBackground(ImagePlus ip) {
		BackgroundSubtracter bs = new BackgroundSubtracter();
		bs.rollingBallBackground(ip.getProcessor(), 50, false, false, false, false, false);
		
		return new ImagePlus(ip.getTitle() + " - Subtract background", ip.getProcessor());
	}
	
	public static ImageProcessor removeOutliers(ImageProcessor ip) {
		RankFilters rf = new RankFilters();
		rf.rank(ip, 20, RankFilters.OUTLIERS, RankFilters.BRIGHT_OUTLIERS, 50);
		
		return ip;
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
	
	public static ImageIcon getScaledImage(ImageIcon img, Dimension dim) {
		Dimension d = getScaledDimension(new Dimension(img.getIconWidth(), img.getIconHeight()), dim);

		BufferedImage bi = new BufferedImage(d.width, d.height, BufferedImage.TYPE_INT_ARGB);
		Graphics g1 = bi.createGraphics();
		g1.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
		g1.dispose();

		return (new ImageIcon(bi));
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

	private static void processOneImage(ImageProcessor ip, FloatProcessor fp, boolean snapshotDone, int filterType) {
		TopHat th = new TopHat(100, 100, true, 255);
		boolean convertToFloat = true;
		
		if (convertToFloat) {
			for (int i=0; i<ip.getNChannels(); i++) {
				fp = ip.toFloat(i, fp);
				fp.setSliceNumber(ip.getSliceNumber());
				fp.snapshot();
				
				th.transform(fp, filterType);

				ip.setPixels(i, fp);
			}
		} else {
			th.transform(fp, filterType);
		}
   }
}
