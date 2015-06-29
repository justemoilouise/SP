package Interfaces;

import java.io.InputStream;
import java.util.ArrayList;

import Data.Species;

public interface IDECISIONTREE {

	public ArrayList<Species> readImageSet(InputStream stream);
	
	public double crossValidate();
}
