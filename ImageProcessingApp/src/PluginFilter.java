

import ij.ImagePlus;
import ij.Prefs;
import ij.Undo;
import ij.gui.Roi;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilter;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;

/**
 * 
 * This is based on ImageJ's PluginFilterRunner
 *
 */

public class PluginFilter {
	private Object filter;
	private ImagePlus imp;
	private int flags, nPasses;
	private boolean snapshotDone = false;
	
	public PluginFilter(Object filter, ImagePlus imp) {
		this.filter = filter;
		this.imp = imp;
		this.flags = ((PlugInFilter)filter).setup("", imp);
	}
	
	public ImagePlus runFilter() {		
		if (filter instanceof ExtendedPlugInFilter) { // calling showDialog required?
			if (snapshotDone)
				Undo.setup(Undo.FILTER, imp);
		}
		if ((flags&PlugInFilter.PARALLELIZE_IMAGES)!=0)
			flags &= ~PlugInFilter.PARALLELIZE_STACKS;

		if ((flags&PlugInFilter.PARALLELIZE_IMAGES)!=0)
			flags &= ~PlugInFilter.PARALLELIZE_STACKS;

		ImageProcessor ip = imp.getProcessor();

		FloatProcessor fp = null;
		prepareProcessor(ip, imp);
		if (filter instanceof ExtendedPlugInFilter)
			((ExtendedPlugInFilter)filter).setNPasses(nPasses);
		if ((flags&PlugInFilter.NO_CHANGES)==0) {	// for filters modifying the image
			boolean disableUndo = Prefs.disableUndo || (flags&PlugInFilter.NO_UNDO)!=0;
			if (!disableUndo) {
				ip.snapshot();
				snapshotDone = true;
			}
		}
		processOneImage(ip, fp, snapshotDone);		// may also set snapShotDone
		if ((flags&PlugInFilter.NO_CHANGES)==0) {	// (filters doing no modifications don't change undo status)
			if (snapshotDone)
				Undo.setup(Undo.FILTER, imp);
			else
				Undo.reset();
		}
		if ((flags&PlugInFilter.NO_CHANGES)==0&&(flags&PlugInFilter.KEEP_THRESHOLD)==0)
			ip.resetBinaryThreshold();
		
		imp.updateAndDraw();
		
		return imp;
	}
	
	private void prepareProcessor(ImageProcessor ip, ImagePlus imp) {
		Roi roi = imp.getRoi();
		if (roi!=null && roi.isArea())
			ip.setRoi(roi);
		else
			ip.setRoi((Roi)null);
		if (imp.getStackSize()>1) {
			ImageProcessor ip2 = imp.getProcessor();
			double min1 = ip2.getMinThreshold();
			double max1 = ip2.getMaxThreshold();
			double min2 = ip.getMinThreshold();
			double max2 = ip.getMaxThreshold();
			if (min1!=ImageProcessor.NO_THRESHOLD && (min1!=min2||max1!=max2))
				ip.setThreshold(min1, max1, ImageProcessor.NO_LUT_UPDATE);
		}
	}
	
	private void processOneImage(ImageProcessor ip, FloatProcessor fp, boolean snapshotDone) {
		Thread thread = Thread.currentThread();
		boolean convertToFloat = (flags&PlugInFilter.CONVERT_TO_FLOAT)!=0 && !(ip instanceof FloatProcessor);
		boolean doMasking = (flags&PlugInFilter.SUPPORTS_MASKING)!=0 && ip.getMask() != null;
		
		if (!snapshotDone && (doMasking || ((flags&PlugInFilter.SNAPSHOT)!=0) && !convertToFloat)) {
			ip.snapshot();
			this.snapshotDone = true;
		}
		
		if (convertToFloat) {
			for (int i=0; i<ip.getNChannels(); i++) {
				fp = ip.toFloat(i, fp);
				fp.setSliceNumber(ip.getSliceNumber());
				
				if (thread.isInterrupted())
					return;
				
				if ((flags&PlugInFilter.SNAPSHOT)!=0)
					fp.snapshot();
				
				((PlugInFilter)filter).run(fp);
				
				if (thread.isInterrupted())
					return;
				
				if ((flags&PlugInFilter.NO_CHANGES)==0) {
					ip.setPixels(i, fp);
				}
			}
		} else {
			((PlugInFilter)filter).run(ip);
		}
		
		if (thread.isInterrupted())
			return;
		
		if (doMasking)
			ip.reset(ip.getMask());	 //restore image outside irregular roi
   }

}