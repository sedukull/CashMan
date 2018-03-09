package com.company.cashman.lib;

/**
 *Represents the denomination (denomination type and count) and related interfaces for CashMan application.
 */
public interface Denomination extends Comparable<Denomination> {
    /**
     * Adds the given denomination count to the existing denomination count for the given denomination type.
     * @param denominationCount
     */
    void addCount(final int denominationCount);

    /**
     * Removes the given denomination count to the existing denomination count for the given denomination type.
     * @param denominationCount
     */
    void removeCount(final int denominationCount);

    /**
     * provides the given DenominationType(EX: 5 dollars, 10 dollars etc) for this denomination.
     * @return DenominationType
     */
    DenominationType getDenominationType();

    /**
     * Returns the denomination count for the given DenominationType.
     * @return denomination count
     */
    int getAvailableDenominationCount();

    /**
     * Provides the total currency for the given denomination(DenominationType * denomination count)
     * @return total currency value.
     */
    long totalValue();
}
