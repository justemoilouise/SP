package preprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import Data.Species;
import FileHandlers.FileOutput;

public class Preprocess_Scale extends Preprocess {
	private String[] keys;
	private double[] min, max;
	private String key;
	private boolean isIJ;

	public Preprocess_Scale() {
		super();
	}

	public Preprocess_Scale(String key, ArrayList<Species> dataset_raw) {
		super(dataset_raw);
		this.key = key;
		this.keys = dataset_raw.get(0).getFeatureLabels();
		
		int length = keys.length;

		this.min = new double[length];
		this.max = new double[length];

		Arrays.fill(min, 0);
		Arrays.fill(max, 0);
		
		readFromFile();
	}
	
	public Preprocess_Scale(String key, ArrayList<Species> dataset_raw, boolean isIJ) {
		super(dataset_raw);
		this.key = key;
		this.keys = dataset_raw.get(0).getFeatureLabels();
		this.isIJ = isIJ;
		
		int length = keys.length;

		this.min = new double[length];
		this.max = new double[length];

		Arrays.fill(min, 0);
		Arrays.fill(max, 0);
		
		readFromFile();
	}

	public double[] scale(double[] features) {
		double[] scaled = new double[features.length];
		
		for(int i=0; i<features.length; i++) {
			scaled[i] = (features[i]-min[i])/(max[i]-min[i]);
		}
		
		return scaled;
	}
	
	@SuppressWarnings("resource")
	private void readFromFile() {
		StringBuilder fileName = new StringBuilder();
		fileName.append("settings/"+key);
		
		if(isIJ) {
			fileName.append("_IJ");
		}
		else {
			fileName.append("_JF");
		}
		
		fileName.append("_preprocess.txt");
		
		File f = new File(fileName.toString());

		if(f.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(f));
				int rowCount = 0;
				double[] arr;
				
				while(reader.ready()) {
					if(rowCount == 0)
						arr = this.min;
					else
						arr = this.max;
					
					StringTokenizer tokenizer = new StringTokenizer(reader.readLine(), "|");
					int tokCount = 0;
					while(tokenizer.hasMoreTokens()) {
						arr[tokCount] = Double.parseDouble(tokenizer.nextToken());
						tokCount++;
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void saveToFile(double[] arr) {
		FileOutput output = new FileOutput();
		output.saveToFile(arr, isIJ);
	}

	public void setMaximum(double[] max) {
		// TODO Auto-generated method stub
		this.max = max;
	}

	public void setMinimum(double[] min) {
		// TODO Auto-generated method stub
		this.min = min;
	}
}
