package antonafanasjew.cosmodog.rendering.renderer.portals;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.portals.Portal;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.AbstractRenderer;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import java.util.List;

public class PortalRenderer extends AbstractRenderer {

    public record PortalRendererParam(boolean bottomNotTop) {
        public static PortalRenderer.PortalRendererParam BOTTOM = new PortalRenderer.PortalRendererParam(true);
        public static PortalRenderer.PortalRendererParam TOP = new PortalRenderer.PortalRendererParam(false);
    }

    private static final Color PORTAL_BASE_COLOR_A = new Color(1, 0.5f, 0, 0.7f);
    private static final Color PORTAL_BASE_COLOR_B = new Color(1, 0.5f, 0, 0.5f);

    private static final Color PORTAL_ROOF_COLOR_A = new Color(0.7f, 0.3f, 0, 0.7f);
    private static final Color PORTAL_ROOF_COLOR_B = new Color(0.7f, 0.3f, 0, 0.5f);

    private static final int PORTAL_THICKNESS = 4;
    private static final int PORTAL_SOUTH_WALL_EDGE_OFFSET = 9;
    private static final int PORTAL_NORTH_WALL_EDGE_OFFSET = 7;

    @Override
    public void render(GameContainer gameContainer, Graphics graphics, Object renderingParameter) {

        long timestamp = System.currentTimeMillis();

        ApplicationContext applicationContext = ApplicationContext.instance();
        Cosmodog cosmodog = applicationContext.getCosmodog();
        CosmodogGame cosmodogGame = cosmodog.getCosmodogGame();
        CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
        Cam cam = cosmodogGame.getCam();


        List<Portal> portals = cosmodogGame.getPortals();

        if (portals.isEmpty()) {
            return;
        }

        //Start drawing at the beginning of the scene. Usually, it is the top left corner of the screen.
        DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
        graphics.translate(sceneDrawingContext.x(), sceneDrawingContext.y());

        //The original tile width and height. They are usually 16x16 pixels big.
        int tileLength = TileUtils.tileLengthSupplier.get();

        //The scaled tile width and height.
        //They are the original tile width and height multiplied by the zoom factor of the camera.
        //Example: An 16x16 tile with a zoom factor of 2 will be drawn as a 32x32 tile.
        int scaledTileLength = (int) (tileLength * cam.getZoomFactor());

        //The position of the camera on the map in pixels.
        //Example: Having the cam on the tile 5,5 and the tile width is 16 pixels, the cam position on the map in pixels is 80,80.
        //While the player is moving to tile 5,6, the cam will follow him and its position will gradually change from 80,80 to 80,96.
        int camPositionOnMapInPixelsX = (int) cam.viewCopy().x();
        int camPositionOnMapInPixelsY = (int) cam.viewCopy().y();

        //While the player is moving between tiles, the cam will not be exactly on a tile, but between two tiles.
        //These variables show the offset from the beginning of the tile during the movement.
        int camOffsetFromTileBeginX = (int) ((camPositionOnMapInPixelsX % scaledTileLength));
        int camOffsetFromTileBeginY = (int) ((camPositionOnMapInPixelsY % scaledTileLength));

        //The position of the camera on the map in tiles. It ignores offsets while transitioning the cam from tile to tile.
        int camPositionOnMapInTilesX = camPositionOnMapInPixelsX / scaledTileLength;
        int camPositionOnMapInTilesY = camPositionOnMapInPixelsY / scaledTileLength;

        //Defines how many (scaled) tiles fit in the camera's view. Adds two tiles to have some buffer.
        int camViewWidthInTiles = (int) (cam.viewCopy().width()) / scaledTileLength + 2;
        int camViewHeightInTiles = (int) (cam.viewCopy().height()) / scaledTileLength + 2;

        //When the cam sits between two tiles (for instance when the player moves or teleports)
        //the top row and/or the left column of tiles on the screen will be visible only partially.
        //The offset causes the drawing to start at a negative offset related to the top left corner of the screen.
        graphics.translate(-camOffsetFromTileBeginX, -camOffsetFromTileBeginY);
        graphics.scale(cam.getZoomFactor(), cam.getZoomFactor());

        for (int i = 0; i < portals.size(); i++) {
            Portal portal = portals.get(i);
            Position portalPosition = portal.position;

            if (!portalPosition.inMapBounds(map)) {
                continue;
            }

            boolean tooFarWest = portalPosition.getX() < camPositionOnMapInTilesX;
            boolean tooFarEast = portalPosition.getX() >= camPositionOnMapInTilesX + camViewWidthInTiles;
            boolean tooFarNorth = portalPosition.getY() < camPositionOnMapInTilesY;
            boolean tooFarSouth = portalPosition.getY() >= camPositionOnMapInTilesY + camViewHeightInTiles;

            if (!(tooFarEast || tooFarWest || tooFarNorth || tooFarSouth)) {



                float portalTilePositionRelatedToCamX = portalPosition.getX() - camPositionOnMapInTilesX;
                float portalTilePositionRelatedToCamY = portalPosition.getY() - camPositionOnMapInTilesY;

                float offsetX = (portalTilePositionRelatedToCamX) * tileLength;
                float offsetY = (portalTilePositionRelatedToCamY) * tileLength;

                if (i == 0) {
                    graphics.setColor(Color.blue);
                } else {
                    graphics.setColor(Color.orange);
                }

                DirectionType direction = portal.directionType;
                boolean bottomNotTop = ((PortalRendererParam)renderingParameter).bottomNotTop;

                if (bottomNotTop) {
                    boolean alternateColor = timestamp / 120 % 2 == 1;
                    Color baseColor = alternateColor ? PORTAL_BASE_COLOR_B : PORTAL_BASE_COLOR_A;
                    if (direction == DirectionType.DOWN) {
                        graphics.setColor(baseColor);
                        graphics.fillRect(offsetX, offsetY + PORTAL_SOUTH_WALL_EDGE_OFFSET, tileLength, tileLength - PORTAL_SOUTH_WALL_EDGE_OFFSET);
                    } else if (direction == DirectionType.UP) {
                    } else if (direction == DirectionType.LEFT) {
                        graphics.setColor(baseColor);
                        graphics.fillRect(offsetX, offsetY + PORTAL_SOUTH_WALL_EDGE_OFFSET, PORTAL_THICKNESS, tileLength - PORTAL_SOUTH_WALL_EDGE_OFFSET);
                    } else {
                        graphics.setColor(baseColor);
                        graphics.fillRect(offsetX + tileLength - PORTAL_THICKNESS, offsetY + PORTAL_SOUTH_WALL_EDGE_OFFSET, PORTAL_THICKNESS, tileLength - PORTAL_SOUTH_WALL_EDGE_OFFSET);
                    }
                } else {
                    boolean alternateColor = timestamp / 120 % 2 == 1;
                    Color roofColor = alternateColor ? PORTAL_ROOF_COLOR_B : PORTAL_ROOF_COLOR_A;
                    if (direction == DirectionType.DOWN) {
                        graphics.setColor(roofColor);
                        graphics.fillRect(offsetX, offsetY + PORTAL_SOUTH_WALL_EDGE_OFFSET - PORTAL_THICKNESS, tileLength, PORTAL_THICKNESS);
                    } else if (direction == DirectionType.UP) {
                        graphics.setColor(roofColor);
                        graphics.fillRect(offsetX, offsetY - PORTAL_NORTH_WALL_EDGE_OFFSET, tileLength, PORTAL_THICKNESS);
                    } else if (direction == DirectionType.LEFT) {
                        graphics.setColor(roofColor);
                        graphics.fillRect(offsetX, offsetY - PORTAL_NORTH_WALL_EDGE_OFFSET, PORTAL_THICKNESS, tileLength);
                    } else {
                        graphics.setColor(roofColor);
                        graphics.fillRect(offsetX + tileLength - PORTAL_THICKNESS, offsetY - PORTAL_NORTH_WALL_EDGE_OFFSET, PORTAL_THICKNESS, tileLength);
                    }
                }
            }
        }

        graphics.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
        graphics.translate(camOffsetFromTileBeginX, camOffsetFromTileBeginY);


        graphics.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());



    }


}
