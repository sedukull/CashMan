package com.company.cashman.lib;

/**
 *Represents the default denomination in the CashMan application.
 *{@inheritDoc}
 */
public class DefaultDenomination implements Denomination {

    private DenominationType denominationType;
    private int availableDenominationCount;

    public DefaultDenomination(final DenominationType denominationType, final int denominationCount) {
        if (denominationCount > 0) {
            this.denominationType = denominationType;
            this.availableDenominationCount = denominationCount;
        }
    }

    @Override
    public void addCount(final int denominationCount) {
        if (denominationCount > 0) {
            this.availableDenominationCount += denominationCount;
        }
    }

    @Override
    public void removeCount(final int denominationCount) {
        if ((denominationCount > 0) && (denominationCount > availableDenominationCount)) {
            this.availableDenominationCount -= denominationCount;
        }
    }

    @Override
    public DenominationType getDenominationType() {
        return this.denominationType;
    }

    @Override
    public int getAvailableDenominationCount() {
        return this.availableDenominationCount;
    }

    public void setDenominationType(final DenominationType denominationType) {
        this.denominationType = denominationType;
    }

    public void setAvailableDenominationCount(final int availableDenominationCount) {
        this.availableDenominationCount = availableDenominationCount;
    }

    @Override
    public long totalValue() {
        return this.availableDenominationCount * this.denominationType.getValue();
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
            this.getDenominationType().name(), this.getAvailableDenominationCount(), (this.totalValue()));
    }
}
