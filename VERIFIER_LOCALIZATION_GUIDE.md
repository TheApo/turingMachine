# Verifier Localization System - Developer Guide

## Overview

This guide explains the new flexible localization system for Verifier rendering, which supports multiple languages with different text lengths and automatically adapts layouts.

## Architecture

### 1. **VerifierTextLayout.java** - Layout Helper Class

Located in: `core/src/com/apogames/logic/game/logic/verifier/VerifierTextLayout.java`

**Key Features:**
- **TextLine**: Stores text with position and font information
- **ColumnLayout**: Defines column-based layouts with configurable widths
- **parseTextLines()**: Splits multi-line text from localization strings (separator: `|` or `;`)
- **parseColumns()**: Parses column-based layouts (separator: `::`)
- **selectFont()**: Automatically selects appropriate font size based on text width

### 2. **Localization Files**

Located in: `assets/i18n/`
- `turingMachine_en.properties` (English)
- `turingMachine_de.properties` (German)

**Naming Convention:**
```
verifier_[id]_[element]
```

**Examples:**
- `verifier_preciseoddnumber_title` - Title text (multi-line with `|` separator)
- `verifier_preciseoddnumber_col1_line1` - Column 1, Line 1
- `verifier_comparetoother_symbol1` - Symbol for column 1
- `verifier_allsumnumber_col1_text` - Description text with placeholders `{0}`

## Implementation Examples

### Example 1: PreciseOddNumberVerifier (Verifier A)

**4 columns with 3 lines each**

**Localization Keys:**
```properties
# English
verifier_preciseoddnumber_title=how many odd numbers|there are in the|code
verifier_preciseoddnumber_col1_line1=Zero odd
verifier_preciseoddnumber_col1_line2=number
verifier_preciseoddnumber_col1_line3=242
verifier_preciseoddnumber_col2_line1=One odd
...

# German
verifier_preciseoddnumber_title=Wie viele ungerade|Zahlen sind im|Code
verifier_preciseoddnumber_col1_line1=Null ungerade
verifier_preciseoddnumber_col1_line2=Zahlen
verifier_preciseoddnumber_col1_line3=242
```

**Code Implementation:**
```java
public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
    String titleText = Localization.getInstance().getCommon().get("verifier_preciseoddnumber_title");
    List<VerifierTextLayout.TextLine> titleLines = VerifierTextLayout.parseTextLines(
        titleText, 40, 25, AssetLoader.font15
    );

    for (VerifierTextLayout.TextLine line : titleLines) {
        BitmapFont font = VerifierTextLayout.selectFont(line.text, 280, line.font);
        mainPanel.drawString(line.text, changeX + 100, changeY + line.offsetY,
            Constants.COLOR_ORANGE, font, DrawString.BEGIN, false, false);
    }
}

public void renderText(MainPanel mainPanel, int changeX, int changeY) {
    Localization loc = Localization.getInstance();
    int[] columnOffsets = {50, 150, 250, 350};
    String[] columnKeys = {"verifier_preciseoddnumber_col1_", "verifier_preciseoddnumber_col2_",
                           "verifier_preciseoddnumber_col3_", "verifier_preciseoddnumber_col4_"};

    for (int col = 0; col < 4; col++) {
        for (int line = 1; line <= 3; line++) {
            String key = columnKeys[col] + "line" + line;
            String text = loc.getCommon().get(key);
            BitmapFont font = VerifierTextLayout.selectFont(text, 90, AssetLoader.font15);
            mainPanel.drawString(text, changeX + columnOffsets[col],
                changeY + 127 + (line - 1) * 18, Constants.COLOR_BLACK, font,
                DrawString.MIDDLE, false, false);
        }
    }
}
```

### Example 2: CompareToOtherVerifier (Verifier B)

**3 columns with symbols and text**

**Localization Keys:**
```properties
# English
verifier_comparetoother_title=the      and the     number|compared to each other
verifier_comparetoother_symbol1=<
verifier_comparetoother_symbol2==
verifier_comparetoother_symbol3=>
verifier_comparetoother_col1_text=is smaller than
verifier_comparetoother_col2_text=is equal to
verifier_comparetoother_col3_text=is greater than

# German
verifier_comparetoother_title=Die      und die     Nummer|miteinander vergleichen
verifier_comparetoother_symbol1=<
verifier_comparetoother_symbol2==
verifier_comparetoother_symbol3=>
verifier_comparetoother_col1_text=ist kleiner als
verifier_comparetoother_col2_text=ist gleich
verifier_comparetoother_col3_text=ist groesser als
```

**Code Implementation:**
```java
public void renderText(MainPanel mainPanel, int changeX, int changeY) {
    Localization loc = Localization.getInstance();
    String[] symbolKeys = {"verifier_comparetoother_symbol1", "verifier_comparetoother_symbol2", "verifier_comparetoother_symbol3"};
    String[] textKeys = {"verifier_comparetoother_col1_text", "verifier_comparetoother_col2_text", "verifier_comparetoother_col3_text"};

    for (int col = 0; col < 3; col++) {
        int x = changeX + 60 + col * 133;

        // Render symbol
        String symbol = loc.getCommon().get(symbolKeys[col]);
        mainPanel.drawString(symbol, x, changeY + 135, Constants.COLOR_BLACK,
            AssetLoader.font20, DrawString.MIDDLE, false, false);

        // Render description with auto font selection
        String text = loc.getCommon().get(textKeys[col]);
        BitmapFont font = VerifierTextLayout.selectFont(text, 123, AssetLoader.font15);
        mainPanel.drawString(text, x + 2, changeY + 170, Constants.COLOR_BLACK,
            font, DrawString.MIDDLE, false, false);
    }
}
```

### Example 3: AllSumNumberVerifier (Verifier C)

**3 columns with dynamic value placeholders**

**Localization Keys:**
```properties
# English
verifier_allsumnumber_title=the sum of the     and    |numbers to {0}
verifier_allsumnumber_symbol1=< {0}
verifier_allsumnumber_symbol2== {0}
verifier_allsumnumber_symbol3=> {0}
verifier_allsumnumber_col1_text=is smaller than {0}
verifier_allsumnumber_col2_text=is equal to {0}
verifier_allsumnumber_col3_text=is greater than {0}

# German
verifier_allsumnumber_title=Die Summe der     und    |Nummern zu {0}
verifier_allsumnumber_symbol1=< {0}
verifier_allsumnumber_symbol2== {0}
verifier_allsumnumber_symbol3=> {0}
verifier_allsumnumber_col1_text=ist kleiner als {0}
verifier_allsumnumber_col2_text=ist gleich {0}
verifier_allsumnumber_col3_text=ist groesser als {0}
```

**Code Implementation:**
```java
public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
    String titleText = Localization.getInstance().getCommon().get("verifier_allsumnumber_title");
    // Replace placeholder with actual value
    titleText = titleText.replace("{0}", String.valueOf(this.value));

    List<VerifierTextLayout.TextLine> titleLines = VerifierTextLayout.parseTextLines(
        titleText, 40, 25, AssetLoader.font20
    );

    for (VerifierTextLayout.TextLine line : titleLines) {
        BitmapFont font = VerifierTextLayout.selectFont(line.text, 280, line.font);
        mainPanel.drawString(line.text, changeX + 100, changeY + line.offsetY,
            Constants.COLOR_ORANGE, font, DrawString.BEGIN, false, false);
    }
}

public void renderText(MainPanel mainPanel, int changeX, int changeY) {
    Localization loc = Localization.getInstance();
    String[] symbolKeys = {"verifier_allsumnumber_symbol1", "verifier_allsumnumber_symbol2", "verifier_allsumnumber_symbol3"};
    String[] textKeys = {"verifier_allsumnumber_col1_text", "verifier_allsumnumber_col2_text", "verifier_allsumnumber_col3_text"};

    for (int col = 0; col < 3; col++) {
        int x = changeX + 66 + col * 133;

        // Replace placeholder in symbol
        String symbol = loc.getCommon().get(symbolKeys[col])
            .replace("{0}", String.valueOf(this.value));
        mainPanel.drawString(symbol, x + 24, changeY + 135, Constants.COLOR_BLACK,
            AssetLoader.font15, DrawString.BEGIN, false, false);

        // Replace placeholder in text
        String text = loc.getCommon().get(textKeys[col])
            .replace("{0}", String.valueOf(this.value));
        BitmapFont font = VerifierTextLayout.selectFont(text, 123, AssetLoader.font15);
        mainPanel.drawString(text, x, changeY + 170, Constants.COLOR_BLACK,
            font, DrawString.MIDDLE, false, false);
    }
}
```

## Adding a New Verifier (Step-by-Step)

### Step 1: Add Localization Keys

Add to both `turingMachine_en.properties` and `turingMachine_de.properties`:

```properties
# Verifier E - NewVerifier
verifier_e_title=First line|Second line|Third line
verifier_e_col1_line1=Text 1
verifier_e_col1_line2=Text 2
verifier_e_col2_line1=Text 3
# ... add all necessary keys
```

### Step 2: Implement renderTextBig()

```java
public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
    String titleText = Localization.getInstance().getCommon().get("verifier_e_title");
    List<VerifierTextLayout.TextLine> titleLines = VerifierTextLayout.parseTextLines(
        titleText, 40, 25, AssetLoader.font20
    );

    for (VerifierTextLayout.TextLine line : titleLines) {
        BitmapFont font = VerifierTextLayout.selectFont(line.text, 280, line.font);
        mainPanel.drawString(line.text, changeX + 100, changeY + line.offsetY,
            Constants.COLOR_ORANGE, font, DrawString.BEGIN, false, false);
    }
}
```

### Step 3: Implement renderText()

```java
public void renderText(MainPanel mainPanel, int changeX, int changeY) {
    super.renderText(mainPanel, changeX, changeY);

    Localization loc = Localization.getInstance();

    // Define your layout
    int columnCount = 3; // Adjust based on your needs
    int columnWidth = 400 / columnCount;

    for (int col = 0; col < columnCount; col++) {
        int x = changeX + (col * columnWidth);

        // Load and render text for each column
        String key = "verifier_e_col" + (col + 1) + "_line1";
        String text = loc.getCommon().get(key);

        BitmapFont font = VerifierTextLayout.selectFont(text, columnWidth - 10, AssetLoader.font15);
        mainPanel.drawString(text, x, changeY + 135, Constants.COLOR_BLACK,
            font, DrawString.MIDDLE, false, false);
    }
}
```

## Best Practices

1. **Always use VerifierTextLayout.selectFont()** for text that might vary in length across languages
2. **Use placeholders `{0}`, `{1}`** for dynamic values (like numbers)
3. **Test with both English and German** to ensure layouts work correctly
4. **Use `|` separator** for multi-line titles
5. **Keep column widths flexible** - calculate based on total width divided by column count
6. **Consistent naming:** Follow the pattern `verifier_[id]_[type]_[detail]`

## Migration Checklist for Existing Verifiers

- [ ] Add all text to localization files (EN + DE)
- [ ] Replace hardcoded strings in `renderTextBig()`
- [ ] Replace hardcoded strings in `renderText()`
- [ ] Use `VerifierTextLayout.selectFont()` for adaptive font sizing
- [ ] Test with both languages
- [ ] Update `getName()` to use localization if needed

## Advantages of This System

✅ **Language-Independent Rendering**: Layouts automatically adapt to text length
✅ **No Code Changes for Translations**: Add new languages by just adding `.properties` files
✅ **Automatic Font Scaling**: Prevents text overflow with `selectFont()`
✅ **Minimal Code Changes**: Existing verifier structure remains mostly intact
✅ **Easy to Extend**: Clear patterns for adding new verifiers
✅ **Placeholder Support**: Dynamic values can be injected into translations

## Future Improvements

- Add support for RTL (Right-to-Left) languages
- Create a VerifierLayoutBuilder for more complex layouts
- Add visual debugging mode to show column boundaries
- Support for custom fonts per language
