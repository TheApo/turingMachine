package com.apogames.logic;

import com.apogames.logic.game.logic.ai.ApoSolver;
import com.apogames.logic.game.logic.level.Level;
import com.apogames.logic.game.logic.level.Round;
import com.apogames.logic.game.logic.level.Solution;
import com.apogames.logic.game.logic.verifier.DoubleAmountNumberVerifier;
import com.apogames.logic.game.logic.verifier.PreciseOddNumberVerifier;
import com.apogames.logic.game.logic.verifier.SpecificCompareToOtherVerifier;
import com.apogames.logic.game.logic.verifier.SpecificCompareToValueVerifier;
import com.apogames.logic.game.logic.verifier.Verifier;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Files;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Reproduziert das User-Level mit 4 Verifeyern und 4 Runden.
 * Erwartet wird genau eine Lösung: (5, 4, 5).
 */
public class SolverDebugTest {

    public static void main(String[] args) {
        Gdx.files = new Lwjgl3Files();

        Solution sol = new Solution(5, 4, 5);

        // A = SpecificCompareToValue, value=3, isThird (User: "dritte Zahl > 3")
        Verifier verifierA = new SpecificCompareToValueVerifier(sol, false, false, true, 3);
        // B = SpecificCompareToOther: third (show=3) vs first (User: "third = first")
        // Da der Konstruktor show zufällig setzt, müssen wir den passenden show finden.
        Verifier verifierB = pickConfigForB(sol);
        // C = PreciseOddNumber (User: "wie viele ungerade")
        Verifier verifierC = new PreciseOddNumberVerifier(sol);
        // D = DoubleAmountNumber, values=1,2, chooseFirstValue=false (User: "keine 2en")
        Verifier verifierD = new DoubleAmountNumberVerifier(sol, 1, 2, false);

        ArrayList<Verifier> verifiers = new ArrayList<>(Arrays.asList(verifierA, verifierB, verifierC, verifierD));
        Level level = new Level(4, sol, verifiers);

        // 4 Runden
        int[][] guesses = {
                {1, 2, 3},
                {4, 3, 2},
                {3, 4, 4},
                {3, 4, 3},
        };
        Boolean[][] answers = {
                // A,    B,     C,     D
                {false, false, null,  false},
                {false, false, false, null},
                {true,  false, null,  true},
                {false, null,  true,  null},
        };

        for (int r = 0; r < guesses.length; r++) {
            Round round = level.getRounds()[r];
            round.setGuess(new Solution(guesses[r][0], guesses[r][1], guesses[r][2]));
            for (int v = 0; v < 4; v++) {
                round.getVerifier()[v] = answers[r][v];
            }
        }
        for (int i = 0; i < guesses.length; i++) level.nextRound();

        // Auto-X-Cells berechnen
        Map<Integer, Set<Integer>> excludedCells = new HashMap<>();
        for (int v = 0; v < 4; v++) {
            Verifier verifier = verifiers.get(v);
            Set<Integer> excluded = new HashSet<>();
            for (int r = 0; r < guesses.length; r++) {
                Boolean answer = answers[r][v];
                if (answer == null) continue;
                int[] hits = verifier.getCellsForGuess(guesses[r][0], guesses[r][1], guesses[r][2]);
                int totalCells = verifier.getRows() * verifier.getColumn();
                Set<Integer> hitSet = new HashSet<>();
                for (int h : hits) hitSet.add(h);
                if (answer) {
                    for (int c = 0; c < totalCells; c++) {
                        if (!hitSet.contains(c)) excluded.add(c);
                    }
                } else {
                    excluded.addAll(hitSet);
                }
            }
            excludedCells.put(v, excluded);
            System.out.println("Verifier " + (char)('A'+v) + " ausgekreuzte cells: " + excluded);
        }
        System.out.println();

        ApoSolver solver = new ApoSolver();
        ArrayList<Solution> possible = solver.getPossibleGuessesForTipps(level, excludedCells);

        System.out.println("=== Solver fand " + possible.size() + " mögliche Lösungen ===");
        for (Solution s : possible) {
            System.out.println("(" + s.getFirst() + ", " + s.getSecond() + ", " + s.getThird() + ")");
            for (int v = 0; v < 4; v++) {
                Verifier verifier = verifiers.get(v);
                Set<Integer> excluded = excludedCells.getOrDefault(v, new HashSet<>());
                for (Verifier config : verifier.getAllConfigurations(s)) {
                    int[] cc = config.getConfigCells();
                    boolean allEx = false;
                    if (cc.length > 0 && !excluded.isEmpty()) {
                        allEx = true;
                        for (int c : cc) if (!excluded.contains(c)) { allEx = false; break; }
                    }
                    if (allEx) continue;
                    if (isTrivial(config)) continue;
                    boolean ok = true;
                    for (int r = 0; r < guesses.length; r++) {
                        if (answers[r][v] == null) continue;
                        boolean sim = config.check(guesses[r][0], guesses[r][1], guesses[r][2]);
                        if (sim != answers[r][v]) { ok = false; break; }
                    }
                    if (ok) {
                        System.out.println("    " + (char)('A'+v) + ": cfg [first=" + config.isFirst()
                                + ", second=" + config.isSecond() + ", third=" + config.isThird()
                                + "] cells=" + Arrays.toString(cc));
                        break;
                    }
                }
            }
        }
        if (possible.size() == 1 && possible.get(0).isSame(sol)) {
            System.out.println("\nERWARTUNG ERFÜLLT.");
        } else {
            System.out.println("\nERWARTUNG NICHT ERFÜLLT — sollte genau (5,4,5) sein.");
        }
    }

    private static Verifier pickConfigForB(Solution sol) {
        // Wir wollen Konfig mit show=3 ("third" shown). Public Konstruktor wählt show random.
        // Iteriere bis show=3 erreicht, dann nutze getAllConfigurations für die "third vs first" Variante.
        for (int attempt = 0; attempt < 200; attempt++) {
            SpecificCompareToOtherVerifier candidate = new SpecificCompareToOtherVerifier(sol, true, false, true);
            if (!"third".equals(candidate.getName())) continue;
            // show=3 erreicht. Sucht (isFirst, isThird, show=3).
            for (Verifier cfg : candidate.getAllConfigurations(sol)) {
                SpecificCompareToOtherVerifier sc = (SpecificCompareToOtherVerifier) cfg;
                if (sc.isFirst() && !sc.isSecond() && sc.isThird()
                        && "third".equals(sc.getName())) {
                    return sc;
                }
            }
        }
        throw new RuntimeException("Konfig (third vs first, show=3) nicht gefunden");
    }

    private static boolean isTrivial(Verifier c) {
        boolean canYes = false, canNo = false;
        for (int f = 1; f <= 5; f++) for (int s = 1; s <= 5; s++) for (int t = 1; t <= 5; t++) {
            if (c.check(f, s, t)) canYes = true; else canNo = true;
            if (canYes && canNo) return false;
        }
        return true;
    }
}
