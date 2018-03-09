package com.company.cashman.dao;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.TreeSet;
import java.util.Set;
import java.util.Map;

import com.company.cashman.lib.Denomination;
import com.company.cashman.lib.DefaultDenomination;
import com.company.cashman.lib.DenominationType;

/**
 * Repository to
 * 1. Retrieve the denomination set(available currency) from external store say DynamoDB.
 * 2. Persist the updated denomination by the application to an external store say DynamoDB.
 {@inheritDoc}
*/
@Component
public class DynamoDbBasedCashManRepository implements CashManRepository {

    private AmazonDynamoDB client;

    /**
     *CashMan table information. Stores the denomination type and count for CashMan application.
     *We will use DynamoDB table to store the CashMan denomination information.
     **/
    public static final String CASHMAN_TABLE = "CashManDenomination";

    @Autowired
    public DynamoDbBasedCashManRepository(final AmazonDynamoDB client) {
        this.client = client;
    }

    /**
     * Retrieves the set of denominations persisted in dynamodb.
     * @return Set of denominations.
     */
    @Override
    public Set<Denomination> retrieveDenomination() {
        synchronized (this) {
            ScanRequest scanRequest = new ScanRequest()
                .withTableName(CASHMAN_TABLE);
            ScanResult result = client.scan(scanRequest);
            TreeSet<Denomination> denominationTreeSet = new TreeSet<>();
            for (Map<String, AttributeValue> item : result.getItems()) {
                String denominationType = item.get("getDenominationType").getS();
                int denominationCount = Integer.parseInt(item.get("getAvailableDenominationCount").getS());
                denominationTreeSet.add(new DefaultDenomination(DenominationType.valueOf(denominationType), denominationCount));
            }
            return denominationTreeSet;
        }
    }

    /**
     * Persist the updated denomination of currency available to an external store say DynamoDB.
     * @param denominationSet Denomination set to be persisted.
     */
    @Override
    public void persistDenomination(final Set<Denomination> denominationSet) {
        synchronized (this) {
            if (denominationSet != null && !denominationSet.isEmpty()) {
                ArrayList<DynamoDBBasedCashManTable> cashManTableItemList = new ArrayList<>();
                denominationSet.stream().forEach(
                    x -> cashManTableItemList.add(
                        new DynamoDBBasedCashManTable(x.getDenominationType().getValue(), x.getAvailableDenominationCount())));
                synchronized (this) {
                    DynamoDBMapper mapper = new DynamoDBMapper(client);
                    try {
                        mapper.batchSave(cashManTableItemList);
                    } catch (Exception e) {
                    }
                }
            }
        }
    }

    @Override
    public void initialize(final Set<Denomination> denominationSet) {
        synchronized (this) {
            this.retrieveDenomination();
        }
    }
}
