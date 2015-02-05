package preprocess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import CoreHandler.MathFunctions;
import CoreHandler.Prompt;
import Data.Species;
import Interfaces.ISCALE;
import core.Client;

public class Preprocess_Scale extends Preprocess implements ISCALE {
	private ArrayList<Species> dataset_raw;
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
		this.dataset_raw = dataset_raw;
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
		this.dataset_raw = dataset_raw;
		this.keys = dataset_raw.get(0).getFeatureLabels();
		this.isIJ = isIJ;
		
		int length = keys.length;

		this.min = new double[length];
		this.max = new double[length];

		Arrays.fill(min, 0);
		Arrays.fill(max, 0);
		
		readFromFile();
	}
	
	public ArrayList<Species> getPreprocessedDataset() {
		return super.getPreprocessedDataset();
	}

	public double[] scale(double[] features) {
		double[] scaled = new double[features.length];
		
		for(int i=0; i<features.length; i++) {
			scaled[i] = (features[i]-min[i])/(max[i]-min[i]);
		}
		
		return scaled;
	}
		
	@Override
	public void scale() {
		// TODO Auto-generated method stub
		double[][] data_raw = MathFunctions.Transpose(extractFeatures());

		if(min[0]==0 && max[0]==0)
			getMinAndMax(data_raw);

		for(int i=0; i<data_raw.length; i++) {			
			for(int j=0; j<dataset_raw.size(); j++) {
				data_raw[i][j] = (data_raw[i][j]-min[i])/(max[i]-min[i]);
			}
		}

		double[][] data_preprocessed = MathFunctions.Transpose(data_raw);
		updateDataset(data_preprocessed);
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
	
	@Override
	public void saveToFile(double[] arr) {
		StringBuilder sb = new StringBuilder();

		for(int i=0; i<arr.length; i++) {
			sb.append(arr[i]+"|");
		}
		sb.setLength(sb.length()-1);
		
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

		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(f, true));
			pw.print(sb.toString());
			pw.println();
			pw.flush();
			pw.close();

			Files.setAttribute(f.toPath(), "dos:hidden", true);

		} catch (Exception e) {
			Prompt.PromptError("ERROR_SAVE_SCALE");
			Client.printStackTrace(e);
		}
	}

	@Override
	public void setMaximum(double[] max) {
		// TODO Auto-generated method stub
		this.max = max;
	}

	@Override
	public void setMinimum(double[] min) {
		// TODO Auto-generated method stub
		this.min = min;
	}
	
	@Override
	public void getMinAndMax(double[][] data) {
		// TODO Auto-generated method stub
		for(int i=0; i<data.length; i++) {
			double[] temp = data[i].clone();
			Arrays.sort(temp);
			min[i] = temp[0];
			max[i] = temp[temp.length-1];
		}

		saveToFile(min);
		saveToFile(max);
	}
}
