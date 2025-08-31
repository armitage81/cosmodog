package antonafanasjew.cosmodog;

import antonafanasjew.cosmodog.domains.DirectionType;
import antonafanasjew.cosmodog.topology.Position;
import antonafanasjew.cosmodog.util.PositionUtils;
import com.google.common.collect.Lists;
import org.newdawn.slick.Animation;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.util.Log;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * This is an unfinished attempt to replace the animations.txt file by programmable animation provider.
 */

public class AnimationProvider {

    public static final String PATH_PREFIX = "assets/graphics/";
    public static final String FILE_SUFFIX = ".png";
    public static final int DEFAULT_SPRITE_LENGTH = 16;


    private boolean looping = true;
    private int spriteWidth = DEFAULT_SPRITE_LENGTH;
    private int spriteHeight = DEFAULT_SPRITE_LENGTH;
    private String address = null;

    Map<String, Animation> animations() {

        Map<String, Animation> retVal = new HashMap<>();



        address = "spacelift/spacelift_animations";
        looping = true;
        spriteWidth = 32;
        spriteHeight = 64;


        retVal.put("spaceliftCabin", animation(frames(one(2, 0), oneLongDur())));


        spriteWidth = DEFAULT_SPRITE_LENGTH;
        spriteHeight = DEFAULT_SPRITE_LENGTH;

        retVal.put("spaceliftRay", animation(frames(down(2, 0, 2), constDurs(50, 2))));
        retVal.put("spaceliftGroundDoor", animation(frames(one(0, 0), oneLongDur())));
        retVal.put("spaceliftSpaceDoor", animation(frames(one(1, 0), oneLongDur())));


        address = "player/player";
        looping = false;

        retVal.put("playerDying", animation(frames(right(0, 37, 8), durs(500, 500, 500, 500, 500, 500, 1500, 3000))));


        looping = true;


        List<String> dirSuffixes = Stream.of("Right", "Down", "Left", "Up").toList();

        for (int i = 0; i < dirSuffixes.size(); i++) {
            retVal.put("playerDefaultInanimated" + dirSuffixes.get(i), animation(frames(alt(0, i, 1, i, 8), altDurs(2000, 300, 8))));
            retVal.put("playerDefaultAnimated" + dirSuffixes.get(i), animation(frames(osc(right(0, 4 + i, 3)), constDurs(50, 4))));
            retVal.put("playerDefaultHit" + dirSuffixes.get(i), animation(frames(right(3, i, 3), constDurs(25, 3))));


            retVal.put("playerInPlatformInanimated" + dirSuffixes.get(i), animation(frames(one(6, 0), oneLongDur())));
            retVal.put("playerInPlatformAnimated" + dirSuffixes.get(i), retVal.get("playerInPlatformInanimated"));
            retVal.put("playerInPlatformHit" + dirSuffixes.get(i), retVal.get("playerInPlatformInanimated"));
            retVal.put("playerInPlatformHoldingItem" + dirSuffixes.get(i), retVal.get("playerInPlatformInanimated"));

        }


        address = "collectibles/vehicle";


        for (int i = 0; i < dirSuffixes.size(); i++) {
            retVal.put("playerInVehicleInanimated" + dirSuffixes.get(i), animation(frames(right(0, i, 2), constDurs(500, 2))));
            retVal.put("playerInVehicleAnimated" + dirSuffixes.get(i), retVal.get("playerInVehicleInanimated"));
            retVal.put("playerInVehicleHit" + dirSuffixes.get(i), retVal.get("playerInVehicleInanimated"));
            retVal.put("playerInVehicleHoldingItem" + dirSuffixes.get(i), retVal.get("playerInVehicleInanimated"));
        }

        address = "player/boat";


        for (int i = 0; i < dirSuffixes.size(); i++) {
            retVal.put("playerOnBoatInanimated" + dirSuffixes.get(i), animation(frames(osc(right(0, i, 3)), constDurs(250, 4))));
            retVal.put("playerOnBoatAnimated" + dirSuffixes.get(i), animation(frames(osc(right(0, 4 + i, 3)), constDurs(100, 4))));
            retVal.put("playerOnBoatHit" + dirSuffixes.get(i), retVal.get("playerOnBoatInanimated"));
            retVal.put("playerOnBoatHoldingItem" + dirSuffixes.get(i), retVal.get("playerOnBoatInanimated"));
        }

        return retVal;

    }

    public Animation animation(List<int[]> frames) {
        List<Position> positions = new ArrayList<>();
        List<Integer> durations = new ArrayList<>();
        for (int[] frame : frames) {
            Position position = Position.fromCoordinates(frame[0], frame[1], null);
            positions.add(position);
            durations.add(frame[2]);
        }
        return animation(address, positions, durations);
    }

    public Animation animation(String address, List<Position> spritePositions, List<Integer> frameDurations) {

        String path = PATH_PREFIX + address + FILE_SUFFIX;

        SpriteSheet spriteSheet = null;
        try {
            spriteSheet = new SpriteSheet(path, spriteWidth, spriteHeight);
        } catch (SlickException e) {
            Log.error("Could not load sprite sheet from the resource: " + path + ". " + e.getLocalizedMessage(), e);
        }

        Animation animation = new Animation();

        for (int i = 0; i < spritePositions.size(); i++) {
            Position spritePosition = spritePositions.get(i);
            int spriteX = (int)spritePosition.getX();
            int spriteY = (int)spritePosition.getY();
            int frameDuration = frameDurations.get(i);
            animation.addFrame(spriteSheet.getSprite(spriteX, spriteY), frameDuration);

        }

        boolean autoUpdate = false;

        animation.setAutoUpdate(autoUpdate);
        animation.setLooping(looping);

        return animation;
    }

    private List<int[]> frames(Supplier<List<Position>> posSupplier, Supplier<List<Integer>> durSupplier) {

        List<int[]> retVal = new ArrayList<>();

        List<Position> positions = posSupplier.get();
        List<Integer> durations = durSupplier.get();

        for (int i = 0; i < positions.size(); i++) {
            int x = (int)positions.get(i).getX();
            int y = (int)positions.get(i).getY();
            retVal.add(new int[]{x, y, durations.get(i)});
        }

        return retVal;

    }

    private Supplier<List<Position>> osc(Supplier<List<Position>> pos) {
        List<Position> seq = pos.get();
        List<Position> rev = seq.reversed().subList(1, seq.size() - 1);
        seq.addAll(rev);
        return () -> seq;
    }

    private Supplier<List<Position>> alt(int x, int y, int x2, int y2, int number) {
        List<Position> retVal = new ArrayList<>();
        Position pos1 = Position.fromCoordinates(x, y, null);
        Position pos2 = Position.fromCoordinates(x2, y2, null);
        for (int i = 0; i < number; i++) {
            retVal.add(i % 2 == 0 ? pos1 : pos2);
        }
        return () -> retVal;
    }

    private Supplier<List<Position>> down(int x, int y, int number) {
        return seq(x, y, number, DirectionType.DOWN);
    }

    private Supplier<List<Position>> right(int x, int y, int number) {
        return seq(x, y, number, DirectionType.RIGHT);
    }

    private Supplier<List<Position>> seq(int x, int y, int number, DirectionType directionType) {
        List<Position> retVal = new ArrayList<>();
        Position pos = Position.fromCoordinates(x, y, null);
        for (int i = 0; i < number; i++) {
            retVal.add(pos);
            pos = pos.nextPosition(directionType);
        }
        return () -> retVal;
    }

    private Supplier<List<Position>> one(int x, int y) {
        List<Position> retVal = new ArrayList<>();
        retVal.add(Position.fromCoordinates(x, y, null));
        return () -> retVal;
    }

    private Supplier<List<Integer>> altDurs(int dur, int dur2, int number) {
        List<Integer> retVal = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            retVal.add(i % 2 == 0 ? dur : dur2);
        }
        return () -> retVal;
    }

    private Supplier<List<Integer>> constDurs(int dur, int number) {
        List<Integer> retVal = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            retVal.add(dur);
        }
        return () -> retVal;
    }

    private Supplier<List<Integer>> durs(Integer... durs) {
        return () -> new ArrayList<>(Arrays.asList(durs));
    }

    private Supplier<List<Integer>> oneLongDur() {
        return oneDur(10000);
    }

    private Supplier<List<Integer>> oneDur(int dur) {
        List<Integer> retVal = new ArrayList<>();
        retVal.add(dur);
        return () -> retVal;
    }

}
