package com.company.cashman.lib;

/**
 *Represents the default denomination in the CashMan application.
 *{@inheritDoc}
 */
public class DefaultDenomination implements Denomination {

    private DenominationType denominationType;
    private int denominationCount;

    public DefaultDenomination(final DenominationType denominationType, final int denominationCount) {
        this.denominationType = denominationType;
        this.denominationCount = denominationCount;
    }

    @Override
    public void addCount(final int denominationCount) {
        if (denominationCount >= 1) {
            this.denominationCount += denominationCount;
        }
    }

    @Override
    public void removeCount(final int denominationCount) {
        if (denominationCount >= 1) {
            this.denominationCount -= denominationCount;
        }
    }

    @Override
    public DenominationType getDenominationType() {
        return this.denominationType;
    }

    @Override
    public int getDenominationCount() {
        return this.denominationCount;
    }

    public void setDenominationType(final DenominationType denominationType) {
        this.denominationType = denominationType;
    }

    public void setDenominationCount(final int denominationCount) {
        this.denominationCount = denominationCount;
    }

    @Override
    public long totalValue() {
        return this.denominationCount * this.denominationType.getValue();
    }

    @Override
    public int hashCode() {
        return this.denominationType.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Denomination that = (Denomination) o;
        return this.denominationType.getValue() == that.getDenominationType().getValue();
    }
    @Override
    public int compareTo(Denomination o) {
       return this.denominationType.compareTo(o.getDenominationType());
    }

    @Override
    public String toString() {
        return String.format("DenominationType: %s DenominationCount: %d TotalDenominationValue: %d",
            this.getDenominationType().name(), this.getDenominationCount(), (this.totalValue()));
    }
}
