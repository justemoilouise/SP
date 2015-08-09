import Helpers.ProcessImage;
import ImageProcessing.BaseShape;
import ImageProcessing.Protrusions;
import ij.ImagePlus;

public class index {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String img = "img/Eptingium manfredi Dumitrica - 1.png";
		ImagePlus imp = new ImagePlus(img);
		imp.show();
		
		ImagePlus th = ProcessImage.topHatTransform(imp.duplicate());
		th.show();
		Protrusions p = new Protrusions();
		p.identifyProtrusions(imp, th);
//		BaseShape bs = new BaseShape();
//		bs.identifyBaseShape(imp.duplicate(), th);
		
		ImagePlus output = p.getImage();
		output.show("OUTPUT");
	}
}
