# InputOverlayX

[![Build](https://github.com/your-username/InputOverlayX/actions/workflows/build.yml/badge.svg)](https://github.com/your-username/InputOverlayX/actions/workflows/build.yml)

A premium **Keyboard & Mouse Overlay** mod for Minecraft Java Edition, built with Fabric.

Displays a fully accurate 104-key keyboard and a realistic mouse overlay directly on your HUD — with real-time input highlighting, smooth animations, and extensive customization.

---

## Features

### Full 104-Key Keyboard Overlay
- Every key on a real desktop keyboard: Esc, F1–F12, Print Screen, Scroll Lock, Pause Break, number row, all letter rows, navigation cluster, arrow keys, and full numpad
- Accurate key sizing and proportions matching a real physical keyboard
- **Full** and **Compact** layout modes — same keys, different scale

### Real-Time Input Highlighting
- Every key press lights up instantly
- Smooth animated transitions (press and release)
- Scroll wheel, left/right/middle mouse button all animate

### Realistic Mouse Overlay
- Left button, right button, middle button
- Scroll wheel with directional scroll indicators
- All clicks and scrolling animate in real time

### Themes
13 built-in themes: **Default · Dark · Light · Purple · Blue · Emerald · Red · Glass · Neon · Frost · Carbon · Minimal · Transparent**

### Full Customization
- Background color, border color, border thickness, corner radius
- Font color, font size, glow, shadow
- Pressed key color, hover color
- Opacity and scale (independent for keyboard and mouse)
- Drag & drop positioning via the built-in Editor

### Right Shift Quick Menu
Press **Right Shift** (configurable) to open a floating control panel — the game keeps running. Toggle overlays, switch themes, adjust opacity, open the editor, reset positions.

### Drag-and-Drop Editor
Open from the Quick Menu. Drag both overlays anywhere on screen. Right-click-drag or scroll to resize. Save or cancel independently. Game world remains fully visible.

### Mod Menu + Cloth Config Integration
Full settings screen accessible via Mod Menu with organized categories: General · Keyboard · Mouse · Themes · Appearance · Animation · Keybinds.

### Performance
- Client-side only — no server required
- Optimized HUD rendering — no FPS impact
- No memory leaks — proper lifecycle management

---

## Supported Minecraft Versions

| Version | Status |
|---------|--------|
| 1.21.1  | ✅ |
| 1.21.2  | ✅ |
| 1.21.3  | ✅ |
| 1.21.4  | ✅ |
| 1.21.5  | ✅ |
| 1.21.6  | ✅ |
| 1.21.7  | ✅ |
| 1.21.8  | ✅ |
| 1.21.9  | ✅ |
| 1.21.10 | ✅ |
| 1.21.11 | ✅ |

---

## Requirements

- **Minecraft Java Edition** (see table above)
- **Fabric Loader** 0.16.9+
- **Fabric API**
- **Cloth Config** (for settings screen)
- **Mod Menu** (optional, for in-game settings access)

---

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/installer/)
2. Download Fabric API and place it in your `mods/` folder
3. Download Cloth Config and place it in your `mods/` folder
4. Download `InputOverlayX-<version>.jar` from [Releases](../../releases) and place it in `mods/`
5. (Optional) Install [Mod Menu](https://modrinth.com/mod/modmenu) for in-game settings

---

## Building from Source

```bash
# Clone the repository
git clone https://github.com/your-username/InputOverlayX.git
cd InputOverlayX

# Build for the default version (1.21.1)
./gradlew build

# Build for a specific Minecraft version
./gradlew build \
  -Pminecraft_version=1.21.4 \
  -Pyarn_mappings=1.21.4+build.8 \
  -Ploader_version=0.16.9 \
  -Pfabric_version=0.110.0+1.21.4 \
  -Pcloth_config_version=15.0.140 \
  -Pmodmenu_version=13.0.0
```

The output JAR will be in `build/libs/`.

---

## GitHub Actions (CI)

Every push and pull request automatically builds JARs for **all 11 supported Minecraft versions** using a matrix build. Each version is independent — if one fails, the others continue. Artifacts are uploaded per-version and named `InputOverlayX-<mc_version>.jar`.

---

## Usage

| Action | Default |
|--------|---------|
| Open quick menu | Right Shift |
| Open editor from menu | Click "Open Editor" |
| Move overlay (editor) | Left-click drag |
| Resize overlay (editor) | Right-click drag or scroll wheel |
| Save editor changes | Ctrl+S or "Save" button |
| Close editor without saving | Escape or "Cancel" button |

---

## Project Structure

```
src/main/java/com/inputoverlayx/
├── client/          # Mod entrypoint, Mod Menu integration
├── config/          # Config class, Cloth Config factory
├── animation/       # Smooth press/release animation system
├── input/           # Raw GLFW input state tracker
├── keybind/         # Key binding registration & tick handler
├── mixin/           # Keyboard & mouse GLFW mixins
├── render/
│   ├── keyboard/    # Full 104-key layout data + renderer
│   └── mouse/       # Mouse silhouette renderer
├── theme/           # Theme enum + preset applier
├── ui/              # Quick menu screen, editor screen
└── util/            # Color helpers, render helpers
```

---

## Compatibility

Compatible with: Fabric API · Mod Menu · Cloth Config · Sodium · Lithium · Iris · ReplayMod · Fullscreen · Windowed · All GUI scales · High-DPI displays

---

## License

MIT License — see [LICENSE](LICENSE) for details.
