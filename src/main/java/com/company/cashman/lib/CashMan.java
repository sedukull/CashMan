package com.company.cashman.lib;

import java.util.Set;

/**
 *CashMan interface exposes the contract for the following:
 *1. initialize: Initializes the CashMan application, retrieving the available currency information from repository(Example: DynamoDB).
 *2. withDraw: With draws the given amount from CashMan available currency.
 *3. addToAvailableCurrency: Adds to the available total currency for the CashMan application.
 *4. removeFromAvailableCurrency: Removes from the available total currency.
 *5. getAvailableDenominationCount: Retrieve the total denomination count, for the given denomination type.
 *6. totalAvailableCurrency: Provides the total available currency in the CashMan application.
 */
public interface CashMan {

    /**
     * Initializes the CashMan application with required currency from repository.
     * Provides to the user available denomination types and counts in CashMan application.
     */
    void initialize();

    /**
     * With draws the given amount from CashMan application and provides the list of Denominations matched with the provided input amount.
     * @param withDrawAmount
     * @return
     */
    Set<Denomination> withDraw(final int withDrawAmount) throws CashNotAvailableException;

    /**
     * Adds the denomination to the available currency.
     * @param denominationList
     */
    void addToAvailableCurrency(final Set<Denomination> denominationList);

    /**
     * Removes the denominations from the available currency.
     * @param denominationList
     */
    void removeFromAvailableCurrency(final Set<Denomination> denominationList);

    /**
     * Provides the available denomination count for a given denomination type.
     * (EX: count of 5$ notes in the available currency).
     * @param  denominationValue
     * @return count of denomination for the given type.
     */
    int getDenominationCount(final int denominationValue);

    /**
     * provides the total currency (Set of DenominationType(5$,10$ etc) and their count in CashMan application.
     * @return  Set with available total currency in CashMan application.
     */
    Set<Denomination> totalAvailableCurrency();
}
