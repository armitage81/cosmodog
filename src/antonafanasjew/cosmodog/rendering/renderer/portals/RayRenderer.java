package antonafanasjew.cosmodog.rendering.renderer.portals;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.globals.DrawingContextProviderHolder;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.Cosmodog;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.dynamicpieces.portals.Emp;
import antonafanasjew.cosmodog.model.portals.Ray;
import antonafanasjew.cosmodog.rendering.context.DrawingContext;
import antonafanasjew.cosmodog.rendering.renderer.AbstractRenderer;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Animation;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import javax.sound.sampled.Port;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class RayRenderer extends AbstractRenderer {

    public record RayRendererParam(boolean bottomNotTop) {
        public static RayRenderer.RayRendererParam BOTTOM = new RayRenderer.RayRendererParam(true);
        public static RayRenderer.RayRendererParam TOP = new RayRenderer.RayRendererParam(false);
    }

    @Override
    public void render(GameContainer gameContainer, Graphics g, Object renderingParameter) {

        boolean bottomNotTop = ((RayRenderer.RayRendererParam)renderingParameter).bottomNotTop;

        CosmodogMap map = ApplicationContextUtils.mapOfPlayerLocation();
        Player player = ApplicationContextUtils.getPlayer();
        Cam cam = ApplicationContextUtils.getCosmodogGame().getCam();

        if (player.getPortalRay() != null) {

            //Start drawing at the beginning of the scene. Usually, it is the top left corner of the screen.
            DrawingContext sceneDrawingContext = DrawingContextProviderHolder.get().getDrawingContextProvider().sceneDrawingContext();
            g.translate(sceneDrawingContext.x(), sceneDrawingContext.y());

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
            g.translate(-camOffsetFromTileBeginX, -camOffsetFromTileBeginY);
            g.scale(cam.getZoomFactor(), cam.getZoomFactor());

            Ray ray = player.getPortalRay();
            long timestamp = System.currentTimeMillis() / 250;
            Color color = timestamp % 2 == 0 ? Color.red : Color.blue;
            g.setColor(color);

            /*
            When drawing the ray, following must be considered.
            - If the player stands on a field with an EMP piece, there is no ray.
            - The ray always starts at the position adjacent to the player in the facing direction, with one exception:
            - The exception being when the player is facing an adjacent non-transparent tile, in which case the ray covers no positions.
            - In the simplest case, the ray is a straight line covering one or multiple positions which ends at an non-transparent tile.
            - The ray can be bent though. When the ray hits a reflector, the ray is bent in the direction of the reflector.
            - A bent ray can hit itself (like in the Snake game), in which case the ray ends.
            - A bent ray can also hit the protagonist, in which case the ray also ends.
            - A ray that hits either itself or protagonist does not have a target position.
            - A ray that ends before a non-transparent tile has a target position, which is this tile's position.
            - When drawing a ray, we look at all it's positions and draw the ray's segment for each of those positions.
            - Each position refers to the previous position and to the next position.
            - For the very first position, the previous position is the protagonist's position.
            - For the very last position, the next position is the target position (if it exists)
            - or the position adjacent to the last position in the direction of the last direction.
            - Each segment of the ray is drawn in two steps.
            - The first half is drawn depending on the direction of the previous position.
            - The second half is drawn depending on the direction of the next position.
            - Each half of the segment can be a straight line or an L-shape.
            - If the target position does exist, it is drawn at the end.

             */

            CosmodogGame game = ApplicationContextUtils.getCosmodogGame();

            Player protagonist = game.getPlayer();

            if (!bottomNotTop) {
                List<Position> positions = ray.getRayPositions();
                for (int i = 0; i < positions.size(); i++) {

                    Position lookBehindPosition;
                    if (i == 0) {
                        lookBehindPosition = Position.fromCoordinatesOnPlayerLocationMap((int) protagonist.getPosition().getX(), (int) protagonist.getPosition().getY());
                    } else {
                        lookBehindPosition = positions.get(i - 1);
                    }

                    Position position = positions.get(i);
                    DirectionType startDirection = DirectionType.direction(lookBehindPosition, position);

                    Position lookAheadPosition = null;

                    if (i < positions.size() - 1) {
                        lookAheadPosition = positions.get(i + 1);
                    } else {
                        if (ray.getTargetPosition() != null) {
                            lookAheadPosition = ray.getTargetPosition();
                        } else {
                            lookAheadPosition = DirectionType.facedAdjacentPosition(position, startDirection);
                        }
                    }

                    DirectionType endDirection = lookAheadPosition != null ? DirectionType.direction(position, lookAheadPosition) : startDirection;

                    if (!position.inMapBounds(map)) {
                        continue;
                    }

                    boolean tooFarWest = position.getX() < camPositionOnMapInTilesX;
                    boolean tooFarEast = position.getX() >= camPositionOnMapInTilesX + camViewWidthInTiles;
                    boolean tooFarNorth = position.getY() < camPositionOnMapInTilesY;
                    boolean tooFarSouth = position.getY() >= camPositionOnMapInTilesY + camViewHeightInTiles;

                    if (!(tooFarEast || tooFarWest || tooFarNorth || tooFarSouth)) {

                        float x = (position.getX() - camPositionOnMapInTilesX) * tileLength;
                        float y = (position.getY() - camPositionOnMapInTilesY) * tileLength;

                        Set<DirectionType> rayDirections = new HashSet<>();
                        rayDirections.add(startDirection);
                        rayDirections.add(endDirection);

                        String animationId = null;

                        if (i < positions.size() - 1) {

                            if (!rayDirections.contains(DirectionType.LEFT) && !rayDirections.contains(DirectionType.RIGHT)) {
                                animationId = "portalRayVerticalA";
                            }

                            if (!rayDirections.contains(DirectionType.UP) && !rayDirections.contains(DirectionType.DOWN)) {
                                animationId = "portalRayHorizontalA";
                            }

                            if (startDirection == DirectionType.RIGHT && endDirection == DirectionType.UP) {
                                animationId = "portalRayBentNW";
                            }

                            if (startDirection == DirectionType.DOWN && endDirection == DirectionType.LEFT) {
                                animationId = "portalRayBentNW";
                            }

                            if (startDirection == DirectionType.RIGHT && endDirection == DirectionType.DOWN) {
                                animationId = "portalRayBentSW";
                            }

                            if (startDirection == DirectionType.UP && endDirection == DirectionType.LEFT) {
                                animationId = "portalRayBentSW";
                            }

                            if (startDirection == DirectionType.LEFT && endDirection == DirectionType.UP) {
                                animationId = "portalRayBentNE";
                            }

                            if (startDirection == DirectionType.DOWN && endDirection == DirectionType.RIGHT) {
                                animationId = "portalRayBentNE";
                            }

                            if (startDirection == DirectionType.UP && endDirection == DirectionType.RIGHT) {
                                animationId = "portalRayBentSE";
                            }

                            if (startDirection == DirectionType.LEFT && endDirection == DirectionType.DOWN) {
                                animationId = "portalRayBentSE";
                            }
                        } else {
                            DirectionType lastRayDirection = ray.getLastDirection();
                            if (lastRayDirection == DirectionType.LEFT) {
                                animationId = "portalRayEndWest";
                            } else if (lastRayDirection == DirectionType.RIGHT) {
                                animationId = "portalRayEndEast";
                            } else if (lastRayDirection == DirectionType.UP) {
                                animationId = "portalRayEndNorth";
                            } else {
                                animationId = "portalRayEndSouth";
                            }
                        }

                        Animation animation = ApplicationContext.instance().getAnimations().get(animationId);
                        animation.draw(x, y, tileLength, tileLength);

                    }
                }

            }

            Position targetPosition = ray.getTargetPosition();

            if (targetPosition != null) {

                boolean tooFarWest = targetPosition.getX() < camPositionOnMapInTilesX;
                boolean tooFarEast = targetPosition.getX() >= camPositionOnMapInTilesX + camViewWidthInTiles;
                boolean tooFarNorth = targetPosition.getY() < camPositionOnMapInTilesY;
                boolean tooFarSouth = targetPosition.getY() >= camPositionOnMapInTilesY + camViewHeightInTiles;

                if (!(tooFarEast || tooFarWest || tooFarNorth || tooFarSouth)) {

                    float portalTilePositionRelatedToCamX = ray.getTargetPosition().getX() - camPositionOnMapInTilesX;
                    float portalTilePositionRelatedToCamY = ray.getTargetPosition().getY() - camPositionOnMapInTilesY;

                    float offsetX = (portalTilePositionRelatedToCamX) * tileLength;
                    float offsetY = (portalTilePositionRelatedToCamY) * tileLength;

                    DirectionType direction = DirectionType.reverse(ray.getLastDirection());

                    Color colorPortalRoof = new Color(0.7f, 0.3f, 0.5f, 0.7f);
                    Color colorPortalWall = new Color(1, 0.5f, 0.5f, 0.7f);

                    if (bottomNotTop) {
                        if (direction == DirectionType.DOWN) {
                            g.setColor(colorPortalWall);
                            g.fillRect(offsetX, offsetY + PortalRenderer.PORTAL_SOUTH_WALL_EDGE_OFFSET, tileLength, tileLength - PortalRenderer.PORTAL_SOUTH_WALL_EDGE_OFFSET);
                        } else if (direction == DirectionType.UP) {
                        } else if (direction == DirectionType.LEFT) {
                            g.setColor(colorPortalWall);
                            g.fillRect(offsetX, offsetY + PortalRenderer.PORTAL_SOUTH_WALL_EDGE_OFFSET, PortalRenderer.PORTAL_THICKNESS, tileLength - PortalRenderer.PORTAL_SOUTH_WALL_EDGE_OFFSET);
                        } else {
                            g.setColor(colorPortalWall);
                            g.fillRect(offsetX + tileLength - PortalRenderer.PORTAL_THICKNESS, offsetY + PortalRenderer.PORTAL_SOUTH_WALL_EDGE_OFFSET, PortalRenderer.PORTAL_THICKNESS, tileLength - PortalRenderer.PORTAL_SOUTH_WALL_EDGE_OFFSET);
                        }
                    } else {
                        boolean alternateColor = timestamp / 120 % 2 == 1;
                        if (direction == DirectionType.DOWN) {
                            g.setColor(colorPortalRoof);
                            g.fillRect(offsetX, offsetY + PortalRenderer.PORTAL_SOUTH_WALL_EDGE_OFFSET - PortalRenderer.PORTAL_THICKNESS, tileLength, PortalRenderer.PORTAL_THICKNESS);
                        } else if (direction == DirectionType.UP) {
                            g.setColor(colorPortalRoof);
                            g.fillRect(offsetX, offsetY - PortalRenderer.PORTAL_NORTH_WALL_EDGE_OFFSET, tileLength, PortalRenderer.PORTAL_THICKNESS);
                        } else if (direction == DirectionType.LEFT) {
                            g.setColor(colorPortalRoof);
                            g.fillRect(offsetX, offsetY - PortalRenderer.PORTAL_NORTH_WALL_EDGE_OFFSET, PortalRenderer.PORTAL_THICKNESS, tileLength);
                        } else {
                            g.setColor(colorPortalRoof);
                            g.fillRect(offsetX + tileLength - PortalRenderer.PORTAL_THICKNESS, offsetY - PortalRenderer.PORTAL_NORTH_WALL_EDGE_OFFSET, PortalRenderer.PORTAL_THICKNESS, tileLength);
                        }
                    }

                }
            }

            g.scale(1 / cam.getZoomFactor(), 1 / cam.getZoomFactor());
            g.translate(camOffsetFromTileBeginX, camOffsetFromTileBeginY);
            g.translate(-sceneDrawingContext.x(), -sceneDrawingContext.y());
        }
    }

}
