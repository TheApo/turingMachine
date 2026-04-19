# Verifier System - Vollständiges Verständnis

## Übersicht

Dieses Dokument beschreibt, wie das Verifier-System funktioniert, wie Übersetzungen integriert sind und wie die Rendering-Pipeline arbeitet.

## 1. Rendering-Pipeline (LibGDX)

### Kritische Regel: Zwei-Phasen-Rendering

LibGDX erfordert strikte Trennung zwischen ShapeRenderer und SpriteBatch:

1. **Phase 1: renderFill()** - ShapeRenderer (Shapes/Icons zeichnen)
2. **Phase 2: renderTextBig() + renderText()** - SpriteBatch (Text rendern)

**NIEMALS MISCHEN!** Diese Renderer können nicht verschachtelt werden.

### Die drei Render-Methoden

Jeder Verifier hat drei Render-Methoden:

#### renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all)
- **Zweck:** Icons und Shapes zeichnen
- **Wann:** Wird ZUERST aufgerufen
- **Was hier passiert:**
  - Icons für Spalten zeichnen (falls vorhanden)
  - Separatoren zeichnen
  - **WICHTIG:** Wenn der Titel Icons enthält ({ICON} Platzhalter), müssen diese hier berechnet und gezeichnet werden!

#### renderTextBig(MainPanel mainPanel, int changeX, int changeY)
- **Zweck:** Titel des Verifiers rendern
- **Wann:** Wird nach renderFill() aufgerufen
- **Was hier passiert:**
  - Titel-Text aus Localization laden
  - Platzhalter ersetzen (z.B. `{0}` mit Werten)
  - Text mit automatischem Wrapping rendern

#### renderText(MainPanel mainPanel, int changeX, int changeY)
- **Zweck:** Spalten-Inhalte rendern
- **Wann:** Wird nach renderTextBig() aufgerufen
- **Was hier passiert:**
  - Spalten-Symbole rendern (oft hardcodiert)
  - Spalten-Beschreibungen aus Localization laden
  - Automatische Font-Auswahl für verschiedene Textlängen

## 2. Icon-System mit Titel-Text

### Problem: Icons müssen IM Text erscheinen

Wenn der Titel-Text Icons enthält (z.B. "Die {ICON} und die {ICON} Nummer"), müssen diese Icons gezeichnet werden, obwohl:
- Icons in renderFill() (ShapeRenderer Phase) gezeichnet werden
- Text in renderTextBig() (SpriteBatch Phase) gerendert wird

### Lösung: Zwei-Phasen Icon-System

#### Phase 1: Berechnung in renderFill()
```java
String title = Localization.getInstance().getCommon().get("verifier_xxx_title");
int[] icons = {1, 2};  // Welche Icons verwendet werden
VerifierTextLayout.calculateIconPositions(title, changeX + 100, changeY + 40,
                                          280, 25, AssetLoader.font20, icons, 25);

// Icons aus berechneten Positionen zeichnen
for (VerifierTextLayout.IconPosition iconPos : VerifierTextLayout.pendingIcons) {
    IconDraw.renderIcon(mainPanel, iconPos.x, iconPos.y, 20, 20, iconPos.icon);
}
```

#### Phase 2: Text-Rendering in renderTextBig()
```java
String title = Localization.getInstance().getCommon().get("verifier_xxx_title");
int[] icons = {1, 2};

VerifierTextLayout.renderWrappedTextWithIcons(mainPanel, title,
    changeX + 100, changeY + 40, 280, 25, AssetLoader.font20,
    Constants.COLOR_ORANGE, icons, 25);
```

**WICHTIG:** Das icons-Array muss in beiden Phasen IDENTISCH sein!

### Titel OHNE Icons

Wenn der Titel KEINE Icons enthält:

```java
// renderFill() - NUR Spalten-Icons, KEINE Titel-Icon-Berechnung
public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
    if (all) {
        super.renderFill(mainPanel, changeX, changeY);
        // Nur Spalten-Icons hier
        renderFillSeparator(mainPanel, changeX, changeY, 3);
    }
}

// renderTextBig() - Einfaches Text-Rendering OHNE Icons
public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
    String title = Localization.getInstance().getCommon().get("verifier_xxx_title");
    VerifierTextLayout.renderWrappedText(mainPanel, title, changeX + 100, changeY + 40,
                                         280, 25, AssetLoader.font20, Constants.COLOR_ORANGE);
}
```

## 3. Übersetzungs-System

### Key-Namenskonvention

Format: `verifier_[klassenname]_[element]`

**Beispiele:**
- `verifier_comparetoother_title` - Titel
- `verifier_comparetoother_col1_text` - Spalte 1 Text
- `verifier_preciseoddnumber_col2_line1` - Spalte 2, Zeile 1

### Struktur in Properties-Dateien

#### Title (Titel)
```properties
verifier_xxx_title=Text mit optionalem {ICON} und {0} Platzhalter
```

- `{ICON}` = Platzhalter für Icons (werden durch icons-Array ersetzt)
- `{0}`, `{1}` = Platzhalter für dynamische Werte (z.B. Zahlen)
- Automatisches Word-Wrapping bei maxWidth (280px)

#### Spalten - Variante 1: Nur Text
```properties
verifier_xxx_col1_text=Text für Spalte 1
verifier_xxx_col2_text=Text für Spalte 2
verifier_xxx_col3_text=Text für Spalte 3
```

#### Spalten - Variante 2: Mehrzeilig
```properties
verifier_xxx_col1_line1=Zeile 1 von Spalte 1
verifier_xxx_col1_line2=Zeile 2 von Spalte 1
verifier_xxx_col1_line3=Zeile 3 von Spalte 1
verifier_xxx_col2_line1=Zeile 1 von Spalte 2
...
```

### Was ist hardcodiert, was übersetzt?

#### HARDCODIERT (nicht übersetzbar):
- **Symbole in Spalten:** `<`, `=`, `>`, `+`, etc.
  ```java
  String[] symbols = {"<", "=", ">"};
  ```
- **Icon-IDs:** 1 = Dreieck, 2 = Quadrat, 3 = Kreis
- **Positionen und Layout-Parameter**

#### ÜBERSETZBAR (in Properties):
- **Alle Titel-Texte**
- **Alle Beschreibungs-Texte in Spalten**
- **Beispiel-Codes** (z.B. "242", "225")

## 4. Altes vs. Neues System

### Altes System (vor der Migration)

**Titel:**
- Manuell auf Zeilen aufgeteilt mit `;` Separator
- Beispiel: `verifier_xxx=Zeile 1;Zeile 2`
- Problem: Funktioniert nicht für alle Sprachen (unterschiedliche Textlängen)

**Spalten:**
- Komplett hardcodiert im Java-Code
- Keine Übersetzung möglich

**Icons:**
- Fest positioniert (z.B. `changeX + 140, changeY + 35`)
- Keine Icons im Titel-Text

### Neues System (aktuell)

**Titel:**
- **Eine durchgehende Zeile** ohne manuelle Umbrüche
- Automatisches Word-Wrapping basierend auf Textbreite
- Funktioniert für alle Sprachen automatisch
- Icons können mit `{ICON}` Platzhaltern eingefügt werden

**Spalten:**
- Texte aus Localization-Dateien
- Symbole bleiben hardcodiert
- Automatische Font-Auswahl für lange Texte

**Icons:**
- Dynamisch positioniert basierend auf Text
- Können im Titel-Text eingefügt werden

## 5. VerifierTextLayout Helper-Klasse

### Wichtige Methoden

#### calculateIconPositions()
```java
public static void calculateIconPositions(String text, int startX, int startY,
                                          int maxWidth, int lineHeight, BitmapFont font,
                                          int[] icons, int iconWidth)
```
- Berechnet Positionen für Icons basierend auf {ICON} Platzhaltern
- Füllt `pendingIcons` Liste
- **Zeichnet NICHTS!**
- Wird in renderFill() aufgerufen

#### renderWrappedTextWithIcons()
```java
public static int renderWrappedTextWithIcons(MainPanel mainPanel, String text, int startX, int startY,
                                             int maxWidth, int lineHeight, BitmapFont font, float[] color,
                                             int[] icons, int iconWidth)
```
- Rendert Text mit automatischem Wrapping
- Ersetzt {ICON} durch Leerraum (Icons wurden bereits in renderFill gezeichnet!)
- Wird in renderTextBig() aufgerufen

#### renderWrappedText()
```java
public static int renderWrappedText(MainPanel mainPanel, String text, int startX, int startY,
                                    int maxWidth, int lineHeight, BitmapFont font, float[] color)
```
- Einfaches Text-Rendering ohne Icons
- Automatisches Word-Wrapping
- Für Titel ohne Icons

#### selectFont()
```java
public static BitmapFont selectFont(String text, float maxWidth, BitmapFont defaultFont)
```
- Wählt automatisch kleinere Font, wenn Text zu lang
- Verhindert Überlauf bei langen Übersetzungen
- Fallback: font20 → font15 → font12

## 6. Standard-Parameter

### Titel-Position und Größe
- **Position:** `changeX + 100, changeY + 40`
- **Max-Breite:** `280` Pixel
- **Zeilenhöhe:** `25` Pixel
- **Font:** `AssetLoader.font20` (mit auto-downgrade)
- **Farbe:** `Constants.COLOR_ORANGE`

### Icon-Parameter
- **Icon-Breite im Text:** `25` Pixel (Platzhalter-Breite)
- **Icon-Render-Größe:** `20x20` Pixel (tatsächliche Größe)
- **Icons:**
  - 1 = Triangle (Dreieck)
  - 2 = Square (Quadrat)
  - 3 = Circle (Kreis)

### Spalten-Parameter
- **Spaltenbreite:** Meist `133` Pixel (bei 3 Spalten)
- **Spalten-Y-Position:** `changeY + 135` (Symbole), `changeY + 170` (Text)

## 7. Typisches Verifier-Pattern

### Verifier MIT Icons im Titel

```java
public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
    if (all) {
        super.renderFill(mainPanel, changeX, changeY);
    }

    // Icon-Positionen für Titel berechnen
    String title = Localization.getInstance().getCommon().get("verifier_xxx_title");
    int[] icons = {1, 2};
    VerifierTextLayout.calculateIconPositions(title, changeX + 100, changeY + 40,
                                              280, 25, AssetLoader.font20, icons, 25);

    // Icons zeichnen
    for (VerifierTextLayout.IconPosition iconPos : VerifierTextLayout.pendingIcons) {
        IconDraw.renderIcon(mainPanel, iconPos.x, iconPos.y, 20, 20, iconPos.icon);
    }

    if (all) {
        // Spalten-Icons hier...
        renderFillSeparator(mainPanel, changeX, changeY, 2);
    }
}

public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
    String title = Localization.getInstance().getCommon().get("verifier_xxx_title");
    int[] icons = {1, 2};
    VerifierTextLayout.renderWrappedTextWithIcons(mainPanel, title,
        changeX + 100, changeY + 40, 280, 25, AssetLoader.font20,
        Constants.COLOR_ORANGE, icons, 25);
}
```

### Verifier OHNE Icons im Titel

```java
public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {
    if (all) {
        super.renderFill(mainPanel, changeX, changeY);
        // Nur Spalten-Icons hier...
        renderFillSeparator(mainPanel, changeX, changeY, 3);
    }
}

public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
    String title = Localization.getInstance().getCommon().get("verifier_xxx_title");
    VerifierTextLayout.renderWrappedText(mainPanel, title,
        changeX + 100, changeY + 40, 280, 25, AssetLoader.font20,
        Constants.COLOR_ORANGE);
}
```

### Spalten mit Symbolen und Texten

```java
public void renderText(MainPanel mainPanel, int changeX, int changeY) {
    super.renderText(mainPanel, changeX, changeY);

    Localization loc = Localization.getInstance();
    String[] textKeys = {"verifier_xxx_col1_text", "verifier_xxx_col2_text", "verifier_xxx_col3_text"};
    String[] symbols = {"<", "=", ">"};  // HARDCODIERT

    for (int col = 0; col < 3; col++) {
        int x = changeX + 60 + col * 133;

        // Symbol (hardcodiert)
        mainPanel.drawString(symbols[col], x, changeY + 135, Constants.COLOR_BLACK,
            AssetLoader.font20, DrawString.MIDDLE, false, false);

        // Text aus Localization mit automatischer Font-Auswahl
        String text = loc.getCommon().get(textKeys[col]);
        BitmapFont font = VerifierTextLayout.selectFont(text, 123, AssetLoader.font15);
        mainPanel.drawString(text, x + 2, changeY + 170, Constants.COLOR_BLACK,
            font, DrawString.MIDDLE, false, false);
    }
}
```

## 8. Häufige Fehler vermeiden

### ❌ FALSCH: Icons in renderTextBig zeichnen
```java
// FEHLER: Falscher Renderer-Kontext!
IconDraw.renderIcon(...) // in renderTextBig()
```

### ❌ FALSCH: Text in renderFill zeichnen
```java
// FEHLER: Falscher Renderer-Kontext!
mainPanel.drawString(...) // in renderFill()
```

### ❌ FALSCH: Icons-Array unterschiedlich
```java
// renderFill()
int[] icons = {1, 2, 3};  // 3 Icons!

// renderTextBig()
int[] icons = {1, 2};  // 2 Icons! FALSCH!
```

### ✅ RICHTIG: Synchronisierte Phasen
- Icon-Berechnung und Zeichnung in renderFill()
- Text-Rendering in renderTextBig()
- Icons-Array muss identisch sein

## 9. Migration-Checklist & Vergleichs-Prozess

### KRITISCH: Vergleichsprozess für jeden Verifier

**IMMER diese Schritte befolgen:**

1. **BACKUP-DATEI LESEN** (D:\workspace\verifier\XxxVerifier.java)
   - renderTextBig() analysieren → Originaler Titel-Text
   - renderText() analysieren → Originale Spalten-Texte
   - renderFill() analysieren → Icons im Titel? Ja/Nein

2. **AKTUELLE DATEI LESEN** (D:\workspace\logic\core\src\...\XxxVerifier.java)
   - Welche Keys werden verwendet? (z.B. "verifier_a_title" → alt!)
   - Welche Render-Methoden sind implementiert?
   - Sind Icons im Titel enthalten? (calculateIconPositions/renderWrappedTextWithIcons)

3. **PROPERTIES LESEN** (turingMachine_en.properties)
   - Welcher Text steht aktuell im Title?
   - Welcher Text steht in den Column-Texten?

4. **VERGLEICHEN:**
   - ✅ **Titel-Text:** ALT vs. NEU vergleichen
     - Wurde "that" zu "if" geändert? → ZURÜCK zu "that"
     - Wurde Text verkürzt/vereinfacht? → ZURÜCK zum alten Text
   - ✅ **Column-Texte:** ALT vs. NEU vergleichen
     - Wurden Texte geändert? → ZURÜCK zu alten Texten
   - ✅ **Icons im Titel:**
     - ALT hatte KEINE Icons → NEU darf KEINE Icons haben!
     - ALT hatte Icons → NEU muss Icons haben!

5. **KORREKTUREN DURCHFÜHREN:**
   - Properties korrigieren (Texte zurück zu alt)
   - Java-Keys updaten (z.B. "verifier_a_title" → "verifier_classname_title")
   - renderFill() korrigieren (Icons entfernen falls nicht im Original)
   - renderTextBig() korrigieren (renderWrappedText vs. renderWrappedTextWithIcons)
   - renderText() korrigieren (Keys + gleiche Positionen wie alt)

### Wichtig: Alte Texte haben IMMER Recht!

**Die Texte aus D:\workspace\verifier\ sind die korrekte Quelle!**
- Diese Texte sind die Original-Texte VOR der kaputten Migration
- ALLE Texte in turingMachine_en.properties müssen mit den alten Texten übereinstimmen
- NEUE flexible Methoden (VerifierTextLayout) verwenden, aber mit ALTEN Texten!

### Migration-Checklist

Beim Migrieren eines alten Verifiers zum neuen System:

- [ ] **Alte Texte aus Backup extrahieren** (D:\workspace\verifier\)
- [ ] **Titel überprüfen:**
  - [ ] Hat der alte Titel Icons? → renderFill() anpassen
  - [ ] Kein Icon-System? → Einfaches renderWrappedText() verwenden
  - [ ] Titel-Text 1:1 übernehmen (NICHT vereinfachen!)
- [ ] **Spalten überprüfen:**
  - [ ] Symbole identifizieren (bleiben hardcodiert)
  - [ ] Texte 1:1 aus alter Version übernehmen
  - [ ] selectFont() für automatische Font-Auswahl verwenden
  - [ ] Gleiche Positionen wie in alter Version
- [ ] **Properties erstellen/korrigieren:**
  - [ ] EN und DE Version
  - [ ] Gleiche Keys verwenden
  - [ ] {0} Platzhalter für Werte (wie im alten Code)
  - [ ] {ICON} nur wenn wirklich Icons im ALTEN Titel waren
- [ ] **Java-Code korrigieren:**
  - [ ] Keys von alt (verifier_a_) zu neu (verifier_classname_)
  - [ ] renderFill() Icons nur wenn im alten Titel vorhanden
  - [ ] renderTextBig() mit korrekter Methode (mit/ohne Icons)
  - [ ] renderText() mit gleichen Positionen wie alt
- [ ] **Testen mit beiden Sprachen**

## 10. Wichtige Erkenntnisse

1. **Icons im Titel sind OPTIONAL** - Viele Verifier haben KEINE Icons im Titel, nur in den Spalten!
2. **Symbole bleiben hardcodiert** - `<`, `=`, `>` etc. werden NICHT übersetzt
3. **Ein Titel, eine Zeile** - Automatisches Wrapping macht manuelle Umbrüche überflüssig
4. **Zwei-Phasen-Rendering ist essentiell** - ShapeRenderer und SpriteBatch niemals mischen!
5. **Icons-Array muss synchron sein** - Zwischen renderFill() und renderTextBig()
