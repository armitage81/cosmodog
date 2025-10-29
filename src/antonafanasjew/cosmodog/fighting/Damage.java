package antonafanasjew.cosmodog.fighting;

import antonafanasjew.cosmodog.domains.WeaponType;

public class Damage {

    private int amount;

    private boolean includingCriticalHit;

    private boolean includingBackstabbing;

    private boolean includingUpgradeBonus;

    private boolean includingOffGuard;

    private boolean includingSquashed;

    public Damage() {
        this.amount = 0;
        this.setIncludingBackstabbing(false);
        this.setIncludingCriticalHit(false);
        this.setIncludingUpgradeBonus(false);
        this.setIncludingOffGuard(false);
        this.setIncludingSquashed(false);
    }

    public boolean weaponUsed() {
        return !includingSquashed && !includingOffGuard;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isIncludingCriticalHit() {
        return includingCriticalHit;
    }

    public void setIncludingCriticalHit(boolean includingCriticalHit) {
        this.includingCriticalHit = includingCriticalHit;
    }

    public boolean isIncludingBackstabbing() {
        return includingBackstabbing;
    }

    public void setIncludingBackstabbing(boolean includingBackstabbing) {
        this.includingBackstabbing = includingBackstabbing;
    }

    public boolean isIncludingUpgradeBonus() {
        return includingUpgradeBonus;
    }

    public void setIncludingUpgradeBonus(boolean includingUpgradeBonus) {
        this.includingUpgradeBonus = includingUpgradeBonus;
    }

    public boolean isIncludingOffGuard() {
        return includingOffGuard;
    }

    public void setIncludingOffGuard(boolean includingOffGuard) {
        this.includingOffGuard = includingOffGuard;
    }

    public boolean isIncludingSquashed() {
        return includingSquashed;
    }

    public void setIncludingSquashed(boolean includingSquashed) {
        this.includingSquashed = includingSquashed;
    }
}
