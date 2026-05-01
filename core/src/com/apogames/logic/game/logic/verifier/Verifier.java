package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.common.Localization;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.level.CheckHelp;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Solution;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import java.util.ArrayList;

public abstract class Verifier {

    private final String REPLACEMENT_STRING = "    ";

    /** Lokalisiertes " oder " als Trenner zwischen mehreren Hit-Bedingungen. */
    public static String hitSeparator() {
        return " " + Localization.getInstance().getCommon().get("common_or") + " ";
    }

    private final float extraInformationWidth = (Constants.GAME_WIDTH - 805) / 2f - 15;

    private String verifier = "A";
    private final boolean isFirst;
    private final boolean isSecond;
    private final boolean isThird;

    private int column = 2;

    private final Solution solution;

    private String check = "";

    public Verifier(Solution solution, boolean isFirst, boolean isSecond, boolean isThird) {
        this.solution = solution;

        this.isFirst = isFirst;
        this.isSecond = isSecond;
        this.isThird = isThird;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public String getTranslation() {
        String number = "first";
        if (this.isSecond) {
            number = "second";
        } else if (this.isThird()) {
            number = "third";
        }
        return number;
    }

    public String getTranslationWithValue(int value) {
        Localization loc = Localization.getInstance();
        String number = loc.getCommon().get("common_number_one");
        if (value == 2) {
            number = loc.getCommon().get("common_number_two");
        } else if (value == 3) {
            number = loc.getCommon().get("common_number_three");
        } else if (value == 0) {
            number = loc.getCommon().get("common_number_zero");
        }
        return number;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public int getValue() {
        return 0;
    }

    public String getVerifier() {
        return verifier;
    }

    public void setVerifier(String verifier) {
        this.verifier = verifier;
    }

    public Solution getSolution() {
        return solution;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public boolean isSecond() {
        return isSecond;
    }

    public boolean isThird() {
        return isThird;
    }

    public abstract boolean check(int first, int second, int third);

    public abstract boolean check(int first, int second, int third, ArrayList<Solution> possibleSolutions, int index);

    /**
     * Number of answer rows on the verifier card. Default 1 (single-row).
     */
    public int getRows() {
        return 1;
    }

    /**
     * Y-offset (relative to the verifier card top) for each answer row. Length must equal getRows().
     */
    public int[] getRowYOffsets() {
        return DEFAULT_ROW_OFFSETS;
    }

    /**
     * Pixel height of one answer cell.
     */
    public int getCellHeight() {
        return DEFAULT_CELL_HEIGHT;
    }

    /**
     * Returns the cell indices (row * getColumn() + col) that the given guess satisfies.
     * A returned cell is "hit": if check() returned true, the rule lies among the hit cells;
     * if check() returned false, the rule is none of the hit cells.
     */
    public abstract int[] getCellsForGuess(int first, int second, int third);

    private static final int[] DEFAULT_ROW_OFFSETS = {125};
    private static final int DEFAULT_CELL_HEIGHT = 70;

    /**
     * 0 if a < b, 1 if a == b, 2 if a > b. Reused by many verifiers.
     */
    protected static int cmpCol(int a, int b) {
        return a < b ? 0 : a == b ? 1 : 2;
    }

    /**
     * Builds a hit-cell array from per-cell booleans. cellHits[i] = true marks cell index i as hit.
     */
    protected static int[] hitsOf(boolean... cellHits) {
        int count = 0;
        for (boolean b : cellHits) if (b) count++;
        int[] out = new int[count];
        int idx = 0;
        for (int i = 0; i < cellHits.length; i++) {
            if (cellHits[i]) out[idx++] = i;
        }
        return out;
    }

    public abstract String getText();

    public abstract String getName();

    public void renderFill(MainPanel mainPanel, int changeX, int changeY, boolean all) {

    }

    public void renderFill(MainPanel mainPanel, int changeX, int changeY) {
        mainPanel.getRenderer().setColor(Constants.COLOR_VERIFIER_BACKGROUND_LIGHTER[0], Constants.COLOR_VERIFIER_BACKGROUND_LIGHTER[1], Constants.COLOR_VERIFIER_BACKGROUND_LIGHTER[2], 1f);
        mainPanel.getRenderer().roundedRect(changeX, changeY + 100, 395, 95, 10);
        mainPanel.getRenderer().setColor(Constants.COLOR_VERIFIER_BACKGROUND[0], Constants.COLOR_VERIFIER_BACKGROUND[1], Constants.COLOR_VERIFIER_BACKGROUND[2], 1f);
        mainPanel.getRenderer().roundedRect(changeX, changeY, 395, 120, 10);

        mainPanel.getRenderer().setColor(Constants.COLOR_VERIFIER_BACKGROUND_LIGHTER[0], Constants.COLOR_VERIFIER_BACKGROUND_LIGHTER[1], Constants.COLOR_VERIFIER_BACKGROUND_LIGHTER[2], 1f);
        for (int i = 0; i < this.getDifficulty().getValue(); i++) {
            mainPanel.getRenderer().circle(changeX + 35 + i * 10, changeY + 10, 4);
        }
    }

    public void renderFillSeparator(MainPanel mainPanel, int changeX, int changeY, int count) {
        mainPanel.getRenderer().setColor(Constants.COLOR_VERIFIER_BACKGROUND[0], Constants.COLOR_VERIFIER_BACKGROUND[1], Constants.COLOR_VERIFIER_BACKGROUND[2], Constants.COLOR_VERIFIER_BACKGROUND[3]);
        if (this.getColumn() != count + 1) {
            this.setColumn(count + 1);
        }
        if (count == 1 || count == 3) {
            mainPanel.getRenderer().rect(changeX + 200, changeY + 125, 2, 70);
        } else if (count == 2) {
            mainPanel.getRenderer().rect(changeX + 133, changeY + 125, 2, 70);
            mainPanel.getRenderer().rect(changeX + 266, changeY + 125, 2, 70);
        }
        if (count == 3) {
            mainPanel.getRenderer().rect(changeX + 100, changeY + 125, 2, 70);
            mainPanel.getRenderer().rect(changeX + 300, changeY + 125, 2, 70);
        }
    }

    public void renderFillSeparatorHorizontal(MainPanel mainPanel, int changeX, int changeY, int count) {
        mainPanel.getRenderer().setColor(Constants.COLOR_VERIFIER_BACKGROUND[0], Constants.COLOR_VERIFIER_BACKGROUND[1], Constants.COLOR_VERIFIER_BACKGROUND[2], Constants.COLOR_VERIFIER_BACKGROUND[3]);
        if (count == 1) {
            mainPanel.getRenderer().rect(changeX + 10, changeY + 160, 400 - 20, 2);
        } else if (count == 2) {
            mainPanel.getRenderer().rect(changeX + 10, changeY + 160, 200 - 20, 2);
            mainPanel.getRenderer().rect(changeX + 10 + 200, changeY + 160, 200 - 20, 2);
        } else if (count == 3) {
            mainPanel.getRenderer().rect(changeX + 10, changeY + 160, 133 - 20, 2);
            mainPanel.getRenderer().rect(changeX + 10 + 133, changeY + 160, 133 - 20, 2);
            mainPanel.getRenderer().rect(changeX + 10 + 133 * 2, changeY + 160, 133 - 20, 2);
        } else if (count == 4) {
            mainPanel.getRenderer().rect(changeX + 10, changeY + 160, 100 - 20, 2);
            mainPanel.getRenderer().rect(changeX + 10 + 100, changeY + 160, 100 - 20, 2);
            mainPanel.getRenderer().rect(changeX + 10 + 100 * 2, changeY + 160, 100 - 20, 2);
            mainPanel.getRenderer().rect(changeX + 10 + 100 * 3, changeY + 160, 100 - 20, 2);
        }
    }

    public abstract Verifier getCopy();

    public abstract Verifier getCopyWithSolution(Solution newSolution);

    /**
     * Liefert alle möglichen Konfigurationen dieses Verifiers für eine hypothetische Lösung.
     * Bei Verifeyern mit versteckter Konfig (die der Spieler aus den Antworten ableiten muss)
     * sollte diese Methode überschrieben werden, damit der Solver alle möglichen Konfigs prüft.
     * Default: nur die echte Konfiguration mit der gegebenen Solution.
     */
    public java.util.List<Verifier> getAllConfigurations(Solution sol) {
        java.util.List<Verifier> list = new java.util.ArrayList<>();
        list.add(getCopyWithSolution(sol));
        return list;
    }

    /**
     * Liefert die Layout-Cells, die diese Konfiguration im Verifier-Grid repräsentiert.
     * Wird vom Solver benutzt, um ausgekreuzte (vom Spieler eliminierte) Konfigs zu verwerfen.
     * Default: leer — Verifier ohne versteckte Konfig.
     */
    public int[] getConfigCells() {
        return new int[0];
    }

    public int renderFillExtraInformation(MainPanel mainPanel, int changeX, int changeY, String check) {
        return renderFillExtraInformation(mainPanel, changeX, changeY, check, Integer.MAX_VALUE);
    }

    public int renderFillExtraInformation(MainPanel mainPanel, int changeX, int changeY, String check, int maxLines) {
        if (check == null || check.isEmpty()) {
            return 0;
        }
        BitmapFont font = AssetLoader.font15;
        int iconSize = 17;
        java.util.List<String> lines = computeWrappedLines(mainPanel, check, font);
        int linesToDraw = Math.min(lines.size(), Math.max(1, maxLines));
        for (int i = 0; i < linesToDraw; i++) {
            drawIconsForLine(mainPanel, font, iconSize, changeX, changeY + i * 20, lines.get(i));
        }
        return linesToDraw;
    }

    private void drawIconsForLine(MainPanel mainPanel, BitmapFont font, int iconSize, int changeX, int changeY, String line) {
        if (!line.contains("first") && !line.contains("second") && !line.contains("third")) {
            return;
        }
        String working = line;
        int indexFirst = working.indexOf("first");
        int indexSecond = working.indexOf("second");
        int indexThird = working.indexOf("third");
        while (indexFirst >= 0 || indexSecond >= 0 || indexThird >= 0) {
            int chosenIcon;
            int chosenIdx;
            String token;
            if (indexFirst >= 0 && (indexSecond < 0 || indexFirst < indexSecond) && (indexThird < 0 || indexFirst < indexThird)) {
                chosenIcon = 1;
                chosenIdx = indexFirst;
                token = "first";
            } else if (indexSecond >= 0 && (indexThird < 0 || indexSecond < indexThird)) {
                chosenIcon = 2;
                chosenIdx = indexSecond;
                token = "second";
            } else {
                chosenIcon = 3;
                chosenIdx = indexThird;
                token = "third";
            }
            mainPanel.getGlyphLayout().setText(font, working.substring(0, chosenIdx));
            int startX = (int) mainPanel.getGlyphLayout().width;
            working = working.substring(0, chosenIdx) + REPLACEMENT_STRING + working.substring(chosenIdx + token.length());
            IconDraw.renderIcon(mainPanel, changeX + startX, changeY, iconSize, iconSize, chosenIcon);
            indexFirst = working.indexOf("first");
            indexSecond = working.indexOf("second");
            indexThird = working.indexOf("third");
        }
    }

    public void renderAllText(MainPanel mainPanel, int changeX, int changeY) {
        mainPanel.drawString(""+this.verifier, changeX + 5, changeY + 5, Constants.COLOR_WHITE, AssetLoader.font20, DrawString.BEGIN, false, false);
        mainPanel.drawString(Localization.getInstance().getCommon().get("verifier_verifies"), changeX + 100, changeY + 10, Constants.COLOR_ORANGE, AssetLoader.font20, DrawString.BEGIN, false, false);

        this.renderText(mainPanel, changeX, changeY);
    }

    public void renderText(MainPanel mainPanel, int changeX, int changeY) {
        this.renderTextBig(mainPanel, changeX, changeY);
    }

    public void renderTextBig(MainPanel mainPanel, int changeX, int changeY) {
    }

    public int renderTextExtraInformation(MainPanel mainPanel, int changeX, int changeY, String check) {
        return renderTextExtraInformation(mainPanel, changeX, changeY, check, Integer.MAX_VALUE);
    }

    public int renderTextExtraInformation(MainPanel mainPanel, int changeX, int changeY, String check, int maxLines) {
        if (check == null || check.isEmpty()) {
            return 0;
        }
        BitmapFont font = AssetLoader.font15;
        java.util.List<String> lines = computeWrappedLines(mainPanel, check, font);
        int linesToDraw = Math.min(lines.size(), Math.max(1, maxLines));
        for (int i = 0; i < linesToDraw; i++) {
            String replaced = lines.get(i)
                    .replace("first", REPLACEMENT_STRING)
                    .replace("second", REPLACEMENT_STRING)
                    .replace("third", REPLACEMENT_STRING);
            mainPanel.drawString(replaced, changeX, changeY + i * 20, Constants.COLOR_BUTTONS_DARK, font, DrawString.BEGIN, false, false);
        }
        return linesToDraw;
    }

    public int countWrappedLines(MainPanel mainPanel, String check) {
        if (check == null || check.isEmpty()) {
            return 0;
        }
        return computeWrappedLines(mainPanel, check, AssetLoader.font15).size();
    }

    private java.util.List<String> computeWrappedLines(MainPanel mainPanel, String check, BitmapFont font) {
        java.util.List<String> lines = new java.util.ArrayList<>();
        if (renderedWidth(mainPanel, font, check) <= extraInformationWidth) {
            lines.add(check);
            return lines;
        }
        String[] words = check.split(" ");
        StringBuilder cur = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) continue;
            String candidate = cur.length() == 0 ? word : cur + " " + word;
            if (renderedWidth(mainPanel, font, candidate) <= extraInformationWidth || cur.length() == 0) {
                cur.setLength(0);
                cur.append(candidate);
            } else {
                lines.add(cur.toString());
                cur.setLength(0);
                cur.append(word);
            }
        }
        if (cur.length() > 0) {
            lines.add(cur.toString());
        }
        return lines;
    }

    private float textWidth(MainPanel mainPanel, BitmapFont font, String text) {
        mainPanel.getGlyphLayout().setText(font, text);
        return mainPanel.getGlyphLayout().width;
    }

    /**
     * Breite des Strings nach dem Token-Replacement ("first"/"second"/"third" → REPLACEMENT_STRING),
     * also die tatsächliche Render-Breite. Roh-Breite wäre zu groß weil "first" (5 Zeichen) deutlich
     * breiter ist als die 4 Replacement-Spaces, hinter denen das Icon liegt.
     */
    private float renderedWidth(MainPanel mainPanel, BitmapFont font, String text) {
        String rendered = text
                .replace("first", REPLACEMENT_STRING)
                .replace("second", REPLACEMENT_STRING)
                .replace("third", REPLACEMENT_STRING);
        return textWidth(mainPanel, font, rendered);
    }

    public abstract int getId();

    public Difficulty getDifficulty() {
        return Difficulty.EASY;
    }

    public String[] getString(String value) {
        String result = value;
        result = result.replace("this.value", String.valueOf(this.getValue()));
        return result.split(";");
    }
}
