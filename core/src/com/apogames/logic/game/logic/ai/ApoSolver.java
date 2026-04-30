package com.apogames.logic.game.logic.ai;

import com.apogames.logic.game.logic.level.Difficulty;
import com.apogames.logic.game.logic.level.Level;
import com.apogames.logic.game.logic.level.Round;
import com.apogames.logic.game.logic.level.Solution;
import com.apogames.logic.game.logic.verifier.AllSumNumberVerifier;
import com.apogames.logic.game.logic.verifier.Verifier;
import com.apogames.logic.game.logic.verifier.VerifyIDEnum;

import java.util.ArrayList;
import java.util.Collections;

public class ApoSolver {

    private ArrayList<Solution> possibleGuesses = new ArrayList<>();

    public static void main(String[] args) {
        ApoSolver solver = new ApoSolver();
        for (int i = 0; i < 1000; i++) {
            solver.getNextLevel(6, Difficulty.MEDIUM);
        }
    }

    public ApoSolver() {
    }

    public ArrayList<Solution> getNewPossibleGuesses() {
        fillPossibleGuesses();
        return possibleGuesses;
    }

    public ArrayList<Solution> getPossibleGuessesForTipps(Level level) {
        return getPossibleGuessesForTipps(level, java.util.Collections.emptyMap());
    }

    public ArrayList<Solution> getPossibleGuessesForTipps(Level level, java.util.Map<Integer, java.util.Set<Integer>> excludedCells) {
        fillPossibleGuesses();

        int lastRound = Math.min(level.getCurRound(), level.getRounds().length - 1);

        for (int j = this.possibleGuesses.size() - 1; j >= 0; j--) {
            Solution solution = this.possibleGuesses.get(j);
            boolean solutionConsistent = true;
            for (int verifey = 0; verifey < level.getVerifiers().size() && solutionConsistent; verifey++) {
                Verifier verifier = level.getVerifiers().get(verifey);
                java.util.Set<Integer> excluded = excludedCells.getOrDefault(verifey, java.util.Collections.emptySet());

                // Sammle alle Antworten dieses Verifiers über alle Runden
                java.util.List<int[]> guesses = new java.util.ArrayList<>();
                java.util.List<Boolean> answers = new java.util.ArrayList<>();
                for (int r = 0; r <= lastRound; r++) {
                    Round round = level.getRounds()[r];
                    if (round == null || round.getGuess() == null) continue;
                    Boolean ans = round.getVerifier()[verifey];
                    if (ans == null) continue;
                    guesses.add(new int[]{ round.getGuess().getFirst(), round.getGuess().getSecond(), round.getGuess().getThird() });
                    answers.add(ans);
                }
                if (answers.isEmpty()) continue;

                // Echte Verifier-Konfig + Lösung: simuliere ob die Antworten konsistent sind.
                Verifier echteKonfig = verifier.getCopyWithSolution(solution);
                boolean answersOk = true;
                for (int i = 0; i < guesses.size(); i++) {
                    int[] g = guesses.get(i);
                    boolean simulated = echteKonfig.check(g[0], g[1], g[2]);
                    if (simulated != answers.get(i)) {
                        answersOk = false;
                        break;
                    }
                }
                if (!answersOk) {
                    solutionConsistent = false;
                    continue;
                }

                // Auto-X-Subset-Check: die Cells, die Auto-X für diese Lösung mit der echten
                // Konfig auskreuzt, müssen Subset der tatsächlich ausgekreuzten sein. Manuelle
                // zusätzliche Auskreuzungen des Spielers sind erlaubt — fehlende Auto-X-Cells
                // bedeuten dagegen, dass die Lösung nicht zu den Auskreuzungen passt.
                if (!excluded.isEmpty()) {
                    java.util.Set<Integer> simulated = simulateAutoX(echteKonfig, guesses, answers);
                    if (!excluded.containsAll(simulated)) {
                        solutionConsistent = false;
                    }
                }
            }
            if (!solutionConsistent) {
                this.possibleGuesses.remove(j);
            }
        }

        return this.possibleGuesses;
    }

    /**
     * Simuliert welche Cells der Auto-X-Helfer für die gegebene Hypothese-Konfig
     * ausgekreuzt hätte: bei JA-Antwort werden alle NICHT-Hit-Cells ausgekreuzt,
     * bei NEIN-Antwort die Hit-Cells.
     */
    private java.util.Set<Integer> simulateAutoX(Verifier config, java.util.List<int[]> guesses, java.util.List<Boolean> answers) {
        java.util.Set<Integer> simulated = new java.util.HashSet<>();
        int totalCells = config.getRows() * config.getColumn();
        for (int i = 0; i < guesses.size(); i++) {
            int[] g = guesses.get(i);
            int[] hits = config.getCellsForGuess(g[0], g[1], g[2]);
            java.util.Set<Integer> hitSet = new java.util.HashSet<>();
            for (int h : hits) hitSet.add(h);
            if (answers.get(i)) {
                for (int c = 0; c < totalCells; c++) {
                    if (!hitSet.contains(c)) simulated.add(c);
                }
            } else {
                simulated.addAll(hitSet);
            }
        }
        return simulated;
    }

    /**
     * Eine Konfig ist trivial wenn sie über alle 125 möglichen Guesses entweder
     * immer JA oder immer NEIN antwortet — sie liefert dann keine Information und
     * würde vom Level-Generator nicht gewählt werden.
     */
    private boolean isTrivialConfig(Verifier config) {
        boolean canYes = false;
        boolean canNo = false;
        for (int f = 1; f <= 5; f++) {
            for (int s = 1; s <= 5; s++) {
                for (int t = 1; t <= 5; t++) {
                    if (config.check(f, s, t)) canYes = true;
                    else canNo = true;
                    if (canYes && canNo) return false;
                }
            }
        }
        return true;
    }

    public Level getNextLevel(int amount, Difficulty difficulty) {
        final int MAX = 1000000;
        int count = 0;
        Level level = null;
        while (count < MAX) {
            count += 1;
            level = new Level(amount, difficulty);

            this.fillPossibleGuesses();
            if (this.canBeSolved(level.getVerifiers(), -1)) {
                boolean realBreak = true;
                for (int i = 0; i < level.getVerifiers().size(); i++) {
                    this.fillPossibleGuesses();
                    if (this.canBeSolved(level.getVerifiers(), i)) {
                        realBreak = false;
                        break;
                    }
                }
                if (realBreak) {
                    System.out.println("Lösung mit "+level.getSolution().getSolutionString());
                    break;
                }
            }
        }

        return level;
    }

    private void fillPossibleGuesses() {
        possibleGuesses.clear();
        for (int first = 1; first <= 5; first += 1) {
            for (int second = 1; second <= 5; second += 1) {
                for (int third = 1; third <= 5; third += 1) {
                    possibleGuesses.add(new Solution(first, second, third));
                }
            }
        }
        Collections.shuffle(possibleGuesses);
    }

    public boolean canBeSolved(ArrayList<Verifier> verifiers, int withoutVerifier) {
        boolean[] used = new boolean[100];
        ArrayList<Solution> alreadyTested = new ArrayList<>();
        while (this.possibleGuesses.size() > 1) {
            int alreadyTestedCounter = 0;
            int index = (int)(Math.random() * this.possibleGuesses.size());
            Solution curSolutionCheck = this.possibleGuesses.get(index);
            while (alreadyTested.contains(curSolutionCheck) && alreadyTestedCounter < this.possibleGuesses.size()) {
                index += 1;
                if (index >= this.possibleGuesses.size()) {
                    index = 0;
                }
                curSolutionCheck = this.possibleGuesses.get(index);
                alreadyTestedCounter += 1;
            }
            if (alreadyTestedCounter >= this.possibleGuesses.size()) {
                break;
            }
            alreadyTested.add(curSolutionCheck);

            for (int i = 0; i < verifiers.size(); i++) {
                if (i == withoutVerifier) {
                    continue;
                }
                Verifier verifier = verifiers.get(i);
                used[verifier.getId()] = true;
                generalCheck(curSolutionCheck, verifier);
            }
        }

        if (withoutVerifier >= 0 && this.possibleGuesses.size() != 1) {
            Verifier verifier = verifiers.get(withoutVerifier);

            int countCorrect = 0;
            for (int solu = this.possibleGuesses.size() - 1; solu >= 0; solu -= 1) {
                Solution solution = this.possibleGuesses.get(solu);
                if (!verifier.check(solution.getFirst(), solution.getSecond(), solution.getThird())) {
                    countCorrect += 1;
                }
            }

            if (countCorrect == 1) {
                this.possibleGuesses.clear();
            }
        }

        return this.possibleGuesses.size() == 1;
    }

    private void generalCheck(Solution curSolutionCheck, Verifier verifier) {
        for (int solu = this.possibleGuesses.size() - 1; solu >= 0; solu -= 1) {
            Solution solution = this.possibleGuesses.get(solu);
            if (solution.isSame(curSolutionCheck)) {
                continue;
            }
            if (!verifier.check(solution.getFirst(), solution.getSecond(), solution.getThird())) {
                this.possibleGuesses.remove(solution);
            }
        }
    }

}
