package com.company.cashman;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;

import com.company.cashman.algorithm.CashManAlgorithm;
import com.company.cashman.algorithm.DynamicProgrammingOfCashManAlgorithm;
import com.company.cashman.algorithm.RecursiveImplementationOfCashManAlgorithm;
import com.company.cashman.dao.CashManRepository;
import com.company.cashman.lib.CashMan;
import com.company.cashman.dao.DynamoDbBasedCashManRepository;
import com.company.cashman.dao.DefaultCashManRepository;
import com.company.cashman.lib.DefaultCashMan;

@Configuration
@EnableAutoConfiguration
public class CashManConfig {
    /**
     *AWS access key, secret key, and region information used in AWS SDK for DynamoDB.
     *Note: Please mention the access key, secret key and region information below for your AWS account.
     */
    public static final String AWS_ACCESS_KEY_ID = "";
    public static final String AWS_SECRET_ACCESS_KEY = "";

    /**
     *Represents the AWS region(EX: Sydney).
     */
    public static final String AWS_REGION = "ap-southeast-2";

    public void initializeAWSDefaults() {
        System.setProperty("AWS_ACCESS_KEY_ID", AWS_ACCESS_KEY_ID);
        System.setProperty("AWS_SECRET_ACCESS_KEY", AWS_SECRET_ACCESS_KEY);
    }

    @Bean
    public Regions awsRegion() {
        return Regions.fromName(AWS_REGION);
    }

    @Bean
    public AmazonDynamoDB awsDynamoDbClient(final Regions awsRegion) {
        return AmazonDynamoDBClientBuilder.standard()
            .withRegion(awsRegion)
            .build();
    }

    @Bean
    @Qualifier("dynamoDBBasedCashManRepository")
    public CashManRepository dynamoDBBasedCashManRepository(final AmazonDynamoDB client) {
        return new DynamoDbBasedCashManRepository(client);
    }

    @Bean
    @Qualifier("defaultCashManRepository")
    public CashManRepository defaultCashManRepository() {
        return new DefaultCashManRepository();
    }

    @Bean
    @Qualifier("recursiveCashManAlgorithm")
    public CashManAlgorithm recurisveCashManAlgorithm() {
        return new RecursiveImplementationOfCashManAlgorithm();
    }

    @Bean
    @Qualifier("dynamicProgrammingCashManAlgorithm")
    public CashManAlgorithm dynamicProgrammingCashManAlgorithm() {
        return new DynamicProgrammingOfCashManAlgorithm();
    }

    @Bean
    @Qualifier("defaultCashMan")
    public CashMan defaultCashMan(@Qualifier("defaultCashManRepository") final CashManRepository cashManRepository,
                                  @Qualifier("dynamicProgrammingCashManAlgorithm") final CashManAlgorithm cashManAlgorithm) {
        return DefaultCashMan.getInstance(cashManRepository, cashManAlgorithm);
    }
}
