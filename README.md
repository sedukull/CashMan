# CashMan Problem and Algorithm

## Problem Statement:
Given an amount W, also a positive integer, to find a set of non-negative (positive or zero) integers {x1, x2, ..., xn}, with each xj representing how often the coin with value wj is used, with minimal total number of coins, i.e., For (j: 1 to N) Sum(xj)  should be minimum, subject to Sum(wj * xj) = W.

## Glossary and Terminology:
1. Denomination Type: Represents the valid denomination types supported by CashMan application. (EX: 5$, 10$, 20$, 50$ etc).
2. Denomination Count: Represents the count of various denomination types available in the system.
3. Denomination: An entity representation of given DenominationType and its total Count Ex: Denomination (5$, 10) represents the count of 5$ available in the CashMan application and its count as 10.
4. Available Total Currency: Represents the total currency available in the CashMan application, i.e., Sum(denominationType(J) * availableDenominationCount(j)).

## Scope:

### InScope:
1. CashMan Withdraw algorithm: Core Algorithm for withdrawing the amount from the given CashMan available currency.
2. CashMan DynamoDB repository: DynamoDB based repository for the available currency in CashMan application.
3. CashMan Default repository: Repository of available currency persisted and retrieved as part of memory.
4. CashMan APIs: Spring boot-based API's for withdraw, addition, remove denominations from the available currency.

### OutOfScope:
1. Support of Transactions.
2. Integration tests.
3. Automated build.


## Requirements:
Following are broad use cases and requirements for CashMan application.
1. The CashMan application must know how many of each type of bank note it has. It should be able to report back how much of each note it has.
2. The CashMan application should be possible to tell it that it has so many of each type of note during initialisation. After initialisation, it is only possible to add or remove notes.
3. The CashMan application must support $20 and $50 Australian denominations.
4. The CashMan application should be able to dispense legal combinations of notes. For example, a request for $100 can be satisfied by either five $20 notes or 2 $50 notes. It is not required to present a list of options.
5. The CashMan application should report an error condition If a request cannot be satisfied due to failure to find a suitable combination of notes, in some fashion. For example, in an ATM with only $20 and $50 notes, it is not possible to dispense $30.
6. The CashMan application should reduce the amount of available cash in the machine post dispensation.
7. The CashMan application should not reduce the amount of available cash in case of an error.

## CashMan WithDraw Algorithm:
We will provide the following CashMan WithDraw algorithms for the mentioned CashMan problem.


### Greedy Algorithm:
A recursive mechanism to iterate over the available currency, deriving the required denomination at each stage for the provided input.
### Optimal Dynamic Programming variant: 
Below we provide the dynamic programming algorithm solve the given problem.

The mentioned problem is a semi NP-hard problem.
1. Let's say the amount to be withdrawn is x. Set WithDrawAmount = x.
2. Number of denominations required = min (1 + numberOfCoins(WithDrawAmount-di)), where i <= number of denominations.
3. A recursive solution to the mentioned problem finding combinations of outputs, will require semi polynomial time (i.e., 2 power n) and so time consuming. It may not be an optimal solution.
4. Instead dynamic programming approach, where we break the original problem in to sub problems and use a matrix to keep track of the optimal solutions to sub problems, and returns each of those sub problems just once, and storing their solutions.
5. We reduce the complexity by storing some of the past results for each denomination, so we can avoid recalculating the results we already know.
6. We maintain a denominationTrackingTable to keep track of the solutions to sub problems. Then before we compute a new minimum, we first check the table to see if a result is already known.
7. Set DenominationTypes = [1$, 2$, 5$, 10$, 20$, 100$]. /*Assign valid list of denominations. */
8. Set denominationTypesCount = len(DenominationTypes).
9. Set int[][] denominationTrackingTable = new int[denominationTypesCount + 1][withDrawAmount + 1]; //Denomination tracking table with optimal solutions stored for each subproblem.
10. For every denomination, we can either include the denomination or exclude it.
11. For I in [0, withDrawAmount], do the following:
12. Check, if number of denominations for I, is already found and available in denominationTrackingTable.
13. If yes, use the available result
14. If (denomination value <= WithDrawAmount):
         //Two choices are possible.
         Include the denomination: Verify the count of denominations included for this type. If it is in the available count, reduce the input WithDrawAmount by denomination value, and then use the sub problem solution using (WithDrawAmount - denomination).
         Exclude the denomination: solution for the same amount without considering that denomination.
    Else If denomination value is greater than the amount, then we can't consider that denomination, so solution will be without considering that denomination.

## Synchronization/Concurrency:
We will synchronize the available currency set in the CashMan application for the withdraw, add, remove operations. More details about these operations mentioned below. There will only be single instance of CashMan dealing with these operations per given process.

## CashMan Repository

CashMan repository stores the available currency of denominations for CashMan application. It by default provides two sets of repositories: DynamoDB based and Default.
1.    DynamoDB Repository: Stores and retrieves the denomination information from DynamoDB.
2.    Default CashMan Repository: Stores and retrieves the denomination information inside memory as part of application. Initialized through a bean.

Following are the repository API's.

    public interface CashManRepository {
    /**
     * Retrieves the initial denomination of currency available from external store say DynamoDB.
     * This currency will be used to initialize the CashMan application for further vending.
     * @return TreeSet with Denomination.
     */
    Set<Denomination> retrieveDenomination();

    /**
     * Persist the updated denomination of currency available to an external store say DynamoDB.
     * @param denominationSet  Denomination set to be persisted.
     */
    void persistDenomination(final Set<Denomination> denominationSet);
    }


## CashMan Application:

CashMan interface for CashMan application. Following are the API's, provided as extension point for the CashMan application.

    /**
     * Initializes the CashMan application with required currency from repository.
     * Provides to the user available denomination types and counts in CashMan application.
     */
    void initialize();

    /**
     * With draws the given amount from CashMan application and provides the list of Denominations matched with the provided        input amount.
     * @param withDrawAmount
     * @return
     */
    Set<Denomination> withDraw(final int withDrawAmount) throws CashNotAvailableException;

    /**
     * Adds the provided denomination set to the available currency.
     * @param denominationList
     */
    void addToAvailableCurrency(final Set<Denomination> denominationList);

    /**
     * Removes the denominations from the available currency.
     * @param denominationList
     */
    void removeFromAvailableCurrency(final Set<Denomination> denominationList);

    /**
     * Provides the available denomination count for a given denomination type.
     * (EX: count of 5$ notes in the available currency).
     * @return count of denomination for the given type.
     */
    int getDenominationCount(final int DenominationType);

    /**
     * provides the total currency (Set of DenominationType(5$,10$ etc) and their count in CashMan application.
     * @return set with available total currency in CashMan application.
     */
    Set<Denomination> totalAvailableCurrency();

## Pre-Requisites:
    1. Amazon AWS Java SDK: 1.11.106
    2. Junit: 4.13, mockito:
    3. ApacheMaven: 4.x
    4. Spring: 5.0.3
    5. SpringBoot: 1.5.10
    6. AWS Account for DynamoDB (In case you want to use DynamoDB as repository). The implementation as well adds a default repository with predefined denomination for usage.


## Running the CashMan Application through CLI

    mvn compile //Compiles the code.
    mvn test //Runs the test.
    mvn spring-boot:run //It will then launch an application on port 8080.

## Running the CashMan Application through IntelliJ
1. Import the CashMan project(pom.xml) as mvn project.
2. Right click on CashManApplication.java and click run.

 // It will then launch an application on port 8080. Example:
    1. http://localhost:8080/withDraw/10 --> With draws 10$ from the application.
    2. http://localhost:8080/ í Provides the total available currency in Cashman application.
    3. http://localhost:8080/getDenominationCount/100 --> provides the denomination count for the given denomination. EX:   100$


