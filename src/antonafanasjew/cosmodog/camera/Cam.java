package antonafanasjew.cosmodog.camera;

import java.io.Serial;
import java.io.Serializable;

import org.newdawn.slick.util.Log;

import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.Piece;
import antonafanasjew.cosmodog.topology.PlacedRectangle;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.topology.Rectangle;

/**
 * Represents the information about the part of the scene that is visible on the screen.
 * (The scene is the complete map. It can be bigger or smaller depending on zoom factor.
 * The camera is the part of the map that is visible on the screen).
 * <p>
 * Take note: The camera is equivalent to the screen size (or its part in case the map view is not drawn on the whole screen).
 */
public class Cam implements Serializable {

	@Serial
	private static final long serialVersionUID = -8932980175152168004L;

	public static final int ZOOM_FACTOR_FAR = 3;
	public static final int ZOOM_FACTOR_CLOSE = 5;

	/**
	 * In-scene camera mode. The view must never leave the border of the scene.
	 */
	public static final int CAM_MODE_COMPLETELY_IN_SCENE = 0;
	
	/**
	 * Center-In-Scene camera mode. The center of the view must never leave the border of the scene.
	 */
	public static final int CAM_MODE_CENTER_IN_SCENE = 1;
	
	/**
	 * Scene intersection camera mode. The view must always show at least little piece of the scene
	 */
	public static final int CAM_MODE_INTERSECTS_SCENE = 2;
	
	/**
	 * Free camera mode. No restrictions on camera movement.
	 */
	public static final int CAM_MODE_FREE = 3;

	/**
	 * Camera mode that is used in the game. Defines how far the view can be placed from the scene.
	 */
	private final int mode;


	private float zoomFactor = 1.0f;

	/**
	 * Information about the scene's width and height. The scene can be zoomed in and out, hence changing its size.
	 */
	private final Rectangle scene;
	
	/**
	 * The visible camera view. It is placed related to scene.
	 * The size of the view will never change (it correlates to the screen size). Zooming is done by scaling the scene.
	 * The position of the view, though, will change when scrolling through the scene.
	 */
	private PlacedRectangle view;

	/*
	 * Throws the camera positioning exception in case the view position is out of bounds of the scene
	 * considering the camera mode.
	 */
	private void assertPositioning() throws CamPositioningException {
		PlacedRectangle placedScene = PlacedRectangle.fromAnchorAndSize(0, 0, scene.getWidth(), scene.getHeight());
		PlacedRectangle sceneViewIntersection = placedScene.intersection(view);
		if (mode == CAM_MODE_COMPLETELY_IN_SCENE) {
			if (sceneViewIntersection == null || !sceneViewIntersection.equals(view)) {
				throw new CamPositioningException();
			}
		}
		
		if (mode == CAM_MODE_CENTER_IN_SCENE) {
			if (sceneViewIntersection == null || sceneViewIntersection.width() < view.width() / 2 || sceneViewIntersection.height() < view.height() / 2) {
				throw new CamPositioningException();
			}
		}
		
		if (mode == CAM_MODE_INTERSECTS_SCENE) {
			if (sceneViewIntersection == null) {
				throw new CamPositioningException();
			}
		}
	}
	
	/**
	 * Constructor. Initializes the view and the scene. Can throw an exception in case the initial view positioning is out of bounds of the scene.
	 * 
	 * @param mode Camera mode. Defines the bounds of the view.
	 * @param scene A non placed rectangle that will be used as a field for the view movement. It can change size later (zooming).
	 * @param x The X coordinate of the view as related to the scene.
	 * @param y The Y coordinate of the view as related to the scene.
	 * @param width The view width. (It will not change while zooming. Instead, the scene will be doubled).
	 * @param height The view height. (It will not change while zooming. Instead, the scene will be doubled).
	 * @throws CamPositioningException Indicates the wrong positioning of the view on the scene.
	 */
	public Cam(int mode, Rectangle scene, float x, float y, float width, float height) throws CamPositioningException {
		this.mode = mode;
		this.scene = scene;
		this.view = PlacedRectangle.fromAnchorAndSize(x, y, width, height);
		assertPositioning();
	}

	/**
	 * Convenience method for shifting the view position as defined in {@link Position}.
	 * @param offsetX Positive or negative difference on the view position (horizontal).
	 * @param offsetY Positive or negative difference on the view position (vertical).
	 * @throws CamPositioningException Thrown if the shift causes the view to become out of scene bounds.
	 */
	public void shift(float offsetX, float offsetY) throws CamPositioningException {
		this.view.shift(offsetX, offsetY);
		assertPositioning();
	}
	
	/**
	 * As compared to {@link #shift(float, float)}, this method will shift the view only as far as the camera mode allows.
	 * This way, positioning exceptions will be prevented.
	 * 
	 * @param offsetX Positive or negative difference on the view position (horizontal).
	 * @param offsetY Positive or negative difference on the view position (vertical).
	 */
	public void gentleShift(float offsetX, float offsetY) {
		
		float correctedOffsetX = offsetX;
		float correctedOffsetY = offsetY;
		
		float minX = 0;
		float maxX = scene.getWidth();
		
		float minY = 0;
		float maxY = scene.getHeight();
		
		if (mode == CAM_MODE_COMPLETELY_IN_SCENE) {
			maxX -= view.width();
			maxY -= view.height();
		}
		
		if (mode == CAM_MODE_CENTER_IN_SCENE) {
			
			//Both are negated, that's not an error
			minX -= view.width() / 2;
			minY -= view.height() / 2;
			
			maxX -= view.width() / 2;
			maxY -= view.height() / 2;
		}
		
		if (mode == CAM_MODE_INTERSECTS_SCENE) {
			
			minX -= view.width();
			minY -= view.height();
			
		}
		
		if (mode == CAM_MODE_FREE) {
			minX -= 1000000000;
			minY -= 1000000000;
			maxX = 1000000000;
			maxY = 1000000000;
		}
		
		if (view.x() + offsetX < minX) { //OffsetX must be negative here
			correctedOffsetX = minX - view.x();
		}
		
		if (view.x() + offsetX > maxX) { //OffsetX must be positive here
			correctedOffsetX = maxX - view.x();
		}
		
		if (view.y() + offsetY < minY) {
			correctedOffsetY = minY - view.y();
		}
		
		if (view.y() + offsetY > maxY) {
			correctedOffsetY = maxY - view.y();
		}
		
		this.view.shift(correctedOffsetX, correctedOffsetY);
		
	}
	
	/**
	 * Places the camera view on the scene (Direct view positioning related to the scene). 
	 * @param x Absolute x coordinate related to the scene.
	 * @param y Absolute y coordinate related to the scene.
	 * 
	 * @throws CamPositioningException Thrown if the positioning locates the view out of scene bounds.
	 */
	public void move(float x, float y) throws CamPositioningException {
		this.view.move(x, y);
		assertPositioning();
	}
	
	/**
	 * Returns the view copy. This way, the view cannot be modified outside of the camera class.
	 * @return View copy.
	 */
	public PlacedRectangle viewCopy() {
		return PlacedRectangle.fromAnchorAndSize(view.x(), view.y(), view.width(), view.height());
	}
	
	/**
	 * Zooms in by multiplying the scene size by a default factor.
	 */
	public void zoomIn() {
		zoom(ZOOM_FACTOR_CLOSE);
	}
	
	/**
	 * Zooms out by dividing the scene size by a default coefficient.
	 */
	public void zoomOut() {
		zoom(ZOOM_FACTOR_FAR);
	}
	
	/**
	 * Zooms by scaling the scene by the given factor as related to the original scene size. Repeated calls of this method with
	 * the same zoom factor will not change the scene size.
	 * 
	 * Take care: This method does not throw a positioning exception while zooming out. The caller should make sure, that the
	 * scaling of the scene does not cause the view to be out of scene bounds.
	 * 
	 * @param newZoomFactor New zoom factor. It won't be applied on the current zoom factor but on initial zoom factor.
	 */
	public void zoom(float newZoomFactor) {
		
		if (newZoomFactor == zoomFactor) {
			return;
		}
		
		float scaleFactor = newZoomFactor / this.zoomFactor;

		this.zoomFactor = newZoomFactor;
		
		float xCenterOld = view.centerX();
		float yCenterOld = view.centerY();
		float sceneWidthOld = this.scene.getWidth();
		float sceneHeightOld = this.scene.getHeight();
		
		this.scene.scale(scaleFactor);
		
		float xCenterNew = xCenterOld / sceneWidthOld * scene.getWidth();
		float yCenterNew = yCenterOld / sceneHeightOld * scene.getHeight();
		
		float newViewMinX = xCenterNew - view.width() / 2;
		float newViewMinY = yCenterNew - view.height() / 2;
		
		this.view = PlacedRectangle.fromAnchorAndSize(newViewMinX, newViewMinY, this.view.width(), this.view.height());
		
	}
	
	/**
	 * Returns the current zoom factor. This factor defines the scale factor of the scene rectangle
	 * as compared to its original size. 
	 * @return Current zoom factor.
	 */
	public float getZoomFactor() {
		return zoomFactor;
	}
	
	/**
	 * Returns the copy of the scene rectangle. This way, the scene is read only. 
	 * @return Copy of the scene rectangle.
	 */
	public Rectangle sceneCopy() {
		return Rectangle.fromSize(scene.getWidth(), scene.getHeight());
	}
	
	/**
	 * Focuses the camera on the given piece based on its tile coordinates.
	 * The map parameter is needed as the piece does not contain any real coordinates, so the tile width and height
	 * need to be considered.
	 * 
	 * @param map The tiled map object to identify the width and the height of a tile.
	 * @param offsetX Horizontal offset that should be applied. (Normally used to focus on a moving piece, like the walking player)
	 * @param offsetY Vertical offset that should be applied. (Normally used to focus on a moving piece, like the walking player)
	 * @param piece The piece which coordinates the camera will focus on.
	 */
	public void focusOnPiece(CosmodogMap map, float offsetX, float offsetY, Piece piece) {
		
		int zoomedTileWidth = (int)(map.getTileWidth() * zoomFactor);
		int zoomedTileHeight = (int)(map.getTileHeight() * zoomFactor);
		
		float pieceX = piece.getPositionX() * zoomedTileWidth + offsetX * zoomFactor;
		float pieceY = piece.getPositionY() * zoomedTileHeight + offsetY * zoomFactor;
		
		int pieceWidth = zoomedTileWidth;
		int pieceHeight = zoomedTileHeight;
		
		int camWidth = (int)this.viewCopy().width();
		int camHeight = (int)this.viewCopy().height();
		
		float newCamX = pieceX + pieceWidth / 2.0f - camWidth / 2.0f;
		float newCamY = pieceY + pieceHeight / 2.0f - camHeight / 2.0f;
		
		try {
			this.move(newCamX, newCamY);
		} catch (CamPositioningException e) {
			//This will never happen as the pieces are always parts of the map
			//and the cam focuses on them.
			Log.error("This is a programming error. Coordinates: " + newCamX + "/" + newCamY + " are not supposed to be out of the scene in no case.", e);
			Log.error(pieceX + "/" + pieceY + "/" + pieceWidth + "/" + pieceHeight + "/" + camWidth + "/" + camHeight + "/" + newCamX + "/" + newCamY);
			System.exit(1);
		}
		
	}
}
