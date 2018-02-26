package com.company.cashman.lib;

/**
 *Represents the valid denomination types supported by CashMan application.
 *Note: We can add/remove to the below, with more relevant denominations.
 *The below are representative denominations for the CashMan problem described.
 */
public enum DenominationType {

    FIVE_DOLLARS(5),
    TEN_DOLLARS(10),
    TWENTY_DOLLARS(20),
    FIFTY_DOLLARS(50),
    HUNDRED_DOLLARS(100);

    private final int value;

    DenominationType(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static DenominationType getDenominationType(final int value) {
        DenominationType denominationType = null;
        for (DenominationType denomination : DenominationType.values()) {
            if (denomination.getValue() == value) {
                denominationType = denomination;
                break;
            }
        }
        return denominationType;
    }
}