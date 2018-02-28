package main.java.com.company.cashman;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

import main.java.com.company.cashman.dao.CashManRepository;
import main.java.com.company.cashman.lib.CashManAlgorithm;
import main.java.com.company.cashman.lib.DefaultCashMan;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import main.java.com.company.cashman.lib.DenominationType;
import main.java.com.company.cashman.lib.CashMan;
import main.java.com.company.cashman.lib.Denomination;
import main.java.com.company.cashman.lib.DefaultDenomination;
import main.java.com.company.cashman.lib.CashNotAvailableException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {CashManConfig.class})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CashManTests {

    private CashMan cashManInstance;

    @Autowired
    @Qualifier("defaultCashManRepository")
    private CashManRepository cashManRepository;

    private Set<Denomination> originalDenominationSet;

    @Autowired
    @Qualifier("dynamicProgrammingCashManAlgorithm")
    private CashManAlgorithm cashManAlgorithm;

    @Before
    public void setup() {
        this.originalDenominationSet = cashManRepository.retrieveDenomination();
        cashManRepository.initialize(originalDenominationSet);
        this.cashManInstance = DefaultCashMan.getInstance(cashManRepository, cashManAlgorithm);
    }

    @Test
    public void testCashManWithDrawAmount20() throws CashNotAvailableException {
        Set<Denomination> denominationList = this.cashManInstance.withDraw(20);
        Assert.assertEquals(denominationList.size(), 1);
        Assert.assertEquals(denominationList.stream().filter(x -> x.getDenominationType().getValue() == 20).count(), 1);
    }

    @Test
    public void testCashManWithDrawAmount40() throws CashNotAvailableException {
        Set<Denomination> denominationList = this.cashManInstance.withDraw(40);
        Assert.assertEquals(denominationList.size(), 1);
        Assert.assertEquals(denominationList.stream().filter(x -> x.getDenominationType().getValue() == 20)
            .mapToInt(x->x.getDenominationCount()).findFirst().orElse(0), 2);
    }

    @Test
    public void testCashManWithDrawAmount50() throws CashNotAvailableException, IllegalArgumentException {
        Set<Denomination> denominationList = this.cashManInstance.withDraw(50);
        Assert.assertEquals(denominationList.size(), 1);
        Assert.assertEquals(denominationList.stream().filter(x -> x.getDenominationType().getValue() == 50)
            .mapToInt(x->x.getDenominationCount()).findFirst().orElse(0), 1);
    }

    @Test
    public void testCashManWithDrawAmount60() throws CashNotAvailableException, IllegalArgumentException {
        Set<Denomination> denominationList = this.cashManInstance.withDraw(60);
        Assert.assertEquals(denominationList.size(), 2);
        Assert.assertEquals(denominationList.stream().filter(x -> x.getDenominationType().getValue() == 50)
            .mapToInt(x->x.getDenominationCount()).findFirst().orElse(0), 1);
        Assert.assertEquals(denominationList.stream().filter(x -> x.getDenominationType().getValue() == 10)
            .mapToInt(x->x.getDenominationCount()).findFirst().orElse(0), 1);
    }

    @Test
    public void testCashManWithDrawAmount80() throws CashNotAvailableException, IllegalArgumentException {
        Set<Denomination> denominationList = this.cashManInstance.withDraw(80);
        Assert.assertEquals(denominationList.size(), 3);
        Assert.assertEquals(denominationList.stream().filter(x -> x.getDenominationType().getValue() == 50)
            .mapToInt(x->x.getDenominationCount()).findFirst().orElse(0), 1);
        Assert.assertEquals(denominationList.stream().filter(x -> x.getDenominationType().getValue() == 10)
            .mapToInt(x->x.getDenominationCount()).findFirst().orElse(0), 1);
        Assert.assertEquals(denominationList.stream().filter(x -> x.getDenominationType().getValue() == 20)
            .mapToInt(x->x.getDenominationCount()).findFirst().orElse(0), 1);
    }

    @Test
    public void testCashManWithDrawAmount100() throws IllegalArgumentException, CashNotAvailableException{
        Set<Denomination> denominationList = this.cashManInstance.withDraw(100);
        Assert.assertEquals(denominationList.size(), 1);
        Assert.assertEquals(denominationList.stream().filter(x -> x.getDenominationType().getValue() == 100)
            .mapToInt(x->x.getDenominationCount()).findFirst().orElse(0), 1);
    }

    @Test
    public void testCashManWithDrawAmount110() throws IllegalArgumentException, CashNotAvailableException{
        Set<Denomination> denominationList = this.cashManInstance.withDraw(110);
        Assert.assertEquals(denominationList.size(), 2);
        Assert.assertEquals(denominationList.stream()
            .filter(x -> x.getDenominationType()
                .getValue() == 100).mapToInt(x->x.getDenominationCount()).findFirst().orElse(0), 1);
        Assert.assertEquals(denominationList.stream().filter(x -> x.getDenominationType().getValue() == 10)
            .mapToInt(x->x.getDenominationCount()).findFirst().orElse(0), 1);
    }

    @Test
    public void testCashManWithDrawAmount150() throws IllegalArgumentException, CashNotAvailableException{
        Set<Denomination> denominationList = this.cashManInstance.withDraw(150);
        Assert.assertEquals(denominationList.size(), 2);
        Assert.assertEquals(denominationList.stream().filter(x -> x.getDenominationType().getValue() == 100)
            .mapToInt(x->x.getDenominationCount()).findFirst().orElse(0), 1);
        Assert.assertEquals(denominationList.stream().filter(x -> x.getDenominationType().getValue() == 50)
            .mapToInt(x->x.getDenominationCount()).findFirst().orElse(0), 1);
    }

    @Test
    public void testCashManWithDrawAmount200() throws IllegalArgumentException, CashNotAvailableException {
        Set<Denomination> denominationList = this.cashManInstance.withDraw(200);
        Assert.assertEquals(denominationList.size(), 1);
        Assert.assertEquals(denominationList.stream().filter(x -> x.getDenominationType().getValue() == 100)
            .mapToInt(x->x.getDenominationCount()).findFirst().orElse(0), 2);
    }

    @Test(expected = CashNotAvailableException.class)
    public void testCashManWithDrawAmountHighUnAvailableAmount() throws IllegalArgumentException, CashNotAvailableException {
        this.cashManInstance.withDraw(1000);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCashManWithDrawAmountIllegalAmount() throws IllegalArgumentException, CashNotAvailableException {
        this.cashManInstance.withDraw(0);
    }

    @Test
    public void testTotalAvailableCurrency() {
        Set<Denomination> actualDenominationList = this.cashManInstance.totalAvailableCurrency();
        Set<Denomination> expectedDenominationList = new TreeSet<>(Arrays.asList(
            new DefaultDenomination(DenominationType.FIVE_DOLLARS, 5),
            new DefaultDenomination(DenominationType.TEN_DOLLARS, 5),
            new DefaultDenomination(DenominationType.TWENTY_DOLLARS, 5),
            new DefaultDenomination(DenominationType.FIFTY_DOLLARS, 5),
            new DefaultDenomination(DenominationType.HUNDRED_DOLLARS, 10)));
        Assert.assertEquals(expectedDenominationList, actualDenominationList);

    }

    @Test
    public void testAddToAvailableCurrency() throws IllegalArgumentException, CashNotAvailableException {
        Denomination toAdd = new DefaultDenomination(DenominationType.FIFTY_DOLLARS, 5);
        this.cashManInstance.addToAvailableCurrency(new HashSet<>(Arrays.asList(toAdd)));
        int count = this.cashManInstance.totalAvailableCurrency()
            .stream().filter(x -> x.getDenominationType().getValue() == 50)
            .mapToInt(x->x.getDenominationCount()).findFirst().orElse(0);
        Assert.assertEquals(count, 10);
    }
}
