package com.apogames.logic.game.logic.level;

import com.apogames.logic.Constants;
import com.apogames.logic.game.logic.verifier.Verifier;

import java.util.ArrayList;

public class Level {

    private final int amount;

    private Solution solution;

    private Solution guess;

    private VerifierHelper helper;

    private ArrayList<Verifier> verifiers;

    private Round[] rounds;

    private boolean[] lastChecks;

    private int curRound = 0;

    public Level(final int amount, Difficulty difficulty) {
        this.amount = amount;

        this.rounds = new Round[12];
        for (int i = 0; i < this.rounds.length; i++) {
            this.rounds[i] = new Round(i+1);
        }

        this.setNewSolution(difficulty);
    }

    public boolean isTippAlreadyIn(String newTipp, int verifierIndex) {
        for (Round round : this.rounds) {
            if (round.getTipp() != null && newTipp.equals(round.getTipp()[verifierIndex])) {
                return true;
            }
        }
        return false;
    }

    public Round[] getRounds() {
        return rounds;
    }

    public int getCurRound() {
        return curRound;
    }

    public boolean nextRound() {
        this.curRound += 1;
        return this.curRound <= Constants.MAX_ROUNDS;
    }

    public void setNewSolution(Difficulty difficulty) {
        this.solution = new Solution();
        this.guess = new Solution(1, 2, 3);

        this.helper = new VerifierHelper(this.solution);

        this.verifiers = this.helper.getVerifiers(this.amount, difficulty);
        this.lastChecks = new boolean[this.verifiers.size()];
    }

    public void fillLastCheck() {
        for (int i = 0; i < this.verifiers.size(); i++) {
            Verifier verifier = this.verifiers.get(i);
            this.lastChecks[i] = verifier.check(this.guess.getFirst(), this.guess.getSecond(), this.guess.getThird());
        }
    }

    public boolean[] getLastChecks() {
        return lastChecks;
    }

    public int getAmount() {
        return amount;
    }

    public Solution getSolution() {
        return solution;
    }

    public Solution getGuess() {
        return guess;
    }

    public ArrayList<Verifier> getVerifiers() {
        return verifiers;
    }

    public int getVerifyChecks() {
        int verifyCount = 0;
        for (Round round : this.rounds) {
            if (round.getVerifier() != null) {
                for (int verify = 0; verify < round.getVerifier().length; verify++) {
                    if (round.getVerifier()[verify] != null) {
                        verifyCount += 1;
                    }
                }
            }
        }
        return verifyCount;
    }
}
