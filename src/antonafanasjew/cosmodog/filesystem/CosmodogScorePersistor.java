package antonafanasjew.cosmodog.filesystem;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import antonafanasjew.cosmodog.model.ScoreList;

public class CosmodogScorePersistor {

	private CosmodogScorePersistor() {
	}
	
	private static CosmodogScorePersistor instance = new CosmodogScorePersistor();

	public static CosmodogScorePersistor instance() {
		return instance;
	}
	
	public void saveScoreList(ScoreList scoreList, String filePath) throws CosmodogPersistenceException {
		try {
			File file = new File(filePath);
			file.getParentFile().mkdirs();
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			OutputStream fileStream = new FileOutputStream(file);
			OutputStream buffer = new BufferedOutputStream(fileStream);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject(scoreList);
			output.close();
		} catch (IOException ex) {
			throw new CosmodogPersistenceException(ex.getMessage(), ex);
		}
	}

	public ScoreList restoreScoreList(String filePath) throws CosmodogPersistenceException {
		try {
			File file = new File(filePath);
			file.getParentFile().mkdirs();
			InputStream fileStream = new FileInputStream(file);
			InputStream buffer = new BufferedInputStream(fileStream);
			ObjectInput input = new ObjectInputStream(buffer);
			ScoreList scoreList = (ScoreList) input.readObject();
			input.close();
			return scoreList;
		} catch (ClassNotFoundException ex) {
			throw new CosmodogPersistenceException(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new CosmodogPersistenceException(ex.getMessage(), ex);
		}
	}
	
}
