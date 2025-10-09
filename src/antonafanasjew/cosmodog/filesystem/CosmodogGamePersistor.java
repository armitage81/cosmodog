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

import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.popup.PopUpNotificationAction;
import antonafanasjew.cosmodog.domains.MapType;
import antonafanasjew.cosmodog.rules.Rule;
import antonafanasjew.cosmodog.rules.RuleAction;
import antonafanasjew.cosmodog.rules.RuleTrigger;
import antonafanasjew.cosmodog.rules.actions.AsyncActionRegistrationRuleAction;
import antonafanasjew.cosmodog.rules.events.GameEventPieceInteraction;
import antonafanasjew.cosmodog.rules.triggers.GameProgressPropertyTrigger;
import antonafanasjew.cosmodog.rules.triggers.logical.AndTrigger;
import antonafanasjew.cosmodog.util.TileUtils;
import com.google.common.collect.Lists;
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

	private final PathProvider pathProvider;

	public CosmodogGamePersistor(PathProvider pathProvider) {
		this.pathProvider = pathProvider;
	}

	public void saveCosmodogGame(CosmodogGame game){
		try {
			File file = new File(pathProvider.gameSaveDir() + "/" + game.getGameName() + ".sav");
			file.mkdirs();
			if (file.exists()) {
				if (!file.delete()) {
					throw new IOException("Could not delete existing save game file: " + file.getAbsolutePath());
				}
			}
			if (!file.createNewFile()) {
				throw new IOException("Could not create save game file: " + file.getAbsolutePath());
			}

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

	public CosmodogGame restoreCosmodogGame(int saveSlotNumber) {
		try {
			String filePath = pathProvider.gameSaveDir() + "/" + saveSlotNumber + ".sav";
			File file = new File(filePath);
			if (file.exists()) {
				InputStream fileStream = new FileInputStream(file);
				InputStream buffer = new BufferedInputStream(fileStream);
				ObjectInput input = new ObjectInputStream(buffer);
				@SuppressWarnings("unused")
				CosmodogGameHeader header = (CosmodogGameHeader) input.readObject();
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
					int tileLength = TileUtils.tileLengthSupplier.get();
					CosmodogMap map = game.mapOfPlayerLocation();
					float oldZoomFactor = game.getCam().getZoomFactor();
					Rectangle scene = Rectangle.fromSize((float) (map.getMapType().getWidth() * tileLength), (float) (map.getMapType().getHeight() * tileLength));
					DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
					Cam cam = new Cam(Cam.CAM_MODE_CENTER_IN_SCENE, scene, sceneDrawingContext.x(), sceneDrawingContext.y(), sceneDrawingContext.w(), sceneDrawingContext.h(), map.getMapType());
					cam.focusOnPiece(game, 0, 0, game.getPlayer());
					cam.zoom(oldZoomFactor);
					game.setCam(cam);

				} catch (CamPositioningException e) {
					Log.error("Camera positioning could not be established", e);
				}
				return game;
			} else {
				throw new IOException("Save game file does not exist: " + filePath);
			}
		} catch (ClassNotFoundException | IOException ex) {
			Log.error("Could not restore game", ex);
			System.exit(1);
			return null;
		}
	}

	public boolean cosmodogGameHeaderExists(int saveSlotNumber) {
		String filePath = pathProvider.gameSaveDir() + "/" + saveSlotNumber + ".sav";
		File file = new File(filePath);
		return file.exists();
	}

	public CosmodogGameHeader loadCosmodogGameHeader(int saveSlotNumber) throws CosmodogPersistenceException {
		try {

			String filePath = pathProvider.gameSaveDir() + "/" + saveSlotNumber + ".sav";

			File file = new File(filePath);
			if (file.exists()) {
				InputStream fileStream = new FileInputStream(file);
				InputStream buffer = new BufferedInputStream(fileStream);
				ObjectInput input = new ObjectInputStream(buffer);
				CosmodogGameHeader header = (CosmodogGameHeader) input.readObject();
				input.close();
				return header;
			} else {
				throw new IOException("Save game file does not exist: " + filePath);
			}
		} catch (ClassNotFoundException | IOException ex) {
			throw new CosmodogPersistenceException(ex.getMessage(), ex);
		}
    }
	
}
