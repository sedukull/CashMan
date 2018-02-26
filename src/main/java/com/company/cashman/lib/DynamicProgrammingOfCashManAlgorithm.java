package com.company.cashman.lib;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Arrays;
import java.util.Set;

@Component
public class DynamicProgrammingOfCashManAlgorithm implements CashManAlgorithm {

    private Set<Denomination> availableCurrencySet;
    private final static Logger logger = LoggerFactory.getLogger(RecursiveImplementationOfCashManAlgorithm.class);

    @Override
    public Set<Denomination> withDraw(final int withDrawAmount, final Set<Denomination> availableCurrencySet) {
        this.availableCurrencySet = availableCurrencySet;
        int [] denominationTypesList = this.availableCurrencySet.stream().mapToInt(x -> x.getDenominationType().getValue()).toArray();
        int [] usedDenominationList = getDenominationUsed(withDrawAmount, denominationTypesList);
        return getFinalDenominations(withDrawAmount, usedDenominationList);
    }

    private int[] getDenominationUsed(final int withDrawAmount, final int [] denominationTypes) {
        int [] denominationsUsed = new int [withDrawAmount+1];
        int [] minimumDenominationCounts = new int [withDrawAmount+1];
        Arrays.fill(denominationsUsed, 0);
        Arrays.fill(minimumDenominationCounts, 0);
        for (int currentRequiredDenominationCount = 0; currentRequiredDenominationCount <=  withDrawAmount; currentRequiredDenominationCount++) {
            int j = currentRequiredDenominationCount;
            int newDenomination = 1;
            for(int  c : denominationTypes ) {
                if (c <= currentRequiredDenominationCount) {
                    if (minimumDenominationCounts[currentRequiredDenominationCount - c] + 1 < j) {
                        j = minimumDenominationCounts[currentRequiredDenominationCount - c] + 1;
                        newDenomination = c;
                    }
                }
            }
            minimumDenominationCounts[currentRequiredDenominationCount] = j;
            denominationsUsed[currentRequiredDenominationCount] = newDenomination;
        }
        return denominationsUsed;
    }

    private Set<Denomination> getFinalDenominations(final int withDrawAmount, final int [] denominationsUsed) {
        int totalAmount = withDrawAmount;
        Set<Denomination> output = new HashSet<>();
        while (totalAmount > 0) {
            int currentDenominationValue = denominationsUsed[totalAmount];
            long availableDenominationCount = availableCurrencySet.stream()
                .filter(x -> x.getDenominationType().getValue() == currentDenominationValue).count();

            if (availableDenominationCount == 0) {
                logger.info("Not enough denominations available to withdraw");
                return output;
            }

            Optional<Denomination> matchedDenomination = output.stream()
                    .filter(x -> x.getDenominationType().getValue() == currentDenominationValue).findAny();

            DenominationType denominationType;
            int denominationCount = 1;
            if (matchedDenomination.isPresent() && matchedDenomination.get().getDenominationCount() < availableDenominationCount) {
                denominationType = matchedDenomination.get().getDenominationType();
                denominationCount = matchedDenomination.get().getDenominationCount() + 1;
            } else {
                denominationType = DenominationType.getDenominationType(currentDenominationValue);
            }
            Denomination denomination = new DefaultDenomination(denominationType, denominationCount);
            output.add(denomination);
            totalAmount = totalAmount - currentDenominationValue;
        }
        if (output.stream().mapToLong(x->x.totalValue()).sum() == withDrawAmount) {
            return output;
        } else {
            return new HashSet<Denomination>();
        }
    }

    /*

    public Set<Denomination> withDrawBackup(final int withDrawAmount, final Set<Denomination> availableCurrencySet) {

        int denominationTypesCount = availableCurrencySet.size();

        int [] denominationTypes = new int [denominationTypesCount];
        int [] denominationCounts = new int [denominationTypesCount];

        int[][] denominationTrackingTable = new int[denominationTypesCount + 1][withDrawAmount + 1];

        for (int i = 0; i <= denominationTypesCount; i++) {
            for (int j = 0; j <= withDrawAmount; j++){
                denominationTrackingTable[i][j] = 0;
            }
        }

        for (int i = 0; i <= withDrawAmount; i++) {
            denominationTrackingTable[0][i] = i;
        }

        int k = 0;
        for (Denomination denomination: availableCurrencySet) {
            denominationTypes[k] = denomination.getDenominationType().getValue();
            denominationCounts[k] = denomination.getDenominationCount();
            k++;
        }

        Map<Integer, Integer> output = new HashMap<>();
        for (int i = 1; i <= denominationTypesCount; i++) {
            for (int j = 1; j <= withDrawAmount; j++) {
                if (denominationTypes[i - 1] == j) {
                    denominationTrackingTable[i][j] = 1;
                } else if (denominationTypes[i - 1] > j) {
                    denominationTrackingTable[i][j] = denominationTrackingTable[i - 1][j];
                } else {
                    denominationTrackingTable[i][j] = Math.min(denominationTrackingTable[i - 1][j],
                        1 + denominationTrackingTable[i][j - denominationTypes[i - 1]]);
                }
            }
        }
        int i = denominationTypesCount;
        int j = withDrawAmount;
        while (j != 0) {
            if (denominationTrackingTable[i][Math.abs(j - denominationTypes[i - 1])] == denominationTrackingTable[i][j] - 1) {
                int denominationValue = denominationTypes[i - 1];
                output.put(denominationValue, output.getOrDefault(denominationValue, 0)+1);
                j = j - denominationTypes[i - 1];
            } else {
                i = i - 1;
            }
        }
        return output.entrySet().stream().flatMap(
            x -> Stream.of(new DefaultDenomination(DenominationType.getDenominationType(x.getKey()), x.getValue())))
            .collect(Collectors.toSet());
    }*/
}
