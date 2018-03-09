package com.company.cashman.lib;

import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.company.cashman.algorithm.CashManAlgorithm;
import com.company.cashman.dao.CashManRepository;

/**
 *DefaultCashMan implementation for CashMan.
 *Provides the default implementation for CashMan interfaces.
 *1. Initializing the cash man application.
 *2. With drawing the amount.
 *3. Adding to the available total currency.
 *4. Removing from the available total currency.
 *5. Retrieve the total denomination for a given denomination type.
 *{@inheritDoc}
 */
@Component
public class DefaultCashMan implements CashMan {

    final static Logger logger = LoggerFactory.getLogger(DefaultCashMan.class);

    private Set<Denomination> availableCurrencySet = null;

    private static CashManRepository cashManRepository = null;
    private static CashManAlgorithm cashManAlgorithm = null;
    private static CashMan cashManInstance = null;

    private DefaultCashMan() {
        initialize();
    }

    public static CashMan getInstance(final CashManRepository inputCashManRepository, final CashManAlgorithm inputCashManAlgorithm) {
        cashManRepository = inputCashManRepository;
        cashManAlgorithm = inputCashManAlgorithm;
        if (cashManInstance != null) {
            return cashManInstance;
        } else {
            return new DefaultCashMan();
        }
    }

    /**
     * Initializes the available currency with retrieved denomination from persistent store say EX: DynamoDB.
     */
    @Override
    public void initialize() {
        synchronized (this) {
            //Retrieve from DynamoDB and initialize availableCurrency.
            //Note: For time being for this program we will hard code few denominations through DefaultCashManRepository.
            this.availableCurrencySet = this.cashManRepository.retrieveDenomination();
            logger.info("CashMan. Available Total Currency Value: {}", availableCurrencySet.stream()
                .map(x -> x.toString())
                .collect(Collectors.joining()));
        }
    }

    /**
     * With draws the given amount from the available currency.
     * It is semi NP-hard problem.
     * Finding all combinations of denominations to match the given input amount recursively
     * may require polynomial time (2 power n), and so will be time consuming.
     * Instead the approach to the mentioned problem will be solved through dynamic programming.
     * It breaks the original problem in to sub problems and uses a matrix to keep track of the optimal solutions to sub problems.
     * Returns each of those sub problems just once, and storing their solutions.
     * Returns the set of denominations matched with the given input amount to be withdrawn.
     *
     * @param withDrawAmount
     * @return set of denominations matched with the given input amount.
     * @throws IllegalArgumentException in case of an invalid with draw amount.
     */
    @Override
    public Set<Denomination> withDraw(final int withDrawAmount) throws IllegalArgumentException, CashNotAvailableException {
        if (withDrawAmount <= 0) {
            throw new IllegalArgumentException("Invalid amount to be withdrawn, please check.");
        }
        if (availableCurrencySet.size() <= 0) {
            throw new CashNotAvailableException("Enough currency is not available to withdraw, sorry please try again!!");
        }
        synchronized (this) {
            Set<Denomination> output = cashManAlgorithm.withDraw(withDrawAmount, availableCurrencySet);
            if (!output.isEmpty()) {
                logger.info("Withdraw Amount: {}. Denomination list: {}", withDrawAmount, output.stream()
                    .map(x -> x.toString())
                    .collect(Collectors.joining()));
                // Remove the amount from the total currency.
                removeFromAvailableCurrency(output);
                return output;
            } else {
                throw new CashNotAvailableException(String.format("Enough denominations are not available to withdraw amount: %d",
                    withDrawAmount));
            }
        }
    }

    /**
     * {@inheritDoc}
     * @param denominationSet
     */
    @Override
    public void addToAvailableCurrency(final Set<Denomination> denominationSet) throws IllegalArgumentException {
        if (denominationSet == null || denominationSet.size() == 0) {
            throw new IllegalArgumentException("Invalid denomination to add, please check.");
        }
        synchronized (this) {
            logger.info("AddToAvailableCurrency: Denomination list: {}", denominationSet.stream()
                .map(x -> x.toString())
                .collect(Collectors.joining()));
            denominationSet
                .stream().filter(e -> e.getAvailableDenominationCount() > 0).forEach(e -> availableCurrencySet.stream()
                .filter(d -> d.getDenominationType().getValue() == e.getDenominationType().getValue())
                .findFirst().ifPresent(x->x.addCount(e.getAvailableDenominationCount())));
        }
    }

    /**
     * {@inheritDoc}
     * @param denominationSet
     */
    @Override
    public void removeFromAvailableCurrency(final Set<Denomination> denominationSet) throws IllegalArgumentException {
        if (denominationSet == null || denominationSet.size() == 0) {
            throw new IllegalArgumentException("Invalid denomination to be removed, please check.");
        }
        synchronized (this) {
            if (denominationSet != null) {
                logger.info("removeFromAvailableCurrency: Denomination list: {}", denominationSet.stream()
                    .map(x -> x.toString())
                    .collect(Collectors.joining()));
                denominationSet
                    .stream().filter(e->e.getAvailableDenominationCount() > 0).forEach(e -> availableCurrencySet.stream()
                    .filter(d -> (d.getDenominationType().getValue() == e.getDenominationType().getValue())
                        && (d.getAvailableDenominationCount()>e.getAvailableDenominationCount()))
                    .findFirst().ifPresent(x->x.removeCount(e.getAvailableDenominationCount())));
            }
        }
    }

    /**
     * {@inheritDoc}
     * @param denominationValue
     * @return the denomination count for a given denomination type.
     */
    @Override
    public int getDenominationCount(final int denominationValue) throws IllegalArgumentException {
        int availableCount = 0;
        synchronized(this) {
            if (denominationValue > 0) {
                availableCount = availableCurrencySet.stream()
                    .filter(x -> x.getDenominationType().getValue() == denominationValue)
                    .mapToInt(x-> x.getAvailableDenominationCount()).findFirst().orElse(0);
            }
            if (availableCount <= 0) {
                throw new IllegalArgumentException("Invalid denomination type, please check.");
            }
            return availableCount;
        }
    }

    /**
     * {@inheritDoc}
     * @return the total denomination available in the CashMan system at any given point of time.
     */
    @Override
    public Set<Denomination> totalAvailableCurrency() {
        return this.availableCurrencySet;
    }
}
