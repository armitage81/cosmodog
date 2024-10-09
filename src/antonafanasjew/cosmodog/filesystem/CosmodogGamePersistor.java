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

import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.camera.CamPositioningException;
import antonafanasjew.cosmodog.globals.Constants;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogGameHeader;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.topology.Rectangle;

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
	public void saveCosmodogGame(CosmodogGame game, String filePath){
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
			Log.error("Could not save game", ex);
			System.exit(1);
		}
	}

	/**
	 * Restores a cosmodog game from disk.
	 * @param filePath Save file on disk.
	 * @return Cosmodog game as deserialized from a save file.
	 * @throws CosmodogPersistenceException Thrown if I/O errors occur.
	 */
	public CosmodogGame restoreCosmodogGame(String filePath) {
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
			
			/*
			 * When storing a game, the camera is stored, too.
			 * Part of the camera persisted state is its size and location.
			 * If saving a game in one resolution and then loading it in another resolution, the restored camera will be wrong.
			 * That's why we fix it here after the restoration process.
			 */
			try {
				CosmodogMap map = game.getMap();
				float oldZoomFactor = game.getCam().getZoomFactor();
				Rectangle scene = Rectangle.fromSize((float) (map.getWidth() * map.getTileWidth()), (float) (map.getHeight() * map.getTileHeight()));
				DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
				Cam cam = new Cam(Cam.CAM_MODE_CENTER_IN_SCENE, scene, sceneDrawingContext.x(), sceneDrawingContext.y(), sceneDrawingContext.w(), sceneDrawingContext.h());
				cam.focusOnPiece(map, 0, 0, game.getPlayer());
				cam.zoom(oldZoomFactor);
				game.setCam(cam);
			} catch (CamPositioningException e) {
				Log.error("Camera positioning could not be established", e);
			}
			return game;
		} catch (ClassNotFoundException | IOException ex) {
			Log.error("Could not restore game", ex);
			System.exit(1);
			return null;
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
