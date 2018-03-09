package com.company.cashman.algorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.company.cashman.lib.DefaultDenomination;
import com.company.cashman.lib.Denomination;
import com.company.cashman.lib.DenominationType;

/**
 * {@inheritDoc}
 * Note: This approach is WIP. Please use DynamicProgrammingOfCashMan Algorithm instead.
 */
@Component
public class RecursiveImplementationOfCashManAlgorithm implements CashManAlgorithm {

    private Set<Denomination> availableCurrencySet;
    private final static Logger logger = LoggerFactory.getLogger(RecursiveImplementationOfCashManAlgorithm.class);

    /**
     * With draws the given amount from CashMan application.
     * Uses the available currency set to provide the set of denominations matched with the provided input amount.
     * A recursive approach to identify required denomination set for the given amount.
     * @param withDrawAmount
     * @return set of denominations for the provided input.
     */
    public Set<Denomination> withDraw(final int withDrawAmount, final Set<Denomination> denominationSet) {
        this.availableCurrencySet = denominationSet;
        return withDrawRecursive(denominationSet, withDrawAmount, new ArrayList<Integer>());
    }

    public Set<Denomination> withDrawRecursive(Set<Denomination> availableDenominations, int withDrawAmount,
                                  final List<Integer> partialDenominationList) {
        int currentSum = partialDenominationList.stream().mapToInt(Integer::valueOf).sum();
        Set <Denomination> outputDenominationSet = outputDenominationSet = partialDenominationList.stream()
            .collect(Collectors.groupingBy(Integer::valueOf, Collectors.counting()))
            .entrySet().stream().map(x -> new DefaultDenomination(DenominationType.getDenominationType(x.getKey()),
                x.getValue().intValue())).collect(Collectors.toSet());
        long matchedCount = outputDenominationSet
            .stream().map(e -> availableCurrencySet.stream()
            .filter(d -> d.getDenominationType() == e.getDenominationType() && e.getAvailableDenominationCount() <= d.getAvailableDenominationCount())).count();
        if ((currentSum == withDrawAmount) && matchedCount == outputDenominationSet.size()) {
            logger.info("Withdraw Amount: {}. Found required denominations: {}", withDrawAmount,
                outputDenominationSet.stream().map(x->x.toString()).collect(Collectors.joining()));
            return outputDenominationSet;
        }
        if (currentSum >= withDrawAmount)
            return new HashSet<>();
        for(Denomination d: availableDenominations) {
            List<Integer> collectedDenominationList = new ArrayList<>();
            collectedDenominationList.add(d.getDenominationType().getValue());
            Set<Denomination> remainingDenominations = availableDenominations.stream()
                .skip(1)
                .limit(availableDenominations.size())
                .collect(Collectors.toSet());
            withDrawRecursive(remainingDenominations, withDrawAmount, collectedDenominationList);
        }
        return outputDenominationSet;
    }

}
