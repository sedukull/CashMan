package com.company.cashman;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.junit.runners.MethodSorters;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.company.cashman.lib.DefaultDenomination;
import com.company.cashman.lib.Denomination;
import com.company.cashman.lib.DenominationType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {CashManConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DenominationTest {

    @Test
    public void testDefaultDenomination() {
        Denomination denomination = new DefaultDenomination(DenominationType.FIVE_DOLLARS, 10);
        Assert.assertEquals(denomination.totalValue(), 50);
        Assert.assertEquals(denomination.getAvailableDenominationCount(), 10);
        Assert.assertEquals(denomination.getDenominationType().getValue(), 5);
    }

    @Test
    public void testAddDefaultDenomination() {
        Denomination denomination = new DefaultDenomination(DenominationType.FIVE_DOLLARS, 10);
        Assert.assertEquals(denomination.totalValue(), 50);
        denomination.addCount(10);
        Assert.assertEquals(denomination.totalValue(), 100);
    }

    @Test
    public void testDenominationSet() {
        Denomination denomination1 = new DefaultDenomination(DenominationType.FIVE_DOLLARS, 10);
        denomination1.addCount(10);
        Assert.assertEquals(denomination1.getAvailableDenominationCount(), 20);
        Assert.assertEquals(denomination1.totalValue(), 20*5);
    }
}
