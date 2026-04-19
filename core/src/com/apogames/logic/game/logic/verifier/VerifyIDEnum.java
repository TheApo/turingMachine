package com.apogames.logic.game.logic.verifier;

public enum VerifyIDEnum {
    AllSumNumberVerifier(1),
    AmountNumberVerifier(2),
    CompareToOtherVerifier(3),
    CompareToValueVerifier(4),
    DecreasingVerifier(5),
    DoubleSumNumberVerifier(6),
    InAndDecreasingVerifier(7),
    IncreasingVerifier(8),
    IsBiggestVerifier(9),
    IsSmallestVerifier(10),
    NumberRepeatsVerifier(11),
    OddEvenValueVerifier(12),
    OrderVerifier(13),
    PairVerifier(14),
    PreciseEvenNumberVerifier(15),
    SumIsEvenOrOddVerifier(16),
    OddEvenGeneralVerifier(17),
    SmallerValueVerifier(18),
    BiggestValueVerifier(19),
    SpecificSmallerValueVerifier(20),
    SpecificBiggestValueVerifier(21),
    SpecificSameValueVerifier(22),
    SumMultiplerValueVerifier(23),
    IsSumMultiplerOneValueVerifier(24),
    PreciseOddNumberVerifier(25),
    SpecificOddEvenVerifier(26),
    SpecificSmallerEqualOtherVerifier(27),
    SpecificBiggerEqualOtherVerifier(28),
    SpecificDoubleSumEqualNumberVerifier(29),
    SpecificCompareToValueVerifier(30),
    IsSmallestOrLargestVerifier(31),
    SpecificCompareToOtherVerifier(32),
    DoubleAmountNumberVerifier(33);

    private final int value;

    VerifyIDEnum(final int value) {
        this.value = value;
    }

    public final int getValue() {
        return value;
    }
}
