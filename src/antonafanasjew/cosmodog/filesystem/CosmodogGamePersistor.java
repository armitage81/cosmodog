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

import antonafanasjew.cosmodog.model.CosmodogGame;

public class CosmodogGamePersistor {

	private CosmodogGamePersistor() {

	}
	
	private static CosmodogGamePersistor instance = new CosmodogGamePersistor();

	public static CosmodogGamePersistor instance() {
		return instance;
	}

	public void saveCosmodogGame(CosmodogGame game, String filePath) throws CosmodogPersistenceException {
		try {
			File file = new File(filePath);
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			file.mkdirs();
			OutputStream fileStream = new FileOutputStream(file);
			OutputStream buffer = new BufferedOutputStream(fileStream);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject(game);
			output.close();
		} catch (IOException ex) {
			throw new CosmodogPersistenceException(ex.getMessage(), ex);
		}
	}

	public CosmodogGame restoreCosmodogGame(String filePath) throws CosmodogPersistenceException {
		try {
			File file = new File(filePath);
			file.mkdirs();
			InputStream fileStream = new FileInputStream(file);
			InputStream buffer = new BufferedInputStream(fileStream);
			ObjectInput input = new ObjectInputStream(buffer);
			CosmodogGame game = (CosmodogGame) input.readObject();
			input.close();
			return game;
		} catch (ClassNotFoundException ex) {
			throw new CosmodogPersistenceException(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new CosmodogPersistenceException(ex.getMessage(), ex);
		}
	}

}
