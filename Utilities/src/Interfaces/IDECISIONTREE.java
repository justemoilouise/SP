package Interfaces;

import java.io.InputStream;
import java.util.ArrayList;

public interface IDECISIONTREE {

	public ArrayList<Object> readImageSet(InputStream stream);
	
	public double crossValidate();
}
