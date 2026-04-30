# Overhead Notifications: Pixel Font

## Goal

Replace the TrueTypeFont (Courier New 20pt) used for overhead notifications above player and NPC heads with a pixel-based renderer driven by the existing `alphabeth_white.png` sprite sheet. The text color must be selectable per notification type, and each glyph must have a contrasting outline for readability.

## Context

Overhead notifications are managed by `OverheadNotificationAction` and rendered by `OverheadNotificationRenderer` → `TextBookRenderer` → Slick2D `TrueTypeFont`. The pixel letter system (`LetterTextRenderer`, `DefaultLetterBuilder`, `characterLetters`) already exists and is used by `OnScreenNotificationRenderer`. The existing layout utilities can be reused directly — no new builder class is needed.

Key existing infrastructure:
- `ApplicationContext.getCharacterLetters()` — the loaded variable-width letter map (ASCII 32–126), built from the original colored sprite sheet
- `LetterUtils.letterListBounds()` — computes actual pixel width of a string
- `LetterUtils.letterLineDrawingContexts()` — computes per-glyph draw positions
- `LetterUtils.splitTextForTextBoxWidth()` — word-wraps text to a pixel width
- `Letter.LETTER_HEIGHT = 17` — fixed glyph height

A second sprite sheet `assets/graphics/ui/alphabeth_white.png` exists — same layout and dimensions as `alphabeth.png` (1280×640, RGBA) but with white glyphs on a transparent background. This is what the overhead renderer will use so that Slick2D color filtering can tint each glyph to any color at render time. The existing `characterLetters` (colored) are preserved unchanged for `OnScreenNotificationRenderer`.

The current text does **not** scale with `cam.getZoomFactor()` — only its screen position tracks the zoomed actor location. The new implementation preserves this (fixed scale factor, position tracks zoom).

---

## Step 1 — Load `alphabeth_white.png` in `ApplicationContext`

In `ApplicationContext.java` (next to where `alphabethSheet` is loaded, near line 648):

1. Load `SpriteSheet alphabethWhiteSheet = new SpriteSheet("assets/graphics/ui/alphabeth_white.png", 16, 16)`.
2. Build a second letter map: `this.overheadCharacterLetters = new DefaultLetterBuilder(alphabethWhiteSheet).buildLetters();`
3. Add field `Map<Character, Letter> overheadCharacterLetters` and a `getOverheadCharacterLetters()` getter.

The existing `characterLetters` (built from the original colored sheet) remains untouched.

---

## Step 2 — Define pixel font color constants

In `FontType.java` (or a new dedicated constants class), add color pair constants (not TrueTypeFont entries):

| Constant | Value | Usage |
|---|---|---|
| `OVERHEAD_PIXEL_DEFAULT_COLOR` | `new Color(0.0f, 0.7f, 0.0f)` | Matches existing `Overhead` font color |
| `OVERHEAD_PIXEL_CRITICAL_COLOR` | `Color.red` | Matches existing `OverheadCritical` font color |
| `OVERHEAD_PIXEL_OUTLINE_COLOR` | `Color.black` | Always dark for contrast |

---

## Step 3 — Create `OverheadPixelTextRenderer`

**New class:** `src/antonafanasjew/cosmodog/rendering/renderer/OverheadPixelTextRenderer.java`

A self-contained, stateless singleton renderer.

**Rendering parameter inner class** `OverheadPixelTextParam`:

| Field | Type | Purpose |
|---|---|---|
| `text` | `String` | Display text (tags already stripped) |
| `color` | `Color` | Main glyph color |
| `outlineColor` | `Color` | Contrasting outline color |
| `scaleFactor` | `float` | Fixed multiplier (e.g. `2.0f`) |
| `x` | `float` | Top-left screen X |
| `y` | `float` | Top-left screen Y |
| `maxWidth` | `float` | Available width for word wrap |

**Rendering logic:**

1. Obtain the white letter map from `ApplicationContext.instance().getOverheadCharacterLetters()`.
2. Use `LetterUtils.splitTextForTextBoxWidth(text, scaleFactor, maxWidth)` to split text into lines.
3. For each line, get the `Letter` list via `LetterUtils.lettersForText()` and the per-glyph positions via `LetterUtils.letterLineDrawingContexts()`.
4. For each glyph, draw the scaled sprite **8 times** (4 cardinal + 4 diagonal, 1px offset each) using `outlineColor` as the Slick2D image filter (`image.draw(x + dx, y + dy, outlineColor)`).
5. Draw the scaled sprite once at the actual position using `color` as the image filter.
6. Advance Y by `Letter.LETTER_HEIGHT * scaleFactor` for each subsequent line.

---

## Step 4 — Rewrite `OverheadNotificationRenderer`

In `OverheadNotificationRenderer.java`, replace the `TextBookRenderer` block (lines 125–127) with:

1. **Parse font tags:** scan `text` for `<font:critical>` and `<font:default>` tags, split into segments each with an associated color (`default` → `OVERHEAD_PIXEL_DEFAULT_COLOR`, `critical` → `OVERHEAD_PIXEL_CRITICAL_COLOR`). Strip the tags from the display string.
2. **Scale factor:** use a fixed `2.0f`, independent of `cam.getZoomFactor()`. This matches the current TrueTypeFont behavior where glyph size is constant regardless of zoom.
3. **Centering:** use `LetterUtils.letterListBounds()` to get the actual pixel width of the text, then center it over the actor: `actorPosXOnVisibleMapInclMovementInclScale + (cam.getZoomFactor() * tileLength - textPixelWidth) / 2`.
4. **Invoke `OverheadPixelTextRenderer`** with the resolved `color`, `OVERHEAD_PIXEL_OUTLINE_COLOR`, `scaleFactor = 2.0f`, the centered `x`/`y`, and `maxWidth = 500`.
5. Remove unused imports: `FontRefToFontTypeMap`, `TextPageConstraints`, `TextBookRendererUtils`, `Book`.

---

## What does NOT change

- `OnScreenNotificationRenderer` and its `LetterTextRenderer` pipeline.
- `TextBookRenderer`, `FontType`, `FontRefToFontTypeMap` (other screens still use TrueTypeFont).
- All notification logic in `OverheadNotificationAction`.
- `DefaultLetterBuilder` and the existing `characterLetters` map (used by `OnScreenNotificationRenderer`).
