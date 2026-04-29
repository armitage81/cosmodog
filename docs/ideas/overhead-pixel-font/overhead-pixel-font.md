# Overhead Notifications: 8×8 Pixel Font

## Goal

Replace the TrueTypeFont (Courier New 20pt) used for overhead notifications above player and NPC heads with a pixel-based renderer driven by 8×8 sprite glyphs. The text color must be selectable per notification type, and each glyph must have a contrasting outline for readability.

## Context

Overhead notifications are managed by `OverheadNotificationAction` and rendered by `OverheadNotificationRenderer` → `TextBookRenderer` → Slick2D `TrueTypeFont`. A separate pixel letter system (`LetterTextRenderer`, `DefaultLetterBuilder`, `alphabeth.png`) already exists but is variable-width, not monospace, and has no color tinting or outline support.

The current text does **not** scale with `cam.getZoomFactor()` — only its screen position tracks the zoomed actor location. The new implementation must preserve this behavior (fixed scale factor, position tracks zoom).

---

## Step 1 — Create the 8×8 monospace sprite sheet

**New file:** `assets/graphics/ui/overhead_font_8x8.png`

- White-on-transparent pixels so Slick2D color filters can tint them to any color at render time.
- Fixed 8×8 pixels per cell.
- Layout: 16 columns × 6 rows = 96 slots, covering ASCII 32 (space) through 126 (~).
  - Index formula: `index = c - 32; col = index % 16; row = index / 16`
- Total image size: **128 × 48 pixels**.
- Source from a free 8×8 pixel font (e.g. IBM CP437 / PC Screen Font) or draw one. Pixels must be white with alpha transparency.

---

## Step 2 — Create `OverheadLetterBuilder`

**New class:** `src/antonafanasjew/cosmodog/text/OverheadLetterBuilder.java`

- Implements `LetterBuilder`.
- Constructor takes a `SpriteSheet` (the 128×48 sheet loaded with tile size 8×8).
- `buildLetters()`: iterates ASCII 32–126, extracts `spriteSheet.getSprite(index % 16, index / 16)` for each character, wraps it in `Letter.create(c, 8.0f, image)`.
- All characters have width `8.0f` (true monospace). No shadow padding — outline is handled at render time.

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
| `scaleFactor` | `float` | Fixed multiplier (e.g. `2.0f` → 16×16 output) |
| `x` | `float` | Top-left screen X |
| `y` | `float` | Top-left screen Y |

**Rendering logic per glyph:**

1. Look up the 8×8 sprite from the overhead letter map via `ApplicationContext.getOverheadCharacterLetters()`.
2. Scale the sprite copy by `scaleFactor`.
3. Draw the scaled sprite **8 times** (4 cardinal + 4 diagonal directions, each shifted by 1px) using `outlineColor` as the Slick2D image filter.
4. Draw the scaled sprite once at the actual position using `color` as the image filter.

**Word wrap:** compute total text width (`numChars × 8 × scaleFactor`), split on spaces when the line exceeds the allotted width (follow the pattern in `LetterUtils.splitTextForTextBoxWidth` but with fixed 8px char width), render each subsequent line below the previous at `8 × scaleFactor` pixel intervals.

---

## Step 4 — Load the sprite sheet in `ApplicationContext`

In `ApplicationContext.java` (near line 648 where `alphabethSheet` is loaded):

1. Load `SpriteSheet overheadFontSheet = new SpriteSheet("assets/graphics/ui/overhead_font_8x8.png", 8, 8)`.
2. Build: `this.overheadCharacterLetters = new OverheadLetterBuilder(overheadFontSheet).buildLetters();`
3. Add field `Map<Character, Letter> overheadCharacterLetters` and a `getOverheadCharacterLetters()` getter.

---

## Step 5 — Define pixel font color constants

In `FontType.java` (or a new dedicated constants class), add color pair constants (not TrueTypeFont entries):

| Constant | Value | Usage |
|---|---|---|
| `OVERHEAD_PIXEL_DEFAULT_COLOR` | `new Color(0.0f, 0.7f, 0.0f)` | Matches existing `Overhead` font color |
| `OVERHEAD_PIXEL_CRITICAL_COLOR` | `Color.red` | Matches existing `OverheadCritical` font color |
| `OVERHEAD_PIXEL_OUTLINE_COLOR` | `Color.black` | Always dark for contrast |

---

## Step 6 — Rewrite `OverheadNotificationRenderer`

In `OverheadNotificationRenderer.java`, replace the `TextBookRenderer` block (lines 125–127) with:

1. **Parse font tags:** scan `text` for `<font:critical>` and split into segments, each with an associated color (`default` → `OVERHEAD_PIXEL_DEFAULT_COLOR`, `critical` → `OVERHEAD_PIXEL_CRITICAL_COLOR`). Strip the tags from the display string.
2. **Scale factor:** use a fixed `2.0f` (renders 8×8 sprites as 16×16 pixels), independent of `cam.getZoomFactor()`. This matches the current TrueTypeFont behavior where glyph size is constant regardless of zoom.
3. **Centering:** compute `textPixelWidth = text.length() × 8 × scaleFactor` and center over the actor using the same `textOffsetX` approach already in the file.
4. **Invoke `OverheadPixelTextRenderer`** with the resolved color, `OVERHEAD_PIXEL_OUTLINE_COLOR`, scale factor, and screen position.
5. Remove the `FontRefToFontTypeMap` / `TextPageConstraints` / `TextBookRenderer` call.

---

## What does NOT change

- `OnScreenNotificationRenderer` and its `LetterTextRenderer` pipeline.
- `TextBookRenderer`, `FontType`, `FontRefToFontTypeMap` (other screens still use TrueTypeFont).
- All notification logic in `OverheadNotificationAction`.
- `alphabeth.png`, `DefaultLetterBuilder`, and the existing `characterLetters` map.
