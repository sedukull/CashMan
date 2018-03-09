package com.company.cashman.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Set;

import com.company.cashman.lib.Denomination;
import com.company.cashman.lib.CashMan;
import com.company.cashman.lib.CashNotAvailableException;

/**
 *CashMan REST request router.
 */
@RestController
public class CashManController {

    @Autowired
    @Qualifier("defaultCashMan")
    private CashMan cashMan;

    /**
     * Initializes and provides the initial value of total currency in terms of denomination type and their count.
     * @return Set of denomination(denomination type and count) with the total currency.
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Set<Denomination>> index() {
        Set<Denomination> denominationSet = cashMan.totalAvailableCurrency();
        return new ResponseEntity<>(denominationSet, HttpStatus.OK);
    }

    /**
     * Withdraws the given amount of money from the available total currency.
     * @param amount to with draw.
     * @return Set of denomination(denomination type and count).
     */
    @RequestMapping(value = "/withDraw/{amount:[\\d]+}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Set<Denomination>> withdraw(@PathVariable final int amount) throws CashNotAvailableException {
        Set<Denomination> withDrawDenominationSet = cashMan.withDraw(amount);
        return new ResponseEntity<>(withDrawDenominationSet, HttpStatus.OK);
    }

    /**
     * Adds to the available total currency in the CashMan application.
     * @param denominationList
     * @return OK if successful.
     */
    @RequestMapping("/addToCurrency")
    @ResponseBody
    public ResponseEntity<?> addToCurrency(@RequestBody final Set<Denomination> denominationList) {
        cashMan.addToAvailableCurrency(denominationList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Removes the amount from the available total currency in the CashMan application.
     * @param denominationList
     * @return OK if successful.
     */
    @RequestMapping("/removeFromCurrency")
    @ResponseBody
    public ResponseEntity<?> removeFromCurrency(@RequestBody final Set<Denomination> denominationList) {
        cashMan.removeFromAvailableCurrency(denominationList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Provides the available total currency in the CashMan application.
     * @return Set of denomination(denomination type and count) with the total currency. OK if successful.
     */
    @RequestMapping("/totalAvailableCurrency")
    @ResponseBody
    public ResponseEntity<Set<Denomination>> totalAvailableCurrency() {
        Set<Denomination> totalAvailableCurrencySet = cashMan.totalAvailableCurrency();
        return new ResponseEntity<>(totalAvailableCurrencySet, HttpStatus.OK);
    }

    /**
     * Provides the denomination count for the given denomination type.
     * Retrieves this count from available total currency in the CashMan application.
     * @return the denomination count for the given denomination type. OK if successful.
     */
    @RequestMapping("/getAvailableDenominationCount/{denominationType:[\\d]+}")
    @ResponseBody
    public ResponseEntity<Long> denominationCount(@PathVariable final int denominationType) {
        long count =  cashMan.getDenominationCount(denominationType);
        return new ResponseEntity<>(Long.valueOf(count), HttpStatus.OK);
    }
}
