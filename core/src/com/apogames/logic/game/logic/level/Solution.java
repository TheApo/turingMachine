package com.apogames.logic.game.logic.level;

public class Solution {

    private int first;
    private int second;
    private int third;

    public Solution() {
        this.first = (int)(Math.random() * 5) + 1;
        this.second = (int)(Math.random() * 5) + 1;
        this.third = (int)(Math.random() * 5) + 1;
    }

    public Solution(int first, int second, int third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public Solution getCopy() {
        return new Solution(this.first, this.second, this.third);
    }

    public boolean isSame(Solution checkSolution) {
        return this.first == checkSolution.getFirst() &&
                this.second == checkSolution.getSecond() &&
                this.third == checkSolution.getThird();
    }

    public int getSum() {
        return this.first + this.second + this.third;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public int getThird() {
        return third;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void setThird(int third) {
        this.third = third;
    }

    public String getSolutionString() {
        return this.first + " " + this.second+" "+ this.third;
    }
}
