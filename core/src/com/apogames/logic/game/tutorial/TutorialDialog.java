package com.apogames.logic.game.tutorial;

import com.apogames.logic.Constants;
import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.common.Localization;
import com.apogames.logic.game.MainPanel;

import java.util.ArrayList;
import java.util.List;

public class TutorialDialog {

    private static final int LINE_HEIGHT = 24;
    private static final int TITLE_HEIGHT = 40;
    private static final int PADDING = 25;
    private static final int FOOTER_HEIGHT = 70;
    private static final int ARROW_THICKNESS = 5;
    private static final int ARROW_HEAD_SIZE = 16;
    private static final int HIGHLIGHT_THICKNESS = 10;


    private int x, y, width, height;
    private int descriptionTop;
    private int extraDescriptionOffset;

    private boolean hasHighlight;
    private int highlightX, highlightY, highlightW, highlightH;

    private final List<TutorialAnnotation> annotations = new ArrayList<>();

    public void setBox(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.descriptionTop = y + PADDING + TITLE_HEIGHT + extraDescriptionOffset;
    }

    public void setExtraDescriptionOffset(int offset) {
        this.extraDescriptionOffset = offset;
        this.descriptionTop = y + PADDING + TITLE_HEIGHT + offset;
    }

    public void setHighlight(int x, int y, int w, int h) {
        this.hasHighlight = true;
        this.highlightX = x;
        this.highlightY = y;
        this.highlightW = w;
        this.highlightH = h;
    }

    public void clearHighlight() {
        this.hasHighlight = false;
    }

    public void addAnnotation(TutorialAnnotation annotation) {
        this.annotations.add(annotation);
    }

    public void clearAnnotations() {
        this.annotations.clear();
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public int getNextButtonCenterX() {
        return x + width / 2;
    }

    public int getNextButtonY() {
        return y + height - FOOTER_HEIGHT + 10;
    }

    public int getDescriptionTop() {
        return descriptionTop;
    }

    public void renderBoxFill(MainPanel mainPanel) {
        mainPanel.getRenderer().setColor(Constants.COLOR_BUTTONS[0], Constants.COLOR_BUTTONS[1], Constants.COLOR_BUTTONS[2], 1f);
        mainPanel.getRenderer().roundedRect(x, y, width, height, 18);
    }

    public void renderArrowsFill(MainPanel mainPanel) {
        mainPanel.getRenderer().setColor(Constants.COLOR_BUTTONS_DARK[0], Constants.COLOR_BUTTONS_DARK[1], Constants.COLOR_BUTTONS_DARK[2], 1f);
        for (TutorialAnnotation a : annotations) {
            drawArrow(mainPanel, a.fromX, a.fromY, a.toX, a.toY);
        }
    }

    public void renderBoxLine(MainPanel mainPanel) {
        mainPanel.getRenderer().setColor(Constants.COLOR_BUTTONS_DARK[0], Constants.COLOR_BUTTONS_DARK[1], Constants.COLOR_BUTTONS_DARK[2], 1f);
        mainPanel.getRenderer().roundedRectLine(x, y, width, height, 18);
    }

    public void renderHighlightFill(MainPanel mainPanel) {
        if (!hasHighlight) return;
        mainPanel.getRenderer().setColor(Constants.COLOR_BUTTONS_DARK[0], Constants.COLOR_BUTTONS_DARK[1], Constants.COLOR_BUTTONS_DARK[2], 1f);
        int t = HIGHLIGHT_THICKNESS;
        int half = t / 2;
        int x1 = highlightX;
        int y1 = highlightY;
        int x2 = highlightX + highlightW;
        int y2 = highlightY + highlightH;
        mainPanel.getRenderer().rectLine(x1 - half, y1, x2 + half, y1, t);
        mainPanel.getRenderer().rectLine(x1 - half, y2, x2 + half, y2, t);
        mainPanel.getRenderer().rectLine(x1, y1, x1, y2, t);
        mainPanel.getRenderer().rectLine(x2, y1, x2, y2, t);
    }

    public void renderTitle(MainPanel mainPanel, String titleKey) {
        Localization loc = Localization.getInstance();
        String title = loc.getCommon().get(titleKey);
        if (title != null && title.length() > 0) {
            mainPanel.drawString(title, x + width / 2f, y + PADDING, Constants.COLOR_BUTTONS_DARK,
                    AssetLoader.font30, DrawString.MIDDLE, false, false);
        }
    }

    public void renderTitleAndDescription(MainPanel mainPanel, String titleKey, String descriptionText) {
        renderTitle(mainPanel, titleKey);
        if (descriptionText != null && descriptionText.length() > 0) {
            String[] lines = descriptionText.split(";", -1);
            int textY = descriptionTop;
            int textWidth = width - 2 * PADDING;
            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    textY += LINE_HEIGHT;
                    continue;
                }
                String[] wrapped = wrap(mainPanel, line, textWidth);
                for (String wrappedLine : wrapped) {
                    mainPanel.drawString(wrappedLine, x + width / 2f, textY,
                            Constants.COLOR_BUTTONS_DARK, AssetLoader.font20, DrawString.MIDDLE, false, false);
                    textY += LINE_HEIGHT;
                }
            }
        }
    }

    public void renderLabels(MainPanel mainPanel) {
        Localization loc = Localization.getInstance();
        for (TutorialAnnotation a : annotations) {
            if (a.label == null || a.label.length() == 0) continue;
            String resolved = loc.getCommon().get(a.label);
            if (resolved == null) resolved = a.label;
            String[] labelLines = resolved.split(";");
            int ly = a.labelY;
            for (String labelLine : labelLines) {
                mainPanel.drawString(labelLine, a.labelX, ly, Constants.COLOR_BUTTONS_DARK,
                        AssetLoader.font20, a.labelAlign, false, false);
                ly += LINE_HEIGHT;
            }
        }
    }

    private String[] wrap(MainPanel mainPanel, String text, int maxWidth) {
        List<String> result = new ArrayList<>();
        String[] words = text.split(" ");
        StringBuilder current = new StringBuilder();
        for (String word : words) {
            String tentative = current.length() == 0 ? word : current + " " + word;
            mainPanel.getGlyphLayout().setText(AssetLoader.font20, tentative);
            if (mainPanel.getGlyphLayout().width <= maxWidth || current.length() == 0) {
                if (current.length() > 0) current.append(' ');
                current.append(word);
            } else {
                result.add(current.toString());
                current = new StringBuilder(word);
            }
        }
        if (current.length() > 0) result.add(current.toString());
        return result.toArray(new String[0]);
    }

    private void drawArrow(MainPanel mainPanel, int x1, int y1, int x2, int y2) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len <= 0) return;
        float ux = dx / len;
        float uy = dy / len;
        float headBaseX = x2 - ux * ARROW_HEAD_SIZE;
        float headBaseY = y2 - uy * ARROW_HEAD_SIZE;
        drawThickLine(mainPanel, x1, y1, headBaseX, headBaseY, ARROW_THICKNESS);
        float perpX = -uy * ARROW_HEAD_SIZE * 0.6f;
        float perpY = ux * ARROW_HEAD_SIZE * 0.6f;
        mainPanel.getRenderer().triangle(x2, y2,
                headBaseX + perpX, headBaseY + perpY,
                headBaseX - perpX, headBaseY - perpY);
    }

    private void drawThickLine(MainPanel mainPanel, float x1, float y1, float x2, float y2, float thickness) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        float len = (float) Math.sqrt(dx * dx + dy * dy);
        if (len <= 0) return;
        float nx = -dy / len;
        float ny = dx / len;
        float h = thickness / 2f;
        float vx0 = x1 + nx * h, vy0 = y1 + ny * h;
        float vx1 = x1 - nx * h, vy1 = y1 - ny * h;
        float vx2 = x2 - nx * h, vy2 = y2 - ny * h;
        float vx3 = x2 + nx * h, vy3 = y2 + ny * h;
        mainPanel.getRenderer().triangle(vx0, vy0, vx1, vy1, vx2, vy2);
        mainPanel.getRenderer().triangle(vx0, vy0, vx2, vy2, vx3, vy3);
    }
}
