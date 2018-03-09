package com.company.cashman.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

/**
 *CashManDenomination dynamo table, used to store different denomination types and counts.
 */
@DynamoDBTable(tableName="CashManDenomination")
public class DynamoDBBasedCashManTable {

    private long denominationType;
    private long denominationCount;

    public DynamoDBBasedCashManTable(final long denominationType, final long denominationCount) {
        this.denominationType = denominationType;
        this.denominationCount = denominationCount;
    }

    @DynamoDBHashKey(attributeName = "getDenominationType")
    public long getDenominationType() {
        return this.denominationType;
    }

    public void setDenominationType(final long denominationType) {
        this.denominationType = denominationType;
    }

    @DynamoDBAttribute(attributeName = "getAvailableDenominationCount")
    public long getDenominationCount() {
        return this.denominationCount;
    }

    public void setDenominationCount(final long denominationCount) {
        this.denominationCount = denominationCount;
    }
}
