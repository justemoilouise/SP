package helpers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.channels.Channels;
import java.util.ArrayList;

import Data.ClassifierModel;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.tools.cloudstorage.GcsFileOptions;
import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsOutputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

public class ServerHelper {
	final static String gcsBucket = "radiss-training.appspot.com";
	final static String modelKeysFilename = "model-keys.dat";
	
	private static BlobstoreService blobstoreService;
	private static GcsService gcsService;
	
	public static void Init() {
		blobstoreService = BlobstoreServiceFactory.getBlobstoreService();
		gcsService = GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());
	}
	
	public static BlobKey writeFileToBlob(ClassifierModel model) {
		try {
			String fileName = "classifier-model-" + model.getVersion() + ".dat";
			GcsFilename gcsFilename = new GcsFilename(gcsBucket, fileName);
			GcsOutputChannel outputChannel = gcsService.createOrReplace(gcsFilename, GcsFileOptions.getDefaultInstance());
			ObjectOutputStream oStream = new ObjectOutputStream(Channels.newOutputStream(outputChannel));
			oStream.writeObject(model);
			oStream.close();
			
			return blobstoreService.createGsBlobKey(fileName);
		} catch(Exception ex) {}

		return null;
	}
	
	public static void saveModelKeyToGCS(BlobKey key) {
		ArrayList<BlobKey> modelKeys = readModelKeysFromGCS();
		modelKeys.add(key);

		try {
			GcsFilename gcsFilename = new GcsFilename(gcsBucket, modelKeysFilename);
			GcsOutputChannel outputChannel = gcsService.createOrReplace(gcsFilename, GcsFileOptions.getDefaultInstance());
			ObjectOutputStream oStream = new ObjectOutputStream(Channels.newOutputStream(outputChannel));
			oStream.writeObject(modelKeys);
			oStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static ArrayList<BlobKey> readModelKeysFromGCS() {
		ArrayList<BlobKey> modelKeys = new ArrayList<BlobKey>();

		try {
			GcsFilename gcsFilename = new GcsFilename(gcsBucket, modelKeysFilename);
			GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(gcsFilename, 0, 1024 * 1024);
			ObjectInputStream iStream = new ObjectInputStream(Channels.newInputStream(readChannel));
			modelKeys = (ArrayList<BlobKey>) iStream.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return modelKeys;
	}
}
