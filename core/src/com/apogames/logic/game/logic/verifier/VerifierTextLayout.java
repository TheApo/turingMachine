package com.apogames.logic.game.logic.verifier;

import com.apogames.logic.asset.AssetLoader;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.game.MainPanel;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Defines flexible text layout for verifier rendering.
 * Handles multi-language text positioning with automatic line wrapping.
 */
public class VerifierTextLayout {

    private static final GlyphLayout glyphLayout = new GlyphLayout();

    // Storage for icon positions calculated during text rendering
    public static class IconPosition {
        public int x, y, icon;
        public IconPosition(int x, int y, int icon) { this.x = x; this.y = y; this.icon = icon; }
    }

    public static List<IconPosition> pendingIcons = new ArrayList<>();

    /**
     * Calculates icon positions without rendering. MUST be called before renderFill().
     */
    public static void calculateIconPositions(String text, int startX, int startY,
                                              int maxWidth, int lineHeight, BitmapFont font,
                                              int[] icons, int iconWidth) {
        pendingIcons.clear();
        if (text == null || text.isEmpty() || icons == null) return;

        String[] parts = text.split("\\s+");
        int currentX = startX;
        int currentY = startY;
        int iconIndex = 0;

        for (String part : parts) {
            if (part.isEmpty()) continue;

            if (part.contains("{ICON}")) {
                while (part.contains("{ICON}")) {
                    int iconPos = part.indexOf("{ICON}");
                    String beforeIcon = part.substring(0, iconPos);

                    if (!beforeIcon.isEmpty()) {
                        glyphLayout.setText(font, beforeIcon);
                        float wordWidth = glyphLayout.width;
                        if (currentX > startX && currentX + wordWidth > startX + maxWidth) {
                            currentX = startX;
                            currentY += lineHeight;
                        }
                        currentX += wordWidth;
                    }

                    if (iconIndex < icons.length) {
                        pendingIcons.add(new IconPosition((int)currentX, currentY - 5, icons[iconIndex]));
                        currentX += iconWidth;
                        iconIndex++;
                    }

                    part = part.substring(iconPos + 6);
                }

                if (!part.isEmpty()) {
                    glyphLayout.setText(font, part + " ");
                    float wordWidth = glyphLayout.width;
                    if (currentX > startX && currentX + wordWidth > startX + maxWidth) {
                        currentX = startX;
                        currentY += lineHeight;
                    }
                    currentX += wordWidth;
                }
            } else {
                glyphLayout.setText(font, part + " ");
                float wordWidth = glyphLayout.width;
                if (currentX > startX && currentX + wordWidth > startX + maxWidth) {
                    currentX = startX;
                    currentY += lineHeight;
                }
                currentX += wordWidth;
            }
        }
    }

    /**
     * Renders text with automatic word wrapping and replaces {ICON} placeholders with spaces.
     */
    public static int renderWrappedTextWithIcons(MainPanel mainPanel, String text, int startX, int startY,
                                         int maxWidth, int lineHeight, BitmapFont font, float[] color,
                                         int[] icons, int iconWidth) {
        if (text == null || text.isEmpty()) return 0;

        String[] parts = text.split("\\s+");
        int currentX = startX;
        int currentY = startY;
        int lineCount = 0;
        int iconIndex = 0;

        for (String part : parts) {
            if (part.isEmpty()) continue;

            if (part.contains("{ICON}")) {
                while (part.contains("{ICON}")) {
                    int iconPos = part.indexOf("{ICON}");
                    String beforeIcon = part.substring(0, iconPos);

                    if (!beforeIcon.isEmpty()) {
                        glyphLayout.setText(font, beforeIcon);
                        float wordWidth = glyphLayout.width;
                        if (currentX > startX && currentX + wordWidth > startX + maxWidth) {
                            currentX = startX;
                            currentY += lineHeight;
                            lineCount++;
                        }
                        mainPanel.drawString(beforeIcon, currentX, currentY, color, font, DrawString.BEGIN, false, false);
                        currentX += wordWidth;
                    }

                    if (icons != null && iconIndex < icons.length) {
                        currentX += iconWidth;
                        iconIndex++;
                    }

                    part = part.substring(iconPos + 6);
                }

                if (!part.isEmpty()) {
                    glyphLayout.setText(font, part + " ");
                    float wordWidth = glyphLayout.width;
                    if (currentX > startX && currentX + wordWidth > startX + maxWidth) {
                        currentX = startX;
                        currentY += lineHeight;
                        lineCount++;
                    }
                    mainPanel.drawString(part, currentX, currentY, color, font, DrawString.BEGIN, false, false);
                    currentX += wordWidth;
                }
            } else {
                glyphLayout.setText(font, part + " ");
                float wordWidth = glyphLayout.width;
                if (currentX > startX && currentX + wordWidth > startX + maxWidth) {
                    currentX = startX;
                    currentY += lineHeight;
                    lineCount++;
                }
                mainPanel.drawString(part, currentX, currentY, color, font, DrawString.BEGIN, false, false);
                currentX += wordWidth;
            }
        }

        return lineCount + 1;
    }

    /**
     * Simple text rendering without icon tracking.
     */
    public static int renderWrappedText(MainPanel mainPanel, String text, int startX, int startY,
                                         int maxWidth, int lineHeight, BitmapFont font, float[] color) {
        return renderWrappedTextWithIcons(mainPanel, text, startX, startY, maxWidth, lineHeight, font, color, null, 0);
    }

    /**
     * Calculates how many lines the text will need with word wrapping.
     */
    public static int calculateLines(String text, int maxWidth, BitmapFont font) {
        if (text == null || text.isEmpty()) return 0;

        String[] words = text.split("\\s+");
        float currentLineWidth = 0;
        int lineCount = 1;

        for (String word : words) {
            if (word.isEmpty()) continue;

            glyphLayout.setText(font, word + " ");
            float wordWidth = glyphLayout.width;

            if (currentLineWidth > 0 && currentLineWidth + wordWidth > maxWidth) {
                lineCount++;
                currentLineWidth = wordWidth;
            } else {
                currentLineWidth += wordWidth;
            }
        }

        return lineCount;
    }

    /**
     * Renders multiple text segments in a single line, separated by a space or custom separator.
     * Useful for rendering text with icons/symbols in between.
     */
    public static void renderInlineSegments(MainPanel mainPanel, String[] segments, int startX, int startY,
                                            BitmapFont font, float[] color, int spacing) {
        int currentX = startX;

        for (String segment : segments) {
            if (segment == null || segment.trim().isEmpty()) continue;

            segment = segment.trim();
            glyphLayout.setText(font, segment);

            mainPanel.drawString(segment, currentX, startY, color, font, DrawString.BEGIN, false, false);
            currentX += glyphLayout.width + spacing;
        }
    }

    /**
     * Auto-selects font based on text length to fit in given width.
     */
    public static BitmapFont selectFont(String text, float maxWidth, BitmapFont defaultFont) {
        glyphLayout.setText(defaultFont, text);

        if (glyphLayout.width <= maxWidth) {
            return defaultFont;
        }

        // Try smaller fonts
        if (defaultFont == AssetLoader.font20) {
            return selectFont(text, maxWidth, AssetLoader.font15);
        } else if (defaultFont == AssetLoader.font15) {
            return selectFont(text, maxWidth, AssetLoader.font12);
        }

        return AssetLoader.font12;
    }

    /**
     * Calculates the width of a text segment.
     */
    public static float getTextWidth(String text, BitmapFont font) {
        glyphLayout.setText(font, text);
        return glyphLayout.width;
    }

    /**
     * Parses semicolon-separated text parts.
     */
    public static String[] parseTextParts(String text) {
        if (text == null || text.isEmpty()) {
            return new String[0];
        }
        return text.split(";");
    }
}
