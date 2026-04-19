# Rendering System Documentation

## WICHTIG: LibGDX Rendering Pipeline

### Rendering-Reihenfolge im System

1. **ShapeRenderer Phase** (renderFill) - wird ZUERST aufgerufen
2. **SpriteBatch Phase** (renderTextBig, renderText) - wird DANACH aufgerufen

### Kritische Regel

**NIEMALS ShapeRenderer und SpriteBatch mischen!**
- ShapeRenderer benötigt `begin()` und `end()`
- SpriteBatch benötigt `begin()` und `end()`
- Diese können NICHT verschachtelt werden

### Icon-Rendering mit Text

Problem: Icons (Shapes) müssen im Text erscheinen, aber:
- Text wird in SpriteBatch gerendert (später)
- Icons werden in ShapeRenderer gerendert (früher)

Lösung: Zwei-Phasen-System

#### Phase 1: Berechnung (in renderFill)
```java
public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
    // 1. Titel holen mit {ICON} Platzhaltern
    String title = Localization.getInstance().getCommon().get("verifier_x_title");

    // 2. Icon-Array definieren
    int[] icons = {1, 2, 3};

    // 3. NUR BERECHNEN, nicht zeichnen!
    VerifierTextLayout.calculateIconPositions(title, changeX + 100, changeY + 40,
                                              280, 25, AssetLoader.font20, icons, 25);

    // 4. Icons aus berechneten Positionen zeichnen
    for (VerifierTextLayout.IconPosition iconPos : VerifierTextLayout.pendingIcons) {
        IconDraw.renderIcon(mainPanel, iconPos.x, iconPos.y, 20, 20, iconPos.icon);
    }
}
```

#### Phase 2: Text-Rendering (in renderTextBig)
```java
public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
    String title = Localization.getInstance().getCommon().get("verifier_x_title");
    int[] icons = {1, 2, 3};

    // Text mit Platz für Icons rendern
    VerifierTextLayout.renderWrappedTextWithIcons(mainPanel, title,
        changeX + 100, changeY + 40, 280, 25, AssetLoader.font20,
        Constants.COLOR_ORANGE, icons, 25);
}
```

### Übersetzungssystem mit Icons

#### Translation-Format
```properties
# In turingMachine_de.properties / turingMachine_en.properties
verifier_comparetoother_title=Die {ICON} und die {ICON} Nummer miteinander vergleichen
verifier_allsumnumber_title=Die Summe {ICON}{ICON}{ICON} aller Nummern zu {0}
```

- `{ICON}` wird automatisch durch das entsprechende Icon ersetzt
- Die Reihenfolge der Icons wird durch das icons-Array bestimmt
- Icons haben feste Breite (25px)

#### VerifierTextLayout Methoden

**calculateIconPositions()** - NUR Berechnung
- Wird in renderFill() aufgerufen
- Füllt `pendingIcons` Liste
- Zeichnet NICHTS

**renderWrappedTextWithIcons()** - NUR Text-Rendering
- Wird in renderTextBig() aufgerufen
- Rendert Text mit Platzhaltern für Icons
- Zeichnet KEINE Icons

### Warum diese Trennung?

**Performance-Gründe:**
- Renderer nicht bei jeder Entity neu erstellen
- Batching von Shapes und Sprites
- ShapeRenderer und SpriteBatch haben unterschiedliche begin/end Zyklen

### Typisches Verifier-Muster

```java
public class MyVerifier extends Verifier {

    @Override
    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
        if (all) {
            super.renderFill(mainPanel, changeX, changeY);
        }

        // Icon-Positionen berechnen
        String title = Localization.getInstance().getCommon().get("verifier_key");
        int[] icons = {1, 2}; // oder {1,2,3}
        VerifierTextLayout.calculateIconPositions(title, changeX + 100, changeY + 40,
                                                  280, 25, AssetLoader.font20, icons, 25);

        // Icons zeichnen
        for (VerifierTextLayout.IconPosition iconPos : VerifierTextLayout.pendingIcons) {
            IconDraw.renderIcon(mainPanel, iconPos.x, iconPos.y, 20, 20, iconPos.icon);
        }

        if (all) {
            // Weitere Icons für Spalten etc.
        }
    }

    @Override
    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
        String title = Localization.getInstance().getCommon().get("verifier_key");
        int[] icons = {1, 2};
        VerifierTextLayout.renderWrappedTextWithIcons(mainPanel, title,
            changeX + 100, changeY + 40, 280, 25, AssetLoader.font20,
            Constants.COLOR_ORANGE, icons, 25);
    }
}
```

### Icons

1. Triangle (Dreieck) = Icon 1
2. Square (Quadrat) = Icon 2
3. Circle (Kreis) = Icon 3

### Wichtige Parameter

- `changeX + 100, changeY + 40` - Standard-Position für Titel
- `280` - Max-Breite für Text-Wrapping
- `25` - Zeilenhöhe
- `25` - Icon-Breite (fest)
- `20, 20` - Icon-Größe beim Zeichnen

### Fehler vermeiden

❌ **FALSCH:**
```java
// Icons direkt in renderTextBig zeichnen
IconDraw.renderIcon(...) // FEHLER: Falscher Renderer-Kontext!
```

❌ **FALSCH:**
```java
// Text in renderFill zeichnen
mainPanel.drawString(...) // FEHLER: Falscher Renderer-Kontext!
```

✅ **RICHTIG:**
- Berechnung in renderFill
- Icons in renderFill zeichnen
- Text in renderTextBig zeichnen
