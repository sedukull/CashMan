package com.company.cashman.dao;

import java.util.Set;

import com.company.cashman.lib.Denomination;

/**
 * CashMan Repository does the following:
 * 1. Retrieves the denomination set (available denominations and count of each denomination) from external store say DynamoDB.
 * 2. Persist the updated denomination by the application, to an external store say DynamoDB.
 */
public interface CashManRepository {
    /**
     * Retrieves the initial denomination of currency available from external store say DynamoDB.
     * This currency will be used to initialize the CashMan application for further vending.
     * @return TreeSet with Denomination.
     */
    Set<Denomination> retrieveDenomination();

    /**
     * Persist the updated denomination of currency available to an external store say DynamoDB.
     * @param denominationSet  Denomination set to be persisted.
     */
    void persistDenomination(final Set<Denomination> denominationSet);

    /**
     *Initialize the repository with given denomination set.
     * @param denominationSet
     */
    void initialize(final Set<Denomination> denominationSet);
}
