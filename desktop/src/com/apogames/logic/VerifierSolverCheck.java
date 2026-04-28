package com.apogames.logic;

import com.apogames.logic.game.logic.level.Solution;
import com.apogames.logic.game.logic.level.VerifierHelper;
import com.apogames.logic.game.logic.verifier.Verifier;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;

import java.util.ArrayList;
import java.util.List;

/**
 * Validates that no verifier's removePossibilities side-effect wrongly eliminates
 * the TRUE solution from the candidate list. Run via:
 *   gradlew desktop:verifierCheck
 */
public class VerifierSolverCheck {

    public static void main(String[] args) {
        Gdx.files = new Lwjgl3Files();

        Solution dummy = new Solution(1, 1, 1);
        VerifierHelper helper = new VerifierHelper(dummy);
        List<Solution> all = allSolutions();

        int totalFailures = 0;
        for (Verifier prototype : helper.getAllVerifiers()) {
            int failures = checkRemovesTrueSolution(prototype, all);
            String name = prototype.getClass().getSimpleName();
            String tag = describe(prototype);
            if (failures > 0) {
                System.out.println("FAIL  " + name + tag + ": " + failures + " inconsistencies");
                totalFailures += failures;
            } else {
                System.out.println("OK    " + name + tag);
            }
        }
        System.out.println("=== Total inconsistencies: " + totalFailures + " ===");
        if (totalFailures > 0) {
            System.exit(1);
        }
    }

    /**
     * For every (solution, guess) pair, the side-effect must NEVER remove the true
     * solution itself from the candidate list — the true solution is by definition
     * consistent with its own verifier output.
     */
    private static int checkRemovesTrueSolution(Verifier prototype, List<Solution> all) {
        int failures = 0;
        for (Solution s : all) {
            for (Solution g : all) {
                Verifier v = prototype.getCopyWithSolution(s);
                ArrayList<Solution> list = new ArrayList<>();
                list.add(s);
                v.check(g.getFirst(), g.getSecond(), g.getThird(), list, 0);
                if (list.isEmpty()) {
                    if (failures < 3) {
                        System.out.println("  removed TRUE solution " + s.getSolutionString()
                                + " for guess " + g.getSolutionString()
                                + " on " + prototype.getClass().getSimpleName());
                    }
                    failures++;
                }
            }
        }
        return failures;
    }

    private static String describe(Verifier v) {
        try {
            int value = v.getValue();
            return "(value=" + value + ", first=" + v.isFirst() + ",second=" + v.isSecond() + ",third=" + v.isThird() + ")";
        } catch (Exception e) {
            return "";
        }
    }

    private static List<Solution> allSolutions() {
        List<Solution> all = new ArrayList<>();
        for (int a = 1; a <= 5; a++) {
            for (int b = 1; b <= 5; b++) {
                for (int c = 1; c <= 5; c++) {
                    all.add(new Solution(a, b, c));
                }
            }
        }
        return all;
    }
}
