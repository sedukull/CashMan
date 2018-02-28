package com.company.cashman.algorithm;

import java.util.Set;

import com.company.cashman.lib.Denomination;

/**
 *CashMan algorithm for with drawing amount from the given available currency set.
 */
@FunctionalInterface
public interface CashManAlgorithm {

    /**
     * With draws the given amount from CashMan application and provides the list of denominations matched with the provided input amount.
     * @param withDrawAmount
     * @return
     */
    Set<Denomination> withDraw(final int withDrawAmount, final Set<Denomination> availableCurrencySet);
}
