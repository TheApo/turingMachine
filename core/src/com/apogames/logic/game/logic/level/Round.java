package com.apogames.logic.game.logic.level;

public class Round {

    private final int round;

    private Solution guess;

    private Boolean[] verifier = new Boolean[6];

    private Boolean[][] verifierCheck = new Boolean[6][4];

    private String[] tipp = new String[6];

    public Round(int round) {
        this.round = round;
    }

    public int getRound() {
        return round;
    }

    public Boolean[] getVerifier() {
        return verifier;
    }

    public Boolean[][] getVerifierCheck() {
        return verifierCheck;
    }

    public String[] getTipp() {
        return tipp;
    }

    public Solution getGuess() {
        return guess;
    }

    public void setGuess(Solution guess) {
        this.guess = guess.getCopy();
    }
}
