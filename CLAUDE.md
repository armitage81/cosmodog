# Cosmodog — Code Specification

## Framework & Build

- **Slick2D** (LWJGL wrapper). All rendering runs on the OpenGL thread.
- **Apache Ant** (`build.xml`). Source root: `src/`. Output: `dist/cosmodog.jar`.
- Dependencies: `slick.jar`, `lwjgl.jar`, `guava-18.0.jar`, `jdom2-2.0.6.jar`, `json-20240303.jar`.

## Building and Launching from the Terminal

`javac` is not on the system PATH. Locate it with:
```bash
JAVAC=$(find ~/.jdks -name "javac.exe" | head -1)
JAVA=$(find ~/.jdks -name "java.exe" | head -1)
```
The classpath separator is `;` (Windows JDK, even when invoked from bash).

**Compile:**
```bash
find src -name "*.java" > .sources.txt && \
$JAVAC -d build \
  -cp "lib/slick2d/lib/slick.jar;lib/slick2d/lib/lwjgl.jar;lib/guava-18.0.jar;lib/jdom2-2.0.6.jar;lib/json-20240303.jar;lib/gdx-1.8.0.jar;lib/commons-beanutils-1.9.2.jar;lib/commons-logging-1.2.jar;lib/commons-collections-3.2.2.jar" \
  @.sources.txt 2>&1 && rm -f .sources.txt
```
Only deprecation/unchecked warnings are expected — any `Fehler` (error) line means a real compile failure.

**Launch:**
```bash
$JAVA \
  -Djava.library.path=lib/lwjgl/native/windows \
  -cp "build;resources;lib/slick2d/lib/slick.jar;lib/slick2d/lib/lwjgl.jar;lib/slick2d/lib/lwjgl_util.jar;lib/slick2d/lib/jinput.jar;lib/guava-18.0.jar;lib/jdom2-2.0.6.jar;lib/json-20240303.jar;lib/gdx-1.8.0.jar;lib/commons-beanutils-1.9.2.jar;lib/commons-logging-1.2.jar;lib/commons-collections-3.2.2.jar;lib/jogg-0.0.7.jar;lib/jorbis-0.0.15.jar" \
  antonafanasjew.cosmodog.CosmodogStarter &
```
Working directory must be the project root so asset paths resolve correctly.

## Package Layout

```
antonafanasjew/cosmodog/
  actions/          async action framework and all concrete actions
  camera/           camera and zoom logic
  globals/          singletons and constants (ApplicationContext, FontType, ResolutionHolder)
  model/            game model: actors, collectibles, dynamic pieces, inventory
  rendering/
    context/        DrawingContext hierarchy
    renderer/       all Renderer implementations (100+)
  resourcehandling/ asset loading and builder classes
  text/             Letter, LetterBuilder, LetterUtils (pixel font system)
  util/             stateless utility classes
```

Assets live outside `src/`:
```
assets/graphics/   sprites, tilesets, ui elements
assets/audio/      music/ and sounds/
assets/maps/       Tiled TMX map files
assets/texts/      gamelogs, intro, outro, credits
resources/         config files embedded in JAR (animations.txt, menus.txt, etc.)
```

## Naming Conventions

- Renderer classes: `[Domain]Renderer` — e.g. `PlayerRenderer`, `OverheadNotificationRenderer`
- Action classes: `[Event]Action` — e.g. `MovementAction`, `FightAction`
- Abstract bases: `Abstract[Type]` — e.g. `AbstractRenderer`, `AbstractAsyncAction`
- Holders/singletons: `[Domain]Holder` — e.g. `DrawingContextProviderHolder`
- Utilities: `[Domain]Utils` — e.g. `ApplicationContextUtils`, `CosmodogMapUtils`
- Constants: `UPPER_SNAKE_CASE`
- All fields are `private`. Expose via `get`/`set` pairs.

## Programming Rules

- **No `get` prefix on non-getter methods.** `get` is reserved for simple field accessors. Methods that do anything beyond returning a field value are named without it. Examples:
  - Singleton accessor: `instance()` not `getInstance()` — the backing field is static, so nothing is being "gotten" in the JavaBean sense.
  - Factory/lookup methods follow the same rule: name them by what they return or compute, not by prefixing `get`.

## Renderer Pattern

All renderers extend `AbstractRenderer` and implement:
```java
protected void renderInternally(GameContainer gc, Graphics g, Object renderingParameter)
```
`AbstractRenderer.render()` wraps this in profiling — never call `renderInternally` directly from outside. Cast `renderingParameter` to the expected type inside `renderInternally`. Pass rendering config as an inner parameter class on the renderer itself.

## Async Action Pattern

- All actions extend `AbstractAsyncAction` (adapter, no-op defaults).
- **Fixed-length**: extend `FixedLengthAsyncAction`; override `onUpdateInternal()`. Access progress via `completionRate` (0.0–1.0).
- **Variable-length**: extend `VariableLengthAsyncAction`; implementation controls when `hasFinished()` returns true.
- Store per-action state in the inherited `Map<String, Object> properties` via `getProperty(name)`.
- Lifecycle hooks: `onTrigger()`, `onUpdate()` / `onUpdateInternal()`, `onEnd()`, `beforeRegistration()`, `afterUnregistration()`.
- Register/query actions via `ActionRegistry` using `AsyncActionType` enum keys. Non-stackable types allow only one concurrent instance; stackable types queue.

## ApplicationContext

Singleton (double-checked locking). Holds all loaded assets: images, sprite sheets, animations, sounds, fonts, menus, character letter maps. **Must be instantiated on the OpenGL thread.** Access via `ApplicationContext.instance()`. Add new assets as private fields with a getter; load them in the constructor near related assets.

## DrawingContext

All screen regions are expressed as `DrawingContext` (interface: `x()`, `y()`, `w()`, `h()`). Obtain contexts from `DrawingContextProviderHolder.get().getDrawingContextProvider()`. There are two providers — `1920x1080` and `1280x720` — selected by `ResolutionHolder`. Never hardcode screen coordinates; derive from a DrawingContext.

## Font System

`FontType` enum pairs a `TrueTypeFont` with a `Color`. Every entry has a standard and a `_LowRes` variant (the LowRes variant typically uses antiAlias=true and a slightly smaller point size). When adding a new font type, add both variants. `FontProvider` selects the correct variant based on `ResolutionHolder`.

The project also has a pixel letter system (`Letter`, `DefaultLetterBuilder`, `alphabeth.png`) used by `OnScreenNotificationRenderer`. It is variable-width and separate from `FontType`.

## Model Serialization

Core model classes extend `CosmodogModel implements Serializable`. Include `@Serial private static final long serialVersionUID`. Mark non-serializable fields `transient` and restore them in `initTransientFields()` via `InitializationUtils`.

## Asset Loading Conventions

- Pixel-art images: `new Image(path, false, Image.FILTER_NEAREST)`
- Sprite sheets: `new SpriteSheet(path, tileWidth, tileHeight)`
- Colors: use `org.newdawn.slick.Color`. Custom colors use float RGB (0.0–1.0).

## Utility Classes

Utility classes contain only `static` methods; no instances. Follow the `[Domain]Utils` naming pattern.

## Commit Messages

Use the form **"[Subject] is [past participle] now."** — a single sentence, passive voice, present tense.

Examples:
- `Collision is implemented now.`
- `New unit type is added now.`
- `Overhead font rendering is replaced now.`
- `Player death animation is fixed now.`

## Ideas & Planning

Design plans and task lists live in `docs/ideas/[idea-name]/`. Each idea gets its own subdirectory containing a plan document and a `tasks.md` with flat, identified tasks (T01, T02, …).
