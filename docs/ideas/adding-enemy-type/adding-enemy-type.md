
Replace `MYENEMY` / `MyEnemy` with your actual enemy name throughout.

---

## 1. UnitType enum

**File:** `src/antonafanasjew/cosmodog/domains/UnitType.java`

Add a new entry to the enum. The three boolean flags are `(alive, robotic, rangedUnit)`.

```java
MYENEMY(false, true, false),
```

Existing values for reference:

| Value       | alive | robotic | rangedUnit |
|-------------|-------|---------|------------|
| ROBOT       | false | true    | false      |
| LIGHT_TANK  | false | true    | false      |
| DRONE       | false | true    | false      |
| TURRET      | false | true    | false      |
| PIGRAT      | true  | false   | false      |
| ARTILLERY   | false | true    | true       |
| SCOUT       | false | true    | false      |
| FLOATER     | false | true    | false      |
| CONDUCTOR   | false | true    | true       |
| GUARDIAN    | false | false   | true       |
| SOLARTANK   | false | true    | false      |

---

## 2. TileType constant

**File:** `src/antonafanasjew/cosmodog/globals/TileType.java`

Add a new tile entry in the `META_ENEMY_TILE` section. Use `counter()` to auto-increment — the order here must match the order of tiles in the NPC meta tileset.

```java
META_ENEMY_TILE_MYENEMY(Layers.LAYER_META_NPC, counter()),
```

---

## 3. Mappings

**File:** `src/antonafanasjew/cosmodog/util/Mappings.java`

Add two entries:

```java
// In ENEMY_TILE_TYPE_TO_UNIT_TYPE block:
ENEMY_TILE_TYPE_TO_UNIT_TYPE.put(TileType.META_ENEMY_TILE_MYENEMY, UnitType.MYENEMY);

// In ANIMATION_KEY_BY_UNIT_TYPE block:
ANIMATION_KEY_BY_UNIT_TYPE.put(UnitType.MYENEMY, "MyEnemy");
```

The animation key (e.g. `"MyEnemy"`) is used to construct animation IDs like `enemyDefaultMyEnemyAnimatedRight`. Keep it short and PascalCase.

---

## 4. Enemy stats (JSON)

**File:** `resources/antonafanasjew/cosmodog/enemyfactorybuilder/enemyfactorymapping.json`

Add a new object to the array. The `id` must exactly match the `UnitType` enum name.

```json
{
  "id": "MYENEMY",
  "armorType": "LIGHT",
  "weaponType": "ENEMY_PISTOL",
  "chaussieType": "FEET",
  "maxLife": 15,
  "speedFactor": 1,
  "vision": {
    "stealth": "0/-1",
    "night": "0/-2 -1/-3 0/-3 1/-3 0/-4",
    "full": "-1/-4 1/-4 0/-5"
  }
}
```

Vision offsets are space-separated `x/y` pairs relative to the enemy's position (y is negative = upward on screen).

**ArmorType values:** `NONE`, `CHITIN`, `LIGHT`, `MEDIUM`, `HEAVY`, `FORTIFIED`

**ChaussieType values:** `FEET`, `WHEELS`, `TRACKS`, `FLYING`, `SWIMMING`, `STATIONARY`, `HOVERING`

**Enemy WeaponType values:**

| Value             | Basic Damage |
|-------------------|-------------|
| ENEMY_PISTOL      | 1           |
| ENEMY_DOUBLEGUN   | 4           |
| ENEMY_MACHINEGUN  | 3           |
| ENEMY_TURRET      | 7           |
| ENEMY_RAILGUN     | 9           |
| ENEMY_CLAWS       | 5           |
| ENEMY_ROCKETS     | 4           |
| ENEMY_SHOCKER     | 5           |
| ENEMY_TELEPATHIC  | 8           |
| ENEMY_LASER       | 15          |

---

## 5. Animations

**File:** `resources/antonafanasjew/cosmodog/animationbuilder/animations.txt`

Add one line per animation state. Format:

```
id;spriteSheetPath;tileWidth;tileHeight;[spriteX/spriteY/durationMs,...];looping;looping
```

Animation ID pattern: `enemy{Appearance}{AnimationKey}{Action}{Direction}`
- **Appearance:** `Default`, `InGrass`, `NoFeet`
- **Action:** `Inanimated` (idle), `Animated` (walking), `Shooting`, `Exploding`
- **Direction:** `Down`, `Up`, `Left`, `Right`

Minimum required entries (16 total for `Default` appearance):

```
enemyDefaultMyEnemyInanimatedDown;assets/graphics/enemies/enemy.myenemy.png;16;16;[0/0/500,1/0/500];true;true
enemyDefaultMyEnemyInanimatedUp;assets/graphics/enemies/enemy.myenemy.png;16;16;[0/1/500,1/1/500];true;true
enemyDefaultMyEnemyInanimatedLeft;assets/graphics/enemies/enemy.myenemy.png;16;16;[0/2/500,1/2/500];true;true
enemyDefaultMyEnemyInanimatedRight;assets/graphics/enemies/enemy.myenemy.png;16;16;[0/3/500,1/3/500];true;true

enemyDefaultMyEnemyAnimatedDown;assets/graphics/enemies/enemy.myenemy.png;16;16;[4/0/50,5/0/50];true;true
enemyDefaultMyEnemyAnimatedUp;assets/graphics/enemies/enemy.myenemy.png;16;16;[4/1/50,5/1/50];true;true
enemyDefaultMyEnemyAnimatedLeft;assets/graphics/enemies/enemy.myenemy.png;16;16;[4/2/50,5/2/50];true;true
enemyDefaultMyEnemyAnimatedRight;assets/graphics/enemies/enemy.myenemy.png;16;16;[4/3/50,5/3/50];true;true

enemyDefaultMyEnemyShootingDown;assets/graphics/enemies/enemy.myenemy.png;16;16;[0/5/500,1/5/500];true;true
enemyDefaultMyEnemyShootingUp;assets/graphics/enemies/enemy.myenemy.png;16;16;[0/6/500,1/6/500];true;true
enemyDefaultMyEnemyShootingLeft;assets/graphics/enemies/enemy.myenemy.png;16;16;[0/7/500,1/7/500];true;true
enemyDefaultMyEnemyShootingRight;assets/graphics/enemies/enemy.myenemy.png;16;16;[0/8/500,1/8/500];true;true

enemyDefaultMyEnemyExplodingDown;assets/graphics/enemies/enemy.myenemy.png;16;16;[6/5/300,7/5/300];false;false
enemyDefaultMyEnemyExplodingUp;assets/graphics/enemies/enemy.myenemy.png;16;16;[6/6/300,7/6/300];false;false
enemyDefaultMyEnemyExplodingLeft;assets/graphics/enemies/enemy.myenemy.png;16;16;[6/7/300,7/7/300];false;false
enemyDefaultMyEnemyExplodingRight;assets/graphics/enemies/enemy.myenemy.png;16;16;[6/8/300,7/8/300];false;false
```

Add equivalent blocks for `InGrass` and `NoFeet` appearances if the enemy can be covered by grass - up to the waist feet deep.

Sprite coordinates (`spriteX/spriteY`) are zero-based column/row indices into the sprite sheet.

---

## 6. Sprite sheet

**File:** `assets/graphics/enemies/enemy.myenemy.png`

Create a PNG sprite sheet with 16×16 pixel tiles. Arrange rows by direction and columns by animation frame, matching the coordinates referenced in your `animations.txt` entries. See existing sheets like `enemy.robot.png` as a reference.

---

## 7. Place on map

Open the target `.tmx` file (e.g. `assets/maps/map_main.tmx`) in **Tiled Map Editor**:

1. Select the `Meta_NPCs` layer.
2. Find the tile corresponding to `META_ENEMY_TILE_MYENEMY` in the NPC meta tileset (its position matches the order in `TileType.java`).
3. Paint the tile at the desired spawn positions.

Optionally, draw a rectangle in the `NpcHomeRegions` object group to define a patrol area for the enemy.
