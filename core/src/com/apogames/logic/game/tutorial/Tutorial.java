package com.apogames.logic.game.tutorial;

import com.apogames.logic.Constants;
import com.apogames.logic.backend.DrawString;
import com.apogames.logic.common.Localization;
import com.apogames.logic.entity.ApoButton;
import com.apogames.logic.entity.ApoButtonCheckbox;
import com.apogames.logic.entity.ApoButtonTuringNumber;
import com.apogames.logic.game.MainPanel;
import com.apogames.logic.game.logic.IconDraw;
import com.apogames.logic.game.logic.Logic;
import com.apogames.logic.game.logic.Step;
import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Level;
import com.apogames.logic.game.logic.level.Solution;
import com.apogames.logic.game.logic.verifier.CompareToValueVerifier;
import com.apogames.logic.game.logic.verifier.IsBiggestVerifier;
import com.apogames.logic.game.logic.verifier.OddEvenValueVerifier;
import com.apogames.logic.game.logic.verifier.SpecificCompareToValueVerifier;
import com.apogames.logic.game.logic.verifier.Verifier;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Tutorial extends Logic {

    public static final String FUNCTION_TUTORIAL_NEXT = "TUTORIAL_NEXT";

    private static final int TUTORIAL_AMOUNT = 4;

    private static final int VERIFIER_CARD_WIDTH = 395;
    private static final int VERIFIER_CARD_HEIGHT = 195;
    private static final int VERIFIER_GRID_START_X = 5;
    private static final int VERIFIER_GRID_START_Y = 170;
    private static final int VERIFIER_COL_PITCH = 400;
    private static final int VERIFIER_ROW_PITCH = 200;
    private static final int VERIFIER_LOGIC_WIDTH = 400;

    private static final int VERIFY_BUTTON_DX = 10;
    private static final int VERIFY_BUTTON_DY = 35;

    private static final int CODE_BOX_SIZE = 60;
    private static final int CODE_BOX_SPACING = 6;
    private static final int CODE_BOX_Y_OFFSET = 15;
    private static final int CODE_BOX_DESC_OFFSET = 35;

    private TutorialStep tutorialStep = TutorialStep.INTRO_CODE;
    private final TutorialDialog dialog = new TutorialDialog();
    private final ApoButtonTuringNumber[] codeBoxes = new ApoButtonTuringNumber[3];

    private int dialogVerifierX;
    private int dialogVerifierY;

    public Tutorial(MainPanel game) {
        super(game);
        float[][] colors = { Constants.COLOR_NUMBER_ONE, Constants.COLOR_NUMBER_TWO, Constants.COLOR_NUMBER_THREE };
        for (int i = 0; i < 3; i++) {
            codeBoxes[i] = new ApoButtonTuringNumber(0, 0, CODE_BOX_SIZE, CODE_BOX_SIZE,
                    "_TUTORIAL_CODE_" + i, "1", colors[i], Constants.COLOR_WHITE, i + 1);
            codeBoxes[i].setFont(com.apogames.logic.asset.AssetLoader.font30);
            codeBoxes[i].setVisible(true);
        }
    }

    @Override
    public void setNeededButtonsVisible() {
        super.setNeededButtonsVisible();
        ApoButton next = getMainPanel().getButtonByFunction(FUNCTION_TUTORIAL_NEXT);
        if (next != null) {
            next.setVisible(true);
        }
    }

    @Override
    public void init() {
        setAmount(TUTORIAL_AMOUNT);
        setDifficulty(Difficulty.EXPERT);
        super.init();
        ApoButtonCheckbox helpBtn = (ApoButtonCheckbox) getMainPanel().getButtonByFunction(Logic.FUNCTION_HELP);
        helpBtn.setChecked(true);
        this.tutorialStep = TutorialStep.INTRO_CODE;
        configureForCurrentStep();
    }

    @Override
    protected Level createLevel() {
        Solution solution = new Solution(4, 1, 5);
        ArrayList<Verifier> verifiers = new ArrayList<>();

        Verifier a = new CompareToValueVerifier(solution, true, false, false, 3);
        a.setVerifier("A");
        verifiers.add(a);

        Verifier b = new OddEvenValueVerifier(solution, false, false, true);
        b.setVerifier("B");
        verifiers.add(b);

        Verifier c = new IsBiggestVerifier(solution);
        c.setVerifier("C");
        verifiers.add(c);

        Verifier d = new SpecificCompareToValueVerifier(solution, false, true, false, 2);
        d.setVerifier("D");
        verifiers.add(d);

        return new Level(TUTORIAL_AMOUNT, solution, verifiers);
    }

    @Override
    public void mouseButtonFunction(String function) {
        if (FUNCTION_TUTORIAL_NEXT.equals(function)) {
            advanceStep();
            return;
        }
        if (this.tutorialStep == TutorialStep.DONE
                && Logic.FUNCTION_SOLVED_NEXTROUND.equals(function)) {
            getMainPanel().changeToGame(TUTORIAL_AMOUNT, Difficulty.EASY);
            return;
        }
        if (!isAllowedInCurrentStep(function)) {
            return;
        }
        super.mouseButtonFunction(function);
        if (this.tutorialStep == TutorialStep.SIMPLE_HINT || this.tutorialStep == TutorialStep.HARD_HINT) {
            int idx = verifierIndexInDialog();
            rebuildHintAnnotations(idx);
            updateNextButtonVisibility();
        }
        if (this.tutorialStep == TutorialStep.ROUND_FLOW
                && (Logic.FUNCTION_GUESS_SOLUTION.equals(function)
                        || Logic.FUNCTION_STEP_TO_NEXT.equals(function))) {
            dismissTutorialOverlay();
        }
    }

    private void dismissTutorialOverlay() {
        this.tutorialStep = TutorialStep.DONE;
        ApoButton next = getMainPanel().getButtonByFunction(FUNCTION_TUTORIAL_NEXT);
        if (next != null) next.setVisible(false);
    }

    private boolean isAllowedInCurrentStep(String function) {
        if (Logic.FUNCTION_LOGIC_BACK.equals(function)) {
            return true;
        }
        if (this.tutorialStep == TutorialStep.INTRO_CODE) {
            return Logic.FUNCTION_GUESS_FIRST.equals(function)
                    || Logic.FUNCTION_GUESS_SECOND.equals(function)
                    || Logic.FUNCTION_GUESS_THIRD.equals(function);
        }
        if (this.tutorialStep == TutorialStep.RIGHT_UI) {
            return !Logic.FUNCTION_STEP_TO_NEXT.equals(function);
        }
        return true;
    }

    private void advanceStep() {
        TutorialStep previous = this.tutorialStep;
        this.tutorialStep = this.tutorialStep.next();
        if (this.tutorialStep == TutorialStep.DONE) {
            getMainPanel().changeToMenu();
            return;
        }
        leavingStepCleanup(previous);
        configureForCurrentStep();
    }

    private void leavingStepCleanup(TutorialStep leaving) {
        if (leaving == TutorialStep.SIMPLE_HINT) {
            restoreVerifierButtonsToOriginal(0);
        } else if (leaving == TutorialStep.HARD_HINT) {
            restoreVerifierButtonsToOriginal(3);
        }
    }

    private void configureForCurrentStep() {
        dialog.clearAnnotations();
        dialog.clearHighlight();
        dialog.setExtraDescriptionOffset(0);
        ApoButton next = getMainPanel().getButtonByFunction(FUNCTION_TUTORIAL_NEXT);
        if (next != null) next.setId("button_tutorial_next");
        switch (this.tutorialStep) {
            case INTRO_CODE:
                configureIntroCode();
                break;
            case SIMPLE_HINT:
                configureHintStep(0);
                break;
            case HARD_HINT:
                configureHintStep(3);
                break;
            case RIGHT_UI:
                configureRightUI();
                break;
            case ROUND_FLOW:
                configureRoundFlow();
                break;
            default:
                break;
        }
        positionNextButton();
    }

    private void configureIntroCode() {
        int codeX = 275;
        int codeY = 75;
        int codeW = 250;
        int codeH = 90;
        int codeCenterX = codeX + codeW / 2;
        dialog.setHighlight(codeX, codeY, codeW, codeH);

        int dialogWidth = 720;
        int dialogHeight = 260;
        int dialogX = codeCenterX - dialogWidth / 2;
        int dialogY = codeY + codeH + 45;
        dialog.setBox(dialogX, dialogY, dialogWidth, dialogHeight);

        int arrowFromX = codeCenterX;
        int arrowFromY = dialogY - 5;
        int arrowToX = codeCenterX;
        int arrowToY = codeY + codeH + 5;
        dialog.addAnnotation(new TutorialAnnotation(arrowFromX, arrowFromY, arrowToX, arrowToY,
                "", 0, 0, DrawString.BEGIN));
    }

    private void configureHintStep(int verifierIdx) {
        enterQuestionStepIfNeeded();
        hideAllVerifierVerifyButtons();

        int dialogWidth = 1160;
        int dialogHeight = 720;
        int dialogX = (Constants.GAME_WIDTH - dialogWidth) / 2;
        int dialogY = 30;
        dialog.setBox(dialogX, dialogY, dialogWidth, dialogHeight);

        this.dialogVerifierX = (Constants.GAME_WIDTH - VERIFIER_CARD_WIDTH) / 2;
        this.dialogVerifierY = dialogY + 280;

        positionVerifierButtons(verifierIdx, this.dialogVerifierX, this.dialogVerifierY);
        showVerifierVerifyButtonIfUnasked(verifierIdx);
        positionCodeBoxesInDialog();
        dialog.setExtraDescriptionOffset(CODE_BOX_DESC_OFFSET);

        rebuildHintAnnotations(verifierIdx);
    }

    private void rebuildHintAnnotations(int verifierIdx) {
        dialog.clearAnnotations();
        if (verifierIdx < 0) return;
        int vx = this.dialogVerifierX;
        int vy = this.dialogVerifierY;
        Boolean tested = getLevel().getRounds()[getLevel().getCurRound()].getVerifier()[verifierIdx];

        dialog.addAnnotation(new TutorialAnnotation(
                vx - 130, vy + 15, vx - 8, vy + 15,
                annotationKey("letter"), vx - 140, vy + 4, DrawString.END));

        if (tested == null) {
            dialog.addAnnotation(new TutorialAnnotation(
                    vx + 45, vy - 20, vx + 45, vy - 5,
                    annotationKey("difficulty"), vx + 45, vy - 90, DrawString.MIDDLE));
        }

        dialog.addAnnotation(new TutorialAnnotation(
                vx + VERIFIER_CARD_WIDTH + 130, vy + 45, vx + VERIFIER_CARD_WIDTH + 8, vy + 45,
                annotationKey("question"), vx + VERIFIER_CARD_WIDTH + 140, vy + 30, DrawString.BEGIN));

        dialog.addAnnotation(new TutorialAnnotation(
                vx + VERIFIER_CARD_WIDTH / 2, vy + VERIFIER_CARD_HEIGHT + 70,
                vx + VERIFIER_CARD_WIDTH / 2, vy + VERIFIER_CARD_HEIGHT + 8,
                annotationKey("answers"), vx + VERIFIER_CARD_WIDTH / 2,
                vy + VERIFIER_CARD_HEIGHT + 80, DrawString.MIDDLE));
    }

    private boolean isNextButtonVisibleForCurrentStep() {
        if (this.tutorialStep == TutorialStep.SIMPLE_HINT || this.tutorialStep == TutorialStep.HARD_HINT) {
            int idx = verifierIndexInDialog();
            if (idx < 0) return false;
            return getLevel().getRounds()[getLevel().getCurRound()].getVerifier()[idx] != null;
        }
        return true;
    }

    private void updateNextButtonVisibility() {
        ApoButton next = getMainPanel().getButtonByFunction(FUNCTION_TUTORIAL_NEXT);
        if (next == null) return;
        next.setVisible(isNextButtonVisibleForCurrentStep());
    }

    private String annotationKey(String suffix) {
        return "tutorial_label_" + suffix;
    }

    private void positionCodeBoxesInDialog() {
        int boxX = dialog.getX() + 25;
        int boxY = dialog.getY() + CODE_BOX_Y_OFFSET;
        for (int i = 0; i < 3; i++) {
            codeBoxes[i].setX(boxX + i * (CODE_BOX_SIZE + CODE_BOX_SPACING));
            codeBoxes[i].setY(boxY);
        }
    }

    private void renderCodeBoxesInDialog() {
        codeBoxes[0].setText(String.valueOf(getLevel().getGuess().getFirst()));
        codeBoxes[1].setText(String.valueOf(getLevel().getGuess().getSecond()));
        codeBoxes[2].setText(String.valueOf(getLevel().getGuess().getThird()));
        for (int i = 0; i < 3; i++) {
            codeBoxes[i].render(getMainPanel(), 0, 0);
        }
    }

    private void configureRightUI() {
        restoreAllVerifierButtonsAndVisibility();

        int dialogX = 20;
        int dialogWidth = 770;
        int dialogY = 200;
        int dialogHeight = 380;
        dialog.setBox(dialogX, dialogY, dialogWidth, dialogHeight);

        int rightX = 815;
        int rightY = 20;
        int rightW = 575;
        int rightH = 576;
        dialog.setHighlight(rightX, rightY, rightW, rightH);

        int arrowFromX = dialogX + dialogWidth + 4;
        int arrowFromY = dialogY + dialogHeight / 2;
        int arrowToX = rightX - 5;
        int arrowToY = arrowFromY;
        dialog.addAnnotation(new TutorialAnnotation(arrowFromX, arrowFromY, arrowToX, arrowToY,
                "", 0, 0, DrawString.BEGIN));
    }

    private void configureRoundFlow() {
        enterEndStep();

        ApoButton next = getMainPanel().getButtonByFunction(FUNCTION_TUTORIAL_NEXT);
        if (next != null) next.setId("button_tutorial_finish");

        int dialogWidth = 1100;
        int dialogHeight = 540;
        int dialogX = (Constants.GAME_WIDTH - dialogWidth) / 2;
        int dialogY = 220;
        dialog.setBox(dialogX, dialogY, dialogWidth, dialogHeight);

        int arrowFromY = dialogY - 5;
        dialog.addAnnotation(new TutorialAnnotation(
                200, arrowFromY, 85, 158, "", 0, 0, DrawString.BEGIN));
        dialog.addAnnotation(new TutorialAnnotation(
                400, arrowFromY, 400, 168, "", 0, 0, DrawString.BEGIN));
        dialog.addAnnotation(new TutorialAnnotation(
                720, arrowFromY, 720, 158, "", 0, 0, DrawString.BEGIN));
    }

    private void enterEndStep() {
        int safety = 4;
        while (getStep() != Step.END && safety-- > 0) {
            nextStep();
        }
    }

    private static final int STEP5_TABLE_NUM_BOX_SIZE = 42;
    private static final int STEP5_TABLE_ROW_HEIGHT = 52;
    private static final int STEP5_TABLE_LEFT_PADDING = 120;
    private static final int STEP5_TABLE_TOP_OFFSET = 150;
    private static final int STEP5_GOAL_TOP_OFFSET = 90;
    private static final int STEP5_TABLE_ROWS = 5;

    private void renderStep5FilledContent(MainPanel mp) {
        int dialogX = dialog.getX();
        int dialogY = dialog.getY();
        int boxX = dialogX + STEP5_TABLE_LEFT_PADDING;
        int tableY = dialogY + STEP5_TABLE_TOP_OFFSET;
        for (int i = 0; i < STEP5_TABLE_ROWS; i++) {
            int boxY = tableY + i * STEP5_TABLE_ROW_HEIGHT;
            mp.getRenderer().setColor(Constants.COLOR_BUTTONS_DARK[0], Constants.COLOR_BUTTONS_DARK[1],
                    Constants.COLOR_BUTTONS_DARK[2], 1f);
            mp.getRenderer().roundedRect(boxX, boxY, STEP5_TABLE_NUM_BOX_SIZE, STEP5_TABLE_NUM_BOX_SIZE, 8);
        }
    }

    private void renderStep5SpriteContent(MainPanel mp) {
        Localization loc = Localization.getInstance();
        int dialogX = dialog.getX();
        int dialogY = dialog.getY();
        int dialogW = dialog.getWidth();

        String goal = loc.getCommon().get("tutorial_step5_goal");
        if (goal != null) {
            mp.drawString(goal, dialogX + dialogW / 2f, dialogY + STEP5_GOAL_TOP_OFFSET,
                    Constants.COLOR_BUTTONS_DARK, com.apogames.logic.asset.AssetLoader.font30,
                    DrawString.MIDDLE, false, false);
        }

        int boxX = dialogX + STEP5_TABLE_LEFT_PADDING;
        int tableY = dialogY + STEP5_TABLE_TOP_OFFSET;
        int textX = boxX + STEP5_TABLE_NUM_BOX_SIZE + 22;
        int numberInBoxY = (STEP5_TABLE_NUM_BOX_SIZE - 25) / 2 + 2;
        for (int i = 0; i < STEP5_TABLE_ROWS; i++) {
            int boxY = tableY + i * STEP5_TABLE_ROW_HEIGHT;
            mp.drawString(String.valueOf(i + 1), boxX + STEP5_TABLE_NUM_BOX_SIZE / 2f, boxY + numberInBoxY,
                    Constants.COLOR_WHITE, com.apogames.logic.asset.AssetLoader.font25,
                    DrawString.MIDDLE, false, false);
            String desc = loc.getCommon().get("tutorial_step5_row" + (i + 1));
            if (desc != null) {
                mp.drawString(desc, textX, boxY + 12, Constants.COLOR_BUTTONS_DARK,
                        com.apogames.logic.asset.AssetLoader.font20, DrawString.BEGIN, false, false);
            }
        }

        String hint = loc.getCommon().get("tutorial_step5_hint");
        if (hint != null) {
            int hintY = tableY + STEP5_TABLE_ROWS * STEP5_TABLE_ROW_HEIGHT + 20;
            String[] hintLines = hint.split(";");
            for (String line : hintLines) {
                mp.drawString(line, dialogX + dialogW / 2f, hintY,
                        Constants.COLOR_BUTTONS_DARK, com.apogames.logic.asset.AssetLoader.font20,
                        DrawString.MIDDLE, false, false);
                hintY += 24;
            }
        }
    }

    private void enterQuestionStepIfNeeded() {
        if (getStep() == Step.PROPOSOL) {
            nextStep();
        }
    }

    private void hideAllVerifierVerifyButtons() {
        for (int i = 0; i < TUTORIAL_AMOUNT; i++) {
            ApoButton verify = getMainPanel().getButtonByFunction(Logic.FUNCTION_VERIFIER + "_" + (i + 1));
            if (verify != null) verify.setVisible(false);
        }
    }

    private void showVerifierVerifyButtonIfUnasked(int verifierIdx) {
        Boolean tested = getLevel().getRounds()[getLevel().getCurRound()].getVerifier()[verifierIdx];
        ApoButton verify = getMainPanel().getButtonByFunction(Logic.FUNCTION_VERIFIER + "_" + (verifierIdx + 1));
        if (verify != null && tested == null) {
            verify.setVisible(true);
        }
    }

    private void restoreAllVerifierButtonsAndVisibility() {
        for (int i = 0; i < TUTORIAL_AMOUNT; i++) {
            restoreVerifierButtonsToOriginal(i);
            ApoButton verify = getMainPanel().getButtonByFunction(Logic.FUNCTION_VERIFIER + "_" + (i + 1));
            Boolean tested = getLevel().getRounds()[getLevel().getCurRound()].getVerifier()[i];
            if (verify != null) verify.setVisible(tested == null && getStep() == Step.QUESTION);
        }
    }

    private void positionVerifierButtons(int verifierIdx, int targetX, int targetY) {
        Verifier v = getLevel().getVerifiers().get(verifierIdx);
        int cols = v.getColumn();
        int rows = v.getRows();
        int[] rowYOffsets = v.getRowYOffsets();
        int cellWidth = VERIFIER_LOGIC_WIDTH / cols;

        ApoButton verify = getMainPanel().getButtonByFunction(Logic.FUNCTION_VERIFIER + "_" + (verifierIdx + 1));
        if (verify != null) {
            verify.setX(targetX + VERIFY_BUTTON_DX);
            verify.setY(targetY + VERIFY_BUTTON_DY);
        }
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int curX = targetX + cellWidth * col;
                int curY = targetY + rowYOffsets[row];
                int cellIdx = row * cols + col;
                ApoButton ausx = getMainPanel().getButtonByFunction(Logic.FUNCTION_VERIFIER_START + verifierIdx + "_" + cellIdx);
                if (ausx != null) {
                    ausx.setX(curX);
                    ausx.setY(curY);
                }
            }
        }
    }

    private void restoreVerifierButtonsToOriginal(int verifierIdx) {
        int origX = verifierX(verifierIdx);
        int origY = verifierY(verifierIdx);
        positionVerifierButtons(verifierIdx, origX, origY);
    }

    private int verifierX(int index) {
        int col = index % 2;
        return VERIFIER_GRID_START_X + col * VERIFIER_COL_PITCH;
    }

    private int verifierY(int index) {
        int row = index / 2;
        return VERIFIER_GRID_START_Y + row * VERIFIER_ROW_PITCH;
    }

    private void positionNextButton() {
        ApoButton next = getMainPanel().getButtonByFunction(FUNCTION_TUTORIAL_NEXT);
        if (next == null) return;
        int btnX = dialog.getNextButtonCenterX() - (int) (next.getWidth() / 2);
        int btnY = dialog.getNextButtonY();
        next.setX(btnX);
        next.setY(btnY);
        next.setVisible(isNextButtonVisibleForCurrentStep());
    }

    @Override
    public void render() {
        super.render();
        if (this.tutorialStep == TutorialStep.DONE) return;

        int verifierInDialogIdx = verifierIndexInDialog();
        Boolean testResult = verifierInDialogIdx < 0 ? null
                : getLevel().getRounds()[getLevel().getCurRound()].getVerifier()[verifierInDialogIdx];

        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);
        dialog.renderBoxFill(getMainPanel());
        if (verifierInDialogIdx >= 0) {
            getLevel().getVerifiers().get(verifierInDialogIdx)
                    .renderFill(getMainPanel(), dialogVerifierX, dialogVerifierY, true);
            if (testResult != null) {
                IconDraw.renderSolutionCheck(getMainPanel(), testResult,
                        dialogVerifierX + 30, dialogVerifierY + 35, 40, 40);
            }
        }
        if (this.tutorialStep == TutorialStep.ROUND_FLOW) {
            renderStep5FilledContent(getMainPanel());
        }
        dialog.renderArrowsFill(getMainPanel());
        dialog.renderHighlightFill(getMainPanel());
        getMainPanel().getRenderer().end();

        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Line);
        dialog.renderBoxLine(getMainPanel());
        getMainPanel().getRenderer().end();

        String descriptionText = buildDescriptionText(verifierInDialogIdx, testResult);

        getMainPanel().spriteBatch.begin();
        if (this.tutorialStep == TutorialStep.ROUND_FLOW) {
            dialog.renderTitle(getMainPanel(), this.tutorialStep.titleKey());
            renderStep5SpriteContent(getMainPanel());
        } else {
            dialog.renderTitleAndDescription(getMainPanel(), this.tutorialStep.titleKey(), descriptionText);
        }
        if (verifierInDialogIdx >= 0) {
            getLevel().getVerifiers().get(verifierInDialogIdx)
                    .renderAllText(getMainPanel(), dialogVerifierX, dialogVerifierY);
        }
        dialog.renderLabels(getMainPanel());
        getMainPanel().spriteBatch.end();

        if (verifierInDialogIdx >= 0) {
            renderVerifierButtonsOnTop(verifierInDialogIdx);
            renderCodeBoxesInDialog();
        }

        ApoButton next = getMainPanel().getButtonByFunction(FUNCTION_TUTORIAL_NEXT);
        if (next != null && next.isVisible()) {
            next.render(getMainPanel());
        }
    }

    private String buildDescriptionText(int verifierInDialogIdx, Boolean testResult) {
        if (verifierInDialogIdx < 0) {
            return Localization.getInstance().getCommon().get(this.tutorialStep.textKey());
        }
        String prefix = "tutorial_step" + this.tutorialStep.getValue();
        Localization loc = Localization.getInstance();
        Solution guess = getLevel().getGuess();
        Verifier verifier = getLevel().getVerifiers().get(verifierInDialogIdx);
        int compareValue = verifier.getValue();
        int relevant = verifier.isFirst() ? guess.getFirst()
                : verifier.isSecond() ? guess.getSecond()
                : verifier.isThird() ? guess.getThird()
                : 0;
        String relation = relevant < compareValue ? "<" : (relevant == compareValue ? "=" : ">");
        String opposite = relevant < compareValue ? ">=" : (relevant == compareValue ? "!=" : "<=");

        String key = testResult == null ? prefix + "_before"
                : testResult ? prefix + "_after_check" : prefix + "_after_x";
        String text = loc.getCommon().get(key);
        if (text == null) text = "";
        text = text.replace("{first}", String.valueOf(guess.getFirst()));
        text = text.replace("{second}", String.valueOf(guess.getSecond()));
        text = text.replace("{third}", String.valueOf(guess.getThird()));
        text = text.replace("{value}", String.valueOf(relevant));
        text = text.replace("{compare}", String.valueOf(compareValue));
        text = text.replace("{relation}", relation);
        text = text.replace("{opposite}", opposite);

        if (testResult != null) {
            String extra = loc.getCommon().get(prefix + "_extra");
            if (extra != null && extra.length() > 0) {
                text = text + ";" + extra;
            }
        }
        return text;
    }

    private int verifierIndexInDialog() {
        if (this.tutorialStep == TutorialStep.SIMPLE_HINT) return 0;
        if (this.tutorialStep == TutorialStep.HARD_HINT) return 3;
        return -1;
    }

    private void renderVerifierButtonsOnTop(int verifierIdx) {
        ApoButton verify = getMainPanel().getButtonByFunction(Logic.FUNCTION_VERIFIER + "_" + (verifierIdx + 1));
        if (verify != null && verify.isVisible()) {
            verify.render(getMainPanel());
        }
        Verifier v = getLevel().getVerifiers().get(verifierIdx);
        int totalCells = v.getRows() * v.getColumn();
        for (int cell = 0; cell < totalCells; cell++) {
            ApoButton ausx = getMainPanel().getButtonByFunction(Logic.FUNCTION_VERIFIER_START + verifierIdx + "_" + cell);
            if (ausx != null && ausx.isVisible()) {
                ausx.render(getMainPanel());
            }
        }
    }

    @Override
    protected void quit() {
        this.tutorialStep = TutorialStep.DONE;
        ApoButton next = getMainPanel().getButtonByFunction(FUNCTION_TUTORIAL_NEXT);
        if (next != null) next.setVisible(false);
        super.quit();
    }
}
