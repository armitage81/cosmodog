package antonafanasjew.cosmodog.pathfinding;

import antonafanasjew.cosmodog.actions.movement.MovementPlan;
import antonafanasjew.cosmodog.collision.CollisionStatus;
import antonafanasjew.cosmodog.collision.CollisionValidator;
import antonafanasjew.cosmodog.collision.PassageBlockerType;
import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.util.ApplicationContextUtils;
import antonafanasjew.cosmodog.model.CosmodogGame;
import antonafanasjew.cosmodog.model.CosmodogMap;
import antonafanasjew.cosmodog.model.actors.Actor;
import antonafanasjew.cosmodog.model.actors.Enemy;
import antonafanasjew.cosmodog.model.portals.Entrance;
import antonafanasjew.cosmodog.topology.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoamingMovementPlanerTest {

    private CollisionValidator pass = new CollisionValidator() {
        @Override
        public CollisionStatus collisionStatus(CosmodogGame cosmodogGame, Actor actor, CosmodogMap cosmodogMap, Entrance entrance) {
            return CollisionStatus.instance(actor, cosmodogMap, entrance, true, PassageBlockerType.PASSABLE);
        }
    };

    private CollisionValidator block = new CollisionValidator() {
        @Override
        public CollisionStatus collisionStatus(CosmodogGame cosmodogGame, Actor actor, CosmodogMap cosmodogMap, Entrance entrance) {
            return CollisionStatus.instance(actor, cosmodogMap, entrance, false, PassageBlockerType.BLOCKED);
        }
    };


    @Test
    public void testIdleness() {
        RoamingMovementPlaner out = new RoamingMovementPlaner();
        out.setRandomBooleanSupplier(() -> false);
        out.setCosmodogMapSupplier(() -> new CosmodogMap(null));
        out.setCosmodogGameSupplier(CosmodogGame::new);
        Actor enemy = new Enemy();
        enemy.setPosition(Position.fromCoordinates(5, 10, ApplicationContextUtils.mapDescriptorMain()));
        MovementPlan movementPlan = out.calculateMovementPlanInternal(enemy, 3, pass, null);
        assertEquals(5, movementPlan.getStartPosition().getX());
        assertEquals(10, movementPlan.getStartPosition().getY());
        assertEquals(0, movementPlan.getMovementSteps().size());
    }

    @Test
    public void testMovingStraight() {
        RoamingMovementPlaner out = new RoamingMovementPlaner();
        out.setRandomBooleanSupplier(() -> true);
        out.setRandomIntegerSupplier(() -> 0);
        out.setCosmodogMapSupplier(() -> new CosmodogMap(null));
        out.setCosmodogGameSupplier(CosmodogGame::new);
        Actor enemy = new Enemy();
        enemy.setPosition(Position.fromCoordinates(5, 10, ApplicationContextUtils.mapDescriptorMain()));
        enemy.setDirection(DirectionType.LEFT);
        MovementPlan movementPlan = out.calculateMovementPlanInternal(enemy, 3, pass, null);

        assertEquals(5, movementPlan.getStartPosition().getX());
        assertEquals(10, movementPlan.getStartPosition().getY());

        assertEquals(1, movementPlan.getMovementSteps().size());

        assertEquals(4, movementPlan.positionAfterExecution().getX());
        assertEquals(10, movementPlan.positionAfterExecution().getY());

        enemy.setDirection(DirectionType.RIGHT);
        movementPlan = out.calculateMovementPlanInternal(enemy, 3, pass, null);
        assertEquals(5, movementPlan.getStartPosition().getX());
        assertEquals(10, movementPlan.getStartPosition().getY());

        assertEquals(1, movementPlan.getMovementSteps().size());

        assertEquals(6, movementPlan.positionAfterExecution().getX());
        assertEquals(10, movementPlan.positionAfterExecution().getY());

        enemy.setDirection(DirectionType.UP);
        movementPlan = out.calculateMovementPlanInternal(enemy, 3, pass, null);

        assertEquals(5, movementPlan.getStartPosition().getX());
        assertEquals(10, movementPlan.getStartPosition().getY());

        assertEquals(1, movementPlan.getMovementSteps().size());

        assertEquals(5, movementPlan.positionAfterExecution().getX());
        assertEquals(9, movementPlan.positionAfterExecution().getY());

        enemy.setDirection(DirectionType.DOWN);
        movementPlan = out.calculateMovementPlanInternal(enemy, 3, pass, null);

        assertEquals(5, movementPlan.getStartPosition().getX());
        assertEquals(10, movementPlan.getStartPosition().getY());

        assertEquals(1, movementPlan.getMovementSteps().size());

        assertEquals(5, movementPlan.positionAfterExecution().getX());
        assertEquals(11, movementPlan.positionAfterExecution().getY());

    }

}
