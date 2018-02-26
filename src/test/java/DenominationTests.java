
import com.company.cashman.CashManConfig;
import com.company.cashman.lib.DenominationType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.company.cashman.lib.Denomination;
import com.company.cashman.lib.DefaultDenomination;

import java.util.HashSet;
import java.util.Set;


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
        Denomination denomination2 = new DefaultDenomination(DenominationType.FIVE_DOLLARS, 20);
        Set<Denomination> denominationSet = new HashSet<>();
        denominationSet.add(denomination1);
        denominationSet.add(denomination2);
        Assert.assertEquals(denominationSet.size(), 1);
    }
}
