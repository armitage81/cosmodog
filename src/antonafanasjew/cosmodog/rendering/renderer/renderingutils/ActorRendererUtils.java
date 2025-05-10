package antonafanasjew.cosmodog.rendering.renderer.renderingutils;

import antonafanasjew.cosmodog.ApplicationContext;
import antonafanasjew.cosmodog.actions.AsyncAction;
import antonafanasjew.cosmodog.actions.AsyncActionType;
import antonafanasjew.cosmodog.actions.camera.FadingAction;
import antonafanasjew.cosmodog.actions.death.RespawnAction;
import antonafanasjew.cosmodog.actions.death.WormAttackAction;
import antonafanasjew.cosmodog.actions.environmentaldamage.MineExplosionAction;
import antonafanasjew.cosmodog.actions.environmentaldamage.RadiationDamageAction;
import antonafanasjew.cosmodog.actions.environmentaldamage.ShockDamageAction;
import antonafanasjew.cosmodog.actions.fight.AbstractFightActionPhase;
import antonafanasjew.cosmodog.actions.fight.ArtilleryAttackActionPhase;
import antonafanasjew.cosmodog.actions.fight.EnemyAttackActionPhase;
import antonafanasjew.cosmodog.actions.fight.PlayerAttackActionPhase;
import antonafanasjew.cosmodog.actions.movement.CrossTileMotion;
import antonafanasjew.cosmodog.actions.movement.MovementAction;
import antonafanasjew.cosmodog.actions.movement.MovementAttemptAction;
import antonafanasjew.cosmodog.actions.teleportation.TeleportationAction;
import antonafanasjew.cosmodog.camera.Cam;
import antonafanasjew.cosmodog.domains.ActorAppearanceType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.domains.PlayerActionType;
import antonafanasjew.cosmodog.domains.WeaponType;
import antonafanasjew.cosmodog.globals.Layers;
import antonafanasjew.cosmodog.globals.TileType;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.PlayerMovementCache;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Player;
import antonafanasjew.cosmodog.model.inventory.Arsenal;
import antonafanasjew.cosmodog.model.inventory.InventoryItemType;
import antonafanasjew.cosmodog.model.portals.Portal;
import antonafanasjew.cosmodog.topology.Vector;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.util.Mappings;
import antonafanasjew.cosmodog.util.RenderingUtils;
import antonafanasjew.cosmodog.util.TileUtils;
import org.newdawn.slick.Animation;

import java.util.List;
import java.util.Optional;

public class ActorRendererUtils {


    public static CrossTileMotion actorMotion(Actor actor) {
        CosmodogGame game = ApplicationContextUtils.getCosmodogGame();

        MovementAction movementAction = (MovementAction)game
                .getActionRegistry()
                .getRegisteredAction(AsyncActionType.MOVEMENT, MovementAction.class)
        ;

        CrossTileMotion motion = null;
        if (movementAction != null) {
            motion = movementAction.getActorMotions().get(actor);
        }

        return motion;
    }
}
