package antonafanasjew.cosmodog.model.dynamicpieces.portals;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.model.MoveableDynamicPiece;
import antonafanasjew.cosmodog.model.dynamicpieces.Block;
import antonafanasjew.cosmodog.topology.Position;

public class Cube  extends MoveableDynamicPiece {

    private boolean transparent;

    public static Cube create(Position position, boolean transparent) {
        Cube cube = new Cube();
        cube.setPosition(position);
        cube.transparent = transparent;
        return cube;
    }

    @Override
    public boolean permeableForPortalRay(DirectionType incomingDirection) {
        return transparent;
    }

    @Override
    public String animationId(boolean bottomNotTop) {
        if (bottomNotTop) {
            if (transparent) {
                return "dynamicPieceTransparentCubeBottom";
            } else {
                return "dynamicPieceCubeBottom";
            }
        } else {
            if (transparent) {
                return "dynamicPieceTransparentCubeTop";
            } else {
                return "dynamicPieceCubeTop";
            }
        }
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }
}
