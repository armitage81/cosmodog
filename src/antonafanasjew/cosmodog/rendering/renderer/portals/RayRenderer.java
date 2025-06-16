package antonafanasjew.cosmodog.rendering.renderer.portals;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AbstractAsyncAction;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.portals.PortalShotAction;
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

        PortalShotAction action = ApplicationContextUtils.getCosmodogGame().getActionRegistry().getRegisteredAction(AsyncActionType.CUTSCENE, PortalShotAction.class);

        if (action != null) {
            Optional<AsyncAction> optPhase = action.getPhaseRegistry().currentPhase();
            int numberOfPhases = action.getPhaseRegistry().numberOfPhases();
            int currentPhaseNumber = action.getPhaseRegistry().currentPhaseNumber().orElse(numberOfPhases);

            //Second last phase is the creation of the portal.
            //Last phase is the camera returning to player.
            if (currentPhaseNumber < numberOfPhases - 2) {
                Animation animation = ApplicationContext.instance().getAnimations().get("portalPlasmaBall");
                Cam.CamTilePosition camTilePosition = cam.camTilePosition();
                float w = animation.getWidth() * cam.getZoomFactor();
                float h = animation.getHeight() * cam.getZoomFactor();
                float x = (gameContainer.getWidth() - w) / 2.0f;
                float y = (gameContainer.getHeight() - h) / 2.0f;
                animation.draw(x, y, w, h);
            }
        }

    }

}
