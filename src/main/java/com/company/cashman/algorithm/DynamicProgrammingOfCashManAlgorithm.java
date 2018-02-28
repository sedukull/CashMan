package main.java.com.company.cashman.algorithm;

import main.java.com.company.cashman.lib.DefaultDenomination;
import main.java.com.company.cashman.lib.Denomination;
import main.java.com.company.cashman.lib.DenominationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
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
            int denominationCount = currentRequiredDenominationCount;
            int newDenomination = 1;
            for(int  c : denominationTypes) {
                if (c <= currentRequiredDenominationCount) {
                    if (minimumDenominationCounts[currentRequiredDenominationCount - c] + 1 < denominationCount) {
                        denominationCount = minimumDenominationCounts[currentRequiredDenominationCount - c] + 1;
                        newDenomination = c;
                    }
                }
            }
            minimumDenominationCounts[currentRequiredDenominationCount] = denominationCount;
            denominationsUsed[currentRequiredDenominationCount] = newDenomination;
        }
        return denominationsUsed;
    }

    private Set<Denomination> getFinalDenominations(final int withDrawAmount, final int [] denominationsUsed) {
        int totalAmount = withDrawAmount;
        Set<Denomination> output = new HashSet<>();
        while (totalAmount > 0) {
            int currentDenominationValue = denominationsUsed[totalAmount];
            int availableDenominationCount = availableCurrencySet.stream()
                .filter(x -> x.getDenominationType().getValue() == currentDenominationValue)
                .findFirst().map(x->x.getDenominationCount()).orElse(0);

            if (availableDenominationCount == 0) {
                logger.info("Not enough denominations available to withdraw");
                return new HashSet<>();
            }
            int denominationCount = output.stream()
                    .filter(x -> x.getDenominationType().getValue() == currentDenominationValue)
                    .findFirst().map(x->x.getDenominationCount()).orElse(0);
            DenominationType denominationType = DenominationType.getDenominationType(currentDenominationValue);
            if (denominationCount < availableDenominationCount) {
                denominationCount = denominationCount + 1;
                Denomination denomination = new DefaultDenomination(denominationType, denominationCount);
                output.remove(denomination);
                output.add(denomination);
                totalAmount = totalAmount - currentDenominationValue;
            } else {
                logger.info("Not enough denominations available to withdraw");
                return new HashSet<>();
            }
        }
        return output;
    }
}
