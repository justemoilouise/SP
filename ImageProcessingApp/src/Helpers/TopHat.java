package Helpers;


import java.awt.Rectangle;

import ij.IJ;
import ij.plugin.filter.RankFilters;
import ij.process.ImageProcessor;

/**
 * 
 * 
 * This is based on the ImageJ plugin Fast_Filters by Michael Schmid
 *
 */

public class TopHat {
	private int x, y;
	private boolean subtract;
	private float offset;

	private int maxThreads = Runtime.getRuntime().availableProcessors(); 
	private final int MEAN=0, BORDER_LIMITED_MEAN=1, MEDIAN=2, MIN=3, MAX=4;
	private final int[][] taskLists = new int[][] {
        {MEAN},
        {BORDER_LIMITED_MEAN},
        {MEDIAN},
        {MIN},
        {MAX},
        {MIN, MAX},
        {MAX, MIN},
        {MIN, MAX, BORDER_LIMITED_MEAN},
        {MAX, MIN, BORDER_LIMITED_MEAN},
        {MEDIAN, BORDER_LIMITED_MEAN}
    };
	
	public TopHat(int x, int y, boolean subtract, float offset) {
		this.x = x;
		this.y = y;
		this.subtract = subtract;
		this.offset = offset;
	}
	
	public void transform(ImageProcessor ip, int type) {
		int width = ip.getWidth();
        Rectangle roiRect = ip.getRoi();
        
		int[] taskList = taskLists[type];
		int nTasks = taskList.length;
		
		for (int iTask=0; iTask<nTasks; iTask++) {
            if (x>0)
                filterFloat(ip, taskList[iTask], x, true, x*(nTasks-iTask-1), y*(nTasks-iTask), maxThreads);
            if (y>0)
                filterFloat(ip, taskList[iTask], y, false, x*(nTasks-iTask-1), y*(nTasks-iTask-1), maxThreads);
            if (Thread.currentThread().isInterrupted()) return; // interruption for new parameters during preview?
        }
        if (subtract) {
            float[] pixels = (float[])ip.getPixels();
            float[] snapPixels = (float[])ip.getSnapshotPixels();
            
            for (int y=roiRect.y; y<roiRect.y+roiRect.height; y++)
                for (int x=roiRect.x, p=x+y*width; x<roiRect.x+roiRect.width; x++,p++)
                    pixels[p] = snapPixels[p] - pixels[p] + offset;
        }
	}
	
	private void filterFloat(ImageProcessor ip, final int type, final int radius, boolean xDirection,
            int extraX, int extraY, int maxThreads) {
		final float sign = (type==MIN) ? -1 : 1;
        final int width = ip.getWidth();
        final int height = ip.getHeight();
        Rectangle roiRect = (Rectangle)ip.getRoi().clone();
        roiRect.grow(extraX, extraY);
        Rectangle rect = roiRect.intersection(new Rectangle(width, height));

        final float[] pixels = (float[])ip.getPixels();
        final int length = xDirection ? width : height; //number of points per line (line can be a row or column)
        final int pointInc = xDirection ? 1 : width;    //increment of the pixels array index to the next point in a line
        final int lineInc = xDirection ? width : 1;     //increment of the pixels array index to the next line
        final int lineFrom = Math.max((xDirection ? rect.y : rect.x), 0);  //the first line to process
        int totalLines = (xDirection ? height:width);
        final int lineTo = Math.min((xDirection ? rect.y+rect.height : rect.x+rect.width), totalLines); //the last line+1 to process
        final int writeFrom = xDirection? rect.x : rect.y;  //first point of a line that needs to be written
        final int writeTo = xDirection ? rect.x+rect.width : rect.y+rect.height;
        final int readFrom = (writeFrom-radius < 0) ? 0 : writeFrom-radius;
        final int readTo = (writeTo+radius > length) ? length : writeTo+radius;

        int tmpMaxThreads = Math.min(((lineTo-lineFrom)*(writeTo-writeFrom))/100000+1, maxThreads); //avoid multithread overhead for small areas
        final int numThreads = Math.min(tmpMaxThreads, lineTo-lineFrom);
        final Thread[] lineThreads = new Thread[numThreads-1];
        for (int t=0; t < numThreads-1; t++) {
            final int ti = t+1;    //this thread starts at lineFrom+0, the other (new) threads do the rest
            final Thread thread = new Thread(
                new Runnable() {
                    final public void run() { try {
                        filterLines(pixels, type, sign, radius, lineFrom+ti, lineTo, /*lineStep=*/numThreads,
                                    length, readFrom, readTo, writeFrom, writeTo, pointInc, lineInc, /*isMainThread=*/false);
                    } catch(Exception ex) {IJ.handleException(ex);} }
                },
                "FastFilters-"+t);
            thread.setPriority( Thread.currentThread().getPriority() );
            lineThreads[t] = thread;
            thread.start();
        }
        filterLines(pixels, type, sign, radius, lineFrom+0, lineTo, /*lineStep=*/numThreads,
                    length, readFrom, readTo, writeFrom, writeTo, pointInc, lineInc, /*isMainThread=*/true);

        try {
            for ( final Thread thread : lineThreads )
                if ( thread != null ) thread.join();
        }
        catch ( InterruptedException e ) {
            for ( final Thread thread : lineThreads )
                thread.interrupt();
            try {
                for ( final Thread thread : lineThreads )
                    thread.join();
            }
            catch ( InterruptedException f ) {}
            Thread.currentThread().interrupt();     //set 'interrupted', which was cleared by handling InterruptedException
        }
    }
	
	private void filterLines(float[] pixels, int type, float sign, int radius, int startLine, int lineTo, int lineStep,
            int length, int readFrom, int readTo, int writeFrom, int writeTo, int pointInc, int lineInc, boolean isMainThread) {
        float[] cache = new float[length];    //input for filter, hopefully in CPU cache
        float[] vHi = (type == MEDIAN) ? new float[(2*x+1)*(2*y+1)] : null; //needed for median
        float[] vLo = (type == MEDIAN) ? new float[(2*x+1)*(2*y+1)] : null; //needed for median
        long lastTime = System.currentTimeMillis();
        for (int line=startLine; line<lineTo; line+=lineStep) {
            int pixel0 = line*lineInc + writeFrom*pointInc; //the first pixel to write in a line
            long time = System.currentTimeMillis();
            if (time - lastTime >110) {
                lastTime = time;
            }
            int p = line*lineInc + readFrom*pointInc;
            for (int i=readFrom; i<readTo; i++ ,p+=pointInc)
                cache[i] = pixels[p]*sign;
            switch (type) {
                case MEAN:
                    lineMean (radius, cache, pixels, writeFrom, writeTo, pixel0, pointInc);
                    break;
                case BORDER_LIMITED_MEAN:
                    lineBorderLimitedMean (radius, cache, pixels, writeFrom, writeTo, pixel0, pointInc);
                    break;
                case MEDIAN:
                    lineMedian (radius, cache, pixels, writeFrom, writeTo, pixel0, pointInc, vLo, vHi);
                    break;
                case MIN: case MAX:
                    lineMax(radius, sign, cache, pixels, writeFrom, writeTo, pixel0, pointInc);
            }
        }
    }
	
	private void lineMean (int radius, float[] cache, float[] pixels, int writeFrom, int writeTo, int pixel0, int pointInc) {
        double sum = 0;
        double factor = 1./(1 + 2*radius);
        int length = cache.length;
        float first = cache[0];
        float last = cache[length-1];
        int sumFrom = writeFrom-radius;
        int sumTo = writeFrom+radius;
        if (sumFrom < 0) {
            sum = -sumFrom*first;
            sumFrom = 0;
        }
        if (sumTo > length) {
            sum += (sumTo-length)*last;
            sumTo = length;
        }
        for (int i=sumFrom; i<sumTo; i++)
            sum += cache[i];
        for (int i = writeFrom, iMinus =i-radius, iPlus=i+radius, p=pixel0;
                i<writeTo; i++,iMinus++,iPlus++,p+=pointInc) {
            sum += (iPlus<length) ? cache[iPlus] : last;
            if (Double.isNaN(sum))
                sum = nanAwareSum (radius, cache, i, pixels, p); //writes pixel, returns NaN unless no NaNs
            else
                pixels[p] = (float)(sum*factor);
            sum -= (iMinus>=0) ? cache[iMinus] : first;
        }
    }

    private double nanAwareSum (int radius, float[] cache, int cachePos, float[] pixels, int p) {
        int n = 0;
        double sum = 0;
        for (int i = cachePos-radius; i<=cachePos+radius; i++) {
            float v = cache[i<0? 0 : (i>=cache.length ? cache.length-1 : i)];
            if (v==v) { //!isNaN(v)
                sum += v;
                n++;
            }
        }
        if (n > 0) pixels[p] = (float)(sum/n);
        return n==2*radius+1 ? sum : Double.NaN ;
    }

    private void lineBorderLimitedMean (int radius, float[] cache, float[] pixels, int writeFrom, int writeTo,
            int pixel0, int pointInc) {
        double sum = 0;
        int length = cache.length;
        int sumFrom = (writeFrom-radius>0) ? writeFrom-radius : 0;
        int sumTo = (writeFrom+radius<length) ? writeFrom+radius : length;
        int kSize = sumTo - sumFrom;
        for (int i=sumFrom; i<sumTo; i++)
            sum += cache[i];
        for (int i = writeFrom, iMinus =i-radius, iPlus=i+radius, p=pixel0;
                i<writeTo; i++,iMinus++,iPlus++,p+=pointInc) {
            if (iPlus<length) { sum += cache[iPlus]; kSize++; }
            if (Double.isNaN(sum))
                sum = nanAwareBLMean (radius, cache, i, kSize, pixels, p);
            else
                pixels[p] = ((float)sum)/kSize;
            if (iMinus>=0) { sum -= cache[iMinus]; kSize--; }
        }
    }

    private double nanAwareBLMean (int radius, float[] cache, int cachePos, int kSize, float[] pixels, int p) {
        int n = 0;
        double sum = 0;
        for (int i = Math.max(cachePos-radius,0); i<=Math.min(cachePos+radius,cache.length-1); i++) {
            float v = cache[i];
            if (v==v) { //!isNaN(v)
                sum += v;
                n++;
            }
        }
        if (n > 0) pixels[p] = (float)(sum/n);
        return n==kSize ? sum : Float.NaN;
    }

    private void lineMedian (int radius, float[] cache, float[] pixels, int writeFrom, int writeTo,
            int pixel0, int pointInc, float[] vHi, float[] vLo) {
        int length = cache.length;
        float median = Float.isNaN(cache[writeFrom]) ? 0 : cache[writeFrom]; //a first guess
        for (int i=writeFrom, iMinus=i-radius, iPlus=i+radius, p=pixel0;
                i<writeTo; i++, iMinus++,iPlus++,p+=pointInc) {
            int nHi = 0, nLo = 0;
            int iStart = (iMinus>=0) ? iMinus : 0;
            int iStop = (iPlus<length) ? iPlus : length-1;
            int nEqual = 0;
            for (int iRead=iStart; iRead <= iStop; iRead++) {
                float v = cache[iRead];
                if (v > median) vHi[nHi++] = v;
                else if (v < median) vLo[nLo++] = v;
                else if (v==v) nEqual++;    //if (!isNaN(v))
            }
            int nPoints = nHi + nLo + nEqual;
            if (nPoints == 0) {
                pixels[p] = Float.NaN;
            } else {
                if (nPoints%2 == 0) {  //avoid an even number of points: in case of doubt, leave it closer to original value
                    float v = cache[i];
                    if (v > median) vHi[nHi++] = v;
                    else if (v < median) vLo[nLo++] = v;
                }
                int half = nPoints/2;//>>1; //(nHi+nLo)/2, but faster
                if (nHi>half)
                    median = RankFilters.findNthLowestNumber(vHi, nHi, nHi-half-1);
                else if (nLo>half)
                    median = RankFilters.findNthLowestNumber(vLo, nLo, half);
                pixels[p] = median;
            }
        }
    }

    private void lineMax (int radius, float sign, float[] cache, float[] pixels, int writeFrom, int writeTo,
            int pixel0, int pointInc) {
        int length = cache.length;
        int pUp = pixel0;
        int pDn = pixel0 + (writeTo-writeFrom-1)*pointInc;
        float maxUp = -Float.MAX_VALUE;
        float maxDn = -Float.MAX_VALUE;
        int iInUp = writeFrom + radius;         //new in the range that we have to find the max of
        int iOutUp = writeFrom - radius - 1;    //not in the range any more
        int iInDn = writeTo - radius -1;
        int iOutDn = writeTo + radius;
        boolean firstUp = true;
        boolean firstDn = true;

        while (pUp<=pDn) {
            boolean switchDirection = false;
            for (; pUp<=pDn; pUp+=pointInc, iInUp++, iOutUp++) {
                float oldmax = maxUp;
                if (iInUp<length) {
                    if (firstUp || Float.isNaN(oldmax) || Float.isNaN(cache[iInUp])) {
                        maxUp = nanAwareMax (iOutUp+1, iInUp, cache, sign, pixels, pUp); //find max one-by-one with possible NaNs
                        firstUp = false;
                        continue;
                    }
                    if(maxUp<=cache[iInUp]) {
                        maxUp = cache[iInUp];
                        pixels[pUp] = maxUp*sign;
                        continue;
                    }
                }
                if (iOutUp>=0 && cache[iOutUp]==oldmax) {           //full one-by-one determination
                    if (switchDirection)
                        break;
                    switchDirection = true;
                    int maxFrom = (iOutUp >= -1) ? iOutUp+1 : 0;
                    int maxTo = (iInUp < length) ? iInUp : length-1;
                    maxUp = cache[maxFrom];
                    for (int i=maxFrom+1; i<=maxTo; i++)
                        if (maxUp < cache[i]) maxUp = cache[i];
                }
                pixels[pUp] = maxUp*sign;
            }
            for (; pUp<=pDn; pDn-=pointInc, iInDn--, iOutDn--) {
                switchDirection = false;
                float oldmax = maxDn;
                if (iInDn>=0) {
                    if (firstDn || Float.isNaN(oldmax) || Float.isNaN(cache[iInDn])) {
                        maxDn = nanAwareMax (iInDn, iOutDn-1, cache, sign, pixels, pDn);
                        firstDn = false;
                        continue;
                    }
                    if (maxDn<cache[iInDn]) {
                        maxDn = cache[iInDn];
                        pixels[pDn] = maxDn*sign;
                        continue;
                    }
                }
                if (iOutDn<length && cache[iOutDn]==oldmax) {   //full one-by-one determination
                    if (switchDirection)
                        break;
                    switchDirection = true;
                    int maxFrom = (iOutDn <=length) ? iOutDn-1 : length-1;
                    int maxTo = (iInDn > 0) ? iInDn : 0;
                    maxDn = cache[maxFrom];
                    for (int i=maxFrom-1; i>=maxTo; i--)
                        if (maxDn < cache[i]) maxDn = cache[i];
                }
                pixels[pDn] = maxDn*sign;
            }
        }
    }

    private float nanAwareMax (int from, int to, float[] cache, float sign, float[] pixels, int p) {
        float max = -Float.MAX_VALUE;
        boolean anyGood = false;
        boolean anyNaN = false;
        for (int i=Math.max(from,0); i<=Math.min(to,cache.length-1); i++) {
            float v = cache[i];
            if (Float.isNaN(v)) {
                anyNaN = true;
            } else {
                if (max < cache[i]) max = cache[i];
                anyGood = true;
            }
        }
        if (anyGood) pixels[p] = max*sign;
        return anyNaN ? Float.NaN : max;
    }
}
