package com.company.cashman.dao;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.stereotype.Component;

import com.company.cashman.lib.DefaultDenomination;
import com.company.cashman.lib.Denomination;
import com.company.cashman.lib.DenominationType;

/**
 *Default CashMan repository. Provides the persistence layer for CashMan repository.
 *Its used as an alternative to DynamoDB based CashMan repository. Repository used
 *in case the dynamodb/aws account is not available.
 *{@inheritDoc}
*/
@Component
public class DefaultCashManRepository implements CashManRepository {

    /**
     *Below are the default denominations used in initializing the CashMan application.
     *Note: Denominations in general will be persisted and so will be retrieved from DynamoDB.
     *The below denomination values are used by default if DynamoDb is not available.
     *EX: We will use the below as sample denominations for our CashMan application as such AWS account is not available.
     */
    private  Set<Denomination> totalDenominations = new TreeSet<>(Arrays.asList(
        new DefaultDenomination(DenominationType.FIVE_DOLLARS, 5),
        new DefaultDenomination(DenominationType.TEN_DOLLARS, 5),
        new DefaultDenomination(DenominationType.TWENTY_DOLLARS, 5),
        new DefaultDenomination(DenominationType.FIFTY_DOLLARS, 5),
        new DefaultDenomination(DenominationType.HUNDRED_DOLLARS, 10)));

   // private  Set<Denomination> totalDenominations = new TreeSet<>(Arrays.asList(
     //   new DefaultDenomination(DenominationType.FIVE_DOLLARS, 5);
    /**
     * Retrieves the set of denominations persisted in CashManDaoConfig.
     * @return Set of denominations.
     */
    @Override
    public Set<Denomination> retrieveDenomination() {
        synchronized (this) {
            return totalDenominations;
        }
    }

    /**
     * Updates the available total denomination with input denomination set.
     * Note: Default CashManRepository won't persist on an external store.
     * Instead it uses in-memory maintained set to store and retrieve these denominations.
     * @param denominationSet Denomination set to be persisted.
     */
    @Override
    public void persistDenomination(final Set<Denomination> denominationSet) {
        synchronized (this) {
            if (denominationSet.size() > 0) {
                denominationSet
                    .stream().forEach(e -> totalDenominations.stream()
                    .filter(d -> d.getDenominationType() == e.getDenominationType()).findAny().get().addCount(e.getAvailableDenominationCount()));
            }
        }
    }

    @Override
    public void initialize(final Set<Denomination> denominationSet) {
        synchronized (this) {
            this.totalDenominations = denominationSet;
        }
    }
}
