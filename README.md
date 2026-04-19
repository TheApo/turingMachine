# Turing Machine

> A digital adaptation of the acclaimed deduction board game — with **infinite procedurally generated puzzles** and additional validators beyond the tabletop original.

Crack the code. One verifier at a time.

---

## What is this?

**Turing Machine** is a pen-and-paper-meets-logic-puzzle board game designed by Fabien Gridel and Yoann Levet, published by Le Scorpion Masqué. You try to find a secret three-digit code by testing your guesses against a set of verifiers — each verifier answers *yes* or *no*, and from the pattern of answers you deduce the code.

This project is a **fan-made digital adaptation** built in Java with libGDX. It runs on Windows, Linux, macOS, Android, and directly in the browser via TeaVM.

## Why play the digital version?

| Feature                          | Tabletop | This project |
| -------------------------------- | :------: | :----------: |
| Curated puzzle book              |     ✓    |       ✓      |
| **Infinite generated levels**    |     —    |       ✓      |
| **Extra validator families**     |     —    |       ✓      |
| Auto-tracked deduction sheet     |     —    |       ✓      |
| Plays anywhere, instantly        |     —    |       ✓      |
| Smell of fresh cardboard         |     ✓    |       —      |

## How to play (30-second primer)

1. A secret three-digit code is hidden. Each digit is between **1** and **5**.
2. You pick a guess, then ask a handful of **verifiers** about it.
3. Each verifier checks one property — *"Is the first digit even?"*, *"Is the sum bigger than 6?"*, *"Does the triplet contain exactly one 3?"* …
4. The verifier replies **yes** or **no**, but *which* verifier gave which answer is what you have to figure out.
5. Combine the answers, eliminate the impossible, and submit the code.

No luck. No randomness in the answers. Pure deduction.

## Running it

Requires **JDK 17** on your PATH.

```bash
# Desktop (LWJGL3)
./gradlew desktop:run

# Browser (TeaVM, starts a local Jetty server)
./gradlew teavm:run

# Browser (release build, static files in teavm/build/dist)
./gradlew teavm:buildRelease

# Android — open the project in Android Studio and run the `android` module
```

On Windows use `gradlew.bat` instead of `./gradlew`.

## Tech stack

- **[libGDX](https://libgdx.com/) 1.14.0** — rendering, input, cross-platform game loop
- **[gdx-teavm](https://github.com/xpenatan/gdx-teavm) 1.5.3** — compiles Java straight to JavaScript, no GWT
- **Java 17** — across all modules
- **Gradle 8.13** — build tool
- Android Gradle Plugin 8.12 (minSdk 28, targetSdk 35)

## Project layout

```
core/     Platform-independent game code (the interesting stuff)
desktop/  LWJGL3 launcher
android/  Android launcher + manifest
teavm/    Web launcher — compiles core to JS
assets/   Fonts, images, i18n bundles (shared across platforms)
```

## Credits

- **Turing Machine** the board game was created by **Fabien Gridel** & **Yoann Levet**, published by **Le Scorpion Masqué**. Go buy a copy — it's genuinely great.
- Digital adaptation by [Dirk Aporius](https://github.com/TheApo).
- Built with [libGDX](https://libgdx.com/) and [gdx-teavm](https://github.com/xpenatan/gdx-teavm).

## License

This is a fan project, not affiliated with or endorsed by Le Scorpion Masqué. Code is provided as-is under the BSD-style notice in the source headers.
