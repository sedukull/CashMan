package com.company.cashman.algorithm;

import java.util.Set;

import com.company.cashman.lib.Denomination;

/**
 *CashMan algorithm for with drawing an amount from the given available currency set.
 */
@FunctionalInterface
public interface CashManAlgorithm {

    /**
     * With draws the user provided amount from CashMan available currency set.
     * Provides the list of denominations and counts matched with the provided input amount.
     * @param withDrawAmount
     * @return Set of denominations matched with input withdraw amount.
     */
    Set<Denomination> withDraw(final int withDrawAmount, final Set<Denomination> availableCurrencySet);
}
