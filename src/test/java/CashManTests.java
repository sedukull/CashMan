
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import com.company.cashman.CashManConfig;
import com.company.cashman.dao.CashManRepository;
import com.company.cashman.lib.CashManAlgorithm;
import com.company.cashman.lib.DefaultCashMan;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.company.cashman.lib.DenominationType;
import com.company.cashman.lib.CashMan;
import com.company.cashman.lib.Denomination;
import com.company.cashman.lib.DefaultDenomination;
import com.company.cashman.lib.CashNotAvailableException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = {CashManConfig.class})
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
        try {
            Set<Denomination> denominationList = this.cashManInstance.withDraw(20);
            Assert.assertEquals(denominationList.size(), 1);
        } catch (CashNotAvailableException ex) {
            System.out.println("Here");
        }
    }

    @Test
    public void testCashManWithDrawAmount40() throws CashNotAvailableException {
        Set<Denomination> denominationList = this.cashManInstance.withDraw(40);
        print(denominationList);
    }

    @Test
    public void testCashManWithDrawAmount50() throws CashNotAvailableException, IllegalArgumentException  {
        Set<Denomination> denominationList = this.cashManInstance.withDraw(50);
        print(denominationList);
    }

    @Test
    public void testCashManWithDrawAmount60() throws CashNotAvailableException, IllegalArgumentException{
        Set<Denomination> denominationList = this.cashManInstance.withDraw(60);
    }

    @Test
    public void testCashManWithDrawAmount80() throws CashNotAvailableException, IllegalArgumentException {
        Set<Denomination> denominationList = this.cashManInstance.withDraw(80);
        print(denominationList);
    }

    @Test
    public void testCashManWithDrawAmount100() throws IllegalArgumentException, CashNotAvailableException{
        Set<Denomination> denominationList = this.cashManInstance.withDraw(100);
        print(denominationList);
    }

    @Test
    public void testCashManWithDrawAmount110() throws IllegalArgumentException, CashNotAvailableException{
        Set<Denomination> denominationList = this.cashManInstance.withDraw(110);
        print(denominationList);
    }

    @Test
    public void testCashManWithDrawAmount150() throws IllegalArgumentException, CashNotAvailableException{
        Set<Denomination> denominationList = this.cashManInstance.withDraw(150);
        print(denominationList);
    }

    @Test
    public void testCashManWithDrawAmount200() throws IllegalArgumentException, CashNotAvailableException {
        Set<Denomination> denominationList = this.cashManInstance.withDraw(200);
        print(denominationList);
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
    public void testAddToAvailableCurrency() throws IllegalArgumentException, CashNotAvailableException {
        Set<Denomination> denominationList = this.cashManInstance.totalAvailableCurrency();
        System.out.println("CashMan. Available Total Currency Value: " + denominationList.stream()
            .map(x ->
                String.format("DenominationType: %s DenominationValue: %s",
                    x.getDenominationType().name(), x.getDenominationCount()))
            .collect(Collectors.joining()));
    }

    @Test
    public void testRemoveFromAvailableCurrency() throws IllegalArgumentException, CashNotAvailableException {
        Set<Denomination> toRemoveDenomination = new TreeSet<>();
        toRemoveDenomination.add(new DefaultDenomination(DenominationType.HUNDRED_DOLLARS, 1));
        this.cashManInstance.removeFromAvailableCurrency(toRemoveDenomination);
        Set<Denomination> denominationList = this.cashManInstance.totalAvailableCurrency();
    }

    @Test
    public void testGetDenominationCountForFiveDollars() {
        Assert.assertEquals(this.cashManInstance.getDenominationCount(DenominationType.FIFTY_DOLLARS), 5);
    }

    @Test
    public void testGetDenominationCountForTenDollars() {
        Assert.assertEquals(this.cashManInstance.getDenominationCount(DenominationType.TEN_DOLLARS), 5);
    }

    @Test
    public void testGetDenominationCountForTwentyDollars() {
        Assert.assertEquals(this.cashManInstance.getDenominationCount(DenominationType.TWENTY_DOLLARS), 5);
    }

    @Test
    public void testGetDenominationCountForFiftyDollars() {
        Assert.assertEquals(this.cashManInstance.getDenominationCount(DenominationType.FIFTY_DOLLARS), 5);
    }

    @Test
    public void testGetDenominationCountForHundredDollars() {
        Assert.assertEquals(this.cashManInstance.getDenominationCount(DenominationType.HUNDRED_DOLLARS), 10);
    }

    public void print(Set<Denomination> denominationSet) {
        System.out.println("CashMan. Available Total Currency Value: " + denominationSet.stream()
            .map(x -> x.toString())
            .collect(Collectors.joining()));
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
}
