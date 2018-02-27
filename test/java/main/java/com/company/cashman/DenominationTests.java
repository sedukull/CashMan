package main.java.com.company.cashman;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import main.java.com.company.cashman.lib.Denomination;
import main.java.com.company.cashman.lib.DefaultDenomination;
import main.java.com.company.cashman.lib.DenominationType;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {CashManConfig.class})
public class DenominationTests {

    @Test
    public void testDefaultDenomination() {
        Denomination denomination = new DefaultDenomination(DenominationType.FIVE_DOLLARS, 10);
        Assert.assertEquals(denomination.totalValue(), 50);
        Assert.assertEquals(denomination.getDenominationCount(), 10);
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
    public void testRemoveDefaultDenomination() {
        Denomination denomination = new DefaultDenomination(DenominationType.FIVE_DOLLARS, 10);
        Assert.assertEquals(denomination.totalValue(), 50);
        denomination.removeCount(2);
        Assert.assertEquals(denomination.totalValue(), 40);
    }

    @Test
    public void testDenominationSet() {
        Denomination denomination1 = new DefaultDenomination(DenominationType.FIVE_DOLLARS, 10);
        denomination1.addCount(10);
        Assert.assertEquals(denomination1.getDenominationCount(), 20);
        Assert.assertEquals(denomination1.totalValue(), 20*5);
    }
}
