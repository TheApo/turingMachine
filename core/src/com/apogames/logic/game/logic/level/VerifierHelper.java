package com.apogames.logic.game.logic.level;

import com.apogames.logic.game.logic.verifier.*;
import jdk.internal.org.jline.utils.DiffHelper;

import java.lang.reflect.Array;
import java.util.*;

public class VerifierHelper {

    ArrayList<Verifier> allVerifiers = new ArrayList<>();

    public VerifierHelper(Solution solution) {
        this.setAllVerifiers(solution);
    }

    public void setAllVerifiers(Solution solution) {
        this.allVerifiers.clear();

        for (int sum = 6; sum <= 12; sum++) {
            this.allVerifiers.add(new AllSumNumberVerifier(solution, sum));
        }

        for (int number = 1; number <= 5; number++) {
            this.allVerifiers.add(new AmountNumberVerifier(solution, number));
        }

        for (int number = 1; number <= 5; number++) {
            for (int secondNumber = number + 1; secondNumber <= 5; secondNumber++) {
                this.allVerifiers.add(new DoubleAmountNumberVerifier(solution, number, secondNumber, true));
                this.allVerifiers.add(new DoubleAmountNumberVerifier(solution, number, secondNumber, false));
            }

        }

        this.allVerifiers.add(new CompareToOtherVerifier(solution, true, true, false));
        this.allVerifiers.add(new CompareToOtherVerifier(solution, true, false, true));
        this.allVerifiers.add(new CompareToOtherVerifier(solution, false, true, true));

        this.allVerifiers.add(new SpecificCompareToOtherVerifier(solution, true, true, false));
        this.allVerifiers.add(new SpecificCompareToOtherVerifier(solution, true, false, true));
        this.allVerifiers.add(new SpecificCompareToOtherVerifier(solution, false, true, true));

        for (int number = 1; number <= 5; number++) {
            this.allVerifiers.add(new CompareToValueVerifier(solution, true, false, false, number));
            this.allVerifiers.add(new CompareToValueVerifier(solution, false, true, false, number));
            this.allVerifiers.add(new CompareToValueVerifier(solution, false, false, true, number));

            this.allVerifiers.add(new SpecificCompareToValueVerifier(solution, true, false, false, number));
            this.allVerifiers.add(new SpecificCompareToValueVerifier(solution, false, true, false, number));
            this.allVerifiers.add(new SpecificCompareToValueVerifier(solution, false, false, true, number));
        }

        this.allVerifiers.add(new DecreasingVerifier(solution));

        for (int sum = 4; sum <= 8; sum++) {
            this.allVerifiers.add(new DoubleSumNumberVerifier(solution, true, false, false, sum));
            this.allVerifiers.add(new DoubleSumNumberVerifier(solution, false, true, false, sum));
            this.allVerifiers.add(new DoubleSumNumberVerifier(solution, false, false, true, sum));

            this.allVerifiers.add(new SpecificDoubleSumEqualNumberVerifier(solution, true, false, false, sum));
            this.allVerifiers.add(new SpecificDoubleSumEqualNumberVerifier(solution, false, true, false, sum));
            this.allVerifiers.add(new SpecificDoubleSumEqualNumberVerifier(solution, false, false, true, sum));
        }

        this.allVerifiers.add(new InAndDecreasingVerifier(solution));

        this.allVerifiers.add(new IncreasingVerifier(solution));

        this.allVerifiers.add(new IsBiggestVerifier(solution));

        this.allVerifiers.add(new IsSmallestVerifier(solution));

        this.allVerifiers.add(new IsSmallestOrLargestVerifier(solution, true));
        this.allVerifiers.add(new IsSmallestOrLargestVerifier(solution, false));

        this.allVerifiers.add(new NumberRepeatsVerifier(solution));

        this.allVerifiers.add(new OddEvenGeneralVerifier(solution));

        this.allVerifiers.add(new OddEvenValueVerifier(solution, true, false, false));
        this.allVerifiers.add(new OddEvenValueVerifier(solution, false, true, false));
        this.allVerifiers.add(new OddEvenValueVerifier(solution, false, false, true));

        this.allVerifiers.add(new OrderVerifier(solution));

        this.allVerifiers.add(new PairVerifier(solution));

        this.allVerifiers.add(new PreciseEvenNumberVerifier(solution));

        this.allVerifiers.add(new PreciseOddNumberVerifier(solution));

        this.allVerifiers.add(new SumIsEvenOrOddVerifier(solution));

        for (int number = 2; number <= 4; number++) {
            this.allVerifiers.add(new SmallerValueVerifier(solution, number));
            this.allVerifiers.add(new BiggestValueVerifier(solution, number));

            this.allVerifiers.add(new SpecificBiggestValueVerifier(solution, true, false, false, number));
            this.allVerifiers.add(new SpecificBiggestValueVerifier(solution, false, true, false, number));
            this.allVerifiers.add(new SpecificBiggestValueVerifier(solution, false, false, true, number));

            this.allVerifiers.add(new SpecificSmallerValueVerifier(solution, true, false, false, number));
            this.allVerifiers.add(new SpecificSmallerValueVerifier(solution, false, true, false, number));
            this.allVerifiers.add(new SpecificSmallerValueVerifier(solution, false, false, true, number));

            this.allVerifiers.add(new SpecificSameValueVerifier(solution, true, false, false, number));
            this.allVerifiers.add(new SpecificSameValueVerifier(solution, false, true, false, number));
            this.allVerifiers.add(new SpecificSameValueVerifier(solution, false, false, true, number));
        }

        for (int number = 3; number <= 5; number++) {
            this.allVerifiers.add(new SumMultiplerValueVerifier(solution, number));
            this.allVerifiers.add(new IsSumMultiplerOneValueVerifier(solution, number));
        }

        this.allVerifiers.add(new SpecificOddEvenVerifier(solution, true, false, false));
        this.allVerifiers.add(new SpecificOddEvenVerifier(solution, false, true, false));
        this.allVerifiers.add(new SpecificOddEvenVerifier(solution, false, false, true));

        this.allVerifiers.add(new SpecificSmallerEqualOtherVerifier(solution, true, false, false));
        this.allVerifiers.add(new SpecificSmallerEqualOtherVerifier(solution, false, true, false));
        this.allVerifiers.add(new SpecificSmallerEqualOtherVerifier(solution, false, false, true));

        this.allVerifiers.add(new SpecificBiggerEqualOtherVerifier(solution, true, false, false));
        this.allVerifiers.add(new SpecificBiggerEqualOtherVerifier(solution, false, true, false));
        this.allVerifiers.add(new SpecificBiggerEqualOtherVerifier(solution, false, false, true));
    }

    public ArrayList<Verifier> getVerifiers(int amount, Difficulty difficulty) {
        ArrayList<Verifier> verifiers = new ArrayList<>();

        Difficulty curDifficulty = difficulty;
        do {
            ArrayList<Integer> possibleIds = getPossibleIDsForDifficulty(difficulty);
            verifiers.clear();
            int count = 2;
            if (difficulty == Difficulty.INSANE) {
                curDifficulty = Difficulty.EXPERT;
                count = amount - 1;
            }
            for (int i = 0; i < amount; i++) {
                int idIndex = (int) (Math.random() * possibleIds.size());
                while (difficulty != Difficulty.EASY && i < count && getVerifiersForID(possibleIds.get(idIndex)).get(0).getDifficulty() != curDifficulty) {
                    idIndex = (int) (Math.random() * possibleIds.size());
                }
                int id = possibleIds.remove(idIndex);

                ArrayList<Verifier> verifiersForID = getVerifiersForID(id);
                Verifier verifier = verifiersForID.get((int) (Math.random() * verifiersForID.size()));
                verifier.setVerifier(String.valueOf((char) (i + 65)));
                verifiers.add(verifier);
            }
        } while (!isSolutionValid(verifiers));

        Collections.shuffle(verifiers);
        int i = 0;
        for (Verifier verifier : verifiers) {
            verifier.setVerifier(String.valueOf((char) (i + 65)));
            i += 1;
        }
//        if (isSolutionValid(verifiers)) {
//            System.out.println(" " + isSolutionValid(verifiers));
//        }

        return verifiers;
    }

    private boolean isSolutionValid(ArrayList<Verifier> verifiers) {
        ArrayList<Solution> possibleGuesses = new ArrayList<>();

        for (int first = 1; first <= 5; first += 1) {
            for (int second = 1; second <= 5; second += 1) {
                for (int third = 1; third <= 5; third += 1) {
                    possibleGuesses.add(new Solution(first, second, third));
                }
            }
        }

        int foundAllGreen = 0;
        for (Solution solution : possibleGuesses) {
            boolean foundSolution = true;
            for (Verifier verifier : verifiers) {
                if (!verifier.check(solution.getFirst(), solution.getSecond(), solution.getThird())) {
                    foundSolution = false;
                    break;
                }
            }
            if (foundSolution) {
                foundAllGreen += 1;
                if (foundAllGreen > 1) {
                    return false;
                }
            }
        }

        return foundAllGreen == 1;
    }


    private ArrayList<Integer> getPossibleIDsForDifficulty(Difficulty difficulty) {
        Set<Integer> ids = new HashSet<>();
        for (Verifier verifier : this.allVerifiers) {
            if (verifier.getDifficulty().getValue() <= difficulty.getValue()) {
                ids.add(verifier.getId());
            }
        }
        return new ArrayList<>(ids);
    }

    private int getMaxID() {
        int maxID = 0;
        for (Verifier verifier : this.allVerifiers) {
            if (verifier.getId() > maxID) {
                maxID = verifier.getId();
            }
        }
        return maxID;
    }

    private ArrayList<Verifier> getVerifiersForID(int id) {
        ArrayList<Verifier> verifiers = new ArrayList<>();
        for (Verifier verifier : this.allVerifiers) {
            if (verifier.getId() == id) {
                verifiers.add(verifier);
            }
        }
        return verifiers;
    }

}
