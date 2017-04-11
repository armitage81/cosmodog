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
import java.util.Date;

import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogGameHeader;

/**
 * Used for serializing and deserializing the cosmodog game state.
 * Singleton.
 */
public class CosmodogGamePersistor {

	private CosmodogGamePersistor() {

	}
	
	private static CosmodogGamePersistor instance = new CosmodogGamePersistor();

	/**
	 * Returns the singleton instance of the game persistor.
	 */
	public static CosmodogGamePersistor instance() {
		return instance;
	}

	/**
	 * Saves the game on disk.
	 * @param game Cosmodog game state.
	 * @param filePath Path of the save file on disk.
	 * @throws CosmodogPersistenceException Thrown if I/O errors occur.
	 */
	public void saveCosmodogGame(CosmodogGame game, String filePath) throws CosmodogPersistenceException {
		try {
			File file = new File(filePath);
			file.mkdirs();
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
			file.mkdirs();
			OutputStream fileStream = new FileOutputStream(file);
			OutputStream buffer = new BufferedOutputStream(fileStream);
			ObjectOutput output = new ObjectOutputStream(buffer);
			
			CosmodogGameHeader cosmodogGameHeader = new CosmodogGameHeader(game, new Date());
			
			output.writeObject(cosmodogGameHeader);
			output.writeObject(game);
			output.close();
		} catch (IOException ex) {
			throw new CosmodogPersistenceException(ex.getMessage(), ex);
		}
	}

	/**
	 * Restores a cosmodog game from disk.
	 * @param filePath Save file on disk.
	 * @return Cosmodog game as deserialized from a save file.
	 * @throws CosmodogPersistenceException Thrown if I/O errors occur.
	 */
	public CosmodogGame restoreCosmodogGame(String filePath) throws CosmodogPersistenceException {
		try {
			File file = new File(filePath);
			file.mkdirs();
			InputStream fileStream = new FileInputStream(file);
			InputStream buffer = new BufferedInputStream(fileStream);
			ObjectInput input = new ObjectInputStream(buffer);
			@SuppressWarnings("unused")
			CosmodogGameHeader header = (CosmodogGameHeader)input.readObject();
			CosmodogGame game = (CosmodogGame) input.readObject();
			input.close();
			game.initTransientFields();
			return game;
		} catch (ClassNotFoundException ex) {
			throw new CosmodogPersistenceException(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new CosmodogPersistenceException(ex.getMessage(), ex);
		}
	}

	public CosmodogGameHeader loadCosmodogGameHeader(String filePath) throws CosmodogPersistenceException {
		try {
			File file = new File(filePath);
			file.mkdirs();
			InputStream fileStream = new FileInputStream(file);
			InputStream buffer = new BufferedInputStream(fileStream);
			ObjectInput input = new ObjectInputStream(buffer);
			CosmodogGameHeader header = (CosmodogGameHeader)input.readObject();
			input.close();
			return header;
		} catch (ClassNotFoundException ex) {
			throw new CosmodogPersistenceException(ex.getMessage(), ex);
		} catch (IOException ex) {
			throw new CosmodogPersistenceException(ex.getMessage(), ex);
		}
	}
	
}
