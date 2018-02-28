package com.company.cashman;

import com.company.cashman.dao.CashManRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import static org.mockito.Mockito.spy;

@Profile("cashmantest")
@Configuration
@EnableAutoConfiguration
public class CashManTestConfiguration {

    @Bean
    @Primary
    @Qualifier("testCashManRepository")
    public CashManRepository getTestCashManRepository(CashManRepository cashManRepository) {
        return spy(cashManRepository);
    }
}
