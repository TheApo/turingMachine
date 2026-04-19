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
        fillPossibleGuesses();
//        System.out.println("=== ApoSolver: Start mit " + this.possibleGuesses.size() + " möglichen Lösungen ===");
        for (int round = 0; round <= level.getRounds().length && round <= level.getCurRound(); round++) {
            Round curRound = level.getRounds()[round];
            for (int verifey = 0; verifey < level.getVerifiers().size(); verifey++) {
                Verifier verifier = level.getVerifiers().get(verifey);
                if (curRound.getVerifier()[verifey] != null) {
                    int beforeSize = this.possibleGuesses.size();
                    for (int j = this.possibleGuesses.size() - 1; j >= 0; j--) {
                        if (j >= this.possibleGuesses.size()) {
                            continue;
                        }
                        Solution solution = this.possibleGuesses.get(j);
                        int sizeBeforeCheck = this.possibleGuesses.size();

                        // Echte Antwort: Was hat der Verifier auf den Guess geantwortet?
                        boolean orgGuess = verifier.check(curRound.getGuess().getFirst(), curRound.getGuess().getSecond(), curRound.getGuess().getThird(), this.possibleGuesses, j);

                        // Prüfe ob verifier.check() die Liste bereits modifiziert hat
                        if (this.possibleGuesses.size() != sizeBeforeCheck) {
                            // Liste wurde bereits modifiziert, Index anpassen
                            continue;
                        }

                        // Simuliere: Wenn possibleSolution die echte Lösung wäre, was würde der Verifier auf den Guess antworten?
                        Verifier tempVerifier = verifier.getCopyWithSolution(solution);
                        boolean wouldAnswer = tempVerifier.check(curRound.getGuess().getFirst(), curRound.getGuess().getSecond(), curRound.getGuess().getThird());

                        // Wenn die simulierte Antwort anders ist als die echte Antwort → possibleSolution kann nicht die Lösung sein
                        if (wouldAnswer != orgGuess) {
                            if (j < this.possibleGuesses.size() && this.possibleGuesses.get(j).equals(solution)) {
                                this.possibleGuesses.remove(j);
                            }
                        }
                    }
//                    int afterSize = this.possibleGuesses.size();
//                    if (beforeSize != afterSize) {
//                        System.out.println("Runde " + (round + 1) + ", Verifier " + (char)('A' + verifey) + ": " + beforeSize + " -> " + afterSize + " Lösungen");
//                    }
                }
            }
        }
//        System.out.println("Verbleibende mögliche Lösungen (" + this.possibleGuesses.size() + "):");
//        for (Solution sol : this.possibleGuesses) {
//            System.out.println("  " + sol.getSolutionString());
//        }
//        System.out.println("Echte Lösung: " + level.getSolution().getSolutionString());
//        System.out.println("===");
        return this.possibleGuesses;
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
