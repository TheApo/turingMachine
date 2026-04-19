# Turing Machine

> A digital adaptation of the acclaimed deduction board game — with **infinite procedurally generated puzzles** and additional validators beyond the tabletop original.

Crack the code. One verifier at a time.

---

## What is this?

**Turing Machine** is a pen-and-paper-meets-logic-puzzle board game designed by Fabien Gridel and Yoann Levet, published by Le Scorpion Masqué. A secret three-digit code is hiding behind a set of verifiers. Each verifier knows one property of the code — something like *"first digit is greater than 3"* or *"sum of the digits is less than 12"*. None of them reveals the code directly. Only by **combining every verifier's hint** does exactly one code survive.

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

## How to play

A secret three-digit code is hidden. Each digit is between **1** and **5**. Around it sits a handful of verifiers, each guarding one rule about the code.

**Each round:**

1. You enter a guess — any triple from `1 1 1` to `5 5 5`.
2. You pick up to three verifiers to question.
3. Each verifier compares your guess against *its own secret rule about the code* and hands back a **hint**, not an answer. Examples of what you might see:
   - `first > 3` — the first digit of the code is greater than 3
   - `sum < 12` — the digits of the code add up to less than 12
   - `second = 4` — the middle digit of the code equals 4
   - `third is even` — the last digit of the code is even
4. No single hint solves the puzzle. You cross-reference, eliminate, and slowly box the code in.
5. When only one code remains consistent with every hint, you submit it.

**The goal:** crack the code in as few rounds and as few verifier tests as possible. Puzzles are designed so that exactly one code satisfies every verifier — no luck, no randomness, pure deduction.

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
