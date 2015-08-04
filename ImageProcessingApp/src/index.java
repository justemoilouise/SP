import ij.ImagePlus;


public class index {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String img = "img/Eptingium manfredi Dumitrica - 1.png";
		ImagePlus imp = new ImagePlus(img);
		imp.show();
		ImagePlus output = ProcessImage.topHatTransform(imp);
		output.show("OUTPUT");
	}

}
